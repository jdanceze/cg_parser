package org.jvnet.fastinfoset.sax;

import org.jvnet.fastinfoset.FastInfosetSerializer;
import org.xml.sax.ContentHandler;
import org.xml.sax.ext.LexicalHandler;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:org/jvnet/fastinfoset/sax/FastInfosetWriter.class */
public interface FastInfosetWriter extends ContentHandler, LexicalHandler, EncodingAlgorithmContentHandler, PrimitiveTypeContentHandler, RestrictedAlphabetContentHandler, ExtendedContentHandler, FastInfosetSerializer {
}
