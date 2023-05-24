package org.powermock.api.extension.listener;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.GenericMaster;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.mockito.internal.configuration.PowerMockitoInjectingAnnotationEngine;
import org.powermock.core.spi.listener.AnnotationEnablerListener;
import org.powermock.core.spi.support.AbstractPowerMockTestListenerBase;
import org.powermock.reflect.Whitebox;
/* loaded from: gencallgraphv3.jar:powermock-api-mockito2-2.0.9.jar:org/powermock/api/extension/listener/AnnotationEnabler.class */
public class AnnotationEnabler extends AbstractPowerMockTestListenerBase implements AnnotationEnablerListener {
    @Override // org.powermock.core.spi.support.AbstractPowerMockTestListenerBase, org.powermock.core.spi.PowerMockTestListener
    public void beforeTestMethod(Object testInstance, Method method, Object[] arguments) throws Exception {
        standardInject(testInstance);
        injectSpiesAndInjectToSetters(testInstance);
        injectCaptor(testInstance);
    }

    private void injectSpiesAndInjectToSetters(Object testInstance) {
        new PowerMockitoInjectingAnnotationEngine().process(testInstance.getClass(), testInstance);
    }

    private void injectCaptor(Object testInstance) throws Exception {
        Set<Field> fieldsAnnotatedWithCaptor = Whitebox.getFieldsAnnotatedWith(testInstance, Captor.class, new Class[0]);
        for (Field field : fieldsAnnotatedWithCaptor) {
            Object captor = processAnnotationOn((Captor) field.getAnnotation(Captor.class), field);
            field.set(testInstance, captor);
        }
    }

    private void standardInject(Object testInstance) throws IllegalAccessException {
        Set<Field> fields = Whitebox.getFieldsAnnotatedWith(testInstance, getMockAnnotations());
        for (Field field : fields) {
            if (field.get(testInstance) == null) {
                Class<?> type = field.getType();
                if (field.isAnnotationPresent(Mock.class)) {
                    Mock mockAnnotation = (Mock) field.getAnnotation(Mock.class);
                    MockSettings mockSettings = Mockito.withSettings();
                    Answers answers = mockAnnotation.answer();
                    if (answers != null) {
                        mockSettings.defaultAnswer(answers.get());
                    }
                    Class<?>[] extraInterfaces = mockAnnotation.extraInterfaces();
                    if (extraInterfaces != null && extraInterfaces.length > 0) {
                        mockSettings.extraInterfaces(extraInterfaces);
                    }
                    String name = mockAnnotation.name();
                    if (name != null && name.length() > 0) {
                        mockSettings.name(name);
                    }
                    field.set(testInstance, PowerMockito.mock(type, mockSettings));
                } else {
                    field.set(testInstance, PowerMockito.mock(type));
                }
            }
        }
    }

    @Override // org.powermock.core.spi.listener.AnnotationEnablerListener
    public Class<? extends Annotation>[] getMockAnnotations() {
        return new Class[]{Mock.class, Mock.class};
    }

    private Object processAnnotationOn(Captor annotation, Field field) {
        Class<?> type = field.getType();
        if (!ArgumentCaptor.class.isAssignableFrom(type)) {
            throw new MockitoException("@Captor field must be of the type ArgumentCaptor.\nField: '" + field.getName() + "' has wrong type\nFor info how to use @Captor annotations see examples in javadoc for MockitoAnnotations class.");
        }
        Class cls = new GenericMaster().getGenericType(field);
        return ArgumentCaptor.forClass(cls);
    }
}
