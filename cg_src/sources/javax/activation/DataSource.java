package javax.activation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:j2ee.jar:javax/activation/DataSource.class
 */
/* loaded from: gencallgraphv3.jar:javax.activation-api-1.2.0.jar:javax/activation/DataSource.class */
public interface DataSource {
    InputStream getInputStream() throws IOException;

    OutputStream getOutputStream() throws IOException;

    String getContentType();

    String getName();
}
