package android.media.audiofx;

import java.util.UUID;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/audiofx/AudioEffect.class */
public class AudioEffect {
    public static final int SUCCESS = 0;
    public static final int ERROR = -1;
    public static final int ALREADY_EXISTS = -2;
    public static final int ERROR_NO_INIT = -3;
    public static final int ERROR_BAD_VALUE = -4;
    public static final int ERROR_INVALID_OPERATION = -5;
    public static final int ERROR_NO_MEMORY = -6;
    public static final int ERROR_DEAD_OBJECT = -7;
    public static final String EFFECT_INSERT = "Insert";
    public static final String EFFECT_AUXILIARY = "Auxiliary";
    public static final String ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL = "android.media.action.DISPLAY_AUDIO_EFFECT_CONTROL_PANEL";
    public static final String ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION = "android.media.action.OPEN_AUDIO_EFFECT_CONTROL_SESSION";
    public static final String ACTION_CLOSE_AUDIO_EFFECT_CONTROL_SESSION = "android.media.action.CLOSE_AUDIO_EFFECT_CONTROL_SESSION";
    public static final String EXTRA_AUDIO_SESSION = "android.media.extra.AUDIO_SESSION";
    public static final String EXTRA_PACKAGE_NAME = "android.media.extra.PACKAGE_NAME";
    public static final String EXTRA_CONTENT_TYPE = "android.media.extra.CONTENT_TYPE";
    public static final int CONTENT_TYPE_MUSIC = 0;
    public static final int CONTENT_TYPE_MOVIE = 1;
    public static final int CONTENT_TYPE_GAME = 2;
    public static final int CONTENT_TYPE_VOICE = 3;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/audiofx/AudioEffect$OnControlStatusChangeListener.class */
    public interface OnControlStatusChangeListener {
        void onControlStatusChange(AudioEffect audioEffect, boolean z);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/audiofx/AudioEffect$OnEnableStatusChangeListener.class */
    public interface OnEnableStatusChangeListener {
        void onEnableStatusChange(AudioEffect audioEffect, boolean z);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/audiofx/AudioEffect$Descriptor.class */
    public static class Descriptor {
        public UUID type;
        public UUID uuid;
        public String connectMode;
        public String name;
        public String implementor;

        public Descriptor() {
            throw new RuntimeException("Stub!");
        }

        public Descriptor(String type, String uuid, String connectMode, String name, String implementor) {
            throw new RuntimeException("Stub!");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AudioEffect() {
        throw new RuntimeException("Stub!");
    }

    public void release() {
        throw new RuntimeException("Stub!");
    }

    protected void finalize() {
        throw new RuntimeException("Stub!");
    }

    public Descriptor getDescriptor() throws IllegalStateException {
        throw new RuntimeException("Stub!");
    }

    public static Descriptor[] queryEffects() {
        throw new RuntimeException("Stub!");
    }

    public int setEnabled(boolean enabled) throws IllegalStateException {
        throw new RuntimeException("Stub!");
    }

    public int getId() throws IllegalStateException {
        throw new RuntimeException("Stub!");
    }

    public boolean getEnabled() throws IllegalStateException {
        throw new RuntimeException("Stub!");
    }

    public boolean hasControl() throws IllegalStateException {
        throw new RuntimeException("Stub!");
    }

    public void setEnableStatusListener(OnEnableStatusChangeListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void setControlStatusListener(OnControlStatusChangeListener listener) {
        throw new RuntimeException("Stub!");
    }
}
