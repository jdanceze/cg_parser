package android.text.style;

import android.text.TextPaint;
import android.view.View;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/style/ClickableSpan.class */
public abstract class ClickableSpan extends CharacterStyle implements UpdateAppearance {
    public abstract void onClick(View view);

    public ClickableSpan() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.style.CharacterStyle
    public void updateDrawState(TextPaint ds) {
        throw new RuntimeException("Stub!");
    }
}
