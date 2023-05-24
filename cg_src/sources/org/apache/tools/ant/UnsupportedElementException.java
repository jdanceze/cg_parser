package org.apache.tools.ant;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/UnsupportedElementException.class */
public class UnsupportedElementException extends BuildException {
    private static final long serialVersionUID = 1;
    private final String element;

    public UnsupportedElementException(String msg, String element) {
        super(msg);
        this.element = element;
    }

    public String getElement() {
        return this.element;
    }
}
