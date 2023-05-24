package android.provider;

import android.content.Context;
import android.net.Uri;
import java.util.Locale;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/provider/UserDictionary.class */
public class UserDictionary {
    public static final String AUTHORITY = "user_dictionary";
    public static final Uri CONTENT_URI = null;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/provider/UserDictionary$Words.class */
    public static class Words implements BaseColumns {
        public static final Uri CONTENT_URI = null;
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.userword";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.google.userword";
        public static final String _ID = "_id";
        public static final String WORD = "word";
        public static final String FREQUENCY = "frequency";
        public static final String LOCALE = "locale";
        public static final String APP_ID = "appid";
        public static final String SHORTCUT = "shortcut";
        @Deprecated
        public static final int LOCALE_TYPE_ALL = 0;
        @Deprecated
        public static final int LOCALE_TYPE_CURRENT = 1;
        public static final String DEFAULT_SORT_ORDER = "frequency DESC";

        public Words() {
            throw new RuntimeException("Stub!");
        }

        @Deprecated
        public static void addWord(Context context, String word, int frequency, int localeType) {
            throw new RuntimeException("Stub!");
        }

        public static void addWord(Context context, String word, int frequency, String shortcut, Locale locale) {
            throw new RuntimeException("Stub!");
        }
    }

    public UserDictionary() {
        throw new RuntimeException("Stub!");
    }
}
