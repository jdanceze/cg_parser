package android.graphics.drawable;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/graphics/drawable/GradientDrawable.class */
public class GradientDrawable extends Drawable {
    public static final int RECTANGLE = 0;
    public static final int OVAL = 1;
    public static final int LINE = 2;
    public static final int RING = 3;
    public static final int LINEAR_GRADIENT = 0;
    public static final int RADIAL_GRADIENT = 1;
    public static final int SWEEP_GRADIENT = 2;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/graphics/drawable/GradientDrawable$Orientation.class */
    public enum Orientation {
        BL_TR,
        BOTTOM_TOP,
        BR_TL,
        LEFT_RIGHT,
        RIGHT_LEFT,
        TL_BR,
        TOP_BOTTOM,
        TR_BL
    }

    public GradientDrawable() {
        throw new RuntimeException("Stub!");
    }

    public GradientDrawable(Orientation orientation, int[] colors) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Drawable
    public boolean getPadding(Rect padding) {
        throw new RuntimeException("Stub!");
    }

    public void setCornerRadii(float[] radii) {
        throw new RuntimeException("Stub!");
    }

    public void setCornerRadius(float radius) {
        throw new RuntimeException("Stub!");
    }

    public void setStroke(int width, int color) {
        throw new RuntimeException("Stub!");
    }

    public void setStroke(int width, int color, float dashWidth, float dashGap) {
        throw new RuntimeException("Stub!");
    }

    public void setSize(int width, int height) {
        throw new RuntimeException("Stub!");
    }

    public void setShape(int shape) {
        throw new RuntimeException("Stub!");
    }

    public void setGradientType(int gradient) {
        throw new RuntimeException("Stub!");
    }

    public void setGradientCenter(float x, float y) {
        throw new RuntimeException("Stub!");
    }

    public void setGradientRadius(float gradientRadius) {
        throw new RuntimeException("Stub!");
    }

    public void setUseLevel(boolean useLevel) {
        throw new RuntimeException("Stub!");
    }

    public Orientation getOrientation() {
        throw new RuntimeException("Stub!");
    }

    public void setOrientation(Orientation orientation) {
        throw new RuntimeException("Stub!");
    }

    public void setColors(int[] colors) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        throw new RuntimeException("Stub!");
    }

    public void setColor(int argb) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Drawable
    public int getChangingConfigurations() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int alpha) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Drawable
    public void setDither(boolean dither) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter cf) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect r) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Drawable
    protected boolean onLevelChange(int level) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Drawable
    public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs) throws XmlPullParserException, IOException {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable.ConstantState getConstantState() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable mutate() {
        throw new RuntimeException("Stub!");
    }
}
