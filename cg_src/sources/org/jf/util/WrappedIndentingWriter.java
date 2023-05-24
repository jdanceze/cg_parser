package org.jf.util;

import com.google.common.collect.Lists;
import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/util/WrappedIndentingWriter.class */
public class WrappedIndentingWriter extends FilterWriter {
    private final int maxIndent;
    private final int maxWidth;
    private int currentIndent;
    private final StringBuilder line;

    public WrappedIndentingWriter(Writer out, int maxIndent, int maxWidth) {
        super(out);
        this.currentIndent = 0;
        this.line = new StringBuilder();
        this.maxIndent = maxIndent;
        this.maxWidth = maxWidth;
    }

    private void writeIndent() throws IOException {
        for (int i = 0; i < getIndent(); i++) {
            write(32);
        }
    }

    private int getIndent() {
        if (this.currentIndent < 0) {
            return 0;
        }
        if (this.currentIndent > this.maxIndent) {
            return this.maxIndent;
        }
        return this.currentIndent;
    }

    public void indent(int indent) {
        this.currentIndent += indent;
    }

    public void deindent(int indent) {
        this.currentIndent -= indent;
    }

    private void wrapLine() throws IOException {
        List<String> wrapped = Lists.newArrayList(StringWrapper.wrapStringOnBreaks(this.line.toString(), this.maxWidth));
        this.out.write(wrapped.get(0), 0, wrapped.get(0).length());
        this.out.write(10);
        this.line.replace(0, this.line.length(), "");
        writeIndent();
        for (int i = 1; i < wrapped.size(); i++) {
            if (i > 1) {
                write(10);
            }
            write(wrapped.get(i));
        }
    }

    @Override // java.io.FilterWriter, java.io.Writer
    public void write(int c) throws IOException {
        if (c == 10) {
            this.out.write(this.line.toString());
            this.out.write(c);
            this.line.replace(0, this.line.length(), "");
            writeIndent();
            return;
        }
        this.line.append((char) c);
        if (this.line.length() > this.maxWidth) {
            wrapLine();
        }
    }

    @Override // java.io.FilterWriter, java.io.Writer
    public void write(char[] cbuf, int off, int len) throws IOException {
        for (int i = 0; i < len; i++) {
            write(cbuf[i + off]);
        }
    }

    @Override // java.io.FilterWriter, java.io.Writer
    public void write(String str, int off, int len) throws IOException {
        for (int i = 0; i < len; i++) {
            write(str.charAt(i + off));
        }
    }

    @Override // java.io.FilterWriter, java.io.Writer, java.io.Flushable
    public void flush() throws IOException {
        this.out.write(this.line.toString());
        this.line.replace(0, this.line.length(), "");
    }
}
