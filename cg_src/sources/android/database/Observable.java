package android.database;

import java.util.ArrayList;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/database/Observable.class */
public abstract class Observable<T> {
    protected final ArrayList<T> mObservers;

    public Observable() {
        throw new RuntimeException("Stub!");
    }

    public void registerObserver(T observer) {
        throw new RuntimeException("Stub!");
    }

    public void unregisterObserver(T observer) {
        throw new RuntimeException("Stub!");
    }

    public void unregisterAll() {
        throw new RuntimeException("Stub!");
    }
}
