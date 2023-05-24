package org.slf4j.simple;

import java.io.PrintStream;
/* loaded from: gencallgraphv3.jar:slf4j-simple-2.0.3.jar:org/slf4j/simple/OutputChoice.class */
class OutputChoice {
    final OutputChoiceType outputChoiceType;
    final PrintStream targetPrintStream;

    /* loaded from: gencallgraphv3.jar:slf4j-simple-2.0.3.jar:org/slf4j/simple/OutputChoice$OutputChoiceType.class */
    enum OutputChoiceType {
        SYS_OUT,
        CACHED_SYS_OUT,
        SYS_ERR,
        CACHED_SYS_ERR,
        FILE
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public OutputChoice(OutputChoiceType outputChoiceType) {
        if (outputChoiceType == OutputChoiceType.FILE) {
            throw new IllegalArgumentException();
        }
        this.outputChoiceType = outputChoiceType;
        if (outputChoiceType == OutputChoiceType.CACHED_SYS_OUT) {
            this.targetPrintStream = System.out;
        } else if (outputChoiceType == OutputChoiceType.CACHED_SYS_ERR) {
            this.targetPrintStream = System.err;
        } else {
            this.targetPrintStream = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public OutputChoice(PrintStream printStream) {
        this.outputChoiceType = OutputChoiceType.FILE;
        this.targetPrintStream = printStream;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PrintStream getTargetPrintStream() {
        switch (this.outputChoiceType) {
            case SYS_OUT:
                return System.out;
            case SYS_ERR:
                return System.err;
            case CACHED_SYS_ERR:
            case CACHED_SYS_OUT:
            case FILE:
                return this.targetPrintStream;
            default:
                throw new IllegalArgumentException();
        }
    }
}
