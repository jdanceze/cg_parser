package android.provider;

import android.content.ContentResolver;
import android.content.Context;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/provider/SearchRecentSuggestions.class */
public class SearchRecentSuggestions {
    public static final String[] QUERIES_PROJECTION_1LINE = null;
    public static final String[] QUERIES_PROJECTION_2LINE = null;
    public static final int QUERIES_PROJECTION_DATE_INDEX = 1;
    public static final int QUERIES_PROJECTION_QUERY_INDEX = 2;
    public static final int QUERIES_PROJECTION_DISPLAY1_INDEX = 3;
    public static final int QUERIES_PROJECTION_DISPLAY2_INDEX = 4;

    public SearchRecentSuggestions(Context context, String authority, int mode) {
        throw new RuntimeException("Stub!");
    }

    public void saveRecentQuery(String queryString, String line2) {
        throw new RuntimeException("Stub!");
    }

    public void clearHistory() {
        throw new RuntimeException("Stub!");
    }

    protected void truncateHistory(ContentResolver cr, int maxEntries) {
        throw new RuntimeException("Stub!");
    }
}
