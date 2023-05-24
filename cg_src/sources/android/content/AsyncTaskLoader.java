package android.content;

import java.io.FileDescriptor;
import java.io.PrintWriter;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/AsyncTaskLoader.class */
public abstract class AsyncTaskLoader<D> extends Loader<D> {
    public abstract D loadInBackground();

    public AsyncTaskLoader(Context context) {
        super(null);
        throw new RuntimeException("Stub!");
    }

    public void setUpdateThrottle(long delayMS) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Loader
    protected void onForceLoad() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Loader
    protected boolean onCancelLoad() {
        throw new RuntimeException("Stub!");
    }

    public void onCanceled(D data) {
        throw new RuntimeException("Stub!");
    }

    protected D onLoadInBackground() {
        throw new RuntimeException("Stub!");
    }

    public void cancelLoadInBackground() {
        throw new RuntimeException("Stub!");
    }

    public boolean isLoadInBackgroundCanceled() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Loader
    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        throw new RuntimeException("Stub!");
    }
}
