package net.bytebuddy.implementation;

import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.scaffold.InstrumentedType;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.bytecode.ByteCodeAppender;
import net.bytebuddy.implementation.bytecode.Duplication;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.implementation.bytecode.Throw;
import net.bytebuddy.implementation.bytecode.TypeCreation;
import net.bytebuddy.implementation.bytecode.constant.TextConstant;
import net.bytebuddy.implementation.bytecode.member.MethodInvocation;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.matcher.ElementMatchers;
@HashCodeAndEqualsPlugin.Enhance
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/ExceptionMethod.class */
public class ExceptionMethod implements Implementation, ByteCodeAppender {
    private final ConstructionDelegate constructionDelegate;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass() && this.constructionDelegate.equals(((ExceptionMethod) obj).constructionDelegate);
    }

    public int hashCode() {
        return (17 * 31) + this.constructionDelegate.hashCode();
    }

    public ExceptionMethod(ConstructionDelegate constructionDelegate) {
        this.constructionDelegate = constructionDelegate;
    }

    public static Implementation throwing(Class<? extends Throwable> throwableType) {
        return throwing(TypeDescription.ForLoadedType.of(throwableType));
    }

    public static Implementation throwing(TypeDescription throwableType) {
        if (!throwableType.isAssignableTo(Throwable.class)) {
            throw new IllegalArgumentException(throwableType + " does not extend throwable");
        }
        return new ExceptionMethod(new ConstructionDelegate.ForDefaultConstructor(throwableType));
    }

    public static Implementation throwing(Class<? extends Throwable> throwableType, String message) {
        return throwing(TypeDescription.ForLoadedType.of(throwableType), message);
    }

    public static Implementation throwing(TypeDescription throwableType, String message) {
        if (!throwableType.isAssignableTo(Throwable.class)) {
            throw new IllegalArgumentException(throwableType + " does not extend throwable");
        }
        return new ExceptionMethod(new ConstructionDelegate.ForStringConstructor(throwableType, message));
    }

    @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
    public InstrumentedType prepare(InstrumentedType instrumentedType) {
        return instrumentedType;
    }

    @Override // net.bytebuddy.implementation.Implementation
    public ByteCodeAppender appender(Implementation.Target implementationTarget) {
        return this;
    }

    @Override // net.bytebuddy.implementation.bytecode.ByteCodeAppender
    public ByteCodeAppender.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext, MethodDescription instrumentedMethod) {
        StackManipulation.Size stackSize = new StackManipulation.Compound(this.constructionDelegate.make(), Throw.INSTANCE).apply(methodVisitor, implementationContext);
        return new ByteCodeAppender.Size(stackSize.getMaximalSize(), instrumentedMethod.getStackSize());
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/ExceptionMethod$ConstructionDelegate.class */
    public interface ConstructionDelegate {
        StackManipulation make();

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/ExceptionMethod$ConstructionDelegate$ForDefaultConstructor.class */
        public static class ForDefaultConstructor implements ConstructionDelegate {
            private final TypeDescription throwableType;
            private final MethodDescription targetConstructor;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.throwableType.equals(((ForDefaultConstructor) obj).throwableType) && this.targetConstructor.equals(((ForDefaultConstructor) obj).targetConstructor);
            }

            public int hashCode() {
                return (((17 * 31) + this.throwableType.hashCode()) * 31) + this.targetConstructor.hashCode();
            }

            public ForDefaultConstructor(TypeDescription throwableType) {
                this.throwableType = throwableType;
                this.targetConstructor = (MethodDescription) throwableType.getDeclaredMethods().filter(ElementMatchers.isConstructor().and(ElementMatchers.takesArguments(0))).getOnly();
            }

            @Override // net.bytebuddy.implementation.ExceptionMethod.ConstructionDelegate
            public StackManipulation make() {
                return new StackManipulation.Compound(TypeCreation.of(this.throwableType), Duplication.SINGLE, MethodInvocation.invoke(this.targetConstructor));
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/ExceptionMethod$ConstructionDelegate$ForStringConstructor.class */
        public static class ForStringConstructor implements ConstructionDelegate {
            private final TypeDescription throwableType;
            private final MethodDescription targetConstructor;
            private final String message;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.message.equals(((ForStringConstructor) obj).message) && this.throwableType.equals(((ForStringConstructor) obj).throwableType) && this.targetConstructor.equals(((ForStringConstructor) obj).targetConstructor);
            }

            public int hashCode() {
                return (((((17 * 31) + this.throwableType.hashCode()) * 31) + this.targetConstructor.hashCode()) * 31) + this.message.hashCode();
            }

            public ForStringConstructor(TypeDescription throwableType, String message) {
                this.throwableType = throwableType;
                this.targetConstructor = (MethodDescription) throwableType.getDeclaredMethods().filter(ElementMatchers.isConstructor().and(ElementMatchers.takesArguments(String.class))).getOnly();
                this.message = message;
            }

            @Override // net.bytebuddy.implementation.ExceptionMethod.ConstructionDelegate
            public StackManipulation make() {
                return new StackManipulation.Compound(TypeCreation.of(this.throwableType), Duplication.SINGLE, new TextConstant(this.message), MethodInvocation.invoke(this.targetConstructor));
            }
        }
    }
}
