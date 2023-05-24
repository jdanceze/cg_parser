package android.animation;

import android.util.Property;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/animation/PropertyValuesHolder.class */
public class PropertyValuesHolder implements Cloneable {
    PropertyValuesHolder() {
        throw new RuntimeException("Stub!");
    }

    public static PropertyValuesHolder ofInt(String propertyName, int... values) {
        throw new RuntimeException("Stub!");
    }

    public static PropertyValuesHolder ofInt(Property<?, Integer> property, int... values) {
        throw new RuntimeException("Stub!");
    }

    public static PropertyValuesHolder ofFloat(String propertyName, float... values) {
        throw new RuntimeException("Stub!");
    }

    public static PropertyValuesHolder ofFloat(Property<?, Float> property, float... values) {
        throw new RuntimeException("Stub!");
    }

    public static PropertyValuesHolder ofObject(String propertyName, TypeEvaluator evaluator, Object... values) {
        throw new RuntimeException("Stub!");
    }

    public static <V> PropertyValuesHolder ofObject(Property property, TypeEvaluator<V> evaluator, V... values) {
        throw new RuntimeException("Stub!");
    }

    public static PropertyValuesHolder ofKeyframe(String propertyName, Keyframe... values) {
        throw new RuntimeException("Stub!");
    }

    public static PropertyValuesHolder ofKeyframe(Property property, Keyframe... values) {
        throw new RuntimeException("Stub!");
    }

    public void setIntValues(int... values) {
        throw new RuntimeException("Stub!");
    }

    public void setFloatValues(float... values) {
        throw new RuntimeException("Stub!");
    }

    public void setKeyframes(Keyframe... values) {
        throw new RuntimeException("Stub!");
    }

    public void setObjectValues(Object... values) {
        throw new RuntimeException("Stub!");
    }

    /* renamed from: clone */
    public PropertyValuesHolder m6clone() {
        throw new RuntimeException("Stub!");
    }

    public void setEvaluator(TypeEvaluator evaluator) {
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

    public String toString() {
        throw new RuntimeException("Stub!");
    }
}
