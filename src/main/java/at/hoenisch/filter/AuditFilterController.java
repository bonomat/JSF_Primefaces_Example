package at.hoenisch.filter;

import at.hoenisch.models.AuditEntry;
import at.hoenisch.models.AuditGraph;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by Philipp Hoenisch on 09/04/16.
 */
@Named
@SessionScoped
public class AuditFilterController implements Serializable {

    private AuditGraph auditGraph;

    public void addAuditGraph(AuditGraph auditGraph) {
        this.auditGraph = auditGraph;
    }

    public void addAuditEntry(AuditEntry auditEntry) {
        if (auditGraph == null) {
            auditGraph = new AuditGraph();
        }
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
