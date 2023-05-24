package android.widget;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/ResourceCursorTreeAdapter.class */
public abstract class ResourceCursorTreeAdapter extends CursorTreeAdapter {
    public ResourceCursorTreeAdapter(Context context, Cursor cursor, int collapsedGroupLayout, int expandedGroupLayout, int childLayout, int lastChildLayout) {
        super(null, null, false);
        throw new RuntimeException("Stub!");
    }

    public ResourceCursorTreeAdapter(Context context, Cursor cursor, int collapsedGroupLayout, int expandedGroupLayout, int childLayout) {
        super(null, null, false);
        throw new RuntimeException("Stub!");
    }

    public ResourceCursorTreeAdapter(Context context, Cursor cursor, int groupLayout, int childLayout) {
        super(null, null, false);
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.CursorTreeAdapter
    public View newChildView(Context context, Cursor cursor, boolean isLastChild, ViewGroup parent) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.CursorTreeAdapter
    public View newGroupView(Context context, Cursor cursor, boolean isExpanded, ViewGroup parent) {
        throw new RuntimeException("Stub!");
    }
}
