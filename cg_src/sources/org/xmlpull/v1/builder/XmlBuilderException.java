package org.xmlpull.v1.builder;

import java.io.PrintStream;
import java.io.PrintWriter;
/* loaded from: gencallgraphv3.jar:xpp3-1.1.4c.jar:org/xmlpull/v1/builder/XmlBuilderException.class */
public class XmlBuilderException extends RuntimeException {
    protected Throwable detail;

    public XmlBuilderException(String s) {
        super(s);
    }

    public XmlBuilderException(String s, Throwable thrwble) {
        super(s);
        this.detail = thrwble;
    }

    public Throwable getDetail() {
        return this.detail;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        if (this.detail == null) {
            return super.getMessage();
        }
        return new StringBuffer().append(super.getMessage()).append("; nested exception is: \n\t").append(this.detail.getMessage()).toString();
    }

    @Override // java.lang.Throwable
    public void printStackTrace(PrintStream ps) {
        if (this.detail == null) {
            super.printStackTrace(ps);
            return;
        }
        synchronized (ps) {
            ps.println(new StringBuffer().append(super.getMessage()).append("; nested exception is:").toString());
            this.detail.printStackTrace(ps);
        }
    }

    @Override // java.lang.Throwable
    public void printStackTrace() {
        printStackTrace(System.err);
    }

    @Override // java.lang.Throwable
    public void printStackTrace(PrintWriter pw) {
        if (this.detail == null) {
            super.printStackTrace(pw);
            return;
        }
        synchronized (pw) {
            pw.println(new StringBuffer().append(super.getMessage()).append("; nested exception is:").toString());
            this.detail.printStackTrace(pw);
        }
    }
}
