package com.sun.xml.bind.v2.runtime.output;

import java.io.IOException;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/output/Encoded.class */
public final class Encoded {
    public byte[] buf;
    public int len;
    private static final byte[][] entities = new byte[128];
    private static final byte[][] attributeEntities = new byte[128];

    public Encoded() {
    }

    public Encoded(String text) {
        set(text);
    }

    public void ensureSize(int size) {
        if (this.buf == null || this.buf.length < size) {
            this.buf = new byte[size];
        }
    }

    public final void set(String text) {
        int ptr;
        int length = text.length();
        ensureSize((length * 3) + 1);
        int ptr2 = 0;
        int i = 0;
        while (i < length) {
            char chr = text.charAt(i);
            if (chr > 127) {
                if (chr > 2047) {
                    if (55296 <= chr && chr <= 57343) {
                        i++;
                        int uc = (((chr & 1023) << 10) | (text.charAt(i) & 1023)) + 65536;
                        int i2 = ptr2;
                        int ptr3 = ptr2 + 1;
                        this.buf[i2] = (byte) (240 | (uc >> 18));
                        int ptr4 = ptr3 + 1;
                        this.buf[ptr3] = (byte) (128 | ((uc >> 12) & 63));
                        int ptr5 = ptr4 + 1;
                        this.buf[ptr4] = (byte) (128 | ((uc >> 6) & 63));
                        ptr2 = ptr5 + 1;
                        this.buf[ptr5] = (byte) (128 + (uc & 63));
                    } else {
                        int i3 = ptr2;
                        int ptr6 = ptr2 + 1;
                        this.buf[i3] = (byte) (224 + (chr >> '\f'));
                        ptr = ptr6 + 1;
                        this.buf[ptr6] = (byte) (128 + ((chr >> 6) & 63));
                    }
                } else {
                    int i4 = ptr2;
                    ptr = ptr2 + 1;
                    this.buf[i4] = (byte) (192 + (chr >> 6));
                }
                int i5 = ptr;
                ptr2 = ptr + 1;
                this.buf[i5] = (byte) (128 + (chr & '?'));
            } else {
                int i6 = ptr2;
                ptr2++;
                this.buf[i6] = (byte) chr;
            }
            i++;
        }
        this.len = ptr2;
    }

    public final void setEscape(String text, boolean isAttribute) {
        int ptr1;
        int ptr12;
        int length = text.length();
        ensureSize((length * 6) + 1);
        int ptr = 0;
        int i = 0;
        while (i < length) {
            char chr = text.charAt(i);
            int ptr13 = ptr;
            if (chr > 127) {
                if (chr > 2047) {
                    if (55296 <= chr && chr <= 57343) {
                        i++;
                        int uc = (((chr & 1023) << 10) | (text.charAt(i) & 1023)) + 65536;
                        int i2 = ptr;
                        int ptr2 = ptr + 1;
                        this.buf[i2] = (byte) (240 | (uc >> 18));
                        int ptr3 = ptr2 + 1;
                        this.buf[ptr2] = (byte) (128 | ((uc >> 12) & 63));
                        int ptr4 = ptr3 + 1;
                        this.buf[ptr3] = (byte) (128 | ((uc >> 6) & 63));
                        ptr = ptr4 + 1;
                        this.buf[ptr4] = (byte) (128 + (uc & 63));
                        i++;
                    } else {
                        int ptr14 = ptr13 + 1;
                        this.buf[ptr13] = (byte) (224 + (chr >> '\f'));
                        ptr12 = ptr14 + 1;
                        this.buf[ptr14] = (byte) (128 + ((chr >> 6) & 63));
                    }
                } else {
                    ptr12 = ptr13 + 1;
                    this.buf[ptr13] = (byte) (192 + (chr >> 6));
                }
                int i3 = ptr12;
                ptr1 = ptr12 + 1;
                this.buf[i3] = (byte) (128 + (chr & '?'));
            } else {
                byte[] ent = attributeEntities[chr];
                if (ent != null) {
                    if (isAttribute || entities[chr] != null) {
                        ptr1 = writeEntity(ent, ptr13);
                    } else {
                        ptr1 = ptr13 + 1;
                        this.buf[ptr13] = (byte) chr;
                    }
                } else {
                    ptr1 = ptr13 + 1;
                    this.buf[ptr13] = (byte) chr;
                }
            }
            ptr = ptr1;
            i++;
        }
        this.len = ptr;
    }

    private int writeEntity(byte[] entity, int ptr) {
        System.arraycopy(entity, 0, this.buf, ptr, entity.length);
        return ptr + entity.length;
    }

    public final void write(UTF8XmlOutput out) throws IOException {
        out.write(this.buf, 0, this.len);
    }

    public void append(char b) {
        byte[] bArr = this.buf;
        int i = this.len;
        this.len = i + 1;
        bArr[i] = (byte) b;
    }

    public void compact() {
        byte[] b = new byte[this.len];
        System.arraycopy(this.buf, 0, b, 0, this.len);
        this.buf = b;
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r0v3, types: [byte[], byte[][]] */
    static {
        add('&', "&amp;", false);
        add('<', "&lt;", false);
        add('>', "&gt;", false);
        add('\"', "&quot;", true);
        add('\t', "&#x9;", true);
        add('\r', "&#xD;", false);
        add('\n', "&#xA;", true);
    }

    private static void add(char c, String s, boolean attOnly) {
        byte[] image = UTF8XmlOutput.toBytes(s);
        attributeEntities[c] = image;
        if (!attOnly) {
            entities[c] = image;
        }
    }
}
