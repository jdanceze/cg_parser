package org.apache.commons.cli;
/* loaded from: gencallgraphv3.jar:commons-cli-1.2.jar:org/apache/commons/cli/UnrecognizedOptionException.class */
public class UnrecognizedOptionException extends ParseException {
    private String option;

    public UnrecognizedOptionException(String message) {
        super(message);
    }

    public UnrecognizedOptionException(String message, String option) {
        this(message);
        this.option = option;
    }

    public String getOption() {
        return this.option;
    }
}
