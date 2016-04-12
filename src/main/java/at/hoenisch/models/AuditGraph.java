package at.hoenisch.models;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Created by Philipp Hoenisch on 11/04/16.
 */
public class AuditGraph implements Serializable {

    private LinkedList<AuditEntry> m_auditEntries;

    public AuditGraph() {
        m_auditEntries = new LinkedList<>();
    }

    public void addEntry(AuditEntry auditEntry) {
        m_auditEntries.add(auditEntry);
    }

    public AuditEntry getLastEntry() {
        try {
            return m_auditEntries.getLast();
        } catch (NoSuchElementException ex) {
            return null;
        }
    }
}
