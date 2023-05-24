package android.graphics;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/graphics/Camera.class */
public class Camera {
    public native void save();

    public native void restore();

    public native void translate(float f, float f2, float f3);

    public native void rotateX(float f);

    public native void rotateY(float f);

    public native void rotateZ(float f);

    public native void rotate(float f, float f2, float f3);

    public native float getLocationX();

    public native float getLocationY();

    public native float getLocationZ();

    public native void setLocation(float f, float f2, float f3);

    public native float dotWithNormal(float f, float f2, float f3);

    public Camera() {
        throw new RuntimeException("Stub!");
    }

    public void getMatrix(Matrix matrix) {
        throw new RuntimeException("Stub!");
    }

    public void applyToCanvas(Canvas canvas) {
        throw new RuntimeException("Stub!");
    }

    protected void finalize() throws Throwable {
        throw new RuntimeException("Stub!");
    }
}
