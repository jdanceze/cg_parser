package android.text;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.Layout;
import android.text.TextUtils;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/BoringLayout.class */
public class BoringLayout extends Layout implements TextUtils.EllipsizeCallback {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/BoringLayout$Metrics.class */
    public static class Metrics extends Paint.FontMetricsInt {
        public int width;

        public Metrics() {
            throw new RuntimeException("Stub!");
        }

        @Override // android.graphics.Paint.FontMetricsInt
        public String toString() {
            throw new RuntimeException("Stub!");
        }
    }

    public BoringLayout(CharSequence source, TextPaint paint, int outerwidth, Layout.Alignment align, float spacingmult, float spacingadd, Metrics metrics, boolean includepad) {
        super(null, null, 0, null, 0.0f, 0.0f);
        throw new RuntimeException("Stub!");
    }

    public BoringLayout(CharSequence source, TextPaint paint, int outerwidth, Layout.Alignment align, float spacingmult, float spacingadd, Metrics metrics, boolean includepad, TextUtils.TruncateAt ellipsize, int ellipsizedWidth) {
        super(null, null, 0, null, 0.0f, 0.0f);
        throw new RuntimeException("Stub!");
    }

    public static BoringLayout make(CharSequence source, TextPaint paint, int outerwidth, Layout.Alignment align, float spacingmult, float spacingadd, Metrics metrics, boolean includepad) {
        throw new RuntimeException("Stub!");
    }

    public static BoringLayout make(CharSequence source, TextPaint paint, int outerwidth, Layout.Alignment align, float spacingmult, float spacingadd, Metrics metrics, boolean includepad, TextUtils.TruncateAt ellipsize, int ellipsizedWidth) {
        throw new RuntimeException("Stub!");
    }

    public BoringLayout replaceOrMake(CharSequence source, TextPaint paint, int outerwidth, Layout.Alignment align, float spacingmult, float spacingadd, Metrics metrics, boolean includepad) {
        throw new RuntimeException("Stub!");
    }

    public BoringLayout replaceOrMake(CharSequence source, TextPaint paint, int outerwidth, Layout.Alignment align, float spacingmult, float spacingadd, Metrics metrics, boolean includepad, TextUtils.TruncateAt ellipsize, int ellipsizedWidth) {
        throw new RuntimeException("Stub!");
    }

    public static Metrics isBoring(CharSequence text, TextPaint paint) {
        throw new RuntimeException("Stub!");
    }

    public static Metrics isBoring(CharSequence text, TextPaint paint, Metrics metrics) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.Layout
    public int getHeight() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.Layout
    public int getLineCount() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.Layout
    public int getLineTop(int line) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.Layout
    public int getLineDescent(int line) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.Layout
    public int getLineStart(int line) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.Layout
    public int getParagraphDirection(int line) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.Layout
    public boolean getLineContainsTab(int line) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.Layout
    public float getLineMax(int line) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.Layout
    public final Layout.Directions getLineDirections(int line) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.Layout
    public int getTopPadding() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.Layout
    public int getBottomPadding() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.Layout
    public int getEllipsisCount(int line) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.Layout
    public int getEllipsisStart(int line) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.Layout
    public int getEllipsizedWidth() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.Layout
    public void draw(Canvas c, Path highlight, Paint highlightpaint, int cursorOffset) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.TextUtils.EllipsizeCallback
    public void ellipsized(int start, int end) {
        throw new RuntimeException("Stub!");
    }
}
