package android.content;

import android.database.ContentObserver;
import java.io.FileDescriptor;
import java.io.PrintWriter;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/Loader.class */
public class Loader<D> {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/Loader$OnLoadCanceledListener.class */
    public interface OnLoadCanceledListener<D> {
        void onLoadCanceled(Loader<D> loader);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/Loader$OnLoadCompleteListener.class */
    public interface OnLoadCompleteListener<D> {
        void onLoadComplete(Loader<D> loader, D d);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/Loader$ForceLoadContentObserver.class */
    public final class ForceLoadContentObserver extends ContentObserver {
        public ForceLoadContentObserver() {
            super(null);
            throw new RuntimeException("Stub!");
        }

        @Override // android.database.ContentObserver
        public boolean deliverSelfNotifications() {
            throw new RuntimeException("Stub!");
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            throw new RuntimeException("Stub!");
        }
    }

    public Loader(Context context) {
        throw new RuntimeException("Stub!");
    }

    public void deliverResult(D data) {
        throw new RuntimeException("Stub!");
    }

    public void deliverCancellation() {
        throw new RuntimeException("Stub!");
    }

    public Context getContext() {
        throw new RuntimeException("Stub!");
    }

    public int getId() {
        throw new RuntimeException("Stub!");
    }

    public void registerListener(int id, OnLoadCompleteListener<D> listener) {
        throw new RuntimeException("Stub!");
    }

    public void unregisterListener(OnLoadCompleteListener<D> listener) {
        throw new RuntimeException("Stub!");
    }

    public void registerOnLoadCanceledListener(OnLoadCanceledListener<D> listener) {
        throw new RuntimeException("Stub!");
    }

    public void unregisterOnLoadCanceledListener(OnLoadCanceledListener<D> listener) {
        throw new RuntimeException("Stub!");
    }

    public boolean isStarted() {
        throw new RuntimeException("Stub!");
    }

    public boolean isAbandoned() {
        throw new RuntimeException("Stub!");
    }

    public boolean isReset() {
        throw new RuntimeException("Stub!");
    }

    public final void startLoading() {
        throw new RuntimeException("Stub!");
    }

    protected void onStartLoading() {
        throw new RuntimeException("Stub!");
    }

    public boolean cancelLoad() {
        throw new RuntimeException("Stub!");
    }

    protected boolean onCancelLoad() {
        throw new RuntimeException("Stub!");
    }

    public void forceLoad() {
        throw new RuntimeException("Stub!");
    }

    protected void onForceLoad() {
        throw new RuntimeException("Stub!");
    }

    public void stopLoading() {
        throw new RuntimeException("Stub!");
    }

    protected void onStopLoading() {
        throw new RuntimeException("Stub!");
    }

    public void abandon() {
        throw new RuntimeException("Stub!");
    }

    protected void onAbandon() {
        throw new RuntimeException("Stub!");
    }

    public void reset() {
        throw new RuntimeException("Stub!");
    }

    protected void onReset() {
        throw new RuntimeException("Stub!");
    }

    public boolean takeContentChanged() {
        throw new RuntimeException("Stub!");
    }

    public void onContentChanged() {
        throw new RuntimeException("Stub!");
    }

    public String dataToString(D data) {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
        throw new RuntimeException("Stub!");
    }

    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        throw new RuntimeException("Stub!");
    }
}
