package net.bytebuddy.implementation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.build.CachedReturnPlugin;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.annotation.AnnotationList;
import net.bytebuddy.description.annotation.AnnotationValue;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.ParameterDescription;
import net.bytebuddy.description.method.ParameterList;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.description.type.TypeList;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.scaffold.InstrumentedType;
import net.bytebuddy.dynamic.scaffold.MethodGraph;
import net.bytebuddy.dynamic.scaffold.TypeInitializer;
import net.bytebuddy.dynamic.scaffold.TypeWriter;
import net.bytebuddy.implementation.MethodAccessorFactory;
import net.bytebuddy.implementation.attribute.AnnotationValueFilter;
import net.bytebuddy.implementation.auxiliary.AuxiliaryType;
import net.bytebuddy.implementation.bytecode.ByteCodeAppender;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.implementation.bytecode.member.FieldAccess;
import net.bytebuddy.implementation.bytecode.member.MethodInvocation;
import net.bytebuddy.implementation.bytecode.member.MethodReturn;
import net.bytebuddy.implementation.bytecode.member.MethodVariableAccess;
import net.bytebuddy.jar.asm.ClassVisitor;
import net.bytebuddy.jar.asm.FieldVisitor;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.utility.CompoundList;
import net.bytebuddy.utility.RandomString;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/Implementation.class */
public interface Implementation extends InstrumentedType.Prepareable {

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/Implementation$Composable.class */
    public interface Composable extends Implementation {
        Implementation andThen(Implementation implementation);

        Composable andThen(Composable composable);
    }

    ByteCodeAppender appender(Target target);

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/Implementation$SpecialMethodInvocation.class */
    public interface SpecialMethodInvocation extends StackManipulation {
        MethodDescription getMethodDescription();

        TypeDescription getTypeDescription();

        SpecialMethodInvocation withCheckedCompatibilityTo(MethodDescription.TypeToken typeToken);

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/Implementation$SpecialMethodInvocation$Illegal.class */
        public enum Illegal implements SpecialMethodInvocation {
            INSTANCE;

            @Override // net.bytebuddy.implementation.bytecode.StackManipulation
            public boolean isValid() {
                return false;
            }

            @Override // net.bytebuddy.implementation.bytecode.StackManipulation
            public StackManipulation.Size apply(MethodVisitor methodVisitor, Context implementationContext) {
                throw new IllegalStateException("Cannot implement an undefined method");
            }

            @Override // net.bytebuddy.implementation.Implementation.SpecialMethodInvocation
            public MethodDescription getMethodDescription() {
                throw new IllegalStateException("An illegal special method invocation must not be applied");
            }

            @Override // net.bytebuddy.implementation.Implementation.SpecialMethodInvocation
            public TypeDescription getTypeDescription() {
                throw new IllegalStateException("An illegal special method invocation must not be applied");
            }

            @Override // net.bytebuddy.implementation.Implementation.SpecialMethodInvocation
            public SpecialMethodInvocation withCheckedCompatibilityTo(MethodDescription.TypeToken token) {
                return this;
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/Implementation$SpecialMethodInvocation$AbstractBase.class */
        public static abstract class AbstractBase implements SpecialMethodInvocation {
            private transient /* synthetic */ int hashCode_3qRxzpQM;

            @Override // net.bytebuddy.implementation.bytecode.StackManipulation
            public boolean isValid() {
                return true;
            }

            @CachedReturnPlugin.Enhance
            public int hashCode() {
                int hashCode = this.hashCode_3qRxzpQM != 0 ? 0 : (31 * getMethodDescription().asSignatureToken().hashCode()) + getTypeDescription().hashCode();
                if (hashCode == 0) {
                    hashCode = this.hashCode_3qRxzpQM;
                } else {
                    this.hashCode_3qRxzpQM = hashCode;
                }
                return hashCode;
            }

            public boolean equals(Object other) {
                if (this == other) {
                    return true;
                }
                if (!(other instanceof SpecialMethodInvocation)) {
                    return false;
                }
                SpecialMethodInvocation specialMethodInvocation = (SpecialMethodInvocation) other;
                return getMethodDescription().asSignatureToken().equals(specialMethodInvocation.getMethodDescription().asSignatureToken()) && getTypeDescription().equals(specialMethodInvocation.getTypeDescription());
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/Implementation$SpecialMethodInvocation$Simple.class */
        public static class Simple extends AbstractBase {
            private final MethodDescription methodDescription;
            private final TypeDescription typeDescription;
            private final StackManipulation stackManipulation;

            protected Simple(MethodDescription methodDescription, TypeDescription typeDescription, StackManipulation stackManipulation) {
                this.methodDescription = methodDescription;
                this.typeDescription = typeDescription;
                this.stackManipulation = stackManipulation;
            }

            public static SpecialMethodInvocation of(MethodDescription methodDescription, TypeDescription typeDescription) {
                StackManipulation stackManipulation = MethodInvocation.invoke(methodDescription).special(typeDescription);
                return stackManipulation.isValid() ? new Simple(methodDescription, typeDescription, stackManipulation) : Illegal.INSTANCE;
            }

            @Override // net.bytebuddy.implementation.Implementation.SpecialMethodInvocation
            public MethodDescription getMethodDescription() {
                return this.methodDescription;
            }

            @Override // net.bytebuddy.implementation.Implementation.SpecialMethodInvocation
            public TypeDescription getTypeDescription() {
                return this.typeDescription;
            }

            @Override // net.bytebuddy.implementation.bytecode.StackManipulation
            public StackManipulation.Size apply(MethodVisitor methodVisitor, Context implementationContext) {
                return this.stackManipulation.apply(methodVisitor, implementationContext);
            }

            @Override // net.bytebuddy.implementation.Implementation.SpecialMethodInvocation
            public SpecialMethodInvocation withCheckedCompatibilityTo(MethodDescription.TypeToken token) {
                if (this.methodDescription.asTypeToken().equals(token)) {
                    return this;
                }
                return Illegal.INSTANCE;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/Implementation$Target.class */
    public interface Target {

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/Implementation$Target$Factory.class */
        public interface Factory {
            Target make(TypeDescription typeDescription, MethodGraph.Linked linked, ClassFileVersion classFileVersion);
        }

        TypeDescription getInstrumentedType();

        TypeDefinition getOriginType();

        SpecialMethodInvocation invokeSuper(MethodDescription.SignatureToken signatureToken);

        SpecialMethodInvocation invokeDefault(MethodDescription.SignatureToken signatureToken);

        SpecialMethodInvocation invokeDefault(MethodDescription.SignatureToken signatureToken, TypeDescription typeDescription);

        SpecialMethodInvocation invokeDominant(MethodDescription.SignatureToken signatureToken);

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/Implementation$Target$AbstractBase.class */
        public static abstract class AbstractBase implements Target {
            protected final TypeDescription instrumentedType;
            protected final MethodGraph.Linked methodGraph;
            protected final DefaultMethodInvocation defaultMethodInvocation;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.defaultMethodInvocation.equals(((AbstractBase) obj).defaultMethodInvocation) && this.instrumentedType.equals(((AbstractBase) obj).instrumentedType) && this.methodGraph.equals(((AbstractBase) obj).methodGraph);
            }

            public int hashCode() {
                return (((((17 * 31) + this.instrumentedType.hashCode()) * 31) + this.methodGraph.hashCode()) * 31) + this.defaultMethodInvocation.hashCode();
            }

            /* JADX INFO: Access modifiers changed from: protected */
            public AbstractBase(TypeDescription instrumentedType, MethodGraph.Linked methodGraph, DefaultMethodInvocation defaultMethodInvocation) {
                this.instrumentedType = instrumentedType;
                this.methodGraph = methodGraph;
                this.defaultMethodInvocation = defaultMethodInvocation;
            }

            @Override // net.bytebuddy.implementation.Implementation.Target
            public TypeDescription getInstrumentedType() {
                return this.instrumentedType;
            }

            @Override // net.bytebuddy.implementation.Implementation.Target
            public SpecialMethodInvocation invokeDefault(MethodDescription.SignatureToken token) {
                SpecialMethodInvocation specialMethodInvocation = SpecialMethodInvocation.Illegal.INSTANCE;
                for (TypeDescription interfaceType : this.instrumentedType.getInterfaces().asErasures()) {
                    SpecialMethodInvocation invocation = invokeDefault(token, interfaceType).withCheckedCompatibilityTo(token.asTypeToken());
                    if (invocation.isValid()) {
                        if (specialMethodInvocation.isValid()) {
                            return SpecialMethodInvocation.Illegal.INSTANCE;
                        }
                        specialMethodInvocation = invocation;
                    }
                }
                return specialMethodInvocation;
            }

            @Override // net.bytebuddy.implementation.Implementation.Target
            public SpecialMethodInvocation invokeDefault(MethodDescription.SignatureToken token, TypeDescription targetType) {
                return this.defaultMethodInvocation.apply(this.methodGraph.getInterfaceGraph(targetType).locate(token), targetType);
            }

            @Override // net.bytebuddy.implementation.Implementation.Target
            public SpecialMethodInvocation invokeDominant(MethodDescription.SignatureToken token) {
                SpecialMethodInvocation specialMethodInvocation = invokeSuper(token);
                return specialMethodInvocation.isValid() ? specialMethodInvocation : invokeDefault(token);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/Implementation$Target$AbstractBase$DefaultMethodInvocation.class */
            public enum DefaultMethodInvocation {
                ENABLED { // from class: net.bytebuddy.implementation.Implementation.Target.AbstractBase.DefaultMethodInvocation.1
                    @Override // net.bytebuddy.implementation.Implementation.Target.AbstractBase.DefaultMethodInvocation
                    protected SpecialMethodInvocation apply(MethodGraph.Node node, TypeDescription targetType) {
                        return node.getSort().isUnique() ? SpecialMethodInvocation.Simple.of(node.getRepresentative(), targetType) : SpecialMethodInvocation.Illegal.INSTANCE;
                    }
                },
                DISABLED { // from class: net.bytebuddy.implementation.Implementation.Target.AbstractBase.DefaultMethodInvocation.2
                    @Override // net.bytebuddy.implementation.Implementation.Target.AbstractBase.DefaultMethodInvocation
                    protected SpecialMethodInvocation apply(MethodGraph.Node node, TypeDescription targetType) {
                        return SpecialMethodInvocation.Illegal.INSTANCE;
                    }
                };

                protected abstract SpecialMethodInvocation apply(MethodGraph.Node node, TypeDescription typeDescription);

                public static DefaultMethodInvocation of(ClassFileVersion classFileVersion) {
                    return classFileVersion.isAtLeast(ClassFileVersion.JAVA_V8) ? ENABLED : DISABLED;
                }
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/Implementation$Context.class */
    public interface Context extends MethodAccessorFactory {

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/Implementation$Context$Factory.class */
        public interface Factory {
            ExtractableView make(TypeDescription typeDescription, AuxiliaryType.NamingStrategy namingStrategy, TypeInitializer typeInitializer, ClassFileVersion classFileVersion, ClassFileVersion classFileVersion2);
        }

        TypeDescription register(AuxiliaryType auxiliaryType);

        FieldDescription.InDefinedShape cache(StackManipulation stackManipulation, TypeDescription typeDescription);

        TypeDescription getInstrumentedType();

        ClassFileVersion getClassFileVersion();

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/Implementation$Context$ExtractableView.class */
        public interface ExtractableView extends Context {
            boolean isEnabled();

            List<DynamicType> getAuxiliaryTypes();

            void drain(TypeInitializer.Drain drain, ClassVisitor classVisitor, AnnotationValueFilter.Factory factory);

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/Implementation$Context$ExtractableView$AbstractBase.class */
            public static abstract class AbstractBase implements ExtractableView {
                protected final TypeDescription instrumentedType;
                protected final ClassFileVersion classFileVersion;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.instrumentedType.equals(((AbstractBase) obj).instrumentedType) && this.classFileVersion.equals(((AbstractBase) obj).classFileVersion);
                }

                public int hashCode() {
                    return (((17 * 31) + this.instrumentedType.hashCode()) * 31) + this.classFileVersion.hashCode();
                }

                protected AbstractBase(TypeDescription instrumentedType, ClassFileVersion classFileVersion) {
                    this.instrumentedType = instrumentedType;
                    this.classFileVersion = classFileVersion;
                }

                @Override // net.bytebuddy.implementation.Implementation.Context
                public TypeDescription getInstrumentedType() {
                    return this.instrumentedType;
                }

                @Override // net.bytebuddy.implementation.Implementation.Context
                public ClassFileVersion getClassFileVersion() {
                    return this.classFileVersion;
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/Implementation$Context$Disabled.class */
        public static class Disabled extends ExtractableView.AbstractBase {
            protected Disabled(TypeDescription instrumentedType, ClassFileVersion classFileVersion) {
                super(instrumentedType, classFileVersion);
            }

            @Override // net.bytebuddy.implementation.Implementation.Context.ExtractableView
            public boolean isEnabled() {
                return false;
            }

            @Override // net.bytebuddy.implementation.Implementation.Context.ExtractableView
            public List<DynamicType> getAuxiliaryTypes() {
                return Collections.emptyList();
            }

            @Override // net.bytebuddy.implementation.Implementation.Context.ExtractableView
            public void drain(TypeInitializer.Drain drain, ClassVisitor classVisitor, AnnotationValueFilter.Factory annotationValueFilterFactory) {
                drain.apply(classVisitor, TypeInitializer.None.INSTANCE, this);
            }

            @Override // net.bytebuddy.implementation.Implementation.Context
            public TypeDescription register(AuxiliaryType auxiliaryType) {
                throw new IllegalStateException("Registration of auxiliary types was disabled: " + auxiliaryType);
            }

            @Override // net.bytebuddy.implementation.MethodAccessorFactory
            public MethodDescription.InDefinedShape registerAccessorFor(SpecialMethodInvocation specialMethodInvocation, MethodAccessorFactory.AccessType accessType) {
                throw new IllegalStateException("Registration of method accessors was disabled: " + specialMethodInvocation.getMethodDescription());
            }

            @Override // net.bytebuddy.implementation.MethodAccessorFactory
            public MethodDescription.InDefinedShape registerGetterFor(FieldDescription fieldDescription, MethodAccessorFactory.AccessType accessType) {
                throw new IllegalStateException("Registration of field accessor was disabled: " + fieldDescription);
            }

            @Override // net.bytebuddy.implementation.MethodAccessorFactory
            public MethodDescription.InDefinedShape registerSetterFor(FieldDescription fieldDescription, MethodAccessorFactory.AccessType accessType) {
                throw new IllegalStateException("Registration of field accessor was disabled: " + fieldDescription);
            }

            @Override // net.bytebuddy.implementation.Implementation.Context
            public FieldDescription.InDefinedShape cache(StackManipulation fieldValue, TypeDescription fieldType) {
                throw new IllegalStateException("Field values caching was disabled: " + fieldType);
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/Implementation$Context$Disabled$Factory.class */
            public enum Factory implements Factory {
                INSTANCE;

                @Override // net.bytebuddy.implementation.Implementation.Context.Factory
                public ExtractableView make(TypeDescription instrumentedType, AuxiliaryType.NamingStrategy auxiliaryTypeNamingStrategy, TypeInitializer typeInitializer, ClassFileVersion classFileVersion, ClassFileVersion auxiliaryClassFileVersion) {
                    if (typeInitializer.isDefined()) {
                        throw new IllegalStateException("Cannot define type initializer which was explicitly disabled: " + typeInitializer);
                    }
                    return new Disabled(instrumentedType, classFileVersion);
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/Implementation$Context$Default.class */
        public static class Default extends ExtractableView.AbstractBase {
            public static final String ACCESSOR_METHOD_SUFFIX = "accessor";
            public static final String FIELD_CACHE_PREFIX = "cachedValue";
            private final AuxiliaryType.NamingStrategy auxiliaryTypeNamingStrategy;
            private final TypeInitializer typeInitializer;
            private final ClassFileVersion auxiliaryClassFileVersion;
            private final Map<SpecialMethodInvocation, DelegationRecord> registeredAccessorMethods;
            private final Map<FieldDescription, DelegationRecord> registeredGetters;
            private final Map<FieldDescription, DelegationRecord> registeredSetters;
            private final Map<AuxiliaryType, DynamicType> auxiliaryTypes;
            private final Map<FieldCacheEntry, FieldDescription.InDefinedShape> registeredFieldCacheEntries;
            private final Set<FieldDescription.InDefinedShape> registeredFieldCacheFields;
            private final String suffix;
            private boolean fieldCacheCanAppendEntries;

            protected Default(TypeDescription instrumentedType, ClassFileVersion classFileVersion, AuxiliaryType.NamingStrategy auxiliaryTypeNamingStrategy, TypeInitializer typeInitializer, ClassFileVersion auxiliaryClassFileVersion) {
                super(instrumentedType, classFileVersion);
                this.auxiliaryTypeNamingStrategy = auxiliaryTypeNamingStrategy;
                this.typeInitializer = typeInitializer;
                this.auxiliaryClassFileVersion = auxiliaryClassFileVersion;
                this.registeredAccessorMethods = new HashMap();
                this.registeredGetters = new HashMap();
                this.registeredSetters = new HashMap();
                this.auxiliaryTypes = new HashMap();
                this.registeredFieldCacheEntries = new HashMap();
                this.registeredFieldCacheFields = new HashSet();
                this.suffix = RandomString.make();
                this.fieldCacheCanAppendEntries = true;
            }

            @Override // net.bytebuddy.implementation.Implementation.Context.ExtractableView
            public boolean isEnabled() {
                return true;
            }

            @Override // net.bytebuddy.implementation.MethodAccessorFactory
            public MethodDescription.InDefinedShape registerAccessorFor(SpecialMethodInvocation specialMethodInvocation, MethodAccessorFactory.AccessType accessType) {
                DelegationRecord record = this.registeredAccessorMethods.get(specialMethodInvocation);
                DelegationRecord record2 = record == null ? new AccessorMethodDelegation(this.instrumentedType, this.suffix, accessType, specialMethodInvocation) : record.with(accessType);
                this.registeredAccessorMethods.put(specialMethodInvocation, record2);
                return record2.getMethod();
            }

            @Override // net.bytebuddy.implementation.MethodAccessorFactory
            public MethodDescription.InDefinedShape registerGetterFor(FieldDescription fieldDescription, MethodAccessorFactory.AccessType accessType) {
                DelegationRecord record = this.registeredGetters.get(fieldDescription);
                DelegationRecord record2 = record == null ? new FieldGetterDelegation(this.instrumentedType, this.suffix, accessType, fieldDescription) : record.with(accessType);
                this.registeredGetters.put(fieldDescription, record2);
                return record2.getMethod();
            }

            @Override // net.bytebuddy.implementation.MethodAccessorFactory
            public MethodDescription.InDefinedShape registerSetterFor(FieldDescription fieldDescription, MethodAccessorFactory.AccessType accessType) {
                DelegationRecord record = this.registeredSetters.get(fieldDescription);
                DelegationRecord record2 = record == null ? new FieldSetterDelegation(this.instrumentedType, this.suffix, accessType, fieldDescription) : record.with(accessType);
                this.registeredSetters.put(fieldDescription, record2);
                return record2.getMethod();
            }

            @Override // net.bytebuddy.implementation.Implementation.Context
            public TypeDescription register(AuxiliaryType auxiliaryType) {
                DynamicType dynamicType = this.auxiliaryTypes.get(auxiliaryType);
                if (dynamicType == null) {
                    dynamicType = auxiliaryType.make(this.auxiliaryTypeNamingStrategy.name(this.instrumentedType), this.auxiliaryClassFileVersion, this);
                    this.auxiliaryTypes.put(auxiliaryType, dynamicType);
                }
                return dynamicType.getTypeDescription();
            }

            @Override // net.bytebuddy.implementation.Implementation.Context.ExtractableView
            public List<DynamicType> getAuxiliaryTypes() {
                return new ArrayList(this.auxiliaryTypes.values());
            }

            @Override // net.bytebuddy.implementation.Implementation.Context
            public FieldDescription.InDefinedShape cache(StackManipulation fieldValue, TypeDescription fieldType) {
                FieldDescription.InDefinedShape fieldCache;
                FieldCacheEntry fieldCacheEntry = new FieldCacheEntry(fieldValue, fieldType);
                FieldDescription.InDefinedShape fieldCache2 = this.registeredFieldCacheEntries.get(fieldCacheEntry);
                if (fieldCache2 != null) {
                    return fieldCache2;
                }
                if (!this.fieldCacheCanAppendEntries) {
                    throw new IllegalStateException("Cached values cannot be registered after defining the type initializer for " + this.instrumentedType);
                }
                int hashCode = fieldValue.hashCode();
                do {
                    int i = hashCode;
                    hashCode++;
                    fieldCache = new CacheValueField(this.instrumentedType, fieldType.asGenericType(), this.suffix, i);
                } while (!this.registeredFieldCacheFields.add(fieldCache));
                this.registeredFieldCacheEntries.put(fieldCacheEntry, fieldCache);
                return fieldCache;
            }

            @Override // net.bytebuddy.implementation.Implementation.Context.ExtractableView
            public void drain(TypeInitializer.Drain drain, ClassVisitor classVisitor, AnnotationValueFilter.Factory annotationValueFilterFactory) {
                this.fieldCacheCanAppendEntries = false;
                TypeInitializer typeInitializer = this.typeInitializer;
                for (Map.Entry<FieldCacheEntry, FieldDescription.InDefinedShape> entry : this.registeredFieldCacheEntries.entrySet()) {
                    FieldVisitor fieldVisitor = classVisitor.visitField(entry.getValue().getModifiers(), entry.getValue().getInternalName(), entry.getValue().getDescriptor(), entry.getValue().getGenericSignature(), FieldDescription.NO_DEFAULT_VALUE);
                    if (fieldVisitor != null) {
                        fieldVisitor.visitEnd();
                        typeInitializer = typeInitializer.expandWith(entry.getKey().storeIn(entry.getValue()));
                    }
                }
                drain.apply(classVisitor, typeInitializer, this);
                for (TypeWriter.MethodPool.Record record : this.registeredAccessorMethods.values()) {
                    record.apply(classVisitor, this, annotationValueFilterFactory);
                }
                for (TypeWriter.MethodPool.Record record2 : this.registeredGetters.values()) {
                    record2.apply(classVisitor, this, annotationValueFilterFactory);
                }
                for (TypeWriter.MethodPool.Record record3 : this.registeredSetters.values()) {
                    record3.apply(classVisitor, this, annotationValueFilterFactory);
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/Implementation$Context$Default$CacheValueField.class */
            protected static class CacheValueField extends FieldDescription.InDefinedShape.AbstractBase {
                private final TypeDescription instrumentedType;
                private final TypeDescription.Generic fieldType;
                private final String name;

                protected CacheValueField(TypeDescription instrumentedType, TypeDescription.Generic fieldType, String suffix, int hashCode) {
                    this.instrumentedType = instrumentedType;
                    this.fieldType = fieldType;
                    this.name = "cachedValue$" + suffix + "$" + RandomString.hashOf(hashCode);
                }

                @Override // net.bytebuddy.description.field.FieldDescription
                public TypeDescription.Generic getType() {
                    return this.fieldType;
                }

                @Override // net.bytebuddy.description.annotation.AnnotationSource
                public AnnotationList getDeclaredAnnotations() {
                    return new AnnotationList.Empty();
                }

                @Override // net.bytebuddy.description.DeclaredByType, net.bytebuddy.description.field.FieldDescription.InDefinedShape
                public TypeDescription getDeclaringType() {
                    return this.instrumentedType;
                }

                @Override // net.bytebuddy.description.ModifierReviewable
                public int getModifiers() {
                    return 4120 | (this.instrumentedType.isInterface() ? 1 : 2);
                }

                @Override // net.bytebuddy.description.NamedElement.WithRuntimeName
                public String getName() {
                    return this.name;
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/Implementation$Context$Default$FieldCacheEntry.class */
            protected static class FieldCacheEntry implements StackManipulation {
                private final StackManipulation fieldValue;
                private final TypeDescription fieldType;

                protected FieldCacheEntry(StackManipulation fieldValue, TypeDescription fieldType) {
                    this.fieldValue = fieldValue;
                    this.fieldType = fieldType;
                }

                protected ByteCodeAppender storeIn(FieldDescription fieldDescription) {
                    return new ByteCodeAppender.Simple(this, FieldAccess.forField(fieldDescription).write());
                }

                protected TypeDescription getFieldType() {
                    return this.fieldType;
                }

                @Override // net.bytebuddy.implementation.bytecode.StackManipulation
                public boolean isValid() {
                    return this.fieldValue.isValid();
                }

                @Override // net.bytebuddy.implementation.bytecode.StackManipulation
                public StackManipulation.Size apply(MethodVisitor methodVisitor, Context implementationContext) {
                    return this.fieldValue.apply(methodVisitor, implementationContext);
                }

                public int hashCode() {
                    int result = this.fieldValue.hashCode();
                    return (31 * result) + this.fieldType.hashCode();
                }

                public boolean equals(Object other) {
                    if (this == other) {
                        return true;
                    }
                    if (other == null || getClass() != other.getClass()) {
                        return false;
                    }
                    FieldCacheEntry fieldCacheEntry = (FieldCacheEntry) other;
                    return this.fieldValue.equals(fieldCacheEntry.fieldValue) && this.fieldType.equals(fieldCacheEntry.fieldType);
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/Implementation$Context$Default$AbstractPropertyAccessorMethod.class */
            protected static abstract class AbstractPropertyAccessorMethod extends MethodDescription.InDefinedShape.AbstractBase {
                protected abstract int getBaseModifiers();

                protected AbstractPropertyAccessorMethod() {
                }

                @Override // net.bytebuddy.description.ModifierReviewable
                public int getModifiers() {
                    return 4096 | getBaseModifiers() | (getDeclaringType().isInterface() ? 1 : 16);
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/Implementation$Context$Default$AccessorMethod.class */
            protected static class AccessorMethod extends AbstractPropertyAccessorMethod {
                private final TypeDescription instrumentedType;
                private final MethodDescription methodDescription;
                private final String name;

                protected AccessorMethod(TypeDescription instrumentedType, MethodDescription methodDescription, String suffix) {
                    this.instrumentedType = instrumentedType;
                    this.methodDescription = methodDescription;
                    this.name = methodDescription.getInternalName() + "$" + Default.ACCESSOR_METHOD_SUFFIX + "$" + suffix;
                }

                @Override // net.bytebuddy.description.method.MethodDescription
                public TypeDescription.Generic getReturnType() {
                    return this.methodDescription.getReturnType().asRawType();
                }

                @Override // net.bytebuddy.description.method.MethodDescription, net.bytebuddy.description.method.MethodDescription.InDefinedShape
                public ParameterList<ParameterDescription.InDefinedShape> getParameters() {
                    return new ParameterList.Explicit.ForTypes(this, this.methodDescription.getParameters().asTypeList().asRawTypes());
                }

                @Override // net.bytebuddy.description.method.MethodDescription
                public TypeList.Generic getExceptionTypes() {
                    return this.methodDescription.getExceptionTypes().asRawTypes();
                }

                @Override // net.bytebuddy.description.method.MethodDescription
                public AnnotationValue<?, ?> getDefaultValue() {
                    return AnnotationValue.UNDEFINED;
                }

                @Override // net.bytebuddy.description.TypeVariableSource
                public TypeList.Generic getTypeVariables() {
                    return new TypeList.Generic.Empty();
                }

                @Override // net.bytebuddy.description.annotation.AnnotationSource
                public AnnotationList getDeclaredAnnotations() {
                    return new AnnotationList.Empty();
                }

                @Override // net.bytebuddy.description.DeclaredByType, net.bytebuddy.description.field.FieldDescription.InDefinedShape
                public TypeDescription getDeclaringType() {
                    return this.instrumentedType;
                }

                @Override // net.bytebuddy.implementation.Implementation.Context.Default.AbstractPropertyAccessorMethod
                protected int getBaseModifiers() {
                    return this.methodDescription.isStatic() ? 8 : 0;
                }

                @Override // net.bytebuddy.description.NamedElement.WithRuntimeName
                public String getInternalName() {
                    return this.name;
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/Implementation$Context$Default$FieldGetter.class */
            protected static class FieldGetter extends AbstractPropertyAccessorMethod {
                private final TypeDescription instrumentedType;
                private final FieldDescription fieldDescription;
                private final String name;

                protected FieldGetter(TypeDescription instrumentedType, FieldDescription fieldDescription, String suffix) {
                    this.instrumentedType = instrumentedType;
                    this.fieldDescription = fieldDescription;
                    this.name = fieldDescription.getName() + "$" + Default.ACCESSOR_METHOD_SUFFIX + "$" + suffix;
                }

                @Override // net.bytebuddy.description.method.MethodDescription
                public TypeDescription.Generic getReturnType() {
                    return this.fieldDescription.getType().asRawType();
                }

                @Override // net.bytebuddy.description.method.MethodDescription, net.bytebuddy.description.method.MethodDescription.InDefinedShape
                public ParameterList<ParameterDescription.InDefinedShape> getParameters() {
                    return new ParameterList.Empty();
                }

                @Override // net.bytebuddy.description.method.MethodDescription
                public TypeList.Generic getExceptionTypes() {
                    return new TypeList.Generic.Empty();
                }

                @Override // net.bytebuddy.description.method.MethodDescription
                public AnnotationValue<?, ?> getDefaultValue() {
                    return AnnotationValue.UNDEFINED;
                }

                @Override // net.bytebuddy.description.TypeVariableSource
                public TypeList.Generic getTypeVariables() {
                    return new TypeList.Generic.Empty();
                }

                @Override // net.bytebuddy.description.annotation.AnnotationSource
                public AnnotationList getDeclaredAnnotations() {
                    return new AnnotationList.Empty();
                }

                @Override // net.bytebuddy.description.DeclaredByType, net.bytebuddy.description.field.FieldDescription.InDefinedShape
                public TypeDescription getDeclaringType() {
                    return this.instrumentedType;
                }

                @Override // net.bytebuddy.implementation.Implementation.Context.Default.AbstractPropertyAccessorMethod
                protected int getBaseModifiers() {
                    return this.fieldDescription.isStatic() ? 8 : 0;
                }

                @Override // net.bytebuddy.description.NamedElement.WithRuntimeName
                public String getInternalName() {
                    return this.name;
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/Implementation$Context$Default$FieldSetter.class */
            protected static class FieldSetter extends AbstractPropertyAccessorMethod {
                private final TypeDescription instrumentedType;
                private final FieldDescription fieldDescription;
                private final String name;

                protected FieldSetter(TypeDescription instrumentedType, FieldDescription fieldDescription, String suffix) {
                    this.instrumentedType = instrumentedType;
                    this.fieldDescription = fieldDescription;
                    this.name = fieldDescription.getName() + "$" + Default.ACCESSOR_METHOD_SUFFIX + "$" + suffix;
                }

                @Override // net.bytebuddy.description.method.MethodDescription
                public TypeDescription.Generic getReturnType() {
                    return TypeDescription.Generic.VOID;
                }

                @Override // net.bytebuddy.description.method.MethodDescription, net.bytebuddy.description.method.MethodDescription.InDefinedShape
                public ParameterList<ParameterDescription.InDefinedShape> getParameters() {
                    return new ParameterList.Explicit.ForTypes(this, Collections.singletonList(this.fieldDescription.getType().asRawType()));
                }

                @Override // net.bytebuddy.description.method.MethodDescription
                public TypeList.Generic getExceptionTypes() {
                    return new TypeList.Generic.Empty();
                }

                @Override // net.bytebuddy.description.method.MethodDescription
                public AnnotationValue<?, ?> getDefaultValue() {
                    return AnnotationValue.UNDEFINED;
                }

                @Override // net.bytebuddy.description.TypeVariableSource
                public TypeList.Generic getTypeVariables() {
                    return new TypeList.Generic.Empty();
                }

                @Override // net.bytebuddy.description.annotation.AnnotationSource
                public AnnotationList getDeclaredAnnotations() {
                    return new AnnotationList.Empty();
                }

                @Override // net.bytebuddy.description.DeclaredByType, net.bytebuddy.description.field.FieldDescription.InDefinedShape
                public TypeDescription getDeclaringType() {
                    return this.instrumentedType;
                }

                @Override // net.bytebuddy.implementation.Implementation.Context.Default.AbstractPropertyAccessorMethod
                protected int getBaseModifiers() {
                    return this.fieldDescription.isStatic() ? 8 : 0;
                }

                @Override // net.bytebuddy.description.NamedElement.WithRuntimeName
                public String getInternalName() {
                    return this.name;
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/Implementation$Context$Default$DelegationRecord.class */
            protected static abstract class DelegationRecord extends TypeWriter.MethodPool.Record.ForDefinedMethod implements ByteCodeAppender {
                protected final MethodDescription.InDefinedShape methodDescription;
                protected final Visibility visibility;

                protected abstract DelegationRecord with(MethodAccessorFactory.AccessType accessType);

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.visibility.equals(((DelegationRecord) obj).visibility) && this.methodDescription.equals(((DelegationRecord) obj).methodDescription);
                }

                public int hashCode() {
                    return (((17 * 31) + this.methodDescription.hashCode()) * 31) + this.visibility.hashCode();
                }

                protected DelegationRecord(MethodDescription.InDefinedShape methodDescription, Visibility visibility) {
                    this.methodDescription = methodDescription;
                    this.visibility = visibility;
                }

                @Override // net.bytebuddy.dynamic.scaffold.TypeWriter.MethodPool.Record
                public MethodDescription.InDefinedShape getMethod() {
                    return this.methodDescription;
                }

                @Override // net.bytebuddy.dynamic.scaffold.TypeWriter.MethodPool.Record
                public TypeWriter.MethodPool.Record.Sort getSort() {
                    return TypeWriter.MethodPool.Record.Sort.IMPLEMENTED;
                }

                @Override // net.bytebuddy.dynamic.scaffold.TypeWriter.MethodPool.Record
                public Visibility getVisibility() {
                    return this.visibility;
                }

                @Override // net.bytebuddy.dynamic.scaffold.TypeWriter.MethodPool.Record
                public void applyHead(MethodVisitor methodVisitor) {
                }

                @Override // net.bytebuddy.dynamic.scaffold.TypeWriter.MethodPool.Record
                public void applyBody(MethodVisitor methodVisitor, Context implementationContext, AnnotationValueFilter.Factory annotationValueFilterFactory) {
                    methodVisitor.visitCode();
                    ByteCodeAppender.Size size = applyCode(methodVisitor, implementationContext);
                    methodVisitor.visitMaxs(size.getOperandStackSize(), size.getLocalVariableSize());
                }

                @Override // net.bytebuddy.dynamic.scaffold.TypeWriter.MethodPool.Record
                public void applyAttributes(MethodVisitor methodVisitor, AnnotationValueFilter.Factory annotationValueFilterFactory) {
                }

                @Override // net.bytebuddy.dynamic.scaffold.TypeWriter.MethodPool.Record
                public ByteCodeAppender.Size applyCode(MethodVisitor methodVisitor, Context implementationContext) {
                    return apply(methodVisitor, implementationContext, getMethod());
                }

                @Override // net.bytebuddy.dynamic.scaffold.TypeWriter.MethodPool.Record
                public TypeWriter.MethodPool.Record prepend(ByteCodeAppender byteCodeAppender) {
                    throw new UnsupportedOperationException("Cannot prepend code to a delegation for " + this.methodDescription);
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/Implementation$Context$Default$AccessorMethodDelegation.class */
            protected static class AccessorMethodDelegation extends DelegationRecord {
                private final StackManipulation accessorMethodInvocation;

                @Override // net.bytebuddy.implementation.Implementation.Context.Default.DelegationRecord
                public boolean equals(Object obj) {
                    if (super.equals(obj)) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.accessorMethodInvocation.equals(((AccessorMethodDelegation) obj).accessorMethodInvocation);
                    }
                    return false;
                }

                @Override // net.bytebuddy.implementation.Implementation.Context.Default.DelegationRecord
                public int hashCode() {
                    return (super.hashCode() * 31) + this.accessorMethodInvocation.hashCode();
                }

                protected AccessorMethodDelegation(TypeDescription instrumentedType, String suffix, MethodAccessorFactory.AccessType accessType, SpecialMethodInvocation specialMethodInvocation) {
                    this(new AccessorMethod(instrumentedType, specialMethodInvocation.getMethodDescription(), suffix), accessType.getVisibility(), specialMethodInvocation);
                }

                private AccessorMethodDelegation(MethodDescription.InDefinedShape methodDescription, Visibility visibility, StackManipulation accessorMethodInvocation) {
                    super(methodDescription, visibility);
                    this.accessorMethodInvocation = accessorMethodInvocation;
                }

                @Override // net.bytebuddy.implementation.Implementation.Context.Default.DelegationRecord
                protected DelegationRecord with(MethodAccessorFactory.AccessType accessType) {
                    return new AccessorMethodDelegation(this.methodDescription, this.visibility.expandTo(accessType.getVisibility()), this.accessorMethodInvocation);
                }

                @Override // net.bytebuddy.implementation.bytecode.ByteCodeAppender
                public ByteCodeAppender.Size apply(MethodVisitor methodVisitor, Context implementationContext, MethodDescription instrumentedMethod) {
                    StackManipulation.Size stackSize = new StackManipulation.Compound(MethodVariableAccess.allArgumentsOf(instrumentedMethod).prependThisReference(), this.accessorMethodInvocation, MethodReturn.of(instrumentedMethod.getReturnType())).apply(methodVisitor, implementationContext);
                    return new ByteCodeAppender.Size(stackSize.getMaximalSize(), instrumentedMethod.getStackSize());
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/Implementation$Context$Default$FieldGetterDelegation.class */
            protected static class FieldGetterDelegation extends DelegationRecord {
                private final FieldDescription fieldDescription;

                @Override // net.bytebuddy.implementation.Implementation.Context.Default.DelegationRecord
                public boolean equals(Object obj) {
                    if (super.equals(obj)) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.fieldDescription.equals(((FieldGetterDelegation) obj).fieldDescription);
                    }
                    return false;
                }

                @Override // net.bytebuddy.implementation.Implementation.Context.Default.DelegationRecord
                public int hashCode() {
                    return (super.hashCode() * 31) + this.fieldDescription.hashCode();
                }

                protected FieldGetterDelegation(TypeDescription instrumentedType, String suffix, MethodAccessorFactory.AccessType accessType, FieldDescription fieldDescription) {
                    this(new FieldGetter(instrumentedType, fieldDescription, suffix), accessType.getVisibility(), fieldDescription);
                }

                private FieldGetterDelegation(MethodDescription.InDefinedShape methodDescription, Visibility visibility, FieldDescription fieldDescription) {
                    super(methodDescription, visibility);
                    this.fieldDescription = fieldDescription;
                }

                @Override // net.bytebuddy.implementation.Implementation.Context.Default.DelegationRecord
                protected DelegationRecord with(MethodAccessorFactory.AccessType accessType) {
                    return new FieldGetterDelegation(this.methodDescription, this.visibility.expandTo(accessType.getVisibility()), this.fieldDescription);
                }

                @Override // net.bytebuddy.implementation.bytecode.ByteCodeAppender
                public ByteCodeAppender.Size apply(MethodVisitor methodVisitor, Context implementationContext, MethodDescription instrumentedMethod) {
                    StackManipulation[] stackManipulationArr = new StackManipulation[3];
                    stackManipulationArr[0] = this.fieldDescription.isStatic() ? StackManipulation.Trivial.INSTANCE : MethodVariableAccess.loadThis();
                    stackManipulationArr[1] = FieldAccess.forField(this.fieldDescription).read();
                    stackManipulationArr[2] = MethodReturn.of(this.fieldDescription.getType());
                    StackManipulation.Size stackSize = new StackManipulation.Compound(stackManipulationArr).apply(methodVisitor, implementationContext);
                    return new ByteCodeAppender.Size(stackSize.getMaximalSize(), instrumentedMethod.getStackSize());
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/Implementation$Context$Default$FieldSetterDelegation.class */
            protected static class FieldSetterDelegation extends DelegationRecord {
                private final FieldDescription fieldDescription;

                @Override // net.bytebuddy.implementation.Implementation.Context.Default.DelegationRecord
                public boolean equals(Object obj) {
                    if (super.equals(obj)) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.fieldDescription.equals(((FieldSetterDelegation) obj).fieldDescription);
                    }
                    return false;
                }

                @Override // net.bytebuddy.implementation.Implementation.Context.Default.DelegationRecord
                public int hashCode() {
                    return (super.hashCode() * 31) + this.fieldDescription.hashCode();
                }

                protected FieldSetterDelegation(TypeDescription instrumentedType, String suffix, MethodAccessorFactory.AccessType accessType, FieldDescription fieldDescription) {
                    this(new FieldSetter(instrumentedType, fieldDescription, suffix), accessType.getVisibility(), fieldDescription);
                }

                private FieldSetterDelegation(MethodDescription.InDefinedShape methodDescription, Visibility visibility, FieldDescription fieldDescription) {
                    super(methodDescription, visibility);
                    this.fieldDescription = fieldDescription;
                }

                @Override // net.bytebuddy.implementation.Implementation.Context.Default.DelegationRecord
                protected DelegationRecord with(MethodAccessorFactory.AccessType accessType) {
                    return new FieldSetterDelegation(this.methodDescription, this.visibility.expandTo(accessType.getVisibility()), this.fieldDescription);
                }

                @Override // net.bytebuddy.implementation.bytecode.ByteCodeAppender
                public ByteCodeAppender.Size apply(MethodVisitor methodVisitor, Context implementationContext, MethodDescription instrumentedMethod) {
                    StackManipulation.Size stackSize = new StackManipulation.Compound(MethodVariableAccess.allArgumentsOf(instrumentedMethod).prependThisReference(), FieldAccess.forField(this.fieldDescription).write(), MethodReturn.VOID).apply(methodVisitor, implementationContext);
                    return new ByteCodeAppender.Size(stackSize.getMaximalSize(), instrumentedMethod.getStackSize());
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/Implementation$Context$Default$Factory.class */
            public enum Factory implements Factory {
                INSTANCE;

                @Override // net.bytebuddy.implementation.Implementation.Context.Factory
                public ExtractableView make(TypeDescription instrumentedType, AuxiliaryType.NamingStrategy auxiliaryTypeNamingStrategy, TypeInitializer typeInitializer, ClassFileVersion classFileVersion, ClassFileVersion auxiliaryClassFileVersion) {
                    return new Default(instrumentedType, classFileVersion, auxiliaryTypeNamingStrategy, typeInitializer, auxiliaryClassFileVersion);
                }
            }
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/Implementation$Compound.class */
    public static class Compound implements Implementation {
        private final List<Implementation> implementations;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.implementations.equals(((Compound) obj).implementations);
        }

        public int hashCode() {
            return (17 * 31) + this.implementations.hashCode();
        }

        public Compound(Implementation... implementation) {
            this(Arrays.asList(implementation));
        }

        public Compound(List<? extends Implementation> implementations) {
            this.implementations = new ArrayList();
            for (Implementation implementation : implementations) {
                if (!(implementation instanceof Composable)) {
                    if (implementation instanceof Compound) {
                        this.implementations.addAll(((Compound) implementation).implementations);
                    } else {
                        this.implementations.add(implementation);
                    }
                } else {
                    this.implementations.addAll(((Composable) implementation).implementations);
                    this.implementations.add(((Composable) implementation).composable);
                }
            }
        }

        @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
        public InstrumentedType prepare(InstrumentedType instrumentedType) {
            for (Implementation implementation : this.implementations) {
                instrumentedType = implementation.prepare(instrumentedType);
            }
            return instrumentedType;
        }

        @Override // net.bytebuddy.implementation.Implementation
        public ByteCodeAppender appender(Target implementationTarget) {
            ByteCodeAppender[] byteCodeAppender = new ByteCodeAppender[this.implementations.size()];
            int index = 0;
            for (Implementation implementation : this.implementations) {
                int i = index;
                index++;
                byteCodeAppender[i] = implementation.appender(implementationTarget);
            }
            return new ByteCodeAppender.Compound(byteCodeAppender);
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/Implementation$Compound$Composable.class */
        public static class Composable implements Composable {
            private final Composable composable;
            private final List<Implementation> implementations;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.composable.equals(((Composable) obj).composable) && this.implementations.equals(((Composable) obj).implementations);
            }

            public int hashCode() {
                return (((17 * 31) + this.composable.hashCode()) * 31) + this.implementations.hashCode();
            }

            public Composable(Implementation implementation, Composable composable) {
                this(Collections.singletonList(implementation), composable);
            }

            public Composable(List<? extends Implementation> implementations, Composable composable) {
                this.implementations = new ArrayList();
                for (Implementation implementation : implementations) {
                    if (implementation instanceof Composable) {
                        this.implementations.addAll(((Composable) implementation).implementations);
                        this.implementations.add(((Composable) implementation).composable);
                    } else if (implementation instanceof Compound) {
                        this.implementations.addAll(((Compound) implementation).implementations);
                    } else {
                        this.implementations.add(implementation);
                    }
                }
                if (composable instanceof Composable) {
                    this.implementations.addAll(((Composable) composable).implementations);
                    this.composable = ((Composable) composable).composable;
                    return;
                }
                this.composable = composable;
            }

            @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
            public InstrumentedType prepare(InstrumentedType instrumentedType) {
                for (Implementation implementation : this.implementations) {
                    instrumentedType = implementation.prepare(instrumentedType);
                }
                return this.composable.prepare(instrumentedType);
            }

            @Override // net.bytebuddy.implementation.Implementation
            public ByteCodeAppender appender(Target implementationTarget) {
                ByteCodeAppender[] byteCodeAppender = new ByteCodeAppender[this.implementations.size() + 1];
                int index = 0;
                for (Implementation implementation : this.implementations) {
                    int i = index;
                    index++;
                    byteCodeAppender[i] = implementation.appender(implementationTarget);
                }
                byteCodeAppender[index] = this.composable.appender(implementationTarget);
                return new ByteCodeAppender.Compound(byteCodeAppender);
            }

            @Override // net.bytebuddy.implementation.Implementation.Composable
            public Implementation andThen(Implementation implementation) {
                return new Compound(CompoundList.of(this.implementations, this.composable.andThen(implementation)));
            }

            @Override // net.bytebuddy.implementation.Implementation.Composable
            public Composable andThen(Composable implementation) {
                return new Composable(this.implementations, this.composable.andThen(implementation));
            }
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/Implementation$Simple.class */
    public static class Simple implements Implementation {
        private final ByteCodeAppender byteCodeAppender;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.byteCodeAppender.equals(((Simple) obj).byteCodeAppender);
        }

        public int hashCode() {
            return (17 * 31) + this.byteCodeAppender.hashCode();
        }

        public Simple(ByteCodeAppender... byteCodeAppender) {
            this.byteCodeAppender = new ByteCodeAppender.Compound(byteCodeAppender);
        }

        public Simple(StackManipulation... stackManipulation) {
            this.byteCodeAppender = new ByteCodeAppender.Simple(stackManipulation);
        }

        @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
        public InstrumentedType prepare(InstrumentedType instrumentedType) {
            return instrumentedType;
        }

        @Override // net.bytebuddy.implementation.Implementation
        public ByteCodeAppender appender(Target implementationTarget) {
            return this.byteCodeAppender;
        }
    }
}
