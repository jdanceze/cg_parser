package android.provider;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.webkit.WebIconDatabase;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/provider/Browser.class */
public class Browser {
    public static final String INITIAL_ZOOM_LEVEL = "browser.initialZoomLevel";
    public static final String EXTRA_APPLICATION_ID = "com.android.browser.application_id";
    public static final String EXTRA_HEADERS = "com.android.browser.headers";
    public static final int HISTORY_PROJECTION_ID_INDEX = 0;
    public static final int HISTORY_PROJECTION_URL_INDEX = 1;
    public static final int HISTORY_PROJECTION_VISITS_INDEX = 2;
    public static final int HISTORY_PROJECTION_DATE_INDEX = 3;
    public static final int HISTORY_PROJECTION_BOOKMARK_INDEX = 4;
    public static final int HISTORY_PROJECTION_TITLE_INDEX = 5;
    public static final int HISTORY_PROJECTION_FAVICON_INDEX = 6;
    public static final int TRUNCATE_HISTORY_PROJECTION_ID_INDEX = 0;
    public static final int TRUNCATE_N_OLDEST = 5;
    public static final int SEARCHES_PROJECTION_SEARCH_INDEX = 1;
    public static final int SEARCHES_PROJECTION_DATE_INDEX = 2;
    public static final String EXTRA_CREATE_NEW_TAB = "create_new_tab";
    public static final String[] HISTORY_PROJECTION = null;
    public static final String[] TRUNCATE_HISTORY_PROJECTION = null;
    public static final String[] SEARCHES_PROJECTION = null;
    public static final Uri BOOKMARKS_URI = null;
    public static final Uri SEARCHES_URI = null;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/provider/Browser$BookmarkColumns.class */
    public static class BookmarkColumns implements BaseColumns {
        public static final String URL = "url";
        public static final String VISITS = "visits";
        public static final String DATE = "date";
        public static final String BOOKMARK = "bookmark";
        public static final String TITLE = "title";
        public static final String CREATED = "created";
        public static final String FAVICON = "favicon";

        public BookmarkColumns() {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/provider/Browser$SearchColumns.class */
    public static class SearchColumns implements BaseColumns {
        @Deprecated
        public static final String URL = "url";
        public static final String SEARCH = "search";
        public static final String DATE = "date";

        public SearchColumns() {
            throw new RuntimeException("Stub!");
        }
    }

    public Browser() {
        throw new RuntimeException("Stub!");
    }

    public static final void saveBookmark(Context c, String title, String url) {
        throw new RuntimeException("Stub!");
    }

    public static final void sendString(Context context, String string) {
        throw new RuntimeException("Stub!");
    }

    public static final Cursor getAllBookmarks(ContentResolver cr) throws IllegalStateException {
        throw new RuntimeException("Stub!");
    }

    public static final Cursor getAllVisitedUrls(ContentResolver cr) throws IllegalStateException {
        throw new RuntimeException("Stub!");
    }

    public static final void updateVisitedHistory(ContentResolver cr, String url, boolean real) {
        throw new RuntimeException("Stub!");
    }

    public static final void truncateHistory(ContentResolver cr) {
        throw new RuntimeException("Stub!");
    }

    public static final boolean canClearHistory(ContentResolver cr) {
        throw new RuntimeException("Stub!");
    }

    public static final void clearHistory(ContentResolver cr) {
        throw new RuntimeException("Stub!");
    }

    public static final void deleteHistoryTimeFrame(ContentResolver cr, long begin, long end) {
        throw new RuntimeException("Stub!");
    }

    public static final void deleteFromHistory(ContentResolver cr, String url) {
        throw new RuntimeException("Stub!");
    }

    public static final void addSearchUrl(ContentResolver cr, String search) {
        throw new RuntimeException("Stub!");
    }

    public static final void clearSearches(ContentResolver cr) {
        throw new RuntimeException("Stub!");
    }

    public static final void requestAllIcons(ContentResolver cr, String where, WebIconDatabase.IconListener listener) {
        throw new RuntimeException("Stub!");
    }
}
