package org.mockito.internal.util.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.util.Checks;
import org.mockito.plugins.MemberAccessor;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/reflection/InstanceField.class */
public class InstanceField {
    private final Field field;
    private final Object instance;
    private FieldReader fieldReader;

    public InstanceField(Field field, Object instance) {
        this.field = (Field) Checks.checkNotNull(field, "field");
        this.instance = Checks.checkNotNull(instance, "instance");
    }

    public Object read() {
        return reader().read();
    }

    public void set(Object value) {
        MemberAccessor accessor = Plugins.getMemberAccessor();
        try {
            accessor.set(this.field, this.instance, value);
        } catch (IllegalAccessException e) {
            throw new MockitoException("Access to " + this.field + " was denied", e);
        }
    }

    public boolean isNull() {
        return reader().isNull();
    }

    public boolean isAnnotatedBy(Class<? extends Annotation> annotationClass) {
        return this.field.isAnnotationPresent(annotationClass);
    }

    public boolean isSynthetic() {
        return this.field.isSynthetic();
    }

    public <A extends Annotation> A annotation(Class<A> annotationClass) {
        return (A) this.field.getAnnotation(annotationClass);
    }

    public Field jdkField() {
        return this.field;
    }

    private FieldReader reader() {
        if (this.fieldReader == null) {
            this.fieldReader = new FieldReader(this.instance, this.field);
        }
        return this.fieldReader;
    }

    public String name() {
        return this.field.getName();
    }

    public String toString() {
        return name();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InstanceField that = (InstanceField) o;
        return this.field.equals(that.field) && this.instance.equals(that.instance);
    }

    public int hashCode() {
        int result = this.field.hashCode();
        return (31 * result) + this.instance.hashCode();
    }
}
