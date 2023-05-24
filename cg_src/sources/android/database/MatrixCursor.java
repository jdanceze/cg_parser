package android.database;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/database/MatrixCursor.class */
public class MatrixCursor extends AbstractCursor {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/database/MatrixCursor$RowBuilder.class */
    public class RowBuilder {
        RowBuilder() {
            throw new RuntimeException("Stub!");
        }

        public RowBuilder add(Object columnValue) {
            throw new RuntimeException("Stub!");
        }
    }

    public MatrixCursor(String[] columnNames, int initialCapacity) {
        throw new RuntimeException("Stub!");
    }

    public MatrixCursor(String[] columnNames) {
        throw new RuntimeException("Stub!");
    }

    public RowBuilder newRow() {
        throw new RuntimeException("Stub!");
    }

    public void addRow(Object[] columnValues) {
        throw new RuntimeException("Stub!");
    }

    public void addRow(Iterable<?> columnValues) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public int getCount() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public String[] getColumnNames() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public String getString(int column) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public short getShort(int column) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public int getInt(int column) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public long getLong(int column) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public float getFloat(int column) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public double getDouble(int column) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public byte[] getBlob(int column) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public int getType(int column) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public boolean isNull(int column) {
        throw new RuntimeException("Stub!");
    }
}
