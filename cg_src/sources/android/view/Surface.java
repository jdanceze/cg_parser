package android.view;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/Surface.class */
public class Surface implements Parcelable {
    public static final int ROTATION_0 = 0;
    public static final int ROTATION_90 = 1;
    public static final int ROTATION_180 = 2;
    public static final int ROTATION_270 = 3;
    public static final Parcelable.Creator<Surface> CREATOR = null;

    public native boolean isValid();

    public native void release();

    public native void unlockCanvasAndPost(Canvas canvas);

    public native void unlockCanvas(Canvas canvas);

    public native void readFromParcel(Parcel parcel);

    @Override // android.os.Parcelable
    public native void writeToParcel(Parcel parcel, int i);

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/Surface$OutOfResourcesException.class */
    public static class OutOfResourcesException extends Exception {
        public OutOfResourcesException() {
            throw new RuntimeException("Stub!");
        }

        public OutOfResourcesException(String name) {
            throw new RuntimeException("Stub!");
        }
    }

    public Surface(SurfaceTexture surfaceTexture) {
        throw new RuntimeException("Stub!");
    }

    public Canvas lockCanvas(Rect dirty) throws OutOfResourcesException, IllegalArgumentException {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        throw new RuntimeException("Stub!");
    }

    protected void finalize() throws Throwable {
        throw new RuntimeException("Stub!");
    }
}
