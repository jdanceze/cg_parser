package javax.servlet.http;

import com.google.common.net.HttpHeaders;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.ResourceBundle;
import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/servlet/http/HttpServlet.class */
public abstract class HttpServlet extends GenericServlet implements Serializable {
    private static final String METHOD_DELETE = "DELETE";
    private static final String METHOD_HEAD = "HEAD";
    private static final String METHOD_GET = "GET";
    private static final String METHOD_OPTIONS = "OPTIONS";
    private static final String METHOD_POST = "POST";
    private static final String METHOD_PUT = "PUT";
    private static final String METHOD_TRACE = "TRACE";
    private static final String HEADER_IFMODSINCE = "If-Modified-Since";
    private static final String HEADER_LASTMOD = "Last-Modified";
    private static final String LSTRING_FILE = "javax.servlet.http.LocalStrings";
    private static ResourceBundle lStrings = ResourceBundle.getBundle(LSTRING_FILE);

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String protocol = req.getProtocol();
        String msg = lStrings.getString("http.method_get_not_supported");
        if (protocol.endsWith("1.1")) {
            resp.sendError(405, msg);
        } else {
            resp.sendError(400, msg);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public long getLastModified(HttpServletRequest req) {
        return -1L;
    }

    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        NoBodyResponse response = new NoBodyResponse(resp);
        doGet(req, response);
        response.setContentLength();
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String protocol = req.getProtocol();
        String msg = lStrings.getString("http.method_post_not_supported");
        if (protocol.endsWith("1.1")) {
            resp.sendError(405, msg);
        } else {
            resp.sendError(400, msg);
        }
    }

    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String protocol = req.getProtocol();
        String msg = lStrings.getString("http.method_put_not_supported");
        if (protocol.endsWith("1.1")) {
            resp.sendError(405, msg);
        } else {
            resp.sendError(400, msg);
        }
    }

    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String protocol = req.getProtocol();
        String msg = lStrings.getString("http.method_delete_not_supported");
        if (protocol.endsWith("1.1")) {
            resp.sendError(405, msg);
        } else {
            resp.sendError(400, msg);
        }
    }

    private Method[] getAllDeclaredMethods(Class c) {
        if (c.getName().equals("javax.servlet.http.HttpServlet")) {
            return null;
        }
        int j = 0;
        Method[] parentMethods = getAllDeclaredMethods(c.getSuperclass());
        Method[] thisMethods = c.getDeclaredMethods();
        if (parentMethods != null) {
            Method[] allMethods = new Method[parentMethods.length + thisMethods.length];
            for (int i = 0; i < parentMethods.length; i++) {
                allMethods[i] = parentMethods[i];
                j = i;
            }
            int j2 = j + 1;
            for (int i2 = j2; i2 < thisMethods.length + j2; i2++) {
                allMethods[i2] = thisMethods[i2 - j2];
            }
            return allMethods;
        }
        return thisMethods;
    }

    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Method[] methods = getAllDeclaredMethods(getClass());
        boolean ALLOW_GET = false;
        boolean ALLOW_HEAD = false;
        boolean ALLOW_POST = false;
        boolean ALLOW_PUT = false;
        boolean ALLOW_DELETE = false;
        for (Method m : methods) {
            if (m.getName().equals("doGet")) {
                ALLOW_GET = true;
                ALLOW_HEAD = true;
            }
            if (m.getName().equals("doPost")) {
                ALLOW_POST = true;
            }
            if (m.getName().equals("doPut")) {
                ALLOW_PUT = true;
            }
            if (m.getName().equals("doDelete")) {
                ALLOW_DELETE = true;
            }
        }
        String allow = null;
        if (ALLOW_GET && 0 == 0) {
            allow = "GET";
        }
        if (ALLOW_HEAD) {
            allow = allow == null ? "HEAD" : new StringBuffer().append(allow).append(", HEAD").toString();
        }
        if (ALLOW_POST) {
            allow = allow == null ? "POST" : new StringBuffer().append(allow).append(", POST").toString();
        }
        if (ALLOW_PUT) {
            allow = allow == null ? "PUT" : new StringBuffer().append(allow).append(", PUT").toString();
        }
        if (ALLOW_DELETE) {
            allow = allow == null ? "DELETE" : new StringBuffer().append(allow).append(", DELETE").toString();
        }
        if (1 != 0) {
            allow = allow == null ? "TRACE" : new StringBuffer().append(allow).append(", TRACE").toString();
        }
        if (1 != 0) {
            allow = allow == null ? "OPTIONS" : new StringBuffer().append(allow).append(", OPTIONS").toString();
        }
        resp.setHeader(HttpHeaders.ALLOW, allow);
    }

    protected void doTrace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String responseString = new StringBuffer().append("TRACE ").append(req.getRequestURI()).append(Instruction.argsep).append(req.getProtocol()).toString();
        Enumeration reqHeaderEnum = req.getHeaderNames();
        while (reqHeaderEnum.hasMoreElements()) {
            String headerName = (String) reqHeaderEnum.nextElement();
            responseString = new StringBuffer().append(responseString).append("\r\n").append(headerName).append(": ").append(req.getHeader(headerName)).toString();
        }
        String responseString2 = new StringBuffer().append(responseString).append("\r\n").toString();
        int responseLength = responseString2.length();
        resp.setContentType("message/http");
        resp.setContentLength(responseLength);
        ServletOutputStream out = resp.getOutputStream();
        out.print(responseString2);
        out.close();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        if (method.equals("GET")) {
            long lastModified = getLastModified(req);
            if (lastModified == -1) {
                doGet(req, resp);
                return;
            }
            long ifModifiedSince = req.getDateHeader("If-Modified-Since");
            if (ifModifiedSince < (lastModified / 1000) * 1000) {
                maybeSetLastModified(resp, lastModified);
                doGet(req, resp);
                return;
            }
            resp.setStatus(304);
        } else if (method.equals("HEAD")) {
            maybeSetLastModified(resp, getLastModified(req));
            doHead(req, resp);
        } else if (method.equals("POST")) {
            doPost(req, resp);
        } else if (method.equals("PUT")) {
            doPut(req, resp);
        } else if (method.equals("DELETE")) {
            doDelete(req, resp);
        } else if (method.equals("OPTIONS")) {
            doOptions(req, resp);
        } else if (method.equals("TRACE")) {
            doTrace(req, resp);
        } else {
            String errMsg = lStrings.getString("http.method_not_implemented");
            Object[] errArgs = {method};
            resp.sendError(501, MessageFormat.format(errMsg, errArgs));
        }
    }

    private void maybeSetLastModified(HttpServletResponse resp, long lastModified) {
        if (!resp.containsHeader("Last-Modified") && lastModified >= 0) {
            resp.setDateHeader("Last-Modified", lastModified);
        }
    }

    @Override // javax.servlet.GenericServlet, javax.servlet.Servlet
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        try {
            HttpServletRequest request = (HttpServletRequest) req;
            HttpServletResponse response = (HttpServletResponse) res;
            service(request, response);
        } catch (ClassCastException e) {
            throw new ServletException("non-HTTP request or response");
        }
    }
}
