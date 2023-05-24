package org.junit.runners.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runners/model/FrameworkField.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runners/model/FrameworkField.class */
public class FrameworkField extends FrameworkMember<FrameworkField> {
    private final Field field;

    public FrameworkField(Field field) {
        if (field == null) {
            throw new NullPointerException("FrameworkField cannot be created without an underlying field.");
        }
        this.field = field;
        if (isPublic()) {
            try {
                field.setAccessible(true);
            } catch (SecurityException e) {
            }
        }
    }

    @Override // org.junit.runners.model.FrameworkMember
    public String getName() {
        return getField().getName();
    }

    @Override // org.junit.runners.model.Annotatable
    public Annotation[] getAnnotations() {
        return this.field.getAnnotations();
    }

    @Override // org.junit.runners.model.Annotatable
    public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
        return (T) this.field.getAnnotation(annotationType);
    }

    @Override // org.junit.runners.model.FrameworkMember
    public boolean isShadowedBy(FrameworkField otherMember) {
        return otherMember.getName().equals(getName());
    }

    @Override // org.junit.runners.model.FrameworkMember
    boolean isBridgeMethod() {
        return false;
    }

    @Override // org.junit.runners.model.FrameworkMember
    protected int getModifiers() {
        return this.field.getModifiers();
    }

    public Field getField() {
        return this.field;
    }

    @Override // org.junit.runners.model.FrameworkMember
    public Class<?> getType() {
        return this.field.getType();
    }

    @Override // org.junit.runners.model.FrameworkMember
    public Class<?> getDeclaringClass() {
        return this.field.getDeclaringClass();
    }

    public Object get(Object target) throws IllegalArgumentException, IllegalAccessException {
        return this.field.get(target);
    }

    public String toString() {
        return this.field.toString();
    }
}
