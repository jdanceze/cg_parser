package android.content;

import android.database.Cursor;
import android.net.Uri;
import java.io.FileDescriptor;
import java.io.PrintWriter;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/CursorLoader.class */
public class CursorLoader extends AsyncTaskLoader<Cursor> {
    public CursorLoader(Context context) {
        super(null);
        throw new RuntimeException("Stub!");
    }

    public CursorLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        super(null);
        throw new RuntimeException("Stub!");
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.content.AsyncTaskLoader
    public Cursor loadInBackground() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.AsyncTaskLoader
    public void cancelLoadInBackground() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Loader
    public void deliverResult(Cursor cursor) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Loader
    protected void onStartLoading() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Loader
    protected void onStopLoading() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.AsyncTaskLoader
    public void onCanceled(Cursor cursor) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Loader
    protected void onReset() {
        throw new RuntimeException("Stub!");
    }

    public Uri getUri() {
        throw new RuntimeException("Stub!");
    }

    public void setUri(Uri uri) {
        throw new RuntimeException("Stub!");
    }

    public String[] getProjection() {
        throw new RuntimeException("Stub!");
    }

    public void setProjection(String[] projection) {
        throw new RuntimeException("Stub!");
    }

    public String getSelection() {
        throw new RuntimeException("Stub!");
    }

    public void setSelection(String selection) {
        throw new RuntimeException("Stub!");
    }

    public String[] getSelectionArgs() {
        throw new RuntimeException("Stub!");
    }

    public void setSelectionArgs(String[] selectionArgs) {
        throw new RuntimeException("Stub!");
    }

    public String getSortOrder() {
        throw new RuntimeException("Stub!");
    }

    public void setSortOrder(String sortOrder) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.AsyncTaskLoader, android.content.Loader
    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        throw new RuntimeException("Stub!");
    }
}
