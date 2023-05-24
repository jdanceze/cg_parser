package javax.mail;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/Header.class */
public class Header {
    private String name;
    private String value;

    public Header(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }
}
