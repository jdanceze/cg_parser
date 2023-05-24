package android.content;

import android.database.Cursor;
import android.net.Uri;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/SearchRecentSuggestionsProvider.class */
public class SearchRecentSuggestionsProvider extends ContentProvider {
    public static final int DATABASE_MODE_QUERIES = 1;
    public static final int DATABASE_MODE_2LINES = 2;

    public SearchRecentSuggestionsProvider() {
        throw new RuntimeException("Stub!");
    }

    protected void setupSuggestions(String authority, int mode) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.ContentProvider
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.ContentProvider
    public String getType(Uri uri) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.ContentProvider
    public Uri insert(Uri uri, ContentValues values) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.ContentProvider
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new RuntimeException("Stub!");
    }
}
