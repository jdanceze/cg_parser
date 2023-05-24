package android.database;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/database/CrossProcessCursorWrapper.class */
public class CrossProcessCursorWrapper extends CursorWrapper implements CrossProcessCursor {
    public CrossProcessCursorWrapper(Cursor cursor) {
        super(null);
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.CrossProcessCursor
    public void fillWindow(int position, CursorWindow window) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.CrossProcessCursor
    public CursorWindow getWindow() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.CrossProcessCursor
    public boolean onMove(int oldPosition, int newPosition) {
        throw new RuntimeException("Stub!");
    }
}
