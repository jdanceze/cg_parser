package android.media;

import android.hardware.Camera;
import android.view.Surface;
import java.io.FileDescriptor;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/MediaRecorder.class */
public class MediaRecorder {
    public static final int MEDIA_RECORDER_ERROR_UNKNOWN = 1;
    public static final int MEDIA_RECORDER_INFO_UNKNOWN = 1;
    public static final int MEDIA_RECORDER_INFO_MAX_DURATION_REACHED = 800;
    public static final int MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED = 801;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/MediaRecorder$OnErrorListener.class */
    public interface OnErrorListener {
        void onError(MediaRecorder mediaRecorder, int i, int i2);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/MediaRecorder$OnInfoListener.class */
    public interface OnInfoListener {
        void onInfo(MediaRecorder mediaRecorder, int i, int i2);
    }

    public native void setCamera(Camera camera);

    public native void setAudioSource(int i) throws IllegalStateException;

    public native void setVideoSource(int i) throws IllegalStateException;

    public native void setOutputFormat(int i) throws IllegalStateException;

    public native void setVideoSize(int i, int i2) throws IllegalStateException;

    public native void setVideoFrameRate(int i) throws IllegalStateException;

    public native void setMaxDuration(int i) throws IllegalArgumentException;

    public native void setMaxFileSize(long j) throws IllegalArgumentException;

    public native void setAudioEncoder(int i) throws IllegalStateException;

    public native void setVideoEncoder(int i) throws IllegalStateException;

    public native void start() throws IllegalStateException;

    public native void stop() throws IllegalStateException;

    public native int getMaxAmplitude() throws IllegalStateException;

    public native void release();

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/MediaRecorder$AudioSource.class */
    public final class AudioSource {
        public static final int DEFAULT = 0;
        public static final int MIC = 1;
        public static final int VOICE_UPLINK = 2;
        public static final int VOICE_DOWNLINK = 3;
        public static final int VOICE_CALL = 4;
        public static final int CAMCORDER = 5;
        public static final int VOICE_RECOGNITION = 6;
        public static final int VOICE_COMMUNICATION = 7;

        AudioSource() {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/MediaRecorder$VideoSource.class */
    public final class VideoSource {
        public static final int DEFAULT = 0;
        public static final int CAMERA = 1;

        VideoSource() {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/MediaRecorder$OutputFormat.class */
    public final class OutputFormat {
        public static final int DEFAULT = 0;
        public static final int THREE_GPP = 1;
        public static final int MPEG_4 = 2;
        @Deprecated
        public static final int RAW_AMR = 3;
        public static final int AMR_NB = 3;
        public static final int AMR_WB = 4;
        public static final int AAC_ADTS = 6;

        OutputFormat() {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/MediaRecorder$AudioEncoder.class */
    public final class AudioEncoder {
        public static final int DEFAULT = 0;
        public static final int AMR_NB = 1;
        public static final int AMR_WB = 2;
        public static final int AAC = 3;
        public static final int HE_AAC = 4;
        public static final int AAC_ELD = 5;

        AudioEncoder() {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/MediaRecorder$VideoEncoder.class */
    public final class VideoEncoder {
        public static final int DEFAULT = 0;
        public static final int H263 = 1;
        public static final int H264 = 2;
        public static final int MPEG_4_SP = 3;

        VideoEncoder() {
            throw new RuntimeException("Stub!");
        }
    }

    public MediaRecorder() {
        throw new RuntimeException("Stub!");
    }

    public void setPreviewDisplay(Surface sv) {
        throw new RuntimeException("Stub!");
    }

    public static final int getAudioSourceMax() {
        throw new RuntimeException("Stub!");
    }

    public void setProfile(CamcorderProfile profile) {
        throw new RuntimeException("Stub!");
    }

    public void setCaptureRate(double fps) {
        throw new RuntimeException("Stub!");
    }

    public void setOrientationHint(int degrees) {
        throw new RuntimeException("Stub!");
    }

    public void setLocation(float latitude, float longitude) {
        throw new RuntimeException("Stub!");
    }

    public void setAudioSamplingRate(int samplingRate) {
        throw new RuntimeException("Stub!");
    }

    public void setAudioChannels(int numChannels) {
        throw new RuntimeException("Stub!");
    }

    public void setAudioEncodingBitRate(int bitRate) {
        throw new RuntimeException("Stub!");
    }

    public void setVideoEncodingBitRate(int bitRate) {
        throw new RuntimeException("Stub!");
    }

    public void setOutputFile(FileDescriptor fd) throws IllegalStateException {
        throw new RuntimeException("Stub!");
    }

    public void setOutputFile(String path) throws IllegalStateException {
        throw new RuntimeException("Stub!");
    }

    public void prepare() throws IllegalStateException, IOException {
        throw new RuntimeException("Stub!");
    }

    public void reset() {
        throw new RuntimeException("Stub!");
    }

    public void setOnErrorListener(OnErrorListener l) {
        throw new RuntimeException("Stub!");
    }

    public void setOnInfoListener(OnInfoListener listener) {
        throw new RuntimeException("Stub!");
    }

    protected void finalize() {
        throw new RuntimeException("Stub!");
    }
}
