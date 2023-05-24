package org.jvnet.fastinfoset.sax;

import org.xml.sax.SAXException;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:org/jvnet/fastinfoset/sax/PrimitiveTypeContentHandler.class */
public interface PrimitiveTypeContentHandler {
    void booleans(boolean[] zArr, int i, int i2) throws SAXException;

    void bytes(byte[] bArr, int i, int i2) throws SAXException;

    void shorts(short[] sArr, int i, int i2) throws SAXException;

    void ints(int[] iArr, int i, int i2) throws SAXException;

    void longs(long[] jArr, int i, int i2) throws SAXException;

    void floats(float[] fArr, int i, int i2) throws SAXException;

    void doubles(double[] dArr, int i, int i2) throws SAXException;

    void uuids(long[] jArr, int i, int i2) throws SAXException;
}
