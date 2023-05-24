package javax.mail.internet;

import java.io.InputStream;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/internet/SharedInputStream.class */
public interface SharedInputStream {
    long getPosition();

    InputStream newStream(long j, long j2);
}
