package android.app;

import android.content.ComponentCallbacks2;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;
import java.io.FileDescriptor;
import java.io.PrintWriter;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/Service.class */
public abstract class Service extends ContextWrapper implements ComponentCallbacks2 {
    public static final int START_CONTINUATION_MASK = 15;
    public static final int START_STICKY_COMPATIBILITY = 0;
    public static final int START_STICKY = 1;
    public static final int START_NOT_STICKY = 2;
    public static final int START_REDELIVER_INTENT = 3;
    public static final int START_FLAG_REDELIVERY = 1;
    public static final int START_FLAG_RETRY = 2;

    public abstract IBinder onBind(Intent intent);

    public Service() {
        super(null);
        throw new RuntimeException("Stub!");
    }

    public final Application getApplication() {
        throw new RuntimeException("Stub!");
    }

    public void onCreate() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void onStart(Intent intent, int startId) {
        throw new RuntimeException("Stub!");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        throw new RuntimeException("Stub!");
    }

    public void onDestroy() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.ComponentCallbacks
    public void onLowMemory() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.ComponentCallbacks2
    public void onTrimMemory(int level) {
        throw new RuntimeException("Stub!");
    }

    public boolean onUnbind(Intent intent) {
        throw new RuntimeException("Stub!");
    }

    public void onRebind(Intent intent) {
        throw new RuntimeException("Stub!");
    }

    public void onTaskRemoved(Intent rootIntent) {
        throw new RuntimeException("Stub!");
    }

    public final void stopSelf() {
        throw new RuntimeException("Stub!");
    }

    public final void stopSelf(int startId) {
        throw new RuntimeException("Stub!");
    }

    public final boolean stopSelfResult(int startId) {
        throw new RuntimeException("Stub!");
    }

    public final void startForeground(int id, Notification notification) {
        throw new RuntimeException("Stub!");
    }

    public final void stopForeground(boolean removeNotification) {
        throw new RuntimeException("Stub!");
    }

    protected void dump(FileDescriptor fd, PrintWriter writer, String[] args) {
        throw new RuntimeException("Stub!");
    }
}
