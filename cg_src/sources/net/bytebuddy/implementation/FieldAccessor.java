package net.bytebuddy.implementation;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.ParameterDescription;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.scaffold.FieldLocator;
import net.bytebuddy.dynamic.scaffold.InstrumentedType;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.LoadedTypeInitializer;
import net.bytebuddy.implementation.bytecode.ByteCodeAppender;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
import net.bytebuddy.implementation.bytecode.constant.ClassConstant;
import net.bytebuddy.implementation.bytecode.constant.DefaultValue;
import net.bytebuddy.implementation.bytecode.constant.DoubleConstant;
import net.bytebuddy.implementation.bytecode.constant.FloatConstant;
import net.bytebuddy.implementation.bytecode.constant.IntegerConstant;
import net.bytebuddy.implementation.bytecode.constant.JavaConstantValue;
import net.bytebuddy.implementation.bytecode.constant.LongConstant;
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
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/FieldAccessor.class */
public abstract class FieldAccessor implements Implementation {
    protected final FieldLocation fieldLocation;
    protected final Assigner assigner;
    protected final Assigner.Typing typing;

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/FieldAccessor$AssignerConfigurable.class */
    public interface AssignerConfigurable extends PropertyConfigurable {
        PropertyConfigurable withAssigner(Assigner assigner, Assigner.Typing typing);
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/FieldAccessor$OwnerTypeLocatable.class */
    public interface OwnerTypeLocatable extends AssignerConfigurable {
        AssignerConfigurable in(Class<?> cls);

        AssignerConfigurable in(TypeDescription typeDescription);

        AssignerConfigurable in(FieldLocator.Factory factory);
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/FieldAccessor$PropertyConfigurable.class */
    public interface PropertyConfigurable extends Implementation {
        Implementation.Composable setsArgumentAt(int i);

        Implementation.Composable setsDefaultValue();

        Implementation.Composable setsValue(Object obj);

        Implementation.Composable setsValue(TypeDescription typeDescription);

        Implementation.Composable setsValue(JavaConstant javaConstant);

        Implementation.Composable setsValue(StackManipulation stackManipulation, Type type);

        Implementation.Composable setsValue(StackManipulation stackManipulation, TypeDescription.Generic generic);

        Implementation.Composable setsReference(Object obj);

        Implementation.Composable setsReference(Object obj, String str);

        Implementation.Composable setsFieldValueOf(Field field);

        Implementation.Composable setsFieldValueOf(FieldDescription fieldDescription);

        Implementation.Composable setsFieldValueOf(String str);

        Implementation.Composable setsFieldValueOf(FieldNameExtractor fieldNameExtractor);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass() && this.typing.equals(((FieldAccessor) obj).typing) && this.fieldLocation.equals(((FieldAccessor) obj).fieldLocation) && this.assigner.equals(((FieldAccessor) obj).assigner);
    }

    public int hashCode() {
        return (((((17 * 31) + this.fieldLocation.hashCode()) * 31) + this.assigner.hashCode()) * 31) + this.typing.hashCode();
    }

    protected FieldAccessor(FieldLocation fieldLocation, Assigner assigner, Assigner.Typing typing) {
        this.fieldLocation = fieldLocation;
        this.assigner = assigner;
        this.typing = typing;
    }

    public static OwnerTypeLocatable ofField(String name) {
        return of(new FieldNameExtractor.ForFixedValue(name));
    }

    public static OwnerTypeLocatable ofBeanProperty() {
        return of(FieldNameExtractor.ForBeanProperty.INSTANCE);
    }

    public static OwnerTypeLocatable of(FieldNameExtractor fieldNameExtractor) {
        return new ForImplicitProperty(new FieldLocation.Relative(fieldNameExtractor));
    }

    public static AssignerConfigurable of(Field field) {
        return of(new FieldDescription.ForLoadedField(field));
    }

    public static AssignerConfigurable of(FieldDescription fieldDescription) {
        return new ForImplicitProperty(new FieldLocation.Absolute(fieldDescription));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/FieldAccessor$FieldLocation.class */
    public interface FieldLocation {

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/FieldAccessor$FieldLocation$Prepared.class */
        public interface Prepared {
            FieldDescription resolve(MethodDescription methodDescription);
        }

        FieldLocation with(FieldLocator.Factory factory);

        Prepared prepare(TypeDescription typeDescription);

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/FieldAccessor$FieldLocation$Absolute.class */
        public static class Absolute implements FieldLocation, Prepared {
            private final FieldDescription fieldDescription;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.fieldDescription.equals(((Absolute) obj).fieldDescription);
            }

            public int hashCode() {
                return (17 * 31) + this.fieldDescription.hashCode();
            }

            protected Absolute(FieldDescription fieldDescription) {
                this.fieldDescription = fieldDescription;
            }

            @Override // net.bytebuddy.implementation.FieldAccessor.FieldLocation
            public FieldLocation with(FieldLocator.Factory fieldLocatorFactory) {
                throw new IllegalStateException("Cannot specify a field locator factory for an absolute field location");
            }

            @Override // net.bytebuddy.implementation.FieldAccessor.FieldLocation
            public Prepared prepare(TypeDescription instrumentedType) {
                if (!this.fieldDescription.isStatic() && !instrumentedType.isAssignableTo(this.fieldDescription.getDeclaringType().asErasure())) {
                    throw new IllegalStateException(this.fieldDescription + " is not declared by " + instrumentedType);
                }
                if (!this.fieldDescription.isAccessibleTo(instrumentedType)) {
                    throw new IllegalStateException("Cannot access " + this.fieldDescription + " from " + instrumentedType);
                }
                return this;
            }

            @Override // net.bytebuddy.implementation.FieldAccessor.FieldLocation.Prepared
            public FieldDescription resolve(MethodDescription instrumentedMethod) {
                return this.fieldDescription;
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/FieldAccessor$FieldLocation$Relative.class */
        public static class Relative implements FieldLocation {
            private final FieldNameExtractor fieldNameExtractor;
            private final FieldLocator.Factory fieldLocatorFactory;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.fieldNameExtractor.equals(((Relative) obj).fieldNameExtractor) && this.fieldLocatorFactory.equals(((Relative) obj).fieldLocatorFactory);
            }

            public int hashCode() {
                return (((17 * 31) + this.fieldNameExtractor.hashCode()) * 31) + this.fieldLocatorFactory.hashCode();
            }

            protected Relative(FieldNameExtractor fieldNameExtractor) {
                this(fieldNameExtractor, FieldLocator.ForClassHierarchy.Factory.INSTANCE);
            }

            private Relative(FieldNameExtractor fieldNameExtractor, FieldLocator.Factory fieldLocatorFactory) {
                this.fieldNameExtractor = fieldNameExtractor;
                this.fieldLocatorFactory = fieldLocatorFactory;
            }

            @Override // net.bytebuddy.implementation.FieldAccessor.FieldLocation
            public FieldLocation with(FieldLocator.Factory fieldLocatorFactory) {
                return new Relative(this.fieldNameExtractor, fieldLocatorFactory);
            }

            @Override // net.bytebuddy.implementation.FieldAccessor.FieldLocation
            public Prepared prepare(TypeDescription instrumentedType) {
                return new Prepared(this.fieldNameExtractor, this.fieldLocatorFactory.make(instrumentedType));
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/FieldAccessor$FieldLocation$Relative$Prepared.class */
            protected static class Prepared implements Prepared {
                private final FieldNameExtractor fieldNameExtractor;
                private final FieldLocator fieldLocator;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.fieldNameExtractor.equals(((Prepared) obj).fieldNameExtractor) && this.fieldLocator.equals(((Prepared) obj).fieldLocator);
                }

                public int hashCode() {
                    return (((17 * 31) + this.fieldNameExtractor.hashCode()) * 31) + this.fieldLocator.hashCode();
                }

                protected Prepared(FieldNameExtractor fieldNameExtractor, FieldLocator fieldLocator) {
                    this.fieldNameExtractor = fieldNameExtractor;
                    this.fieldLocator = fieldLocator;
                }

                @Override // net.bytebuddy.implementation.FieldAccessor.FieldLocation.Prepared
                public FieldDescription resolve(MethodDescription instrumentedMethod) {
                    FieldLocator.Resolution resolution = this.fieldLocator.locate(this.fieldNameExtractor.resolve(instrumentedMethod));
                    if (!resolution.isResolved()) {
                        throw new IllegalStateException("Cannot resolve field for " + instrumentedMethod + " using " + this.fieldLocator);
                    }
                    return resolution.getField();
                }
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/FieldAccessor$FieldNameExtractor.class */
    public interface FieldNameExtractor {
        String resolve(MethodDescription methodDescription);

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/FieldAccessor$FieldNameExtractor$ForBeanProperty.class */
        public enum ForBeanProperty implements FieldNameExtractor {
            INSTANCE;

            @Override // net.bytebuddy.implementation.FieldAccessor.FieldNameExtractor
            public String resolve(MethodDescription methodDescription) {
                int crop;
                String name;
                String name2 = methodDescription.getInternalName();
                if (name2.startsWith("get") || name2.startsWith("set")) {
                    crop = 3;
                } else if (name2.startsWith("is")) {
                    crop = 2;
                } else {
                    throw new IllegalArgumentException(methodDescription + " does not follow Java bean naming conventions");
                }
                if (name2.substring(crop).length() == 0) {
                    throw new IllegalArgumentException(methodDescription + " does not specify a bean name");
                }
                return Character.toLowerCase(name.charAt(0)) + name.substring(1);
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/FieldAccessor$FieldNameExtractor$ForFixedValue.class */
        public static class ForFixedValue implements FieldNameExtractor {
            private final String name;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.name.equals(((ForFixedValue) obj).name);
            }

            public int hashCode() {
                return (17 * 31) + this.name.hashCode();
            }

            protected ForFixedValue(String name) {
                this.name = name;
            }

            @Override // net.bytebuddy.implementation.FieldAccessor.FieldNameExtractor
            public String resolve(MethodDescription methodDescription) {
                return this.name;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/FieldAccessor$ForImplicitProperty.class */
    public static class ForImplicitProperty extends FieldAccessor implements OwnerTypeLocatable {
        protected ForImplicitProperty(FieldLocation fieldLocation) {
            this(fieldLocation, Assigner.DEFAULT, Assigner.Typing.STATIC);
        }

        private ForImplicitProperty(FieldLocation fieldLocation, Assigner assigner, Assigner.Typing typing) {
            super(fieldLocation, assigner, typing);
        }

        @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
        public InstrumentedType prepare(InstrumentedType instrumentedType) {
            return instrumentedType;
        }

        @Override // net.bytebuddy.implementation.Implementation
        public ByteCodeAppender appender(Implementation.Target implementationTarget) {
            return new Appender(this.fieldLocation.prepare(implementationTarget.getInstrumentedType()));
        }

        @Override // net.bytebuddy.implementation.FieldAccessor.PropertyConfigurable
        public Implementation.Composable setsArgumentAt(int index) {
            if (index < 0) {
                throw new IllegalArgumentException("A parameter index cannot be negative: " + index);
            }
            return new ForSetter.OfParameterValue(this.fieldLocation, this.assigner, this.typing, ForSetter.TerminationHandler.RETURNING, index);
        }

        @Override // net.bytebuddy.implementation.FieldAccessor.PropertyConfigurable
        public Implementation.Composable setsDefaultValue() {
            return new ForSetter.OfDefaultValue(this.fieldLocation, this.assigner, this.typing, ForSetter.TerminationHandler.RETURNING);
        }

        @Override // net.bytebuddy.implementation.FieldAccessor.PropertyConfigurable
        public Implementation.Composable setsValue(Object value) {
            Class<?> type = value.getClass();
            if (type == String.class) {
                return setsValue(new TextConstant((String) value), String.class);
            }
            if (type == Class.class) {
                return setsValue(ClassConstant.of(TypeDescription.ForLoadedType.of((Class) value)), Class.class);
            }
            if (type == Boolean.class) {
                return setsValue(IntegerConstant.forValue(((Boolean) value).booleanValue()), Boolean.TYPE);
            }
            if (type == Byte.class) {
                return setsValue(IntegerConstant.forValue(((Byte) value).byteValue()), Byte.TYPE);
            }
            if (type == Short.class) {
                return setsValue(IntegerConstant.forValue(((Short) value).shortValue()), Short.TYPE);
            }
            if (type == Character.class) {
                return setsValue(IntegerConstant.forValue(((Character) value).charValue()), Character.TYPE);
            }
            if (type == Integer.class) {
                return setsValue(IntegerConstant.forValue(((Integer) value).intValue()), Integer.TYPE);
            }
            if (type == Long.class) {
                return setsValue(LongConstant.forValue(((Long) value).longValue()), Long.TYPE);
            }
            if (type == Float.class) {
                return setsValue(FloatConstant.forValue(((Float) value).floatValue()), Float.TYPE);
            }
            if (type == Double.class) {
                return setsValue(DoubleConstant.forValue(((Double) value).doubleValue()), Double.TYPE);
            }
            if (JavaType.METHOD_HANDLE.getTypeStub().isAssignableFrom(type)) {
                return setsValue(new JavaConstantValue(JavaConstant.MethodHandle.ofLoaded(value)), type);
            }
            if (JavaType.METHOD_TYPE.getTypeStub().represents(type)) {
                return setsValue(new JavaConstantValue(JavaConstant.MethodType.ofLoaded(value)), type);
            }
            return setsReference(value);
        }

        @Override // net.bytebuddy.implementation.FieldAccessor.PropertyConfigurable
        public Implementation.Composable setsValue(TypeDescription typeDescription) {
            return setsValue(ClassConstant.of(typeDescription), Class.class);
        }

        @Override // net.bytebuddy.implementation.FieldAccessor.PropertyConfigurable
        public Implementation.Composable setsValue(JavaConstant constant) {
            return setsValue(new JavaConstantValue(constant), constant.getType().asGenericType());
        }

        @Override // net.bytebuddy.implementation.FieldAccessor.PropertyConfigurable
        public Implementation.Composable setsValue(StackManipulation stackManipulation, Type type) {
            return setsValue(stackManipulation, TypeDefinition.Sort.describe(type));
        }

        @Override // net.bytebuddy.implementation.FieldAccessor.PropertyConfigurable
        public Implementation.Composable setsValue(StackManipulation stackManipulation, TypeDescription.Generic typeDescription) {
            return new ForSetter.OfConstantValue(this.fieldLocation, this.assigner, this.typing, ForSetter.TerminationHandler.RETURNING, typeDescription, stackManipulation);
        }

        @Override // net.bytebuddy.implementation.FieldAccessor.PropertyConfigurable
        public Implementation.Composable setsReference(Object value) {
            return setsReference(value, "fixedFieldValue$" + RandomString.hashOf(value.hashCode()));
        }

        @Override // net.bytebuddy.implementation.FieldAccessor.PropertyConfigurable
        public Implementation.Composable setsReference(Object value, String name) {
            return new ForSetter.OfReferenceValue(this.fieldLocation, this.assigner, this.typing, ForSetter.TerminationHandler.RETURNING, value, name);
        }

        @Override // net.bytebuddy.implementation.FieldAccessor.PropertyConfigurable
        public Implementation.Composable setsFieldValueOf(Field field) {
            return setsFieldValueOf(new FieldDescription.ForLoadedField(field));
        }

        @Override // net.bytebuddy.implementation.FieldAccessor.PropertyConfigurable
        public Implementation.Composable setsFieldValueOf(FieldDescription fieldDescription) {
            return new ForSetter.OfFieldValue(this.fieldLocation, this.assigner, this.typing, ForSetter.TerminationHandler.RETURNING, new FieldLocation.Absolute(fieldDescription));
        }

        @Override // net.bytebuddy.implementation.FieldAccessor.PropertyConfigurable
        public Implementation.Composable setsFieldValueOf(String fieldName) {
            return setsFieldValueOf(new FieldNameExtractor.ForFixedValue(fieldName));
        }

        @Override // net.bytebuddy.implementation.FieldAccessor.PropertyConfigurable
        public Implementation.Composable setsFieldValueOf(FieldNameExtractor fieldNameExtractor) {
            return new ForSetter.OfFieldValue(this.fieldLocation, this.assigner, this.typing, ForSetter.TerminationHandler.RETURNING, new FieldLocation.Relative(fieldNameExtractor));
        }

        @Override // net.bytebuddy.implementation.FieldAccessor.AssignerConfigurable
        public PropertyConfigurable withAssigner(Assigner assigner, Assigner.Typing typing) {
            return new ForImplicitProperty(this.fieldLocation, assigner, typing);
        }

        @Override // net.bytebuddy.implementation.FieldAccessor.OwnerTypeLocatable
        public AssignerConfigurable in(Class<?> type) {
            return in(TypeDescription.ForLoadedType.of(type));
        }

        @Override // net.bytebuddy.implementation.FieldAccessor.OwnerTypeLocatable
        public AssignerConfigurable in(TypeDescription typeDescription) {
            return in(new FieldLocator.ForExactType.Factory(typeDescription));
        }

        @Override // net.bytebuddy.implementation.FieldAccessor.OwnerTypeLocatable
        public AssignerConfigurable in(FieldLocator.Factory fieldLocatorFactory) {
            return new ForImplicitProperty(this.fieldLocation.with(fieldLocatorFactory), this.assigner, this.typing);
        }

        @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/FieldAccessor$ForImplicitProperty$Appender.class */
        protected class Appender implements ByteCodeAppender {
            private final FieldLocation.Prepared fieldLocation;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.fieldLocation.equals(((Appender) obj).fieldLocation) && ForImplicitProperty.this.equals(ForImplicitProperty.this);
            }

            public int hashCode() {
                return (((17 * 31) + this.fieldLocation.hashCode()) * 31) + ForImplicitProperty.this.hashCode();
            }

            protected Appender(FieldLocation.Prepared fieldLocation) {
                this.fieldLocation = fieldLocation;
            }

            @Override // net.bytebuddy.implementation.bytecode.ByteCodeAppender
            public ByteCodeAppender.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext, MethodDescription instrumentedMethod) {
                StackManipulation implementation;
                if (!instrumentedMethod.isMethod()) {
                    throw new IllegalArgumentException(instrumentedMethod + " does not describe a field getter or setter");
                }
                FieldDescription fieldDescription = this.fieldLocation.resolve(instrumentedMethod);
                if (!fieldDescription.isStatic() && instrumentedMethod.isStatic()) {
                    throw new IllegalStateException("Cannot set instance field " + fieldDescription + " from " + instrumentedMethod);
                }
                StackManipulation initialization = fieldDescription.isStatic() ? StackManipulation.Trivial.INSTANCE : MethodVariableAccess.loadThis();
                if (!instrumentedMethod.getReturnType().represents(Void.TYPE)) {
                    implementation = new StackManipulation.Compound(initialization, FieldAccess.forField(fieldDescription).read(), ForImplicitProperty.this.assigner.assign(fieldDescription.getType(), instrumentedMethod.getReturnType(), ForImplicitProperty.this.typing), MethodReturn.of(instrumentedMethod.getReturnType()));
                } else if (instrumentedMethod.getReturnType().represents(Void.TYPE) && instrumentedMethod.getParameters().size() == 1) {
                    if (fieldDescription.isFinal() && instrumentedMethod.isMethod()) {
                        throw new IllegalStateException("Cannot set final field " + fieldDescription + " from " + instrumentedMethod);
                    }
                    implementation = new StackManipulation.Compound(initialization, MethodVariableAccess.load((ParameterDescription) instrumentedMethod.getParameters().get(0)), ForImplicitProperty.this.assigner.assign(((ParameterDescription) instrumentedMethod.getParameters().get(0)).getType(), fieldDescription.getType(), ForImplicitProperty.this.typing), FieldAccess.forField(fieldDescription).write(), MethodReturn.VOID);
                } else {
                    throw new IllegalArgumentException("Method " + instrumentedMethod + " is no bean accessor");
                }
                if (!implementation.isValid()) {
                    throw new IllegalStateException("Cannot set or get value of " + instrumentedMethod + " using " + fieldDescription);
                }
                return new ByteCodeAppender.Size(implementation.apply(methodVisitor, implementationContext).getMaximalSize(), instrumentedMethod.getStackSize());
            }
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/FieldAccessor$ForSetter.class */
    protected static abstract class ForSetter<T> extends FieldAccessor implements Implementation.Composable {
        private final TerminationHandler terminationHandler;

        /* JADX INFO: Access modifiers changed from: protected */
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/FieldAccessor$ForSetter$TerminationHandler.class */
        public enum TerminationHandler {
            RETURNING { // from class: net.bytebuddy.implementation.FieldAccessor.ForSetter.TerminationHandler.1
                @Override // net.bytebuddy.implementation.FieldAccessor.ForSetter.TerminationHandler
                protected StackManipulation resolve(MethodDescription instrumentedMethod) {
                    if (!instrumentedMethod.getReturnType().represents(Void.TYPE)) {
                        throw new IllegalStateException("Cannot implement setter with return value for " + instrumentedMethod);
                    }
                    return MethodReturn.VOID;
                }
            },
            NON_OPERATIONAL { // from class: net.bytebuddy.implementation.FieldAccessor.ForSetter.TerminationHandler.2
                @Override // net.bytebuddy.implementation.FieldAccessor.ForSetter.TerminationHandler
                protected StackManipulation resolve(MethodDescription instrumentedMethod) {
                    return StackManipulation.Trivial.INSTANCE;
                }
            };

            protected abstract StackManipulation resolve(MethodDescription methodDescription);
        }

        protected abstract T initialize(TypeDescription typeDescription);

        protected abstract StackManipulation resolve(T t, FieldDescription fieldDescription, TypeDescription typeDescription, MethodDescription methodDescription);

        @Override // net.bytebuddy.implementation.FieldAccessor
        public boolean equals(Object obj) {
            if (super.equals(obj)) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.terminationHandler.equals(((ForSetter) obj).terminationHandler);
            }
            return false;
        }

        @Override // net.bytebuddy.implementation.FieldAccessor
        public int hashCode() {
            return (super.hashCode() * 31) + this.terminationHandler.hashCode();
        }

        protected ForSetter(FieldLocation fieldLocation, Assigner assigner, Assigner.Typing typing, TerminationHandler terminationHandler) {
            super(fieldLocation, assigner, typing);
            this.terminationHandler = terminationHandler;
        }

        @Override // net.bytebuddy.implementation.Implementation
        public ByteCodeAppender appender(Implementation.Target implementationTarget) {
            return new Appender(implementationTarget.getInstrumentedType(), initialize(implementationTarget.getInstrumentedType()), this.fieldLocation.prepare(implementationTarget.getInstrumentedType()));
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/FieldAccessor$ForSetter$OfParameterValue.class */
        protected static class OfParameterValue extends ForSetter<Void> {
            private final int index;

            @Override // net.bytebuddy.implementation.FieldAccessor.ForSetter, net.bytebuddy.implementation.FieldAccessor
            public boolean equals(Object obj) {
                if (super.equals(obj)) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.index == ((OfParameterValue) obj).index;
                }
                return false;
            }

            @Override // net.bytebuddy.implementation.FieldAccessor.ForSetter, net.bytebuddy.implementation.FieldAccessor
            public int hashCode() {
                return (super.hashCode() * 31) + this.index;
            }

            protected OfParameterValue(FieldLocation fieldLocation, Assigner assigner, Assigner.Typing typing, TerminationHandler terminationHandler, int index) {
                super(fieldLocation, assigner, typing, terminationHandler);
                this.index = index;
            }

            @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
            public InstrumentedType prepare(InstrumentedType instrumentedType) {
                return instrumentedType;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // net.bytebuddy.implementation.FieldAccessor.ForSetter
            public Void initialize(TypeDescription instrumentedType) {
                return null;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // net.bytebuddy.implementation.FieldAccessor.ForSetter
            public StackManipulation resolve(Void unused, FieldDescription fieldDescription, TypeDescription instrumentedType, MethodDescription instrumentedMethod) {
                if (instrumentedMethod.getParameters().size() <= this.index) {
                    throw new IllegalStateException(instrumentedMethod + " does not define a parameter with index " + this.index);
                }
                return new StackManipulation.Compound(MethodVariableAccess.load((ParameterDescription) instrumentedMethod.getParameters().get(this.index)), this.assigner.assign(((ParameterDescription) instrumentedMethod.getParameters().get(this.index)).getType(), fieldDescription.getType(), this.typing));
            }

            @Override // net.bytebuddy.implementation.Implementation.Composable
            public Implementation andThen(Implementation implementation) {
                return new Implementation.Compound(new OfParameterValue(this.fieldLocation, this.assigner, this.typing, TerminationHandler.NON_OPERATIONAL, this.index), implementation);
            }

            @Override // net.bytebuddy.implementation.Implementation.Composable
            public Implementation.Composable andThen(Implementation.Composable implementation) {
                return new Implementation.Compound.Composable(new OfParameterValue(this.fieldLocation, this.assigner, this.typing, TerminationHandler.NON_OPERATIONAL, this.index), implementation);
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/FieldAccessor$ForSetter$OfDefaultValue.class */
        protected static class OfDefaultValue extends ForSetter<Void> {
            protected OfDefaultValue(FieldLocation fieldLocation, Assigner assigner, Assigner.Typing typing, TerminationHandler terminationHandler) {
                super(fieldLocation, assigner, typing, terminationHandler);
            }

            @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
            public InstrumentedType prepare(InstrumentedType instrumentedType) {
                return instrumentedType;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // net.bytebuddy.implementation.FieldAccessor.ForSetter
            public Void initialize(TypeDescription instrumentedType) {
                return null;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // net.bytebuddy.implementation.FieldAccessor.ForSetter
            public StackManipulation resolve(Void initialized, FieldDescription fieldDescription, TypeDescription instrumentedType, MethodDescription instrumentedMethod) {
                return DefaultValue.of(fieldDescription.getType());
            }

            @Override // net.bytebuddy.implementation.Implementation.Composable
            public Implementation andThen(Implementation implementation) {
                return new Implementation.Compound(new OfDefaultValue(this.fieldLocation, this.assigner, this.typing, TerminationHandler.NON_OPERATIONAL), implementation);
            }

            @Override // net.bytebuddy.implementation.Implementation.Composable
            public Implementation.Composable andThen(Implementation.Composable implementation) {
                return new Implementation.Compound.Composable(new OfDefaultValue(this.fieldLocation, this.assigner, this.typing, TerminationHandler.NON_OPERATIONAL), implementation);
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/FieldAccessor$ForSetter$OfConstantValue.class */
        public static class OfConstantValue extends ForSetter<Void> {
            private final TypeDescription.Generic typeDescription;
            private final StackManipulation stackManipulation;

            @Override // net.bytebuddy.implementation.FieldAccessor.ForSetter, net.bytebuddy.implementation.FieldAccessor
            public boolean equals(Object obj) {
                if (super.equals(obj)) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.typeDescription.equals(((OfConstantValue) obj).typeDescription) && this.stackManipulation.equals(((OfConstantValue) obj).stackManipulation);
                }
                return false;
            }

            @Override // net.bytebuddy.implementation.FieldAccessor.ForSetter, net.bytebuddy.implementation.FieldAccessor
            public int hashCode() {
                return (((super.hashCode() * 31) + this.typeDescription.hashCode()) * 31) + this.stackManipulation.hashCode();
            }

            protected OfConstantValue(FieldLocation fieldLocation, Assigner assigner, Assigner.Typing typing, TerminationHandler terminationHandler, TypeDescription.Generic typeDescription, StackManipulation stackManipulation) {
                super(fieldLocation, assigner, typing, terminationHandler);
                this.typeDescription = typeDescription;
                this.stackManipulation = stackManipulation;
            }

            @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
            public InstrumentedType prepare(InstrumentedType instrumentedType) {
                return instrumentedType;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // net.bytebuddy.implementation.FieldAccessor.ForSetter
            public Void initialize(TypeDescription instrumentedType) {
                return null;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // net.bytebuddy.implementation.FieldAccessor.ForSetter
            public StackManipulation resolve(Void unused, FieldDescription fieldDescription, TypeDescription instrumentedType, MethodDescription instrumentedMethod) {
                return new StackManipulation.Compound(this.stackManipulation, this.assigner.assign(this.typeDescription, fieldDescription.getType(), this.typing));
            }

            @Override // net.bytebuddy.implementation.Implementation.Composable
            public Implementation andThen(Implementation implementation) {
                return new Implementation.Compound(new OfConstantValue(this.fieldLocation, this.assigner, this.typing, TerminationHandler.NON_OPERATIONAL, this.typeDescription, this.stackManipulation), implementation);
            }

            @Override // net.bytebuddy.implementation.Implementation.Composable
            public Implementation.Composable andThen(Implementation.Composable implementation) {
                return new Implementation.Compound.Composable(new OfConstantValue(this.fieldLocation, this.assigner, this.typing, TerminationHandler.NON_OPERATIONAL, this.typeDescription, this.stackManipulation), implementation);
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/FieldAccessor$ForSetter$OfReferenceValue.class */
        public static class OfReferenceValue extends ForSetter<FieldDescription.InDefinedShape> {
            protected static final String PREFIX = "fixedFieldValue";
            private final Object value;
            private final String name;

            @Override // net.bytebuddy.implementation.FieldAccessor.ForSetter, net.bytebuddy.implementation.FieldAccessor
            public boolean equals(Object obj) {
                if (super.equals(obj)) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.name.equals(((OfReferenceValue) obj).name) && this.value.equals(((OfReferenceValue) obj).value);
                }
                return false;
            }

            @Override // net.bytebuddy.implementation.FieldAccessor.ForSetter, net.bytebuddy.implementation.FieldAccessor
            public int hashCode() {
                return (((super.hashCode() * 31) + this.value.hashCode()) * 31) + this.name.hashCode();
            }

            protected OfReferenceValue(FieldLocation fieldLocation, Assigner assigner, Assigner.Typing typing, TerminationHandler terminationHandler, Object value, String name) {
                super(fieldLocation, assigner, typing, terminationHandler);
                this.value = value;
                this.name = name;
            }

            @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
            public InstrumentedType prepare(InstrumentedType instrumentedType) {
                return instrumentedType.withField(new FieldDescription.Token(this.name, 4105, TypeDescription.ForLoadedType.of(this.value.getClass()).asGenericType())).withInitializer(new LoadedTypeInitializer.ForStaticField(this.name, this.value));
            }

            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // net.bytebuddy.implementation.FieldAccessor.ForSetter
            public FieldDescription.InDefinedShape initialize(TypeDescription instrumentedType) {
                return (FieldDescription.InDefinedShape) instrumentedType.getDeclaredFields().filter(ElementMatchers.named(this.name)).getOnly();
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // net.bytebuddy.implementation.FieldAccessor.ForSetter
            public StackManipulation resolve(FieldDescription.InDefinedShape target, FieldDescription fieldDescription, TypeDescription instrumentedType, MethodDescription instrumentedMethod) {
                if (fieldDescription.isFinal() && instrumentedMethod.isMethod()) {
                    throw new IllegalArgumentException("Cannot set final field " + fieldDescription + " from " + instrumentedMethod);
                }
                return new StackManipulation.Compound(FieldAccess.forField(target).read(), this.assigner.assign(TypeDescription.ForLoadedType.of(this.value.getClass()).asGenericType(), fieldDescription.getType(), this.typing));
            }

            @Override // net.bytebuddy.implementation.Implementation.Composable
            public Implementation andThen(Implementation implementation) {
                return new Implementation.Compound(new OfReferenceValue(this.fieldLocation, this.assigner, this.typing, TerminationHandler.NON_OPERATIONAL, this.value, this.name), implementation);
            }

            @Override // net.bytebuddy.implementation.Implementation.Composable
            public Implementation.Composable andThen(Implementation.Composable implementation) {
                return new Implementation.Compound.Composable(new OfReferenceValue(this.fieldLocation, this.assigner, this.typing, TerminationHandler.NON_OPERATIONAL, this.value, this.name), implementation);
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/FieldAccessor$ForSetter$OfFieldValue.class */
        public static class OfFieldValue extends ForSetter<FieldLocation.Prepared> {
            private final FieldLocation target;

            @Override // net.bytebuddy.implementation.FieldAccessor.ForSetter, net.bytebuddy.implementation.FieldAccessor
            public boolean equals(Object obj) {
                if (super.equals(obj)) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.target.equals(((OfFieldValue) obj).target);
                }
                return false;
            }

            @Override // net.bytebuddy.implementation.FieldAccessor.ForSetter, net.bytebuddy.implementation.FieldAccessor
            public int hashCode() {
                return (super.hashCode() * 31) + this.target.hashCode();
            }

            protected OfFieldValue(FieldLocation fieldLocation, Assigner assigner, Assigner.Typing typing, TerminationHandler terminationHandler, FieldLocation target) {
                super(fieldLocation, assigner, typing, terminationHandler);
                this.target = target;
            }

            @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
            public InstrumentedType prepare(InstrumentedType instrumentedType) {
                return instrumentedType;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // net.bytebuddy.implementation.FieldAccessor.ForSetter
            public FieldLocation.Prepared initialize(TypeDescription instrumentedType) {
                return this.target.prepare(instrumentedType);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // net.bytebuddy.implementation.FieldAccessor.ForSetter
            public StackManipulation resolve(FieldLocation.Prepared target, FieldDescription fieldDescription, TypeDescription instrumentedType, MethodDescription instrumentedMethod) {
                FieldDescription resolved = target.resolve(instrumentedMethod);
                if (!resolved.isStatic() && instrumentedMethod.isStatic()) {
                    throw new IllegalStateException("Cannot set instance field " + fieldDescription + " from " + instrumentedMethod);
                }
                StackManipulation[] stackManipulationArr = new StackManipulation[3];
                stackManipulationArr[0] = resolved.isStatic() ? StackManipulation.Trivial.INSTANCE : MethodVariableAccess.loadThis();
                stackManipulationArr[1] = FieldAccess.forField(resolved).read();
                stackManipulationArr[2] = this.assigner.assign(resolved.getType(), fieldDescription.getType(), this.typing);
                return new StackManipulation.Compound(stackManipulationArr);
            }

            @Override // net.bytebuddy.implementation.Implementation.Composable
            public Implementation andThen(Implementation implementation) {
                return new Implementation.Compound(new OfFieldValue(this.fieldLocation, this.assigner, this.typing, TerminationHandler.NON_OPERATIONAL, this.target), implementation);
            }

            @Override // net.bytebuddy.implementation.Implementation.Composable
            public Implementation.Composable andThen(Implementation.Composable implementation) {
                return new Implementation.Compound.Composable(new OfFieldValue(this.fieldLocation, this.assigner, this.typing, TerminationHandler.NON_OPERATIONAL, this.target), implementation);
            }
        }

        @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/FieldAccessor$ForSetter$Appender.class */
        protected class Appender implements ByteCodeAppender {
            private final TypeDescription instrumentedType;
            private final T initialized;
            private final FieldLocation.Prepared fieldLocation;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.instrumentedType.equals(((Appender) obj).instrumentedType) && this.initialized.equals(((Appender) obj).initialized) && this.fieldLocation.equals(((Appender) obj).fieldLocation) && ForSetter.this.equals(ForSetter.this);
            }

            public int hashCode() {
                return (((((((17 * 31) + this.instrumentedType.hashCode()) * 31) + this.initialized.hashCode()) * 31) + this.fieldLocation.hashCode()) * 31) + ForSetter.this.hashCode();
            }

            protected Appender(TypeDescription instrumentedType, T initialized, FieldLocation.Prepared fieldLocation) {
                this.instrumentedType = instrumentedType;
                this.initialized = initialized;
                this.fieldLocation = fieldLocation;
            }

            @Override // net.bytebuddy.implementation.bytecode.ByteCodeAppender
            public ByteCodeAppender.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext, MethodDescription instrumentedMethod) {
                FieldDescription fieldDescription = this.fieldLocation.resolve(instrumentedMethod);
                if (!fieldDescription.isStatic() && instrumentedMethod.isStatic()) {
                    throw new IllegalStateException("Cannot set instance field " + fieldDescription + " from " + instrumentedMethod);
                }
                if (fieldDescription.isFinal() && instrumentedMethod.isMethod()) {
                    throw new IllegalStateException("Cannot set final field " + fieldDescription + " from " + instrumentedMethod);
                }
                StackManipulation stackManipulation = ForSetter.this.resolve(this.initialized, fieldDescription, this.instrumentedType, instrumentedMethod);
                if (!stackManipulation.isValid()) {
                    throw new IllegalStateException("Set value cannot be assigned to " + fieldDescription);
                }
                StackManipulation[] stackManipulationArr = new StackManipulation[4];
                stackManipulationArr[0] = instrumentedMethod.isStatic() ? StackManipulation.Trivial.INSTANCE : MethodVariableAccess.loadThis();
                stackManipulationArr[1] = stackManipulation;
                stackManipulationArr[2] = FieldAccess.forField(fieldDescription).write();
                stackManipulationArr[3] = ForSetter.this.terminationHandler.resolve(instrumentedMethod);
                return new ByteCodeAppender.Size(new StackManipulation.Compound(stackManipulationArr).apply(methodVisitor, implementationContext).getMaximalSize(), instrumentedMethod.getStackSize());
            }
        }
    }
}
