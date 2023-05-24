package org.mockito.internal.invocation;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.internal.exceptions.VerificationAwareInvocation;
import org.mockito.internal.invocation.mockref.MockReference;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.invocation.StubInfo;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/invocation/InterceptedInvocation.class */
public class InterceptedInvocation implements Invocation, VerificationAwareInvocation {
    private static final long serialVersionUID = 475027563923510472L;
    private final MockReference<Object> mockRef;
    private final MockitoMethod mockitoMethod;
    private final Object[] arguments;
    private final Object[] rawArguments;
    private final RealMethod realMethod;
    private final int sequenceNumber;
    private final Location location;
    private boolean verified;
    private boolean isIgnoredForVerification;
    private StubInfo stubInfo;
    public static final RealMethod NO_OP = new RealMethod() { // from class: org.mockito.internal.invocation.InterceptedInvocation.1
        @Override // org.mockito.internal.invocation.RealMethod
        public boolean isInvokable() {
            return false;
        }

        @Override // org.mockito.internal.invocation.RealMethod
        public Object invoke() throws Throwable {
            return null;
        }
    };

    public InterceptedInvocation(MockReference<Object> mockRef, MockitoMethod mockitoMethod, Object[] arguments, RealMethod realMethod, Location location, int sequenceNumber) {
        this.mockRef = mockRef;
        this.mockitoMethod = mockitoMethod;
        this.arguments = ArgumentsProcessor.expandArgs(mockitoMethod, arguments);
        this.rawArguments = arguments;
        this.realMethod = realMethod;
        this.location = location;
        this.sequenceNumber = sequenceNumber;
    }

    @Override // org.mockito.invocation.Invocation, org.mockito.internal.exceptions.VerificationAwareInvocation
    public boolean isVerified() {
        return this.verified || this.isIgnoredForVerification;
    }

    @Override // org.mockito.invocation.Invocation
    public int getSequenceNumber() {
        return this.sequenceNumber;
    }

    @Override // org.mockito.invocation.Invocation, org.mockito.invocation.DescribedInvocation
    public Location getLocation() {
        return this.location;
    }

    @Override // org.mockito.invocation.Invocation
    public Object[] getRawArguments() {
        return this.rawArguments;
    }

    @Override // org.mockito.invocation.Invocation
    public Class<?> getRawReturnType() {
        return this.mockitoMethod.getReturnType();
    }

    @Override // org.mockito.invocation.Invocation
    public void markVerified() {
        this.verified = true;
    }

    @Override // org.mockito.invocation.Invocation
    public StubInfo stubInfo() {
        return this.stubInfo;
    }

    @Override // org.mockito.invocation.Invocation
    public void markStubbed(StubInfo stubInfo) {
        this.stubInfo = stubInfo;
    }

    @Override // org.mockito.invocation.Invocation
    public boolean isIgnoredForVerification() {
        return this.isIgnoredForVerification;
    }

    @Override // org.mockito.invocation.Invocation
    public void ignoreForVerification() {
        this.isIgnoredForVerification = true;
    }

    @Override // org.mockito.invocation.InvocationOnMock
    public Object getMock() {
        return this.mockRef.get();
    }

    @Override // org.mockito.invocation.InvocationOnMock
    public Method getMethod() {
        return this.mockitoMethod.getJavaMethod();
    }

    @Override // org.mockito.invocation.InvocationOnMock
    public Object[] getArguments() {
        return this.arguments;
    }

    @Override // org.mockito.invocation.InvocationOnMock
    public <T> T getArgument(int index) {
        return (T) this.arguments[index];
    }

    @Override // org.mockito.invocation.Invocation
    public List<ArgumentMatcher> getArgumentsAsMatchers() {
        return ArgumentsProcessor.argumentsToMatchers(getArguments());
    }

    @Override // org.mockito.invocation.InvocationOnMock
    public <T> T getArgument(int index, Class<T> clazz) {
        return clazz.cast(this.arguments[index]);
    }

    @Override // org.mockito.invocation.InvocationOnMock
    public Object callRealMethod() throws Throwable {
        if (!this.realMethod.isInvokable()) {
            throw Reporter.cannotCallAbstractRealMethod();
        }
        return this.realMethod.invoke();
    }

    @Deprecated
    public MockReference<Object> getMockRef() {
        return this.mockRef;
    }

    @Deprecated
    public MockitoMethod getMockitoMethod() {
        return this.mockitoMethod;
    }

    @Deprecated
    public RealMethod getRealMethod() {
        return this.realMethod;
    }

    public int hashCode() {
        return 1;
    }

    public boolean equals(Object o) {
        if (o == null || !o.getClass().equals(getClass())) {
            return false;
        }
        InterceptedInvocation other = (InterceptedInvocation) o;
        return this.mockRef.get().equals(other.mockRef.get()) && this.mockitoMethod.equals(other.mockitoMethod) && equalArguments(other.arguments);
    }

    private boolean equalArguments(Object[] arguments) {
        return Arrays.equals(arguments, this.arguments);
    }

    @Override // org.mockito.invocation.DescribedInvocation
    public String toString() {
        return new PrintSettings().print(getArgumentsAsMatchers(), this);
    }
}
