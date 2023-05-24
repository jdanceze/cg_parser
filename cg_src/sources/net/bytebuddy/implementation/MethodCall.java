package net.bytebuddy.implementation;

import android.provider.ContactsContract;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.enumeration.EnumerationDescription;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.field.FieldList;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.ParameterDescription;
import net.bytebuddy.description.method.ParameterList;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.scaffold.FieldLocator;
import net.bytebuddy.dynamic.scaffold.InstrumentedType;
import net.bytebuddy.dynamic.scaffold.MethodGraph;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.LoadedTypeInitializer;
import net.bytebuddy.implementation.bytecode.ByteCodeAppender;
import net.bytebuddy.implementation.bytecode.Duplication;
import net.bytebuddy.implementation.bytecode.Removal;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.implementation.bytecode.TypeCreation;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
import net.bytebuddy.implementation.bytecode.collection.ArrayAccess;
import net.bytebuddy.implementation.bytecode.collection.ArrayFactory;
import net.bytebuddy.implementation.bytecode.constant.ClassConstant;
import net.bytebuddy.implementation.bytecode.constant.DoubleConstant;
import net.bytebuddy.implementation.bytecode.constant.FloatConstant;
import net.bytebuddy.implementation.bytecode.constant.IntegerConstant;
import net.bytebuddy.implementation.bytecode.constant.JavaConstantValue;
import net.bytebuddy.implementation.bytecode.constant.LongConstant;
import net.bytebuddy.implementation.bytecode.constant.NullConstant;
import net.bytebuddy.implementation.bytecode.constant.TextConstant;
import net.bytebuddy.implementation.bytecode.member.FieldAccess;
import net.bytebuddy.implementation.bytecode.member.MethodInvocation;
import net.bytebuddy.implementation.bytecode.member.MethodReturn;
import net.bytebuddy.implementation.bytecode.member.MethodVariableAccess;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.CompoundList;
import net.bytebuddy.utility.JavaConstant;
import net.bytebuddy.utility.JavaType;
import net.bytebuddy.utility.RandomString;
@HashCodeAndEqualsPlugin.Enhance
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall.class */
public class MethodCall implements Implementation.Composable {
    protected final MethodLocator.Factory methodLocator;
    protected final TargetHandler.Factory targetHandler;
    protected final List<ArgumentLoader.Factory> argumentLoaders;
    protected final MethodInvoker.Factory methodInvoker;
    protected final TerminationHandler.Factory terminationHandler;
    protected final Assigner assigner;
    protected final Assigner.Typing typing;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass() && this.typing.equals(((MethodCall) obj).typing) && this.methodLocator.equals(((MethodCall) obj).methodLocator) && this.targetHandler.equals(((MethodCall) obj).targetHandler) && this.argumentLoaders.equals(((MethodCall) obj).argumentLoaders) && this.methodInvoker.equals(((MethodCall) obj).methodInvoker) && this.terminationHandler.equals(((MethodCall) obj).terminationHandler) && this.assigner.equals(((MethodCall) obj).assigner);
    }

    public int hashCode() {
        return (((((((((((((17 * 31) + this.methodLocator.hashCode()) * 31) + this.targetHandler.hashCode()) * 31) + this.argumentLoaders.hashCode()) * 31) + this.methodInvoker.hashCode()) * 31) + this.terminationHandler.hashCode()) * 31) + this.assigner.hashCode()) * 31) + this.typing.hashCode();
    }

    protected MethodCall(MethodLocator.Factory methodLocator, TargetHandler.Factory targetHandler, List<ArgumentLoader.Factory> argumentLoaders, MethodInvoker.Factory methodInvoker, TerminationHandler.Factory terminationHandler, Assigner assigner, Assigner.Typing typing) {
        this.methodLocator = methodLocator;
        this.targetHandler = targetHandler;
        this.argumentLoaders = argumentLoaders;
        this.methodInvoker = methodInvoker;
        this.terminationHandler = terminationHandler;
        this.assigner = assigner;
        this.typing = typing;
    }

    public static WithoutSpecifiedTarget invoke(Method method) {
        return invoke(new MethodDescription.ForLoadedMethod(method));
    }

    public static WithoutSpecifiedTarget invoke(Constructor<?> constructor) {
        return invoke(new MethodDescription.ForLoadedConstructor(constructor));
    }

    public static WithoutSpecifiedTarget invoke(MethodDescription methodDescription) {
        return invoke(new MethodLocator.ForExplicitMethod(methodDescription));
    }

    public static WithoutSpecifiedTarget invoke(ElementMatcher<? super MethodDescription> matcher) {
        return invoke(matcher, MethodGraph.Compiler.DEFAULT);
    }

    public static WithoutSpecifiedTarget invoke(ElementMatcher<? super MethodDescription> matcher, MethodGraph.Compiler methodGraphCompiler) {
        return invoke(new MethodLocator.ForElementMatcher.Factory(matcher, methodGraphCompiler));
    }

    public static WithoutSpecifiedTarget invoke(MethodLocator.Factory methodLocator) {
        return new WithoutSpecifiedTarget(methodLocator);
    }

    public static WithoutSpecifiedTarget invokeSelf() {
        return new WithoutSpecifiedTarget(MethodLocator.ForInstrumentedMethod.INSTANCE);
    }

    public static MethodCall invokeSuper() {
        return invokeSelf().onSuper();
    }

    public static Implementation.Composable call(Callable<?> callable) {
        try {
            return invoke(Callable.class.getMethod(ContactsContract.DataUsageFeedback.USAGE_TYPE_CALL, new Class[0])).on((WithoutSpecifiedTarget) callable, (Class<? super WithoutSpecifiedTarget>) Callable.class).withAssigner(Assigner.DEFAULT, Assigner.Typing.DYNAMIC);
        } catch (NoSuchMethodException exception) {
            throw new IllegalStateException("Could not locate Callable::call method", exception);
        }
    }

    public static Implementation.Composable run(Runnable runnable) {
        try {
            return invoke(Runnable.class.getMethod("run", new Class[0])).on((WithoutSpecifiedTarget) runnable, (Class<? super WithoutSpecifiedTarget>) Runnable.class).withAssigner(Assigner.DEFAULT, Assigner.Typing.DYNAMIC);
        } catch (NoSuchMethodException exception) {
            throw new IllegalStateException("Could not locate Runnable::run method", exception);
        }
    }

    public static MethodCall construct(Constructor<?> constructor) {
        return construct(new MethodDescription.ForLoadedConstructor(constructor));
    }

    public static MethodCall construct(MethodDescription methodDescription) {
        if (!methodDescription.isConstructor()) {
            throw new IllegalArgumentException("Not a constructor: " + methodDescription);
        }
        return new MethodCall(new MethodLocator.ForExplicitMethod(methodDescription), TargetHandler.ForConstructingInvocation.Factory.INSTANCE, Collections.emptyList(), MethodInvoker.ForContextualInvocation.Factory.INSTANCE, TerminationHandler.Simple.RETURNING, Assigner.DEFAULT, Assigner.Typing.STATIC);
    }

    public MethodCall with(Object... argument) {
        List<ArgumentLoader.Factory> argumentLoaders = new ArrayList<>(argument.length);
        for (Object anArgument : argument) {
            argumentLoaders.add(ArgumentLoader.ForStackManipulation.of(anArgument));
        }
        return with(argumentLoaders);
    }

    public MethodCall with(TypeDescription... typeDescription) {
        List<ArgumentLoader.Factory> argumentLoaders = new ArrayList<>(typeDescription.length);
        for (TypeDescription aTypeDescription : typeDescription) {
            argumentLoaders.add(new ArgumentLoader.ForStackManipulation(ClassConstant.of(aTypeDescription), Class.class));
        }
        return with(argumentLoaders);
    }

    public MethodCall with(EnumerationDescription... enumerationDescription) {
        List<ArgumentLoader.Factory> argumentLoaders = new ArrayList<>(enumerationDescription.length);
        for (EnumerationDescription anEnumerationDescription : enumerationDescription) {
            argumentLoaders.add(new ArgumentLoader.ForStackManipulation(FieldAccess.forEnumeration(anEnumerationDescription), anEnumerationDescription.getEnumerationType()));
        }
        return with(argumentLoaders);
    }

    public MethodCall with(JavaConstant... javaConstant) {
        List<ArgumentLoader.Factory> argumentLoaders = new ArrayList<>(javaConstant.length);
        for (JavaConstant aJavaConstant : javaConstant) {
            argumentLoaders.add(new ArgumentLoader.ForStackManipulation(new JavaConstantValue(aJavaConstant), aJavaConstant.getType()));
        }
        return with(argumentLoaders);
    }

    public MethodCall withReference(Object... argument) {
        List<ArgumentLoader.Factory> argumentLoaders = new ArrayList<>(argument.length);
        int length = argument.length;
        for (int i = 0; i < length; i++) {
            Object anArgument = argument[i];
            argumentLoaders.add(anArgument == null ? ArgumentLoader.ForNullConstant.INSTANCE : new ArgumentLoader.ForInstance.Factory(anArgument));
        }
        return with(argumentLoaders);
    }

    public MethodCall withArgument(int... index) {
        List<ArgumentLoader.Factory> argumentLoaders = new ArrayList<>(index.length);
        for (int anIndex : index) {
            if (anIndex < 0) {
                throw new IllegalArgumentException("Negative index: " + anIndex);
            }
            argumentLoaders.add(new ArgumentLoader.ForMethodParameter.Factory(anIndex));
        }
        return with(argumentLoaders);
    }

    public MethodCall withAllArguments() {
        return with(ArgumentLoader.ForMethodParameter.OfInstrumentedMethod.INSTANCE);
    }

    public MethodCall withArgumentArray() {
        return with(ArgumentLoader.ForMethodParameterArray.ForInstrumentedMethod.INSTANCE);
    }

    public MethodCall withArgumentArrayElements(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("A parameter index cannot be negative: " + index);
        }
        return with(new ArgumentLoader.ForMethodParameterArrayElement.OfInvokedMethod(index));
    }

    public MethodCall withArgumentArrayElements(int index, int size) {
        return withArgumentArrayElements(index, 0, size);
    }

    public MethodCall withArgumentArrayElements(int index, int start, int size) {
        if (index < 0) {
            throw new IllegalArgumentException("A parameter index cannot be negative: " + index);
        }
        if (start < 0) {
            throw new IllegalArgumentException("An array index cannot be negative: " + start);
        }
        if (size == 0) {
            return this;
        }
        if (size < 0) {
            throw new IllegalArgumentException("Size cannot be negative: " + size);
        }
        List<ArgumentLoader.Factory> argumentLoaders = new ArrayList<>(size);
        for (int position = 0; position < size; position++) {
            argumentLoaders.add(new ArgumentLoader.ForMethodParameterArrayElement.OfParameter(index, start + position));
        }
        return with(argumentLoaders);
    }

    public MethodCall withThis() {
        return with(ArgumentLoader.ForThisReference.Factory.INSTANCE);
    }

    public MethodCall withOwnType() {
        return with(ArgumentLoader.ForInstrumentedType.Factory.INSTANCE);
    }

    public MethodCall withField(String... name) {
        return withField(FieldLocator.ForClassHierarchy.Factory.INSTANCE, name);
    }

    public MethodCall withField(FieldLocator.Factory fieldLocatorFactory, String... name) {
        List<ArgumentLoader.Factory> argumentLoaders = new ArrayList<>(name.length);
        for (String aName : name) {
            argumentLoaders.add(new ArgumentLoader.ForField.Factory(aName, fieldLocatorFactory));
        }
        return with(argumentLoaders);
    }

    public MethodCall withMethodCall(MethodCall methodCall) {
        return with(new ArgumentLoader.ForMethodCall.Factory(methodCall));
    }

    public MethodCall with(StackManipulation stackManipulation, Type type) {
        return with(stackManipulation, TypeDefinition.Sort.describe(type));
    }

    public MethodCall with(StackManipulation stackManipulation, TypeDefinition typeDefinition) {
        return with(new ArgumentLoader.ForStackManipulation(stackManipulation, typeDefinition));
    }

    public MethodCall with(ArgumentLoader.Factory... argumentLoader) {
        return with(Arrays.asList(argumentLoader));
    }

    public MethodCall with(List<? extends ArgumentLoader.Factory> argumentLoaders) {
        return new MethodCall(this.methodLocator, this.targetHandler, CompoundList.of((List) this.argumentLoaders, (List) argumentLoaders), this.methodInvoker, this.terminationHandler, this.assigner, this.typing);
    }

    public FieldSetting setsField(Field field) {
        return setsField(new FieldDescription.ForLoadedField(field));
    }

    public FieldSetting setsField(FieldDescription fieldDescription) {
        return new FieldSetting(new MethodCall(this.methodLocator, this.targetHandler, this.argumentLoaders, this.methodInvoker, new TerminationHandler.FieldSetting.Explicit(fieldDescription), this.assigner, this.typing));
    }

    public FieldSetting setsField(ElementMatcher<? super FieldDescription> matcher) {
        return new FieldSetting(new MethodCall(this.methodLocator, this.targetHandler, this.argumentLoaders, this.methodInvoker, new TerminationHandler.FieldSetting.Implicit(matcher), this.assigner, this.typing));
    }

    public Implementation.Composable withAssigner(Assigner assigner, Assigner.Typing typing) {
        return new MethodCall(this.methodLocator, this.targetHandler, this.argumentLoaders, this.methodInvoker, this.terminationHandler, assigner, typing);
    }

    @Override // net.bytebuddy.implementation.Implementation.Composable
    public Implementation andThen(Implementation implementation) {
        return new Implementation.Compound(new MethodCall(this.methodLocator, this.targetHandler, this.argumentLoaders, this.methodInvoker, TerminationHandler.Simple.DROPPING, this.assigner, this.typing), implementation);
    }

    @Override // net.bytebuddy.implementation.Implementation.Composable
    public Implementation.Composable andThen(Implementation.Composable implementation) {
        return new Implementation.Compound.Composable(new MethodCall(this.methodLocator, this.targetHandler, this.argumentLoaders, this.methodInvoker, TerminationHandler.Simple.DROPPING, this.assigner, this.typing), implementation);
    }

    @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
    public InstrumentedType prepare(InstrumentedType instrumentedType) {
        for (InstrumentedType.Prepareable prepareable : this.argumentLoaders) {
            instrumentedType = prepareable.prepare(instrumentedType);
        }
        return this.targetHandler.prepare(instrumentedType);
    }

    @Override // net.bytebuddy.implementation.Implementation
    public ByteCodeAppender appender(Implementation.Target implementationTarget) {
        return new Appender(implementationTarget, this.terminationHandler.make(implementationTarget.getInstrumentedType()));
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$MethodLocator.class */
    public interface MethodLocator {

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$MethodLocator$Factory.class */
        public interface Factory {
            MethodLocator make(TypeDescription typeDescription);
        }

        MethodDescription resolve(TypeDescription typeDescription, MethodDescription methodDescription);

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$MethodLocator$ForInstrumentedMethod.class */
        public enum ForInstrumentedMethod implements MethodLocator, Factory {
            INSTANCE;

            @Override // net.bytebuddy.implementation.MethodCall.MethodLocator.Factory
            public MethodLocator make(TypeDescription instrumentedType) {
                return this;
            }

            @Override // net.bytebuddy.implementation.MethodCall.MethodLocator
            public MethodDescription resolve(TypeDescription targetType, MethodDescription instrumentedMethod) {
                return instrumentedMethod;
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$MethodLocator$ForExplicitMethod.class */
        public static class ForExplicitMethod implements MethodLocator, Factory {
            private final MethodDescription methodDescription;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.methodDescription.equals(((ForExplicitMethod) obj).methodDescription);
            }

            public int hashCode() {
                return (17 * 31) + this.methodDescription.hashCode();
            }

            protected ForExplicitMethod(MethodDescription methodDescription) {
                this.methodDescription = methodDescription;
            }

            @Override // net.bytebuddy.implementation.MethodCall.MethodLocator.Factory
            public MethodLocator make(TypeDescription instrumentedType) {
                return this;
            }

            @Override // net.bytebuddy.implementation.MethodCall.MethodLocator
            public MethodDescription resolve(TypeDescription targetType, MethodDescription instrumentedMethod) {
                return this.methodDescription;
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$MethodLocator$ForElementMatcher.class */
        public static class ForElementMatcher implements MethodLocator {
            private final TypeDescription instrumentedType;
            private final ElementMatcher<? super MethodDescription> matcher;
            private final MethodGraph.Compiler methodGraphCompiler;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.instrumentedType.equals(((ForElementMatcher) obj).instrumentedType) && this.matcher.equals(((ForElementMatcher) obj).matcher) && this.methodGraphCompiler.equals(((ForElementMatcher) obj).methodGraphCompiler);
            }

            public int hashCode() {
                return (((((17 * 31) + this.instrumentedType.hashCode()) * 31) + this.matcher.hashCode()) * 31) + this.methodGraphCompiler.hashCode();
            }

            protected ForElementMatcher(TypeDescription instrumentedType, ElementMatcher<? super MethodDescription> matcher, MethodGraph.Compiler methodGraphCompiler) {
                this.instrumentedType = instrumentedType;
                this.matcher = matcher;
                this.methodGraphCompiler = methodGraphCompiler;
            }

            @Override // net.bytebuddy.implementation.MethodCall.MethodLocator
            public MethodDescription resolve(TypeDescription targetType, MethodDescription instrumentedMethod) {
                List<MethodDescription> candidates = CompoundList.of(this.instrumentedType.getSuperClass().getDeclaredMethods().filter(ElementMatchers.isConstructor().and(this.matcher)), this.methodGraphCompiler.compile(targetType, this.instrumentedType).listNodes().asMethodList().filter(this.matcher));
                if (candidates.size() == 1) {
                    return candidates.get(0);
                }
                throw new IllegalStateException(this.instrumentedType + " does not define exactly one virtual method or constructor for " + this.matcher);
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$MethodLocator$ForElementMatcher$Factory.class */
            public static class Factory implements Factory {
                private final ElementMatcher<? super MethodDescription> matcher;
                private final MethodGraph.Compiler methodGraphCompiler;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.matcher.equals(((Factory) obj).matcher) && this.methodGraphCompiler.equals(((Factory) obj).methodGraphCompiler);
                }

                public int hashCode() {
                    return (((17 * 31) + this.matcher.hashCode()) * 31) + this.methodGraphCompiler.hashCode();
                }

                public Factory(ElementMatcher<? super MethodDescription> matcher, MethodGraph.Compiler methodGraphCompiler) {
                    this.matcher = matcher;
                    this.methodGraphCompiler = methodGraphCompiler;
                }

                @Override // net.bytebuddy.implementation.MethodCall.MethodLocator.Factory
                public MethodLocator make(TypeDescription instrumentedType) {
                    return new ForElementMatcher(instrumentedType, this.matcher, this.methodGraphCompiler);
                }
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$ArgumentLoader.class */
    public interface ArgumentLoader {

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$ArgumentLoader$ArgumentProvider.class */
        public interface ArgumentProvider {
            List<ArgumentLoader> resolve(MethodDescription methodDescription, MethodDescription methodDescription2);
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$ArgumentLoader$Factory.class */
        public interface Factory extends InstrumentedType.Prepareable {
            ArgumentProvider make(Implementation.Target target);
        }

        StackManipulation toStackManipulation(ParameterDescription parameterDescription, Assigner assigner, Assigner.Typing typing);

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$ArgumentLoader$ForNullConstant.class */
        public enum ForNullConstant implements ArgumentLoader, ArgumentProvider, Factory {
            INSTANCE;

            @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
            public InstrumentedType prepare(InstrumentedType instrumentedType) {
                return instrumentedType;
            }

            @Override // net.bytebuddy.implementation.MethodCall.ArgumentLoader.Factory
            public ArgumentProvider make(Implementation.Target implementationTarget) {
                return this;
            }

            @Override // net.bytebuddy.implementation.MethodCall.ArgumentLoader.ArgumentProvider
            public List<ArgumentLoader> resolve(MethodDescription instrumentedMethod, MethodDescription invokedMethod) {
                return Collections.singletonList(this);
            }

            @Override // net.bytebuddy.implementation.MethodCall.ArgumentLoader
            public StackManipulation toStackManipulation(ParameterDescription target, Assigner assigner, Assigner.Typing typing) {
                if (target.getType().isPrimitive()) {
                    throw new IllegalStateException("Cannot assign null to " + target);
                }
                return NullConstant.INSTANCE;
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$ArgumentLoader$ForThisReference.class */
        public static class ForThisReference implements ArgumentLoader, ArgumentProvider {
            private final TypeDescription instrumentedType;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.instrumentedType.equals(((ForThisReference) obj).instrumentedType);
            }

            public int hashCode() {
                return (17 * 31) + this.instrumentedType.hashCode();
            }

            public ForThisReference(TypeDescription instrumentedType) {
                this.instrumentedType = instrumentedType;
            }

            @Override // net.bytebuddy.implementation.MethodCall.ArgumentLoader.ArgumentProvider
            public List<ArgumentLoader> resolve(MethodDescription instrumentedMethod, MethodDescription invokedMethod) {
                if (instrumentedMethod.isStatic()) {
                    throw new IllegalStateException(instrumentedMethod + " is static and cannot supply an invoker instance");
                }
                return Collections.singletonList(this);
            }

            @Override // net.bytebuddy.implementation.MethodCall.ArgumentLoader
            public StackManipulation toStackManipulation(ParameterDescription target, Assigner assigner, Assigner.Typing typing) {
                StackManipulation stackManipulation = new StackManipulation.Compound(MethodVariableAccess.loadThis(), assigner.assign(this.instrumentedType.asGenericType(), target.getType(), typing));
                if (!stackManipulation.isValid()) {
                    throw new IllegalStateException("Cannot assign " + this.instrumentedType + " to " + target);
                }
                return stackManipulation;
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$ArgumentLoader$ForThisReference$Factory.class */
            public enum Factory implements Factory {
                INSTANCE;

                @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
                public InstrumentedType prepare(InstrumentedType instrumentedType) {
                    return instrumentedType;
                }

                @Override // net.bytebuddy.implementation.MethodCall.ArgumentLoader.Factory
                public ArgumentProvider make(Implementation.Target implementationTarget) {
                    return new ForThisReference(implementationTarget.getInstrumentedType());
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$ArgumentLoader$ForInstrumentedType.class */
        public static class ForInstrumentedType implements ArgumentLoader, ArgumentProvider {
            private final TypeDescription instrumentedType;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.instrumentedType.equals(((ForInstrumentedType) obj).instrumentedType);
            }

            public int hashCode() {
                return (17 * 31) + this.instrumentedType.hashCode();
            }

            public ForInstrumentedType(TypeDescription instrumentedType) {
                this.instrumentedType = instrumentedType;
            }

            @Override // net.bytebuddy.implementation.MethodCall.ArgumentLoader.ArgumentProvider
            public List<ArgumentLoader> resolve(MethodDescription instrumentedMethod, MethodDescription invokedMethod) {
                return Collections.singletonList(this);
            }

            @Override // net.bytebuddy.implementation.MethodCall.ArgumentLoader
            public StackManipulation toStackManipulation(ParameterDescription target, Assigner assigner, Assigner.Typing typing) {
                StackManipulation stackManipulation = new StackManipulation.Compound(ClassConstant.of(this.instrumentedType), assigner.assign(TypeDescription.Generic.OfNonGenericType.ForLoadedType.CLASS, target.getType(), typing));
                if (!stackManipulation.isValid()) {
                    throw new IllegalStateException("Cannot assign Class value to " + target);
                }
                return stackManipulation;
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$ArgumentLoader$ForInstrumentedType$Factory.class */
            public enum Factory implements Factory {
                INSTANCE;

                @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
                public InstrumentedType prepare(InstrumentedType instrumentedType) {
                    return instrumentedType;
                }

                @Override // net.bytebuddy.implementation.MethodCall.ArgumentLoader.Factory
                public ArgumentProvider make(Implementation.Target implementationTarget) {
                    return new ForInstrumentedType(implementationTarget.getInstrumentedType());
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$ArgumentLoader$ForMethodParameter.class */
        public static class ForMethodParameter implements ArgumentLoader {
            private final int index;
            private final MethodDescription instrumentedMethod;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.index == ((ForMethodParameter) obj).index && this.instrumentedMethod.equals(((ForMethodParameter) obj).instrumentedMethod);
            }

            public int hashCode() {
                return (((17 * 31) + this.index) * 31) + this.instrumentedMethod.hashCode();
            }

            public ForMethodParameter(int index, MethodDescription instrumentedMethod) {
                this.index = index;
                this.instrumentedMethod = instrumentedMethod;
            }

            @Override // net.bytebuddy.implementation.MethodCall.ArgumentLoader
            public StackManipulation toStackManipulation(ParameterDescription target, Assigner assigner, Assigner.Typing typing) {
                ParameterDescription parameterDescription = (ParameterDescription) this.instrumentedMethod.getParameters().get(this.index);
                StackManipulation stackManipulation = new StackManipulation.Compound(MethodVariableAccess.load(parameterDescription), assigner.assign(parameterDescription.getType(), target.getType(), typing));
                if (!stackManipulation.isValid()) {
                    throw new IllegalStateException("Cannot assign " + parameterDescription + " to " + target + " for " + this.instrumentedMethod);
                }
                return stackManipulation;
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$ArgumentLoader$ForMethodParameter$OfInstrumentedMethod.class */
            protected enum OfInstrumentedMethod implements Factory, ArgumentProvider {
                INSTANCE;

                @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
                public InstrumentedType prepare(InstrumentedType instrumentedType) {
                    return instrumentedType;
                }

                @Override // net.bytebuddy.implementation.MethodCall.ArgumentLoader.Factory
                public ArgumentProvider make(Implementation.Target implementationTarget) {
                    return this;
                }

                @Override // net.bytebuddy.implementation.MethodCall.ArgumentLoader.ArgumentProvider
                public List<ArgumentLoader> resolve(MethodDescription instrumentedMethod, MethodDescription invokedMethod) {
                    List<ArgumentLoader> argumentLoaders = new ArrayList<>(instrumentedMethod.getParameters().size());
                    Iterator it = instrumentedMethod.getParameters().iterator();
                    while (it.hasNext()) {
                        ParameterDescription parameterDescription = (ParameterDescription) it.next();
                        argumentLoaders.add(new ForMethodParameter(parameterDescription.getIndex(), instrumentedMethod));
                    }
                    return argumentLoaders;
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$ArgumentLoader$ForMethodParameter$Factory.class */
            public static class Factory implements Factory, ArgumentProvider {
                private final int index;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.index == ((Factory) obj).index;
                }

                public int hashCode() {
                    return (17 * 31) + this.index;
                }

                public Factory(int index) {
                    this.index = index;
                }

                @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
                public InstrumentedType prepare(InstrumentedType instrumentedType) {
                    return instrumentedType;
                }

                @Override // net.bytebuddy.implementation.MethodCall.ArgumentLoader.Factory
                public ArgumentProvider make(Implementation.Target implementationTarget) {
                    return this;
                }

                @Override // net.bytebuddy.implementation.MethodCall.ArgumentLoader.ArgumentProvider
                public List<ArgumentLoader> resolve(MethodDescription instrumentedMethod, MethodDescription invokedMethod) {
                    if (this.index >= instrumentedMethod.getParameters().size()) {
                        throw new IllegalStateException(instrumentedMethod + " does not have a parameter with index " + this.index);
                    }
                    return Collections.singletonList(new ForMethodParameter(this.index, instrumentedMethod));
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$ArgumentLoader$ForMethodParameterArray.class */
        public static class ForMethodParameterArray implements ArgumentLoader {
            private final ParameterList<?> parameters;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.parameters.equals(((ForMethodParameterArray) obj).parameters);
            }

            public int hashCode() {
                return (17 * 31) + this.parameters.hashCode();
            }

            public ForMethodParameterArray(ParameterList<?> parameters) {
                this.parameters = parameters;
            }

            @Override // net.bytebuddy.implementation.MethodCall.ArgumentLoader
            public StackManipulation toStackManipulation(ParameterDescription target, Assigner assigner, Assigner.Typing typing) {
                TypeDescription.Generic componentType;
                if (target.getType().represents(Object.class)) {
                    componentType = TypeDescription.Generic.OBJECT;
                } else if (target.getType().isArray()) {
                    componentType = target.getType().getComponentType();
                } else {
                    throw new IllegalStateException("Cannot set method parameter array for non-array type: " + target);
                }
                List<StackManipulation> stackManipulations = new ArrayList<>(this.parameters.size());
                Iterator it = this.parameters.iterator();
                while (it.hasNext()) {
                    ParameterDescription parameter = (ParameterDescription) it.next();
                    StackManipulation stackManipulation = new StackManipulation.Compound(MethodVariableAccess.load(parameter), assigner.assign(parameter.getType(), componentType, typing));
                    if (stackManipulation.isValid()) {
                        stackManipulations.add(stackManipulation);
                    } else {
                        throw new IllegalStateException("Cannot assign " + parameter + " to " + componentType);
                    }
                }
                return new StackManipulation.Compound(ArrayFactory.forType(componentType).withValues(stackManipulations));
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$ArgumentLoader$ForMethodParameterArray$ForInstrumentedMethod.class */
            public enum ForInstrumentedMethod implements Factory, ArgumentProvider {
                INSTANCE;

                @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
                public InstrumentedType prepare(InstrumentedType instrumentedType) {
                    return instrumentedType;
                }

                @Override // net.bytebuddy.implementation.MethodCall.ArgumentLoader.Factory
                public ArgumentProvider make(Implementation.Target implementationTarget) {
                    return this;
                }

                @Override // net.bytebuddy.implementation.MethodCall.ArgumentLoader.ArgumentProvider
                public List<ArgumentLoader> resolve(MethodDescription instrumentedMethod, MethodDescription invokedMethod) {
                    return Collections.singletonList(new ForMethodParameterArray(instrumentedMethod.getParameters()));
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$ArgumentLoader$ForMethodParameterArrayElement.class */
        public static class ForMethodParameterArrayElement implements ArgumentLoader {
            private final ParameterDescription parameterDescription;
            private final int index;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.index == ((ForMethodParameterArrayElement) obj).index && this.parameterDescription.equals(((ForMethodParameterArrayElement) obj).parameterDescription);
            }

            public int hashCode() {
                return (((17 * 31) + this.parameterDescription.hashCode()) * 31) + this.index;
            }

            public ForMethodParameterArrayElement(ParameterDescription parameterDescription, int index) {
                this.parameterDescription = parameterDescription;
                this.index = index;
            }

            @Override // net.bytebuddy.implementation.MethodCall.ArgumentLoader
            public StackManipulation toStackManipulation(ParameterDescription target, Assigner assigner, Assigner.Typing typing) {
                StackManipulation stackManipulation = new StackManipulation.Compound(MethodVariableAccess.load(this.parameterDescription), IntegerConstant.forValue(this.index), ArrayAccess.of(this.parameterDescription.getType().getComponentType()).load(), assigner.assign(this.parameterDescription.getType().getComponentType(), target.getType(), typing));
                if (!stackManipulation.isValid()) {
                    throw new IllegalStateException("Cannot assign " + this.parameterDescription.getType().getComponentType() + " to " + target);
                }
                return stackManipulation;
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$ArgumentLoader$ForMethodParameterArrayElement$OfParameter.class */
            public static class OfParameter implements Factory, ArgumentProvider {
                private final int index;
                private final int arrayIndex;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.index == ((OfParameter) obj).index && this.arrayIndex == ((OfParameter) obj).arrayIndex;
                }

                public int hashCode() {
                    return (((17 * 31) + this.index) * 31) + this.arrayIndex;
                }

                public OfParameter(int index, int arrayIndex) {
                    this.index = index;
                    this.arrayIndex = arrayIndex;
                }

                @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
                public InstrumentedType prepare(InstrumentedType instrumentedType) {
                    return instrumentedType;
                }

                @Override // net.bytebuddy.implementation.MethodCall.ArgumentLoader.Factory
                public ArgumentProvider make(Implementation.Target implementationTarget) {
                    return this;
                }

                @Override // net.bytebuddy.implementation.MethodCall.ArgumentLoader.ArgumentProvider
                public List<ArgumentLoader> resolve(MethodDescription instrumentedMethod, MethodDescription invokedMethod) {
                    if (instrumentedMethod.getParameters().size() <= this.index) {
                        throw new IllegalStateException(instrumentedMethod + " does not declare a parameter with index " + this.index);
                    }
                    if (!((ParameterDescription) instrumentedMethod.getParameters().get(this.index)).getType().isArray()) {
                        throw new IllegalStateException("Cannot access an item from non-array parameter " + instrumentedMethod.getParameters().get(this.index));
                    }
                    return Collections.singletonList(new ForMethodParameterArrayElement((ParameterDescription) instrumentedMethod.getParameters().get(this.index), this.arrayIndex));
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$ArgumentLoader$ForMethodParameterArrayElement$OfInvokedMethod.class */
            public static class OfInvokedMethod implements Factory, ArgumentProvider {
                private final int index;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.index == ((OfInvokedMethod) obj).index;
                }

                public int hashCode() {
                    return (17 * 31) + this.index;
                }

                public OfInvokedMethod(int index) {
                    this.index = index;
                }

                @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
                public InstrumentedType prepare(InstrumentedType instrumentedType) {
                    return instrumentedType;
                }

                @Override // net.bytebuddy.implementation.MethodCall.ArgumentLoader.Factory
                public ArgumentProvider make(Implementation.Target implementationTarget) {
                    return this;
                }

                @Override // net.bytebuddy.implementation.MethodCall.ArgumentLoader.ArgumentProvider
                public List<ArgumentLoader> resolve(MethodDescription instrumentedMethod, MethodDescription invokedMethod) {
                    if (instrumentedMethod.getParameters().size() <= this.index) {
                        throw new IllegalStateException(instrumentedMethod + " does not declare a parameter with index " + this.index);
                    }
                    if (!((ParameterDescription) instrumentedMethod.getParameters().get(this.index)).getType().isArray()) {
                        throw new IllegalStateException("Cannot access an item from non-array parameter " + instrumentedMethod.getParameters().get(this.index));
                    }
                    List<ArgumentLoader> argumentLoaders = new ArrayList<>(invokedMethod.getParameters().size());
                    for (int index = 0; index < invokedMethod.getParameters().size(); index++) {
                        argumentLoaders.add(new ForMethodParameterArrayElement((ParameterDescription) instrumentedMethod.getParameters().get(this.index), index));
                    }
                    return argumentLoaders;
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$ArgumentLoader$ForInstance.class */
        public static class ForInstance implements ArgumentLoader, ArgumentProvider {
            private final FieldDescription fieldDescription;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.fieldDescription.equals(((ForInstance) obj).fieldDescription);
            }

            public int hashCode() {
                return (17 * 31) + this.fieldDescription.hashCode();
            }

            public ForInstance(FieldDescription fieldDescription) {
                this.fieldDescription = fieldDescription;
            }

            @Override // net.bytebuddy.implementation.MethodCall.ArgumentLoader.ArgumentProvider
            public List<ArgumentLoader> resolve(MethodDescription instrumentedMethod, MethodDescription invokedMethod) {
                return Collections.singletonList(this);
            }

            @Override // net.bytebuddy.implementation.MethodCall.ArgumentLoader
            public StackManipulation toStackManipulation(ParameterDescription target, Assigner assigner, Assigner.Typing typing) {
                StackManipulation stackManipulation = new StackManipulation.Compound(FieldAccess.forField(this.fieldDescription).read(), assigner.assign(this.fieldDescription.getType(), target.getType(), typing));
                if (!stackManipulation.isValid()) {
                    throw new IllegalStateException("Cannot assign " + this.fieldDescription.getType() + " to " + target);
                }
                return stackManipulation;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$ArgumentLoader$ForInstance$Factory.class */
            public static class Factory implements Factory {
                private static final String FIELD_PREFIX = "methodCall";
                private final Object value;
                @HashCodeAndEqualsPlugin.ValueHandling(HashCodeAndEqualsPlugin.ValueHandling.Sort.IGNORE)
                private final String name = "methodCall$" + RandomString.make();

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.value.equals(((Factory) obj).value);
                }

                public int hashCode() {
                    return (17 * 31) + this.value.hashCode();
                }

                public Factory(Object value) {
                    this.value = value;
                }

                @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
                public InstrumentedType prepare(InstrumentedType instrumentedType) {
                    return instrumentedType.withField(new FieldDescription.Token(this.name, 4105, TypeDescription.Generic.OfNonGenericType.ForLoadedType.of(this.value.getClass()))).withInitializer(new LoadedTypeInitializer.ForStaticField(this.name, this.value));
                }

                @Override // net.bytebuddy.implementation.MethodCall.ArgumentLoader.Factory
                public ArgumentProvider make(Implementation.Target implementationTarget) {
                    return new ForInstance((FieldDescription) implementationTarget.getInstrumentedType().getDeclaredFields().filter(ElementMatchers.named(this.name)).getOnly());
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$ArgumentLoader$ForField.class */
        public static class ForField implements ArgumentLoader {
            private final FieldDescription fieldDescription;
            private final MethodDescription instrumentedMethod;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.fieldDescription.equals(((ForField) obj).fieldDescription) && this.instrumentedMethod.equals(((ForField) obj).instrumentedMethod);
            }

            public int hashCode() {
                return (((17 * 31) + this.fieldDescription.hashCode()) * 31) + this.instrumentedMethod.hashCode();
            }

            public ForField(FieldDescription fieldDescription, MethodDescription instrumentedMethod) {
                this.fieldDescription = fieldDescription;
                this.instrumentedMethod = instrumentedMethod;
            }

            @Override // net.bytebuddy.implementation.MethodCall.ArgumentLoader
            public StackManipulation toStackManipulation(ParameterDescription target, Assigner assigner, Assigner.Typing typing) {
                if (!this.fieldDescription.isStatic() && this.instrumentedMethod.isStatic()) {
                    throw new IllegalStateException("Cannot access non-static " + this.fieldDescription + " from " + this.instrumentedMethod);
                }
                StackManipulation[] stackManipulationArr = new StackManipulation[3];
                stackManipulationArr[0] = this.fieldDescription.isStatic() ? StackManipulation.Trivial.INSTANCE : MethodVariableAccess.loadThis();
                stackManipulationArr[1] = FieldAccess.forField(this.fieldDescription).read();
                stackManipulationArr[2] = assigner.assign(this.fieldDescription.getType(), target.getType(), typing);
                StackManipulation stackManipulation = new StackManipulation.Compound(stackManipulationArr);
                if (!stackManipulation.isValid()) {
                    throw new IllegalStateException("Cannot assign " + this.fieldDescription + " to " + target);
                }
                return stackManipulation;
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$ArgumentLoader$ForField$ArgumentProvider.class */
            protected static class ArgumentProvider implements ArgumentProvider {
                private final FieldDescription fieldDescription;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.fieldDescription.equals(((ArgumentProvider) obj).fieldDescription);
                }

                public int hashCode() {
                    return (17 * 31) + this.fieldDescription.hashCode();
                }

                protected ArgumentProvider(FieldDescription fieldDescription) {
                    this.fieldDescription = fieldDescription;
                }

                @Override // net.bytebuddy.implementation.MethodCall.ArgumentLoader.ArgumentProvider
                public List<ArgumentLoader> resolve(MethodDescription instrumentedMethod, MethodDescription invokedMethod) {
                    return Collections.singletonList(new ForField(this.fieldDescription, instrumentedMethod));
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$ArgumentLoader$ForField$Factory.class */
            public static class Factory implements Factory {
                private final String name;
                private final FieldLocator.Factory fieldLocatorFactory;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.name.equals(((Factory) obj).name) && this.fieldLocatorFactory.equals(((Factory) obj).fieldLocatorFactory);
                }

                public int hashCode() {
                    return (((17 * 31) + this.name.hashCode()) * 31) + this.fieldLocatorFactory.hashCode();
                }

                public Factory(String name, FieldLocator.Factory fieldLocatorFactory) {
                    this.name = name;
                    this.fieldLocatorFactory = fieldLocatorFactory;
                }

                @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
                public InstrumentedType prepare(InstrumentedType instrumentedType) {
                    return instrumentedType;
                }

                @Override // net.bytebuddy.implementation.MethodCall.ArgumentLoader.Factory
                public ArgumentProvider make(Implementation.Target implementationTarget) {
                    FieldLocator.Resolution resolution = this.fieldLocatorFactory.make(implementationTarget.getInstrumentedType()).locate(this.name);
                    if (!resolution.isResolved()) {
                        throw new IllegalStateException("Could not locate field '" + this.name + "' on " + implementationTarget.getInstrumentedType());
                    }
                    return new ArgumentProvider(resolution.getField());
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$ArgumentLoader$ForMethodCall.class */
        public static class ForMethodCall implements ArgumentLoader {
            private final Appender appender;
            private final MethodDescription methodDescription;
            private final MethodDescription instrumentedMethod;
            private final TargetHandler.Resolved targetHandler;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.appender.equals(((ForMethodCall) obj).appender) && this.methodDescription.equals(((ForMethodCall) obj).methodDescription) && this.instrumentedMethod.equals(((ForMethodCall) obj).instrumentedMethod) && this.targetHandler.equals(((ForMethodCall) obj).targetHandler);
            }

            public int hashCode() {
                return (((((((17 * 31) + this.appender.hashCode()) * 31) + this.methodDescription.hashCode()) * 31) + this.instrumentedMethod.hashCode()) * 31) + this.targetHandler.hashCode();
            }

            public ForMethodCall(Appender appender, MethodDescription methodDescription, MethodDescription instrumentedMethod, TargetHandler.Resolved targetHandler) {
                this.appender = appender;
                this.methodDescription = methodDescription;
                this.instrumentedMethod = instrumentedMethod;
                this.targetHandler = targetHandler;
            }

            @Override // net.bytebuddy.implementation.MethodCall.ArgumentLoader
            public StackManipulation toStackManipulation(ParameterDescription target, Assigner assigner, Assigner.Typing typing) {
                TypeDescription.Generic returnType;
                StackManipulation[] stackManipulationArr = new StackManipulation[2];
                stackManipulationArr[0] = this.appender.toStackManipulation(this.instrumentedMethod, this.methodDescription, this.targetHandler);
                if (this.methodDescription.isConstructor()) {
                    returnType = this.methodDescription.getDeclaringType().asGenericType();
                } else {
                    returnType = this.methodDescription.getReturnType();
                }
                stackManipulationArr[1] = assigner.assign(returnType, target.getType(), typing);
                StackManipulation stackManipulation = new StackManipulation.Compound(stackManipulationArr);
                if (!stackManipulation.isValid()) {
                    throw new IllegalStateException("Cannot assign return type of " + this.methodDescription + " to " + target);
                }
                return stackManipulation;
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$ArgumentLoader$ForMethodCall$ArgumentProvider.class */
            protected static class ArgumentProvider implements ArgumentProvider {
                private final Appender appender;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.appender.equals(((ArgumentProvider) obj).appender);
                }

                public int hashCode() {
                    return (17 * 31) + this.appender.hashCode();
                }

                protected ArgumentProvider(Appender appender) {
                    this.appender = appender;
                }

                @Override // net.bytebuddy.implementation.MethodCall.ArgumentLoader.ArgumentProvider
                public List<ArgumentLoader> resolve(MethodDescription instrumentedMethod, MethodDescription invokedMethod) {
                    TargetHandler.Resolved targetHandler = this.appender.targetHandler.resolve(instrumentedMethod);
                    return Collections.singletonList(new ForMethodCall(this.appender, this.appender.toInvokedMethod(instrumentedMethod, targetHandler), instrumentedMethod, targetHandler));
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$ArgumentLoader$ForMethodCall$Factory.class */
            protected static class Factory implements Factory {
                private final MethodCall methodCall;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.methodCall.equals(((Factory) obj).methodCall);
                }

                public int hashCode() {
                    return (17 * 31) + this.methodCall.hashCode();
                }

                public Factory(MethodCall methodCall) {
                    this.methodCall = methodCall;
                }

                @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
                public InstrumentedType prepare(InstrumentedType instrumentedType) {
                    return this.methodCall.prepare(instrumentedType);
                }

                @Override // net.bytebuddy.implementation.MethodCall.ArgumentLoader.Factory
                public ArgumentProvider make(Implementation.Target implementationTarget) {
                    MethodCall methodCall = this.methodCall;
                    methodCall.getClass();
                    return new ArgumentProvider(new Appender(implementationTarget, TerminationHandler.Simple.IGNORING));
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$ArgumentLoader$ForStackManipulation.class */
        public static class ForStackManipulation implements ArgumentLoader, ArgumentProvider, Factory {
            private final StackManipulation stackManipulation;
            private final TypeDefinition typeDefinition;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.stackManipulation.equals(((ForStackManipulation) obj).stackManipulation) && this.typeDefinition.equals(((ForStackManipulation) obj).typeDefinition);
            }

            public int hashCode() {
                return (((17 * 31) + this.stackManipulation.hashCode()) * 31) + this.typeDefinition.hashCode();
            }

            public ForStackManipulation(StackManipulation stackManipulation, Type type) {
                this(stackManipulation, TypeDefinition.Sort.describe(type));
            }

            public ForStackManipulation(StackManipulation stackManipulation, TypeDefinition typeDefinition) {
                this.stackManipulation = stackManipulation;
                this.typeDefinition = typeDefinition;
            }

            public static Factory of(Object value) {
                if (value == null) {
                    return ForNullConstant.INSTANCE;
                }
                if (value instanceof String) {
                    return new ForStackManipulation(new TextConstant((String) value), String.class);
                }
                if (value instanceof Boolean) {
                    return new ForStackManipulation(IntegerConstant.forValue(((Boolean) value).booleanValue()), Boolean.TYPE);
                }
                if (value instanceof Byte) {
                    return new ForStackManipulation(IntegerConstant.forValue(((Byte) value).byteValue()), Byte.TYPE);
                }
                if (value instanceof Short) {
                    return new ForStackManipulation(IntegerConstant.forValue(((Short) value).shortValue()), Short.TYPE);
                }
                if (value instanceof Character) {
                    return new ForStackManipulation(IntegerConstant.forValue(((Character) value).charValue()), Character.TYPE);
                }
                if (value instanceof Integer) {
                    return new ForStackManipulation(IntegerConstant.forValue(((Integer) value).intValue()), Integer.TYPE);
                }
                if (value instanceof Long) {
                    return new ForStackManipulation(LongConstant.forValue(((Long) value).longValue()), Long.TYPE);
                }
                if (value instanceof Float) {
                    return new ForStackManipulation(FloatConstant.forValue(((Float) value).floatValue()), Float.TYPE);
                }
                if (value instanceof Double) {
                    return new ForStackManipulation(DoubleConstant.forValue(((Double) value).doubleValue()), Double.TYPE);
                }
                if (value instanceof Class) {
                    return new ForStackManipulation(ClassConstant.of(TypeDescription.ForLoadedType.of((Class) value)), Class.class);
                }
                if (JavaType.METHOD_HANDLE.isInstance(value)) {
                    return new ForStackManipulation(new JavaConstantValue(JavaConstant.MethodHandle.ofLoaded(value)), JavaType.METHOD_HANDLE.getTypeStub());
                }
                if (JavaType.METHOD_TYPE.isInstance(value)) {
                    return new ForStackManipulation(new JavaConstantValue(JavaConstant.MethodType.ofLoaded(value)), JavaType.METHOD_TYPE.getTypeStub());
                }
                if (value instanceof Enum) {
                    EnumerationDescription enumerationDescription = new EnumerationDescription.ForLoadedEnumeration((Enum) value);
                    return new ForStackManipulation(FieldAccess.forEnumeration(enumerationDescription), enumerationDescription.getEnumerationType());
                }
                return new ForInstance.Factory(value);
            }

            @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
            public InstrumentedType prepare(InstrumentedType instrumentedType) {
                return instrumentedType;
            }

            @Override // net.bytebuddy.implementation.MethodCall.ArgumentLoader.Factory
            public ArgumentProvider make(Implementation.Target implementationTarget) {
                return this;
            }

            @Override // net.bytebuddy.implementation.MethodCall.ArgumentLoader.ArgumentProvider
            public List<ArgumentLoader> resolve(MethodDescription instrumentedMethod, MethodDescription invokedMethod) {
                return Collections.singletonList(this);
            }

            @Override // net.bytebuddy.implementation.MethodCall.ArgumentLoader
            public StackManipulation toStackManipulation(ParameterDescription target, Assigner assigner, Assigner.Typing typing) {
                StackManipulation assignment = assigner.assign(this.typeDefinition.asGenericType(), target.getType(), typing);
                if (!assignment.isValid()) {
                    throw new IllegalStateException("Cannot assign " + target + " to " + this.typeDefinition);
                }
                return new StackManipulation.Compound(this.stackManipulation, assignment);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$TargetHandler.class */
    public interface TargetHandler {

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$TargetHandler$Factory.class */
        public interface Factory extends InstrumentedType.Prepareable {
            TargetHandler make(Implementation.Target target);
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$TargetHandler$Resolved.class */
        public interface Resolved {
            TypeDescription getTypeDescription();

            StackManipulation toStackManipulation(MethodDescription methodDescription, Assigner assigner, Assigner.Typing typing);
        }

        Resolved resolve(MethodDescription methodDescription);

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$TargetHandler$Simple.class */
        public static class Simple implements TargetHandler, Factory, Resolved {
            private final TypeDescription typeDescription;
            private final StackManipulation stackManipulation;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.typeDescription.equals(((Simple) obj).typeDescription) && this.stackManipulation.equals(((Simple) obj).stackManipulation);
            }

            public int hashCode() {
                return (((17 * 31) + this.typeDescription.hashCode()) * 31) + this.stackManipulation.hashCode();
            }

            protected Simple(TypeDescription typeDescription, StackManipulation stackManipulation) {
                this.typeDescription = typeDescription;
                this.stackManipulation = stackManipulation;
            }

            @Override // net.bytebuddy.implementation.MethodCall.TargetHandler.Factory
            public TargetHandler make(Implementation.Target implementationTarget) {
                return this;
            }

            @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
            public InstrumentedType prepare(InstrumentedType instrumentedType) {
                return instrumentedType;
            }

            @Override // net.bytebuddy.implementation.MethodCall.TargetHandler
            public Resolved resolve(MethodDescription instrumentedMethod) {
                return this;
            }

            @Override // net.bytebuddy.implementation.MethodCall.TargetHandler.Resolved
            public TypeDescription getTypeDescription() {
                return this.typeDescription;
            }

            @Override // net.bytebuddy.implementation.MethodCall.TargetHandler.Resolved
            public StackManipulation toStackManipulation(MethodDescription invokedMethod, Assigner assigner, Assigner.Typing typing) {
                return this.stackManipulation;
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$TargetHandler$ForSelfOrStaticInvocation.class */
        public static class ForSelfOrStaticInvocation implements TargetHandler {
            private final TypeDescription instrumentedType;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.instrumentedType.equals(((ForSelfOrStaticInvocation) obj).instrumentedType);
            }

            public int hashCode() {
                return (17 * 31) + this.instrumentedType.hashCode();
            }

            protected ForSelfOrStaticInvocation(TypeDescription instrumentedType) {
                this.instrumentedType = instrumentedType;
            }

            @Override // net.bytebuddy.implementation.MethodCall.TargetHandler
            public Resolved resolve(MethodDescription instrumentedMethod) {
                return new Resolved(this.instrumentedType, instrumentedMethod);
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$TargetHandler$ForSelfOrStaticInvocation$Resolved.class */
            protected static class Resolved implements Resolved {
                private final TypeDescription instrumentedType;
                private final MethodDescription instrumentedMethod;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.instrumentedType.equals(((Resolved) obj).instrumentedType) && this.instrumentedMethod.equals(((Resolved) obj).instrumentedMethod);
                }

                public int hashCode() {
                    return (((17 * 31) + this.instrumentedType.hashCode()) * 31) + this.instrumentedMethod.hashCode();
                }

                protected Resolved(TypeDescription instrumentedType, MethodDescription instrumentedMethod) {
                    this.instrumentedType = instrumentedType;
                    this.instrumentedMethod = instrumentedMethod;
                }

                @Override // net.bytebuddy.implementation.MethodCall.TargetHandler.Resolved
                public TypeDescription getTypeDescription() {
                    return this.instrumentedType;
                }

                @Override // net.bytebuddy.implementation.MethodCall.TargetHandler.Resolved
                public StackManipulation toStackManipulation(MethodDescription invokedMethod, Assigner assigner, Assigner.Typing typing) {
                    if (this.instrumentedMethod.isStatic() && !invokedMethod.isStatic() && !invokedMethod.isConstructor()) {
                        throw new IllegalStateException("Cannot invoke " + invokedMethod + " from " + this.instrumentedMethod);
                    }
                    if (invokedMethod.isConstructor() && (!this.instrumentedMethod.isConstructor() || (!this.instrumentedType.equals(invokedMethod.getDeclaringType().asErasure()) && !this.instrumentedType.getSuperClass().asErasure().equals(invokedMethod.getDeclaringType().asErasure())))) {
                        throw new IllegalStateException("Cannot invoke " + invokedMethod + " from " + this.instrumentedMethod + " in " + this.instrumentedType);
                    }
                    StackManipulation[] stackManipulationArr = new StackManipulation[2];
                    stackManipulationArr[0] = invokedMethod.isStatic() ? StackManipulation.Trivial.INSTANCE : MethodVariableAccess.loadThis();
                    stackManipulationArr[1] = invokedMethod.isConstructor() ? Duplication.SINGLE : StackManipulation.Trivial.INSTANCE;
                    return new StackManipulation.Compound(stackManipulationArr);
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$TargetHandler$ForSelfOrStaticInvocation$Factory.class */
            public enum Factory implements Factory {
                INSTANCE;

                @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
                public InstrumentedType prepare(InstrumentedType instrumentedType) {
                    return instrumentedType;
                }

                @Override // net.bytebuddy.implementation.MethodCall.TargetHandler.Factory
                public TargetHandler make(Implementation.Target implementationTarget) {
                    return new ForSelfOrStaticInvocation(implementationTarget.getInstrumentedType());
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$TargetHandler$ForConstructingInvocation.class */
        public static class ForConstructingInvocation implements TargetHandler, Resolved {
            private final TypeDescription instrumentedType;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.instrumentedType.equals(((ForConstructingInvocation) obj).instrumentedType);
            }

            public int hashCode() {
                return (17 * 31) + this.instrumentedType.hashCode();
            }

            protected ForConstructingInvocation(TypeDescription instrumentedType) {
                this.instrumentedType = instrumentedType;
            }

            @Override // net.bytebuddy.implementation.MethodCall.TargetHandler
            public Resolved resolve(MethodDescription instrumentedMethod) {
                return this;
            }

            @Override // net.bytebuddy.implementation.MethodCall.TargetHandler.Resolved
            public TypeDescription getTypeDescription() {
                return this.instrumentedType;
            }

            @Override // net.bytebuddy.implementation.MethodCall.TargetHandler.Resolved
            public StackManipulation toStackManipulation(MethodDescription invokedMethod, Assigner assigner, Assigner.Typing typing) {
                return new StackManipulation.Compound(TypeCreation.of(invokedMethod.getDeclaringType().asErasure()), Duplication.SINGLE);
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$TargetHandler$ForConstructingInvocation$Factory.class */
            public enum Factory implements Factory {
                INSTANCE;

                @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
                public InstrumentedType prepare(InstrumentedType instrumentedType) {
                    return instrumentedType;
                }

                @Override // net.bytebuddy.implementation.MethodCall.TargetHandler.Factory
                public TargetHandler make(Implementation.Target implementationTarget) {
                    return new ForConstructingInvocation(implementationTarget.getInstrumentedType());
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$TargetHandler$ForValue.class */
        public static class ForValue implements TargetHandler, Resolved {
            private final FieldDescription.InDefinedShape fieldDescription;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.fieldDescription.equals(((ForValue) obj).fieldDescription);
            }

            public int hashCode() {
                return (17 * 31) + this.fieldDescription.hashCode();
            }

            protected ForValue(FieldDescription.InDefinedShape fieldDescription) {
                this.fieldDescription = fieldDescription;
            }

            @Override // net.bytebuddy.implementation.MethodCall.TargetHandler
            public Resolved resolve(MethodDescription instrumentedMethod) {
                return this;
            }

            @Override // net.bytebuddy.implementation.MethodCall.TargetHandler.Resolved
            public TypeDescription getTypeDescription() {
                return this.fieldDescription.getType().asErasure();
            }

            @Override // net.bytebuddy.implementation.MethodCall.TargetHandler.Resolved
            public StackManipulation toStackManipulation(MethodDescription invokedMethod, Assigner assigner, Assigner.Typing typing) {
                StackManipulation stackManipulation = assigner.assign(this.fieldDescription.getType(), invokedMethod.getDeclaringType().asGenericType(), typing);
                if (!stackManipulation.isValid()) {
                    throw new IllegalStateException("Cannot invoke " + invokedMethod + " on " + this.fieldDescription);
                }
                return new StackManipulation.Compound(FieldAccess.forField(this.fieldDescription).read(), stackManipulation);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$TargetHandler$ForValue$Factory.class */
            public static class Factory implements Factory {
                private static final String FIELD_PREFIX = "invocationTarget";
                private final Object target;
                private final TypeDescription.Generic fieldType;
                @HashCodeAndEqualsPlugin.ValueHandling(HashCodeAndEqualsPlugin.ValueHandling.Sort.IGNORE)
                private final String name = "invocationTarget$" + RandomString.make();

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.target.equals(((Factory) obj).target) && this.fieldType.equals(((Factory) obj).fieldType);
                }

                public int hashCode() {
                    return (((17 * 31) + this.target.hashCode()) * 31) + this.fieldType.hashCode();
                }

                protected Factory(Object target, TypeDescription.Generic fieldType) {
                    this.target = target;
                    this.fieldType = fieldType;
                }

                @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
                public InstrumentedType prepare(InstrumentedType instrumentedType) {
                    return instrumentedType.withField(new FieldDescription.Token(this.name, 4169, this.fieldType)).withInitializer(new LoadedTypeInitializer.ForStaticField(this.name, this.target));
                }

                @Override // net.bytebuddy.implementation.MethodCall.TargetHandler.Factory
                public TargetHandler make(Implementation.Target implementationTarget) {
                    return new ForValue((FieldDescription.InDefinedShape) implementationTarget.getInstrumentedType().getDeclaredFields().filter(ElementMatchers.named(this.name)).getOnly());
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$TargetHandler$ForField.class */
        public static class ForField implements TargetHandler, Resolved {
            private final FieldDescription fieldDescription;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.fieldDescription.equals(((ForField) obj).fieldDescription);
            }

            public int hashCode() {
                return (17 * 31) + this.fieldDescription.hashCode();
            }

            protected ForField(FieldDescription fieldDescription) {
                this.fieldDescription = fieldDescription;
            }

            @Override // net.bytebuddy.implementation.MethodCall.TargetHandler
            public Resolved resolve(MethodDescription instrumentedMethod) {
                return this;
            }

            @Override // net.bytebuddy.implementation.MethodCall.TargetHandler.Resolved
            public TypeDescription getTypeDescription() {
                return this.fieldDescription.getType().asErasure();
            }

            @Override // net.bytebuddy.implementation.MethodCall.TargetHandler.Resolved
            public StackManipulation toStackManipulation(MethodDescription invokedMethod, Assigner assigner, Assigner.Typing typing) {
                if (!invokedMethod.isMethod() || !invokedMethod.isVirtual() || !invokedMethod.isVisibleTo(this.fieldDescription.getType().asErasure())) {
                    throw new IllegalStateException("Cannot invoke " + invokedMethod + " on " + this.fieldDescription);
                }
                StackManipulation stackManipulation = assigner.assign(this.fieldDescription.getType(), invokedMethod.getDeclaringType().asGenericType(), typing);
                if (!stackManipulation.isValid()) {
                    throw new IllegalStateException("Cannot invoke " + invokedMethod + " on " + this.fieldDescription);
                }
                StackManipulation[] stackManipulationArr = new StackManipulation[3];
                stackManipulationArr[0] = (invokedMethod.isStatic() || this.fieldDescription.isStatic()) ? StackManipulation.Trivial.INSTANCE : MethodVariableAccess.loadThis();
                stackManipulationArr[1] = FieldAccess.forField(this.fieldDescription).read();
                stackManipulationArr[2] = stackManipulation;
                return new StackManipulation.Compound(stackManipulationArr);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$TargetHandler$ForField$Location.class */
            public interface Location {
                FieldDescription resolve(TypeDescription typeDescription);

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$TargetHandler$ForField$Location$ForImplicitField.class */
                public static class ForImplicitField implements Location {
                    private final String name;
                    private final FieldLocator.Factory fieldLocatorFactory;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.name.equals(((ForImplicitField) obj).name) && this.fieldLocatorFactory.equals(((ForImplicitField) obj).fieldLocatorFactory);
                    }

                    public int hashCode() {
                        return (((17 * 31) + this.name.hashCode()) * 31) + this.fieldLocatorFactory.hashCode();
                    }

                    protected ForImplicitField(String name, FieldLocator.Factory fieldLocatorFactory) {
                        this.name = name;
                        this.fieldLocatorFactory = fieldLocatorFactory;
                    }

                    @Override // net.bytebuddy.implementation.MethodCall.TargetHandler.ForField.Location
                    public FieldDescription resolve(TypeDescription instrumentedType) {
                        FieldLocator.Resolution resolution = this.fieldLocatorFactory.make(instrumentedType).locate(this.name);
                        if (!resolution.isResolved()) {
                            throw new IllegalStateException("Could not locate field name " + this.name + " on " + instrumentedType);
                        }
                        return resolution.getField();
                    }
                }

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$TargetHandler$ForField$Location$ForExplicitField.class */
                public static class ForExplicitField implements Location {
                    private final FieldDescription fieldDescription;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.fieldDescription.equals(((ForExplicitField) obj).fieldDescription);
                    }

                    public int hashCode() {
                        return (17 * 31) + this.fieldDescription.hashCode();
                    }

                    protected ForExplicitField(FieldDescription fieldDescription) {
                        this.fieldDescription = fieldDescription;
                    }

                    @Override // net.bytebuddy.implementation.MethodCall.TargetHandler.ForField.Location
                    public FieldDescription resolve(TypeDescription instrumentedType) {
                        return this.fieldDescription;
                    }
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$TargetHandler$ForField$Factory.class */
            public static class Factory implements Factory {
                private final Location location;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.location.equals(((Factory) obj).location);
                }

                public int hashCode() {
                    return (17 * 31) + this.location.hashCode();
                }

                protected Factory(Location location) {
                    this.location = location;
                }

                @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
                public InstrumentedType prepare(InstrumentedType instrumentedType) {
                    return instrumentedType;
                }

                @Override // net.bytebuddy.implementation.MethodCall.TargetHandler.Factory
                public TargetHandler make(Implementation.Target implementationTarget) {
                    FieldDescription fieldDescription = this.location.resolve(implementationTarget.getInstrumentedType());
                    if (!fieldDescription.isStatic() && !implementationTarget.getInstrumentedType().isAssignableTo(fieldDescription.getDeclaringType().asErasure())) {
                        throw new IllegalStateException("Cannot access " + fieldDescription + " from " + implementationTarget.getInstrumentedType());
                    }
                    return new ForField(fieldDescription);
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$TargetHandler$ForMethodParameter.class */
        public static class ForMethodParameter implements TargetHandler, Factory {
            private final int index;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.index == ((ForMethodParameter) obj).index;
            }

            public int hashCode() {
                return (17 * 31) + this.index;
            }

            protected ForMethodParameter(int index) {
                this.index = index;
            }

            @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
            public InstrumentedType prepare(InstrumentedType instrumentedType) {
                return instrumentedType;
            }

            @Override // net.bytebuddy.implementation.MethodCall.TargetHandler.Factory
            public TargetHandler make(Implementation.Target implementationTarget) {
                return this;
            }

            @Override // net.bytebuddy.implementation.MethodCall.TargetHandler
            public Resolved resolve(MethodDescription instrumentedMethod) {
                if (instrumentedMethod.getParameters().size() < this.index) {
                    throw new IllegalArgumentException(instrumentedMethod + " does not have a parameter with index " + this.index);
                }
                return new Resolved((ParameterDescription) instrumentedMethod.getParameters().get(this.index));
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$TargetHandler$ForMethodParameter$Resolved.class */
            protected static class Resolved implements Resolved {
                private final ParameterDescription parameterDescription;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.parameterDescription.equals(((Resolved) obj).parameterDescription);
                }

                public int hashCode() {
                    return (17 * 31) + this.parameterDescription.hashCode();
                }

                protected Resolved(ParameterDescription parameterDescription) {
                    this.parameterDescription = parameterDescription;
                }

                @Override // net.bytebuddy.implementation.MethodCall.TargetHandler.Resolved
                public TypeDescription getTypeDescription() {
                    return this.parameterDescription.getType().asErasure();
                }

                @Override // net.bytebuddy.implementation.MethodCall.TargetHandler.Resolved
                public StackManipulation toStackManipulation(MethodDescription invokedMethod, Assigner assigner, Assigner.Typing typing) {
                    StackManipulation stackManipulation = assigner.assign(this.parameterDescription.getType(), invokedMethod.getDeclaringType().asGenericType(), typing);
                    if (!stackManipulation.isValid()) {
                        throw new IllegalStateException("Cannot invoke " + invokedMethod + " on " + this.parameterDescription.getType());
                    }
                    return new StackManipulation.Compound(MethodVariableAccess.load(this.parameterDescription), stackManipulation);
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$TargetHandler$ForMethodCall.class */
        public static class ForMethodCall implements TargetHandler {
            private final Appender appender;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.appender.equals(((ForMethodCall) obj).appender);
            }

            public int hashCode() {
                return (17 * 31) + this.appender.hashCode();
            }

            protected ForMethodCall(Appender appender) {
                this.appender = appender;
            }

            @Override // net.bytebuddy.implementation.MethodCall.TargetHandler
            public Resolved resolve(MethodDescription instrumentedMethod) {
                Resolved targetHandler = this.appender.targetHandler.resolve(instrumentedMethod);
                return new Resolved(this.appender, this.appender.toInvokedMethod(instrumentedMethod, targetHandler), instrumentedMethod, targetHandler);
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$TargetHandler$ForMethodCall$Resolved.class */
            protected static class Resolved implements Resolved {
                private final Appender appender;
                private final MethodDescription methodDescription;
                private final MethodDescription instrumentedMethod;
                private final Resolved targetHandler;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.appender.equals(((Resolved) obj).appender) && this.methodDescription.equals(((Resolved) obj).methodDescription) && this.instrumentedMethod.equals(((Resolved) obj).instrumentedMethod) && this.targetHandler.equals(((Resolved) obj).targetHandler);
                }

                public int hashCode() {
                    return (((((((17 * 31) + this.appender.hashCode()) * 31) + this.methodDescription.hashCode()) * 31) + this.instrumentedMethod.hashCode()) * 31) + this.targetHandler.hashCode();
                }

                protected Resolved(Appender appender, MethodDescription methodDescription, MethodDescription instrumentedMethod, Resolved targetHandler) {
                    this.appender = appender;
                    this.methodDescription = methodDescription;
                    this.instrumentedMethod = instrumentedMethod;
                    this.targetHandler = targetHandler;
                }

                @Override // net.bytebuddy.implementation.MethodCall.TargetHandler.Resolved
                public TypeDescription getTypeDescription() {
                    return this.methodDescription.getReturnType().asErasure();
                }

                @Override // net.bytebuddy.implementation.MethodCall.TargetHandler.Resolved
                public StackManipulation toStackManipulation(MethodDescription invokedMethod, Assigner assigner, Assigner.Typing typing) {
                    StackManipulation stackManipulation = assigner.assign(this.methodDescription.getReturnType(), invokedMethod.getDeclaringType().asGenericType(), typing);
                    if (!stackManipulation.isValid()) {
                        throw new IllegalStateException("Cannot invoke " + invokedMethod + " on " + this.methodDescription.getReturnType());
                    }
                    return new StackManipulation.Compound(this.appender.toStackManipulation(this.instrumentedMethod, this.methodDescription, this.targetHandler), stackManipulation);
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$TargetHandler$ForMethodCall$Factory.class */
            protected static class Factory implements Factory {
                private final MethodCall methodCall;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.methodCall.equals(((Factory) obj).methodCall);
                }

                public int hashCode() {
                    return (17 * 31) + this.methodCall.hashCode();
                }

                public Factory(MethodCall methodCall) {
                    this.methodCall = methodCall;
                }

                @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
                public InstrumentedType prepare(InstrumentedType instrumentedType) {
                    return this.methodCall.prepare(instrumentedType);
                }

                @Override // net.bytebuddy.implementation.MethodCall.TargetHandler.Factory
                public TargetHandler make(Implementation.Target implementationTarget) {
                    MethodCall methodCall = this.methodCall;
                    methodCall.getClass();
                    return new ForMethodCall(new Appender(implementationTarget, TerminationHandler.Simple.IGNORING));
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$MethodInvoker.class */
    public interface MethodInvoker {

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$MethodInvoker$Factory.class */
        public interface Factory {
            MethodInvoker make(TypeDescription typeDescription);
        }

        StackManipulation toStackManipulation(MethodDescription methodDescription, Implementation.Target target);

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$MethodInvoker$ForContextualInvocation.class */
        public static class ForContextualInvocation implements MethodInvoker {
            private final TypeDescription instrumentedType;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.instrumentedType.equals(((ForContextualInvocation) obj).instrumentedType);
            }

            public int hashCode() {
                return (17 * 31) + this.instrumentedType.hashCode();
            }

            protected ForContextualInvocation(TypeDescription instrumentedType) {
                this.instrumentedType = instrumentedType;
            }

            @Override // net.bytebuddy.implementation.MethodCall.MethodInvoker
            public StackManipulation toStackManipulation(MethodDescription invokedMethod, Implementation.Target implementationTarget) {
                if (invokedMethod.isVirtual() && !invokedMethod.isInvokableOn(this.instrumentedType)) {
                    throw new IllegalStateException("Cannot invoke " + invokedMethod + " on " + this.instrumentedType);
                }
                if (invokedMethod.isVirtual()) {
                    return MethodInvocation.invoke(invokedMethod).virtual(this.instrumentedType);
                }
                return MethodInvocation.invoke(invokedMethod);
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$MethodInvoker$ForContextualInvocation$Factory.class */
            public enum Factory implements Factory {
                INSTANCE;

                @Override // net.bytebuddy.implementation.MethodCall.MethodInvoker.Factory
                public MethodInvoker make(TypeDescription instrumentedType) {
                    return new ForContextualInvocation(instrumentedType);
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$MethodInvoker$ForVirtualInvocation.class */
        public static class ForVirtualInvocation implements MethodInvoker {
            private final TypeDescription typeDescription;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.typeDescription.equals(((ForVirtualInvocation) obj).typeDescription);
            }

            public int hashCode() {
                return (17 * 31) + this.typeDescription.hashCode();
            }

            protected ForVirtualInvocation(TypeDescription typeDescription) {
                this.typeDescription = typeDescription;
            }

            @Override // net.bytebuddy.implementation.MethodCall.MethodInvoker
            public StackManipulation toStackManipulation(MethodDescription invokedMethod, Implementation.Target implementationTarget) {
                if (!invokedMethod.isInvokableOn(this.typeDescription)) {
                    throw new IllegalStateException("Cannot invoke " + invokedMethod + " on " + this.typeDescription);
                }
                return MethodInvocation.invoke(invokedMethod).virtual(this.typeDescription);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$MethodInvoker$ForVirtualInvocation$WithImplicitType.class */
            public enum WithImplicitType implements MethodInvoker, Factory {
                INSTANCE;

                @Override // net.bytebuddy.implementation.MethodCall.MethodInvoker.Factory
                public MethodInvoker make(TypeDescription instrumentedType) {
                    return this;
                }

                @Override // net.bytebuddy.implementation.MethodCall.MethodInvoker
                public StackManipulation toStackManipulation(MethodDescription invokedMethod, Implementation.Target implementationTarget) {
                    if (!invokedMethod.isAccessibleTo(implementationTarget.getInstrumentedType()) || !invokedMethod.isVirtual()) {
                        throw new IllegalStateException("Cannot invoke " + invokedMethod + " virtually");
                    }
                    return MethodInvocation.invoke(invokedMethod);
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$MethodInvoker$ForVirtualInvocation$Factory.class */
            public static class Factory implements Factory {
                private final TypeDescription typeDescription;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.typeDescription.equals(((Factory) obj).typeDescription);
                }

                public int hashCode() {
                    return (17 * 31) + this.typeDescription.hashCode();
                }

                protected Factory(TypeDescription typeDescription) {
                    this.typeDescription = typeDescription;
                }

                @Override // net.bytebuddy.implementation.MethodCall.MethodInvoker.Factory
                public MethodInvoker make(TypeDescription instrumentedType) {
                    if (!this.typeDescription.asErasure().isAccessibleTo(instrumentedType)) {
                        throw new IllegalStateException(this.typeDescription + " is not accessible to " + instrumentedType);
                    }
                    return new ForVirtualInvocation(this.typeDescription);
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$MethodInvoker$ForSuperMethodInvocation.class */
        public static class ForSuperMethodInvocation implements MethodInvoker {
            private final TypeDescription instrumentedType;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.instrumentedType.equals(((ForSuperMethodInvocation) obj).instrumentedType);
            }

            public int hashCode() {
                return (17 * 31) + this.instrumentedType.hashCode();
            }

            protected ForSuperMethodInvocation(TypeDescription instrumentedType) {
                this.instrumentedType = instrumentedType;
            }

            @Override // net.bytebuddy.implementation.MethodCall.MethodInvoker
            public StackManipulation toStackManipulation(MethodDescription invokedMethod, Implementation.Target implementationTarget) {
                if (!invokedMethod.isInvokableOn(implementationTarget.getOriginType().asErasure())) {
                    throw new IllegalStateException("Cannot invoke " + invokedMethod + " as super method of " + this.instrumentedType);
                }
                StackManipulation stackManipulation = implementationTarget.invokeDominant(invokedMethod.asSignatureToken()).withCheckedCompatibilityTo(invokedMethod.asTypeToken());
                if (!stackManipulation.isValid()) {
                    throw new IllegalStateException("Cannot invoke " + invokedMethod + " as a super method");
                }
                return stackManipulation;
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$MethodInvoker$ForSuperMethodInvocation$Factory.class */
            public enum Factory implements Factory {
                INSTANCE;

                @Override // net.bytebuddy.implementation.MethodCall.MethodInvoker.Factory
                public MethodInvoker make(TypeDescription instrumentedType) {
                    if (instrumentedType.getSuperClass() == null) {
                        throw new IllegalStateException("Cannot invoke super method for " + instrumentedType);
                    }
                    return new ForSuperMethodInvocation(instrumentedType);
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$MethodInvoker$ForDefaultMethodInvocation.class */
        public static class ForDefaultMethodInvocation implements MethodInvoker {
            private final TypeDescription instrumentedType;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.instrumentedType.equals(((ForDefaultMethodInvocation) obj).instrumentedType);
            }

            public int hashCode() {
                return (17 * 31) + this.instrumentedType.hashCode();
            }

            protected ForDefaultMethodInvocation(TypeDescription instrumentedType) {
                this.instrumentedType = instrumentedType;
            }

            @Override // net.bytebuddy.implementation.MethodCall.MethodInvoker
            public StackManipulation toStackManipulation(MethodDescription invokedMethod, Implementation.Target implementationTarget) {
                if (!invokedMethod.isInvokableOn(this.instrumentedType)) {
                    throw new IllegalStateException("Cannot invoke " + invokedMethod + " as default method of " + this.instrumentedType);
                }
                StackManipulation stackManipulation = implementationTarget.invokeDefault(invokedMethod.asSignatureToken(), invokedMethod.getDeclaringType().asErasure()).withCheckedCompatibilityTo(invokedMethod.asTypeToken());
                if (!stackManipulation.isValid()) {
                    throw new IllegalStateException("Cannot invoke " + invokedMethod + " on " + this.instrumentedType);
                }
                return stackManipulation;
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$MethodInvoker$ForDefaultMethodInvocation$Factory.class */
            enum Factory implements Factory {
                INSTANCE;

                @Override // net.bytebuddy.implementation.MethodCall.MethodInvoker.Factory
                public MethodInvoker make(TypeDescription instrumentedType) {
                    return new ForDefaultMethodInvocation(instrumentedType);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$TerminationHandler.class */
    public interface TerminationHandler {

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$TerminationHandler$Factory.class */
        public interface Factory {
            TerminationHandler make(TypeDescription typeDescription);
        }

        StackManipulation prepare();

        StackManipulation toStackManipulation(MethodDescription methodDescription, MethodDescription methodDescription2, Assigner assigner, Assigner.Typing typing);

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$TerminationHandler$Simple.class */
        public enum Simple implements TerminationHandler, Factory {
            RETURNING { // from class: net.bytebuddy.implementation.MethodCall.TerminationHandler.Simple.1
                @Override // net.bytebuddy.implementation.MethodCall.TerminationHandler
                public StackManipulation toStackManipulation(MethodDescription invokedMethod, MethodDescription instrumentedMethod, Assigner assigner, Assigner.Typing typing) {
                    TypeDescription.Generic returnType;
                    if (invokedMethod.isConstructor()) {
                        returnType = invokedMethod.getDeclaringType().asGenericType();
                    } else {
                        returnType = invokedMethod.getReturnType();
                    }
                    StackManipulation stackManipulation = assigner.assign(returnType, instrumentedMethod.getReturnType(), typing);
                    if (!stackManipulation.isValid()) {
                        throw new IllegalStateException("Cannot return " + invokedMethod.getReturnType() + " from " + instrumentedMethod);
                    }
                    return new StackManipulation.Compound(stackManipulation, MethodReturn.of(instrumentedMethod.getReturnType()));
                }
            },
            DROPPING { // from class: net.bytebuddy.implementation.MethodCall.TerminationHandler.Simple.2
                @Override // net.bytebuddy.implementation.MethodCall.TerminationHandler
                public StackManipulation toStackManipulation(MethodDescription invokedMethod, MethodDescription instrumentedMethod, Assigner assigner, Assigner.Typing typing) {
                    TypeDefinition returnType;
                    if (invokedMethod.isConstructor()) {
                        returnType = invokedMethod.getDeclaringType();
                    } else {
                        returnType = invokedMethod.getReturnType();
                    }
                    return Removal.of(returnType);
                }
            },
            IGNORING { // from class: net.bytebuddy.implementation.MethodCall.TerminationHandler.Simple.3
                @Override // net.bytebuddy.implementation.MethodCall.TerminationHandler
                public StackManipulation toStackManipulation(MethodDescription invokedMethod, MethodDescription instrumentedMethod, Assigner assigner, Assigner.Typing typing) {
                    return StackManipulation.Trivial.INSTANCE;
                }
            };

            @Override // net.bytebuddy.implementation.MethodCall.TerminationHandler.Factory
            public TerminationHandler make(TypeDescription instrumentedType) {
                return this;
            }

            @Override // net.bytebuddy.implementation.MethodCall.TerminationHandler
            public StackManipulation prepare() {
                return StackManipulation.Trivial.INSTANCE;
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$TerminationHandler$FieldSetting.class */
        public static class FieldSetting implements TerminationHandler {
            private final FieldDescription fieldDescription;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.fieldDescription.equals(((FieldSetting) obj).fieldDescription);
            }

            public int hashCode() {
                return (17 * 31) + this.fieldDescription.hashCode();
            }

            protected FieldSetting(FieldDescription fieldDescription) {
                this.fieldDescription = fieldDescription;
            }

            @Override // net.bytebuddy.implementation.MethodCall.TerminationHandler
            public StackManipulation prepare() {
                return this.fieldDescription.isStatic() ? StackManipulation.Trivial.INSTANCE : MethodVariableAccess.loadThis();
            }

            @Override // net.bytebuddy.implementation.MethodCall.TerminationHandler
            public StackManipulation toStackManipulation(MethodDescription invokedMethod, MethodDescription instrumentedMethod, Assigner assigner, Assigner.Typing typing) {
                StackManipulation stackManipulation = assigner.assign(invokedMethod.getReturnType(), this.fieldDescription.getType(), typing);
                if (!stackManipulation.isValid()) {
                    throw new IllegalStateException("Cannot assign result of " + invokedMethod + " to " + this.fieldDescription);
                }
                return new StackManipulation.Compound(stackManipulation, FieldAccess.forField(this.fieldDescription).write());
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$TerminationHandler$FieldSetting$Explicit.class */
            public static class Explicit implements Factory {
                private final FieldDescription fieldDescription;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.fieldDescription.equals(((Explicit) obj).fieldDescription);
                }

                public int hashCode() {
                    return (17 * 31) + this.fieldDescription.hashCode();
                }

                protected Explicit(FieldDescription fieldDescription) {
                    this.fieldDescription = fieldDescription;
                }

                @Override // net.bytebuddy.implementation.MethodCall.TerminationHandler.Factory
                public TerminationHandler make(TypeDescription instrumentedType) {
                    if (!this.fieldDescription.isStatic() && !instrumentedType.isAssignableTo(this.fieldDescription.getDeclaringType().asErasure())) {
                        throw new IllegalStateException("Cannot set " + this.fieldDescription + " from " + instrumentedType);
                    }
                    if (!this.fieldDescription.isAccessibleTo(instrumentedType)) {
                        throw new IllegalStateException("Cannot access " + this.fieldDescription + " from " + instrumentedType);
                    }
                    return new FieldSetting(this.fieldDescription);
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$TerminationHandler$FieldSetting$Implicit.class */
            protected static class Implicit implements Factory {
                private final ElementMatcher<? super FieldDescription> matcher;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.matcher.equals(((Implicit) obj).matcher);
                }

                public int hashCode() {
                    return (17 * 31) + this.matcher.hashCode();
                }

                protected Implicit(ElementMatcher<? super FieldDescription> matcher) {
                    this.matcher = matcher;
                }

                @Override // net.bytebuddy.implementation.MethodCall.TerminationHandler.Factory
                public TerminationHandler make(TypeDescription instrumentedType) {
                    TypeDefinition current;
                    TypeDefinition current2 = instrumentedType;
                    do {
                        FieldList candidates = current2.getDeclaredFields().filter(ElementMatchers.isAccessibleTo(instrumentedType).and(this.matcher));
                        if (candidates.size() == 1) {
                            return new FieldSetting((FieldDescription) candidates.getOnly());
                        }
                        if (candidates.size() == 2) {
                            throw new IllegalStateException(this.matcher + " is ambigous and resolved: " + candidates);
                        }
                        current = current2.getSuperClass();
                        current2 = current;
                    } while (current != null);
                    throw new IllegalStateException(this.matcher + " does not locate any accessible fields for " + instrumentedType);
                }
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$WithoutSpecifiedTarget.class */
    public static class WithoutSpecifiedTarget extends MethodCall {
        protected WithoutSpecifiedTarget(MethodLocator.Factory methodLocator) {
            super(methodLocator, TargetHandler.ForSelfOrStaticInvocation.Factory.INSTANCE, Collections.emptyList(), MethodInvoker.ForContextualInvocation.Factory.INSTANCE, TerminationHandler.Simple.RETURNING, Assigner.DEFAULT, Assigner.Typing.STATIC);
        }

        public MethodCall on(Object target) {
            return on((WithoutSpecifiedTarget) target, (Class<? super WithoutSpecifiedTarget>) target.getClass());
        }

        public <T> MethodCall on(T target, Class<? super T> type) {
            return new MethodCall(this.methodLocator, new TargetHandler.ForValue.Factory(target, TypeDescription.Generic.OfNonGenericType.ForLoadedType.of(type)), this.argumentLoaders, new MethodInvoker.ForVirtualInvocation.Factory(TypeDescription.ForLoadedType.of(type)), this.terminationHandler, this.assigner, this.typing);
        }

        public MethodCall on(StackManipulation stackManipulation, Class<?> type) {
            return on(stackManipulation, TypeDescription.ForLoadedType.of(type));
        }

        public MethodCall on(StackManipulation stackManipulation, TypeDescription typeDescription) {
            return new MethodCall(this.methodLocator, new TargetHandler.Simple(typeDescription, stackManipulation), this.argumentLoaders, new MethodInvoker.ForVirtualInvocation.Factory(typeDescription), this.terminationHandler, this.assigner, this.typing);
        }

        public MethodCall onArgument(int index) {
            if (index < 0) {
                throw new IllegalArgumentException("An argument index cannot be negative: " + index);
            }
            return new MethodCall(this.methodLocator, new TargetHandler.ForMethodParameter(index), this.argumentLoaders, MethodInvoker.ForVirtualInvocation.WithImplicitType.INSTANCE, this.terminationHandler, this.assigner, this.typing);
        }

        public MethodCall onField(String name) {
            return onField(name, FieldLocator.ForClassHierarchy.Factory.INSTANCE);
        }

        public MethodCall onField(String name, FieldLocator.Factory fieldLocatorFactory) {
            return new MethodCall(this.methodLocator, new TargetHandler.ForField.Factory(new TargetHandler.ForField.Location.ForImplicitField(name, fieldLocatorFactory)), this.argumentLoaders, MethodInvoker.ForVirtualInvocation.WithImplicitType.INSTANCE, this.terminationHandler, this.assigner, this.typing);
        }

        public MethodCall onField(Field field) {
            return onField(new FieldDescription.ForLoadedField(field));
        }

        public MethodCall onField(FieldDescription fieldDescription) {
            return new MethodCall(this.methodLocator, new TargetHandler.ForField.Factory(new TargetHandler.ForField.Location.ForExplicitField(fieldDescription)), this.argumentLoaders, MethodInvoker.ForVirtualInvocation.WithImplicitType.INSTANCE, this.terminationHandler, this.assigner, this.typing);
        }

        public MethodCall onMethodCall(MethodCall methodCall) {
            return new MethodCall(this.methodLocator, new TargetHandler.ForMethodCall.Factory(methodCall), this.argumentLoaders, MethodInvoker.ForVirtualInvocation.WithImplicitType.INSTANCE, this.terminationHandler, this.assigner, this.typing);
        }

        public MethodCall onSuper() {
            return new MethodCall(this.methodLocator, TargetHandler.ForSelfOrStaticInvocation.Factory.INSTANCE, this.argumentLoaders, MethodInvoker.ForSuperMethodInvocation.Factory.INSTANCE, this.terminationHandler, this.assigner, this.typing);
        }

        public MethodCall onDefault() {
            return new MethodCall(this.methodLocator, TargetHandler.ForSelfOrStaticInvocation.Factory.INSTANCE, this.argumentLoaders, MethodInvoker.ForDefaultMethodInvocation.Factory.INSTANCE, this.terminationHandler, this.assigner, this.typing);
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$FieldSetting.class */
    public static class FieldSetting implements Implementation.Composable {
        private final MethodCall methodCall;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.methodCall.equals(((FieldSetting) obj).methodCall);
        }

        public int hashCode() {
            return (17 * 31) + this.methodCall.hashCode();
        }

        protected FieldSetting(MethodCall methodCall) {
            this.methodCall = methodCall;
        }

        public Implementation.Composable withAssigner(Assigner assigner, Assigner.Typing typing) {
            return new FieldSetting((MethodCall) this.methodCall.withAssigner(assigner, typing));
        }

        @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
        public InstrumentedType prepare(InstrumentedType instrumentedType) {
            return this.methodCall.prepare(instrumentedType);
        }

        @Override // net.bytebuddy.implementation.Implementation
        public ByteCodeAppender appender(Implementation.Target implementationTarget) {
            return new ByteCodeAppender.Compound(this.methodCall.appender(implementationTarget), Appender.INSTANCE);
        }

        @Override // net.bytebuddy.implementation.Implementation.Composable
        public Implementation andThen(Implementation implementation) {
            return new Implementation.Compound(this.methodCall, implementation);
        }

        @Override // net.bytebuddy.implementation.Implementation.Composable
        public Implementation.Composable andThen(Implementation.Composable implementation) {
            return new Implementation.Compound.Composable(this.methodCall, implementation);
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$FieldSetting$Appender.class */
        protected enum Appender implements ByteCodeAppender {
            INSTANCE;

            @Override // net.bytebuddy.implementation.bytecode.ByteCodeAppender
            public ByteCodeAppender.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext, MethodDescription instrumentedMethod) {
                if (!instrumentedMethod.getReturnType().represents(Void.TYPE)) {
                    throw new IllegalStateException("Instrumented method " + instrumentedMethod + " does not return void for field setting method call");
                }
                return new ByteCodeAppender.Size(MethodReturn.VOID.apply(methodVisitor, implementationContext).getMaximalSize(), instrumentedMethod.getStackSize());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/MethodCall$Appender.class */
    public class Appender implements ByteCodeAppender {
        private final Implementation.Target implementationTarget;
        private final MethodLocator methodLocator;
        private final List<ArgumentLoader.ArgumentProvider> argumentProviders;
        private final MethodInvoker methodInvoker;
        private final TargetHandler targetHandler;
        private final TerminationHandler terminationHandler;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.implementationTarget.equals(((Appender) obj).implementationTarget) && this.methodLocator.equals(((Appender) obj).methodLocator) && this.argumentProviders.equals(((Appender) obj).argumentProviders) && this.methodInvoker.equals(((Appender) obj).methodInvoker) && this.targetHandler.equals(((Appender) obj).targetHandler) && this.terminationHandler.equals(((Appender) obj).terminationHandler) && MethodCall.this.equals(MethodCall.this);
        }

        public int hashCode() {
            return (((((((((((((17 * 31) + this.implementationTarget.hashCode()) * 31) + this.methodLocator.hashCode()) * 31) + this.argumentProviders.hashCode()) * 31) + this.methodInvoker.hashCode()) * 31) + this.targetHandler.hashCode()) * 31) + this.terminationHandler.hashCode()) * 31) + MethodCall.this.hashCode();
        }

        protected Appender(Implementation.Target implementationTarget, TerminationHandler terminationHandler) {
            this.implementationTarget = implementationTarget;
            this.methodLocator = MethodCall.this.methodLocator.make(implementationTarget.getInstrumentedType());
            this.argumentProviders = new ArrayList(MethodCall.this.argumentLoaders.size());
            for (ArgumentLoader.Factory factory : MethodCall.this.argumentLoaders) {
                this.argumentProviders.add(factory.make(implementationTarget));
            }
            this.methodInvoker = MethodCall.this.methodInvoker.make(implementationTarget.getInstrumentedType());
            this.targetHandler = MethodCall.this.targetHandler.make(implementationTarget);
            this.terminationHandler = terminationHandler;
        }

        @Override // net.bytebuddy.implementation.bytecode.ByteCodeAppender
        public ByteCodeAppender.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext, MethodDescription instrumentedMethod) {
            TargetHandler.Resolved targetHandler = this.targetHandler.resolve(instrumentedMethod);
            return new ByteCodeAppender.Size(new StackManipulation.Compound(this.terminationHandler.prepare(), toStackManipulation(instrumentedMethod, toInvokedMethod(instrumentedMethod, targetHandler), targetHandler)).apply(methodVisitor, implementationContext).getMaximalSize(), instrumentedMethod.getStackSize());
        }

        protected MethodDescription toInvokedMethod(MethodDescription instrumentedMethod, TargetHandler.Resolved targetHandler) {
            return this.methodLocator.resolve(targetHandler.getTypeDescription(), instrumentedMethod);
        }

        protected StackManipulation toStackManipulation(MethodDescription instrumentedMethod, MethodDescription invokedMethod, TargetHandler.Resolved targetHandler) {
            List<ArgumentLoader> argumentLoaders = new ArrayList<>();
            for (ArgumentLoader.ArgumentProvider argumentProvider : this.argumentProviders) {
                argumentLoaders.addAll(argumentProvider.resolve(instrumentedMethod, invokedMethod));
            }
            ParameterList<?> parameters = invokedMethod.getParameters();
            if (parameters.size() != argumentLoaders.size()) {
                throw new IllegalStateException(invokedMethod + " does not accept " + argumentLoaders.size() + " arguments");
            }
            Iterator<? extends ParameterDescription> parameterIterator = parameters.iterator();
            List<StackManipulation> argumentInstructions = new ArrayList<>(argumentLoaders.size());
            for (ArgumentLoader argumentLoader : argumentLoaders) {
                argumentInstructions.add(argumentLoader.toStackManipulation((ParameterDescription) parameterIterator.next(), MethodCall.this.assigner, MethodCall.this.typing));
            }
            return new StackManipulation.Compound(targetHandler.toStackManipulation(invokedMethod, MethodCall.this.assigner, MethodCall.this.typing), new StackManipulation.Compound(argumentInstructions), this.methodInvoker.toStackManipulation(invokedMethod, this.implementationTarget), this.terminationHandler.toStackManipulation(invokedMethod, instrumentedMethod, MethodCall.this.assigner, MethodCall.this.typing));
        }
    }
}
