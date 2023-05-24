package javax.mail.internet;

import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
/* compiled from: MimeUtility.java */
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/internet/AsciiOutputStream.class */
class AsciiOutputStream extends OutputStream {
    private boolean breakOnNonAscii;
    private boolean checkEOL;
    private int ascii = 0;
    private int non_ascii = 0;
    private int linelen = 0;
    private boolean longLine = false;
    private boolean badEOL = false;
    private int lastb = 0;
    private int ret = 0;

    public AsciiOutputStream(boolean breakOnNonAscii, boolean encodeEolStrict) {
        this.checkEOL = false;
        this.breakOnNonAscii = breakOnNonAscii;
        this.checkEOL = encodeEolStrict && breakOnNonAscii;
    }

    @Override // java.io.OutputStream
    public void write(int b) throws IOException {
        check(b);
    }

    @Override // java.io.OutputStream
    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    @Override // java.io.OutputStream
    public void write(byte[] b, int off, int len) throws IOException {
        int len2 = len + off;
        for (int i = off; i < len2; i++) {
            check(b[i]);
        }
    }

    private final void check(int b) throws IOException {
        int b2 = b & 255;
        if (this.checkEOL && ((this.lastb == 13 && b2 != 10) || (this.lastb != 13 && b2 == 10))) {
            this.badEOL = true;
        }
        if (b2 == 13 || b2 == 10) {
            this.linelen = 0;
        } else {
            this.linelen++;
            if (this.linelen > 998) {
                this.longLine = true;
            }
        }
        if (MimeUtility.nonascii(b2)) {
            this.non_ascii++;
            if (this.breakOnNonAscii) {
                this.ret = 3;
                throw new EOFException();
            }
        } else {
            this.ascii++;
        }
        this.lastb = b2;
    }

    public int getAscii() {
        if (this.ret != 0) {
            return this.ret;
        }
        if (this.badEOL) {
            return 3;
        }
        if (this.non_ascii == 0) {
            if (this.longLine) {
                return 2;
            }
            return 1;
        } else if (this.ascii > this.non_ascii) {
            return 2;
        } else {
            return 3;
        }
    }
}
