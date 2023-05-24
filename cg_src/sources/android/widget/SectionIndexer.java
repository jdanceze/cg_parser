package android.widget;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/SectionIndexer.class */
public interface SectionIndexer {
    Object[] getSections();

    int getPositionForSection(int i);

    int getSectionForPosition(int i);
}
