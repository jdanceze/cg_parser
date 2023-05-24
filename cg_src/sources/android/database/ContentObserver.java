package android.database;

import android.net.Uri;
import android.os.Handler;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/database/ContentObserver.class */
public abstract class ContentObserver {
    public ContentObserver(Handler handler) {
        throw new RuntimeException("Stub!");
    }

    public boolean deliverSelfNotifications() {
        throw new RuntimeException("Stub!");
    }

    public void onChange(boolean selfChange) {
        throw new RuntimeException("Stub!");
    }

    public void onChange(boolean selfChange, Uri uri) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public final void dispatchChange(boolean selfChange) {
        throw new RuntimeException("Stub!");
    }

    public final void dispatchChange(boolean selfChange, Uri uri) {
        throw new RuntimeException("Stub!");
    }
}
