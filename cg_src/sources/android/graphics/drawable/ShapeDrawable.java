package android.graphics.drawable;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/graphics/drawable/ShapeDrawable.class */
public class ShapeDrawable extends Drawable {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/graphics/drawable/ShapeDrawable$ShaderFactory.class */
    public static abstract class ShaderFactory {
        public abstract Shader resize(int i, int i2);

        public ShaderFactory() {
            throw new RuntimeException("Stub!");
        }
    }

    public ShapeDrawable() {
        throw new RuntimeException("Stub!");
    }

    public ShapeDrawable(Shape s) {
        throw new RuntimeException("Stub!");
    }

    public Shape getShape() {
        throw new RuntimeException("Stub!");
    }

    public void setShape(Shape s) {
        throw new RuntimeException("Stub!");
    }

    public void setShaderFactory(ShaderFactory fact) {
        throw new RuntimeException("Stub!");
    }

    public ShaderFactory getShaderFactory() {
        throw new RuntimeException("Stub!");
    }

    public Paint getPaint() {
        throw new RuntimeException("Stub!");
    }

    public void setPadding(int left, int top, int right, int bottom) {
        throw new RuntimeException("Stub!");
    }

    public void setPadding(Rect padding) {
        throw new RuntimeException("Stub!");
    }

    public void setIntrinsicWidth(int width) {
        throw new RuntimeException("Stub!");
    }

    public void setIntrinsicHeight(int height) {
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
    public boolean getPadding(Rect padding) {
        throw new RuntimeException("Stub!");
    }

    protected void onDraw(Shape shape, Canvas canvas, Paint paint) {
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
    public void setAlpha(int alpha) {
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
    public void setDither(boolean dither) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect bounds) {
        throw new RuntimeException("Stub!");
    }

    protected boolean inflateTag(String name, Resources r, XmlPullParser parser, AttributeSet attrs) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Drawable
    public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs) throws XmlPullParserException, IOException {
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
