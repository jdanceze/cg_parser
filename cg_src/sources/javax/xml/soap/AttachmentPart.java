package javax.xml.soap;

import com.google.common.net.HttpHeaders;
import java.util.Iterator;
import javax.activation.DataHandler;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/soap/AttachmentPart.class */
public abstract class AttachmentPart {
    public abstract int getSize() throws SOAPException;

    public abstract void clearContent();

    public abstract Object getContent() throws SOAPException;

    public abstract void setContent(Object obj, String str);

    public abstract DataHandler getDataHandler() throws SOAPException;

    public abstract void setDataHandler(DataHandler dataHandler);

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

    public String getContentType() {
        String[] mimeHeader = getMimeHeader("Content-Type");
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

    public void setContentType(String str) {
        setMimeHeader("Content-Type", str);
    }

    public abstract void removeMimeHeader(String str);

    public abstract void removeAllMimeHeaders();

    public abstract String[] getMimeHeader(String str);

    public abstract void setMimeHeader(String str, String str2);

    public abstract void addMimeHeader(String str, String str2);

    public abstract Iterator getAllMimeHeaders();

    public abstract Iterator getMatchingMimeHeaders(String[] strArr);

    public abstract Iterator getNonMatchingMimeHeaders(String[] strArr);
}
