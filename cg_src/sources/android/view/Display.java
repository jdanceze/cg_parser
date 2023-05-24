package android.view;

import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/Display.class */
public class Display {
    public static final int DEFAULT_DISPLAY = 0;

    @Deprecated
    public native int getOrientation();

    Display() {
        throw new RuntimeException("Stub!");
    }

    public int getDisplayId() {
        throw new RuntimeException("Stub!");
    }

    public void getSize(Point outSize) {
        throw new RuntimeException("Stub!");
    }

    public void getRectSize(Rect outSize) {
        throw new RuntimeException("Stub!");
    }

    public void getCurrentSizeRange(Point outSmallestSize, Point outLargestSize) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public int getWidth() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public int getHeight() {
        throw new RuntimeException("Stub!");
    }

    public int getRotation() {
        throw new RuntimeException("Stub!");
    }

    public int getPixelFormat() {
        throw new RuntimeException("Stub!");
    }

    public float getRefreshRate() {
        throw new RuntimeException("Stub!");
    }

    public void getMetrics(DisplayMetrics outMetrics) {
        throw new RuntimeException("Stub!");
    }
}
