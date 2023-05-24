package android.graphics;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import java.io.OutputStream;
import java.nio.Buffer;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/graphics/Bitmap.class */
public final class Bitmap implements Parcelable {
    public static final int DENSITY_NONE = 0;
    public static final Parcelable.Creator<Bitmap> CREATOR = null;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/graphics/Bitmap$CompressFormat.class */
    public enum CompressFormat {
        JPEG,
        PNG,
        WEBP
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/graphics/Bitmap$Config.class */
    public enum Config {
        ALPHA_8,
        ARGB_4444,
        ARGB_8888,
        RGB_565
    }

    Bitmap() {
        throw new RuntimeException("Stub!");
    }

    public int getDensity() {
        throw new RuntimeException("Stub!");
    }

    public void setDensity(int density) {
        throw new RuntimeException("Stub!");
    }

    public void recycle() {
        throw new RuntimeException("Stub!");
    }

    public final boolean isRecycled() {
        throw new RuntimeException("Stub!");
    }

    public int getGenerationId() {
        throw new RuntimeException("Stub!");
    }

    public void copyPixelsToBuffer(Buffer dst) {
        throw new RuntimeException("Stub!");
    }

    public void copyPixelsFromBuffer(Buffer src) {
        throw new RuntimeException("Stub!");
    }

    public Bitmap copy(Config config, boolean isMutable) {
        throw new RuntimeException("Stub!");
    }

    public static Bitmap createScaledBitmap(Bitmap src, int dstWidth, int dstHeight, boolean filter) {
        throw new RuntimeException("Stub!");
    }

    public static Bitmap createBitmap(Bitmap src) {
        throw new RuntimeException("Stub!");
    }

    public static Bitmap createBitmap(Bitmap source, int x, int y, int width, int height) {
        throw new RuntimeException("Stub!");
    }

    public static Bitmap createBitmap(Bitmap source, int x, int y, int width, int height, Matrix m, boolean filter) {
        throw new RuntimeException("Stub!");
    }

    public static Bitmap createBitmap(int width, int height, Config config) {
        throw new RuntimeException("Stub!");
    }

    public static Bitmap createBitmap(int[] colors, int offset, int stride, int width, int height, Config config) {
        throw new RuntimeException("Stub!");
    }

    public static Bitmap createBitmap(int[] colors, int width, int height, Config config) {
        throw new RuntimeException("Stub!");
    }

    public byte[] getNinePatchChunk() {
        throw new RuntimeException("Stub!");
    }

    public boolean compress(CompressFormat format, int quality, OutputStream stream) {
        throw new RuntimeException("Stub!");
    }

    public final boolean isMutable() {
        throw new RuntimeException("Stub!");
    }

    public final int getWidth() {
        throw new RuntimeException("Stub!");
    }

    public final int getHeight() {
        throw new RuntimeException("Stub!");
    }

    public int getScaledWidth(Canvas canvas) {
        throw new RuntimeException("Stub!");
    }

    public int getScaledHeight(Canvas canvas) {
        throw new RuntimeException("Stub!");
    }

    public int getScaledWidth(DisplayMetrics metrics) {
        throw new RuntimeException("Stub!");
    }

    public int getScaledHeight(DisplayMetrics metrics) {
        throw new RuntimeException("Stub!");
    }

    public int getScaledWidth(int targetDensity) {
        throw new RuntimeException("Stub!");
    }

    public int getScaledHeight(int targetDensity) {
        throw new RuntimeException("Stub!");
    }

    public final int getRowBytes() {
        throw new RuntimeException("Stub!");
    }

    public final int getByteCount() {
        throw new RuntimeException("Stub!");
    }

    public final Config getConfig() {
        throw new RuntimeException("Stub!");
    }

    public final boolean hasAlpha() {
        throw new RuntimeException("Stub!");
    }

    public void setHasAlpha(boolean hasAlpha) {
        throw new RuntimeException("Stub!");
    }

    public void eraseColor(int c) {
        throw new RuntimeException("Stub!");
    }

    public int getPixel(int x, int y) {
        throw new RuntimeException("Stub!");
    }

    public void getPixels(int[] pixels, int offset, int stride, int x, int y, int width, int height) {
        throw new RuntimeException("Stub!");
    }

    public void setPixel(int x, int y, int color) {
        throw new RuntimeException("Stub!");
    }

    public void setPixels(int[] pixels, int offset, int stride, int x, int y, int width, int height) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel p, int flags) {
        throw new RuntimeException("Stub!");
    }

    public Bitmap extractAlpha() {
        throw new RuntimeException("Stub!");
    }

    public Bitmap extractAlpha(Paint paint, int[] offsetXY) {
        throw new RuntimeException("Stub!");
    }

    public boolean sameAs(Bitmap other) {
        throw new RuntimeException("Stub!");
    }

    public void prepareToDraw() {
        throw new RuntimeException("Stub!");
    }
}
