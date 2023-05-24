package javax.mail.internet;

import java.util.Enumeration;
import javax.mail.MessagingException;
import javax.mail.Part;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/internet/MimePart.class */
public interface MimePart extends Part {
    String getHeader(String str, String str2) throws MessagingException;

    void addHeaderLine(String str) throws MessagingException;

    Enumeration getAllHeaderLines() throws MessagingException;

    Enumeration getMatchingHeaderLines(String[] strArr) throws MessagingException;

    Enumeration getNonMatchingHeaderLines(String[] strArr) throws MessagingException;

    String getEncoding() throws MessagingException;

    String getContentID() throws MessagingException;

    String getContentMD5() throws MessagingException;

    void setContentMD5(String str) throws MessagingException;

    String[] getContentLanguage() throws MessagingException;

    void setContentLanguage(String[] strArr) throws MessagingException;

    @Override // 
    void setText(String str) throws MessagingException;

    void setText(String str, String str2) throws MessagingException;
}
