package android.hardware;
@Deprecated
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/hardware/SensorListener.class */
public interface SensorListener {
    void onSensorChanged(int i, float[] fArr);

    void onAccuracyChanged(int i, int i2);
}
