package android.graphics;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/graphics/Paint.class */
public class Paint {
    public static final int ANTI_ALIAS_FLAG = 1;
    public static final int FILTER_BITMAP_FLAG = 2;
    public static final int DITHER_FLAG = 4;
    public static final int UNDERLINE_TEXT_FLAG = 8;
    public static final int STRIKE_THRU_TEXT_FLAG = 16;
    public static final int FAKE_BOLD_TEXT_FLAG = 32;
    public static final int LINEAR_TEXT_FLAG = 64;
    public static final int SUBPIXEL_TEXT_FLAG = 128;
    public static final int DEV_KERN_TEXT_FLAG = 256;
    public static final int HINTING_OFF = 0;
    public static final int HINTING_ON = 1;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/graphics/Paint$Align.class */
    public enum Align {
        CENTER,
        LEFT,
        RIGHT
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/graphics/Paint$Cap.class */
    public enum Cap {
        BUTT,
        ROUND,
        SQUARE
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/graphics/Paint$Join.class */
    public enum Join {
        BEVEL,
        MITER,
        ROUND
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/graphics/Paint$Style.class */
    public enum Style {
        FILL,
        FILL_AND_STROKE,
        STROKE
    }

    public native int getFlags();

    public native void setFlags(int i);

    public native int getHinting();

    public native void setHinting(int i);

    public native void setAntiAlias(boolean z);

    public native void setDither(boolean z);

    @Deprecated
    public native void setLinearText(boolean z);

    public native void setSubpixelText(boolean z);

    public native void setUnderlineText(boolean z);

    public native void setStrikeThruText(boolean z);

    public native void setFakeBoldText(boolean z);

    public native void setFilterBitmap(boolean z);

    public native int getColor();

    public native void setColor(int i);

    public native int getAlpha();

    public native void setAlpha(int i);

    public native float getStrokeWidth();

    public native void setStrokeWidth(float f);

    public native float getStrokeMiter();

    public native void setStrokeMiter(float f);

    public native float getTextSize();

    public native void setTextSize(float f);

    public native float getTextScaleX();

    public native void setTextScaleX(float f);

    public native float getTextSkewX();

    public native void setTextSkewX(float f);

    public native float ascent();

    public native float descent();

    public native float getFontMetrics(FontMetrics fontMetrics);

    public native int getFontMetricsInt(FontMetricsInt fontMetricsInt);

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/graphics/Paint$FontMetrics.class */
    public static class FontMetrics {
        public float top;
        public float ascent;
        public float descent;
        public float bottom;
        public float leading;

        public FontMetrics() {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/graphics/Paint$FontMetricsInt.class */
    public static class FontMetricsInt {
        public int top;
        public int ascent;
        public int descent;
        public int bottom;
        public int leading;

        public FontMetricsInt() {
            throw new RuntimeException("Stub!");
        }

        public String toString() {
            throw new RuntimeException("Stub!");
        }
    }

    public Paint() {
        throw new RuntimeException("Stub!");
    }

    public Paint(int flags) {
        throw new RuntimeException("Stub!");
    }

    public Paint(Paint paint) {
        throw new RuntimeException("Stub!");
    }

    public void reset() {
        throw new RuntimeException("Stub!");
    }

    public void set(Paint src) {
        throw new RuntimeException("Stub!");
    }

    public final boolean isAntiAlias() {
        throw new RuntimeException("Stub!");
    }

    public final boolean isDither() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public final boolean isLinearText() {
        throw new RuntimeException("Stub!");
    }

    public final boolean isSubpixelText() {
        throw new RuntimeException("Stub!");
    }

    public final boolean isUnderlineText() {
        throw new RuntimeException("Stub!");
    }

    public final boolean isStrikeThruText() {
        throw new RuntimeException("Stub!");
    }

    public final boolean isFakeBoldText() {
        throw new RuntimeException("Stub!");
    }

    public final boolean isFilterBitmap() {
        throw new RuntimeException("Stub!");
    }

    public Style getStyle() {
        throw new RuntimeException("Stub!");
    }

    public void setStyle(Style style) {
        throw new RuntimeException("Stub!");
    }

    public void setARGB(int a, int r, int g, int b) {
        throw new RuntimeException("Stub!");
    }

    public Cap getStrokeCap() {
        throw new RuntimeException("Stub!");
    }

    public void setStrokeCap(Cap cap) {
        throw new RuntimeException("Stub!");
    }

    public Join getStrokeJoin() {
        throw new RuntimeException("Stub!");
    }

    public void setStrokeJoin(Join join) {
        throw new RuntimeException("Stub!");
    }

    public boolean getFillPath(Path src, Path dst) {
        throw new RuntimeException("Stub!");
    }

    public Shader getShader() {
        throw new RuntimeException("Stub!");
    }

    public Shader setShader(Shader shader) {
        throw new RuntimeException("Stub!");
    }

    public ColorFilter getColorFilter() {
        throw new RuntimeException("Stub!");
    }

    public ColorFilter setColorFilter(ColorFilter filter) {
        throw new RuntimeException("Stub!");
    }

    public Xfermode getXfermode() {
        throw new RuntimeException("Stub!");
    }

    public Xfermode setXfermode(Xfermode xfermode) {
        throw new RuntimeException("Stub!");
    }

    public PathEffect getPathEffect() {
        throw new RuntimeException("Stub!");
    }

    public PathEffect setPathEffect(PathEffect effect) {
        throw new RuntimeException("Stub!");
    }

    public MaskFilter getMaskFilter() {
        throw new RuntimeException("Stub!");
    }

    public MaskFilter setMaskFilter(MaskFilter maskfilter) {
        throw new RuntimeException("Stub!");
    }

    public Typeface getTypeface() {
        throw new RuntimeException("Stub!");
    }

    public Typeface setTypeface(Typeface typeface) {
        throw new RuntimeException("Stub!");
    }

    public Rasterizer getRasterizer() {
        throw new RuntimeException("Stub!");
    }

    public Rasterizer setRasterizer(Rasterizer rasterizer) {
        throw new RuntimeException("Stub!");
    }

    public void setShadowLayer(float radius, float dx, float dy, int color) {
        throw new RuntimeException("Stub!");
    }

    public void clearShadowLayer() {
        throw new RuntimeException("Stub!");
    }

    public Align getTextAlign() {
        throw new RuntimeException("Stub!");
    }

    public void setTextAlign(Align align) {
        throw new RuntimeException("Stub!");
    }

    public FontMetrics getFontMetrics() {
        throw new RuntimeException("Stub!");
    }

    public FontMetricsInt getFontMetricsInt() {
        throw new RuntimeException("Stub!");
    }

    public float getFontSpacing() {
        throw new RuntimeException("Stub!");
    }

    public float measureText(char[] text, int index, int count) {
        throw new RuntimeException("Stub!");
    }

    public float measureText(String text, int start, int end) {
        throw new RuntimeException("Stub!");
    }

    public float measureText(String text) {
        throw new RuntimeException("Stub!");
    }

    public float measureText(CharSequence text, int start, int end) {
        throw new RuntimeException("Stub!");
    }

    public int breakText(char[] text, int index, int count, float maxWidth, float[] measuredWidth) {
        throw new RuntimeException("Stub!");
    }

    public int breakText(CharSequence text, int start, int end, boolean measureForwards, float maxWidth, float[] measuredWidth) {
        throw new RuntimeException("Stub!");
    }

    public int breakText(String text, boolean measureForwards, float maxWidth, float[] measuredWidth) {
        throw new RuntimeException("Stub!");
    }

    public int getTextWidths(char[] text, int index, int count, float[] widths) {
        throw new RuntimeException("Stub!");
    }

    public int getTextWidths(CharSequence text, int start, int end, float[] widths) {
        throw new RuntimeException("Stub!");
    }

    public int getTextWidths(String text, int start, int end, float[] widths) {
        throw new RuntimeException("Stub!");
    }

    public int getTextWidths(String text, float[] widths) {
        throw new RuntimeException("Stub!");
    }

    public void getTextPath(char[] text, int index, int count, float x, float y, Path path) {
        throw new RuntimeException("Stub!");
    }

    public void getTextPath(String text, int start, int end, float x, float y, Path path) {
        throw new RuntimeException("Stub!");
    }

    public void getTextBounds(String text, int start, int end, Rect bounds) {
        throw new RuntimeException("Stub!");
    }

    public void getTextBounds(char[] text, int index, int count, Rect bounds) {
        throw new RuntimeException("Stub!");
    }

    protected void finalize() throws Throwable {
        throw new RuntimeException("Stub!");
    }
}
