package android.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.AdapterView;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/ExpandableListView.class */
public class ExpandableListView extends ListView {
    public static final int PACKED_POSITION_TYPE_GROUP = 0;
    public static final int PACKED_POSITION_TYPE_CHILD = 1;
    public static final int PACKED_POSITION_TYPE_NULL = 2;
    public static final long PACKED_POSITION_VALUE_NULL = 4294967295L;
    public static final int CHILD_INDICATOR_INHERIT = -1;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/ExpandableListView$OnChildClickListener.class */
    public interface OnChildClickListener {
        boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i2, long j);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/ExpandableListView$OnGroupClickListener.class */
    public interface OnGroupClickListener {
        boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long j);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/ExpandableListView$OnGroupCollapseListener.class */
    public interface OnGroupCollapseListener {
        void onGroupCollapse(int i);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/ExpandableListView$OnGroupExpandListener.class */
    public interface OnGroupExpandListener {
        void onGroupExpand(int i);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/ExpandableListView$ExpandableListContextMenuInfo.class */
    public static class ExpandableListContextMenuInfo implements ContextMenu.ContextMenuInfo {
        public View targetView;
        public long packedPosition;
        public long id;

        public ExpandableListContextMenuInfo(View targetView, long packedPosition, long id) {
            throw new RuntimeException("Stub!");
        }
    }

    public ExpandableListView(Context context) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public ExpandableListView(Context context, AttributeSet attrs) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public ExpandableListView(Context context, AttributeSet attrs, int defStyle) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.ListView, android.widget.AbsListView, android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        throw new RuntimeException("Stub!");
    }

    public void setChildDivider(Drawable childDivider) {
        throw new RuntimeException("Stub!");
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.widget.ListView, android.widget.AbsListView, android.widget.AdapterView
    public void setAdapter(ListAdapter adapter) {
        throw new RuntimeException("Stub!");
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.widget.ListView, android.widget.AdapterView
    public ListAdapter getAdapter() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AdapterView
    public void setOnItemClickListener(AdapterView.OnItemClickListener l) {
        throw new RuntimeException("Stub!");
    }

    public void setAdapter(ExpandableListAdapter adapter) {
        throw new RuntimeException("Stub!");
    }

    public ExpandableListAdapter getExpandableListAdapter() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AbsListView, android.widget.AdapterView
    public boolean performItemClick(View v, int position, long id) {
        throw new RuntimeException("Stub!");
    }

    public boolean expandGroup(int groupPos) {
        throw new RuntimeException("Stub!");
    }

    public boolean expandGroup(int groupPos, boolean animate) {
        throw new RuntimeException("Stub!");
    }

    public boolean collapseGroup(int groupPos) {
        throw new RuntimeException("Stub!");
    }

    public void setOnGroupCollapseListener(OnGroupCollapseListener onGroupCollapseListener) {
        throw new RuntimeException("Stub!");
    }

    public void setOnGroupExpandListener(OnGroupExpandListener onGroupExpandListener) {
        throw new RuntimeException("Stub!");
    }

    public void setOnGroupClickListener(OnGroupClickListener onGroupClickListener) {
        throw new RuntimeException("Stub!");
    }

    public void setOnChildClickListener(OnChildClickListener onChildClickListener) {
        throw new RuntimeException("Stub!");
    }

    public long getExpandableListPosition(int flatListPosition) {
        throw new RuntimeException("Stub!");
    }

    public int getFlatListPosition(long packedPosition) {
        throw new RuntimeException("Stub!");
    }

    public long getSelectedPosition() {
        throw new RuntimeException("Stub!");
    }

    public long getSelectedId() {
        throw new RuntimeException("Stub!");
    }

    public void setSelectedGroup(int groupPosition) {
        throw new RuntimeException("Stub!");
    }

    public boolean setSelectedChild(int groupPosition, int childPosition, boolean shouldExpandGroup) {
        throw new RuntimeException("Stub!");
    }

    public boolean isGroupExpanded(int groupPosition) {
        throw new RuntimeException("Stub!");
    }

    public static int getPackedPositionType(long packedPosition) {
        throw new RuntimeException("Stub!");
    }

    public static int getPackedPositionGroup(long packedPosition) {
        throw new RuntimeException("Stub!");
    }

    public static int getPackedPositionChild(long packedPosition) {
        throw new RuntimeException("Stub!");
    }

    public static long getPackedPositionForChild(int groupPosition, int childPosition) {
        throw new RuntimeException("Stub!");
    }

    public static long getPackedPositionForGroup(int groupPosition) {
        throw new RuntimeException("Stub!");
    }

    public void setChildIndicator(Drawable childIndicator) {
        throw new RuntimeException("Stub!");
    }

    public void setChildIndicatorBounds(int left, int right) {
        throw new RuntimeException("Stub!");
    }

    public void setGroupIndicator(Drawable groupIndicator) {
        throw new RuntimeException("Stub!");
    }

    public void setIndicatorBounds(int left, int right) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AbsListView, android.view.View
    public Parcelable onSaveInstanceState() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AbsListView, android.view.View
    public void onRestoreInstanceState(Parcelable state) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.ListView, android.widget.AbsListView, android.widget.AdapterView, android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.ListView, android.widget.AbsListView, android.widget.AdapterView, android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        throw new RuntimeException("Stub!");
    }
}
