package android.media.effect;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/effect/Effect.class */
public abstract class Effect {
    public abstract String getName();

    public abstract void apply(int i, int i2, int i3, int i4);

    public abstract void setParameter(String str, Object obj);

    public abstract void release();

    public Effect() {
        throw new RuntimeException("Stub!");
    }

    public void setUpdateListener(EffectUpdateListener listener) {
        throw new RuntimeException("Stub!");
    }
}
