package android.text.method;

import android.graphics.Rect;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/method/PasswordTransformationMethod.class */
public class PasswordTransformationMethod implements TransformationMethod, TextWatcher {
    public PasswordTransformationMethod() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.method.TransformationMethod
    public CharSequence getTransformation(CharSequence source, View view) {
        throw new RuntimeException("Stub!");
    }

    public static PasswordTransformationMethod getInstance() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable s) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.method.TransformationMethod
    public void onFocusChanged(View view, CharSequence sourceText, boolean focused, int direction, Rect previouslyFocusedRect) {
        throw new RuntimeException("Stub!");
    }
}
