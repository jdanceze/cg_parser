package android.widget;

import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/TabHost.class */
public class TabHost extends FrameLayout implements ViewTreeObserver.OnTouchModeChangeListener {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/TabHost$OnTabChangeListener.class */
    public interface OnTabChangeListener {
        void onTabChanged(String str);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/TabHost$TabContentFactory.class */
    public interface TabContentFactory {
        View createTabContent(String str);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/TabHost$TabSpec.class */
    public class TabSpec {
        TabSpec() {
            throw new RuntimeException("Stub!");
        }

        public TabSpec setIndicator(CharSequence label) {
            throw new RuntimeException("Stub!");
        }

        public TabSpec setIndicator(CharSequence label, Drawable icon) {
            throw new RuntimeException("Stub!");
        }

        public TabSpec setIndicator(View view) {
            throw new RuntimeException("Stub!");
        }

        public TabSpec setContent(int viewId) {
            throw new RuntimeException("Stub!");
        }

        public TabSpec setContent(TabContentFactory contentFactory) {
            throw new RuntimeException("Stub!");
        }

        public TabSpec setContent(Intent intent) {
            throw new RuntimeException("Stub!");
        }

        public String getTag() {
            throw new RuntimeException("Stub!");
        }
    }

    public TabHost(Context context) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public TabHost(Context context, AttributeSet attrs) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public TabSpec newTabSpec(String tag) {
        throw new RuntimeException("Stub!");
    }

    public void setup() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View, android.view.accessibility.AccessibilityEventSource
    public void sendAccessibilityEvent(int eventType) {
        throw new RuntimeException("Stub!");
    }

    public void setup(LocalActivityManager activityGroup) {
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

    @Override // android.view.ViewTreeObserver.OnTouchModeChangeListener
    public void onTouchModeChanged(boolean isInTouchMode) {
        throw new RuntimeException("Stub!");
    }

    public void addTab(TabSpec tabSpec) {
        throw new RuntimeException("Stub!");
    }

    public void clearAllTabs() {
        throw new RuntimeException("Stub!");
    }

    public TabWidget getTabWidget() {
        throw new RuntimeException("Stub!");
    }

    public int getCurrentTab() {
        throw new RuntimeException("Stub!");
    }

    public String getCurrentTabTag() {
        throw new RuntimeException("Stub!");
    }

    public View getCurrentTabView() {
        throw new RuntimeException("Stub!");
    }

    public View getCurrentView() {
        throw new RuntimeException("Stub!");
    }

    public void setCurrentTabByTag(String tag) {
        throw new RuntimeException("Stub!");
    }

    public FrameLayout getTabContentView() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEvent(KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.View
    public void dispatchWindowFocusChanged(boolean hasFocus) {
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

    public void setCurrentTab(int index) {
        throw new RuntimeException("Stub!");
    }

    public void setOnTabChangedListener(OnTabChangeListener l) {
        throw new RuntimeException("Stub!");
    }
}
