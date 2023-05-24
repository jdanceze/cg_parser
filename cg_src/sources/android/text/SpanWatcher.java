package android.text;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/SpanWatcher.class */
public interface SpanWatcher extends NoCopySpan {
    void onSpanAdded(Spannable spannable, Object obj, int i, int i2);

    void onSpanRemoved(Spannable spannable, Object obj, int i, int i2);

    void onSpanChanged(Spannable spannable, Object obj, int i, int i2, int i3, int i4);
}
