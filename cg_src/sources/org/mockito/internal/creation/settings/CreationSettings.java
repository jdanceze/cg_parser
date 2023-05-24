package org.mockito.internal.creation.settings;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import org.mockito.listeners.InvocationListener;
import org.mockito.listeners.StubbingLookupListener;
import org.mockito.listeners.VerificationStartedListener;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.MockName;
import org.mockito.mock.SerializableMode;
import org.mockito.stubbing.Answer;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/settings/CreationSettings.class */
public class CreationSettings<T> implements MockCreationSettings<T>, Serializable {
    private static final long serialVersionUID = -6789800638070123629L;
    protected Class<T> typeToMock;
    protected Set<Class<?>> extraInterfaces;
    protected String name;
    protected Object spiedInstance;
    protected Answer<Object> defaultAnswer;
    protected MockName mockName;
    protected SerializableMode serializableMode;
    protected List<InvocationListener> invocationListeners;
    protected List<StubbingLookupListener> stubbingLookupListeners;
    protected List<VerificationStartedListener> verificationStartedListeners;
    protected boolean stubOnly;
    protected boolean stripAnnotations;
    private boolean useConstructor;
    private Object outerClassInstance;
    private Object[] constructorArgs;
    protected boolean lenient;

    public CreationSettings() {
        this.extraInterfaces = new LinkedHashSet();
        this.serializableMode = SerializableMode.NONE;
        this.invocationListeners = new ArrayList();
        this.stubbingLookupListeners = new CopyOnWriteArrayList();
        this.verificationStartedListeners = new LinkedList();
    }

    public CreationSettings(CreationSettings copy) {
        this.extraInterfaces = new LinkedHashSet();
        this.serializableMode = SerializableMode.NONE;
        this.invocationListeners = new ArrayList();
        this.stubbingLookupListeners = new CopyOnWriteArrayList();
        this.verificationStartedListeners = new LinkedList();
        this.typeToMock = copy.typeToMock;
        this.extraInterfaces = copy.extraInterfaces;
        this.name = copy.name;
        this.spiedInstance = copy.spiedInstance;
        this.defaultAnswer = copy.defaultAnswer;
        this.mockName = copy.mockName;
        this.serializableMode = copy.serializableMode;
        this.invocationListeners = copy.invocationListeners;
        this.stubbingLookupListeners = copy.stubbingLookupListeners;
        this.verificationStartedListeners = copy.verificationStartedListeners;
        this.stubOnly = copy.stubOnly;
        this.useConstructor = copy.isUsingConstructor();
        this.outerClassInstance = copy.getOuterClassInstance();
        this.constructorArgs = copy.getConstructorArgs();
        this.lenient = copy.lenient;
        this.stripAnnotations = copy.stripAnnotations;
    }

    @Override // org.mockito.mock.MockCreationSettings
    public Class<T> getTypeToMock() {
        return this.typeToMock;
    }

    public CreationSettings<T> setTypeToMock(Class<T> typeToMock) {
        this.typeToMock = typeToMock;
        return this;
    }

    @Override // org.mockito.mock.MockCreationSettings
    public Set<Class<?>> getExtraInterfaces() {
        return this.extraInterfaces;
    }

    public CreationSettings<T> setExtraInterfaces(Set<Class<?>> extraInterfaces) {
        this.extraInterfaces = extraInterfaces;
        return this;
    }

    public String getName() {
        return this.name;
    }

    @Override // org.mockito.mock.MockCreationSettings
    public Object getSpiedInstance() {
        return this.spiedInstance;
    }

    @Override // org.mockito.mock.MockCreationSettings
    public Answer<Object> getDefaultAnswer() {
        return this.defaultAnswer;
    }

    @Override // org.mockito.mock.MockCreationSettings
    public MockName getMockName() {
        return this.mockName;
    }

    public CreationSettings<T> setMockName(MockName mockName) {
        this.mockName = mockName;
        return this;
    }

    @Override // org.mockito.mock.MockCreationSettings
    public boolean isSerializable() {
        return this.serializableMode != SerializableMode.NONE;
    }

    public CreationSettings<T> setSerializableMode(SerializableMode serializableMode) {
        this.serializableMode = serializableMode;
        return this;
    }

    @Override // org.mockito.mock.MockCreationSettings
    public SerializableMode getSerializableMode() {
        return this.serializableMode;
    }

    @Override // org.mockito.mock.MockCreationSettings
    public List<InvocationListener> getInvocationListeners() {
        return this.invocationListeners;
    }

    @Override // org.mockito.mock.MockCreationSettings
    public List<VerificationStartedListener> getVerificationStartedListeners() {
        return this.verificationStartedListeners;
    }

    @Override // org.mockito.mock.MockCreationSettings
    public List<StubbingLookupListener> getStubbingLookupListeners() {
        return this.stubbingLookupListeners;
    }

    @Override // org.mockito.mock.MockCreationSettings
    public boolean isUsingConstructor() {
        return this.useConstructor;
    }

    @Override // org.mockito.mock.MockCreationSettings
    public boolean isStripAnnotations() {
        return this.stripAnnotations;
    }

    @Override // org.mockito.mock.MockCreationSettings
    public Object[] getConstructorArgs() {
        return this.constructorArgs;
    }

    @Override // org.mockito.mock.MockCreationSettings
    public Object getOuterClassInstance() {
        return this.outerClassInstance;
    }

    @Override // org.mockito.mock.MockCreationSettings
    public boolean isStubOnly() {
        return this.stubOnly;
    }

    @Override // org.mockito.mock.MockCreationSettings
    public boolean isLenient() {
        return this.lenient;
    }
}
