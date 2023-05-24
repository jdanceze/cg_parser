package android.database.sqlite;

import java.io.Closeable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/database/sqlite/SQLiteClosable.class */
public abstract class SQLiteClosable implements Closeable {
    protected abstract void onAllReferencesReleased();

    public SQLiteClosable() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    protected void onAllReferencesReleasedFromContainer() {
        throw new RuntimeException("Stub!");
    }

    public void acquireReference() {
        throw new RuntimeException("Stub!");
    }

    public void releaseReference() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void releaseReferenceFromContainer() {
        throw new RuntimeException("Stub!");
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        throw new RuntimeException("Stub!");
    }
}
