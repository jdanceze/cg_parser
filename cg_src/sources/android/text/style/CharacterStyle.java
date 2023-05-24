package android.text.style;

import android.text.TextPaint;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/style/CharacterStyle.class */
public abstract class CharacterStyle {
    public abstract void updateDrawState(TextPaint textPaint);

    public CharacterStyle() {
        throw new RuntimeException("Stub!");
    }

    public static CharacterStyle wrap(CharacterStyle cs) {
        throw new RuntimeException("Stub!");
    }

    public CharacterStyle getUnderlying() {
        throw new RuntimeException("Stub!");
    }
}
