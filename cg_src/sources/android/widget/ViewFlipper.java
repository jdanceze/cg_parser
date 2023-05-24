package android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.RemoteViews;
@RemoteViews.RemoteView
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/ViewFlipper.class */
public class ViewFlipper extends ViewAnimator {
    public ViewFlipper(Context context) {
        super(null, null);
        throw new RuntimeException("Stub!");
    }

    public ViewFlipper(Context context, AttributeSet attrs) {
        super(null, null);
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onAttachedToWindow() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onWindowVisibilityChanged(int visibility) {
        throw new RuntimeException("Stub!");
    }

    public void setFlipInterval(int milliseconds) {
        throw new RuntimeException("Stub!");
    }

    public void startFlipping() {
        throw new RuntimeException("Stub!");
    }

    public void stopFlipping() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.ViewAnimator, android.widget.FrameLayout, android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.ViewAnimator, android.widget.FrameLayout, android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
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
}
