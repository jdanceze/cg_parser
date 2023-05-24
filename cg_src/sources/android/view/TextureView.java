package android.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/TextureView.class */
public class TextureView extends View {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/TextureView$SurfaceTextureListener.class */
    public interface SurfaceTextureListener {
        void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2);

        void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2);

        boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture);

        void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture);
    }

    public TextureView(Context context) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public TextureView(Context context, AttributeSet attrs) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public TextureView(Context context, AttributeSet attrs, int defStyle) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public boolean isOpaque() {
        throw new RuntimeException("Stub!");
    }

    public void setOpaque(boolean opaque) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onAttachedToWindow() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void setLayerType(int layerType, Paint paint) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public int getLayerType() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void buildLayer() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public final void draw(Canvas canvas) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected final void onDraw(Canvas canvas) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onVisibilityChanged(View changedView, int visibility) {
        throw new RuntimeException("Stub!");
    }

    public void setTransform(Matrix transform) {
        throw new RuntimeException("Stub!");
    }

    public Matrix getTransform(Matrix transform) {
        throw new RuntimeException("Stub!");
    }

    public Bitmap getBitmap() {
        throw new RuntimeException("Stub!");
    }

    public Bitmap getBitmap(int width, int height) {
        throw new RuntimeException("Stub!");
    }

    public Bitmap getBitmap(Bitmap bitmap) {
        throw new RuntimeException("Stub!");
    }

    public boolean isAvailable() {
        throw new RuntimeException("Stub!");
    }

    public Canvas lockCanvas() {
        throw new RuntimeException("Stub!");
    }

    public Canvas lockCanvas(Rect dirty) {
        throw new RuntimeException("Stub!");
    }

    public void unlockCanvasAndPost(Canvas canvas) {
        throw new RuntimeException("Stub!");
    }

    public SurfaceTexture getSurfaceTexture() {
        throw new RuntimeException("Stub!");
    }

    public void setSurfaceTexture(SurfaceTexture surfaceTexture) {
        throw new RuntimeException("Stub!");
    }

    public SurfaceTextureListener getSurfaceTextureListener() {
        throw new RuntimeException("Stub!");
    }

    public void setSurfaceTextureListener(SurfaceTextureListener listener) {
        throw new RuntimeException("Stub!");
    }
}
