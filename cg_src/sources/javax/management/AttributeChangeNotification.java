package javax.management;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/AttributeChangeNotification.class */
public class AttributeChangeNotification extends Notification {
    private static final long serialVersionUID = 535176054565814134L;
    public static final String ATTRIBUTE_CHANGE = new String("jmx.attribute.change");
    private String attributeName;
    private String attributeType;
    private Object oldValue;
    private Object newValue;

    public AttributeChangeNotification(Object obj, long j, long j2, String str, String str2, String str3, Object obj2, Object obj3) {
        super(ATTRIBUTE_CHANGE, obj, j, j2, str);
        this.attributeName = null;
        this.attributeType = null;
        this.oldValue = null;
        this.newValue = null;
        this.attributeName = str2;
        this.attributeType = str3;
        this.oldValue = obj2;
        this.newValue = obj3;
    }

    public String getAttributeName() {
        return this.attributeName;
    }

    public String getAttributeType() {
        return this.attributeType;
    }

    public Object getOldValue() {
        return this.oldValue;
    }

    public Object getNewValue() {
        return this.newValue;
    }
}
