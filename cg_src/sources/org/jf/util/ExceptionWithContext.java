package org.jf.util;

import java.io.PrintStream;
import java.io.PrintWriter;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/util/ExceptionWithContext.class */
public class ExceptionWithContext extends RuntimeException {
    private StringBuffer context;

    public static ExceptionWithContext withContext(Throwable ex, String str, Object... formatArgs) {
        ExceptionWithContext ewc;
        if (ex instanceof ExceptionWithContext) {
            ewc = (ExceptionWithContext) ex;
        } else {
            ewc = new ExceptionWithContext(ex);
        }
        ewc.addContext(String.format(str, formatArgs));
        return ewc;
    }

    public ExceptionWithContext(String message, Object... formatArgs) {
        this(null, message, formatArgs);
    }

    public ExceptionWithContext(Throwable cause) {
        this(cause, null, new Object[0]);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public ExceptionWithContext(java.lang.Throwable r7, java.lang.String r8, java.lang.Object... r9) {
        /*
            r6 = this;
            r0 = r6
            r1 = r8
            if (r1 == 0) goto Ld
            r1 = r8
            r2 = r9
            java.lang.String r1 = formatMessage(r1, r2)
            goto L19
        Ld:
            r1 = r7
            if (r1 == 0) goto L18
            r1 = r7
            java.lang.String r1 = r1.getMessage()
            goto L19
        L18:
            r1 = 0
        L19:
            r2 = r7
            r0.<init>(r1, r2)
            r0 = r7
            boolean r0 = r0 instanceof org.jf.util.ExceptionWithContext
            if (r0 == 0) goto L51
            r0 = r7
            org.jf.util.ExceptionWithContext r0 = (org.jf.util.ExceptionWithContext) r0
            java.lang.StringBuffer r0 = r0.context
            java.lang.String r0 = r0.toString()
            r10 = r0
            r0 = r6
            java.lang.StringBuffer r1 = new java.lang.StringBuffer
            r2 = r1
            r3 = r10
            int r3 = r3.length()
            r4 = 200(0xc8, float:2.8E-43)
            int r3 = r3 + r4
            r2.<init>(r3)
            r0.context = r1
            r0 = r6
            java.lang.StringBuffer r0 = r0.context
            r1 = r10
            java.lang.StringBuffer r0 = r0.append(r1)
            goto L5f
        L51:
            r0 = r6
            java.lang.StringBuffer r1 = new java.lang.StringBuffer
            r2 = r1
            r3 = 200(0xc8, float:2.8E-43)
            r2.<init>(r3)
            r0.context = r1
        L5f:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jf.util.ExceptionWithContext.<init>(java.lang.Throwable, java.lang.String, java.lang.Object[]):void");
    }

    private static String formatMessage(String message, Object... formatArgs) {
        if (message == null) {
            return null;
        }
        return String.format(message, formatArgs);
    }

    @Override // java.lang.Throwable
    public void printStackTrace(PrintStream out) {
        super.printStackTrace(out);
        out.println(this.context);
    }

    @Override // java.lang.Throwable
    public void printStackTrace(PrintWriter out) {
        super.printStackTrace(out);
        out.println(this.context);
    }

    public void addContext(String str) {
        if (str == null) {
            throw new NullPointerException("str == null");
        }
        this.context.append(str);
        if (!str.endsWith("\n")) {
            this.context.append('\n');
        }
    }

    public String getContext() {
        return this.context.toString();
    }

    public void printContext(PrintStream out) {
        out.println(getMessage());
        out.print(this.context);
    }

    public void printContext(PrintWriter out) {
        out.println(getMessage());
        out.print(this.context);
    }
}
