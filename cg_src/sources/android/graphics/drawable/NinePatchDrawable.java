package android.graphics.drawable;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/graphics/drawable/NinePatchDrawable.class */
public class NinePatchDrawable extends Drawable {
    @Deprecated
    public NinePatchDrawable(Bitmap bitmap, byte[] chunk, Rect padding, String srcName) {
        throw new RuntimeException("Stub!");
    }

    public NinePatchDrawable(Resources res, Bitmap bitmap, byte[] chunk, Rect padding, String srcName) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public NinePatchDrawable(NinePatch patch) {
        throw new RuntimeException("Stub!");
    }

    public NinePatchDrawable(Resources res, NinePatch patch) {
        throw new RuntimeException("Stub!");
    }

    public void setTargetDensity(Canvas canvas) {
        throw new RuntimeException("Stub!");
    }

    public void setTargetDensity(DisplayMetrics metrics) {
        throw new RuntimeException("Stub!");
    }

    public void setTargetDensity(int density) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Drawable
    public int getChangingConfigurations() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Drawable
    public boolean getPadding(Rect padding) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int alpha) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter cf) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Drawable
    public void setDither(boolean dither) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Drawable
    public void setFilterBitmap(boolean filter) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Drawable
    public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs) throws XmlPullParserException, IOException {
        throw new RuntimeException("Stub!");
    }

    public Paint getPaint() {
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
    public int getMinimumWidth() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Drawable
    public int getMinimumHeight() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Drawable
    public Region getTransparentRegion() {
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
