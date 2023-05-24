package net.bytebuddy.implementation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.description.type.TypeList;
import net.bytebuddy.dynamic.scaffold.InstrumentedType;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.bytecode.ByteCodeAppender;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.implementation.bytecode.member.MethodReturn;
import net.bytebuddy.implementation.bytecode.member.MethodVariableAccess;
import net.bytebuddy.jar.asm.MethodVisitor;
@HashCodeAndEqualsPlugin.Enhance
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/DefaultMethodCall.class */
public class DefaultMethodCall implements Implementation {
    private final List<TypeDescription> prioritizedInterfaces;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass() && this.prioritizedInterfaces.equals(((DefaultMethodCall) obj).prioritizedInterfaces);
    }

    public int hashCode() {
        return (17 * 31) + this.prioritizedInterfaces.hashCode();
    }

    protected DefaultMethodCall(List<TypeDescription> prioritizedInterfaces) {
        this.prioritizedInterfaces = prioritizedInterfaces;
    }

    public static Implementation prioritize(Class<?>... prioritizedInterface) {
        return prioritize((Collection<? extends TypeDescription>) new TypeList.ForLoadedTypes(prioritizedInterface));
    }

    public static Implementation prioritize(Iterable<? extends Class<?>> prioritizedInterfaces) {
        List<Class<?>> list = new ArrayList<>();
        for (Class<?> prioritizedInterface : prioritizedInterfaces) {
            list.add(prioritizedInterface);
        }
        return prioritize((Collection<? extends TypeDescription>) new TypeList.ForLoadedTypes(list));
    }

    public static Implementation prioritize(TypeDescription... prioritizedInterface) {
        return prioritize((Collection<? extends TypeDescription>) Arrays.asList(prioritizedInterface));
    }

    public static Implementation prioritize(Collection<? extends TypeDescription> prioritizedInterfaces) {
        return new DefaultMethodCall(new ArrayList(prioritizedInterfaces));
    }

    public static Implementation unambiguousOnly() {
        return new DefaultMethodCall(Collections.emptyList());
    }

    @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
    public InstrumentedType prepare(InstrumentedType instrumentedType) {
        return instrumentedType;
    }

    @Override // net.bytebuddy.implementation.Implementation
    public ByteCodeAppender appender(Implementation.Target implementationTarget) {
        return new Appender(implementationTarget, filterRelevant(implementationTarget.getInstrumentedType()));
    }

    private List<TypeDescription> filterRelevant(TypeDescription typeDescription) {
        List<TypeDescription> filtered = new ArrayList<>(this.prioritizedInterfaces.size());
        Set<TypeDescription> relevant = new HashSet<>(typeDescription.getInterfaces().asErasures());
        for (TypeDescription prioritizedInterface : this.prioritizedInterfaces) {
            if (relevant.remove(prioritizedInterface)) {
                filtered.add(prioritizedInterface);
            }
        }
        return filtered;
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/DefaultMethodCall$Appender.class */
    protected static class Appender implements ByteCodeAppender {
        private final Implementation.Target implementationTarget;
        private final List<TypeDescription> prioritizedInterfaces;
        @HashCodeAndEqualsPlugin.ValueHandling(HashCodeAndEqualsPlugin.ValueHandling.Sort.IGNORE)
        private final Set<TypeDescription> nonPrioritizedInterfaces;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.implementationTarget.equals(((Appender) obj).implementationTarget) && this.prioritizedInterfaces.equals(((Appender) obj).prioritizedInterfaces);
        }

        public int hashCode() {
            return (((17 * 31) + this.implementationTarget.hashCode()) * 31) + this.prioritizedInterfaces.hashCode();
        }

        protected Appender(Implementation.Target implementationTarget, List<TypeDescription> prioritizedInterfaces) {
            this.implementationTarget = implementationTarget;
            this.prioritizedInterfaces = prioritizedInterfaces;
            this.nonPrioritizedInterfaces = new HashSet(implementationTarget.getInstrumentedType().getInterfaces().asErasures());
            this.nonPrioritizedInterfaces.removeAll(prioritizedInterfaces);
        }

        @Override // net.bytebuddy.implementation.bytecode.ByteCodeAppender
        public ByteCodeAppender.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext, MethodDescription instrumentedMethod) {
            StackManipulation defaultMethodInvocation = locateDefault(instrumentedMethod);
            if (!defaultMethodInvocation.isValid()) {
                throw new IllegalStateException("Cannot invoke default method on " + instrumentedMethod);
            }
            StackManipulation.Size stackSize = new StackManipulation.Compound(MethodVariableAccess.allArgumentsOf(instrumentedMethod).prependThisReference(), defaultMethodInvocation, MethodReturn.of(instrumentedMethod.getReturnType())).apply(methodVisitor, implementationContext);
            return new ByteCodeAppender.Size(stackSize.getMaximalSize(), instrumentedMethod.getStackSize());
        }

        private StackManipulation locateDefault(MethodDescription methodDescription) {
            MethodDescription.SignatureToken methodToken = methodDescription.asSignatureToken();
            Implementation.SpecialMethodInvocation specialMethodInvocation = Implementation.SpecialMethodInvocation.Illegal.INSTANCE;
            for (TypeDescription typeDescription : this.prioritizedInterfaces) {
                specialMethodInvocation = this.implementationTarget.invokeDefault(methodToken, typeDescription).withCheckedCompatibilityTo(methodDescription.asTypeToken());
                if (specialMethodInvocation.isValid()) {
                    return specialMethodInvocation;
                }
            }
            for (TypeDescription typeDescription2 : this.nonPrioritizedInterfaces) {
                Implementation.SpecialMethodInvocation other = this.implementationTarget.invokeDefault(methodToken, typeDescription2).withCheckedCompatibilityTo(methodDescription.asTypeToken());
                if (specialMethodInvocation.isValid() && other.isValid()) {
                    throw new IllegalStateException(methodDescription + " has an ambiguous default method with " + other.getMethodDescription() + " and " + specialMethodInvocation.getMethodDescription());
                }
                specialMethodInvocation = other;
            }
            return specialMethodInvocation;
        }
    }
}
