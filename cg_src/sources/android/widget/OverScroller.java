package android.widget;

import android.content.Context;
import android.view.animation.Interpolator;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/OverScroller.class */
public class OverScroller {
    public OverScroller(Context context) {
        throw new RuntimeException("Stub!");
    }

    public OverScroller(Context context, Interpolator interpolator) {
        throw new RuntimeException("Stub!");
    }

    public OverScroller(Context context, Interpolator interpolator, float bounceCoefficientX, float bounceCoefficientY) {
        throw new RuntimeException("Stub!");
    }

    public OverScroller(Context context, Interpolator interpolator, float bounceCoefficientX, float bounceCoefficientY, boolean flywheel) {
        throw new RuntimeException("Stub!");
    }

    public final void setFriction(float friction) {
        throw new RuntimeException("Stub!");
    }

    public final boolean isFinished() {
        throw new RuntimeException("Stub!");
    }

    public final void forceFinished(boolean finished) {
        throw new RuntimeException("Stub!");
    }

    public final int getCurrX() {
        throw new RuntimeException("Stub!");
    }

    public final int getCurrY() {
        throw new RuntimeException("Stub!");
    }

    public float getCurrVelocity() {
        throw new RuntimeException("Stub!");
    }

    public final int getStartX() {
        throw new RuntimeException("Stub!");
    }

    public final int getStartY() {
        throw new RuntimeException("Stub!");
    }

    public final int getFinalX() {
        throw new RuntimeException("Stub!");
    }

    public final int getFinalY() {
        throw new RuntimeException("Stub!");
    }

    public boolean computeScrollOffset() {
        throw new RuntimeException("Stub!");
    }

    public void startScroll(int startX, int startY, int dx, int dy) {
        throw new RuntimeException("Stub!");
    }

    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        throw new RuntimeException("Stub!");
    }

    public boolean springBack(int startX, int startY, int minX, int maxX, int minY, int maxY) {
        throw new RuntimeException("Stub!");
    }

    public void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY) {
        throw new RuntimeException("Stub!");
    }

    public void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY, int overX, int overY) {
        throw new RuntimeException("Stub!");
    }

    public void notifyHorizontalEdgeReached(int startX, int finalX, int overX) {
        throw new RuntimeException("Stub!");
    }

    public void notifyVerticalEdgeReached(int startY, int finalY, int overY) {
        throw new RuntimeException("Stub!");
    }

    public boolean isOverScrolled() {
        throw new RuntimeException("Stub!");
    }

    public void abortAnimation() {
        throw new RuntimeException("Stub!");
    }
}
