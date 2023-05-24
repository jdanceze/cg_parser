package android.text.method;

import android.text.Editable;
import android.text.SpanWatcher;
import android.text.Spannable;
import android.text.method.TextKeyListener;
import android.view.KeyEvent;
import android.view.View;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/method/MultiTapKeyListener.class */
public class MultiTapKeyListener extends BaseKeyListener implements SpanWatcher {
    public MultiTapKeyListener(TextKeyListener.Capitalize cap, boolean autotext) {
        throw new RuntimeException("Stub!");
    }

    public static MultiTapKeyListener getInstance(boolean autotext, TextKeyListener.Capitalize cap) {
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

    @Override // android.text.SpanWatcher
    public void onSpanChanged(Spannable buf, Object what, int s, int e, int start, int stop) {
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
}
