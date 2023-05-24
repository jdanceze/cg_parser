package android.animation;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/animation/Keyframe.class */
public abstract class Keyframe implements Cloneable {
    public abstract Object getValue();

    public abstract void setValue(Object obj);

    /* renamed from: clone */
    public abstract Keyframe m5clone();

    public Keyframe() {
        throw new RuntimeException("Stub!");
    }

    public static Keyframe ofInt(float fraction, int value) {
        throw new RuntimeException("Stub!");
    }

    public static Keyframe ofInt(float fraction) {
        throw new RuntimeException("Stub!");
    }

    public static Keyframe ofFloat(float fraction, float value) {
        throw new RuntimeException("Stub!");
    }

    public static Keyframe ofFloat(float fraction) {
        throw new RuntimeException("Stub!");
    }

    public static Keyframe ofObject(float fraction, Object value) {
        throw new RuntimeException("Stub!");
    }

    public static Keyframe ofObject(float fraction) {
        throw new RuntimeException("Stub!");
    }

    public boolean hasValue() {
        throw new RuntimeException("Stub!");
    }

    public float getFraction() {
        throw new RuntimeException("Stub!");
    }

    public void setFraction(float fraction) {
        throw new RuntimeException("Stub!");
    }

    public TimeInterpolator getInterpolator() {
        throw new RuntimeException("Stub!");
    }

    public void setInterpolator(TimeInterpolator interpolator) {
        throw new RuntimeException("Stub!");
    }

    public Class getType() {
        throw new RuntimeException("Stub!");
    }
}
