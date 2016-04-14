package at.hoenisch.parser;

import at.hoenisch.models.AuditEntry;
import at.hoenisch.models.AuditGraph;
import at.hoenisch.parser.helpers.AuditActionExtractor;
import at.hoenisch.parser.helpers.PartialResponseUpdater;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileFilter;
import java.util.*;

/**
 * Created by Philipp Hoenisch on 12/04/16.
 */
public class AuditParser {

    public static final List<String> USER_ACTION_TAG_NAMES = Arrays.asList("button", "link", "a");

    public static void main(String args[]) throws Exception {
        String tempDirectoryPath = FileUtils.getTempDirectoryPath();
        FileFilter fileFilter = new WildcardFileFilter("auditgraph*");
        File[] dirs = new File(tempDirectoryPath).listFiles(fileFilter);


        for (File file : dirs) {
            String content = FileUtils.readFileToString(file);
            try {
                Gson gson = new Gson();
                AuditGraph auditGraph = gson.fromJson(content, AuditGraph.class);

                LinkedList<AuditEntry> auditEntries = auditGraph.getAuditEntries();

                //get first entry, usually the page load
                AuditEntry firstAuditEntry = auditEntries.getFirst();

                // get current user
                String currentUser = firstAuditEntry.getCurrentUser();
                if (firstAuditEntry.isAjaxUpdate()) {
                    System.err.println("First page was a ajax request, log file is invalid or corrupt");
                    continue;
                }

                Document currentPage = Jsoup.parse(firstAuditEntry.getResponse());
                for (AuditEntry auditEntry : auditEntries) {

                    //timestamp of this entry, i.e., when did this happen
                    long timestamp = auditEntry.getTimestamp();
                    if (auditEntry.equals(firstAuditEntry)) {
                        String message = String.format("*loaded page* %s", auditEntry.getContextPath());
                        printAction(currentUser, message, timestamp);
                        continue;
                    }

                    String message = extract(auditEntry, currentPage);
                    printAction(currentUser, message, timestamp);

                    currentPage = PartialResponseUpdater.updatePage(currentPage, auditEntry.getResponse());


                }
            } catch (Exception ex) {
                System.err.println(String.format("Could not parse file: %s", file.getAbsolutePath()));
                ex.printStackTrace();
            } finally {
                System.out.println("\n");
            }
        }
    }

    private static String extract(AuditEntry auditEntry, Document currentPage) {
        String message;
        if (AuditActionExtractor.isActiveClick(auditEntry, currentPage)) {
            String actionSource = AuditActionExtractor.getActionSource(auditEntry, currentPage);
            StringBuilder stringBuilder = new StringBuilder("");

            stringBuilder.append("\n\t").append(String.format("clicked '%s' ", actionSource));

            String formValues = AuditActionExtractor.getFormValues(auditEntry, "\t\t");
            stringBuilder.append("\n\t").append(String.format("Form values: {%s", formValues)).append("\n\t}");

            message = stringBuilder.toString();
        } else {
            message = String.format("*updating content automatically*%s",
                    auditEntry.getParameters().get("javax.faces.source"));
        }
        return message;
    }


    /**
     * @param currentUser: the user in charge
     * @param message:     the message to be printed
     * @param timestamp:   the log entry's timestamp
     */
    private static void printAction(String currentUser, String message, long timestamp) {
        System.out.println(String.format("'%s': current user: '%s': %s", new Date(timestamp), currentUser, message));
    }
}
