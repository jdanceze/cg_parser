package org.mockito.internal.configuration;

import java.lang.reflect.Field;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.GenericMaster;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/configuration/CaptorAnnotationProcessor.class */
public class CaptorAnnotationProcessor implements FieldAnnotationProcessor<Captor> {
    @Override // org.mockito.internal.configuration.FieldAnnotationProcessor
    public Object process(Captor annotation, Field field) {
        Class<?> type = field.getType();
        if (!ArgumentCaptor.class.isAssignableFrom(type)) {
            throw new MockitoException("@Captor field must be of the type ArgumentCaptor.\nField: '" + field.getName() + "' has wrong type\nFor info how to use @Captor annotations see examples in javadoc for MockitoAnnotations class.");
        }
        Class<?> cls = new GenericMaster().getGenericType(field);
        return ArgumentCaptor.forClass(cls);
    }
}
