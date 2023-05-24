package android.os;

import java.io.PrintWriter;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/os/TokenWatcher.class */
public abstract class TokenWatcher {
    public abstract void acquired();

    public abstract void released();

    public TokenWatcher(Handler h, String tag) {
        throw new RuntimeException("Stub!");
    }

    public void acquire(IBinder token, String tag) {
        throw new RuntimeException("Stub!");
    }

    public void cleanup(IBinder token, boolean unlink) {
        throw new RuntimeException("Stub!");
    }

    public void release(IBinder token) {
        throw new RuntimeException("Stub!");
    }

    public boolean isAcquired() {
        throw new RuntimeException("Stub!");
    }

    public void dump() {
        throw new RuntimeException("Stub!");
    }

    public void dump(PrintWriter pw) {
        throw new RuntimeException("Stub!");
    }
}
