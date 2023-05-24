package org.xml.sax.helpers;

import org.xml.sax.Attributes;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/xml/sax/helpers/AttributesImpl.class */
public class AttributesImpl implements Attributes {
    int length;
    String[] data;

    public AttributesImpl() {
        this.length = 0;
        this.data = null;
    }

    public AttributesImpl(Attributes attributes) {
        setAttributes(attributes);
    }

    @Override // org.xml.sax.Attributes
    public int getLength() {
        return this.length;
    }

    @Override // org.xml.sax.Attributes
    public String getURI(int i) {
        if (i < 0 || i >= this.length) {
            return null;
        }
        return this.data[i * 5];
    }

    @Override // org.xml.sax.Attributes
    public String getLocalName(int i) {
        if (i < 0 || i >= this.length) {
            return null;
        }
        return this.data[(i * 5) + 1];
    }

    @Override // org.xml.sax.Attributes
    public String getQName(int i) {
        if (i < 0 || i >= this.length) {
            return null;
        }
        return this.data[(i * 5) + 2];
    }

    @Override // org.xml.sax.Attributes
    public String getType(int i) {
        if (i < 0 || i >= this.length) {
            return null;
        }
        return this.data[(i * 5) + 3];
    }

    @Override // org.xml.sax.Attributes
    public String getValue(int i) {
        if (i < 0 || i >= this.length) {
            return null;
        }
        return this.data[(i * 5) + 4];
    }

    @Override // org.xml.sax.Attributes
    public int getIndex(String str, String str2) {
        int i = this.length * 5;
        for (int i2 = 0; i2 < i; i2 += 5) {
            if (this.data[i2].equals(str) && this.data[i2 + 1].equals(str2)) {
                return i2 / 5;
            }
        }
        return -1;
    }

    @Override // org.xml.sax.Attributes
    public int getIndex(String str) {
        int i = this.length * 5;
        for (int i2 = 0; i2 < i; i2 += 5) {
            if (this.data[i2 + 2].equals(str)) {
                return i2 / 5;
            }
        }
        return -1;
    }

    @Override // org.xml.sax.Attributes
    public String getType(String str, String str2) {
        int i = this.length * 5;
        for (int i2 = 0; i2 < i; i2 += 5) {
            if (this.data[i2].equals(str) && this.data[i2 + 1].equals(str2)) {
                return this.data[i2 + 3];
            }
        }
        return null;
    }

    @Override // org.xml.sax.Attributes
    public String getType(String str) {
        int i = this.length * 5;
        for (int i2 = 0; i2 < i; i2 += 5) {
            if (this.data[i2 + 2].equals(str)) {
                return this.data[i2 + 3];
            }
        }
        return null;
    }

    @Override // org.xml.sax.Attributes
    public String getValue(String str, String str2) {
        int i = this.length * 5;
        for (int i2 = 0; i2 < i; i2 += 5) {
            if (this.data[i2].equals(str) && this.data[i2 + 1].equals(str2)) {
                return this.data[i2 + 4];
            }
        }
        return null;
    }

    @Override // org.xml.sax.Attributes
    public String getValue(String str) {
        int i = this.length * 5;
        for (int i2 = 0; i2 < i; i2 += 5) {
            if (this.data[i2 + 2].equals(str)) {
                return this.data[i2 + 4];
            }
        }
        return null;
    }

    public void clear() {
        if (this.data != null) {
            for (int i = 0; i < this.length * 5; i++) {
                this.data[i] = null;
            }
        }
        this.length = 0;
    }

    public void setAttributes(Attributes attributes) {
        clear();
        this.length = attributes.getLength();
        if (this.length > 0) {
            this.data = new String[this.length * 5];
            for (int i = 0; i < this.length; i++) {
                this.data[i * 5] = attributes.getURI(i);
                this.data[(i * 5) + 1] = attributes.getLocalName(i);
                this.data[(i * 5) + 2] = attributes.getQName(i);
                this.data[(i * 5) + 3] = attributes.getType(i);
                this.data[(i * 5) + 4] = attributes.getValue(i);
            }
        }
    }

    public void addAttribute(String str, String str2, String str3, String str4, String str5) {
        ensureCapacity(this.length + 1);
        this.data[this.length * 5] = str;
        this.data[(this.length * 5) + 1] = str2;
        this.data[(this.length * 5) + 2] = str3;
        this.data[(this.length * 5) + 3] = str4;
        this.data[(this.length * 5) + 4] = str5;
        this.length++;
    }

    public void setAttribute(int i, String str, String str2, String str3, String str4, String str5) {
        if (i < 0 || i >= this.length) {
            badIndex(i);
            return;
        }
        this.data[i * 5] = str;
        this.data[(i * 5) + 1] = str2;
        this.data[(i * 5) + 2] = str3;
        this.data[(i * 5) + 3] = str4;
        this.data[(i * 5) + 4] = str5;
    }

    public void removeAttribute(int i) {
        if (i < 0 || i >= this.length) {
            badIndex(i);
            return;
        }
        if (i < this.length - 1) {
            System.arraycopy(this.data, (i + 1) * 5, this.data, i * 5, ((this.length - i) - 1) * 5);
        }
        int i2 = (this.length - 1) * 5;
        int i3 = i2 + 1;
        this.data[i2] = null;
        int i4 = i3 + 1;
        this.data[i3] = null;
        int i5 = i4 + 1;
        this.data[i4] = null;
        this.data[i5] = null;
        this.data[i5 + 1] = null;
        this.length--;
    }

    public void setURI(int i, String str) {
        if (i < 0 || i >= this.length) {
            badIndex(i);
        } else {
            this.data[i * 5] = str;
        }
    }

    public void setLocalName(int i, String str) {
        if (i < 0 || i >= this.length) {
            badIndex(i);
        } else {
            this.data[(i * 5) + 1] = str;
        }
    }

    public void setQName(int i, String str) {
        if (i < 0 || i >= this.length) {
            badIndex(i);
        } else {
            this.data[(i * 5) + 2] = str;
        }
    }

    public void setType(int i, String str) {
        if (i < 0 || i >= this.length) {
            badIndex(i);
        } else {
            this.data[(i * 5) + 3] = str;
        }
    }

    public void setValue(int i, String str) {
        if (i < 0 || i >= this.length) {
            badIndex(i);
        } else {
            this.data[(i * 5) + 4] = str;
        }
    }

    private void ensureCapacity(int i) {
        int i2;
        int i3;
        if (i <= 0) {
            return;
        }
        if (this.data == null || this.data.length == 0) {
            i2 = 25;
        } else if (this.data.length >= i * 5) {
            return;
        } else {
            i2 = this.data.length;
        }
        while (true) {
            i3 = i2;
            if (i3 >= i * 5) {
                break;
            }
            i2 = i3 * 2;
        }
        String[] strArr = new String[i3];
        if (this.length > 0) {
            System.arraycopy(this.data, 0, strArr, 0, this.length * 5);
        }
        this.data = strArr;
    }

    private void badIndex(int i) throws ArrayIndexOutOfBoundsException {
        throw new ArrayIndexOutOfBoundsException(new StringBuffer().append("Attempt to modify attribute at illegal index: ").append(i).toString());
    }
}
