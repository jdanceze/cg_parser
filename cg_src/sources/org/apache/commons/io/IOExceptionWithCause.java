package org.apache.commons.io;

import java.io.IOException;
@Deprecated
/* loaded from: gencallgraphv3.jar:commons-io-2.7.jar:org/apache/commons/io/IOExceptionWithCause.class */
public class IOExceptionWithCause extends IOException {
    private static final long serialVersionUID = 1;

    public IOExceptionWithCause(String message, Throwable cause) {
        super(message, cause);
    }

    public IOExceptionWithCause(Throwable cause) {
        super(cause);
    }
}
