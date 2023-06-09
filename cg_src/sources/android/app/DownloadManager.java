package android.app;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import java.io.FileNotFoundException;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/DownloadManager.class */
public class DownloadManager {
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_URI = "uri";
    public static final String COLUMN_MEDIA_TYPE = "media_type";
    public static final String COLUMN_TOTAL_SIZE_BYTES = "total_size";
    public static final String COLUMN_LOCAL_URI = "local_uri";
    public static final String COLUMN_LOCAL_FILENAME = "local_filename";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_REASON = "reason";
    public static final String COLUMN_BYTES_DOWNLOADED_SO_FAR = "bytes_so_far";
    public static final String COLUMN_LAST_MODIFIED_TIMESTAMP = "last_modified_timestamp";
    public static final String COLUMN_MEDIAPROVIDER_URI = "mediaprovider_uri";
    public static final int STATUS_PENDING = 1;
    public static final int STATUS_RUNNING = 2;
    public static final int STATUS_PAUSED = 4;
    public static final int STATUS_SUCCESSFUL = 8;
    public static final int STATUS_FAILED = 16;
    public static final int ERROR_UNKNOWN = 1000;
    public static final int ERROR_FILE_ERROR = 1001;
    public static final int ERROR_UNHANDLED_HTTP_CODE = 1002;
    public static final int ERROR_HTTP_DATA_ERROR = 1004;
    public static final int ERROR_TOO_MANY_REDIRECTS = 1005;
    public static final int ERROR_INSUFFICIENT_SPACE = 1006;
    public static final int ERROR_DEVICE_NOT_FOUND = 1007;
    public static final int ERROR_CANNOT_RESUME = 1008;
    public static final int ERROR_FILE_ALREADY_EXISTS = 1009;
    public static final int PAUSED_WAITING_TO_RETRY = 1;
    public static final int PAUSED_WAITING_FOR_NETWORK = 2;
    public static final int PAUSED_QUEUED_FOR_WIFI = 3;
    public static final int PAUSED_UNKNOWN = 4;
    public static final String ACTION_DOWNLOAD_COMPLETE = "android.intent.action.DOWNLOAD_COMPLETE";
    public static final String ACTION_NOTIFICATION_CLICKED = "android.intent.action.DOWNLOAD_NOTIFICATION_CLICKED";
    public static final String ACTION_VIEW_DOWNLOADS = "android.intent.action.VIEW_DOWNLOADS";
    public static final String INTENT_EXTRAS_SORT_BY_SIZE = "android.app.DownloadManager.extra_sortBySize";
    public static final String EXTRA_DOWNLOAD_ID = "extra_download_id";
    public static final String EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS = "extra_click_download_ids";

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/DownloadManager$Request.class */
    public static class Request {
        public static final int NETWORK_MOBILE = 1;
        public static final int NETWORK_WIFI = 2;
        public static final int VISIBILITY_VISIBLE = 0;
        public static final int VISIBILITY_VISIBLE_NOTIFY_COMPLETED = 1;
        public static final int VISIBILITY_HIDDEN = 2;
        public static final int VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION = 3;

        public Request(Uri uri) {
            throw new RuntimeException("Stub!");
        }

        public Request setDestinationUri(Uri uri) {
            throw new RuntimeException("Stub!");
        }

        public Request setDestinationInExternalFilesDir(Context context, String dirType, String subPath) {
            throw new RuntimeException("Stub!");
        }

        public Request setDestinationInExternalPublicDir(String dirType, String subPath) {
            throw new RuntimeException("Stub!");
        }

        public void allowScanningByMediaScanner() {
            throw new RuntimeException("Stub!");
        }

        public Request addRequestHeader(String header, String value) {
            throw new RuntimeException("Stub!");
        }

        public Request setTitle(CharSequence title) {
            throw new RuntimeException("Stub!");
        }

        public Request setDescription(CharSequence description) {
            throw new RuntimeException("Stub!");
        }

        public Request setMimeType(String mimeType) {
            throw new RuntimeException("Stub!");
        }

        @Deprecated
        public Request setShowRunningNotification(boolean show) {
            throw new RuntimeException("Stub!");
        }

        public Request setNotificationVisibility(int visibility) {
            throw new RuntimeException("Stub!");
        }

        public Request setAllowedNetworkTypes(int flags) {
            throw new RuntimeException("Stub!");
        }

        public Request setAllowedOverRoaming(boolean allowed) {
            throw new RuntimeException("Stub!");
        }

        public Request setAllowedOverMetered(boolean allow) {
            throw new RuntimeException("Stub!");
        }

        public Request setVisibleInDownloadsUi(boolean isVisible) {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/DownloadManager$Query.class */
    public static class Query {
        public Query() {
            throw new RuntimeException("Stub!");
        }

        public Query setFilterById(long... ids) {
            throw new RuntimeException("Stub!");
        }

        public Query setFilterByStatus(int flags) {
            throw new RuntimeException("Stub!");
        }
    }

    DownloadManager() {
        throw new RuntimeException("Stub!");
    }

    public long enqueue(Request request) {
        throw new RuntimeException("Stub!");
    }

    public int remove(long... ids) {
        throw new RuntimeException("Stub!");
    }

    public Cursor query(Query query) {
        throw new RuntimeException("Stub!");
    }

    public ParcelFileDescriptor openDownloadedFile(long id) throws FileNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public Uri getUriForDownloadedFile(long id) {
        throw new RuntimeException("Stub!");
    }

    public String getMimeTypeForDownloadedFile(long id) {
        throw new RuntimeException("Stub!");
    }

    public static Long getMaxBytesOverMobile(Context context) {
        throw new RuntimeException("Stub!");
    }

    public static Long getRecommendedMaxBytesOverMobile(Context context) {
        throw new RuntimeException("Stub!");
    }

    public long addCompletedDownload(String title, String description, boolean isMediaScannerScannable, String mimeType, String path, long length, boolean showNotification) {
        throw new RuntimeException("Stub!");
    }
}
