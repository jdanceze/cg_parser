package android.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/AdapterViewAnimator.class */
public abstract class AdapterViewAnimator extends AdapterView<Adapter> implements Advanceable {
    public AdapterViewAnimator(Context context) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public AdapterViewAnimator(Context context, AttributeSet attrs) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public AdapterViewAnimator(Context context, AttributeSet attrs, int defStyleAttr) {
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

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent ev) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AdapterView, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
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

    public View getCurrentView() {
        throw new RuntimeException("Stub!");
    }

    public ObjectAnimator getInAnimation() {
        throw new RuntimeException("Stub!");
    }

    public void setInAnimation(ObjectAnimator inAnimation) {
        throw new RuntimeException("Stub!");
    }

    public ObjectAnimator getOutAnimation() {
        throw new RuntimeException("Stub!");
    }

    public void setOutAnimation(ObjectAnimator outAnimation) {
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

    @Override // android.widget.AdapterView
    public Adapter getAdapter() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AdapterView
    public void setAdapter(Adapter adapter) {
        throw new RuntimeException("Stub!");
    }

    public void setRemoteViewsAdapter(Intent intent) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AdapterView
    public void setSelection(int position) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AdapterView
    public View getSelectedView() {
        throw new RuntimeException("Stub!");
    }

    public void deferNotifyDataSetChanged() {
        throw new RuntimeException("Stub!");
    }

    public boolean onRemoteAdapterConnected() {
        throw new RuntimeException("Stub!");
    }

    public void onRemoteAdapterDisconnected() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.Advanceable
    public void advance() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.Advanceable
    public void fyiWillBeAdvancedByHostKThx() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AdapterView, android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AdapterView, android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        throw new RuntimeException("Stub!");
    }
}
