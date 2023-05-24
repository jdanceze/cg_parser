package javax.resource.cci;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/resource/cci/Streamable.class */
public interface Streamable {
    void read(InputStream inputStream) throws IOException;

    void write(OutputStream outputStream) throws IOException;
}
