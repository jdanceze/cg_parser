package android.database.sqlite;

import android.database.AbstractWindowedCursor;
import android.database.CursorWindow;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/database/sqlite/SQLiteCursor.class */
public class SQLiteCursor extends AbstractWindowedCursor {
    @Deprecated
    public SQLiteCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
        throw new RuntimeException("Stub!");
    }

    public SQLiteCursor(SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
        throw new RuntimeException("Stub!");
    }

    public SQLiteDatabase getDatabase() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.AbstractCursor, android.database.CrossProcessCursor
    public boolean onMove(int oldPosition, int newPosition) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public int getCount() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public int getColumnIndex(String columnName) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public String[] getColumnNames() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public void deactivate() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.AbstractCursor, android.database.Cursor, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public boolean requery() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.AbstractWindowedCursor
    public void setWindow(CursorWindow window) {
        throw new RuntimeException("Stub!");
    }

    public void setSelectionArguments(String[] selectionArgs) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.AbstractCursor
    protected void finalize() {
        throw new RuntimeException("Stub!");
    }
}
