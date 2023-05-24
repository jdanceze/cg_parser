package org.xmlpull.mxp1;

import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: gencallgraphv3.jar:xpp3-1.1.4c.jar:org/xmlpull/mxp1/MXParserNonValidating.class */
public class MXParserNonValidating extends MXParserCachingStrings {
    private boolean processDocDecl;

    @Override // org.xmlpull.mxp1.MXParserCachingStrings, org.xmlpull.mxp1.MXParser, org.xmlpull.v1.XmlPullParser
    public void setFeature(String name, boolean state) throws XmlPullParserException {
        if ("http://xmlpull.org/v1/doc/features.html#process-docdecl".equals(name)) {
            if (this.eventType != 0) {
                throw new XmlPullParserException("process DOCDECL feature can only be changed before parsing", this, null);
            }
            this.processDocDecl = state;
            if (!state) {
            }
            return;
        }
        super.setFeature(name, state);
    }

    @Override // org.xmlpull.mxp1.MXParserCachingStrings, org.xmlpull.mxp1.MXParser, org.xmlpull.v1.XmlPullParser
    public boolean getFeature(String name) {
        if ("http://xmlpull.org/v1/doc/features.html#process-docdecl".equals(name)) {
            return this.processDocDecl;
        }
        return super.getFeature(name);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.xmlpull.mxp1.MXParser
    public char more() throws IOException, XmlPullParserException {
        return super.more();
    }

    @Override // org.xmlpull.mxp1.MXParser
    protected char[] lookuEntityReplacement(int entitNameLen) throws XmlPullParserException, IOException {
        if (!this.allStringsInterned) {
            int hash = MXParser.fastHash(this.buf, this.posStart, this.posEnd - this.posStart);
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

    @Override // org.xmlpull.mxp1.MXParser
    protected void parseDocdecl() throws XmlPullParserException, IOException {
        boolean oldTokenize = this.tokenize;
        try {
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
            char ch7 = requireNextS();
            int i = this.pos;
            char ch8 = readName(ch7);
            int i2 = this.pos;
            char ch9 = skipS(ch8);
            if (ch9 == 'S' || ch9 == 'P') {
                ch9 = skipS(processExternalId(ch9));
            }
            if (ch9 == '[') {
                processInternalSubset();
            }
            char ch10 = skipS(ch9);
            if (ch10 != '>') {
                throw new XmlPullParserException(new StringBuffer().append("expected > to finish <[DOCTYPE but got ").append(printable(ch10)).toString(), this, null);
            }
            this.posEnd = this.pos - 1;
            this.tokenize = oldTokenize;
        } catch (Throwable th) {
            this.tokenize = oldTokenize;
            throw th;
        }
    }

    protected char processExternalId(char ch) throws XmlPullParserException, IOException {
        return ch;
    }

    protected void processInternalSubset() throws XmlPullParserException, IOException {
        while (true) {
            char ch = more();
            if (ch != ']') {
                if (ch == '%') {
                    processPEReference();
                } else if (isS(ch)) {
                    skipS(ch);
                } else {
                    processMarkupDecl(ch);
                }
            } else {
                return;
            }
        }
    }

    protected void processPEReference() throws XmlPullParserException, IOException {
    }

    protected void processMarkupDecl(char ch) throws XmlPullParserException, IOException {
        if (ch != '<') {
            throw new XmlPullParserException(new StringBuffer().append("expected < for markupdecl in DTD not ").append(printable(ch)).toString(), this, null);
        }
        char ch2 = more();
        if (ch2 == '?') {
            parsePI();
        } else if (ch2 == '!') {
            if (more() == '-') {
                parseComment();
                return;
            }
            char ch3 = more();
            if (ch3 == 'A') {
                processAttlistDecl(ch3);
            } else if (ch3 == 'E') {
                char ch4 = more();
                if (ch4 == 'L') {
                    processElementDecl(ch4);
                } else if (ch4 == 'N') {
                    processEntityDecl(ch4);
                } else {
                    throw new XmlPullParserException(new StringBuffer().append("expected ELEMENT or ENTITY after <! in DTD not ").append(printable(ch4)).toString(), this, null);
                }
            } else if (ch3 == 'N') {
                processNotationDecl(ch3);
            } else {
                throw new XmlPullParserException(new StringBuffer().append("expected markupdecl after <! in DTD not ").append(printable(ch3)).toString(), this, null);
            }
        } else {
            throw new XmlPullParserException(new StringBuffer().append("expected markupdecl in DTD not ").append(printable(ch2)).toString(), this, null);
        }
    }

    protected void processElementDecl(char ch) throws XmlPullParserException, IOException {
        char ch2 = requireNextS();
        readName(ch2);
        requireNextS();
    }

    protected void processAttlistDecl(char ch) throws XmlPullParserException, IOException {
    }

    protected void processEntityDecl(char ch) throws XmlPullParserException, IOException {
    }

    protected void processNotationDecl(char ch) throws XmlPullParserException, IOException {
    }

    protected char readName(char ch) throws XmlPullParserException, IOException {
        if (isNameStartChar(ch)) {
            throw new XmlPullParserException(new StringBuffer().append("XML name must start with name start character not ").append(printable(ch)).toString(), this, null);
        }
        while (isNameChar(ch)) {
            ch = more();
        }
        return ch;
    }
}
