package javax.xml.soap;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/soap/MimeHeader.class */
public class MimeHeader {
    private String name;
    private String value;

    public MimeHeader(String str, String str2) {
        this.name = str;
        this.value = str2;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }
}
