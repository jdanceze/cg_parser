package android.drm;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/drm/DrmInfoStatus.class */
public class DrmInfoStatus {
    public static final int STATUS_OK = 1;
    public static final int STATUS_ERROR = 2;
    public final int statusCode;
    public final int infoType;
    public final String mimeType;
    public final ProcessedData data;

    public DrmInfoStatus(int statusCode, int infoType, ProcessedData data, String mimeType) {
        throw new RuntimeException("Stub!");
    }
}
