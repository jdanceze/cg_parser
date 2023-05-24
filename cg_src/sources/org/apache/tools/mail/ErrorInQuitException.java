package org.apache.tools.mail;

import java.io.IOException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/mail/ErrorInQuitException.class */
public class ErrorInQuitException extends IOException {
    private static final long serialVersionUID = 1;

    public ErrorInQuitException(IOException e) {
        super(e.getMessage());
    }
}
