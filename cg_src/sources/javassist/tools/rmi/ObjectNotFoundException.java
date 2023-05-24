package javassist.tools.rmi;
/* loaded from: gencallgraphv3.jar:javassist-3.28.0-GA.jar:javassist/tools/rmi/ObjectNotFoundException.class */
public class ObjectNotFoundException extends Exception {
    private static final long serialVersionUID = 1;

    public ObjectNotFoundException(String name) {
        super(name + " is not exported");
    }

    public ObjectNotFoundException(String name, Exception e) {
        super(name + " because of " + e.toString());
    }
}
