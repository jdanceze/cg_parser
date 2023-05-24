package android.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/SlidingDrawer.class */
public class SlidingDrawer extends ViewGroup {
    public static final int ORIENTATION_HORIZONTAL = 0;
    public static final int ORIENTATION_VERTICAL = 1;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/SlidingDrawer$OnDrawerCloseListener.class */
    public interface OnDrawerCloseListener {
        void onDrawerClosed();
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/SlidingDrawer$OnDrawerOpenListener.class */
    public interface OnDrawerOpenListener {
        void onDrawerOpened();
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/SlidingDrawer$OnDrawerScrollListener.class */
    public interface OnDrawerScrollListener {
        void onScrollStarted();

        void onScrollEnded();
    }

    public SlidingDrawer(Context context, AttributeSet attrs) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public SlidingDrawer(Context context, AttributeSet attrs, int defStyle) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        throw new RuntimeException("Stub!");
    }

    public void toggle() {
        throw new RuntimeException("Stub!");
    }

    public void animateToggle() {
        throw new RuntimeException("Stub!");
    }

    public void open() {
        throw new RuntimeException("Stub!");
    }

    public void close() {
        throw new RuntimeException("Stub!");
    }

    public void animateClose() {
        throw new RuntimeException("Stub!");
    }

    public void animateOpen() {
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

    public void setOnDrawerOpenListener(OnDrawerOpenListener onDrawerOpenListener) {
        throw new RuntimeException("Stub!");
    }

    public void setOnDrawerCloseListener(OnDrawerCloseListener onDrawerCloseListener) {
        throw new RuntimeException("Stub!");
    }

    public void setOnDrawerScrollListener(OnDrawerScrollListener onDrawerScrollListener) {
        throw new RuntimeException("Stub!");
    }

    public View getHandle() {
        throw new RuntimeException("Stub!");
    }

    public View getContent() {
        throw new RuntimeException("Stub!");
    }

    public void unlock() {
        throw new RuntimeException("Stub!");
    }

    public void lock() {
        throw new RuntimeException("Stub!");
    }

    public boolean isOpened() {
        throw new RuntimeException("Stub!");
    }

    public boolean isMoving() {
        throw new RuntimeException("Stub!");
    }
}
