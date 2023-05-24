package android.text.style;

import android.text.TextPaint;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/style/MetricAffectingSpan.class */
public abstract class MetricAffectingSpan extends CharacterStyle implements UpdateLayout {
    public abstract void updateMeasureState(TextPaint textPaint);

    public MetricAffectingSpan() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.style.CharacterStyle
    public MetricAffectingSpan getUnderlying() {
        throw new RuntimeException("Stub!");
    }
}
