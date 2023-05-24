package android.database;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/database/CrossProcessCursor.class */
public interface CrossProcessCursor extends Cursor {
    CursorWindow getWindow();

    void fillWindow(int i, CursorWindow cursorWindow);

    boolean onMove(int i, int i2);
}
