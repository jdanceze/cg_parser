package javax.servlet.jsp;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.tagext.BodyContent;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/servlet/jsp/PageContext.class */
public abstract class PageContext extends JspContext {
    public static final int PAGE_SCOPE = 1;
    public static final int REQUEST_SCOPE = 2;
    public static final int SESSION_SCOPE = 3;
    public static final int APPLICATION_SCOPE = 4;
    public static final String PAGE = "javax.servlet.jsp.jspPage";
    public static final String PAGECONTEXT = "javax.servlet.jsp.jspPageContext";
    public static final String REQUEST = "javax.servlet.jsp.jspRequest";
    public static final String RESPONSE = "javax.servlet.jsp.jspResponse";
    public static final String CONFIG = "javax.servlet.jsp.jspConfig";
    public static final String SESSION = "javax.servlet.jsp.jspSession";
    public static final String OUT = "javax.servlet.jsp.jspOut";
    public static final String APPLICATION = "javax.servlet.jsp.jspApplication";
    public static final String EXCEPTION = "javax.servlet.jsp.jspException";

    public abstract void initialize(Servlet servlet, ServletRequest servletRequest, ServletResponse servletResponse, String str, boolean z, int i, boolean z2) throws IOException, IllegalStateException, IllegalArgumentException;

    public abstract void release();

    public abstract HttpSession getSession();

    public abstract Object getPage();

    public abstract ServletRequest getRequest();

    public abstract ServletResponse getResponse();

    public abstract Exception getException();

    public abstract ServletConfig getServletConfig();

    public abstract ServletContext getServletContext();

    public abstract void forward(String str) throws ServletException, IOException;

    public abstract void include(String str) throws ServletException, IOException;

    public abstract void include(String str, boolean z) throws ServletException, IOException;

    public abstract void handlePageException(Exception exc) throws ServletException, IOException;

    public abstract void handlePageException(Throwable th) throws ServletException, IOException;

    public BodyContent pushBody() {
        return null;
    }

    public ErrorData getErrorData() {
        return new ErrorData((Throwable) getRequest().getAttribute("javax.servlet.error.exception"), ((Integer) getRequest().getAttribute("javax.servlet.error.status_code")).intValue(), (String) getRequest().getAttribute("javax.servlet.error.request_uri"), (String) getRequest().getAttribute("javax.servlet.error.servlet_name"));
    }
}
