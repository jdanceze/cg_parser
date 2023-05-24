package android.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/database/CursorWrapper.class */
public class CursorWrapper implements Cursor {
    public CursorWrapper(Cursor cursor) {
        throw new RuntimeException("Stub!");
    }

    public Cursor getWrappedCursor() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public boolean isClosed() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public int getCount() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public void deactivate() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public boolean moveToFirst() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public int getColumnCount() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public int getColumnIndex(String columnName) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public int getColumnIndexOrThrow(String columnName) throws IllegalArgumentException {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public String getColumnName(int columnIndex) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public String[] getColumnNames() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public double getDouble(int columnIndex) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public Bundle getExtras() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public float getFloat(int columnIndex) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public int getInt(int columnIndex) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public long getLong(int columnIndex) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public short getShort(int columnIndex) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public String getString(int columnIndex) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public byte[] getBlob(int columnIndex) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public boolean getWantsAllOnMoveCalls() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public boolean isAfterLast() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public boolean isBeforeFirst() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public boolean isFirst() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public boolean isLast() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public int getType(int columnIndex) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public boolean isNull(int columnIndex) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public boolean moveToLast() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public boolean move(int offset) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public boolean moveToPosition(int position) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public boolean moveToNext() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public int getPosition() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public boolean moveToPrevious() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public void registerContentObserver(ContentObserver observer) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public void registerDataSetObserver(DataSetObserver observer) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public boolean requery() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public Bundle respond(Bundle extras) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public void setNotificationUri(ContentResolver cr, Uri uri) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public void unregisterContentObserver(ContentObserver observer) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.Cursor
    public void unregisterDataSetObserver(DataSetObserver observer) {
        throw new RuntimeException("Stub!");
    }
}
