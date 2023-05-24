package javax.servlet;

import java.io.IOException;
import java.io.InputStream;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/servlet/ServletInputStream.class */
public abstract class ServletInputStream extends InputStream {
    public int readLine(byte[] b, int off, int len) throws IOException {
        if (len <= 0) {
            return 0;
        }
        int count = 0;
        do {
            int read = read();
            if (read != -1) {
                int i = off;
                off++;
                b[i] = (byte) read;
                count++;
                if (read == 10) {
                    break;
                }
            } else {
                break;
            }
        } while (count != len);
        if (count > 0) {
            return count;
        }
        return -1;
    }
}
