package javax.servlet.jsp.tagext;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/servlet/jsp/tagext/TagVariableInfo.class */
public class TagVariableInfo {
    private String nameGiven;
    private String nameFromAttribute;
    private String className;
    private boolean declare;
    private int scope;

    public TagVariableInfo(String nameGiven, String nameFromAttribute, String className, boolean declare, int scope) {
        this.nameGiven = nameGiven;
        this.nameFromAttribute = nameFromAttribute;
        this.className = className;
        this.declare = declare;
        this.scope = scope;
    }

    public String getNameGiven() {
        return this.nameGiven;
    }

    public String getNameFromAttribute() {
        return this.nameFromAttribute;
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
