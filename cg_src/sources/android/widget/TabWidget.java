package android.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/TabWidget.class */
public class TabWidget extends LinearLayout implements View.OnFocusChangeListener {
    public TabWidget(Context context) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public TabWidget(Context context, AttributeSet attrs) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public TabWidget(Context context, AttributeSet attrs, int defStyle) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    protected int getChildDrawingOrder(int childCount, int i) {
        throw new RuntimeException("Stub!");
    }

    public View getChildTabViewAt(int index) {
        throw new RuntimeException("Stub!");
    }

    public int getTabCount() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.LinearLayout
    public void setDividerDrawable(Drawable drawable) {
        throw new RuntimeException("Stub!");
    }

    public void setDividerDrawable(int resId) {
        throw new RuntimeException("Stub!");
    }

    public void setLeftStripDrawable(Drawable drawable) {
        throw new RuntimeException("Stub!");
    }

    public void setLeftStripDrawable(int resId) {
        throw new RuntimeException("Stub!");
    }

    public void setRightStripDrawable(Drawable drawable) {
        throw new RuntimeException("Stub!");
    }

    public void setRightStripDrawable(int resId) {
        throw new RuntimeException("Stub!");
    }

    public void setStripEnabled(boolean stripEnabled) {
        throw new RuntimeException("Stub!");
    }

    public boolean isStripEnabled() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void childDrawableStateChanged(View child) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
        throw new RuntimeException("Stub!");
    }

    public void setCurrentTab(int index) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.LinearLayout, android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View, android.view.accessibility.AccessibilityEventSource
    public void sendAccessibilityEventUnchecked(AccessibilityEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.LinearLayout, android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        throw new RuntimeException("Stub!");
    }

    public void focusCurrentTab(int index) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void setEnabled(boolean enabled) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    public void addView(View child) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    public void removeAllViews() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View.OnFocusChangeListener
    public void onFocusChange(View v, boolean hasFocus) {
        throw new RuntimeException("Stub!");
    }
}
