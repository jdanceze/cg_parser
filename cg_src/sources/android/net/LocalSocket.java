package android.net;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/LocalSocket.class */
public class LocalSocket {
    public LocalSocket() {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
        throw new RuntimeException("Stub!");
    }

    public void connect(LocalSocketAddress endpoint) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public void bind(LocalSocketAddress bindpoint) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public LocalSocketAddress getLocalSocketAddress() {
        throw new RuntimeException("Stub!");
    }

    public InputStream getInputStream() throws IOException {
        throw new RuntimeException("Stub!");
    }

    public OutputStream getOutputStream() throws IOException {
        throw new RuntimeException("Stub!");
    }

    public void close() throws IOException {
        throw new RuntimeException("Stub!");
    }

    public void shutdownInput() throws IOException {
        throw new RuntimeException("Stub!");
    }

    public void shutdownOutput() throws IOException {
        throw new RuntimeException("Stub!");
    }

    public void setReceiveBufferSize(int size) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public int getReceiveBufferSize() throws IOException {
        throw new RuntimeException("Stub!");
    }

    public void setSoTimeout(int n) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public int getSoTimeout() throws IOException {
        throw new RuntimeException("Stub!");
    }

    public void setSendBufferSize(int n) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public int getSendBufferSize() throws IOException {
        throw new RuntimeException("Stub!");
    }

    public LocalSocketAddress getRemoteSocketAddress() {
        throw new RuntimeException("Stub!");
    }

    public synchronized boolean isConnected() {
        throw new RuntimeException("Stub!");
    }

    public boolean isClosed() {
        throw new RuntimeException("Stub!");
    }

    public synchronized boolean isBound() {
        throw new RuntimeException("Stub!");
    }

    public boolean isOutputShutdown() {
        throw new RuntimeException("Stub!");
    }

    public boolean isInputShutdown() {
        throw new RuntimeException("Stub!");
    }

    public void connect(LocalSocketAddress endpoint, int timeout) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public void setFileDescriptorsForSend(FileDescriptor[] fds) {
        throw new RuntimeException("Stub!");
    }

    public FileDescriptor[] getAncillaryFileDescriptors() throws IOException {
        throw new RuntimeException("Stub!");
    }

    public Credentials getPeerCredentials() throws IOException {
        throw new RuntimeException("Stub!");
    }

    public FileDescriptor getFileDescriptor() {
        throw new RuntimeException("Stub!");
    }
}
