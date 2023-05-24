package android.graphics;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.TypedValue;
import java.io.FileDescriptor;
import java.io.InputStream;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/graphics/BitmapFactory.class */
public class BitmapFactory {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/graphics/BitmapFactory$Options.class */
    public static class Options {
        public Bitmap inBitmap;
        public boolean inMutable;
        public boolean inJustDecodeBounds;
        public int inSampleSize;
        public Bitmap.Config inPreferredConfig;
        public boolean inDither;
        public int inDensity;
        public int inTargetDensity;
        public int inScreenDensity;
        public boolean inScaled;
        public boolean inPurgeable;
        public boolean inInputShareable;
        public boolean inPreferQualityOverSpeed;
        public int outWidth;
        public int outHeight;
        public String outMimeType;
        public byte[] inTempStorage = null;
        public boolean mCancel;

        public Options() {
            throw new RuntimeException("Stub!");
        }

        public void requestCancelDecode() {
            throw new RuntimeException("Stub!");
        }
    }

    public BitmapFactory() {
        throw new RuntimeException("Stub!");
    }

    public static Bitmap decodeFile(String pathName, Options opts) {
        throw new RuntimeException("Stub!");
    }

    public static Bitmap decodeFile(String pathName) {
        throw new RuntimeException("Stub!");
    }

    public static Bitmap decodeResourceStream(Resources res, TypedValue value, InputStream is, Rect pad, Options opts) {
        throw new RuntimeException("Stub!");
    }

    public static Bitmap decodeResource(Resources res, int id, Options opts) {
        throw new RuntimeException("Stub!");
    }

    public static Bitmap decodeResource(Resources res, int id) {
        throw new RuntimeException("Stub!");
    }

    public static Bitmap decodeByteArray(byte[] data, int offset, int length, Options opts) {
        throw new RuntimeException("Stub!");
    }

    public static Bitmap decodeByteArray(byte[] data, int offset, int length) {
        throw new RuntimeException("Stub!");
    }

    public static Bitmap decodeStream(InputStream is, Rect outPadding, Options opts) {
        throw new RuntimeException("Stub!");
    }

    public static Bitmap decodeStream(InputStream is) {
        throw new RuntimeException("Stub!");
    }

    public static Bitmap decodeFileDescriptor(FileDescriptor fd, Rect outPadding, Options opts) {
        throw new RuntimeException("Stub!");
    }

    public static Bitmap decodeFileDescriptor(FileDescriptor fd) {
        throw new RuntimeException("Stub!");
    }
}
