package soot.util;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/* loaded from: gencallgraphv3.jar:soot/util/EscapedReader.class */
public class EscapedReader extends FilterReader {
    private static final Logger logger = LoggerFactory.getLogger(EscapedReader.class);
    private StringBuffer mini;
    boolean nextF;
    int nextch;

    public EscapedReader(Reader fos) {
        super(fos);
        this.mini = new StringBuffer();
        this.nextch = 0;
    }

    @Override // java.io.FilterReader, java.io.Reader
    public int read() throws IOException {
        if (this.nextF) {
            this.nextF = false;
            return this.nextch;
        }
        int ch = super.read();
        if (ch != 92) {
            return ch;
        }
        this.mini = new StringBuffer();
        int ch2 = super.read();
        if (ch2 != 117) {
            this.nextF = true;
            this.nextch = ch2;
            return 92;
        }
        this.mini.append("\\u");
        while (this.mini.length() < 6) {
            this.mini.append((char) super.read());
        }
        return Integer.parseInt(this.mini.substring(2).toString(), 16);
    }
}
