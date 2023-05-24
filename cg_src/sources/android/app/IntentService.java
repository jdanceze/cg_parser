package android.app;

import android.content.Intent;
import android.os.IBinder;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/IntentService.class */
public abstract class IntentService extends Service {
    protected abstract void onHandleIntent(Intent intent);

    public IntentService(String name) {
        throw new RuntimeException("Stub!");
    }

    public void setIntentRedelivery(boolean enabled) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Service
    public void onCreate() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Service
    public void onStart(Intent intent, int startId) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int flags, int startId) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Service
    public void onDestroy() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        throw new RuntimeException("Stub!");
    }
}
