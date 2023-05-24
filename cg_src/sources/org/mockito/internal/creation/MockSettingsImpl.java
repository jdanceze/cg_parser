package org.mockito.internal.creation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.mockito.MockSettings;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.debugging.VerboseMockInvocationLogger;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.internal.util.Checks;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockNameImpl;
import org.mockito.internal.util.collections.Sets;
import org.mockito.listeners.InvocationListener;
import org.mockito.listeners.StubbingLookupListener;
import org.mockito.listeners.VerificationStartedListener;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.MockName;
import org.mockito.mock.SerializableMode;
import org.mockito.stubbing.Answer;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/MockSettingsImpl.class */
public class MockSettingsImpl<T> extends CreationSettings<T> implements MockSettings, MockCreationSettings<T> {
    private static final long serialVersionUID = 4475297236197939569L;
    private boolean useConstructor;
    private Object outerClassInstance;
    private Object[] constructorArgs;

    @Override // org.mockito.MockSettings
    public MockSettings serializable() {
        return serializable(SerializableMode.BASIC);
    }

    @Override // org.mockito.MockSettings
    public MockSettings serializable(SerializableMode mode) {
        this.serializableMode = mode;
        return this;
    }

    @Override // org.mockito.MockSettings
    public MockSettings extraInterfaces(Class<?>... extraInterfaces) {
        if (extraInterfaces == null || extraInterfaces.length == 0) {
            throw Reporter.extraInterfacesRequiresAtLeastOneInterface();
        }
        for (Class<?> i : extraInterfaces) {
            if (i == null) {
                throw Reporter.extraInterfacesDoesNotAcceptNullParameters();
            }
            if (!i.isInterface()) {
                throw Reporter.extraInterfacesAcceptsOnlyInterfaces(i);
            }
        }
        this.extraInterfaces = Sets.newSet(extraInterfaces);
        return this;
    }

    @Override // org.mockito.internal.creation.settings.CreationSettings, org.mockito.mock.MockCreationSettings
    public MockName getMockName() {
        return this.mockName;
    }

    @Override // org.mockito.internal.creation.settings.CreationSettings, org.mockito.mock.MockCreationSettings
    public Set<Class<?>> getExtraInterfaces() {
        return this.extraInterfaces;
    }

    @Override // org.mockito.internal.creation.settings.CreationSettings, org.mockito.mock.MockCreationSettings
    public Object getSpiedInstance() {
        return this.spiedInstance;
    }

    @Override // org.mockito.MockSettings
    public MockSettings name(String name) {
        this.name = name;
        return this;
    }

    @Override // org.mockito.MockSettings
    public MockSettings spiedInstance(Object spiedInstance) {
        this.spiedInstance = spiedInstance;
        return this;
    }

    @Override // org.mockito.MockSettings
    public MockSettings defaultAnswer(Answer defaultAnswer) {
        this.defaultAnswer = defaultAnswer;
        if (defaultAnswer == null) {
            throw Reporter.defaultAnswerDoesNotAcceptNullParameter();
        }
        return this;
    }

    @Override // org.mockito.internal.creation.settings.CreationSettings, org.mockito.mock.MockCreationSettings
    public Answer<Object> getDefaultAnswer() {
        return this.defaultAnswer;
    }

    @Override // org.mockito.MockSettings
    public MockSettingsImpl<T> stubOnly() {
        this.stubOnly = true;
        return this;
    }

    @Override // org.mockito.MockSettings
    public MockSettings useConstructor(Object... constructorArgs) {
        Checks.checkNotNull(constructorArgs, "constructorArgs", "If you need to pass null, please cast it to the right type, e.g.: useConstructor((String) null)");
        this.useConstructor = true;
        this.constructorArgs = constructorArgs;
        return this;
    }

    @Override // org.mockito.MockSettings
    public MockSettings outerInstance(Object outerClassInstance) {
        this.outerClassInstance = outerClassInstance;
        return this;
    }

    @Override // org.mockito.MockSettings
    public MockSettings withoutAnnotations() {
        this.stripAnnotations = true;
        return this;
    }

    @Override // org.mockito.internal.creation.settings.CreationSettings, org.mockito.mock.MockCreationSettings
    public boolean isUsingConstructor() {
        return this.useConstructor;
    }

    @Override // org.mockito.internal.creation.settings.CreationSettings, org.mockito.mock.MockCreationSettings
    public Object getOuterClassInstance() {
        return this.outerClassInstance;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.mockito.internal.creation.settings.CreationSettings, org.mockito.mock.MockCreationSettings
    public Object[] getConstructorArgs() {
        if (this.outerClassInstance == null) {
            return this.constructorArgs;
        }
        List<Object> resultArgs = new ArrayList<>(this.constructorArgs.length + 1);
        resultArgs.add(this.outerClassInstance);
        resultArgs.addAll(Arrays.asList(this.constructorArgs));
        return resultArgs.toArray(new Object[this.constructorArgs.length + 1]);
    }

    @Override // org.mockito.internal.creation.settings.CreationSettings, org.mockito.mock.MockCreationSettings
    public boolean isStubOnly() {
        return this.stubOnly;
    }

    @Override // org.mockito.MockSettings
    public MockSettings verboseLogging() {
        if (!invocationListenersContainsType(VerboseMockInvocationLogger.class)) {
            invocationListeners(new VerboseMockInvocationLogger());
        }
        return this;
    }

    @Override // org.mockito.MockSettings
    public MockSettings invocationListeners(InvocationListener... listeners) {
        addListeners(listeners, this.invocationListeners, "invocationListeners");
        return this;
    }

    @Override // org.mockito.MockSettings
    public MockSettings stubbingLookupListeners(StubbingLookupListener... listeners) {
        addListeners(listeners, this.stubbingLookupListeners, "stubbingLookupListeners");
        return this;
    }

    static <T> void addListeners(T[] listeners, List<T> container, String method) {
        if (listeners == null) {
            throw Reporter.methodDoesNotAcceptParameter(method, "null vararg array.");
        }
        if (listeners.length == 0) {
            throw Reporter.requiresAtLeastOneListener(method);
        }
        for (T listener : listeners) {
            if (listener == null) {
                throw Reporter.methodDoesNotAcceptParameter(method, "null listeners.");
            }
            container.add(listener);
        }
    }

    @Override // org.mockito.MockSettings
    public MockSettings verificationStartedListeners(VerificationStartedListener... listeners) {
        addListeners(listeners, this.verificationStartedListeners, "verificationStartedListeners");
        return this;
    }

    private boolean invocationListenersContainsType(Class<?> clazz) {
        for (InvocationListener listener : this.invocationListeners) {
            if (listener.getClass().equals(clazz)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasInvocationListeners() {
        return !getInvocationListeners().isEmpty();
    }

    @Override // org.mockito.internal.creation.settings.CreationSettings, org.mockito.mock.MockCreationSettings
    public Class<T> getTypeToMock() {
        return this.typeToMock;
    }

    @Override // org.mockito.MockSettings
    public <T> MockCreationSettings<T> build(Class<T> typeToMock) {
        return validatedSettings(typeToMock, this);
    }

    @Override // org.mockito.MockSettings
    public <T> MockCreationSettings<T> buildStatic(Class<T> classToMock) {
        return validatedStaticSettings(classToMock, this);
    }

    @Override // org.mockito.MockSettings
    public MockSettings lenient() {
        this.lenient = true;
        return this;
    }

    private static <T> CreationSettings<T> validatedSettings(Class<T> typeToMock, CreationSettings<T> source) {
        MockCreationValidator validator = new MockCreationValidator();
        validator.validateType(typeToMock);
        validator.validateExtraInterfaces(typeToMock, source.getExtraInterfaces());
        validator.validateMockedType(typeToMock, source.getSpiedInstance());
        validator.validateConstructorUse(source.isUsingConstructor(), source.getSerializableMode());
        CreationSettings<T> settings = new CreationSettings<>(source);
        settings.setMockName(new MockNameImpl(source.getName(), typeToMock, false));
        settings.setTypeToMock(typeToMock);
        settings.setExtraInterfaces(prepareExtraInterfaces(source));
        return settings;
    }

    private static <T> CreationSettings<T> validatedStaticSettings(Class<T> classToMock, CreationSettings<T> source) {
        if (classToMock.isPrimitive()) {
            throw new MockitoException("Cannot create static mock of primitive type " + classToMock);
        }
        if (!source.getExtraInterfaces().isEmpty()) {
            throw new MockitoException("Cannot specify additional interfaces for static mock of " + classToMock);
        }
        if (source.getSpiedInstance() != null) {
            throw new MockitoException("Cannot specify spied instance for static mock of " + classToMock);
        }
        CreationSettings<T> settings = new CreationSettings<>(source);
        settings.setMockName(new MockNameImpl(source.getName(), classToMock, true));
        settings.setTypeToMock(classToMock);
        return settings;
    }

    private static Set<Class<?>> prepareExtraInterfaces(CreationSettings settings) {
        Set<Class<?>> interfaces = new HashSet<>(settings.getExtraInterfaces());
        if (settings.isSerializable()) {
            interfaces.add(Serializable.class);
        }
        return interfaces;
    }
}
