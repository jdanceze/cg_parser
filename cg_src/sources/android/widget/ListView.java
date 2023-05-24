package android.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.RemoteViews;
@RemoteViews.RemoteView
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/ListView.class */
public class ListView extends AbsListView {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/ListView$FixedViewInfo.class */
    public class FixedViewInfo {
        public View view;
        public Object data;
        public boolean isSelectable;

        public FixedViewInfo() {
            throw new RuntimeException("Stub!");
        }
    }

    public ListView(Context context) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public ListView(Context context, AttributeSet attrs) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public ListView(Context context, AttributeSet attrs, int defStyle) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public int getMaxScrollAmount() {
        throw new RuntimeException("Stub!");
    }

    public void addHeaderView(View v, Object data, boolean isSelectable) {
        throw new RuntimeException("Stub!");
    }

    public void addHeaderView(View v) {
        throw new RuntimeException("Stub!");
    }

    public int getHeaderViewsCount() {
        throw new RuntimeException("Stub!");
    }

    public boolean removeHeaderView(View v) {
        throw new RuntimeException("Stub!");
    }

    public void addFooterView(View v, Object data, boolean isSelectable) {
        throw new RuntimeException("Stub!");
    }

    public void addFooterView(View v) {
        throw new RuntimeException("Stub!");
    }

    public int getFooterViewsCount() {
        throw new RuntimeException("Stub!");
    }

    public boolean removeFooterView(View v) {
        throw new RuntimeException("Stub!");
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.widget.AdapterView
    public ListAdapter getAdapter() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AbsListView
    public void setRemoteViewsAdapter(Intent intent) {
        throw new RuntimeException("Stub!");
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.widget.AbsListView, android.widget.AdapterView
    public void setAdapter(ListAdapter adapter) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean requestChildRectangleOnScreen(View child, Rect rect, boolean immediate) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AbsListView
    public void smoothScrollToPosition(int position) {
        throw new RuntimeException("Stub!");
    }

    public void smoothScrollByOffset(int offset) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AbsListView, android.view.View
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AbsListView, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AbsListView
    protected void layoutChildren() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AdapterView, android.view.ViewGroup
    protected boolean canAnimate() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AdapterView
    public void setSelection(int position) {
        throw new RuntimeException("Stub!");
    }

    public void setSelectionFromTop(int position, int y) {
        throw new RuntimeException("Stub!");
    }

    public void setSelectionAfterHeaderView() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEvent(KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AbsListView, android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AbsListView, android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    public void setItemsCanFocus(boolean itemsCanFocus) {
        throw new RuntimeException("Stub!");
    }

    public boolean getItemsCanFocus() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public boolean isOpaque() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AbsListView
    public void setCacheColorHint(int color) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AbsListView, android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        throw new RuntimeException("Stub!");
    }

    public Drawable getDivider() {
        throw new RuntimeException("Stub!");
    }

    public void setDivider(Drawable divider) {
        throw new RuntimeException("Stub!");
    }

    public int getDividerHeight() {
        throw new RuntimeException("Stub!");
    }

    public void setDividerHeight(int height) {
        throw new RuntimeException("Stub!");
    }

    public void setHeaderDividersEnabled(boolean headerDividersEnabled) {
        throw new RuntimeException("Stub!");
    }

    public void setFooterDividersEnabled(boolean footerDividersEnabled) {
        throw new RuntimeException("Stub!");
    }

    public void setOverscrollHeader(Drawable header) {
        throw new RuntimeException("Stub!");
    }

    public Drawable getOverscrollHeader() {
        throw new RuntimeException("Stub!");
    }

    public void setOverscrollFooter(Drawable footer) {
        throw new RuntimeException("Stub!");
    }

    public Drawable getOverscrollFooter() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AbsListView, android.view.View
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        throw new RuntimeException("Stub!");
    }

    protected View findViewTraversal(int id) {
        throw new RuntimeException("Stub!");
    }

    protected View findViewWithTagTraversal(Object tag) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public long[] getCheckItemIds() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AbsListView, android.widget.AdapterView, android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AbsListView, android.widget.AdapterView, android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        throw new RuntimeException("Stub!");
    }
}
