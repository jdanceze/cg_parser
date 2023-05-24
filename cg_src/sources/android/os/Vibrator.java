package android.os;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/os/Vibrator.class */
public abstract class Vibrator {
    public abstract boolean hasVibrator();

    public abstract void vibrate(long j);

    public abstract void vibrate(long[] jArr, int i);

    public abstract void cancel();

    Vibrator() {
        throw new RuntimeException("Stub!");
    }
}
