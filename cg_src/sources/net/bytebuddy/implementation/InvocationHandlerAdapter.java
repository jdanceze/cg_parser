package net.bytebuddy.implementation;

import java.lang.reflect.InvocationHandler;
import java.util.ArrayList;
import java.util.List;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.description.type.TypeList;
import net.bytebuddy.dynamic.scaffold.FieldLocator;
import net.bytebuddy.dynamic.scaffold.InstrumentedType;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.LoadedTypeInitializer;
import net.bytebuddy.implementation.bytecode.ByteCodeAppender;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
import net.bytebuddy.implementation.bytecode.collection.ArrayFactory;
import net.bytebuddy.implementation.bytecode.constant.MethodConstant;
import net.bytebuddy.implementation.bytecode.member.FieldAccess;
import net.bytebuddy.implementation.bytecode.member.MethodInvocation;
import net.bytebuddy.implementation.bytecode.member.MethodReturn;
import net.bytebuddy.implementation.bytecode.member.MethodVariableAccess;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.RandomString;
@HashCodeAndEqualsPlugin.Enhance
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvocationHandlerAdapter.class */
public abstract class InvocationHandlerAdapter implements Implementation {
    private static final TypeDescription.Generic INVOCATION_HANDLER_TYPE = TypeDescription.Generic.OfNonGenericType.ForLoadedType.of(InvocationHandler.class);
    private static final boolean UNCACHED = false;
    private static final boolean CACHED = true;
    private static final boolean UNPRIVILEGED = false;
    private static final boolean PRIVILEGED = true;
    protected final String fieldName;
    protected final Assigner assigner;
    protected final boolean cached;
    protected final boolean privileged;

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvocationHandlerAdapter$AssignerConfigurable.class */
    public interface AssignerConfigurable extends Implementation {
        Implementation withAssigner(Assigner assigner);
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvocationHandlerAdapter$WithoutPrivilegeConfiguration.class */
    public interface WithoutPrivilegeConfiguration extends AssignerConfigurable {
        AssignerConfigurable withPrivilegedLookup();
    }

    public abstract WithoutPrivilegeConfiguration withoutMethodCache();

    public abstract Implementation withAssigner(Assigner assigner);

    public abstract AssignerConfigurable withPrivilegedLookup();

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass() && this.cached == ((InvocationHandlerAdapter) obj).cached && this.privileged == ((InvocationHandlerAdapter) obj).privileged && this.fieldName.equals(((InvocationHandlerAdapter) obj).fieldName) && this.assigner.equals(((InvocationHandlerAdapter) obj).assigner);
    }

    public int hashCode() {
        return (((((((17 * 31) + this.fieldName.hashCode()) * 31) + this.assigner.hashCode()) * 31) + (this.cached ? 1 : 0)) * 31) + (this.privileged ? 1 : 0);
    }

    protected InvocationHandlerAdapter(String fieldName, boolean cached, boolean privileged, Assigner assigner) {
        this.fieldName = fieldName;
        this.cached = cached;
        this.privileged = privileged;
        this.assigner = assigner;
    }

    public static InvocationHandlerAdapter of(InvocationHandler invocationHandler) {
        return of(invocationHandler, "invocationHandler$" + RandomString.hashOf(invocationHandler.hashCode()));
    }

    public static InvocationHandlerAdapter of(InvocationHandler invocationHandler, String fieldName) {
        return new ForInstance(fieldName, true, false, Assigner.DEFAULT, invocationHandler);
    }

    public static InvocationHandlerAdapter toField(String name) {
        return toField(name, FieldLocator.ForClassHierarchy.Factory.INSTANCE);
    }

    public static InvocationHandlerAdapter toField(String name, FieldLocator.Factory fieldLocatorFactory) {
        return new ForField(name, true, false, Assigner.DEFAULT, fieldLocatorFactory);
    }

    private List<StackManipulation> argumentValuesOf(MethodDescription instrumentedMethod) {
        TypeList.Generic<TypeDescription.Generic> parameterTypes = instrumentedMethod.getParameters().asTypeList();
        List<StackManipulation> instruction = new ArrayList<>(parameterTypes.size());
        int currentIndex = 1;
        for (TypeDescription.Generic parameterType : parameterTypes) {
            instruction.add(new StackManipulation.Compound(MethodVariableAccess.of(parameterType).loadFrom(currentIndex), this.assigner.assign(parameterType, TypeDescription.Generic.OBJECT, Assigner.Typing.STATIC)));
            currentIndex += parameterType.getStackSize().getSize();
        }
        return instruction;
    }

    protected ByteCodeAppender.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext, MethodDescription instrumentedMethod, StackManipulation preparingManipulation, FieldDescription fieldDescription) {
        MethodConstant.CanCache of;
        if (instrumentedMethod.isStatic()) {
            throw new IllegalStateException("It is not possible to apply an invocation handler onto the static method " + instrumentedMethod);
        }
        if (this.privileged) {
            of = MethodConstant.ofPrivileged(instrumentedMethod.asDefined());
        } else {
            of = MethodConstant.of(instrumentedMethod.asDefined());
        }
        MethodConstant.CanCache methodConstant = of;
        StackManipulation[] stackManipulationArr = new StackManipulation[8];
        stackManipulationArr[0] = preparingManipulation;
        stackManipulationArr[1] = FieldAccess.forField(fieldDescription).read();
        stackManipulationArr[2] = MethodVariableAccess.loadThis();
        stackManipulationArr[3] = this.cached ? methodConstant.cached() : methodConstant;
        stackManipulationArr[4] = ArrayFactory.forType(TypeDescription.Generic.OBJECT).withValues(argumentValuesOf(instrumentedMethod));
        stackManipulationArr[5] = MethodInvocation.invoke((MethodDescription) INVOCATION_HANDLER_TYPE.getDeclaredMethods().getOnly());
        stackManipulationArr[6] = this.assigner.assign(TypeDescription.Generic.OBJECT, instrumentedMethod.getReturnType(), Assigner.Typing.DYNAMIC);
        stackManipulationArr[7] = MethodReturn.of(instrumentedMethod.getReturnType());
        StackManipulation.Size stackSize = new StackManipulation.Compound(stackManipulationArr).apply(methodVisitor, implementationContext);
        return new ByteCodeAppender.Size(stackSize.getMaximalSize(), instrumentedMethod.getStackSize());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvocationHandlerAdapter$ForInstance.class */
    public static class ForInstance extends InvocationHandlerAdapter implements WithoutPrivilegeConfiguration {
        private static final String PREFIX = "invocationHandler";
        protected final InvocationHandler invocationHandler;

        @Override // net.bytebuddy.implementation.InvocationHandlerAdapter
        public boolean equals(Object obj) {
            if (super.equals(obj)) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.invocationHandler.equals(((ForInstance) obj).invocationHandler);
            }
            return false;
        }

        @Override // net.bytebuddy.implementation.InvocationHandlerAdapter
        public int hashCode() {
            return (super.hashCode() * 31) + this.invocationHandler.hashCode();
        }

        protected ForInstance(String fieldName, boolean cached, boolean privileged, Assigner assigner, InvocationHandler invocationHandler) {
            super(fieldName, cached, privileged, assigner);
            this.invocationHandler = invocationHandler;
        }

        @Override // net.bytebuddy.implementation.InvocationHandlerAdapter
        public WithoutPrivilegeConfiguration withoutMethodCache() {
            return new ForInstance(this.fieldName, false, this.privileged, this.assigner, this.invocationHandler);
        }

        @Override // net.bytebuddy.implementation.InvocationHandlerAdapter, net.bytebuddy.implementation.InvocationHandlerAdapter.AssignerConfigurable
        public Implementation withAssigner(Assigner assigner) {
            return new ForInstance(this.fieldName, this.cached, this.privileged, assigner, this.invocationHandler);
        }

        @Override // net.bytebuddy.implementation.InvocationHandlerAdapter, net.bytebuddy.implementation.InvocationHandlerAdapter.WithoutPrivilegeConfiguration
        public AssignerConfigurable withPrivilegedLookup() {
            return new ForInstance(this.fieldName, this.cached, true, this.assigner, this.invocationHandler);
        }

        @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
        public InstrumentedType prepare(InstrumentedType instrumentedType) {
            return instrumentedType.withField(new FieldDescription.Token(this.fieldName, 4169, InvocationHandlerAdapter.INVOCATION_HANDLER_TYPE)).withInitializer(new LoadedTypeInitializer.ForStaticField(this.fieldName, this.invocationHandler));
        }

        @Override // net.bytebuddy.implementation.Implementation
        public ByteCodeAppender appender(Implementation.Target implementationTarget) {
            return new Appender(implementationTarget.getInstrumentedType());
        }

        @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvocationHandlerAdapter$ForInstance$Appender.class */
        protected class Appender implements ByteCodeAppender {
            private final TypeDescription instrumentedType;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.instrumentedType.equals(((Appender) obj).instrumentedType) && ForInstance.this.equals(ForInstance.this);
            }

            public int hashCode() {
                return (((17 * 31) + this.instrumentedType.hashCode()) * 31) + ForInstance.this.hashCode();
            }

            protected Appender(TypeDescription instrumentedType) {
                this.instrumentedType = instrumentedType;
            }

            @Override // net.bytebuddy.implementation.bytecode.ByteCodeAppender
            public ByteCodeAppender.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext, MethodDescription instrumentedMethod) {
                return ForInstance.this.apply(methodVisitor, implementationContext, instrumentedMethod, StackManipulation.Trivial.INSTANCE, (FieldDescription) this.instrumentedType.getDeclaredFields().filter(ElementMatchers.named(ForInstance.this.fieldName).and(ElementMatchers.genericFieldType(InvocationHandlerAdapter.INVOCATION_HANDLER_TYPE))).getOnly());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvocationHandlerAdapter$ForField.class */
    public static class ForField extends InvocationHandlerAdapter implements WithoutPrivilegeConfiguration {
        private final FieldLocator.Factory fieldLocatorFactory;

        @Override // net.bytebuddy.implementation.InvocationHandlerAdapter
        public boolean equals(Object obj) {
            if (super.equals(obj)) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.fieldLocatorFactory.equals(((ForField) obj).fieldLocatorFactory);
            }
            return false;
        }

        @Override // net.bytebuddy.implementation.InvocationHandlerAdapter
        public int hashCode() {
            return (super.hashCode() * 31) + this.fieldLocatorFactory.hashCode();
        }

        protected ForField(String fieldName, boolean cached, boolean privileged, Assigner assigner, FieldLocator.Factory fieldLocatorFactory) {
            super(fieldName, cached, privileged, assigner);
            this.fieldLocatorFactory = fieldLocatorFactory;
        }

        @Override // net.bytebuddy.implementation.InvocationHandlerAdapter
        public WithoutPrivilegeConfiguration withoutMethodCache() {
            return new ForField(this.fieldName, false, this.privileged, this.assigner, this.fieldLocatorFactory);
        }

        @Override // net.bytebuddy.implementation.InvocationHandlerAdapter, net.bytebuddy.implementation.InvocationHandlerAdapter.AssignerConfigurable
        public Implementation withAssigner(Assigner assigner) {
            return new ForField(this.fieldName, this.cached, this.privileged, assigner, this.fieldLocatorFactory);
        }

        @Override // net.bytebuddy.implementation.InvocationHandlerAdapter, net.bytebuddy.implementation.InvocationHandlerAdapter.WithoutPrivilegeConfiguration
        public AssignerConfigurable withPrivilegedLookup() {
            return new ForField(this.fieldName, this.cached, true, this.assigner, this.fieldLocatorFactory);
        }

        @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
        public InstrumentedType prepare(InstrumentedType instrumentedType) {
            return instrumentedType;
        }

        @Override // net.bytebuddy.implementation.Implementation
        public ByteCodeAppender appender(Implementation.Target implementationTarget) {
            FieldLocator.Resolution resolution = this.fieldLocatorFactory.make(implementationTarget.getInstrumentedType()).locate(this.fieldName);
            if (!resolution.isResolved()) {
                throw new IllegalStateException("Could not find a field named '" + this.fieldName + "' for " + implementationTarget.getInstrumentedType());
            }
            if (!resolution.getField().getType().asErasure().isAssignableTo(InvocationHandler.class)) {
                throw new IllegalStateException("Field " + resolution.getField() + " does not declare a type that is assignable to invocation handler");
            }
            return new Appender(resolution.getField());
        }

        @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvocationHandlerAdapter$ForField$Appender.class */
        protected class Appender implements ByteCodeAppender {
            private final FieldDescription fieldDescription;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.fieldDescription.equals(((Appender) obj).fieldDescription) && ForField.this.equals(ForField.this);
            }

            public int hashCode() {
                return (((17 * 31) + this.fieldDescription.hashCode()) * 31) + ForField.this.hashCode();
            }

            protected Appender(FieldDescription fieldDescription) {
                this.fieldDescription = fieldDescription;
            }

            @Override // net.bytebuddy.implementation.bytecode.ByteCodeAppender
            public ByteCodeAppender.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext, MethodDescription instrumentedMethod) {
                return ForField.this.apply(methodVisitor, implementationContext, instrumentedMethod, this.fieldDescription.isStatic() ? StackManipulation.Trivial.INSTANCE : MethodVariableAccess.loadThis(), this.fieldDescription);
            }
        }
    }
}
