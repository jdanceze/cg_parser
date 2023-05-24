package android.graphics;

import android.graphics.BitmapFactory;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/graphics/BitmapRegionDecoder.class */
public final class BitmapRegionDecoder {
    BitmapRegionDecoder() {
        throw new RuntimeException("Stub!");
    }

    public static BitmapRegionDecoder newInstance(byte[] data, int offset, int length, boolean isShareable) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public static BitmapRegionDecoder newInstance(FileDescriptor fd, boolean isShareable) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public static BitmapRegionDecoder newInstance(InputStream is, boolean isShareable) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public static BitmapRegionDecoder newInstance(String pathName, boolean isShareable) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public Bitmap decodeRegion(Rect rect, BitmapFactory.Options options) {
        throw new RuntimeException("Stub!");
    }

    public int getWidth() {
        throw new RuntimeException("Stub!");
    }

    public int getHeight() {
        throw new RuntimeException("Stub!");
    }

    public void recycle() {
        throw new RuntimeException("Stub!");
    }

    public final boolean isRecycled() {
        throw new RuntimeException("Stub!");
    }

    protected void finalize() throws Throwable {
        throw new RuntimeException("Stub!");
    }
}
