package javassist.compiler;
/* loaded from: gencallgraphv3.jar:javassist-3.28.0-GA.jar:javassist/compiler/SyntaxError.class */
public class SyntaxError extends CompileError {
    private static final long serialVersionUID = 1;

    public SyntaxError(Lex lexer) {
        super("syntax error near \"" + lexer.getTextAround() + "\"", lexer);
    }
}
