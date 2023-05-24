package soot.jimple.toolkits.pointer.nativemethods;

import soot.SootMethod;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/pointer/nativemethods/NativeMethodNotSupportedException.class */
public class NativeMethodNotSupportedException extends RuntimeException {
    private String msg;

    public NativeMethodNotSupportedException(SootMethod method) {
        String message = "The following native method is not supported: \n  " + method.getSignature();
        this.msg = message;
    }

    public NativeMethodNotSupportedException(String message) {
        this.msg = message;
    }

    public NativeMethodNotSupportedException() {
    }

    @Override // java.lang.Throwable
    public String toString() {
        return this.msg;
    }
}
