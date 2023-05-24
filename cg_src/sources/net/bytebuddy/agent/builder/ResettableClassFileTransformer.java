package net.bytebuddy.agent.builder;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.Iterator;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.utility.JavaModule;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/ResettableClassFileTransformer.class */
public interface ResettableClassFileTransformer extends ClassFileTransformer {
    Iterator<AgentBuilder.Transformer> iterator(TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule, Class<?> cls, ProtectionDomain protectionDomain);

    boolean reset(Instrumentation instrumentation, AgentBuilder.RedefinitionStrategy redefinitionStrategy);

    boolean reset(Instrumentation instrumentation, AgentBuilder.RedefinitionStrategy redefinitionStrategy, AgentBuilder.RedefinitionStrategy.BatchAllocator batchAllocator);

    boolean reset(Instrumentation instrumentation, AgentBuilder.RedefinitionStrategy redefinitionStrategy, AgentBuilder.RedefinitionStrategy.DiscoveryStrategy discoveryStrategy);

    boolean reset(Instrumentation instrumentation, AgentBuilder.RedefinitionStrategy redefinitionStrategy, AgentBuilder.RedefinitionStrategy.BatchAllocator batchAllocator, AgentBuilder.RedefinitionStrategy.DiscoveryStrategy discoveryStrategy);

    boolean reset(Instrumentation instrumentation, AgentBuilder.RedefinitionStrategy redefinitionStrategy, AgentBuilder.RedefinitionStrategy.DiscoveryStrategy discoveryStrategy, AgentBuilder.RedefinitionStrategy.Listener listener);

    boolean reset(Instrumentation instrumentation, AgentBuilder.RedefinitionStrategy redefinitionStrategy, AgentBuilder.RedefinitionStrategy.BatchAllocator batchAllocator, AgentBuilder.RedefinitionStrategy.Listener listener);

    boolean reset(Instrumentation instrumentation, AgentBuilder.RedefinitionStrategy redefinitionStrategy, AgentBuilder.RedefinitionStrategy.DiscoveryStrategy discoveryStrategy, AgentBuilder.RedefinitionStrategy.BatchAllocator batchAllocator, AgentBuilder.RedefinitionStrategy.Listener listener);

    boolean reset(Instrumentation instrumentation, ResettableClassFileTransformer resettableClassFileTransformer, AgentBuilder.RedefinitionStrategy redefinitionStrategy, AgentBuilder.RedefinitionStrategy.DiscoveryStrategy discoveryStrategy, AgentBuilder.RedefinitionStrategy.BatchAllocator batchAllocator, AgentBuilder.RedefinitionStrategy.Listener listener);

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/ResettableClassFileTransformer$AbstractBase.class */
    public static abstract class AbstractBase implements ResettableClassFileTransformer {
        @Override // net.bytebuddy.agent.builder.ResettableClassFileTransformer
        public boolean reset(Instrumentation instrumentation, AgentBuilder.RedefinitionStrategy redefinitionStrategy) {
            return reset(instrumentation, redefinitionStrategy, AgentBuilder.RedefinitionStrategy.BatchAllocator.ForTotal.INSTANCE);
        }

        @Override // net.bytebuddy.agent.builder.ResettableClassFileTransformer
        public boolean reset(Instrumentation instrumentation, AgentBuilder.RedefinitionStrategy redefinitionStrategy, AgentBuilder.RedefinitionStrategy.BatchAllocator redefinitionBatchAllocator) {
            return reset(instrumentation, redefinitionStrategy, redefinitionBatchAllocator, AgentBuilder.RedefinitionStrategy.Listener.NoOp.INSTANCE);
        }

        @Override // net.bytebuddy.agent.builder.ResettableClassFileTransformer
        public boolean reset(Instrumentation instrumentation, AgentBuilder.RedefinitionStrategy redefinitionStrategy, AgentBuilder.RedefinitionStrategy.DiscoveryStrategy redefinitionDiscoveryStrategy) {
            return reset(instrumentation, redefinitionStrategy, redefinitionDiscoveryStrategy, AgentBuilder.RedefinitionStrategy.Listener.NoOp.INSTANCE);
        }

        @Override // net.bytebuddy.agent.builder.ResettableClassFileTransformer
        public boolean reset(Instrumentation instrumentation, AgentBuilder.RedefinitionStrategy redefinitionStrategy, AgentBuilder.RedefinitionStrategy.BatchAllocator redefinitionBatchAllocator, AgentBuilder.RedefinitionStrategy.DiscoveryStrategy redefinitionDiscoveryStrategy) {
            return reset(instrumentation, redefinitionStrategy, redefinitionDiscoveryStrategy, redefinitionBatchAllocator, AgentBuilder.RedefinitionStrategy.Listener.NoOp.INSTANCE);
        }

        @Override // net.bytebuddy.agent.builder.ResettableClassFileTransformer
        public boolean reset(Instrumentation instrumentation, AgentBuilder.RedefinitionStrategy redefinitionStrategy, AgentBuilder.RedefinitionStrategy.DiscoveryStrategy redefinitionDiscoveryStrategy, AgentBuilder.RedefinitionStrategy.Listener redefinitionListener) {
            return reset(instrumentation, redefinitionStrategy, redefinitionDiscoveryStrategy, AgentBuilder.RedefinitionStrategy.BatchAllocator.ForTotal.INSTANCE, redefinitionListener);
        }

        @Override // net.bytebuddy.agent.builder.ResettableClassFileTransformer
        public boolean reset(Instrumentation instrumentation, AgentBuilder.RedefinitionStrategy redefinitionStrategy, AgentBuilder.RedefinitionStrategy.BatchAllocator redefinitionBatchAllocator, AgentBuilder.RedefinitionStrategy.Listener redefinitionListener) {
            return reset(instrumentation, redefinitionStrategy, AgentBuilder.RedefinitionStrategy.DiscoveryStrategy.SinglePass.INSTANCE, redefinitionBatchAllocator, redefinitionListener);
        }

        @Override // net.bytebuddy.agent.builder.ResettableClassFileTransformer
        public boolean reset(Instrumentation instrumentation, AgentBuilder.RedefinitionStrategy redefinitionStrategy, AgentBuilder.RedefinitionStrategy.DiscoveryStrategy redefinitionDiscoveryStrategy, AgentBuilder.RedefinitionStrategy.BatchAllocator redefinitionBatchAllocator, AgentBuilder.RedefinitionStrategy.Listener redefinitionListener) {
            return reset(instrumentation, this, redefinitionStrategy, redefinitionDiscoveryStrategy, redefinitionBatchAllocator, redefinitionListener);
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/ResettableClassFileTransformer$WithDelegation.class */
    public static abstract class WithDelegation extends AbstractBase {
        protected final ResettableClassFileTransformer classFileTransformer;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.classFileTransformer.equals(((WithDelegation) obj).classFileTransformer);
        }

        public int hashCode() {
            return (17 * 31) + this.classFileTransformer.hashCode();
        }

        protected WithDelegation(ResettableClassFileTransformer classFileTransformer) {
            this.classFileTransformer = classFileTransformer;
        }

        @Override // net.bytebuddy.agent.builder.ResettableClassFileTransformer
        public Iterator<AgentBuilder.Transformer> iterator(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, Class<?> classBeingRedefined, ProtectionDomain protectionDomain) {
            return this.classFileTransformer.iterator(typeDescription, classLoader, module, classBeingRedefined, protectionDomain);
        }

        @Override // net.bytebuddy.agent.builder.ResettableClassFileTransformer
        public boolean reset(Instrumentation instrumentation, ResettableClassFileTransformer classFileTransformer, AgentBuilder.RedefinitionStrategy redefinitionStrategy, AgentBuilder.RedefinitionStrategy.DiscoveryStrategy redefinitionDiscoveryStrategy, AgentBuilder.RedefinitionStrategy.BatchAllocator redefinitionBatchAllocator, AgentBuilder.RedefinitionStrategy.Listener redefinitionListener) {
            return this.classFileTransformer.reset(instrumentation, classFileTransformer, redefinitionStrategy, redefinitionDiscoveryStrategy, redefinitionBatchAllocator, redefinitionListener);
        }
    }
}
