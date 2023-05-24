package android.opengl;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/opengl/GLSurfaceView.class */
public class GLSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    public static final int RENDERMODE_WHEN_DIRTY = 0;
    public static final int RENDERMODE_CONTINUOUSLY = 1;
    public static final int DEBUG_CHECK_GL_ERROR = 1;
    public static final int DEBUG_LOG_GL_CALLS = 2;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/opengl/GLSurfaceView$EGLConfigChooser.class */
    public interface EGLConfigChooser {
        javax.microedition.khronos.egl.EGLConfig chooseConfig(EGL10 egl10, javax.microedition.khronos.egl.EGLDisplay eGLDisplay);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/opengl/GLSurfaceView$EGLContextFactory.class */
    public interface EGLContextFactory {
        javax.microedition.khronos.egl.EGLContext createContext(EGL10 egl10, javax.microedition.khronos.egl.EGLDisplay eGLDisplay, javax.microedition.khronos.egl.EGLConfig eGLConfig);

        void destroyContext(EGL10 egl10, javax.microedition.khronos.egl.EGLDisplay eGLDisplay, javax.microedition.khronos.egl.EGLContext eGLContext);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/opengl/GLSurfaceView$EGLWindowSurfaceFactory.class */
    public interface EGLWindowSurfaceFactory {
        javax.microedition.khronos.egl.EGLSurface createWindowSurface(EGL10 egl10, javax.microedition.khronos.egl.EGLDisplay eGLDisplay, javax.microedition.khronos.egl.EGLConfig eGLConfig, Object obj);

        void destroySurface(EGL10 egl10, javax.microedition.khronos.egl.EGLDisplay eGLDisplay, javax.microedition.khronos.egl.EGLSurface eGLSurface);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/opengl/GLSurfaceView$GLWrapper.class */
    public interface GLWrapper {
        GL wrap(GL gl);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/opengl/GLSurfaceView$Renderer.class */
    public interface Renderer {
        void onSurfaceCreated(GL10 gl10, javax.microedition.khronos.egl.EGLConfig eGLConfig);

        void onSurfaceChanged(GL10 gl10, int i, int i2);

        void onDrawFrame(GL10 gl10);
    }

    public GLSurfaceView(Context context) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public GLSurfaceView(Context context, AttributeSet attrs) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    protected void finalize() throws Throwable {
        throw new RuntimeException("Stub!");
    }

    public void setGLWrapper(GLWrapper glWrapper) {
        throw new RuntimeException("Stub!");
    }

    public void setDebugFlags(int debugFlags) {
        throw new RuntimeException("Stub!");
    }

    public int getDebugFlags() {
        throw new RuntimeException("Stub!");
    }

    public void setPreserveEGLContextOnPause(boolean preserveOnPause) {
        throw new RuntimeException("Stub!");
    }

    public boolean getPreserveEGLContextOnPause() {
        throw new RuntimeException("Stub!");
    }

    public void setRenderer(Renderer renderer) {
        throw new RuntimeException("Stub!");
    }

    public void setEGLContextFactory(EGLContextFactory factory) {
        throw new RuntimeException("Stub!");
    }

    public void setEGLWindowSurfaceFactory(EGLWindowSurfaceFactory factory) {
        throw new RuntimeException("Stub!");
    }

    public void setEGLConfigChooser(EGLConfigChooser configChooser) {
        throw new RuntimeException("Stub!");
    }

    public void setEGLConfigChooser(boolean needDepth) {
        throw new RuntimeException("Stub!");
    }

    public void setEGLConfigChooser(int redSize, int greenSize, int blueSize, int alphaSize, int depthSize, int stencilSize) {
        throw new RuntimeException("Stub!");
    }

    public void setEGLContextClientVersion(int version) {
        throw new RuntimeException("Stub!");
    }

    public void setRenderMode(int renderMode) {
        throw new RuntimeException("Stub!");
    }

    public int getRenderMode() {
        throw new RuntimeException("Stub!");
    }

    public void requestRender() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder holder) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder holder) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        throw new RuntimeException("Stub!");
    }

    public void onPause() {
        throw new RuntimeException("Stub!");
    }

    public void onResume() {
        throw new RuntimeException("Stub!");
    }

    public void queueEvent(Runnable r) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.SurfaceView, android.view.View
    protected void onAttachedToWindow() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.SurfaceView, android.view.View
    protected void onDetachedFromWindow() {
        throw new RuntimeException("Stub!");
    }
}
