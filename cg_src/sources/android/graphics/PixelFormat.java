package android.graphics;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/graphics/PixelFormat.class */
public class PixelFormat {
    public static final int UNKNOWN = 0;
    public static final int TRANSLUCENT = -3;
    public static final int TRANSPARENT = -2;
    public static final int OPAQUE = -1;
    public static final int RGBA_8888 = 1;
    public static final int RGBX_8888 = 2;
    public static final int RGB_888 = 3;
    public static final int RGB_565 = 4;
    @Deprecated
    public static final int RGBA_5551 = 6;
    @Deprecated
    public static final int RGBA_4444 = 7;
    public static final int A_8 = 8;
    public static final int L_8 = 9;
    @Deprecated
    public static final int LA_88 = 10;
    @Deprecated
    public static final int RGB_332 = 11;
    @Deprecated
    public static final int YCbCr_422_SP = 16;
    @Deprecated
    public static final int YCbCr_420_SP = 17;
    @Deprecated
    public static final int YCbCr_422_I = 20;
    @Deprecated
    public static final int JPEG = 256;
    public int bytesPerPixel;
    public int bitsPerPixel;

    public static native void getPixelFormatInfo(int i, PixelFormat pixelFormat);

    public PixelFormat() {
        throw new RuntimeException("Stub!");
    }

    public static boolean formatHasAlpha(int format) {
        throw new RuntimeException("Stub!");
    }
}
