package com.sun.xml.fastinfoset.util;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/util/CharArrayString.class */
public class CharArrayString extends CharArray {
    protected String _s;

    public CharArrayString(String s) {
        this(s, true);
    }

    public CharArrayString(String s, boolean createArray) {
        this._s = s;
        if (createArray) {
            this.ch = this._s.toCharArray();
            this.start = 0;
            this.length = this.ch.length;
        }
    }

    @Override // com.sun.xml.fastinfoset.util.CharArray, java.lang.CharSequence
    public String toString() {
        return this._s;
    }

    @Override // com.sun.xml.fastinfoset.util.CharArray
    public int hashCode() {
        return this._s.hashCode();
    }

    @Override // com.sun.xml.fastinfoset.util.CharArray
    public boolean equals(Object obj) {
        int i;
        int i2;
        if (this == obj) {
            return true;
        }
        if (obj instanceof CharArrayString) {
            CharArrayString chas = (CharArrayString) obj;
            return this._s.equals(chas._s);
        } else if (obj instanceof CharArray) {
            CharArray cha = (CharArray) obj;
            if (this.length == cha.length) {
                int n = this.length;
                int i3 = this.start;
                int j = cha.start;
                do {
                    int i4 = n;
                    n--;
                    if (i4 == 0) {
                        return true;
                    }
                    i = i3;
                    i3++;
                    i2 = j;
                    j++;
                } while (this.ch[i] == cha.ch[i2]);
                return false;
            }
            return false;
        } else {
            return false;
        }
    }
}
