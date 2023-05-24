package javax.management;

import java.io.Serializable;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/Attribute.class */
public class Attribute implements Serializable {
    private static final long serialVersionUID = 2484220110589082382L;
    private String name;
    private Object value;

    public Attribute(String str, Object obj) {
        this.value = null;
        if (str == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Attribute name cannot be null "));
        }
        this.name = str;
        this.value = obj;
    }

    public String getName() {
        return this.name;
    }

    public Object getValue() {
        return this.value;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Attribute)) {
            return false;
        }
        Attribute attribute = (Attribute) obj;
        if (this.value != null) {
            return this.name.equals(attribute.getName()) && this.value.equals(attribute.getValue());
        } else if (attribute.getValue() == null) {
            return this.name.equals(attribute.getName());
        } else {
            return false;
        }
    }
}
