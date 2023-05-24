package javax.ejb;

import java.io.PrintStream;
import java.io.PrintWriter;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/ejb/EJBException.class */
public class EJBException extends RuntimeException {
    private Exception causeException;

    public EJBException() {
        this.causeException = null;
    }

    public EJBException(String message) {
        super(message);
        this.causeException = null;
    }

    public EJBException(Exception ex) {
        this.causeException = null;
        this.causeException = ex;
    }

    public EJBException(String message, Exception ex) {
        super(message);
        this.causeException = null;
        this.causeException = ex;
    }

    public Exception getCausedByException() {
        return this.causeException;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        String msg = super.getMessage();
        if (this.causeException == null) {
            return msg;
        }
        if (msg == null) {
            return new StringBuffer().append("nested exception is: ").append(this.causeException.toString()).toString();
        }
        return new StringBuffer().append(msg).append("; nested exception is: ").append(this.causeException.toString()).toString();
    }

    @Override // java.lang.Throwable
    public void printStackTrace(PrintStream ps) {
        if (this.causeException == null) {
            super.printStackTrace(ps);
            return;
        }
        synchronized (ps) {
            ps.println(this);
            this.causeException.printStackTrace(ps);
            super.printStackTrace(ps);
        }
    }

    @Override // java.lang.Throwable
    public void printStackTrace() {
        printStackTrace(System.err);
    }

    @Override // java.lang.Throwable
    public void printStackTrace(PrintWriter pw) {
        if (this.causeException == null) {
            super.printStackTrace(pw);
            return;
        }
        synchronized (pw) {
            pw.println(this);
            this.causeException.printStackTrace(pw);
            super.printStackTrace(pw);
        }
    }
}
