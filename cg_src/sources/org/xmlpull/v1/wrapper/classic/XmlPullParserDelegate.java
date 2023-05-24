package org.xmlpull.v1.wrapper.classic;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: gencallgraphv3.jar:xpp3-1.1.4c.jar:org/xmlpull/v1/wrapper/classic/XmlPullParserDelegate.class */
public class XmlPullParserDelegate implements XmlPullParser {
    protected XmlPullParser pp;

    public XmlPullParserDelegate(XmlPullParser pp) {
        this.pp = pp;
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String getText() {
        return this.pp.getText();
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public void setFeature(String name, boolean state) throws XmlPullParserException {
        this.pp.setFeature(name, state);
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public char[] getTextCharacters(int[] holderForStartAndLength) {
        return this.pp.getTextCharacters(holderForStartAndLength);
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public int getColumnNumber() {
        return this.pp.getColumnNumber();
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public int getNamespaceCount(int depth) throws XmlPullParserException {
        return this.pp.getNamespaceCount(depth);
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String getNamespacePrefix(int pos) throws XmlPullParserException {
        return this.pp.getNamespacePrefix(pos);
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String getAttributeName(int index) {
        return this.pp.getAttributeName(index);
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String getName() {
        return this.pp.getName();
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public boolean getFeature(String name) {
        return this.pp.getFeature(name);
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String getInputEncoding() {
        return this.pp.getInputEncoding();
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String getAttributeValue(int index) {
        return this.pp.getAttributeValue(index);
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String getNamespace(String prefix) {
        return this.pp.getNamespace(prefix);
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public void setInput(Reader in) throws XmlPullParserException {
        this.pp.setInput(in);
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public int getLineNumber() {
        return this.pp.getLineNumber();
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public Object getProperty(String name) {
        return this.pp.getProperty(name);
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public boolean isEmptyElementTag() throws XmlPullParserException {
        return this.pp.isEmptyElementTag();
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public boolean isAttributeDefault(int index) {
        return this.pp.isAttributeDefault(index);
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String getNamespaceUri(int pos) throws XmlPullParserException {
        return this.pp.getNamespaceUri(pos);
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public int next() throws XmlPullParserException, IOException {
        return this.pp.next();
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public int nextToken() throws XmlPullParserException, IOException {
        return this.pp.nextToken();
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public void defineEntityReplacementText(String entityName, String replacementText) throws XmlPullParserException {
        this.pp.defineEntityReplacementText(entityName, replacementText);
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public int getAttributeCount() {
        return this.pp.getAttributeCount();
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public boolean isWhitespace() throws XmlPullParserException {
        return this.pp.isWhitespace();
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String getPrefix() {
        return this.pp.getPrefix();
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public void require(int type, String namespace, String name) throws XmlPullParserException, IOException {
        this.pp.require(type, namespace, name);
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String nextText() throws XmlPullParserException, IOException {
        return this.pp.nextText();
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String getAttributeType(int index) {
        return this.pp.getAttributeType(index);
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public int getDepth() {
        return this.pp.getDepth();
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public int nextTag() throws XmlPullParserException, IOException {
        return this.pp.nextTag();
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public int getEventType() throws XmlPullParserException {
        return this.pp.getEventType();
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String getAttributePrefix(int index) {
        return this.pp.getAttributePrefix(index);
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public void setInput(InputStream inputStream, String inputEncoding) throws XmlPullParserException {
        this.pp.setInput(inputStream, inputEncoding);
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String getAttributeValue(String namespace, String name) {
        return this.pp.getAttributeValue(namespace, name);
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public void setProperty(String name, Object value) throws XmlPullParserException {
        this.pp.setProperty(name, value);
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String getPositionDescription() {
        return this.pp.getPositionDescription();
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String getNamespace() {
        return this.pp.getNamespace();
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String getAttributeNamespace(int index) {
        return this.pp.getAttributeNamespace(index);
    }
}
