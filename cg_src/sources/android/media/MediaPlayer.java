package android.media;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.Surface;
import android.view.SurfaceHolder;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/MediaPlayer.class */
public class MediaPlayer {
    public static final int VIDEO_SCALING_MODE_SCALE_TO_FIT = 1;
    public static final int VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING = 2;
    public static final String MEDIA_MIMETYPE_TEXT_SUBRIP = "application/x-subrip";
    public static final int MEDIA_ERROR_UNKNOWN = 1;
    public static final int MEDIA_ERROR_SERVER_DIED = 100;
    public static final int MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK = 200;
    public static final int MEDIA_INFO_UNKNOWN = 1;
    public static final int MEDIA_INFO_VIDEO_TRACK_LAGGING = 700;
    public static final int MEDIA_INFO_BUFFERING_START = 701;
    public static final int MEDIA_INFO_BUFFERING_END = 702;
    public static final int MEDIA_INFO_BAD_INTERLEAVING = 800;
    public static final int MEDIA_INFO_NOT_SEEKABLE = 801;
    public static final int MEDIA_INFO_METADATA_UPDATE = 802;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/MediaPlayer$OnBufferingUpdateListener.class */
    public interface OnBufferingUpdateListener {
        void onBufferingUpdate(MediaPlayer mediaPlayer, int i);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/MediaPlayer$OnCompletionListener.class */
    public interface OnCompletionListener {
        void onCompletion(MediaPlayer mediaPlayer);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/MediaPlayer$OnErrorListener.class */
    public interface OnErrorListener {
        boolean onError(MediaPlayer mediaPlayer, int i, int i2);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/MediaPlayer$OnInfoListener.class */
    public interface OnInfoListener {
        boolean onInfo(MediaPlayer mediaPlayer, int i, int i2);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/MediaPlayer$OnPreparedListener.class */
    public interface OnPreparedListener {
        void onPrepared(MediaPlayer mediaPlayer);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/MediaPlayer$OnSeekCompleteListener.class */
    public interface OnSeekCompleteListener {
        void onSeekComplete(MediaPlayer mediaPlayer);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/MediaPlayer$OnTimedTextListener.class */
    public interface OnTimedTextListener {
        void onTimedText(MediaPlayer mediaPlayer, TimedText timedText);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/MediaPlayer$OnVideoSizeChangedListener.class */
    public interface OnVideoSizeChangedListener {
        void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i2);
    }

    public native void setDataSource(FileDescriptor fileDescriptor, long j, long j2) throws IOException, IllegalArgumentException, IllegalStateException;

    public native void prepare() throws IOException, IllegalStateException;

    public native void prepareAsync() throws IllegalStateException;

    public native int getVideoWidth();

    public native int getVideoHeight();

    public native boolean isPlaying();

    public native void seekTo(int i) throws IllegalStateException;

    public native int getCurrentPosition();

    public native int getDuration();

    public native void setNextMediaPlayer(MediaPlayer mediaPlayer);

    public native void setAudioStreamType(int i);

    public native void setLooping(boolean z);

    public native boolean isLooping();

    public native void setVolume(float f, float f2);

    public native void setAudioSessionId(int i) throws IllegalArgumentException, IllegalStateException;

    public native int getAudioSessionId();

    public native void attachAuxEffect(int i);

    public native void setAuxEffectSendLevel(float f);

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/MediaPlayer$TrackInfo.class */
    public static class TrackInfo implements Parcelable {
        public static final int MEDIA_TRACK_TYPE_UNKNOWN = 0;
        public static final int MEDIA_TRACK_TYPE_VIDEO = 1;
        public static final int MEDIA_TRACK_TYPE_AUDIO = 2;
        public static final int MEDIA_TRACK_TYPE_TIMEDTEXT = 3;

        TrackInfo() {
            throw new RuntimeException("Stub!");
        }

        public int getTrackType() {
            throw new RuntimeException("Stub!");
        }

        public String getLanguage() {
            throw new RuntimeException("Stub!");
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            throw new RuntimeException("Stub!");
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            throw new RuntimeException("Stub!");
        }
    }

    public MediaPlayer() {
        throw new RuntimeException("Stub!");
    }

    public void setDisplay(SurfaceHolder sh) {
        throw new RuntimeException("Stub!");
    }

    public void setSurface(Surface surface) {
        throw new RuntimeException("Stub!");
    }

    public void setVideoScalingMode(int mode) {
        throw new RuntimeException("Stub!");
    }

    public static MediaPlayer create(Context context, Uri uri) {
        throw new RuntimeException("Stub!");
    }

    public static MediaPlayer create(Context context, Uri uri, SurfaceHolder holder) {
        throw new RuntimeException("Stub!");
    }

    public static MediaPlayer create(Context context, int resid) {
        throw new RuntimeException("Stub!");
    }

    public void setDataSource(Context context, Uri uri) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        throw new RuntimeException("Stub!");
    }

    public void setDataSource(Context context, Uri uri, Map<String, String> headers) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        throw new RuntimeException("Stub!");
    }

    public void setDataSource(String path) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        throw new RuntimeException("Stub!");
    }

    public void setDataSource(FileDescriptor fd) throws IOException, IllegalArgumentException, IllegalStateException {
        throw new RuntimeException("Stub!");
    }

    public void start() throws IllegalStateException {
        throw new RuntimeException("Stub!");
    }

    public void stop() throws IllegalStateException {
        throw new RuntimeException("Stub!");
    }

    public void pause() throws IllegalStateException {
        throw new RuntimeException("Stub!");
    }

    public void setWakeMode(Context context, int mode) {
        throw new RuntimeException("Stub!");
    }

    public void setScreenOnWhilePlaying(boolean screenOn) {
        throw new RuntimeException("Stub!");
    }

    public void release() {
        throw new RuntimeException("Stub!");
    }

    public void reset() {
        throw new RuntimeException("Stub!");
    }

    public TrackInfo[] getTrackInfo() throws IllegalStateException {
        throw new RuntimeException("Stub!");
    }

    public void addTimedTextSource(String path, String mimeType) throws IOException, IllegalArgumentException, IllegalStateException {
        throw new RuntimeException("Stub!");
    }

    public void addTimedTextSource(Context context, Uri uri, String mimeType) throws IOException, IllegalArgumentException, IllegalStateException {
        throw new RuntimeException("Stub!");
    }

    public void addTimedTextSource(FileDescriptor fd, String mimeType) throws IllegalArgumentException, IllegalStateException {
        throw new RuntimeException("Stub!");
    }

    public void addTimedTextSource(FileDescriptor fd, long offset, long length, String mimeType) throws IllegalArgumentException, IllegalStateException {
        throw new RuntimeException("Stub!");
    }

    public void selectTrack(int index) throws IllegalStateException {
        throw new RuntimeException("Stub!");
    }

    public void deselectTrack(int index) throws IllegalStateException {
        throw new RuntimeException("Stub!");
    }

    protected void finalize() {
        throw new RuntimeException("Stub!");
    }

    public void setOnPreparedListener(OnPreparedListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void setOnCompletionListener(OnCompletionListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void setOnBufferingUpdateListener(OnBufferingUpdateListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void setOnSeekCompleteListener(OnSeekCompleteListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void setOnVideoSizeChangedListener(OnVideoSizeChangedListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void setOnTimedTextListener(OnTimedTextListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void setOnErrorListener(OnErrorListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void setOnInfoListener(OnInfoListener listener) {
        throw new RuntimeException("Stub!");
    }
}
