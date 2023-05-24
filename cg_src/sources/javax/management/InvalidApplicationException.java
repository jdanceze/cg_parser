package javax.management;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/InvalidApplicationException.class */
public class InvalidApplicationException extends Exception {
    private static final long serialVersionUID = -3048022274675537269L;
    private Object val;

    public InvalidApplicationException(Object obj) {
        this.val = obj;
    }
}
