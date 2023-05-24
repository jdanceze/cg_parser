package android.app;

import android.content.ComponentCallbacks;
import android.content.ComponentCallbacks2;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.os.Bundle;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/Application.class */
public class Application extends ContextWrapper implements ComponentCallbacks2 {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/Application$ActivityLifecycleCallbacks.class */
    public interface ActivityLifecycleCallbacks {
        void onActivityCreated(Activity activity, Bundle bundle);

        void onActivityStarted(Activity activity);

        void onActivityResumed(Activity activity);

        void onActivityPaused(Activity activity);

        void onActivityStopped(Activity activity);

        void onActivitySaveInstanceState(Activity activity, Bundle bundle);

        void onActivityDestroyed(Activity activity);
    }

    public Application() {
        super(null);
        throw new RuntimeException("Stub!");
    }

    public void onCreate() {
        throw new RuntimeException("Stub!");
    }

    public void onTerminate() {
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

    @Override // android.content.Context
    public void registerComponentCallbacks(ComponentCallbacks callback) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public void unregisterComponentCallbacks(ComponentCallbacks callback) {
        throw new RuntimeException("Stub!");
    }

    public void registerActivityLifecycleCallbacks(ActivityLifecycleCallbacks callback) {
        throw new RuntimeException("Stub!");
    }

    public void unregisterActivityLifecycleCallbacks(ActivityLifecycleCallbacks callback) {
        throw new RuntimeException("Stub!");
    }
}
