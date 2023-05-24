package javax.mail;

import javax.activation.DataSource;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/MultipartDataSource.class */
public interface MultipartDataSource extends DataSource {
    int getCount();

    BodyPart getBodyPart(int i) throws MessagingException;
}
