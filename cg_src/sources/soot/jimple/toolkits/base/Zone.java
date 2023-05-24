package soot.jimple.toolkits.base;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/base/Zone.class */
public class Zone {
    private final String name;

    public Zone(String name) {
        this.name = name;
    }

    public String toString() {
        return "<zone: " + this.name + ">";
    }
}
