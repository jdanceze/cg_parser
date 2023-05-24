package android.content.res;

import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/res/AssetFileDescriptor.class */
public class AssetFileDescriptor implements Parcelable {
    public static final long UNKNOWN_LENGTH = -1;
    public static final Parcelable.Creator<AssetFileDescriptor> CREATOR = null;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/res/AssetFileDescriptor$AutoCloseInputStream.class */
    public static class AutoCloseInputStream extends ParcelFileDescriptor.AutoCloseInputStream {
        public AutoCloseInputStream(AssetFileDescriptor fd) throws IOException {
            super(null);
            throw new RuntimeException("Stub!");
        }

        @Override // java.io.FileInputStream, java.io.InputStream
        public int available() throws IOException {
            throw new RuntimeException("Stub!");
        }

        @Override // java.io.FileInputStream, java.io.InputStream
        public int read() throws IOException {
            throw new RuntimeException("Stub!");
        }

        @Override // java.io.FileInputStream, java.io.InputStream
        public int read(byte[] buffer, int offset, int count) throws IOException {
            throw new RuntimeException("Stub!");
        }

        @Override // java.io.FileInputStream, java.io.InputStream
        public int read(byte[] buffer) throws IOException {
            throw new RuntimeException("Stub!");
        }

        @Override // java.io.FileInputStream, java.io.InputStream
        public long skip(long count) throws IOException {
            throw new RuntimeException("Stub!");
        }

        @Override // java.io.InputStream
        public void mark(int readlimit) {
            throw new RuntimeException("Stub!");
        }

        @Override // java.io.InputStream
        public boolean markSupported() {
            throw new RuntimeException("Stub!");
        }

        @Override // java.io.InputStream
        public synchronized void reset() throws IOException {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/res/AssetFileDescriptor$AutoCloseOutputStream.class */
    public static class AutoCloseOutputStream extends ParcelFileDescriptor.AutoCloseOutputStream {
        public AutoCloseOutputStream(AssetFileDescriptor fd) throws IOException {
            super(null);
            throw new RuntimeException("Stub!");
        }

        @Override // java.io.FileOutputStream, java.io.OutputStream
        public void write(byte[] buffer, int offset, int count) throws IOException {
            throw new RuntimeException("Stub!");
        }

        @Override // java.io.FileOutputStream, java.io.OutputStream
        public void write(byte[] buffer) throws IOException {
            throw new RuntimeException("Stub!");
        }

        @Override // java.io.FileOutputStream, java.io.OutputStream
        public void write(int oneByte) throws IOException {
            throw new RuntimeException("Stub!");
        }
    }

    public AssetFileDescriptor(ParcelFileDescriptor fd, long startOffset, long length) {
        throw new RuntimeException("Stub!");
    }

    public ParcelFileDescriptor getParcelFileDescriptor() {
        throw new RuntimeException("Stub!");
    }

    public FileDescriptor getFileDescriptor() {
        throw new RuntimeException("Stub!");
    }

    public long getStartOffset() {
        throw new RuntimeException("Stub!");
    }

    public long getLength() {
        throw new RuntimeException("Stub!");
    }

    public long getDeclaredLength() {
        throw new RuntimeException("Stub!");
    }

    public void close() throws IOException {
        throw new RuntimeException("Stub!");
    }

    public FileInputStream createInputStream() throws IOException {
        throw new RuntimeException("Stub!");
    }

    public FileOutputStream createOutputStream() throws IOException {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        throw new RuntimeException("Stub!");
    }
}
