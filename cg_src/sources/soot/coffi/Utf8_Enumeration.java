package soot.coffi;

import java.util.Enumeration;
/* loaded from: gencallgraphv3.jar:soot/coffi/Utf8_Enumeration.class */
public class Utf8_Enumeration implements Enumeration {
    public int c;
    private short curindex;
    private short length;
    private byte[] bytes;

    public Utf8_Enumeration() {
    }

    public Utf8_Enumeration(byte[] b) {
        this.bytes = b;
        this.curindex = (short) 2;
        this.length = (short) (((this.bytes[0] & 255) << 8) + (this.bytes[1] & 255) + 2);
    }

    public void reset(byte[] b) {
        this.bytes = b;
        this.curindex = (short) 2;
        this.length = (short) (((this.bytes[0] & 255) << 8) + (this.bytes[1] & 255) + 2);
    }

    @Override // java.util.Enumeration
    public boolean hasMoreElements() {
        if (this.curindex < this.length) {
            return true;
        }
        return false;
    }

    @Override // java.util.Enumeration
    public Object nextElement() {
        byte[] bArr = this.bytes;
        short s = this.curindex;
        this.curindex = (short) (s + 1);
        byte b = bArr[s];
        if ((b & Byte.MIN_VALUE) == 0) {
            this.c = b;
        } else if ((b & (-32)) == 192) {
            this.c = (b & 31) << 6;
            byte[] bArr2 = this.bytes;
            short s2 = this.curindex;
            this.curindex = (short) (s2 + 1);
            this.c |= bArr2[s2] & 63;
        } else {
            this.c = (b & 15) << 12;
            byte[] bArr3 = this.bytes;
            short s3 = this.curindex;
            this.curindex = (short) (s3 + 1);
            this.c |= (bArr3[s3] & 63) << 6;
            byte[] bArr4 = this.bytes;
            short s4 = this.curindex;
            this.curindex = (short) (s4 + 1);
            this.c |= bArr4[s4] & 63;
        }
        return this;
    }
}
