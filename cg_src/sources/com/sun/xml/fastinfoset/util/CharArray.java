package com.sun.xml.fastinfoset.util;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/util/CharArray.class */
public class CharArray implements CharSequence {
    public char[] ch;
    public int start;
    public int length;
    protected int _hash;

    /* JADX INFO: Access modifiers changed from: protected */
    public CharArray() {
    }

    public CharArray(char[] _ch, int _start, int _length, boolean copy) {
        set(_ch, _start, _length, copy);
    }

    public final void set(char[] _ch, int _start, int _length, boolean copy) {
        if (copy) {
            this.ch = new char[_length];
            this.start = 0;
            this.length = _length;
            System.arraycopy(_ch, _start, this.ch, 0, _length);
        } else {
            this.ch = _ch;
            this.start = _start;
            this.length = _length;
        }
        this._hash = 0;
    }

    public final void cloneArray() {
        char[] _ch = new char[this.length];
        System.arraycopy(this.ch, this.start, _ch, 0, this.length);
        this.ch = _ch;
        this.start = 0;
    }

    @Override // java.lang.CharSequence
    public String toString() {
        return new String(this.ch, this.start, this.length);
    }

    public int hashCode() {
        if (this._hash == 0) {
            for (int i = this.start; i < this.start + this.length; i++) {
                this._hash = (31 * this._hash) + this.ch[i];
            }
        }
        return this._hash;
    }

    public static final int hashCode(char[] ch, int start, int length) {
        int hash = 0;
        for (int i = start; i < start + length; i++) {
            hash = (31 * hash) + ch[i];
        }
        return hash;
    }

    public final boolean equalsCharArray(CharArray cha) {
        int i;
        int i2;
        if (this == cha) {
            return true;
        }
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
    }

    public final boolean equalsCharArray(char[] ch, int start, int length) {
        int i;
        int i2;
        if (this.length == length) {
            int n = this.length;
            int i3 = this.start;
            int j = start;
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
            } while (this.ch[i] == ch[i2]);
            return false;
        }
        return false;
    }

    public boolean equals(Object obj) {
        int i;
        int i2;
        if (this == obj) {
            return true;
        }
        if (obj instanceof CharArray) {
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
        }
        return false;
    }

    @Override // java.lang.CharSequence
    public final int length() {
        return this.length;
    }

    @Override // java.lang.CharSequence
    public final char charAt(int index) {
        return this.ch[this.start + index];
    }

    @Override // java.lang.CharSequence
    public final CharSequence subSequence(int start, int end) {
        return new CharArray(this.ch, this.start + start, end - start, false);
    }
}
