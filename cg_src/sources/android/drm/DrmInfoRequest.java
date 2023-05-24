package android.drm;

import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/drm/DrmInfoRequest.class */
public class DrmInfoRequest {
    public static final int TYPE_REGISTRATION_INFO = 1;
    public static final int TYPE_UNREGISTRATION_INFO = 2;
    public static final int TYPE_RIGHTS_ACQUISITION_INFO = 3;
    public static final int TYPE_RIGHTS_ACQUISITION_PROGRESS_INFO = 4;
    public static final String ACCOUNT_ID = "account_id";
    public static final String SUBSCRIPTION_ID = "subscription_id";

    public DrmInfoRequest(int infoType, String mimeType) {
        throw new RuntimeException("Stub!");
    }

    public String getMimeType() {
        throw new RuntimeException("Stub!");
    }

    public int getInfoType() {
        throw new RuntimeException("Stub!");
    }

    public void put(String key, Object value) {
        throw new RuntimeException("Stub!");
    }

    public Object get(String key) {
        throw new RuntimeException("Stub!");
    }

    public Iterator<String> keyIterator() {
        throw new RuntimeException("Stub!");
    }

    public Iterator<Object> iterator() {
        throw new RuntimeException("Stub!");
    }
}
