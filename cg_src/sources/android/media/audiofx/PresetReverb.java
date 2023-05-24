package android.media.audiofx;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/audiofx/PresetReverb.class */
public class PresetReverb extends AudioEffect {
    public static final int PARAM_PRESET = 0;
    public static final short PRESET_NONE = 0;
    public static final short PRESET_SMALLROOM = 1;
    public static final short PRESET_MEDIUMROOM = 2;
    public static final short PRESET_LARGEROOM = 3;
    public static final short PRESET_MEDIUMHALL = 4;
    public static final short PRESET_LARGEHALL = 5;
    public static final short PRESET_PLATE = 6;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/audiofx/PresetReverb$OnParameterChangeListener.class */
    public interface OnParameterChangeListener {
        void onParameterChange(PresetReverb presetReverb, int i, int i2, short s);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/audiofx/PresetReverb$Settings.class */
    public static class Settings {
        public short preset;

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

    public PresetReverb(int priority, int audioSession) throws IllegalArgumentException, UnsupportedOperationException, RuntimeException {
        throw new RuntimeException("Stub!");
    }

    public void setPreset(short preset) throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        throw new RuntimeException("Stub!");
    }

    public short getPreset() throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
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
