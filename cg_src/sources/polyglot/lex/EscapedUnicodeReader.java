package polyglot.lex;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/lex/EscapedUnicodeReader.class */
public class EscapedUnicodeReader extends FilterReader {
    int pushback;
    boolean isEvenSlash;

    public EscapedUnicodeReader(Reader in) {
        super(in);
        this.pushback = -1;
        this.isEvenSlash = true;
    }

    @Override // java.io.FilterReader, java.io.Reader
    public int read() throws IOException {
        int read;
        int r;
        int r2 = this.pushback == -1 ? this.in.read() : this.pushback;
        this.pushback = -1;
        if (r2 != 92) {
            this.isEvenSlash = true;
            return r2;
        } else if (!this.isEvenSlash) {
            this.isEvenSlash = true;
            return r2;
        } else {
            this.pushback = this.in.read();
            if (this.pushback != 117) {
                this.isEvenSlash = false;
                return 92;
            }
            this.pushback = -1;
            do {
                read = this.in.read();
                r = read;
            } while (read == 117);
            int val = 0;
            int i = 0;
            while (i < 4) {
                int d = Character.digit((char) r, 16);
                if (r < 0 || d < 0) {
                    throw new Error("Invalid unicode escape character.");
                }
                val = (val * 16) + d;
                i++;
                r = this.in.read();
            }
            this.pushback = r;
            this.isEvenSlash = true;
            return val;
        }
    }

    @Override // java.io.FilterReader, java.io.Reader
    public int read(char[] cbuf, int off, int len) throws IOException {
        for (int i = 0; i < len; i++) {
            int c = read();
            if (c == -1) {
                if (i == 0) {
                    return -1;
                }
                return i;
            }
            cbuf[i + off] = (char) c;
        }
        return len;
    }

    @Override // java.io.FilterReader, java.io.Reader
    public boolean markSupported() {
        return false;
    }

    @Override // java.io.FilterReader, java.io.Reader
    public boolean ready() throws IOException {
        if (this.pushback != -1) {
            return true;
        }
        return this.in.ready();
    }
}
