package android.widget;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/SimpleCursorAdapter.class */
public class SimpleCursorAdapter extends ResourceCursorAdapter {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/SimpleCursorAdapter$CursorToStringConverter.class */
    public interface CursorToStringConverter {
        CharSequence convertToString(Cursor cursor);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/SimpleCursorAdapter$ViewBinder.class */
    public interface ViewBinder {
        boolean setViewValue(View view, Cursor cursor, int i);
    }

    @Deprecated
    public SimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super((Context) null, 0, (Cursor) null, 0);
        throw new RuntimeException("Stub!");
    }

    public SimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super((Context) null, 0, (Cursor) null, 0);
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.CursorAdapter
    public void bindView(View view, Context context, Cursor cursor) {
        throw new RuntimeException("Stub!");
    }

    public ViewBinder getViewBinder() {
        throw new RuntimeException("Stub!");
    }

    public void setViewBinder(ViewBinder viewBinder) {
        throw new RuntimeException("Stub!");
    }

    public void setViewImage(ImageView v, String value) {
        throw new RuntimeException("Stub!");
    }

    public void setViewText(TextView v, String text) {
        throw new RuntimeException("Stub!");
    }

    public int getStringConversionColumn() {
        throw new RuntimeException("Stub!");
    }

    public void setStringConversionColumn(int stringConversionColumn) {
        throw new RuntimeException("Stub!");
    }

    public CursorToStringConverter getCursorToStringConverter() {
        throw new RuntimeException("Stub!");
    }

    public void setCursorToStringConverter(CursorToStringConverter cursorToStringConverter) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.CursorAdapter
    public CharSequence convertToString(Cursor cursor) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.CursorAdapter
    public Cursor swapCursor(Cursor c) {
        throw new RuntimeException("Stub!");
    }

    public void changeCursorAndColumns(Cursor c, String[] from, int[] to) {
        throw new RuntimeException("Stub!");
    }
}
