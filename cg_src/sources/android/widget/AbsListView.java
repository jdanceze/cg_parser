package android.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.Filter;
import java.util.ArrayList;
import java.util.List;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/AbsListView.class */
public abstract class AbsListView extends AdapterView<ListAdapter> implements TextWatcher, ViewTreeObserver.OnGlobalLayoutListener, Filter.FilterListener, ViewTreeObserver.OnTouchModeChangeListener {
    public static final int TRANSCRIPT_MODE_DISABLED = 0;
    public static final int TRANSCRIPT_MODE_NORMAL = 1;
    public static final int TRANSCRIPT_MODE_ALWAYS_SCROLL = 2;
    public static final int CHOICE_MODE_NONE = 0;
    public static final int CHOICE_MODE_SINGLE = 1;
    public static final int CHOICE_MODE_MULTIPLE = 2;
    public static final int CHOICE_MODE_MULTIPLE_MODAL = 3;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/AbsListView$MultiChoiceModeListener.class */
    public interface MultiChoiceModeListener extends ActionMode.Callback {
        void onItemCheckedStateChanged(ActionMode actionMode, int i, long j, boolean z);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/AbsListView$OnScrollListener.class */
    public interface OnScrollListener {
        public static final int SCROLL_STATE_IDLE = 0;
        public static final int SCROLL_STATE_TOUCH_SCROLL = 1;
        public static final int SCROLL_STATE_FLING = 2;

        void onScrollStateChanged(AbsListView absListView, int i);

        void onScroll(AbsListView absListView, int i, int i2, int i3);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/AbsListView$RecyclerListener.class */
    public interface RecyclerListener {
        void onMovedToScrapHeap(View view);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/AbsListView$SelectionBoundsAdjuster.class */
    public interface SelectionBoundsAdjuster {
        void adjustListItemSelectionBounds(Rect rect);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/AbsListView$LayoutParams.class */
    public static class LayoutParams extends ViewGroup.LayoutParams {
        public LayoutParams(Context c, AttributeSet attrs) {
            super(null);
            throw new RuntimeException("Stub!");
        }

        public LayoutParams(int w, int h) {
            super(null);
            throw new RuntimeException("Stub!");
        }

        public LayoutParams(int w, int h, int viewType) {
            super(null);
            throw new RuntimeException("Stub!");
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(null);
            throw new RuntimeException("Stub!");
        }
    }

    public AbsListView(Context context) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public AbsListView(Context context, AttributeSet attrs) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public AbsListView(Context context, AttributeSet attrs, int defStyle) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void setOverScrollMode(int mode) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AdapterView
    public void setAdapter(ListAdapter adapter) {
        throw new RuntimeException("Stub!");
    }

    public int getCheckedItemCount() {
        throw new RuntimeException("Stub!");
    }

    public boolean isItemChecked(int position) {
        throw new RuntimeException("Stub!");
    }

    public int getCheckedItemPosition() {
        throw new RuntimeException("Stub!");
    }

    public SparseBooleanArray getCheckedItemPositions() {
        throw new RuntimeException("Stub!");
    }

    public long[] getCheckedItemIds() {
        throw new RuntimeException("Stub!");
    }

    public void clearChoices() {
        throw new RuntimeException("Stub!");
    }

    public void setItemChecked(int position, boolean value) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AdapterView
    public boolean performItemClick(View view, int position, long id) {
        throw new RuntimeException("Stub!");
    }

    public int getChoiceMode() {
        throw new RuntimeException("Stub!");
    }

    public void setChoiceMode(int choiceMode) {
        throw new RuntimeException("Stub!");
    }

    public void setMultiChoiceModeListener(MultiChoiceModeListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void setFastScrollEnabled(boolean enabled) {
        throw new RuntimeException("Stub!");
    }

    public void setFastScrollAlwaysVisible(boolean alwaysShow) {
        throw new RuntimeException("Stub!");
    }

    public boolean isFastScrollAlwaysVisible() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public int getVerticalScrollbarWidth() {
        throw new RuntimeException("Stub!");
    }

    @ViewDebug.ExportedProperty
    public boolean isFastScrollEnabled() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void setVerticalScrollbarPosition(int position) {
        throw new RuntimeException("Stub!");
    }

    public void setSmoothScrollbarEnabled(boolean enabled) {
        throw new RuntimeException("Stub!");
    }

    @ViewDebug.ExportedProperty
    public boolean isSmoothScrollbarEnabled() {
        throw new RuntimeException("Stub!");
    }

    public void setOnScrollListener(OnScrollListener l) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.View
    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public View focusSearch(int direction) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public View focusSearch(View focused, int direction) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View, android.view.accessibility.AccessibilityEventSource
    public void sendAccessibilityEvent(int eventType) {
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

    @Override // android.view.View
    public boolean performAccessibilityAction(int action, Bundle arguments) {
        throw new RuntimeException("Stub!");
    }

    @ViewDebug.ExportedProperty
    public boolean isScrollingCacheEnabled() {
        throw new RuntimeException("Stub!");
    }

    public void setScrollingCacheEnabled(boolean enabled) {
        throw new RuntimeException("Stub!");
    }

    public void setTextFilterEnabled(boolean textFilterEnabled) {
        throw new RuntimeException("Stub!");
    }

    @ViewDebug.ExportedProperty
    public boolean isTextFilterEnabled() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void getFocusedRect(Rect r) {
        throw new RuntimeException("Stub!");
    }

    @ViewDebug.ExportedProperty
    public boolean isStackFromBottom() {
        throw new RuntimeException("Stub!");
    }

    public void setStackFromBottom(boolean stackFromBottom) {
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

    public void setFilterText(String filterText) {
        throw new RuntimeException("Stub!");
    }

    public CharSequence getTextFilter() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View, android.view.ViewParent
    public void requestLayout() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected int computeVerticalScrollExtent() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected int computeVerticalScrollOffset() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected int computeVerticalScrollRange() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected float getTopFadingEdgeStrength() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected float getBottomFadingEdgeStrength() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AdapterView, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        throw new RuntimeException("Stub!");
    }

    protected void layoutChildren() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AdapterView
    @ViewDebug.ExportedProperty
    public View getSelectedView() {
        throw new RuntimeException("Stub!");
    }

    public int getListPaddingTop() {
        throw new RuntimeException("Stub!");
    }

    public int getListPaddingBottom() {
        throw new RuntimeException("Stub!");
    }

    public int getListPaddingLeft() {
        throw new RuntimeException("Stub!");
    }

    public int getListPaddingRight() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected boolean isPaddingOffsetRequired() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected int getLeftPaddingOffset() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected int getTopPaddingOffset() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected int getRightPaddingOffset() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected int getBottomPaddingOffset() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        throw new RuntimeException("Stub!");
    }

    public void setDrawSelectorOnTop(boolean onTop) {
        throw new RuntimeException("Stub!");
    }

    public void setSelector(int resID) {
        throw new RuntimeException("Stub!");
    }

    public void setSelector(Drawable sel) {
        throw new RuntimeException("Stub!");
    }

    public Drawable getSelector() {
        throw new RuntimeException("Stub!");
    }

    public void setScrollIndicators(View up, View down) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void drawableStateChanged() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.View
    protected int[] onCreateDrawableState(int extraSpace) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public boolean verifyDrawable(Drawable dr) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.View
    public void jumpDrawablesToCurrentState() {
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
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected ContextMenu.ContextMenuInfo getContextMenuInfo() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean showContextMenuForChild(View originalView) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchSetPressed(boolean pressed) {
        throw new RuntimeException("Stub!");
    }

    public int pointToPosition(int x, int y) {
        throw new RuntimeException("Stub!");
    }

    public long pointToRowId(int x, int y) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewTreeObserver.OnTouchModeChangeListener
    public void onTouchModeChanged(boolean isInTouchMode) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent ev) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public boolean onGenericMotionEvent(MotionEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.View
    public void addTouchables(ArrayList<View> views) {
        throw new RuntimeException("Stub!");
    }

    public void setFriction(float friction) {
        throw new RuntimeException("Stub!");
    }

    public void setVelocityScale(float scale) {
        throw new RuntimeException("Stub!");
    }

    public void smoothScrollToPosition(int position) {
        throw new RuntimeException("Stub!");
    }

    public void smoothScrollToPositionFromTop(int position, int offset, int duration) {
        throw new RuntimeException("Stub!");
    }

    public void smoothScrollToPositionFromTop(int position, int offset) {
        throw new RuntimeException("Stub!");
    }

    public void smoothScrollToPosition(int position, int boundPosition) {
        throw new RuntimeException("Stub!");
    }

    public void smoothScrollBy(int distance, int duration) {
        throw new RuntimeException("Stub!");
    }

    public void invalidateViews() {
        throw new RuntimeException("Stub!");
    }

    protected void handleDataChanged() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onDisplayHint(int hint) {
        throw new RuntimeException("Stub!");
    }

    protected boolean isInFilterMode() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public boolean checkInputConnectionProxy(View view) {
        throw new RuntimeException("Stub!");
    }

    public void clearTextFilter() {
        throw new RuntimeException("Stub!");
    }

    public boolean hasTextFilter() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
    public void onGlobalLayout() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable s) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.Filter.FilterListener
    public void onFilterComplete(int count) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        throw new RuntimeException("Stub!");
    }

    public void setTranscriptMode(int mode) {
        throw new RuntimeException("Stub!");
    }

    public int getTranscriptMode() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public int getSolidColor() {
        throw new RuntimeException("Stub!");
    }

    public void setCacheColorHint(int color) {
        throw new RuntimeException("Stub!");
    }

    @ViewDebug.ExportedProperty(category = "drawing")
    public int getCacheColorHint() {
        throw new RuntimeException("Stub!");
    }

    public void reclaimViews(List<View> views) {
        throw new RuntimeException("Stub!");
    }

    public void setRemoteViewsAdapter(Intent intent) {
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

    public void setRecyclerListener(RecyclerListener listener) {
        throw new RuntimeException("Stub!");
    }
}
