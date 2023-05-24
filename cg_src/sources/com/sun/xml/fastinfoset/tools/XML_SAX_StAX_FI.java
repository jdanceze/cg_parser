package com.sun.xml.fastinfoset.tools;

import com.sun.xml.fastinfoset.sax.Properties;
import com.sun.xml.fastinfoset.stax.StAXDocumentSerializer;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/tools/XML_SAX_StAX_FI.class */
public class XML_SAX_StAX_FI extends TransformInputOutput {
    @Override // com.sun.xml.fastinfoset.tools.TransformInputOutput
    public void parse(InputStream xml, OutputStream finf, String workingDirectory) throws Exception {
        StAXDocumentSerializer documentSerializer = new StAXDocumentSerializer();
        documentSerializer.setOutputStream(finf);
        SAX2StAXWriter saxTostax = new SAX2StAXWriter(documentSerializer);
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        saxParserFactory.setNamespaceAware(true);
        SAXParser saxParser = saxParserFactory.newSAXParser();
        XMLReader reader = saxParser.getXMLReader();
        reader.setProperty(Properties.LEXICAL_HANDLER_PROPERTY, saxTostax);
        reader.setContentHandler(saxTostax);
        if (workingDirectory != null) {
            reader.setEntityResolver(createRelativePathResolver(workingDirectory));
        }
        reader.parse(new InputSource(xml));
        xml.close();
        finf.close();
    }

    @Override // com.sun.xml.fastinfoset.tools.TransformInputOutput
    public void parse(InputStream xml, OutputStream finf) throws Exception {
        parse(xml, finf, null);
    }

    public static void main(String[] args) throws Exception {
        XML_SAX_StAX_FI s = new XML_SAX_StAX_FI();
        s.parse(args);
    }
}
