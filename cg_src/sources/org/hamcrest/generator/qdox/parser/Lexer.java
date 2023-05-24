package org.hamcrest.generator.qdox.parser;

import java.io.IOException;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/parser/Lexer.class */
public interface Lexer {
    int lex() throws IOException;

    String text();

    int getLine();

    int getColumn();

    String getCodeBody();
}
