package android.database;

import android.database.sqlite.SQLiteClosable;
import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/database/CursorWindow.class */
public class CursorWindow extends SQLiteClosable implements Parcelable {
    public static final Parcelable.Creator<CursorWindow> CREATOR = null;

    public CursorWindow(String name) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public CursorWindow(boolean localWindow) {
        throw new RuntimeException("Stub!");
    }

    protected void finalize() throws Throwable {
        throw new RuntimeException("Stub!");
    }

    public void clear() {
        throw new RuntimeException("Stub!");
    }

    public int getStartPosition() {
        throw new RuntimeException("Stub!");
    }

    public void setStartPosition(int pos) {
        throw new RuntimeException("Stub!");
    }

    public int getNumRows() {
        throw new RuntimeException("Stub!");
    }

    public boolean setNumColumns(int columnNum) {
        throw new RuntimeException("Stub!");
    }

    public boolean allocRow() {
        throw new RuntimeException("Stub!");
    }

    public void freeLastRow() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public boolean isNull(int row, int column) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public boolean isBlob(int row, int column) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public boolean isLong(int row, int column) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public boolean isFloat(int row, int column) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public boolean isString(int row, int column) {
        throw new RuntimeException("Stub!");
    }

    public int getType(int row, int column) {
        throw new RuntimeException("Stub!");
    }

    public byte[] getBlob(int row, int column) {
        throw new RuntimeException("Stub!");
    }

    public String getString(int row, int column) {
        throw new RuntimeException("Stub!");
    }

    public void copyStringToBuffer(int row, int column, CharArrayBuffer buffer) {
        throw new RuntimeException("Stub!");
    }

    public long getLong(int row, int column) {
        throw new RuntimeException("Stub!");
    }

    public double getDouble(int row, int column) {
        throw new RuntimeException("Stub!");
    }

    public short getShort(int row, int column) {
        throw new RuntimeException("Stub!");
    }

    public int getInt(int row, int column) {
        throw new RuntimeException("Stub!");
    }

    public float getFloat(int row, int column) {
        throw new RuntimeException("Stub!");
    }

    public boolean putBlob(byte[] value, int row, int column) {
        throw new RuntimeException("Stub!");
    }

    public boolean putString(String value, int row, int column) {
        throw new RuntimeException("Stub!");
    }

    public boolean putLong(long value, int row, int column) {
        throw new RuntimeException("Stub!");
    }

    public boolean putDouble(double value, int row, int column) {
        throw new RuntimeException("Stub!");
    }

    public boolean putNull(int row, int column) {
        throw new RuntimeException("Stub!");
    }

    public static CursorWindow newFromParcel(Parcel p) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.sqlite.SQLiteClosable
    protected void onAllReferencesReleased() {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
        throw new RuntimeException("Stub!");
    }
}
