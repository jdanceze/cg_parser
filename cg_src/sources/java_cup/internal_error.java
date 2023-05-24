package java_cup;
/* loaded from: gencallgraphv3.jar:java_cup-0.9.2.jar:java_cup/internal_error.class */
public class internal_error extends Exception {
    public internal_error(String msg) {
        super(msg);
    }

    public void crash() {
        ErrorManager.getManager().emit_fatal("JavaCUP Internal Error Detected: " + getMessage());
        printStackTrace();
        System.exit(-1);
    }
}
