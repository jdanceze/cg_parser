package soot.jimple.infoflow.android.axml;

import com.google.common.io.Files;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/axml/ApkHandler.class */
public class ApkHandler implements AutoCloseable {
    private final Logger logger;
    protected File apk;
    protected ZipFile zip;

    public ApkHandler(String path) throws ZipException, IOException {
        this(new File(path));
    }

    public ApkHandler(File apk) throws ZipException, IOException {
        this.logger = LoggerFactory.getLogger(getClass());
        this.apk = apk;
    }

    public String getAbsolutePath() {
        return this.apk.getAbsolutePath();
    }

    public String getPath() {
        return this.apk.getPath();
    }

    public String getFilename() {
        return this.apk.getName();
    }

    public InputStream getInputStream(String filename) throws IOException {
        InputStream is = null;
        if (this.zip == null) {
            this.zip = new ZipFile(this.apk);
        }
        Enumeration<?> entries = this.zip.entries();
        while (true) {
            if (!entries.hasMoreElements()) {
                break;
            }
            ZipEntry entry = entries.nextElement();
            String entryName = entry.getName();
            if (entryName.equals(filename)) {
                is = this.zip.getInputStream(entry);
                break;
            }
        }
        return is;
    }

    public void addFilesToApk(List<File> files) throws IOException {
        addFilesToApk(files, new HashMap());
    }

    /* JADX WARN: Finally extract failed */
    public void addFilesToApk(List<File> files, Map<String, String> paths) throws IOException {
        if (this.zip != null) {
            this.zip.close();
            this.zip = null;
        }
        for (File file : files) {
            if (!paths.containsKey(file.getPath())) {
                paths.put(file.getPath(), file.getName());
            }
        }
        File tempFile = File.createTempFile(this.apk.getName(), null);
        tempFile.delete();
        boolean renameOk = this.apk.renameTo(tempFile);
        if (!renameOk) {
            try {
                Files.move(this.apk, tempFile);
            } catch (IOException ex) {
                throw new IOException("could not rename the file " + this.apk.getAbsolutePath() + " to " + tempFile.getAbsolutePath(), ex);
            }
        }
        byte[] buf = new byte[1024];
        Throwable th = null;
        try {
            ZipInputStream zin = new ZipInputStream(new FileInputStream(tempFile));
            try {
                ZipOutputStream out = new ZipOutputStream(new FileOutputStream(this.apk));
                while (true) {
                    try {
                        ZipEntry entry = zin.getNextEntry();
                        if (entry == null) {
                            break;
                        }
                        Iterator<String> it = paths.values().iterator();
                        while (true) {
                            if (it.hasNext()) {
                                String path = it.next();
                                if (entry.getName().equals(path)) {
                                    break;
                                }
                            } else if (!entry.getName().startsWith("META-INF/") || (!entry.getName().endsWith(".RSA") && !entry.getName().endsWith(".SF"))) {
                                ZipEntry ze = new ZipEntry(entry.getName());
                                ze.setMethod(entry.getMethod());
                                if (entry.getTime() != -1) {
                                    ze.setTime(entry.getTime());
                                }
                                if (entry.getSize() != -1) {
                                    ze.setSize(entry.getSize());
                                }
                                if (entry.getCrc() != -1) {
                                    ze.setCrc(entry.getCrc());
                                }
                                out.putNextEntry(ze);
                                while (true) {
                                    int len = zin.read(buf);
                                    if (len <= 0) {
                                        break;
                                    }
                                    out.write(buf, 0, len);
                                }
                                zin.closeEntry();
                                out.closeEntry();
                            }
                        }
                    } catch (Throwable th2) {
                        if (out != null) {
                            out.close();
                        }
                        throw th2;
                    }
                }
                for (File file2 : files) {
                    Throwable th3 = null;
                    try {
                        InputStream in = new FileInputStream(file2);
                        try {
                            out.putNextEntry(new ZipEntry(paths.get(file2.getPath())));
                            while (true) {
                                int len2 = in.read(buf);
                                if (len2 <= 0) {
                                    break;
                                }
                                out.write(buf, 0, len2);
                            }
                            out.closeEntry();
                            if (in != null) {
                                in.close();
                            }
                        } catch (Throwable th4) {
                            th3 = th4;
                            if (in != null) {
                                in.close();
                            }
                            throw th3;
                        }
                    } catch (Throwable th5) {
                        if (th3 == null) {
                            th3 = th5;
                        } else if (th3 != th5) {
                            th3.addSuppressed(th5);
                        }
                        throw th3;
                    }
                }
                if (out != null) {
                    out.close();
                }
                if (zin != null) {
                    zin.close();
                }
            } catch (Throwable th6) {
                if (0 == 0) {
                    th = th6;
                } else if (null != th6) {
                    th.addSuppressed(th6);
                }
                if (zin != null) {
                    zin.close();
                }
                throw th;
            }
        } finally {
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        if (this.zip != null) {
            try {
                this.zip.close();
            } catch (IOException e) {
                this.logger.error("Could not close apk file", (Throwable) e);
            }
            this.zip = null;
        }
    }
}
