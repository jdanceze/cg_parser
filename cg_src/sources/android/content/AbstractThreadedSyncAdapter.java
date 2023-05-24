package android.content;

import android.accounts.Account;
import android.os.Bundle;
import android.os.IBinder;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/AbstractThreadedSyncAdapter.class */
public abstract class AbstractThreadedSyncAdapter {
    @Deprecated
    public static final int LOG_SYNC_DETAILS = 2743;

    public abstract void onPerformSync(Account account, Bundle bundle, String str, ContentProviderClient contentProviderClient, SyncResult syncResult);

    public AbstractThreadedSyncAdapter(Context context, boolean autoInitialize) {
        throw new RuntimeException("Stub!");
    }

    public AbstractThreadedSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        throw new RuntimeException("Stub!");
    }

    public Context getContext() {
        throw new RuntimeException("Stub!");
    }

    public final IBinder getSyncAdapterBinder() {
        throw new RuntimeException("Stub!");
    }

    public void onSyncCanceled() {
        throw new RuntimeException("Stub!");
    }

    public void onSyncCanceled(Thread thread) {
        throw new RuntimeException("Stub!");
    }
}
