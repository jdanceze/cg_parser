package android.widget;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.ContextMenu;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Adapter;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/AdapterView.class */
public abstract class AdapterView<T extends Adapter> extends ViewGroup {
    public static final int ITEM_VIEW_TYPE_IGNORE = -1;
    public static final int ITEM_VIEW_TYPE_HEADER_OR_FOOTER = -2;
    public static final int INVALID_POSITION = -1;
    public static final long INVALID_ROW_ID = Long.MIN_VALUE;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/AdapterView$OnItemClickListener.class */
    public interface OnItemClickListener {
        void onItemClick(AdapterView<?> adapterView, View view, int i, long j);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/AdapterView$OnItemLongClickListener.class */
    public interface OnItemLongClickListener {
        boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/AdapterView$OnItemSelectedListener.class */
    public interface OnItemSelectedListener {
        void onItemSelected(AdapterView<?> adapterView, View view, int i, long j);

        void onNothingSelected(AdapterView<?> adapterView);
    }

    public abstract T getAdapter();

    public abstract void setAdapter(T t);

    public abstract View getSelectedView();

    public abstract void setSelection(int i);

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/AdapterView$AdapterContextMenuInfo.class */
    public static class AdapterContextMenuInfo implements ContextMenu.ContextMenuInfo {
        public View targetView;
        public int position;
        public long id;

        public AdapterContextMenuInfo(View targetView, int position, long id) {
            throw new RuntimeException("Stub!");
        }
    }

    public AdapterView(Context context) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public AdapterView(Context context, AttributeSet attrs) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public AdapterView(Context context, AttributeSet attrs, int defStyle) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        throw new RuntimeException("Stub!");
    }

    public final OnItemClickListener getOnItemClickListener() {
        throw new RuntimeException("Stub!");
    }

    public boolean performItemClick(View view, int position, long id) {
        throw new RuntimeException("Stub!");
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        throw new RuntimeException("Stub!");
    }

    public final OnItemLongClickListener getOnItemLongClickListener() {
        throw new RuntimeException("Stub!");
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        throw new RuntimeException("Stub!");
    }

    public final OnItemSelectedListener getOnItemSelectedListener() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    public void addView(View child) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    public void addView(View child, int index) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.ViewManager
    public void addView(View child, ViewGroup.LayoutParams params) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.ViewManager
    public void removeView(View child) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    public void removeViewAt(int index) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    public void removeAllViews() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        throw new RuntimeException("Stub!");
    }

    @ViewDebug.CapturedViewProperty
    public int getSelectedItemPosition() {
        throw new RuntimeException("Stub!");
    }

    @ViewDebug.CapturedViewProperty
    public long getSelectedItemId() {
        throw new RuntimeException("Stub!");
    }

    public Object getSelectedItem() {
        throw new RuntimeException("Stub!");
    }

    @ViewDebug.CapturedViewProperty
    public int getCount() {
        throw new RuntimeException("Stub!");
    }

    public int getPositionForView(View view) {
        throw new RuntimeException("Stub!");
    }

    public int getFirstVisiblePosition() {
        throw new RuntimeException("Stub!");
    }

    public int getLastVisiblePosition() {
        throw new RuntimeException("Stub!");
    }

    public void setEmptyView(View emptyView) {
        throw new RuntimeException("Stub!");
    }

    public View getEmptyView() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void setFocusable(boolean focusable) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void setFocusableInTouchMode(boolean focusable) {
        throw new RuntimeException("Stub!");
    }

    public Object getItemAtPosition(int position) {
        throw new RuntimeException("Stub!");
    }

    public long getItemIdAtPosition(int position) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void setOnClickListener(View.OnClickListener l) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    public boolean onRequestSendAccessibilityEvent(View child, AccessibilityEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    protected boolean canAnimate() {
        throw new RuntimeException("Stub!");
    }
}
