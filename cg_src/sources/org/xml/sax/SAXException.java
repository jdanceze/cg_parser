package org.xml.sax;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/xml/sax/SAXException.class */
public class SAXException extends Exception {
    private static final long serialVersionUID = 583241635256073760L;
    private Exception exception;

    public SAXException() {
        this.exception = null;
    }

    public SAXException(String str) {
        super(str);
        this.exception = null;
    }

    public SAXException(Exception exc) {
        this.exception = exc;
    }

    public SAXException(String str, Exception exc) {
        super(str);
        this.exception = exc;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        String message = super.getMessage();
        return (message != null || this.exception == null) ? message : this.exception.getMessage();
    }

    public Exception getException() {
        return this.exception;
    }

    @Override // java.lang.Throwable
    public String toString() {
        return this.exception != null ? this.exception.toString() : super.toString();
    }
}
