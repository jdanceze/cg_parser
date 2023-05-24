package org.xml.sax.helpers;

import org.xml.sax.Locator;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/xml/sax/helpers/LocatorImpl.class */
public class LocatorImpl implements Locator {
    private String publicId;
    private String systemId;
    private int lineNumber;
    private int columnNumber;

    public LocatorImpl() {
    }

    public LocatorImpl(Locator locator) {
        setPublicId(locator.getPublicId());
        setSystemId(locator.getSystemId());
        setLineNumber(locator.getLineNumber());
        setColumnNumber(locator.getColumnNumber());
    }

    @Override // org.xml.sax.Locator
    public String getPublicId() {
        return this.publicId;
    }

    @Override // org.xml.sax.Locator
    public String getSystemId() {
        return this.systemId;
    }

    @Override // org.xml.sax.Locator
    public int getLineNumber() {
        return this.lineNumber;
    }

    @Override // org.xml.sax.Locator
    public int getColumnNumber() {
        return this.columnNumber;
    }

    public void setPublicId(String str) {
        this.publicId = str;
    }

    public void setSystemId(String str) {
        this.systemId = str;
    }

    public void setLineNumber(int i) {
        this.lineNumber = i;
    }

    public void setColumnNumber(int i) {
        this.columnNumber = i;
    }
}
