package org.apache.tools.ant.filters;

import java.io.IOException;
import java.io.Reader;
import org.apache.tools.ant.util.UnicodeUtil;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/filters/EscapeUnicode.class */
public class EscapeUnicode extends BaseParamFilterReader implements ChainableReader {
    private StringBuffer unicodeBuf;

    public EscapeUnicode() {
        this.unicodeBuf = new StringBuffer();
    }

    public EscapeUnicode(Reader in) {
        super(in);
        this.unicodeBuf = new StringBuffer();
    }

    @Override // java.io.FilterReader, java.io.Reader
    public final int read() throws IOException {
        int ch;
        char achar;
        if (!getInitialized()) {
            initialize();
            setInitialized(true);
        }
        if (this.unicodeBuf.length() > 0) {
            ch = this.unicodeBuf.charAt(0);
            this.unicodeBuf.deleteCharAt(0);
        } else {
            ch = this.in.read();
            if (ch != -1 && (achar = (char) ch) >= 128) {
                this.unicodeBuf = UnicodeUtil.EscapeUnicode(achar);
                ch = 92;
            }
        }
        return ch;
    }

    @Override // org.apache.tools.ant.filters.ChainableReader
    public final Reader chain(Reader rdr) {
        EscapeUnicode newFilter = new EscapeUnicode(rdr);
        newFilter.setInitialized(true);
        return newFilter;
    }

    private void initialize() {
    }
}
