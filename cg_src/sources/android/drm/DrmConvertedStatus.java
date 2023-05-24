package android.drm;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/drm/DrmConvertedStatus.class */
public class DrmConvertedStatus {
    public static final int STATUS_OK = 1;
    public static final int STATUS_INPUTDATA_ERROR = 2;
    public static final int STATUS_ERROR = 3;
    public final int statusCode;
    public final byte[] convertedData = null;
    public final int offset;

    public DrmConvertedStatus(int statusCode, byte[] convertedData, int offset) {
        throw new RuntimeException("Stub!");
    }
}
