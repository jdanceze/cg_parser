package android.widget;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/CursorTreeAdapter.class */
public abstract class CursorTreeAdapter extends BaseExpandableListAdapter implements Filterable {
    protected abstract Cursor getChildrenCursor(Cursor cursor);

    protected abstract View newGroupView(Context context, Cursor cursor, boolean z, ViewGroup viewGroup);

    protected abstract void bindGroupView(View view, Context context, Cursor cursor, boolean z);

    protected abstract View newChildView(Context context, Cursor cursor, boolean z, ViewGroup viewGroup);

    protected abstract void bindChildView(View view, Context context, Cursor cursor, boolean z);

    public CursorTreeAdapter(Cursor cursor, Context context) {
        throw new RuntimeException("Stub!");
    }

    public CursorTreeAdapter(Cursor cursor, Context context, boolean autoRequery) {
        throw new RuntimeException("Stub!");
    }

    public void setGroupCursor(Cursor cursor) {
        throw new RuntimeException("Stub!");
    }

    public void setChildrenCursor(int groupPosition, Cursor childrenCursor) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.ExpandableListAdapter
    public Cursor getChild(int groupPosition, int childPosition) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.ExpandableListAdapter
    public long getChildId(int groupPosition, int childPosition) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.ExpandableListAdapter
    public int getChildrenCount(int groupPosition) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.ExpandableListAdapter
    public Cursor getGroup(int groupPosition) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.ExpandableListAdapter
    public int getGroupCount() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.ExpandableListAdapter
    public long getGroupId(int groupPosition) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.ExpandableListAdapter
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.ExpandableListAdapter
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.ExpandableListAdapter
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.ExpandableListAdapter
    public boolean hasStableIds() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.BaseExpandableListAdapter
    public void notifyDataSetChanged() {
        throw new RuntimeException("Stub!");
    }

    public void notifyDataSetChanged(boolean releaseCursors) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.BaseExpandableListAdapter
    public void notifyDataSetInvalidated() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.BaseExpandableListAdapter, android.widget.ExpandableListAdapter
    public void onGroupCollapsed(int groupPosition) {
        throw new RuntimeException("Stub!");
    }

    public String convertToString(Cursor cursor) {
        throw new RuntimeException("Stub!");
    }

    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.Filterable
    public Filter getFilter() {
        throw new RuntimeException("Stub!");
    }

    public FilterQueryProvider getFilterQueryProvider() {
        throw new RuntimeException("Stub!");
    }

    public void setFilterQueryProvider(FilterQueryProvider filterQueryProvider) {
        throw new RuntimeException("Stub!");
    }

    public void changeCursor(Cursor cursor) {
        throw new RuntimeException("Stub!");
    }

    public Cursor getCursor() {
        throw new RuntimeException("Stub!");
    }
}
