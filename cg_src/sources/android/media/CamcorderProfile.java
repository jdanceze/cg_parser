package android.media;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/CamcorderProfile.class */
public class CamcorderProfile {
    public static final int QUALITY_LOW = 0;
    public static final int QUALITY_HIGH = 1;
    public static final int QUALITY_QCIF = 2;
    public static final int QUALITY_CIF = 3;
    public static final int QUALITY_480P = 4;
    public static final int QUALITY_720P = 5;
    public static final int QUALITY_1080P = 6;
    public static final int QUALITY_QVGA = 7;
    public static final int QUALITY_TIME_LAPSE_LOW = 1000;
    public static final int QUALITY_TIME_LAPSE_HIGH = 1001;
    public static final int QUALITY_TIME_LAPSE_QCIF = 1002;
    public static final int QUALITY_TIME_LAPSE_CIF = 1003;
    public static final int QUALITY_TIME_LAPSE_480P = 1004;
    public static final int QUALITY_TIME_LAPSE_720P = 1005;
    public static final int QUALITY_TIME_LAPSE_1080P = 1006;
    public static final int QUALITY_TIME_LAPSE_QVGA = 1007;
    public int duration;
    public int quality;
    public int fileFormat;
    public int videoCodec;
    public int videoBitRate;
    public int videoFrameRate;
    public int videoFrameWidth;
    public int videoFrameHeight;
    public int audioCodec;
    public int audioBitRate;
    public int audioSampleRate;
    public int audioChannels;

    CamcorderProfile() {
        throw new RuntimeException("Stub!");
    }

    public static CamcorderProfile get(int quality) {
        throw new RuntimeException("Stub!");
    }

    public static CamcorderProfile get(int cameraId, int quality) {
        throw new RuntimeException("Stub!");
    }

    public static boolean hasProfile(int quality) {
        throw new RuntimeException("Stub!");
    }

    public static boolean hasProfile(int cameraId, int quality) {
        throw new RuntimeException("Stub!");
    }
}
