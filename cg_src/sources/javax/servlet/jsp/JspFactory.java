package javax.servlet.jsp;

import javax.servlet.Servlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/servlet/jsp/JspFactory.class */
public abstract class JspFactory {
    private static JspFactory deflt = null;

    public abstract PageContext getPageContext(Servlet servlet, ServletRequest servletRequest, ServletResponse servletResponse, String str, boolean z, int i, boolean z2);

    public abstract void releasePageContext(PageContext pageContext);

    public abstract JspEngineInfo getEngineInfo();

    public static synchronized void setDefaultFactory(JspFactory deflt2) {
        deflt = deflt2;
    }

    public static synchronized JspFactory getDefaultFactory() {
        return deflt;
    }
}
