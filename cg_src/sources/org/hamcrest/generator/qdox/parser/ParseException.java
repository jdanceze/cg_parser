package org.hamcrest.generator.qdox.parser;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/parser/ParseException.class */
public class ParseException extends RuntimeException {
    private int line;
    private int column;
    private String errorMessage;

    public ParseException(String message, int line, int column) {
        this.errorMessage = new StringBuffer().append(message).append(" @[").append(line).append(",").append(column).append("] in ").toString();
        this.line = line;
        this.column = column;
    }

    public int getLine() {
        return this.line;
    }

    public int getColumn() {
        return this.column;
    }

    public void setSourceInfo(String sourceInfo) {
        this.errorMessage = new StringBuffer().append(this.errorMessage).append(sourceInfo).toString();
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        return this.errorMessage;
    }
}
