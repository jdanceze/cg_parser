package android.graphics.drawable;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/graphics/drawable/BitmapDrawable.class */
public class BitmapDrawable extends Drawable {
    @Deprecated
    public BitmapDrawable() {
        throw new RuntimeException("Stub!");
    }

    public BitmapDrawable(Resources res) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public BitmapDrawable(Bitmap bitmap) {
        throw new RuntimeException("Stub!");
    }

    public BitmapDrawable(Resources res, Bitmap bitmap) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public BitmapDrawable(String filepath) {
        throw new RuntimeException("Stub!");
    }

    public BitmapDrawable(Resources res, String filepath) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public BitmapDrawable(InputStream is) {
        throw new RuntimeException("Stub!");
    }

    public BitmapDrawable(Resources res, InputStream is) {
        throw new RuntimeException("Stub!");
    }

    public final Paint getPaint() {
        throw new RuntimeException("Stub!");
    }

    public final Bitmap getBitmap() {
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

    public int getGravity() {
        throw new RuntimeException("Stub!");
    }

    public void setGravity(int gravity) {
        throw new RuntimeException("Stub!");
    }

    public void setAntiAlias(boolean aa) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Drawable
    public void setFilterBitmap(boolean filter) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Drawable
    public void setDither(boolean dither) {
        throw new RuntimeException("Stub!");
    }

    public Shader.TileMode getTileModeX() {
        throw new RuntimeException("Stub!");
    }

    public Shader.TileMode getTileModeY() {
        throw new RuntimeException("Stub!");
    }

    public void setTileModeX(Shader.TileMode mode) {
        throw new RuntimeException("Stub!");
    }

    public final void setTileModeY(Shader.TileMode mode) {
        throw new RuntimeException("Stub!");
    }

    public void setTileModeXY(Shader.TileMode xmode, Shader.TileMode ymode) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Drawable
    public int getChangingConfigurations() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect bounds) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
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
    public Drawable mutate() {
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
    public int getOpacity() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Drawable
    public final Drawable.ConstantState getConstantState() {
        throw new RuntimeException("Stub!");
    }
}
