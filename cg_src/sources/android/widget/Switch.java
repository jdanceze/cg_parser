package android.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/Switch.class */
public class Switch extends CompoundButton {
    public Switch(Context context) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public Switch(Context context, AttributeSet attrs) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public Switch(Context context, AttributeSet attrs, int defStyle) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public void setSwitchTextAppearance(Context context, int resid) {
        throw new RuntimeException("Stub!");
    }

    public void setSwitchTypeface(Typeface tf, int style) {
        throw new RuntimeException("Stub!");
    }

    public void setSwitchTypeface(Typeface tf) {
        throw new RuntimeException("Stub!");
    }

    public void setSwitchPadding(int pixels) {
        throw new RuntimeException("Stub!");
    }

    public int getSwitchPadding() {
        throw new RuntimeException("Stub!");
    }

    public void setSwitchMinWidth(int pixels) {
        throw new RuntimeException("Stub!");
    }

    public int getSwitchMinWidth() {
        throw new RuntimeException("Stub!");
    }

    public void setThumbTextPadding(int pixels) {
        throw new RuntimeException("Stub!");
    }

    public int getThumbTextPadding() {
        throw new RuntimeException("Stub!");
    }

    public void setTrackDrawable(Drawable track) {
        throw new RuntimeException("Stub!");
    }

    public void setTrackResource(int resId) {
        throw new RuntimeException("Stub!");
    }

    public Drawable getTrackDrawable() {
        throw new RuntimeException("Stub!");
    }

    public void setThumbDrawable(Drawable thumb) {
        throw new RuntimeException("Stub!");
    }

    public void setThumbResource(int resId) {
        throw new RuntimeException("Stub!");
    }

    public Drawable getThumbDrawable() {
        throw new RuntimeException("Stub!");
    }

    public CharSequence getTextOn() {
        throw new RuntimeException("Stub!");
    }

    public void setTextOn(CharSequence textOn) {
        throw new RuntimeException("Stub!");
    }

    public CharSequence getTextOff() {
        throw new RuntimeException("Stub!");
    }

    public void setTextOff(CharSequence textOff) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.TextView, android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.TextView, android.view.View
    public void onPopulateAccessibilityEvent(AccessibilityEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.TextView, android.view.View
    public boolean onTouchEvent(MotionEvent ev) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.CompoundButton, android.widget.Checkable
    public void setChecked(boolean checked) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.TextView, android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    protected void onDraw(Canvas canvas) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.TextView
    public int getCompoundPaddingRight() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    protected int[] onCreateDrawableState(int extraSpace) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    protected void drawableStateChanged() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    protected boolean verifyDrawable(Drawable who) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    public void jumpDrawablesToCurrentState() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.CompoundButton, android.widget.Button, android.widget.TextView, android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.CompoundButton, android.widget.Button, android.widget.TextView, android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        throw new RuntimeException("Stub!");
    }
}
