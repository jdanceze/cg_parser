package android.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.RemoteViews;
@RemoteViews.RemoteView
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/GridView.class */
public class GridView extends AbsListView {
    public static final int NO_STRETCH = 0;
    public static final int STRETCH_SPACING = 1;
    public static final int STRETCH_COLUMN_WIDTH = 2;
    public static final int STRETCH_SPACING_UNIFORM = 3;
    public static final int AUTO_FIT = -1;

    public GridView(Context context) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public GridView(Context context, AttributeSet attrs) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public GridView(Context context, AttributeSet attrs, int defStyle) {
        super(null, null, 0);
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

    @Override // android.widget.AbsListView
    public void smoothScrollToPosition(int position) {
        throw new RuntimeException("Stub!");
    }

    public void smoothScrollByOffset(int offset) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AbsListView, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    protected void attachLayoutAnimationParameters(View child, ViewGroup.LayoutParams params, int index, int count) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AbsListView
    protected void layoutChildren() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AdapterView
    public void setSelection(int position) {
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

    @Override // android.widget.AbsListView, android.view.View
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        throw new RuntimeException("Stub!");
    }

    public void setGravity(int gravity) {
        throw new RuntimeException("Stub!");
    }

    public int getGravity() {
        throw new RuntimeException("Stub!");
    }

    public void setHorizontalSpacing(int horizontalSpacing) {
        throw new RuntimeException("Stub!");
    }

    public int getHorizontalSpacing() {
        throw new RuntimeException("Stub!");
    }

    public int getRequestedHorizontalSpacing() {
        throw new RuntimeException("Stub!");
    }

    public void setVerticalSpacing(int verticalSpacing) {
        throw new RuntimeException("Stub!");
    }

    public int getVerticalSpacing() {
        throw new RuntimeException("Stub!");
    }

    public void setStretchMode(int stretchMode) {
        throw new RuntimeException("Stub!");
    }

    public int getStretchMode() {
        throw new RuntimeException("Stub!");
    }

    public void setColumnWidth(int columnWidth) {
        throw new RuntimeException("Stub!");
    }

    public int getColumnWidth() {
        throw new RuntimeException("Stub!");
    }

    public int getRequestedColumnWidth() {
        throw new RuntimeException("Stub!");
    }

    public void setNumColumns(int numColumns) {
        throw new RuntimeException("Stub!");
    }

    @ViewDebug.ExportedProperty
    public int getNumColumns() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AbsListView, android.view.View
    protected int computeVerticalScrollExtent() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AbsListView, android.view.View
    protected int computeVerticalScrollOffset() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AbsListView, android.view.View
    protected int computeVerticalScrollRange() {
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
