package org.powermock.reflect.internal.matcherstrategies;

import java.lang.reflect.Field;
import org.powermock.reflect.exceptions.FieldNotFoundException;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:powermock-reflect-2.0.9.jar:org/powermock/reflect/internal/matcherstrategies/FieldTypeMatcherStrategy.class */
public class FieldTypeMatcherStrategy extends FieldMatcherStrategy {
    final Class<?> expectedFieldType;

    public FieldTypeMatcherStrategy(Class<?> fieldType) {
        if (fieldType == null) {
            throw new IllegalArgumentException("field type cannot be null.");
        }
        this.expectedFieldType = fieldType;
    }

    @Override // org.powermock.reflect.internal.matcherstrategies.FieldMatcherStrategy
    public boolean matches(Field field) {
        return this.expectedFieldType.equals(field.getType());
    }

    @Override // org.powermock.reflect.internal.matcherstrategies.FieldMatcherStrategy
    public void notFound(Class<?> type, boolean isInstanceField) throws FieldNotFoundException {
        Object[] objArr = new Object[3];
        objArr[0] = isInstanceField ? "instance" : Jimple.STATIC;
        objArr[1] = this.expectedFieldType.getName();
        objArr[2] = type.getName();
        throw new FieldNotFoundException(String.format("No %s field of type \"%s\" could be found in the class hierarchy of %s.", objArr));
    }

    public String toString() {
        return "type " + this.expectedFieldType.getName();
    }
}
