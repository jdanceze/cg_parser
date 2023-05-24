package android.database.sqlite;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/database/sqlite/SQLiteTransactionListener.class */
public interface SQLiteTransactionListener {
    void onBegin();

    void onCommit();

    void onRollback();
}
