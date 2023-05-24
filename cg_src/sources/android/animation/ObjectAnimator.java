package android.animation;

import android.util.Property;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/animation/ObjectAnimator.class */
public final class ObjectAnimator extends ValueAnimator {
    public ObjectAnimator() {
        throw new RuntimeException("Stub!");
    }

    public void setPropertyName(String propertyName) {
        throw new RuntimeException("Stub!");
    }

    public void setProperty(Property property) {
        throw new RuntimeException("Stub!");
    }

    public String getPropertyName() {
        throw new RuntimeException("Stub!");
    }

    public static ObjectAnimator ofInt(Object target, String propertyName, int... values) {
        throw new RuntimeException("Stub!");
    }

    public static <T> ObjectAnimator ofInt(T target, Property<T, Integer> property, int... values) {
        throw new RuntimeException("Stub!");
    }

    public static ObjectAnimator ofFloat(Object target, String propertyName, float... values) {
        throw new RuntimeException("Stub!");
    }

    public static <T> ObjectAnimator ofFloat(T target, Property<T, Float> property, float... values) {
        throw new RuntimeException("Stub!");
    }

    public static ObjectAnimator ofObject(Object target, String propertyName, TypeEvaluator evaluator, Object... values) {
        throw new RuntimeException("Stub!");
    }

    public static <T, V> ObjectAnimator ofObject(T target, Property<T, V> property, TypeEvaluator<V> evaluator, V... values) {
        throw new RuntimeException("Stub!");
    }

    public static ObjectAnimator ofPropertyValuesHolder(Object target, PropertyValuesHolder... values) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.animation.ValueAnimator
    public void setIntValues(int... values) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.animation.ValueAnimator
    public void setFloatValues(float... values) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.animation.ValueAnimator
    public void setObjectValues(Object... values) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.animation.ValueAnimator, android.animation.Animator
    public void start() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.animation.ValueAnimator, android.animation.Animator
    public ObjectAnimator setDuration(long duration) {
        throw new RuntimeException("Stub!");
    }

    public Object getTarget() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.animation.Animator
    public void setTarget(Object target) {
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

    @Override // android.animation.ValueAnimator, android.animation.Animator
    /* renamed from: clone */
    public ObjectAnimator mo4clone() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.animation.ValueAnimator
    public String toString() {
        throw new RuntimeException("Stub!");
    }
}
