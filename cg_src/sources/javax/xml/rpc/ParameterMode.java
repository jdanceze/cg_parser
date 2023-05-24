package javax.xml.rpc;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/rpc/ParameterMode.class */
public class ParameterMode {
    private final String mode;
    public static final ParameterMode IN = new ParameterMode("IN");
    public static final ParameterMode OUT = new ParameterMode("OUT");
    public static final ParameterMode INOUT = new ParameterMode("INOUT");

    private ParameterMode(String mode) {
        this.mode = mode;
    }

    public String toString() {
        return this.mode;
    }
}
