package android.view.animation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import java.util.Random;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/animation/LayoutAnimationController.class */
public class LayoutAnimationController {
    public static final int ORDER_NORMAL = 0;
    public static final int ORDER_REVERSE = 1;
    public static final int ORDER_RANDOM = 2;
    protected Animation mAnimation;
    protected Random mRandomizer;
    protected Interpolator mInterpolator;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/animation/LayoutAnimationController$AnimationParameters.class */
    public static class AnimationParameters {
        public int count;
        public int index;

        public AnimationParameters() {
            throw new RuntimeException("Stub!");
        }
    }

    public LayoutAnimationController(Context context, AttributeSet attrs) {
        throw new RuntimeException("Stub!");
    }

    public LayoutAnimationController(Animation animation) {
        throw new RuntimeException("Stub!");
    }

    public LayoutAnimationController(Animation animation, float delay) {
        throw new RuntimeException("Stub!");
    }

    public int getOrder() {
        throw new RuntimeException("Stub!");
    }

    public void setOrder(int order) {
        throw new RuntimeException("Stub!");
    }

    public void setAnimation(Context context, int resourceID) {
        throw new RuntimeException("Stub!");
    }

    public void setAnimation(Animation animation) {
        throw new RuntimeException("Stub!");
    }

    public Animation getAnimation() {
        throw new RuntimeException("Stub!");
    }

    public void setInterpolator(Context context, int resourceID) {
        throw new RuntimeException("Stub!");
    }

    public void setInterpolator(Interpolator interpolator) {
        throw new RuntimeException("Stub!");
    }

    public Interpolator getInterpolator() {
        throw new RuntimeException("Stub!");
    }

    public float getDelay() {
        throw new RuntimeException("Stub!");
    }

    public void setDelay(float delay) {
        throw new RuntimeException("Stub!");
    }

    public boolean willOverlap() {
        throw new RuntimeException("Stub!");
    }

    public void start() {
        throw new RuntimeException("Stub!");
    }

    public final Animation getAnimationForView(View view) {
        throw new RuntimeException("Stub!");
    }

    public boolean isDone() {
        throw new RuntimeException("Stub!");
    }

    protected long getDelayForView(View view) {
        throw new RuntimeException("Stub!");
    }

    protected int getTransformedIndex(AnimationParameters params) {
        throw new RuntimeException("Stub!");
    }
}
