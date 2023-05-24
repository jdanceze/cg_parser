package android.view;

import android.content.Context;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/OrientationEventListener.class */
public abstract class OrientationEventListener {
    public static final int ORIENTATION_UNKNOWN = -1;

    public abstract void onOrientationChanged(int i);

    public OrientationEventListener(Context context) {
        throw new RuntimeException("Stub!");
    }

    public OrientationEventListener(Context context, int rate) {
        throw new RuntimeException("Stub!");
    }

    public void enable() {
        throw new RuntimeException("Stub!");
    }

    public void disable() {
        throw new RuntimeException("Stub!");
    }

    public boolean canDetectOrientation() {
        throw new RuntimeException("Stub!");
    }
}
