package android.widget;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/SimpleCursorTreeAdapter.class */
public abstract class SimpleCursorTreeAdapter extends ResourceCursorTreeAdapter {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/SimpleCursorTreeAdapter$ViewBinder.class */
    public interface ViewBinder {
        boolean setViewValue(View view, Cursor cursor, int i);
    }

    public SimpleCursorTreeAdapter(Context context, Cursor cursor, int collapsedGroupLayout, int expandedGroupLayout, String[] groupFrom, int[] groupTo, int childLayout, int lastChildLayout, String[] childFrom, int[] childTo) {
        super(null, null, 0, 0);
        throw new RuntimeException("Stub!");
    }

    public SimpleCursorTreeAdapter(Context context, Cursor cursor, int collapsedGroupLayout, int expandedGroupLayout, String[] groupFrom, int[] groupTo, int childLayout, String[] childFrom, int[] childTo) {
        super(null, null, 0, 0);
        throw new RuntimeException("Stub!");
    }

    public SimpleCursorTreeAdapter(Context context, Cursor cursor, int groupLayout, String[] groupFrom, int[] groupTo, int childLayout, String[] childFrom, int[] childTo) {
        super(null, null, 0, 0);
        throw new RuntimeException("Stub!");
    }

    public ViewBinder getViewBinder() {
        throw new RuntimeException("Stub!");
    }

    public void setViewBinder(ViewBinder viewBinder) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.CursorTreeAdapter
    protected void bindChildView(View view, Context context, Cursor cursor, boolean isLastChild) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.CursorTreeAdapter
    protected void bindGroupView(View view, Context context, Cursor cursor, boolean isExpanded) {
        throw new RuntimeException("Stub!");
    }

    protected void setViewImage(ImageView v, String value) {
        throw new RuntimeException("Stub!");
    }

    public void setViewText(TextView v, String text) {
        throw new RuntimeException("Stub!");
    }
}
