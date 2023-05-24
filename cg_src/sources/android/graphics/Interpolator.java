package android.graphics;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/graphics/Interpolator.class */
public class Interpolator {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/graphics/Interpolator$Result.class */
    public enum Result {
        FREEZE_END,
        FREEZE_START,
        NORMAL
    }

    public Interpolator(int valueCount) {
        throw new RuntimeException("Stub!");
    }

    public Interpolator(int valueCount, int frameCount) {
        throw new RuntimeException("Stub!");
    }

    public void reset(int valueCount) {
        throw new RuntimeException("Stub!");
    }

    public void reset(int valueCount, int frameCount) {
        throw new RuntimeException("Stub!");
    }

    public final int getKeyFrameCount() {
        throw new RuntimeException("Stub!");
    }

    public final int getValueCount() {
        throw new RuntimeException("Stub!");
    }

    public void setKeyFrame(int index, int msec, float[] values) {
        throw new RuntimeException("Stub!");
    }

    public void setKeyFrame(int index, int msec, float[] values, float[] blend) {
        throw new RuntimeException("Stub!");
    }

    public void setRepeatMirror(float repeatCount, boolean mirror) {
        throw new RuntimeException("Stub!");
    }

    public Result timeToValues(float[] values) {
        throw new RuntimeException("Stub!");
    }

    public Result timeToValues(int msec, float[] values) {
        throw new RuntimeException("Stub!");
    }

    protected void finalize() throws Throwable {
        throw new RuntimeException("Stub!");
    }
}
