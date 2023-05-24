package soot.JastAddJ;

import java.io.FilterReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/Unicode.class */
public class Unicode extends FilterReader {
    private static final int SIZE = 1024;
    private char[] buffer;
    private int pos;
    private int length;
    private int lookahead;
    private int numConsecutiveBackSlash;

    public Unicode(Reader in) {
        super(in);
        this.buffer = new char[1024];
        this.pos = 0;
        this.length = 0;
        this.lookahead = -1;
        this.numConsecutiveBackSlash = 0;
        try {
            next();
        } catch (IOException e) {
        }
    }

    public Unicode(InputStream in) {
        this(new InputStreamReader(in));
    }

    private void refill() throws IOException {
        if (this.pos >= this.length) {
            this.pos = 0;
            int i = this.in.read(this.buffer, 0, 1024);
            this.length = i != -1 ? i : 0;
        }
    }

    private int next() throws IOException {
        char c;
        int c2 = this.lookahead;
        refill();
        if (this.pos >= this.length) {
            c = 65535;
        } else {
            char[] cArr = this.buffer;
            int i = this.pos;
            this.pos = i + 1;
            c = cArr[i];
        }
        this.lookahead = c;
        return c2;
    }

    @Override // java.io.FilterReader, java.io.Reader
    public int read() throws IOException {
        int current = next();
        if (current != 92) {
            this.numConsecutiveBackSlash = 0;
            return current;
        }
        boolean isEven = (this.numConsecutiveBackSlash & 1) == 0;
        if (!isEven || this.lookahead != 117) {
            this.numConsecutiveBackSlash++;
            return current;
        }
        this.numConsecutiveBackSlash = 0;
        while (this.lookahead == 117) {
            next();
        }
        int result = 0;
        for (int i = 0; i < 4; i++) {
            int c = next();
            int value = Character.digit((char) c, 16);
            if (value == -1) {
                throw new Error("Invalid Unicode Escape");
            }
            result = (result << 4) + value;
        }
        return result;
    }

    @Override // java.io.FilterReader, java.io.Reader
    public int read(char[] cbuf, int off, int len) throws IOException {
        if (!ready()) {
            return -1;
        }
        int len2 = len + off;
        int i = off;
        while (i < len2) {
            while (this.pos < this.length && i < len2 - 1 && this.lookahead != 92) {
                if (this.lookahead < 0) {
                    return i - off;
                }
                int i2 = i;
                i++;
                cbuf[i2] = (char) this.lookahead;
                char[] cArr = this.buffer;
                int i3 = this.pos;
                this.pos = i3 + 1;
                this.lookahead = cArr[i3];
                this.numConsecutiveBackSlash = 0;
            }
            int c = read();
            if (c < 0) {
                return i - off;
            }
            cbuf[i] = (char) c;
            i++;
        }
        return len2 - off;
    }

    @Override // java.io.FilterReader, java.io.Reader
    public boolean ready() throws IOException {
        return this.pos < this.length || super.ready();
    }
}
