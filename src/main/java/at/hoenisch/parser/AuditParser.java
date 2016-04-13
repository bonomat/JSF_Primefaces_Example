package at.hoenisch.parser;

import at.hoenisch.models.AuditEntry;
import at.hoenisch.models.AuditGraph;
import at.hoenisch.parser.exceptions.ElementNotFoundException;
import at.hoenisch.parser.helpers.FormIdExtractor;
import at.hoenisch.parser.helpers.SubmitButtonFinder;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.w3c.dom.CharacterData;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileFilter;
import java.io.StringReader;
import java.util.*;

/**
 * Created by Philipp Hoenisch on 12/04/16.
 */
public class AuditParser {

    private static final List<String> USER_ACTION_TAG_NAMES = Arrays.asList("button", "link", "a");

    public static void main(String args[]) throws Exception {
        String tempDirectoryPath = FileUtils.getTempDirectoryPath();
        FileFilter fileFilter = new WildcardFileFilter("auditgraph*");
        File[] dirs = new File(tempDirectoryPath).listFiles(fileFilter);


        for (File file : dirs) {
//            file = new File("/var/folders/mr/jqcpf4md0_v3dtqq6_6_3sy8l26xks/T/auditgraph72461877385584842731460549916393189000");
            System.out.println(String.format("NEW FILE: %s \n", file.getAbsoluteFile()));
            String content = FileUtils.readFileToString(file);
            try {
                Gson gson = new Gson();
                AuditGraph auditGraph = gson.fromJson(content, AuditGraph.class);

                // get current user
                LinkedList<AuditEntry> auditEntries = auditGraph.getAuditEntries();
                AuditEntry first = auditEntries.getFirst();

                String currentUser = first.getCurrentUser();
                Document currentPage = loadHtmlFromString(first.getResponse());

                for (AuditEntry auditEntry : auditEntries) {
//                    System.out.println("-----------new-entry: \n" + auditEntry.getResponse() + "\n\n");
                    long timestamp = auditEntry.getTimestamp();
                    if (auditEntry.equals(first)) {
                        printAction(currentUser, " *loaded page* " + auditEntry.getContextPath(), timestamp);
                        continue;
                    }
                    if (isActiveClick(auditEntry, currentPage)) {
                        String actionSource = getActionSource(auditEntry, currentPage);
                        StringBuilder stringBuilder = new StringBuilder("");

                        stringBuilder.append("\n\t").append(String.format("clicked '%s' ", actionSource));

                        stringBuilder.append("\n\t").append(String.format("Form values: {%s", getFormValues(auditEntry,
                                "\t\t"))).append("\n\t}");

                        printAction(currentUser, stringBuilder.toString(), timestamp);
                    } else {
                        printAction(currentUser, String.format("*updating content automatically*%s",
                                auditEntry.getParameters().get("javax.faces.source")),
                                timestamp);
                    }
                    //todo do manual update
                    currentPage = updatePage(currentPage, auditEntry);


                }
            } catch (Exception ex) {
                System.err.println(String.format("Could not parse file: %s", file.getAbsolutePath()));
                ex.printStackTrace();
            } finally {
                System.out.println("\n");
            }
        }
    }

    private static Document updatePage(Document currentPage, AuditEntry auditEntry) throws Exception {
        String response = auditEntry.getResponse();
        org.w3c.dom.Document partialUpdate = loadXMLFromString(response);
        NodeList update = partialUpdate.getElementsByTagName("update");
        for (int i = 0; i < update.getLength(); i++) {
            Node item = update.item(i);
            NamedNodeMap attributes = item.getAttributes();
            Node id = attributes.getNamedItem("id");
            if (id.getNodeValue().startsWith("javax")) {
                continue;//can be ignored I guess....
            }
            NodeList childNodes = item.getChildNodes();
            for (int j = 0; j < childNodes.getLength(); j++) {
                Node cDataNode = childNodes.item(j);
                if (cDataNode instanceof CharacterData) {
                    String data = ((CharacterData) cDataNode).getData();
                    //TODO replace
                    Element elementById = currentPage.getElementById(id.getNodeValue());
                    if (elementById != null) {
                        elementById.empty().append(data);
                    } //else, nothing to do here
                }
            }
        }
        return currentPage;
    }

    private static String getFormValues(AuditEntry auditEntry, String preString) {
        Map<String, String> parameters = auditEntry.getParameters();
        StringBuilder stringBuilder = new StringBuilder("");
        for (String key : parameters.keySet()) {
            if (!key.startsWith("javax")) {
                stringBuilder.append("\n").append(preString).append("'")
                        .append(key).append("':'").append(parameters.get(key)).append("'");
            }
        }
        return stringBuilder.toString();
    }

    private static String getActionSource(AuditEntry auditEntry, Document currentPage) {
        Map<String, String> parameters = auditEntry.getParameters();
        String source = parameters.get("javax.faces.source");
        Element elementById = currentPage.getElementById(source);
        String elementText = "";
        if (elementById == null) {
            try {
                String formTagName = FormIdExtractor.extract(source);
                elementText = SubmitButtonFinder.find(currentPage, formTagName, source).text();
            } catch (ElementNotFoundException e) {
                elementText = "*BUTTON_HAS_NO_NAME*";
            }
        } else {
            elementText = elementById.text();
        }
        return String.format("(id:'%s', value:'%s')", source, elementText);
    }

    private static void printAction(String currentUser, String message, long timestamp) {
        System.out.println(String.format("'%s': current user: '%s': %s", new Date(timestamp), currentUser, message));
    }

    private static boolean isActiveClick(AuditEntry auditEntry, Document currentPage) {
        Map<String, String> parameters = auditEntry.getParameters();
        String source = parameters.get("javax.faces.source");
        Element elementById = currentPage.getElementById(source);

        //special case: emelement not found, let's check parent form for "submittable" elements
        String tagName = "";
        if (elementById == null) {
            String formTagName = FormIdExtractor.extract(source);
            try {
                tagName = SubmitButtonFinder.find(currentPage, formTagName, source).tagName();
            } catch (ElementNotFoundException e) {
                tagName = source; // could not find submit element, let's take the source
            }
        } else {
            tagName = elementById.tagName();
        }
        return USER_ACTION_TAG_NAMES.contains(tagName);

    }

    private static org.jsoup.nodes.Document loadHtmlFromString(String html) throws Exception {
        return Jsoup.parse(html);
    }


    private static org.w3c.dom.Document loadXMLFromString(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }


}
