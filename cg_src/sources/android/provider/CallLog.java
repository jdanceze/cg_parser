package android.provider;

import android.content.Context;
import android.net.Uri;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/provider/CallLog.class */
public class CallLog {
    public static final String AUTHORITY = "call_log";
    public static final Uri CONTENT_URI = null;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/provider/CallLog$Calls.class */
    public static class Calls implements BaseColumns {
        public static final Uri CONTENT_URI = null;
        public static final Uri CONTENT_FILTER_URI = null;
        public static final String DEFAULT_SORT_ORDER = "date DESC";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/calls";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/calls";
        public static final String TYPE = "type";
        public static final int INCOMING_TYPE = 1;
        public static final int OUTGOING_TYPE = 2;
        public static final int MISSED_TYPE = 3;
        public static final String NUMBER = "number";
        public static final String DATE = "date";
        public static final String DURATION = "duration";
        public static final String NEW = "new";
        public static final String CACHED_NAME = "name";
        public static final String CACHED_NUMBER_TYPE = "numbertype";
        public static final String CACHED_NUMBER_LABEL = "numberlabel";
        public static final String IS_READ = "is_read";

        public Calls() {
            throw new RuntimeException("Stub!");
        }

        public static String getLastOutgoingCall(Context context) {
            throw new RuntimeException("Stub!");
        }
    }

    public CallLog() {
        throw new RuntimeException("Stub!");
    }
}
