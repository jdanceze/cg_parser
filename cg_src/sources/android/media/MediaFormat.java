package android.media;

import java.nio.ByteBuffer;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/MediaFormat.class */
public final class MediaFormat {
    public static final String KEY_MIME = "mime";
    public static final String KEY_SAMPLE_RATE = "sample-rate";
    public static final String KEY_CHANNEL_COUNT = "channel-count";
    public static final String KEY_WIDTH = "width";
    public static final String KEY_HEIGHT = "height";
    public static final String KEY_MAX_INPUT_SIZE = "max-input-size";
    public static final String KEY_BIT_RATE = "bitrate";
    public static final String KEY_COLOR_FORMAT = "color-format";
    public static final String KEY_FRAME_RATE = "frame-rate";
    public static final String KEY_I_FRAME_INTERVAL = "i-frame-interval";
    public static final String KEY_DURATION = "durationUs";
    public static final String KEY_IS_ADTS = "is-adts";
    public static final String KEY_CHANNEL_MASK = "channel-mask";
    public static final String KEY_AAC_PROFILE = "aac-profile";
    public static final String KEY_FLAC_COMPRESSION_LEVEL = "flac-compression-level";

    public MediaFormat() {
        throw new RuntimeException("Stub!");
    }

    public final boolean containsKey(String name) {
        throw new RuntimeException("Stub!");
    }

    public final int getInteger(String name) {
        throw new RuntimeException("Stub!");
    }

    public final long getLong(String name) {
        throw new RuntimeException("Stub!");
    }

    public final float getFloat(String name) {
        throw new RuntimeException("Stub!");
    }

    public final String getString(String name) {
        throw new RuntimeException("Stub!");
    }

    public final ByteBuffer getByteBuffer(String name) {
        throw new RuntimeException("Stub!");
    }

    public final void setInteger(String name, int value) {
        throw new RuntimeException("Stub!");
    }

    public final void setLong(String name, long value) {
        throw new RuntimeException("Stub!");
    }

    public final void setFloat(String name, float value) {
        throw new RuntimeException("Stub!");
    }

    public final void setString(String name, String value) {
        throw new RuntimeException("Stub!");
    }

    public final void setByteBuffer(String name, ByteBuffer bytes) {
        throw new RuntimeException("Stub!");
    }

    public static final MediaFormat createAudioFormat(String mime, int sampleRate, int channelCount) {
        throw new RuntimeException("Stub!");
    }

    public static final MediaFormat createVideoFormat(String mime, int width, int height) {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
        throw new RuntimeException("Stub!");
    }
}
