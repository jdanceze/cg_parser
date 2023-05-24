package javax.transaction;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/transaction/SystemException.class */
public class SystemException extends Exception {
    public int errorCode;

    public SystemException() {
    }

    public SystemException(String s) {
        super(s);
    }

    public SystemException(int errcode) {
        this.errorCode = errcode;
    }
}
