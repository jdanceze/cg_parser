package org.mockito.internal.creation.bytebuddy;

import java.lang.reflect.Modifier;
import org.mockito.creation.instance.InstantiationException;
import org.mockito.creation.instance.Instantiator;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.util.Platform;
import org.mockito.internal.util.StringUtil;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.MockMaker;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/SubclassByteBuddyMockMaker.class */
public class SubclassByteBuddyMockMaker implements ClassCreatingMockMaker {
    private final BytecodeGenerator cachingMockBytecodeGenerator;

    public SubclassByteBuddyMockMaker() {
        this(new SubclassInjectionLoader());
    }

    public SubclassByteBuddyMockMaker(SubclassLoader loader) {
        this.cachingMockBytecodeGenerator = new TypeCachingBytecodeGenerator(new SubclassBytecodeGenerator(loader), false);
    }

    @Override // org.mockito.plugins.MockMaker
    public <T> T createMock(MockCreationSettings<T> settings, MockHandler handler) {
        Class<? extends T> mockedProxyType = createMockType(settings);
        Instantiator instantiator = Plugins.getInstantiatorProvider().getInstantiator(settings);
        Object obj = null;
        try {
            obj = instantiator.newInstance(mockedProxyType);
            MockAccess mockAccess = (MockAccess) obj;
            mockAccess.setMockitoInterceptor(new MockMethodInterceptor(handler, settings));
            return (T) ensureMockIsAssignableToMockedType(settings, obj);
        } catch (ClassCastException cce) {
            throw new MockitoException(StringUtil.join("ClassCastException occurred while creating the mockito mock :", "  class to mock : " + describeClass((Class<?>) settings.getTypeToMock()), "  created class : " + describeClass((Class<?>) mockedProxyType), "  proxy instance class : " + describeClass(obj), "  instance creation by : " + instantiator.getClass().getSimpleName(), "", "You might experience classloading issues, please ask the mockito mailing-list.", ""), cce);
        } catch (InstantiationException e) {
            throw new MockitoException("Unable to create mock instance of type '" + mockedProxyType.getSuperclass().getSimpleName() + "'", e);
        }
    }

    @Override // org.mockito.internal.creation.bytebuddy.ClassCreatingMockMaker
    public <T> Class<? extends T> createMockType(MockCreationSettings<T> settings) {
        try {
            return this.cachingMockBytecodeGenerator.mockClass(MockFeatures.withMockFeatures(settings.getTypeToMock(), settings.getExtraInterfaces(), settings.getSerializableMode(), settings.isStripAnnotations()));
        } catch (Exception bytecodeGenerationFailed) {
            throw prettifyFailure(settings, bytecodeGenerationFailed);
        }
    }

    private static <T> T ensureMockIsAssignableToMockedType(MockCreationSettings<T> settings, T mock) {
        Class<T> typeToMock = settings.getTypeToMock();
        return typeToMock.cast(mock);
    }

    private <T> RuntimeException prettifyFailure(MockCreationSettings<T> mockFeatures, Exception generationFailed) {
        String str;
        if (mockFeatures.getTypeToMock().isArray()) {
            throw new MockitoException(StringUtil.join("Mockito cannot mock arrays: " + mockFeatures.getTypeToMock() + ".", ""), generationFailed);
        }
        if (Modifier.isPrivate(mockFeatures.getTypeToMock().getModifiers())) {
            throw new MockitoException(StringUtil.join("Mockito cannot mock this class: " + mockFeatures.getTypeToMock() + ".", "Most likely it is due to mocking a private class that is not visible to Mockito", ""), generationFailed);
        }
        Object[] objArr = new Object[9];
        objArr[0] = "Mockito cannot mock this class: " + mockFeatures.getTypeToMock() + ".";
        objArr[1] = "";
        objArr[2] = "Mockito can only mock non-private & non-final classes.";
        objArr[3] = "If you're not sure why you're getting this error, please report to the mailing list.";
        objArr[4] = "";
        if (Platform.isJava8BelowUpdate45()) {
            str = "Java 8 early builds have bugs that were addressed in Java 1.8.0_45, please update your JDK!\n";
        } else {
            str = "";
        }
        objArr[5] = Platform.warnForVM("IBM J9 VM", "Early IBM virtual machine are known to have issues with Mockito, please upgrade to an up-to-date version.\n", "Hotspot", str);
        objArr[6] = Platform.describe();
        objArr[7] = "";
        objArr[8] = "Underlying exception : " + generationFailed;
        throw new MockitoException(StringUtil.join(objArr), generationFailed);
    }

    private static String describeClass(Class<?> type) {
        if (type == null) {
            return Jimple.NULL;
        }
        return "'" + type.getCanonicalName() + "', loaded by classloader : '" + type.getClassLoader() + "'";
    }

    private static String describeClass(Object instance) {
        return instance == null ? Jimple.NULL : describeClass(instance.getClass());
    }

    @Override // org.mockito.plugins.MockMaker
    public MockHandler getHandler(Object mock) {
        if (!(mock instanceof MockAccess)) {
            return null;
        }
        return ((MockAccess) mock).getMockitoInterceptor().getMockHandler();
    }

    @Override // org.mockito.plugins.MockMaker
    public void resetMock(Object mock, MockHandler newHandler, MockCreationSettings settings) {
        ((MockAccess) mock).setMockitoInterceptor(new MockMethodInterceptor(newHandler, settings));
    }

    @Override // org.mockito.plugins.MockMaker
    public MockMaker.TypeMockability isTypeMockable(final Class<?> type) {
        return new MockMaker.TypeMockability() { // from class: org.mockito.internal.creation.bytebuddy.SubclassByteBuddyMockMaker.1
            @Override // org.mockito.plugins.MockMaker.TypeMockability
            public boolean mockable() {
                return (type.isPrimitive() || Modifier.isFinal(type.getModifiers())) ? false : true;
            }

            @Override // org.mockito.plugins.MockMaker.TypeMockability
            public String nonMockableReason() {
                if (mockable()) {
                    return "";
                }
                if (type.isPrimitive()) {
                    return "primitive type";
                }
                return Modifier.isFinal(type.getModifiers()) ? "final class" : StringUtil.join("not handled type");
            }
        };
    }
}
