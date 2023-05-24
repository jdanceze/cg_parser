package soot.JastAddJ;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/ZipFilePart.class */
public class ZipFilePart extends PathPart {
    private Set<String> set = new HashSet();
    private File file;

    @Override // soot.JastAddJ.PathPart
    public boolean hasPackage(String name) {
        return this.set.contains(name);
    }

    public ZipFilePart(File file) throws IOException {
        this.file = file;
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(file);
            Enumeration<? extends ZipEntry> e = zipFile.entries();
            while (e.hasMoreElements()) {
                ZipEntry entry = e.nextElement();
                String pathName = new File(entry.getName()).getParent();
                pathName = pathName != null ? pathName.replace(File.separatorChar, '.') : pathName;
                if (!this.set.contains(pathName)) {
                    int pos = 0;
                    while (pathName != null) {
                        int indexOf = pathName.indexOf(46, pos + 1);
                        pos = indexOf;
                        if (-1 == indexOf) {
                            break;
                        }
                        String n = pathName.substring(0, pos);
                        if (!this.set.contains(n)) {
                            this.set.add(n);
                        }
                    }
                    this.set.add(pathName);
                }
                this.set.add(entry.getName());
            }
            if (zipFile != null) {
                zipFile.close();
            }
        } catch (Throwable th) {
            if (zipFile != null) {
                zipFile.close();
            }
            throw th;
        }
    }

    @Override // soot.JastAddJ.PathPart
    public boolean selectCompilationUnit(String canonicalName) throws IOException {
        ZipEntry zipEntry;
        ZipFile zipFile = null;
        boolean success = false;
        try {
            zipFile = new ZipFile(this.file);
            String name = String.valueOf(canonicalName.replace('.', '/')) + fileSuffix();
            if (this.set.contains(name) && (zipEntry = zipFile.getEntry(name)) != null && !zipEntry.isDirectory()) {
                this.is = new ZipEntryInputStreamWrapper(zipFile, zipEntry);
                this.age = zipEntry.getTime();
                this.pathName = zipFile.getName();
                this.relativeName = String.valueOf(name) + fileSuffix();
                this.fullName = canonicalName;
                success = true;
            }
            if (zipFile != null && !success) {
                zipFile.close();
            }
            return success;
        } catch (Throwable th) {
            if (zipFile != null && !success) {
                zipFile.close();
            }
            throw th;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/JastAddJ/ZipFilePart$ZipEntryInputStreamWrapper.class */
    public static class ZipEntryInputStreamWrapper extends InputStream {
        private ZipFile zipFile;
        private InputStream entryInputStream;

        public ZipEntryInputStreamWrapper(ZipFile zipFile, ZipEntry zipEntry) throws IOException {
            this.zipFile = zipFile;
            this.entryInputStream = zipFile.getInputStream(zipEntry);
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            return this.entryInputStream.read();
        }

        @Override // java.io.InputStream
        public int read(byte[] b) throws IOException {
            return this.entryInputStream.read(b);
        }

        @Override // java.io.InputStream
        public int read(byte[] b, int off, int len) throws IOException {
            return this.entryInputStream.read(b, off, len);
        }

        @Override // java.io.InputStream
        public long skip(long n) throws IOException {
            return this.entryInputStream.skip(n);
        }

        @Override // java.io.InputStream
        public int available() throws IOException {
            return this.entryInputStream.available();
        }

        @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            try {
                this.entryInputStream.close();
            } finally {
                if (this.zipFile != null) {
                    this.zipFile.close();
                    this.zipFile = null;
                }
            }
        }

        @Override // java.io.InputStream
        public synchronized void mark(int readlimit) {
            this.entryInputStream.mark(readlimit);
        }

        @Override // java.io.InputStream
        public synchronized void reset() throws IOException {
            this.entryInputStream.reset();
        }

        @Override // java.io.InputStream
        public boolean markSupported() {
            return this.entryInputStream.markSupported();
        }
    }
}
