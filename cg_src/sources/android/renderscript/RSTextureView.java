package android.renderscript;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.renderscript.RenderScriptGL;
import android.util.AttributeSet;
import android.view.TextureView;
@Deprecated
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/renderscript/RSTextureView.class */
public class RSTextureView extends TextureView implements TextureView.SurfaceTextureListener {
    @Deprecated
    public RSTextureView(Context context) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public RSTextureView(Context context, AttributeSet attrs) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    @Deprecated
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    @Deprecated
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    @Deprecated
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    @Deprecated
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
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
