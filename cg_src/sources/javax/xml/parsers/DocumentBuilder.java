package javax.xml.parsers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:javax/xml/parsers/DocumentBuilder.class */
public abstract class DocumentBuilder {
    private static final boolean DEBUG = false;

    protected DocumentBuilder() {
    }

    public Document parse(InputStream inputStream) throws SAXException, IOException {
        if (inputStream == null) {
            throw new IllegalArgumentException("InputStream cannot be null");
        }
        return parse(new InputSource(inputStream));
    }

    public Document parse(InputStream inputStream, String str) throws SAXException, IOException {
        if (inputStream == null) {
            throw new IllegalArgumentException("InputStream cannot be null");
        }
        InputSource inputSource = new InputSource(inputStream);
        inputSource.setSystemId(str);
        return parse(inputSource);
    }

    public Document parse(String str) throws SAXException, IOException {
        if (str == null) {
            throw new IllegalArgumentException("URI cannot be null");
        }
        return parse(new InputSource(str));
    }

    public Document parse(File file) throws SAXException, IOException {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }
        return parse(new InputSource(FilePathToURI.filepath2URI(file.getAbsolutePath())));
    }

    public abstract Document parse(InputSource inputSource) throws SAXException, IOException;

    public abstract boolean isNamespaceAware();

    public abstract boolean isValidating();

    public abstract void setEntityResolver(EntityResolver entityResolver);

    public abstract void setErrorHandler(ErrorHandler errorHandler);

    public abstract Document newDocument();

    public abstract DOMImplementation getDOMImplementation();
}
