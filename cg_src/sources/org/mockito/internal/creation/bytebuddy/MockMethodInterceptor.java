package org.mockito.internal.creation.bytebuddy;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Argument;
import net.bytebuddy.implementation.bind.annotation.BindingPriority;
import net.bytebuddy.implementation.bind.annotation.FieldValue;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.StubValue;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;
import org.mockito.internal.debugging.LocationImpl;
import org.mockito.internal.invocation.DefaultInvocationFactory;
import org.mockito.internal.invocation.RealMethod;
import org.mockito.invocation.Location;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/MockMethodInterceptor.class */
public class MockMethodInterceptor implements Serializable {
    private static final long serialVersionUID = 7152947254057253027L;
    final MockHandler handler;
    private final MockCreationSettings mockCreationSettings;
    private transient ThreadLocal<Object> weakReferenceHatch = new ThreadLocal<>();
    private final ByteBuddyCrossClassLoaderSerializationSupport serializationSupport = new ByteBuddyCrossClassLoaderSerializationSupport();

    public MockMethodInterceptor(MockHandler handler, MockCreationSettings mockCreationSettings) {
        this.handler = handler;
        this.mockCreationSettings = mockCreationSettings;
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.weakReferenceHatch = new ThreadLocal<>();
    }

    Object doIntercept(Object mock, Method invokedMethod, Object[] arguments, RealMethod realMethod) throws Throwable {
        return doIntercept(mock, invokedMethod, arguments, realMethod, new LocationImpl());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Object doIntercept(Object mock, Method invokedMethod, Object[] arguments, RealMethod realMethod, Location location) throws Throwable {
        this.weakReferenceHatch.set(mock);
        try {
            Object handle = this.handler.handle(DefaultInvocationFactory.createInvocation(mock, invokedMethod, arguments, realMethod, this.mockCreationSettings, location));
            this.weakReferenceHatch.remove();
            return handle;
        } catch (Throwable th) {
            this.weakReferenceHatch.remove();
            throw th;
        }
    }

    public MockHandler getMockHandler() {
        return this.handler;
    }

    public ByteBuddyCrossClassLoaderSerializationSupport getSerializationSupport() {
        return this.serializationSupport;
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/MockMethodInterceptor$ForHashCode.class */
    public static class ForHashCode {
        public static int doIdentityHashCode(@This Object thiz) {
            return System.identityHashCode(thiz);
        }
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/MockMethodInterceptor$ForEquals.class */
    public static class ForEquals {
        public static boolean doIdentityEquals(@This Object thiz, @Argument(0) Object other) {
            return thiz == other;
        }
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/MockMethodInterceptor$ForWriteReplace.class */
    public static class ForWriteReplace {
        public static Object doWriteReplace(@This MockAccess thiz) throws ObjectStreamException {
            return thiz.getMockitoInterceptor().getSerializationSupport().writeReplace(thiz);
        }
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/MockMethodInterceptor$DispatcherDefaultingToRealMethod.class */
    public static class DispatcherDefaultingToRealMethod {
        @RuntimeType
        @BindingPriority(2)
        public static Object interceptSuperCallable(@This Object mock, @FieldValue("mockitoInterceptor") MockMethodInterceptor interceptor, @Origin Method invokedMethod, @AllArguments Object[] arguments, @SuperCall(serializableProxy = true) Callable<?> superCall) throws Throwable {
            if (interceptor == null) {
                return superCall.call();
            }
            return interceptor.doIntercept(mock, invokedMethod, arguments, new RealMethod.FromCallable(superCall));
        }

        @RuntimeType
        public static Object interceptAbstract(@This Object mock, @FieldValue("mockitoInterceptor") MockMethodInterceptor interceptor, @StubValue Object stubValue, @Origin Method invokedMethod, @AllArguments Object[] arguments) throws Throwable {
            if (interceptor == null) {
                return stubValue;
            }
            return interceptor.doIntercept(mock, invokedMethod, arguments, RealMethod.IsIllegal.INSTANCE);
        }
    }
}
