package polyglot.util;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/util/UnicodeWriter.class */
public class UnicodeWriter extends FilterWriter {
    public UnicodeWriter(Writer out) {
        super(out);
    }

    @Override // java.io.FilterWriter, java.io.Writer
    public void write(int c) throws IOException {
        if (c <= 255) {
            super.write(c);
            return;
        }
        String s = String.valueOf(Integer.toHexString(c));
        super.write(92);
        super.write(117);
        for (int i = s.length(); i < 4; i++) {
            super.write(48);
        }
        write(s);
    }

    @Override // java.io.FilterWriter, java.io.Writer
    public void write(char[] cbuf, int off, int len) throws IOException {
        for (int i = 0; i < len; i++) {
            write(cbuf[i + off]);
        }
    }

    @Override // java.io.FilterWriter, java.io.Writer
    public void write(String str, int off, int len) throws IOException {
        write(str.toCharArray(), off, len);
    }
}
