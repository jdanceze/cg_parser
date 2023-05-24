package org.apache.tools.ant;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/UnsupportedAttributeException.class */
public class UnsupportedAttributeException extends BuildException {
    private static final long serialVersionUID = 1;
    private final String attribute;

    public UnsupportedAttributeException(String msg, String attribute) {
        super(msg);
        this.attribute = attribute;
    }

    public String getAttribute() {
        return this.attribute;
    }
}
