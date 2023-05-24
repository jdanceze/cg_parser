package android.renderscript;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.view.SurfaceHolder;
@Deprecated
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/renderscript/RenderScriptGL.class */
public class RenderScriptGL extends RenderScript {

    @Deprecated
    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/renderscript/RenderScriptGL$SurfaceConfig.class */
    public static class SurfaceConfig {
        @Deprecated
        public SurfaceConfig() {
            throw new RuntimeException("Stub!");
        }

        @Deprecated
        public SurfaceConfig(SurfaceConfig sc) {
            throw new RuntimeException("Stub!");
        }

        @Deprecated
        public void setColor(int minimum, int preferred) {
            throw new RuntimeException("Stub!");
        }

        @Deprecated
        public void setAlpha(int minimum, int preferred) {
            throw new RuntimeException("Stub!");
        }

        @Deprecated
        public void setDepth(int minimum, int preferred) {
            throw new RuntimeException("Stub!");
        }

        @Deprecated
        public void setSamples(int minimum, int preferred, float Q) {
            throw new RuntimeException("Stub!");
        }
    }

    @Deprecated
    public RenderScriptGL(Context ctx, SurfaceConfig sc) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void setSurface(SurfaceHolder sur, int w, int h) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void setSurfaceTexture(SurfaceTexture sur, int w, int h) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public int getHeight() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public int getWidth() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void pause() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void resume() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void bindRootScript(Script s) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void bindProgramStore(ProgramStore p) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void bindProgramFragment(ProgramFragment p) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void bindProgramRaster(ProgramRaster p) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void bindProgramVertex(ProgramVertex p) {
        throw new RuntimeException("Stub!");
    }
}
