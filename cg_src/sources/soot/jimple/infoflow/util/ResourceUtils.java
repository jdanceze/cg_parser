package soot.jimple.infoflow.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/util/ResourceUtils.class */
public class ResourceUtils {
    public static InputStream getResourceStream(Class<?> clazz, String filename) throws IOException {
        File f = new File(filename);
        if (f.exists()) {
            return new FileInputStream(f);
        }
        if (!filename.startsWith("/")) {
            filename = "/" + filename;
        }
        InputStream inp = clazz.getResourceAsStream(filename);
        if (inp == null) {
            throw new IOException(String.format("Resource %s was not found", filename));
        }
        return inp;
    }

    public static InputStream getResourceStream(String filename) throws IOException {
        return getResourceStream(ResourceUtils.class, filename);
    }
}
