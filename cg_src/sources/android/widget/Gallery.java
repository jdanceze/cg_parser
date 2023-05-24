package android.widget;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.Transformation;
@Deprecated
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/Gallery.class */
public class Gallery extends AbsSpinner implements GestureDetector.OnGestureListener {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/Gallery$LayoutParams.class */
    public static class LayoutParams extends ViewGroup.LayoutParams {
        public LayoutParams(Context c, AttributeSet attrs) {
            super(null);
            throw new RuntimeException("Stub!");
        }

        public LayoutParams(int w, int h) {
            super(null);
            throw new RuntimeException("Stub!");
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(null);
            throw new RuntimeException("Stub!");
        }
    }

    public Gallery(Context context) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public Gallery(Context context, AttributeSet attrs) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public Gallery(Context context, AttributeSet attrs, int defStyle) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public void setCallbackDuringFling(boolean shouldCallback) {
        throw new RuntimeException("Stub!");
    }

    public void setAnimationDuration(int animationDurationMillis) {
        throw new RuntimeException("Stub!");
    }

    public void setSpacing(int spacing) {
        throw new RuntimeException("Stub!");
    }

    public void setUnselectedAlpha(float unselectedAlpha) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    protected boolean getChildStaticTransformation(View child, Transformation t) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected int computeHorizontalScrollExtent() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected int computeHorizontalScrollOffset() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected int computeHorizontalScrollRange() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AbsSpinner, android.view.ViewGroup
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AdapterView, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onSingleTapUp(MotionEvent e) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onDown(MotionEvent e) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public void onLongPress(MotionEvent e) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public void onShowPress(MotionEvent e) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.View
    public void dispatchSetSelected(boolean selected) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchSetPressed(boolean pressed) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected ContextMenu.ContextMenuInfo getContextMenuInfo() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean showContextMenuForChild(View originalView) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public boolean showContextMenu() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEvent(KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    public void setGravity(int gravity) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    protected int getChildDrawingOrder(int childCount, int i) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AbsSpinner, android.widget.AdapterView, android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AbsSpinner, android.widget.AdapterView, android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public boolean performAccessibilityAction(int action, Bundle arguments) {
        throw new RuntimeException("Stub!");
    }
}
