package android.text.style;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Layout;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/style/DrawableMarginSpan.class */
public class DrawableMarginSpan implements LeadingMarginSpan, LineHeightSpan {
    public DrawableMarginSpan(Drawable b) {
        throw new RuntimeException("Stub!");
    }

    public DrawableMarginSpan(Drawable b, int pad) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.style.LeadingMarginSpan
    public int getLeadingMargin(boolean first) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.style.LeadingMarginSpan
    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout layout) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.style.LineHeightSpan
    public void chooseHeight(CharSequence text, int start, int end, int istartv, int v, Paint.FontMetricsInt fm) {
        throw new RuntimeException("Stub!");
    }
}
