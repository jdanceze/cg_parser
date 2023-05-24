package android.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import java.util.HashMap;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/database/AbstractCursor.class */
public abstract class AbstractCursor implements CrossProcessCursor {
    @Deprecated
    protected HashMap<Long, Map<String, Object>> mUpdatedRows;
    protected int mPos;
    @Deprecated
    protected int mRowIdColumnIndex;
    @Deprecated
    protected Long mCurrentRowID;
    protected boolean mClosed;
    protected ContentResolver mContentResolver;

    @Override // android.database.Cursor
    public abstract int getCount();

    @Override // android.database.Cursor
    public abstract String[] getColumnNames();

    @Override // android.database.Cursor
    public abstract String getString(int i);

    @Override // android.database.Cursor
    public abstract short getShort(int i);

    @Override // android.database.Cursor
    public abstract int getInt(int i);

    @Override // android.database.Cursor
    public abstract long getLong(int i);

    @Override // android.database.Cursor
    public abstract float getFloat(int i);

    @Override // android.database.Cursor
    public abstract double getDouble(int i);

    @Override // android.database.Cursor
    public abstract boolean isNull(int i);

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/database/AbstractCursor$SelfContentObserver.class */
    protected static class SelfContentObserver extends ContentObserver {
        public SelfContentObserver(AbstractCursor cursor) {
            super(null);
            throw new RuntimeException("Stub!");
        }

        @Override // android.database.ContentObserver
        public boolean deliverSelfNotifications() {
            throw new RuntimeException("Stub!");
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            throw new RuntimeException("Stub!");
        }
    }

    public AbstractCursor() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public int getType(int column) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public byte[] getBlob(int column) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.CrossProcessCursor
    public CursorWindow getWindow() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public int getColumnCount() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public void deactivate() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public boolean requery() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public boolean isClosed() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.CrossProcessCursor
    public boolean onMove(int oldPosition, int newPosition) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public final int getPosition() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public final boolean moveToPosition(int position) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.CrossProcessCursor
    public void fillWindow(int position, CursorWindow window) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public final boolean move(int offset) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public final boolean moveToFirst() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public final boolean moveToLast() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public final boolean moveToNext() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public final boolean moveToPrevious() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public final boolean isFirst() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public final boolean isLast() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public final boolean isBeforeFirst() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public final boolean isAfterLast() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public int getColumnIndex(String columnName) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public int getColumnIndexOrThrow(String columnName) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public String getColumnName(int columnIndex) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public void registerContentObserver(ContentObserver observer) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public void unregisterContentObserver(ContentObserver observer) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public void registerDataSetObserver(DataSetObserver observer) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public void unregisterDataSetObserver(DataSetObserver observer) {
        throw new RuntimeException("Stub!");
    }

    protected void onChange(boolean selfChange) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public void setNotificationUri(ContentResolver cr, Uri notifyUri) {
        throw new RuntimeException("Stub!");
    }

    public Uri getNotificationUri() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public boolean getWantsAllOnMoveCalls() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public Bundle getExtras() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public Bundle respond(Bundle extras) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    protected boolean isFieldUpdated(int columnIndex) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    protected Object getUpdatedField(int columnIndex) {
        throw new RuntimeException("Stub!");
    }

    protected void checkPosition() {
        throw new RuntimeException("Stub!");
    }

    protected void finalize() {
        throw new RuntimeException("Stub!");
    }
}
