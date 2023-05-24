package android.text.method;

import android.text.Spannable;
import android.view.KeyEvent;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/method/DialerKeyListener.class */
public class DialerKeyListener extends NumberKeyListener {
    public static final char[] CHARACTERS = null;

    public DialerKeyListener() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.method.NumberKeyListener
    protected char[] getAcceptedChars() {
        throw new RuntimeException("Stub!");
    }

    public static DialerKeyListener getInstance() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.method.KeyListener
    public int getInputType() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.method.NumberKeyListener
    protected int lookup(KeyEvent event, Spannable content) {
        throw new RuntimeException("Stub!");
    }
}
