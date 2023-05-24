package org.jvnet.fastinfoset.sax.helpers;

import org.jvnet.fastinfoset.sax.EncodingAlgorithmContentHandler;
import org.jvnet.fastinfoset.sax.PrimitiveTypeContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:org/jvnet/fastinfoset/sax/helpers/FastInfosetDefaultHandler.class */
public class FastInfosetDefaultHandler extends DefaultHandler implements LexicalHandler, EncodingAlgorithmContentHandler, PrimitiveTypeContentHandler {
    @Override // org.xml.sax.ext.LexicalHandler
    public void comment(char[] ch, int start, int length) throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startCDATA() throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endCDATA() throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startDTD(String name, String publicId, String systemId) throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endDTD() throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startEntity(String name) throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endEntity(String name) throws SAXException {
    }

    @Override // org.jvnet.fastinfoset.sax.EncodingAlgorithmContentHandler
    public void octets(String URI, int algorithm, byte[] b, int start, int length) throws SAXException {
    }

    @Override // org.jvnet.fastinfoset.sax.EncodingAlgorithmContentHandler
    public void object(String URI, int algorithm, Object o) throws SAXException {
    }

    @Override // org.jvnet.fastinfoset.sax.PrimitiveTypeContentHandler
    public void booleans(boolean[] b, int start, int length) throws SAXException {
    }

    @Override // org.jvnet.fastinfoset.sax.PrimitiveTypeContentHandler
    public void bytes(byte[] b, int start, int length) throws SAXException {
    }

    @Override // org.jvnet.fastinfoset.sax.PrimitiveTypeContentHandler
    public void shorts(short[] s, int start, int length) throws SAXException {
    }

    @Override // org.jvnet.fastinfoset.sax.PrimitiveTypeContentHandler
    public void ints(int[] i, int start, int length) throws SAXException {
    }

    @Override // org.jvnet.fastinfoset.sax.PrimitiveTypeContentHandler
    public void longs(long[] l, int start, int length) throws SAXException {
    }

    @Override // org.jvnet.fastinfoset.sax.PrimitiveTypeContentHandler
    public void floats(float[] f, int start, int length) throws SAXException {
    }

    @Override // org.jvnet.fastinfoset.sax.PrimitiveTypeContentHandler
    public void doubles(double[] d, int start, int length) throws SAXException {
    }

    @Override // org.jvnet.fastinfoset.sax.PrimitiveTypeContentHandler
    public void uuids(long[] msblsb, int start, int length) throws SAXException {
    }
}
