package javax.microedition.khronos.egl;

import javax.microedition.khronos.opengles.GL;
/* loaded from: gencallgraphv3.jar:opengl-api-gl1.1-android-2.1_r1.jar:javax/microedition/khronos/egl/EGLContext.class */
public abstract class EGLContext {
    public abstract GL getGL();

    public EGLContext() {
        throw new RuntimeException("Stub!");
    }

    public static EGL getEGL() {
        throw new RuntimeException("Stub!");
    }
}
