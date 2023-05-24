package android.os;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/os/PowerManager.class */
public class PowerManager {
    public static final int PARTIAL_WAKE_LOCK = 1;
    public static final int FULL_WAKE_LOCK = 26;
    @Deprecated
    public static final int SCREEN_BRIGHT_WAKE_LOCK = 10;
    public static final int SCREEN_DIM_WAKE_LOCK = 6;
    public static final int ACQUIRE_CAUSES_WAKEUP = 268435456;
    public static final int ON_AFTER_RELEASE = 536870912;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/os/PowerManager$WakeLock.class */
    public class WakeLock {
        WakeLock() {
            throw new RuntimeException("Stub!");
        }

        public void setReferenceCounted(boolean value) {
            throw new RuntimeException("Stub!");
        }

        public void acquire() {
            throw new RuntimeException("Stub!");
        }

        public void acquire(long timeout) {
            throw new RuntimeException("Stub!");
        }

        public void release() {
            throw new RuntimeException("Stub!");
        }

        public boolean isHeld() {
            throw new RuntimeException("Stub!");
        }

        public void setWorkSource(WorkSource ws) {
            throw new RuntimeException("Stub!");
        }

        public String toString() {
            throw new RuntimeException("Stub!");
        }

        protected void finalize() throws Throwable {
            throw new RuntimeException("Stub!");
        }
    }

    PowerManager() {
        throw new RuntimeException("Stub!");
    }

    public WakeLock newWakeLock(int flags, String tag) {
        throw new RuntimeException("Stub!");
    }

    public void userActivity(long when, boolean noChangeLights) {
        throw new RuntimeException("Stub!");
    }

    public void goToSleep(long time) {
        throw new RuntimeException("Stub!");
    }

    public boolean isScreenOn() {
        throw new RuntimeException("Stub!");
    }

    public void reboot(String reason) {
        throw new RuntimeException("Stub!");
    }
}
