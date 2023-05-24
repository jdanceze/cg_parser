package javax.servlet.jsp.tagext;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/servlet/jsp/tagext/VariableInfo.class */
public class VariableInfo {
    public static final int NESTED = 0;
    public static final int AT_BEGIN = 1;
    public static final int AT_END = 2;
    private String varName;
    private String className;
    private boolean declare;
    private int scope;

    public VariableInfo(String varName, String className, boolean declare, int scope) {
        this.varName = varName;
        this.className = className;
        this.declare = declare;
        this.scope = scope;
    }

    public String getVarName() {
        return this.varName;
    }

    public String getClassName() {
        return this.className;
    }

    public boolean getDeclare() {
        return this.declare;
    }

    public int getScope() {
        return this.scope;
    }
}
