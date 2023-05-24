package android.text.method;

import android.graphics.Rect;
import android.view.View;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/method/ReplacementTransformationMethod.class */
public abstract class ReplacementTransformationMethod implements TransformationMethod {
    protected abstract char[] getOriginal();

    protected abstract char[] getReplacement();

    public ReplacementTransformationMethod() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.method.TransformationMethod
    public CharSequence getTransformation(CharSequence source, View v) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.method.TransformationMethod
    public void onFocusChanged(View view, CharSequence sourceText, boolean focused, int direction, Rect previouslyFocusedRect) {
        throw new RuntimeException("Stub!");
    }
}
