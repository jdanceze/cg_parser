package javax.servlet.jsp.tagext;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/servlet/jsp/tagext/FunctionInfo.class */
public class FunctionInfo {
    private String name;
    private String functionClass;
    private String functionSignature;

    public FunctionInfo(String name, String klass, String signature) {
        this.name = name;
        this.functionClass = klass;
        this.functionSignature = signature;
    }

    public String getName() {
        return this.name;
    }

    public String getFunctionClass() {
        return this.functionClass;
    }

    public String getFunctionSignature() {
        return this.functionSignature;
    }
}
