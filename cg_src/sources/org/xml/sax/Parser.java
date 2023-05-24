package org.xml.sax;

import java.io.IOException;
import java.util.Locale;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/xml/sax/Parser.class */
public interface Parser {
    void setLocale(Locale locale) throws SAXException;

    void setEntityResolver(EntityResolver entityResolver);

    void setDTDHandler(DTDHandler dTDHandler);

    void setDocumentHandler(DocumentHandler documentHandler);

    void setErrorHandler(ErrorHandler errorHandler);

    void parse(InputSource inputSource) throws SAXException, IOException;

    void parse(String str) throws SAXException, IOException;
}
