package android.content;

import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/AsyncQueryHandler.class */
public abstract class AsyncQueryHandler extends Handler {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/AsyncQueryHandler$WorkerArgs.class */
    protected static final class WorkerArgs {
        public Uri uri;
        public Handler handler;
        public String selection;
        public String orderBy;
        public Object result;
        public Object cookie;
        public ContentValues values;
        public String[] projection = null;
        public String[] selectionArgs = null;

        protected WorkerArgs() {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/AsyncQueryHandler$WorkerHandler.class */
    protected class WorkerHandler extends Handler {
        public WorkerHandler(Looper looper) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            throw new RuntimeException("Stub!");
        }
    }

    public AsyncQueryHandler(ContentResolver cr) {
        throw new RuntimeException("Stub!");
    }

    protected Handler createHandler(Looper looper) {
        throw new RuntimeException("Stub!");
    }

    public void startQuery(int token, Object cookie, Uri uri, String[] projection, String selection, String[] selectionArgs, String orderBy) {
        throw new RuntimeException("Stub!");
    }

    public final void cancelOperation(int token) {
        throw new RuntimeException("Stub!");
    }

    public final void startInsert(int token, Object cookie, Uri uri, ContentValues initialValues) {
        throw new RuntimeException("Stub!");
    }

    public final void startUpdate(int token, Object cookie, Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new RuntimeException("Stub!");
    }

    public final void startDelete(int token, Object cookie, Uri uri, String selection, String[] selectionArgs) {
        throw new RuntimeException("Stub!");
    }

    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        throw new RuntimeException("Stub!");
    }

    protected void onInsertComplete(int token, Object cookie, Uri uri) {
        throw new RuntimeException("Stub!");
    }

    protected void onUpdateComplete(int token, Object cookie, int result) {
        throw new RuntimeException("Stub!");
    }

    protected void onDeleteComplete(int token, Object cookie, int result) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Handler
    public void handleMessage(Message msg) {
        throw new RuntimeException("Stub!");
    }
}
