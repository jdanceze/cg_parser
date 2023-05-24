package android.drm;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/drm/DrmManagerClient.class */
public class DrmManagerClient {
    public static final int ERROR_NONE = 0;
    public static final int ERROR_UNKNOWN = -2000;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/drm/DrmManagerClient$OnErrorListener.class */
    public interface OnErrorListener {
        void onError(DrmManagerClient drmManagerClient, DrmErrorEvent drmErrorEvent);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/drm/DrmManagerClient$OnEventListener.class */
    public interface OnEventListener {
        void onEvent(DrmManagerClient drmManagerClient, DrmEvent drmEvent);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/drm/DrmManagerClient$OnInfoListener.class */
    public interface OnInfoListener {
        void onInfo(DrmManagerClient drmManagerClient, DrmInfoEvent drmInfoEvent);
    }

    public DrmManagerClient(Context context) {
        throw new RuntimeException("Stub!");
    }

    protected void finalize() {
        throw new RuntimeException("Stub!");
    }

    public void release() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setOnInfoListener(OnInfoListener infoListener) {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setOnEventListener(OnEventListener eventListener) {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setOnErrorListener(OnErrorListener errorListener) {
        throw new RuntimeException("Stub!");
    }

    public String[] getAvailableDrmEngines() {
        throw new RuntimeException("Stub!");
    }

    public ContentValues getConstraints(String path, int action) {
        throw new RuntimeException("Stub!");
    }

    public ContentValues getMetadata(String path) {
        throw new RuntimeException("Stub!");
    }

    public ContentValues getConstraints(Uri uri, int action) {
        throw new RuntimeException("Stub!");
    }

    public ContentValues getMetadata(Uri uri) {
        throw new RuntimeException("Stub!");
    }

    public int saveRights(DrmRights drmRights, String rightsPath, String contentPath) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public boolean canHandle(String path, String mimeType) {
        throw new RuntimeException("Stub!");
    }

    public boolean canHandle(Uri uri, String mimeType) {
        throw new RuntimeException("Stub!");
    }

    public int processDrmInfo(DrmInfo drmInfo) {
        throw new RuntimeException("Stub!");
    }

    public DrmInfo acquireDrmInfo(DrmInfoRequest drmInfoRequest) {
        throw new RuntimeException("Stub!");
    }

    public int acquireRights(DrmInfoRequest drmInfoRequest) {
        throw new RuntimeException("Stub!");
    }

    public int getDrmObjectType(String path, String mimeType) {
        throw new RuntimeException("Stub!");
    }

    public int getDrmObjectType(Uri uri, String mimeType) {
        throw new RuntimeException("Stub!");
    }

    public String getOriginalMimeType(String path) {
        throw new RuntimeException("Stub!");
    }

    public String getOriginalMimeType(Uri uri) {
        throw new RuntimeException("Stub!");
    }

    public int checkRightsStatus(String path) {
        throw new RuntimeException("Stub!");
    }

    public int checkRightsStatus(Uri uri) {
        throw new RuntimeException("Stub!");
    }

    public int checkRightsStatus(String path, int action) {
        throw new RuntimeException("Stub!");
    }

    public int checkRightsStatus(Uri uri, int action) {
        throw new RuntimeException("Stub!");
    }

    public int removeRights(String path) {
        throw new RuntimeException("Stub!");
    }

    public int removeRights(Uri uri) {
        throw new RuntimeException("Stub!");
    }

    public int removeAllRights() {
        throw new RuntimeException("Stub!");
    }

    public int openConvertSession(String mimeType) {
        throw new RuntimeException("Stub!");
    }

    public DrmConvertedStatus convertData(int convertId, byte[] inputData) {
        throw new RuntimeException("Stub!");
    }

    public DrmConvertedStatus closeConvertSession(int convertId) {
        throw new RuntimeException("Stub!");
    }
}
