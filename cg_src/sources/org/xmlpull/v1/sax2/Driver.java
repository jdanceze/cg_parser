package org.xmlpull.v1.sax2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
/* loaded from: gencallgraphv3.jar:xpp3-1.1.4c.jar:org/xmlpull/v1/sax2/Driver.class */
public class Driver implements Locator, XMLReader, Attributes {
    protected static final String DECLARATION_HANDLER_PROPERTY = "http://xml.org/sax/properties/declaration-handler";
    protected static final String LEXICAL_HANDLER_PROPERTY = "http://xml.org/sax/properties/lexical-handler";
    protected static final String NAMESPACES_FEATURE = "http://xml.org/sax/features/namespaces";
    protected static final String NAMESPACE_PREFIXES_FEATURE = "http://xml.org/sax/features/namespace-prefixes";
    protected static final String VALIDATION_FEATURE = "http://xml.org/sax/features/validation";
    protected static final String APACHE_SCHEMA_VALIDATION_FEATURE = "http://apache.org/xml/features/validation/schema";
    protected static final String APACHE_DYNAMIC_VALIDATION_FEATURE = "http://apache.org/xml/features/validation/dynamic";
    protected ContentHandler contentHandler = new DefaultHandler();
    protected ErrorHandler errorHandler = new DefaultHandler();
    protected String systemId;
    protected XmlPullParser pp;

    public Driver() throws XmlPullParserException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        this.pp = factory.newPullParser();
    }

    public Driver(XmlPullParser pp) throws XmlPullParserException {
        this.pp = pp;
    }

    @Override // org.xml.sax.Attributes
    public int getLength() {
        return this.pp.getAttributeCount();
    }

    @Override // org.xml.sax.Attributes
    public String getURI(int index) {
        return this.pp.getAttributeNamespace(index);
    }

    @Override // org.xml.sax.Attributes
    public String getLocalName(int index) {
        return this.pp.getAttributeName(index);
    }

    @Override // org.xml.sax.Attributes
    public String getQName(int index) {
        String prefix = this.pp.getAttributePrefix(index);
        if (prefix != null) {
            return new StringBuffer().append(prefix).append(':').append(this.pp.getAttributeName(index)).toString();
        }
        return this.pp.getAttributeName(index);
    }

    @Override // org.xml.sax.Attributes
    public String getType(int index) {
        return this.pp.getAttributeType(index);
    }

    @Override // org.xml.sax.Attributes
    public String getValue(int index) {
        return this.pp.getAttributeValue(index);
    }

    @Override // org.xml.sax.Attributes
    public int getIndex(String uri, String localName) {
        for (int i = 0; i < this.pp.getAttributeCount(); i++) {
            if (this.pp.getAttributeNamespace(i).equals(uri) && this.pp.getAttributeName(i).equals(localName)) {
                return i;
            }
        }
        return -1;
    }

    @Override // org.xml.sax.Attributes
    public int getIndex(String qName) {
        for (int i = 0; i < this.pp.getAttributeCount(); i++) {
            if (this.pp.getAttributeName(i).equals(qName)) {
                return i;
            }
        }
        return -1;
    }

    @Override // org.xml.sax.Attributes
    public String getType(String uri, String localName) {
        for (int i = 0; i < this.pp.getAttributeCount(); i++) {
            if (this.pp.getAttributeNamespace(i).equals(uri) && this.pp.getAttributeName(i).equals(localName)) {
                return this.pp.getAttributeType(i);
            }
        }
        return null;
    }

    @Override // org.xml.sax.Attributes
    public String getType(String qName) {
        for (int i = 0; i < this.pp.getAttributeCount(); i++) {
            if (this.pp.getAttributeName(i).equals(qName)) {
                return this.pp.getAttributeType(i);
            }
        }
        return null;
    }

    @Override // org.xml.sax.Attributes
    public String getValue(String uri, String localName) {
        return this.pp.getAttributeValue(uri, localName);
    }

    @Override // org.xml.sax.Attributes
    public String getValue(String qName) {
        return this.pp.getAttributeValue(null, qName);
    }

    @Override // org.xml.sax.Locator
    public String getPublicId() {
        return null;
    }

    @Override // org.xml.sax.Locator
    public String getSystemId() {
        return this.systemId;
    }

    @Override // org.xml.sax.Locator
    public int getLineNumber() {
        return this.pp.getLineNumber();
    }

    @Override // org.xml.sax.Locator
    public int getColumnNumber() {
        return this.pp.getColumnNumber();
    }

    @Override // org.xml.sax.XMLReader
    public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        if ("http://xml.org/sax/features/namespaces".equals(name)) {
            return this.pp.getFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces");
        }
        if ("http://xml.org/sax/features/namespace-prefixes".equals(name)) {
            return this.pp.getFeature("http://xmlpull.org/v1/doc/features.html#report-namespace-prefixes");
        }
        if ("http://xml.org/sax/features/validation".equals(name)) {
            return this.pp.getFeature("http://xmlpull.org/v1/doc/features.html#validation");
        }
        return this.pp.getFeature(name);
    }

    @Override // org.xml.sax.XMLReader
    public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
        try {
            if ("http://xml.org/sax/features/namespaces".equals(name)) {
                this.pp.setFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces", value);
            } else if ("http://xml.org/sax/features/namespace-prefixes".equals(name)) {
                if (this.pp.getFeature("http://xmlpull.org/v1/doc/features.html#report-namespace-prefixes") != value) {
                    this.pp.setFeature("http://xmlpull.org/v1/doc/features.html#report-namespace-prefixes", value);
                }
            } else if ("http://xml.org/sax/features/validation".equals(name)) {
                this.pp.setFeature("http://xmlpull.org/v1/doc/features.html#validation", value);
            } else {
                this.pp.setFeature(name, value);
            }
        } catch (XmlPullParserException ex) {
            throw new SAXNotSupportedException(new StringBuffer().append("problem with setting feature ").append(name).append(": ").append(ex).toString());
        }
    }

    @Override // org.xml.sax.XMLReader
    public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        if ("http://xml.org/sax/properties/declaration-handler".equals(name) || "http://xml.org/sax/properties/lexical-handler".equals(name)) {
            return null;
        }
        return this.pp.getProperty(name);
    }

    @Override // org.xml.sax.XMLReader
    public void setProperty(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException {
        if ("http://xml.org/sax/properties/declaration-handler".equals(name)) {
            throw new SAXNotSupportedException(new StringBuffer().append("not supported setting property ").append(name).toString());
        }
        if ("http://xml.org/sax/properties/lexical-handler".equals(name)) {
            throw new SAXNotSupportedException(new StringBuffer().append("not supported setting property ").append(name).toString());
        }
        try {
            this.pp.setProperty(name, value);
        } catch (XmlPullParserException ex) {
            throw new SAXNotSupportedException(new StringBuffer().append("not supported set property ").append(name).append(": ").append(ex).toString());
        }
    }

    @Override // org.xml.sax.XMLReader
    public void setEntityResolver(EntityResolver resolver) {
    }

    @Override // org.xml.sax.XMLReader
    public EntityResolver getEntityResolver() {
        return null;
    }

    @Override // org.xml.sax.XMLReader
    public void setDTDHandler(DTDHandler handler) {
    }

    @Override // org.xml.sax.XMLReader
    public DTDHandler getDTDHandler() {
        return null;
    }

    @Override // org.xml.sax.XMLReader
    public void setContentHandler(ContentHandler handler) {
        this.contentHandler = handler;
    }

    @Override // org.xml.sax.XMLReader
    public ContentHandler getContentHandler() {
        return this.contentHandler;
    }

    @Override // org.xml.sax.XMLReader
    public void setErrorHandler(ErrorHandler handler) {
        this.errorHandler = handler;
    }

    @Override // org.xml.sax.XMLReader
    public ErrorHandler getErrorHandler() {
        return this.errorHandler;
    }

    @Override // org.xml.sax.XMLReader
    public void parse(InputSource source) throws SAXException, IOException {
        this.systemId = source.getSystemId();
        this.contentHandler.setDocumentLocator(this);
        Reader reader = source.getCharacterStream();
        try {
            if (reader == null) {
                InputStream stream = source.getByteStream();
                String encoding = source.getEncoding();
                if (stream == null) {
                    this.systemId = source.getSystemId();
                    if (this.systemId == null) {
                        SAXParseException saxException = new SAXParseException("null source systemId", this);
                        this.errorHandler.fatalError(saxException);
                        return;
                    }
                    try {
                        URL url = new URL(this.systemId);
                        stream = url.openStream();
                    } catch (MalformedURLException e) {
                        try {
                            stream = new FileInputStream(this.systemId);
                        } catch (FileNotFoundException fnfe) {
                            SAXParseException saxException2 = new SAXParseException(new StringBuffer().append("could not open file with systemId ").append(this.systemId).toString(), this, fnfe);
                            this.errorHandler.fatalError(saxException2);
                            return;
                        }
                    }
                }
                this.pp.setInput(stream, encoding);
            } else {
                this.pp.setInput(reader);
            }
            try {
                this.contentHandler.startDocument();
                this.pp.next();
                if (this.pp.getEventType() != 2) {
                    SAXParseException saxException3 = new SAXParseException(new StringBuffer().append("expected start tag not").append(this.pp.getPositionDescription()).toString(), this);
                    this.errorHandler.fatalError(saxException3);
                    return;
                }
                parseSubTree(this.pp);
                this.contentHandler.endDocument();
            } catch (XmlPullParserException ex) {
                SAXParseException saxException4 = new SAXParseException(new StringBuffer().append("parsing initialization error: ").append(ex).toString(), this, ex);
                this.errorHandler.fatalError(saxException4);
            }
        } catch (XmlPullParserException ex2) {
            SAXParseException saxException5 = new SAXParseException(new StringBuffer().append("parsing initialization error: ").append(ex2).toString(), this, ex2);
            this.errorHandler.fatalError(saxException5);
        }
    }

    @Override // org.xml.sax.XMLReader
    public void parse(String systemId) throws SAXException, IOException {
        parse(new InputSource(systemId));
    }

    public void parseSubTree(XmlPullParser pp) throws SAXException, IOException {
        this.pp = pp;
        boolean namespaceAware = pp.getFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces");
        try {
            if (pp.getEventType() != 2) {
                throw new SAXException(new StringBuffer().append("start tag must be read before skiping subtree").append(pp.getPositionDescription()).toString());
            }
            int[] holderForStartAndLength = new int[2];
            StringBuffer rawName = new StringBuffer(16);
            int level = pp.getDepth() - 1;
            int type = 2;
            do {
                switch (type) {
                    case 1:
                    case 2:
                        if (namespaceAware) {
                            int depth = pp.getDepth() - 1;
                            int countPrev = level > depth ? pp.getNamespaceCount(depth) : 0;
                            int count = pp.getNamespaceCount(depth + 1);
                            for (int i = countPrev; i < count; i++) {
                                this.contentHandler.startPrefixMapping(pp.getNamespacePrefix(i), pp.getNamespaceUri(i));
                            }
                            String name = pp.getName();
                            String prefix = pp.getPrefix();
                            if (prefix != null) {
                                rawName.setLength(0);
                                rawName.append(prefix);
                                rawName.append(':');
                                rawName.append(name);
                            }
                            startElement(pp.getNamespace(), name, prefix != null ? rawName.toString() : name);
                            break;
                        } else {
                            startElement(pp.getNamespace(), pp.getName(), pp.getName());
                            break;
                        }
                    case 3:
                        if (namespaceAware) {
                            String name2 = pp.getName();
                            String prefix2 = pp.getPrefix();
                            if (prefix2 != null) {
                                rawName.setLength(0);
                                rawName.append(prefix2);
                                rawName.append(':');
                                rawName.append(name2);
                            }
                            this.contentHandler.endElement(pp.getNamespace(), name2, prefix2 != null ? rawName.toString() : name2);
                            int countPrev2 = level > pp.getDepth() ? pp.getNamespaceCount(pp.getDepth()) : 0;
                            int count2 = pp.getNamespaceCount(pp.getDepth() - 1);
                            for (int i2 = count2 - 1; i2 >= countPrev2; i2--) {
                                this.contentHandler.endPrefixMapping(pp.getNamespacePrefix(i2));
                            }
                            break;
                        } else {
                            this.contentHandler.endElement(pp.getNamespace(), pp.getName(), pp.getName());
                            break;
                        }
                    case 4:
                        char[] chars = pp.getTextCharacters(holderForStartAndLength);
                        this.contentHandler.characters(chars, holderForStartAndLength[0], holderForStartAndLength[1]);
                        break;
                }
                type = pp.next();
            } while (pp.getDepth() > level);
        } catch (XmlPullParserException ex) {
            SAXParseException saxException = new SAXParseException(new StringBuffer().append("parsing error: ").append(ex).toString(), this, ex);
            ex.printStackTrace();
            this.errorHandler.fatalError(saxException);
        }
    }

    protected void startElement(String namespace, String localName, String qName) throws SAXException {
        this.contentHandler.startElement(namespace, localName, qName, this);
    }
}
