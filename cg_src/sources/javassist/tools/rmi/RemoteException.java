package javassist.tools.rmi;
/* loaded from: gencallgraphv3.jar:javassist-3.28.0-GA.jar:javassist/tools/rmi/RemoteException.class */
public class RemoteException extends RuntimeException {
    private static final long serialVersionUID = 1;

    public RemoteException(String msg) {
        super(msg);
    }

    public RemoteException(Exception e) {
        super("by " + e.toString());
    }
}
