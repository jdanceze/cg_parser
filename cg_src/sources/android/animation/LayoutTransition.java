package android.animation;

import android.view.View;
import android.view.ViewGroup;
import java.util.List;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/animation/LayoutTransition.class */
public class LayoutTransition {
    public static final int CHANGE_APPEARING = 0;
    public static final int CHANGE_DISAPPEARING = 1;
    public static final int APPEARING = 2;
    public static final int DISAPPEARING = 3;
    public static final int CHANGING = 4;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/animation/LayoutTransition$TransitionListener.class */
    public interface TransitionListener {
        void startTransition(LayoutTransition layoutTransition, ViewGroup viewGroup, View view, int i);

        void endTransition(LayoutTransition layoutTransition, ViewGroup viewGroup, View view, int i);
    }

    public LayoutTransition() {
        throw new RuntimeException("Stub!");
    }

    public void setDuration(long duration) {
        throw new RuntimeException("Stub!");
    }

    public void enableTransitionType(int transitionType) {
        throw new RuntimeException("Stub!");
    }

    public void disableTransitionType(int transitionType) {
        throw new RuntimeException("Stub!");
    }

    public boolean isTransitionTypeEnabled(int transitionType) {
        throw new RuntimeException("Stub!");
    }

    public void setStartDelay(int transitionType, long delay) {
        throw new RuntimeException("Stub!");
    }

    public long getStartDelay(int transitionType) {
        throw new RuntimeException("Stub!");
    }

    public void setDuration(int transitionType, long duration) {
        throw new RuntimeException("Stub!");
    }

    public long getDuration(int transitionType) {
        throw new RuntimeException("Stub!");
    }

    public void setStagger(int transitionType, long duration) {
        throw new RuntimeException("Stub!");
    }

    public long getStagger(int transitionType) {
        throw new RuntimeException("Stub!");
    }

    public void setInterpolator(int transitionType, TimeInterpolator interpolator) {
        throw new RuntimeException("Stub!");
    }

    public TimeInterpolator getInterpolator(int transitionType) {
        throw new RuntimeException("Stub!");
    }

    public void setAnimator(int transitionType, Animator animator) {
        throw new RuntimeException("Stub!");
    }

    public Animator getAnimator(int transitionType) {
        throw new RuntimeException("Stub!");
    }

    public void setAnimateParentHierarchy(boolean animateParentHierarchy) {
        throw new RuntimeException("Stub!");
    }

    public boolean isChangingLayout() {
        throw new RuntimeException("Stub!");
    }

    public boolean isRunning() {
        throw new RuntimeException("Stub!");
    }

    public void addChild(ViewGroup parent, View child) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void showChild(ViewGroup parent, View child) {
        throw new RuntimeException("Stub!");
    }

    public void showChild(ViewGroup parent, View child, int oldVisibility) {
        throw new RuntimeException("Stub!");
    }

    public void removeChild(ViewGroup parent, View child) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void hideChild(ViewGroup parent, View child) {
        throw new RuntimeException("Stub!");
    }

    public void hideChild(ViewGroup parent, View child, int newVisibility) {
        throw new RuntimeException("Stub!");
    }

    public void addTransitionListener(TransitionListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void removeTransitionListener(TransitionListener listener) {
        throw new RuntimeException("Stub!");
    }

    public List<TransitionListener> getTransitionListeners() {
        throw new RuntimeException("Stub!");
    }
}
