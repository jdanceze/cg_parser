package android.media;

import android.content.Context;
import android.media.MediaCodec;
import android.net.Uri;
import java.io.FileDescriptor;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/MediaExtractor.class */
public final class MediaExtractor {
    public static final int SEEK_TO_PREVIOUS_SYNC = 0;
    public static final int SEEK_TO_NEXT_SYNC = 1;
    public static final int SEEK_TO_CLOSEST_SYNC = 2;
    public static final int SAMPLE_FLAG_SYNC = 1;
    public static final int SAMPLE_FLAG_ENCRYPTED = 2;

    public final native void setDataSource(FileDescriptor fileDescriptor, long j, long j2);

    public final native void release();

    public final native int getTrackCount();

    public native void selectTrack(int i);

    public native void unselectTrack(int i);

    public native void seekTo(long j, int i);

    public native boolean advance();

    public native int readSampleData(ByteBuffer byteBuffer, int i);

    public native int getSampleTrackIndex();

    public native long getSampleTime();

    public native int getSampleFlags();

    public native boolean getSampleCryptoInfo(MediaCodec.CryptoInfo cryptoInfo);

    public native long getCachedDuration();

    public native boolean hasCacheReachedEndOfStream();

    public MediaExtractor() {
        throw new RuntimeException("Stub!");
    }

    public final void setDataSource(Context context, Uri uri, Map<String, String> headers) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public final void setDataSource(String path, Map<String, String> headers) {
        throw new RuntimeException("Stub!");
    }

    public final void setDataSource(String path) {
        throw new RuntimeException("Stub!");
    }

    public final void setDataSource(FileDescriptor fd) {
        throw new RuntimeException("Stub!");
    }

    protected void finalize() {
        throw new RuntimeException("Stub!");
    }

    public MediaFormat getTrackFormat(int index) {
        throw new RuntimeException("Stub!");
    }
}
