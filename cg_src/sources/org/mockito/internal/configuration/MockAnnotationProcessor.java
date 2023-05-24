package org.mockito.internal.configuration;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;
import org.mockito.Mock;
import org.mockito.MockSettings;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.StringUtil;
import org.mockito.internal.util.Supplier;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/configuration/MockAnnotationProcessor.class */
public class MockAnnotationProcessor implements FieldAnnotationProcessor<Mock> {
    @Override // org.mockito.internal.configuration.FieldAnnotationProcessor
    public Object process(Mock annotation, Field field) {
        Class<?> type = field.getType();
        Objects.requireNonNull(field);
        return processAnnotationForMock(annotation, type, this::getGenericType, field.getName());
    }

    public static Object processAnnotationForMock(Mock annotation, Class<?> type, Supplier<Type> genericType, String name) {
        MockSettings mockSettings = Mockito.withSettings();
        if (annotation.extraInterfaces().length > 0) {
            mockSettings.extraInterfaces(annotation.extraInterfaces());
        }
        if ("".equals(annotation.name())) {
            mockSettings.name(name);
        } else {
            mockSettings.name(annotation.name());
        }
        if (annotation.serializable()) {
            mockSettings.serializable();
        }
        if (annotation.stubOnly()) {
            mockSettings.stubOnly();
        }
        if (annotation.lenient()) {
            mockSettings.lenient();
        }
        mockSettings.defaultAnswer(annotation.answer());
        if (type == MockedStatic.class) {
            return Mockito.mockStatic(inferParameterizedType(genericType.get(), name, MockedStatic.class.getSimpleName()), mockSettings);
        }
        if (type == MockedConstruction.class) {
            return Mockito.mockConstruction(inferParameterizedType(genericType.get(), name, MockedConstruction.class.getSimpleName()), mockSettings);
        }
        return Mockito.mock(type, mockSettings);
    }

    static Class<?> inferParameterizedType(Type type, String name, String sort) {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type[] arguments = parameterizedType.getActualTypeArguments();
            if (arguments.length == 1 && (arguments[0] instanceof Class)) {
                return (Class) arguments[0];
            }
        }
        throw new MockitoException(StringUtil.join("Mockito cannot infer a static mock from a raw type for " + name, "", "Instead of @Mock " + sort + " you need to specify a parameterized type", "For example, if you would like to mock Sample.class, specify", "", "@Mock " + sort + "<Sample>", "", "as the type parameter. If the type is itself parameterized, it should be specified as raw type."));
    }
}
