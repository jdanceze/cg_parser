package org.mockito.internal.configuration.injection;

import java.lang.reflect.Field;
import java.util.Set;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.FieldReader;
import org.mockito.plugins.MemberAccessor;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/configuration/injection/SpyOnInjectedFieldsHandler.class */
public class SpyOnInjectedFieldsHandler extends MockInjectionStrategy {
    private final MemberAccessor accessor = Plugins.getMemberAccessor();

    @Override // org.mockito.internal.configuration.injection.MockInjectionStrategy
    protected boolean processInjection(Field field, Object fieldOwner, Set<Object> mockCandidates) {
        FieldReader fieldReader = new FieldReader(fieldOwner, field);
        if (!fieldReader.isNull() && field.isAnnotationPresent(Spy.class)) {
            try {
                Object instance = fieldReader.read();
                if (MockUtil.isMock(instance)) {
                    Mockito.reset(instance);
                } else {
                    Object mock = Mockito.mock(instance.getClass(), Mockito.withSettings().spiedInstance(instance).defaultAnswer(Mockito.CALLS_REAL_METHODS).name(field.getName()));
                    this.accessor.set(field, fieldOwner, mock);
                }
                return false;
            } catch (Exception e) {
                throw new MockitoException("Problems initiating spied field " + field.getName(), e);
            }
        }
        return false;
    }
}
