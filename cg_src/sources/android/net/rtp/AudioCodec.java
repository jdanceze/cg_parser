package android.net.rtp;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/rtp/AudioCodec.class */
public class AudioCodec {
    public final int type;
    public final String rtpmap;
    public final String fmtp;
    public static final AudioCodec PCMU = null;
    public static final AudioCodec PCMA = null;
    public static final AudioCodec GSM = null;
    public static final AudioCodec GSM_EFR = null;
    public static final AudioCodec AMR = null;

    AudioCodec() {
        throw new RuntimeException("Stub!");
    }

    public static AudioCodec[] getCodecs() {
        throw new RuntimeException("Stub!");
    }

    public static AudioCodec getCodec(int type, String rtpmap, String fmtp) {
        throw new RuntimeException("Stub!");
    }
}
