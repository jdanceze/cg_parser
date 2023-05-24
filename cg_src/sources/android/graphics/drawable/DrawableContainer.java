package android.graphics.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/graphics/drawable/DrawableContainer.class */
public class DrawableContainer extends Drawable implements Drawable.Callback {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/graphics/drawable/DrawableContainer$DrawableContainerState.class */
    public static abstract class DrawableContainerState extends Drawable.ConstantState {
        DrawableContainerState() {
            throw new RuntimeException("Stub!");
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public int getChangingConfigurations() {
            throw new RuntimeException("Stub!");
        }

        public final int addChild(Drawable dr) {
            throw new RuntimeException("Stub!");
        }

        public final int getChildCount() {
            throw new RuntimeException("Stub!");
        }

        public final Drawable[] getChildren() {
            throw new RuntimeException("Stub!");
        }

        public final void setVariablePadding(boolean variable) {
            throw new RuntimeException("Stub!");
        }

        public final Rect getConstantPadding() {
            throw new RuntimeException("Stub!");
        }

        public final void setConstantSize(boolean constant) {
            throw new RuntimeException("Stub!");
        }

        public final boolean isConstantSize() {
            throw new RuntimeException("Stub!");
        }

        public final int getConstantWidth() {
            throw new RuntimeException("Stub!");
        }

        public final int getConstantHeight() {
            throw new RuntimeException("Stub!");
        }

        public final int getConstantMinimumWidth() {
            throw new RuntimeException("Stub!");
        }

        public final int getConstantMinimumHeight() {
            throw new RuntimeException("Stub!");
        }

        protected void computeConstantSize() {
            throw new RuntimeException("Stub!");
        }

        public final void setEnterFadeDuration(int duration) {
            throw new RuntimeException("Stub!");
        }

        public final int getEnterFadeDuration() {
            throw new RuntimeException("Stub!");
        }

        public final void setExitFadeDuration(int duration) {
            throw new RuntimeException("Stub!");
        }

        public final int getExitFadeDuration() {
            throw new RuntimeException("Stub!");
        }

        public final int getOpacity() {
            throw new RuntimeException("Stub!");
        }

        public final boolean isStateful() {
            throw new RuntimeException("Stub!");
        }

        public void growArray(int oldSize, int newSize) {
            throw new RuntimeException("Stub!");
        }

        public synchronized boolean canConstantState() {
            throw new RuntimeException("Stub!");
        }
    }

    public DrawableContainer() {
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
    public void setDither(boolean dither) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter cf) {
        throw new RuntimeException("Stub!");
    }

    public void setEnterFadeDuration(int ms) {
        throw new RuntimeException("Stub!");
    }

    public void setExitFadeDuration(int ms) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect bounds) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isStateful() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Drawable
    public void jumpToCurrentState() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Drawable
    protected boolean onStateChange(int[] state) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Drawable
    protected boolean onLevelChange(int level) {
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

    @Override // android.graphics.drawable.Drawable.Callback
    public void invalidateDrawable(Drawable who) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void unscheduleDrawable(Drawable who, Runnable what) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Drawable
    public boolean setVisible(boolean visible, boolean restart) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        throw new RuntimeException("Stub!");
    }

    public boolean selectDrawable(int idx) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable getCurrent() {
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

    protected void setConstantState(DrawableContainerState state) {
        throw new RuntimeException("Stub!");
    }
}
