package android.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/HorizontalScrollView.class */
public class HorizontalScrollView extends FrameLayout {
    public HorizontalScrollView(Context context) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public HorizontalScrollView(Context context, AttributeSet attrs) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public HorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected float getLeftFadingEdgeStrength() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected float getRightFadingEdgeStrength() {
        throw new RuntimeException("Stub!");
    }

    public int getMaxScrollAmount() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    public void addView(View child) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    public void addView(View child, int index) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.ViewManager
    public void addView(View child, ViewGroup.LayoutParams params) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        throw new RuntimeException("Stub!");
    }

    public boolean isFillViewport() {
        throw new RuntimeException("Stub!");
    }

    public void setFillViewport(boolean fillViewport) {
        throw new RuntimeException("Stub!");
    }

    public boolean isSmoothScrollingEnabled() {
        throw new RuntimeException("Stub!");
    }

    public void setSmoothScrollingEnabled(boolean smoothScrollingEnabled) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEvent(KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    public boolean executeKeyEvent(KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent ev) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public boolean onGenericMotionEvent(MotionEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public boolean shouldDelayChildPressedState() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public boolean performAccessibilityAction(int action, Bundle arguments) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.FrameLayout, android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.FrameLayout, android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        throw new RuntimeException("Stub!");
    }

    public boolean pageScroll(int direction) {
        throw new RuntimeException("Stub!");
    }

    public boolean fullScroll(int direction) {
        throw new RuntimeException("Stub!");
    }

    public boolean arrowScroll(int direction) {
        throw new RuntimeException("Stub!");
    }

    public final void smoothScrollBy(int dx, int dy) {
        throw new RuntimeException("Stub!");
    }

    public final void smoothScrollTo(int x, int y) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected int computeHorizontalScrollRange() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected int computeHorizontalScrollOffset() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void computeScroll() {
        throw new RuntimeException("Stub!");
    }

    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void requestChildFocus(View child, View focused) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean requestChildRectangleOnScreen(View child, Rect rectangle, boolean immediate) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View, android.view.ViewParent
    public void requestLayout() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        throw new RuntimeException("Stub!");
    }

    public void fling(int velocityX) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void scrollTo(int x, int y) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void setOverScrollMode(int mode) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.FrameLayout, android.view.View
    public void draw(Canvas canvas) {
        throw new RuntimeException("Stub!");
    }
}
