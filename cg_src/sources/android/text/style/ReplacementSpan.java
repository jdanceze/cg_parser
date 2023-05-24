package android.text.style;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/style/ReplacementSpan.class */
public abstract class ReplacementSpan extends MetricAffectingSpan {
    public abstract int getSize(Paint paint, CharSequence charSequence, int i, int i2, Paint.FontMetricsInt fontMetricsInt);

    public abstract void draw(Canvas canvas, CharSequence charSequence, int i, int i2, float f, int i3, int i4, int i5, Paint paint);

    public ReplacementSpan() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.style.MetricAffectingSpan
    public void updateMeasureState(TextPaint p) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.style.CharacterStyle
    public void updateDrawState(TextPaint ds) {
        throw new RuntimeException("Stub!");
    }
}
