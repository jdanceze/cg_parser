package android.view;

import android.content.Context;
import android.hardware.SensorListener;
@Deprecated
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/OrientationListener.class */
public abstract class OrientationListener implements SensorListener {
    public static final int ORIENTATION_UNKNOWN = -1;

    public abstract void onOrientationChanged(int i);

    public OrientationListener(Context context) {
        throw new RuntimeException("Stub!");
    }

    public OrientationListener(Context context, int rate) {
        throw new RuntimeException("Stub!");
    }

    public void enable() {
        throw new RuntimeException("Stub!");
    }

    public void disable() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.hardware.SensorListener
    public void onAccuracyChanged(int sensor, int accuracy) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.hardware.SensorListener
    public void onSensorChanged(int sensor, float[] values) {
        throw new RuntimeException("Stub!");
    }
}
