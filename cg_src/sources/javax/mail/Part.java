package javax.mail;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import javax.activation.DataHandler;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/Part.class */
public interface Part {
    public static final String ATTACHMENT = "attachment";
    public static final String INLINE = "inline";

    int getSize() throws MessagingException;

    int getLineCount() throws MessagingException;

    String getContentType() throws MessagingException;

    boolean isMimeType(String str) throws MessagingException;

    String getDisposition() throws MessagingException;

    void setDisposition(String str) throws MessagingException;

    String getDescription() throws MessagingException;

    void setDescription(String str) throws MessagingException;

    String getFileName() throws MessagingException;

    void setFileName(String str) throws MessagingException;

    InputStream getInputStream() throws IOException, MessagingException;

    DataHandler getDataHandler() throws MessagingException;

    Object getContent() throws IOException, MessagingException;

    void setDataHandler(DataHandler dataHandler) throws MessagingException;

    void setContent(Object obj, String str) throws MessagingException;

    void setText(String str) throws MessagingException;

    void setContent(Multipart multipart) throws MessagingException;

    void writeTo(OutputStream outputStream) throws IOException, MessagingException;

    String[] getHeader(String str) throws MessagingException;

    void setHeader(String str, String str2) throws MessagingException;

    void addHeader(String str, String str2) throws MessagingException;

    void removeHeader(String str) throws MessagingException;

    Enumeration getAllHeaders() throws MessagingException;

    Enumeration getMatchingHeaders(String[] strArr) throws MessagingException;

    Enumeration getNonMatchingHeaders(String[] strArr) throws MessagingException;
}
