package org.hamcrest.generator.qdox.model;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/model/IndentBuffer.class */
public class IndentBuffer {
    private StringBuffer buffer = new StringBuffer();
    private int depth = 0;
    private boolean newLine;

    public void write(String s) {
        checkNewLine();
        this.buffer.append(s);
    }

    public void write(char s) {
        checkNewLine();
        this.buffer.append(s);
    }

    public void newline() {
        this.buffer.append('\n');
        this.newLine = true;
    }

    public void indent() {
        this.depth++;
    }

    public void deindent() {
        this.depth--;
    }

    public String toString() {
        return this.buffer.toString();
    }

    private void checkNewLine() {
        if (this.newLine) {
            for (int i = 0; i < this.depth; i++) {
                this.buffer.append('\t');
            }
            this.newLine = false;
        }
    }
}
