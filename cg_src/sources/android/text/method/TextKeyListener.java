package android.text.method;

import android.text.Editable;
import android.text.SpanWatcher;
import android.text.Spannable;
import android.view.KeyEvent;
import android.view.View;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/method/TextKeyListener.class */
public class TextKeyListener extends BaseKeyListener implements SpanWatcher {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/method/TextKeyListener$Capitalize.class */
    public enum Capitalize {
        CHARACTERS,
        NONE,
        SENTENCES,
        WORDS
    }

    public TextKeyListener(Capitalize cap, boolean autotext) {
        throw new RuntimeException("Stub!");
    }

    public static TextKeyListener getInstance(boolean autotext, Capitalize cap) {
        throw new RuntimeException("Stub!");
    }

    public static TextKeyListener getInstance() {
        throw new RuntimeException("Stub!");
    }

    public static boolean shouldCap(Capitalize cap, CharSequence cs, int off) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.method.KeyListener
    public int getInputType() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.method.BaseKeyListener, android.text.method.MetaKeyKeyListener, android.text.method.KeyListener
    public boolean onKeyDown(View view, Editable content, int keyCode, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.method.MetaKeyKeyListener, android.text.method.KeyListener
    public boolean onKeyUp(View view, Editable content, int keyCode, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.method.BaseKeyListener, android.text.method.KeyListener
    public boolean onKeyOther(View view, Editable content, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    public static void clear(Editable e) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.SpanWatcher
    public void onSpanAdded(Spannable s, Object what, int start, int end) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.SpanWatcher
    public void onSpanRemoved(Spannable s, Object what, int start, int end) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.SpanWatcher
    public void onSpanChanged(Spannable s, Object what, int start, int end, int st, int en) {
        throw new RuntimeException("Stub!");
    }

    public void release() {
        throw new RuntimeException("Stub!");
    }
}
