package soot;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.util.SharedCloseable;
/* loaded from: gencallgraphv3.jar:soot/FoundFile.class */
public class FoundFile implements IFoundFile {
    private static final Logger logger = LoggerFactory.getLogger(FoundFile.class);
    protected final List<InputStream> openedInputStreams = new ArrayList();
    protected Path path;
    protected File file;
    protected String entryName;
    protected SharedCloseable<ZipFile> zipFile;
    protected ZipEntry zipEntry;

    public FoundFile(String archivePath, String entryName) {
        if (archivePath == null || entryName == null) {
            throw new IllegalArgumentException("Error: The archive path and entry name cannot be null.");
        }
        this.file = new File(archivePath);
        this.entryName = entryName;
    }

    public FoundFile(File file) {
        if (file == null) {
            throw new IllegalArgumentException("Error: The file cannot be null.");
        }
        this.file = file;
        this.entryName = null;
    }

    public FoundFile(Path path) {
        this.path = path;
    }

    @Override // soot.IFoundFile
    public String getFilePath() {
        File f;
        if (this.file == null) {
            if (this.path != null && (f = this.path.toFile()) != null) {
                return f.getPath();
            }
            return null;
        }
        return this.file.getPath();
    }

    @Override // soot.IFoundFile
    public boolean isZipFile() {
        return this.entryName != null;
    }

    @Override // soot.IFoundFile
    public ZipFile getZipFile() {
        if (this.zipFile != null) {
            return this.zipFile.get();
        }
        return null;
    }

    @Override // soot.IFoundFile
    public File getFile() {
        return this.file;
    }

    @Override // soot.IFoundFile
    public String getAbsolutePath() {
        try {
            return this.file != null ? this.file.getCanonicalPath() : this.path.toRealPath(new LinkOption[0]).toString();
        } catch (IOException e) {
            return this.file != null ? this.file.getAbsolutePath() : this.path.toAbsolutePath().toString();
        }
    }

    @Override // soot.IFoundFile
    public InputStream inputStream() {
        InputStream ret;
        if (this.path != null) {
            try {
                ret = Files.newInputStream(this.path, new OpenOption[0]);
            } catch (IOException e) {
                throw new RuntimeException("Error: Failed to open a InputStream for the file at path '" + this.path.toAbsolutePath().toString() + "'.", e);
            }
        } else if (!isZipFile()) {
            try {
                ret = new FileInputStream(this.file);
            } catch (Exception e2) {
                throw new RuntimeException("Error: Failed to open a InputStream for the file at path '" + this.file.getPath() + "'.", e2);
            }
        } else {
            if (this.zipFile == null) {
                try {
                    this.zipFile = SourceLocator.v().archivePathToZip.getRef(this.file.getPath());
                    this.zipEntry = this.zipFile.get().getEntry(this.entryName);
                    if (this.zipEntry == null) {
                        silentClose();
                        throw new RuntimeException("Error: Failed to find entry '" + this.entryName + "' in the archive file at path '" + this.file.getPath() + "'.");
                    }
                } catch (Exception e3) {
                    silentClose();
                    throw new RuntimeException("Error: Failed to open the archive file at path '" + this.file.getPath() + "' for entry '" + this.entryName + "'.", e3);
                }
            }
            try {
                InputStream stream = this.zipFile.get().getInputStream(this.zipEntry);
                try {
                    ret = doJDKBugWorkaround(stream, this.zipEntry.getSize());
                    if (stream != null) {
                        stream.close();
                    }
                } catch (Throwable th) {
                    if (stream != null) {
                        stream.close();
                    }
                    throw th;
                }
            } catch (Exception e4) {
                throw new RuntimeException("Error: Failed to open a InputStream for the entry '" + this.zipEntry.getName() + "' of the archive at path '" + this.zipFile.get().getName() + "'.", e4);
            }
        }
        InputStream ret2 = new BufferedInputStream(ret);
        this.openedInputStreams.add(ret2);
        return ret2;
    }

    @Override // soot.IFoundFile
    public void close() {
        List<Exception> errs = new ArrayList<>(0);
        for (InputStream is : this.openedInputStreams) {
            try {
                is.close();
            } catch (Exception e) {
                errs.add(e);
            }
        }
        this.openedInputStreams.clear();
        closeZipFile(errs);
        if (!errs.isEmpty()) {
            String msg = null;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                PrintStream ps = new PrintStream(baos, true, "utf-8");
                try {
                    ps.println("Error: Failed to close all opened resources. The following exceptions were thrown in the process: ");
                    int i = 0;
                    for (Throwable t : errs) {
                        ps.print("Exception ");
                        int i2 = i;
                        i++;
                        ps.print(i2);
                        ps.print(": ");
                        logger.error(t.getMessage(), t);
                    }
                    msg = new String(baos.toByteArray(), StandardCharsets.UTF_8);
                    if (ps != null) {
                        ps.close();
                    }
                } catch (Throwable th) {
                    if (ps != null) {
                        ps.close();
                    }
                    throw th;
                }
            } catch (Exception e2) {
            }
            throw new RuntimeException(msg);
        }
    }

    protected void closeZipFile(List<Exception> errs) {
        if (this.zipFile != null) {
            try {
                this.zipFile.close();
                errs.clear();
            } catch (Exception e) {
                errs.add(e);
            }
            this.zipFile = null;
            this.zipEntry = null;
        }
    }

    private static InputStream doJDKBugWorkaround(InputStream is, long size) throws IOException {
        int ln;
        int sz = (int) size;
        byte[] buf = new byte[sz];
        int count = 0;
        while (sz > 0 && (ln = is.read(buf, count, Math.min(1024, sz))) != -1) {
            count += ln;
            sz -= ln;
        }
        return new ByteArrayInputStream(buf);
    }
}
