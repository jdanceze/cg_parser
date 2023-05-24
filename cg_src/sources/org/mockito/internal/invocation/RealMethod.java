package org.mockito.internal.invocation;

import java.io.Serializable;
import java.util.concurrent.Callable;
import org.mockito.internal.exceptions.stacktrace.ConditionalStackTraceFilter;
import org.mockito.invocation.InvocationFactory;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/invocation/RealMethod.class */
public interface RealMethod extends Serializable {
    boolean isInvokable();

    Object invoke() throws Throwable;

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/invocation/RealMethod$IsIllegal.class */
    public enum IsIllegal implements RealMethod {
        INSTANCE;

        @Override // org.mockito.internal.invocation.RealMethod
        public boolean isInvokable() {
            return false;
        }

        @Override // org.mockito.internal.invocation.RealMethod
        public Object invoke() {
            throw new IllegalStateException();
        }
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/invocation/RealMethod$FromCallable.class */
    public static class FromCallable extends FromBehavior implements RealMethod {
        public FromCallable(final Callable<?> callable) {
            super(new InvocationFactory.RealMethodBehavior() { // from class: org.mockito.internal.invocation.RealMethod.FromCallable.1
                @Override // org.mockito.invocation.InvocationFactory.RealMethodBehavior
                public Object call() throws Throwable {
                    return callable.call();
                }
            });
        }
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/invocation/RealMethod$FromBehavior.class */
    public static class FromBehavior implements RealMethod {
        private final InvocationFactory.RealMethodBehavior<?> behavior;

        /* JADX INFO: Access modifiers changed from: package-private */
        public FromBehavior(InvocationFactory.RealMethodBehavior<?> behavior) {
            this.behavior = behavior;
        }

        @Override // org.mockito.internal.invocation.RealMethod
        public boolean isInvokable() {
            return true;
        }

        @Override // org.mockito.internal.invocation.RealMethod
        public Object invoke() throws Throwable {
            try {
                return this.behavior.call();
            } catch (Throwable t) {
                new ConditionalStackTraceFilter().filter(t);
                throw t;
            }
        }
    }
}
