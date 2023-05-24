package javax.servlet.jsp.tagext;

import java.util.Enumeration;
import java.util.Hashtable;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/servlet/jsp/tagext/TagData.class */
public class TagData implements Cloneable {
    public static final Object REQUEST_TIME_VALUE = new Object();
    private Hashtable attributes;

    public TagData(Object[][] atts) {
        if (atts == null) {
            this.attributes = new Hashtable();
        } else {
            this.attributes = new Hashtable(atts.length);
        }
        if (atts != null) {
            for (int i = 0; i < atts.length; i++) {
                this.attributes.put(atts[i][0], atts[i][1]);
            }
        }
    }

    public TagData(Hashtable attrs) {
        this.attributes = attrs;
    }

    public String getId() {
        return getAttributeString("id");
    }

    public Object getAttribute(String attName) {
        return this.attributes.get(attName);
    }

    public void setAttribute(String attName, Object value) {
        this.attributes.put(attName, value);
    }

    public String getAttributeString(String attName) {
        Object o = this.attributes.get(attName);
        if (o == null) {
            return null;
        }
        return (String) o;
    }

    public Enumeration getAttributes() {
        return this.attributes.keys();
    }
}
