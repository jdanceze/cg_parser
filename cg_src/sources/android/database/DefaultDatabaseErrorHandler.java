package android.database;

import android.database.sqlite.SQLiteDatabase;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/database/DefaultDatabaseErrorHandler.class */
public final class DefaultDatabaseErrorHandler implements DatabaseErrorHandler {
    public DefaultDatabaseErrorHandler() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.DatabaseErrorHandler
    public void onCorruption(SQLiteDatabase dbObj) {
        throw new RuntimeException("Stub!");
    }
}
