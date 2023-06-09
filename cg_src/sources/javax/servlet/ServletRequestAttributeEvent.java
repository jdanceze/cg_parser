package javax.servlet;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/servlet/ServletRequestAttributeEvent.class */
public class ServletRequestAttributeEvent extends ServletRequestEvent {
    private String name;
    private Object value;

    public ServletRequestAttributeEvent(ServletContext sc, ServletRequest request, String name, Object value) {
        super(sc, request);
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public Object getValue() {
        return this.value;
    }
}
