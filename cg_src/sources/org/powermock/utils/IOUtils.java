package org.powermock.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/utils/IOUtils.class */
public class IOUtils {
    public static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            while (true) {
                int length = is.read(buffer);
                if (length <= 0) {
                    break;
                }
                os.write(buffer, 0, length);
            }
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
        } catch (Throwable th) {
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
            throw th;
        }
    }
}
