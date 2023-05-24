package org.apache.tools.ant.taskdefs.optional.testing;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Location;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/testing/BuildTimeoutException.class */
public class BuildTimeoutException extends BuildException {
    private static final long serialVersionUID = -8057644603246297562L;

    public BuildTimeoutException() {
    }

    public BuildTimeoutException(String message) {
        super(message);
    }

    public BuildTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public BuildTimeoutException(String msg, Throwable cause, Location location) {
        super(msg, cause, location);
    }

    public BuildTimeoutException(Throwable cause) {
        super(cause);
    }

    public BuildTimeoutException(String message, Location location) {
        super(message, location);
    }

    public BuildTimeoutException(Throwable cause, Location location) {
        super(cause, location);
    }
}
