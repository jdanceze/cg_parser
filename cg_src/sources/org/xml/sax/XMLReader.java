package org.xml.sax;

import java.io.IOException;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/xml/sax/XMLReader.class */
public interface XMLReader {
    boolean getFeature(String str) throws SAXNotRecognizedException, SAXNotSupportedException;

    void setFeature(String str, boolean z) throws SAXNotRecognizedException, SAXNotSupportedException;

    Object getProperty(String str) throws SAXNotRecognizedException, SAXNotSupportedException;

    void setProperty(String str, Object obj) throws SAXNotRecognizedException, SAXNotSupportedException;

    void setEntityResolver(EntityResolver entityResolver);

    EntityResolver getEntityResolver();

    void setDTDHandler(DTDHandler dTDHandler);

    DTDHandler getDTDHandler();

    void setContentHandler(ContentHandler contentHandler);

    ContentHandler getContentHandler();

    void setErrorHandler(ErrorHandler errorHandler);

    ErrorHandler getErrorHandler();

    void parse(InputSource inputSource) throws IOException, SAXException;

    void parse(String str) throws IOException, SAXException;
}
