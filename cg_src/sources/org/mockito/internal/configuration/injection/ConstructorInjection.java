package org.mockito.internal.configuration.injection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/configuration/injection/ConstructorInjection.class */
public class ConstructorInjection extends MockInjectionStrategy {
    @Override // org.mockito.internal.configuration.injection.MockInjectionStrategy
    public boolean processInjection(Field field, Object fieldOwner, Set<Object> mockCandidates) {
        try {
            SimpleArgumentResolver simpleArgumentResolver = new SimpleArgumentResolver(mockCandidates);
            FieldInitializationReport report = new FieldInitializer(fieldOwner, field, simpleArgumentResolver).initialize();
            return report.fieldWasInitializedUsingContructorArgs();
        } catch (MockitoException e) {
            if (e.getCause() instanceof InvocationTargetException) {
                Throwable realCause = e.getCause().getCause();
                throw Reporter.fieldInitialisationThrewException(field, realCause);
            }
            return false;
        }
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/configuration/injection/ConstructorInjection$SimpleArgumentResolver.class */
    static class SimpleArgumentResolver implements FieldInitializer.ConstructorArgumentResolver {
        final Set<Object> objects;

        public SimpleArgumentResolver(Set<Object> objects) {
            this.objects = objects;
        }

        @Override // org.mockito.internal.util.reflection.FieldInitializer.ConstructorArgumentResolver
        public Object[] resolveTypeInstances(Class<?>... argTypes) {
            List<Object> argumentInstances = new ArrayList<>(argTypes.length);
            for (Class<?> argType : argTypes) {
                argumentInstances.add(objectThatIsAssignableFrom(argType));
            }
            return argumentInstances.toArray();
        }

        private Object objectThatIsAssignableFrom(Class<?> argType) {
            for (Object object : this.objects) {
                if (argType.isAssignableFrom(object.getClass())) {
                    return object;
                }
            }
            return null;
        }
    }
}
