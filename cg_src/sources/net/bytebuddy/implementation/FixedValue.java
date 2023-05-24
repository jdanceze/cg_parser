package net.bytebuddy.implementation;

import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.ParameterDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.scaffold.InstrumentedType;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.LoadedTypeInitializer;
import net.bytebuddy.implementation.bytecode.ByteCodeAppender;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
import net.bytebuddy.implementation.bytecode.constant.ClassConstant;
import net.bytebuddy.implementation.bytecode.constant.DoubleConstant;
import net.bytebuddy.implementation.bytecode.constant.FloatConstant;
import net.bytebuddy.implementation.bytecode.constant.IntegerConstant;
import net.bytebuddy.implementation.bytecode.constant.JavaConstantValue;
import net.bytebuddy.implementation.bytecode.constant.LongConstant;
import net.bytebuddy.implementation.bytecode.constant.NullConstant;
import net.bytebuddy.implementation.bytecode.constant.TextConstant;
import net.bytebuddy.implementation.bytecode.member.FieldAccess;
import net.bytebuddy.implementation.bytecode.member.MethodReturn;
import net.bytebuddy.implementation.bytecode.member.MethodVariableAccess;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaConstant;
import net.bytebuddy.utility.JavaType;
import net.bytebuddy.utility.RandomString;
@HashCodeAndEqualsPlugin.Enhance
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/FixedValue.class */
public abstract class FixedValue implements Implementation {
    protected final Assigner assigner;
    protected final Assigner.Typing typing;

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/FixedValue$AssignerConfigurable.class */
    public interface AssignerConfigurable extends Implementation {
        Implementation withAssigner(Assigner assigner, Assigner.Typing typing);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass() && this.typing.equals(((FixedValue) obj).typing) && this.assigner.equals(((FixedValue) obj).assigner);
    }

    public int hashCode() {
        return (((17 * 31) + this.assigner.hashCode()) * 31) + this.typing.hashCode();
    }

    protected FixedValue(Assigner assigner, Assigner.Typing typing) {
        this.assigner = assigner;
        this.typing = typing;
    }

    public static AssignerConfigurable value(Object fixedValue) {
        Class<?> type = fixedValue.getClass();
        if (type == String.class) {
            return new ForPoolValue(new TextConstant((String) fixedValue), TypeDescription.STRING);
        }
        if (type == Class.class) {
            return new ForPoolValue(ClassConstant.of(TypeDescription.ForLoadedType.of((Class) fixedValue)), TypeDescription.CLASS);
        }
        if (type == Boolean.class) {
            return new ForPoolValue(IntegerConstant.forValue(((Boolean) fixedValue).booleanValue()), Boolean.TYPE);
        }
        if (type == Byte.class) {
            return new ForPoolValue(IntegerConstant.forValue(((Byte) fixedValue).byteValue()), Byte.TYPE);
        }
        if (type == Short.class) {
            return new ForPoolValue(IntegerConstant.forValue(((Short) fixedValue).shortValue()), Short.TYPE);
        }
        if (type == Character.class) {
            return new ForPoolValue(IntegerConstant.forValue(((Character) fixedValue).charValue()), Character.TYPE);
        }
        if (type == Integer.class) {
            return new ForPoolValue(IntegerConstant.forValue(((Integer) fixedValue).intValue()), Integer.TYPE);
        }
        if (type == Long.class) {
            return new ForPoolValue(LongConstant.forValue(((Long) fixedValue).longValue()), Long.TYPE);
        }
        if (type == Float.class) {
            return new ForPoolValue(FloatConstant.forValue(((Float) fixedValue).floatValue()), Float.TYPE);
        }
        if (type == Double.class) {
            return new ForPoolValue(DoubleConstant.forValue(((Double) fixedValue).doubleValue()), Double.TYPE);
        }
        if (JavaType.METHOD_HANDLE.getTypeStub().isAssignableFrom(type)) {
            return new ForPoolValue(new JavaConstantValue(JavaConstant.MethodHandle.ofLoaded(fixedValue)), type);
        }
        if (JavaType.METHOD_TYPE.getTypeStub().represents(type)) {
            return new ForPoolValue(new JavaConstantValue(JavaConstant.MethodType.ofLoaded(fixedValue)), type);
        }
        return reference(fixedValue);
    }

    public static AssignerConfigurable reference(Object fixedValue) {
        return new ForValue(fixedValue);
    }

    public static AssignerConfigurable reference(Object fixedValue, String fieldName) {
        return new ForValue(fieldName, fixedValue);
    }

    public static AssignerConfigurable value(TypeDescription fixedValue) {
        return new ForPoolValue(ClassConstant.of(fixedValue), TypeDescription.CLASS);
    }

    public static AssignerConfigurable value(JavaConstant fixedValue) {
        return new ForPoolValue(new JavaConstantValue(fixedValue), fixedValue.getType());
    }

    public static AssignerConfigurable argument(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("Argument index cannot be negative: " + index);
        }
        return new ForArgument(index);
    }

    public static AssignerConfigurable self() {
        return new ForThisValue();
    }

    public static Implementation nullValue() {
        return ForNullValue.INSTANCE;
    }

    public static AssignerConfigurable originType() {
        return new ForOriginType();
    }

    protected ByteCodeAppender.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext, MethodDescription instrumentedMethod, TypeDescription.Generic fixedValueType, StackManipulation valueLoadingInstruction) {
        StackManipulation assignment = this.assigner.assign(fixedValueType, instrumentedMethod.getReturnType(), this.typing);
        if (!assignment.isValid()) {
            throw new IllegalArgumentException("Cannot return value of type " + fixedValueType + " for " + instrumentedMethod);
        }
        StackManipulation.Size stackSize = new StackManipulation.Compound(valueLoadingInstruction, assignment, MethodReturn.of(instrumentedMethod.getReturnType())).apply(methodVisitor, implementationContext);
        return new ByteCodeAppender.Size(stackSize.getMaximalSize(), instrumentedMethod.getStackSize());
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/FixedValue$ForNullValue.class */
    protected enum ForNullValue implements Implementation, ByteCodeAppender {
        INSTANCE;

        @Override // net.bytebuddy.implementation.Implementation
        public ByteCodeAppender appender(Implementation.Target implementationTarget) {
            return this;
        }

        @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
        public InstrumentedType prepare(InstrumentedType instrumentedType) {
            return instrumentedType;
        }

        @Override // net.bytebuddy.implementation.bytecode.ByteCodeAppender
        public ByteCodeAppender.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext, MethodDescription instrumentedMethod) {
            if (instrumentedMethod.getReturnType().isPrimitive()) {
                throw new IllegalStateException("Cannot return null from " + instrumentedMethod);
            }
            return new ByteCodeAppender.Simple(NullConstant.INSTANCE, MethodReturn.REFERENCE).apply(methodVisitor, implementationContext, instrumentedMethod);
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/FixedValue$ForOriginType.class */
    protected static class ForOriginType extends FixedValue implements AssignerConfigurable {
        protected ForOriginType() {
            this(Assigner.DEFAULT, Assigner.Typing.STATIC);
        }

        private ForOriginType(Assigner assigner, Assigner.Typing typing) {
            super(assigner, typing);
        }

        @Override // net.bytebuddy.implementation.FixedValue.AssignerConfigurable
        public Implementation withAssigner(Assigner assigner, Assigner.Typing typing) {
            return new ForOriginType(assigner, typing);
        }

        @Override // net.bytebuddy.implementation.Implementation
        public ByteCodeAppender appender(Implementation.Target implementationTarget) {
            return new Appender(implementationTarget.getOriginType().asErasure());
        }

        @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
        public InstrumentedType prepare(InstrumentedType instrumentedType) {
            return instrumentedType;
        }

        @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/FixedValue$ForOriginType$Appender.class */
        protected class Appender implements ByteCodeAppender {
            private final TypeDescription originType;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.originType.equals(((Appender) obj).originType) && ForOriginType.this.equals(ForOriginType.this);
            }

            public int hashCode() {
                return (((17 * 31) + this.originType.hashCode()) * 31) + ForOriginType.this.hashCode();
            }

            protected Appender(TypeDescription originType) {
                this.originType = originType;
            }

            @Override // net.bytebuddy.implementation.bytecode.ByteCodeAppender
            public ByteCodeAppender.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext, MethodDescription instrumentedMethod) {
                return ForOriginType.this.apply(methodVisitor, implementationContext, instrumentedMethod, TypeDescription.CLASS.asGenericType(), ClassConstant.of(this.originType));
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/FixedValue$ForThisValue.class */
    protected static class ForThisValue extends FixedValue implements AssignerConfigurable {
        protected ForThisValue() {
            super(Assigner.DEFAULT, Assigner.Typing.STATIC);
        }

        private ForThisValue(Assigner assigner, Assigner.Typing typing) {
            super(assigner, typing);
        }

        @Override // net.bytebuddy.implementation.Implementation
        public ByteCodeAppender appender(Implementation.Target implementationTarget) {
            return new Appender(implementationTarget.getInstrumentedType());
        }

        @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
        public InstrumentedType prepare(InstrumentedType instrumentedType) {
            return instrumentedType;
        }

        @Override // net.bytebuddy.implementation.FixedValue.AssignerConfigurable
        public Implementation withAssigner(Assigner assigner, Assigner.Typing typing) {
            return new ForThisValue(assigner, typing);
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/FixedValue$ForThisValue$Appender.class */
        protected static class Appender implements ByteCodeAppender {
            private final TypeDescription instrumentedType;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.instrumentedType.equals(((Appender) obj).instrumentedType);
            }

            public int hashCode() {
                return (17 * 31) + this.instrumentedType.hashCode();
            }

            protected Appender(TypeDescription instrumentedType) {
                this.instrumentedType = instrumentedType;
            }

            @Override // net.bytebuddy.implementation.bytecode.ByteCodeAppender
            public ByteCodeAppender.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext, MethodDescription instrumentedMethod) {
                if (instrumentedMethod.isStatic() || !this.instrumentedType.isAssignableTo(instrumentedMethod.getReturnType().asErasure())) {
                    throw new IllegalStateException("Cannot return 'this' from " + instrumentedMethod);
                }
                return new ByteCodeAppender.Simple(MethodVariableAccess.loadThis(), MethodReturn.REFERENCE).apply(methodVisitor, implementationContext, instrumentedMethod);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/FixedValue$ForArgument.class */
    public static class ForArgument extends FixedValue implements AssignerConfigurable, ByteCodeAppender {
        private final int index;

        @Override // net.bytebuddy.implementation.FixedValue
        public boolean equals(Object obj) {
            if (super.equals(obj)) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.index == ((ForArgument) obj).index;
            }
            return false;
        }

        @Override // net.bytebuddy.implementation.FixedValue
        public int hashCode() {
            return (super.hashCode() * 31) + this.index;
        }

        protected ForArgument(int index) {
            this(Assigner.DEFAULT, Assigner.Typing.STATIC, index);
        }

        private ForArgument(Assigner assigner, Assigner.Typing typing, int index) {
            super(assigner, typing);
            this.index = index;
        }

        @Override // net.bytebuddy.implementation.bytecode.ByteCodeAppender
        public ByteCodeAppender.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext, MethodDescription instrumentedMethod) {
            if (instrumentedMethod.getParameters().size() <= this.index) {
                throw new IllegalStateException(instrumentedMethod + " does not define a parameter with index " + this.index);
            }
            ParameterDescription parameterDescription = (ParameterDescription) instrumentedMethod.getParameters().get(this.index);
            StackManipulation stackManipulation = new StackManipulation.Compound(MethodVariableAccess.load(parameterDescription), this.assigner.assign(parameterDescription.getType(), instrumentedMethod.getReturnType(), this.typing), MethodReturn.of(instrumentedMethod.getReturnType()));
            if (!stackManipulation.isValid()) {
                throw new IllegalStateException("Cannot assign " + instrumentedMethod.getReturnType() + " to " + parameterDescription);
            }
            return new ByteCodeAppender.Size(stackManipulation.apply(methodVisitor, implementationContext).getMaximalSize(), instrumentedMethod.getStackSize());
        }

        @Override // net.bytebuddy.implementation.Implementation
        public ByteCodeAppender appender(Implementation.Target implementationTarget) {
            return this;
        }

        @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
        public InstrumentedType prepare(InstrumentedType instrumentedType) {
            return instrumentedType;
        }

        @Override // net.bytebuddy.implementation.FixedValue.AssignerConfigurable
        public Implementation withAssigner(Assigner assigner, Assigner.Typing typing) {
            return new ForArgument(assigner, typing, this.index);
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/FixedValue$ForPoolValue.class */
    protected static class ForPoolValue extends FixedValue implements AssignerConfigurable, ByteCodeAppender {
        private final StackManipulation valueLoadInstruction;
        private final TypeDescription loadedType;

        @Override // net.bytebuddy.implementation.FixedValue
        public boolean equals(Object obj) {
            if (super.equals(obj)) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.valueLoadInstruction.equals(((ForPoolValue) obj).valueLoadInstruction) && this.loadedType.equals(((ForPoolValue) obj).loadedType);
            }
            return false;
        }

        @Override // net.bytebuddy.implementation.FixedValue
        public int hashCode() {
            return (((super.hashCode() * 31) + this.valueLoadInstruction.hashCode()) * 31) + this.loadedType.hashCode();
        }

        protected ForPoolValue(StackManipulation valueLoadInstruction, Class<?> loadedType) {
            this(valueLoadInstruction, TypeDescription.ForLoadedType.of(loadedType));
        }

        protected ForPoolValue(StackManipulation valueLoadInstruction, TypeDescription loadedType) {
            this(Assigner.DEFAULT, Assigner.Typing.STATIC, valueLoadInstruction, loadedType);
        }

        private ForPoolValue(Assigner assigner, Assigner.Typing typing, StackManipulation valueLoadInstruction, TypeDescription loadedType) {
            super(assigner, typing);
            this.valueLoadInstruction = valueLoadInstruction;
            this.loadedType = loadedType;
        }

        @Override // net.bytebuddy.implementation.FixedValue.AssignerConfigurable
        public Implementation withAssigner(Assigner assigner, Assigner.Typing typing) {
            return new ForPoolValue(assigner, typing, this.valueLoadInstruction, this.loadedType);
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
            return apply(methodVisitor, implementationContext, instrumentedMethod, this.loadedType.asGenericType(), this.valueLoadInstruction);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/FixedValue$ForValue.class */
    public static class ForValue extends FixedValue implements AssignerConfigurable {
        private static final String PREFIX = "value";
        private final String fieldName;
        private final Object value;
        @HashCodeAndEqualsPlugin.ValueHandling(HashCodeAndEqualsPlugin.ValueHandling.Sort.IGNORE)
        private final TypeDescription.Generic fieldType;

        @Override // net.bytebuddy.implementation.FixedValue
        public boolean equals(Object obj) {
            if (super.equals(obj)) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.fieldName.equals(((ForValue) obj).fieldName) && this.value.equals(((ForValue) obj).value);
            }
            return false;
        }

        @Override // net.bytebuddy.implementation.FixedValue
        public int hashCode() {
            return (((super.hashCode() * 31) + this.fieldName.hashCode()) * 31) + this.value.hashCode();
        }

        protected ForValue(Object value) {
            this("value$" + RandomString.hashOf(value.hashCode()), value);
        }

        protected ForValue(String fieldName, Object value) {
            this(Assigner.DEFAULT, Assigner.Typing.STATIC, fieldName, value);
        }

        private ForValue(Assigner assigner, Assigner.Typing typing, String fieldName, Object value) {
            super(assigner, typing);
            this.fieldName = fieldName;
            this.value = value;
            this.fieldType = TypeDescription.Generic.OfNonGenericType.ForLoadedType.of(value.getClass());
        }

        @Override // net.bytebuddy.implementation.FixedValue.AssignerConfigurable
        public Implementation withAssigner(Assigner assigner, Assigner.Typing typing) {
            return new ForValue(assigner, typing, this.fieldName, this.value);
        }

        @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
        public InstrumentedType prepare(InstrumentedType instrumentedType) {
            return instrumentedType.withField(new FieldDescription.Token(this.fieldName, 4169, this.fieldType)).withInitializer(new LoadedTypeInitializer.ForStaticField(this.fieldName, this.value));
        }

        @Override // net.bytebuddy.implementation.Implementation
        public ByteCodeAppender appender(Implementation.Target implementationTarget) {
            return new StaticFieldByteCodeAppender(implementationTarget.getInstrumentedType());
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/FixedValue$ForValue$StaticFieldByteCodeAppender.class */
        private class StaticFieldByteCodeAppender implements ByteCodeAppender {
            private final StackManipulation fieldGetAccess;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.fieldGetAccess.equals(((StaticFieldByteCodeAppender) obj).fieldGetAccess);
            }

            public int hashCode() {
                return (17 * 31) + this.fieldGetAccess.hashCode();
            }

            private StaticFieldByteCodeAppender(TypeDescription instrumentedType) {
                this.fieldGetAccess = FieldAccess.forField((FieldDescription.InDefinedShape) instrumentedType.getDeclaredFields().filter(ElementMatchers.named(ForValue.this.fieldName)).getOnly()).read();
            }

            @Override // net.bytebuddy.implementation.bytecode.ByteCodeAppender
            public ByteCodeAppender.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext, MethodDescription instrumentedMethod) {
                return ForValue.this.apply(methodVisitor, implementationContext, instrumentedMethod, ForValue.this.fieldType, this.fieldGetAccess);
            }
        }
    }
}
