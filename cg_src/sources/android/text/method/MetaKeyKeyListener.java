package android.text.method;

import android.text.Editable;
import android.text.Spannable;
import android.view.KeyEvent;
import android.view.View;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/method/MetaKeyKeyListener.class */
public abstract class MetaKeyKeyListener {
    public static final int META_SHIFT_ON = 1;
    public static final int META_ALT_ON = 2;
    public static final int META_SYM_ON = 4;
    public static final int META_CAP_LOCKED = 256;
    public static final int META_ALT_LOCKED = 512;
    public static final int META_SYM_LOCKED = 1024;

    public MetaKeyKeyListener() {
        throw new RuntimeException("Stub!");
    }

    public static void resetMetaState(Spannable text) {
        throw new RuntimeException("Stub!");
    }

    public static final int getMetaState(CharSequence text) {
        throw new RuntimeException("Stub!");
    }

    public static final int getMetaState(CharSequence text, int meta) {
        throw new RuntimeException("Stub!");
    }

    public static void adjustMetaAfterKeypress(Spannable content) {
        throw new RuntimeException("Stub!");
    }

    public static boolean isMetaTracker(CharSequence text, Object what) {
        throw new RuntimeException("Stub!");
    }

    public static boolean isSelectingMetaTracker(CharSequence text, Object what) {
        throw new RuntimeException("Stub!");
    }

    protected static void resetLockedMeta(Spannable content) {
        throw new RuntimeException("Stub!");
    }

    public boolean onKeyDown(View view, Editable content, int keyCode, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    public boolean onKeyUp(View view, Editable content, int keyCode, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    public void clearMetaKeyState(View view, Editable content, int states) {
        throw new RuntimeException("Stub!");
    }

    public static void clearMetaKeyState(Editable content, int states) {
        throw new RuntimeException("Stub!");
    }

    public static long resetLockedMeta(long state) {
        throw new RuntimeException("Stub!");
    }

    public static final int getMetaState(long state) {
        throw new RuntimeException("Stub!");
    }

    public static final int getMetaState(long state, int meta) {
        throw new RuntimeException("Stub!");
    }

    public static long adjustMetaAfterKeypress(long state) {
        throw new RuntimeException("Stub!");
    }

    public static long handleKeyDown(long state, int keyCode, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    public static long handleKeyUp(long state, int keyCode, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    public long clearMetaKeyState(long state, int which) {
        throw new RuntimeException("Stub!");
    }
}
