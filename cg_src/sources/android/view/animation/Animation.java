package android.view.animation;

import android.content.Context;
import android.util.AttributeSet;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/animation/Animation.class */
public abstract class Animation implements Cloneable {
    public static final int INFINITE = -1;
    public static final int RESTART = 1;
    public static final int REVERSE = 2;
    public static final int START_ON_FIRST_FRAME = -1;
    public static final int ABSOLUTE = 0;
    public static final int RELATIVE_TO_SELF = 1;
    public static final int RELATIVE_TO_PARENT = 2;
    public static final int ZORDER_NORMAL = 0;
    public static final int ZORDER_TOP = 1;
    public static final int ZORDER_BOTTOM = -1;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/animation/Animation$AnimationListener.class */
    public interface AnimationListener {
        void onAnimationStart(Animation animation);

        void onAnimationEnd(Animation animation);

        void onAnimationRepeat(Animation animation);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/animation/Animation$Description.class */
    protected static class Description {
        public int type;
        public float value;

        protected Description() {
            throw new RuntimeException("Stub!");
        }
    }

    public Animation() {
        throw new RuntimeException("Stub!");
    }

    public Animation(Context context, AttributeSet attrs) {
        throw new RuntimeException("Stub!");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // 
    /* renamed from: clone */
    public Animation mo258clone() throws CloneNotSupportedException {
        throw new RuntimeException("Stub!");
    }

    public void reset() {
        throw new RuntimeException("Stub!");
    }

    public void cancel() {
        throw new RuntimeException("Stub!");
    }

    public boolean isInitialized() {
        throw new RuntimeException("Stub!");
    }

    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        throw new RuntimeException("Stub!");
    }

    public void setInterpolator(Context context, int resID) {
        throw new RuntimeException("Stub!");
    }

    public void setInterpolator(Interpolator i) {
        throw new RuntimeException("Stub!");
    }

    public void setStartOffset(long startOffset) {
        throw new RuntimeException("Stub!");
    }

    public void setDuration(long durationMillis) {
        throw new RuntimeException("Stub!");
    }

    public void restrictDuration(long durationMillis) {
        throw new RuntimeException("Stub!");
    }

    public void scaleCurrentDuration(float scale) {
        throw new RuntimeException("Stub!");
    }

    public void setStartTime(long startTimeMillis) {
        throw new RuntimeException("Stub!");
    }

    public void start() {
        throw new RuntimeException("Stub!");
    }

    public void startNow() {
        throw new RuntimeException("Stub!");
    }

    public void setRepeatMode(int repeatMode) {
        throw new RuntimeException("Stub!");
    }

    public void setRepeatCount(int repeatCount) {
        throw new RuntimeException("Stub!");
    }

    public boolean isFillEnabled() {
        throw new RuntimeException("Stub!");
    }

    public void setFillEnabled(boolean fillEnabled) {
        throw new RuntimeException("Stub!");
    }

    public void setFillBefore(boolean fillBefore) {
        throw new RuntimeException("Stub!");
    }

    public void setFillAfter(boolean fillAfter) {
        throw new RuntimeException("Stub!");
    }

    public void setZAdjustment(int zAdjustment) {
        throw new RuntimeException("Stub!");
    }

    public void setBackgroundColor(int bg) {
        throw new RuntimeException("Stub!");
    }

    protected float getScaleFactor() {
        throw new RuntimeException("Stub!");
    }

    public void setDetachWallpaper(boolean detachWallpaper) {
        throw new RuntimeException("Stub!");
    }

    public Interpolator getInterpolator() {
        throw new RuntimeException("Stub!");
    }

    public long getStartTime() {
        throw new RuntimeException("Stub!");
    }

    public long getDuration() {
        throw new RuntimeException("Stub!");
    }

    public long getStartOffset() {
        throw new RuntimeException("Stub!");
    }

    public int getRepeatMode() {
        throw new RuntimeException("Stub!");
    }

    public int getRepeatCount() {
        throw new RuntimeException("Stub!");
    }

    public boolean getFillBefore() {
        throw new RuntimeException("Stub!");
    }

    public boolean getFillAfter() {
        throw new RuntimeException("Stub!");
    }

    public int getZAdjustment() {
        throw new RuntimeException("Stub!");
    }

    public int getBackgroundColor() {
        throw new RuntimeException("Stub!");
    }

    public boolean getDetachWallpaper() {
        throw new RuntimeException("Stub!");
    }

    public boolean willChangeTransformationMatrix() {
        throw new RuntimeException("Stub!");
    }

    public boolean willChangeBounds() {
        throw new RuntimeException("Stub!");
    }

    public void setAnimationListener(AnimationListener listener) {
        throw new RuntimeException("Stub!");
    }

    protected void ensureInterpolator() {
        throw new RuntimeException("Stub!");
    }

    public long computeDurationHint() {
        throw new RuntimeException("Stub!");
    }

    public boolean getTransformation(long currentTime, Transformation outTransformation) {
        throw new RuntimeException("Stub!");
    }

    public boolean getTransformation(long currentTime, Transformation outTransformation, float scale) {
        throw new RuntimeException("Stub!");
    }

    public boolean hasStarted() {
        throw new RuntimeException("Stub!");
    }

    public boolean hasEnded() {
        throw new RuntimeException("Stub!");
    }

    protected void applyTransformation(float interpolatedTime, Transformation t) {
        throw new RuntimeException("Stub!");
    }

    protected float resolveSize(int type, float value, int size, int parentSize) {
        throw new RuntimeException("Stub!");
    }

    protected void finalize() throws Throwable {
        throw new RuntimeException("Stub!");
    }
}
