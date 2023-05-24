package org.xmlpull.mxp1;

import com.sun.xml.fastinfoset.EncodingConstants;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import soot.coffi.Instruction;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:xmlpull-1.1.3.4d_b4_min.jar:org/xmlpull/mxp1/MXParser.class
 */
/* loaded from: gencallgraphv3.jar:xpp3-1.1.4c.jar:org/xmlpull/mxp1/MXParser.class */
public class MXParser implements XmlPullParser {
    protected static final String XML_URI = "http://www.w3.org/XML/1998/namespace";
    protected static final String XMLNS_URI = "http://www.w3.org/2000/xmlns/";
    protected static final String FEATURE_XML_ROUNDTRIP = "http://xmlpull.org/v1/doc/features.html#xml-roundtrip";
    protected static final String FEATURE_NAMES_INTERNED = "http://xmlpull.org/v1/doc/features.html#names-interned";
    protected static final String PROPERTY_XMLDECL_VERSION = "http://xmlpull.org/v1/doc/properties.html#xmldecl-version";
    protected static final String PROPERTY_XMLDECL_STANDALONE = "http://xmlpull.org/v1/doc/properties.html#xmldecl-standalone";
    protected static final String PROPERTY_XMLDECL_CONTENT = "http://xmlpull.org/v1/doc/properties.html#xmldecl-content";
    protected static final String PROPERTY_LOCATION = "http://xmlpull.org/v1/doc/properties.html#location";
    protected boolean allStringsInterned;
    private static final boolean TRACE_SIZING = false;
    protected boolean processNamespaces;
    protected boolean roundtripSupported;
    protected String location;
    protected int lineNumber;
    protected int columnNumber;
    protected boolean seenRoot;
    protected boolean reachedEnd;
    protected int eventType;
    protected boolean emptyElementTag;
    protected int depth;
    protected char[][] elRawName;
    protected int[] elRawNameEnd;
    protected int[] elRawNameLine;
    protected String[] elName;
    protected String[] elPrefix;
    protected String[] elUri;
    protected int[] elNamespaceCount;
    protected int attributeCount;
    protected String[] attributeName;
    protected int[] attributeNameHash;
    protected String[] attributePrefix;
    protected String[] attributeUri;
    protected String[] attributeValue;
    protected int namespaceEnd;
    protected String[] namespacePrefix;
    protected int[] namespacePrefixHash;
    protected String[] namespaceUri;
    protected int entityEnd;
    protected String[] entityName;
    protected char[][] entityNameBuf;
    protected String[] entityReplacement;
    protected char[][] entityReplacementBuf;
    protected int[] entityNameHash;
    protected static final int READ_CHUNK_SIZE = 8192;
    protected Reader reader;
    protected String inputEncoding;
    protected InputStream inputStream;
    protected int bufLoadFactor = 95;
    protected char[] buf;
    protected int bufSoftLimit;
    protected boolean preventBufferCompaction;
    protected int bufAbsoluteStart;
    protected int bufStart;
    protected int bufEnd;
    protected int pos;
    protected int posStart;
    protected int posEnd;
    protected char[] pc;
    protected int pcStart;
    protected int pcEnd;
    protected boolean usePC;
    protected boolean seenStartTag;
    protected boolean seenEndTag;
    protected boolean pastEndTag;
    protected boolean seenAmpersand;
    protected boolean seenMarkup;
    protected boolean seenDocdecl;
    protected boolean tokenize;
    protected String text;
    protected String entityRefName;
    protected String xmlDeclVersion;
    protected Boolean xmlDeclStandalone;
    protected String xmlDeclContent;
    protected char[] charRefOneCharBuf;
    protected static final int LOOKUP_MAX = 1024;
    protected static final char LOOKUP_MAX_CHAR = 1024;
    protected static final char[] VERSION = "version".toCharArray();
    protected static final char[] NCODING = "ncoding".toCharArray();
    protected static final char[] TANDALONE = "tandalone".toCharArray();
    protected static final char[] YES = "yes".toCharArray();
    protected static final char[] NO = "no".toCharArray();
    protected static boolean[] lookupNameStartChar = new boolean[1024];
    protected static boolean[] lookupNameChar = new boolean[1024];

    protected void resetStringCache() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String newString(char[] cbuf, int off, int len) {
        return new String(cbuf, off, len);
    }

    protected String newStringIntern(char[] cbuf, int off, int len) {
        return new String(cbuf, off, len).intern();
    }

    /* JADX WARN: Type inference failed for: r0v42, types: [char[], char[][], java.lang.Object] */
    protected void ensureElementsCapacity() {
        int elStackSize = this.elName != null ? this.elName.length : 0;
        if (this.depth + 1 >= elStackSize) {
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
                System.arraycopy(this.elUri, 0, arr3, 0, elStackSize);
            }
            this.elUri = arr3;
            int[] iarr = new int[newSize];
            if (needsCopying) {
                System.arraycopy(this.elNamespaceCount, 0, iarr, 0, elStackSize);
            } else {
                iarr[0] = 0;
            }
            this.elNamespaceCount = iarr;
            int[] iarr2 = new int[newSize];
            if (needsCopying) {
                System.arraycopy(this.elRawNameEnd, 0, iarr2, 0, elStackSize);
            }
            this.elRawNameEnd = iarr2;
            int[] iarr3 = new int[newSize];
            if (needsCopying) {
                System.arraycopy(this.elRawNameLine, 0, iarr3, 0, elStackSize);
            }
            this.elRawNameLine = iarr3;
            ?? r0 = new char[newSize];
            if (needsCopying) {
                System.arraycopy(this.elRawName, 0, r0, 0, elStackSize);
            }
            this.elRawName = r0;
        }
    }

    protected void ensureAttributesCapacity(int size) {
        int attrPosSize = this.attributeName != null ? this.attributeName.length : 0;
        if (size >= attrPosSize) {
            int newSize = size > 7 ? 2 * size : 8;
            boolean needsCopying = attrPosSize > 0;
            String[] arr = new String[newSize];
            if (needsCopying) {
                System.arraycopy(this.attributeName, 0, arr, 0, attrPosSize);
            }
            this.attributeName = arr;
            String[] arr2 = new String[newSize];
            if (needsCopying) {
                System.arraycopy(this.attributePrefix, 0, arr2, 0, attrPosSize);
            }
            this.attributePrefix = arr2;
            String[] arr3 = new String[newSize];
            if (needsCopying) {
                System.arraycopy(this.attributeUri, 0, arr3, 0, attrPosSize);
            }
            this.attributeUri = arr3;
            String[] arr4 = new String[newSize];
            if (needsCopying) {
                System.arraycopy(this.attributeValue, 0, arr4, 0, attrPosSize);
            }
            this.attributeValue = arr4;
            if (!this.allStringsInterned) {
                int[] iarr = new int[newSize];
                if (needsCopying) {
                    System.arraycopy(this.attributeNameHash, 0, iarr, 0, attrPosSize);
                }
                this.attributeNameHash = iarr;
            }
        }
    }

    protected void ensureNamespacesCapacity(int size) {
        int namespaceSize = this.namespacePrefix != null ? this.namespacePrefix.length : 0;
        if (size >= namespaceSize) {
            int newSize = size > 7 ? 2 * size : 8;
            String[] newNamespacePrefix = new String[newSize];
            String[] newNamespaceUri = new String[newSize];
            if (this.namespacePrefix != null) {
                System.arraycopy(this.namespacePrefix, 0, newNamespacePrefix, 0, this.namespaceEnd);
                System.arraycopy(this.namespaceUri, 0, newNamespaceUri, 0, this.namespaceEnd);
            }
            this.namespacePrefix = newNamespacePrefix;
            this.namespaceUri = newNamespaceUri;
            if (!this.allStringsInterned) {
                int[] newNamespacePrefixHash = new int[newSize];
                if (this.namespacePrefixHash != null) {
                    System.arraycopy(this.namespacePrefixHash, 0, newNamespacePrefixHash, 0, this.namespaceEnd);
                }
                this.namespacePrefixHash = newNamespacePrefixHash;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static final int fastHash(char[] ch, int off, int len) {
        if (len == 0) {
            return 0;
        }
        int hash = (ch[off] << 7) + ch[(off + len) - 1];
        if (len > 16) {
            hash = (hash << 7) + ch[off + (len / 4)];
        }
        if (len > 8) {
            hash = (hash << 7) + ch[off + (len / 2)];
        }
        return hash;
    }

    /* JADX WARN: Type inference failed for: r0v13, types: [char[], char[][], java.lang.Object] */
    /* JADX WARN: Type inference failed for: r0v17, types: [char[], char[][], java.lang.Object] */
    protected void ensureEntityCapacity() {
        int entitySize = this.entityReplacementBuf != null ? this.entityReplacementBuf.length : 0;
        if (this.entityEnd >= entitySize) {
            int newSize = this.entityEnd > 7 ? 2 * this.entityEnd : 8;
            String[] newEntityName = new String[newSize];
            ?? r0 = new char[newSize];
            String[] newEntityReplacement = new String[newSize];
            ?? r02 = new char[newSize];
            if (this.entityName != null) {
                System.arraycopy(this.entityName, 0, newEntityName, 0, this.entityEnd);
                System.arraycopy(this.entityNameBuf, 0, r0, 0, this.entityEnd);
                System.arraycopy(this.entityReplacement, 0, newEntityReplacement, 0, this.entityEnd);
                System.arraycopy(this.entityReplacementBuf, 0, r02, 0, this.entityEnd);
            }
            this.entityName = newEntityName;
            this.entityNameBuf = r0;
            this.entityReplacement = newEntityReplacement;
            this.entityReplacementBuf = r02;
            if (!this.allStringsInterned) {
                int[] newEntityNameHash = new int[newSize];
                if (this.entityNameHash != null) {
                    System.arraycopy(this.entityNameHash, 0, newEntityNameHash, 0, this.entityEnd);
                }
                this.entityNameHash = newEntityNameHash;
            }
        }
    }

    protected void reset() {
        this.location = null;
        this.lineNumber = 1;
        this.columnNumber = 0;
        this.seenRoot = false;
        this.reachedEnd = false;
        this.eventType = 0;
        this.emptyElementTag = false;
        this.depth = 0;
        this.attributeCount = 0;
        this.namespaceEnd = 0;
        this.entityEnd = 0;
        this.reader = null;
        this.inputEncoding = null;
        this.preventBufferCompaction = false;
        this.bufAbsoluteStart = 0;
        this.bufStart = 0;
        this.bufEnd = 0;
        this.posEnd = 0;
        this.posStart = 0;
        this.pos = 0;
        this.pcStart = 0;
        this.pcEnd = 0;
        this.usePC = false;
        this.seenStartTag = false;
        this.seenEndTag = false;
        this.pastEndTag = false;
        this.seenAmpersand = false;
        this.seenMarkup = false;
        this.seenDocdecl = false;
        this.xmlDeclVersion = null;
        this.xmlDeclStandalone = null;
        this.xmlDeclContent = null;
        resetStringCache();
    }

    public MXParser() {
        this.buf = new char[Runtime.getRuntime().freeMemory() > 1000000 ? 8192 : 256];
        this.bufSoftLimit = (this.bufLoadFactor * this.buf.length) / 100;
        this.pc = new char[Runtime.getRuntime().freeMemory() > 1000000 ? 8192 : 64];
        this.charRefOneCharBuf = new char[1];
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public void setFeature(String name, boolean state) throws XmlPullParserException {
        if (name == null) {
            throw new IllegalArgumentException("feature name should not be null");
        }
        if ("http://xmlpull.org/v1/doc/features.html#process-namespaces".equals(name)) {
            if (this.eventType != 0) {
                throw new XmlPullParserException("namespace processing feature can only be changed before parsing", this, null);
            }
            this.processNamespaces = state;
        } else if ("http://xmlpull.org/v1/doc/features.html#names-interned".equals(name)) {
            if (state) {
                throw new XmlPullParserException("interning names in this implementation is not supported");
            }
        } else if ("http://xmlpull.org/v1/doc/features.html#process-docdecl".equals(name)) {
            if (state) {
                throw new XmlPullParserException("processing DOCDECL is not supported");
            }
        } else if ("http://xmlpull.org/v1/doc/features.html#xml-roundtrip".equals(name)) {
            this.roundtripSupported = state;
        } else {
            throw new XmlPullParserException(new StringBuffer().append("unsupported feature ").append(name).toString());
        }
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public boolean getFeature(String name) {
        if (name == null) {
            throw new IllegalArgumentException("feature name should not be null");
        }
        if ("http://xmlpull.org/v1/doc/features.html#process-namespaces".equals(name)) {
            return this.processNamespaces;
        }
        if (!"http://xmlpull.org/v1/doc/features.html#names-interned".equals(name) && !"http://xmlpull.org/v1/doc/features.html#process-docdecl".equals(name) && "http://xmlpull.org/v1/doc/features.html#xml-roundtrip".equals(name)) {
            return this.roundtripSupported;
        }
        return false;
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public void setProperty(String name, Object value) throws XmlPullParserException {
        if ("http://xmlpull.org/v1/doc/properties.html#location".equals(name)) {
            this.location = (String) value;
            return;
        }
        throw new XmlPullParserException(new StringBuffer().append("unsupported property: '").append(name).append("'").toString());
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public Object getProperty(String name) {
        if (name == null) {
            throw new IllegalArgumentException("property name should not be null");
        }
        if ("http://xmlpull.org/v1/doc/properties.html#xmldecl-version".equals(name)) {
            return this.xmlDeclVersion;
        }
        if ("http://xmlpull.org/v1/doc/properties.html#xmldecl-standalone".equals(name)) {
            return this.xmlDeclStandalone;
        }
        if ("http://xmlpull.org/v1/doc/properties.html#xmldecl-content".equals(name)) {
            return this.xmlDeclContent;
        }
        if ("http://xmlpull.org/v1/doc/properties.html#location".equals(name)) {
            return this.location;
        }
        return null;
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public void setInput(Reader in) throws XmlPullParserException {
        reset();
        this.reader = in;
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public void setInput(InputStream inputStream, String inputEncoding) throws XmlPullParserException {
        Reader reader;
        if (inputStream == null) {
            throw new IllegalArgumentException("input stream can not be null");
        }
        this.inputStream = inputStream;
        try {
            if (inputEncoding != null) {
                reader = new InputStreamReader(inputStream, inputEncoding);
            } else {
                reader = new InputStreamReader(inputStream, "UTF-8");
            }
            setInput(reader);
            this.inputEncoding = inputEncoding;
        } catch (UnsupportedEncodingException une) {
            throw new XmlPullParserException(new StringBuffer().append("could not create reader for encoding ").append(inputEncoding).append(" : ").append(une).toString(), this, une);
        }
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String getInputEncoding() {
        return this.inputEncoding;
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public void defineEntityReplacementText(String entityName, String replacementText) throws XmlPullParserException {
        ensureEntityCapacity();
        this.entityName[this.entityEnd] = newString(entityName.toCharArray(), 0, entityName.length());
        this.entityNameBuf[this.entityEnd] = entityName.toCharArray();
        this.entityReplacement[this.entityEnd] = replacementText;
        this.entityReplacementBuf[this.entityEnd] = replacementText.toCharArray();
        if (!this.allStringsInterned) {
            this.entityNameHash[this.entityEnd] = fastHash(this.entityNameBuf[this.entityEnd], 0, this.entityNameBuf[this.entityEnd].length);
        }
        this.entityEnd++;
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public int getNamespaceCount(int depth) throws XmlPullParserException {
        if (!this.processNamespaces || depth == 0) {
            return 0;
        }
        if (depth < 0 || depth > this.depth) {
            throw new IllegalArgumentException(new StringBuffer().append("allowed namespace depth 0..").append(this.depth).append(" not ").append(depth).toString());
        }
        return this.elNamespaceCount[depth];
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String getNamespacePrefix(int pos) throws XmlPullParserException {
        if (pos < this.namespaceEnd) {
            return this.namespacePrefix[pos];
        }
        throw new XmlPullParserException(new StringBuffer().append("position ").append(pos).append(" exceeded number of available namespaces ").append(this.namespaceEnd).toString());
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String getNamespaceUri(int pos) throws XmlPullParserException {
        if (pos < this.namespaceEnd) {
            return this.namespaceUri[pos];
        }
        throw new XmlPullParserException(new StringBuffer().append("position ").append(pos).append(" exceeded number of available namespaces ").append(this.namespaceEnd).toString());
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String getNamespace(String prefix) {
        if (prefix != null) {
            for (int i = this.namespaceEnd - 1; i >= 0; i--) {
                if (prefix.equals(this.namespacePrefix[i])) {
                    return this.namespaceUri[i];
                }
            }
            if (EncodingConstants.XML_NAMESPACE_PREFIX.equals(prefix)) {
                return "http://www.w3.org/XML/1998/namespace";
            }
            if (EncodingConstants.XMLNS_NAMESPACE_PREFIX.equals(prefix)) {
                return EncodingConstants.XMLNS_NAMESPACE_NAME;
            }
            return null;
        }
        for (int i2 = this.namespaceEnd - 1; i2 >= 0; i2--) {
            if (this.namespacePrefix[i2] == null) {
                return this.namespaceUri[i2];
            }
        }
        return null;
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public int getDepth() {
        return this.depth;
    }

    private static int findFragment(int bufMinPos, char[] b, int start, int end) {
        if (start < bufMinPos) {
            int start2 = bufMinPos;
            if (start2 > end) {
                start2 = end;
            }
            return start2;
        }
        if (end - start > 65) {
            start = end - 10;
        }
        int i = start + 1;
        while (true) {
            i--;
            if (i <= bufMinPos || end - i > 65) {
                break;
            }
            char c = b[i];
            if (c == '<' && start - i > 10) {
                break;
            }
        }
        return i;
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String getPositionDescription() {
        String fragment = null;
        if (this.posStart <= this.pos) {
            int start = findFragment(0, this.buf, this.posStart, this.pos);
            if (start < this.pos) {
                fragment = new String(this.buf, start, this.pos - start);
            }
            if (this.bufAbsoluteStart > 0 || start > 0) {
                fragment = new StringBuffer().append("...").append(fragment).toString();
            }
        }
        return new StringBuffer().append(Instruction.argsep).append(XmlPullParser.TYPES[this.eventType]).append(fragment != null ? new StringBuffer().append(" seen ").append(printable(fragment)).append("...").toString() : "").append(Instruction.argsep).append(this.location != null ? this.location : "").append("@").append(getLineNumber()).append(":").append(getColumnNumber()).toString();
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public int getLineNumber() {
        return this.lineNumber;
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public int getColumnNumber() {
        return this.columnNumber;
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public boolean isWhitespace() throws XmlPullParserException {
        if (this.eventType == 4 || this.eventType == 5) {
            if (this.usePC) {
                for (int i = this.pcStart; i < this.pcEnd; i++) {
                    if (!isS(this.pc[i])) {
                        return false;
                    }
                }
                return true;
            }
            for (int i2 = this.posStart; i2 < this.posEnd; i2++) {
                if (!isS(this.buf[i2])) {
                    return false;
                }
            }
            return true;
        } else if (this.eventType == 7) {
            return true;
        } else {
            throw new XmlPullParserException("no content available to check for white spaces");
        }
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String getText() {
        if (this.eventType == 0 || this.eventType == 1) {
            return null;
        }
        if (this.eventType == 6) {
            return this.text;
        }
        if (this.text == null) {
            if (!this.usePC || this.eventType == 2 || this.eventType == 3) {
                this.text = new String(this.buf, this.posStart, this.posEnd - this.posStart);
            } else {
                this.text = new String(this.pc, this.pcStart, this.pcEnd - this.pcStart);
            }
        }
        return this.text;
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public char[] getTextCharacters(int[] holderForStartAndLength) {
        if (this.eventType == 4) {
            if (this.usePC) {
                holderForStartAndLength[0] = this.pcStart;
                holderForStartAndLength[1] = this.pcEnd - this.pcStart;
                return this.pc;
            }
            holderForStartAndLength[0] = this.posStart;
            holderForStartAndLength[1] = this.posEnd - this.posStart;
            return this.buf;
        } else if (this.eventType == 2 || this.eventType == 3 || this.eventType == 5 || this.eventType == 9 || this.eventType == 6 || this.eventType == 8 || this.eventType == 7 || this.eventType == 10) {
            holderForStartAndLength[0] = this.posStart;
            holderForStartAndLength[1] = this.posEnd - this.posStart;
            return this.buf;
        } else if (this.eventType == 0 || this.eventType == 1) {
            holderForStartAndLength[1] = -1;
            holderForStartAndLength[0] = -1;
            return null;
        } else {
            throw new IllegalArgumentException(new StringBuffer().append("unknown text eventType: ").append(this.eventType).toString());
        }
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String getNamespace() {
        if (this.eventType == 2) {
            return this.processNamespaces ? this.elUri[this.depth] : "";
        } else if (this.eventType == 3) {
            return this.processNamespaces ? this.elUri[this.depth] : "";
        } else {
            return null;
        }
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String getName() {
        if (this.eventType == 2) {
            return this.elName[this.depth];
        }
        if (this.eventType == 3) {
            return this.elName[this.depth];
        }
        if (this.eventType == 6) {
            if (this.entityRefName == null) {
                this.entityRefName = newString(this.buf, this.posStart, this.posEnd - this.posStart);
            }
            return this.entityRefName;
        }
        return null;
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String getPrefix() {
        if (this.eventType == 2) {
            return this.elPrefix[this.depth];
        }
        if (this.eventType == 3) {
            return this.elPrefix[this.depth];
        }
        return null;
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public boolean isEmptyElementTag() throws XmlPullParserException {
        if (this.eventType != 2) {
            throw new XmlPullParserException("parser must be on START_TAG to check for empty element", this, null);
        }
        return this.emptyElementTag;
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public int getAttributeCount() {
        if (this.eventType != 2) {
            return -1;
        }
        return this.attributeCount;
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String getAttributeNamespace(int index) {
        if (this.eventType != 2) {
            throw new IndexOutOfBoundsException("only START_TAG can have attributes");
        }
        if (this.processNamespaces) {
            if (index < 0 || index >= this.attributeCount) {
                throw new IndexOutOfBoundsException(new StringBuffer().append("attribute position must be 0..").append(this.attributeCount - 1).append(" and not ").append(index).toString());
            }
            return this.attributeUri[index];
        }
        return "";
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String getAttributeName(int index) {
        if (this.eventType != 2) {
            throw new IndexOutOfBoundsException("only START_TAG can have attributes");
        }
        if (index < 0 || index >= this.attributeCount) {
            throw new IndexOutOfBoundsException(new StringBuffer().append("attribute position must be 0..").append(this.attributeCount - 1).append(" and not ").append(index).toString());
        }
        return this.attributeName[index];
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String getAttributePrefix(int index) {
        if (this.eventType != 2) {
            throw new IndexOutOfBoundsException("only START_TAG can have attributes");
        }
        if (this.processNamespaces) {
            if (index < 0 || index >= this.attributeCount) {
                throw new IndexOutOfBoundsException(new StringBuffer().append("attribute position must be 0..").append(this.attributeCount - 1).append(" and not ").append(index).toString());
            }
            return this.attributePrefix[index];
        }
        return null;
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String getAttributeType(int index) {
        if (this.eventType != 2) {
            throw new IndexOutOfBoundsException("only START_TAG can have attributes");
        }
        if (index < 0 || index >= this.attributeCount) {
            throw new IndexOutOfBoundsException(new StringBuffer().append("attribute position must be 0..").append(this.attributeCount - 1).append(" and not ").append(index).toString());
        }
        return "CDATA";
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public boolean isAttributeDefault(int index) {
        if (this.eventType != 2) {
            throw new IndexOutOfBoundsException("only START_TAG can have attributes");
        }
        if (index < 0 || index >= this.attributeCount) {
            throw new IndexOutOfBoundsException(new StringBuffer().append("attribute position must be 0..").append(this.attributeCount - 1).append(" and not ").append(index).toString());
        }
        return false;
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String getAttributeValue(int index) {
        if (this.eventType != 2) {
            throw new IndexOutOfBoundsException("only START_TAG can have attributes");
        }
        if (index < 0 || index >= this.attributeCount) {
            throw new IndexOutOfBoundsException(new StringBuffer().append("attribute position must be 0..").append(this.attributeCount - 1).append(" and not ").append(index).toString());
        }
        return this.attributeValue[index];
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String getAttributeValue(String namespace, String name) {
        if (this.eventType != 2) {
            throw new IndexOutOfBoundsException(new StringBuffer().append("only START_TAG can have attributes").append(getPositionDescription()).toString());
        }
        if (name == null) {
            throw new IllegalArgumentException("attribute name can not be null");
        }
        if (this.processNamespaces) {
            if (namespace == null) {
                namespace = "";
            }
            for (int i = 0; i < this.attributeCount; i++) {
                if ((namespace == this.attributeUri[i] || namespace.equals(this.attributeUri[i])) && name.equals(this.attributeName[i])) {
                    return this.attributeValue[i];
                }
            }
            return null;
        }
        if (namespace != null && namespace.length() == 0) {
            namespace = null;
        }
        if (namespace != null) {
            throw new IllegalArgumentException("when namespaces processing is disabled attribute namespace must be null");
        }
        for (int i2 = 0; i2 < this.attributeCount; i2++) {
            if (name.equals(this.attributeName[i2])) {
                return this.attributeValue[i2];
            }
        }
        return null;
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public int getEventType() throws XmlPullParserException {
        return this.eventType;
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public void require(int type, String namespace, String name) throws XmlPullParserException, IOException {
        if (!this.processNamespaces && namespace != null) {
            throw new XmlPullParserException(new StringBuffer().append("processing namespaces must be enabled on parser (or factory) to have possible namespaces declared on elements").append(" (position:").append(getPositionDescription()).append(")").toString());
        }
        if (type != getEventType() || ((namespace != null && !namespace.equals(getNamespace())) || (name != null && !name.equals(getName())))) {
            throw new XmlPullParserException(new StringBuffer().append("expected event ").append(XmlPullParser.TYPES[type]).append(name != null ? new StringBuffer().append(" with name '").append(name).append("'").toString() : "").append((namespace == null || name == null) ? "" : " and").append(namespace != null ? new StringBuffer().append(" with namespace '").append(namespace).append("'").toString() : "").append(" but got").append(type != getEventType() ? new StringBuffer().append(Instruction.argsep).append(XmlPullParser.TYPES[getEventType()]).toString() : "").append((name == null || getName() == null || name.equals(getName())) ? "" : new StringBuffer().append(" name '").append(getName()).append("'").toString()).append((namespace == null || name == null || getName() == null || name.equals(getName()) || getNamespace() == null || namespace.equals(getNamespace())) ? "" : " and").append((namespace == null || getNamespace() == null || namespace.equals(getNamespace())) ? "" : new StringBuffer().append(" namespace '").append(getNamespace()).append("'").toString()).append(" (position:").append(getPositionDescription()).append(")").toString());
        }
    }

    public void skipSubTree() throws XmlPullParserException, IOException {
        require(2, null, null);
        int level = 1;
        while (level > 0) {
            int eventType = next();
            if (eventType == 3) {
                level--;
            } else if (eventType == 2) {
                level++;
            }
        }
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String nextText() throws XmlPullParserException, IOException {
        if (getEventType() != 2) {
            throw new XmlPullParserException("parser must be on START_TAG to read next text", this, null);
        }
        int eventType = next();
        if (eventType == 4) {
            String result = getText();
            if (next() != 3) {
                throw new XmlPullParserException(new StringBuffer().append("TEXT must be immediately followed by END_TAG and not ").append(XmlPullParser.TYPES[getEventType()]).toString(), this, null);
            }
            return result;
        } else if (eventType == 3) {
            return "";
        } else {
            throw new XmlPullParserException("parser must be on START_TAG or TEXT to read text", this, null);
        }
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public int nextTag() throws XmlPullParserException, IOException {
        next();
        if (this.eventType == 4 && isWhitespace()) {
            next();
        }
        if (this.eventType != 2 && this.eventType != 3) {
            throw new XmlPullParserException(new StringBuffer().append("expected START_TAG or END_TAG not ").append(XmlPullParser.TYPES[getEventType()]).toString(), this, null);
        }
        return this.eventType;
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public int next() throws XmlPullParserException, IOException {
        this.tokenize = false;
        return nextImpl();
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public int nextToken() throws XmlPullParserException, IOException {
        this.tokenize = true;
        return nextImpl();
    }

    protected int nextImpl() throws XmlPullParserException, IOException {
        char ch;
        this.text = null;
        this.pcStart = 0;
        this.pcEnd = 0;
        this.usePC = false;
        this.bufStart = this.posEnd;
        if (this.pastEndTag) {
            this.pastEndTag = false;
            this.depth--;
            this.namespaceEnd = this.elNamespaceCount[this.depth];
        }
        if (this.emptyElementTag) {
            this.emptyElementTag = false;
            this.pastEndTag = true;
            this.eventType = 3;
            return 3;
        } else if (this.depth > 0) {
            if (this.seenStartTag) {
                this.seenStartTag = false;
                int parseStartTag = parseStartTag();
                this.eventType = parseStartTag;
                return parseStartTag;
            } else if (this.seenEndTag) {
                this.seenEndTag = false;
                int parseEndTag = parseEndTag();
                this.eventType = parseEndTag;
                return parseEndTag;
            } else {
                if (this.seenMarkup) {
                    this.seenMarkup = false;
                    ch = '<';
                } else if (this.seenAmpersand) {
                    this.seenAmpersand = false;
                    ch = '&';
                } else {
                    ch = more();
                }
                this.posStart = this.pos - 1;
                boolean hadCharData = false;
                boolean needsMerging = false;
                while (true) {
                    if (ch == '<') {
                        if (hadCharData && this.tokenize) {
                            this.seenMarkup = true;
                            this.eventType = 4;
                            return 4;
                        }
                        char ch2 = more();
                        if (ch2 == '/') {
                            if (!this.tokenize && hadCharData) {
                                this.seenEndTag = true;
                                this.eventType = 4;
                                return 4;
                            }
                            int parseEndTag2 = parseEndTag();
                            this.eventType = parseEndTag2;
                            return parseEndTag2;
                        } else if (ch2 == '!') {
                            char ch3 = more();
                            if (ch3 == '-') {
                                parseComment();
                                if (this.tokenize) {
                                    this.eventType = 9;
                                    return 9;
                                } else if (!this.usePC && hadCharData) {
                                    needsMerging = true;
                                } else {
                                    this.posStart = this.pos;
                                }
                            } else if (ch3 == '[') {
                                parseCDSect(hadCharData);
                                if (this.tokenize) {
                                    this.eventType = 5;
                                    return 5;
                                }
                                int cdStart = this.posStart;
                                int cdEnd = this.posEnd;
                                int cdLen = cdEnd - cdStart;
                                if (cdLen > 0) {
                                    hadCharData = true;
                                    if (!this.usePC) {
                                        needsMerging = true;
                                    }
                                }
                            } else {
                                throw new XmlPullParserException(new StringBuffer().append("unexpected character in markup ").append(printable(ch3)).toString(), this, null);
                            }
                        } else if (ch2 == '?') {
                            parsePI();
                            if (this.tokenize) {
                                this.eventType = 8;
                                return 8;
                            } else if (!this.usePC && hadCharData) {
                                needsMerging = true;
                            } else {
                                this.posStart = this.pos;
                            }
                        } else {
                            if (isNameStartChar(ch2)) {
                                if (!this.tokenize && hadCharData) {
                                    this.seenStartTag = true;
                                    this.eventType = 4;
                                    return 4;
                                }
                                int parseStartTag2 = parseStartTag();
                                this.eventType = parseStartTag2;
                                return parseStartTag2;
                            }
                            throw new XmlPullParserException(new StringBuffer().append("unexpected character in markup ").append(printable(ch2)).toString(), this, null);
                        }
                    } else if (ch == '&') {
                        if (this.tokenize && hadCharData) {
                            this.seenAmpersand = true;
                            this.eventType = 4;
                            return 4;
                        }
                        int oldStart = this.posStart + this.bufAbsoluteStart;
                        int oldEnd = this.posEnd + this.bufAbsoluteStart;
                        char[] resolvedEntity = parseEntityRef();
                        if (this.tokenize) {
                            this.eventType = 6;
                            return 6;
                        } else if (resolvedEntity == null) {
                            if (this.entityRefName == null) {
                                this.entityRefName = newString(this.buf, this.posStart, this.posEnd - this.posStart);
                            }
                            throw new XmlPullParserException(new StringBuffer().append("could not resolve entity named '").append(printable(this.entityRefName)).append("'").toString(), this, null);
                        } else {
                            this.posStart = oldStart - this.bufAbsoluteStart;
                            this.posEnd = oldEnd - this.bufAbsoluteStart;
                            if (!this.usePC) {
                                if (hadCharData) {
                                    joinPC();
                                    needsMerging = false;
                                } else {
                                    this.usePC = true;
                                    this.pcEnd = 0;
                                    this.pcStart = 0;
                                }
                            }
                            for (char c : resolvedEntity) {
                                if (this.pcEnd >= this.pc.length) {
                                    ensurePC(this.pcEnd);
                                }
                                char[] cArr = this.pc;
                                int i = this.pcEnd;
                                this.pcEnd = i + 1;
                                cArr[i] = c;
                            }
                            hadCharData = true;
                        }
                    } else {
                        if (needsMerging) {
                            joinPC();
                            needsMerging = false;
                        }
                        hadCharData = true;
                        boolean normalizedCR = false;
                        boolean normalizeInput = (this.tokenize && this.roundtripSupported) ? false : true;
                        boolean seenBracket = false;
                        boolean seenBracketBracket = false;
                        do {
                            if (ch == ']') {
                                if (seenBracket) {
                                    seenBracketBracket = true;
                                } else {
                                    seenBracket = true;
                                }
                            } else if (seenBracketBracket && ch == '>') {
                                throw new XmlPullParserException("characters ]]> are not allowed in content", this, null);
                            } else {
                                if (seenBracket) {
                                    seenBracket = false;
                                    seenBracketBracket = false;
                                }
                            }
                            if (normalizeInput) {
                                if (ch == '\r') {
                                    normalizedCR = true;
                                    this.posEnd = this.pos - 1;
                                    if (!this.usePC) {
                                        if (this.posEnd > this.posStart) {
                                            joinPC();
                                        } else {
                                            this.usePC = true;
                                            this.pcEnd = 0;
                                            this.pcStart = 0;
                                        }
                                    }
                                    if (this.pcEnd >= this.pc.length) {
                                        ensurePC(this.pcEnd);
                                    }
                                    char[] cArr2 = this.pc;
                                    int i2 = this.pcEnd;
                                    this.pcEnd = i2 + 1;
                                    cArr2[i2] = '\n';
                                } else if (ch == '\n') {
                                    if (!normalizedCR && this.usePC) {
                                        if (this.pcEnd >= this.pc.length) {
                                            ensurePC(this.pcEnd);
                                        }
                                        char[] cArr3 = this.pc;
                                        int i3 = this.pcEnd;
                                        this.pcEnd = i3 + 1;
                                        cArr3[i3] = '\n';
                                    }
                                    normalizedCR = false;
                                } else {
                                    if (this.usePC) {
                                        if (this.pcEnd >= this.pc.length) {
                                            ensurePC(this.pcEnd);
                                        }
                                        char[] cArr4 = this.pc;
                                        int i4 = this.pcEnd;
                                        this.pcEnd = i4 + 1;
                                        cArr4[i4] = ch;
                                    }
                                    normalizedCR = false;
                                }
                            }
                            ch = more();
                            if (ch == '<') {
                                break;
                            }
                        } while (ch != '&');
                        this.posEnd = this.pos - 1;
                    }
                    ch = more();
                }
            }
        } else if (this.seenRoot) {
            return parseEpilog();
        } else {
            return parseProlog();
        }
    }

    protected int parseProlog() throws XmlPullParserException, IOException {
        char ch;
        if (this.seenMarkup) {
            ch = this.buf[this.pos - 1];
        } else {
            ch = more();
        }
        if (this.eventType == 0) {
            if (ch == 65534) {
                throw new XmlPullParserException("first character in input was UNICODE noncharacter (0xFFFE)- input requires int swapping", this, null);
            }
            if (ch == 65279) {
                ch = more();
            }
        }
        this.seenMarkup = false;
        boolean gotS = false;
        this.posStart = this.pos - 1;
        boolean normalizeIgnorableWS = this.tokenize && !this.roundtripSupported;
        boolean normalizedCR = false;
        while (true) {
            if (ch == '<') {
                if (gotS && this.tokenize) {
                    this.posEnd = this.pos - 1;
                    this.seenMarkup = true;
                    this.eventType = 7;
                    return 7;
                }
                char ch2 = more();
                if (ch2 == '?') {
                    if (parsePI()) {
                        if (this.tokenize) {
                            this.eventType = 8;
                            return 8;
                        }
                    } else {
                        this.posStart = this.pos;
                        gotS = false;
                    }
                } else if (ch2 == '!') {
                    char ch3 = more();
                    if (ch3 == 'D') {
                        if (this.seenDocdecl) {
                            throw new XmlPullParserException("only one docdecl allowed in XML document", this, null);
                        }
                        this.seenDocdecl = true;
                        parseDocdecl();
                        if (this.tokenize) {
                            this.eventType = 10;
                            return 10;
                        }
                    } else if (ch3 == '-') {
                        parseComment();
                        if (this.tokenize) {
                            this.eventType = 9;
                            return 9;
                        }
                    } else {
                        throw new XmlPullParserException(new StringBuffer().append("unexpected markup <!").append(printable(ch3)).toString(), this, null);
                    }
                } else if (ch2 == '/') {
                    throw new XmlPullParserException(new StringBuffer().append("expected start tag name and not ").append(printable(ch2)).toString(), this, null);
                } else {
                    if (isNameStartChar(ch2)) {
                        this.seenRoot = true;
                        return parseStartTag();
                    }
                    throw new XmlPullParserException(new StringBuffer().append("expected start tag name and not ").append(printable(ch2)).toString(), this, null);
                }
            } else if (isS(ch)) {
                gotS = true;
                if (normalizeIgnorableWS) {
                    if (ch == '\r') {
                        normalizedCR = true;
                        if (!this.usePC) {
                            this.posEnd = this.pos - 1;
                            if (this.posEnd > this.posStart) {
                                joinPC();
                            } else {
                                this.usePC = true;
                                this.pcEnd = 0;
                                this.pcStart = 0;
                            }
                        }
                        if (this.pcEnd >= this.pc.length) {
                            ensurePC(this.pcEnd);
                        }
                        char[] cArr = this.pc;
                        int i = this.pcEnd;
                        this.pcEnd = i + 1;
                        cArr[i] = '\n';
                    } else if (ch == '\n') {
                        if (!normalizedCR && this.usePC) {
                            if (this.pcEnd >= this.pc.length) {
                                ensurePC(this.pcEnd);
                            }
                            char[] cArr2 = this.pc;
                            int i2 = this.pcEnd;
                            this.pcEnd = i2 + 1;
                            cArr2[i2] = '\n';
                        }
                        normalizedCR = false;
                    } else {
                        if (this.usePC) {
                            if (this.pcEnd >= this.pc.length) {
                                ensurePC(this.pcEnd);
                            }
                            char[] cArr3 = this.pc;
                            int i3 = this.pcEnd;
                            this.pcEnd = i3 + 1;
                            cArr3[i3] = ch;
                        }
                        normalizedCR = false;
                    }
                }
            } else {
                throw new XmlPullParserException(new StringBuffer().append("only whitespace content allowed before start tag and not ").append(printable(ch)).toString(), this, null);
            }
            ch = more();
        }
    }

    protected int parseEpilog() throws XmlPullParserException, IOException {
        char ch;
        if (this.eventType == 1) {
            throw new XmlPullParserException("already reached end of XML input", this, null);
        }
        if (this.reachedEnd) {
            this.eventType = 1;
            return 1;
        }
        boolean gotS = false;
        boolean normalizeIgnorableWS = this.tokenize && !this.roundtripSupported;
        boolean normalizedCR = false;
        try {
            if (this.seenMarkup) {
                ch = this.buf[this.pos - 1];
            } else {
                ch = more();
            }
            this.seenMarkup = false;
            this.posStart = this.pos - 1;
            if (!this.reachedEnd) {
                do {
                    if (ch == '<') {
                        if (gotS && this.tokenize) {
                            this.posEnd = this.pos - 1;
                            this.seenMarkup = true;
                            this.eventType = 7;
                            return 7;
                        }
                        char ch2 = more();
                        if (this.reachedEnd) {
                            break;
                        } else if (ch2 == '?') {
                            parsePI();
                            if (this.tokenize) {
                                this.eventType = 8;
                                return 8;
                            }
                        } else if (ch2 == '!') {
                            char ch3 = more();
                            if (this.reachedEnd) {
                                break;
                            } else if (ch3 == 'D') {
                                parseDocdecl();
                                if (this.tokenize) {
                                    this.eventType = 10;
                                    return 10;
                                }
                            } else if (ch3 == '-') {
                                parseComment();
                                if (this.tokenize) {
                                    this.eventType = 9;
                                    return 9;
                                }
                            } else {
                                throw new XmlPullParserException(new StringBuffer().append("unexpected markup <!").append(printable(ch3)).toString(), this, null);
                            }
                        } else if (ch2 == '/') {
                            throw new XmlPullParserException(new StringBuffer().append("end tag not allowed in epilog but got ").append(printable(ch2)).toString(), this, null);
                        } else {
                            if (isNameStartChar(ch2)) {
                                throw new XmlPullParserException(new StringBuffer().append("start tag not allowed in epilog but got ").append(printable(ch2)).toString(), this, null);
                            }
                            throw new XmlPullParserException(new StringBuffer().append("in epilog expected ignorable content and not ").append(printable(ch2)).toString(), this, null);
                        }
                    } else if (isS(ch)) {
                        gotS = true;
                        if (normalizeIgnorableWS) {
                            if (ch == '\r') {
                                normalizedCR = true;
                                if (!this.usePC) {
                                    this.posEnd = this.pos - 1;
                                    if (this.posEnd > this.posStart) {
                                        joinPC();
                                    } else {
                                        this.usePC = true;
                                        this.pcEnd = 0;
                                        this.pcStart = 0;
                                    }
                                }
                                if (this.pcEnd >= this.pc.length) {
                                    ensurePC(this.pcEnd);
                                }
                                char[] cArr = this.pc;
                                int i = this.pcEnd;
                                this.pcEnd = i + 1;
                                cArr[i] = '\n';
                            } else if (ch == '\n') {
                                if (!normalizedCR && this.usePC) {
                                    if (this.pcEnd >= this.pc.length) {
                                        ensurePC(this.pcEnd);
                                    }
                                    char[] cArr2 = this.pc;
                                    int i2 = this.pcEnd;
                                    this.pcEnd = i2 + 1;
                                    cArr2[i2] = '\n';
                                }
                                normalizedCR = false;
                            } else {
                                if (this.usePC) {
                                    if (this.pcEnd >= this.pc.length) {
                                        ensurePC(this.pcEnd);
                                    }
                                    char[] cArr3 = this.pc;
                                    int i3 = this.pcEnd;
                                    this.pcEnd = i3 + 1;
                                    cArr3[i3] = ch;
                                }
                                normalizedCR = false;
                            }
                        }
                    } else {
                        throw new XmlPullParserException(new StringBuffer().append("in epilog non whitespace content is not allowed but got ").append(printable(ch)).toString(), this, null);
                    }
                    ch = more();
                } while (!this.reachedEnd);
            }
        } catch (EOFException e) {
            this.reachedEnd = true;
        }
        if (this.reachedEnd) {
            if (this.tokenize && gotS) {
                this.posEnd = this.pos;
                this.eventType = 7;
                return 7;
            }
            this.eventType = 1;
            return 1;
        }
        throw new XmlPullParserException("internal error in parseEpilog");
    }

    public int parseEndTag() throws XmlPullParserException, IOException {
        char ch;
        char ch2 = more();
        if (!isNameStartChar(ch2)) {
            throw new XmlPullParserException(new StringBuffer().append("expected name start and not ").append(printable(ch2)).toString(), this, null);
        }
        this.posStart = this.pos - 3;
        int nameStart = (this.pos - 1) + this.bufAbsoluteStart;
        do {
            ch = more();
        } while (isNameChar(ch));
        int off = nameStart - this.bufAbsoluteStart;
        int len = (this.pos - 1) - off;
        char[] cbuf = this.elRawName[this.depth];
        if (this.elRawNameEnd[this.depth] != len) {
            String startname = new String(cbuf, 0, this.elRawNameEnd[this.depth]);
            String endname = new String(this.buf, off, len);
            throw new XmlPullParserException(new StringBuffer().append("end tag name </").append(endname).append("> must match start tag name <").append(startname).append(">").append(" from line ").append(this.elRawNameLine[this.depth]).toString(), this, null);
        }
        for (int i = 0; i < len; i++) {
            int i2 = off;
            off++;
            if (this.buf[i2] != cbuf[i]) {
                String startname2 = new String(cbuf, 0, len);
                String endname2 = new String(this.buf, (off - i) - 1, len);
                throw new XmlPullParserException(new StringBuffer().append("end tag name </").append(endname2).append("> must be the same as start tag <").append(startname2).append(">").append(" from line ").append(this.elRawNameLine[this.depth]).toString(), this, null);
            }
        }
        while (isS(ch)) {
            ch = more();
        }
        if (ch != '>') {
            throw new XmlPullParserException(new StringBuffer().append("expected > to finish end tag not ").append(printable(ch)).append(" from line ").append(this.elRawNameLine[this.depth]).toString(), this, null);
        }
        this.posEnd = this.pos;
        this.pastEndTag = true;
        this.eventType = 3;
        return 3;
    }

    public int parseStartTag() throws XmlPullParserException, IOException {
        this.depth++;
        this.posStart = this.pos - 2;
        this.emptyElementTag = false;
        this.attributeCount = 0;
        int nameStart = (this.pos - 1) + this.bufAbsoluteStart;
        int colonPos = -1;
        if (this.buf[this.pos - 1] == ':' && this.processNamespaces) {
            throw new XmlPullParserException("when namespaces processing enabled colon can not be at element name start", this, null);
        }
        while (true) {
            char ch = more();
            if (isNameChar(ch)) {
                if (ch == ':' && this.processNamespaces) {
                    if (colonPos != -1) {
                        throw new XmlPullParserException("only one colon is allowed in name of element when namespaces are enabled", this, null);
                    }
                    colonPos = (this.pos - 1) + this.bufAbsoluteStart;
                }
            } else {
                ensureElementsCapacity();
                int elLen = (this.pos - 1) - (nameStart - this.bufAbsoluteStart);
                if (this.elRawName[this.depth] == null || this.elRawName[this.depth].length < elLen) {
                    this.elRawName[this.depth] = new char[2 * elLen];
                }
                System.arraycopy(this.buf, nameStart - this.bufAbsoluteStart, this.elRawName[this.depth], 0, elLen);
                this.elRawNameEnd[this.depth] = elLen;
                this.elRawNameLine[this.depth] = this.lineNumber;
                String prefix = null;
                if (this.processNamespaces) {
                    if (colonPos != -1) {
                        String[] strArr = this.elPrefix;
                        int i = this.depth;
                        String newString = newString(this.buf, nameStart - this.bufAbsoluteStart, colonPos - nameStart);
                        strArr[i] = newString;
                        prefix = newString;
                        this.elName[this.depth] = newString(this.buf, (colonPos + 1) - this.bufAbsoluteStart, (this.pos - 2) - (colonPos - this.bufAbsoluteStart));
                    } else {
                        this.elPrefix[this.depth] = null;
                        prefix = null;
                        this.elName[this.depth] = newString(this.buf, nameStart - this.bufAbsoluteStart, elLen);
                    }
                } else {
                    this.elName[this.depth] = newString(this.buf, nameStart - this.bufAbsoluteStart, elLen);
                }
                while (true) {
                    if (isS(ch)) {
                        ch = more();
                    } else if (ch == '>') {
                        break;
                    } else if (ch == '/') {
                        if (this.emptyElementTag) {
                            throw new XmlPullParserException("repeated / in tag declaration", this, null);
                        }
                        this.emptyElementTag = true;
                        char ch2 = more();
                        if (ch2 != '>') {
                            throw new XmlPullParserException(new StringBuffer().append("expected > to end empty tag not ").append(printable(ch2)).toString(), this, null);
                        }
                    } else if (isNameStartChar(ch)) {
                        parseAttribute();
                        ch = more();
                    } else {
                        throw new XmlPullParserException(new StringBuffer().append("start tag unexpected character ").append(printable(ch)).toString(), this, null);
                    }
                }
                if (this.processNamespaces) {
                    String uri = getNamespace(prefix);
                    if (uri == null) {
                        if (prefix == null) {
                            uri = "";
                        } else {
                            throw new XmlPullParserException(new StringBuffer().append("could not determine namespace bound to element prefix ").append(prefix).toString(), this, null);
                        }
                    }
                    this.elUri[this.depth] = uri;
                    for (int i2 = 0; i2 < this.attributeCount; i2++) {
                        String attrPrefix = this.attributePrefix[i2];
                        if (attrPrefix != null) {
                            String attrUri = getNamespace(attrPrefix);
                            if (attrUri == null) {
                                throw new XmlPullParserException(new StringBuffer().append("could not determine namespace bound to attribute prefix ").append(attrPrefix).toString(), this, null);
                            }
                            this.attributeUri[i2] = attrUri;
                        } else {
                            this.attributeUri[i2] = "";
                        }
                    }
                    for (int i3 = 1; i3 < this.attributeCount; i3++) {
                        for (int j = 0; j < i3; j++) {
                            if (this.attributeUri[j] == this.attributeUri[i3] && ((this.allStringsInterned && this.attributeName[j].equals(this.attributeName[i3])) || (!this.allStringsInterned && this.attributeNameHash[j] == this.attributeNameHash[i3] && this.attributeName[j].equals(this.attributeName[i3])))) {
                                String attr1 = this.attributeName[j];
                                if (this.attributeUri[j] != null) {
                                    attr1 = new StringBuffer().append(this.attributeUri[j]).append(":").append(attr1).toString();
                                }
                                String attr2 = this.attributeName[i3];
                                if (this.attributeUri[i3] != null) {
                                    attr2 = new StringBuffer().append(this.attributeUri[i3]).append(":").append(attr2).toString();
                                }
                                throw new XmlPullParserException(new StringBuffer().append("duplicated attributes ").append(attr1).append(" and ").append(attr2).toString(), this, null);
                            }
                        }
                    }
                } else {
                    for (int i4 = 1; i4 < this.attributeCount; i4++) {
                        for (int j2 = 0; j2 < i4; j2++) {
                            if ((this.allStringsInterned && this.attributeName[j2].equals(this.attributeName[i4])) || (!this.allStringsInterned && this.attributeNameHash[j2] == this.attributeNameHash[i4] && this.attributeName[j2].equals(this.attributeName[i4]))) {
                                throw new XmlPullParserException(new StringBuffer().append("duplicated attributes ").append(this.attributeName[j2]).append(" and ").append(this.attributeName[i4]).toString(), this, null);
                            }
                        }
                    }
                }
                this.elNamespaceCount[this.depth] = this.namespaceEnd;
                this.posEnd = this.pos;
                this.eventType = 2;
                return 2;
            }
        }
    }

    protected char parseAttribute() throws XmlPullParserException, IOException {
        char ch;
        String ns;
        int prevPosStart = this.posStart + this.bufAbsoluteStart;
        int nameStart = (this.pos - 1) + this.bufAbsoluteStart;
        int colonPos = -1;
        char ch2 = this.buf[this.pos - 1];
        if (ch2 == ':' && this.processNamespaces) {
            throw new XmlPullParserException("when namespaces processing enabled colon can not be at attribute name start", this, null);
        }
        boolean startsWithXmlns = this.processNamespaces && ch2 == 'x';
        int xmlnsPos = 0;
        char more = more();
        while (true) {
            char ch3 = more;
            if (isNameChar(ch3)) {
                if (this.processNamespaces) {
                    if (startsWithXmlns && xmlnsPos < 5) {
                        xmlnsPos++;
                        if (xmlnsPos == 1) {
                            if (ch3 != 'm') {
                                startsWithXmlns = false;
                            }
                        } else if (xmlnsPos == 2) {
                            if (ch3 != 'l') {
                                startsWithXmlns = false;
                            }
                        } else if (xmlnsPos == 3) {
                            if (ch3 != 'n') {
                                startsWithXmlns = false;
                            }
                        } else if (xmlnsPos == 4) {
                            if (ch3 != 's') {
                                startsWithXmlns = false;
                            }
                        } else if (xmlnsPos == 5 && ch3 != ':') {
                            throw new XmlPullParserException("after xmlns in attribute name must be colonwhen namespaces are enabled", this, null);
                        }
                    }
                    if (ch3 != ':') {
                        continue;
                    } else if (colonPos != -1) {
                        throw new XmlPullParserException("only one colon is allowed in attribute name when namespaces are enabled", this, null);
                    } else {
                        colonPos = (this.pos - 1) + this.bufAbsoluteStart;
                    }
                }
                more = more();
            } else {
                ensureAttributesCapacity(this.attributeCount);
                String name = null;
                if (this.processNamespaces) {
                    if (xmlnsPos < 4) {
                        startsWithXmlns = false;
                    }
                    if (startsWithXmlns) {
                        if (colonPos != -1) {
                            int nameLen = (this.pos - 2) - (colonPos - this.bufAbsoluteStart);
                            if (nameLen == 0) {
                                throw new XmlPullParserException("namespace prefix is required after xmlns:  when namespaces are enabled", this, null);
                            }
                            name = newString(this.buf, (colonPos - this.bufAbsoluteStart) + 1, nameLen);
                        }
                    } else {
                        if (colonPos != -1) {
                            int prefixLen = colonPos - nameStart;
                            this.attributePrefix[this.attributeCount] = newString(this.buf, nameStart - this.bufAbsoluteStart, prefixLen);
                            int nameLen2 = (this.pos - 2) - (colonPos - this.bufAbsoluteStart);
                            String[] strArr = this.attributeName;
                            int i = this.attributeCount;
                            String newString = newString(this.buf, (colonPos - this.bufAbsoluteStart) + 1, nameLen2);
                            strArr[i] = newString;
                            name = newString;
                        } else {
                            this.attributePrefix[this.attributeCount] = null;
                            String[] strArr2 = this.attributeName;
                            int i2 = this.attributeCount;
                            String newString2 = newString(this.buf, nameStart - this.bufAbsoluteStart, (this.pos - 1) - (nameStart - this.bufAbsoluteStart));
                            strArr2[i2] = newString2;
                            name = newString2;
                        }
                        if (!this.allStringsInterned) {
                            this.attributeNameHash[this.attributeCount] = name.hashCode();
                        }
                    }
                } else {
                    String[] strArr3 = this.attributeName;
                    int i3 = this.attributeCount;
                    String newString3 = newString(this.buf, nameStart - this.bufAbsoluteStart, (this.pos - 1) - (nameStart - this.bufAbsoluteStart));
                    strArr3[i3] = newString3;
                    name = newString3;
                    if (!this.allStringsInterned) {
                        this.attributeNameHash[this.attributeCount] = name.hashCode();
                    }
                }
                while (isS(ch3)) {
                    ch3 = more();
                }
                if (ch3 != '=') {
                    throw new XmlPullParserException("expected = after attribute name", this, null);
                }
                char more2 = more();
                while (true) {
                    ch = more2;
                    if (!isS(ch)) {
                        break;
                    }
                    more2 = more();
                }
                if (ch != '\"' && ch != '\'') {
                    throw new XmlPullParserException(new StringBuffer().append("attribute value must start with quotation or apostrophe not ").append(printable(ch)).toString(), this, null);
                }
                boolean normalizedCR = false;
                this.usePC = false;
                this.pcStart = this.pcEnd;
                this.posStart = this.pos;
                while (true) {
                    char ch4 = more();
                    if (ch4 != ch) {
                        if (ch4 == '<') {
                            throw new XmlPullParserException("markup not allowed inside attribute value - illegal < ", this, null);
                        }
                        if (ch4 == '&') {
                            this.posEnd = this.pos - 1;
                            if (!this.usePC) {
                                boolean hadCharData = this.posEnd > this.posStart;
                                if (hadCharData) {
                                    joinPC();
                                } else {
                                    this.usePC = true;
                                    this.pcEnd = 0;
                                    this.pcStart = 0;
                                }
                            }
                            char[] resolvedEntity = parseEntityRef();
                            if (resolvedEntity == null) {
                                if (this.entityRefName == null) {
                                    this.entityRefName = newString(this.buf, this.posStart, this.posEnd - this.posStart);
                                }
                                throw new XmlPullParserException(new StringBuffer().append("could not resolve entity named '").append(printable(this.entityRefName)).append("'").toString(), this, null);
                            }
                            for (char c : resolvedEntity) {
                                if (this.pcEnd >= this.pc.length) {
                                    ensurePC(this.pcEnd);
                                }
                                char[] cArr = this.pc;
                                int i4 = this.pcEnd;
                                this.pcEnd = i4 + 1;
                                cArr[i4] = c;
                            }
                        } else if (ch4 == '\t' || ch4 == '\n' || ch4 == '\r') {
                            if (!this.usePC) {
                                this.posEnd = this.pos - 1;
                                if (this.posEnd > this.posStart) {
                                    joinPC();
                                } else {
                                    this.usePC = true;
                                    this.pcStart = 0;
                                    this.pcEnd = 0;
                                }
                            }
                            if (this.pcEnd >= this.pc.length) {
                                ensurePC(this.pcEnd);
                            }
                            if (ch4 != '\n' || !normalizedCR) {
                                char[] cArr2 = this.pc;
                                int i5 = this.pcEnd;
                                this.pcEnd = i5 + 1;
                                cArr2[i5] = ' ';
                            }
                        } else if (this.usePC) {
                            if (this.pcEnd >= this.pc.length) {
                                ensurePC(this.pcEnd);
                            }
                            char[] cArr3 = this.pc;
                            int i6 = this.pcEnd;
                            this.pcEnd = i6 + 1;
                            cArr3[i6] = ch4;
                        }
                        normalizedCR = ch4 == '\r';
                    } else {
                        if (this.processNamespaces && startsWithXmlns) {
                            if (!this.usePC) {
                                ns = newStringIntern(this.buf, this.posStart, (this.pos - 1) - this.posStart);
                            } else {
                                ns = newStringIntern(this.pc, this.pcStart, this.pcEnd - this.pcStart);
                            }
                            ensureNamespacesCapacity(this.namespaceEnd);
                            int prefixHash = -1;
                            if (colonPos != -1) {
                                if (ns.length() == 0) {
                                    throw new XmlPullParserException("non-default namespace can not be declared to be empty string", this, null);
                                }
                                this.namespacePrefix[this.namespaceEnd] = name;
                                if (!this.allStringsInterned) {
                                    int[] iArr = this.namespacePrefixHash;
                                    int i7 = this.namespaceEnd;
                                    int hashCode = name.hashCode();
                                    iArr[i7] = hashCode;
                                    prefixHash = hashCode;
                                }
                            } else {
                                this.namespacePrefix[this.namespaceEnd] = null;
                                if (!this.allStringsInterned) {
                                    this.namespacePrefixHash[this.namespaceEnd] = -1;
                                    prefixHash = -1;
                                }
                            }
                            this.namespaceUri[this.namespaceEnd] = ns;
                            int startNs = this.elNamespaceCount[this.depth - 1];
                            for (int i8 = this.namespaceEnd - 1; i8 >= startNs; i8--) {
                                if (((this.allStringsInterned || name == null) && this.namespacePrefix[i8] == name) || (!this.allStringsInterned && name != null && this.namespacePrefixHash[i8] == prefixHash && name.equals(this.namespacePrefix[i8]))) {
                                    String s = name == null ? "default" : new StringBuffer().append("'").append(name).append("'").toString();
                                    throw new XmlPullParserException(new StringBuffer().append("duplicated namespace declaration for ").append(s).append(" prefix").toString(), this, null);
                                }
                            }
                            this.namespaceEnd++;
                        } else {
                            if (!this.usePC) {
                                this.attributeValue[this.attributeCount] = new String(this.buf, this.posStart, (this.pos - 1) - this.posStart);
                            } else {
                                this.attributeValue[this.attributeCount] = new String(this.pc, this.pcStart, this.pcEnd - this.pcStart);
                            }
                            this.attributeCount++;
                        }
                        this.posStart = prevPosStart - this.bufAbsoluteStart;
                        return ch4;
                    }
                }
            }
        }
    }

    protected char[] parseEntityRef() throws XmlPullParserException, IOException {
        char ch;
        char ch2;
        this.entityRefName = null;
        this.posStart = this.pos;
        char ch3 = more();
        if (ch3 == '#') {
            char charRef = 0;
            char ch4 = more();
            if (ch4 == 'x') {
                while (true) {
                    ch2 = more();
                    if (ch2 >= '0' && ch2 <= '9') {
                        charRef = (char) ((charRef * 16) + (ch2 - '0'));
                    } else if (ch2 >= 'a' && ch2 <= 'f') {
                        charRef = (char) ((charRef * 16) + (ch2 - 'W'));
                    } else if (ch2 < 'A' || ch2 > 'F') {
                        break;
                    } else {
                        charRef = (char) ((charRef * 16) + (ch2 - '7'));
                    }
                }
                if (ch2 != ';') {
                    throw new XmlPullParserException(new StringBuffer().append("character reference (with hex value) may not contain ").append(printable(ch2)).toString(), this, null);
                }
            } else {
                while (ch4 >= '0' && ch4 <= '9') {
                    charRef = (char) ((charRef * '\n') + (ch4 - '0'));
                    ch4 = more();
                }
                if (ch4 != ';') {
                    throw new XmlPullParserException(new StringBuffer().append("character reference (with decimal value) may not contain ").append(printable(ch4)).toString(), this, null);
                }
            }
            this.posEnd = this.pos - 1;
            this.charRefOneCharBuf[0] = charRef;
            if (this.tokenize) {
                this.text = newString(this.charRefOneCharBuf, 0, 1);
            }
            return this.charRefOneCharBuf;
        } else if (!isNameStartChar(ch3)) {
            throw new XmlPullParserException(new StringBuffer().append("entity reference names can not start with character '").append(printable(ch3)).append("'").toString(), this, null);
        } else {
            do {
                ch = more();
                if (ch == ';') {
                    this.posEnd = this.pos - 1;
                    int len = this.posEnd - this.posStart;
                    if (len == 2 && this.buf[this.posStart] == 'l' && this.buf[this.posStart + 1] == 't') {
                        if (this.tokenize) {
                            this.text = "<";
                        }
                        this.charRefOneCharBuf[0] = '<';
                        return this.charRefOneCharBuf;
                    } else if (len == 3 && this.buf[this.posStart] == 'a' && this.buf[this.posStart + 1] == 'm' && this.buf[this.posStart + 2] == 'p') {
                        if (this.tokenize) {
                            this.text = "&";
                        }
                        this.charRefOneCharBuf[0] = '&';
                        return this.charRefOneCharBuf;
                    } else if (len == 2 && this.buf[this.posStart] == 'g' && this.buf[this.posStart + 1] == 't') {
                        if (this.tokenize) {
                            this.text = ">";
                        }
                        this.charRefOneCharBuf[0] = '>';
                        return this.charRefOneCharBuf;
                    } else if (len == 4 && this.buf[this.posStart] == 'a' && this.buf[this.posStart + 1] == 'p' && this.buf[this.posStart + 2] == 'o' && this.buf[this.posStart + 3] == 's') {
                        if (this.tokenize) {
                            this.text = "'";
                        }
                        this.charRefOneCharBuf[0] = '\'';
                        return this.charRefOneCharBuf;
                    } else if (len == 4 && this.buf[this.posStart] == 'q' && this.buf[this.posStart + 1] == 'u' && this.buf[this.posStart + 2] == 'o' && this.buf[this.posStart + 3] == 't') {
                        if (this.tokenize) {
                            this.text = "\"";
                        }
                        this.charRefOneCharBuf[0] = '\"';
                        return this.charRefOneCharBuf;
                    } else {
                        char[] result = lookuEntityReplacement(len);
                        if (result != null) {
                            return result;
                        }
                        if (this.tokenize) {
                            this.text = null;
                            return null;
                        }
                        return null;
                    }
                }
            } while (isNameChar(ch));
            throw new XmlPullParserException(new StringBuffer().append("entity reference name can not contain character ").append(printable(ch)).append("'").toString(), this, null);
        }
    }

    protected char[] lookuEntityReplacement(int entitNameLen) throws XmlPullParserException, IOException {
        if (!this.allStringsInterned) {
            int hash = fastHash(this.buf, this.posStart, this.posEnd - this.posStart);
            for (int i = this.entityEnd - 1; i >= 0; i--) {
                if (hash == this.entityNameHash[i] && entitNameLen == this.entityNameBuf[i].length) {
                    char[] entityBuf = this.entityNameBuf[i];
                    for (int j = 0; j < entitNameLen; j++) {
                        if (this.buf[this.posStart + j] != entityBuf[j]) {
                            break;
                        }
                    }
                    if (this.tokenize) {
                        this.text = this.entityReplacement[i];
                    }
                    return this.entityReplacementBuf[i];
                }
            }
            return null;
        }
        this.entityRefName = newString(this.buf, this.posStart, this.posEnd - this.posStart);
        for (int i2 = this.entityEnd - 1; i2 >= 0; i2--) {
            if (this.entityRefName == this.entityName[i2]) {
                if (this.tokenize) {
                    this.text = this.entityReplacement[i2];
                }
                return this.entityReplacementBuf[i2];
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x007f, code lost:
        throw new org.xmlpull.v1.XmlPullParserException(new java.lang.StringBuffer().append("in comment after two dashes (--) next character must be > not ").append(printable(r0)).toString(), r6, null);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void parseComment() throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            Method dump skipped, instructions count: 481
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.xmlpull.mxp1.MXParser.parseComment():void");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x00ef, code lost:
        if (r0 <= 3) goto L98;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x00fd, code lost:
        throw new org.xmlpull.v1.XmlPullParserException("processing instruction can not have PITarget with reserveld xml name", r6, null);
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x0106, code lost:
        if (r6.buf[r0] == 'x') goto L106;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x0113, code lost:
        if (r6.buf[r0 + 1] == 'm') goto L106;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x0120, code lost:
        if (r6.buf[r0 + 2] == 'l') goto L106;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x012e, code lost:
        throw new org.xmlpull.v1.XmlPullParserException("XMLDecl must have xml name in lowercase", r6, null);
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x012f, code lost:
        parseXmlDecl(r14);
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x0139, code lost:
        if (r6.tokenize == false) goto L109;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x013c, code lost:
        r6.posEnd = r6.pos - 2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x0146, code lost:
        r0 = (r0 - r6.bufAbsoluteStart) + 3;
        r0 = (r6.pos - 2) - r0;
        r6.xmlDeclContent = newString(r6.buf, r0, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x016c, code lost:
        return false;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean parsePI() throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            Method dump skipped, instructions count: 712
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.xmlpull.mxp1.MXParser.parsePI():boolean");
    }

    static {
        setNameStart(':');
        char c = 'A';
        while (true) {
            char ch = c;
            if (ch > 'Z') {
                break;
            }
            setNameStart(ch);
            c = (char) (ch + 1);
        }
        setNameStart('_');
        char c2 = 'a';
        while (true) {
            char ch2 = c2;
            if (ch2 > 'z') {
                break;
            }
            setNameStart(ch2);
            c2 = (char) (ch2 + 1);
        }
        char c3 = 192;
        while (true) {
            char ch3 = c3;
            if (ch3 > 767) {
                break;
            }
            setNameStart(ch3);
            c3 = (char) (ch3 + 1);
        }
        char c4 = 880;
        while (true) {
            char ch4 = c4;
            if (ch4 > 893) {
                break;
            }
            setNameStart(ch4);
            c4 = (char) (ch4 + 1);
        }
        char c5 = 895;
        while (true) {
            char ch5 = c5;
            if (ch5 >= 1024) {
                break;
            }
            setNameStart(ch5);
            c5 = (char) (ch5 + 1);
        }
        setName('-');
        setName('.');
        char c6 = '0';
        while (true) {
            char ch6 = c6;
            if (ch6 > '9') {
                break;
            }
            setName(ch6);
            c6 = (char) (ch6 + 1);
        }
        setName((char) 183);
        char c7 = 768;
        while (true) {
            char ch7 = c7;
            if (ch7 > 879) {
                return;
            }
            setName(ch7);
            c7 = (char) (ch7 + 1);
        }
    }

    protected void parseXmlDecl(char ch) throws XmlPullParserException, IOException {
        this.preventBufferCompaction = true;
        this.bufStart = 0;
        char ch2 = skipS(requireInput(skipS(ch), VERSION));
        if (ch2 != '=') {
            throw new XmlPullParserException(new StringBuffer().append("expected equals sign (=) after version and not ").append(printable(ch2)).toString(), this, null);
        }
        char ch3 = more();
        char ch4 = skipS(ch3);
        if (ch4 != '\'' && ch4 != '\"') {
            throw new XmlPullParserException(new StringBuffer().append("expected apostrophe (') or quotation mark (\") after version and not ").append(printable(ch4)).toString(), this, null);
        }
        int versionStart = this.pos;
        char more = more();
        while (true) {
            char ch5 = more;
            if (ch5 != ch4) {
                if ((ch5 < 'a' || ch5 > 'z') && ((ch5 < 'A' || ch5 > 'Z') && ((ch5 < '0' || ch5 > '9') && ch5 != '_' && ch5 != '.' && ch5 != ':' && ch5 != '-'))) {
                    throw new XmlPullParserException(new StringBuffer().append("<?xml version value expected to be in ([a-zA-Z0-9_.:] | '-') not ").append(printable(ch5)).toString(), this, null);
                }
                more = more();
            } else {
                int versionEnd = this.pos - 1;
                parseXmlDeclWithVersion(versionStart, versionEnd);
                this.preventBufferCompaction = false;
                return;
            }
        }
    }

    protected void parseXmlDeclWithVersion(int versionStart, int versionEnd) throws XmlPullParserException, IOException {
        char ch;
        String str = this.inputEncoding;
        if (versionEnd - versionStart != 3 || this.buf[versionStart] != '1' || this.buf[versionStart + 1] != '.' || this.buf[versionStart + 2] != '0') {
            throw new XmlPullParserException(new StringBuffer().append("only 1.0 is supported as <?xml version not '").append(printable(new String(this.buf, versionStart, versionEnd - versionStart))).append("'").toString(), this, null);
        }
        this.xmlDeclVersion = newString(this.buf, versionStart, versionEnd - versionStart);
        char ch2 = skipS(more());
        if (ch2 == 'e') {
            char ch3 = skipS(requireInput(more(), NCODING));
            if (ch3 != '=') {
                throw new XmlPullParserException(new StringBuffer().append("expected equals sign (=) after encoding and not ").append(printable(ch3)).toString(), this, null);
            }
            char ch4 = skipS(more());
            if (ch4 != '\'' && ch4 != '\"') {
                throw new XmlPullParserException(new StringBuffer().append("expected apostrophe (') or quotation mark (\") after encoding and not ").append(printable(ch4)).toString(), this, null);
            }
            int encodingStart = this.pos;
            char ch5 = more();
            if ((ch5 < 'a' || ch5 > 'z') && (ch5 < 'A' || ch5 > 'Z')) {
                throw new XmlPullParserException(new StringBuffer().append("<?xml encoding name expected to start with [A-Za-z] not ").append(printable(ch5)).toString(), this, null);
            }
            char more = more();
            while (true) {
                char ch6 = more;
                if (ch6 != ch4) {
                    if ((ch6 < 'a' || ch6 > 'z') && ((ch6 < 'A' || ch6 > 'Z') && ((ch6 < '0' || ch6 > '9') && ch6 != '.' && ch6 != '_' && ch6 != '-'))) {
                        throw new XmlPullParserException(new StringBuffer().append("<?xml encoding value expected to be in ([A-Za-z0-9._] | '-') not ").append(printable(ch6)).toString(), this, null);
                    }
                    more = more();
                } else {
                    int encodingEnd = this.pos - 1;
                    this.inputEncoding = newString(this.buf, encodingStart, encodingEnd - encodingStart);
                    ch2 = more();
                    break;
                }
            }
        }
        char ch7 = skipS(ch2);
        if (ch7 == 's') {
            char ch8 = skipS(requireInput(more(), TANDALONE));
            if (ch8 != '=') {
                throw new XmlPullParserException(new StringBuffer().append("expected equals sign (=) after standalone and not ").append(printable(ch8)).toString(), this, null);
            }
            char ch9 = skipS(more());
            if (ch9 != '\'' && ch9 != '\"') {
                throw new XmlPullParserException(new StringBuffer().append("expected apostrophe (') or quotation mark (\") after encoding and not ").append(printable(ch9)).toString(), this, null);
            }
            int i = this.pos;
            char ch10 = more();
            if (ch10 == 'y') {
                ch = requireInput(ch10, YES);
                this.xmlDeclStandalone = new Boolean(true);
            } else if (ch10 == 'n') {
                ch = requireInput(ch10, NO);
                this.xmlDeclStandalone = new Boolean(false);
            } else {
                throw new XmlPullParserException(new StringBuffer().append("expected 'yes' or 'no' after standalone and not ").append(printable(ch10)).toString(), this, null);
            }
            if (ch != ch9) {
                throw new XmlPullParserException(new StringBuffer().append("expected ").append(ch9).append(" after standalone value not ").append(printable(ch)).toString(), this, null);
            }
            ch7 = more();
        }
        char ch11 = skipS(ch7);
        if (ch11 != '?') {
            throw new XmlPullParserException(new StringBuffer().append("expected ?> as last part of <?xml not ").append(printable(ch11)).toString(), this, null);
        }
        char ch12 = more();
        if (ch12 != '>') {
            throw new XmlPullParserException(new StringBuffer().append("expected ?> as last part of <?xml not ").append(printable(ch12)).toString(), this, null);
        }
    }

    protected void parseDocdecl() throws XmlPullParserException, IOException {
        char ch = more();
        if (ch != 'O') {
            throw new XmlPullParserException("expected <!DOCTYPE", this, null);
        }
        char ch2 = more();
        if (ch2 != 'C') {
            throw new XmlPullParserException("expected <!DOCTYPE", this, null);
        }
        char ch3 = more();
        if (ch3 != 'T') {
            throw new XmlPullParserException("expected <!DOCTYPE", this, null);
        }
        char ch4 = more();
        if (ch4 != 'Y') {
            throw new XmlPullParserException("expected <!DOCTYPE", this, null);
        }
        char ch5 = more();
        if (ch5 != 'P') {
            throw new XmlPullParserException("expected <!DOCTYPE", this, null);
        }
        char ch6 = more();
        if (ch6 != 'E') {
            throw new XmlPullParserException("expected <!DOCTYPE", this, null);
        }
        this.posStart = this.pos;
        int bracketLevel = 0;
        boolean normalizeIgnorableWS = this.tokenize && !this.roundtripSupported;
        boolean normalizedCR = false;
        while (true) {
            char ch7 = more();
            if (ch7 == '[') {
                bracketLevel++;
            }
            if (ch7 == ']') {
                bracketLevel--;
            }
            if (ch7 != '>' || bracketLevel != 0) {
                if (normalizeIgnorableWS) {
                    if (ch7 == '\r') {
                        normalizedCR = true;
                        if (!this.usePC) {
                            this.posEnd = this.pos - 1;
                            if (this.posEnd > this.posStart) {
                                joinPC();
                            } else {
                                this.usePC = true;
                                this.pcEnd = 0;
                                this.pcStart = 0;
                            }
                        }
                        if (this.pcEnd >= this.pc.length) {
                            ensurePC(this.pcEnd);
                        }
                        char[] cArr = this.pc;
                        int i = this.pcEnd;
                        this.pcEnd = i + 1;
                        cArr[i] = '\n';
                    } else if (ch7 == '\n') {
                        if (!normalizedCR && this.usePC) {
                            if (this.pcEnd >= this.pc.length) {
                                ensurePC(this.pcEnd);
                            }
                            char[] cArr2 = this.pc;
                            int i2 = this.pcEnd;
                            this.pcEnd = i2 + 1;
                            cArr2[i2] = '\n';
                        }
                        normalizedCR = false;
                    } else {
                        if (this.usePC) {
                            if (this.pcEnd >= this.pc.length) {
                                ensurePC(this.pcEnd);
                            }
                            char[] cArr3 = this.pc;
                            int i3 = this.pcEnd;
                            this.pcEnd = i3 + 1;
                            cArr3[i3] = ch7;
                        }
                        normalizedCR = false;
                    }
                }
            } else {
                this.posEnd = this.pos - 1;
                return;
            }
        }
    }

    protected void parseCDSect(boolean hadCharData) throws XmlPullParserException, IOException {
        char ch = more();
        if (ch != 'C') {
            throw new XmlPullParserException("expected <[CDATA[ for comment start", this, null);
        }
        char ch2 = more();
        if (ch2 != 'D') {
            throw new XmlPullParserException("expected <[CDATA[ for comment start", this, null);
        }
        char ch3 = more();
        if (ch3 != 'A') {
            throw new XmlPullParserException("expected <[CDATA[ for comment start", this, null);
        }
        char ch4 = more();
        if (ch4 != 'T') {
            throw new XmlPullParserException("expected <[CDATA[ for comment start", this, null);
        }
        char ch5 = more();
        if (ch5 != 'A') {
            throw new XmlPullParserException("expected <[CDATA[ for comment start", this, null);
        }
        char ch6 = more();
        if (ch6 != '[') {
            throw new XmlPullParserException("expected <![CDATA[ for comment start", this, null);
        }
        int cdStart = this.pos + this.bufAbsoluteStart;
        int curLine = this.lineNumber;
        int curColumn = this.columnNumber;
        boolean normalizeInput = (this.tokenize && this.roundtripSupported) ? false : true;
        if (normalizeInput && hadCharData) {
            try {
                if (!this.usePC) {
                    if (this.posEnd > this.posStart) {
                        joinPC();
                    } else {
                        this.usePC = true;
                        this.pcEnd = 0;
                        this.pcStart = 0;
                    }
                }
            } catch (EOFException ex) {
                throw new XmlPullParserException(new StringBuffer().append("CDATA section started on line ").append(curLine).append(" and column ").append(curColumn).append(" was not closed").toString(), this, ex);
            }
        }
        boolean seenBracket = false;
        boolean seenBracketBracket = false;
        boolean normalizedCR = false;
        while (true) {
            char ch7 = more();
            if (ch7 == ']') {
                if (!seenBracket) {
                    seenBracket = true;
                } else {
                    seenBracketBracket = true;
                }
            } else if (ch7 == '>') {
                if (seenBracket && seenBracketBracket) {
                    break;
                }
                seenBracketBracket = false;
                seenBracket = false;
            } else if (seenBracket) {
                seenBracket = false;
            }
            if (normalizeInput) {
                if (ch7 == '\r') {
                    normalizedCR = true;
                    this.posStart = cdStart - this.bufAbsoluteStart;
                    this.posEnd = this.pos - 1;
                    if (!this.usePC) {
                        if (this.posEnd > this.posStart) {
                            joinPC();
                        } else {
                            this.usePC = true;
                            this.pcEnd = 0;
                            this.pcStart = 0;
                        }
                    }
                    if (this.pcEnd >= this.pc.length) {
                        ensurePC(this.pcEnd);
                    }
                    char[] cArr = this.pc;
                    int i = this.pcEnd;
                    this.pcEnd = i + 1;
                    cArr[i] = '\n';
                } else if (ch7 == '\n') {
                    if (!normalizedCR && this.usePC) {
                        if (this.pcEnd >= this.pc.length) {
                            ensurePC(this.pcEnd);
                        }
                        char[] cArr2 = this.pc;
                        int i2 = this.pcEnd;
                        this.pcEnd = i2 + 1;
                        cArr2[i2] = '\n';
                    }
                    normalizedCR = false;
                } else {
                    if (this.usePC) {
                        if (this.pcEnd >= this.pc.length) {
                            ensurePC(this.pcEnd);
                        }
                        char[] cArr3 = this.pc;
                        int i3 = this.pcEnd;
                        this.pcEnd = i3 + 1;
                        cArr3[i3] = ch7;
                    }
                    normalizedCR = false;
                }
            }
        }
        if (normalizeInput && this.usePC) {
            this.pcEnd -= 2;
        }
        this.posStart = cdStart - this.bufAbsoluteStart;
        this.posEnd = this.pos - 3;
    }

    protected void fillBuf() throws IOException, XmlPullParserException {
        if (this.reader == null) {
            throw new XmlPullParserException("reader must be set before parsing is started");
        }
        if (this.bufEnd > this.bufSoftLimit) {
            boolean compact = this.bufStart > this.bufSoftLimit;
            boolean expand = false;
            if (this.preventBufferCompaction) {
                compact = false;
                expand = true;
            } else if (!compact) {
                if (this.bufStart < this.buf.length / 2) {
                    expand = true;
                } else {
                    compact = true;
                }
            }
            if (compact) {
                System.arraycopy(this.buf, this.bufStart, this.buf, 0, this.bufEnd - this.bufStart);
            } else if (expand) {
                int newSize = 2 * this.buf.length;
                char[] newBuf = new char[newSize];
                System.arraycopy(this.buf, this.bufStart, newBuf, 0, this.bufEnd - this.bufStart);
                this.buf = newBuf;
                if (this.bufLoadFactor > 0) {
                    this.bufSoftLimit = (int) ((this.bufLoadFactor * this.buf.length) / 100);
                }
            } else {
                throw new XmlPullParserException("internal error in fillBuffer()");
            }
            this.bufEnd -= this.bufStart;
            this.pos -= this.bufStart;
            this.posStart -= this.bufStart;
            this.posEnd -= this.bufStart;
            this.bufAbsoluteStart += this.bufStart;
            this.bufStart = 0;
        }
        int len = this.buf.length - this.bufEnd > 8192 ? 8192 : this.buf.length - this.bufEnd;
        int ret = this.reader.read(this.buf, this.bufEnd, len);
        if (ret <= 0) {
            if (ret == -1) {
                if (this.bufAbsoluteStart == 0 && this.pos == 0) {
                    throw new EOFException("input contained no data");
                }
                if (this.seenRoot && this.depth == 0) {
                    this.reachedEnd = true;
                    return;
                }
                StringBuffer expectedTagStack = new StringBuffer();
                if (this.depth > 0) {
                    expectedTagStack.append(" - expected end tag");
                    if (this.depth > 1) {
                        expectedTagStack.append("s");
                    }
                    expectedTagStack.append(Instruction.argsep);
                    for (int i = this.depth; i > 0; i--) {
                        String tagName = new String(this.elRawName[i], 0, this.elRawNameEnd[i]);
                        expectedTagStack.append("</").append(tagName).append('>');
                    }
                    expectedTagStack.append(" to close");
                    for (int i2 = this.depth; i2 > 0; i2--) {
                        if (i2 != this.depth) {
                            expectedTagStack.append(" and");
                        }
                        String tagName2 = new String(this.elRawName[i2], 0, this.elRawNameEnd[i2]);
                        expectedTagStack.append(new StringBuffer().append(" start tag <").append(tagName2).append(">").toString());
                        expectedTagStack.append(new StringBuffer().append(" from line ").append(this.elRawNameLine[i2]).toString());
                    }
                    expectedTagStack.append(", parser stopped on");
                }
                throw new EOFException(new StringBuffer().append("no more data available").append(expectedTagStack.toString()).append(getPositionDescription()).toString());
            } else {
                throw new IOException(new StringBuffer().append("error reading input, returned ").append(ret).toString());
            }
        }
        this.bufEnd += ret;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public char more() throws IOException, XmlPullParserException {
        if (this.pos >= this.bufEnd) {
            fillBuf();
            if (this.reachedEnd) {
                return (char) 65535;
            }
        }
        char[] cArr = this.buf;
        int i = this.pos;
        this.pos = i + 1;
        char ch = cArr[i];
        if (ch == '\n') {
            this.lineNumber++;
            this.columnNumber = 1;
        } else {
            this.columnNumber++;
        }
        return ch;
    }

    protected void ensurePC(int end) {
        int newSize = end > 8192 ? 2 * end : 16384;
        char[] newPC = new char[newSize];
        System.arraycopy(this.pc, 0, newPC, 0, this.pcEnd);
        this.pc = newPC;
    }

    protected void joinPC() {
        int len = this.posEnd - this.posStart;
        int newEnd = this.pcEnd + len + 1;
        if (newEnd >= this.pc.length) {
            ensurePC(newEnd);
        }
        System.arraycopy(this.buf, this.posStart, this.pc, this.pcEnd, len);
        this.pcEnd += len;
        this.usePC = true;
    }

    protected char requireInput(char ch, char[] input) throws XmlPullParserException, IOException {
        for (int i = 0; i < input.length; i++) {
            if (ch != input[i]) {
                throw new XmlPullParserException(new StringBuffer().append("expected ").append(printable(input[i])).append(" in ").append(new String(input)).append(" and not ").append(printable(ch)).toString(), this, null);
            }
            ch = more();
        }
        return ch;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public char requireNextS() throws XmlPullParserException, IOException {
        char ch = more();
        if (!isS(ch)) {
            throw new XmlPullParserException(new StringBuffer().append("white space is required and not ").append(printable(ch)).toString(), this, null);
        }
        return skipS(ch);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public char skipS(char ch) throws XmlPullParserException, IOException {
        while (isS(ch)) {
            ch = more();
        }
        return ch;
    }

    private static final void setName(char ch) {
        lookupNameChar[ch] = true;
    }

    private static final void setNameStart(char ch) {
        lookupNameStartChar[ch] = true;
        setName(ch);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isNameStartChar(char ch) {
        return (ch < 1024 && lookupNameStartChar[ch]) || (ch >= 1024 && ch <= 8231) || ((ch >= 8234 && ch <= 8591) || (ch >= 10240 && ch <= 65519));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isNameChar(char ch) {
        return (ch < 1024 && lookupNameChar[ch]) || (ch >= 1024 && ch <= 8231) || ((ch >= 8234 && ch <= 8591) || (ch >= 10240 && ch <= 65519));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isS(char ch) {
        return ch == ' ' || ch == '\n' || ch == '\r' || ch == '\t';
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String printable(char ch) {
        if (ch == '\n') {
            return "\\n";
        }
        if (ch == '\r') {
            return "\\r";
        }
        if (ch == '\t') {
            return "\\t";
        }
        if (ch == '\'') {
            return "\\'";
        }
        if (ch > 127 || ch < ' ') {
            return new StringBuffer().append("\\u").append(Integer.toHexString(ch)).toString();
        }
        return new StringBuffer().append("").append(ch).toString();
    }

    protected String printable(String s) {
        if (s == null) {
            return null;
        }
        int sLen = s.length();
        StringBuffer buf = new StringBuffer(sLen + 10);
        for (int i = 0; i < sLen; i++) {
            buf.append(printable(s.charAt(i)));
        }
        return buf.toString();
    }
}
