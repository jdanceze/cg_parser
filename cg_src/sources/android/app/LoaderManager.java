package android.app;

import android.content.Loader;
import android.os.Bundle;
import java.io.FileDescriptor;
import java.io.PrintWriter;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/LoaderManager.class */
public abstract class LoaderManager {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/LoaderManager$LoaderCallbacks.class */
    public interface LoaderCallbacks<D> {
        Loader<D> onCreateLoader(int i, Bundle bundle);

        void onLoadFinished(Loader<D> loader, D d);

        void onLoaderReset(Loader<D> loader);
    }

    public abstract <D> Loader<D> initLoader(int i, Bundle bundle, LoaderCallbacks<D> loaderCallbacks);

    public abstract <D> Loader<D> restartLoader(int i, Bundle bundle, LoaderCallbacks<D> loaderCallbacks);

    public abstract void destroyLoader(int i);

    public abstract <D> Loader<D> getLoader(int i);

    public abstract void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr);

    public LoaderManager() {
        throw new RuntimeException("Stub!");
    }

    public static void enableDebugLogging(boolean enabled) {
        throw new RuntimeException("Stub!");
    }
}
