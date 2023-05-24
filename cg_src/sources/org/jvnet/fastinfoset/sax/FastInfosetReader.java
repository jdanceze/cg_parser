package org.jvnet.fastinfoset.sax;

import java.io.IOException;
import java.io.InputStream;
import org.jvnet.fastinfoset.FastInfosetException;
import org.jvnet.fastinfoset.FastInfosetParser;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:org/jvnet/fastinfoset/sax/FastInfosetReader.class */
public interface FastInfosetReader extends XMLReader, FastInfosetParser {
    public static final String ENCODING_ALGORITHM_CONTENT_HANDLER_PROPERTY = "http://jvnet.org/fastinfoset/sax/properties/encoding-algorithm-content-handler";
    public static final String PRIMITIVE_TYPE_CONTENT_HANDLER_PROPERTY = "http://jvnet.org/fastinfoset/sax/properties/primitive-type-content-handler";

    void parse(InputStream inputStream) throws IOException, FastInfosetException, SAXException;

    void setLexicalHandler(LexicalHandler lexicalHandler);

    LexicalHandler getLexicalHandler();

    void setDeclHandler(DeclHandler declHandler);

    DeclHandler getDeclHandler();

    void setEncodingAlgorithmContentHandler(EncodingAlgorithmContentHandler encodingAlgorithmContentHandler);

    EncodingAlgorithmContentHandler getEncodingAlgorithmContentHandler();

    void setPrimitiveTypeContentHandler(PrimitiveTypeContentHandler primitiveTypeContentHandler);

    PrimitiveTypeContentHandler getPrimitiveTypeContentHandler();
}
