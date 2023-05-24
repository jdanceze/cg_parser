package android.text;

import android.text.Layout;
import android.text.TextUtils;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/StaticLayout.class */
public class StaticLayout extends Layout {
    public StaticLayout(CharSequence source, TextPaint paint, int width, Layout.Alignment align, float spacingmult, float spacingadd, boolean includepad) {
        super(null, null, 0, null, 0.0f, 0.0f);
        throw new RuntimeException("Stub!");
    }

    public StaticLayout(CharSequence source, int bufstart, int bufend, TextPaint paint, int outerwidth, Layout.Alignment align, float spacingmult, float spacingadd, boolean includepad) {
        super(null, null, 0, null, 0.0f, 0.0f);
        throw new RuntimeException("Stub!");
    }

    public StaticLayout(CharSequence source, int bufstart, int bufend, TextPaint paint, int outerwidth, Layout.Alignment align, float spacingmult, float spacingadd, boolean includepad, TextUtils.TruncateAt ellipsize, int ellipsizedWidth) {
        super(null, null, 0, null, 0.0f, 0.0f);
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.Layout
    public int getLineForVertical(int vertical) {
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
}
