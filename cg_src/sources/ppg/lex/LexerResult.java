package ppg.lex;

import java.io.IOException;
import java.io.OutputStream;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:ppg/lex/LexerResult.class */
interface LexerResult {
    void unparse(OutputStream outputStream) throws IOException;

    int lineNumber();
}
