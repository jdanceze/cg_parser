package org.powermock.reflect.internal.matcherstrategies;

import java.lang.reflect.Field;
import org.powermock.reflect.exceptions.FieldNotFoundException;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:powermock-reflect-2.0.9.jar:org/powermock/reflect/internal/matcherstrategies/AllFieldsMatcherStrategy.class */
public class AllFieldsMatcherStrategy extends FieldMatcherStrategy {
    @Override // org.powermock.reflect.internal.matcherstrategies.FieldMatcherStrategy
    public boolean matches(Field field) {
        return true;
    }

    @Override // org.powermock.reflect.internal.matcherstrategies.FieldMatcherStrategy
    public void notFound(Class<?> type, boolean isInstanceField) throws FieldNotFoundException {
        Object[] objArr = new Object[2];
        objArr[0] = isInstanceField ? "instance" : Jimple.STATIC;
        objArr[1] = type.getName();
        throw new FieldNotFoundException(String.format("No %s fields were declared in %s.", objArr));
    }
}
