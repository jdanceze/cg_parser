package org.apache.tools.ant;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/ExitStatusException.class */
public class ExitStatusException extends BuildException {
    private static final long serialVersionUID = 7760846806886585968L;
    private int status;

    public ExitStatusException(int status) {
        this.status = status;
    }

    public ExitStatusException(String msg, int status) {
        super(msg);
        this.status = status;
    }

    public ExitStatusException(String message, int status, Location location) {
        super(message, location);
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }
}
