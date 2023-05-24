package android.os;

import android.os.Parcelable;
import java.io.Closeable;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/os/ParcelFileDescriptor.class */
public class ParcelFileDescriptor implements Parcelable, Closeable {
    public static final int MODE_WORLD_READABLE = 1;
    public static final int MODE_WORLD_WRITEABLE = 2;
    public static final int MODE_READ_ONLY = 268435456;
    public static final int MODE_WRITE_ONLY = 536870912;
    public static final int MODE_READ_WRITE = 805306368;
    public static final int MODE_CREATE = 134217728;
    public static final int MODE_TRUNCATE = 67108864;
    public static final int MODE_APPEND = 33554432;
    public static final Parcelable.Creator<ParcelFileDescriptor> CREATOR = null;

    public native long getStatSize();

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/os/ParcelFileDescriptor$AutoCloseInputStream.class */
    public static class AutoCloseInputStream extends FileInputStream {
        public AutoCloseInputStream(ParcelFileDescriptor fd) {
            super((FileDescriptor) null);
            throw new RuntimeException("Stub!");
        }

        @Override // java.io.FileInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/os/ParcelFileDescriptor$AutoCloseOutputStream.class */
    public static class AutoCloseOutputStream extends FileOutputStream {
        public AutoCloseOutputStream(ParcelFileDescriptor fd) {
            super((FileDescriptor) null);
            throw new RuntimeException("Stub!");
        }

        @Override // java.io.FileOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            throw new RuntimeException("Stub!");
        }
    }

    public ParcelFileDescriptor(ParcelFileDescriptor descriptor) {
        throw new RuntimeException("Stub!");
    }

    public static ParcelFileDescriptor open(File file, int mode) throws FileNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public static ParcelFileDescriptor dup(FileDescriptor orig) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public ParcelFileDescriptor dup() throws IOException {
        throw new RuntimeException("Stub!");
    }

    public static ParcelFileDescriptor fromFd(int fd) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public static ParcelFileDescriptor adoptFd(int fd) {
        throw new RuntimeException("Stub!");
    }

    public static ParcelFileDescriptor fromSocket(Socket socket) {
        throw new RuntimeException("Stub!");
    }

    public static ParcelFileDescriptor fromDatagramSocket(DatagramSocket datagramSocket) {
        throw new RuntimeException("Stub!");
    }

    public static ParcelFileDescriptor[] createPipe() throws IOException {
        throw new RuntimeException("Stub!");
    }

    public FileDescriptor getFileDescriptor() {
        throw new RuntimeException("Stub!");
    }

    public int getFd() {
        throw new RuntimeException("Stub!");
    }

    public int detachFd() {
        throw new RuntimeException("Stub!");
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
        throw new RuntimeException("Stub!");
    }

    protected void finalize() throws Throwable {
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
