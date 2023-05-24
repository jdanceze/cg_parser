package org.xmlpull.v1.wrapper;

import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: gencallgraphv3.jar:xpp3-1.1.4c.jar:org/xmlpull/v1/wrapper/XmlPullParserWrapper.class */
public interface XmlPullParserWrapper extends XmlPullParser {
    public static final String XSI_NS = "http://www.w3.org/2001/XMLSchema-instance";
    public static final String XSD_NS = "http://www.w3.org/2001/XMLSchema";

    String getAttributeValue(String str);

    String getPITarget() throws IllegalStateException;

    String getPIData() throws IllegalStateException;

    String getRequiredAttributeValue(String str) throws IOException, XmlPullParserException;

    String getRequiredAttributeValue(String str, String str2) throws IOException, XmlPullParserException;

    String getRequiredElementText(String str, String str2) throws IOException, XmlPullParserException;

    boolean isNil() throws IOException, XmlPullParserException;

    boolean matches(int i, String str, String str2) throws XmlPullParserException;

    void nextStartTag() throws XmlPullParserException, IOException;

    void nextStartTag(String str) throws XmlPullParserException, IOException;

    void nextStartTag(String str, String str2) throws XmlPullParserException, IOException;

    void nextEndTag() throws XmlPullParserException, IOException;

    void nextEndTag(String str) throws XmlPullParserException, IOException;

    void nextEndTag(String str, String str2) throws XmlPullParserException, IOException;

    String nextText(String str, String str2) throws IOException, XmlPullParserException;

    void skipSubTree() throws XmlPullParserException, IOException;
}
