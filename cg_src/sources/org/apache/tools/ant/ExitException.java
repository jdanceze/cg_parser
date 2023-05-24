package org.apache.tools.ant;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/ExitException.class */
public class ExitException extends SecurityException {
    private static final long serialVersionUID = 2772487854280543363L;
    private int status;

    public ExitException(int status) {
        super("ExitException: status " + status);
        this.status = status;
    }

    public ExitException(String msg, int status) {
        super(msg);
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }
}
