package org.xmlpull.mxp1_serializer;

import com.sun.xml.fastinfoset.EncodingConstants;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import org.xmlpull.v1.XmlSerializer;
import soot.coffi.Instruction;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:xpp3-1.1.4c.jar:org/xmlpull/mxp1_serializer/MXSerializer.class */
public class MXSerializer implements XmlSerializer {
    protected static final String XML_URI = "http://www.w3.org/XML/1998/namespace";
    protected static final String XMLNS_URI = "http://www.w3.org/2000/xmlns/";
    private static final boolean TRACE_SIZING = false;
    private static final boolean TRACE_ESCAPING = false;
    protected static final String PROPERTY_LOCATION = "http://xmlpull.org/v1/doc/properties.html#location";
    protected boolean namesInterned;
    protected boolean attributeUseApostrophe;
    protected String location;
    protected Writer out;
    protected int autoDeclaredPrefixes;
    protected boolean finished;
    protected boolean pastRoot;
    protected boolean setPrefixCalled;
    protected boolean startTagIncomplete;
    protected boolean doIndent;
    protected boolean seenTag;
    protected boolean seenBracket;
    protected boolean seenBracketBracket;
    private static final int BUF_LEN;
    protected static final String[] precomputedPrefixes;
    protected int offsetNewLine;
    protected int indentationJump;
    protected char[] indentationBuf;
    protected int maxIndentLevel;
    protected boolean writeLineSepartor;
    protected boolean writeIndentation;
    protected final String FEATURE_SERIALIZER_ATTVALUE_USE_APOSTROPHE = "http://xmlpull.org/v1/doc/features.html#serializer-attvalue-use-apostrophe";
    protected final String FEATURE_NAMES_INTERNED = "http://xmlpull.org/v1/doc/features.html#names-interned";
    protected final String PROPERTY_SERIALIZER_INDENTATION = "http://xmlpull.org/v1/doc/properties.html#serializer-indentation";
    protected final String PROPERTY_SERIALIZER_LINE_SEPARATOR = "http://xmlpull.org/v1/doc/properties.html#serializer-line-separator";
    protected String indentationString = null;
    protected String lineSeparator = "\n";
    protected int depth = 0;
    protected String[] elNamespace = new String[2];
    protected String[] elName = new String[this.elNamespace.length];
    protected String[] elPrefix = new String[this.elNamespace.length];
    protected int[] elNamespaceCount = new int[this.elNamespace.length];
    protected int namespaceEnd = 0;
    protected String[] namespacePrefix = new String[8];
    protected String[] namespaceUri = new String[this.namespacePrefix.length];
    protected char[] buf = new char[BUF_LEN];
    private boolean checkNamesInterned = false;

    static {
        BUF_LEN = Runtime.getRuntime().freeMemory() > 1000000 ? 8192 : 256;
        precomputedPrefixes = new String[32];
        for (int i = 0; i < precomputedPrefixes.length; i++) {
            precomputedPrefixes[i] = new StringBuffer().append("n").append(i).toString().intern();
        }
    }

    private void checkInterning(String name) {
        if (this.namesInterned && name != name.intern()) {
            throw new IllegalArgumentException("all names passed as arguments must be internedwhen NAMES INTERNED feature is enabled");
        }
    }

    protected void reset() {
        this.location = null;
        this.out = null;
        this.autoDeclaredPrefixes = 0;
        this.depth = 0;
        for (int i = 0; i < this.elNamespaceCount.length; i++) {
            this.elName[i] = null;
            this.elPrefix[i] = null;
            this.elNamespace[i] = null;
            this.elNamespaceCount[i] = 2;
        }
        this.namespaceEnd = 0;
        this.namespacePrefix[this.namespaceEnd] = EncodingConstants.XMLNS_NAMESPACE_PREFIX;
        this.namespaceUri[this.namespaceEnd] = "http://www.w3.org/2000/xmlns/";
        this.namespaceEnd++;
        this.namespacePrefix[this.namespaceEnd] = EncodingConstants.XML_NAMESPACE_PREFIX;
        this.namespaceUri[this.namespaceEnd] = "http://www.w3.org/XML/1998/namespace";
        this.namespaceEnd++;
        this.finished = false;
        this.pastRoot = false;
        this.setPrefixCalled = false;
        this.startTagIncomplete = false;
        this.seenTag = false;
        this.seenBracket = false;
        this.seenBracketBracket = false;
    }

    protected void ensureElementsCapacity() {
        int elStackSize = this.elName.length;
        int newSize = (this.depth >= 7 ? 2 * this.depth : 8) + 2;
        boolean needsCopying = elStackSize > 0;
        String[] arr = new String[newSize];
        if (needsCopying) {
            System.arraycopy(this.elName, 0, arr, 0, elStackSize);
        }
        this.elName = arr;
        String[] arr2 = new String[newSize];
        if (needsCopying) {
            System.arraycopy(this.elPrefix, 0, arr2, 0, elStackSize);
        }
        this.elPrefix = arr2;
        String[] arr3 = new String[newSize];
        if (needsCopying) {
            System.arraycopy(this.elNamespace, 0, arr3, 0, elStackSize);
        }
        this.elNamespace = arr3;
        int[] iarr = new int[newSize];
        if (needsCopying) {
            System.arraycopy(this.elNamespaceCount, 0, iarr, 0, elStackSize);
        } else {
            iarr[0] = 0;
        }
        this.elNamespaceCount = iarr;
    }

    protected void ensureNamespacesCapacity() {
        int newSize = this.namespaceEnd > 7 ? 2 * this.namespaceEnd : 8;
        String[] newNamespacePrefix = new String[newSize];
        String[] newNamespaceUri = new String[newSize];
        if (this.namespacePrefix != null) {
            System.arraycopy(this.namespacePrefix, 0, newNamespacePrefix, 0, this.namespaceEnd);
            System.arraycopy(this.namespaceUri, 0, newNamespaceUri, 0, this.namespaceEnd);
        }
        this.namespacePrefix = newNamespacePrefix;
        this.namespaceUri = newNamespaceUri;
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void setFeature(String name, boolean state) throws IllegalArgumentException, IllegalStateException {
        if (name == null) {
            throw new IllegalArgumentException("feature name can not be null");
        }
        if ("http://xmlpull.org/v1/doc/features.html#names-interned".equals(name)) {
            this.namesInterned = state;
        } else if ("http://xmlpull.org/v1/doc/features.html#serializer-attvalue-use-apostrophe".equals(name)) {
            this.attributeUseApostrophe = state;
        } else {
            throw new IllegalStateException(new StringBuffer().append("unsupported feature ").append(name).toString());
        }
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public boolean getFeature(String name) throws IllegalArgumentException {
        if (name == null) {
            throw new IllegalArgumentException("feature name can not be null");
        }
        if ("http://xmlpull.org/v1/doc/features.html#names-interned".equals(name)) {
            return this.namesInterned;
        }
        if ("http://xmlpull.org/v1/doc/features.html#serializer-attvalue-use-apostrophe".equals(name)) {
            return this.attributeUseApostrophe;
        }
        return false;
    }

    protected void rebuildIndentationBuf() {
        if (this.doIndent) {
            int bufSize = 0;
            this.offsetNewLine = 0;
            if (this.writeLineSepartor) {
                this.offsetNewLine = this.lineSeparator.length();
                bufSize = 0 + this.offsetNewLine;
            }
            this.maxIndentLevel = 0;
            if (this.writeIndentation) {
                this.indentationJump = this.indentationString.length();
                this.maxIndentLevel = 65 / this.indentationJump;
                bufSize += this.maxIndentLevel * this.indentationJump;
            }
            if (this.indentationBuf == null || this.indentationBuf.length < bufSize) {
                this.indentationBuf = new char[bufSize + 8];
            }
            int bufPos = 0;
            if (this.writeLineSepartor) {
                for (int i = 0; i < this.lineSeparator.length(); i++) {
                    int i2 = bufPos;
                    bufPos++;
                    this.indentationBuf[i2] = this.lineSeparator.charAt(i);
                }
            }
            if (this.writeIndentation) {
                for (int i3 = 0; i3 < this.maxIndentLevel; i3++) {
                    for (int j = 0; j < this.indentationString.length(); j++) {
                        int i4 = bufPos;
                        bufPos++;
                        this.indentationBuf[i4] = this.indentationString.charAt(j);
                    }
                }
            }
        }
    }

    protected void writeIndent() throws IOException {
        int start = this.writeLineSepartor ? 0 : this.offsetNewLine;
        int level = this.depth > this.maxIndentLevel ? this.maxIndentLevel : this.depth;
        this.out.write(this.indentationBuf, start, ((level - 1) * this.indentationJump) + this.offsetNewLine);
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void setProperty(String name, Object value) throws IllegalArgumentException, IllegalStateException {
        if (name == null) {
            throw new IllegalArgumentException("property name can not be null");
        }
        if ("http://xmlpull.org/v1/doc/properties.html#serializer-indentation".equals(name)) {
            this.indentationString = (String) value;
        } else if ("http://xmlpull.org/v1/doc/properties.html#serializer-line-separator".equals(name)) {
            this.lineSeparator = (String) value;
        } else if (PROPERTY_LOCATION.equals(name)) {
            this.location = (String) value;
        } else {
            throw new IllegalStateException(new StringBuffer().append("unsupported property ").append(name).toString());
        }
        this.writeLineSepartor = this.lineSeparator != null && this.lineSeparator.length() > 0;
        this.writeIndentation = this.indentationString != null && this.indentationString.length() > 0;
        this.doIndent = this.indentationString != null && (this.writeLineSepartor || this.writeIndentation);
        rebuildIndentationBuf();
        this.seenTag = false;
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public Object getProperty(String name) throws IllegalArgumentException {
        if (name == null) {
            throw new IllegalArgumentException("property name can not be null");
        }
        if ("http://xmlpull.org/v1/doc/properties.html#serializer-indentation".equals(name)) {
            return this.indentationString;
        }
        if ("http://xmlpull.org/v1/doc/properties.html#serializer-line-separator".equals(name)) {
            return this.lineSeparator;
        }
        if (PROPERTY_LOCATION.equals(name)) {
            return this.location;
        }
        return null;
    }

    private String getLocation() {
        return this.location != null ? new StringBuffer().append(" @").append(this.location).toString() : "";
    }

    public Writer getWriter() {
        return this.out;
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void setOutput(Writer writer) {
        reset();
        this.out = writer;
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void setOutput(OutputStream os, String encoding) throws IOException {
        if (os == null) {
            throw new IllegalArgumentException("output stream can not be null");
        }
        reset();
        if (encoding != null) {
            this.out = new OutputStreamWriter(os, encoding);
        } else {
            this.out = new OutputStreamWriter(os);
        }
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void startDocument(String encoding, Boolean standalone) throws IOException {
        char c = this.attributeUseApostrophe ? '\'' : '\"';
        if (this.attributeUseApostrophe) {
            this.out.write("<?xml version='1.0'");
        } else {
            this.out.write("<?xml version=\"1.0\"");
        }
        if (encoding != null) {
            this.out.write(" encoding=");
            this.out.write(this.attributeUseApostrophe ? 39 : 34);
            this.out.write(encoding);
            this.out.write(this.attributeUseApostrophe ? 39 : 34);
        }
        if (standalone != null) {
            this.out.write(" standalone=");
            this.out.write(this.attributeUseApostrophe ? 39 : 34);
            if (standalone.booleanValue()) {
                this.out.write("yes");
            } else {
                this.out.write("no");
            }
            this.out.write(this.attributeUseApostrophe ? 39 : 34);
        }
        this.out.write("?>");
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void endDocument() throws IOException {
        while (this.depth > 0) {
            endTag(this.elNamespace[this.depth], this.elName[this.depth]);
        }
        this.startTagIncomplete = true;
        this.pastRoot = true;
        this.finished = true;
        this.out.flush();
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void setPrefix(String prefix, String namespace) throws IOException {
        if (this.startTagIncomplete) {
            closeStartTag();
        }
        if (prefix == null) {
            prefix = "";
        }
        if (!this.namesInterned) {
            prefix = prefix.intern();
        } else if (this.checkNamesInterned) {
            checkInterning(prefix);
        } else if (prefix == null) {
            throw new IllegalArgumentException(new StringBuffer().append("prefix must be not null").append(getLocation()).toString());
        }
        for (int i = this.elNamespaceCount[this.depth]; i < this.namespaceEnd; i++) {
            if (prefix == this.namespacePrefix[i]) {
                throw new IllegalStateException(new StringBuffer().append("duplicated prefix ").append(printable(prefix)).append(getLocation()).toString());
            }
        }
        if (!this.namesInterned) {
            namespace = namespace.intern();
        } else if (this.checkNamesInterned) {
            checkInterning(namespace);
        } else if (namespace == null) {
            throw new IllegalArgumentException(new StringBuffer().append("namespace must be not null").append(getLocation()).toString());
        }
        if (this.namespaceEnd >= this.namespacePrefix.length) {
            ensureNamespacesCapacity();
        }
        this.namespacePrefix[this.namespaceEnd] = prefix;
        this.namespaceUri[this.namespaceEnd] = namespace;
        this.namespaceEnd++;
        this.setPrefixCalled = true;
    }

    protected String lookupOrDeclarePrefix(String namespace) {
        return getPrefix(namespace, true);
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public String getPrefix(String namespace, boolean generatePrefix) {
        return getPrefix(namespace, generatePrefix, false);
    }

    protected String getPrefix(String namespace, boolean generatePrefix, boolean nonEmpty) {
        if (!this.namesInterned) {
            namespace = namespace.intern();
        } else if (this.checkNamesInterned) {
            checkInterning(namespace);
        }
        if (namespace == null) {
            throw new IllegalArgumentException(new StringBuffer().append("namespace must be not null").append(getLocation()).toString());
        }
        if (namespace.length() == 0) {
            throw new IllegalArgumentException(new StringBuffer().append("default namespace cannot have prefix").append(getLocation()).toString());
        }
        for (int i = this.namespaceEnd - 1; i >= 0; i--) {
            if (namespace == this.namespaceUri[i]) {
                String prefix = this.namespacePrefix[i];
                if (!nonEmpty || prefix.length() != 0) {
                    for (int p = this.namespaceEnd - 1; p > i; p--) {
                        if (prefix == this.namespacePrefix[p]) {
                        }
                    }
                    return prefix;
                }
            }
        }
        if (!generatePrefix) {
            return null;
        }
        return generatePrefix(namespace);
    }

    private String generatePrefix(String namespace) {
        this.autoDeclaredPrefixes++;
        String prefix = this.autoDeclaredPrefixes < precomputedPrefixes.length ? precomputedPrefixes[this.autoDeclaredPrefixes] : new StringBuffer().append("n").append(this.autoDeclaredPrefixes).toString().intern();
        for (int i = this.namespaceEnd - 1; i >= 0; i--) {
            if (prefix == this.namespacePrefix[i]) {
            }
        }
        if (this.namespaceEnd >= this.namespacePrefix.length) {
            ensureNamespacesCapacity();
        }
        this.namespacePrefix[this.namespaceEnd] = prefix;
        this.namespaceUri[this.namespaceEnd] = namespace;
        this.namespaceEnd++;
        return prefix;
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public int getDepth() {
        return this.depth;
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public String getNamespace() {
        return this.elNamespace[this.depth];
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public String getName() {
        return this.elName[this.depth];
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public XmlSerializer startTag(String namespace, String name) throws IOException {
        String uri;
        if (this.startTagIncomplete) {
            closeStartTag();
        }
        this.seenBracketBracket = false;
        this.seenBracket = false;
        this.depth++;
        if (this.doIndent && this.depth > 0 && this.seenTag) {
            writeIndent();
        }
        this.seenTag = true;
        this.setPrefixCalled = false;
        this.startTagIncomplete = true;
        if (this.depth + 1 >= this.elName.length) {
            ensureElementsCapacity();
        }
        if (this.checkNamesInterned && this.namesInterned) {
            checkInterning(namespace);
        }
        this.elNamespace[this.depth] = (this.namesInterned || namespace == null) ? namespace : namespace.intern();
        if (this.checkNamesInterned && this.namesInterned) {
            checkInterning(name);
        }
        this.elName[this.depth] = (this.namesInterned || name == null) ? name : name.intern();
        if (this.out == null) {
            throw new IllegalStateException("setOutput() must called set before serialization can start");
        }
        this.out.write(60);
        if (namespace != null) {
            if (namespace.length() > 0) {
                String prefix = null;
                if (this.depth > 0 && this.namespaceEnd - this.elNamespaceCount[this.depth - 1] == 1 && ((uri = this.namespaceUri[this.namespaceEnd - 1]) == namespace || uri.equals(namespace))) {
                    String elPfx = this.namespacePrefix[this.namespaceEnd - 1];
                    for (int pos = this.elNamespaceCount[this.depth - 1] - 1; pos >= 2; pos--) {
                        String pf = this.namespacePrefix[pos];
                        if (pf == elPfx || pf.equals(elPfx)) {
                            String n = this.namespaceUri[pos];
                            if (n == uri || n.equals(uri)) {
                                this.namespaceEnd--;
                                prefix = elPfx;
                            }
                        }
                    }
                }
                if (prefix == null) {
                    prefix = lookupOrDeclarePrefix(namespace);
                }
                if (prefix.length() > 0) {
                    this.elPrefix[this.depth] = prefix;
                    this.out.write(prefix);
                    this.out.write(58);
                } else {
                    this.elPrefix[this.depth] = "";
                }
            } else {
                int i = this.namespaceEnd - 1;
                while (true) {
                    if (i < 0) {
                        break;
                    } else if (this.namespacePrefix[i] != "") {
                        i--;
                    } else {
                        String uri2 = this.namespaceUri[i];
                        if (uri2 == null) {
                            setPrefix("", "");
                        } else if (uri2.length() > 0) {
                            throw new IllegalStateException(new StringBuffer().append("start tag can not be written in empty default namespace as default namespace is currently bound to '").append(uri2).append("'").append(getLocation()).toString());
                        }
                    }
                }
                this.elPrefix[this.depth] = "";
            }
        } else {
            this.elPrefix[this.depth] = "";
        }
        this.out.write(name);
        return this;
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public XmlSerializer attribute(String namespace, String name, String value) throws IOException {
        if (!this.startTagIncomplete) {
            throw new IllegalArgumentException(new StringBuffer().append("startTag() must be called before attribute()").append(getLocation()).toString());
        }
        this.out.write(32);
        if (namespace != null && namespace.length() > 0) {
            if (!this.namesInterned) {
                namespace = namespace.intern();
            } else if (this.checkNamesInterned) {
                checkInterning(namespace);
            }
            String prefix = getPrefix(namespace, false, true);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
            }
            this.out.write(prefix);
            this.out.write(58);
        }
        this.out.write(name);
        this.out.write(61);
        this.out.write(this.attributeUseApostrophe ? 39 : 34);
        writeAttributeValue(value, this.out);
        this.out.write(this.attributeUseApostrophe ? 39 : 34);
        return this;
    }

    protected void closeStartTag() throws IOException {
        if (this.finished) {
            throw new IllegalArgumentException(new StringBuffer().append("trying to write past already finished output").append(getLocation()).toString());
        }
        if (this.seenBracket) {
            this.seenBracketBracket = false;
            this.seenBracket = false;
        }
        if (this.startTagIncomplete || this.setPrefixCalled) {
            if (this.setPrefixCalled) {
                throw new IllegalArgumentException(new StringBuffer().append("startTag() must be called immediately after setPrefix()").append(getLocation()).toString());
            }
            if (!this.startTagIncomplete) {
                throw new IllegalArgumentException(new StringBuffer().append("trying to close start tag that is not opened").append(getLocation()).toString());
            }
            writeNamespaceDeclarations();
            this.out.write(62);
            this.elNamespaceCount[this.depth] = this.namespaceEnd;
            this.startTagIncomplete = false;
        }
    }

    private void writeNamespaceDeclarations() throws IOException {
        for (int i = this.elNamespaceCount[this.depth - 1]; i < this.namespaceEnd; i++) {
            if (this.doIndent && this.namespaceUri[i].length() > 40) {
                writeIndent();
                this.out.write(Instruction.argsep);
            }
            if (this.namespacePrefix[i] != "") {
                this.out.write(" xmlns:");
                this.out.write(this.namespacePrefix[i]);
                this.out.write(61);
            } else {
                this.out.write(" xmlns=");
            }
            this.out.write(this.attributeUseApostrophe ? 39 : 34);
            writeAttributeValue(this.namespaceUri[i], this.out);
            this.out.write(this.attributeUseApostrophe ? 39 : 34);
        }
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public XmlSerializer endTag(String namespace, String name) throws IOException {
        this.seenBracketBracket = false;
        this.seenBracket = false;
        if (namespace != null) {
            if (!this.namesInterned) {
                namespace = namespace.intern();
            } else if (this.checkNamesInterned) {
                checkInterning(namespace);
            }
        }
        if (namespace != this.elNamespace[this.depth]) {
            throw new IllegalArgumentException(new StringBuffer().append("expected namespace ").append(printable(this.elNamespace[this.depth])).append(" and not ").append(printable(namespace)).append(getLocation()).toString());
        }
        if (name == null) {
            throw new IllegalArgumentException(new StringBuffer().append("end tag name can not be null").append(getLocation()).toString());
        }
        if (this.checkNamesInterned && this.namesInterned) {
            checkInterning(name);
        }
        String startTagName = this.elName[this.depth];
        if ((!this.namesInterned && !name.equals(startTagName)) || (this.namesInterned && name != startTagName)) {
            throw new IllegalArgumentException(new StringBuffer().append("expected element name ").append(printable(this.elName[this.depth])).append(" and not ").append(printable(name)).append(getLocation()).toString());
        }
        if (this.startTagIncomplete) {
            writeNamespaceDeclarations();
            this.out.write(" />");
            this.depth--;
        } else {
            if (this.doIndent && this.seenTag) {
                writeIndent();
            }
            this.out.write("</");
            String startTagPrefix = this.elPrefix[this.depth];
            if (startTagPrefix.length() > 0) {
                this.out.write(startTagPrefix);
                this.out.write(58);
            }
            this.out.write(name);
            this.out.write(62);
            this.depth--;
        }
        this.namespaceEnd = this.elNamespaceCount[this.depth];
        this.startTagIncomplete = false;
        this.seenTag = true;
        return this;
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public XmlSerializer text(String text) throws IOException {
        if (this.startTagIncomplete || this.setPrefixCalled) {
            closeStartTag();
        }
        if (this.doIndent && this.seenTag) {
            this.seenTag = false;
        }
        writeElementContent(text, this.out);
        return this;
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public XmlSerializer text(char[] buf, int start, int len) throws IOException {
        if (this.startTagIncomplete || this.setPrefixCalled) {
            closeStartTag();
        }
        if (this.doIndent && this.seenTag) {
            this.seenTag = false;
        }
        writeElementContent(buf, start, len, this.out);
        return this;
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void cdsect(String text) throws IOException {
        if (this.startTagIncomplete || this.setPrefixCalled || this.seenBracket) {
            closeStartTag();
        }
        if (this.doIndent && this.seenTag) {
            this.seenTag = false;
        }
        this.out.write("<![CDATA[");
        this.out.write(text);
        this.out.write("]]>");
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void entityRef(String text) throws IOException {
        if (this.startTagIncomplete || this.setPrefixCalled || this.seenBracket) {
            closeStartTag();
        }
        if (this.doIndent && this.seenTag) {
            this.seenTag = false;
        }
        this.out.write(38);
        this.out.write(text);
        this.out.write(59);
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void processingInstruction(String text) throws IOException {
        if (this.startTagIncomplete || this.setPrefixCalled || this.seenBracket) {
            closeStartTag();
        }
        if (this.doIndent && this.seenTag) {
            this.seenTag = false;
        }
        this.out.write("<?");
        this.out.write(text);
        this.out.write("?>");
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void comment(String text) throws IOException {
        if (this.startTagIncomplete || this.setPrefixCalled || this.seenBracket) {
            closeStartTag();
        }
        if (this.doIndent && this.seenTag) {
            this.seenTag = false;
        }
        this.out.write("<!--");
        this.out.write(text);
        this.out.write("-->");
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void docdecl(String text) throws IOException {
        if (this.startTagIncomplete || this.setPrefixCalled || this.seenBracket) {
            closeStartTag();
        }
        if (this.doIndent && this.seenTag) {
            this.seenTag = false;
        }
        this.out.write("<!DOCTYPE");
        this.out.write(text);
        this.out.write(">");
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void ignorableWhitespace(String text) throws IOException {
        if (this.startTagIncomplete || this.setPrefixCalled || this.seenBracket) {
            closeStartTag();
        }
        if (this.doIndent && this.seenTag) {
            this.seenTag = false;
        }
        if (text.length() == 0) {
            throw new IllegalArgumentException(new StringBuffer().append("empty string is not allowed for ignorable whitespace").append(getLocation()).toString());
        }
        this.out.write(text);
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void flush() throws IOException {
        if (!this.finished && this.startTagIncomplete) {
            closeStartTag();
        }
        this.out.flush();
    }

    protected void writeAttributeValue(String value, Writer out) throws IOException {
        char quot = this.attributeUseApostrophe ? '\'' : '\"';
        String quotEntity = this.attributeUseApostrophe ? "&apos;" : "&quot;";
        int pos = 0;
        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);
            if (ch == '&') {
                if (i > pos) {
                    out.write(value.substring(pos, i));
                }
                out.write("&amp;");
                pos = i + 1;
            }
            if (ch == '<') {
                if (i > pos) {
                    out.write(value.substring(pos, i));
                }
                out.write("&lt;");
                pos = i + 1;
            } else if (ch == quot) {
                if (i > pos) {
                    out.write(value.substring(pos, i));
                }
                out.write(quotEntity);
                pos = i + 1;
            } else if (ch >= ' ') {
                continue;
            } else if (ch == '\r' || ch == '\n' || ch == '\t') {
                if (i > pos) {
                    out.write(value.substring(pos, i));
                }
                out.write("&#");
                out.write(Integer.toString(ch));
                out.write(59);
                pos = i + 1;
            } else {
                throw new IllegalStateException(new StringBuffer().append("character ").append(printable(ch)).append(" (").append(Integer.toString(ch)).append(") is not allowed in output").append(getLocation()).append(" (attr value=").append(printable(value)).append(")").toString());
            }
        }
        if (pos > 0) {
            out.write(value.substring(pos));
        } else {
            out.write(value);
        }
    }

    protected void writeElementContent(String text, Writer out) throws IOException {
        int pos = 0;
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch == ']') {
                if (this.seenBracket) {
                    this.seenBracketBracket = true;
                } else {
                    this.seenBracket = true;
                }
            } else {
                if (ch == '&') {
                    if (i > pos) {
                        out.write(text.substring(pos, i));
                    }
                    out.write("&amp;");
                    pos = i + 1;
                } else if (ch == '<') {
                    if (i > pos) {
                        out.write(text.substring(pos, i));
                    }
                    out.write("&lt;");
                    pos = i + 1;
                } else if (this.seenBracketBracket && ch == '>') {
                    if (i > pos) {
                        out.write(text.substring(pos, i));
                    }
                    out.write("&gt;");
                    pos = i + 1;
                } else if (ch < ' ' && ch != '\t' && ch != '\n' && ch != '\r') {
                    throw new IllegalStateException(new StringBuffer().append("character ").append(Integer.toString(ch)).append(" is not allowed in output").append(getLocation()).append(" (text value=").append(printable(text)).append(")").toString());
                }
                if (this.seenBracket) {
                    this.seenBracket = false;
                    this.seenBracketBracket = false;
                }
            }
        }
        if (pos > 0) {
            out.write(text.substring(pos));
        } else {
            out.write(text);
        }
    }

    protected void writeElementContent(char[] buf, int off, int len, Writer out) throws IOException {
        int end = off + len;
        int pos = off;
        for (int i = off; i < end; i++) {
            char ch = buf[i];
            if (ch == ']') {
                if (this.seenBracket) {
                    this.seenBracketBracket = true;
                } else {
                    this.seenBracket = true;
                }
            } else {
                if (ch == '&') {
                    if (i > pos) {
                        out.write(buf, pos, i - pos);
                    }
                    out.write("&amp;");
                    pos = i + 1;
                } else if (ch == '<') {
                    if (i > pos) {
                        out.write(buf, pos, i - pos);
                    }
                    out.write("&lt;");
                    pos = i + 1;
                } else if (this.seenBracketBracket && ch == '>') {
                    if (i > pos) {
                        out.write(buf, pos, i - pos);
                    }
                    out.write("&gt;");
                    pos = i + 1;
                } else if (ch < ' ' && ch != '\t' && ch != '\n' && ch != '\r') {
                    throw new IllegalStateException(new StringBuffer().append("character ").append(printable(ch)).append(" (").append(Integer.toString(ch)).append(") is not allowed in output").append(getLocation()).toString());
                }
                if (this.seenBracket) {
                    this.seenBracket = false;
                    this.seenBracketBracket = false;
                }
            }
        }
        if (end > pos) {
            out.write(buf, pos, end - pos);
        }
    }

    protected static final String printable(String s) {
        if (s == null) {
            return Jimple.NULL;
        }
        StringBuffer retval = new StringBuffer(s.length() + 16);
        retval.append("'");
        for (int i = 0; i < s.length(); i++) {
            addPrintable(retval, s.charAt(i));
        }
        retval.append("'");
        return retval.toString();
    }

    protected static final String printable(char ch) {
        StringBuffer retval = new StringBuffer();
        addPrintable(retval, ch);
        return retval.toString();
    }

    private static void addPrintable(StringBuffer retval, char ch) {
        switch (ch) {
            case '\b':
                retval.append("\\b");
                return;
            case '\t':
                retval.append("\\t");
                return;
            case '\n':
                retval.append("\\n");
                return;
            case '\f':
                retval.append("\\f");
                return;
            case '\r':
                retval.append("\\r");
                return;
            case '\"':
                retval.append("\\\"");
                return;
            case '\'':
                retval.append("\\'");
                return;
            case '\\':
                retval.append("\\\\");
                return;
            default:
                if (ch < ' ' || ch > '~') {
                    String ss = new StringBuffer().append("0000").append(Integer.toString(ch, 16)).toString();
                    retval.append(new StringBuffer().append("\\u").append(ss.substring(ss.length() - 4, ss.length())).toString());
                    return;
                }
                retval.append(ch);
                return;
        }
    }
}
