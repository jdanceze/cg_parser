package android.database.sqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/database/sqlite/SQLiteCursorDriver.class */
public interface SQLiteCursorDriver {
    Cursor query(SQLiteDatabase.CursorFactory cursorFactory, String[] strArr);

    void cursorDeactivated();

    void cursorRequeried(Cursor cursor);

    void cursorClosed();

    void setBindArguments(String[] strArr);
}
