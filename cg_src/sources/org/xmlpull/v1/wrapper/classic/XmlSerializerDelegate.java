package org.xmlpull.v1.wrapper.classic;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import org.xmlpull.v1.XmlSerializer;
/* loaded from: gencallgraphv3.jar:xpp3-1.1.4c.jar:org/xmlpull/v1/wrapper/classic/XmlSerializerDelegate.class */
public class XmlSerializerDelegate implements XmlSerializer {
    protected XmlSerializer xs;

    public XmlSerializerDelegate(XmlSerializer serializer) {
        this.xs = serializer;
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public String getName() {
        return this.xs.getName();
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void setPrefix(String prefix, String namespace) throws IOException, IllegalArgumentException, IllegalStateException {
        this.xs.setPrefix(prefix, namespace);
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void setOutput(OutputStream os, String encoding) throws IOException, IllegalArgumentException, IllegalStateException {
        this.xs.setOutput(os, encoding);
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void endDocument() throws IOException, IllegalArgumentException, IllegalStateException {
        this.xs.endDocument();
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void comment(String text) throws IOException, IllegalArgumentException, IllegalStateException {
        this.xs.comment(text);
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public int getDepth() {
        return this.xs.getDepth();
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void setProperty(String name, Object value) throws IllegalArgumentException, IllegalStateException {
        this.xs.setProperty(name, value);
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void cdsect(String text) throws IOException, IllegalArgumentException, IllegalStateException {
        this.xs.cdsect(text);
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void setFeature(String name, boolean state) throws IllegalArgumentException, IllegalStateException {
        this.xs.setFeature(name, state);
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void entityRef(String text) throws IOException, IllegalArgumentException, IllegalStateException {
        this.xs.entityRef(text);
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void processingInstruction(String text) throws IOException, IllegalArgumentException, IllegalStateException {
        this.xs.processingInstruction(text);
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void setOutput(Writer writer) throws IOException, IllegalArgumentException, IllegalStateException {
        this.xs.setOutput(writer);
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void docdecl(String text) throws IOException, IllegalArgumentException, IllegalStateException {
        this.xs.docdecl(text);
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void flush() throws IOException {
        this.xs.flush();
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public Object getProperty(String name) {
        return this.xs.getProperty(name);
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public XmlSerializer startTag(String namespace, String name) throws IOException, IllegalArgumentException, IllegalStateException {
        return this.xs.startTag(namespace, name);
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void ignorableWhitespace(String text) throws IOException, IllegalArgumentException, IllegalStateException {
        this.xs.ignorableWhitespace(text);
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public XmlSerializer text(String text) throws IOException, IllegalArgumentException, IllegalStateException {
        return this.xs.text(text);
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public boolean getFeature(String name) {
        return this.xs.getFeature(name);
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public XmlSerializer attribute(String namespace, String name, String value) throws IOException, IllegalArgumentException, IllegalStateException {
        return this.xs.attribute(namespace, name, value);
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void startDocument(String encoding, Boolean standalone) throws IOException, IllegalArgumentException, IllegalStateException {
        this.xs.startDocument(encoding, standalone);
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public String getPrefix(String namespace, boolean generatePrefix) throws IllegalArgumentException {
        return this.xs.getPrefix(namespace, generatePrefix);
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public String getNamespace() {
        return this.xs.getNamespace();
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public XmlSerializer endTag(String namespace, String name) throws IOException, IllegalArgumentException, IllegalStateException {
        return this.xs.endTag(namespace, name);
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public XmlSerializer text(char[] buf, int start, int len) throws IOException, IllegalArgumentException, IllegalStateException {
        return this.xs.text(buf, start, len);
    }
}
