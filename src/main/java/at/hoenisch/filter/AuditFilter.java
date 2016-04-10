package at.hoenisch.filter;

import org.omnifaces.filter.HttpFilter;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;


/**
 * Created by Philipp Hoenisch, Senacor on 09/04/16.
 */
public class AuditFilter extends HttpFilter {


    @Inject
    private AuditFilterController m_auditFilterController;


    @Override
    public void doFilter(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, HttpSession httpSession, FilterChain filterChain) throws ServletException, IOException {

        //1. check if it is ajax request
        boolean isAjax = isAJAXRequest(httpServletRequest);

        //2. check request, POST, PUT, GET or DELETE parameters
        String method = httpServletRequest.getMethod();

        Enumeration attributeNames = httpServletRequest.getAttributeNames();
        while(attributeNames.hasMoreElements()) {
            Object o = attributeNames.nextElement();
            System.out.println(o);
        }

        Enumeration parameterNames = httpServletRequest.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            Object o = parameterNames.nextElement();
            System.out.println(o);
        }
//
        String contextPath = httpServletRequest.getContextPath();
        String pathInfo = httpServletRequest.getPathInfo();
//

//        OutputStreamResponseWrapper wrappedResponse = new OutputStreamResponseWrapper(httpServletResponse,
//                ByteArrayOutputStream.class);

        //don't forget to continue
        filterChain.doFilter(httpServletRequest, httpServletResponse);
//        wrappedResponse.finishResponse();
//        ByteArrayOutputStream baos = (ByteArrayOutputStream) wrappedResponse.getRealOutputStream();
//         and make use of this
//        String response = baos.toString();

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
