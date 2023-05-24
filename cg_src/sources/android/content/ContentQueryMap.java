package android.content;

import android.database.Cursor;
import android.os.Handler;
import java.util.Map;
import java.util.Observable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/ContentQueryMap.class */
public class ContentQueryMap extends Observable {
    public ContentQueryMap(Cursor cursor, String columnNameOfKey, boolean keepUpdated, Handler handlerForUpdateNotifications) {
        throw new RuntimeException("Stub!");
    }

    public void setKeepUpdated(boolean keepUpdated) {
        throw new RuntimeException("Stub!");
    }

    public synchronized ContentValues getValues(String rowName) {
        throw new RuntimeException("Stub!");
    }

    public void requery() {
        throw new RuntimeException("Stub!");
    }

    public synchronized Map<String, ContentValues> getRows() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void close() {
        throw new RuntimeException("Stub!");
    }

    protected void finalize() throws Throwable {
        throw new RuntimeException("Stub!");
    }
}
