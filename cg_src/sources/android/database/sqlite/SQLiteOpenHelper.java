package android.database.sqlite;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/database/sqlite/SQLiteOpenHelper.class */
public abstract class SQLiteOpenHelper {
    public abstract void onCreate(SQLiteDatabase sQLiteDatabase);

    public abstract void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2);

    public SQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        throw new RuntimeException("Stub!");
    }

    public SQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        throw new RuntimeException("Stub!");
    }

    public String getDatabaseName() {
        throw new RuntimeException("Stub!");
    }

    public void setWriteAheadLoggingEnabled(boolean enabled) {
        throw new RuntimeException("Stub!");
    }

    public SQLiteDatabase getWritableDatabase() {
        throw new RuntimeException("Stub!");
    }

    public SQLiteDatabase getReadableDatabase() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void close() {
        throw new RuntimeException("Stub!");
    }

    public void onConfigure(SQLiteDatabase db) {
        throw new RuntimeException("Stub!");
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new RuntimeException("Stub!");
    }

    public void onOpen(SQLiteDatabase db) {
        throw new RuntimeException("Stub!");
    }
}
