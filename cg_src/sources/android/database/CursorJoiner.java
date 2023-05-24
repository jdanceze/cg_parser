package android.database;

import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/database/CursorJoiner.class */
public final class CursorJoiner implements Iterator<Result>, Iterable<Result> {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/database/CursorJoiner$Result.class */
    public enum Result {
        BOTH,
        LEFT,
        RIGHT
    }

    public CursorJoiner(Cursor cursorLeft, String[] columnNamesLeft, Cursor cursorRight, String[] columnNamesRight) {
        throw new RuntimeException("Stub!");
    }

    @Override // java.lang.Iterable
    public Iterator<Result> iterator() {
        throw new RuntimeException("Stub!");
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        throw new RuntimeException("Stub!");
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Iterator
    public Result next() {
        throw new RuntimeException("Stub!");
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new RuntimeException("Stub!");
    }
}
