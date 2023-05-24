package com.sun.xml.fastinfoset.tools;

import com.sun.xml.fastinfoset.Decoder;
import com.sun.xml.fastinfoset.sax.Properties;
import com.sun.xml.fastinfoset.stax.StAXDocumentParser;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/tools/FI_StAX_SAX_Or_XML_SAX_SAXEvent.class */
public class FI_StAX_SAX_Or_XML_SAX_SAXEvent extends TransformInputOutput {
    @Override // com.sun.xml.fastinfoset.tools.TransformInputOutput
    public void parse(InputStream document, OutputStream events) throws Exception {
        if (!document.markSupported()) {
            document = new BufferedInputStream(document);
        }
        document.mark(4);
        boolean isFastInfosetDocument = Decoder.isFastInfosetDocument(document);
        document.reset();
        if (isFastInfosetDocument) {
            StAXDocumentParser parser = new StAXDocumentParser();
            parser.setInputStream(document);
            SAXEventSerializer ses = new SAXEventSerializer(events);
            StAX2SAXReader reader = new StAX2SAXReader(parser, ses);
            reader.setLexicalHandler(ses);
            reader.adapt();
            return;
        }
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        parserFactory.setNamespaceAware(true);
        SAXParser parser2 = parserFactory.newSAXParser();
        SAXEventSerializer ses2 = new SAXEventSerializer(events);
        parser2.setProperty(Properties.LEXICAL_HANDLER_PROPERTY, ses2);
        parser2.parse(document, ses2);
    }

    public static void main(String[] args) throws Exception {
        FI_StAX_SAX_Or_XML_SAX_SAXEvent p = new FI_StAX_SAX_Or_XML_SAX_SAXEvent();
        p.parse(args);
    }
}
