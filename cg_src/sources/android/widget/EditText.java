package android.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.MovementMethod;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.TextView;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/EditText.class */
public class EditText extends TextView {
    public EditText(Context context) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public EditText(Context context, AttributeSet attrs) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public EditText(Context context, AttributeSet attrs, int defStyle) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.TextView
    protected boolean getDefaultEditable() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.TextView
    protected MovementMethod getDefaultMovementMethod() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.TextView
    public Editable getText() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.TextView
    public void setText(CharSequence text, TextView.BufferType type) {
        throw new RuntimeException("Stub!");
    }

    public void setSelection(int start, int stop) {
        throw new RuntimeException("Stub!");
    }

    public void setSelection(int index) {
        throw new RuntimeException("Stub!");
    }

    public void selectAll() {
        throw new RuntimeException("Stub!");
    }

    public void extendSelection(int index) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.TextView
    public void setEllipsize(TextUtils.TruncateAt ellipsis) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.TextView, android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.TextView, android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        throw new RuntimeException("Stub!");
    }
}
