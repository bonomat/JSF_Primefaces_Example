package at.hoenisch.models;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Philipp Hoenisch on 11/04/16.
 */
public class AuditEntry implements Serializable {

    private AuditEntry m_parent;
    private AuditEntry m_child;

    private String m_request;
    private String m_requestMethod;
    private Map<String, String> m_parameters;
    private String m_response;
    private boolean m_ajaxUpdate;
    private Map<String, String> m_attributes;
    private Map<String, String> m_headers;
    private String m_query;
    private String m_contextPath;
    private String m_pathInfo;
    private long m_timestamp;


    public AuditEntry getParent() {
        return m_parent;
    }

    public void setParent(AuditEntry parent) {
        this.m_parent = parent;
    }

    public AuditEntry getChild() {
        return m_child;
    }

    public void setChild(AuditEntry child) {
        this.m_child = child;
    }

    public String getRequest() {
        return m_request;
    }

    public void setRequest(String request) {
        this.m_request = request;
    }

    public String getRequestMethod() {
        return m_requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.m_requestMethod = requestMethod;
    }

    public Map<String, String> getParameters() {
        return m_parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.m_parameters = parameters;
    }

    public String getResponse() {
        return m_response;
    }

    public void setResponse(String response) {
        this.m_response = response;
    }

    public boolean isAjaxUpdate() {
        return m_ajaxUpdate;
    }

    public void setAjaxUpdate(boolean ajaxUpdate) {
        this.m_ajaxUpdate = ajaxUpdate;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.m_attributes = attributes;
    }

    public void setHeaders(Map<String, String> headers) {
        this.m_headers = headers;
    }

    public void setQuery(String query) {
        this.m_query = query;
    }

    public void setContextPath(String contextPath) {
        this.m_contextPath = contextPath;
    }

    public void setPathInfo(String pathInfo) {
        this.m_pathInfo = pathInfo;
    }

    public Map<String, String> getAttributes() {
        return m_attributes;
    }

    public Map<String, String> getHeaders() {
        return m_headers;
    }

    public String getQuery() {
        return m_query;
    }

    public String getContextPath() {
        return m_contextPath;
    }

    public String getPathInfo() {
        return m_pathInfo;
    }

    public void setTimestamp(long timestamp) {
        this.m_timestamp = timestamp;
    }

    public long getTimestamp() {
        return m_timestamp;
    }


    @Override
    public String toString() {
        return "AuditEntry{" +
                "', m_timestamp=" + m_timestamp +
                ", m_requestMethod='" + m_requestMethod + '\'' +
                ", m_contextPath='" + m_contextPath + '\'' +
                ", m_attributes=" + m_attributes +
                ", m_ajaxUpdate=" + m_ajaxUpdate +
                ", m_parameters=" + m_parameters +
                ", m_request='" + m_request + '\'' +
                ", m_parent=" + (m_parent != null ? m_parent.getTimestamp() : "none") +
                '}';
    }
}


