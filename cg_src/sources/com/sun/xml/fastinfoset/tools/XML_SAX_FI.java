package com.sun.xml.fastinfoset.tools;

import com.sun.xml.fastinfoset.sax.Properties;
import com.sun.xml.fastinfoset.sax.SAXDocumentSerializer;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/tools/XML_SAX_FI.class */
public class XML_SAX_FI extends TransformInputOutput {
    @Override // com.sun.xml.fastinfoset.tools.TransformInputOutput
    public void parse(InputStream xml, OutputStream finf, String workingDirectory) throws Exception {
        SAXParser saxParser = getParser();
        SAXDocumentSerializer documentSerializer = getSerializer(finf);
        XMLReader reader = saxParser.getXMLReader();
        reader.setProperty(Properties.LEXICAL_HANDLER_PROPERTY, documentSerializer);
        reader.setContentHandler(documentSerializer);
        if (workingDirectory != null) {
            reader.setEntityResolver(createRelativePathResolver(workingDirectory));
        }
        reader.parse(new InputSource(xml));
    }

    @Override // com.sun.xml.fastinfoset.tools.TransformInputOutput
    public void parse(InputStream xml, OutputStream finf) throws Exception {
        parse(xml, finf, null);
    }

    public void convert(Reader reader, OutputStream finf) throws Exception {
        InputSource is = new InputSource(reader);
        SAXParser saxParser = getParser();
        SAXDocumentSerializer documentSerializer = getSerializer(finf);
        saxParser.setProperty(Properties.LEXICAL_HANDLER_PROPERTY, documentSerializer);
        saxParser.parse(is, documentSerializer);
    }

    private SAXParser getParser() {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        saxParserFactory.setNamespaceAware(true);
        try {
            return saxParserFactory.newSAXParser();
        } catch (Exception e) {
            return null;
        }
    }

    private SAXDocumentSerializer getSerializer(OutputStream finf) {
        SAXDocumentSerializer documentSerializer = new SAXDocumentSerializer();
        documentSerializer.setOutputStream(finf);
        return documentSerializer;
    }

    public static void main(String[] args) throws Exception {
        XML_SAX_FI s = new XML_SAX_FI();
        s.parse(args);
    }
}
