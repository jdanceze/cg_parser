package org.jvnet.fastinfoset.sax;

import org.xml.sax.SAXException;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:org/jvnet/fastinfoset/sax/EncodingAlgorithmContentHandler.class */
public interface EncodingAlgorithmContentHandler {
    void octets(String str, int i, byte[] bArr, int i2, int i3) throws SAXException;

    void object(String str, int i, Object obj) throws SAXException;
}
