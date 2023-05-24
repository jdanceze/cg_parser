package net.bytebuddy.implementation;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.ParameterDescription;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.scaffold.InstrumentedType;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.bytecode.ByteCodeAppender;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.implementation.bytecode.StackSize;
import net.bytebuddy.implementation.bytecode.assign.InstanceCheck;
import net.bytebuddy.implementation.bytecode.assign.TypeCasting;
import net.bytebuddy.implementation.bytecode.constant.IntegerConstant;
import net.bytebuddy.implementation.bytecode.member.FieldAccess;
import net.bytebuddy.implementation.bytecode.member.MethodInvocation;
import net.bytebuddy.implementation.bytecode.member.MethodReturn;
import net.bytebuddy.implementation.bytecode.member.MethodVariableAccess;
import net.bytebuddy.jar.asm.Label;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.jar.asm.Type;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
@HashCodeAndEqualsPlugin.Enhance
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/EqualsMethod.class */
public class EqualsMethod implements Implementation {
    private static final MethodDescription.InDefinedShape EQUALS = (MethodDescription.InDefinedShape) TypeDescription.OBJECT.getDeclaredMethods().filter(ElementMatchers.isEquals()).getOnly();
    private final SuperClassCheck superClassCheck;
    private final TypeCompatibilityCheck typeCompatibilityCheck;
    private final ElementMatcher.Junction<? super FieldDescription.InDefinedShape> ignored;
    private final ElementMatcher.Junction<? super FieldDescription.InDefinedShape> nonNullable;
    private final Comparator<? super FieldDescription.InDefinedShape> comparator;

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/EqualsMethod$SuperClassCheck.class */
    public enum SuperClassCheck {
        DISABLED { // from class: net.bytebuddy.implementation.EqualsMethod.SuperClassCheck.1
            @Override // net.bytebuddy.implementation.EqualsMethod.SuperClassCheck
            protected StackManipulation resolve(TypeDescription instrumentedType) {
                return StackManipulation.Trivial.INSTANCE;
            }
        },
        ENABLED { // from class: net.bytebuddy.implementation.EqualsMethod.SuperClassCheck.2
            @Override // net.bytebuddy.implementation.EqualsMethod.SuperClassCheck
            protected StackManipulation resolve(TypeDescription instrumentedType) {
                TypeDefinition superClass = instrumentedType.getSuperClass();
                if (superClass == null) {
                    throw new IllegalStateException(instrumentedType + " does not declare a super class");
                }
                return new StackManipulation.Compound(MethodVariableAccess.loadThis(), MethodVariableAccess.REFERENCE.loadFrom(1), MethodInvocation.invoke(EqualsMethod.EQUALS).special(superClass.asErasure()), ConditionalReturn.onZeroInteger());
            }
        };

        protected abstract StackManipulation resolve(TypeDescription typeDescription);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/EqualsMethod$TypeCompatibilityCheck.class */
    public enum TypeCompatibilityCheck {
        EXACT { // from class: net.bytebuddy.implementation.EqualsMethod.TypeCompatibilityCheck.1
            @Override // net.bytebuddy.implementation.EqualsMethod.TypeCompatibilityCheck
            public StackManipulation resolve(TypeDescription instrumentedType) {
                return new StackManipulation.Compound(MethodVariableAccess.REFERENCE.loadFrom(1), ConditionalReturn.onNullValue(), MethodVariableAccess.REFERENCE.loadFrom(0), MethodInvocation.invoke(GET_CLASS), MethodVariableAccess.REFERENCE.loadFrom(1), MethodInvocation.invoke(GET_CLASS), ConditionalReturn.onNonIdentity());
            }
        },
        SUBCLASS { // from class: net.bytebuddy.implementation.EqualsMethod.TypeCompatibilityCheck.2
            @Override // net.bytebuddy.implementation.EqualsMethod.TypeCompatibilityCheck
            protected StackManipulation resolve(TypeDescription instrumentedType) {
                return new StackManipulation.Compound(MethodVariableAccess.REFERENCE.loadFrom(1), InstanceCheck.of(instrumentedType), ConditionalReturn.onZeroInteger());
            }
        };
        
        protected static final MethodDescription.InDefinedShape GET_CLASS = (MethodDescription.InDefinedShape) TypeDescription.ForLoadedType.of(Object.class).getDeclaredMethods().filter(ElementMatchers.named("getClass")).getOnly();

        protected abstract StackManipulation resolve(TypeDescription typeDescription);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass() && this.superClassCheck.equals(((EqualsMethod) obj).superClassCheck) && this.typeCompatibilityCheck.equals(((EqualsMethod) obj).typeCompatibilityCheck) && this.ignored.equals(((EqualsMethod) obj).ignored) && this.nonNullable.equals(((EqualsMethod) obj).nonNullable) && this.comparator.equals(((EqualsMethod) obj).comparator);
    }

    public int hashCode() {
        return (((((((((17 * 31) + this.superClassCheck.hashCode()) * 31) + this.typeCompatibilityCheck.hashCode()) * 31) + this.ignored.hashCode()) * 31) + this.nonNullable.hashCode()) * 31) + this.comparator.hashCode();
    }

    protected EqualsMethod(SuperClassCheck superClassCheck) {
        this(superClassCheck, TypeCompatibilityCheck.EXACT, ElementMatchers.none(), ElementMatchers.none(), NaturalOrderComparator.INSTANCE);
    }

    private EqualsMethod(SuperClassCheck superClassCheck, TypeCompatibilityCheck typeCompatibilityCheck, ElementMatcher.Junction<? super FieldDescription.InDefinedShape> ignored, ElementMatcher.Junction<? super FieldDescription.InDefinedShape> nonNullable, Comparator<? super FieldDescription.InDefinedShape> comparator) {
        this.superClassCheck = superClassCheck;
        this.typeCompatibilityCheck = typeCompatibilityCheck;
        this.ignored = ignored;
        this.nonNullable = nonNullable;
        this.comparator = comparator;
    }

    public static EqualsMethod requiringSuperClassEquality() {
        return new EqualsMethod(SuperClassCheck.ENABLED);
    }

    public static EqualsMethod isolated() {
        return new EqualsMethod(SuperClassCheck.DISABLED);
    }

    public EqualsMethod withIgnoredFields(ElementMatcher<? super FieldDescription.InDefinedShape> ignored) {
        return new EqualsMethod(this.superClassCheck, this.typeCompatibilityCheck, this.ignored.or(ignored), this.nonNullable, this.comparator);
    }

    public EqualsMethod withNonNullableFields(ElementMatcher<? super FieldDescription.InDefinedShape> nonNullable) {
        return new EqualsMethod(this.superClassCheck, this.typeCompatibilityCheck, this.ignored, this.nonNullable.or(nonNullable), this.comparator);
    }

    public EqualsMethod withPrimitiveTypedFieldsFirst() {
        return withFieldOrder(TypePropertyComparator.FOR_PRIMITIVE_TYPES);
    }

    public EqualsMethod withEnumerationTypedFieldsFirst() {
        return withFieldOrder(TypePropertyComparator.FOR_ENUMERATION_TYPES);
    }

    public EqualsMethod withPrimitiveWrapperTypedFieldsFirst() {
        return withFieldOrder(TypePropertyComparator.FOR_PRIMITIVE_WRAPPER_TYPES);
    }

    public EqualsMethod withStringTypedFieldsFirst() {
        return withFieldOrder(TypePropertyComparator.FOR_STRING_TYPES);
    }

    public EqualsMethod withFieldOrder(Comparator<? super FieldDescription.InDefinedShape> comparator) {
        return new EqualsMethod(this.superClassCheck, this.typeCompatibilityCheck, this.ignored, this.nonNullable, new CompoundComparator(this.comparator, comparator));
    }

    public Implementation withSubclassEquality() {
        return new EqualsMethod(this.superClassCheck, TypeCompatibilityCheck.SUBCLASS, this.ignored, this.nonNullable, this.comparator);
    }

    @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
    public InstrumentedType prepare(InstrumentedType instrumentedType) {
        return instrumentedType;
    }

    @Override // net.bytebuddy.implementation.Implementation
    public ByteCodeAppender appender(Implementation.Target implementationTarget) {
        if (implementationTarget.getInstrumentedType().isInterface()) {
            throw new IllegalStateException("Cannot implement meaningful equals method for " + implementationTarget.getInstrumentedType());
        }
        List<FieldDescription.InDefinedShape> fields = new ArrayList<>(implementationTarget.getInstrumentedType().getDeclaredFields().filter(ElementMatchers.not(ElementMatchers.isStatic().or(this.ignored))));
        Collections.sort(fields, this.comparator);
        return new Appender(implementationTarget.getInstrumentedType(), new StackManipulation.Compound(this.superClassCheck.resolve(implementationTarget.getInstrumentedType()), MethodVariableAccess.loadThis(), MethodVariableAccess.REFERENCE.loadFrom(1), ConditionalReturn.onIdentity().returningTrue(), this.typeCompatibilityCheck.resolve(implementationTarget.getInstrumentedType())), fields, this.nonNullable);
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/EqualsMethod$NullValueGuard.class */
    protected interface NullValueGuard {
        StackManipulation before();

        StackManipulation after();

        int getRequiredVariablePadding();

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/EqualsMethod$NullValueGuard$NoOp.class */
        public enum NoOp implements NullValueGuard {
            INSTANCE;

            @Override // net.bytebuddy.implementation.EqualsMethod.NullValueGuard
            public StackManipulation before() {
                return StackManipulation.Trivial.INSTANCE;
            }

            @Override // net.bytebuddy.implementation.EqualsMethod.NullValueGuard
            public StackManipulation after() {
                return StackManipulation.Trivial.INSTANCE;
            }

            @Override // net.bytebuddy.implementation.EqualsMethod.NullValueGuard
            public int getRequiredVariablePadding() {
                return StackSize.ZERO.getSize();
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/EqualsMethod$NullValueGuard$UsingJump.class */
        public static class UsingJump implements NullValueGuard {
            private static final Object[] EMPTY = new Object[0];
            private static final Object[] REFERENCE = {Type.getInternalName(Object.class)};
            private final MethodDescription instrumentedMethod;
            private final Label firstValueNull = new Label();
            private final Label secondValueNull = new Label();
            private final Label endOfBlock = new Label();

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.instrumentedMethod.equals(((UsingJump) obj).instrumentedMethod) && this.firstValueNull.equals(((UsingJump) obj).firstValueNull) && this.secondValueNull.equals(((UsingJump) obj).secondValueNull) && this.endOfBlock.equals(((UsingJump) obj).endOfBlock);
            }

            public int hashCode() {
                return (((((((17 * 31) + this.instrumentedMethod.hashCode()) * 31) + this.firstValueNull.hashCode()) * 31) + this.secondValueNull.hashCode()) * 31) + this.endOfBlock.hashCode();
            }

            protected UsingJump(MethodDescription instrumentedMethod) {
                this.instrumentedMethod = instrumentedMethod;
            }

            @Override // net.bytebuddy.implementation.EqualsMethod.NullValueGuard
            public StackManipulation before() {
                return new BeforeInstruction();
            }

            @Override // net.bytebuddy.implementation.EqualsMethod.NullValueGuard
            public StackManipulation after() {
                return new AfterInstruction();
            }

            @Override // net.bytebuddy.implementation.EqualsMethod.NullValueGuard
            public int getRequiredVariablePadding() {
                return 2;
            }

            @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/EqualsMethod$NullValueGuard$UsingJump$BeforeInstruction.class */
            protected class BeforeInstruction implements StackManipulation {
                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && UsingJump.this.equals(UsingJump.this);
                }

                public int hashCode() {
                    return (17 * 31) + UsingJump.this.hashCode();
                }

                protected BeforeInstruction() {
                }

                @Override // net.bytebuddy.implementation.bytecode.StackManipulation
                public boolean isValid() {
                    return true;
                }

                @Override // net.bytebuddy.implementation.bytecode.StackManipulation
                public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
                    methodVisitor.visitVarInsn(58, UsingJump.this.instrumentedMethod.getStackSize());
                    methodVisitor.visitVarInsn(58, UsingJump.this.instrumentedMethod.getStackSize() + 1);
                    methodVisitor.visitVarInsn(25, UsingJump.this.instrumentedMethod.getStackSize() + 1);
                    methodVisitor.visitVarInsn(25, UsingJump.this.instrumentedMethod.getStackSize());
                    methodVisitor.visitJumpInsn(198, UsingJump.this.secondValueNull);
                    methodVisitor.visitJumpInsn(198, UsingJump.this.firstValueNull);
                    methodVisitor.visitVarInsn(25, UsingJump.this.instrumentedMethod.getStackSize() + 1);
                    methodVisitor.visitVarInsn(25, UsingJump.this.instrumentedMethod.getStackSize());
                    return new StackManipulation.Size(0, 0);
                }
            }

            @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/EqualsMethod$NullValueGuard$UsingJump$AfterInstruction.class */
            protected class AfterInstruction implements StackManipulation {
                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && UsingJump.this.equals(UsingJump.this);
                }

                public int hashCode() {
                    return (17 * 31) + UsingJump.this.hashCode();
                }

                protected AfterInstruction() {
                }

                @Override // net.bytebuddy.implementation.bytecode.StackManipulation
                public boolean isValid() {
                    return true;
                }

                @Override // net.bytebuddy.implementation.bytecode.StackManipulation
                public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
                    methodVisitor.visitJumpInsn(167, UsingJump.this.endOfBlock);
                    methodVisitor.visitLabel(UsingJump.this.secondValueNull);
                    if (implementationContext.getClassFileVersion().isAtLeast(ClassFileVersion.JAVA_V6)) {
                        methodVisitor.visitFrame(4, UsingJump.EMPTY.length, UsingJump.EMPTY, UsingJump.REFERENCE.length, UsingJump.REFERENCE);
                    }
                    methodVisitor.visitJumpInsn(198, UsingJump.this.endOfBlock);
                    methodVisitor.visitLabel(UsingJump.this.firstValueNull);
                    if (implementationContext.getClassFileVersion().isAtLeast(ClassFileVersion.JAVA_V6)) {
                        methodVisitor.visitFrame(3, UsingJump.EMPTY.length, UsingJump.EMPTY, UsingJump.EMPTY.length, UsingJump.EMPTY);
                    }
                    methodVisitor.visitInsn(3);
                    methodVisitor.visitInsn(172);
                    methodVisitor.visitLabel(UsingJump.this.endOfBlock);
                    if (implementationContext.getClassFileVersion().isAtLeast(ClassFileVersion.JAVA_V6)) {
                        methodVisitor.visitFrame(3, UsingJump.EMPTY.length, UsingJump.EMPTY, UsingJump.EMPTY.length, UsingJump.EMPTY);
                    }
                    return new StackManipulation.Size(0, 0);
                }
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/EqualsMethod$ValueComparator.class */
    protected enum ValueComparator implements StackManipulation {
        LONG { // from class: net.bytebuddy.implementation.EqualsMethod.ValueComparator.1
            @Override // net.bytebuddy.implementation.bytecode.StackManipulation
            public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
                methodVisitor.visitInsn(148);
                return new StackManipulation.Size(-2, 0);
            }
        },
        FLOAT { // from class: net.bytebuddy.implementation.EqualsMethod.ValueComparator.2
            @Override // net.bytebuddy.implementation.bytecode.StackManipulation
            public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
                methodVisitor.visitMethodInsn(184, "java/lang/Float", "compare", "(FF)I", false);
                return new StackManipulation.Size(-1, 0);
            }
        },
        DOUBLE { // from class: net.bytebuddy.implementation.EqualsMethod.ValueComparator.3
            @Override // net.bytebuddy.implementation.bytecode.StackManipulation
            public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
                methodVisitor.visitMethodInsn(184, "java/lang/Double", "compare", "(DD)I", false);
                return new StackManipulation.Size(-2, 0);
            }
        },
        BOOLEAN_ARRAY { // from class: net.bytebuddy.implementation.EqualsMethod.ValueComparator.4
            @Override // net.bytebuddy.implementation.bytecode.StackManipulation
            public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
                methodVisitor.visitMethodInsn(184, "java/util/Arrays", "equals", "([Z[Z)Z", false);
                return new StackManipulation.Size(-1, 0);
            }
        },
        BYTE_ARRAY { // from class: net.bytebuddy.implementation.EqualsMethod.ValueComparator.5
            @Override // net.bytebuddy.implementation.bytecode.StackManipulation
            public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
                methodVisitor.visitMethodInsn(184, "java/util/Arrays", "equals", "([B[B)Z", false);
                return new StackManipulation.Size(-1, 0);
            }
        },
        SHORT_ARRAY { // from class: net.bytebuddy.implementation.EqualsMethod.ValueComparator.6
            @Override // net.bytebuddy.implementation.bytecode.StackManipulation
            public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
                methodVisitor.visitMethodInsn(184, "java/util/Arrays", "equals", "([S[S)Z", false);
                return new StackManipulation.Size(-1, 0);
            }
        },
        CHARACTER_ARRAY { // from class: net.bytebuddy.implementation.EqualsMethod.ValueComparator.7
            @Override // net.bytebuddy.implementation.bytecode.StackManipulation
            public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
                methodVisitor.visitMethodInsn(184, "java/util/Arrays", "equals", "([C[C)Z", false);
                return new StackManipulation.Size(-1, 0);
            }
        },
        INTEGER_ARRAY { // from class: net.bytebuddy.implementation.EqualsMethod.ValueComparator.8
            @Override // net.bytebuddy.implementation.bytecode.StackManipulation
            public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
                methodVisitor.visitMethodInsn(184, "java/util/Arrays", "equals", "([I[I)Z", false);
                return new StackManipulation.Size(-1, 0);
            }
        },
        LONG_ARRAY { // from class: net.bytebuddy.implementation.EqualsMethod.ValueComparator.9
            @Override // net.bytebuddy.implementation.bytecode.StackManipulation
            public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
                methodVisitor.visitMethodInsn(184, "java/util/Arrays", "equals", "([J[J)Z", false);
                return new StackManipulation.Size(-1, 0);
            }
        },
        FLOAT_ARRAY { // from class: net.bytebuddy.implementation.EqualsMethod.ValueComparator.10
            @Override // net.bytebuddy.implementation.bytecode.StackManipulation
            public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
                methodVisitor.visitMethodInsn(184, "java/util/Arrays", "equals", "([F[F)Z", false);
                return new StackManipulation.Size(-1, 0);
            }
        },
        DOUBLE_ARRAY { // from class: net.bytebuddy.implementation.EqualsMethod.ValueComparator.11
            @Override // net.bytebuddy.implementation.bytecode.StackManipulation
            public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
                methodVisitor.visitMethodInsn(184, "java/util/Arrays", "equals", "([D[D)Z", false);
                return new StackManipulation.Size(-1, 0);
            }
        },
        REFERENCE_ARRAY { // from class: net.bytebuddy.implementation.EqualsMethod.ValueComparator.12
            @Override // net.bytebuddy.implementation.bytecode.StackManipulation
            public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
                methodVisitor.visitMethodInsn(184, "java/util/Arrays", "equals", "([Ljava/lang/Object;[Ljava/lang/Object;)Z", false);
                return new StackManipulation.Size(-1, 0);
            }
        },
        NESTED_ARRAY { // from class: net.bytebuddy.implementation.EqualsMethod.ValueComparator.13
            @Override // net.bytebuddy.implementation.bytecode.StackManipulation
            public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
                methodVisitor.visitMethodInsn(184, "java/util/Arrays", "deepEquals", "([Ljava/lang/Object;[Ljava/lang/Object;)Z", false);
                return new StackManipulation.Size(-1, 0);
            }
        };

        public static StackManipulation of(TypeDefinition typeDefinition) {
            if (typeDefinition.represents(Boolean.TYPE) || typeDefinition.represents(Byte.TYPE) || typeDefinition.represents(Short.TYPE) || typeDefinition.represents(Character.TYPE) || typeDefinition.represents(Integer.TYPE)) {
                return ConditionalReturn.onNonEqualInteger();
            }
            if (typeDefinition.represents(Long.TYPE)) {
                return new StackManipulation.Compound(LONG, ConditionalReturn.onNonZeroInteger());
            }
            if (typeDefinition.represents(Float.TYPE)) {
                return new StackManipulation.Compound(FLOAT, ConditionalReturn.onNonZeroInteger());
            }
            if (typeDefinition.represents(Double.TYPE)) {
                return new StackManipulation.Compound(DOUBLE, ConditionalReturn.onNonZeroInteger());
            }
            if (typeDefinition.represents(boolean[].class)) {
                return new StackManipulation.Compound(BOOLEAN_ARRAY, ConditionalReturn.onZeroInteger());
            }
            if (typeDefinition.represents(byte[].class)) {
                return new StackManipulation.Compound(BYTE_ARRAY, ConditionalReturn.onZeroInteger());
            }
            if (typeDefinition.represents(short[].class)) {
                return new StackManipulation.Compound(SHORT_ARRAY, ConditionalReturn.onZeroInteger());
            }
            if (typeDefinition.represents(char[].class)) {
                return new StackManipulation.Compound(CHARACTER_ARRAY, ConditionalReturn.onZeroInteger());
            }
            if (typeDefinition.represents(int[].class)) {
                return new StackManipulation.Compound(INTEGER_ARRAY, ConditionalReturn.onZeroInteger());
            }
            if (typeDefinition.represents(long[].class)) {
                return new StackManipulation.Compound(LONG_ARRAY, ConditionalReturn.onZeroInteger());
            }
            if (typeDefinition.represents(float[].class)) {
                return new StackManipulation.Compound(FLOAT_ARRAY, ConditionalReturn.onZeroInteger());
            }
            if (typeDefinition.represents(double[].class)) {
                return new StackManipulation.Compound(DOUBLE_ARRAY, ConditionalReturn.onZeroInteger());
            }
            if (typeDefinition.isArray()) {
                StackManipulation[] stackManipulationArr = new StackManipulation[2];
                stackManipulationArr[0] = typeDefinition.getComponentType().isArray() ? NESTED_ARRAY : REFERENCE_ARRAY;
                stackManipulationArr[1] = ConditionalReturn.onZeroInteger();
                return new StackManipulation.Compound(stackManipulationArr);
            }
            return new StackManipulation.Compound(MethodInvocation.invoke(EqualsMethod.EQUALS).virtual(typeDefinition.asErasure()), ConditionalReturn.onZeroInteger());
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public boolean isValid() {
            return true;
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/EqualsMethod$Appender.class */
    protected static class Appender implements ByteCodeAppender {
        private final TypeDescription instrumentedType;
        private final StackManipulation baseline;
        private final List<FieldDescription.InDefinedShape> fieldDescriptions;
        private final ElementMatcher<? super FieldDescription.InDefinedShape> nonNullable;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.instrumentedType.equals(((Appender) obj).instrumentedType) && this.baseline.equals(((Appender) obj).baseline) && this.fieldDescriptions.equals(((Appender) obj).fieldDescriptions) && this.nonNullable.equals(((Appender) obj).nonNullable);
        }

        public int hashCode() {
            return (((((((17 * 31) + this.instrumentedType.hashCode()) * 31) + this.baseline.hashCode()) * 31) + this.fieldDescriptions.hashCode()) * 31) + this.nonNullable.hashCode();
        }

        protected Appender(TypeDescription instrumentedType, StackManipulation baseline, List<FieldDescription.InDefinedShape> fieldDescriptions, ElementMatcher<? super FieldDescription.InDefinedShape> nonNullable) {
            this.instrumentedType = instrumentedType;
            this.baseline = baseline;
            this.fieldDescriptions = fieldDescriptions;
            this.nonNullable = nonNullable;
        }

        @Override // net.bytebuddy.implementation.bytecode.ByteCodeAppender
        public ByteCodeAppender.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext, MethodDescription instrumentedMethod) {
            if (instrumentedMethod.isStatic()) {
                throw new IllegalStateException("Hash code method must not be static: " + instrumentedMethod);
            }
            if (instrumentedMethod.getParameters().size() != 1 || ((ParameterDescription) instrumentedMethod.getParameters().getOnly()).getType().isPrimitive()) {
                throw new IllegalStateException();
            }
            if (!instrumentedMethod.getReturnType().represents(Boolean.TYPE)) {
                throw new IllegalStateException("Hash code method does not return primitive boolean: " + instrumentedMethod);
            }
            List<StackManipulation> stackManipulations = new ArrayList<>(3 + (this.fieldDescriptions.size() * 8));
            stackManipulations.add(this.baseline);
            int padding = 0;
            for (FieldDescription.InDefinedShape fieldDescription : this.fieldDescriptions) {
                stackManipulations.add(MethodVariableAccess.loadThis());
                stackManipulations.add(FieldAccess.forField(fieldDescription).read());
                stackManipulations.add(MethodVariableAccess.REFERENCE.loadFrom(1));
                stackManipulations.add(TypeCasting.to(this.instrumentedType));
                stackManipulations.add(FieldAccess.forField(fieldDescription).read());
                NullValueGuard nullValueGuard = (fieldDescription.getType().isPrimitive() || fieldDescription.getType().isArray() || this.nonNullable.matches(fieldDescription)) ? NullValueGuard.NoOp.INSTANCE : new NullValueGuard.UsingJump(instrumentedMethod);
                stackManipulations.add(nullValueGuard.before());
                stackManipulations.add(ValueComparator.of(fieldDescription.getType()));
                stackManipulations.add(nullValueGuard.after());
                padding = Math.max(padding, nullValueGuard.getRequiredVariablePadding());
            }
            stackManipulations.add(IntegerConstant.forValue(true));
            stackManipulations.add(MethodReturn.INTEGER);
            return new ByteCodeAppender.Size(new StackManipulation.Compound(stackManipulations).apply(methodVisitor, implementationContext).getMaximalSize(), instrumentedMethod.getStackSize() + padding);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/EqualsMethod$ConditionalReturn.class */
    public static class ConditionalReturn implements StackManipulation {
        private static final Object[] EMPTY = new Object[0];
        private final int jumpCondition;
        private final int value;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.jumpCondition == ((ConditionalReturn) obj).jumpCondition && this.value == ((ConditionalReturn) obj).value;
        }

        public int hashCode() {
            return (((17 * 31) + this.jumpCondition) * 31) + this.value;
        }

        protected ConditionalReturn(int jumpCondition) {
            this(jumpCondition, 3);
        }

        private ConditionalReturn(int jumpCondition, int value) {
            this.jumpCondition = jumpCondition;
            this.value = value;
        }

        protected static ConditionalReturn onZeroInteger() {
            return new ConditionalReturn(154);
        }

        protected static ConditionalReturn onNonZeroInteger() {
            return new ConditionalReturn(153);
        }

        protected static ConditionalReturn onNullValue() {
            return new ConditionalReturn(199);
        }

        protected static ConditionalReturn onNonIdentity() {
            return new ConditionalReturn(165);
        }

        protected static ConditionalReturn onIdentity() {
            return new ConditionalReturn(166);
        }

        protected static ConditionalReturn onNonEqualInteger() {
            return new ConditionalReturn(159);
        }

        protected StackManipulation returningTrue() {
            return new ConditionalReturn(this.jumpCondition, 4);
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public boolean isValid() {
            return true;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
            Label label = new Label();
            methodVisitor.visitJumpInsn(this.jumpCondition, label);
            methodVisitor.visitInsn(this.value);
            methodVisitor.visitInsn(172);
            methodVisitor.visitLabel(label);
            if (implementationContext.getClassFileVersion().isAtLeast(ClassFileVersion.JAVA_V6)) {
                methodVisitor.visitFrame(3, EMPTY.length, EMPTY, EMPTY.length, EMPTY);
            }
            return new StackManipulation.Size(-1, 1);
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/EqualsMethod$NaturalOrderComparator.class */
    protected enum NaturalOrderComparator implements Comparator<FieldDescription.InDefinedShape> {
        INSTANCE;

        @Override // java.util.Comparator
        public int compare(FieldDescription.InDefinedShape left, FieldDescription.InDefinedShape right) {
            return 0;
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/EqualsMethod$TypePropertyComparator.class */
    protected enum TypePropertyComparator implements Comparator<FieldDescription.InDefinedShape> {
        FOR_PRIMITIVE_TYPES { // from class: net.bytebuddy.implementation.EqualsMethod.TypePropertyComparator.1
            @Override // net.bytebuddy.implementation.EqualsMethod.TypePropertyComparator, java.util.Comparator
            public /* bridge */ /* synthetic */ int compare(FieldDescription.InDefinedShape inDefinedShape, FieldDescription.InDefinedShape inDefinedShape2) {
                return super.compare(inDefinedShape, inDefinedShape2);
            }

            @Override // net.bytebuddy.implementation.EqualsMethod.TypePropertyComparator
            protected boolean resolve(TypeDefinition typeDefinition) {
                return typeDefinition.isPrimitive();
            }
        },
        FOR_ENUMERATION_TYPES { // from class: net.bytebuddy.implementation.EqualsMethod.TypePropertyComparator.2
            @Override // net.bytebuddy.implementation.EqualsMethod.TypePropertyComparator, java.util.Comparator
            public /* bridge */ /* synthetic */ int compare(FieldDescription.InDefinedShape inDefinedShape, FieldDescription.InDefinedShape inDefinedShape2) {
                return super.compare(inDefinedShape, inDefinedShape2);
            }

            @Override // net.bytebuddy.implementation.EqualsMethod.TypePropertyComparator
            protected boolean resolve(TypeDefinition typeDefinition) {
                return typeDefinition.isEnum();
            }
        },
        FOR_STRING_TYPES { // from class: net.bytebuddy.implementation.EqualsMethod.TypePropertyComparator.3
            @Override // net.bytebuddy.implementation.EqualsMethod.TypePropertyComparator, java.util.Comparator
            public /* bridge */ /* synthetic */ int compare(FieldDescription.InDefinedShape inDefinedShape, FieldDescription.InDefinedShape inDefinedShape2) {
                return super.compare(inDefinedShape, inDefinedShape2);
            }

            @Override // net.bytebuddy.implementation.EqualsMethod.TypePropertyComparator
            protected boolean resolve(TypeDefinition typeDefinition) {
                return typeDefinition.represents(String.class);
            }
        },
        FOR_PRIMITIVE_WRAPPER_TYPES { // from class: net.bytebuddy.implementation.EqualsMethod.TypePropertyComparator.4
            @Override // net.bytebuddy.implementation.EqualsMethod.TypePropertyComparator, java.util.Comparator
            public /* bridge */ /* synthetic */ int compare(FieldDescription.InDefinedShape inDefinedShape, FieldDescription.InDefinedShape inDefinedShape2) {
                return super.compare(inDefinedShape, inDefinedShape2);
            }

            @Override // net.bytebuddy.implementation.EqualsMethod.TypePropertyComparator
            protected boolean resolve(TypeDefinition typeDefinition) {
                return typeDefinition.asErasure().isPrimitiveWrapper();
            }
        };

        protected abstract boolean resolve(TypeDefinition typeDefinition);

        @Override // java.util.Comparator
        public int compare(FieldDescription.InDefinedShape left, FieldDescription.InDefinedShape right) {
            if (resolve(left.getType()) && !resolve(right.getType())) {
                return -1;
            }
            if (!resolve(left.getType()) && resolve(right.getType())) {
                return 1;
            }
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @SuppressFBWarnings(value = {"SE_COMPARATOR_SHOULD_BE_SERIALIZABLE"}, justification = "Not used within a serializable instance")
    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/EqualsMethod$CompoundComparator.class */
    public static class CompoundComparator implements Comparator<FieldDescription.InDefinedShape> {
        private final List<Comparator<? super FieldDescription.InDefinedShape>> comparators;

        public int hashCode() {
            return (17 * 31) + this.comparators.hashCode();
        }

        @Override // java.util.Comparator
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.comparators.equals(((CompoundComparator) obj).comparators);
        }

        protected CompoundComparator(Comparator<? super FieldDescription.InDefinedShape>... comparator) {
            this(Arrays.asList(comparator));
        }

        protected CompoundComparator(List<? extends Comparator<? super FieldDescription.InDefinedShape>> comparators) {
            this.comparators = new ArrayList();
            for (Comparator<? super FieldDescription.InDefinedShape> comparator : comparators) {
                if (comparator instanceof CompoundComparator) {
                    this.comparators.addAll(((CompoundComparator) comparator).comparators);
                } else if (!(comparator instanceof NaturalOrderComparator)) {
                    this.comparators.add(comparator);
                }
            }
        }

        @Override // java.util.Comparator
        public int compare(FieldDescription.InDefinedShape left, FieldDescription.InDefinedShape right) {
            for (Comparator<? super FieldDescription.InDefinedShape> comparator : this.comparators) {
                int comparison = comparator.compare(left, right);
                if (comparison != 0) {
                    return comparison;
                }
            }
            return 0;
        }
    }
}
