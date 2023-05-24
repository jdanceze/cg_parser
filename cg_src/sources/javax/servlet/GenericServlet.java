package javax.servlet;

import java.io.IOException;
import java.io.Serializable;
import java.util.Enumeration;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/servlet/GenericServlet.class */
public abstract class GenericServlet implements Servlet, ServletConfig, Serializable {
    private transient ServletConfig config;

    @Override // javax.servlet.Servlet
    public abstract void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException;

    @Override // javax.servlet.Servlet
    public void destroy() {
    }

    @Override // javax.servlet.ServletConfig
    public String getInitParameter(String name) {
        return getServletConfig().getInitParameter(name);
    }

    @Override // javax.servlet.ServletConfig
    public Enumeration getInitParameterNames() {
        return getServletConfig().getInitParameterNames();
    }

    @Override // javax.servlet.Servlet
    public ServletConfig getServletConfig() {
        return this.config;
    }

    @Override // javax.servlet.ServletConfig
    public ServletContext getServletContext() {
        return getServletConfig().getServletContext();
    }

    @Override // javax.servlet.Servlet
    public String getServletInfo() {
        return "";
    }

    @Override // javax.servlet.Servlet
    public void init(ServletConfig config) throws ServletException {
        this.config = config;
        init();
    }

    public void init() throws ServletException {
    }

    public void log(String msg) {
        getServletContext().log(new StringBuffer().append(getServletName()).append(": ").append(msg).toString());
    }

    public void log(String message, Throwable t) {
        getServletContext().log(new StringBuffer().append(getServletName()).append(": ").append(message).toString(), t);
    }

    @Override // javax.servlet.ServletConfig
    public String getServletName() {
        return this.config.getServletName();
    }
}
