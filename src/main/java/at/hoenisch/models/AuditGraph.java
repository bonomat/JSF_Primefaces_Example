package at.hoenisch.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Philipp Hoenisch on 11/04/16.
 */
public class AuditGraph implements Serializable {

    private List<AuditEntry> auditEntries;

    public AuditGraph() {
        auditEntries = new ArrayList<>();
    }

    public void addEntry(AuditEntry auditEntry) {
        auditEntries.add(auditEntry);
    }

    public AuditEntry getLastEntry() {
        if (auditEntries.size() > 0) {
            return auditEntries.get(auditEntries.size() - 1);
        }
        return null;
    }
}
