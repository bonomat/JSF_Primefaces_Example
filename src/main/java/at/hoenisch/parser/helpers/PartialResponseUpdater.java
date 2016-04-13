package at.hoenisch.parser.helpers;

import at.hoenisch.models.AuditEntry;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.w3c.dom.CharacterData;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

/**
 * Created by Philipp Hoenisch on 13/04/16.
 */
public class PartialResponseUpdater {

    /**
     * updates a given document with the given partial response from an ajax request
     *
     * @param currentPage:     the page which needs to be updated
     * @param partialResponse: the partial response which contains the update
     * @return the page, it's the same object as given as parameter
     * @throws Exception
     */
    public static Document updatePage(Document currentPage, String partialResponse) throws Exception {
        org.w3c.dom.Document partialUpdate;
        try {
            partialUpdate = loadXMLFromString(partialResponse);
        } catch (SAXParseException exception) {
            System.err.println("could not parse partial response");
            return currentPage;
        }
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
                    Element elementById = currentPage.getElementById(id.getNodeValue());
                    if (elementById != null) {
                        elementById.empty().append(data);
                    } //else, nothing to do here
                }
            }
        }
        return currentPage;
    }

    /**
     * parses a String to a W3C Document
     *
     * @param xml: the string to be parsed
     * @return the parsed document
     * @throws Exception if the string was not valid
     */
    private static org.w3c.dom.Document loadXMLFromString(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }
}
