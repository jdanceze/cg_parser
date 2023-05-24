package org.jvnet.fastinfoset.sax;

import org.xml.sax.SAXException;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:org/jvnet/fastinfoset/sax/RestrictedAlphabetContentHandler.class */
public interface RestrictedAlphabetContentHandler {
    void numericCharacters(char[] cArr, int i, int i2) throws SAXException;

    void dateTimeCharacters(char[] cArr, int i, int i2) throws SAXException;

    void alphabetCharacters(String str, char[] cArr, int i, int i2) throws SAXException;
}
