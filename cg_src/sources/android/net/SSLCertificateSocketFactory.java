package android.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import javax.net.SocketFactory;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/SSLCertificateSocketFactory.class */
public class SSLCertificateSocketFactory extends SSLSocketFactory {
    @Deprecated
    public SSLCertificateSocketFactory(int handshakeTimeoutMillis) {
        throw new RuntimeException("Stub!");
    }

    public static SocketFactory getDefault(int handshakeTimeoutMillis) {
        throw new RuntimeException("Stub!");
    }

    public static SSLSocketFactory getDefault(int handshakeTimeoutMillis, SSLSessionCache cache) {
        throw new RuntimeException("Stub!");
    }

    public static SSLSocketFactory getInsecure(int handshakeTimeoutMillis, SSLSessionCache cache) {
        throw new RuntimeException("Stub!");
    }

    public static org.apache.http.conn.ssl.SSLSocketFactory getHttpSocketFactory(int handshakeTimeoutMillis, SSLSessionCache cache) {
        throw new RuntimeException("Stub!");
    }

    public void setTrustManagers(TrustManager[] trustManager) {
        throw new RuntimeException("Stub!");
    }

    public void setNpnProtocols(byte[][] npnProtocols) {
        throw new RuntimeException("Stub!");
    }

    public byte[] getNpnSelectedProtocol(Socket socket) {
        throw new RuntimeException("Stub!");
    }

    public void setKeyManagers(KeyManager[] keyManagers) {
        throw new RuntimeException("Stub!");
    }

    @Override // javax.net.ssl.SSLSocketFactory
    public Socket createSocket(Socket k, String host, int port, boolean close) throws IOException {
        throw new RuntimeException("Stub!");
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket() throws IOException {
        throw new RuntimeException("Stub!");
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(InetAddress addr, int port, InetAddress localAddr, int localPort) throws IOException {
        throw new RuntimeException("Stub!");
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(InetAddress addr, int port) throws IOException {
        throw new RuntimeException("Stub!");
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(String host, int port, InetAddress localAddr, int localPort) throws IOException {
        throw new RuntimeException("Stub!");
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(String host, int port) throws IOException {
        throw new RuntimeException("Stub!");
    }

    @Override // javax.net.ssl.SSLSocketFactory
    public String[] getDefaultCipherSuites() {
        throw new RuntimeException("Stub!");
    }

    @Override // javax.net.ssl.SSLSocketFactory
    public String[] getSupportedCipherSuites() {
        throw new RuntimeException("Stub!");
    }
}
