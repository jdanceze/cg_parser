package org.powermock.reflect.internal.matcherstrategies;

import java.lang.reflect.Field;
import org.powermock.reflect.exceptions.FieldNotFoundException;
import soot.coffi.Instruction;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:powermock-reflect-2.0.9.jar:org/powermock/reflect/internal/matcherstrategies/FieldNameMatcherStrategy.class */
public class FieldNameMatcherStrategy extends FieldMatcherStrategy {
    private final String fieldName;

    public FieldNameMatcherStrategy(String fieldName) {
        if (fieldName == null || fieldName.equals("") || fieldName.startsWith(Instruction.argsep)) {
            throw new IllegalArgumentException("field name cannot be null.");
        }
        this.fieldName = fieldName;
    }

    @Override // org.powermock.reflect.internal.matcherstrategies.FieldMatcherStrategy
    public boolean matches(Field field) {
        return this.fieldName.equals(field.getName());
    }

    @Override // org.powermock.reflect.internal.matcherstrategies.FieldMatcherStrategy
    public void notFound(Class<?> type, boolean isInstanceField) throws FieldNotFoundException {
        Object[] objArr = new Object[3];
        objArr[0] = isInstanceField ? "instance" : Jimple.STATIC;
        objArr[1] = this.fieldName;
        objArr[2] = type.getName();
        throw new FieldNotFoundException(String.format("No %s field named \"%s\" could be found in the class hierarchy of %s.", objArr));
    }

    public String toString() {
        return "fieldName " + this.fieldName;
    }
}
