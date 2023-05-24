package javax.xml.parsers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.xml.sax.HandlerBase;
import org.xml.sax.InputSource;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:javax/xml/parsers/SAXParser.class */
public abstract class SAXParser {
    private static final boolean DEBUG = false;

    protected SAXParser() {
    }

    public void parse(InputStream inputStream, HandlerBase handlerBase) throws SAXException, IOException {
        if (inputStream == null) {
            throw new IllegalArgumentException("InputStream cannot be null");
        }
        parse(new InputSource(inputStream), handlerBase);
    }

    public void parse(InputStream inputStream, HandlerBase handlerBase, String str) throws SAXException, IOException {
        if (inputStream == null) {
            throw new IllegalArgumentException("InputStream cannot be null");
        }
        InputSource inputSource = new InputSource(inputStream);
        inputSource.setSystemId(str);
        parse(inputSource, handlerBase);
    }

    public void parse(InputStream inputStream, DefaultHandler defaultHandler) throws SAXException, IOException {
        if (inputStream == null) {
            throw new IllegalArgumentException("InputStream cannot be null");
        }
        parse(new InputSource(inputStream), defaultHandler);
    }

    public void parse(InputStream inputStream, DefaultHandler defaultHandler, String str) throws SAXException, IOException {
        if (inputStream == null) {
            throw new IllegalArgumentException("InputStream cannot be null");
        }
        InputSource inputSource = new InputSource(inputStream);
        inputSource.setSystemId(str);
        parse(inputSource, defaultHandler);
    }

    public void parse(String str, HandlerBase handlerBase) throws SAXException, IOException {
        if (str == null) {
            throw new IllegalArgumentException("uri cannot be null");
        }
        parse(new InputSource(str), handlerBase);
    }

    public void parse(String str, DefaultHandler defaultHandler) throws SAXException, IOException {
        if (str == null) {
            throw new IllegalArgumentException("uri cannot be null");
        }
        parse(new InputSource(str), defaultHandler);
    }

    public void parse(File file, HandlerBase handlerBase) throws SAXException, IOException {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }
        parse(new InputSource(FilePathToURI.filepath2URI(file.getAbsolutePath())), handlerBase);
    }

    public void parse(File file, DefaultHandler defaultHandler) throws SAXException, IOException {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }
        parse(new InputSource(FilePathToURI.filepath2URI(file.getAbsolutePath())), defaultHandler);
    }

    public void parse(InputSource inputSource, HandlerBase handlerBase) throws SAXException, IOException {
        if (inputSource == null) {
            throw new IllegalArgumentException("InputSource cannot be null");
        }
        Parser parser = getParser();
        if (handlerBase != null) {
            parser.setDocumentHandler(handlerBase);
            parser.setEntityResolver(handlerBase);
            parser.setErrorHandler(handlerBase);
            parser.setDTDHandler(handlerBase);
        }
        parser.parse(inputSource);
    }

    public void parse(InputSource inputSource, DefaultHandler defaultHandler) throws SAXException, IOException {
        if (inputSource == null) {
            throw new IllegalArgumentException("InputSource cannot be null");
        }
        XMLReader xMLReader = getXMLReader();
        if (defaultHandler != null) {
            xMLReader.setContentHandler(defaultHandler);
            xMLReader.setEntityResolver(defaultHandler);
            xMLReader.setErrorHandler(defaultHandler);
            xMLReader.setDTDHandler(defaultHandler);
        }
        xMLReader.parse(inputSource);
    }

    public abstract Parser getParser() throws SAXException;

    public abstract XMLReader getXMLReader() throws SAXException;

    public abstract boolean isNamespaceAware();

    public abstract boolean isValidating();

    public abstract void setProperty(String str, Object obj) throws SAXNotRecognizedException, SAXNotSupportedException;

    public abstract Object getProperty(String str) throws SAXNotRecognizedException, SAXNotSupportedException;
}
