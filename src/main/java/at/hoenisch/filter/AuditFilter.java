package at.hoenisch.filter;

import at.hoenisch.filter.helpers.HttpServletResponseCopier;
import at.hoenisch.models.AuditEntry;
import org.omnifaces.filter.HttpFilter;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Philipp Hoenisch on 09/04/16.
 */
public class AuditFilter extends HttpFilter {


    @Inject
    private AuditFilterController m_auditFilterController;


    @Override
    public void doFilter(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, HttpSession httpSession, FilterChain filterChain) throws ServletException, IOException {

        AuditEntry auditEntry = new AuditEntry();

        //1. check if it is ajax request
        boolean isAjax = isAJAXRequest(httpServletRequest);
        auditEntry.setAjaxUpdate(isAjax);

        //2. check request, POST, PUT, GET or DELETE parameters
        String method = httpServletRequest.getMethod();
        auditEntry.setRequestMethod(method);

        Enumeration attributeNames = httpServletRequest.getAttributeNames();
        System.out.println("-----request attributes");
        Map<String, String> attributes = new HashMap<>();
        while (attributeNames.hasMoreElements()) {
            Object o = attributeNames.nextElement();
            attributes.put(o.toString(), httpServletRequest.getAttribute(o.toString()).toString());
        }
        auditEntry.setAttributes(attributes);

        Enumeration parameterNames = httpServletRequest.getParameterNames();
        System.out.println("-----request parameters + values");
        Map<String, String> parameters = new HashMap<>();
        while (parameterNames.hasMoreElements()) {
            Object o = parameterNames.nextElement();
            parameters.put(o.toString(), httpServletRequest.getParameter(o.toString()));
        }
        auditEntry.setParameters(parameters);

        Enumeration headerNames = httpServletRequest.getHeaderNames();
        System.out.println("-----request header names + value");
        Map<String, String> headers = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            Object o = headerNames.nextElement();
            headers.put(o.toString(), httpServletRequest.getHeader(o.toString()));
        }
        auditEntry.setHeaders(headers);

        String queryString = httpServletRequest.getQueryString();
        System.out.println("-----request query string");
        auditEntry.setQuery(queryString);

        String contextPath = httpServletRequest.getContextPath();
        System.out.println("-----request context path");
        auditEntry.setContextPath(contextPath);

        String pathInfo = httpServletRequest.getPathInfo();
        System.out.println("-----request path info");
        auditEntry.setPathInfo(pathInfo);

        if (httpServletResponse.getCharacterEncoding() == null) {
            httpServletResponse.setCharacterEncoding("UTF-8"); // Or whatever default. UTF-8 is good for World Domination.
        }

        HttpServletResponseCopier responseCopier = new HttpServletResponseCopier(httpServletResponse);

        try {
            filterChain.doFilter(httpServletRequest, responseCopier);
            responseCopier.flushBuffer();
        } finally {
            byte[] copy = responseCopier.getCopy();
            System.out.println("---------response--------");
            String response = new String(copy, httpServletResponse.getCharacterEncoding());
            auditEntry.setResponse(response);
        }

        m_auditFilterController.addAuditEntry(auditEntry);

        //don't forget to continue
//        filterChain.doFilter(httpServletRequest, httpServletResponse);


    }

    private boolean isAJAXRequest(HttpServletRequest request) {
        boolean check = false;
        String facesRequest = request.getHeader("Faces-Request");
        if (facesRequest != null && facesRequest.equals("partial/ajax")) {
            check = true;
        }
        return check;
    }
}
