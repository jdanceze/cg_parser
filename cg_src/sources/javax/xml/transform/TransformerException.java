package javax.xml.transform;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:javax/xml/transform/TransformerException.class */
public class TransformerException extends Exception {
    SourceLocator locator;
    Throwable containedException;

    public SourceLocator getLocator() {
        return this.locator;
    }

    public void setLocator(SourceLocator sourceLocator) {
        this.locator = sourceLocator;
    }

    public Throwable getException() {
        return this.containedException;
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        if (this.containedException == this) {
            return null;
        }
        return this.containedException;
    }

    @Override // java.lang.Throwable
    public synchronized Throwable initCause(Throwable th) {
        if (this.containedException != null) {
            throw new IllegalStateException("Can't overwrite cause");
        }
        if (th == this) {
            throw new IllegalArgumentException("Self-causation not permitted");
        }
        this.containedException = th;
        return this;
    }

    public TransformerException(String str) {
        super(str);
        this.containedException = null;
        this.locator = null;
    }

    public TransformerException(Throwable th) {
        super(th.toString());
        this.containedException = th;
        this.locator = null;
    }

    public TransformerException(String str, Throwable th) {
        super((str == null || str.length() == 0) ? th.toString() : str);
        this.containedException = th;
        this.locator = null;
    }

    public TransformerException(String str, SourceLocator sourceLocator) {
        super(str);
        this.containedException = null;
        this.locator = sourceLocator;
    }

    public TransformerException(String str, SourceLocator sourceLocator, Throwable th) {
        super(str);
        this.containedException = th;
        this.locator = sourceLocator;
    }

    public String getMessageAndLocation() {
        StringBuffer stringBuffer = new StringBuffer();
        String message = super.getMessage();
        if (null != message) {
            stringBuffer.append(message);
        }
        if (null != this.locator) {
            String systemId = this.locator.getSystemId();
            int lineNumber = this.locator.getLineNumber();
            int columnNumber = this.locator.getColumnNumber();
            if (null != systemId) {
                stringBuffer.append("; SystemID: ");
                stringBuffer.append(systemId);
            }
            if (0 != lineNumber) {
                stringBuffer.append("; Line#: ");
                stringBuffer.append(lineNumber);
            }
            if (0 != columnNumber) {
                stringBuffer.append("; Column#: ");
                stringBuffer.append(columnNumber);
            }
        }
        return stringBuffer.toString();
    }

    public String getLocationAsString() {
        if (null != this.locator) {
            StringBuffer stringBuffer = new StringBuffer();
            String systemId = this.locator.getSystemId();
            int lineNumber = this.locator.getLineNumber();
            int columnNumber = this.locator.getColumnNumber();
            if (null != systemId) {
                stringBuffer.append("; SystemID: ");
                stringBuffer.append(systemId);
            }
            if (0 != lineNumber) {
                stringBuffer.append("; Line#: ");
                stringBuffer.append(lineNumber);
            }
            if (0 != columnNumber) {
                stringBuffer.append("; Column#: ");
                stringBuffer.append(columnNumber);
            }
            return stringBuffer.toString();
        }
        return null;
    }

    @Override // java.lang.Throwable
    public void printStackTrace() {
        printStackTrace(new PrintWriter((OutputStream) System.err, true));
    }

    @Override // java.lang.Throwable
    public void printStackTrace(PrintStream printStream) {
        printStackTrace(new PrintWriter(printStream));
    }

    @Override // java.lang.Throwable
    public void printStackTrace(PrintWriter printWriter) {
        String locationAsString;
        if (printWriter == null) {
            printWriter = new PrintWriter((OutputStream) System.err, true);
        }
        try {
            String locationAsString2 = getLocationAsString();
            if (null != locationAsString2) {
                printWriter.println(locationAsString2);
            }
            super.printStackTrace(printWriter);
        } catch (Throwable th) {
        }
        Throwable exception = getException();
        for (int i = 0; i < 10 && null != exception; i++) {
            printWriter.println("---------");
            try {
                if ((exception instanceof TransformerException) && null != (locationAsString = ((TransformerException) exception).getLocationAsString())) {
                    printWriter.println(locationAsString);
                }
                exception.printStackTrace(printWriter);
            } catch (Throwable th2) {
                printWriter.println("Could not print stack trace...");
            }
            try {
                Method method = exception.getClass().getMethod("getException", null);
                if (null != method) {
                    Throwable th3 = exception;
                    exception = (Throwable) method.invoke(exception, null);
                    if (th3 == exception) {
                        break;
                    }
                } else {
                    exception = null;
                }
            } catch (IllegalAccessException e) {
                exception = null;
            } catch (NoSuchMethodException e2) {
                exception = null;
            } catch (InvocationTargetException e3) {
                exception = null;
            }
        }
        printWriter.flush();
    }
}
