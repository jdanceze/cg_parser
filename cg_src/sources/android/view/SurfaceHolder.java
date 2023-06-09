package android.view;

import android.graphics.Canvas;
import android.graphics.Rect;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/SurfaceHolder.class */
public interface SurfaceHolder {
    @Deprecated
    public static final int SURFACE_TYPE_NORMAL = 0;
    @Deprecated
    public static final int SURFACE_TYPE_HARDWARE = 1;
    @Deprecated
    public static final int SURFACE_TYPE_GPU = 2;
    @Deprecated
    public static final int SURFACE_TYPE_PUSH_BUFFERS = 3;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/SurfaceHolder$Callback.class */
    public interface Callback {
        void surfaceCreated(SurfaceHolder surfaceHolder);

        void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3);

        void surfaceDestroyed(SurfaceHolder surfaceHolder);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/SurfaceHolder$Callback2.class */
    public interface Callback2 extends Callback {
        void surfaceRedrawNeeded(SurfaceHolder surfaceHolder);
    }

    void addCallback(Callback callback);

    void removeCallback(Callback callback);

    boolean isCreating();

    @Deprecated
    void setType(int i);

    void setFixedSize(int i, int i2);

    void setSizeFromLayout();

    void setFormat(int i);

    void setKeepScreenOn(boolean z);

    Canvas lockCanvas();

    Canvas lockCanvas(Rect rect);

    void unlockCanvasAndPost(Canvas canvas);

    Rect getSurfaceFrame();

    Surface getSurface();

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/SurfaceHolder$BadSurfaceTypeException.class */
    public static class BadSurfaceTypeException extends RuntimeException {
        public BadSurfaceTypeException() {
            throw new RuntimeException("Stub!");
        }

        public BadSurfaceTypeException(String name) {
            throw new RuntimeException("Stub!");
        }
    }
}
