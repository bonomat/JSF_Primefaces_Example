package at.hoenisch.parser.helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Philipp Hoenisch on 13/04/16.
 */
public class FormIdExtractor {

    public static String extract(String source) {
        Pattern patter = Pattern.compile("form", Pattern.CASE_INSENSITIVE);
        String[] split = source.split(":");
        String found = "";
        for (String s : split) { //take the most inner form
            Matcher matcher = patter.matcher(s);
            if (matcher.find()) {
                found = s;
            }
        }
        return found;
    }
}
