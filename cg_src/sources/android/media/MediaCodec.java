package android.media;

import android.view.Surface;
import java.nio.ByteBuffer;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/MediaCodec.class */
public final class MediaCodec {
    public static final int BUFFER_FLAG_SYNC_FRAME = 1;
    public static final int BUFFER_FLAG_CODEC_CONFIG = 2;
    public static final int BUFFER_FLAG_END_OF_STREAM = 4;
    public static final int CONFIGURE_FLAG_ENCODE = 1;
    public static final int CRYPTO_MODE_UNENCRYPTED = 0;
    public static final int CRYPTO_MODE_AES_CTR = 1;
    public static final int INFO_TRY_AGAIN_LATER = -1;
    public static final int INFO_OUTPUT_FORMAT_CHANGED = -2;
    public static final int INFO_OUTPUT_BUFFERS_CHANGED = -3;
    public static final int VIDEO_SCALING_MODE_SCALE_TO_FIT = 1;
    public static final int VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING = 2;

    public final native void release();

    public final native void start();

    public final native void stop();

    public final native void flush();

    public final native void queueInputBuffer(int i, int i2, int i3, long j, int i4) throws CryptoException;

    public final native void queueSecureInputBuffer(int i, int i2, CryptoInfo cryptoInfo, long j, int i3) throws CryptoException;

    public final native int dequeueInputBuffer(long j);

    public final native int dequeueOutputBuffer(BufferInfo bufferInfo, long j);

    public final native void releaseOutputBuffer(int i, boolean z);

    public final native void setVideoScalingMode(int i);

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/MediaCodec$BufferInfo.class */
    public static final class BufferInfo {
        public int offset;
        public int size;
        public long presentationTimeUs;
        public int flags;

        public BufferInfo() {
            throw new RuntimeException("Stub!");
        }

        public void set(int newOffset, int newSize, long newTimeUs, int newFlags) {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/MediaCodec$CryptoException.class */
    public static final class CryptoException extends RuntimeException {
        public CryptoException(int errorCode, String detailMessage) {
            throw new RuntimeException("Stub!");
        }

        public int getErrorCode() {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/MediaCodec$CryptoInfo.class */
    public static final class CryptoInfo {
        public int numSubSamples;
        public int[] numBytesOfClearData = null;
        public int[] numBytesOfEncryptedData = null;
        public byte[] key = null;
        public byte[] iv = null;
        public int mode;

        public CryptoInfo() {
            throw new RuntimeException("Stub!");
        }

        public void set(int newNumSubSamples, int[] newNumBytesOfClearData, int[] newNumBytesOfEncryptedData, byte[] newKey, byte[] newIV, int newMode) {
            throw new RuntimeException("Stub!");
        }
    }

    MediaCodec() {
        throw new RuntimeException("Stub!");
    }

    public static MediaCodec createDecoderByType(String type) {
        throw new RuntimeException("Stub!");
    }

    public static MediaCodec createEncoderByType(String type) {
        throw new RuntimeException("Stub!");
    }

    public static MediaCodec createByCodecName(String name) {
        throw new RuntimeException("Stub!");
    }

    protected void finalize() {
        throw new RuntimeException("Stub!");
    }

    public void configure(MediaFormat format, Surface surface, MediaCrypto crypto, int flags) {
        throw new RuntimeException("Stub!");
    }

    public final MediaFormat getOutputFormat() {
        throw new RuntimeException("Stub!");
    }

    public ByteBuffer[] getInputBuffers() {
        throw new RuntimeException("Stub!");
    }

    public ByteBuffer[] getOutputBuffers() {
        throw new RuntimeException("Stub!");
    }
}
