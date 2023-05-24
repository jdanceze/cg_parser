package javassist.bytecode;
/* loaded from: gencallgraphv3.jar:javassist-3.28.0-GA.jar:javassist/bytecode/BadBytecode.class */
public class BadBytecode extends Exception {
    private static final long serialVersionUID = 1;

    public BadBytecode(int opcode) {
        super("bytecode " + opcode);
    }

    public BadBytecode(String msg) {
        super(msg);
    }

    public BadBytecode(String msg, Throwable cause) {
        super(msg, cause);
    }

    public BadBytecode(MethodInfo minfo, Throwable cause) {
        super(minfo.toString() + " in " + minfo.getConstPool().getClassName() + ": " + cause.getMessage(), cause);
    }
}
