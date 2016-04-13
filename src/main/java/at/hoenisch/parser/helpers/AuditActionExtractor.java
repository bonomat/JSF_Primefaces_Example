package at.hoenisch.parser.helpers;

import at.hoenisch.models.AuditEntry;
import at.hoenisch.parser.AuditParser;
import at.hoenisch.parser.exceptions.ElementNotFoundException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Map;

/**
 * Created by Philipp Hoenisch on 13/04/16.
 */
public class AuditActionExtractor {


    public static boolean isActiveClick(AuditEntry auditEntry, Document currentPage) {
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
        return AuditParser.USER_ACTION_TAG_NAMES.contains(tagName);

    }

    public static String getFormValues(AuditEntry auditEntry, String preString) {
        Map<String, String> parameters = auditEntry.getParameters();
        StringBuilder stringBuilder = new StringBuilder("");
        for (String key : parameters.keySet()) {
            if (!key.startsWith("javax")) {
                stringBuilder.append("\n")
                        .append(preString).append("'")
                        .append(key).append("':'").append(parameters.get(key)).append("'");
            }
        }
        return stringBuilder.toString();
    }

    public static String getActionSource(AuditEntry auditEntry, Document currentPage) {
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


}
