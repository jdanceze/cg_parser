package android.os.storage;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/os/storage/OnObbStateChangeListener.class */
public abstract class OnObbStateChangeListener {
    public static final int MOUNTED = 1;
    public static final int UNMOUNTED = 2;
    public static final int ERROR_INTERNAL = 20;
    public static final int ERROR_COULD_NOT_MOUNT = 21;
    public static final int ERROR_COULD_NOT_UNMOUNT = 22;
    public static final int ERROR_NOT_MOUNTED = 23;
    public static final int ERROR_ALREADY_MOUNTED = 24;
    public static final int ERROR_PERMISSION_DENIED = 25;

    public OnObbStateChangeListener() {
        throw new RuntimeException("Stub!");
    }

    public void onObbStateChange(String path, int state) {
        throw new RuntimeException("Stub!");
    }
}
