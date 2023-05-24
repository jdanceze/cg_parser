package org.xmlpull.v1.wrapper;

import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;
/* loaded from: gencallgraphv3.jar:xpp3-1.1.4c.jar:org/xmlpull/v1/wrapper/XmlSerializerWrapper.class */
public interface XmlSerializerWrapper extends XmlSerializer {
    public static final String NO_NAMESPACE = "";
    public static final String XSI_NS = "http://www.w3.org/2001/XMLSchema-instance";
    public static final String XSD_NS = "http://www.w3.org/2001/XMLSchema";

    String getCurrentNamespaceForElements();

    String setCurrentNamespaceForElements(String str);

    XmlSerializerWrapper attribute(String str, String str2) throws IOException, IllegalArgumentException, IllegalStateException;

    XmlSerializerWrapper startTag(String str) throws IOException, IllegalArgumentException, IllegalStateException;

    XmlSerializerWrapper endTag(String str) throws IOException, IllegalArgumentException, IllegalStateException;

    XmlSerializerWrapper element(String str, String str2, String str3) throws IOException, XmlPullParserException;

    XmlSerializerWrapper element(String str, String str2) throws IOException, XmlPullParserException;

    void fragment(String str) throws IOException, IllegalArgumentException, IllegalStateException, XmlPullParserException;

    void event(XmlPullParser xmlPullParser) throws IOException, IllegalArgumentException, IllegalStateException, XmlPullParserException;

    String escapeText(String str) throws IllegalArgumentException;

    String escapeAttributeValue(String str) throws IllegalArgumentException;
}
