package javax.servlet.jsp;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/servlet/jsp/ErrorData.class */
public final class ErrorData {
    private Throwable throwable;
    private int statusCode;
    private String uri;
    private String servletName;

    public ErrorData(Throwable throwable, int statusCode, String uri, String servletName) {
        this.throwable = throwable;
        this.statusCode = statusCode;
        this.uri = uri;
        this.servletName = servletName;
    }

    public Throwable getThrowable() {
        return this.throwable;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public String getRequestURI() {
        return this.uri;
    }

    public String getServletName() {
        return this.servletName;
    }
}
