package android.content;

import android.os.IBinder;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/ServiceConnection.class */
public interface ServiceConnection {
    void onServiceConnected(ComponentName componentName, IBinder iBinder);

    void onServiceDisconnected(ComponentName componentName);
}
