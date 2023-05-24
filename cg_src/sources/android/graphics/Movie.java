package android.graphics;

import java.io.InputStream;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/graphics/Movie.class */
public class Movie {
    public native int width();

    public native int height();

    public native boolean isOpaque();

    public native int duration();

    public native boolean setTime(int i);

    public native void draw(Canvas canvas, float f, float f2, Paint paint);

    public static native Movie decodeStream(InputStream inputStream);

    public static native Movie decodeByteArray(byte[] bArr, int i, int i2);

    Movie() {
        throw new RuntimeException("Stub!");
    }

    public void draw(Canvas canvas, float x, float y) {
        throw new RuntimeException("Stub!");
    }

    public static Movie decodeFile(String pathName) {
        throw new RuntimeException("Stub!");
    }

    protected void finalize() throws Throwable {
        throw new RuntimeException("Stub!");
    }
}
