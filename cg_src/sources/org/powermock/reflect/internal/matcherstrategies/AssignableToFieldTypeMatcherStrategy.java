package org.powermock.reflect.internal.matcherstrategies;

import java.lang.reflect.Field;
/* loaded from: gencallgraphv3.jar:powermock-reflect-2.0.9.jar:org/powermock/reflect/internal/matcherstrategies/AssignableToFieldTypeMatcherStrategy.class */
public class AssignableToFieldTypeMatcherStrategy extends FieldTypeMatcherStrategy {
    public AssignableToFieldTypeMatcherStrategy(Class<?> fieldType) {
        super(fieldType);
    }

    @Override // org.powermock.reflect.internal.matcherstrategies.FieldTypeMatcherStrategy, org.powermock.reflect.internal.matcherstrategies.FieldMatcherStrategy
    public boolean matches(Field field) {
        return this.expectedFieldType.isAssignableFrom(field.getType());
    }
}
