package android.webkit;

import java.util.Set;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/webkit/GeolocationPermissions.class */
public class GeolocationPermissions {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/webkit/GeolocationPermissions$Callback.class */
    public interface Callback {
        void invoke(String str, boolean z, boolean z2);
    }

    GeolocationPermissions() {
        throw new RuntimeException("Stub!");
    }

    public static GeolocationPermissions getInstance() {
        throw new RuntimeException("Stub!");
    }

    public void getOrigins(ValueCallback<Set<String>> callback) {
        throw new RuntimeException("Stub!");
    }

    public void getAllowed(String origin, ValueCallback<Boolean> callback) {
        throw new RuntimeException("Stub!");
    }

    public void clear(String origin) {
        throw new RuntimeException("Stub!");
    }

    public void allow(String origin) {
        throw new RuntimeException("Stub!");
    }

    public void clearAll() {
        throw new RuntimeException("Stub!");
    }
}
