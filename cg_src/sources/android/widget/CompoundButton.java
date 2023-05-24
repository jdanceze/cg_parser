package android.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.ViewDebug;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/CompoundButton.class */
public abstract class CompoundButton extends Button implements Checkable {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/CompoundButton$OnCheckedChangeListener.class */
    public interface OnCheckedChangeListener {
        void onCheckedChanged(CompoundButton compoundButton, boolean z);
    }

    public CompoundButton(Context context) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public CompoundButton(Context context, AttributeSet attrs) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public CompoundButton(Context context, AttributeSet attrs, int defStyle) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.Checkable
    public void toggle() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public boolean performClick() {
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

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void setButtonDrawable(int resid) {
        throw new RuntimeException("Stub!");
    }

    public void setButtonDrawable(Drawable d) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.Button, android.widget.TextView, android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.Button, android.widget.TextView, android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
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
    protected boolean verifyDrawable(Drawable who) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.TextView, android.view.View
    public void jumpDrawablesToCurrentState() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.TextView, android.view.View
    public Parcelable onSaveInstanceState() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.TextView, android.view.View
    public void onRestoreInstanceState(Parcelable state) {
        throw new RuntimeException("Stub!");
    }
}
