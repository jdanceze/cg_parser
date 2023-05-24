package org.apache.tools.mail;

import java.io.OutputStream;
import java.io.PrintStream;
/* compiled from: MailMessage.java */
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/mail/MailPrintStream.class */
class MailPrintStream extends PrintStream {
    private int lastChar;

    public MailPrintStream(OutputStream out) {
        super(out, true);
    }

    @Override // java.io.PrintStream, java.io.FilterOutputStream, java.io.OutputStream
    public void write(int b) {
        if (b == 10 && this.lastChar != 13) {
            rawWrite(13);
            rawWrite(b);
        } else if (b == 46 && this.lastChar == 10) {
            rawWrite(46);
            rawWrite(b);
        } else {
            rawWrite(b);
        }
        this.lastChar = b;
    }

    @Override // java.io.PrintStream, java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] buf, int off, int len) {
        for (int i = 0; i < len; i++) {
            write(buf[off + i]);
        }
    }

    void rawWrite(int b) {
        super.write(b);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void rawPrint(String s) {
        char[] charArray;
        for (char ch : s.toCharArray()) {
            rawWrite(ch);
        }
    }
}
