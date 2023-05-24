package android.text.method;

import android.text.Spanned;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/method/DigitsKeyListener.class */
public class DigitsKeyListener extends NumberKeyListener {
    public DigitsKeyListener() {
        throw new RuntimeException("Stub!");
    }

    public DigitsKeyListener(boolean sign, boolean decimal) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.method.NumberKeyListener
    protected char[] getAcceptedChars() {
        throw new RuntimeException("Stub!");
    }

    public static DigitsKeyListener getInstance() {
        throw new RuntimeException("Stub!");
    }

    public static DigitsKeyListener getInstance(boolean sign, boolean decimal) {
        throw new RuntimeException("Stub!");
    }

    public static DigitsKeyListener getInstance(String accepted) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.method.KeyListener
    public int getInputType() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.method.NumberKeyListener, android.text.InputFilter
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        throw new RuntimeException("Stub!");
    }
}
