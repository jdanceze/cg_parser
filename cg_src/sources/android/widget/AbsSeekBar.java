package android.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/AbsSeekBar.class */
public abstract class AbsSeekBar extends ProgressBar {
    public AbsSeekBar(Context context) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public AbsSeekBar(Context context, AttributeSet attrs) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public AbsSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public void setThumb(Drawable thumb) {
        throw new RuntimeException("Stub!");
    }

    public Drawable getThumb() {
        throw new RuntimeException("Stub!");
    }

    public int getThumbOffset() {
        throw new RuntimeException("Stub!");
    }

    public void setThumbOffset(int thumbOffset) {
        throw new RuntimeException("Stub!");
    }

    public void setKeyProgressIncrement(int increment) {
        throw new RuntimeException("Stub!");
    }

    public int getKeyProgressIncrement() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.ProgressBar
    public synchronized void setMax(int max) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.ProgressBar, android.view.View
    protected boolean verifyDrawable(Drawable who) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.ProgressBar, android.view.View
    public void jumpDrawablesToCurrentState() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.ProgressBar, android.view.View
    protected void drawableStateChanged() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.ProgressBar, android.view.View
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.ProgressBar, android.view.View
    protected synchronized void onDraw(Canvas canvas) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.ProgressBar, android.view.View
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.ProgressBar, android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.ProgressBar, android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public boolean performAccessibilityAction(int action, Bundle arguments) {
        throw new RuntimeException("Stub!");
    }
}
