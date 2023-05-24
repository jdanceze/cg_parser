package android.os;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/os/SystemClock.class */
public final class SystemClock {
    public static native boolean setCurrentTimeMillis(long j);

    public static native long uptimeMillis();

    public static native long elapsedRealtime();

    public static native long currentThreadTimeMillis();

    SystemClock() {
        throw new RuntimeException("Stub!");
    }

    public static void sleep(long ms) {
        throw new RuntimeException("Stub!");
    }
}
