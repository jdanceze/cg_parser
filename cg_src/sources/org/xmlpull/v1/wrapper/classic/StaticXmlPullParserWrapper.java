package org.xmlpull.v1.wrapper.classic;

import android.hardware.Camera;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.util.XmlPullUtil;
import org.xmlpull.v1.wrapper.XmlPullParserWrapper;
/* loaded from: gencallgraphv3.jar:xpp3-1.1.4c.jar:org/xmlpull/v1/wrapper/classic/StaticXmlPullParserWrapper.class */
public class StaticXmlPullParserWrapper extends XmlPullParserDelegate implements XmlPullParserWrapper {
    public StaticXmlPullParserWrapper(XmlPullParser pp) {
        super(pp);
    }

    @Override // org.xmlpull.v1.wrapper.XmlPullParserWrapper
    public String getAttributeValue(String name) {
        return XmlPullUtil.getAttributeValue(this.pp, name);
    }

    @Override // org.xmlpull.v1.wrapper.XmlPullParserWrapper
    public String getRequiredAttributeValue(String name) throws IOException, XmlPullParserException {
        return XmlPullUtil.getRequiredAttributeValue(this.pp, null, name);
    }

    @Override // org.xmlpull.v1.wrapper.XmlPullParserWrapper
    public String getRequiredAttributeValue(String namespace, String name) throws IOException, XmlPullParserException {
        return XmlPullUtil.getRequiredAttributeValue(this.pp, namespace, name);
    }

    @Override // org.xmlpull.v1.wrapper.XmlPullParserWrapper
    public String getRequiredElementText(String namespace, String name) throws IOException, XmlPullParserException {
        if (name == null) {
            throw new XmlPullParserException("name for element can not be null");
        }
        String text = null;
        nextStartTag(namespace, name);
        if (isNil()) {
            nextEndTag(namespace, name);
        } else {
            text = this.pp.nextText();
        }
        this.pp.require(3, namespace, name);
        return text;
    }

    @Override // org.xmlpull.v1.wrapper.XmlPullParserWrapper
    public boolean isNil() throws IOException, XmlPullParserException {
        boolean result = false;
        String value = this.pp.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
        if ("true".equals(value)) {
            result = true;
        }
        return result;
    }

    @Override // org.xmlpull.v1.wrapper.XmlPullParserWrapper
    public String getPITarget() throws IllegalStateException {
        return XmlPullUtil.getPITarget(this.pp);
    }

    @Override // org.xmlpull.v1.wrapper.XmlPullParserWrapper
    public String getPIData() throws IllegalStateException {
        return XmlPullUtil.getPIData(this.pp);
    }

    @Override // org.xmlpull.v1.wrapper.XmlPullParserWrapper
    public boolean matches(int type, String namespace, String name) throws XmlPullParserException {
        return XmlPullUtil.matches(this.pp, type, namespace, name);
    }

    @Override // org.xmlpull.v1.wrapper.XmlPullParserWrapper
    public void nextStartTag() throws XmlPullParserException, IOException {
        if (this.pp.nextTag() != 2) {
            throw new XmlPullParserException(new StringBuffer().append("expected START_TAG and not ").append(this.pp.getPositionDescription()).toString());
        }
    }

    @Override // org.xmlpull.v1.wrapper.XmlPullParserWrapper
    public void nextStartTag(String name) throws XmlPullParserException, IOException {
        this.pp.nextTag();
        this.pp.require(2, null, name);
    }

    @Override // org.xmlpull.v1.wrapper.XmlPullParserWrapper
    public void nextStartTag(String namespace, String name) throws XmlPullParserException, IOException {
        this.pp.nextTag();
        this.pp.require(2, namespace, name);
    }

    @Override // org.xmlpull.v1.wrapper.XmlPullParserWrapper
    public void nextEndTag() throws XmlPullParserException, IOException {
        XmlPullUtil.nextEndTag(this.pp);
    }

    @Override // org.xmlpull.v1.wrapper.XmlPullParserWrapper
    public void nextEndTag(String name) throws XmlPullParserException, IOException {
        XmlPullUtil.nextEndTag(this.pp, null, name);
    }

    @Override // org.xmlpull.v1.wrapper.XmlPullParserWrapper
    public void nextEndTag(String namespace, String name) throws XmlPullParserException, IOException {
        XmlPullUtil.nextEndTag(this.pp, namespace, name);
    }

    @Override // org.xmlpull.v1.wrapper.XmlPullParserWrapper
    public String nextText(String namespace, String name) throws IOException, XmlPullParserException {
        return XmlPullUtil.nextText(this.pp, namespace, name);
    }

    @Override // org.xmlpull.v1.wrapper.XmlPullParserWrapper
    public void skipSubTree() throws XmlPullParserException, IOException {
        XmlPullUtil.skipSubTree(this.pp);
    }

    public double readDouble() throws XmlPullParserException, IOException {
        double d;
        String value = this.pp.nextText();
        try {
            d = Double.parseDouble(value);
        } catch (NumberFormatException ex) {
            if (value.equals("INF") || value.toLowerCase().equals(Camera.Parameters.FOCUS_MODE_INFINITY)) {
                d = Double.POSITIVE_INFINITY;
            } else if (value.equals("-INF") || value.toLowerCase().equals("-infinity")) {
                d = Double.NEGATIVE_INFINITY;
            } else if (value.equals("NaN")) {
                d = Double.NaN;
            } else {
                throw new XmlPullParserException(new StringBuffer().append("can't parse double value '").append(value).append("'").toString(), this, ex);
            }
        }
        return d;
    }

    public float readFloat() throws XmlPullParserException, IOException {
        float f;
        String value = this.pp.nextText();
        try {
            f = Float.parseFloat(value);
        } catch (NumberFormatException ex) {
            if (value.equals("INF") || value.toLowerCase().equals(Camera.Parameters.FOCUS_MODE_INFINITY)) {
                f = Float.POSITIVE_INFINITY;
            } else if (value.equals("-INF") || value.toLowerCase().equals("-infinity")) {
                f = Float.NEGATIVE_INFINITY;
            } else if (value.equals("NaN")) {
                f = Float.NaN;
            } else {
                throw new XmlPullParserException(new StringBuffer().append("can't parse float value '").append(value).append("'").toString(), this, ex);
            }
        }
        return f;
    }

    private int parseDigits(String text, int offset, int length) throws XmlPullParserException {
        int value = 0;
        if (length > 9) {
            try {
                value = Integer.parseInt(text.substring(offset, offset + length));
            } catch (NumberFormatException ex) {
                throw new XmlPullParserException(ex.getMessage());
            }
        } else {
            int limit = offset + length;
            while (offset < limit) {
                int i = offset;
                offset++;
                char chr = text.charAt(i);
                if (chr >= '0' && chr <= '9') {
                    value = (value * 10) + (chr - '0');
                } else {
                    throw new XmlPullParserException("non-digit in number value", this, null);
                }
            }
        }
        return value;
    }

    private int parseInt(String text) throws XmlPullParserException {
        int offset = 0;
        int limit = text.length();
        if (limit == 0) {
            throw new XmlPullParserException("empty number value", this, null);
        }
        boolean negate = false;
        char chr = text.charAt(0);
        if (chr == '-') {
            if (limit > 9) {
                try {
                    return Integer.parseInt(text);
                } catch (NumberFormatException ex) {
                    throw new XmlPullParserException(ex.getMessage(), this, null);
                }
            }
            negate = true;
            offset = 0 + 1;
        } else if (chr == '+') {
            offset = 0 + 1;
        }
        if (offset >= limit) {
            throw new XmlPullParserException("Invalid number format", this, null);
        }
        int value = parseDigits(text, offset, limit - offset);
        if (negate) {
            return -value;
        }
        return value;
    }

    public int readInt() throws XmlPullParserException, IOException {
        try {
            int i = parseInt(this.pp.nextText());
            return i;
        } catch (NumberFormatException ex) {
            throw new XmlPullParserException("can't parse int value", this, ex);
        }
    }

    public String readString() throws XmlPullParserException, IOException {
        String xsiNil = this.pp.getAttributeValue("http://www.w3.org/2001/XMLSchema", "nil");
        if ("true".equals(xsiNil)) {
            nextEndTag();
            return null;
        }
        return this.pp.nextText();
    }

    public double readDoubleElement(String namespace, String name) throws XmlPullParserException, IOException {
        this.pp.require(2, namespace, name);
        return readDouble();
    }

    public float readFloatElement(String namespace, String name) throws XmlPullParserException, IOException {
        this.pp.require(2, namespace, name);
        return readFloat();
    }

    public int readIntElement(String namespace, String name) throws XmlPullParserException, IOException {
        this.pp.require(2, namespace, name);
        return readInt();
    }

    public String readStringElemet(String namespace, String name) throws XmlPullParserException, IOException {
        this.pp.require(2, namespace, name);
        return readString();
    }
}
