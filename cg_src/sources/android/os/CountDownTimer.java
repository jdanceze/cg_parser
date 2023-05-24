package android.os;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/os/CountDownTimer.class */
public abstract class CountDownTimer {
    public abstract void onTick(long j);

    public abstract void onFinish();

    public CountDownTimer(long millisInFuture, long countDownInterval) {
        throw new RuntimeException("Stub!");
    }

    public final void cancel() {
        throw new RuntimeException("Stub!");
    }

    public final synchronized CountDownTimer start() {
        throw new RuntimeException("Stub!");
    }
}
