package android.widget;

import android.database.Cursor;
import android.database.DataSetObserver;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/AlphabetIndexer.class */
public class AlphabetIndexer extends DataSetObserver implements SectionIndexer {
    protected Cursor mDataCursor;
    protected int mColumnIndex;
    protected CharSequence mAlphabet;

    public AlphabetIndexer(Cursor cursor, int sortedColumnIndex, CharSequence alphabet) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.SectionIndexer
    public Object[] getSections() {
        throw new RuntimeException("Stub!");
    }

    public void setCursor(Cursor cursor) {
        throw new RuntimeException("Stub!");
    }

    protected int compare(String word, String letter) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.SectionIndexer
    public int getPositionForSection(int sectionIndex) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.SectionIndexer
    public int getSectionForPosition(int position) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.DataSetObserver
    public void onChanged() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.database.DataSetObserver
    public void onInvalidated() {
        throw new RuntimeException("Stub!");
    }
}
