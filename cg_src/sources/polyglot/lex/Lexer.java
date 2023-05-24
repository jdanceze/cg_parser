package polyglot.lex;

import java.io.IOException;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/lex/Lexer.class */
public interface Lexer {
    public static final int YYEOF = -1;

    String file();

    Token nextToken() throws IOException;
}
