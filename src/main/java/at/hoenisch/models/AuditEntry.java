package at.hoenisch.models;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Philipp Hoenisch on 11/04/16.
 */
public class AuditEntry implements Serializable {

    private AuditEntry parent;
    private AuditEntry child;

    private String request;
    private String requestMethod;
    private Map<String, String> parameters;
    private String response;
    private boolean ajaxUpdate;
    private Map<String, String> attributes;
    private Map<String, String> headers;
    private String query;
    private String contextPath;
    private String pathInfo;


    public AuditEntry getParent() {
        return parent;
    }

    public void setParent(AuditEntry parent) {
        this.parent = parent;
    }

    public AuditEntry getChild() {
        return child;
    }

    public void setChild(AuditEntry child) {
        this.child = child;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public boolean isAjaxUpdate() {
        return ajaxUpdate;
    }

    public void setAjaxUpdate(boolean ajaxUpdate) {
        this.ajaxUpdate = ajaxUpdate;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public void setPathInfo(String pathInfo) {
        this.pathInfo = pathInfo;
    }
}
