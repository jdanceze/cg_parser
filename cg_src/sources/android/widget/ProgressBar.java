package android.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewDebug;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.Interpolator;
import android.widget.RemoteViews;
@RemoteViews.RemoteView
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/ProgressBar.class */
public class ProgressBar extends View {
    public ProgressBar(Context context) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public ProgressBar(Context context, AttributeSet attrs) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public ProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    @ViewDebug.ExportedProperty(category = "progress")
    public synchronized boolean isIndeterminate() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setIndeterminate(boolean indeterminate) {
        throw new RuntimeException("Stub!");
    }

    public Drawable getIndeterminateDrawable() {
        throw new RuntimeException("Stub!");
    }

    public void setIndeterminateDrawable(Drawable d) {
        throw new RuntimeException("Stub!");
    }

    public Drawable getProgressDrawable() {
        throw new RuntimeException("Stub!");
    }

    public void setProgressDrawable(Drawable d) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected boolean verifyDrawable(Drawable who) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void jumpDrawablesToCurrentState() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void postInvalidate() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setProgress(int progress) {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setSecondaryProgress(int secondaryProgress) {
        throw new RuntimeException("Stub!");
    }

    @ViewDebug.ExportedProperty(category = "progress")
    public synchronized int getProgress() {
        throw new RuntimeException("Stub!");
    }

    @ViewDebug.ExportedProperty(category = "progress")
    public synchronized int getSecondaryProgress() {
        throw new RuntimeException("Stub!");
    }

    @ViewDebug.ExportedProperty(category = "progress")
    public synchronized int getMax() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setMax(int max) {
        throw new RuntimeException("Stub!");
    }

    public final synchronized void incrementProgressBy(int diff) {
        throw new RuntimeException("Stub!");
    }

    public final synchronized void incrementSecondaryProgressBy(int diff) {
        throw new RuntimeException("Stub!");
    }

    public void setInterpolator(Context context, int resID) {
        throw new RuntimeException("Stub!");
    }

    public void setInterpolator(Interpolator interpolator) {
        throw new RuntimeException("Stub!");
    }

    public Interpolator getInterpolator() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void setVisibility(int v) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onVisibilityChanged(View changedView, int visibility) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View, android.graphics.drawable.Drawable.Callback
    public void invalidateDrawable(Drawable dr) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected synchronized void onDraw(Canvas canvas) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void drawableStateChanged() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public Parcelable onSaveInstanceState() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void onRestoreInstanceState(Parcelable state) {
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
