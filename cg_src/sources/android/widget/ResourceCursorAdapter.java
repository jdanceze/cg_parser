package android.widget;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/ResourceCursorAdapter.class */
public abstract class ResourceCursorAdapter extends CursorAdapter {
    @Deprecated
    public ResourceCursorAdapter(Context context, int layout, Cursor c) {
        super((Context) null, (Cursor) null, 0);
        throw new RuntimeException("Stub!");
    }

    public ResourceCursorAdapter(Context context, int layout, Cursor c, boolean autoRequery) {
        super((Context) null, (Cursor) null, 0);
        throw new RuntimeException("Stub!");
    }

    public ResourceCursorAdapter(Context context, int layout, Cursor c, int flags) {
        super((Context) null, (Cursor) null, 0);
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.CursorAdapter
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.CursorAdapter
    public View newDropDownView(Context context, Cursor cursor, ViewGroup parent) {
        throw new RuntimeException("Stub!");
    }

    public void setViewResource(int layout) {
        throw new RuntimeException("Stub!");
    }

    public void setDropDownViewResource(int dropDownLayout) {
        throw new RuntimeException("Stub!");
    }
}
