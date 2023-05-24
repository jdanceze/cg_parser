package android.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewDebug;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/CheckedTextView.class */
public class CheckedTextView extends TextView implements Checkable {
    public CheckedTextView(Context context) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public CheckedTextView(Context context, AttributeSet attrs) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public CheckedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.Checkable
    public void toggle() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.Checkable
    @ViewDebug.ExportedProperty
    public boolean isChecked() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.Checkable
    public void setChecked(boolean checked) {
        throw new RuntimeException("Stub!");
    }

    public void setCheckMarkDrawable(int resid) {
        throw new RuntimeException("Stub!");
    }

    public void setCheckMarkDrawable(Drawable d) {
        throw new RuntimeException("Stub!");
    }

    public Drawable getCheckMarkDrawable() {
        throw new RuntimeException("Stub!");
    }

    public void onPaddingChanged(int layoutDirection) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.TextView, android.view.View
    public void setPadding(int left, int top, int right, int bottom) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.TextView
    public void setPaddingRelative(int start, int top, int end, int bottom) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.TextView, android.view.View
    protected void onDraw(Canvas canvas) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.TextView, android.view.View
    protected int[] onCreateDrawableState(int extraSpace) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.TextView, android.view.View
    protected void drawableStateChanged() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.TextView, android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.TextView, android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        throw new RuntimeException("Stub!");
    }
}
