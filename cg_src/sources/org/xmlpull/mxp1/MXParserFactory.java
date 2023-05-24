package org.xmlpull.mxp1;

import java.util.Enumeration;
import org.xmlpull.mxp1_serializer.MXSerializer;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;
/* loaded from: gencallgraphv3.jar:xpp3-1.1.4c.jar:org/xmlpull/mxp1/MXParserFactory.class */
public class MXParserFactory extends XmlPullParserFactory {
    protected static boolean stringCachedParserAvailable = true;

    @Override // org.xmlpull.v1.XmlPullParserFactory
    public XmlPullParser newPullParser() throws XmlPullParserException {
        XmlPullParser pp = null;
        if (stringCachedParserAvailable) {
            try {
                pp = new MXParserCachingStrings();
            } catch (Exception e) {
                stringCachedParserAvailable = false;
            }
        }
        if (pp == null) {
            pp = new MXParser();
        }
        Enumeration e2 = this.features.keys();
        while (e2.hasMoreElements()) {
            String key = (String) e2.nextElement();
            Boolean value = (Boolean) this.features.get(key);
            if (value != null && value.booleanValue()) {
                pp.setFeature(key, true);
            }
        }
        return pp;
    }

    @Override // org.xmlpull.v1.XmlPullParserFactory
    public XmlSerializer newSerializer() throws XmlPullParserException {
        return new MXSerializer();
    }
}
