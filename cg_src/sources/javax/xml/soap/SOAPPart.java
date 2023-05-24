package javax.xml.soap;

import com.google.common.net.HttpHeaders;
import java.util.Iterator;
import javax.xml.transform.Source;
import org.w3c.dom.Document;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/soap/SOAPPart.class */
public abstract class SOAPPart implements Document {
    public abstract SOAPEnvelope getEnvelope() throws SOAPException;

    public String getContentId() {
        String[] mimeHeader = getMimeHeader("Content-Id");
        if (mimeHeader == null || mimeHeader.length <= 0) {
            return null;
        }
        return mimeHeader[0];
    }

    public String getContentLocation() {
        String[] mimeHeader = getMimeHeader(HttpHeaders.CONTENT_LOCATION);
        if (mimeHeader == null || mimeHeader.length <= 0) {
            return null;
        }
        return mimeHeader[0];
    }

    public void setContentId(String str) {
        setMimeHeader("Content-Id", str);
    }

    public void setContentLocation(String str) {
        setMimeHeader(HttpHeaders.CONTENT_LOCATION, str);
    }

    public abstract void removeMimeHeader(String str);

    public abstract void removeAllMimeHeaders();

    public abstract String[] getMimeHeader(String str);

    public abstract void setMimeHeader(String str, String str2);

    public abstract void addMimeHeader(String str, String str2);

    public abstract Iterator getAllMimeHeaders();

    public abstract Iterator getMatchingMimeHeaders(String[] strArr);

    public abstract Iterator getNonMatchingMimeHeaders(String[] strArr);

    public abstract void setContent(Source source) throws SOAPException;

    public abstract Source getContent() throws SOAPException;
}
