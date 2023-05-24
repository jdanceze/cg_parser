package com.sun.xml.bind.v2.runtime.unmarshaller;

import com.sun.xml.bind.v2.runtime.output.Pcdata;
import com.sun.xml.bind.v2.runtime.output.UTF8XmlOutput;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/unmarshaller/IntArrayData.class */
public final class IntArrayData extends Pcdata {
    private int[] data;
    private int start;
    private int len;
    private StringBuilder literal;

    public IntArrayData(int[] data, int start, int len) {
        set(data, start, len);
    }

    public IntArrayData() {
    }

    public void set(int[] data, int start, int len) {
        this.data = data;
        this.start = start;
        this.len = len;
        this.literal = null;
    }

    @Override // java.lang.CharSequence
    public int length() {
        return getLiteral().length();
    }

    @Override // java.lang.CharSequence
    public char charAt(int index) {
        return getLiteral().charAt(index);
    }

    @Override // java.lang.CharSequence
    public CharSequence subSequence(int start, int end) {
        return getLiteral().subSequence(start, end);
    }

    private StringBuilder getLiteral() {
        if (this.literal != null) {
            return this.literal;
        }
        this.literal = new StringBuilder();
        int p = this.start;
        for (int i = this.len; i > 0; i--) {
            if (this.literal.length() > 0) {
                this.literal.append(' ');
            }
            int i2 = p;
            p++;
            this.literal.append(this.data[i2]);
        }
        return this.literal;
    }

    @Override // com.sun.xml.bind.v2.runtime.output.Pcdata, java.lang.CharSequence
    public String toString() {
        return this.literal.toString();
    }

    @Override // com.sun.xml.bind.v2.runtime.output.Pcdata
    public void writeTo(UTF8XmlOutput output) throws IOException {
        int p = this.start;
        for (int i = this.len; i > 0; i--) {
            if (i != this.len) {
                output.write(32);
            }
            int i2 = p;
            p++;
            output.text(this.data[i2]);
        }
    }
}
