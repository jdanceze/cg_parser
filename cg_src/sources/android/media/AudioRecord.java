package android.media;

import android.os.Handler;
import java.nio.ByteBuffer;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/AudioRecord.class */
public class AudioRecord {
    public static final int STATE_UNINITIALIZED = 0;
    public static final int STATE_INITIALIZED = 1;
    public static final int RECORDSTATE_STOPPED = 1;
    public static final int RECORDSTATE_RECORDING = 3;
    public static final int SUCCESS = 0;
    public static final int ERROR = -1;
    public static final int ERROR_BAD_VALUE = -2;
    public static final int ERROR_INVALID_OPERATION = -3;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/AudioRecord$OnRecordPositionUpdateListener.class */
    public interface OnRecordPositionUpdateListener {
        void onMarkerReached(AudioRecord audioRecord);

        void onPeriodicNotification(AudioRecord audioRecord);
    }

    public AudioRecord(int audioSource, int sampleRateInHz, int channelConfig, int audioFormat, int bufferSizeInBytes) throws IllegalArgumentException {
        throw new RuntimeException("Stub!");
    }

    public void release() {
        throw new RuntimeException("Stub!");
    }

    protected void finalize() {
        throw new RuntimeException("Stub!");
    }

    public int getSampleRate() {
        throw new RuntimeException("Stub!");
    }

    public int getAudioSource() {
        throw new RuntimeException("Stub!");
    }

    public int getAudioFormat() {
        throw new RuntimeException("Stub!");
    }

    public int getChannelConfiguration() {
        throw new RuntimeException("Stub!");
    }

    public int getChannelCount() {
        throw new RuntimeException("Stub!");
    }

    public int getState() {
        throw new RuntimeException("Stub!");
    }

    public int getRecordingState() {
        throw new RuntimeException("Stub!");
    }

    public int getNotificationMarkerPosition() {
        throw new RuntimeException("Stub!");
    }

    public int getPositionNotificationPeriod() {
        throw new RuntimeException("Stub!");
    }

    public static int getMinBufferSize(int sampleRateInHz, int channelConfig, int audioFormat) {
        throw new RuntimeException("Stub!");
    }

    public int getAudioSessionId() {
        throw new RuntimeException("Stub!");
    }

    public void startRecording() throws IllegalStateException {
        throw new RuntimeException("Stub!");
    }

    public void startRecording(MediaSyncEvent syncEvent) throws IllegalStateException {
        throw new RuntimeException("Stub!");
    }

    public void stop() throws IllegalStateException {
        throw new RuntimeException("Stub!");
    }

    public int read(byte[] audioData, int offsetInBytes, int sizeInBytes) {
        throw new RuntimeException("Stub!");
    }

    public int read(short[] audioData, int offsetInShorts, int sizeInShorts) {
        throw new RuntimeException("Stub!");
    }

    public int read(ByteBuffer audioBuffer, int sizeInBytes) {
        throw new RuntimeException("Stub!");
    }

    public void setRecordPositionUpdateListener(OnRecordPositionUpdateListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void setRecordPositionUpdateListener(OnRecordPositionUpdateListener listener, Handler handler) {
        throw new RuntimeException("Stub!");
    }

    public int setNotificationMarkerPosition(int markerInFrames) {
        throw new RuntimeException("Stub!");
    }

    public int setPositionNotificationPeriod(int periodInFrames) {
        throw new RuntimeException("Stub!");
    }
}
