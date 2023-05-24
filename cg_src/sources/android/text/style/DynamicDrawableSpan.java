package android.text.style;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/style/DynamicDrawableSpan.class */
public abstract class DynamicDrawableSpan extends ReplacementSpan {
    public static final int ALIGN_BOTTOM = 0;
    public static final int ALIGN_BASELINE = 1;
    protected final int mVerticalAlignment;

    public abstract Drawable getDrawable();

    public DynamicDrawableSpan() {
        throw new RuntimeException("Stub!");
    }

    protected DynamicDrawableSpan(int verticalAlignment) {
        throw new RuntimeException("Stub!");
    }

    public int getVerticalAlignment() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.style.ReplacementSpan
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.style.ReplacementSpan
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        throw new RuntimeException("Stub!");
    }
}
