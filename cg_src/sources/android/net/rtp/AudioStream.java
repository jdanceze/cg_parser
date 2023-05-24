package android.net.rtp;

import java.net.InetAddress;
import java.net.SocketException;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/rtp/AudioStream.class */
public class AudioStream extends RtpStream {
    public AudioStream(InetAddress address) throws SocketException {
        throw new RuntimeException("Stub!");
    }

    @Override // android.net.rtp.RtpStream
    public final boolean isBusy() {
        throw new RuntimeException("Stub!");
    }

    public AudioGroup getGroup() {
        throw new RuntimeException("Stub!");
    }

    public void join(AudioGroup group) {
        throw new RuntimeException("Stub!");
    }

    public AudioCodec getCodec() {
        throw new RuntimeException("Stub!");
    }

    public void setCodec(AudioCodec codec) {
        throw new RuntimeException("Stub!");
    }

    public int getDtmfType() {
        throw new RuntimeException("Stub!");
    }

    public void setDtmfType(int type) {
        throw new RuntimeException("Stub!");
    }
}
