package ppg.parse;

import ppg.lex.Token;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:ppg/parse/ParserError.class */
public class ParserError extends Exception {
    protected String errorMessage;

    public ParserError(String message) {
        this.errorMessage = message;
    }

    public ParserError(String file, String msg, Token tok) {
    }

    public ParserError() {
        this("There is a parse error in your code...");
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}
