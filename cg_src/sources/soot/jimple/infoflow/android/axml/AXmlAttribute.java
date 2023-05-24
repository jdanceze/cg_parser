package soot.jimple.infoflow.android.axml;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/axml/AXmlAttribute.class */
public class AXmlAttribute<T> extends AXmlElement {
    protected String name;
    protected int type;
    protected T value;
    protected int resourceId;

    public AXmlAttribute(String name, T value, String ns) {
        this(name, -1, value, ns, true);
    }

    public AXmlAttribute(String name, int resourceId, T value, String ns) {
        this(name, resourceId, value, ns, true);
    }

    public AXmlAttribute(String name, int resourceId, T value, String ns, boolean added) {
        this(name, resourceId, -1, value, ns, added);
    }

    public AXmlAttribute(String name, int resourceId, int type, T value, String ns, boolean added) {
        super(ns, added);
        this.name = name;
        this.resourceId = resourceId;
        this.type = type;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public int getResourceId() {
        return this.resourceId;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return this.value;
    }

    public int getType() {
        if (this.value instanceof Integer) {
            return 17;
        }
        if (this.value instanceof Boolean) {
            return 18;
        }
        return 3;
    }

    public int getAttributeType() {
        return this.type;
    }

    public String toString() {
        return String.valueOf(this.name) + "=\"" + this.value + "\"";
    }

    public int hashCode() {
        int result = (31 * 1) + (this.name == null ? 0 : this.name.hashCode());
        return (31 * ((31 * ((31 * result) + this.resourceId)) + this.type)) + (this.value == null ? 0 : this.value.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AXmlAttribute<?> other = (AXmlAttribute) obj;
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals(other.name)) {
            return false;
        }
        if (this.resourceId != other.resourceId || this.type != other.type) {
            return false;
        }
        if (this.value == null) {
            if (other.value != null) {
                return false;
            }
            return true;
        } else if (!this.value.equals(other.value)) {
            return false;
        } else {
            return true;
        }
    }
}
