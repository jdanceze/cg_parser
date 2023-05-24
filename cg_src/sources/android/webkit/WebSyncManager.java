package android.webkit;

import android.content.Context;
import android.os.Handler;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/webkit/WebSyncManager.class */
abstract class WebSyncManager implements Runnable {
    protected Handler mHandler;
    protected WebViewDatabase mDataBase;
    protected static final String LOGTAG = "websync";

    /* JADX INFO: Access modifiers changed from: protected */
    public WebSyncManager(Context context, String name) {
        throw new RuntimeException("Stub!");
    }

    protected Object clone() throws CloneNotSupportedException {
        throw new RuntimeException("Stub!");
    }

    @Override // java.lang.Runnable
    public void run() {
        throw new RuntimeException("Stub!");
    }

    public void sync() {
        throw new RuntimeException("Stub!");
    }

    public void resetSync() {
        throw new RuntimeException("Stub!");
    }

    public void startSync() {
        throw new RuntimeException("Stub!");
    }

    public void stopSync() {
        throw new RuntimeException("Stub!");
    }

    protected void onSyncInit() {
        throw new RuntimeException("Stub!");
    }
}
