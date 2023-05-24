package org.xmlpull.v1.wrapper.classic;

import java.io.IOException;
import java.io.StringReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;
import org.xmlpull.v1.wrapper.XmlPullParserWrapper;
import org.xmlpull.v1.wrapper.XmlPullWrapperFactory;
import org.xmlpull.v1.wrapper.XmlSerializerWrapper;
/* loaded from: gencallgraphv3.jar:xpp3-1.1.4c.jar:org/xmlpull/v1/wrapper/classic/StaticXmlSerializerWrapper.class */
public class StaticXmlSerializerWrapper extends XmlSerializerDelegate implements XmlSerializerWrapper {
    private static final String PROPERTY_XMLDECL_STANDALONE = "http://xmlpull.org/v1/doc/features.html#xmldecl-standalone";
    private static final boolean TRACE_SIZING = false;
    protected String currentNs;
    protected XmlPullWrapperFactory wf;
    protected XmlPullParserWrapper fragmentParser;
    protected int namespaceEnd;
    protected String[] namespacePrefix;
    protected String[] namespaceUri;
    protected int[] namespaceDepth;

    public StaticXmlSerializerWrapper(XmlSerializer xs, XmlPullWrapperFactory wf) {
        super(xs);
        this.namespaceEnd = 0;
        this.namespacePrefix = new String[8];
        this.namespaceUri = new String[this.namespacePrefix.length];
        this.namespaceDepth = new int[this.namespacePrefix.length];
        this.wf = wf;
    }

    @Override // org.xmlpull.v1.wrapper.XmlSerializerWrapper
    public String getCurrentNamespaceForElements() {
        return this.currentNs;
    }

    @Override // org.xmlpull.v1.wrapper.XmlSerializerWrapper
    public String setCurrentNamespaceForElements(String value) {
        String old = this.currentNs;
        this.currentNs = value;
        return old;
    }

    @Override // org.xmlpull.v1.wrapper.XmlSerializerWrapper
    public XmlSerializerWrapper attribute(String name, String value) throws IOException, IllegalArgumentException, IllegalStateException {
        this.xs.attribute(null, name, value);
        return this;
    }

    @Override // org.xmlpull.v1.wrapper.XmlSerializerWrapper
    public XmlSerializerWrapper startTag(String name) throws IOException, IllegalArgumentException, IllegalStateException {
        this.xs.startTag(this.currentNs, name);
        return this;
    }

    @Override // org.xmlpull.v1.wrapper.XmlSerializerWrapper
    public XmlSerializerWrapper endTag(String name) throws IOException, IllegalArgumentException, IllegalStateException {
        endTag(this.currentNs, name);
        return this;
    }

    @Override // org.xmlpull.v1.wrapper.XmlSerializerWrapper
    public XmlSerializerWrapper element(String elementName, String elementText) throws IOException, XmlPullParserException {
        return element(this.currentNs, elementName, elementText);
    }

    @Override // org.xmlpull.v1.wrapper.XmlSerializerWrapper
    public XmlSerializerWrapper element(String namespace, String elementName, String elementText) throws IOException, XmlPullParserException {
        if (elementName == null) {
            throw new XmlPullParserException("name for element can not be null");
        }
        this.xs.startTag(namespace, elementName);
        if (elementText == null) {
            this.xs.attribute("http://www.w3.org/2001/XMLSchema-instance", "nil", "true");
        } else {
            this.xs.text(elementText);
        }
        this.xs.endTag(namespace, elementName);
        return this;
    }

    private void ensureNamespacesCapacity() {
        int newSize = this.namespaceEnd > 7 ? 2 * this.namespaceEnd : 8;
        String[] newNamespacePrefix = new String[newSize];
        String[] newNamespaceUri = new String[newSize];
        int[] newNamespaceDepth = new int[newSize];
        if (this.namespacePrefix != null) {
            System.arraycopy(this.namespacePrefix, 0, newNamespacePrefix, 0, this.namespaceEnd);
            System.arraycopy(this.namespaceUri, 0, newNamespaceUri, 0, this.namespaceEnd);
            System.arraycopy(this.namespaceDepth, 0, newNamespaceDepth, 0, this.namespaceEnd);
        }
        this.namespacePrefix = newNamespacePrefix;
        this.namespaceUri = newNamespaceUri;
        this.namespaceDepth = newNamespaceDepth;
    }

    @Override // org.xmlpull.v1.wrapper.classic.XmlSerializerDelegate, org.xmlpull.v1.XmlSerializer
    public void setPrefix(String prefix, String namespace) throws IOException, IllegalArgumentException, IllegalStateException {
        this.xs.setPrefix(prefix, namespace);
        int depth = getDepth();
        for (int pos = this.namespaceEnd - 1; pos >= 0 && this.namespaceDepth[pos] > depth; pos--) {
            this.namespaceEnd--;
        }
        if (this.namespaceEnd >= this.namespacePrefix.length) {
            ensureNamespacesCapacity();
        }
        this.namespacePrefix[this.namespaceEnd] = prefix;
        this.namespaceUri[this.namespaceEnd] = namespace;
        this.namespaceEnd++;
    }

    @Override // org.xmlpull.v1.wrapper.XmlSerializerWrapper
    public void fragment(String xmlFragment) throws IOException, IllegalArgumentException, IllegalStateException, XmlPullParserException {
        StringBuffer buf = new StringBuffer(xmlFragment.length() + (this.namespaceEnd * 30));
        buf.append("<fragment");
        for (int pos = this.namespaceEnd - 1; pos >= 0; pos--) {
            String prefix = this.namespacePrefix[pos];
            int i = this.namespaceEnd - 1;
            while (true) {
                if (i > pos) {
                    if (prefix.equals(this.namespacePrefix[i])) {
                        break;
                    }
                    i--;
                } else {
                    buf.append(" xmlns");
                    if (prefix.length() > 0) {
                        buf.append(':').append(prefix);
                    }
                    buf.append("='");
                    buf.append(escapeAttributeValue(this.namespaceUri[pos]));
                    buf.append("'");
                }
            }
        }
        buf.append(">");
        buf.append(xmlFragment);
        buf.append("</fragment>");
        if (this.fragmentParser == null) {
            this.fragmentParser = this.wf.newPullParserWrapper();
        }
        String s = buf.toString();
        this.fragmentParser.setInput(new StringReader(s));
        this.fragmentParser.nextTag();
        this.fragmentParser.require(2, null, "fragment");
        while (true) {
            this.fragmentParser.nextToken();
            if (this.fragmentParser.getDepth() != 1 || this.fragmentParser.getEventType() != 3) {
                event(this.fragmentParser);
            } else {
                this.fragmentParser.require(3, null, "fragment");
                return;
            }
        }
    }

    @Override // org.xmlpull.v1.wrapper.XmlSerializerWrapper
    public void event(XmlPullParser pp) throws XmlPullParserException, IOException {
        int eventType = pp.getEventType();
        switch (eventType) {
            case 0:
                Boolean standalone = (Boolean) pp.getProperty(PROPERTY_XMLDECL_STANDALONE);
                startDocument(pp.getInputEncoding(), standalone);
                return;
            case 1:
                endDocument();
                return;
            case 2:
                writeStartTag(pp);
                return;
            case 3:
                endTag(pp.getNamespace(), pp.getName());
                return;
            case 4:
                if (pp.getDepth() > 0) {
                    text(pp.getText());
                    return;
                } else {
                    ignorableWhitespace(pp.getText());
                    return;
                }
            case 5:
                cdsect(pp.getText());
                return;
            case 6:
                entityRef(pp.getName());
                return;
            case 7:
                String s = pp.getText();
                ignorableWhitespace(s);
                return;
            case 8:
                processingInstruction(pp.getText());
                return;
            case 9:
                comment(pp.getText());
                return;
            case 10:
                docdecl(pp.getText());
                return;
            default:
                return;
        }
    }

    private void writeStartTag(XmlPullParser pp) throws XmlPullParserException, IOException {
        if (!pp.getFeature("http://xmlpull.org/v1/doc/features.html#report-namespace-prefixes")) {
            int nsStart = pp.getNamespaceCount(pp.getDepth() - 1);
            int nsEnd = pp.getNamespaceCount(pp.getDepth());
            for (int i = nsStart; i < nsEnd; i++) {
                String prefix = pp.getNamespacePrefix(i);
                String ns = pp.getNamespaceUri(i);
                setPrefix(prefix, ns);
            }
        }
        startTag(pp.getNamespace(), pp.getName());
        for (int i2 = 0; i2 < pp.getAttributeCount(); i2++) {
            attribute(pp.getAttributeNamespace(i2), pp.getAttributeName(i2), pp.getAttributeValue(i2));
        }
    }

    @Override // org.xmlpull.v1.wrapper.XmlSerializerWrapper
    public String escapeAttributeValue(String value) {
        int posLt = value.indexOf(60);
        int posAmp = value.indexOf(38);
        int posQuot = value.indexOf(34);
        int posApos = value.indexOf(39);
        if (posLt == -1 && posAmp == -1 && posQuot == -1 && posApos == -1) {
            return value;
        }
        StringBuffer buf = new StringBuffer(value.length() + 10);
        int len = value.length();
        for (int pos = 0; pos < len; pos++) {
            char ch = value.charAt(pos);
            switch (ch) {
                case '\"':
                    buf.append("&quot;");
                    break;
                case '&':
                    buf.append("&amp;");
                    break;
                case '\'':
                    buf.append("&apos;");
                    break;
                case '<':
                    buf.append("&lt;");
                    break;
                default:
                    buf.append(ch);
                    break;
            }
        }
        return buf.toString();
    }

    /* JADX WARN: Code restructure failed: missing block: B:39:0x00f2, code lost:
        throw new java.lang.IllegalStateException(new java.lang.StringBuffer().append("wrong state posLt=").append(r7).append(" posAmp=").append(r8).append(" for ").append(r6).toString());
     */
    @Override // org.xmlpull.v1.wrapper.XmlSerializerWrapper
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.lang.String escapeText(java.lang.String r6) {
        /*
            Method dump skipped, instructions count: 249
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.xmlpull.v1.wrapper.classic.StaticXmlSerializerWrapper.escapeText(java.lang.String):java.lang.String");
    }

    public void writeDouble(double d) throws XmlPullParserException, IOException, IllegalArgumentException {
        if (d == Double.POSITIVE_INFINITY) {
            this.xs.text("INF");
        } else if (d == Double.NEGATIVE_INFINITY) {
            this.xs.text("-INF");
        } else {
            this.xs.text(Double.toString(d));
        }
    }

    public void writeFloat(float f) throws XmlPullParserException, IOException, IllegalArgumentException {
        if (f == Float.POSITIVE_INFINITY) {
            this.xs.text("INF");
        } else if (f == Float.NEGATIVE_INFINITY) {
            this.xs.text("-INF");
        } else {
            this.xs.text(Float.toString(f));
        }
    }

    public void writeInt(int i) throws XmlPullParserException, IOException, IllegalArgumentException {
        this.xs.text(Integer.toString(i));
    }

    public void writeString(String s) throws XmlPullParserException, IOException, IllegalArgumentException {
        if (s == null) {
            throw new IllegalArgumentException("null string can not be written");
        }
        this.xs.text(s);
    }

    public void writeDoubleElement(String namespace, String name, double d) throws XmlPullParserException, IOException, IllegalArgumentException {
        this.xs.startTag(namespace, name);
        writeDouble(d);
        this.xs.endTag(namespace, name);
    }

    public void writeFloatElement(String namespace, String name, float f) throws XmlPullParserException, IOException, IllegalArgumentException {
        this.xs.startTag(namespace, name);
        writeFloat(f);
        this.xs.endTag(namespace, name);
    }

    public void writeIntElement(String namespace, String name, int i) throws XmlPullParserException, IOException, IllegalArgumentException {
        this.xs.startTag(namespace, name);
        writeInt(i);
        this.xs.endTag(namespace, name);
    }

    public void writeStringElement(String namespace, String name, String s) throws XmlPullParserException, IOException, IllegalArgumentException {
        this.xs.startTag(namespace, name);
        if (s == null) {
            this.xs.attribute("http://www.w3.org/2001/XMLSchema", "nil", "true");
        } else {
            writeString(s);
        }
        this.xs.endTag(namespace, name);
    }
}
