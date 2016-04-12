package at.hoenisch.filter;

import at.hoenisch.models.AuditEntry;
import at.hoenisch.models.AuditGraph;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Philipp Hoenisch on 09/04/16.
 */
@Named
@SessionScoped
public class AuditFilterController implements Serializable {

    private List<AuditGraph> auditGraphs;

    public AuditFilterController() {
        auditGraphs = new ArrayList<>();
    }

    public void addAuditEntry(AuditEntry auditEntry) {
        AuditGraph auditGraph;
        if (auditGraphs.isEmpty()) {
            auditGraph = new AuditGraph();
            auditGraphs.add(auditGraph);
        }
        auditGraph = auditGraphs.get(auditGraphs.size() - 1);

        if (auditEntry.isAjaxUpdate()) {
            AuditEntry lastEntry = auditGraph.getLastEntry();
            if (lastEntry != null) {
                lastEntry.setChild(auditEntry);
                auditEntry.setParent(lastEntry);
            }
        }
        auditGraph.addEntry(auditEntry);
    }
}
