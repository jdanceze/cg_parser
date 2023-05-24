package android.app;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/ExpandableListActivity.class */
public class ExpandableListActivity extends Activity implements View.OnCreateContextMenuListener, ExpandableListView.OnChildClickListener, ExpandableListView.OnGroupCollapseListener, ExpandableListView.OnGroupExpandListener {
    public ExpandableListActivity() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Activity, android.view.View.OnCreateContextMenuListener
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.ExpandableListView.OnChildClickListener
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.ExpandableListView.OnGroupCollapseListener
    public void onGroupCollapse(int groupPosition) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.ExpandableListView.OnGroupExpandListener
    public void onGroupExpand(int groupPosition) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Activity
    protected void onRestoreInstanceState(Bundle state) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onContentChanged() {
        throw new RuntimeException("Stub!");
    }

    public void setListAdapter(ExpandableListAdapter adapter) {
        throw new RuntimeException("Stub!");
    }

    public ExpandableListView getExpandableListView() {
        throw new RuntimeException("Stub!");
    }

    public ExpandableListAdapter getExpandableListAdapter() {
        throw new RuntimeException("Stub!");
    }

    public long getSelectedId() {
        throw new RuntimeException("Stub!");
    }

    public long getSelectedPosition() {
        throw new RuntimeException("Stub!");
    }

    public boolean setSelectedChild(int groupPosition, int childPosition, boolean shouldExpandGroup) {
        throw new RuntimeException("Stub!");
    }

    public void setSelectedGroup(int groupPosition) {
        throw new RuntimeException("Stub!");
    }
}
