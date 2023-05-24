package android.animation;

import java.util.ArrayList;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/animation/Animator.class */
public abstract class Animator implements Cloneable {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/animation/Animator$AnimatorListener.class */
    public interface AnimatorListener {
        void onAnimationStart(Animator animator);

        void onAnimationEnd(Animator animator);

        void onAnimationCancel(Animator animator);

        void onAnimationRepeat(Animator animator);
    }

    public abstract long getStartDelay();

    public abstract void setStartDelay(long j);

    public abstract Animator setDuration(long j);

    public abstract long getDuration();

    public abstract void setInterpolator(TimeInterpolator timeInterpolator);

    public abstract boolean isRunning();

    public Animator() {
        throw new RuntimeException("Stub!");
    }

    public void start() {
        throw new RuntimeException("Stub!");
    }

    public void cancel() {
        throw new RuntimeException("Stub!");
    }

    public void end() {
        throw new RuntimeException("Stub!");
    }

    public boolean isStarted() {
        throw new RuntimeException("Stub!");
    }

    public void addListener(AnimatorListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void removeListener(AnimatorListener listener) {
        throw new RuntimeException("Stub!");
    }

    public ArrayList<AnimatorListener> getListeners() {
        throw new RuntimeException("Stub!");
    }

    public void removeAllListeners() {
        throw new RuntimeException("Stub!");
    }

    @Override // 
    /* renamed from: clone */
    public Animator mo4clone() {
        throw new RuntimeException("Stub!");
    }

    public void setupStartValues() {
        throw new RuntimeException("Stub!");
    }

    public void setupEndValues() {
        throw new RuntimeException("Stub!");
    }

    public void setTarget(Object target) {
        throw new RuntimeException("Stub!");
    }
}
