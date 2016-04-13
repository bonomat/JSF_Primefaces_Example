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
        Element form = currentPage.getElementById(formId);

        String scriptName = getScriptName(form, sourceId);
        Elements buttons = form.select(String.format("button[onclick^=%s]", scriptName));
        Element lastButton = null;

        for (Element button : buttons) {
            lastButton = button;
        }

        if (lastButton == null) {
            throw new ElementNotFoundException();
        }
        return lastButton;
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
