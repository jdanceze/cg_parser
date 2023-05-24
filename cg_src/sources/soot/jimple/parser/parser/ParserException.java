package soot.jimple.parser.parser;

import soot.jimple.parser.node.Token;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/parser/ParserException.class */
public class ParserException extends Exception {
    Token token;

    public ParserException(Token token, String message) {
        super(message);
        this.token = token;
    }

    public Token getToken() {
        return this.token;
    }
}
