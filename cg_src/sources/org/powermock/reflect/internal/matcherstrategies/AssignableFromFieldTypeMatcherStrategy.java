package org.powermock.reflect.internal.matcherstrategies;

import java.lang.reflect.Field;
import org.powermock.reflect.exceptions.FieldNotFoundException;
import org.powermock.reflect.internal.primitivesupport.PrimitiveWrapper;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:powermock-reflect-2.0.9.jar:org/powermock/reflect/internal/matcherstrategies/AssignableFromFieldTypeMatcherStrategy.class */
public class AssignableFromFieldTypeMatcherStrategy extends FieldTypeMatcherStrategy {
    private final Class<?> primitiveCounterpart;

    public AssignableFromFieldTypeMatcherStrategy(Class<?> fieldType) {
        super(fieldType);
        this.primitiveCounterpart = PrimitiveWrapper.getPrimitiveFromWrapperType(this.expectedFieldType);
    }

    @Override // org.powermock.reflect.internal.matcherstrategies.FieldTypeMatcherStrategy, org.powermock.reflect.internal.matcherstrategies.FieldMatcherStrategy
    public boolean matches(Field field) {
        Class<?> actualFieldType = field.getType();
        return actualFieldType.isAssignableFrom(this.expectedFieldType) || (this.primitiveCounterpart != null && actualFieldType.isAssignableFrom(this.primitiveCounterpart));
    }

    @Override // org.powermock.reflect.internal.matcherstrategies.FieldTypeMatcherStrategy, org.powermock.reflect.internal.matcherstrategies.FieldMatcherStrategy
    public void notFound(Class<?> type, boolean isInstanceField) throws FieldNotFoundException {
        Object[] objArr = new Object[3];
        objArr[0] = isInstanceField ? "instance" : Jimple.STATIC;
        objArr[1] = this.expectedFieldType.getName();
        objArr[2] = type.getName();
        throw new FieldNotFoundException(String.format("No %s field assignable from \"%s\" could be found in the class hierarchy of %s.", objArr));
    }

    @Override // org.powermock.reflect.internal.matcherstrategies.FieldTypeMatcherStrategy
    public String toString() {
        return "type " + (this.primitiveCounterpart == null ? this.expectedFieldType.getName() : this.primitiveCounterpart.getName());
    }
}
