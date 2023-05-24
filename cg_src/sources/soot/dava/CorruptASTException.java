package soot.dava;
/* loaded from: gencallgraphv3.jar:soot/dava/CorruptASTException.class */
public class CorruptASTException extends DecompilationException {
    public CorruptASTException(String message) {
        System.out.println("The Abstract Syntax Tree is corrupt");
        System.out.println(message);
        report();
    }
}
