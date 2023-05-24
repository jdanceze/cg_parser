package net.bytebuddy.implementation.bytecode.constant;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.List;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.auxiliary.PrivilegedMemberLookupAction;
import net.bytebuddy.implementation.auxiliary.TypeProxy;
import net.bytebuddy.implementation.bytecode.Duplication;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.implementation.bytecode.TypeCreation;
import net.bytebuddy.implementation.bytecode.assign.TypeCasting;
import net.bytebuddy.implementation.bytecode.collection.ArrayFactory;
import net.bytebuddy.implementation.bytecode.member.FieldAccess;
import net.bytebuddy.implementation.bytecode.member.MethodInvocation;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.matcher.ElementMatchers;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/constant/MethodConstant.class */
public abstract class MethodConstant implements StackManipulation {
    protected final MethodDescription.InDefinedShape methodDescription;

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/constant/MethodConstant$CanCache.class */
    public interface CanCache extends StackManipulation {
        StackManipulation cached();
    }

    protected abstract StackManipulation methodName();

    protected abstract MethodDescription.InDefinedShape accessorMethod();

    protected MethodConstant(MethodDescription.InDefinedShape methodDescription) {
        this.methodDescription = methodDescription;
    }

    public static CanCache of(MethodDescription.InDefinedShape methodDescription) {
        if (methodDescription.isTypeInitializer()) {
            return CanCacheIllegal.INSTANCE;
        }
        if (methodDescription.isConstructor()) {
            return new ForConstructor(methodDescription);
        }
        return new ForMethod(methodDescription);
    }

    public static CanCache ofPrivileged(MethodDescription.InDefinedShape methodDescription) {
        if (methodDescription.isTypeInitializer()) {
            return CanCacheIllegal.INSTANCE;
        }
        if (methodDescription.isConstructor()) {
            return new ForConstructor(methodDescription).privileged();
        }
        return new ForMethod(methodDescription).privileged();
    }

    protected static List<StackManipulation> typeConstantsFor(List<TypeDescription> parameterTypes) {
        List<StackManipulation> typeConstants = new ArrayList<>(parameterTypes.size());
        for (TypeDescription parameterType : parameterTypes) {
            typeConstants.add(ClassConstant.of(parameterType));
        }
        return typeConstants;
    }

    @Override // net.bytebuddy.implementation.bytecode.StackManipulation
    public boolean isValid() {
        return true;
    }

    @Override // net.bytebuddy.implementation.bytecode.StackManipulation
    public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
        return new StackManipulation.Compound(ClassConstant.of(this.methodDescription.getDeclaringType()), methodName(), ArrayFactory.forType(TypeDescription.Generic.OfNonGenericType.CLASS).withValues(typeConstantsFor(this.methodDescription.getParameters().asTypeList().asErasures())), MethodInvocation.invoke(accessorMethod())).apply(methodVisitor, implementationContext);
    }

    protected CanCache privileged() {
        return new PrivilegedLookup(this.methodDescription, methodName());
    }

    public int hashCode() {
        return this.methodDescription.hashCode();
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        MethodConstant methodConstant = (MethodConstant) other;
        return this.methodDescription.equals(methodConstant.methodDescription);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/constant/MethodConstant$CanCacheIllegal.class */
    public enum CanCacheIllegal implements CanCache {
        INSTANCE;

        @Override // net.bytebuddy.implementation.bytecode.constant.MethodConstant.CanCache
        public StackManipulation cached() {
            return StackManipulation.Illegal.INSTANCE;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public boolean isValid() {
            return false;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
            return StackManipulation.Illegal.INSTANCE.apply(methodVisitor, implementationContext);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/constant/MethodConstant$ForMethod.class */
    public static class ForMethod extends MethodConstant implements CanCache {
        private static final MethodDescription.InDefinedShape GET_METHOD;
        private static final MethodDescription.InDefinedShape GET_DECLARED_METHOD;

        static {
            try {
                GET_METHOD = new MethodDescription.ForLoadedMethod(Class.class.getMethod("getMethod", String.class, Class[].class));
                GET_DECLARED_METHOD = new MethodDescription.ForLoadedMethod(Class.class.getMethod("getDeclaredMethod", String.class, Class[].class));
            } catch (NoSuchMethodException exception) {
                throw new IllegalStateException("Could not locate method lookup", exception);
            }
        }

        protected ForMethod(MethodDescription.InDefinedShape methodDescription) {
            super(methodDescription);
        }

        @Override // net.bytebuddy.implementation.bytecode.constant.MethodConstant
        protected StackManipulation methodName() {
            return new TextConstant(this.methodDescription.getInternalName());
        }

        @Override // net.bytebuddy.implementation.bytecode.constant.MethodConstant
        protected MethodDescription.InDefinedShape accessorMethod() {
            return this.methodDescription.isPublic() ? GET_METHOD : GET_DECLARED_METHOD;
        }

        @Override // net.bytebuddy.implementation.bytecode.constant.MethodConstant.CanCache
        public StackManipulation cached() {
            return new CachedMethod(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/constant/MethodConstant$ForConstructor.class */
    public static class ForConstructor extends MethodConstant implements CanCache {
        private static final MethodDescription.InDefinedShape GET_CONSTRUCTOR;
        private static final MethodDescription.InDefinedShape GET_DECLARED_CONSTRUCTOR;

        static {
            try {
                GET_CONSTRUCTOR = new MethodDescription.ForLoadedMethod(Class.class.getMethod("getConstructor", Class[].class));
                GET_DECLARED_CONSTRUCTOR = new MethodDescription.ForLoadedMethod(Class.class.getMethod(TypeProxy.SilentConstruction.Appender.GET_DECLARED_CONSTRUCTOR_METHOD_NAME, Class[].class));
            } catch (NoSuchMethodException exception) {
                throw new IllegalStateException("Could not locate Class::getDeclaredConstructor", exception);
            }
        }

        protected ForConstructor(MethodDescription.InDefinedShape methodDescription) {
            super(methodDescription);
        }

        @Override // net.bytebuddy.implementation.bytecode.constant.MethodConstant
        protected StackManipulation methodName() {
            return StackManipulation.Trivial.INSTANCE;
        }

        @Override // net.bytebuddy.implementation.bytecode.constant.MethodConstant
        protected MethodDescription.InDefinedShape accessorMethod() {
            return this.methodDescription.isPublic() ? GET_CONSTRUCTOR : GET_DECLARED_CONSTRUCTOR;
        }

        @Override // net.bytebuddy.implementation.bytecode.constant.MethodConstant.CanCache
        public StackManipulation cached() {
            return new CachedConstructor(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/constant/MethodConstant$PrivilegedLookup.class */
    public static class PrivilegedLookup implements StackManipulation, CanCache {
        private static final MethodDescription.InDefinedShape DO_PRIVILEGED;
        private final MethodDescription.InDefinedShape methodDescription;
        private final StackManipulation methodName;

        static {
            try {
                DO_PRIVILEGED = new MethodDescription.ForLoadedMethod(AccessController.class.getMethod("doPrivileged", PrivilegedExceptionAction.class));
            } catch (NoSuchMethodException exception) {
                throw new IllegalStateException("Cannot locate AccessController::doPrivileged", exception);
            }
        }

        protected PrivilegedLookup(MethodDescription.InDefinedShape methodDescription, StackManipulation methodName) {
            this.methodDescription = methodDescription;
            this.methodName = methodName;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public boolean isValid() {
            return this.methodName.isValid();
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
            TypeDescription auxiliaryType = implementationContext.register(PrivilegedMemberLookupAction.of(this.methodDescription));
            StackManipulation[] stackManipulationArr = new StackManipulation[8];
            stackManipulationArr[0] = TypeCreation.of(auxiliaryType);
            stackManipulationArr[1] = Duplication.SINGLE;
            stackManipulationArr[2] = ClassConstant.of(this.methodDescription.getDeclaringType());
            stackManipulationArr[3] = this.methodName;
            stackManipulationArr[4] = ArrayFactory.forType(TypeDescription.Generic.OfNonGenericType.CLASS).withValues(MethodConstant.typeConstantsFor(this.methodDescription.getParameters().asTypeList().asErasures()));
            stackManipulationArr[5] = MethodInvocation.invoke((MethodDescription.InDefinedShape) auxiliaryType.getDeclaredMethods().filter(ElementMatchers.isConstructor()).getOnly());
            stackManipulationArr[6] = MethodInvocation.invoke(DO_PRIVILEGED);
            stackManipulationArr[7] = TypeCasting.to(TypeDescription.ForLoadedType.of(this.methodDescription.isConstructor() ? Constructor.class : Method.class));
            return new StackManipulation.Compound(stackManipulationArr).apply(methodVisitor, implementationContext);
        }

        @Override // net.bytebuddy.implementation.bytecode.constant.MethodConstant.CanCache
        public StackManipulation cached() {
            return this.methodDescription.isConstructor() ? new CachedConstructor(this) : new CachedMethod(this);
        }

        public int hashCode() {
            return this.methodDescription.hashCode();
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (other == null || getClass() != other.getClass()) {
                return false;
            }
            PrivilegedLookup privilegedLookup = (PrivilegedLookup) other;
            return this.methodDescription.equals(privilegedLookup.methodDescription);
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/constant/MethodConstant$CachedMethod.class */
    protected static class CachedMethod implements StackManipulation {
        private static final TypeDescription METHOD_TYPE = TypeDescription.ForLoadedType.of(Method.class);
        private final StackManipulation methodConstant;

        protected CachedMethod(StackManipulation methodConstant) {
            this.methodConstant = methodConstant;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public boolean isValid() {
            return this.methodConstant.isValid();
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
            return FieldAccess.forField(implementationContext.cache(this.methodConstant, METHOD_TYPE)).read().apply(methodVisitor, implementationContext);
        }

        public int hashCode() {
            return this.methodConstant.hashCode();
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (other == null || getClass() != other.getClass()) {
                return false;
            }
            CachedMethod cachedMethod = (CachedMethod) other;
            return this.methodConstant.equals(cachedMethod.methodConstant);
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/constant/MethodConstant$CachedConstructor.class */
    protected static class CachedConstructor implements StackManipulation {
        private static final TypeDescription CONSTRUCTOR_TYPE = TypeDescription.ForLoadedType.of(Constructor.class);
        private final StackManipulation constructorConstant;

        protected CachedConstructor(StackManipulation constructorConstant) {
            this.constructorConstant = constructorConstant;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public boolean isValid() {
            return this.constructorConstant.isValid();
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
            return FieldAccess.forField(implementationContext.cache(this.constructorConstant, CONSTRUCTOR_TYPE)).read().apply(methodVisitor, implementationContext);
        }

        public int hashCode() {
            return this.constructorConstant.hashCode();
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (other == null || getClass() != other.getClass()) {
                return false;
            }
            CachedConstructor cachedConstructor = (CachedConstructor) other;
            return this.constructorConstant.equals(cachedConstructor.constructorConstant);
        }
    }
}
