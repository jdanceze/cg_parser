package ppg.lex;

import java.io.IOException;
import java.io.OutputStream;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:ppg/lex/LexicalError.class */
public class LexicalError extends Exception implements LexerResult {
    private String filename;
    private int lineNumber;
    private String message;

    public LexicalError(String filename, int lineNumber, String message) {
        this.message = message;
        this.filename = filename;
        this.lineNumber = lineNumber;
    }

    @Override // ppg.lex.LexerResult
    public void unparse(OutputStream o) throws IOException {
        o.write(toString().getBytes());
    }

    @Override // java.lang.Throwable
    public String toString() {
        return new StringBuffer().append(this.filename).append("(").append(this.lineNumber).append(") : Lexical error : ").append(this.message).toString();
    }

    public String filename() {
        return this.filename;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        return toString();
    }

    @Override // ppg.lex.LexerResult
    public int lineNumber() {
        return this.lineNumber;
    }
}
