package org.apache.commons.cli;
/* loaded from: gencallgraphv3.jar:commons-cli-1.2.jar:org/apache/commons/cli/MissingArgumentException.class */
public class MissingArgumentException extends ParseException {
    private Option option;

    public MissingArgumentException(String message) {
        super(message);
    }

    public MissingArgumentException(Option option) {
        this(new StringBuffer().append("Missing argument for option: ").append(option.getKey()).toString());
        this.option = option;
    }

    public Option getOption() {
        return this.option;
    }
}
