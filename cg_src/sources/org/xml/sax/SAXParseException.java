package org.xml.sax;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/xml/sax/SAXParseException.class */
public class SAXParseException extends SAXException {
    private static final long serialVersionUID = -5651165872476709336L;
    private String publicId;
    private String systemId;
    private int lineNumber;
    private int columnNumber;

    public SAXParseException(String str, Locator locator) {
        super(str);
        if (locator != null) {
            init(locator.getPublicId(), locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber());
        } else {
            init(null, null, -1, -1);
        }
    }

    public SAXParseException(String str, Locator locator, Exception exc) {
        super(str, exc);
        if (locator != null) {
            init(locator.getPublicId(), locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber());
        } else {
            init(null, null, -1, -1);
        }
    }

    public SAXParseException(String str, String str2, String str3, int i, int i2) {
        super(str);
        init(str2, str3, i, i2);
    }

    public SAXParseException(String str, String str2, String str3, int i, int i2, Exception exc) {
        super(str, exc);
        init(str2, str3, i, i2);
    }

    private void init(String str, String str2, int i, int i2) {
        this.publicId = str;
        this.systemId = str2;
        this.lineNumber = i;
        this.columnNumber = i2;
    }

    public String getPublicId() {
        return this.publicId;
    }

    public String getSystemId() {
        return this.systemId;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    public int getColumnNumber() {
        return this.columnNumber;
    }
}
