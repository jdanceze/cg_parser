package soot.jimple.infoflow.android.resources;

import java.io.File;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/resources/AbstractResourceParser.class */
public abstract class AbstractResourceParser {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Finally extract failed */
    public void handleAndroidResourceFiles(String apk, Set<String> fileNameFilter, IResourceHandler handler) {
        File apkF = new File(apk);
        if (!apkF.exists()) {
            throw new RuntimeException("file '" + apk + "' does not exist!");
        }
        try {
            ZipFile archive = new ZipFile(apkF);
            try {
                Enumeration<?> entries = archive.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry entry = entries.nextElement();
                    String entryName = entry.getName();
                    Throwable th = null;
                    try {
                        InputStream is = archive.getInputStream(entry);
                        try {
                            handler.handleResourceFile(entryName, fileNameFilter, is);
                            if (is != null) {
                                is.close();
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            if (is != null) {
                                is.close();
                            }
                            throw th;
                        }
                    } catch (Throwable th3) {
                        if (th == null) {
                            th = th3;
                        } else if (th != th3) {
                            th.addSuppressed(th3);
                        }
                        throw th;
                    }
                }
                if (archive != null) {
                    archive.close();
                }
            } catch (Throwable th4) {
                if (archive != null) {
                    archive.close();
                }
                throw th4;
            }
        } catch (Exception e) {
            this.logger.error("Error when looking for XML resource files in apk " + apk, (Throwable) e);
            if (e instanceof RuntimeException) {
                throw ((RuntimeException) e);
            }
            throw new RuntimeException(e);
        }
    }
}
