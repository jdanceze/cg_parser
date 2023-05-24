package android.text;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/Layout.class */
public abstract class Layout {
    public static final int DIR_LEFT_TO_RIGHT = 1;
    public static final int DIR_RIGHT_TO_LEFT = -1;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/Layout$Alignment.class */
    public enum Alignment {
        ALIGN_CENTER,
        ALIGN_NORMAL,
        ALIGN_OPPOSITE
    }

    public abstract int getLineCount();

    public abstract int getLineTop(int i);

    public abstract int getLineDescent(int i);

    public abstract int getLineStart(int i);

    public abstract int getParagraphDirection(int i);

    public abstract boolean getLineContainsTab(int i);

    public abstract Directions getLineDirections(int i);

    public abstract int getTopPadding();

    public abstract int getBottomPadding();

    public abstract int getEllipsisStart(int i);

    public abstract int getEllipsisCount(int i);

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/Layout$Directions.class */
    public static class Directions {
        Directions() {
            throw new RuntimeException("Stub!");
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Layout(CharSequence text, TextPaint paint, int width, Alignment align, float spacingMult, float spacingAdd) {
        throw new RuntimeException("Stub!");
    }

    public static float getDesiredWidth(CharSequence source, TextPaint paint) {
        throw new RuntimeException("Stub!");
    }

    public static float getDesiredWidth(CharSequence source, int start, int end, TextPaint paint) {
        throw new RuntimeException("Stub!");
    }

    public void draw(Canvas c) {
        throw new RuntimeException("Stub!");
    }

    public void draw(Canvas canvas, Path highlight, Paint highlightPaint, int cursorOffsetVertical) {
        throw new RuntimeException("Stub!");
    }

    public final CharSequence getText() {
        throw new RuntimeException("Stub!");
    }

    public final TextPaint getPaint() {
        throw new RuntimeException("Stub!");
    }

    public final int getWidth() {
        throw new RuntimeException("Stub!");
    }

    public int getEllipsizedWidth() {
        throw new RuntimeException("Stub!");
    }

    public final void increaseWidthTo(int wid) {
        throw new RuntimeException("Stub!");
    }

    public int getHeight() {
        throw new RuntimeException("Stub!");
    }

    public final Alignment getAlignment() {
        throw new RuntimeException("Stub!");
    }

    public final float getSpacingMultiplier() {
        throw new RuntimeException("Stub!");
    }

    public final float getSpacingAdd() {
        throw new RuntimeException("Stub!");
    }

    public int getLineBounds(int line, Rect bounds) {
        throw new RuntimeException("Stub!");
    }

    public boolean isRtlCharAt(int offset) {
        throw new RuntimeException("Stub!");
    }

    public float getPrimaryHorizontal(int offset) {
        throw new RuntimeException("Stub!");
    }

    public float getSecondaryHorizontal(int offset) {
        throw new RuntimeException("Stub!");
    }

    public float getLineLeft(int line) {
        throw new RuntimeException("Stub!");
    }

    public float getLineRight(int line) {
        throw new RuntimeException("Stub!");
    }

    public float getLineMax(int line) {
        throw new RuntimeException("Stub!");
    }

    public float getLineWidth(int line) {
        throw new RuntimeException("Stub!");
    }

    public int getLineForVertical(int vertical) {
        throw new RuntimeException("Stub!");
    }

    public int getLineForOffset(int offset) {
        throw new RuntimeException("Stub!");
    }

    public int getOffsetForHorizontal(int line, float horiz) {
        throw new RuntimeException("Stub!");
    }

    public final int getLineEnd(int line) {
        throw new RuntimeException("Stub!");
    }

    public int getLineVisibleEnd(int line) {
        throw new RuntimeException("Stub!");
    }

    public final int getLineBottom(int line) {
        throw new RuntimeException("Stub!");
    }

    public final int getLineBaseline(int line) {
        throw new RuntimeException("Stub!");
    }

    public final int getLineAscent(int line) {
        throw new RuntimeException("Stub!");
    }

    public int getOffsetToLeftOf(int offset) {
        throw new RuntimeException("Stub!");
    }

    public int getOffsetToRightOf(int offset) {
        throw new RuntimeException("Stub!");
    }

    public void getCursorPath(int point, Path dest, CharSequence editingBuffer) {
        throw new RuntimeException("Stub!");
    }

    public void getSelectionPath(int start, int end, Path dest) {
        throw new RuntimeException("Stub!");
    }

    public final Alignment getParagraphAlignment(int line) {
        throw new RuntimeException("Stub!");
    }

    public final int getParagraphLeft(int line) {
        throw new RuntimeException("Stub!");
    }

    public final int getParagraphRight(int line) {
        throw new RuntimeException("Stub!");
    }

    protected final boolean isSpanned() {
        throw new RuntimeException("Stub!");
    }
}
