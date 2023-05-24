package android.text.method;

import android.text.Editable;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.View;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/method/NumberKeyListener.class */
public abstract class NumberKeyListener extends BaseKeyListener implements InputFilter {
    protected abstract char[] getAcceptedChars();

    public NumberKeyListener() {
        throw new RuntimeException("Stub!");
    }

    protected int lookup(KeyEvent event, Spannable content) {
        throw new RuntimeException("Stub!");
    }

    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        throw new RuntimeException("Stub!");
    }

    protected static boolean ok(char[] accept, char c) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.method.BaseKeyListener, android.text.method.MetaKeyKeyListener, android.text.method.KeyListener
    public boolean onKeyDown(View view, Editable content, int keyCode, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }
}
