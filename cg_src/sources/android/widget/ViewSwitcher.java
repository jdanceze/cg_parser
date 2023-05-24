package android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/ViewSwitcher.class */
public class ViewSwitcher extends ViewAnimator {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/ViewSwitcher$ViewFactory.class */
    public interface ViewFactory {
        View makeView();
    }

    public ViewSwitcher(Context context) {
        super(null, null);
        throw new RuntimeException("Stub!");
    }

    public ViewSwitcher(Context context, AttributeSet attrs) {
        super(null, null);
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.ViewAnimator, android.view.ViewGroup
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
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

    public View getNextView() {
        throw new RuntimeException("Stub!");
    }

    public void setFactory(ViewFactory factory) {
        throw new RuntimeException("Stub!");
    }

    public void reset() {
        throw new RuntimeException("Stub!");
    }
}
