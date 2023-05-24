package android.graphics;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/graphics/SurfaceTexture.class */
public class SurfaceTexture {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/graphics/SurfaceTexture$OnFrameAvailableListener.class */
    public interface OnFrameAvailableListener {
        void onFrameAvailable(SurfaceTexture surfaceTexture);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/graphics/SurfaceTexture$OutOfResourcesException.class */
    public static class OutOfResourcesException extends Exception {
        public OutOfResourcesException() {
            throw new RuntimeException("Stub!");
        }

        public OutOfResourcesException(String name) {
            throw new RuntimeException("Stub!");
        }
    }

    public SurfaceTexture(int texName) {
        throw new RuntimeException("Stub!");
    }

    public void setOnFrameAvailableListener(OnFrameAvailableListener l) {
        throw new RuntimeException("Stub!");
    }

    public void setDefaultBufferSize(int width, int height) {
        throw new RuntimeException("Stub!");
    }

    public void updateTexImage() {
        throw new RuntimeException("Stub!");
    }

    public void detachFromGLContext() {
        throw new RuntimeException("Stub!");
    }

    public void attachToGLContext(int texName) {
        throw new RuntimeException("Stub!");
    }

    public void getTransformMatrix(float[] mtx) {
        throw new RuntimeException("Stub!");
    }

    public long getTimestamp() {
        throw new RuntimeException("Stub!");
    }

    public void release() {
        throw new RuntimeException("Stub!");
    }

    protected void finalize() throws Throwable {
        throw new RuntimeException("Stub!");
    }
}
