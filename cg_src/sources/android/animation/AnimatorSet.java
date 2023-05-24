package android.animation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/animation/AnimatorSet.class */
public final class AnimatorSet extends Animator {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/animation/AnimatorSet$Builder.class */
    public class Builder {
        Builder() {
            throw new RuntimeException("Stub!");
        }

        public Builder with(Animator anim) {
            throw new RuntimeException("Stub!");
        }

        public Builder before(Animator anim) {
            throw new RuntimeException("Stub!");
        }

        public Builder after(Animator anim) {
            throw new RuntimeException("Stub!");
        }

        public Builder after(long delay) {
            throw new RuntimeException("Stub!");
        }
    }

    public AnimatorSet() {
        throw new RuntimeException("Stub!");
    }

    public void playTogether(Animator... items) {
        throw new RuntimeException("Stub!");
    }

    public void playTogether(Collection<Animator> items) {
        throw new RuntimeException("Stub!");
    }

    public void playSequentially(Animator... items) {
        throw new RuntimeException("Stub!");
    }

    public void playSequentially(List<Animator> items) {
        throw new RuntimeException("Stub!");
    }

    public ArrayList<Animator> getChildAnimations() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.animation.Animator
    public void setTarget(Object target) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.animation.Animator
    public void setInterpolator(TimeInterpolator interpolator) {
        throw new RuntimeException("Stub!");
    }

    public Builder play(Animator anim) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.animation.Animator
    public void cancel() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.animation.Animator
    public void end() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.animation.Animator
    public boolean isRunning() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.animation.Animator
    public boolean isStarted() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.animation.Animator
    public long getStartDelay() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.animation.Animator
    public void setStartDelay(long startDelay) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.animation.Animator
    public long getDuration() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.animation.Animator
    public AnimatorSet setDuration(long duration) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.animation.Animator
    public void setupStartValues() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.animation.Animator
    public void setupEndValues() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.animation.Animator
    public void start() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.animation.Animator
    /* renamed from: clone */
    public AnimatorSet mo4clone() {
        throw new RuntimeException("Stub!");
    }
}
