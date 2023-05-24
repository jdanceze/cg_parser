package android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.RemoteViews;
@RemoteViews.RemoteView
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/AdapterViewFlipper.class */
public class AdapterViewFlipper extends AdapterViewAnimator {
    public AdapterViewFlipper(Context context) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public AdapterViewFlipper(Context context, AttributeSet attrs) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onAttachedToWindow() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AdapterView, android.view.View
    protected void onDetachedFromWindow() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onWindowVisibilityChanged(int visibility) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AdapterViewAnimator, android.widget.AdapterView
    public void setAdapter(Adapter adapter) {
        throw new RuntimeException("Stub!");
    }

    public int getFlipInterval() {
        throw new RuntimeException("Stub!");
    }

    public void setFlipInterval(int flipInterval) {
        throw new RuntimeException("Stub!");
    }

    public void startFlipping() {
        throw new RuntimeException("Stub!");
    }

    public void stopFlipping() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AdapterViewAnimator
    public void showNext() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AdapterViewAnimator
    public void showPrevious() {
        throw new RuntimeException("Stub!");
    }

    public boolean isFlipping() {
        throw new RuntimeException("Stub!");
    }

    public void setAutoStart(boolean autoStart) {
        throw new RuntimeException("Stub!");
    }

    public boolean isAutoStart() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AdapterViewAnimator, android.widget.Advanceable
    public void fyiWillBeAdvancedByHostKThx() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AdapterViewAnimator, android.widget.AdapterView, android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AdapterViewAnimator, android.widget.AdapterView, android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        throw new RuntimeException("Stub!");
    }
}
