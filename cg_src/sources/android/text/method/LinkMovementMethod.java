package android.text.method;

import android.text.Spannable;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.TextView;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/method/LinkMovementMethod.class */
public class LinkMovementMethod extends ScrollingMovementMethod {
    public LinkMovementMethod() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.method.BaseMovementMethod
    protected boolean handleMovementKey(TextView widget, Spannable buffer, int keyCode, int movementMetaState, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.method.ScrollingMovementMethod, android.text.method.BaseMovementMethod
    protected boolean up(TextView widget, Spannable buffer) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.method.ScrollingMovementMethod, android.text.method.BaseMovementMethod
    protected boolean down(TextView widget, Spannable buffer) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.method.ScrollingMovementMethod, android.text.method.BaseMovementMethod
    protected boolean left(TextView widget, Spannable buffer) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.method.ScrollingMovementMethod, android.text.method.BaseMovementMethod
    protected boolean right(TextView widget, Spannable buffer) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.method.ScrollingMovementMethod, android.text.method.BaseMovementMethod, android.text.method.MovementMethod
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.method.BaseMovementMethod, android.text.method.MovementMethod
    public void initialize(TextView widget, Spannable text) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.method.ScrollingMovementMethod, android.text.method.BaseMovementMethod, android.text.method.MovementMethod
    public void onTakeFocus(TextView view, Spannable text, int dir) {
        throw new RuntimeException("Stub!");
    }

    public static MovementMethod getInstance() {
        throw new RuntimeException("Stub!");
    }
}
