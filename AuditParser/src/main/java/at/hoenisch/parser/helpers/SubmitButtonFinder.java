package at.hoenisch.parser.helpers;

import at.hoenisch.parser.exceptions.ElementNotFoundException;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

/**
 * Created by Philipp Hoenisch on 13/04/16.
 */
public class SubmitButtonFinder {


    public static Element find(Document currentPage, String formId, String sourceId) throws ElementNotFoundException {
        //find the form
        Element form;
        if (formId.isEmpty() || currentPage.getElementById(formId) == null) {
            form = currentPage;
        } else {
            form = currentPage.getElementById(formId);
        }


        //although sourceID should be globally unique, we limit the search on the form which was submitted
        Element button = form.getElementById(sourceId);
        if (button != null) {
            return button;
        }

        String scriptName = getScriptName(form, sourceId);
        if (scriptName.isEmpty()) {
            throw new ElementNotFoundException("Script not found");
        }

        Elements buttons = form.select(String.format("button[onclick^=%s]", scriptName));

        //if there are more buttons for one specific script, we take the last one
        if (buttons.size() > 1) {
            System.out.println(String.format("*found more than one button for script: %s", scriptName));
        } else if (buttons.isEmpty()) {
            throw new ElementNotFoundException();
        }
        return buttons.get(0);
    }

    private static String getScriptName(Element currentElement, String sourceId) {
        Elements scripts = currentElement.getElementsByTag("script");
        for (Element script : scripts) {
            List<DataNode> dataNodes = script.dataNodes();
            for (DataNode dataNode : dataNodes) {
                if (dataNode.getWholeData().contains(sourceId)) {
                    String[] split = dataNode.getWholeData().split("=");
                    if (split.length > 0) {
                        return split[0].trim();
                    }
                }
            }
        }
        return "";
    }
}
