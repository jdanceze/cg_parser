package android.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/SimpleExpandableListAdapter.class */
public class SimpleExpandableListAdapter extends BaseExpandableListAdapter {
    public SimpleExpandableListAdapter(Context context, List<? extends Map<String, ?>> groupData, int groupLayout, String[] groupFrom, int[] groupTo, List<? extends List<? extends Map<String, ?>>> childData, int childLayout, String[] childFrom, int[] childTo) {
        throw new RuntimeException("Stub!");
    }

    public SimpleExpandableListAdapter(Context context, List<? extends Map<String, ?>> groupData, int expandedGroupLayout, int collapsedGroupLayout, String[] groupFrom, int[] groupTo, List<? extends List<? extends Map<String, ?>>> childData, int childLayout, String[] childFrom, int[] childTo) {
        throw new RuntimeException("Stub!");
    }

    public SimpleExpandableListAdapter(Context context, List<? extends Map<String, ?>> groupData, int expandedGroupLayout, int collapsedGroupLayout, String[] groupFrom, int[] groupTo, List<? extends List<? extends Map<String, ?>>> childData, int childLayout, int lastChildLayout, String[] childFrom, int[] childTo) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.ExpandableListAdapter
    public Object getChild(int groupPosition, int childPosition) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.ExpandableListAdapter
    public long getChildId(int groupPosition, int childPosition) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.ExpandableListAdapter
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        throw new RuntimeException("Stub!");
    }

    public View newChildView(boolean isLastChild, ViewGroup parent) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.ExpandableListAdapter
    public int getChildrenCount(int groupPosition) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.ExpandableListAdapter
    public Object getGroup(int groupPosition) {
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

    public View newGroupView(boolean isExpanded, ViewGroup parent) {
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
}
