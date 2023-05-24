package android.hardware;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/hardware/SensorEventListener.class */
public interface SensorEventListener {
    void onSensorChanged(SensorEvent sensorEvent);

    void onAccuracyChanged(Sensor sensor, int i);
}
