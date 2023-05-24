package net.bytebuddy.implementation;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.annotation.AnnotationValue;
import net.bytebuddy.description.enumeration.EnumerationDescription;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.MethodList;
import net.bytebuddy.description.method.ParameterDescription;
import net.bytebuddy.description.method.ParameterList;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.description.type.TypeList;
import net.bytebuddy.dynamic.scaffold.FieldLocator;
import net.bytebuddy.dynamic.scaffold.InstrumentedType;
import net.bytebuddy.dynamic.scaffold.MethodGraph;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.LoadedTypeInitializer;
import net.bytebuddy.implementation.bytecode.ByteCodeAppender;
import net.bytebuddy.implementation.bytecode.Removal;
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
import net.bytebuddy.implementation.bytecode.member.MethodInvocation;
import net.bytebuddy.implementation.bytecode.member.MethodReturn;
import net.bytebuddy.implementation.bytecode.member.MethodVariableAccess;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.jar.asm.Type;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.CompoundList;
import net.bytebuddy.utility.JavaConstant;
import net.bytebuddy.utility.JavaType;
import net.bytebuddy.utility.RandomString;
@HashCodeAndEqualsPlugin.Enhance
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic.class */
public class InvokeDynamic implements Implementation.Composable {
    protected final MethodDescription.InDefinedShape bootstrap;
    protected final List<?> arguments;
    protected final InvocationProvider invocationProvider;
    protected final TerminationHandler terminationHandler;
    protected final Assigner assigner;
    protected final Assigner.Typing typing;

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic$TerminationHandler.class */
    public enum TerminationHandler {
        RETURNING { // from class: net.bytebuddy.implementation.InvokeDynamic.TerminationHandler.1
            @Override // net.bytebuddy.implementation.InvokeDynamic.TerminationHandler
            protected StackManipulation resolve(MethodDescription interceptedMethod, TypeDescription returnType, Assigner assigner, Assigner.Typing typing) {
                StackManipulation stackManipulation = assigner.assign(returnType.asGenericType(), interceptedMethod.getReturnType(), typing);
                if (!stackManipulation.isValid()) {
                    throw new IllegalStateException("Cannot return " + returnType + " from " + interceptedMethod);
                }
                return new StackManipulation.Compound(stackManipulation, MethodReturn.of(interceptedMethod.getReturnType()));
            }
        },
        DROPPING { // from class: net.bytebuddy.implementation.InvokeDynamic.TerminationHandler.2
            @Override // net.bytebuddy.implementation.InvokeDynamic.TerminationHandler
            protected StackManipulation resolve(MethodDescription interceptedMethod, TypeDescription returnType, Assigner assigner, Assigner.Typing typing) {
                TypeDefinition returnType2;
                if (interceptedMethod.isConstructor()) {
                    returnType2 = interceptedMethod.getDeclaringType();
                } else {
                    returnType2 = interceptedMethod.getReturnType();
                }
                return Removal.of(returnType2);
            }
        };

        protected abstract StackManipulation resolve(MethodDescription methodDescription, TypeDescription typeDescription, Assigner assigner, Assigner.Typing typing);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass() && this.terminationHandler.equals(((InvokeDynamic) obj).terminationHandler) && this.typing.equals(((InvokeDynamic) obj).typing) && this.bootstrap.equals(((InvokeDynamic) obj).bootstrap) && this.arguments.equals(((InvokeDynamic) obj).arguments) && this.invocationProvider.equals(((InvokeDynamic) obj).invocationProvider) && this.assigner.equals(((InvokeDynamic) obj).assigner);
    }

    public int hashCode() {
        return (((((((((((17 * 31) + this.bootstrap.hashCode()) * 31) + this.arguments.hashCode()) * 31) + this.invocationProvider.hashCode()) * 31) + this.terminationHandler.hashCode()) * 31) + this.assigner.hashCode()) * 31) + this.typing.hashCode();
    }

    protected InvokeDynamic(MethodDescription.InDefinedShape bootstrap, List<?> arguments, InvocationProvider invocationProvider, TerminationHandler terminationHandler, Assigner assigner, Assigner.Typing typing) {
        this.bootstrap = bootstrap;
        this.arguments = arguments;
        this.invocationProvider = invocationProvider;
        this.terminationHandler = terminationHandler;
        this.assigner = assigner;
        this.typing = typing;
    }

    public static WithImplicitTarget bootstrap(Method method, Object... constant) {
        return bootstrap(new MethodDescription.ForLoadedMethod(method), constant);
    }

    public static WithImplicitTarget bootstrap(Method method, List<?> constants) {
        return bootstrap(new MethodDescription.ForLoadedMethod(method), constants);
    }

    public static WithImplicitTarget bootstrap(Constructor<?> constructor, Object... constant) {
        return bootstrap(new MethodDescription.ForLoadedConstructor(constructor), constant);
    }

    public static WithImplicitTarget bootstrap(Constructor<?> constructor, List<?> constants) {
        return bootstrap(new MethodDescription.ForLoadedConstructor(constructor), constants);
    }

    public static WithImplicitTarget bootstrap(MethodDescription.InDefinedShape bootstrap, Object... constant) {
        return bootstrap(bootstrap, Arrays.asList(constant));
    }

    public static WithImplicitTarget bootstrap(MethodDescription.InDefinedShape bootstrap, List<?> constants) {
        List<Object> arguments = new ArrayList<>(constants.size());
        List<TypeDescription> types = new ArrayList<>(constants.size());
        for (Object constant : constants) {
            if (constant instanceof JavaConstant) {
                arguments.add(((JavaConstant) constant).asConstantPoolValue());
                types.add(((JavaConstant) constant).getType());
            } else if (constant instanceof TypeDescription) {
                arguments.add(Type.getType(((TypeDescription) constant).getDescriptor()));
                types.add(TypeDescription.CLASS);
            } else {
                arguments.add(constant);
                TypeDescription typeDescription = TypeDescription.ForLoadedType.of(constant.getClass()).asUnboxed();
                types.add(typeDescription);
                if (JavaType.METHOD_TYPE.isInstance(constant) || JavaType.METHOD_HANDLE.isInstance(constant)) {
                    throw new IllegalArgumentException("Must be represented as a JavaConstant instance: " + constant);
                }
                if (constant instanceof Class) {
                    throw new IllegalArgumentException("Must be represented as a TypeDescription instance: " + constant);
                }
                if (!typeDescription.isCompileTimeConstant()) {
                    throw new IllegalArgumentException("Not a compile-time constant: " + constant);
                }
            }
        }
        if (!bootstrap.isInvokeBootstrap(types)) {
            throw new IllegalArgumentException("Not a valid bootstrap method " + bootstrap + " for " + arguments);
        }
        return new WithImplicitTarget(bootstrap, arguments, new InvocationProvider.Default(), TerminationHandler.RETURNING, Assigner.DEFAULT, Assigner.Typing.STATIC);
    }

    public static WithImplicitArguments lambda(Method method, Class<?> functionalInterface) {
        return lambda(new MethodDescription.ForLoadedMethod(method), TypeDescription.ForLoadedType.of(functionalInterface));
    }

    public static WithImplicitArguments lambda(Method method, Class<?> functionalInterface, MethodGraph.Compiler methodGraphCompiler) {
        return lambda(new MethodDescription.ForLoadedMethod(method), TypeDescription.ForLoadedType.of(functionalInterface), methodGraphCompiler);
    }

    public static WithImplicitArguments lambda(MethodDescription.InDefinedShape methodDescription, TypeDescription functionalInterface) {
        return lambda(methodDescription, functionalInterface, MethodGraph.Compiler.Default.forJavaHierarchy());
    }

    public static WithImplicitArguments lambda(MethodDescription.InDefinedShape methodDescription, TypeDescription functionalInterface, MethodGraph.Compiler methodGraphCompiler) {
        if (!functionalInterface.isInterface()) {
            throw new IllegalArgumentException(functionalInterface + " is not an interface type");
        }
        MethodList methods = methodGraphCompiler.compile(functionalInterface).listNodes().asMethodList().filter(ElementMatchers.isAbstract());
        if (methods.size() != 1) {
            throw new IllegalArgumentException(functionalInterface + " does not define exactly one abstract method: " + methods);
        }
        return bootstrap(new MethodDescription.Latent(new TypeDescription.Latent("java.lang.invoke.LambdaMetafactory", 1, TypeDescription.Generic.OBJECT, new TypeDescription.Generic[0]), "metafactory", 9, Collections.emptyList(), JavaType.CALL_SITE.getTypeStub().asGenericType(), Arrays.asList(new ParameterDescription.Token(JavaType.METHOD_HANDLES_LOOKUP.getTypeStub().asGenericType()), new ParameterDescription.Token(TypeDescription.STRING.asGenericType()), new ParameterDescription.Token(JavaType.METHOD_TYPE.getTypeStub().asGenericType()), new ParameterDescription.Token(JavaType.METHOD_TYPE.getTypeStub().asGenericType()), new ParameterDescription.Token(JavaType.METHOD_HANDLE.getTypeStub().asGenericType()), new ParameterDescription.Token(JavaType.METHOD_TYPE.getTypeStub().asGenericType())), Collections.emptyList(), Collections.emptyList(), AnnotationValue.UNDEFINED, TypeDescription.Generic.UNDEFINED), JavaConstant.MethodType.of((MethodDescription) methods.asDefined().getOnly()), JavaConstant.MethodHandle.of(methodDescription), JavaConstant.MethodType.of((MethodDescription) methods.asDefined().getOnly())).invoke(((MethodDescription.InDefinedShape) methods.asDefined().getOnly()).getInternalName());
    }

    public InvokeDynamic withBooleanValue(boolean... value) {
        List<InvocationProvider.ArgumentProvider> argumentProviders = new ArrayList<>(value.length);
        for (boolean aValue : value) {
            argumentProviders.add(new InvocationProvider.ArgumentProvider.ForBooleanConstant(aValue));
        }
        return new InvokeDynamic(this.bootstrap, this.arguments, this.invocationProvider.appendArguments(argumentProviders), this.terminationHandler, this.assigner, this.typing);
    }

    public InvokeDynamic withByteValue(byte... value) {
        List<InvocationProvider.ArgumentProvider> argumentProviders = new ArrayList<>(value.length);
        for (byte aValue : value) {
            argumentProviders.add(new InvocationProvider.ArgumentProvider.ForByteConstant(aValue));
        }
        return new InvokeDynamic(this.bootstrap, this.arguments, this.invocationProvider.appendArguments(argumentProviders), this.terminationHandler, this.assigner, this.typing);
    }

    public InvokeDynamic withShortValue(short... value) {
        List<InvocationProvider.ArgumentProvider> argumentProviders = new ArrayList<>(value.length);
        for (short aValue : value) {
            argumentProviders.add(new InvocationProvider.ArgumentProvider.ForShortConstant(aValue));
        }
        return new InvokeDynamic(this.bootstrap, this.arguments, this.invocationProvider.appendArguments(argumentProviders), this.terminationHandler, this.assigner, this.typing);
    }

    public InvokeDynamic withCharacterValue(char... value) {
        List<InvocationProvider.ArgumentProvider> argumentProviders = new ArrayList<>(value.length);
        for (char aValue : value) {
            argumentProviders.add(new InvocationProvider.ArgumentProvider.ForCharacterConstant(aValue));
        }
        return new InvokeDynamic(this.bootstrap, this.arguments, this.invocationProvider.appendArguments(argumentProviders), this.terminationHandler, this.assigner, this.typing);
    }

    public InvokeDynamic withIntegerValue(int... value) {
        List<InvocationProvider.ArgumentProvider> argumentProviders = new ArrayList<>(value.length);
        for (int aValue : value) {
            argumentProviders.add(new InvocationProvider.ArgumentProvider.ForIntegerConstant(aValue));
        }
        return new InvokeDynamic(this.bootstrap, this.arguments, this.invocationProvider.appendArguments(argumentProviders), this.terminationHandler, this.assigner, this.typing);
    }

    public InvokeDynamic withLongValue(long... value) {
        List<InvocationProvider.ArgumentProvider> argumentProviders = new ArrayList<>(value.length);
        for (long aValue : value) {
            argumentProviders.add(new InvocationProvider.ArgumentProvider.ForLongConstant(aValue));
        }
        return new InvokeDynamic(this.bootstrap, this.arguments, this.invocationProvider.appendArguments(argumentProviders), this.terminationHandler, this.assigner, this.typing);
    }

    public InvokeDynamic withFloatValue(float... value) {
        List<InvocationProvider.ArgumentProvider> argumentProviders = new ArrayList<>(value.length);
        for (float aValue : value) {
            argumentProviders.add(new InvocationProvider.ArgumentProvider.ForFloatConstant(aValue));
        }
        return new InvokeDynamic(this.bootstrap, this.arguments, this.invocationProvider.appendArguments(argumentProviders), this.terminationHandler, this.assigner, this.typing);
    }

    public InvokeDynamic withDoubleValue(double... value) {
        List<InvocationProvider.ArgumentProvider> argumentProviders = new ArrayList<>(value.length);
        for (double aValue : value) {
            argumentProviders.add(new InvocationProvider.ArgumentProvider.ForDoubleConstant(aValue));
        }
        return new InvokeDynamic(this.bootstrap, this.arguments, this.invocationProvider.appendArguments(argumentProviders), this.terminationHandler, this.assigner, this.typing);
    }

    public InvokeDynamic withValue(Object... value) {
        List<InvocationProvider.ArgumentProvider> argumentProviders = new ArrayList<>(value.length);
        for (Object aValue : value) {
            argumentProviders.add(InvocationProvider.ArgumentProvider.ConstantPoolWrapper.of(aValue));
        }
        return new InvokeDynamic(this.bootstrap, this.arguments, this.invocationProvider.appendArguments(argumentProviders), this.terminationHandler, this.assigner, this.typing);
    }

    public WithImplicitType withReference(Object value) {
        return new WithImplicitType.OfInstance(this.bootstrap, this.arguments, this.invocationProvider, this.terminationHandler, this.assigner, this.typing, value);
    }

    public InvokeDynamic withReference(Object... value) {
        List<InvocationProvider.ArgumentProvider> argumentProviders = new ArrayList<>(value.length);
        for (Object aValue : value) {
            argumentProviders.add(InvocationProvider.ArgumentProvider.ForInstance.of(aValue));
        }
        return new InvokeDynamic(this.bootstrap, this.arguments, this.invocationProvider.appendArguments(argumentProviders), this.terminationHandler, this.assigner, this.typing);
    }

    public InvokeDynamic withType(TypeDescription... typeDescription) {
        List<InvocationProvider.ArgumentProvider> argumentProviders = new ArrayList<>(typeDescription.length);
        for (TypeDescription aTypeDescription : typeDescription) {
            argumentProviders.add(new InvocationProvider.ArgumentProvider.ForClassConstant(aTypeDescription));
        }
        return new InvokeDynamic(this.bootstrap, this.arguments, this.invocationProvider.appendArguments(argumentProviders), this.terminationHandler, this.assigner, this.typing);
    }

    public InvokeDynamic withEnumeration(EnumerationDescription... enumerationDescription) {
        List<InvocationProvider.ArgumentProvider> argumentProviders = new ArrayList<>(enumerationDescription.length);
        for (EnumerationDescription anEnumerationDescription : enumerationDescription) {
            argumentProviders.add(new InvocationProvider.ArgumentProvider.ForEnumerationValue(anEnumerationDescription));
        }
        return new InvokeDynamic(this.bootstrap, this.arguments, this.invocationProvider.appendArguments(argumentProviders), this.terminationHandler, this.assigner, this.typing);
    }

    public InvokeDynamic withInstance(JavaConstant... javaConstant) {
        List<InvocationProvider.ArgumentProvider> argumentProviders = new ArrayList<>(javaConstant.length);
        for (JavaConstant aJavaConstant : javaConstant) {
            argumentProviders.add(new InvocationProvider.ArgumentProvider.ForJavaConstant(aJavaConstant));
        }
        return new InvokeDynamic(this.bootstrap, this.arguments, this.invocationProvider.appendArguments(argumentProviders), this.terminationHandler, this.assigner, this.typing);
    }

    public InvokeDynamic withNullValue(Class<?>... type) {
        return withNullValue((TypeDescription[]) new TypeList.ForLoadedTypes(type).toArray(new TypeDescription[0]));
    }

    public InvokeDynamic withNullValue(TypeDescription... typeDescription) {
        List<InvocationProvider.ArgumentProvider> argumentProviders = new ArrayList<>(typeDescription.length);
        for (TypeDescription aTypeDescription : typeDescription) {
            if (aTypeDescription.isPrimitive()) {
                throw new IllegalArgumentException("Cannot assign null to primitive type: " + aTypeDescription);
            }
            argumentProviders.add(new InvocationProvider.ArgumentProvider.ForNullValue(aTypeDescription));
        }
        return new InvokeDynamic(this.bootstrap, this.arguments, this.invocationProvider.appendArguments(argumentProviders), this.terminationHandler, this.assigner, this.typing);
    }

    public InvokeDynamic withArgument(int... index) {
        List<InvocationProvider.ArgumentProvider> argumentProviders = new ArrayList<>(index.length);
        for (int anIndex : index) {
            if (anIndex < 0) {
                throw new IllegalArgumentException("Method parameter indices cannot be negative: " + anIndex);
            }
            argumentProviders.add(new InvocationProvider.ArgumentProvider.ForMethodParameter(anIndex));
        }
        return new InvokeDynamic(this.bootstrap, this.arguments, this.invocationProvider.appendArguments(argumentProviders), this.terminationHandler, this.assigner, this.typing);
    }

    public WithImplicitType withArgument(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("Method parameter indices cannot be negative: " + index);
        }
        return new WithImplicitType.OfArgument(this.bootstrap, this.arguments, this.invocationProvider, this.terminationHandler, this.assigner, this.typing, index);
    }

    public InvokeDynamic withThis(Class<?>... type) {
        return withThis((TypeDescription[]) new TypeList.ForLoadedTypes(type).toArray(new TypeDescription[0]));
    }

    public InvokeDynamic withThis(TypeDescription... typeDescription) {
        List<InvocationProvider.ArgumentProvider> argumentProviders = new ArrayList<>(typeDescription.length);
        for (TypeDescription aTypeDescription : typeDescription) {
            argumentProviders.add(new InvocationProvider.ArgumentProvider.ForThisInstance(aTypeDescription));
        }
        return new InvokeDynamic(this.bootstrap, this.arguments, this.invocationProvider.appendArguments(argumentProviders), this.terminationHandler, this.assigner, this.typing);
    }

    public InvokeDynamic withMethodArguments() {
        return new InvokeDynamic(this.bootstrap, this.arguments, this.invocationProvider.appendArgument(InvocationProvider.ArgumentProvider.ForInterceptedMethodParameters.INSTANCE), this.terminationHandler, this.assigner, this.typing);
    }

    public InvokeDynamic withImplicitAndMethodArguments() {
        return new InvokeDynamic(this.bootstrap, this.arguments, this.invocationProvider.appendArgument(InvocationProvider.ArgumentProvider.ForInterceptedMethodInstanceAndParameters.INSTANCE), this.terminationHandler, this.assigner, this.typing);
    }

    public InvokeDynamic withField(String... name) {
        return withField(FieldLocator.ForClassHierarchy.Factory.INSTANCE, name);
    }

    public InvokeDynamic withField(FieldLocator.Factory fieldLocatorFactory, String... name) {
        List<InvocationProvider.ArgumentProvider> argumentProviders = new ArrayList<>(name.length);
        for (String aName : name) {
            argumentProviders.add(new InvocationProvider.ArgumentProvider.ForField(aName, fieldLocatorFactory));
        }
        return new InvokeDynamic(this.bootstrap, this.arguments, this.invocationProvider.appendArguments(argumentProviders), this.terminationHandler, this.assigner, this.typing);
    }

    public WithImplicitType withField(String name) {
        return withField(name, FieldLocator.ForClassHierarchy.Factory.INSTANCE);
    }

    public WithImplicitType withField(String name, FieldLocator.Factory fieldLocatorFactory) {
        return new WithImplicitType.OfField(this.bootstrap, this.arguments, this.invocationProvider, this.terminationHandler, this.assigner, this.typing, name, fieldLocatorFactory);
    }

    public Implementation.Composable withAssigner(Assigner assigner, Assigner.Typing typing) {
        return new InvokeDynamic(this.bootstrap, this.arguments, this.invocationProvider, this.terminationHandler, assigner, typing);
    }

    @Override // net.bytebuddy.implementation.Implementation.Composable
    public Implementation andThen(Implementation implementation) {
        return new Implementation.Compound(new InvokeDynamic(this.bootstrap, this.arguments, this.invocationProvider, TerminationHandler.DROPPING, this.assigner, this.typing), implementation);
    }

    @Override // net.bytebuddy.implementation.Implementation.Composable
    public Implementation.Composable andThen(Implementation.Composable implementation) {
        return new Implementation.Compound.Composable(new InvokeDynamic(this.bootstrap, this.arguments, this.invocationProvider, TerminationHandler.DROPPING, this.assigner, this.typing), implementation);
    }

    @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
    public InstrumentedType prepare(InstrumentedType instrumentedType) {
        return this.invocationProvider.prepare(instrumentedType);
    }

    @Override // net.bytebuddy.implementation.Implementation
    public ByteCodeAppender appender(Implementation.Target implementationTarget) {
        return new Appender(implementationTarget.getInstrumentedType());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic$InvocationProvider.class */
    public interface InvocationProvider {
        Target make(MethodDescription methodDescription);

        InvocationProvider appendArguments(List<ArgumentProvider> list);

        InvocationProvider appendArgument(ArgumentProvider argumentProvider);

        InvocationProvider withoutArguments();

        InvocationProvider withNameProvider(NameProvider nameProvider);

        InvocationProvider withReturnTypeProvider(ReturnTypeProvider returnTypeProvider);

        InstrumentedType prepare(InstrumentedType instrumentedType);

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic$InvocationProvider$Target.class */
        public interface Target {
            Resolved resolve(TypeDescription typeDescription, Assigner assigner, Assigner.Typing typing);

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic$InvocationProvider$Target$Resolved.class */
            public interface Resolved {
                StackManipulation getStackManipulation();

                TypeDescription getReturnType();

                String getInternalName();

                List<TypeDescription> getParameterTypes();

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic$InvocationProvider$Target$Resolved$Simple.class */
                public static class Simple implements Resolved {
                    private final StackManipulation stackManipulation;
                    private final String internalName;
                    private final TypeDescription returnType;
                    private final List<TypeDescription> parameterTypes;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.internalName.equals(((Simple) obj).internalName) && this.stackManipulation.equals(((Simple) obj).stackManipulation) && this.returnType.equals(((Simple) obj).returnType) && this.parameterTypes.equals(((Simple) obj).parameterTypes);
                    }

                    public int hashCode() {
                        return (((((((17 * 31) + this.stackManipulation.hashCode()) * 31) + this.internalName.hashCode()) * 31) + this.returnType.hashCode()) * 31) + this.parameterTypes.hashCode();
                    }

                    public Simple(StackManipulation stackManipulation, String internalName, TypeDescription returnType, List<TypeDescription> parameterTypes) {
                        this.stackManipulation = stackManipulation;
                        this.internalName = internalName;
                        this.returnType = returnType;
                        this.parameterTypes = parameterTypes;
                    }

                    @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.Target.Resolved
                    public StackManipulation getStackManipulation() {
                        return this.stackManipulation;
                    }

                    @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.Target.Resolved
                    public TypeDescription getReturnType() {
                        return this.returnType;
                    }

                    @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.Target.Resolved
                    public String getInternalName() {
                        return this.internalName;
                    }

                    @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.Target.Resolved
                    public List<TypeDescription> getParameterTypes() {
                        return this.parameterTypes;
                    }
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic$InvocationProvider$ArgumentProvider.class */
        public interface ArgumentProvider {
            Resolved resolve(TypeDescription typeDescription, MethodDescription methodDescription, Assigner assigner, Assigner.Typing typing);

            InstrumentedType prepare(InstrumentedType instrumentedType);

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic$InvocationProvider$ArgumentProvider$ForInterceptedMethodInstanceAndParameters.class */
            public enum ForInterceptedMethodInstanceAndParameters implements ArgumentProvider {
                INSTANCE;

                @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider
                public Resolved resolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, Assigner assigner, Assigner.Typing typing) {
                    List of;
                    StackManipulation prependThisReference = MethodVariableAccess.allArgumentsOf(instrumentedMethod).prependThisReference();
                    if (instrumentedMethod.isStatic()) {
                        of = instrumentedMethod.getParameters().asTypeList().asErasures();
                    } else {
                        of = CompoundList.of(instrumentedMethod.getDeclaringType().asErasure(), instrumentedMethod.getParameters().asTypeList().asErasures());
                    }
                    return new Resolved.Simple(prependThisReference, of);
                }

                @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider
                public InstrumentedType prepare(InstrumentedType instrumentedType) {
                    return instrumentedType;
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic$InvocationProvider$ArgumentProvider$ForInterceptedMethodParameters.class */
            public enum ForInterceptedMethodParameters implements ArgumentProvider {
                INSTANCE;

                @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider
                public Resolved resolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, Assigner assigner, Assigner.Typing typing) {
                    return new Resolved.Simple(MethodVariableAccess.allArgumentsOf(instrumentedMethod), instrumentedMethod.getParameters().asTypeList().asErasures());
                }

                @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider
                public InstrumentedType prepare(InstrumentedType instrumentedType) {
                    return instrumentedType;
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic$InvocationProvider$ArgumentProvider$ConstantPoolWrapper.class */
            public enum ConstantPoolWrapper {
                BOOLEAN(Boolean.TYPE, Boolean.class) { // from class: net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider.ConstantPoolWrapper.1
                    @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider.ConstantPoolWrapper
                    protected ArgumentProvider make(Object value) {
                        return new WrappingArgumentProvider(IntegerConstant.forValue(((Boolean) value).booleanValue()));
                    }
                },
                BYTE(Byte.TYPE, Byte.class) { // from class: net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider.ConstantPoolWrapper.2
                    @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider.ConstantPoolWrapper
                    protected ArgumentProvider make(Object value) {
                        return new WrappingArgumentProvider(IntegerConstant.forValue(((Byte) value).byteValue()));
                    }
                },
                SHORT(Short.TYPE, Short.class) { // from class: net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider.ConstantPoolWrapper.3
                    @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider.ConstantPoolWrapper
                    protected ArgumentProvider make(Object value) {
                        return new WrappingArgumentProvider(IntegerConstant.forValue(((Short) value).shortValue()));
                    }
                },
                CHARACTER(Character.TYPE, Character.class) { // from class: net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider.ConstantPoolWrapper.4
                    @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider.ConstantPoolWrapper
                    protected ArgumentProvider make(Object value) {
                        return new WrappingArgumentProvider(IntegerConstant.forValue(((Character) value).charValue()));
                    }
                },
                INTEGER(Integer.TYPE, Integer.class) { // from class: net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider.ConstantPoolWrapper.5
                    @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider.ConstantPoolWrapper
                    protected ArgumentProvider make(Object value) {
                        return new WrappingArgumentProvider(IntegerConstant.forValue(((Integer) value).intValue()));
                    }
                },
                LONG(Long.TYPE, Long.class) { // from class: net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider.ConstantPoolWrapper.6
                    @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider.ConstantPoolWrapper
                    protected ArgumentProvider make(Object value) {
                        return new WrappingArgumentProvider(LongConstant.forValue(((Long) value).longValue()));
                    }
                },
                FLOAT(Float.TYPE, Float.class) { // from class: net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider.ConstantPoolWrapper.7
                    @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider.ConstantPoolWrapper
                    protected ArgumentProvider make(Object value) {
                        return new WrappingArgumentProvider(FloatConstant.forValue(((Float) value).floatValue()));
                    }
                },
                DOUBLE(Double.TYPE, Double.class) { // from class: net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider.ConstantPoolWrapper.8
                    @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider.ConstantPoolWrapper
                    protected ArgumentProvider make(Object value) {
                        return new WrappingArgumentProvider(DoubleConstant.forValue(((Double) value).doubleValue()));
                    }
                };
                
                private final TypeDescription primitiveType;
                private final TypeDescription wrapperType;

                protected abstract ArgumentProvider make(Object obj);

                ConstantPoolWrapper(Class cls, Class cls2) {
                    this.primitiveType = TypeDescription.ForLoadedType.of(cls);
                    this.wrapperType = TypeDescription.ForLoadedType.of(cls2);
                }

                public static ArgumentProvider of(Object value) {
                    if (value instanceof Boolean) {
                        return BOOLEAN.make(value);
                    }
                    if (value instanceof Byte) {
                        return BYTE.make(value);
                    }
                    if (value instanceof Short) {
                        return SHORT.make(value);
                    }
                    if (value instanceof Character) {
                        return CHARACTER.make(value);
                    }
                    if (value instanceof Integer) {
                        return INTEGER.make(value);
                    }
                    if (value instanceof Long) {
                        return LONG.make(value);
                    }
                    if (value instanceof Float) {
                        return FLOAT.make(value);
                    }
                    if (value instanceof Double) {
                        return DOUBLE.make(value);
                    }
                    if (value instanceof String) {
                        return new ForStringConstant((String) value);
                    }
                    if (value instanceof Class) {
                        return new ForClassConstant(TypeDescription.ForLoadedType.of((Class) value));
                    }
                    if (value instanceof Enum) {
                        return new ForEnumerationValue(new EnumerationDescription.ForLoadedEnumeration((Enum) value));
                    }
                    if (JavaType.METHOD_HANDLE.isInstance(value)) {
                        return new ForJavaConstant(JavaConstant.MethodHandle.ofLoaded(value));
                    }
                    if (JavaType.METHOD_TYPE.isInstance(value)) {
                        return new ForJavaConstant(JavaConstant.MethodType.ofLoaded(value));
                    }
                    return ForInstance.of(value);
                }

                @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic$InvocationProvider$ArgumentProvider$ConstantPoolWrapper$WrappingArgumentProvider.class */
                protected class WrappingArgumentProvider implements ArgumentProvider {
                    private final StackManipulation stackManipulation;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && ConstantPoolWrapper.this.equals(ConstantPoolWrapper.this) && this.stackManipulation.equals(((WrappingArgumentProvider) obj).stackManipulation);
                    }

                    public int hashCode() {
                        return (((17 * 31) + this.stackManipulation.hashCode()) * 31) + ConstantPoolWrapper.this.hashCode();
                    }

                    protected WrappingArgumentProvider(StackManipulation stackManipulation) {
                        this.stackManipulation = stackManipulation;
                    }

                    @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider
                    public Resolved resolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, Assigner assigner, Assigner.Typing typing) {
                        return new Resolved.Simple(new StackManipulation.Compound(this.stackManipulation, assigner.assign(ConstantPoolWrapper.this.primitiveType.asGenericType(), ConstantPoolWrapper.this.wrapperType.asGenericType(), typing)), ConstantPoolWrapper.this.wrapperType);
                    }

                    @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider
                    public InstrumentedType prepare(InstrumentedType instrumentedType) {
                        return instrumentedType;
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic$InvocationProvider$ArgumentProvider$Resolved.class */
            public interface Resolved {
                StackManipulation getLoadInstruction();

                List<TypeDescription> getLoadedTypes();

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic$InvocationProvider$ArgumentProvider$Resolved$Simple.class */
                public static class Simple implements Resolved {
                    private final StackManipulation stackManipulation;
                    private final List<TypeDescription> loadedTypes;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.stackManipulation.equals(((Simple) obj).stackManipulation) && this.loadedTypes.equals(((Simple) obj).loadedTypes);
                    }

                    public int hashCode() {
                        return (((17 * 31) + this.stackManipulation.hashCode()) * 31) + this.loadedTypes.hashCode();
                    }

                    public Simple(StackManipulation stackManipulation, TypeDescription loadedType) {
                        this(stackManipulation, Collections.singletonList(loadedType));
                    }

                    public Simple(StackManipulation stackManipulation, List<TypeDescription> loadedTypes) {
                        this.stackManipulation = stackManipulation;
                        this.loadedTypes = loadedTypes;
                    }

                    @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider.Resolved
                    public StackManipulation getLoadInstruction() {
                        return this.stackManipulation;
                    }

                    @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider.Resolved
                    public List<TypeDescription> getLoadedTypes() {
                        return this.loadedTypes;
                    }
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic$InvocationProvider$ArgumentProvider$ForThisInstance.class */
            public static class ForThisInstance implements ArgumentProvider {
                private final TypeDescription typeDescription;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.typeDescription.equals(((ForThisInstance) obj).typeDescription);
                }

                public int hashCode() {
                    return (17 * 31) + this.typeDescription.hashCode();
                }

                protected ForThisInstance(TypeDescription typeDescription) {
                    this.typeDescription = typeDescription;
                }

                @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider
                public Resolved resolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, Assigner assigner, Assigner.Typing typing) {
                    if (instrumentedMethod.isStatic()) {
                        throw new IllegalStateException("Cannot get this instance from static method: " + instrumentedMethod);
                    }
                    if (!instrumentedType.isAssignableTo(this.typeDescription)) {
                        throw new IllegalStateException(instrumentedType + " is not assignable to " + instrumentedType);
                    }
                    return new Resolved.Simple(MethodVariableAccess.loadThis(), this.typeDescription);
                }

                @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider
                public InstrumentedType prepare(InstrumentedType instrumentedType) {
                    return instrumentedType;
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic$InvocationProvider$ArgumentProvider$ForInstance.class */
            public static class ForInstance implements ArgumentProvider {
                private static final String FIELD_PREFIX = "invokeDynamic";
                private final Object value;
                private final TypeDescription fieldType;
                @HashCodeAndEqualsPlugin.ValueHandling(HashCodeAndEqualsPlugin.ValueHandling.Sort.IGNORE)
                private final String name = "invokeDynamic$" + RandomString.make();

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.value.equals(((ForInstance) obj).value) && this.fieldType.equals(((ForInstance) obj).fieldType);
                }

                public int hashCode() {
                    return (((17 * 31) + this.value.hashCode()) * 31) + this.fieldType.hashCode();
                }

                protected ForInstance(Object value, TypeDescription fieldType) {
                    this.value = value;
                    this.fieldType = fieldType;
                }

                protected static ArgumentProvider of(Object value) {
                    return new ForInstance(value, TypeDescription.ForLoadedType.of(value.getClass()));
                }

                @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider
                public Resolved resolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, Assigner assigner, Assigner.Typing typing) {
                    FieldDescription fieldDescription = (FieldDescription) instrumentedType.getDeclaredFields().filter(ElementMatchers.named(this.name)).getOnly();
                    StackManipulation stackManipulation = assigner.assign(fieldDescription.getType(), this.fieldType.asGenericType(), typing);
                    if (!stackManipulation.isValid()) {
                        throw new IllegalStateException("Cannot assign " + fieldDescription + " to " + this.fieldType);
                    }
                    return new Resolved.Simple(new StackManipulation.Compound(FieldAccess.forField(fieldDescription).read(), stackManipulation), fieldDescription.getType().asErasure());
                }

                @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider
                public InstrumentedType prepare(InstrumentedType instrumentedType) {
                    return instrumentedType.withField(new FieldDescription.Token(this.name, 4169, this.fieldType.asGenericType())).withInitializer(new LoadedTypeInitializer.ForStaticField(this.name, this.value));
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic$InvocationProvider$ArgumentProvider$ForField.class */
            public static class ForField implements ArgumentProvider {
                protected final String fieldName;
                protected final FieldLocator.Factory fieldLocatorFactory;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.fieldName.equals(((ForField) obj).fieldName) && this.fieldLocatorFactory.equals(((ForField) obj).fieldLocatorFactory);
                }

                public int hashCode() {
                    return (((17 * 31) + this.fieldName.hashCode()) * 31) + this.fieldLocatorFactory.hashCode();
                }

                protected ForField(String fieldName, FieldLocator.Factory fieldLocatorFactory) {
                    this.fieldName = fieldName;
                    this.fieldLocatorFactory = fieldLocatorFactory;
                }

                @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider
                public Resolved resolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, Assigner assigner, Assigner.Typing typing) {
                    FieldLocator.Resolution resolution = this.fieldLocatorFactory.make(instrumentedType).locate(this.fieldName);
                    if (!resolution.isResolved()) {
                        throw new IllegalStateException("Cannot find a field " + this.fieldName + " for " + instrumentedType);
                    }
                    if (!resolution.getField().isStatic() && instrumentedMethod.isStatic()) {
                        throw new IllegalStateException("Cannot access non-static " + resolution.getField() + " from " + instrumentedMethod);
                    }
                    StackManipulation[] stackManipulationArr = new StackManipulation[2];
                    stackManipulationArr[0] = resolution.getField().isStatic() ? StackManipulation.Trivial.INSTANCE : MethodVariableAccess.loadThis();
                    stackManipulationArr[1] = FieldAccess.forField(resolution.getField()).read();
                    return doResolve(new StackManipulation.Compound(stackManipulationArr), resolution.getField().getType(), assigner, typing);
                }

                protected Resolved doResolve(StackManipulation access, TypeDescription.Generic type, Assigner assigner, Assigner.Typing typing) {
                    return new Resolved.Simple(access, type.asErasure());
                }

                @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider
                public InstrumentedType prepare(InstrumentedType instrumentedType) {
                    return instrumentedType;
                }

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic$InvocationProvider$ArgumentProvider$ForField$WithExplicitType.class */
                protected static class WithExplicitType extends ForField {
                    private final TypeDescription typeDescription;

                    @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider.ForField
                    public boolean equals(Object obj) {
                        if (super.equals(obj)) {
                            if (this == obj) {
                                return true;
                            }
                            return obj != null && getClass() == obj.getClass() && this.typeDescription.equals(((WithExplicitType) obj).typeDescription);
                        }
                        return false;
                    }

                    @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider.ForField
                    public int hashCode() {
                        return (super.hashCode() * 31) + this.typeDescription.hashCode();
                    }

                    protected WithExplicitType(String fieldName, FieldLocator.Factory fieldLocatorFactory, TypeDescription typeDescription) {
                        super(fieldName, fieldLocatorFactory);
                        this.typeDescription = typeDescription;
                    }

                    @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider.ForField
                    protected Resolved doResolve(StackManipulation access, TypeDescription.Generic typeDescription, Assigner assigner, Assigner.Typing typing) {
                        StackManipulation stackManipulation = assigner.assign(typeDescription, this.typeDescription.asGenericType(), typing);
                        if (!stackManipulation.isValid()) {
                            throw new IllegalStateException("Cannot assign " + typeDescription + " to " + this.typeDescription);
                        }
                        return new Resolved.Simple(new StackManipulation.Compound(access, stackManipulation), this.typeDescription);
                    }
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic$InvocationProvider$ArgumentProvider$ForMethodParameter.class */
            public static class ForMethodParameter implements ArgumentProvider {
                protected final int index;

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

                @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider
                public Resolved resolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, Assigner assigner, Assigner.Typing typing) {
                    ParameterList<?> parameters = instrumentedMethod.getParameters();
                    if (this.index >= parameters.size()) {
                        throw new IllegalStateException("No parameter " + this.index + " for " + instrumentedMethod);
                    }
                    return doResolve(MethodVariableAccess.load((ParameterDescription) parameters.get(this.index)), ((ParameterDescription) parameters.get(this.index)).getType(), assigner, typing);
                }

                protected Resolved doResolve(StackManipulation access, TypeDescription.Generic type, Assigner assigner, Assigner.Typing typing) {
                    return new Resolved.Simple(access, type.asErasure());
                }

                @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider
                public InstrumentedType prepare(InstrumentedType instrumentedType) {
                    return instrumentedType;
                }

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic$InvocationProvider$ArgumentProvider$ForMethodParameter$WithExplicitType.class */
                protected static class WithExplicitType extends ForMethodParameter {
                    private final TypeDescription typeDescription;

                    @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider.ForMethodParameter
                    public boolean equals(Object obj) {
                        if (super.equals(obj)) {
                            if (this == obj) {
                                return true;
                            }
                            return obj != null && getClass() == obj.getClass() && this.typeDescription.equals(((WithExplicitType) obj).typeDescription);
                        }
                        return false;
                    }

                    @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider.ForMethodParameter
                    public int hashCode() {
                        return (super.hashCode() * 31) + this.typeDescription.hashCode();
                    }

                    protected WithExplicitType(int index, TypeDescription typeDescription) {
                        super(index);
                        this.typeDescription = typeDescription;
                    }

                    @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider.ForMethodParameter
                    protected Resolved doResolve(StackManipulation access, TypeDescription.Generic type, Assigner assigner, Assigner.Typing typing) {
                        StackManipulation stackManipulation = assigner.assign(type, this.typeDescription.asGenericType(), typing);
                        if (!stackManipulation.isValid()) {
                            throw new IllegalStateException("Cannot assign " + type + " to " + this.typeDescription);
                        }
                        return new Resolved.Simple(new StackManipulation.Compound(access, stackManipulation), this.typeDescription);
                    }
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic$InvocationProvider$ArgumentProvider$ForBooleanConstant.class */
            public static class ForBooleanConstant implements ArgumentProvider {
                private final boolean value;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.value == ((ForBooleanConstant) obj).value;
                }

                public int hashCode() {
                    return (17 * 31) + (this.value ? 1 : 0);
                }

                protected ForBooleanConstant(boolean value) {
                    this.value = value;
                }

                @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider
                public Resolved resolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, Assigner assigner, Assigner.Typing typing) {
                    return new Resolved.Simple(IntegerConstant.forValue(this.value), TypeDescription.ForLoadedType.of(Boolean.TYPE));
                }

                @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider
                public InstrumentedType prepare(InstrumentedType instrumentedType) {
                    return instrumentedType;
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic$InvocationProvider$ArgumentProvider$ForByteConstant.class */
            public static class ForByteConstant implements ArgumentProvider {
                private final byte value;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.value == ((ForByteConstant) obj).value;
                }

                public int hashCode() {
                    return (17 * 31) + this.value;
                }

                protected ForByteConstant(byte value) {
                    this.value = value;
                }

                @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider
                public Resolved resolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, Assigner assigner, Assigner.Typing typing) {
                    return new Resolved.Simple(IntegerConstant.forValue(this.value), TypeDescription.ForLoadedType.of(Byte.TYPE));
                }

                @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider
                public InstrumentedType prepare(InstrumentedType instrumentedType) {
                    return instrumentedType;
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic$InvocationProvider$ArgumentProvider$ForShortConstant.class */
            public static class ForShortConstant implements ArgumentProvider {
                private final short value;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.value == ((ForShortConstant) obj).value;
                }

                public int hashCode() {
                    return (17 * 31) + this.value;
                }

                protected ForShortConstant(short value) {
                    this.value = value;
                }

                @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider
                public Resolved resolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, Assigner assigner, Assigner.Typing typing) {
                    return new Resolved.Simple(IntegerConstant.forValue(this.value), TypeDescription.ForLoadedType.of(Short.TYPE));
                }

                @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider
                public InstrumentedType prepare(InstrumentedType instrumentedType) {
                    return instrumentedType;
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic$InvocationProvider$ArgumentProvider$ForCharacterConstant.class */
            public static class ForCharacterConstant implements ArgumentProvider {
                private final char value;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.value == ((ForCharacterConstant) obj).value;
                }

                public int hashCode() {
                    return (17 * 31) + this.value;
                }

                protected ForCharacterConstant(char value) {
                    this.value = value;
                }

                @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider
                public Resolved resolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, Assigner assigner, Assigner.Typing typing) {
                    return new Resolved.Simple(IntegerConstant.forValue(this.value), TypeDescription.ForLoadedType.of(Character.TYPE));
                }

                @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider
                public InstrumentedType prepare(InstrumentedType instrumentedType) {
                    return instrumentedType;
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic$InvocationProvider$ArgumentProvider$ForIntegerConstant.class */
            public static class ForIntegerConstant implements ArgumentProvider {
                private final int value;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.value == ((ForIntegerConstant) obj).value;
                }

                public int hashCode() {
                    return (17 * 31) + this.value;
                }

                protected ForIntegerConstant(int value) {
                    this.value = value;
                }

                @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider
                public Resolved resolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, Assigner assigner, Assigner.Typing typing) {
                    return new Resolved.Simple(IntegerConstant.forValue(this.value), TypeDescription.ForLoadedType.of(Integer.TYPE));
                }

                @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider
                public InstrumentedType prepare(InstrumentedType instrumentedType) {
                    return instrumentedType;
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic$InvocationProvider$ArgumentProvider$ForLongConstant.class */
            public static class ForLongConstant implements ArgumentProvider {
                private final long value;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.value == ((ForLongConstant) obj).value;
                }

                public int hashCode() {
                    int i = 17 * 31;
                    return i + ((int) (i ^ (this.value >>> 32)));
                }

                protected ForLongConstant(long value) {
                    this.value = value;
                }

                @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider
                public Resolved resolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, Assigner assigner, Assigner.Typing typing) {
                    return new Resolved.Simple(LongConstant.forValue(this.value), TypeDescription.ForLoadedType.of(Long.TYPE));
                }

                @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider
                public InstrumentedType prepare(InstrumentedType instrumentedType) {
                    return instrumentedType;
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic$InvocationProvider$ArgumentProvider$ForFloatConstant.class */
            public static class ForFloatConstant implements ArgumentProvider {
                private final float value;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && Float.compare(this.value, ((ForFloatConstant) obj).value) == 0;
                }

                public int hashCode() {
                    return (17 * 31) + Float.floatToIntBits(this.value);
                }

                protected ForFloatConstant(float value) {
                    this.value = value;
                }

                @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider
                public Resolved resolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, Assigner assigner, Assigner.Typing typing) {
                    return new Resolved.Simple(FloatConstant.forValue(this.value), TypeDescription.ForLoadedType.of(Float.TYPE));
                }

                @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider
                public InstrumentedType prepare(InstrumentedType instrumentedType) {
                    return instrumentedType;
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic$InvocationProvider$ArgumentProvider$ForDoubleConstant.class */
            public static class ForDoubleConstant implements ArgumentProvider {
                private final double value;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && Double.compare(this.value, ((ForDoubleConstant) obj).value) == 0;
                }

                public int hashCode() {
                    int i = 17 * 31;
                    return i + ((int) (i ^ (Double.doubleToLongBits(this.value) >>> 32)));
                }

                protected ForDoubleConstant(double value) {
                    this.value = value;
                }

                @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider
                public Resolved resolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, Assigner assigner, Assigner.Typing typing) {
                    return new Resolved.Simple(DoubleConstant.forValue(this.value), TypeDescription.ForLoadedType.of(Double.TYPE));
                }

                @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider
                public InstrumentedType prepare(InstrumentedType instrumentedType) {
                    return instrumentedType;
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic$InvocationProvider$ArgumentProvider$ForStringConstant.class */
            public static class ForStringConstant implements ArgumentProvider {
                private final String value;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.value.equals(((ForStringConstant) obj).value);
                }

                public int hashCode() {
                    return (17 * 31) + this.value.hashCode();
                }

                protected ForStringConstant(String value) {
                    this.value = value;
                }

                @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider
                public Resolved resolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, Assigner assigner, Assigner.Typing typing) {
                    return new Resolved.Simple(new TextConstant(this.value), TypeDescription.STRING);
                }

                @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider
                public InstrumentedType prepare(InstrumentedType instrumentedType) {
                    return instrumentedType;
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic$InvocationProvider$ArgumentProvider$ForClassConstant.class */
            public static class ForClassConstant implements ArgumentProvider {
                private final TypeDescription typeDescription;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.typeDescription.equals(((ForClassConstant) obj).typeDescription);
                }

                public int hashCode() {
                    return (17 * 31) + this.typeDescription.hashCode();
                }

                protected ForClassConstant(TypeDescription typeDescription) {
                    this.typeDescription = typeDescription;
                }

                @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider
                public Resolved resolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, Assigner assigner, Assigner.Typing typing) {
                    return new Resolved.Simple(ClassConstant.of(this.typeDescription), TypeDescription.CLASS);
                }

                @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider
                public InstrumentedType prepare(InstrumentedType instrumentedType) {
                    return instrumentedType;
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic$InvocationProvider$ArgumentProvider$ForEnumerationValue.class */
            public static class ForEnumerationValue implements ArgumentProvider {
                private final EnumerationDescription enumerationDescription;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.enumerationDescription.equals(((ForEnumerationValue) obj).enumerationDescription);
                }

                public int hashCode() {
                    return (17 * 31) + this.enumerationDescription.hashCode();
                }

                protected ForEnumerationValue(EnumerationDescription enumerationDescription) {
                    this.enumerationDescription = enumerationDescription;
                }

                @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider
                public Resolved resolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, Assigner assigner, Assigner.Typing typing) {
                    return new Resolved.Simple(FieldAccess.forEnumeration(this.enumerationDescription), this.enumerationDescription.getEnumerationType());
                }

                @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider
                public InstrumentedType prepare(InstrumentedType instrumentedType) {
                    return instrumentedType;
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic$InvocationProvider$ArgumentProvider$ForNullValue.class */
            public static class ForNullValue implements ArgumentProvider {
                private final TypeDescription typeDescription;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.typeDescription.equals(((ForNullValue) obj).typeDescription);
                }

                public int hashCode() {
                    return (17 * 31) + this.typeDescription.hashCode();
                }

                protected ForNullValue(TypeDescription typeDescription) {
                    this.typeDescription = typeDescription;
                }

                @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider
                public Resolved resolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, Assigner assigner, Assigner.Typing typing) {
                    return new Resolved.Simple(NullConstant.INSTANCE, this.typeDescription);
                }

                @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider
                public InstrumentedType prepare(InstrumentedType instrumentedType) {
                    return instrumentedType;
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic$InvocationProvider$ArgumentProvider$ForJavaConstant.class */
            public static class ForJavaConstant implements ArgumentProvider {
                private final JavaConstant javaConstant;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.javaConstant.equals(((ForJavaConstant) obj).javaConstant);
                }

                public int hashCode() {
                    return (17 * 31) + this.javaConstant.hashCode();
                }

                protected ForJavaConstant(JavaConstant javaConstant) {
                    this.javaConstant = javaConstant;
                }

                @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider
                public Resolved resolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, Assigner assigner, Assigner.Typing typing) {
                    return new Resolved.Simple(new JavaConstantValue(this.javaConstant), this.javaConstant.getType());
                }

                @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider
                public InstrumentedType prepare(InstrumentedType instrumentedType) {
                    return instrumentedType;
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic$InvocationProvider$NameProvider.class */
        public interface NameProvider {
            String resolve(MethodDescription methodDescription);

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic$InvocationProvider$NameProvider$ForInterceptedMethod.class */
            public enum ForInterceptedMethod implements NameProvider {
                INSTANCE;

                @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.NameProvider
                public String resolve(MethodDescription methodDescription) {
                    return methodDescription.getInternalName();
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic$InvocationProvider$NameProvider$ForExplicitName.class */
            public static class ForExplicitName implements NameProvider {
                private final String internalName;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.internalName.equals(((ForExplicitName) obj).internalName);
                }

                public int hashCode() {
                    return (17 * 31) + this.internalName.hashCode();
                }

                protected ForExplicitName(String internalName) {
                    this.internalName = internalName;
                }

                @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.NameProvider
                public String resolve(MethodDescription methodDescription) {
                    return this.internalName;
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic$InvocationProvider$ReturnTypeProvider.class */
        public interface ReturnTypeProvider {
            TypeDescription resolve(MethodDescription methodDescription);

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic$InvocationProvider$ReturnTypeProvider$ForInterceptedMethod.class */
            public enum ForInterceptedMethod implements ReturnTypeProvider {
                INSTANCE;

                @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ReturnTypeProvider
                public TypeDescription resolve(MethodDescription methodDescription) {
                    return methodDescription.getReturnType().asErasure();
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic$InvocationProvider$ReturnTypeProvider$ForExplicitType.class */
            public static class ForExplicitType implements ReturnTypeProvider {
                private final TypeDescription typeDescription;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.typeDescription.equals(((ForExplicitType) obj).typeDescription);
                }

                public int hashCode() {
                    return (17 * 31) + this.typeDescription.hashCode();
                }

                protected ForExplicitType(TypeDescription typeDescription) {
                    this.typeDescription = typeDescription;
                }

                @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ReturnTypeProvider
                public TypeDescription resolve(MethodDescription methodDescription) {
                    return this.typeDescription;
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic$InvocationProvider$Default.class */
        public static class Default implements InvocationProvider {
            private final NameProvider nameProvider;
            private final ReturnTypeProvider returnTypeProvider;
            private final List<ArgumentProvider> argumentProviders;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.nameProvider.equals(((Default) obj).nameProvider) && this.returnTypeProvider.equals(((Default) obj).returnTypeProvider) && this.argumentProviders.equals(((Default) obj).argumentProviders);
            }

            public int hashCode() {
                return (((((17 * 31) + this.nameProvider.hashCode()) * 31) + this.returnTypeProvider.hashCode()) * 31) + this.argumentProviders.hashCode();
            }

            protected Default() {
                this(NameProvider.ForInterceptedMethod.INSTANCE, ReturnTypeProvider.ForInterceptedMethod.INSTANCE, Collections.singletonList(ArgumentProvider.ForInterceptedMethodInstanceAndParameters.INSTANCE));
            }

            protected Default(NameProvider nameProvider, ReturnTypeProvider returnTypeProvider, List<ArgumentProvider> argumentProviders) {
                this.nameProvider = nameProvider;
                this.returnTypeProvider = returnTypeProvider;
                this.argumentProviders = argumentProviders;
            }

            @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider
            public Target make(MethodDescription methodDescription) {
                return new Target(this.nameProvider.resolve(methodDescription), this.returnTypeProvider.resolve(methodDescription), this.argumentProviders, methodDescription);
            }

            @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider
            public InvocationProvider appendArguments(List<ArgumentProvider> argumentProviders) {
                return new Default(this.nameProvider, this.returnTypeProvider, CompoundList.of((List) this.argumentProviders, (List) argumentProviders));
            }

            @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider
            public InvocationProvider appendArgument(ArgumentProvider argumentProvider) {
                return new Default(this.nameProvider, this.returnTypeProvider, CompoundList.of(this.argumentProviders, argumentProvider));
            }

            @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider
            public InvocationProvider withoutArguments() {
                return new Default(this.nameProvider, this.returnTypeProvider, Collections.emptyList());
            }

            @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider
            public InvocationProvider withNameProvider(NameProvider nameProvider) {
                return new Default(nameProvider, this.returnTypeProvider, this.argumentProviders);
            }

            @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider
            public InvocationProvider withReturnTypeProvider(ReturnTypeProvider returnTypeProvider) {
                return new Default(this.nameProvider, returnTypeProvider, this.argumentProviders);
            }

            @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider
            public InstrumentedType prepare(InstrumentedType instrumentedType) {
                for (ArgumentProvider argumentProvider : this.argumentProviders) {
                    instrumentedType = argumentProvider.prepare(instrumentedType);
                }
                return instrumentedType;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic$InvocationProvider$Default$Target.class */
            public static class Target implements Target {
                private final String internalName;
                private final TypeDescription returnType;
                private final List<ArgumentProvider> argumentProviders;
                private final MethodDescription instrumentedMethod;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.internalName.equals(((Target) obj).internalName) && this.returnType.equals(((Target) obj).returnType) && this.argumentProviders.equals(((Target) obj).argumentProviders) && this.instrumentedMethod.equals(((Target) obj).instrumentedMethod);
                }

                public int hashCode() {
                    return (((((((17 * 31) + this.internalName.hashCode()) * 31) + this.returnType.hashCode()) * 31) + this.argumentProviders.hashCode()) * 31) + this.instrumentedMethod.hashCode();
                }

                protected Target(String internalName, TypeDescription returnType, List<ArgumentProvider> argumentProviders, MethodDescription instrumentedMethod) {
                    this.internalName = internalName;
                    this.returnType = returnType;
                    this.argumentProviders = argumentProviders;
                    this.instrumentedMethod = instrumentedMethod;
                }

                @Override // net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.Target
                public Target.Resolved resolve(TypeDescription instrumentedType, Assigner assigner, Assigner.Typing typing) {
                    StackManipulation[] stackManipulation = new StackManipulation[this.argumentProviders.size()];
                    List<TypeDescription> parameterTypes = new ArrayList<>();
                    int index = 0;
                    for (ArgumentProvider argumentProvider : this.argumentProviders) {
                        ArgumentProvider.Resolved resolved = argumentProvider.resolve(instrumentedType, this.instrumentedMethod, assigner, typing);
                        parameterTypes.addAll(resolved.getLoadedTypes());
                        int i = index;
                        index++;
                        stackManipulation[i] = resolved.getLoadInstruction();
                    }
                    return new Target.Resolved.Simple(new StackManipulation.Compound(stackManipulation), this.internalName, this.returnType, parameterTypes);
                }
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic$AbstractDelegator.class */
    protected static abstract class AbstractDelegator extends InvokeDynamic {
        protected abstract InvokeDynamic materialize();

        protected AbstractDelegator(MethodDescription.InDefinedShape bootstrap, List<?> arguments, InvocationProvider invocationProvider, TerminationHandler terminationHandler, Assigner assigner, Assigner.Typing typing) {
            super(bootstrap, arguments, invocationProvider, terminationHandler, assigner, typing);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic
        public InvokeDynamic withBooleanValue(boolean... value) {
            return materialize().withBooleanValue(value);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic
        public InvokeDynamic withByteValue(byte... value) {
            return materialize().withByteValue(value);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic
        public InvokeDynamic withShortValue(short... value) {
            return materialize().withShortValue(value);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic
        public InvokeDynamic withCharacterValue(char... value) {
            return materialize().withCharacterValue(value);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic
        public InvokeDynamic withIntegerValue(int... value) {
            return materialize().withIntegerValue(value);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic
        public InvokeDynamic withLongValue(long... value) {
            return materialize().withLongValue(value);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic
        public InvokeDynamic withFloatValue(float... value) {
            return materialize().withFloatValue(value);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic
        public InvokeDynamic withDoubleValue(double... value) {
            return materialize().withDoubleValue(value);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic
        public InvokeDynamic withValue(Object... value) {
            return materialize().withValue(value);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic
        public WithImplicitType withReference(Object value) {
            return materialize().withReference(value);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic
        public InvokeDynamic withReference(Object... value) {
            return materialize().withReference(value);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic
        public InvokeDynamic withType(TypeDescription... typeDescription) {
            return materialize().withType(typeDescription);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic
        public InvokeDynamic withInstance(JavaConstant... javaConstant) {
            return materialize().withInstance(javaConstant);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic
        public InvokeDynamic withNullValue(Class<?>... type) {
            return materialize().withNullValue(type);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic
        public InvokeDynamic withNullValue(TypeDescription... typeDescription) {
            return materialize().withNullValue(typeDescription);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic
        public InvokeDynamic withArgument(int... index) {
            return materialize().withArgument(index);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic
        public WithImplicitType withArgument(int index) {
            return materialize().withArgument(index);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic
        public InvokeDynamic withThis(Class<?>... type) {
            return materialize().withThis(type);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic
        public InvokeDynamic withThis(TypeDescription... typeDescription) {
            return materialize().withThis(typeDescription);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic
        public InvokeDynamic withMethodArguments() {
            return materialize().withMethodArguments();
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic
        public InvokeDynamic withImplicitAndMethodArguments() {
            return materialize().withImplicitAndMethodArguments();
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic
        public InvokeDynamic withField(String... fieldName) {
            return materialize().withField(fieldName);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic
        public InvokeDynamic withEnumeration(EnumerationDescription... enumerationDescription) {
            return materialize().withEnumeration(enumerationDescription);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic
        public InvokeDynamic withField(FieldLocator.Factory fieldLocatorFactory, String... name) {
            return materialize().withField(fieldLocatorFactory, name);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic
        public WithImplicitType withField(String name) {
            return materialize().withField(name);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic
        public WithImplicitType withField(String name, FieldLocator.Factory fieldLocatorFactory) {
            return materialize().withField(name, fieldLocatorFactory);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic
        public Implementation.Composable withAssigner(Assigner assigner, Assigner.Typing typing) {
            return materialize().withAssigner(assigner, typing);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic, net.bytebuddy.implementation.Implementation.Composable
        public Implementation andThen(Implementation implementation) {
            return materialize().andThen(implementation);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic, net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
        public InstrumentedType prepare(InstrumentedType instrumentedType) {
            return materialize().prepare(instrumentedType);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic, net.bytebuddy.implementation.Implementation
        public ByteCodeAppender appender(Implementation.Target implementationTarget) {
            return materialize().appender(implementationTarget);
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic$WithImplicitArguments.class */
    public static class WithImplicitArguments extends AbstractDelegator {
        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic, net.bytebuddy.implementation.Implementation
        public /* bridge */ /* synthetic */ ByteCodeAppender appender(Implementation.Target target) {
            return super.appender(target);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic, net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
        public /* bridge */ /* synthetic */ InstrumentedType prepare(InstrumentedType instrumentedType) {
            return super.prepare(instrumentedType);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic, net.bytebuddy.implementation.Implementation.Composable
        public /* bridge */ /* synthetic */ Implementation andThen(Implementation implementation) {
            return super.andThen(implementation);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ WithImplicitType withField(String str, FieldLocator.Factory factory) {
            return super.withField(str, factory);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ WithImplicitType withField(String str) {
            return super.withField(str);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ InvokeDynamic withField(FieldLocator.Factory factory, String[] strArr) {
            return super.withField(factory, strArr);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ InvokeDynamic withEnumeration(EnumerationDescription[] enumerationDescriptionArr) {
            return super.withEnumeration(enumerationDescriptionArr);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ InvokeDynamic withField(String[] strArr) {
            return super.withField(strArr);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ InvokeDynamic withImplicitAndMethodArguments() {
            return super.withImplicitAndMethodArguments();
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ InvokeDynamic withMethodArguments() {
            return super.withMethodArguments();
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ InvokeDynamic withThis(TypeDescription[] typeDescriptionArr) {
            return super.withThis(typeDescriptionArr);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ InvokeDynamic withThis(Class[] clsArr) {
            return super.withThis(clsArr);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ WithImplicitType withArgument(int i) {
            return super.withArgument(i);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ InvokeDynamic withArgument(int[] iArr) {
            return super.withArgument(iArr);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ InvokeDynamic withNullValue(TypeDescription[] typeDescriptionArr) {
            return super.withNullValue(typeDescriptionArr);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ InvokeDynamic withNullValue(Class[] clsArr) {
            return super.withNullValue(clsArr);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ InvokeDynamic withInstance(JavaConstant[] javaConstantArr) {
            return super.withInstance(javaConstantArr);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ InvokeDynamic withType(TypeDescription[] typeDescriptionArr) {
            return super.withType(typeDescriptionArr);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ InvokeDynamic withReference(Object[] objArr) {
            return super.withReference(objArr);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ WithImplicitType withReference(Object obj) {
            return super.withReference(obj);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ InvokeDynamic withValue(Object[] objArr) {
            return super.withValue(objArr);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ InvokeDynamic withDoubleValue(double[] dArr) {
            return super.withDoubleValue(dArr);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ InvokeDynamic withFloatValue(float[] fArr) {
            return super.withFloatValue(fArr);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ InvokeDynamic withLongValue(long[] jArr) {
            return super.withLongValue(jArr);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ InvokeDynamic withIntegerValue(int[] iArr) {
            return super.withIntegerValue(iArr);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ InvokeDynamic withCharacterValue(char[] cArr) {
            return super.withCharacterValue(cArr);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ InvokeDynamic withShortValue(short[] sArr) {
            return super.withShortValue(sArr);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ InvokeDynamic withByteValue(byte[] bArr) {
            return super.withByteValue(bArr);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ InvokeDynamic withBooleanValue(boolean[] zArr) {
            return super.withBooleanValue(zArr);
        }

        protected WithImplicitArguments(MethodDescription.InDefinedShape bootstrap, List<?> arguments, InvocationProvider invocationProvider, TerminationHandler terminationHandler, Assigner assigner, Assigner.Typing typing) {
            super(bootstrap, arguments, invocationProvider, terminationHandler, assigner, typing);
        }

        public InvokeDynamic withoutArguments() {
            return new InvokeDynamic(this.bootstrap, this.arguments, this.invocationProvider.withoutArguments(), this.terminationHandler, this.assigner, this.typing);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator
        protected InvokeDynamic materialize() {
            return withoutArguments();
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public WithImplicitArguments withAssigner(Assigner assigner, Assigner.Typing typing) {
            return new WithImplicitArguments(this.bootstrap, this.arguments, this.invocationProvider, this.terminationHandler, assigner, typing);
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic$WithImplicitTarget.class */
    public static class WithImplicitTarget extends WithImplicitArguments {
        protected WithImplicitTarget(MethodDescription.InDefinedShape bootstrap, List<?> arguments, InvocationProvider invocationProvider, TerminationHandler terminationHandler, Assigner assigner, Assigner.Typing typing) {
            super(bootstrap, arguments, invocationProvider, terminationHandler, assigner, typing);
        }

        public WithImplicitArguments invoke(Class<?> returnType) {
            return invoke(TypeDescription.ForLoadedType.of(returnType));
        }

        public WithImplicitArguments invoke(TypeDescription returnType) {
            return new WithImplicitArguments(this.bootstrap, this.arguments, this.invocationProvider.withReturnTypeProvider(new InvocationProvider.ReturnTypeProvider.ForExplicitType(returnType)), this.terminationHandler, this.assigner, this.typing);
        }

        public WithImplicitArguments invoke(String methodName) {
            return new WithImplicitArguments(this.bootstrap, this.arguments, this.invocationProvider.withNameProvider(new InvocationProvider.NameProvider.ForExplicitName(methodName)), this.terminationHandler, this.assigner, this.typing);
        }

        public WithImplicitArguments invoke(String methodName, Class<?> returnType) {
            return invoke(methodName, TypeDescription.ForLoadedType.of(returnType));
        }

        public WithImplicitArguments invoke(String methodName, TypeDescription returnType) {
            return new WithImplicitArguments(this.bootstrap, this.arguments, this.invocationProvider.withNameProvider(new InvocationProvider.NameProvider.ForExplicitName(methodName)).withReturnTypeProvider(new InvocationProvider.ReturnTypeProvider.ForExplicitType(returnType)), this.terminationHandler, this.assigner, this.typing);
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic$WithImplicitType.class */
    public static abstract class WithImplicitType extends AbstractDelegator {
        public abstract InvokeDynamic as(TypeDescription typeDescription);

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic, net.bytebuddy.implementation.Implementation
        public /* bridge */ /* synthetic */ ByteCodeAppender appender(Implementation.Target target) {
            return super.appender(target);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic, net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
        public /* bridge */ /* synthetic */ InstrumentedType prepare(InstrumentedType instrumentedType) {
            return super.prepare(instrumentedType);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic, net.bytebuddy.implementation.Implementation.Composable
        public /* bridge */ /* synthetic */ Implementation andThen(Implementation implementation) {
            return super.andThen(implementation);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ Implementation.Composable withAssigner(Assigner assigner, Assigner.Typing typing) {
            return super.withAssigner(assigner, typing);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ WithImplicitType withField(String str, FieldLocator.Factory factory) {
            return super.withField(str, factory);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ WithImplicitType withField(String str) {
            return super.withField(str);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ InvokeDynamic withField(FieldLocator.Factory factory, String[] strArr) {
            return super.withField(factory, strArr);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ InvokeDynamic withEnumeration(EnumerationDescription[] enumerationDescriptionArr) {
            return super.withEnumeration(enumerationDescriptionArr);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ InvokeDynamic withField(String[] strArr) {
            return super.withField(strArr);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ InvokeDynamic withImplicitAndMethodArguments() {
            return super.withImplicitAndMethodArguments();
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ InvokeDynamic withMethodArguments() {
            return super.withMethodArguments();
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ InvokeDynamic withThis(TypeDescription[] typeDescriptionArr) {
            return super.withThis(typeDescriptionArr);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ InvokeDynamic withThis(Class[] clsArr) {
            return super.withThis(clsArr);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ WithImplicitType withArgument(int i) {
            return super.withArgument(i);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ InvokeDynamic withArgument(int[] iArr) {
            return super.withArgument(iArr);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ InvokeDynamic withNullValue(TypeDescription[] typeDescriptionArr) {
            return super.withNullValue(typeDescriptionArr);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ InvokeDynamic withNullValue(Class[] clsArr) {
            return super.withNullValue(clsArr);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ InvokeDynamic withInstance(JavaConstant[] javaConstantArr) {
            return super.withInstance(javaConstantArr);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ InvokeDynamic withType(TypeDescription[] typeDescriptionArr) {
            return super.withType(typeDescriptionArr);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ InvokeDynamic withReference(Object[] objArr) {
            return super.withReference(objArr);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ WithImplicitType withReference(Object obj) {
            return super.withReference(obj);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ InvokeDynamic withValue(Object[] objArr) {
            return super.withValue(objArr);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ InvokeDynamic withDoubleValue(double[] dArr) {
            return super.withDoubleValue(dArr);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ InvokeDynamic withFloatValue(float[] fArr) {
            return super.withFloatValue(fArr);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ InvokeDynamic withLongValue(long[] jArr) {
            return super.withLongValue(jArr);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ InvokeDynamic withIntegerValue(int[] iArr) {
            return super.withIntegerValue(iArr);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ InvokeDynamic withCharacterValue(char[] cArr) {
            return super.withCharacterValue(cArr);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ InvokeDynamic withShortValue(short[] sArr) {
            return super.withShortValue(sArr);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ InvokeDynamic withByteValue(byte[] bArr) {
            return super.withByteValue(bArr);
        }

        @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator, net.bytebuddy.implementation.InvokeDynamic
        public /* bridge */ /* synthetic */ InvokeDynamic withBooleanValue(boolean[] zArr) {
            return super.withBooleanValue(zArr);
        }

        protected WithImplicitType(MethodDescription.InDefinedShape bootstrap, List<?> arguments, InvocationProvider invocationProvider, TerminationHandler terminationHandler, Assigner assigner, Assigner.Typing typing) {
            super(bootstrap, arguments, invocationProvider, terminationHandler, assigner, typing);
        }

        public InvokeDynamic as(Class<?> type) {
            return as(TypeDescription.ForLoadedType.of(type));
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @SuppressFBWarnings(value = {"EQ_DOESNT_OVERRIDE_EQUALS"}, justification = "Super type implementation covers use case")
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic$WithImplicitType$OfInstance.class */
        public static class OfInstance extends WithImplicitType {
            private final Object value;
            private final InvocationProvider.ArgumentProvider argumentProvider;

            protected OfInstance(MethodDescription.InDefinedShape bootstrap, List<?> arguments, InvocationProvider invocationProvider, TerminationHandler terminationHandler, Assigner assigner, Assigner.Typing typing, Object value) {
                super(bootstrap, arguments, invocationProvider, terminationHandler, assigner, typing);
                this.value = value;
                this.argumentProvider = InvocationProvider.ArgumentProvider.ForInstance.of(value);
            }

            @Override // net.bytebuddy.implementation.InvokeDynamic.WithImplicitType
            public InvokeDynamic as(TypeDescription typeDescription) {
                if (!typeDescription.asBoxed().isInstance(this.value)) {
                    throw new IllegalArgumentException(this.value + " is not of type " + typeDescription);
                }
                return new InvokeDynamic(this.bootstrap, this.arguments, this.invocationProvider.appendArgument(new InvocationProvider.ArgumentProvider.ForInstance(this.value, typeDescription)), this.terminationHandler, this.assigner, this.typing);
            }

            @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator
            protected InvokeDynamic materialize() {
                return new InvokeDynamic(this.bootstrap, this.arguments, this.invocationProvider.appendArgument(this.argumentProvider), this.terminationHandler, this.assigner, this.typing);
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @SuppressFBWarnings(value = {"EQ_DOESNT_OVERRIDE_EQUALS"}, justification = "Super type implementation covers use case")
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic$WithImplicitType$OfArgument.class */
        public static class OfArgument extends WithImplicitType {
            private final int index;

            protected OfArgument(MethodDescription.InDefinedShape bootstrap, List<?> arguments, InvocationProvider invocationProvider, TerminationHandler terminationHandler, Assigner assigner, Assigner.Typing typing, int index) {
                super(bootstrap, arguments, invocationProvider, terminationHandler, assigner, typing);
                this.index = index;
            }

            @Override // net.bytebuddy.implementation.InvokeDynamic.WithImplicitType
            public InvokeDynamic as(TypeDescription typeDescription) {
                return new InvokeDynamic(this.bootstrap, this.arguments, this.invocationProvider.appendArgument(new InvocationProvider.ArgumentProvider.ForMethodParameter.WithExplicitType(this.index, typeDescription)), this.terminationHandler, this.assigner, this.typing);
            }

            @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator
            protected InvokeDynamic materialize() {
                return new InvokeDynamic(this.bootstrap, this.arguments, this.invocationProvider.appendArgument(new InvocationProvider.ArgumentProvider.ForMethodParameter(this.index)), this.terminationHandler, this.assigner, this.typing);
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @SuppressFBWarnings(value = {"EQ_DOESNT_OVERRIDE_EQUALS"}, justification = "Super type implementation covers use case")
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic$WithImplicitType$OfField.class */
        public static class OfField extends WithImplicitType {
            private final String fieldName;
            private final FieldLocator.Factory fieldLocatorFactory;

            protected OfField(MethodDescription.InDefinedShape bootstrap, List<?> arguments, InvocationProvider invocationProvider, TerminationHandler terminationHandler, Assigner assigner, Assigner.Typing typing, String fieldName, FieldLocator.Factory fieldLocatorFactory) {
                super(bootstrap, arguments, invocationProvider, terminationHandler, assigner, typing);
                this.fieldName = fieldName;
                this.fieldLocatorFactory = fieldLocatorFactory;
            }

            @Override // net.bytebuddy.implementation.InvokeDynamic.WithImplicitType
            public InvokeDynamic as(TypeDescription typeDescription) {
                return new InvokeDynamic(this.bootstrap, this.arguments, this.invocationProvider.appendArgument(new InvocationProvider.ArgumentProvider.ForField.WithExplicitType(this.fieldName, this.fieldLocatorFactory, typeDescription)), this.terminationHandler, this.assigner, this.typing);
            }

            @Override // net.bytebuddy.implementation.InvokeDynamic.AbstractDelegator
            protected InvokeDynamic materialize() {
                return new InvokeDynamic(this.bootstrap, this.arguments, this.invocationProvider.appendArgument(new InvocationProvider.ArgumentProvider.ForField(this.fieldName, this.fieldLocatorFactory)), this.terminationHandler, this.assigner, this.typing);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/InvokeDynamic$Appender.class */
    public class Appender implements ByteCodeAppender {
        private final TypeDescription instrumentedType;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.instrumentedType.equals(((Appender) obj).instrumentedType) && InvokeDynamic.this.equals(InvokeDynamic.this);
        }

        public int hashCode() {
            return (((17 * 31) + this.instrumentedType.hashCode()) * 31) + InvokeDynamic.this.hashCode();
        }

        public Appender(TypeDescription instrumentedType) {
            this.instrumentedType = instrumentedType;
        }

        @Override // net.bytebuddy.implementation.bytecode.ByteCodeAppender
        public ByteCodeAppender.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext, MethodDescription instrumentedMethod) {
            InvocationProvider.Target.Resolved target = InvokeDynamic.this.invocationProvider.make(instrumentedMethod).resolve(this.instrumentedType, InvokeDynamic.this.assigner, InvokeDynamic.this.typing);
            StackManipulation.Size size = new StackManipulation.Compound(target.getStackManipulation(), MethodInvocation.invoke(InvokeDynamic.this.bootstrap).dynamic(target.getInternalName(), target.getReturnType(), target.getParameterTypes(), InvokeDynamic.this.arguments), InvokeDynamic.this.terminationHandler.resolve(instrumentedMethod, target.getReturnType(), InvokeDynamic.this.assigner, InvokeDynamic.this.typing)).apply(methodVisitor, implementationContext);
            return new ByteCodeAppender.Size(size.getMaximalSize(), instrumentedMethod.getStackSize());
        }
    }
}
