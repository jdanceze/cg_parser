package javax.servlet.jsp.tagext;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/servlet/jsp/tagext/ValidationMessage.class */
public class ValidationMessage {
    private String id;
    private String message;

    public ValidationMessage(String id, String message) {
        this.id = id;
        this.message = message;
    }

    public String getId() {
        return this.id;
    }

    public String getMessage() {
        return this.message;
    }
}
