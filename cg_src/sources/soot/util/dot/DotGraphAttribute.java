package soot.util.dot;
/* loaded from: gencallgraphv3.jar:soot/util/dot/DotGraphAttribute.class */
public class DotGraphAttribute {
    private final String id;
    private final String value;

    public DotGraphAttribute(String id, String v) {
        this.id = id;
        this.value = v;
    }

    public String toString() {
        return String.valueOf(this.id) + '=' + this.value;
    }

    public String getID() {
        return this.id;
    }

    public String getValue() {
        return this.value;
    }
}
