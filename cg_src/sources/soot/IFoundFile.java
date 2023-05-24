package soot;

import java.io.File;
import java.io.InputStream;
import java.util.zip.ZipFile;
import org.slf4j.LoggerFactory;
/* loaded from: gencallgraphv3.jar:soot/IFoundFile.class */
public interface IFoundFile {
    @Deprecated
    String getFilePath();

    boolean isZipFile();

    ZipFile getZipFile();

    File getFile();

    String getAbsolutePath();

    InputStream inputStream();

    void close();

    default void silentClose() {
        try {
            close();
        } catch (Exception e) {
            LoggerFactory.getLogger(getClass()).debug(e.getMessage(), (Throwable) e);
        }
    }
}
