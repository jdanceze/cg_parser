package android.widget;

import android.database.DataSetObserver;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/BaseExpandableListAdapter.class */
public abstract class BaseExpandableListAdapter implements ExpandableListAdapter, HeterogeneousExpandableList {
    public BaseExpandableListAdapter() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.ExpandableListAdapter
    public void registerDataSetObserver(DataSetObserver observer) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.ExpandableListAdapter
    public void unregisterDataSetObserver(DataSetObserver observer) {
        throw new RuntimeException("Stub!");
    }

    public void notifyDataSetInvalidated() {
        throw new RuntimeException("Stub!");
    }

    public void notifyDataSetChanged() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.ExpandableListAdapter
    public boolean areAllItemsEnabled() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.ExpandableListAdapter
    public void onGroupCollapsed(int groupPosition) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.ExpandableListAdapter
    public void onGroupExpanded(int groupPosition) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.ExpandableListAdapter
    public long getCombinedChildId(long groupId, long childId) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.ExpandableListAdapter
    public long getCombinedGroupId(long groupId) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.ExpandableListAdapter
    public boolean isEmpty() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.HeterogeneousExpandableList
    public int getChildType(int groupPosition, int childPosition) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.HeterogeneousExpandableList
    public int getChildTypeCount() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.HeterogeneousExpandableList
    public int getGroupType(int groupPosition) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.HeterogeneousExpandableList
    public int getGroupTypeCount() {
        throw new RuntimeException("Stub!");
    }
}
