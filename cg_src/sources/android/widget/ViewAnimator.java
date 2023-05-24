package android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.Animation;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/ViewAnimator.class */
public class ViewAnimator extends FrameLayout {
    public ViewAnimator(Context context) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public ViewAnimator(Context context, AttributeSet attrs) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public void setDisplayedChild(int whichChild) {
        throw new RuntimeException("Stub!");
    }

    public int getDisplayedChild() {
        throw new RuntimeException("Stub!");
    }

    public void showNext() {
        throw new RuntimeException("Stub!");
    }

    public void showPrevious() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    public void removeAllViews() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.ViewManager
    public void removeView(View view) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    public void removeViewAt(int index) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    public void removeViewInLayout(View view) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    public void removeViews(int start, int count) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    public void removeViewsInLayout(int start, int count) {
        throw new RuntimeException("Stub!");
    }

    public View getCurrentView() {
        throw new RuntimeException("Stub!");
    }

    public Animation getInAnimation() {
        throw new RuntimeException("Stub!");
    }

    public void setInAnimation(Animation inAnimation) {
        throw new RuntimeException("Stub!");
    }

    public Animation getOutAnimation() {
        throw new RuntimeException("Stub!");
    }

    public void setOutAnimation(Animation outAnimation) {
        throw new RuntimeException("Stub!");
    }

    public void setInAnimation(Context context, int resourceID) {
        throw new RuntimeException("Stub!");
    }

    public void setOutAnimation(Context context, int resourceID) {
        throw new RuntimeException("Stub!");
    }

    public void setAnimateFirstView(boolean animate) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public int getBaseline() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.FrameLayout, android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.FrameLayout, android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        throw new RuntimeException("Stub!");
    }
}
