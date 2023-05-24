package android.content.res;

import java.io.IOException;
import java.io.InputStream;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/res/AssetManager.class */
public final class AssetManager {
    public static final int ACCESS_UNKNOWN = 0;
    public static final int ACCESS_RANDOM = 1;
    public static final int ACCESS_STREAMING = 2;
    public static final int ACCESS_BUFFER = 3;

    public final native String[] list(String str) throws IOException;

    public final native String[] getLocales();

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/res/AssetManager$AssetInputStream.class */
    public final class AssetInputStream extends InputStream {
        AssetInputStream() {
            throw new RuntimeException("Stub!");
        }

        public final int getAssetInt() {
            throw new RuntimeException("Stub!");
        }

        @Override // java.io.InputStream
        public final int read() throws IOException {
            throw new RuntimeException("Stub!");
        }

        @Override // java.io.InputStream
        public final boolean markSupported() {
            throw new RuntimeException("Stub!");
        }

        @Override // java.io.InputStream
        public final int available() throws IOException {
            throw new RuntimeException("Stub!");
        }

        @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public final void close() throws IOException {
            throw new RuntimeException("Stub!");
        }

        @Override // java.io.InputStream
        public final void mark(int readlimit) {
            throw new RuntimeException("Stub!");
        }

        @Override // java.io.InputStream
        public final void reset() throws IOException {
            throw new RuntimeException("Stub!");
        }

        @Override // java.io.InputStream
        public final int read(byte[] b) throws IOException {
            throw new RuntimeException("Stub!");
        }

        @Override // java.io.InputStream
        public final int read(byte[] b, int off, int len) throws IOException {
            throw new RuntimeException("Stub!");
        }

        @Override // java.io.InputStream
        public final long skip(long n) throws IOException {
            throw new RuntimeException("Stub!");
        }

        protected void finalize() throws Throwable {
            throw new RuntimeException("Stub!");
        }
    }

    AssetManager() {
        throw new RuntimeException("Stub!");
    }

    public void close() {
        throw new RuntimeException("Stub!");
    }

    public final InputStream open(String fileName) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public final InputStream open(String fileName, int accessMode) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public final AssetFileDescriptor openFd(String fileName) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public final AssetFileDescriptor openNonAssetFd(String fileName) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public final AssetFileDescriptor openNonAssetFd(int cookie, String fileName) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public final XmlResourceParser openXmlResourceParser(String fileName) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public final XmlResourceParser openXmlResourceParser(int cookie, String fileName) throws IOException {
        throw new RuntimeException("Stub!");
    }

    protected void finalize() throws Throwable {
        throw new RuntimeException("Stub!");
    }
}
