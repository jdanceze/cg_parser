package org.powermock.api.mockito.expectation;

import java.lang.reflect.Constructor;
import org.mockito.ArgumentMatchers;
import org.mockito.stubbing.OngoingStubbing;
import org.powermock.api.mockito.internal.expectation.DelegatingToConstructorsOngoingStubbing;
import org.powermock.api.mockito.internal.invocation.MockitoNewInvocationControl;
import org.powermock.api.mockito.internal.mockcreation.DefaultMockCreator;
import org.powermock.core.MockRepository;
import org.powermock.core.spi.NewInvocationControl;
import org.powermock.core.spi.support.InvocationSubstitute;
import org.powermock.reflect.internal.WhiteboxImpl;
import org.powermock.tests.utils.ArrayMerger;
import org.powermock.tests.utils.impl.ArrayMergerImpl;
/* loaded from: gencallgraphv3.jar:powermock-api-mockito2-2.0.9.jar:org/powermock/api/mockito/expectation/DefaultConstructorExpectationSetup.class */
public class DefaultConstructorExpectationSetup<T> implements ConstructorExpectationSetup<T> {
    private final Class<T> mockType;
    private Class<?>[] parameterTypes = null;
    private final ArrayMerger arrayMerger = new ArrayMergerImpl();
    private final DefaultMockCreator mockCreator = new DefaultMockCreator();
    private final InvocationSubstitute mock = (InvocationSubstitute) getMockCreator().createMock(InvocationSubstitute.class, false, false, null, null, null);

    public DefaultConstructorExpectationSetup(Class<T> mockType) {
        this.mockType = mockType;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.powermock.api.mockito.expectation.WithExpectedArguments
    public OngoingStubbing<T> withArguments(Object firstArgument, Object... additionalArguments) throws Exception {
        return createNewSubstituteMock(this.mockType, this.parameterTypes, this.arrayMerger.mergeArrays(Object.class, new Object[]{firstArgument}, additionalArguments));
    }

    private OngoingStubbing<T> createNewSubstituteMock(Class<T> type, Class<?>[] parameterTypes, Object... arguments) throws Exception {
        if (type == null) {
            throw new IllegalArgumentException("type cannot be null");
        }
        Class<?> originalUnmockedType = WhiteboxImpl.getOriginalUnmockedType(type);
        if (parameterTypes == null) {
            WhiteboxImpl.findUniqueConstructorOrThrowException(type, arguments);
        } else {
            WhiteboxImpl.getConstructor(originalUnmockedType, parameterTypes);
        }
        NewInvocationControl<OngoingStubbing<T>> newInvocationControl = createNewInvocationControl(type, originalUnmockedType);
        return newInvocationControl.expectSubstitutionLogic(arguments);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private NewInvocationControl<OngoingStubbing<T>> createNewInvocationControl(Class<T> type, Class<T> unmockedType) {
        NewInvocationControl newInstanceControl = MockRepository.getNewInstanceControl(unmockedType);
        if (newInstanceControl == null) {
            newInstanceControl = createNewInvocationControl(this.mock);
            MockRepository.putNewInstanceControl(type, newInstanceControl);
            MockRepository.addObjectsToAutomaticallyReplayAndVerify(WhiteboxImpl.getOriginalUnmockedType(type));
        }
        return newInstanceControl;
    }

    @Override // org.powermock.api.mockito.expectation.WithAnyArguments
    public OngoingStubbing<T> withAnyArguments() throws Exception {
        if (this.mockType == null) {
            throw new IllegalArgumentException("Class to expected cannot be null");
        }
        Constructor<?>[] allConstructors = WhiteboxImpl.getAllConstructors(WhiteboxImpl.getOriginalUnmockedType(this.mockType));
        Constructor<?> constructor = allConstructors[0];
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Object[] paramArgs = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> paramType = parameterTypes[i];
            paramArgs[i] = createParamArgMatcher(paramType);
        }
        Constructor<?>[] otherCtors = new Constructor[allConstructors.length - 1];
        System.arraycopy(allConstructors, 1, otherCtors, 0, allConstructors.length - 1);
        OngoingStubbing<T> ongoingStubbing = createNewSubstituteMock(this.mockType, parameterTypes, paramArgs);
        return new DelegatingToConstructorsOngoingStubbing(otherCtors, ongoingStubbing);
    }

    private Object createParamArgMatcher(Class<?> paramType) {
        return ArgumentMatchers.nullable(paramType);
    }

    @Override // org.powermock.api.mockito.expectation.WithoutExpectedArguments
    public OngoingStubbing<T> withNoArguments() throws Exception {
        return createNewSubstituteMock(this.mockType, this.parameterTypes, new Object[0]);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.powermock.api.mockito.expectation.WithExpectedParameterTypes
    public WithExpectedArguments<T> withParameterTypes(Class<?> parameterType, Class<?>... additionalParameterTypes) {
        this.parameterTypes = (Class[]) this.arrayMerger.mergeArrays(Class.class, new Class[]{parameterType}, additionalParameterTypes);
        return this;
    }

    private DefaultMockCreator getMockCreator() {
        return this.mockCreator;
    }

    private NewInvocationControl<OngoingStubbing<T>> createNewInvocationControl(InvocationSubstitute<T> mock) {
        return new MockitoNewInvocationControl(mock);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }
}
