package org.powermock.reflect.internal.matcherstrategies;

import java.lang.reflect.Field;
import org.powermock.reflect.exceptions.FieldNotFoundException;
/* loaded from: gencallgraphv3.jar:powermock-reflect-2.0.9.jar:org/powermock/reflect/internal/matcherstrategies/FieldMatcherStrategy.class */
public abstract class FieldMatcherStrategy {
    public abstract boolean matches(Field field);

    public abstract void notFound(Class<?> cls, boolean z) throws FieldNotFoundException;
}
