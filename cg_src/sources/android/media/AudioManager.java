package android.media;

import android.content.ComponentName;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/AudioManager.class */
public class AudioManager {
    public static final String ACTION_AUDIO_BECOMING_NOISY = "android.media.AUDIO_BECOMING_NOISY";
    public static final String RINGER_MODE_CHANGED_ACTION = "android.media.RINGER_MODE_CHANGED";
    public static final String EXTRA_RINGER_MODE = "android.media.EXTRA_RINGER_MODE";
    @Deprecated
    public static final String VIBRATE_SETTING_CHANGED_ACTION = "android.media.VIBRATE_SETTING_CHANGED";
    @Deprecated
    public static final String EXTRA_VIBRATE_SETTING = "android.media.EXTRA_VIBRATE_SETTING";
    @Deprecated
    public static final String EXTRA_VIBRATE_TYPE = "android.media.EXTRA_VIBRATE_TYPE";
    public static final int STREAM_VOICE_CALL = 0;
    public static final int STREAM_SYSTEM = 1;
    public static final int STREAM_RING = 2;
    public static final int STREAM_MUSIC = 3;
    public static final int STREAM_ALARM = 4;
    public static final int STREAM_NOTIFICATION = 5;
    public static final int STREAM_DTMF = 8;
    @Deprecated
    public static final int NUM_STREAMS = 5;
    public static final int ADJUST_RAISE = 1;
    public static final int ADJUST_LOWER = -1;
    public static final int ADJUST_SAME = 0;
    public static final int FLAG_SHOW_UI = 1;
    public static final int FLAG_ALLOW_RINGER_MODES = 2;
    public static final int FLAG_PLAY_SOUND = 4;
    public static final int FLAG_REMOVE_SOUND_AND_VIBRATE = 8;
    public static final int FLAG_VIBRATE = 16;
    public static final int RINGER_MODE_SILENT = 0;
    public static final int RINGER_MODE_VIBRATE = 1;
    public static final int RINGER_MODE_NORMAL = 2;
    @Deprecated
    public static final int VIBRATE_TYPE_RINGER = 0;
    @Deprecated
    public static final int VIBRATE_TYPE_NOTIFICATION = 1;
    @Deprecated
    public static final int VIBRATE_SETTING_OFF = 0;
    @Deprecated
    public static final int VIBRATE_SETTING_ON = 1;
    @Deprecated
    public static final int VIBRATE_SETTING_ONLY_SILENT = 2;
    public static final int USE_DEFAULT_STREAM_TYPE = Integer.MIN_VALUE;
    @Deprecated
    public static final String ACTION_SCO_AUDIO_STATE_CHANGED = "android.media.SCO_AUDIO_STATE_CHANGED";
    public static final String ACTION_SCO_AUDIO_STATE_UPDATED = "android.media.ACTION_SCO_AUDIO_STATE_UPDATED";
    public static final String EXTRA_SCO_AUDIO_STATE = "android.media.extra.SCO_AUDIO_STATE";
    public static final String EXTRA_SCO_AUDIO_PREVIOUS_STATE = "android.media.extra.SCO_AUDIO_PREVIOUS_STATE";
    public static final int SCO_AUDIO_STATE_DISCONNECTED = 0;
    public static final int SCO_AUDIO_STATE_CONNECTED = 1;
    public static final int SCO_AUDIO_STATE_CONNECTING = 2;
    public static final int SCO_AUDIO_STATE_ERROR = -1;
    public static final int MODE_INVALID = -2;
    public static final int MODE_CURRENT = -1;
    public static final int MODE_NORMAL = 0;
    public static final int MODE_RINGTONE = 1;
    public static final int MODE_IN_CALL = 2;
    public static final int MODE_IN_COMMUNICATION = 3;
    @Deprecated
    public static final int ROUTE_EARPIECE = 1;
    @Deprecated
    public static final int ROUTE_SPEAKER = 2;
    @Deprecated
    public static final int ROUTE_BLUETOOTH = 4;
    @Deprecated
    public static final int ROUTE_BLUETOOTH_SCO = 4;
    @Deprecated
    public static final int ROUTE_HEADSET = 8;
    @Deprecated
    public static final int ROUTE_BLUETOOTH_A2DP = 16;
    @Deprecated
    public static final int ROUTE_ALL = -1;
    public static final int FX_KEY_CLICK = 0;
    public static final int FX_FOCUS_NAVIGATION_UP = 1;
    public static final int FX_FOCUS_NAVIGATION_DOWN = 2;
    public static final int FX_FOCUS_NAVIGATION_LEFT = 3;
    public static final int FX_FOCUS_NAVIGATION_RIGHT = 4;
    public static final int FX_KEYPRESS_STANDARD = 5;
    public static final int FX_KEYPRESS_SPACEBAR = 6;
    public static final int FX_KEYPRESS_DELETE = 7;
    public static final int FX_KEYPRESS_RETURN = 8;
    public static final int AUDIOFOCUS_GAIN = 1;
    public static final int AUDIOFOCUS_GAIN_TRANSIENT = 2;
    public static final int AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK = 3;
    public static final int AUDIOFOCUS_LOSS = -1;
    public static final int AUDIOFOCUS_LOSS_TRANSIENT = -2;
    public static final int AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK = -3;
    public static final int AUDIOFOCUS_REQUEST_FAILED = 0;
    public static final int AUDIOFOCUS_REQUEST_GRANTED = 1;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/AudioManager$OnAudioFocusChangeListener.class */
    public interface OnAudioFocusChangeListener {
        void onAudioFocusChange(int i);
    }

    AudioManager() {
        throw new RuntimeException("Stub!");
    }

    public void adjustStreamVolume(int streamType, int direction, int flags) {
        throw new RuntimeException("Stub!");
    }

    public void adjustVolume(int direction, int flags) {
        throw new RuntimeException("Stub!");
    }

    public void adjustSuggestedStreamVolume(int direction, int suggestedStreamType, int flags) {
        throw new RuntimeException("Stub!");
    }

    public int getRingerMode() {
        throw new RuntimeException("Stub!");
    }

    public int getStreamMaxVolume(int streamType) {
        throw new RuntimeException("Stub!");
    }

    public int getStreamVolume(int streamType) {
        throw new RuntimeException("Stub!");
    }

    public void setRingerMode(int ringerMode) {
        throw new RuntimeException("Stub!");
    }

    public void setStreamVolume(int streamType, int index, int flags) {
        throw new RuntimeException("Stub!");
    }

    public void setStreamSolo(int streamType, boolean state) {
        throw new RuntimeException("Stub!");
    }

    public void setStreamMute(int streamType, boolean state) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public boolean shouldVibrate(int vibrateType) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public int getVibrateSetting(int vibrateType) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void setVibrateSetting(int vibrateType, int vibrateSetting) {
        throw new RuntimeException("Stub!");
    }

    public void setSpeakerphoneOn(boolean on) {
        throw new RuntimeException("Stub!");
    }

    public boolean isSpeakerphoneOn() {
        throw new RuntimeException("Stub!");
    }

    public boolean isBluetoothScoAvailableOffCall() {
        throw new RuntimeException("Stub!");
    }

    public void startBluetoothSco() {
        throw new RuntimeException("Stub!");
    }

    public void stopBluetoothSco() {
        throw new RuntimeException("Stub!");
    }

    public void setBluetoothScoOn(boolean on) {
        throw new RuntimeException("Stub!");
    }

    public boolean isBluetoothScoOn() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void setBluetoothA2dpOn(boolean on) {
        throw new RuntimeException("Stub!");
    }

    public boolean isBluetoothA2dpOn() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void setWiredHeadsetOn(boolean on) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public boolean isWiredHeadsetOn() {
        throw new RuntimeException("Stub!");
    }

    public void setMicrophoneMute(boolean on) {
        throw new RuntimeException("Stub!");
    }

    public boolean isMicrophoneMute() {
        throw new RuntimeException("Stub!");
    }

    public void setMode(int mode) {
        throw new RuntimeException("Stub!");
    }

    public int getMode() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void setRouting(int mode, int routes, int mask) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public int getRouting(int mode) {
        throw new RuntimeException("Stub!");
    }

    public boolean isMusicActive() {
        throw new RuntimeException("Stub!");
    }

    public void setParameters(String keyValuePairs) {
        throw new RuntimeException("Stub!");
    }

    public String getParameters(String keys) {
        throw new RuntimeException("Stub!");
    }

    public void playSoundEffect(int effectType) {
        throw new RuntimeException("Stub!");
    }

    public void playSoundEffect(int effectType, float volume) {
        throw new RuntimeException("Stub!");
    }

    public void loadSoundEffects() {
        throw new RuntimeException("Stub!");
    }

    public void unloadSoundEffects() {
        throw new RuntimeException("Stub!");
    }

    public int requestAudioFocus(OnAudioFocusChangeListener l, int streamType, int durationHint) {
        throw new RuntimeException("Stub!");
    }

    public int abandonAudioFocus(OnAudioFocusChangeListener l) {
        throw new RuntimeException("Stub!");
    }

    public void registerMediaButtonEventReceiver(ComponentName eventReceiver) {
        throw new RuntimeException("Stub!");
    }

    public void unregisterMediaButtonEventReceiver(ComponentName eventReceiver) {
        throw new RuntimeException("Stub!");
    }

    public void registerRemoteControlClient(RemoteControlClient rcClient) {
        throw new RuntimeException("Stub!");
    }

    public void unregisterRemoteControlClient(RemoteControlClient rcClient) {
        throw new RuntimeException("Stub!");
    }
}
