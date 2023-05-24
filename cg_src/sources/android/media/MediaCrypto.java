package android.media;

import java.util.UUID;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/MediaCrypto.class */
public final class MediaCrypto {
    public final native boolean requiresSecureDecoderComponent(String str);

    public final native void release();

    public MediaCrypto(UUID uuid, byte[] initData) throws MediaCryptoException {
        throw new RuntimeException("Stub!");
    }

    public static final boolean isCryptoSchemeSupported(UUID uuid) {
        throw new RuntimeException("Stub!");
    }

    protected void finalize() {
        throw new RuntimeException("Stub!");
    }
}
