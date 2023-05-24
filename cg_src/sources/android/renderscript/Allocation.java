package android.renderscript;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.Surface;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/renderscript/Allocation.class */
public class Allocation extends BaseObj {
    public static final int USAGE_SCRIPT = 1;
    public static final int USAGE_GRAPHICS_TEXTURE = 2;
    public static final int USAGE_GRAPHICS_VERTEX = 4;
    public static final int USAGE_GRAPHICS_CONSTANTS = 8;
    public static final int USAGE_GRAPHICS_RENDER_TARGET = 16;
    public static final int USAGE_IO_INPUT = 32;
    public static final int USAGE_IO_OUTPUT = 64;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/renderscript/Allocation$MipmapControl.class */
    public enum MipmapControl {
        MIPMAP_FULL,
        MIPMAP_NONE,
        MIPMAP_ON_SYNC_TO_TEXTURE
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Allocation() {
        throw new RuntimeException("Stub!");
    }

    public Element getElement() {
        throw new RuntimeException("Stub!");
    }

    public int getUsage() {
        throw new RuntimeException("Stub!");
    }

    public int getBytesSize() {
        throw new RuntimeException("Stub!");
    }

    public Type getType() {
        throw new RuntimeException("Stub!");
    }

    public void syncAll(int srcLocation) {
        throw new RuntimeException("Stub!");
    }

    public void ioSend() {
        throw new RuntimeException("Stub!");
    }

    public void ioReceive() {
        throw new RuntimeException("Stub!");
    }

    public void copyFrom(BaseObj[] d) {
        throw new RuntimeException("Stub!");
    }

    public void copyFromUnchecked(int[] d) {
        throw new RuntimeException("Stub!");
    }

    public void copyFromUnchecked(short[] d) {
        throw new RuntimeException("Stub!");
    }

    public void copyFromUnchecked(byte[] d) {
        throw new RuntimeException("Stub!");
    }

    public void copyFromUnchecked(float[] d) {
        throw new RuntimeException("Stub!");
    }

    public void copyFrom(int[] d) {
        throw new RuntimeException("Stub!");
    }

    public void copyFrom(short[] d) {
        throw new RuntimeException("Stub!");
    }

    public void copyFrom(byte[] d) {
        throw new RuntimeException("Stub!");
    }

    public void copyFrom(float[] d) {
        throw new RuntimeException("Stub!");
    }

    public void copyFrom(Bitmap b) {
        throw new RuntimeException("Stub!");
    }

    public void setFromFieldPacker(int xoff, FieldPacker fp) {
        throw new RuntimeException("Stub!");
    }

    public void setFromFieldPacker(int xoff, int component_number, FieldPacker fp) {
        throw new RuntimeException("Stub!");
    }

    public void generateMipmaps() {
        throw new RuntimeException("Stub!");
    }

    public void copy1DRangeFromUnchecked(int off, int count, int[] d) {
        throw new RuntimeException("Stub!");
    }

    public void copy1DRangeFromUnchecked(int off, int count, short[] d) {
        throw new RuntimeException("Stub!");
    }

    public void copy1DRangeFromUnchecked(int off, int count, byte[] d) {
        throw new RuntimeException("Stub!");
    }

    public void copy1DRangeFromUnchecked(int off, int count, float[] d) {
        throw new RuntimeException("Stub!");
    }

    public void copy1DRangeFrom(int off, int count, int[] d) {
        throw new RuntimeException("Stub!");
    }

    public void copy1DRangeFrom(int off, int count, short[] d) {
        throw new RuntimeException("Stub!");
    }

    public void copy1DRangeFrom(int off, int count, byte[] d) {
        throw new RuntimeException("Stub!");
    }

    public void copy1DRangeFrom(int off, int count, float[] d) {
        throw new RuntimeException("Stub!");
    }

    public void copy1DRangeFrom(int off, int count, Allocation data, int dataOff) {
        throw new RuntimeException("Stub!");
    }

    public void copy2DRangeFrom(int xoff, int yoff, int w, int h, byte[] data) {
        throw new RuntimeException("Stub!");
    }

    public void copy2DRangeFrom(int xoff, int yoff, int w, int h, short[] data) {
        throw new RuntimeException("Stub!");
    }

    public void copy2DRangeFrom(int xoff, int yoff, int w, int h, int[] data) {
        throw new RuntimeException("Stub!");
    }

    public void copy2DRangeFrom(int xoff, int yoff, int w, int h, float[] data) {
        throw new RuntimeException("Stub!");
    }

    public void copy2DRangeFrom(int xoff, int yoff, int w, int h, Allocation data, int dataXoff, int dataYoff) {
        throw new RuntimeException("Stub!");
    }

    public void copy2DRangeFrom(int xoff, int yoff, Bitmap data) {
        throw new RuntimeException("Stub!");
    }

    public void copyTo(Bitmap b) {
        throw new RuntimeException("Stub!");
    }

    public void copyTo(byte[] d) {
        throw new RuntimeException("Stub!");
    }

    public void copyTo(short[] d) {
        throw new RuntimeException("Stub!");
    }

    public void copyTo(int[] d) {
        throw new RuntimeException("Stub!");
    }

    public void copyTo(float[] d) {
        throw new RuntimeException("Stub!");
    }

    public synchronized void resize(int dimX) {
        throw new RuntimeException("Stub!");
    }

    public static Allocation createTyped(RenderScript rs, Type type, MipmapControl mips, int usage) {
        throw new RuntimeException("Stub!");
    }

    public static Allocation createTyped(RenderScript rs, Type type, int usage) {
        throw new RuntimeException("Stub!");
    }

    public static Allocation createTyped(RenderScript rs, Type type) {
        throw new RuntimeException("Stub!");
    }

    public static Allocation createSized(RenderScript rs, Element e, int count, int usage) {
        throw new RuntimeException("Stub!");
    }

    public static Allocation createSized(RenderScript rs, Element e, int count) {
        throw new RuntimeException("Stub!");
    }

    public static Allocation createFromBitmap(RenderScript rs, Bitmap b, MipmapControl mips, int usage) {
        throw new RuntimeException("Stub!");
    }

    public Surface getSurface() {
        throw new RuntimeException("Stub!");
    }

    public void setSurface(Surface sur) {
        throw new RuntimeException("Stub!");
    }

    public static Allocation createFromBitmap(RenderScript rs, Bitmap b) {
        throw new RuntimeException("Stub!");
    }

    public static Allocation createCubemapFromBitmap(RenderScript rs, Bitmap b, MipmapControl mips, int usage) {
        throw new RuntimeException("Stub!");
    }

    public static Allocation createCubemapFromBitmap(RenderScript rs, Bitmap b) {
        throw new RuntimeException("Stub!");
    }

    public static Allocation createCubemapFromCubeFaces(RenderScript rs, Bitmap xpos, Bitmap xneg, Bitmap ypos, Bitmap yneg, Bitmap zpos, Bitmap zneg, MipmapControl mips, int usage) {
        throw new RuntimeException("Stub!");
    }

    public static Allocation createCubemapFromCubeFaces(RenderScript rs, Bitmap xpos, Bitmap xneg, Bitmap ypos, Bitmap yneg, Bitmap zpos, Bitmap zneg) {
        throw new RuntimeException("Stub!");
    }

    public static Allocation createFromBitmapResource(RenderScript rs, Resources res, int id, MipmapControl mips, int usage) {
        throw new RuntimeException("Stub!");
    }

    public static Allocation createFromBitmapResource(RenderScript rs, Resources res, int id) {
        throw new RuntimeException("Stub!");
    }

    public static Allocation createFromString(RenderScript rs, String str, int usage) {
        throw new RuntimeException("Stub!");
    }
}
