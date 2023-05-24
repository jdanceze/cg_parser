package android.graphics.drawable.shapes;

import android.graphics.Canvas;
import android.graphics.Paint;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/graphics/drawable/shapes/Shape.class */
public abstract class Shape implements Cloneable {
    public abstract void draw(Canvas canvas, Paint paint);

    public Shape() {
        throw new RuntimeException("Stub!");
    }

    public final float getWidth() {
        throw new RuntimeException("Stub!");
    }

    public final float getHeight() {
        throw new RuntimeException("Stub!");
    }

    public final void resize(float width, float height) {
        throw new RuntimeException("Stub!");
    }

    public boolean hasAlpha() {
        throw new RuntimeException("Stub!");
    }

    protected void onResize(float width, float height) {
        throw new RuntimeException("Stub!");
    }

    @Override // 
    /* renamed from: clone */
    public Shape mo89clone() throws CloneNotSupportedException {
        throw new RuntimeException("Stub!");
    }
}
