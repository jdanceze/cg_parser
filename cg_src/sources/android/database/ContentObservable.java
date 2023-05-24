package android.database;

import android.net.Uri;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/database/ContentObservable.class */
public class ContentObservable extends Observable<ContentObserver> {
    public ContentObservable() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Observable
    public void registerObserver(ContentObserver observer) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void dispatchChange(boolean selfChange) {
        throw new RuntimeException("Stub!");
    }

    public void dispatchChange(boolean selfChange, Uri uri) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void notifyChange(boolean selfChange) {
        throw new RuntimeException("Stub!");
    }
}
