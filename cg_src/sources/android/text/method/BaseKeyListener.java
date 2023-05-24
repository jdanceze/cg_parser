package android.text.method;

import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/method/BaseKeyListener.class */
public abstract class BaseKeyListener extends MetaKeyKeyListener implements KeyListener {
    public BaseKeyListener() {
        throw new RuntimeException("Stub!");
    }

    public boolean backspace(View view, Editable content, int keyCode, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    public boolean forwardDelete(View view, Editable content, int keyCode, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.method.MetaKeyKeyListener, android.text.method.KeyListener
    public boolean onKeyDown(View view, Editable content, int keyCode, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.method.KeyListener
    public boolean onKeyOther(View view, Editable content, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }
}
