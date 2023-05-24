package org.jvnet.fastinfoset.sax.helpers;

import com.sun.xml.fastinfoset.CommonResourceBundle;
import com.sun.xml.fastinfoset.algorithm.BuiltInEncodingAlgorithmFactory;
import java.io.IOException;
import java.util.Map;
import org.jvnet.fastinfoset.EncodingAlgorithm;
import org.jvnet.fastinfoset.EncodingAlgorithmException;
import org.jvnet.fastinfoset.FastInfosetException;
import org.jvnet.fastinfoset.sax.EncodingAlgorithmAttributes;
import org.xml.sax.Attributes;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:org/jvnet/fastinfoset/sax/helpers/EncodingAlgorithmAttributesImpl.class */
public class EncodingAlgorithmAttributesImpl implements EncodingAlgorithmAttributes {
    private static final int DEFAULT_CAPACITY = 8;
    private static final int URI_OFFSET = 0;
    private static final int LOCALNAME_OFFSET = 1;
    private static final int QNAME_OFFSET = 2;
    private static final int TYPE_OFFSET = 3;
    private static final int VALUE_OFFSET = 4;
    private static final int ALGORITHMURI_OFFSET = 5;
    private static final int SIZE = 6;
    private Map _registeredEncodingAlgorithms;
    private int _length;
    private String[] _data;
    private int[] _algorithmIds;
    private Object[] _algorithmData;
    private String[] _alphabets;
    private boolean[] _toIndex;

    public EncodingAlgorithmAttributesImpl() {
        this(null, null);
    }

    public EncodingAlgorithmAttributesImpl(Attributes attributes) {
        this(null, attributes);
    }

    public EncodingAlgorithmAttributesImpl(Map registeredEncodingAlgorithms, Attributes attributes) {
        this._data = new String[48];
        this._algorithmIds = new int[8];
        this._algorithmData = new Object[8];
        this._alphabets = new String[8];
        this._toIndex = new boolean[8];
        this._registeredEncodingAlgorithms = registeredEncodingAlgorithms;
        if (attributes != null) {
            if (attributes instanceof EncodingAlgorithmAttributes) {
                setAttributes((EncodingAlgorithmAttributes) attributes);
            } else {
                setAttributes(attributes);
            }
        }
    }

    public final void clear() {
        for (int i = 0; i < this._length; i++) {
            this._data[(i * 6) + 4] = null;
            this._algorithmData[i] = null;
        }
        this._length = 0;
    }

    public void addAttribute(String URI, String localName, String qName, String type, String value) {
        if (this._length >= this._algorithmData.length) {
            resize();
        }
        int i = this._length * 6;
        int i2 = i + 1;
        this._data[i] = replaceNull(URI);
        int i3 = i2 + 1;
        this._data[i2] = replaceNull(localName);
        int i4 = i3 + 1;
        this._data[i3] = replaceNull(qName);
        int i5 = i4 + 1;
        this._data[i4] = replaceNull(type);
        int i6 = i5 + 1;
        this._data[i5] = replaceNull(value);
        this._toIndex[this._length] = false;
        this._alphabets[this._length] = null;
        this._length++;
    }

    public void addAttribute(String URI, String localName, String qName, String type, String value, boolean index, String alphabet) {
        if (this._length >= this._algorithmData.length) {
            resize();
        }
        int i = this._length * 6;
        int i2 = i + 1;
        this._data[i] = replaceNull(URI);
        int i3 = i2 + 1;
        this._data[i2] = replaceNull(localName);
        int i4 = i3 + 1;
        this._data[i3] = replaceNull(qName);
        int i5 = i4 + 1;
        this._data[i4] = replaceNull(type);
        int i6 = i5 + 1;
        this._data[i5] = replaceNull(value);
        this._toIndex[this._length] = index;
        this._alphabets[this._length] = alphabet;
        this._length++;
    }

    public void addAttributeWithBuiltInAlgorithmData(String URI, String localName, String qName, int builtInAlgorithmID, Object algorithmData) {
        if (this._length >= this._algorithmData.length) {
            resize();
        }
        int i = this._length * 6;
        int i2 = i + 1;
        this._data[i] = replaceNull(URI);
        int i3 = i2 + 1;
        this._data[i2] = replaceNull(localName);
        int i4 = i3 + 1;
        this._data[i3] = replaceNull(qName);
        int i5 = i4 + 1;
        this._data[i4] = "CDATA";
        int i6 = i5 + 1;
        this._data[i5] = "";
        int i7 = i6 + 1;
        this._data[i6] = null;
        this._algorithmIds[this._length] = builtInAlgorithmID;
        this._algorithmData[this._length] = algorithmData;
        this._toIndex[this._length] = false;
        this._alphabets[this._length] = null;
        this._length++;
    }

    public void addAttributeWithAlgorithmData(String URI, String localName, String qName, String algorithmURI, int algorithmID, Object algorithmData) {
        if (this._length >= this._algorithmData.length) {
            resize();
        }
        int i = this._length * 6;
        int i2 = i + 1;
        this._data[i] = replaceNull(URI);
        int i3 = i2 + 1;
        this._data[i2] = replaceNull(localName);
        int i4 = i3 + 1;
        this._data[i3] = replaceNull(qName);
        int i5 = i4 + 1;
        this._data[i4] = "CDATA";
        int i6 = i5 + 1;
        this._data[i5] = "";
        int i7 = i6 + 1;
        this._data[i6] = algorithmURI;
        this._algorithmIds[this._length] = algorithmID;
        this._algorithmData[this._length] = algorithmData;
        this._toIndex[this._length] = false;
        this._alphabets[this._length] = null;
        this._length++;
    }

    public void replaceWithAttributeAlgorithmData(int index, String algorithmURI, int algorithmID, Object algorithmData) {
        if (index < 0 || index >= this._length) {
            return;
        }
        int i = index * 6;
        this._data[i + 4] = null;
        this._data[i + 5] = algorithmURI;
        this._algorithmIds[index] = algorithmID;
        this._algorithmData[index] = algorithmData;
        this._toIndex[index] = false;
        this._alphabets[index] = null;
    }

    public void setAttributes(Attributes atts) {
        this._length = atts.getLength();
        if (this._length > 0) {
            if (this._length >= this._algorithmData.length) {
                resizeNoCopy();
            }
            int index = 0;
            for (int i = 0; i < this._length; i++) {
                int i2 = index;
                int index2 = index + 1;
                this._data[i2] = atts.getURI(i);
                int index3 = index2 + 1;
                this._data[index2] = atts.getLocalName(i);
                int index4 = index3 + 1;
                this._data[index3] = atts.getQName(i);
                int index5 = index4 + 1;
                this._data[index4] = atts.getType(i);
                this._data[index5] = atts.getValue(i);
                index = index5 + 1 + 1;
                this._toIndex[i] = false;
                this._alphabets[i] = null;
            }
        }
    }

    public void setAttributes(EncodingAlgorithmAttributes atts) {
        this._length = atts.getLength();
        if (this._length > 0) {
            if (this._length >= this._algorithmData.length) {
                resizeNoCopy();
            }
            int index = 0;
            for (int i = 0; i < this._length; i++) {
                int i2 = index;
                int index2 = index + 1;
                this._data[i2] = atts.getURI(i);
                int index3 = index2 + 1;
                this._data[index2] = atts.getLocalName(i);
                int index4 = index3 + 1;
                this._data[index3] = atts.getQName(i);
                int index5 = index4 + 1;
                this._data[index4] = atts.getType(i);
                int index6 = index5 + 1;
                this._data[index5] = atts.getValue(i);
                index = index6 + 1;
                this._data[index6] = atts.getAlgorithmURI(i);
                this._algorithmIds[i] = atts.getAlgorithmIndex(i);
                this._algorithmData[i] = atts.getAlgorithmData(i);
                this._toIndex[i] = false;
                this._alphabets[i] = null;
            }
        }
    }

    @Override // org.xml.sax.Attributes
    public final int getLength() {
        return this._length;
    }

    @Override // org.xml.sax.Attributes
    public final String getLocalName(int index) {
        if (index >= 0 && index < this._length) {
            return this._data[(index * 6) + 1];
        }
        return null;
    }

    @Override // org.xml.sax.Attributes
    public final String getQName(int index) {
        if (index >= 0 && index < this._length) {
            return this._data[(index * 6) + 2];
        }
        return null;
    }

    @Override // org.xml.sax.Attributes
    public final String getType(int index) {
        if (index >= 0 && index < this._length) {
            return this._data[(index * 6) + 3];
        }
        return null;
    }

    @Override // org.xml.sax.Attributes
    public final String getURI(int index) {
        if (index >= 0 && index < this._length) {
            return this._data[(index * 6) + 0];
        }
        return null;
    }

    @Override // org.xml.sax.Attributes
    public final String getValue(int index) {
        if (index >= 0 && index < this._length) {
            String value = this._data[(index * 6) + 4];
            if (value != null) {
                return value;
            }
            if (this._algorithmData[index] == null || this._registeredEncodingAlgorithms == null) {
                return null;
            }
            try {
                String stringBuffer = convertEncodingAlgorithmDataToString(this._algorithmIds[index], this._data[(index * 6) + 5], this._algorithmData[index]).toString();
                this._data[(index * 6) + 4] = stringBuffer;
                return stringBuffer;
            } catch (IOException e) {
                return null;
            } catch (FastInfosetException e2) {
                return null;
            }
        }
        return null;
    }

    @Override // org.xml.sax.Attributes
    public final int getIndex(String qName) {
        for (int index = 0; index < this._length; index++) {
            if (qName.equals(this._data[(index * 6) + 2])) {
                return index;
            }
        }
        return -1;
    }

    @Override // org.xml.sax.Attributes
    public final String getType(String qName) {
        int index = getIndex(qName);
        if (index >= 0) {
            return this._data[(index * 6) + 3];
        }
        return null;
    }

    @Override // org.xml.sax.Attributes
    public final String getValue(String qName) {
        int index = getIndex(qName);
        if (index >= 0) {
            return getValue(index);
        }
        return null;
    }

    @Override // org.xml.sax.Attributes
    public final int getIndex(String uri, String localName) {
        for (int index = 0; index < this._length; index++) {
            if (localName.equals(this._data[(index * 6) + 1]) && uri.equals(this._data[(index * 6) + 0])) {
                return index;
            }
        }
        return -1;
    }

    @Override // org.xml.sax.Attributes
    public final String getType(String uri, String localName) {
        int index = getIndex(uri, localName);
        if (index >= 0) {
            return this._data[(index * 6) + 3];
        }
        return null;
    }

    @Override // org.xml.sax.Attributes
    public final String getValue(String uri, String localName) {
        int index = getIndex(uri, localName);
        if (index >= 0) {
            return getValue(index);
        }
        return null;
    }

    @Override // org.jvnet.fastinfoset.sax.EncodingAlgorithmAttributes
    public final String getAlgorithmURI(int index) {
        if (index >= 0 && index < this._length) {
            return this._data[(index * 6) + 5];
        }
        return null;
    }

    @Override // org.jvnet.fastinfoset.sax.EncodingAlgorithmAttributes
    public final int getAlgorithmIndex(int index) {
        if (index >= 0 && index < this._length) {
            return this._algorithmIds[index];
        }
        return -1;
    }

    @Override // org.jvnet.fastinfoset.sax.EncodingAlgorithmAttributes
    public final Object getAlgorithmData(int index) {
        if (index >= 0 && index < this._length) {
            return this._algorithmData[index];
        }
        return null;
    }

    @Override // org.jvnet.fastinfoset.sax.EncodingAlgorithmAttributes
    public final String getAlpababet(int index) {
        if (index >= 0 && index < this._length) {
            return this._alphabets[index];
        }
        return null;
    }

    @Override // org.jvnet.fastinfoset.sax.EncodingAlgorithmAttributes
    public final boolean getToIndex(int index) {
        if (index >= 0 && index < this._length) {
            return this._toIndex[index];
        }
        return false;
    }

    private final String replaceNull(String s) {
        return s != null ? s : "";
    }

    private final void resizeNoCopy() {
        int newLength = ((this._length * 3) / 2) + 1;
        this._data = new String[newLength * 6];
        this._algorithmIds = new int[newLength];
        this._algorithmData = new Object[newLength];
    }

    private final void resize() {
        int newLength = ((this._length * 3) / 2) + 1;
        String[] data = new String[newLength * 6];
        int[] algorithmIds = new int[newLength];
        Object[] algorithmData = new Object[newLength];
        String[] alphabets = new String[newLength];
        boolean[] toIndex = new boolean[newLength];
        System.arraycopy(this._data, 0, data, 0, this._length * 6);
        System.arraycopy(this._algorithmIds, 0, algorithmIds, 0, this._length);
        System.arraycopy(this._algorithmData, 0, algorithmData, 0, this._length);
        System.arraycopy(this._alphabets, 0, alphabets, 0, this._length);
        System.arraycopy(this._toIndex, 0, toIndex, 0, this._length);
        this._data = data;
        this._algorithmIds = algorithmIds;
        this._algorithmData = algorithmData;
        this._alphabets = alphabets;
        this._toIndex = toIndex;
    }

    private final StringBuffer convertEncodingAlgorithmDataToString(int identifier, String URI, Object data) throws FastInfosetException, IOException {
        EncodingAlgorithm ea;
        if (identifier < 9) {
            ea = BuiltInEncodingAlgorithmFactory.getAlgorithm(identifier);
        } else if (identifier == 9) {
            throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.CDATAAlgorithmNotSupported"));
        } else {
            if (identifier >= 32) {
                if (URI == null) {
                    throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.URINotPresent") + identifier);
                }
                ea = (EncodingAlgorithm) this._registeredEncodingAlgorithms.get(URI);
                if (ea == null) {
                    throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.algorithmNotRegistered") + URI);
                }
            } else {
                throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.identifiers10to31Reserved"));
            }
        }
        StringBuffer sb = new StringBuffer();
        ea.convertToCharacters(data, sb);
        return sb;
    }
}
