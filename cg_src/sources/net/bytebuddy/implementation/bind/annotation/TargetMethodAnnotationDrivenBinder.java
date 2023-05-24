package net.bytebuddy.implementation.bind.annotation;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.ParameterDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.scaffold.FieldLocator;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.bind.MethodDelegationBinder;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Argument;
import net.bytebuddy.implementation.bind.annotation.Default;
import net.bytebuddy.implementation.bind.annotation.DefaultCall;
import net.bytebuddy.implementation.bind.annotation.DefaultMethod;
import net.bytebuddy.implementation.bind.annotation.Empty;
import net.bytebuddy.implementation.bind.annotation.FieldValue;
import net.bytebuddy.implementation.bind.annotation.IgnoreForBinding;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.StubValue;
import net.bytebuddy.implementation.bind.annotation.Super;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.SuperMethod;
import net.bytebuddy.implementation.bind.annotation.This;
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
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaConstant;
import net.bytebuddy.utility.JavaType;
@HashCodeAndEqualsPlugin.Enhance
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/annotation/TargetMethodAnnotationDrivenBinder.class */
public class TargetMethodAnnotationDrivenBinder implements MethodDelegationBinder {
    private final DelegationProcessor delegationProcessor;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass() && this.delegationProcessor.equals(((TargetMethodAnnotationDrivenBinder) obj).delegationProcessor);
    }

    public int hashCode() {
        return (17 * 31) + this.delegationProcessor.hashCode();
    }

    protected TargetMethodAnnotationDrivenBinder(DelegationProcessor delegationProcessor) {
        this.delegationProcessor = delegationProcessor;
    }

    public static MethodDelegationBinder of(List<? extends ParameterBinder<?>> parameterBinders) {
        return new TargetMethodAnnotationDrivenBinder(DelegationProcessor.of(parameterBinders));
    }

    @Override // net.bytebuddy.implementation.bind.MethodDelegationBinder
    public MethodDelegationBinder.Record compile(MethodDescription candidate) {
        if (IgnoreForBinding.Verifier.check(candidate)) {
            return MethodDelegationBinder.Record.Illegal.INSTANCE;
        }
        List<DelegationProcessor.Handler> handlers = new ArrayList<>(candidate.getParameters().size());
        Iterator it = candidate.getParameters().iterator();
        while (it.hasNext()) {
            ParameterDescription parameterDescription = (ParameterDescription) it.next();
            handlers.add(this.delegationProcessor.prepare(parameterDescription));
        }
        return new Record(candidate, handlers, RuntimeType.Verifier.check(candidate));
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/annotation/TargetMethodAnnotationDrivenBinder$Record.class */
    protected static class Record implements MethodDelegationBinder.Record {
        private final MethodDescription candidate;
        private final List<DelegationProcessor.Handler> handlers;
        private final Assigner.Typing typing;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.typing.equals(((Record) obj).typing) && this.candidate.equals(((Record) obj).candidate) && this.handlers.equals(((Record) obj).handlers);
        }

        public int hashCode() {
            return (((((17 * 31) + this.candidate.hashCode()) * 31) + this.handlers.hashCode()) * 31) + this.typing.hashCode();
        }

        protected Record(MethodDescription candidate, List<DelegationProcessor.Handler> handlers, Assigner.Typing typing) {
            this.candidate = candidate;
            this.handlers = handlers;
            this.typing = typing;
        }

        /* JADX WARN: Removed duplicated region for block: B:13:0x005b  */
        @Override // net.bytebuddy.implementation.bind.MethodDelegationBinder.Record
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public net.bytebuddy.implementation.bind.MethodDelegationBinder.MethodBinding bind(net.bytebuddy.implementation.Implementation.Target r7, net.bytebuddy.description.method.MethodDescription r8, net.bytebuddy.implementation.bind.MethodDelegationBinder.TerminationHandler r9, net.bytebuddy.implementation.bind.MethodDelegationBinder.MethodInvoker r10, net.bytebuddy.implementation.bytecode.assign.Assigner r11) {
            /*
                r6 = this;
                r0 = r6
                net.bytebuddy.description.method.MethodDescription r0 = r0.candidate
                r1 = r7
                net.bytebuddy.description.type.TypeDescription r1 = r1.getInstrumentedType()
                boolean r0 = r0.isAccessibleTo(r1)
                if (r0 != 0) goto L16
                net.bytebuddy.implementation.bind.MethodDelegationBinder$MethodBinding$Illegal r0 = net.bytebuddy.implementation.bind.MethodDelegationBinder.MethodBinding.Illegal.INSTANCE
                return r0
            L16:
                r0 = r9
                r1 = r11
                r2 = r6
                net.bytebuddy.implementation.bytecode.assign.Assigner$Typing r2 = r2.typing
                r3 = r8
                r4 = r6
                net.bytebuddy.description.method.MethodDescription r4 = r4.candidate
                net.bytebuddy.implementation.bytecode.StackManipulation r0 = r0.resolve(r1, r2, r3, r4)
                r12 = r0
                r0 = r12
                boolean r0 = r0.isValid()
                if (r0 != 0) goto L37
                net.bytebuddy.implementation.bind.MethodDelegationBinder$MethodBinding$Illegal r0 = net.bytebuddy.implementation.bind.MethodDelegationBinder.MethodBinding.Illegal.INSTANCE
                return r0
            L37:
                net.bytebuddy.implementation.bind.MethodDelegationBinder$MethodBinding$Builder r0 = new net.bytebuddy.implementation.bind.MethodDelegationBinder$MethodBinding$Builder
                r1 = r0
                r2 = r10
                r3 = r6
                net.bytebuddy.description.method.MethodDescription r3 = r3.candidate
                r1.<init>(r2, r3)
                r13 = r0
                r0 = r6
                java.util.List<net.bytebuddy.implementation.bind.annotation.TargetMethodAnnotationDrivenBinder$DelegationProcessor$Handler> r0 = r0.handlers
                java.util.Iterator r0 = r0.iterator()
                r14 = r0
            L51:
                r0 = r14
                boolean r0 = r0.hasNext()
                if (r0 == 0) goto L8f
                r0 = r14
                java.lang.Object r0 = r0.next()
                net.bytebuddy.implementation.bind.annotation.TargetMethodAnnotationDrivenBinder$DelegationProcessor$Handler r0 = (net.bytebuddy.implementation.bind.annotation.TargetMethodAnnotationDrivenBinder.DelegationProcessor.Handler) r0
                r15 = r0
                r0 = r15
                r1 = r8
                r2 = r7
                r3 = r11
                net.bytebuddy.implementation.bind.MethodDelegationBinder$ParameterBinding r0 = r0.bind(r1, r2, r3)
                r16 = r0
                r0 = r16
                boolean r0 = r0.isValid()
                if (r0 == 0) goto L88
                r0 = r13
                r1 = r16
                boolean r0 = r0.append(r1)
                if (r0 != 0) goto L8c
            L88:
                net.bytebuddy.implementation.bind.MethodDelegationBinder$MethodBinding$Illegal r0 = net.bytebuddy.implementation.bind.MethodDelegationBinder.MethodBinding.Illegal.INSTANCE
                return r0
            L8c:
                goto L51
            L8f:
                r0 = r13
                r1 = r12
                net.bytebuddy.implementation.bind.MethodDelegationBinder$MethodBinding r0 = r0.build(r1)
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: net.bytebuddy.implementation.bind.annotation.TargetMethodAnnotationDrivenBinder.Record.bind(net.bytebuddy.implementation.Implementation$Target, net.bytebuddy.description.method.MethodDescription, net.bytebuddy.implementation.bind.MethodDelegationBinder$TerminationHandler, net.bytebuddy.implementation.bind.MethodDelegationBinder$MethodInvoker, net.bytebuddy.implementation.bytecode.assign.Assigner):net.bytebuddy.implementation.bind.MethodDelegationBinder$MethodBinding");
        }

        public String toString() {
            return this.candidate.toString();
        }
    }

    @SuppressFBWarnings(value = {"IC_SUPERCLASS_USES_SUBCLASS_DURING_INITIALIZATION"}, justification = "Safe initialization is implied")
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/annotation/TargetMethodAnnotationDrivenBinder$ParameterBinder.class */
    public interface ParameterBinder<T extends Annotation> {
        public static final List<ParameterBinder<?>> DEFAULTS = Collections.unmodifiableList(Arrays.asList(Argument.Binder.INSTANCE, AllArguments.Binder.INSTANCE, Origin.Binder.INSTANCE, This.Binder.INSTANCE, Super.Binder.INSTANCE, Default.Binder.INSTANCE, SuperCall.Binder.INSTANCE, DefaultCall.Binder.INSTANCE, SuperMethod.Binder.INSTANCE, DefaultMethod.Binder.INSTANCE, FieldValue.Binder.INSTANCE, StubValue.Binder.INSTANCE, Empty.Binder.INSTANCE));

        Class<T> getHandledType();

        MethodDelegationBinder.ParameterBinding<?> bind(AnnotationDescription.Loadable<T> loadable, MethodDescription methodDescription, ParameterDescription parameterDescription, Implementation.Target target, Assigner assigner, Assigner.Typing typing);

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/annotation/TargetMethodAnnotationDrivenBinder$ParameterBinder$ForFixedValue.class */
        public static abstract class ForFixedValue<S extends Annotation> implements ParameterBinder<S> {
            protected abstract Object bind(AnnotationDescription.Loadable<S> loadable, MethodDescription methodDescription, ParameterDescription parameterDescription);

            @Override // net.bytebuddy.implementation.bind.annotation.TargetMethodAnnotationDrivenBinder.ParameterBinder
            public MethodDelegationBinder.ParameterBinding<?> bind(AnnotationDescription.Loadable<S> annotation, MethodDescription source, ParameterDescription target, Implementation.Target implementationTarget, Assigner assigner, Assigner.Typing typing) {
                StackManipulation stackManipulation;
                TypeDescription suppliedType;
                Object value = bind(annotation, source, target);
                if (value == null) {
                    return new MethodDelegationBinder.ParameterBinding.Anonymous(DefaultValue.of(target.getType()));
                }
                if (value instanceof Boolean) {
                    stackManipulation = IntegerConstant.forValue(((Boolean) value).booleanValue());
                    suppliedType = TypeDescription.ForLoadedType.of(Boolean.TYPE);
                } else if (value instanceof Byte) {
                    stackManipulation = IntegerConstant.forValue(((Byte) value).byteValue());
                    suppliedType = TypeDescription.ForLoadedType.of(Byte.TYPE);
                } else if (value instanceof Short) {
                    stackManipulation = IntegerConstant.forValue(((Short) value).shortValue());
                    suppliedType = TypeDescription.ForLoadedType.of(Short.TYPE);
                } else if (value instanceof Character) {
                    stackManipulation = IntegerConstant.forValue(((Character) value).charValue());
                    suppliedType = TypeDescription.ForLoadedType.of(Character.TYPE);
                } else if (value instanceof Integer) {
                    stackManipulation = IntegerConstant.forValue(((Integer) value).intValue());
                    suppliedType = TypeDescription.ForLoadedType.of(Integer.TYPE);
                } else if (value instanceof Long) {
                    stackManipulation = LongConstant.forValue(((Long) value).longValue());
                    suppliedType = TypeDescription.ForLoadedType.of(Long.TYPE);
                } else if (value instanceof Float) {
                    stackManipulation = FloatConstant.forValue(((Float) value).floatValue());
                    suppliedType = TypeDescription.ForLoadedType.of(Float.TYPE);
                } else if (value instanceof Double) {
                    stackManipulation = DoubleConstant.forValue(((Double) value).doubleValue());
                    suppliedType = TypeDescription.ForLoadedType.of(Double.TYPE);
                } else if (value instanceof String) {
                    stackManipulation = new TextConstant((String) value);
                    suppliedType = TypeDescription.STRING;
                } else if (value instanceof Class) {
                    stackManipulation = ClassConstant.of(TypeDescription.ForLoadedType.of((Class) value));
                    suppliedType = TypeDescription.CLASS;
                } else if (value instanceof TypeDescription) {
                    stackManipulation = ClassConstant.of((TypeDescription) value);
                    suppliedType = TypeDescription.CLASS;
                } else if (JavaType.METHOD_HANDLE.isInstance(value)) {
                    stackManipulation = new JavaConstantValue(JavaConstant.MethodHandle.ofLoaded(value));
                    suppliedType = JavaType.METHOD_HANDLE.getTypeStub();
                } else if (value instanceof JavaConstant.MethodHandle) {
                    stackManipulation = new JavaConstantValue((JavaConstant.MethodHandle) value);
                    suppliedType = JavaType.METHOD_HANDLE.getTypeStub();
                } else if (JavaType.METHOD_TYPE.isInstance(value)) {
                    stackManipulation = new JavaConstantValue(JavaConstant.MethodType.ofLoaded(value));
                    suppliedType = JavaType.METHOD_HANDLE.getTypeStub();
                } else if (value instanceof JavaConstant.MethodType) {
                    stackManipulation = new JavaConstantValue((JavaConstant.MethodType) value);
                    suppliedType = JavaType.METHOD_HANDLE.getTypeStub();
                } else {
                    throw new IllegalStateException("Not able to save in class's constant pool: " + value);
                }
                return new MethodDelegationBinder.ParameterBinding.Anonymous(new StackManipulation.Compound(stackManipulation, assigner.assign(suppliedType.asGenericType(), target.getType(), typing)));
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/annotation/TargetMethodAnnotationDrivenBinder$ParameterBinder$ForFixedValue$OfConstant.class */
            public static class OfConstant<U extends Annotation> extends ForFixedValue<U> {
                private final Class<U> type;
                @HashCodeAndEqualsPlugin.ValueHandling(HashCodeAndEqualsPlugin.ValueHandling.Sort.REVERSE_NULLABILITY)
                private final Object value;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    if (obj != null && getClass() == obj.getClass() && this.type.equals(((OfConstant) obj).type)) {
                        Object obj2 = this.value;
                        Object obj3 = ((OfConstant) obj).value;
                        return obj3 != null ? obj2 != null && obj2.equals(obj3) : obj2 == null;
                    }
                    return false;
                }

                public int hashCode() {
                    int hashCode = ((17 * 31) + this.type.hashCode()) * 31;
                    Object obj = this.value;
                    return obj != null ? hashCode + obj.hashCode() : hashCode;
                }

                protected OfConstant(Class<U> type, Object value) {
                    this.type = type;
                    this.value = value;
                }

                public static <V extends Annotation> ParameterBinder<V> of(Class<V> type, Object value) {
                    return new OfConstant(type, value);
                }

                @Override // net.bytebuddy.implementation.bind.annotation.TargetMethodAnnotationDrivenBinder.ParameterBinder
                public Class<U> getHandledType() {
                    return this.type;
                }

                @Override // net.bytebuddy.implementation.bind.annotation.TargetMethodAnnotationDrivenBinder.ParameterBinder.ForFixedValue
                protected Object bind(AnnotationDescription.Loadable<U> annotation, MethodDescription source, ParameterDescription target) {
                    return this.value;
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/annotation/TargetMethodAnnotationDrivenBinder$ParameterBinder$ForFieldBinding.class */
        public static abstract class ForFieldBinding<S extends Annotation> implements ParameterBinder<S> {
            protected static final String BEAN_PROPERTY = "";

            protected abstract String fieldName(AnnotationDescription.Loadable<S> loadable);

            protected abstract TypeDescription declaringType(AnnotationDescription.Loadable<S> loadable);

            protected abstract MethodDelegationBinder.ParameterBinding<?> bind(FieldDescription fieldDescription, AnnotationDescription.Loadable<S> loadable, MethodDescription methodDescription, ParameterDescription parameterDescription, Implementation.Target target, Assigner assigner);

            private static FieldLocator.Resolution resolveAccessor(FieldLocator fieldLocator, MethodDescription methodDescription) {
                String fieldName;
                if (ElementMatchers.isSetter().matches(methodDescription)) {
                    fieldName = methodDescription.getInternalName().substring(3);
                } else if (ElementMatchers.isGetter().matches(methodDescription)) {
                    fieldName = methodDescription.getInternalName().substring(methodDescription.getInternalName().startsWith("is") ? 2 : 3);
                } else {
                    return FieldLocator.Resolution.Illegal.INSTANCE;
                }
                return fieldLocator.locate(Character.toLowerCase(fieldName.charAt(0)) + fieldName.substring(1));
            }

            @Override // net.bytebuddy.implementation.bind.annotation.TargetMethodAnnotationDrivenBinder.ParameterBinder
            public MethodDelegationBinder.ParameterBinding<?> bind(AnnotationDescription.Loadable<S> annotation, MethodDescription source, ParameterDescription target, Implementation.Target implementationTarget, Assigner assigner, Assigner.Typing typing) {
                FieldLocator forExactType;
                FieldLocator.Resolution locate;
                if (!declaringType(annotation).represents(Void.TYPE)) {
                    if (declaringType(annotation).isPrimitive() || declaringType(annotation).isArray()) {
                        throw new IllegalStateException("A primitive type or array type cannot declare a field: " + source);
                    }
                    if (!implementationTarget.getInstrumentedType().isAssignableTo(declaringType(annotation))) {
                        return MethodDelegationBinder.ParameterBinding.Illegal.INSTANCE;
                    }
                }
                if (declaringType(annotation).represents(Void.TYPE)) {
                    forExactType = new FieldLocator.ForClassHierarchy(implementationTarget.getInstrumentedType());
                } else {
                    forExactType = new FieldLocator.ForExactType(declaringType(annotation), implementationTarget.getInstrumentedType());
                }
                FieldLocator fieldLocator = forExactType;
                if (fieldName(annotation).equals("")) {
                    locate = resolveAccessor(fieldLocator, source);
                } else {
                    locate = fieldLocator.locate(fieldName(annotation));
                }
                FieldLocator.Resolution resolution = locate;
                return (!resolution.isResolved() || (source.isStatic() && !resolution.getField().isStatic())) ? MethodDelegationBinder.ParameterBinding.Illegal.INSTANCE : bind(resolution.getField(), annotation, source, target, implementationTarget, assigner);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/annotation/TargetMethodAnnotationDrivenBinder$DelegationProcessor.class */
    public static class DelegationProcessor {
        private final Map<? extends TypeDescription, ? extends ParameterBinder<?>> parameterBinders;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.parameterBinders.equals(((DelegationProcessor) obj).parameterBinders);
        }

        public int hashCode() {
            return (17 * 31) + this.parameterBinders.hashCode();
        }

        protected DelegationProcessor(Map<? extends TypeDescription, ? extends ParameterBinder<?>> parameterBinders) {
            this.parameterBinders = parameterBinders;
        }

        protected static DelegationProcessor of(List<? extends ParameterBinder<?>> parameterBinders) {
            Map<TypeDescription, ParameterBinder<?>> parameterBinderMap = new HashMap<>();
            for (ParameterBinder<?> parameterBinder : parameterBinders) {
                if (parameterBinderMap.put(TypeDescription.ForLoadedType.of(parameterBinder.getHandledType()), parameterBinder) != null) {
                    throw new IllegalArgumentException("Attempt to bind two handlers to " + parameterBinder.getHandledType());
                }
            }
            return new DelegationProcessor(parameterBinderMap);
        }

        /* JADX WARN: Multi-variable type inference failed */
        protected Handler prepare(ParameterDescription target) {
            Assigner.Typing typing = RuntimeType.Verifier.check(target);
            Handler handler = new Handler.Unbound(target, typing);
            for (AnnotationDescription annotation : target.getDeclaredAnnotations()) {
                ParameterBinder<?> parameterBinder = this.parameterBinders.get(annotation.getAnnotationType());
                if (parameterBinder != null && handler.isBound()) {
                    throw new IllegalStateException("Ambiguous binding for parameter annotated with two handled annotation types");
                }
                if (parameterBinder != null) {
                    handler = Handler.Bound.of(target, parameterBinder, annotation, typing);
                }
            }
            return handler;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/annotation/TargetMethodAnnotationDrivenBinder$DelegationProcessor$Handler.class */
        public interface Handler {
            boolean isBound();

            MethodDelegationBinder.ParameterBinding<?> bind(MethodDescription methodDescription, Implementation.Target target, Assigner assigner);

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/annotation/TargetMethodAnnotationDrivenBinder$DelegationProcessor$Handler$Unbound.class */
            public static class Unbound implements Handler {
                private final ParameterDescription target;
                private final Assigner.Typing typing;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.typing.equals(((Unbound) obj).typing) && this.target.equals(((Unbound) obj).target);
                }

                public int hashCode() {
                    return (((17 * 31) + this.target.hashCode()) * 31) + this.typing.hashCode();
                }

                protected Unbound(ParameterDescription target, Assigner.Typing typing) {
                    this.target = target;
                    this.typing = typing;
                }

                @Override // net.bytebuddy.implementation.bind.annotation.TargetMethodAnnotationDrivenBinder.DelegationProcessor.Handler
                public boolean isBound() {
                    return false;
                }

                @Override // net.bytebuddy.implementation.bind.annotation.TargetMethodAnnotationDrivenBinder.DelegationProcessor.Handler
                public MethodDelegationBinder.ParameterBinding<?> bind(MethodDescription source, Implementation.Target implementationTarget, Assigner assigner) {
                    return Argument.Binder.INSTANCE.bind(AnnotationDescription.ForLoadedAnnotation.of(new DefaultArgument(this.target.getIndex())), source, this.target, implementationTarget, assigner, this.typing);
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/annotation/TargetMethodAnnotationDrivenBinder$DelegationProcessor$Handler$Unbound$DefaultArgument.class */
                protected static class DefaultArgument implements Argument {
                    private static final String VALUE = "value";
                    private static final String BINDING_MECHANIC = "bindingMechanic";
                    private final int parameterIndex;

                    protected DefaultArgument(int parameterIndex) {
                        this.parameterIndex = parameterIndex;
                    }

                    @Override // net.bytebuddy.implementation.bind.annotation.Argument
                    public int value() {
                        return this.parameterIndex;
                    }

                    @Override // net.bytebuddy.implementation.bind.annotation.Argument
                    public Argument.BindingMechanic bindingMechanic() {
                        return Argument.BindingMechanic.UNIQUE;
                    }

                    @Override // java.lang.annotation.Annotation
                    public Class<Argument> annotationType() {
                        return Argument.class;
                    }

                    @Override // java.lang.annotation.Annotation
                    public int hashCode() {
                        return ((127 * BINDING_MECHANIC.hashCode()) ^ Argument.BindingMechanic.UNIQUE.hashCode()) + ((127 * "value".hashCode()) ^ this.parameterIndex);
                    }

                    @Override // java.lang.annotation.Annotation
                    public boolean equals(Object other) {
                        return this == other || ((other instanceof Argument) && this.parameterIndex == ((Argument) other).value());
                    }

                    @Override // java.lang.annotation.Annotation
                    public String toString() {
                        return "@" + Argument.class.getName() + "(bindingMechanic=" + Argument.BindingMechanic.UNIQUE.toString() + ", value=" + this.parameterIndex + ")";
                    }
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/annotation/TargetMethodAnnotationDrivenBinder$DelegationProcessor$Handler$Bound.class */
            public static class Bound<T extends Annotation> implements Handler {
                private final ParameterDescription target;
                private final ParameterBinder<T> parameterBinder;
                private final AnnotationDescription.Loadable<T> annotation;
                private final Assigner.Typing typing;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.typing.equals(((Bound) obj).typing) && this.target.equals(((Bound) obj).target) && this.parameterBinder.equals(((Bound) obj).parameterBinder) && this.annotation.equals(((Bound) obj).annotation);
                }

                public int hashCode() {
                    return (((((((17 * 31) + this.target.hashCode()) * 31) + this.parameterBinder.hashCode()) * 31) + this.annotation.hashCode()) * 31) + this.typing.hashCode();
                }

                protected Bound(ParameterDescription target, ParameterBinder<T> parameterBinder, AnnotationDescription.Loadable<T> annotation, Assigner.Typing typing) {
                    this.target = target;
                    this.parameterBinder = parameterBinder;
                    this.annotation = annotation;
                    this.typing = typing;
                }

                protected static Handler of(ParameterDescription target, ParameterBinder<?> parameterBinder, AnnotationDescription annotation, Assigner.Typing typing) {
                    return new Bound(target, parameterBinder, annotation.prepare(parameterBinder.getHandledType()), typing);
                }

                @Override // net.bytebuddy.implementation.bind.annotation.TargetMethodAnnotationDrivenBinder.DelegationProcessor.Handler
                public boolean isBound() {
                    return true;
                }

                @Override // net.bytebuddy.implementation.bind.annotation.TargetMethodAnnotationDrivenBinder.DelegationProcessor.Handler
                public MethodDelegationBinder.ParameterBinding<?> bind(MethodDescription source, Implementation.Target implementationTarget, Assigner assigner) {
                    return this.parameterBinder.bind(this.annotation, source, this.target, implementationTarget, assigner, this.typing);
                }
            }
        }
    }
}
