package android.view;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/Choreographer.class */
public final class Choreographer {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/Choreographer$FrameCallback.class */
    public interface FrameCallback {
        void doFrame(long j);
    }

    Choreographer() {
        throw new RuntimeException("Stub!");
    }

    public static Choreographer getInstance() {
        throw new RuntimeException("Stub!");
    }

    public void postFrameCallback(FrameCallback callback) {
        throw new RuntimeException("Stub!");
    }

    public void postFrameCallbackDelayed(FrameCallback callback, long delayMillis) {
        throw new RuntimeException("Stub!");
    }

    public void removeFrameCallback(FrameCallback callback) {
        throw new RuntimeException("Stub!");
    }
}
