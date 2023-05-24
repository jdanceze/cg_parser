package com.sun.xml.bind.v2.runtime.unmarshaller;

import com.sun.xml.bind.v2.runtime.output.Pcdata;
import com.sun.xml.bind.v2.runtime.output.UTF8XmlOutput;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/unmarshaller/IntData.class */
public class IntData extends Pcdata {
    private int data;
    private int length;
    private static final int[] sizeTable = {9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, 999999999, Integer.MAX_VALUE};

    public void reset(int i) {
        this.data = i;
        if (i == Integer.MIN_VALUE) {
            this.length = 11;
        } else {
            this.length = i < 0 ? stringSizeOfInt(-i) + 1 : stringSizeOfInt(i);
        }
    }

    private static int stringSizeOfInt(int x) {
        int i = 0;
        while (x > sizeTable[i]) {
            i++;
        }
        return i + 1;
    }

    @Override // com.sun.xml.bind.v2.runtime.output.Pcdata, java.lang.CharSequence
    public String toString() {
        return String.valueOf(this.data);
    }

    @Override // java.lang.CharSequence
    public int length() {
        return this.length;
    }

    @Override // java.lang.CharSequence
    public char charAt(int index) {
        return toString().charAt(index);
    }

    @Override // java.lang.CharSequence
    public CharSequence subSequence(int start, int end) {
        return toString().substring(start, end);
    }

    @Override // com.sun.xml.bind.v2.runtime.output.Pcdata
    public void writeTo(UTF8XmlOutput output) throws IOException {
        output.text(this.data);
    }
}
