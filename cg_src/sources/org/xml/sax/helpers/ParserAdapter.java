package org.xml.sax.helpers;

import java.io.IOException;
import java.util.Enumeration;
import org.apache.tools.ant.taskdefs.condition.ParserSupports;
import org.xml.sax.AttributeList;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.DocumentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/xml/sax/helpers/ParserAdapter.class */
public class ParserAdapter implements XMLReader, DocumentHandler {
    private static final String FEATURES = "http://xml.org/sax/features/";
    private static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
    private static final String NAMESPACE_PREFIXES = "http://xml.org/sax/features/namespace-prefixes";
    private NamespaceSupport nsSupport;
    private AttributeListAdapter attAdapter;
    private boolean parsing;
    private String[] nameParts;
    private Parser parser;
    private AttributesImpl atts;
    private boolean namespaces;
    private boolean prefixes;
    Locator locator;
    EntityResolver entityResolver;
    DTDHandler dtdHandler;
    ContentHandler contentHandler;
    ErrorHandler errorHandler;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/xml/sax/helpers/ParserAdapter$AttributeListAdapter.class */
    public final class AttributeListAdapter implements Attributes {
        private AttributeList qAtts;
        private final ParserAdapter this$0;

        AttributeListAdapter(ParserAdapter parserAdapter) {
            this.this$0 = parserAdapter;
        }

        void setAttributeList(AttributeList attributeList) {
            this.qAtts = attributeList;
        }

        @Override // org.xml.sax.Attributes
        public int getLength() {
            return this.qAtts.getLength();
        }

        @Override // org.xml.sax.Attributes
        public String getURI(int i) {
            return "";
        }

        @Override // org.xml.sax.Attributes
        public String getLocalName(int i) {
            return "";
        }

        @Override // org.xml.sax.Attributes
        public String getQName(int i) {
            return this.qAtts.getName(i).intern();
        }

        @Override // org.xml.sax.Attributes
        public String getType(int i) {
            return this.qAtts.getType(i).intern();
        }

        @Override // org.xml.sax.Attributes
        public String getValue(int i) {
            return this.qAtts.getValue(i);
        }

        @Override // org.xml.sax.Attributes
        public int getIndex(String str, String str2) {
            return -1;
        }

        @Override // org.xml.sax.Attributes
        public int getIndex(String str) {
            int length = this.this$0.atts.getLength();
            for (int i = 0; i < length; i++) {
                if (this.qAtts.getName(i).equals(str)) {
                    return i;
                }
            }
            return -1;
        }

        @Override // org.xml.sax.Attributes
        public String getType(String str, String str2) {
            return null;
        }

        @Override // org.xml.sax.Attributes
        public String getType(String str) {
            return this.qAtts.getType(str).intern();
        }

        @Override // org.xml.sax.Attributes
        public String getValue(String str, String str2) {
            return null;
        }

        @Override // org.xml.sax.Attributes
        public String getValue(String str) {
            return this.qAtts.getValue(str);
        }
    }

    public ParserAdapter() throws SAXException {
        this.parsing = false;
        this.nameParts = new String[3];
        this.parser = null;
        this.atts = null;
        this.namespaces = true;
        this.prefixes = false;
        this.entityResolver = null;
        this.dtdHandler = null;
        this.contentHandler = null;
        this.errorHandler = null;
        String systemProperty = SecuritySupport.getInstance().getSystemProperty("org.xml.sax.parser");
        try {
            setup(ParserFactory.makeParser());
        } catch (ClassCastException e) {
            throw new SAXException(new StringBuffer().append("SAX1 driver class ").append(systemProperty).append(" does not implement org.xml.sax.Parser").toString());
        } catch (ClassNotFoundException e2) {
            throw new SAXException(new StringBuffer().append("Cannot find SAX1 driver class ").append(systemProperty).toString(), e2);
        } catch (IllegalAccessException e3) {
            throw new SAXException(new StringBuffer().append("SAX1 driver class ").append(systemProperty).append(" found but cannot be loaded").toString(), e3);
        } catch (InstantiationException e4) {
            throw new SAXException(new StringBuffer().append("SAX1 driver class ").append(systemProperty).append(" loaded but cannot be instantiated").toString(), e4);
        } catch (NullPointerException e5) {
            throw new SAXException("System property org.xml.sax.parser not specified");
        }
    }

    public ParserAdapter(Parser parser) {
        this.parsing = false;
        this.nameParts = new String[3];
        this.parser = null;
        this.atts = null;
        this.namespaces = true;
        this.prefixes = false;
        this.entityResolver = null;
        this.dtdHandler = null;
        this.contentHandler = null;
        this.errorHandler = null;
        setup(parser);
    }

    private void setup(Parser parser) {
        if (parser == null) {
            throw new NullPointerException("Parser argument must not be null");
        }
        this.parser = parser;
        this.atts = new AttributesImpl();
        this.nsSupport = new NamespaceSupport();
        this.attAdapter = new AttributeListAdapter(this);
    }

    @Override // org.xml.sax.XMLReader
    public void setFeature(String str, boolean z) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (str.equals("http://xml.org/sax/features/namespaces")) {
            checkNotParsing(ParserSupports.FEATURE, str);
            this.namespaces = z;
            if (this.namespaces || this.prefixes) {
                return;
            }
            this.prefixes = true;
        } else if (!str.equals("http://xml.org/sax/features/namespace-prefixes")) {
            throw new SAXNotRecognizedException(new StringBuffer().append("Feature: ").append(str).toString());
        } else {
            checkNotParsing(ParserSupports.FEATURE, str);
            this.prefixes = z;
            if (this.prefixes || this.namespaces) {
                return;
            }
            this.namespaces = true;
        }
    }

    @Override // org.xml.sax.XMLReader
    public boolean getFeature(String str) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (str.equals("http://xml.org/sax/features/namespaces")) {
            return this.namespaces;
        }
        if (str.equals("http://xml.org/sax/features/namespace-prefixes")) {
            return this.prefixes;
        }
        throw new SAXNotRecognizedException(new StringBuffer().append("Feature: ").append(str).toString());
    }

    @Override // org.xml.sax.XMLReader
    public void setProperty(String str, Object obj) throws SAXNotRecognizedException, SAXNotSupportedException {
        throw new SAXNotRecognizedException(new StringBuffer().append("Property: ").append(str).toString());
    }

    @Override // org.xml.sax.XMLReader
    public Object getProperty(String str) throws SAXNotRecognizedException, SAXNotSupportedException {
        throw new SAXNotRecognizedException(new StringBuffer().append("Property: ").append(str).toString());
    }

    @Override // org.xml.sax.XMLReader
    public void setEntityResolver(EntityResolver entityResolver) {
        this.entityResolver = entityResolver;
    }

    @Override // org.xml.sax.XMLReader
    public EntityResolver getEntityResolver() {
        return this.entityResolver;
    }

    @Override // org.xml.sax.XMLReader
    public void setDTDHandler(DTDHandler dTDHandler) {
        this.dtdHandler = dTDHandler;
    }

    @Override // org.xml.sax.XMLReader
    public DTDHandler getDTDHandler() {
        return this.dtdHandler;
    }

    @Override // org.xml.sax.XMLReader
    public void setContentHandler(ContentHandler contentHandler) {
        this.contentHandler = contentHandler;
    }

    @Override // org.xml.sax.XMLReader
    public ContentHandler getContentHandler() {
        return this.contentHandler;
    }

    @Override // org.xml.sax.XMLReader
    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    @Override // org.xml.sax.XMLReader
    public ErrorHandler getErrorHandler() {
        return this.errorHandler;
    }

    @Override // org.xml.sax.XMLReader
    public void parse(String str) throws IOException, SAXException {
        parse(new InputSource(str));
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: JavaClassParseException: Unknown opcode: 0xa8 in method: org.xml.sax.helpers.ParserAdapter.parse(org.xml.sax.InputSource):void, file: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/xml/sax/helpers/ParserAdapter.class
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:413)
        	at jadx.core.ProcessClass.process(ProcessClass.java:67)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:107)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:387)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:335)
        Caused by: jadx.plugins.input.java.utils.JavaClassParseException: Unknown opcode: 0xa8
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:71)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:48)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:148)
        	... 5 more
        */
    @Override // org.xml.sax.XMLReader
    public void parse(org.xml.sax.InputSource r1) throws java.io.IOException, org.xml.sax.SAXException {
        /*
        // Can't load method instructions: Load method exception: JavaClassParseException: Unknown opcode: 0xa8 in method: org.xml.sax.helpers.ParserAdapter.parse(org.xml.sax.InputSource):void, file: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/xml/sax/helpers/ParserAdapter.class
        */
        throw new UnsupportedOperationException("Method not decompiled: org.xml.sax.helpers.ParserAdapter.parse(org.xml.sax.InputSource):void");
    }

    @Override // org.xml.sax.DocumentHandler
    public void setDocumentLocator(Locator locator) {
        this.locator = locator;
        if (this.contentHandler != null) {
            this.contentHandler.setDocumentLocator(locator);
        }
    }

    @Override // org.xml.sax.DocumentHandler
    public void startDocument() throws SAXException {
        if (this.contentHandler != null) {
            this.contentHandler.startDocument();
        }
    }

    @Override // org.xml.sax.DocumentHandler
    public void endDocument() throws SAXException {
        if (this.contentHandler != null) {
            this.contentHandler.endDocument();
        }
    }

    /* JADX WARN: Can't wrap try/catch for region: R(7:38|(4:40|(2:53|(1:55)(1:56))(1:44)|45|(3:47|(2:49|50)(1:52)|51))|57|58|60|51|36) */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x0190, code lost:
        r17 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x0193, code lost:
        if (r10 == null) goto L64;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x0196, code lost:
        r10 = new java.util.Vector();
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x019e, code lost:
        r10.addElement(r17);
        r7.atts.addAttribute("", r0, r0, r0, r0);
     */
    @Override // org.xml.sax.DocumentHandler
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void startElement(java.lang.String r8, org.xml.sax.AttributeList r9) throws org.xml.sax.SAXException {
        /*
            Method dump skipped, instructions count: 536
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.xml.sax.helpers.ParserAdapter.startElement(java.lang.String, org.xml.sax.AttributeList):void");
    }

    @Override // org.xml.sax.DocumentHandler
    public void endElement(String str) throws SAXException {
        if (!this.namespaces) {
            if (this.contentHandler != null) {
                this.contentHandler.endElement("", "", str.intern());
                return;
            }
            return;
        }
        String[] processName = processName(str, false, false);
        if (this.contentHandler != null) {
            this.contentHandler.endElement(processName[0], processName[1], processName[2]);
            Enumeration declaredPrefixes = this.nsSupport.getDeclaredPrefixes();
            while (declaredPrefixes.hasMoreElements()) {
                this.contentHandler.endPrefixMapping((String) declaredPrefixes.nextElement());
            }
        }
        this.nsSupport.popContext();
    }

    @Override // org.xml.sax.DocumentHandler
    public void characters(char[] cArr, int i, int i2) throws SAXException {
        if (this.contentHandler != null) {
            this.contentHandler.characters(cArr, i, i2);
        }
    }

    @Override // org.xml.sax.DocumentHandler
    public void ignorableWhitespace(char[] cArr, int i, int i2) throws SAXException {
        if (this.contentHandler != null) {
            this.contentHandler.ignorableWhitespace(cArr, i, i2);
        }
    }

    @Override // org.xml.sax.DocumentHandler
    public void processingInstruction(String str, String str2) throws SAXException {
        if (this.contentHandler != null) {
            this.contentHandler.processingInstruction(str, str2);
        }
    }

    private void setupParser() {
        this.nsSupport.reset();
        if (this.entityResolver != null) {
            this.parser.setEntityResolver(this.entityResolver);
        }
        if (this.dtdHandler != null) {
            this.parser.setDTDHandler(this.dtdHandler);
        }
        if (this.errorHandler != null) {
            this.parser.setErrorHandler(this.errorHandler);
        }
        this.parser.setDocumentHandler(this);
        this.locator = null;
    }

    private String[] processName(String str, boolean z, boolean z2) throws SAXException {
        String[] processName = this.nsSupport.processName(str, this.nameParts, z);
        if (processName == null) {
            if (z2) {
                throw makeException(new StringBuffer().append("Undeclared prefix: ").append(str).toString());
            }
            reportError(new StringBuffer().append("Undeclared prefix: ").append(str).toString());
            processName = new String[]{"", "", str.intern()};
        }
        return processName;
    }

    void reportError(String str) throws SAXException {
        if (this.errorHandler != null) {
            this.errorHandler.error(makeException(str));
        }
    }

    private SAXParseException makeException(String str) {
        return this.locator != null ? new SAXParseException(str, this.locator) : new SAXParseException(str, null, null, -1, -1);
    }

    private void checkNotParsing(String str, String str2) throws SAXNotSupportedException {
        if (this.parsing) {
            throw new SAXNotSupportedException(new StringBuffer().append("Cannot change ").append(str).append(' ').append(str2).append(" while parsing").toString());
        }
    }
}
