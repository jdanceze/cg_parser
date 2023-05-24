package android.text.style;

import android.graphics.Paint;
import android.text.TextPaint;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/style/LineHeightSpan.class */
public interface LineHeightSpan extends ParagraphStyle, WrapTogetherSpan {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/style/LineHeightSpan$WithDensity.class */
    public interface WithDensity extends LineHeightSpan {
        void chooseHeight(CharSequence charSequence, int i, int i2, int i3, int i4, Paint.FontMetricsInt fontMetricsInt, TextPaint textPaint);
    }

    void chooseHeight(CharSequence charSequence, int i, int i2, int i3, int i4, Paint.FontMetricsInt fontMetricsInt);
}
