package android.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewDebug;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.RemoteViews;
@RemoteViews.RemoteView
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/ImageView.class */
public class ImageView extends View {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/ImageView$ScaleType.class */
    public enum ScaleType {
        CENTER,
        CENTER_CROP,
        CENTER_INSIDE,
        FIT_CENTER,
        FIT_END,
        FIT_START,
        FIT_XY,
        MATRIX
    }

    public ImageView(Context context) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public ImageView(Context context, AttributeSet attrs) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public ImageView(Context context, AttributeSet attrs, int defStyle) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected boolean verifyDrawable(Drawable dr) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void jumpDrawablesToCurrentState() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View, android.graphics.drawable.Drawable.Callback
    public void invalidateDrawable(Drawable dr) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void onPopulateAccessibilityEvent(AccessibilityEvent event) {
        throw new RuntimeException("Stub!");
    }

    public boolean getAdjustViewBounds() {
        throw new RuntimeException("Stub!");
    }

    public void setAdjustViewBounds(boolean adjustViewBounds) {
        throw new RuntimeException("Stub!");
    }

    public int getMaxWidth() {
        throw new RuntimeException("Stub!");
    }

    public void setMaxWidth(int maxWidth) {
        throw new RuntimeException("Stub!");
    }

    public int getMaxHeight() {
        throw new RuntimeException("Stub!");
    }

    public void setMaxHeight(int maxHeight) {
        throw new RuntimeException("Stub!");
    }

    public Drawable getDrawable() {
        throw new RuntimeException("Stub!");
    }

    public void setImageResource(int resId) {
        throw new RuntimeException("Stub!");
    }

    public void setImageURI(Uri uri) {
        throw new RuntimeException("Stub!");
    }

    public void setImageDrawable(Drawable drawable) {
        throw new RuntimeException("Stub!");
    }

    public void setImageBitmap(Bitmap bm) {
        throw new RuntimeException("Stub!");
    }

    public void setImageState(int[] state, boolean merge) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void setSelected(boolean selected) {
        throw new RuntimeException("Stub!");
    }

    public void setImageLevel(int level) {
        throw new RuntimeException("Stub!");
    }

    public void setScaleType(ScaleType scaleType) {
        throw new RuntimeException("Stub!");
    }

    public ScaleType getScaleType() {
        throw new RuntimeException("Stub!");
    }

    public Matrix getImageMatrix() {
        throw new RuntimeException("Stub!");
    }

    public void setImageMatrix(Matrix matrix) {
        throw new RuntimeException("Stub!");
    }

    public boolean getCropToPadding() {
        throw new RuntimeException("Stub!");
    }

    public void setCropToPadding(boolean cropToPadding) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public int[] onCreateDrawableState(int extraSpace) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        throw new RuntimeException("Stub!");
    }

    protected boolean setFrame(int l, int t, int r, int b) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void drawableStateChanged() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    @ViewDebug.ExportedProperty(category = "layout")
    public int getBaseline() {
        throw new RuntimeException("Stub!");
    }

    public void setBaseline(int baseline) {
        throw new RuntimeException("Stub!");
    }

    public void setBaselineAlignBottom(boolean aligned) {
        throw new RuntimeException("Stub!");
    }

    public boolean getBaselineAlignBottom() {
        throw new RuntimeException("Stub!");
    }

    public final void setColorFilter(int color, PorterDuff.Mode mode) {
        throw new RuntimeException("Stub!");
    }

    public final void setColorFilter(int color) {
        throw new RuntimeException("Stub!");
    }

    public final void clearColorFilter() {
        throw new RuntimeException("Stub!");
    }

    public ColorFilter getColorFilter() {
        throw new RuntimeException("Stub!");
    }

    public void setColorFilter(ColorFilter cf) {
        throw new RuntimeException("Stub!");
    }

    public int getImageAlpha() {
        throw new RuntimeException("Stub!");
    }

    public void setImageAlpha(int alpha) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void setAlpha(int alpha) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void setVisibility(int visibility) {
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
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        throw new RuntimeException("Stub!");
    }
}
