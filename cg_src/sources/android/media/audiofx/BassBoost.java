package android.media.audiofx;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/audiofx/BassBoost.class */
public class BassBoost extends AudioEffect {
    public static final int PARAM_STRENGTH_SUPPORTED = 0;
    public static final int PARAM_STRENGTH = 1;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/audiofx/BassBoost$OnParameterChangeListener.class */
    public interface OnParameterChangeListener {
        void onParameterChange(BassBoost bassBoost, int i, int i2, short s);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/audiofx/BassBoost$Settings.class */
    public static class Settings {
        public short strength;

        public Settings() {
            throw new RuntimeException("Stub!");
        }

        public Settings(String settings) {
            throw new RuntimeException("Stub!");
        }

        public String toString() {
            throw new RuntimeException("Stub!");
        }
    }

    public BassBoost(int priority, int audioSession) throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException, RuntimeException {
        throw new RuntimeException("Stub!");
    }

    public boolean getStrengthSupported() {
        throw new RuntimeException("Stub!");
    }

    public void setStrength(short strength) throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        throw new RuntimeException("Stub!");
    }

    public short getRoundedStrength() throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        throw new RuntimeException("Stub!");
    }

    public void setParameterListener(OnParameterChangeListener listener) {
        throw new RuntimeException("Stub!");
    }

    public Settings getProperties() throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        throw new RuntimeException("Stub!");
    }

    public void setProperties(Settings settings) throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        throw new RuntimeException("Stub!");
    }
}
