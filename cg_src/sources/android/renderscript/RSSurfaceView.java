package android.renderscript;

import android.content.Context;
import android.renderscript.RenderScriptGL;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
@Deprecated
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/renderscript/RSSurfaceView.class */
public class RSSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    @Deprecated
    public RSSurfaceView(Context context) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public RSSurfaceView(Context context, AttributeSet attrs) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.SurfaceHolder.Callback
    @Deprecated
    public void surfaceCreated(SurfaceHolder holder) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.SurfaceHolder.Callback
    @Deprecated
    public void surfaceDestroyed(SurfaceHolder holder) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.SurfaceHolder.Callback
    @Deprecated
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
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
    public RenderScriptGL createRenderScriptGL(RenderScriptGL.SurfaceConfig sc) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void destroyRenderScriptGL() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void setRenderScriptGL(RenderScriptGL rs) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public RenderScriptGL getRenderScriptGL() {
        throw new RuntimeException("Stub!");
    }
}
