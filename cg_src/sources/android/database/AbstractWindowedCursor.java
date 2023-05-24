package android.database;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/database/AbstractWindowedCursor.class */
public abstract class AbstractWindowedCursor extends AbstractCursor {
    protected CursorWindow mWindow;

    public AbstractWindowedCursor() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public byte[] getBlob(int columnIndex) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public String getString(int columnIndex) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public short getShort(int columnIndex) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public int getInt(int columnIndex) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public long getLong(int columnIndex) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public float getFloat(int columnIndex) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public double getDouble(int columnIndex) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public boolean isNull(int columnIndex) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public boolean isBlob(int columnIndex) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public boolean isString(int columnIndex) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public boolean isLong(int columnIndex) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public boolean isFloat(int columnIndex) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public int getType(int columnIndex) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.AbstractCursor
    protected void checkPosition() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.AbstractCursor, android.database.CrossProcessCursor
    public CursorWindow getWindow() {
        throw new RuntimeException("Stub!");
    }

    public void setWindow(CursorWindow window) {
        throw new RuntimeException("Stub!");
    }

    public boolean hasWindow() {
        throw new RuntimeException("Stub!");
    }
}
