package net.bytebuddy.asm;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.IOException;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.enumeration.EnumerationDescription;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.MethodList;
import net.bytebuddy.description.method.ParameterDescription;
import net.bytebuddy.description.method.ParameterList;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.TargetType;
import net.bytebuddy.dynamic.scaffold.FieldLocator;
import net.bytebuddy.dynamic.scaffold.InstrumentedType;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.implementation.bytecode.Addition;
import net.bytebuddy.implementation.bytecode.ByteCodeAppender;
import net.bytebuddy.implementation.bytecode.Duplication;
import net.bytebuddy.implementation.bytecode.Removal;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.implementation.bytecode.StackSize;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
import net.bytebuddy.implementation.bytecode.collection.ArrayAccess;
import net.bytebuddy.implementation.bytecode.collection.ArrayFactory;
import net.bytebuddy.implementation.bytecode.constant.ClassConstant;
import net.bytebuddy.implementation.bytecode.constant.DefaultValue;
import net.bytebuddy.implementation.bytecode.constant.DoubleConstant;
import net.bytebuddy.implementation.bytecode.constant.FloatConstant;
import net.bytebuddy.implementation.bytecode.constant.IntegerConstant;
import net.bytebuddy.implementation.bytecode.constant.JavaConstantValue;
import net.bytebuddy.implementation.bytecode.constant.LongConstant;
import net.bytebuddy.implementation.bytecode.constant.MethodConstant;
import net.bytebuddy.implementation.bytecode.constant.NullConstant;
import net.bytebuddy.implementation.bytecode.constant.SerializedConstant;
import net.bytebuddy.implementation.bytecode.constant.TextConstant;
import net.bytebuddy.implementation.bytecode.member.FieldAccess;
import net.bytebuddy.implementation.bytecode.member.MethodInvocation;
import net.bytebuddy.implementation.bytecode.member.MethodVariableAccess;
import net.bytebuddy.jar.asm.AnnotationVisitor;
import net.bytebuddy.jar.asm.Attribute;
import net.bytebuddy.jar.asm.ClassReader;
import net.bytebuddy.jar.asm.ClassVisitor;
import net.bytebuddy.jar.asm.Handle;
import net.bytebuddy.jar.asm.Label;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.jar.asm.Opcodes;
import net.bytebuddy.jar.asm.Type;
import net.bytebuddy.jar.asm.TypePath;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.pool.TypePool;
import net.bytebuddy.utility.CompoundList;
import net.bytebuddy.utility.JavaConstant;
import net.bytebuddy.utility.JavaType;
import net.bytebuddy.utility.OpenedClassReader;
import net.bytebuddy.utility.visitor.ExceptionTableSensitiveMethodVisitor;
import net.bytebuddy.utility.visitor.FramePaddingMethodVisitor;
import net.bytebuddy.utility.visitor.LineNumberPrependingMethodVisitor;
import net.bytebuddy.utility.visitor.StackAwareMethodVisitor;
@HashCodeAndEqualsPlugin.Enhance
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice.class */
public class Advice implements AsmVisitorWrapper.ForDeclaredMethods.MethodVisitorWrapper, Implementation {
    private static final ClassReader UNDEFINED = null;
    private static final MethodDescription.InDefinedShape SKIP_ON;
    private static final MethodDescription.InDefinedShape PREPEND_LINE_NUMBER;
    private static final MethodDescription.InDefinedShape INLINE_ENTER;
    private static final MethodDescription.InDefinedShape SUPPRESS_ENTER;
    private static final MethodDescription.InDefinedShape REPEAT_ON;
    private static final MethodDescription.InDefinedShape ON_THROWABLE;
    private static final MethodDescription.InDefinedShape BACKUP_ARGUMENTS;
    private static final MethodDescription.InDefinedShape INLINE_EXIT;
    private static final MethodDescription.InDefinedShape SUPPRESS_EXIT;
    private final Dispatcher.Resolved.ForMethodEnter methodEnter;
    private final Dispatcher.Resolved.ForMethodExit methodExit;
    private final Assigner assigner;
    private final ExceptionHandler exceptionHandler;
    private final Implementation delegate;

    @Target({ElementType.PARAMETER})
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$AllArguments.class */
    public @interface AllArguments {
        boolean readOnly() default true;

        Assigner.Typing typing() default Assigner.Typing.STATIC;
    }

    @Target({ElementType.PARAMETER})
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Argument.class */
    public @interface Argument {
        int value();

        boolean readOnly() default true;

        Assigner.Typing typing() default Assigner.Typing.STATIC;

        boolean optional() default false;
    }

    @Target({ElementType.PARAMETER})
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Enter.class */
    public @interface Enter {
        boolean readOnly() default true;

        Assigner.Typing typing() default Assigner.Typing.STATIC;
    }

    @Target({ElementType.PARAMETER})
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Exit.class */
    public @interface Exit {
        boolean readOnly() default true;

        Assigner.Typing typing() default Assigner.Typing.STATIC;
    }

    @Target({ElementType.PARAMETER})
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$FieldValue.class */
    public @interface FieldValue {
        String value();

        Class<?> declaringType() default void.class;

        boolean readOnly() default true;

        Assigner.Typing typing() default Assigner.Typing.STATIC;
    }

    @Target({ElementType.PARAMETER})
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Local.class */
    public @interface Local {
        String value();
    }

    @Target({ElementType.METHOD})
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OnMethodEnter.class */
    public @interface OnMethodEnter {
        Class<?> skipOn() default void.class;

        boolean prependLineNumber() default true;

        boolean inline() default true;

        Class<? extends Throwable> suppress() default NoExceptionHandler.class;
    }

    @Target({ElementType.METHOD})
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OnMethodExit.class */
    public @interface OnMethodExit {
        Class<?> repeatOn() default void.class;

        Class<? extends Throwable> onThrowable() default NoExceptionHandler.class;

        boolean backupArguments() default true;

        boolean inline() default true;

        Class<? extends Throwable> suppress() default NoExceptionHandler.class;
    }

    @Target({ElementType.PARAMETER})
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Origin.class */
    public @interface Origin {
        public static final String DEFAULT = "";

        String value() default "";
    }

    @Target({ElementType.PARAMETER})
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Return.class */
    public @interface Return {
        boolean readOnly() default true;

        Assigner.Typing typing() default Assigner.Typing.STATIC;
    }

    @Target({ElementType.PARAMETER})
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$StubValue.class */
    public @interface StubValue {
    }

    @Target({ElementType.PARAMETER})
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$This.class */
    public @interface This {
        boolean optional() default false;

        boolean readOnly() default true;

        Assigner.Typing typing() default Assigner.Typing.STATIC;
    }

    @Target({ElementType.PARAMETER})
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Thrown.class */
    public @interface Thrown {
        boolean readOnly() default true;

        Assigner.Typing typing() default Assigner.Typing.DYNAMIC;
    }

    @Target({ElementType.PARAMETER})
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Unused.class */
    public @interface Unused {
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass() && this.methodEnter.equals(((Advice) obj).methodEnter) && this.methodExit.equals(((Advice) obj).methodExit) && this.assigner.equals(((Advice) obj).assigner) && this.exceptionHandler.equals(((Advice) obj).exceptionHandler) && this.delegate.equals(((Advice) obj).delegate);
    }

    public int hashCode() {
        return (((((((((17 * 31) + this.methodEnter.hashCode()) * 31) + this.methodExit.hashCode()) * 31) + this.assigner.hashCode()) * 31) + this.exceptionHandler.hashCode()) * 31) + this.delegate.hashCode();
    }

    static {
        MethodList<MethodDescription.InDefinedShape> enter = TypeDescription.ForLoadedType.of(OnMethodEnter.class).getDeclaredMethods();
        SKIP_ON = (MethodDescription.InDefinedShape) enter.filter(ElementMatchers.named("skipOn")).getOnly();
        PREPEND_LINE_NUMBER = (MethodDescription.InDefinedShape) enter.filter(ElementMatchers.named("prependLineNumber")).getOnly();
        INLINE_ENTER = (MethodDescription.InDefinedShape) enter.filter(ElementMatchers.named("inline")).getOnly();
        SUPPRESS_ENTER = (MethodDescription.InDefinedShape) enter.filter(ElementMatchers.named("suppress")).getOnly();
        MethodList<MethodDescription.InDefinedShape> exit = TypeDescription.ForLoadedType.of(OnMethodExit.class).getDeclaredMethods();
        REPEAT_ON = (MethodDescription.InDefinedShape) exit.filter(ElementMatchers.named("repeatOn")).getOnly();
        ON_THROWABLE = (MethodDescription.InDefinedShape) exit.filter(ElementMatchers.named("onThrowable")).getOnly();
        BACKUP_ARGUMENTS = (MethodDescription.InDefinedShape) exit.filter(ElementMatchers.named("backupArguments")).getOnly();
        INLINE_EXIT = (MethodDescription.InDefinedShape) exit.filter(ElementMatchers.named("inline")).getOnly();
        SUPPRESS_EXIT = (MethodDescription.InDefinedShape) exit.filter(ElementMatchers.named("suppress")).getOnly();
    }

    protected Advice(Dispatcher.Resolved.ForMethodEnter methodEnter, Dispatcher.Resolved.ForMethodExit methodExit) {
        this(methodEnter, methodExit, Assigner.DEFAULT, ExceptionHandler.Default.SUPPRESSING, SuperMethodCall.INSTANCE);
    }

    private Advice(Dispatcher.Resolved.ForMethodEnter methodEnter, Dispatcher.Resolved.ForMethodExit methodExit, Assigner assigner, ExceptionHandler exceptionHandler, Implementation delegate) {
        this.methodEnter = methodEnter;
        this.methodExit = methodExit;
        this.assigner = assigner;
        this.exceptionHandler = exceptionHandler;
        this.delegate = delegate;
    }

    public static Advice to(Class<?> advice) {
        return to(advice, ClassFileLocator.ForClassLoader.of(advice.getClassLoader()));
    }

    public static Advice to(Class<?> advice, ClassFileLocator classFileLocator) {
        return to(TypeDescription.ForLoadedType.of(advice), classFileLocator);
    }

    public static Advice to(TypeDescription advice) {
        return to(advice, ClassFileLocator.NoOp.INSTANCE);
    }

    public static Advice to(TypeDescription advice, ClassFileLocator classFileLocator) {
        return to(advice, PostProcessor.NoOp.INSTANCE, classFileLocator, Collections.emptyList(), Delegator.ForStaticInvocation.INSTANCE);
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected static Advice to(TypeDescription advice, PostProcessor.Factory postProcessorFactory, ClassFileLocator classFileLocator, List<? extends OffsetMapping.Factory<?>> userFactories, Delegator delegator) {
        Dispatcher.Unresolved methodEnter = Dispatcher.Inactive.INSTANCE;
        Dispatcher.Unresolved methodExit = Dispatcher.Inactive.INSTANCE;
        for (MethodDescription.InDefinedShape methodDescription : advice.getDeclaredMethods()) {
            methodEnter = locate(OnMethodEnter.class, INLINE_ENTER, methodEnter, methodDescription, delegator);
            methodExit = locate(OnMethodExit.class, INLINE_EXIT, methodExit, methodDescription, delegator);
        }
        if (!methodEnter.isAlive() && !methodExit.isAlive()) {
            throw new IllegalArgumentException("No advice defined by " + advice);
        }
        try {
            ClassReader classReader = (methodEnter.isBinary() || methodExit.isBinary()) ? OpenedClassReader.of(classFileLocator.locate(advice.getName()).resolve()) : UNDEFINED;
            return new Advice(methodEnter.asMethodEnter(userFactories, classReader, methodExit, postProcessorFactory), methodExit.asMethodExit(userFactories, classReader, methodEnter, postProcessorFactory));
        } catch (IOException exception) {
            throw new IllegalStateException("Error reading class file of " + advice, exception);
        }
    }

    public static Advice to(Class<?> enterAdvice, Class<?> exitAdvice) {
        ClassLoader enterLoader = enterAdvice.getClassLoader();
        ClassLoader exitLoader = exitAdvice.getClassLoader();
        return to(enterAdvice, exitAdvice, enterLoader == exitLoader ? ClassFileLocator.ForClassLoader.of(enterLoader) : new ClassFileLocator.Compound(ClassFileLocator.ForClassLoader.of(enterLoader), ClassFileLocator.ForClassLoader.of(exitLoader)));
    }

    public static Advice to(Class<?> enterAdvice, Class<?> exitAdvice, ClassFileLocator classFileLocator) {
        return to(TypeDescription.ForLoadedType.of(enterAdvice), TypeDescription.ForLoadedType.of(exitAdvice), classFileLocator);
    }

    public static Advice to(TypeDescription enterAdvice, TypeDescription exitAdvice) {
        return to(enterAdvice, exitAdvice, ClassFileLocator.NoOp.INSTANCE);
    }

    public static Advice to(TypeDescription enterAdvice, TypeDescription exitAdvice, ClassFileLocator classFileLocator) {
        return to(enterAdvice, exitAdvice, PostProcessor.NoOp.INSTANCE, classFileLocator, Collections.emptyList(), Delegator.ForStaticInvocation.INSTANCE);
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected static Advice to(TypeDescription enterAdvice, TypeDescription exitAdvice, PostProcessor.Factory postProcessorFactory, ClassFileLocator classFileLocator, List<? extends OffsetMapping.Factory<?>> userFactories, Delegator delegator) {
        Dispatcher.Unresolved methodEnter = Dispatcher.Inactive.INSTANCE;
        Dispatcher.Unresolved methodExit = Dispatcher.Inactive.INSTANCE;
        for (MethodDescription.InDefinedShape methodDescription : enterAdvice.getDeclaredMethods()) {
            methodEnter = locate(OnMethodEnter.class, INLINE_ENTER, methodEnter, methodDescription, delegator);
        }
        if (!methodEnter.isAlive()) {
            throw new IllegalArgumentException("No enter advice defined by " + enterAdvice);
        }
        for (MethodDescription.InDefinedShape methodDescription2 : exitAdvice.getDeclaredMethods()) {
            methodExit = locate(OnMethodExit.class, INLINE_EXIT, methodExit, methodDescription2, delegator);
        }
        if (!methodExit.isAlive()) {
            throw new IllegalArgumentException("No exit advice defined by " + exitAdvice);
        }
        try {
            return new Advice(methodEnter.asMethodEnter(userFactories, methodEnter.isBinary() ? OpenedClassReader.of(classFileLocator.locate(enterAdvice.getName()).resolve()) : UNDEFINED, methodExit, postProcessorFactory), methodExit.asMethodExit(userFactories, methodExit.isBinary() ? OpenedClassReader.of(classFileLocator.locate(exitAdvice.getName()).resolve()) : UNDEFINED, methodEnter, postProcessorFactory));
        } catch (IOException exception) {
            throw new IllegalStateException("Error reading class file of " + enterAdvice + " or " + exitAdvice, exception);
        }
    }

    private static Dispatcher.Unresolved locate(Class<? extends Annotation> type, MethodDescription.InDefinedShape property, Dispatcher.Unresolved dispatcher, MethodDescription.InDefinedShape methodDescription, Delegator delegator) {
        AnnotationDescription annotation = methodDescription.getDeclaredAnnotations().ofType(type);
        if (annotation == null) {
            return dispatcher;
        }
        if (dispatcher.isAlive()) {
            throw new IllegalStateException("Duplicate advice for " + dispatcher + " and " + methodDescription);
        }
        if (methodDescription.isStatic()) {
            return ((Boolean) annotation.getValue(property).resolve(Boolean.class)).booleanValue() ? new Dispatcher.Inlining(methodDescription) : new Dispatcher.Delegating(methodDescription, delegator);
        }
        throw new IllegalStateException("Advice for " + methodDescription + " is not static");
    }

    public static WithCustomMapping withCustomMapping() {
        return new WithCustomMapping();
    }

    public AsmVisitorWrapper.ForDeclaredMethods on(ElementMatcher<? super MethodDescription> matcher) {
        return new AsmVisitorWrapper.ForDeclaredMethods().invokable(matcher, this);
    }

    @Override // net.bytebuddy.asm.AsmVisitorWrapper.ForDeclaredMethods.MethodVisitorWrapper
    public MethodVisitor wrap(TypeDescription instrumentedType, MethodDescription instrumentedMethod, MethodVisitor methodVisitor, Implementation.Context implementationContext, TypePool typePool, int writerFlags, int readerFlags) {
        return (instrumentedMethod.isAbstract() || instrumentedMethod.isNative()) ? methodVisitor : doWrap(instrumentedType, instrumentedMethod, methodVisitor, implementationContext, writerFlags, readerFlags);
    }

    protected MethodVisitor doWrap(TypeDescription instrumentedType, MethodDescription instrumentedMethod, MethodVisitor methodVisitor, Implementation.Context implementationContext, int writerFlags, int readerFlags) {
        MethodVisitor methodVisitor2 = new FramePaddingMethodVisitor(this.methodEnter.isPrependLineNumber() ? new LineNumberPrependingMethodVisitor(methodVisitor) : methodVisitor);
        if (!this.methodExit.isAlive()) {
            return new AdviceVisitor.WithoutExitAdvice(methodVisitor2, implementationContext, this.assigner, this.exceptionHandler.resolve(instrumentedMethod, instrumentedType), instrumentedType, instrumentedMethod, this.methodEnter, writerFlags, readerFlags);
        }
        if (this.methodExit.getThrowable().represents(NoExceptionHandler.class)) {
            return new AdviceVisitor.WithExitAdvice.WithoutExceptionHandling(methodVisitor2, implementationContext, this.assigner, this.exceptionHandler.resolve(instrumentedMethod, instrumentedType), instrumentedType, instrumentedMethod, this.methodEnter, this.methodExit, writerFlags, readerFlags);
        }
        if (instrumentedMethod.isConstructor()) {
            throw new IllegalStateException("Cannot catch exception during constructor call for " + instrumentedMethod);
        }
        return new AdviceVisitor.WithExitAdvice.WithExceptionHandling(methodVisitor2, implementationContext, this.assigner, this.exceptionHandler.resolve(instrumentedMethod, instrumentedType), instrumentedType, instrumentedMethod, this.methodEnter, this.methodExit, writerFlags, readerFlags, this.methodExit.getThrowable());
    }

    @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
    public InstrumentedType prepare(InstrumentedType instrumentedType) {
        return this.delegate.prepare(instrumentedType);
    }

    @Override // net.bytebuddy.implementation.Implementation
    public ByteCodeAppender appender(Implementation.Target implementationTarget) {
        return new Appender(this, implementationTarget, this.delegate.appender(implementationTarget));
    }

    public Advice withAssigner(Assigner assigner) {
        return new Advice(this.methodEnter, this.methodExit, assigner, this.exceptionHandler, this.delegate);
    }

    public Advice withExceptionPrinting() {
        return withExceptionHandler(ExceptionHandler.Default.PRINTING);
    }

    public Advice withExceptionHandler(StackManipulation exceptionHandler) {
        return withExceptionHandler(new ExceptionHandler.Simple(exceptionHandler));
    }

    public Advice withExceptionHandler(ExceptionHandler exceptionHandler) {
        return new Advice(this.methodEnter, this.methodExit, this.assigner, exceptionHandler, this.delegate);
    }

    public Implementation wrap(Implementation implementation) {
        return new Advice(this.methodEnter, this.methodExit, this.assigner, this.exceptionHandler, implementation);
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping.class */
    public interface OffsetMapping {

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$Sort.class */
        public enum Sort {
            ENTER { // from class: net.bytebuddy.asm.Advice.OffsetMapping.Sort.1
                @Override // net.bytebuddy.asm.Advice.OffsetMapping.Sort
                public boolean isPremature(MethodDescription methodDescription) {
                    return methodDescription.isConstructor();
                }
            },
            EXIT { // from class: net.bytebuddy.asm.Advice.OffsetMapping.Sort.2
                @Override // net.bytebuddy.asm.Advice.OffsetMapping.Sort
                public boolean isPremature(MethodDescription methodDescription) {
                    return false;
                }
            };

            public abstract boolean isPremature(MethodDescription methodDescription);
        }

        Target resolve(TypeDescription typeDescription, MethodDescription methodDescription, Assigner assigner, ArgumentHandler argumentHandler, Sort sort);

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$Target.class */
        public interface Target {
            StackManipulation resolveRead();

            StackManipulation resolveWrite();

            StackManipulation resolveIncrement(int i);

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$Target$AbstractReadOnlyAdapter.class */
            public static abstract class AbstractReadOnlyAdapter implements Target {
                @Override // net.bytebuddy.asm.Advice.OffsetMapping.Target
                public StackManipulation resolveWrite() {
                    throw new IllegalStateException("Cannot write to read-only value");
                }

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.Target
                public StackManipulation resolveIncrement(int value) {
                    throw new IllegalStateException("Cannot write to read-only value");
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$Target$ForDefaultValue.class */
            public static abstract class ForDefaultValue implements Target {
                protected final TypeDefinition typeDefinition;
                protected final StackManipulation readAssignment;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.typeDefinition.equals(((ForDefaultValue) obj).typeDefinition) && this.readAssignment.equals(((ForDefaultValue) obj).readAssignment);
                }

                public int hashCode() {
                    return (((17 * 31) + this.typeDefinition.hashCode()) * 31) + this.readAssignment.hashCode();
                }

                protected ForDefaultValue(TypeDefinition typeDefinition, StackManipulation readAssignment) {
                    this.typeDefinition = typeDefinition;
                    this.readAssignment = readAssignment;
                }

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.Target
                public StackManipulation resolveRead() {
                    return new StackManipulation.Compound(DefaultValue.of(this.typeDefinition), this.readAssignment);
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$Target$ForDefaultValue$ReadOnly.class */
                public static class ReadOnly extends ForDefaultValue {
                    public ReadOnly(TypeDefinition typeDefinition) {
                        this(typeDefinition, StackManipulation.Trivial.INSTANCE);
                    }

                    public ReadOnly(TypeDefinition typeDefinition, StackManipulation readAssignment) {
                        super(typeDefinition, readAssignment);
                    }

                    @Override // net.bytebuddy.asm.Advice.OffsetMapping.Target
                    public StackManipulation resolveWrite() {
                        throw new IllegalStateException("Cannot write to read-only default value");
                    }

                    @Override // net.bytebuddy.asm.Advice.OffsetMapping.Target
                    public StackManipulation resolveIncrement(int value) {
                        throw new IllegalStateException("Cannot write to read-only default value");
                    }
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$Target$ForDefaultValue$ReadWrite.class */
                public static class ReadWrite extends ForDefaultValue {
                    public ReadWrite(TypeDefinition typeDefinition) {
                        this(typeDefinition, StackManipulation.Trivial.INSTANCE);
                    }

                    public ReadWrite(TypeDefinition typeDefinition, StackManipulation readAssignment) {
                        super(typeDefinition, readAssignment);
                    }

                    @Override // net.bytebuddy.asm.Advice.OffsetMapping.Target
                    public StackManipulation resolveWrite() {
                        return Removal.of(this.typeDefinition);
                    }

                    @Override // net.bytebuddy.asm.Advice.OffsetMapping.Target
                    public StackManipulation resolveIncrement(int value) {
                        return StackManipulation.Trivial.INSTANCE;
                    }
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$Target$ForVariable.class */
            public static abstract class ForVariable implements Target {
                protected final TypeDefinition typeDefinition;
                protected final int offset;
                protected final StackManipulation readAssignment;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.offset == ((ForVariable) obj).offset && this.typeDefinition.equals(((ForVariable) obj).typeDefinition) && this.readAssignment.equals(((ForVariable) obj).readAssignment);
                }

                public int hashCode() {
                    return (((((17 * 31) + this.typeDefinition.hashCode()) * 31) + this.offset) * 31) + this.readAssignment.hashCode();
                }

                protected ForVariable(TypeDefinition typeDefinition, int offset, StackManipulation readAssignment) {
                    this.typeDefinition = typeDefinition;
                    this.offset = offset;
                    this.readAssignment = readAssignment;
                }

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.Target
                public StackManipulation resolveRead() {
                    return new StackManipulation.Compound(MethodVariableAccess.of(this.typeDefinition).loadFrom(this.offset), this.readAssignment);
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$Target$ForVariable$ReadOnly.class */
                public static class ReadOnly extends ForVariable {
                    public ReadOnly(TypeDefinition typeDefinition, int offset) {
                        this(typeDefinition, offset, StackManipulation.Trivial.INSTANCE);
                    }

                    public ReadOnly(TypeDefinition typeDefinition, int offset, StackManipulation readAssignment) {
                        super(typeDefinition, offset, readAssignment);
                    }

                    @Override // net.bytebuddy.asm.Advice.OffsetMapping.Target
                    public StackManipulation resolveWrite() {
                        throw new IllegalStateException("Cannot write to read-only parameter " + this.typeDefinition + " at " + this.offset);
                    }

                    @Override // net.bytebuddy.asm.Advice.OffsetMapping.Target
                    public StackManipulation resolveIncrement(int value) {
                        throw new IllegalStateException("Cannot write to read-only variable " + this.typeDefinition + " at " + this.offset);
                    }
                }

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$Target$ForVariable$ReadWrite.class */
                public static class ReadWrite extends ForVariable {
                    private final StackManipulation writeAssignment;

                    @Override // net.bytebuddy.asm.Advice.OffsetMapping.Target.ForVariable
                    public boolean equals(Object obj) {
                        if (super.equals(obj)) {
                            if (this == obj) {
                                return true;
                            }
                            return obj != null && getClass() == obj.getClass() && this.writeAssignment.equals(((ReadWrite) obj).writeAssignment);
                        }
                        return false;
                    }

                    @Override // net.bytebuddy.asm.Advice.OffsetMapping.Target.ForVariable
                    public int hashCode() {
                        return (super.hashCode() * 31) + this.writeAssignment.hashCode();
                    }

                    public ReadWrite(TypeDefinition typeDefinition, int offset) {
                        this(typeDefinition, offset, StackManipulation.Trivial.INSTANCE, StackManipulation.Trivial.INSTANCE);
                    }

                    public ReadWrite(TypeDefinition typeDefinition, int offset, StackManipulation readAssignment, StackManipulation writeAssignment) {
                        super(typeDefinition, offset, readAssignment);
                        this.writeAssignment = writeAssignment;
                    }

                    @Override // net.bytebuddy.asm.Advice.OffsetMapping.Target
                    public StackManipulation resolveWrite() {
                        return new StackManipulation.Compound(this.writeAssignment, MethodVariableAccess.of(this.typeDefinition).storeAt(this.offset));
                    }

                    @Override // net.bytebuddy.asm.Advice.OffsetMapping.Target
                    public StackManipulation resolveIncrement(int value) {
                        return this.typeDefinition.represents(Integer.TYPE) ? MethodVariableAccess.of(this.typeDefinition).increment(this.offset, value) : new StackManipulation.Compound(resolveRead(), IntegerConstant.forValue(1), Addition.INTEGER, resolveWrite());
                    }
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$Target$ForArray.class */
            public static abstract class ForArray implements Target {
                protected final TypeDescription.Generic target;
                protected final List<? extends StackManipulation> valueReads;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.target.equals(((ForArray) obj).target) && this.valueReads.equals(((ForArray) obj).valueReads);
                }

                public int hashCode() {
                    return (((17 * 31) + this.target.hashCode()) * 31) + this.valueReads.hashCode();
                }

                protected ForArray(TypeDescription.Generic target, List<? extends StackManipulation> valueReads) {
                    this.target = target;
                    this.valueReads = valueReads;
                }

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.Target
                public StackManipulation resolveRead() {
                    return ArrayFactory.forType(this.target).withValues(this.valueReads);
                }

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.Target
                public StackManipulation resolveIncrement(int value) {
                    throw new IllegalStateException("Cannot increment read-only array value");
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$Target$ForArray$ReadOnly.class */
                public static class ReadOnly extends ForArray {
                    public ReadOnly(TypeDescription.Generic target, List<? extends StackManipulation> valueReads) {
                        super(target, valueReads);
                    }

                    @Override // net.bytebuddy.asm.Advice.OffsetMapping.Target
                    public StackManipulation resolveWrite() {
                        throw new IllegalStateException("Cannot write to read-only array value");
                    }
                }

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$Target$ForArray$ReadWrite.class */
                public static class ReadWrite extends ForArray {
                    private final List<? extends StackManipulation> valueWrites;

                    @Override // net.bytebuddy.asm.Advice.OffsetMapping.Target.ForArray
                    public boolean equals(Object obj) {
                        if (super.equals(obj)) {
                            if (this == obj) {
                                return true;
                            }
                            return obj != null && getClass() == obj.getClass() && this.valueWrites.equals(((ReadWrite) obj).valueWrites);
                        }
                        return false;
                    }

                    @Override // net.bytebuddy.asm.Advice.OffsetMapping.Target.ForArray
                    public int hashCode() {
                        return (super.hashCode() * 31) + this.valueWrites.hashCode();
                    }

                    public ReadWrite(TypeDescription.Generic target, List<? extends StackManipulation> valueReads, List<? extends StackManipulation> valueWrites) {
                        super(target, valueReads);
                        this.valueWrites = valueWrites;
                    }

                    @Override // net.bytebuddy.asm.Advice.OffsetMapping.Target
                    public StackManipulation resolveWrite() {
                        return new StackManipulation.Compound(ArrayAccess.of(this.target).forEach(this.valueWrites), Removal.SINGLE);
                    }
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$Target$ForField.class */
            public static abstract class ForField implements Target {
                protected final FieldDescription fieldDescription;
                protected final StackManipulation readAssignment;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.fieldDescription.equals(((ForField) obj).fieldDescription) && this.readAssignment.equals(((ForField) obj).readAssignment);
                }

                public int hashCode() {
                    return (((17 * 31) + this.fieldDescription.hashCode()) * 31) + this.readAssignment.hashCode();
                }

                protected ForField(FieldDescription fieldDescription, StackManipulation readAssignment) {
                    this.fieldDescription = fieldDescription;
                    this.readAssignment = readAssignment;
                }

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.Target
                public StackManipulation resolveRead() {
                    StackManipulation[] stackManipulationArr = new StackManipulation[3];
                    stackManipulationArr[0] = this.fieldDescription.isStatic() ? StackManipulation.Trivial.INSTANCE : MethodVariableAccess.loadThis();
                    stackManipulationArr[1] = FieldAccess.forField(this.fieldDescription).read();
                    stackManipulationArr[2] = this.readAssignment;
                    return new StackManipulation.Compound(stackManipulationArr);
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$Target$ForField$ReadOnly.class */
                public static class ReadOnly extends ForField {
                    public ReadOnly(FieldDescription fieldDescription) {
                        this(fieldDescription, StackManipulation.Trivial.INSTANCE);
                    }

                    public ReadOnly(FieldDescription fieldDescription, StackManipulation readAssignment) {
                        super(fieldDescription, readAssignment);
                    }

                    @Override // net.bytebuddy.asm.Advice.OffsetMapping.Target
                    public StackManipulation resolveWrite() {
                        throw new IllegalStateException("Cannot write to read-only field value");
                    }

                    @Override // net.bytebuddy.asm.Advice.OffsetMapping.Target
                    public StackManipulation resolveIncrement(int value) {
                        throw new IllegalStateException("Cannot write to read-only field value");
                    }
                }

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$Target$ForField$ReadWrite.class */
                public static class ReadWrite extends ForField {
                    private final StackManipulation writeAssignment;

                    @Override // net.bytebuddy.asm.Advice.OffsetMapping.Target.ForField
                    public boolean equals(Object obj) {
                        if (super.equals(obj)) {
                            if (this == obj) {
                                return true;
                            }
                            return obj != null && getClass() == obj.getClass() && this.writeAssignment.equals(((ReadWrite) obj).writeAssignment);
                        }
                        return false;
                    }

                    @Override // net.bytebuddy.asm.Advice.OffsetMapping.Target.ForField
                    public int hashCode() {
                        return (super.hashCode() * 31) + this.writeAssignment.hashCode();
                    }

                    public ReadWrite(FieldDescription fieldDescription) {
                        this(fieldDescription, StackManipulation.Trivial.INSTANCE, StackManipulation.Trivial.INSTANCE);
                    }

                    public ReadWrite(FieldDescription fieldDescription, StackManipulation readAssignment, StackManipulation writeAssignment) {
                        super(fieldDescription, readAssignment);
                        this.writeAssignment = writeAssignment;
                    }

                    @Override // net.bytebuddy.asm.Advice.OffsetMapping.Target
                    public StackManipulation resolveWrite() {
                        StackManipulation preparation;
                        if (this.fieldDescription.isStatic()) {
                            preparation = StackManipulation.Trivial.INSTANCE;
                        } else {
                            preparation = new StackManipulation.Compound(MethodVariableAccess.loadThis(), Duplication.SINGLE.flipOver(this.fieldDescription.getType()), Removal.SINGLE);
                        }
                        return new StackManipulation.Compound(this.writeAssignment, preparation, FieldAccess.forField(this.fieldDescription).write());
                    }

                    @Override // net.bytebuddy.asm.Advice.OffsetMapping.Target
                    public StackManipulation resolveIncrement(int value) {
                        return new StackManipulation.Compound(resolveRead(), IntegerConstant.forValue(value), Addition.INTEGER, resolveWrite());
                    }
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$Target$ForStackManipulation.class */
            public static class ForStackManipulation implements Target {
                private final StackManipulation stackManipulation;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.stackManipulation.equals(((ForStackManipulation) obj).stackManipulation);
                }

                public int hashCode() {
                    return (17 * 31) + this.stackManipulation.hashCode();
                }

                public ForStackManipulation(StackManipulation stackManipulation) {
                    this.stackManipulation = stackManipulation;
                }

                public static Target of(MethodDescription.InDefinedShape methodDescription) {
                    return new ForStackManipulation(MethodConstant.of(methodDescription));
                }

                public static Target of(TypeDescription typeDescription) {
                    return new ForStackManipulation(ClassConstant.of(typeDescription));
                }

                public static Target of(Object value) {
                    if (value == null) {
                        return new ForStackManipulation(NullConstant.INSTANCE);
                    }
                    if (value instanceof Boolean) {
                        return new ForStackManipulation(IntegerConstant.forValue(((Boolean) value).booleanValue()));
                    }
                    if (value instanceof Byte) {
                        return new ForStackManipulation(IntegerConstant.forValue(((Byte) value).byteValue()));
                    }
                    if (value instanceof Short) {
                        return new ForStackManipulation(IntegerConstant.forValue(((Short) value).shortValue()));
                    }
                    if (value instanceof Character) {
                        return new ForStackManipulation(IntegerConstant.forValue(((Character) value).charValue()));
                    }
                    if (value instanceof Integer) {
                        return new ForStackManipulation(IntegerConstant.forValue(((Integer) value).intValue()));
                    }
                    if (value instanceof Long) {
                        return new ForStackManipulation(LongConstant.forValue(((Long) value).longValue()));
                    }
                    if (value instanceof Float) {
                        return new ForStackManipulation(FloatConstant.forValue(((Float) value).floatValue()));
                    }
                    if (value instanceof Double) {
                        return new ForStackManipulation(DoubleConstant.forValue(((Double) value).doubleValue()));
                    }
                    if (value instanceof String) {
                        return new ForStackManipulation(new TextConstant((String) value));
                    }
                    throw new IllegalArgumentException("Not a constant value: " + value);
                }

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.Target
                public StackManipulation resolveRead() {
                    return this.stackManipulation;
                }

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.Target
                public StackManipulation resolveWrite() {
                    throw new IllegalStateException("Cannot write to constant value: " + this.stackManipulation);
                }

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.Target
                public StackManipulation resolveIncrement(int value) {
                    throw new IllegalStateException("Cannot write to constant value: " + this.stackManipulation);
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$Factory.class */
        public interface Factory<T extends Annotation> {
            Class<T> getAnnotationType();

            OffsetMapping make(ParameterDescription.InDefinedShape inDefinedShape, AnnotationDescription.Loadable<T> loadable, AdviceType adviceType);

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$Factory$AdviceType.class */
            public enum AdviceType {
                DELEGATION(true),
                INLINING(false);
                
                private final boolean delegation;

                AdviceType(boolean delegation) {
                    this.delegation = delegation;
                }

                public boolean isDelegation() {
                    return this.delegation;
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$Factory$Simple.class */
            public static class Simple<T extends Annotation> implements Factory<T> {
                private final Class<T> annotationType;
                private final OffsetMapping offsetMapping;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.annotationType.equals(((Simple) obj).annotationType) && this.offsetMapping.equals(((Simple) obj).offsetMapping);
                }

                public int hashCode() {
                    return (((17 * 31) + this.annotationType.hashCode()) * 31) + this.offsetMapping.hashCode();
                }

                public Simple(Class<T> annotationType, OffsetMapping offsetMapping) {
                    this.annotationType = annotationType;
                    this.offsetMapping = offsetMapping;
                }

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.Factory
                public Class<T> getAnnotationType() {
                    return this.annotationType;
                }

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.Factory
                public OffsetMapping make(ParameterDescription.InDefinedShape target, AnnotationDescription.Loadable<T> annotation, AdviceType adviceType) {
                    return this.offsetMapping;
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$Factory$Illegal.class */
            public static class Illegal<T extends Annotation> implements Factory<T> {
                private final Class<T> annotationType;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.annotationType.equals(((Illegal) obj).annotationType);
                }

                public int hashCode() {
                    return (17 * 31) + this.annotationType.hashCode();
                }

                public Illegal(Class<T> annotationType) {
                    this.annotationType = annotationType;
                }

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.Factory
                public Class<T> getAnnotationType() {
                    return this.annotationType;
                }

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.Factory
                public OffsetMapping make(ParameterDescription.InDefinedShape target, AnnotationDescription.Loadable<T> annotation, AdviceType adviceType) {
                    throw new IllegalStateException("Usage of " + this.annotationType + " is not allowed on " + target);
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$ForArgument.class */
        public static abstract class ForArgument implements OffsetMapping {
            protected final TypeDescription.Generic target;
            protected final boolean readOnly;
            private final Assigner.Typing typing;

            protected abstract ParameterDescription resolve(MethodDescription methodDescription);

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.readOnly == ((ForArgument) obj).readOnly && this.typing.equals(((ForArgument) obj).typing) && this.target.equals(((ForArgument) obj).target);
            }

            public int hashCode() {
                return (((((17 * 31) + this.target.hashCode()) * 31) + (this.readOnly ? 1 : 0)) * 31) + this.typing.hashCode();
            }

            protected ForArgument(TypeDescription.Generic target, boolean readOnly, Assigner.Typing typing) {
                this.target = target;
                this.readOnly = readOnly;
                this.typing = typing;
            }

            @Override // net.bytebuddy.asm.Advice.OffsetMapping
            public Target resolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, Assigner assigner, ArgumentHandler argumentHandler, Sort sort) {
                ParameterDescription parameterDescription = resolve(instrumentedMethod);
                StackManipulation readAssignment = assigner.assign(parameterDescription.getType(), this.target, this.typing);
                if (!readAssignment.isValid()) {
                    throw new IllegalStateException("Cannot assign " + parameterDescription + " to " + this.target);
                }
                if (this.readOnly) {
                    return new Target.ForVariable.ReadOnly(parameterDescription.getType(), argumentHandler.argument(parameterDescription.getOffset()), readAssignment);
                }
                StackManipulation writeAssignment = assigner.assign(this.target, parameterDescription.getType(), this.typing);
                if (!writeAssignment.isValid()) {
                    throw new IllegalStateException("Cannot assign " + parameterDescription + " to " + this.target);
                }
                return new Target.ForVariable.ReadWrite(parameterDescription.getType(), argumentHandler.argument(parameterDescription.getOffset()), readAssignment, writeAssignment);
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$ForArgument$Unresolved.class */
            public static class Unresolved extends ForArgument {
                private final int index;
                private final boolean optional;

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.ForArgument
                public boolean equals(Object obj) {
                    if (super.equals(obj)) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.index == ((Unresolved) obj).index && this.optional == ((Unresolved) obj).optional;
                    }
                    return false;
                }

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.ForArgument
                public int hashCode() {
                    return (((super.hashCode() * 31) + this.index) * 31) + (this.optional ? 1 : 0);
                }

                protected Unresolved(TypeDescription.Generic target, Argument argument) {
                    this(target, argument.readOnly(), argument.typing(), argument.value(), argument.optional());
                }

                protected Unresolved(ParameterDescription parameterDescription) {
                    this(parameterDescription.getType(), true, Assigner.Typing.STATIC, parameterDescription.getIndex());
                }

                public Unresolved(TypeDescription.Generic target, boolean readOnly, Assigner.Typing typing, int index) {
                    this(target, readOnly, typing, index, false);
                }

                public Unresolved(TypeDescription.Generic target, boolean readOnly, Assigner.Typing typing, int index, boolean optional) {
                    super(target, readOnly, typing);
                    this.index = index;
                    this.optional = optional;
                }

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.ForArgument
                protected ParameterDescription resolve(MethodDescription instrumentedMethod) {
                    ParameterList<?> parameters = instrumentedMethod.getParameters();
                    if (parameters.size() <= this.index) {
                        throw new IllegalStateException(instrumentedMethod + " does not define an index " + this.index);
                    }
                    return (ParameterDescription) parameters.get(this.index);
                }

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.ForArgument, net.bytebuddy.asm.Advice.OffsetMapping
                public Target resolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, Assigner assigner, ArgumentHandler argumentHandler, Sort sort) {
                    if (!this.optional || instrumentedMethod.getParameters().size() > this.index) {
                        return super.resolve(instrumentedType, instrumentedMethod, assigner, argumentHandler, sort);
                    }
                    return this.readOnly ? new Target.ForDefaultValue.ReadOnly(this.target) : new Target.ForDefaultValue.ReadWrite(this.target);
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$ForArgument$Unresolved$Factory.class */
                protected enum Factory implements Factory<Argument> {
                    INSTANCE;

                    @Override // net.bytebuddy.asm.Advice.OffsetMapping.Factory
                    public Class<Argument> getAnnotationType() {
                        return Argument.class;
                    }

                    @Override // net.bytebuddy.asm.Advice.OffsetMapping.Factory
                    public OffsetMapping make(ParameterDescription.InDefinedShape target, AnnotationDescription.Loadable<Argument> annotation, Factory.AdviceType adviceType) {
                        if (adviceType.isDelegation() && !annotation.load().readOnly()) {
                            throw new IllegalStateException("Cannot define writable field access for " + target + " when using delegation");
                        }
                        return new Unresolved(target.getType(), annotation.load());
                    }
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$ForArgument$Resolved.class */
            public static class Resolved extends ForArgument {
                private final ParameterDescription parameterDescription;

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.ForArgument
                public boolean equals(Object obj) {
                    if (super.equals(obj)) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.parameterDescription.equals(((Resolved) obj).parameterDescription);
                    }
                    return false;
                }

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.ForArgument
                public int hashCode() {
                    return (super.hashCode() * 31) + this.parameterDescription.hashCode();
                }

                public Resolved(TypeDescription.Generic target, boolean readOnly, Assigner.Typing typing, ParameterDescription parameterDescription) {
                    super(target, readOnly, typing);
                    this.parameterDescription = parameterDescription;
                }

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.ForArgument
                protected ParameterDescription resolve(MethodDescription instrumentedMethod) {
                    if (!this.parameterDescription.getDeclaringMethod().equals(instrumentedMethod)) {
                        throw new IllegalStateException(this.parameterDescription + " is not a parameter of " + instrumentedMethod);
                    }
                    return this.parameterDescription;
                }

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$ForArgument$Resolved$Factory.class */
                public static class Factory<T extends Annotation> implements Factory<T> {
                    private final Class<T> annotationType;
                    private final ParameterDescription parameterDescription;
                    private final boolean readOnly;
                    private final Assigner.Typing typing;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.readOnly == ((Factory) obj).readOnly && this.typing.equals(((Factory) obj).typing) && this.annotationType.equals(((Factory) obj).annotationType) && this.parameterDescription.equals(((Factory) obj).parameterDescription);
                    }

                    public int hashCode() {
                        return (((((((17 * 31) + this.annotationType.hashCode()) * 31) + this.parameterDescription.hashCode()) * 31) + (this.readOnly ? 1 : 0)) * 31) + this.typing.hashCode();
                    }

                    public Factory(Class<T> annotationType, ParameterDescription parameterDescription) {
                        this(annotationType, parameterDescription, true, Assigner.Typing.STATIC);
                    }

                    public Factory(Class<T> annotationType, ParameterDescription parameterDescription, boolean readOnly, Assigner.Typing typing) {
                        this.annotationType = annotationType;
                        this.parameterDescription = parameterDescription;
                        this.readOnly = readOnly;
                        this.typing = typing;
                    }

                    @Override // net.bytebuddy.asm.Advice.OffsetMapping.Factory
                    public Class<T> getAnnotationType() {
                        return this.annotationType;
                    }

                    @Override // net.bytebuddy.asm.Advice.OffsetMapping.Factory
                    public OffsetMapping make(ParameterDescription.InDefinedShape target, AnnotationDescription.Loadable<T> annotation, Factory.AdviceType adviceType) {
                        return new Resolved(target.getType(), this.readOnly, this.typing, this.parameterDescription);
                    }
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$ForThisReference.class */
        public static class ForThisReference implements OffsetMapping {
            private final TypeDescription.Generic target;
            private final boolean readOnly;
            private final Assigner.Typing typing;
            private final boolean optional;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.readOnly == ((ForThisReference) obj).readOnly && this.optional == ((ForThisReference) obj).optional && this.typing.equals(((ForThisReference) obj).typing) && this.target.equals(((ForThisReference) obj).target);
            }

            public int hashCode() {
                return (((((((17 * 31) + this.target.hashCode()) * 31) + (this.readOnly ? 1 : 0)) * 31) + this.typing.hashCode()) * 31) + (this.optional ? 1 : 0);
            }

            protected ForThisReference(TypeDescription.Generic target, This annotation) {
                this(target, annotation.readOnly(), annotation.typing(), annotation.optional());
            }

            public ForThisReference(TypeDescription.Generic target, boolean readOnly, Assigner.Typing typing, boolean optional) {
                this.target = target;
                this.readOnly = readOnly;
                this.typing = typing;
                this.optional = optional;
            }

            @Override // net.bytebuddy.asm.Advice.OffsetMapping
            public Target resolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, Assigner assigner, ArgumentHandler argumentHandler, Sort sort) {
                if (instrumentedMethod.isStatic() || sort.isPremature(instrumentedMethod)) {
                    if (this.optional) {
                        return this.readOnly ? new Target.ForDefaultValue.ReadOnly(instrumentedType) : new Target.ForDefaultValue.ReadWrite(instrumentedType);
                    }
                    throw new IllegalStateException("Cannot map this reference for static method or constructor start: " + instrumentedMethod);
                }
                StackManipulation readAssignment = assigner.assign(instrumentedType.asGenericType(), this.target, this.typing);
                if (!readAssignment.isValid()) {
                    throw new IllegalStateException("Cannot assign " + instrumentedType + " to " + this.target);
                }
                if (this.readOnly) {
                    return new Target.ForVariable.ReadOnly(instrumentedType.asGenericType(), argumentHandler.argument(0), readAssignment);
                }
                StackManipulation writeAssignment = assigner.assign(this.target, instrumentedType.asGenericType(), this.typing);
                if (!writeAssignment.isValid()) {
                    throw new IllegalStateException("Cannot assign " + this.target + " to " + instrumentedType);
                }
                return new Target.ForVariable.ReadWrite(instrumentedType.asGenericType(), argumentHandler.argument(0), readAssignment, writeAssignment);
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$ForThisReference$Factory.class */
            protected enum Factory implements Factory<This> {
                INSTANCE;

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.Factory
                public Class<This> getAnnotationType() {
                    return This.class;
                }

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.Factory
                public OffsetMapping make(ParameterDescription.InDefinedShape target, AnnotationDescription.Loadable<This> annotation, Factory.AdviceType adviceType) {
                    if (adviceType.isDelegation() && !annotation.load().readOnly()) {
                        throw new IllegalStateException("Cannot write to this reference for " + target + " in read-only context");
                    }
                    return new ForThisReference(target.getType(), annotation.load());
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$ForAllArguments.class */
        public static class ForAllArguments implements OffsetMapping {
            private final TypeDescription.Generic target;
            private final boolean readOnly;
            private final Assigner.Typing typing;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.readOnly == ((ForAllArguments) obj).readOnly && this.typing.equals(((ForAllArguments) obj).typing) && this.target.equals(((ForAllArguments) obj).target);
            }

            public int hashCode() {
                return (((((17 * 31) + this.target.hashCode()) * 31) + (this.readOnly ? 1 : 0)) * 31) + this.typing.hashCode();
            }

            protected ForAllArguments(TypeDescription.Generic target, AllArguments annotation) {
                this(target, annotation.readOnly(), annotation.typing());
            }

            public ForAllArguments(TypeDescription.Generic target, boolean readOnly, Assigner.Typing typing) {
                this.target = target;
                this.readOnly = readOnly;
                this.typing = typing;
            }

            @Override // net.bytebuddy.asm.Advice.OffsetMapping
            public Target resolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, Assigner assigner, ArgumentHandler argumentHandler, Sort sort) {
                List<StackManipulation> valueReads = new ArrayList<>(instrumentedMethod.getParameters().size());
                Iterator it = instrumentedMethod.getParameters().iterator();
                while (it.hasNext()) {
                    ParameterDescription parameterDescription = (ParameterDescription) it.next();
                    StackManipulation readAssignment = assigner.assign(parameterDescription.getType(), this.target, this.typing);
                    if (!readAssignment.isValid()) {
                        throw new IllegalStateException("Cannot assign " + parameterDescription + " to " + this.target);
                    }
                    valueReads.add(new StackManipulation.Compound(MethodVariableAccess.of(parameterDescription.getType()).loadFrom(argumentHandler.argument(parameterDescription.getOffset())), readAssignment));
                }
                if (this.readOnly) {
                    return new Target.ForArray.ReadOnly(this.target, valueReads);
                }
                List<StackManipulation> valueWrites = new ArrayList<>(instrumentedMethod.getParameters().size());
                Iterator it2 = instrumentedMethod.getParameters().iterator();
                while (it2.hasNext()) {
                    ParameterDescription parameterDescription2 = (ParameterDescription) it2.next();
                    StackManipulation writeAssignment = assigner.assign(this.target, parameterDescription2.getType(), this.typing);
                    if (!writeAssignment.isValid()) {
                        throw new IllegalStateException("Cannot assign " + this.target + " to " + parameterDescription2);
                    }
                    valueWrites.add(new StackManipulation.Compound(writeAssignment, MethodVariableAccess.of(parameterDescription2.getType()).storeAt(argumentHandler.argument(parameterDescription2.getOffset()))));
                }
                return new Target.ForArray.ReadWrite(this.target, valueReads, valueWrites);
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$ForAllArguments$Factory.class */
            protected enum Factory implements Factory<AllArguments> {
                INSTANCE;

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.Factory
                public Class<AllArguments> getAnnotationType() {
                    return AllArguments.class;
                }

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.Factory
                public OffsetMapping make(ParameterDescription.InDefinedShape target, AnnotationDescription.Loadable<AllArguments> annotation, Factory.AdviceType adviceType) {
                    if (!target.getType().represents(Object.class) && !target.getType().isArray()) {
                        throw new IllegalStateException("Cannot use AllArguments annotation on a non-array type");
                    }
                    if (adviceType.isDelegation() && !annotation.load().readOnly()) {
                        throw new IllegalStateException("Cannot define writable field access for " + target);
                    }
                    return new ForAllArguments(target.getType().represents(Object.class) ? TypeDescription.Generic.OBJECT : target.getType().getComponentType(), annotation.load());
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$ForInstrumentedType.class */
        public enum ForInstrumentedType implements OffsetMapping {
            INSTANCE;

            @Override // net.bytebuddy.asm.Advice.OffsetMapping
            public Target resolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, Assigner assigner, ArgumentHandler argumentHandler, Sort sort) {
                return Target.ForStackManipulation.of(instrumentedType);
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$ForInstrumentedMethod.class */
        public enum ForInstrumentedMethod implements OffsetMapping {
            METHOD { // from class: net.bytebuddy.asm.Advice.OffsetMapping.ForInstrumentedMethod.1
                @Override // net.bytebuddy.asm.Advice.OffsetMapping.ForInstrumentedMethod
                protected boolean isRepresentable(MethodDescription instrumentedMethod) {
                    return instrumentedMethod.isMethod();
                }
            },
            CONSTRUCTOR { // from class: net.bytebuddy.asm.Advice.OffsetMapping.ForInstrumentedMethod.2
                @Override // net.bytebuddy.asm.Advice.OffsetMapping.ForInstrumentedMethod
                protected boolean isRepresentable(MethodDescription instrumentedMethod) {
                    return instrumentedMethod.isConstructor();
                }
            },
            EXECUTABLE { // from class: net.bytebuddy.asm.Advice.OffsetMapping.ForInstrumentedMethod.3
                @Override // net.bytebuddy.asm.Advice.OffsetMapping.ForInstrumentedMethod
                protected boolean isRepresentable(MethodDescription instrumentedMethod) {
                    return true;
                }
            };

            protected abstract boolean isRepresentable(MethodDescription methodDescription);

            @Override // net.bytebuddy.asm.Advice.OffsetMapping
            public Target resolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, Assigner assigner, ArgumentHandler argumentHandler, Sort sort) {
                if (!isRepresentable(instrumentedMethod)) {
                    throw new IllegalStateException("Cannot represent " + instrumentedMethod + " as given method constant");
                }
                return Target.ForStackManipulation.of(instrumentedMethod.asDefined());
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$ForField.class */
        public static abstract class ForField implements OffsetMapping {
            private static final MethodDescription.InDefinedShape VALUE;
            private static final MethodDescription.InDefinedShape DECLARING_TYPE;
            private static final MethodDescription.InDefinedShape READ_ONLY;
            private static final MethodDescription.InDefinedShape TYPING;
            private final TypeDescription.Generic target;
            private final boolean readOnly;
            private final Assigner.Typing typing;

            protected abstract FieldDescription resolve(TypeDescription typeDescription);

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.readOnly == ((ForField) obj).readOnly && this.typing.equals(((ForField) obj).typing) && this.target.equals(((ForField) obj).target);
            }

            public int hashCode() {
                return (((((17 * 31) + this.target.hashCode()) * 31) + (this.readOnly ? 1 : 0)) * 31) + this.typing.hashCode();
            }

            static {
                MethodList<MethodDescription.InDefinedShape> methods = TypeDescription.ForLoadedType.of(FieldValue.class).getDeclaredMethods();
                VALUE = (MethodDescription.InDefinedShape) methods.filter(ElementMatchers.named("value")).getOnly();
                DECLARING_TYPE = (MethodDescription.InDefinedShape) methods.filter(ElementMatchers.named("declaringType")).getOnly();
                READ_ONLY = (MethodDescription.InDefinedShape) methods.filter(ElementMatchers.named("readOnly")).getOnly();
                TYPING = (MethodDescription.InDefinedShape) methods.filter(ElementMatchers.named("typing")).getOnly();
            }

            public ForField(TypeDescription.Generic target, boolean readOnly, Assigner.Typing typing) {
                this.target = target;
                this.readOnly = readOnly;
                this.typing = typing;
            }

            @Override // net.bytebuddy.asm.Advice.OffsetMapping
            public Target resolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, Assigner assigner, ArgumentHandler argumentHandler, Sort sort) {
                FieldDescription fieldDescription = resolve(instrumentedType);
                if (!fieldDescription.isStatic() && instrumentedMethod.isStatic()) {
                    throw new IllegalStateException("Cannot read non-static field " + fieldDescription + " from static method " + instrumentedMethod);
                }
                if (sort.isPremature(instrumentedMethod) && !fieldDescription.isStatic()) {
                    throw new IllegalStateException("Cannot access non-static field before calling constructor: " + instrumentedMethod);
                }
                StackManipulation readAssignment = assigner.assign(fieldDescription.getType(), this.target, this.typing);
                if (!readAssignment.isValid()) {
                    throw new IllegalStateException("Cannot assign " + fieldDescription + " to " + this.target);
                }
                if (this.readOnly) {
                    return new Target.ForField.ReadOnly(fieldDescription, readAssignment);
                }
                StackManipulation writeAssignment = assigner.assign(this.target, fieldDescription.getType(), this.typing);
                if (!writeAssignment.isValid()) {
                    throw new IllegalStateException("Cannot assign " + this.target + " to " + fieldDescription);
                }
                return new Target.ForField.ReadWrite(fieldDescription.asDefined(), readAssignment, writeAssignment);
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$ForField$Unresolved.class */
            public static abstract class Unresolved extends ForField {
                private final String name;

                protected abstract FieldLocator fieldLocator(TypeDescription typeDescription);

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.ForField
                public boolean equals(Object obj) {
                    if (super.equals(obj)) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.name.equals(((Unresolved) obj).name);
                    }
                    return false;
                }

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.ForField
                public int hashCode() {
                    return (super.hashCode() * 31) + this.name.hashCode();
                }

                public Unresolved(TypeDescription.Generic target, boolean readOnly, Assigner.Typing typing, String name) {
                    super(target, readOnly, typing);
                    this.name = name;
                }

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.ForField
                protected FieldDescription resolve(TypeDescription instrumentedType) {
                    FieldLocator.Resolution resolution = fieldLocator(instrumentedType).locate(this.name);
                    if (!resolution.isResolved()) {
                        throw new IllegalStateException("Cannot locate field named " + this.name + " for " + instrumentedType);
                    }
                    return resolution.getField();
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$ForField$Unresolved$WithImplicitType.class */
                public static class WithImplicitType extends Unresolved {
                    protected WithImplicitType(TypeDescription.Generic target, AnnotationDescription.Loadable<FieldValue> annotation) {
                        this(target, ((Boolean) annotation.getValue(ForField.READ_ONLY).resolve(Boolean.class)).booleanValue(), (Assigner.Typing) annotation.getValue(ForField.TYPING).load(Assigner.Typing.class.getClassLoader()).resolve(Assigner.Typing.class), (String) annotation.getValue(ForField.VALUE).resolve(String.class));
                    }

                    public WithImplicitType(TypeDescription.Generic target, boolean readOnly, Assigner.Typing typing, String name) {
                        super(target, readOnly, typing, name);
                    }

                    @Override // net.bytebuddy.asm.Advice.OffsetMapping.ForField.Unresolved
                    protected FieldLocator fieldLocator(TypeDescription instrumentedType) {
                        return new FieldLocator.ForClassHierarchy(instrumentedType);
                    }
                }

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$ForField$Unresolved$WithExplicitType.class */
                public static class WithExplicitType extends Unresolved {
                    private final TypeDescription declaringType;

                    @Override // net.bytebuddy.asm.Advice.OffsetMapping.ForField.Unresolved, net.bytebuddy.asm.Advice.OffsetMapping.ForField
                    public boolean equals(Object obj) {
                        if (super.equals(obj)) {
                            if (this == obj) {
                                return true;
                            }
                            return obj != null && getClass() == obj.getClass() && this.declaringType.equals(((WithExplicitType) obj).declaringType);
                        }
                        return false;
                    }

                    @Override // net.bytebuddy.asm.Advice.OffsetMapping.ForField.Unresolved, net.bytebuddy.asm.Advice.OffsetMapping.ForField
                    public int hashCode() {
                        return (super.hashCode() * 31) + this.declaringType.hashCode();
                    }

                    protected WithExplicitType(TypeDescription.Generic target, AnnotationDescription.Loadable<FieldValue> annotation, TypeDescription declaringType) {
                        this(target, ((Boolean) annotation.getValue(ForField.READ_ONLY).resolve(Boolean.class)).booleanValue(), (Assigner.Typing) annotation.getValue(ForField.TYPING).load(Assigner.Typing.class.getClassLoader()).resolve(Assigner.Typing.class), (String) annotation.getValue(ForField.VALUE).resolve(String.class), declaringType);
                    }

                    public WithExplicitType(TypeDescription.Generic target, boolean readOnly, Assigner.Typing typing, String name, TypeDescription declaringType) {
                        super(target, readOnly, typing, name);
                        this.declaringType = declaringType;
                    }

                    @Override // net.bytebuddy.asm.Advice.OffsetMapping.ForField.Unresolved
                    protected FieldLocator fieldLocator(TypeDescription instrumentedType) {
                        if (!this.declaringType.represents(TargetType.class) && !instrumentedType.isAssignableTo(this.declaringType)) {
                            throw new IllegalStateException(this.declaringType + " is no super type of " + instrumentedType);
                        }
                        return new FieldLocator.ForExactType(TargetType.resolve(this.declaringType, instrumentedType));
                    }
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$ForField$Unresolved$Factory.class */
                protected enum Factory implements Factory<FieldValue> {
                    INSTANCE;

                    @Override // net.bytebuddy.asm.Advice.OffsetMapping.Factory
                    public Class<FieldValue> getAnnotationType() {
                        return FieldValue.class;
                    }

                    @Override // net.bytebuddy.asm.Advice.OffsetMapping.Factory
                    public OffsetMapping make(ParameterDescription.InDefinedShape target, AnnotationDescription.Loadable<FieldValue> annotation, Factory.AdviceType adviceType) {
                        if (adviceType.isDelegation() && !((Boolean) annotation.getValue(ForField.READ_ONLY).resolve(Boolean.class)).booleanValue()) {
                            throw new IllegalStateException("Cannot write to field for " + target + " in read-only context");
                        }
                        TypeDescription declaringType = (TypeDescription) annotation.getValue(ForField.DECLARING_TYPE).resolve(TypeDescription.class);
                        if (declaringType.represents(Void.TYPE)) {
                            return new WithImplicitType(target.getType(), annotation);
                        }
                        return new WithExplicitType(target.getType(), annotation, declaringType);
                    }
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$ForField$Resolved.class */
            public static class Resolved extends ForField {
                private final FieldDescription fieldDescription;

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.ForField
                public boolean equals(Object obj) {
                    if (super.equals(obj)) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.fieldDescription.equals(((Resolved) obj).fieldDescription);
                    }
                    return false;
                }

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.ForField
                public int hashCode() {
                    return (super.hashCode() * 31) + this.fieldDescription.hashCode();
                }

                public Resolved(TypeDescription.Generic target, boolean readOnly, Assigner.Typing typing, FieldDescription fieldDescription) {
                    super(target, readOnly, typing);
                    this.fieldDescription = fieldDescription;
                }

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.ForField
                protected FieldDescription resolve(TypeDescription instrumentedType) {
                    if (!this.fieldDescription.isStatic() && !this.fieldDescription.getDeclaringType().asErasure().isAssignableFrom(instrumentedType)) {
                        throw new IllegalStateException(this.fieldDescription + " is no member of " + instrumentedType);
                    }
                    if (!this.fieldDescription.isAccessibleTo(instrumentedType)) {
                        throw new IllegalStateException("Cannot access " + this.fieldDescription + " from " + instrumentedType);
                    }
                    return this.fieldDescription;
                }

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$ForField$Resolved$Factory.class */
                public static class Factory<T extends Annotation> implements Factory<T> {
                    private final Class<T> annotationType;
                    private final FieldDescription fieldDescription;
                    private final boolean readOnly;
                    private final Assigner.Typing typing;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.readOnly == ((Factory) obj).readOnly && this.typing.equals(((Factory) obj).typing) && this.annotationType.equals(((Factory) obj).annotationType) && this.fieldDescription.equals(((Factory) obj).fieldDescription);
                    }

                    public int hashCode() {
                        return (((((((17 * 31) + this.annotationType.hashCode()) * 31) + this.fieldDescription.hashCode()) * 31) + (this.readOnly ? 1 : 0)) * 31) + this.typing.hashCode();
                    }

                    public Factory(Class<T> annotationType, FieldDescription fieldDescription) {
                        this(annotationType, fieldDescription, true, Assigner.Typing.STATIC);
                    }

                    public Factory(Class<T> annotationType, FieldDescription fieldDescription, boolean readOnly, Assigner.Typing typing) {
                        this.annotationType = annotationType;
                        this.fieldDescription = fieldDescription;
                        this.readOnly = readOnly;
                        this.typing = typing;
                    }

                    @Override // net.bytebuddy.asm.Advice.OffsetMapping.Factory
                    public Class<T> getAnnotationType() {
                        return this.annotationType;
                    }

                    @Override // net.bytebuddy.asm.Advice.OffsetMapping.Factory
                    public OffsetMapping make(ParameterDescription.InDefinedShape target, AnnotationDescription.Loadable<T> annotation, Factory.AdviceType adviceType) {
                        return new Resolved(target.getType(), this.readOnly, this.typing, this.fieldDescription);
                    }
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$ForOrigin.class */
        public static class ForOrigin implements OffsetMapping {
            private static final char DELIMITER = '#';
            private static final char ESCAPE = '\\';
            private final List<Renderer> renderers;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.renderers.equals(((ForOrigin) obj).renderers);
            }

            public int hashCode() {
                return (17 * 31) + this.renderers.hashCode();
            }

            public ForOrigin(List<Renderer> renderers) {
                this.renderers = renderers;
            }

            public static OffsetMapping parse(String pattern) {
                int i;
                int i2;
                if (pattern.equals("")) {
                    return new ForOrigin(Collections.singletonList(Renderer.ForStringRepresentation.INSTANCE));
                }
                List<Renderer> renderers = new ArrayList<>(pattern.length());
                int from = 0;
                int indexOf = pattern.indexOf(35);
                while (true) {
                    int to = indexOf;
                    if (to != -1) {
                        if (to != 0 && pattern.charAt(to - 1) == '\\' && (to == 1 || pattern.charAt(to - 2) != '\\')) {
                            renderers.add(new Renderer.ForConstantValue(pattern.substring(from, Math.max(0, to - 1)) + '#'));
                            i = to;
                            i2 = 1;
                        } else if (pattern.length() == to + 1) {
                            throw new IllegalStateException("Missing sort descriptor for " + pattern + " at index " + to);
                        } else {
                            renderers.add(new Renderer.ForConstantValue(pattern.substring(from, to).replace("\\\\", "\\")));
                            switch (pattern.charAt(to + 1)) {
                                case 'd':
                                    renderers.add(Renderer.ForDescriptor.INSTANCE);
                                    break;
                                case 'm':
                                    renderers.add(Renderer.ForMethodName.INSTANCE);
                                    break;
                                case 'r':
                                    renderers.add(Renderer.ForReturnTypeName.INSTANCE);
                                    break;
                                case 's':
                                    renderers.add(Renderer.ForJavaSignature.INSTANCE);
                                    break;
                                case 't':
                                    renderers.add(Renderer.ForTypeName.INSTANCE);
                                    break;
                                default:
                                    throw new IllegalStateException("Illegal sort descriptor " + pattern.charAt(to + 1) + " for " + pattern);
                            }
                            i = to;
                            i2 = 2;
                        }
                        from = i + i2;
                        indexOf = pattern.indexOf(35, from);
                    } else {
                        renderers.add(new Renderer.ForConstantValue(pattern.substring(from)));
                        return new ForOrigin(renderers);
                    }
                }
            }

            @Override // net.bytebuddy.asm.Advice.OffsetMapping
            public Target resolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, Assigner assigner, ArgumentHandler argumentHandler, Sort sort) {
                StringBuilder stringBuilder = new StringBuilder();
                for (Renderer renderer : this.renderers) {
                    stringBuilder.append(renderer.apply(instrumentedType, instrumentedMethod));
                }
                return Target.ForStackManipulation.of(stringBuilder.toString());
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$ForOrigin$Renderer.class */
            public interface Renderer {
                String apply(TypeDescription typeDescription, MethodDescription methodDescription);

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$ForOrigin$Renderer$ForMethodName.class */
                public enum ForMethodName implements Renderer {
                    INSTANCE;
                    
                    public static final char SYMBOL = 'm';

                    @Override // net.bytebuddy.asm.Advice.OffsetMapping.ForOrigin.Renderer
                    public String apply(TypeDescription instrumentedType, MethodDescription instrumentedMethod) {
                        return instrumentedMethod.getInternalName();
                    }
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$ForOrigin$Renderer$ForTypeName.class */
                public enum ForTypeName implements Renderer {
                    INSTANCE;
                    
                    public static final char SYMBOL = 't';

                    @Override // net.bytebuddy.asm.Advice.OffsetMapping.ForOrigin.Renderer
                    public String apply(TypeDescription instrumentedType, MethodDescription instrumentedMethod) {
                        return instrumentedType.getName();
                    }
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$ForOrigin$Renderer$ForDescriptor.class */
                public enum ForDescriptor implements Renderer {
                    INSTANCE;
                    
                    public static final char SYMBOL = 'd';

                    @Override // net.bytebuddy.asm.Advice.OffsetMapping.ForOrigin.Renderer
                    public String apply(TypeDescription instrumentedType, MethodDescription instrumentedMethod) {
                        return instrumentedMethod.getDescriptor();
                    }
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$ForOrigin$Renderer$ForJavaSignature.class */
                public enum ForJavaSignature implements Renderer {
                    INSTANCE;
                    
                    public static final char SYMBOL = 's';

                    @Override // net.bytebuddy.asm.Advice.OffsetMapping.ForOrigin.Renderer
                    public String apply(TypeDescription instrumentedType, MethodDescription instrumentedMethod) {
                        StringBuilder stringBuilder = new StringBuilder("(");
                        boolean comma = false;
                        for (TypeDescription typeDescription : instrumentedMethod.getParameters().asTypeList().asErasures()) {
                            if (comma) {
                                stringBuilder.append(',');
                            } else {
                                comma = true;
                            }
                            stringBuilder.append(typeDescription.getName());
                        }
                        return stringBuilder.append(')').toString();
                    }
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$ForOrigin$Renderer$ForReturnTypeName.class */
                public enum ForReturnTypeName implements Renderer {
                    INSTANCE;
                    
                    public static final char SYMBOL = 'r';

                    @Override // net.bytebuddy.asm.Advice.OffsetMapping.ForOrigin.Renderer
                    public String apply(TypeDescription instrumentedType, MethodDescription instrumentedMethod) {
                        return instrumentedMethod.getReturnType().asErasure().getName();
                    }
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$ForOrigin$Renderer$ForStringRepresentation.class */
                public enum ForStringRepresentation implements Renderer {
                    INSTANCE;

                    @Override // net.bytebuddy.asm.Advice.OffsetMapping.ForOrigin.Renderer
                    public String apply(TypeDescription instrumentedType, MethodDescription instrumentedMethod) {
                        return instrumentedMethod.toString();
                    }
                }

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$ForOrigin$Renderer$ForConstantValue.class */
                public static class ForConstantValue implements Renderer {
                    private final String value;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.value.equals(((ForConstantValue) obj).value);
                    }

                    public int hashCode() {
                        return (17 * 31) + this.value.hashCode();
                    }

                    public ForConstantValue(String value) {
                        this.value = value;
                    }

                    @Override // net.bytebuddy.asm.Advice.OffsetMapping.ForOrigin.Renderer
                    public String apply(TypeDescription instrumentedType, MethodDescription instrumentedMethod) {
                        return this.value;
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$ForOrigin$Factory.class */
            protected enum Factory implements Factory<Origin> {
                INSTANCE;

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.Factory
                public Class<Origin> getAnnotationType() {
                    return Origin.class;
                }

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.Factory
                public OffsetMapping make(ParameterDescription.InDefinedShape target, AnnotationDescription.Loadable<Origin> annotation, Factory.AdviceType adviceType) {
                    if (target.getType().asErasure().represents(Class.class)) {
                        return ForInstrumentedType.INSTANCE;
                    }
                    if (target.getType().asErasure().represents(Method.class)) {
                        return ForInstrumentedMethod.METHOD;
                    }
                    if (target.getType().asErasure().represents(Constructor.class)) {
                        return ForInstrumentedMethod.CONSTRUCTOR;
                    }
                    if (JavaType.EXECUTABLE.getTypeStub().equals(target.getType().asErasure())) {
                        return ForInstrumentedMethod.EXECUTABLE;
                    }
                    if (target.getType().asErasure().isAssignableFrom(String.class)) {
                        return ForOrigin.parse(annotation.load().value());
                    }
                    throw new IllegalStateException("Non-supported type " + target.getType() + " for @Origin annotation");
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$ForUnusedValue.class */
        public static class ForUnusedValue implements OffsetMapping {
            private final TypeDefinition target;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.target.equals(((ForUnusedValue) obj).target);
            }

            public int hashCode() {
                return (17 * 31) + this.target.hashCode();
            }

            public ForUnusedValue(TypeDefinition target) {
                this.target = target;
            }

            @Override // net.bytebuddy.asm.Advice.OffsetMapping
            public Target resolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, Assigner assigner, ArgumentHandler argumentHandler, Sort sort) {
                return new Target.ForDefaultValue.ReadWrite(this.target);
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$ForUnusedValue$Factory.class */
            protected enum Factory implements Factory<Unused> {
                INSTANCE;

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.Factory
                public Class<Unused> getAnnotationType() {
                    return Unused.class;
                }

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.Factory
                public OffsetMapping make(ParameterDescription.InDefinedShape target, AnnotationDescription.Loadable<Unused> annotation, Factory.AdviceType adviceType) {
                    return new ForUnusedValue(target.getType());
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$ForStubValue.class */
        public enum ForStubValue implements OffsetMapping, Factory<StubValue> {
            INSTANCE;

            @Override // net.bytebuddy.asm.Advice.OffsetMapping
            public Target resolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, Assigner assigner, ArgumentHandler argumentHandler, Sort sort) {
                return new Target.ForDefaultValue.ReadOnly(instrumentedMethod.getReturnType(), assigner.assign(instrumentedMethod.getReturnType(), TypeDescription.Generic.OBJECT, Assigner.Typing.DYNAMIC));
            }

            @Override // net.bytebuddy.asm.Advice.OffsetMapping.Factory
            public Class<StubValue> getAnnotationType() {
                return StubValue.class;
            }

            @Override // net.bytebuddy.asm.Advice.OffsetMapping.Factory
            public OffsetMapping make(ParameterDescription.InDefinedShape target, AnnotationDescription.Loadable<StubValue> annotation, Factory.AdviceType adviceType) {
                if (!target.getType().represents(Object.class)) {
                    throw new IllegalStateException("Cannot use StubValue on non-Object parameter type " + target);
                }
                return this;
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$ForEnterValue.class */
        public static class ForEnterValue implements OffsetMapping {
            private final TypeDescription.Generic target;
            private final TypeDescription.Generic enterType;
            private final boolean readOnly;
            private final Assigner.Typing typing;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.readOnly == ((ForEnterValue) obj).readOnly && this.typing.equals(((ForEnterValue) obj).typing) && this.target.equals(((ForEnterValue) obj).target) && this.enterType.equals(((ForEnterValue) obj).enterType);
            }

            public int hashCode() {
                return (((((((17 * 31) + this.target.hashCode()) * 31) + this.enterType.hashCode()) * 31) + (this.readOnly ? 1 : 0)) * 31) + this.typing.hashCode();
            }

            protected ForEnterValue(TypeDescription.Generic target, TypeDescription.Generic enterType, Enter enter) {
                this(target, enterType, enter.readOnly(), enter.typing());
            }

            public ForEnterValue(TypeDescription.Generic target, TypeDescription.Generic enterType, boolean readOnly, Assigner.Typing typing) {
                this.target = target;
                this.enterType = enterType;
                this.readOnly = readOnly;
                this.typing = typing;
            }

            @Override // net.bytebuddy.asm.Advice.OffsetMapping
            public Target resolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, Assigner assigner, ArgumentHandler argumentHandler, Sort sort) {
                StackManipulation readAssignment = assigner.assign(this.enterType, this.target, this.typing);
                if (!readAssignment.isValid()) {
                    throw new IllegalStateException("Cannot assign " + this.enterType + " to " + this.target);
                }
                if (this.readOnly) {
                    return new Target.ForVariable.ReadOnly(this.target, argumentHandler.enter(), readAssignment);
                }
                StackManipulation writeAssignment = assigner.assign(this.target, this.enterType, this.typing);
                if (!writeAssignment.isValid()) {
                    throw new IllegalStateException("Cannot assign " + this.target + " to " + this.enterType);
                }
                return new Target.ForVariable.ReadWrite(this.target, argumentHandler.enter(), readAssignment, writeAssignment);
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$ForEnterValue$Factory.class */
            protected static class Factory implements Factory<Enter> {
                private final TypeDefinition enterType;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.enterType.equals(((Factory) obj).enterType);
                }

                public int hashCode() {
                    return (17 * 31) + this.enterType.hashCode();
                }

                protected Factory(TypeDefinition enterType) {
                    this.enterType = enterType;
                }

                protected static Factory<Enter> of(TypeDefinition typeDefinition) {
                    return typeDefinition.represents(Void.TYPE) ? new Factory.Illegal(Enter.class) : new Factory(typeDefinition);
                }

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.Factory
                public Class<Enter> getAnnotationType() {
                    return Enter.class;
                }

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.Factory
                public OffsetMapping make(ParameterDescription.InDefinedShape target, AnnotationDescription.Loadable<Enter> annotation, Factory.AdviceType adviceType) {
                    if (adviceType.isDelegation() && !annotation.load().readOnly()) {
                        throw new IllegalStateException("Cannot use writable " + target + " on read-only parameter");
                    }
                    return new ForEnterValue(target.getType(), this.enterType.asGenericType(), annotation.load());
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$ForExitValue.class */
        public static class ForExitValue implements OffsetMapping {
            private final TypeDescription.Generic target;
            private final TypeDescription.Generic exitType;
            private final boolean readOnly;
            private final Assigner.Typing typing;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.readOnly == ((ForExitValue) obj).readOnly && this.typing.equals(((ForExitValue) obj).typing) && this.target.equals(((ForExitValue) obj).target) && this.exitType.equals(((ForExitValue) obj).exitType);
            }

            public int hashCode() {
                return (((((((17 * 31) + this.target.hashCode()) * 31) + this.exitType.hashCode()) * 31) + (this.readOnly ? 1 : 0)) * 31) + this.typing.hashCode();
            }

            protected ForExitValue(TypeDescription.Generic target, TypeDescription.Generic exitType, Exit exit) {
                this(target, exitType, exit.readOnly(), exit.typing());
            }

            public ForExitValue(TypeDescription.Generic target, TypeDescription.Generic exitType, boolean readOnly, Assigner.Typing typing) {
                this.target = target;
                this.exitType = exitType;
                this.readOnly = readOnly;
                this.typing = typing;
            }

            @Override // net.bytebuddy.asm.Advice.OffsetMapping
            public Target resolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, Assigner assigner, ArgumentHandler argumentHandler, Sort sort) {
                StackManipulation readAssignment = assigner.assign(this.exitType, this.target, this.typing);
                if (!readAssignment.isValid()) {
                    throw new IllegalStateException("Cannot assign " + this.exitType + " to " + this.target);
                }
                if (this.readOnly) {
                    return new Target.ForVariable.ReadOnly(this.target, argumentHandler.exit(), readAssignment);
                }
                StackManipulation writeAssignment = assigner.assign(this.target, this.exitType, this.typing);
                if (!writeAssignment.isValid()) {
                    throw new IllegalStateException("Cannot assign " + this.target + " to " + this.exitType);
                }
                return new Target.ForVariable.ReadWrite(this.target, argumentHandler.exit(), readAssignment, writeAssignment);
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$ForExitValue$Factory.class */
            protected static class Factory implements Factory<Exit> {
                private final TypeDefinition exitType;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.exitType.equals(((Factory) obj).exitType);
                }

                public int hashCode() {
                    return (17 * 31) + this.exitType.hashCode();
                }

                protected Factory(TypeDefinition exitType) {
                    this.exitType = exitType;
                }

                protected static Factory<Exit> of(TypeDefinition typeDefinition) {
                    return typeDefinition.represents(Void.TYPE) ? new Factory.Illegal(Exit.class) : new Factory(typeDefinition);
                }

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.Factory
                public Class<Exit> getAnnotationType() {
                    return Exit.class;
                }

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.Factory
                public OffsetMapping make(ParameterDescription.InDefinedShape target, AnnotationDescription.Loadable<Exit> annotation, Factory.AdviceType adviceType) {
                    if (adviceType.isDelegation() && !annotation.load().readOnly()) {
                        throw new IllegalStateException("Cannot use writable " + target + " on read-only parameter");
                    }
                    return new ForExitValue(target.getType(), this.exitType.asGenericType(), annotation.load());
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$ForLocalValue.class */
        public static class ForLocalValue implements OffsetMapping {
            private final TypeDescription.Generic target;
            private final TypeDescription.Generic localType;
            private final String name;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.name.equals(((ForLocalValue) obj).name) && this.target.equals(((ForLocalValue) obj).target) && this.localType.equals(((ForLocalValue) obj).localType);
            }

            public int hashCode() {
                return (((((17 * 31) + this.target.hashCode()) * 31) + this.localType.hashCode()) * 31) + this.name.hashCode();
            }

            public ForLocalValue(TypeDescription.Generic target, TypeDescription.Generic localType, String name) {
                this.target = target;
                this.localType = localType;
                this.name = name;
            }

            @Override // net.bytebuddy.asm.Advice.OffsetMapping
            public Target resolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, Assigner assigner, ArgumentHandler argumentHandler, Sort sort) {
                StackManipulation readAssignment = assigner.assign(this.localType, this.target, Assigner.Typing.STATIC);
                StackManipulation writeAssignment = assigner.assign(this.target, this.localType, Assigner.Typing.STATIC);
                if (!readAssignment.isValid() || !writeAssignment.isValid()) {
                    throw new IllegalStateException("Cannot assign " + this.localType + " to " + this.target);
                }
                return new Target.ForVariable.ReadWrite(this.target, argumentHandler.named(this.name), readAssignment, writeAssignment);
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$ForLocalValue$Factory.class */
            protected static class Factory implements Factory<Local> {
                private final Map<String, TypeDefinition> namedTypes;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.namedTypes.equals(((Factory) obj).namedTypes);
                }

                public int hashCode() {
                    return (17 * 31) + this.namedTypes.hashCode();
                }

                protected Factory(Map<String, TypeDefinition> namedTypes) {
                    this.namedTypes = namedTypes;
                }

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.Factory
                public Class<Local> getAnnotationType() {
                    return Local.class;
                }

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.Factory
                public OffsetMapping make(ParameterDescription.InDefinedShape target, AnnotationDescription.Loadable<Local> annotation, Factory.AdviceType adviceType) {
                    String name = annotation.load().value();
                    TypeDefinition namedType = this.namedTypes.get(name);
                    if (namedType == null) {
                        throw new IllegalStateException("Named local variable is unknown: " + name);
                    }
                    return new ForLocalValue(target.getType(), namedType.asGenericType(), name);
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$ForReturnValue.class */
        public static class ForReturnValue implements OffsetMapping {
            private final TypeDescription.Generic target;
            private final boolean readOnly;
            private final Assigner.Typing typing;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.readOnly == ((ForReturnValue) obj).readOnly && this.typing.equals(((ForReturnValue) obj).typing) && this.target.equals(((ForReturnValue) obj).target);
            }

            public int hashCode() {
                return (((((17 * 31) + this.target.hashCode()) * 31) + (this.readOnly ? 1 : 0)) * 31) + this.typing.hashCode();
            }

            protected ForReturnValue(TypeDescription.Generic target, Return annotation) {
                this(target, annotation.readOnly(), annotation.typing());
            }

            public ForReturnValue(TypeDescription.Generic target, boolean readOnly, Assigner.Typing typing) {
                this.target = target;
                this.readOnly = readOnly;
                this.typing = typing;
            }

            @Override // net.bytebuddy.asm.Advice.OffsetMapping
            public Target resolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, Assigner assigner, ArgumentHandler argumentHandler, Sort sort) {
                StackManipulation readAssignment = assigner.assign(instrumentedMethod.getReturnType(), this.target, this.typing);
                if (!readAssignment.isValid()) {
                    throw new IllegalStateException("Cannot assign " + instrumentedMethod.getReturnType() + " to " + this.target);
                }
                if (this.readOnly) {
                    return instrumentedMethod.getReturnType().represents(Void.TYPE) ? new Target.ForDefaultValue.ReadOnly(this.target) : new Target.ForVariable.ReadOnly(instrumentedMethod.getReturnType(), argumentHandler.returned(), readAssignment);
                }
                StackManipulation writeAssignment = assigner.assign(this.target, instrumentedMethod.getReturnType(), this.typing);
                if (writeAssignment.isValid()) {
                    return instrumentedMethod.getReturnType().represents(Void.TYPE) ? new Target.ForDefaultValue.ReadWrite(this.target) : new Target.ForVariable.ReadWrite(instrumentedMethod.getReturnType(), argumentHandler.returned(), readAssignment, writeAssignment);
                }
                throw new IllegalStateException("Cannot assign " + this.target + " to " + instrumentedMethod.getReturnType());
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$ForReturnValue$Factory.class */
            protected enum Factory implements Factory<Return> {
                INSTANCE;

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.Factory
                public Class<Return> getAnnotationType() {
                    return Return.class;
                }

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.Factory
                public OffsetMapping make(ParameterDescription.InDefinedShape target, AnnotationDescription.Loadable<Return> annotation, Factory.AdviceType adviceType) {
                    if (adviceType.isDelegation() && !annotation.load().readOnly()) {
                        throw new IllegalStateException("Cannot write return value for " + target + " in read-only context");
                    }
                    return new ForReturnValue(target.getType(), annotation.load());
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$ForThrowable.class */
        public static class ForThrowable implements OffsetMapping {
            private final TypeDescription.Generic target;
            private final boolean readOnly;
            private final Assigner.Typing typing;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.readOnly == ((ForThrowable) obj).readOnly && this.typing.equals(((ForThrowable) obj).typing) && this.target.equals(((ForThrowable) obj).target);
            }

            public int hashCode() {
                return (((((17 * 31) + this.target.hashCode()) * 31) + (this.readOnly ? 1 : 0)) * 31) + this.typing.hashCode();
            }

            protected ForThrowable(TypeDescription.Generic target, Thrown annotation) {
                this(target, annotation.readOnly(), annotation.typing());
            }

            public ForThrowable(TypeDescription.Generic target, boolean readOnly, Assigner.Typing typing) {
                this.target = target;
                this.readOnly = readOnly;
                this.typing = typing;
            }

            @Override // net.bytebuddy.asm.Advice.OffsetMapping
            public Target resolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, Assigner assigner, ArgumentHandler argumentHandler, Sort sort) {
                StackManipulation readAssignment = assigner.assign(TypeDescription.THROWABLE.asGenericType(), this.target, this.typing);
                if (!readAssignment.isValid()) {
                    throw new IllegalStateException("Cannot assign Throwable to " + this.target);
                }
                if (this.readOnly) {
                    return new Target.ForVariable.ReadOnly(TypeDescription.THROWABLE, argumentHandler.thrown(), readAssignment);
                }
                StackManipulation writeAssignment = assigner.assign(this.target, TypeDescription.THROWABLE.asGenericType(), this.typing);
                if (!writeAssignment.isValid()) {
                    throw new IllegalStateException("Cannot assign " + this.target + " to Throwable");
                }
                return new Target.ForVariable.ReadWrite(TypeDescription.THROWABLE, argumentHandler.thrown(), readAssignment, writeAssignment);
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$ForThrowable$Factory.class */
            protected enum Factory implements Factory<Thrown> {
                INSTANCE;

                protected static Factory<?> of(MethodDescription.InDefinedShape adviceMethod) {
                    return ((TypeDescription) adviceMethod.getDeclaredAnnotations().ofType(OnMethodExit.class).getValue(Advice.ON_THROWABLE).resolve(TypeDescription.class)).represents(NoExceptionHandler.class) ? new Factory.Illegal(Thrown.class) : INSTANCE;
                }

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.Factory
                public Class<Thrown> getAnnotationType() {
                    return Thrown.class;
                }

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.Factory
                public OffsetMapping make(ParameterDescription.InDefinedShape target, AnnotationDescription.Loadable<Thrown> annotation, Factory.AdviceType adviceType) {
                    if (adviceType.isDelegation() && !annotation.load().readOnly()) {
                        throw new IllegalStateException("Cannot use writable " + target + " on read-only parameter");
                    }
                    return new ForThrowable(target.getType(), annotation.load());
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$ForStackManipulation.class */
        public static class ForStackManipulation implements OffsetMapping {
            private final StackManipulation stackManipulation;
            private final TypeDescription.Generic typeDescription;
            private final TypeDescription.Generic targetType;
            private final Assigner.Typing typing;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.typing.equals(((ForStackManipulation) obj).typing) && this.stackManipulation.equals(((ForStackManipulation) obj).stackManipulation) && this.typeDescription.equals(((ForStackManipulation) obj).typeDescription) && this.targetType.equals(((ForStackManipulation) obj).targetType);
            }

            public int hashCode() {
                return (((((((17 * 31) + this.stackManipulation.hashCode()) * 31) + this.typeDescription.hashCode()) * 31) + this.targetType.hashCode()) * 31) + this.typing.hashCode();
            }

            public ForStackManipulation(StackManipulation stackManipulation, TypeDescription.Generic typeDescription, TypeDescription.Generic targetType, Assigner.Typing typing) {
                this.stackManipulation = stackManipulation;
                this.typeDescription = typeDescription;
                this.targetType = targetType;
                this.typing = typing;
            }

            @Override // net.bytebuddy.asm.Advice.OffsetMapping
            public Target resolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, Assigner assigner, ArgumentHandler argumentHandler, Sort sort) {
                StackManipulation assignment = assigner.assign(this.typeDescription, this.targetType, this.typing);
                if (!assignment.isValid()) {
                    throw new IllegalStateException("Cannot assign " + this.typeDescription + " to " + this.targetType);
                }
                return new Target.ForStackManipulation(new StackManipulation.Compound(this.stackManipulation, assignment));
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$ForStackManipulation$Factory.class */
            public static class Factory<T extends Annotation> implements Factory<T> {
                private final Class<T> annotationType;
                private final StackManipulation stackManipulation;
                private final TypeDescription.Generic typeDescription;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.annotationType.equals(((Factory) obj).annotationType) && this.stackManipulation.equals(((Factory) obj).stackManipulation) && this.typeDescription.equals(((Factory) obj).typeDescription);
                }

                public int hashCode() {
                    return (((((17 * 31) + this.annotationType.hashCode()) * 31) + this.stackManipulation.hashCode()) * 31) + this.typeDescription.hashCode();
                }

                public Factory(Class<T> annotationType, TypeDescription typeDescription) {
                    this(annotationType, ClassConstant.of(typeDescription), TypeDescription.CLASS.asGenericType());
                }

                public Factory(Class<T> annotationType, EnumerationDescription enumerationDescription) {
                    this(annotationType, FieldAccess.forEnumeration(enumerationDescription), enumerationDescription.getEnumerationType().asGenericType());
                }

                public Factory(Class<T> annotationType, StackManipulation stackManipulation, TypeDescription.Generic typeDescription) {
                    this.annotationType = annotationType;
                    this.stackManipulation = stackManipulation;
                    this.typeDescription = typeDescription;
                }

                public static <S extends Annotation> Factory<S> of(Class<S> annotationType, Object value) {
                    StackManipulation stackManipulation;
                    TypeDescription typeDescription;
                    if (value == null) {
                        return new OfDefaultValue(annotationType);
                    }
                    if (value instanceof Boolean) {
                        stackManipulation = IntegerConstant.forValue(((Boolean) value).booleanValue());
                        typeDescription = TypeDescription.ForLoadedType.of(Boolean.TYPE);
                    } else if (value instanceof Byte) {
                        stackManipulation = IntegerConstant.forValue(((Byte) value).byteValue());
                        typeDescription = TypeDescription.ForLoadedType.of(Byte.TYPE);
                    } else if (value instanceof Short) {
                        stackManipulation = IntegerConstant.forValue(((Short) value).shortValue());
                        typeDescription = TypeDescription.ForLoadedType.of(Short.TYPE);
                    } else if (value instanceof Character) {
                        stackManipulation = IntegerConstant.forValue(((Character) value).charValue());
                        typeDescription = TypeDescription.ForLoadedType.of(Character.TYPE);
                    } else if (value instanceof Integer) {
                        stackManipulation = IntegerConstant.forValue(((Integer) value).intValue());
                        typeDescription = TypeDescription.ForLoadedType.of(Integer.TYPE);
                    } else if (value instanceof Long) {
                        stackManipulation = LongConstant.forValue(((Long) value).longValue());
                        typeDescription = TypeDescription.ForLoadedType.of(Long.TYPE);
                    } else if (value instanceof Float) {
                        stackManipulation = FloatConstant.forValue(((Float) value).floatValue());
                        typeDescription = TypeDescription.ForLoadedType.of(Float.TYPE);
                    } else if (value instanceof Double) {
                        stackManipulation = DoubleConstant.forValue(((Double) value).doubleValue());
                        typeDescription = TypeDescription.ForLoadedType.of(Double.TYPE);
                    } else if (value instanceof String) {
                        stackManipulation = new TextConstant((String) value);
                        typeDescription = TypeDescription.STRING;
                    } else if (JavaType.METHOD_HANDLE.isInstance(value)) {
                        JavaConstant constant = JavaConstant.MethodHandle.ofLoaded(value);
                        stackManipulation = new JavaConstantValue(constant);
                        typeDescription = constant.getType();
                    } else if (JavaType.METHOD_TYPE.isInstance(value)) {
                        JavaConstant constant2 = JavaConstant.MethodType.ofLoaded(value);
                        stackManipulation = new JavaConstantValue(constant2);
                        typeDescription = constant2.getType();
                    } else {
                        throw new IllegalStateException("Not a constant value: " + value);
                    }
                    return new Factory(annotationType, stackManipulation, typeDescription.asGenericType());
                }

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.Factory
                public Class<T> getAnnotationType() {
                    return this.annotationType;
                }

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.Factory
                public OffsetMapping make(ParameterDescription.InDefinedShape target, AnnotationDescription.Loadable<T> annotation, Factory.AdviceType adviceType) {
                    return new ForStackManipulation(this.stackManipulation, this.typeDescription, target.getType(), Assigner.Typing.STATIC);
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$ForStackManipulation$OfDefaultValue.class */
            public static class OfDefaultValue<T extends Annotation> implements Factory<T> {
                private final Class<T> annotationType;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.annotationType.equals(((OfDefaultValue) obj).annotationType);
                }

                public int hashCode() {
                    return (17 * 31) + this.annotationType.hashCode();
                }

                public OfDefaultValue(Class<T> annotationType) {
                    this.annotationType = annotationType;
                }

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.Factory
                public Class<T> getAnnotationType() {
                    return this.annotationType;
                }

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.Factory
                public OffsetMapping make(ParameterDescription.InDefinedShape target, AnnotationDescription.Loadable<T> annotation, Factory.AdviceType adviceType) {
                    return new ForStackManipulation(DefaultValue.of(target.getType()), target.getType(), target.getType(), Assigner.Typing.STATIC);
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$ForStackManipulation$OfAnnotationProperty.class */
            public static class OfAnnotationProperty<T extends Annotation> implements Factory<T> {
                private final Class<T> annotationType;
                private final MethodDescription.InDefinedShape property;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.annotationType.equals(((OfAnnotationProperty) obj).annotationType) && this.property.equals(((OfAnnotationProperty) obj).property);
                }

                public int hashCode() {
                    return (((17 * 31) + this.annotationType.hashCode()) * 31) + this.property.hashCode();
                }

                protected OfAnnotationProperty(Class<T> annotationType, MethodDescription.InDefinedShape property) {
                    this.annotationType = annotationType;
                    this.property = property;
                }

                public static <S extends Annotation> Factory<S> of(Class<S> annotationType, String property) {
                    if (!annotationType.isAnnotation()) {
                        throw new IllegalArgumentException("Not an annotation type: " + annotationType);
                    }
                    try {
                        return new OfAnnotationProperty(annotationType, new MethodDescription.ForLoadedMethod(annotationType.getMethod(property, new Class[0])));
                    } catch (NoSuchMethodException exception) {
                        throw new IllegalArgumentException("Cannot find a property " + property + " on " + annotationType, exception);
                    }
                }

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.Factory
                public Class<T> getAnnotationType() {
                    return this.annotationType;
                }

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.Factory
                public OffsetMapping make(ParameterDescription.InDefinedShape target, AnnotationDescription.Loadable<T> annotation, Factory.AdviceType adviceType) {
                    Factory<T> factory;
                    Object value = annotation.getValue(this.property).resolve();
                    if (value instanceof TypeDescription) {
                        factory = new Factory<>(this.annotationType, (TypeDescription) value);
                    } else if (value instanceof EnumerationDescription) {
                        factory = new Factory<>(this.annotationType, (EnumerationDescription) value);
                    } else if (value instanceof AnnotationDescription) {
                        throw new IllegalStateException("Cannot bind annotation as fixed value for " + this.property);
                    } else {
                        factory = Factory.of(this.annotationType, value);
                    }
                    return factory.make(target, annotation, adviceType);
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$ForSerializedValue.class */
        public static class ForSerializedValue implements OffsetMapping {
            private final TypeDescription.Generic target;
            private final TypeDescription typeDescription;
            private final StackManipulation deserialization;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.target.equals(((ForSerializedValue) obj).target) && this.typeDescription.equals(((ForSerializedValue) obj).typeDescription) && this.deserialization.equals(((ForSerializedValue) obj).deserialization);
            }

            public int hashCode() {
                return (((((17 * 31) + this.target.hashCode()) * 31) + this.typeDescription.hashCode()) * 31) + this.deserialization.hashCode();
            }

            public ForSerializedValue(TypeDescription.Generic target, TypeDescription typeDescription, StackManipulation deserialization) {
                this.target = target;
                this.typeDescription = typeDescription;
                this.deserialization = deserialization;
            }

            @Override // net.bytebuddy.asm.Advice.OffsetMapping
            public Target resolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, Assigner assigner, ArgumentHandler argumentHandler, Sort sort) {
                StackManipulation assignment = assigner.assign(this.typeDescription.asGenericType(), this.target, Assigner.Typing.DYNAMIC);
                if (!assignment.isValid()) {
                    throw new IllegalStateException("Cannot assign " + this.typeDescription + " to " + this.target);
                }
                return new Target.ForStackManipulation(new StackManipulation.Compound(this.deserialization, assignment));
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OffsetMapping$ForSerializedValue$Factory.class */
            public static class Factory<T extends Annotation> implements Factory<T> {
                private final Class<T> annotationType;
                private final TypeDescription typeDescription;
                private final StackManipulation deserialization;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.annotationType.equals(((Factory) obj).annotationType) && this.typeDescription.equals(((Factory) obj).typeDescription) && this.deserialization.equals(((Factory) obj).deserialization);
                }

                public int hashCode() {
                    return (((((17 * 31) + this.annotationType.hashCode()) * 31) + this.typeDescription.hashCode()) * 31) + this.deserialization.hashCode();
                }

                protected Factory(Class<T> annotationType, TypeDescription typeDescription, StackManipulation deserialization) {
                    this.annotationType = annotationType;
                    this.typeDescription = typeDescription;
                    this.deserialization = deserialization;
                }

                public static <S extends Annotation> Factory<S> of(Class<S> annotationType, Serializable target, Class<?> targetType) {
                    if (!targetType.isInstance(target)) {
                        throw new IllegalArgumentException(target + " is no instance of " + targetType);
                    }
                    return new Factory(annotationType, TypeDescription.ForLoadedType.of(targetType), SerializedConstant.of(target));
                }

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.Factory
                public Class<T> getAnnotationType() {
                    return this.annotationType;
                }

                @Override // net.bytebuddy.asm.Advice.OffsetMapping.Factory
                public OffsetMapping make(ParameterDescription.InDefinedShape target, AnnotationDescription.Loadable<T> annotation, Factory.AdviceType adviceType) {
                    return new ForSerializedValue(target.getType(), this.typeDescription, this.deserialization);
                }
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$ArgumentHandler.class */
    public interface ArgumentHandler {
        public static final int THIS_REFERENCE = 0;

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$ArgumentHandler$Factory.class */
        public enum Factory {
            SIMPLE { // from class: net.bytebuddy.asm.Advice.ArgumentHandler.Factory.1
                @Override // net.bytebuddy.asm.Advice.ArgumentHandler.Factory
                protected ForInstrumentedMethod resolve(MethodDescription instrumentedMethod, TypeDefinition enterType, TypeDefinition exitType, Map<String, TypeDefinition> namedTypes) {
                    return new ForInstrumentedMethod.Default.Simple(instrumentedMethod, exitType, new TreeMap(namedTypes), enterType);
                }
            },
            COPYING { // from class: net.bytebuddy.asm.Advice.ArgumentHandler.Factory.2
                @Override // net.bytebuddy.asm.Advice.ArgumentHandler.Factory
                protected ForInstrumentedMethod resolve(MethodDescription instrumentedMethod, TypeDefinition enterType, TypeDefinition exitType, Map<String, TypeDefinition> namedTypes) {
                    return new ForInstrumentedMethod.Default.Copying(instrumentedMethod, exitType, new TreeMap(namedTypes), enterType);
                }
            };

            protected abstract ForInstrumentedMethod resolve(MethodDescription methodDescription, TypeDefinition typeDefinition, TypeDefinition typeDefinition2, Map<String, TypeDefinition> map);
        }

        int argument(int i);

        int exit();

        int enter();

        int named(String str);

        int returned();

        int thrown();

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$ArgumentHandler$ForInstrumentedMethod.class */
        public interface ForInstrumentedMethod extends ArgumentHandler {
            int variable(int i);

            int prepare(MethodVisitor methodVisitor);

            ForAdvice bindEnter(MethodDescription methodDescription);

            ForAdvice bindExit(MethodDescription methodDescription, boolean z);

            boolean isCopyingArguments();

            List<TypeDescription> getNamedTypes();

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$ArgumentHandler$ForInstrumentedMethod$Default.class */
            public static abstract class Default implements ForInstrumentedMethod {
                protected final MethodDescription instrumentedMethod;
                protected final TypeDefinition exitType;
                protected final TreeMap<String, TypeDefinition> namedTypes;
                protected final TypeDefinition enterType;

                protected Default(MethodDescription instrumentedMethod, TypeDefinition exitType, TreeMap<String, TypeDefinition> namedTypes, TypeDefinition enterType) {
                    this.instrumentedMethod = instrumentedMethod;
                    this.namedTypes = namedTypes;
                    this.exitType = exitType;
                    this.enterType = enterType;
                }

                @Override // net.bytebuddy.asm.Advice.ArgumentHandler
                public int exit() {
                    return this.instrumentedMethod.getStackSize();
                }

                @Override // net.bytebuddy.asm.Advice.ArgumentHandler
                public int named(String name) {
                    return this.instrumentedMethod.getStackSize() + this.exitType.getStackSize().getSize() + StackSize.of(this.namedTypes.headMap(name).values());
                }

                @Override // net.bytebuddy.asm.Advice.ArgumentHandler
                public int enter() {
                    return this.instrumentedMethod.getStackSize() + this.exitType.getStackSize().getSize() + StackSize.of(this.namedTypes.values());
                }

                @Override // net.bytebuddy.asm.Advice.ArgumentHandler
                public int returned() {
                    return this.instrumentedMethod.getStackSize() + this.exitType.getStackSize().getSize() + StackSize.of(this.namedTypes.values()) + this.enterType.getStackSize().getSize();
                }

                @Override // net.bytebuddy.asm.Advice.ArgumentHandler
                public int thrown() {
                    return this.instrumentedMethod.getStackSize() + this.exitType.getStackSize().getSize() + StackSize.of(this.namedTypes.values()) + this.enterType.getStackSize().getSize() + this.instrumentedMethod.getReturnType().getStackSize().getSize();
                }

                @Override // net.bytebuddy.asm.Advice.ArgumentHandler.ForInstrumentedMethod
                public ForAdvice bindEnter(MethodDescription adviceMethod) {
                    return new ForAdvice.Default.ForMethodEnter(this.instrumentedMethod, adviceMethod, this.exitType, this.namedTypes);
                }

                @Override // net.bytebuddy.asm.Advice.ArgumentHandler.ForInstrumentedMethod
                public ForAdvice bindExit(MethodDescription adviceMethod, boolean skipThrowable) {
                    return new ForAdvice.Default.ForMethodExit(this.instrumentedMethod, adviceMethod, this.exitType, this.namedTypes, this.enterType, skipThrowable ? StackSize.ZERO : StackSize.SINGLE);
                }

                @Override // net.bytebuddy.asm.Advice.ArgumentHandler.ForInstrumentedMethod
                public List<TypeDescription> getNamedTypes() {
                    List<TypeDescription> namedTypes = new ArrayList<>(this.namedTypes.size());
                    for (TypeDefinition typeDefinition : this.namedTypes.values()) {
                        namedTypes.add(typeDefinition.asErasure());
                    }
                    return namedTypes;
                }

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$ArgumentHandler$ForInstrumentedMethod$Default$Simple.class */
                protected static class Simple extends Default {
                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass();
                    }

                    public int hashCode() {
                        return 17;
                    }

                    protected Simple(MethodDescription instrumentedMethod, TypeDefinition exitType, TreeMap<String, TypeDefinition> namedTypes, TypeDefinition enterType) {
                        super(instrumentedMethod, exitType, namedTypes, enterType);
                    }

                    @Override // net.bytebuddy.asm.Advice.ArgumentHandler
                    public int argument(int offset) {
                        return offset < this.instrumentedMethod.getStackSize() ? offset : offset + this.exitType.getStackSize().getSize() + StackSize.of(this.namedTypes.values()) + this.enterType.getStackSize().getSize();
                    }

                    @Override // net.bytebuddy.asm.Advice.ArgumentHandler.ForInstrumentedMethod
                    public int variable(int index) {
                        if (index < (this.instrumentedMethod.isStatic() ? 0 : 1) + this.instrumentedMethod.getParameters().size()) {
                            return index;
                        }
                        return index + (this.exitType.represents(Void.TYPE) ? 0 : 1) + StackSize.of(this.namedTypes.values()) + (this.enterType.represents(Void.TYPE) ? 0 : 1);
                    }

                    @Override // net.bytebuddy.asm.Advice.ArgumentHandler.ForInstrumentedMethod
                    public boolean isCopyingArguments() {
                        return false;
                    }

                    @Override // net.bytebuddy.asm.Advice.ArgumentHandler.ForInstrumentedMethod
                    public int prepare(MethodVisitor methodVisitor) {
                        return 0;
                    }
                }

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$ArgumentHandler$ForInstrumentedMethod$Default$Copying.class */
                protected static class Copying extends Default {
                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass();
                    }

                    public int hashCode() {
                        return 17;
                    }

                    protected Copying(MethodDescription instrumentedMethod, TypeDefinition exitType, TreeMap<String, TypeDefinition> namedTypes, TypeDefinition enterType) {
                        super(instrumentedMethod, exitType, namedTypes, enterType);
                    }

                    @Override // net.bytebuddy.asm.Advice.ArgumentHandler
                    public int argument(int offset) {
                        return this.instrumentedMethod.getStackSize() + this.exitType.getStackSize().getSize() + StackSize.of(this.namedTypes.values()) + this.enterType.getStackSize().getSize() + offset;
                    }

                    @Override // net.bytebuddy.asm.Advice.ArgumentHandler.ForInstrumentedMethod
                    public int variable(int index) {
                        return (this.instrumentedMethod.isStatic() ? 0 : 1) + this.instrumentedMethod.getParameters().size() + (this.exitType.represents(Void.TYPE) ? 0 : 1) + this.namedTypes.size() + (this.enterType.represents(Void.TYPE) ? 0 : 1) + index;
                    }

                    @Override // net.bytebuddy.asm.Advice.ArgumentHandler.ForInstrumentedMethod
                    public boolean isCopyingArguments() {
                        return true;
                    }

                    @Override // net.bytebuddy.asm.Advice.ArgumentHandler.ForInstrumentedMethod
                    public int prepare(MethodVisitor methodVisitor) {
                        StackSize stackSize;
                        if (!this.instrumentedMethod.isStatic()) {
                            methodVisitor.visitVarInsn(25, 0);
                            methodVisitor.visitVarInsn(58, this.instrumentedMethod.getStackSize() + this.exitType.getStackSize().getSize() + StackSize.of(this.namedTypes.values()) + this.enterType.getStackSize().getSize());
                            stackSize = StackSize.SINGLE;
                        } else {
                            stackSize = StackSize.ZERO;
                        }
                        Iterator it = this.instrumentedMethod.getParameters().iterator();
                        while (it.hasNext()) {
                            ParameterDescription parameterDescription = (ParameterDescription) it.next();
                            Type type = Type.getType(parameterDescription.getType().asErasure().getDescriptor());
                            methodVisitor.visitVarInsn(type.getOpcode(21), parameterDescription.getOffset());
                            methodVisitor.visitVarInsn(type.getOpcode(54), this.instrumentedMethod.getStackSize() + this.exitType.getStackSize().getSize() + StackSize.of(this.namedTypes.values()) + this.enterType.getStackSize().getSize() + parameterDescription.getOffset());
                            stackSize = stackSize.maximum(parameterDescription.getType().getStackSize());
                        }
                        return stackSize.getSize();
                    }
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$ArgumentHandler$ForAdvice.class */
        public interface ForAdvice extends ArgumentHandler {
            int mapped(int i);

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$ArgumentHandler$ForAdvice$Default.class */
            public static abstract class Default implements ForAdvice {
                protected final MethodDescription instrumentedMethod;
                protected final MethodDescription adviceMethod;
                protected final TypeDefinition exitType;
                protected final TreeMap<String, TypeDefinition> namedTypes;

                protected Default(MethodDescription instrumentedMethod, MethodDescription adviceMethod, TypeDefinition exitType, TreeMap<String, TypeDefinition> namedTypes) {
                    this.instrumentedMethod = instrumentedMethod;
                    this.adviceMethod = adviceMethod;
                    this.exitType = exitType;
                    this.namedTypes = namedTypes;
                }

                @Override // net.bytebuddy.asm.Advice.ArgumentHandler
                public int argument(int offset) {
                    return offset;
                }

                @Override // net.bytebuddy.asm.Advice.ArgumentHandler
                public int exit() {
                    return this.instrumentedMethod.getStackSize();
                }

                @Override // net.bytebuddy.asm.Advice.ArgumentHandler
                public int named(String name) {
                    return this.instrumentedMethod.getStackSize() + this.exitType.getStackSize().getSize() + StackSize.of(this.namedTypes.headMap(name).values());
                }

                @Override // net.bytebuddy.asm.Advice.ArgumentHandler
                public int enter() {
                    return this.instrumentedMethod.getStackSize() + this.exitType.getStackSize().getSize() + StackSize.of(this.namedTypes.values());
                }

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$ArgumentHandler$ForAdvice$Default$ForMethodEnter.class */
                protected static class ForMethodEnter extends Default {
                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass();
                    }

                    public int hashCode() {
                        return 17;
                    }

                    protected ForMethodEnter(MethodDescription instrumentedMethod, MethodDescription adviceMethod, TypeDefinition exitType, TreeMap<String, TypeDefinition> namedTypes) {
                        super(instrumentedMethod, adviceMethod, exitType, namedTypes);
                    }

                    @Override // net.bytebuddy.asm.Advice.ArgumentHandler
                    public int returned() {
                        throw new IllegalStateException("Cannot resolve the return value offset during enter advice");
                    }

                    @Override // net.bytebuddy.asm.Advice.ArgumentHandler
                    public int thrown() {
                        throw new IllegalStateException("Cannot resolve the thrown value offset during enter advice");
                    }

                    @Override // net.bytebuddy.asm.Advice.ArgumentHandler.ForAdvice
                    public int mapped(int offset) {
                        return (((this.instrumentedMethod.getStackSize() + this.exitType.getStackSize().getSize()) + StackSize.of(this.namedTypes.values())) - this.adviceMethod.getStackSize()) + offset;
                    }
                }

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$ArgumentHandler$ForAdvice$Default$ForMethodExit.class */
                protected static class ForMethodExit extends Default {
                    private final TypeDefinition enterType;
                    private final StackSize throwableSize;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.throwableSize.equals(((ForMethodExit) obj).throwableSize) && this.enterType.equals(((ForMethodExit) obj).enterType);
                    }

                    public int hashCode() {
                        return (((17 * 31) + this.enterType.hashCode()) * 31) + this.throwableSize.hashCode();
                    }

                    protected ForMethodExit(MethodDescription instrumentedMethod, MethodDescription adviceMethod, TypeDefinition exitType, TreeMap<String, TypeDefinition> namedTypes, TypeDefinition enterType, StackSize throwableSize) {
                        super(instrumentedMethod, adviceMethod, exitType, namedTypes);
                        this.enterType = enterType;
                        this.throwableSize = throwableSize;
                    }

                    @Override // net.bytebuddy.asm.Advice.ArgumentHandler
                    public int returned() {
                        return this.instrumentedMethod.getStackSize() + this.exitType.getStackSize().getSize() + StackSize.of(this.namedTypes.values()) + this.enterType.getStackSize().getSize();
                    }

                    @Override // net.bytebuddy.asm.Advice.ArgumentHandler
                    public int thrown() {
                        return this.instrumentedMethod.getStackSize() + this.exitType.getStackSize().getSize() + StackSize.of(this.namedTypes.values()) + this.enterType.getStackSize().getSize() + this.instrumentedMethod.getReturnType().getStackSize().getSize();
                    }

                    @Override // net.bytebuddy.asm.Advice.ArgumentHandler.ForAdvice
                    public int mapped(int offset) {
                        return ((((((this.instrumentedMethod.getStackSize() + this.exitType.getStackSize().getSize()) + StackSize.of(this.namedTypes.values())) + this.enterType.getStackSize().getSize()) + this.instrumentedMethod.getReturnType().getStackSize().getSize()) + this.throwableSize.getSize()) - this.adviceMethod.getStackSize()) + offset;
                    }
                }
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$PostProcessor.class */
    public interface PostProcessor {
        StackManipulation resolve(TypeDescription typeDescription, MethodDescription methodDescription, Assigner assigner, ArgumentHandler argumentHandler);

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$PostProcessor$Factory.class */
        public interface Factory {
            PostProcessor make(MethodDescription.InDefinedShape inDefinedShape, boolean z);

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$PostProcessor$Factory$Compound.class */
            public static class Compound implements Factory {
                private final List<Factory> factories;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.factories.equals(((Compound) obj).factories);
                }

                public int hashCode() {
                    return (17 * 31) + this.factories.hashCode();
                }

                public Compound(Factory... factory) {
                    this(Arrays.asList(factory));
                }

                public Compound(List<? extends Factory> factories) {
                    this.factories = new ArrayList();
                    for (Factory factory : factories) {
                        if (factory instanceof Compound) {
                            this.factories.addAll(((Compound) factory).factories);
                        } else if (!(factory instanceof NoOp)) {
                            this.factories.add(factory);
                        }
                    }
                }

                @Override // net.bytebuddy.asm.Advice.PostProcessor.Factory
                public PostProcessor make(MethodDescription.InDefinedShape advice, boolean exit) {
                    List<PostProcessor> postProcessors = new ArrayList<>(this.factories.size());
                    for (Factory factory : this.factories) {
                        postProcessors.add(factory.make(advice, exit));
                    }
                    return new Compound(postProcessors);
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$PostProcessor$NoOp.class */
        public enum NoOp implements PostProcessor, Factory {
            INSTANCE;

            @Override // net.bytebuddy.asm.Advice.PostProcessor
            public StackManipulation resolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, Assigner assigner, ArgumentHandler argumentHandler) {
                return StackManipulation.Trivial.INSTANCE;
            }

            @Override // net.bytebuddy.asm.Advice.PostProcessor.Factory
            public PostProcessor make(MethodDescription.InDefinedShape advice, boolean exit) {
                return this;
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$PostProcessor$Compound.class */
        public static class Compound implements PostProcessor {
            private final List<PostProcessor> postProcessors;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.postProcessors.equals(((Compound) obj).postProcessors);
            }

            public int hashCode() {
                return (17 * 31) + this.postProcessors.hashCode();
            }

            protected Compound(List<PostProcessor> postProcessors) {
                this.postProcessors = postProcessors;
            }

            @Override // net.bytebuddy.asm.Advice.PostProcessor
            public StackManipulation resolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, Assigner assigner, ArgumentHandler argumentHandler) {
                List<StackManipulation> stackManipulations = new ArrayList<>(this.postProcessors.size());
                for (PostProcessor postProcessor : this.postProcessors) {
                    stackManipulations.add(postProcessor.resolve(instrumentedType, instrumentedMethod, assigner, argumentHandler));
                }
                return new StackManipulation.Compound(stackManipulations);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Delegator.class */
    public interface Delegator {
        void apply(MethodVisitor methodVisitor, MethodDescription.InDefinedShape inDefinedShape, TypeDescription typeDescription, MethodDescription methodDescription, boolean z);

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Delegator$ForStaticInvocation.class */
        public enum ForStaticInvocation implements Delegator {
            INSTANCE;

            @Override // net.bytebuddy.asm.Advice.Delegator
            public void apply(MethodVisitor methodVisitor, MethodDescription.InDefinedShape adviceMethod, TypeDescription instrumentedType, MethodDescription instrumentedMethod, boolean exit) {
                methodVisitor.visitMethodInsn(184, adviceMethod.getDeclaringType().getInternalName(), adviceMethod.getInternalName(), adviceMethod.getDescriptor(), false);
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Delegator$ForDynamicInvocation.class */
        public static class ForDynamicInvocation implements Delegator {
            private final MethodDescription.InDefinedShape bootstrapMethod;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.bootstrapMethod.equals(((ForDynamicInvocation) obj).bootstrapMethod);
            }

            public int hashCode() {
                return (17 * 31) + this.bootstrapMethod.hashCode();
            }

            protected ForDynamicInvocation(MethodDescription.InDefinedShape bootstrapMethod) {
                this.bootstrapMethod = bootstrapMethod;
            }

            protected static Delegator of(MethodDescription.InDefinedShape bootstrapMethod) {
                if (!bootstrapMethod.isInvokeBootstrap()) {
                    throw new IllegalArgumentException("Not a suitable bootstrap target: " + bootstrapMethod);
                }
                return new ForDynamicInvocation(bootstrapMethod);
            }

            @Override // net.bytebuddy.asm.Advice.Delegator
            public void apply(MethodVisitor methodVisitor, MethodDescription.InDefinedShape adviceMethod, TypeDescription instrumentedType, MethodDescription instrumentedMethod, boolean exit) {
                Object[] argument;
                if (instrumentedMethod.isTypeInitializer()) {
                    if (!this.bootstrapMethod.isInvokeBootstrap(Arrays.asList(TypeDescription.STRING, TypeDescription.ForLoadedType.of(Integer.TYPE), TypeDescription.CLASS, TypeDescription.STRING))) {
                        throw new IllegalArgumentException(this.bootstrapMethod + " is not accepting advice bootstrap arguments");
                    }
                    Object[] objArr = new Object[4];
                    objArr[0] = adviceMethod.getDeclaringType().getName();
                    objArr[1] = Integer.valueOf(exit ? 1 : 0);
                    objArr[2] = Type.getType(instrumentedType.getDescriptor());
                    objArr[3] = instrumentedMethod.getInternalName();
                    argument = objArr;
                } else if (!this.bootstrapMethod.isInvokeBootstrap(Arrays.asList(TypeDescription.STRING, TypeDescription.ForLoadedType.of(Integer.TYPE), TypeDescription.CLASS, TypeDescription.STRING, JavaType.METHOD_HANDLE.getTypeStub()))) {
                    throw new IllegalArgumentException(this.bootstrapMethod + " is not accepting advice bootstrap arguments");
                } else {
                    Object[] objArr2 = new Object[5];
                    objArr2[0] = adviceMethod.getDeclaringType().getName();
                    objArr2[1] = Integer.valueOf(exit ? 1 : 0);
                    objArr2[2] = Type.getType(instrumentedType.getDescriptor());
                    objArr2[3] = instrumentedMethod.getInternalName();
                    objArr2[4] = JavaConstant.MethodHandle.of(instrumentedMethod.asDefined()).asConstantPoolValue();
                    argument = objArr2;
                }
                methodVisitor.visitInvokeDynamicInsn(adviceMethod.getInternalName(), adviceMethod.getDescriptor(), new Handle(this.bootstrapMethod.isConstructor() ? 8 : 6, this.bootstrapMethod.getDeclaringType().getInternalName(), this.bootstrapMethod.getInternalName(), this.bootstrapMethod.getDescriptor(), false), argument);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$MethodSizeHandler.class */
    public interface MethodSizeHandler {
        public static final int UNDEFINED_SIZE = 32767;

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$MethodSizeHandler$ForAdvice.class */
        public interface ForAdvice extends MethodSizeHandler {
            void requireStackSizePadding(int i);

            void requireLocalVariableLengthPadding(int i);

            void recordMaxima(int i, int i2);
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$MethodSizeHandler$ForInstrumentedMethod.class */
        public interface ForInstrumentedMethod extends MethodSizeHandler {
            ForAdvice bindEnter(MethodDescription.InDefinedShape inDefinedShape);

            ForAdvice bindExit(MethodDescription.InDefinedShape inDefinedShape);

            int compoundStackSize(int i);

            int compoundLocalVariableLength(int i);
        }

        void requireStackSize(int i);

        void requireLocalVariableLength(int i);

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$MethodSizeHandler$NoOp.class */
        public enum NoOp implements ForInstrumentedMethod, ForAdvice {
            INSTANCE;

            @Override // net.bytebuddy.asm.Advice.MethodSizeHandler.ForInstrumentedMethod
            public ForAdvice bindEnter(MethodDescription.InDefinedShape adviceMethod) {
                return this;
            }

            @Override // net.bytebuddy.asm.Advice.MethodSizeHandler.ForInstrumentedMethod
            public ForAdvice bindExit(MethodDescription.InDefinedShape adviceMethod) {
                return this;
            }

            @Override // net.bytebuddy.asm.Advice.MethodSizeHandler.ForInstrumentedMethod
            public int compoundStackSize(int stackSize) {
                return MethodSizeHandler.UNDEFINED_SIZE;
            }

            @Override // net.bytebuddy.asm.Advice.MethodSizeHandler.ForInstrumentedMethod
            public int compoundLocalVariableLength(int localVariableLength) {
                return MethodSizeHandler.UNDEFINED_SIZE;
            }

            @Override // net.bytebuddy.asm.Advice.MethodSizeHandler
            public void requireStackSize(int stackSize) {
            }

            @Override // net.bytebuddy.asm.Advice.MethodSizeHandler
            public void requireLocalVariableLength(int localVariableLength) {
            }

            @Override // net.bytebuddy.asm.Advice.MethodSizeHandler.ForAdvice
            public void requireStackSizePadding(int stackSizePadding) {
            }

            @Override // net.bytebuddy.asm.Advice.MethodSizeHandler.ForAdvice
            public void requireLocalVariableLengthPadding(int localVariableLengthPadding) {
            }

            @Override // net.bytebuddy.asm.Advice.MethodSizeHandler.ForAdvice
            public void recordMaxima(int stackSize, int localVariableLength) {
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$MethodSizeHandler$Default.class */
        public static abstract class Default implements ForInstrumentedMethod {
            protected final MethodDescription instrumentedMethod;
            protected final List<? extends TypeDescription> initialTypes;
            protected final List<? extends TypeDescription> preMethodTypes;
            protected final List<? extends TypeDescription> postMethodTypes;
            protected int stackSize;
            protected int localVariableLength;

            protected Default(MethodDescription instrumentedMethod, List<? extends TypeDescription> initialTypes, List<? extends TypeDescription> preMethodTypes, List<? extends TypeDescription> postMethodTypes) {
                this.instrumentedMethod = instrumentedMethod;
                this.initialTypes = initialTypes;
                this.preMethodTypes = preMethodTypes;
                this.postMethodTypes = postMethodTypes;
            }

            protected static ForInstrumentedMethod of(MethodDescription instrumentedMethod, List<? extends TypeDescription> initialTypes, List<? extends TypeDescription> preMethodTypes, List<? extends TypeDescription> postMethodTypes, boolean copyArguments, int writerFlags) {
                if ((writerFlags & 3) != 0) {
                    return NoOp.INSTANCE;
                }
                if (copyArguments) {
                    return new WithCopiedArguments(instrumentedMethod, initialTypes, preMethodTypes, postMethodTypes);
                }
                return new WithRetainedArguments(instrumentedMethod, initialTypes, preMethodTypes, postMethodTypes);
            }

            @Override // net.bytebuddy.asm.Advice.MethodSizeHandler.ForInstrumentedMethod
            public ForAdvice bindEnter(MethodDescription.InDefinedShape adviceMethod) {
                return new ForAdvice(adviceMethod, this.instrumentedMethod.getStackSize() + StackSize.of(this.initialTypes));
            }

            @Override // net.bytebuddy.asm.Advice.MethodSizeHandler
            public void requireStackSize(int stackSize) {
                this.stackSize = Math.max(this.stackSize, stackSize);
            }

            @Override // net.bytebuddy.asm.Advice.MethodSizeHandler
            public void requireLocalVariableLength(int localVariableLength) {
                this.localVariableLength = Math.max(this.localVariableLength, localVariableLength);
            }

            @Override // net.bytebuddy.asm.Advice.MethodSizeHandler.ForInstrumentedMethod
            public int compoundStackSize(int stackSize) {
                return Math.max(this.stackSize, stackSize);
            }

            @Override // net.bytebuddy.asm.Advice.MethodSizeHandler.ForInstrumentedMethod
            public int compoundLocalVariableLength(int localVariableLength) {
                return Math.max(this.localVariableLength, localVariableLength + StackSize.of(this.postMethodTypes) + StackSize.of(this.initialTypes) + StackSize.of(this.preMethodTypes));
            }

            /* JADX INFO: Access modifiers changed from: protected */
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$MethodSizeHandler$Default$WithRetainedArguments.class */
            public static class WithRetainedArguments extends Default {
                protected WithRetainedArguments(MethodDescription instrumentedMethod, List<? extends TypeDescription> initialTypes, List<? extends TypeDescription> preMethodTypes, List<? extends TypeDescription> postMethodTypes) {
                    super(instrumentedMethod, initialTypes, preMethodTypes, postMethodTypes);
                }

                @Override // net.bytebuddy.asm.Advice.MethodSizeHandler.ForInstrumentedMethod
                public ForAdvice bindExit(MethodDescription.InDefinedShape adviceMethod) {
                    return new ForAdvice(adviceMethod, this.instrumentedMethod.getStackSize() + StackSize.of(this.postMethodTypes) + StackSize.of(this.initialTypes) + StackSize.of(this.preMethodTypes));
                }

                @Override // net.bytebuddy.asm.Advice.MethodSizeHandler.Default, net.bytebuddy.asm.Advice.MethodSizeHandler.ForInstrumentedMethod
                public int compoundLocalVariableLength(int localVariableLength) {
                    return Math.max(this.localVariableLength, localVariableLength + StackSize.of(this.postMethodTypes) + StackSize.of(this.initialTypes) + StackSize.of(this.preMethodTypes));
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$MethodSizeHandler$Default$WithCopiedArguments.class */
            public static class WithCopiedArguments extends Default {
                protected WithCopiedArguments(MethodDescription instrumentedMethod, List<? extends TypeDescription> initialTypes, List<? extends TypeDescription> preMethodTypes, List<? extends TypeDescription> postMethodTypes) {
                    super(instrumentedMethod, initialTypes, preMethodTypes, postMethodTypes);
                }

                @Override // net.bytebuddy.asm.Advice.MethodSizeHandler.ForInstrumentedMethod
                public ForAdvice bindExit(MethodDescription.InDefinedShape adviceMethod) {
                    return new ForAdvice(adviceMethod, (2 * this.instrumentedMethod.getStackSize()) + StackSize.of(this.initialTypes) + StackSize.of(this.preMethodTypes) + StackSize.of(this.postMethodTypes));
                }

                @Override // net.bytebuddy.asm.Advice.MethodSizeHandler.Default, net.bytebuddy.asm.Advice.MethodSizeHandler.ForInstrumentedMethod
                public int compoundLocalVariableLength(int localVariableLength) {
                    return Math.max(this.localVariableLength, localVariableLength + this.instrumentedMethod.getStackSize() + StackSize.of(this.postMethodTypes) + StackSize.of(this.initialTypes) + StackSize.of(this.preMethodTypes));
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$MethodSizeHandler$Default$ForAdvice.class */
            protected class ForAdvice implements ForAdvice {
                private final MethodDescription.InDefinedShape adviceMethod;
                private final int baseLocalVariableLength;
                private int stackSizePadding;
                private int localVariableLengthPadding;

                protected ForAdvice(MethodDescription.InDefinedShape adviceMethod, int baseLocalVariableLength) {
                    this.adviceMethod = adviceMethod;
                    this.baseLocalVariableLength = baseLocalVariableLength;
                }

                @Override // net.bytebuddy.asm.Advice.MethodSizeHandler
                public void requireStackSize(int stackSize) {
                    Default.this.requireStackSize(stackSize);
                }

                @Override // net.bytebuddy.asm.Advice.MethodSizeHandler
                public void requireLocalVariableLength(int localVariableLength) {
                    Default.this.requireLocalVariableLength(localVariableLength);
                }

                @Override // net.bytebuddy.asm.Advice.MethodSizeHandler.ForAdvice
                public void requireStackSizePadding(int stackSizePadding) {
                    this.stackSizePadding = Math.max(this.stackSizePadding, stackSizePadding);
                }

                @Override // net.bytebuddy.asm.Advice.MethodSizeHandler.ForAdvice
                public void requireLocalVariableLengthPadding(int localVariableLengthPadding) {
                    this.localVariableLengthPadding = Math.max(this.localVariableLengthPadding, localVariableLengthPadding);
                }

                @Override // net.bytebuddy.asm.Advice.MethodSizeHandler.ForAdvice
                public void recordMaxima(int stackSize, int localVariableLength) {
                    Default.this.requireStackSize(stackSize + this.stackSizePadding);
                    Default.this.requireLocalVariableLength((localVariableLength - this.adviceMethod.getStackSize()) + this.baseLocalVariableLength + this.localVariableLengthPadding);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$StackMapFrameHandler.class */
    public interface StackMapFrameHandler {

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$StackMapFrameHandler$ForAdvice.class */
        public interface ForAdvice extends StackMapFrameHandler {
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$StackMapFrameHandler$ForInstrumentedMethod.class */
        public interface ForInstrumentedMethod extends StackMapFrameHandler {
            ForAdvice bindEnter(MethodDescription.InDefinedShape inDefinedShape);

            ForAdvice bindExit(MethodDescription.InDefinedShape inDefinedShape);

            int getReaderHint();

            void injectInitializationFrame(MethodVisitor methodVisitor);

            void injectStartFrame(MethodVisitor methodVisitor);

            void injectPostCompletionFrame(MethodVisitor methodVisitor);
        }

        void translateFrame(MethodVisitor methodVisitor, int i, int i2, Object[] objArr, int i3, Object[] objArr2);

        void injectReturnFrame(MethodVisitor methodVisitor);

        void injectExceptionFrame(MethodVisitor methodVisitor);

        void injectCompletionFrame(MethodVisitor methodVisitor);

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$StackMapFrameHandler$NoOp.class */
        public enum NoOp implements ForInstrumentedMethod, ForAdvice {
            INSTANCE;

            @Override // net.bytebuddy.asm.Advice.StackMapFrameHandler.ForInstrumentedMethod
            public ForAdvice bindEnter(MethodDescription.InDefinedShape adviceMethod) {
                return this;
            }

            @Override // net.bytebuddy.asm.Advice.StackMapFrameHandler.ForInstrumentedMethod
            public ForAdvice bindExit(MethodDescription.InDefinedShape adviceMethod) {
                return this;
            }

            @Override // net.bytebuddy.asm.Advice.StackMapFrameHandler.ForInstrumentedMethod
            public int getReaderHint() {
                return 4;
            }

            @Override // net.bytebuddy.asm.Advice.StackMapFrameHandler
            public void translateFrame(MethodVisitor methodVisitor, int type, int localVariableLength, Object[] localVariable, int stackSize, Object[] stack) {
            }

            @Override // net.bytebuddy.asm.Advice.StackMapFrameHandler
            public void injectReturnFrame(MethodVisitor methodVisitor) {
            }

            @Override // net.bytebuddy.asm.Advice.StackMapFrameHandler
            public void injectExceptionFrame(MethodVisitor methodVisitor) {
            }

            @Override // net.bytebuddy.asm.Advice.StackMapFrameHandler
            public void injectCompletionFrame(MethodVisitor methodVisitor) {
            }

            @Override // net.bytebuddy.asm.Advice.StackMapFrameHandler.ForInstrumentedMethod
            public void injectInitializationFrame(MethodVisitor methodVisitor) {
            }

            @Override // net.bytebuddy.asm.Advice.StackMapFrameHandler.ForInstrumentedMethod
            public void injectStartFrame(MethodVisitor methodVisitor) {
            }

            @Override // net.bytebuddy.asm.Advice.StackMapFrameHandler.ForInstrumentedMethod
            public void injectPostCompletionFrame(MethodVisitor methodVisitor) {
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$StackMapFrameHandler$Default.class */
        public static abstract class Default implements ForInstrumentedMethod {
            protected static final Object[] EMPTY = new Object[0];
            protected final TypeDescription instrumentedType;
            protected final MethodDescription instrumentedMethod;
            protected final List<? extends TypeDescription> initialTypes;
            protected final List<? extends TypeDescription> preMethodTypes;
            protected final List<? extends TypeDescription> postMethodTypes;
            protected final boolean expandFrames;
            protected int currentFrameDivergence;

            /* JADX INFO: Access modifiers changed from: protected */
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$StackMapFrameHandler$Default$Initialization.class */
            public enum Initialization {
                UNITIALIZED { // from class: net.bytebuddy.asm.Advice.StackMapFrameHandler.Default.Initialization.1
                    @Override // net.bytebuddy.asm.Advice.StackMapFrameHandler.Default.Initialization
                    protected Object toFrame(TypeDescription typeDescription) {
                        return Opcodes.UNINITIALIZED_THIS;
                    }
                },
                INITIALIZED { // from class: net.bytebuddy.asm.Advice.StackMapFrameHandler.Default.Initialization.2
                    @Override // net.bytebuddy.asm.Advice.StackMapFrameHandler.Default.Initialization
                    protected Object toFrame(TypeDescription typeDescription) {
                        if (typeDescription.represents(Boolean.TYPE) || typeDescription.represents(Byte.TYPE) || typeDescription.represents(Short.TYPE) || typeDescription.represents(Character.TYPE) || typeDescription.represents(Integer.TYPE)) {
                            return Opcodes.INTEGER;
                        }
                        if (typeDescription.represents(Long.TYPE)) {
                            return Opcodes.LONG;
                        }
                        if (typeDescription.represents(Float.TYPE)) {
                            return Opcodes.FLOAT;
                        }
                        if (typeDescription.represents(Double.TYPE)) {
                            return Opcodes.DOUBLE;
                        }
                        return typeDescription.getInternalName();
                    }
                };

                protected abstract Object toFrame(TypeDescription typeDescription);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$StackMapFrameHandler$Default$TranslationMode.class */
            public enum TranslationMode {
                COPY { // from class: net.bytebuddy.asm.Advice.StackMapFrameHandler.Default.TranslationMode.1
                    @Override // net.bytebuddy.asm.Advice.StackMapFrameHandler.Default.TranslationMode
                    protected int copy(TypeDescription instrumentedType, MethodDescription instrumentedMethod, MethodDescription methodDescription, Object[] localVariable, Object[] translated) {
                        int length = instrumentedMethod.getParameters().size() + (instrumentedMethod.isStatic() ? 0 : 1);
                        System.arraycopy(localVariable, 0, translated, 0, length);
                        return length;
                    }

                    @Override // net.bytebuddy.asm.Advice.StackMapFrameHandler.Default.TranslationMode
                    protected boolean isPossibleThisFrameValue(TypeDescription instrumentedType, MethodDescription instrumentedMethod, Object frame) {
                        return (instrumentedMethod.isConstructor() && Opcodes.UNINITIALIZED_THIS.equals(frame)) || Initialization.INITIALIZED.toFrame(instrumentedType).equals(frame);
                    }
                },
                ENTER { // from class: net.bytebuddy.asm.Advice.StackMapFrameHandler.Default.TranslationMode.2
                    @Override // net.bytebuddy.asm.Advice.StackMapFrameHandler.Default.TranslationMode
                    protected int copy(TypeDescription instrumentedType, MethodDescription instrumentedMethod, MethodDescription methodDescription, Object[] localVariable, Object[] translated) {
                        int index = 0;
                        if (!instrumentedMethod.isStatic()) {
                            index = 0 + 1;
                            translated[0] = instrumentedMethod.isConstructor() ? Opcodes.UNINITIALIZED_THIS : Initialization.INITIALIZED.toFrame(instrumentedType);
                        }
                        for (TypeDescription typeDescription : instrumentedMethod.getParameters().asTypeList().asErasures()) {
                            int i = index;
                            index++;
                            translated[i] = Initialization.INITIALIZED.toFrame(typeDescription);
                        }
                        return index;
                    }

                    @Override // net.bytebuddy.asm.Advice.StackMapFrameHandler.Default.TranslationMode
                    protected boolean isPossibleThisFrameValue(TypeDescription instrumentedType, MethodDescription instrumentedMethod, Object frame) {
                        if (instrumentedMethod.isConstructor()) {
                            return Opcodes.UNINITIALIZED_THIS.equals(frame);
                        }
                        return Initialization.INITIALIZED.toFrame(instrumentedType).equals(frame);
                    }
                },
                EXIT { // from class: net.bytebuddy.asm.Advice.StackMapFrameHandler.Default.TranslationMode.3
                    @Override // net.bytebuddy.asm.Advice.StackMapFrameHandler.Default.TranslationMode
                    protected int copy(TypeDescription instrumentedType, MethodDescription instrumentedMethod, MethodDescription methodDescription, Object[] localVariable, Object[] translated) {
                        int index = 0;
                        if (!instrumentedMethod.isStatic()) {
                            index = 0 + 1;
                            translated[0] = Initialization.INITIALIZED.toFrame(instrumentedType);
                        }
                        for (TypeDescription typeDescription : instrumentedMethod.getParameters().asTypeList().asErasures()) {
                            int i = index;
                            index++;
                            translated[i] = Initialization.INITIALIZED.toFrame(typeDescription);
                        }
                        return index;
                    }

                    @Override // net.bytebuddy.asm.Advice.StackMapFrameHandler.Default.TranslationMode
                    protected boolean isPossibleThisFrameValue(TypeDescription instrumentedType, MethodDescription instrumentedMethod, Object frame) {
                        return Initialization.INITIALIZED.toFrame(instrumentedType).equals(frame);
                    }
                };

                protected abstract int copy(TypeDescription typeDescription, MethodDescription methodDescription, MethodDescription methodDescription2, Object[] objArr, Object[] objArr2);

                protected abstract boolean isPossibleThisFrameValue(TypeDescription typeDescription, MethodDescription methodDescription, Object obj);
            }

            protected Default(TypeDescription instrumentedType, MethodDescription instrumentedMethod, List<? extends TypeDescription> initialTypes, List<? extends TypeDescription> preMethodTypes, List<? extends TypeDescription> postMethodTypes, boolean expandFrames) {
                this.instrumentedType = instrumentedType;
                this.instrumentedMethod = instrumentedMethod;
                this.initialTypes = initialTypes;
                this.preMethodTypes = preMethodTypes;
                this.postMethodTypes = postMethodTypes;
                this.expandFrames = expandFrames;
            }

            protected static ForInstrumentedMethod of(TypeDescription instrumentedType, MethodDescription instrumentedMethod, List<? extends TypeDescription> initialTypes, List<? extends TypeDescription> preMethodTypes, List<? extends TypeDescription> postMethodTypes, boolean exitAdvice, boolean copyArguments, ClassFileVersion classFileVersion, int writerFlags, int readerFlags) {
                if ((writerFlags & 2) != 0 || classFileVersion.isLessThan(ClassFileVersion.JAVA_V6)) {
                    return NoOp.INSTANCE;
                }
                if (!exitAdvice) {
                    if (!initialTypes.isEmpty()) {
                        throw new IllegalStateException("Local parameters are not supported if no exit advice is present");
                    }
                    return new Trivial(instrumentedType, instrumentedMethod, (readerFlags & 8) != 0);
                } else if (copyArguments) {
                    return new WithPreservedArguments.UsingArgumentCopy(instrumentedType, instrumentedMethod, initialTypes, preMethodTypes, postMethodTypes, (readerFlags & 8) != 0);
                } else {
                    return new WithPreservedArguments.RequiringConsistentShape(instrumentedType, instrumentedMethod, initialTypes, preMethodTypes, postMethodTypes, (readerFlags & 8) != 0, !instrumentedMethod.isConstructor());
                }
            }

            @Override // net.bytebuddy.asm.Advice.StackMapFrameHandler.ForInstrumentedMethod
            public ForAdvice bindEnter(MethodDescription.InDefinedShape adviceMethod) {
                return new ForAdvice(adviceMethod, this.initialTypes, this.preMethodTypes, TranslationMode.ENTER, this.instrumentedMethod.isConstructor() ? Initialization.UNITIALIZED : Initialization.INITIALIZED);
            }

            @Override // net.bytebuddy.asm.Advice.StackMapFrameHandler.ForInstrumentedMethod
            public int getReaderHint() {
                return this.expandFrames ? 8 : 0;
            }

            protected void translateFrame(MethodVisitor methodVisitor, TranslationMode translationMode, MethodDescription methodDescription, List<? extends TypeDescription> additionalTypes, int type, int localVariableLength, Object[] localVariable, int stackSize, Object[] stack) {
                int offset;
                switch (type) {
                    case -1:
                    case 0:
                        if (methodDescription.getParameters().size() + (methodDescription.isStatic() ? 0 : 1) > localVariableLength) {
                            throw new IllegalStateException("Inconsistent frame length for " + methodDescription + ": " + localVariableLength);
                        }
                        if (methodDescription.isStatic()) {
                            offset = 0;
                        } else if (!translationMode.isPossibleThisFrameValue(this.instrumentedType, this.instrumentedMethod, localVariable[0])) {
                            throw new IllegalStateException(methodDescription + " is inconsistent for 'this' reference: " + localVariable[0]);
                        } else {
                            offset = 1;
                        }
                        for (int index = 0; index < methodDescription.getParameters().size(); index++) {
                            if (!Initialization.INITIALIZED.toFrame(((ParameterDescription) methodDescription.getParameters().get(index)).getType().asErasure()).equals(localVariable[index + offset])) {
                                throw new IllegalStateException(methodDescription + " is inconsistent at " + index + ": " + localVariable[index + offset]);
                            }
                        }
                        Object[] translated = new Object[((localVariableLength - (methodDescription.isStatic() ? 0 : 1)) - methodDescription.getParameters().size()) + (this.instrumentedMethod.isStatic() ? 0 : 1) + this.instrumentedMethod.getParameters().size() + additionalTypes.size()];
                        int index2 = translationMode.copy(this.instrumentedType, this.instrumentedMethod, methodDescription, localVariable, translated);
                        for (TypeDescription typeDescription : additionalTypes) {
                            int i = index2;
                            index2++;
                            translated[i] = Initialization.INITIALIZED.toFrame(typeDescription);
                        }
                        System.arraycopy(localVariable, methodDescription.getParameters().size() + (methodDescription.isStatic() ? 0 : 1), translated, index2, translated.length - index2);
                        localVariableLength = translated.length;
                        localVariable = translated;
                        this.currentFrameDivergence = translated.length - index2;
                        break;
                    case 1:
                        this.currentFrameDivergence += localVariableLength;
                        break;
                    case 2:
                        this.currentFrameDivergence -= localVariableLength;
                        if (this.currentFrameDivergence < 0) {
                            throw new IllegalStateException(methodDescription + " dropped " + Math.abs(this.currentFrameDivergence) + " implicit frames");
                        }
                        break;
                    case 3:
                    case 4:
                        break;
                    default:
                        throw new IllegalArgumentException("Unexpected frame type: " + type);
                }
                methodVisitor.visitFrame(type, localVariableLength, localVariable, stackSize, stack);
            }

            protected void injectFullFrame(MethodVisitor methodVisitor, Initialization initialization, List<? extends TypeDescription> typesInArray, List<? extends TypeDescription> typesOnStack) {
                Object[] localVariable = new Object[this.instrumentedMethod.getParameters().size() + (this.instrumentedMethod.isStatic() ? 0 : 1) + typesInArray.size()];
                int index = 0;
                if (!this.instrumentedMethod.isStatic()) {
                    index = 0 + 1;
                    localVariable[0] = initialization.toFrame(this.instrumentedType);
                }
                for (TypeDescription typeDescription : this.instrumentedMethod.getParameters().asTypeList().asErasures()) {
                    int i = index;
                    index++;
                    localVariable[i] = Initialization.INITIALIZED.toFrame(typeDescription);
                }
                for (TypeDescription typeDescription2 : typesInArray) {
                    int i2 = index;
                    index++;
                    localVariable[i2] = Initialization.INITIALIZED.toFrame(typeDescription2);
                }
                int index2 = 0;
                Object[] stackType = new Object[typesOnStack.size()];
                for (TypeDescription typeDescription3 : typesOnStack) {
                    int i3 = index2;
                    index2++;
                    stackType[i3] = Initialization.INITIALIZED.toFrame(typeDescription3);
                }
                methodVisitor.visitFrame(this.expandFrames ? -1 : 0, localVariable.length, localVariable, stackType.length, stackType);
                this.currentFrameDivergence = 0;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$StackMapFrameHandler$Default$Trivial.class */
            public static class Trivial extends Default {
                protected Trivial(TypeDescription instrumentedType, MethodDescription instrumentedMethod, boolean expandFrames) {
                    super(instrumentedType, instrumentedMethod, Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), expandFrames);
                }

                @Override // net.bytebuddy.asm.Advice.StackMapFrameHandler
                public void translateFrame(MethodVisitor methodVisitor, int type, int localVariableLength, Object[] localVariable, int stackSize, Object[] stack) {
                    methodVisitor.visitFrame(type, localVariableLength, localVariable, stackSize, stack);
                }

                @Override // net.bytebuddy.asm.Advice.StackMapFrameHandler.ForInstrumentedMethod
                public ForAdvice bindExit(MethodDescription.InDefinedShape adviceMethod) {
                    throw new IllegalStateException("Did not expect exit advice " + adviceMethod + " for " + this.instrumentedMethod);
                }

                @Override // net.bytebuddy.asm.Advice.StackMapFrameHandler
                public void injectReturnFrame(MethodVisitor methodVisitor) {
                    throw new IllegalStateException("Did not expect return frame for " + this.instrumentedMethod);
                }

                @Override // net.bytebuddy.asm.Advice.StackMapFrameHandler
                public void injectExceptionFrame(MethodVisitor methodVisitor) {
                    throw new IllegalStateException("Did not expect exception frame for " + this.instrumentedMethod);
                }

                @Override // net.bytebuddy.asm.Advice.StackMapFrameHandler
                public void injectCompletionFrame(MethodVisitor methodVisitor) {
                    throw new IllegalStateException("Did not expect completion frame for " + this.instrumentedMethod);
                }

                @Override // net.bytebuddy.asm.Advice.StackMapFrameHandler.ForInstrumentedMethod
                public void injectPostCompletionFrame(MethodVisitor methodVisitor) {
                    throw new IllegalStateException("Did not expect post completion frame for " + this.instrumentedMethod);
                }

                @Override // net.bytebuddy.asm.Advice.StackMapFrameHandler.ForInstrumentedMethod
                public void injectInitializationFrame(MethodVisitor methodVisitor) {
                }

                @Override // net.bytebuddy.asm.Advice.StackMapFrameHandler.ForInstrumentedMethod
                public void injectStartFrame(MethodVisitor methodVisitor) {
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$StackMapFrameHandler$Default$WithPreservedArguments.class */
            protected static abstract class WithPreservedArguments extends Default {
                protected boolean allowCompactCompletionFrame;

                protected WithPreservedArguments(TypeDescription instrumentedType, MethodDescription instrumentedMethod, List<? extends TypeDescription> initialTypes, List<? extends TypeDescription> preMethodTypes, List<? extends TypeDescription> postMethodTypes, boolean expandFrames, boolean allowCompactCompletionFrame) {
                    super(instrumentedType, instrumentedMethod, initialTypes, preMethodTypes, postMethodTypes, expandFrames);
                    this.allowCompactCompletionFrame = allowCompactCompletionFrame;
                }

                @Override // net.bytebuddy.asm.Advice.StackMapFrameHandler.Default
                @SuppressFBWarnings(value = {"RC_REF_COMPARISON_BAD_PRACTICE"}, justification = "ASM models frames by reference comparison.")
                protected void translateFrame(MethodVisitor methodVisitor, TranslationMode translationMode, MethodDescription methodDescription, List<? extends TypeDescription> additionalTypes, int type, int localVariableLength, Object[] localVariable, int stackSize, Object[] stack) {
                    if (type == 0 && localVariableLength > 0 && localVariable[0] != Opcodes.UNINITIALIZED_THIS) {
                        this.allowCompactCompletionFrame = true;
                    }
                    super.translateFrame(methodVisitor, translationMode, methodDescription, additionalTypes, type, localVariableLength, localVariable, stackSize, stack);
                }

                @Override // net.bytebuddy.asm.Advice.StackMapFrameHandler.ForInstrumentedMethod
                public ForAdvice bindExit(MethodDescription.InDefinedShape adviceMethod) {
                    return new ForAdvice(adviceMethod, CompoundList.of(this.initialTypes, this.preMethodTypes, this.postMethodTypes), Collections.emptyList(), TranslationMode.EXIT, Initialization.INITIALIZED);
                }

                @Override // net.bytebuddy.asm.Advice.StackMapFrameHandler
                public void injectReturnFrame(MethodVisitor methodVisitor) {
                    List<? extends TypeDescription> singletonList;
                    if (!this.expandFrames && this.currentFrameDivergence == 0) {
                        if (this.instrumentedMethod.getReturnType().represents(Void.TYPE)) {
                            methodVisitor.visitFrame(3, EMPTY.length, EMPTY, EMPTY.length, EMPTY);
                            return;
                        } else {
                            methodVisitor.visitFrame(4, EMPTY.length, EMPTY, 1, new Object[]{Initialization.INITIALIZED.toFrame(this.instrumentedMethod.getReturnType().asErasure())});
                            return;
                        }
                    }
                    Initialization initialization = Initialization.INITIALIZED;
                    List<? extends TypeDescription> of = CompoundList.of((List) this.initialTypes, (List) this.preMethodTypes);
                    if (this.instrumentedMethod.getReturnType().represents(Void.TYPE)) {
                        singletonList = Collections.emptyList();
                    } else {
                        singletonList = Collections.singletonList(this.instrumentedMethod.getReturnType().asErasure());
                    }
                    injectFullFrame(methodVisitor, initialization, of, singletonList);
                }

                @Override // net.bytebuddy.asm.Advice.StackMapFrameHandler
                public void injectExceptionFrame(MethodVisitor methodVisitor) {
                    if (!this.expandFrames && this.currentFrameDivergence == 0) {
                        methodVisitor.visitFrame(4, EMPTY.length, EMPTY, 1, new Object[]{Type.getInternalName(Throwable.class)});
                    } else {
                        injectFullFrame(methodVisitor, Initialization.INITIALIZED, CompoundList.of((List) this.initialTypes, (List) this.preMethodTypes), Collections.singletonList(TypeDescription.THROWABLE));
                    }
                }

                @Override // net.bytebuddy.asm.Advice.StackMapFrameHandler
                public void injectCompletionFrame(MethodVisitor methodVisitor) {
                    if (this.allowCompactCompletionFrame && !this.expandFrames && this.currentFrameDivergence == 0 && this.postMethodTypes.size() < 4) {
                        if (this.postMethodTypes.isEmpty()) {
                            methodVisitor.visitFrame(3, EMPTY.length, EMPTY, EMPTY.length, EMPTY);
                            return;
                        }
                        Object[] local = new Object[this.postMethodTypes.size()];
                        int index = 0;
                        for (TypeDescription typeDescription : this.postMethodTypes) {
                            int i = index;
                            index++;
                            local[i] = Initialization.INITIALIZED.toFrame(typeDescription);
                        }
                        methodVisitor.visitFrame(1, local.length, local, EMPTY.length, EMPTY);
                        return;
                    }
                    injectFullFrame(methodVisitor, Initialization.INITIALIZED, CompoundList.of(this.initialTypes, this.preMethodTypes, this.postMethodTypes), Collections.emptyList());
                }

                @Override // net.bytebuddy.asm.Advice.StackMapFrameHandler.ForInstrumentedMethod
                public void injectPostCompletionFrame(MethodVisitor methodVisitor) {
                    if (!this.expandFrames && this.currentFrameDivergence == 0) {
                        methodVisitor.visitFrame(3, EMPTY.length, EMPTY, EMPTY.length, EMPTY);
                    } else {
                        injectFullFrame(methodVisitor, Initialization.INITIALIZED, CompoundList.of(this.initialTypes, this.preMethodTypes, this.postMethodTypes), Collections.emptyList());
                    }
                }

                @Override // net.bytebuddy.asm.Advice.StackMapFrameHandler.ForInstrumentedMethod
                public void injectInitializationFrame(MethodVisitor methodVisitor) {
                    if (!this.initialTypes.isEmpty()) {
                        if (!this.expandFrames && this.initialTypes.size() < 4) {
                            Object[] localVariable = new Object[this.initialTypes.size()];
                            int index = 0;
                            for (TypeDescription typeDescription : this.initialTypes) {
                                int i = index;
                                index++;
                                localVariable[i] = Initialization.INITIALIZED.toFrame(typeDescription);
                            }
                            methodVisitor.visitFrame(1, localVariable.length, localVariable, EMPTY.length, EMPTY);
                            return;
                        }
                        Object[] localVariable2 = new Object[(this.instrumentedMethod.isStatic() ? 0 : 1) + this.instrumentedMethod.getParameters().size() + this.initialTypes.size()];
                        int index2 = 0;
                        if (this.instrumentedMethod.isConstructor()) {
                            index2 = 0 + 1;
                            localVariable2[0] = Opcodes.UNINITIALIZED_THIS;
                        } else if (!this.instrumentedMethod.isStatic()) {
                            index2 = 0 + 1;
                            localVariable2[0] = Initialization.INITIALIZED.toFrame(this.instrumentedType);
                        }
                        for (TypeDescription typeDescription2 : this.instrumentedMethod.getParameters().asTypeList().asErasures()) {
                            int i2 = index2;
                            index2++;
                            localVariable2[i2] = Initialization.INITIALIZED.toFrame(typeDescription2);
                        }
                        for (TypeDescription typeDescription3 : this.initialTypes) {
                            int i3 = index2;
                            index2++;
                            localVariable2[i3] = Initialization.INITIALIZED.toFrame(typeDescription3);
                        }
                        methodVisitor.visitFrame(this.expandFrames ? -1 : 0, localVariable2.length, localVariable2, EMPTY.length, EMPTY);
                    }
                }

                /* JADX INFO: Access modifiers changed from: protected */
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$StackMapFrameHandler$Default$WithPreservedArguments$RequiringConsistentShape.class */
                public static class RequiringConsistentShape extends WithPreservedArguments {
                    protected RequiringConsistentShape(TypeDescription instrumentedType, MethodDescription instrumentedMethod, List<? extends TypeDescription> initialTypes, List<? extends TypeDescription> preMethodTypes, List<? extends TypeDescription> postMethodTypes, boolean expandFrames, boolean allowCompactCompletionFrame) {
                        super(instrumentedType, instrumentedMethod, initialTypes, preMethodTypes, postMethodTypes, expandFrames, allowCompactCompletionFrame);
                    }

                    @Override // net.bytebuddy.asm.Advice.StackMapFrameHandler.ForInstrumentedMethod
                    public void injectStartFrame(MethodVisitor methodVisitor) {
                    }

                    @Override // net.bytebuddy.asm.Advice.StackMapFrameHandler
                    public void translateFrame(MethodVisitor methodVisitor, int type, int localVariableLength, Object[] localVariable, int stackSize, Object[] stack) {
                        translateFrame(methodVisitor, TranslationMode.COPY, this.instrumentedMethod, CompoundList.of((List) this.initialTypes, (List) this.preMethodTypes), type, localVariableLength, localVariable, stackSize, stack);
                    }
                }

                /* JADX INFO: Access modifiers changed from: protected */
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$StackMapFrameHandler$Default$WithPreservedArguments$UsingArgumentCopy.class */
                public static class UsingArgumentCopy extends WithPreservedArguments {
                    protected UsingArgumentCopy(TypeDescription instrumentedType, MethodDescription instrumentedMethod, List<? extends TypeDescription> initialTypes, List<? extends TypeDescription> preMethodTypes, List<? extends TypeDescription> postMethodTypes, boolean expandFrames) {
                        super(instrumentedType, instrumentedMethod, initialTypes, preMethodTypes, postMethodTypes, expandFrames, true);
                    }

                    @Override // net.bytebuddy.asm.Advice.StackMapFrameHandler.ForInstrumentedMethod
                    public void injectStartFrame(MethodVisitor methodVisitor) {
                        if (!this.instrumentedMethod.isStatic() || !this.instrumentedMethod.getParameters().isEmpty()) {
                            if (!this.expandFrames) {
                                if ((this.instrumentedMethod.isStatic() ? 0 : 1) + this.instrumentedMethod.getParameters().size() < 4) {
                                    Object[] localVariable = new Object[(this.instrumentedMethod.isStatic() ? 0 : 1) + this.instrumentedMethod.getParameters().size()];
                                    int index = 0;
                                    if (this.instrumentedMethod.isConstructor()) {
                                        index = 0 + 1;
                                        localVariable[0] = Opcodes.UNINITIALIZED_THIS;
                                    } else if (!this.instrumentedMethod.isStatic()) {
                                        index = 0 + 1;
                                        localVariable[0] = Initialization.INITIALIZED.toFrame(this.instrumentedType);
                                    }
                                    for (TypeDescription typeDescription : this.instrumentedMethod.getParameters().asTypeList().asErasures()) {
                                        int i = index;
                                        index++;
                                        localVariable[i] = Initialization.INITIALIZED.toFrame(typeDescription);
                                    }
                                    methodVisitor.visitFrame(1, localVariable.length, localVariable, EMPTY.length, EMPTY);
                                }
                            }
                            Object[] localVariable2 = new Object[(this.instrumentedMethod.isStatic() ? 0 : 2) + (this.instrumentedMethod.getParameters().size() * 2) + this.initialTypes.size() + this.preMethodTypes.size()];
                            int index2 = 0;
                            if (this.instrumentedMethod.isConstructor()) {
                                index2 = 0 + 1;
                                localVariable2[0] = Opcodes.UNINITIALIZED_THIS;
                            } else if (!this.instrumentedMethod.isStatic()) {
                                index2 = 0 + 1;
                                localVariable2[0] = Initialization.INITIALIZED.toFrame(this.instrumentedType);
                            }
                            for (TypeDescription typeDescription2 : this.instrumentedMethod.getParameters().asTypeList().asErasures()) {
                                int i2 = index2;
                                index2++;
                                localVariable2[i2] = Initialization.INITIALIZED.toFrame(typeDescription2);
                            }
                            for (TypeDescription typeDescription3 : this.initialTypes) {
                                int i3 = index2;
                                index2++;
                                localVariable2[i3] = Initialization.INITIALIZED.toFrame(typeDescription3);
                            }
                            for (TypeDescription typeDescription4 : this.preMethodTypes) {
                                int i4 = index2;
                                index2++;
                                localVariable2[i4] = Initialization.INITIALIZED.toFrame(typeDescription4);
                            }
                            if (this.instrumentedMethod.isConstructor()) {
                                int i5 = index2;
                                index2++;
                                localVariable2[i5] = Opcodes.UNINITIALIZED_THIS;
                            } else if (!this.instrumentedMethod.isStatic()) {
                                int i6 = index2;
                                index2++;
                                localVariable2[i6] = Initialization.INITIALIZED.toFrame(this.instrumentedType);
                            }
                            for (TypeDescription typeDescription5 : this.instrumentedMethod.getParameters().asTypeList().asErasures()) {
                                int i7 = index2;
                                index2++;
                                localVariable2[i7] = Initialization.INITIALIZED.toFrame(typeDescription5);
                            }
                            methodVisitor.visitFrame(this.expandFrames ? -1 : 0, localVariable2.length, localVariable2, EMPTY.length, EMPTY);
                        }
                        this.currentFrameDivergence = (this.instrumentedMethod.isStatic() ? 0 : 1) + this.instrumentedMethod.getParameters().size();
                    }

                    @Override // net.bytebuddy.asm.Advice.StackMapFrameHandler
                    @SuppressFBWarnings(value = {"RC_REF_COMPARISON_BAD_PRACTICE"}, justification = "Reference equality is required by ASM")
                    public void translateFrame(MethodVisitor methodVisitor, int type, int localVariableLength, Object[] localVariable, int stackSize, Object[] stack) {
                        switch (type) {
                            case -1:
                            case 0:
                                Object[] translated = new Object[localVariableLength + (this.instrumentedMethod.isStatic() ? 0 : 1) + this.instrumentedMethod.getParameters().size() + this.initialTypes.size() + this.preMethodTypes.size()];
                                int index = 0;
                                if (this.instrumentedMethod.isConstructor()) {
                                    Initialization initialization = Initialization.INITIALIZED;
                                    int variableIndex = 0;
                                    while (true) {
                                        if (variableIndex < localVariableLength) {
                                            if (localVariable[variableIndex] != Opcodes.UNINITIALIZED_THIS) {
                                                variableIndex++;
                                            } else {
                                                initialization = Initialization.UNITIALIZED;
                                            }
                                        }
                                    }
                                    index = 0 + 1;
                                    translated[0] = initialization.toFrame(this.instrumentedType);
                                } else if (!this.instrumentedMethod.isStatic()) {
                                    index = 0 + 1;
                                    translated[0] = Initialization.INITIALIZED.toFrame(this.instrumentedType);
                                }
                                for (TypeDescription typeDescription : this.instrumentedMethod.getParameters().asTypeList().asErasures()) {
                                    int i = index;
                                    index++;
                                    translated[i] = Initialization.INITIALIZED.toFrame(typeDescription);
                                }
                                for (TypeDescription typeDescription2 : this.initialTypes) {
                                    int i2 = index;
                                    index++;
                                    translated[i2] = Initialization.INITIALIZED.toFrame(typeDescription2);
                                }
                                for (TypeDescription typeDescription3 : this.preMethodTypes) {
                                    int i3 = index;
                                    index++;
                                    translated[i3] = Initialization.INITIALIZED.toFrame(typeDescription3);
                                }
                                System.arraycopy(localVariable, 0, translated, index, localVariableLength);
                                localVariableLength = translated.length;
                                localVariable = translated;
                                this.currentFrameDivergence = localVariableLength;
                                break;
                            case 1:
                                this.currentFrameDivergence += localVariableLength;
                                break;
                            case 2:
                                this.currentFrameDivergence -= localVariableLength;
                                break;
                            case 3:
                            case 4:
                                break;
                            default:
                                throw new IllegalArgumentException("Unexpected frame type: " + type);
                        }
                        methodVisitor.visitFrame(type, localVariableLength, localVariable, stackSize, stack);
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$StackMapFrameHandler$Default$ForAdvice.class */
            protected class ForAdvice implements ForAdvice {
                protected final MethodDescription.InDefinedShape adviceMethod;
                protected final List<? extends TypeDescription> startTypes;
                protected final List<? extends TypeDescription> endTypes;
                protected final TranslationMode translationMode;
                private final Initialization initialization;

                protected ForAdvice(MethodDescription.InDefinedShape adviceMethod, List<? extends TypeDescription> startTypes, List<? extends TypeDescription> endTypes, TranslationMode translationMode, Initialization initialization) {
                    this.adviceMethod = adviceMethod;
                    this.startTypes = startTypes;
                    this.endTypes = endTypes;
                    this.translationMode = translationMode;
                    this.initialization = initialization;
                }

                @Override // net.bytebuddy.asm.Advice.StackMapFrameHandler
                public void translateFrame(MethodVisitor methodVisitor, int type, int localVariableLength, Object[] localVariable, int stackSize, Object[] stack) {
                    Default.this.translateFrame(methodVisitor, this.translationMode, this.adviceMethod, this.startTypes, type, localVariableLength, localVariable, stackSize, stack);
                }

                @Override // net.bytebuddy.asm.Advice.StackMapFrameHandler
                public void injectReturnFrame(MethodVisitor methodVisitor) {
                    List<? extends TypeDescription> singletonList;
                    if (!Default.this.expandFrames && Default.this.currentFrameDivergence == 0) {
                        if (this.adviceMethod.getReturnType().represents(Void.TYPE)) {
                            methodVisitor.visitFrame(3, Default.EMPTY.length, Default.EMPTY, Default.EMPTY.length, Default.EMPTY);
                            return;
                        } else {
                            methodVisitor.visitFrame(4, Default.EMPTY.length, Default.EMPTY, 1, new Object[]{Initialization.INITIALIZED.toFrame(this.adviceMethod.getReturnType().asErasure())});
                            return;
                        }
                    }
                    Default r0 = Default.this;
                    Initialization initialization = this.initialization;
                    List<? extends TypeDescription> list = this.startTypes;
                    if (this.adviceMethod.getReturnType().represents(Void.TYPE)) {
                        singletonList = Collections.emptyList();
                    } else {
                        singletonList = Collections.singletonList(this.adviceMethod.getReturnType().asErasure());
                    }
                    r0.injectFullFrame(methodVisitor, initialization, list, singletonList);
                }

                @Override // net.bytebuddy.asm.Advice.StackMapFrameHandler
                public void injectExceptionFrame(MethodVisitor methodVisitor) {
                    if (!Default.this.expandFrames && Default.this.currentFrameDivergence == 0) {
                        methodVisitor.visitFrame(4, Default.EMPTY.length, Default.EMPTY, 1, new Object[]{Type.getInternalName(Throwable.class)});
                    } else {
                        Default.this.injectFullFrame(methodVisitor, this.initialization, this.startTypes, Collections.singletonList(TypeDescription.THROWABLE));
                    }
                }

                @Override // net.bytebuddy.asm.Advice.StackMapFrameHandler
                public void injectCompletionFrame(MethodVisitor methodVisitor) {
                    if (Default.this.expandFrames) {
                        Default.this.injectFullFrame(methodVisitor, this.initialization, CompoundList.of((List) this.startTypes, (List) this.endTypes), Collections.emptyList());
                    } else if (Default.this.currentFrameDivergence == 0 && this.endTypes.size() < 4) {
                        if (this.endTypes.isEmpty()) {
                            methodVisitor.visitFrame(3, Default.EMPTY.length, Default.EMPTY, Default.EMPTY.length, Default.EMPTY);
                            return;
                        }
                        Object[] local = new Object[this.endTypes.size()];
                        int index = 0;
                        for (TypeDescription typeDescription : this.endTypes) {
                            int i = index;
                            index++;
                            local[i] = Initialization.INITIALIZED.toFrame(typeDescription);
                        }
                        methodVisitor.visitFrame(1, local.length, local, Default.EMPTY.length, Default.EMPTY);
                    } else if (Default.this.currentFrameDivergence < 3 && this.endTypes.isEmpty()) {
                        methodVisitor.visitFrame(2, Default.this.currentFrameDivergence, Default.EMPTY, Default.EMPTY.length, Default.EMPTY);
                    } else {
                        Default.this.injectFullFrame(methodVisitor, this.initialization, CompoundList.of((List) this.startTypes, (List) this.endTypes), Collections.emptyList());
                    }
                }
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$ExceptionHandler.class */
    public interface ExceptionHandler {

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$ExceptionHandler$Default.class */
        public enum Default implements ExceptionHandler {
            SUPPRESSING { // from class: net.bytebuddy.asm.Advice.ExceptionHandler.Default.1
                @Override // net.bytebuddy.asm.Advice.ExceptionHandler
                public StackManipulation resolve(MethodDescription instrumentedMethod, TypeDescription instrumentedType) {
                    return Removal.SINGLE;
                }
            },
            PRINTING { // from class: net.bytebuddy.asm.Advice.ExceptionHandler.Default.2
                @Override // net.bytebuddy.asm.Advice.ExceptionHandler
                public StackManipulation resolve(MethodDescription instrumentedMethod, TypeDescription instrumentedType) {
                    try {
                        return MethodInvocation.invoke((MethodDescription.InDefinedShape) new MethodDescription.ForLoadedMethod(Throwable.class.getMethod("printStackTrace", new Class[0])));
                    } catch (NoSuchMethodException e) {
                        throw new IllegalStateException("Cannot locate Throwable::printStackTrace");
                    }
                }
            }
        }

        StackManipulation resolve(MethodDescription methodDescription, TypeDescription typeDescription);

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$ExceptionHandler$Simple.class */
        public static class Simple implements ExceptionHandler {
            private final StackManipulation stackManipulation;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.stackManipulation.equals(((Simple) obj).stackManipulation);
            }

            public int hashCode() {
                return (17 * 31) + this.stackManipulation.hashCode();
            }

            public Simple(StackManipulation stackManipulation) {
                this.stackManipulation = stackManipulation;
            }

            @Override // net.bytebuddy.asm.Advice.ExceptionHandler
            public StackManipulation resolve(MethodDescription instrumentedMethod, TypeDescription instrumentedType) {
                return this.stackManipulation;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Dispatcher.class */
    public interface Dispatcher {
        public static final MethodVisitor IGNORE_METHOD = null;
        public static final AnnotationVisitor IGNORE_ANNOTATION = null;

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Dispatcher$Bound.class */
        public interface Bound {
            void prepare();

            void initialize();

            void apply();
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Dispatcher$Unresolved.class */
        public interface Unresolved extends Dispatcher {
            boolean isBinary();

            Map<String, TypeDefinition> getNamedTypes();

            Resolved.ForMethodEnter asMethodEnter(List<? extends OffsetMapping.Factory<?>> list, ClassReader classReader, Unresolved unresolved, PostProcessor.Factory factory);

            Resolved.ForMethodExit asMethodExit(List<? extends OffsetMapping.Factory<?>> list, ClassReader classReader, Unresolved unresolved, PostProcessor.Factory factory);
        }

        boolean isAlive();

        TypeDefinition getAdviceType();

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Dispatcher$SuppressionHandler.class */
        public interface SuppressionHandler {

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Dispatcher$SuppressionHandler$Bound.class */
            public interface Bound {
                void onPrepare(MethodVisitor methodVisitor);

                void onStart(MethodVisitor methodVisitor);

                void onEnd(MethodVisitor methodVisitor, Implementation.Context context, MethodSizeHandler.ForAdvice forAdvice, StackMapFrameHandler.ForAdvice forAdvice2, TypeDefinition typeDefinition);

                void onEndWithSkip(MethodVisitor methodVisitor, Implementation.Context context, MethodSizeHandler.ForAdvice forAdvice, StackMapFrameHandler.ForAdvice forAdvice2, TypeDefinition typeDefinition);
            }

            Bound bind(StackManipulation stackManipulation);

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Dispatcher$SuppressionHandler$NoOp.class */
            public enum NoOp implements SuppressionHandler, Bound {
                INSTANCE;

                @Override // net.bytebuddy.asm.Advice.Dispatcher.SuppressionHandler
                public Bound bind(StackManipulation exceptionHandler) {
                    return this;
                }

                @Override // net.bytebuddy.asm.Advice.Dispatcher.SuppressionHandler.Bound
                public void onPrepare(MethodVisitor methodVisitor) {
                }

                @Override // net.bytebuddy.asm.Advice.Dispatcher.SuppressionHandler.Bound
                public void onStart(MethodVisitor methodVisitor) {
                }

                @Override // net.bytebuddy.asm.Advice.Dispatcher.SuppressionHandler.Bound
                public void onEnd(MethodVisitor methodVisitor, Implementation.Context implementationContext, MethodSizeHandler.ForAdvice methodSizeHandler, StackMapFrameHandler.ForAdvice stackMapFrameHandler, TypeDefinition returnType) {
                }

                @Override // net.bytebuddy.asm.Advice.Dispatcher.SuppressionHandler.Bound
                public void onEndWithSkip(MethodVisitor methodVisitor, Implementation.Context implementationContext, MethodSizeHandler.ForAdvice methodSizeHandler, StackMapFrameHandler.ForAdvice stackMapFrameHandler, TypeDefinition returnType) {
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Dispatcher$SuppressionHandler$Suppressing.class */
            public static class Suppressing implements SuppressionHandler {
                private final TypeDescription suppressedType;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.suppressedType.equals(((Suppressing) obj).suppressedType);
                }

                public int hashCode() {
                    return (17 * 31) + this.suppressedType.hashCode();
                }

                protected Suppressing(TypeDescription suppressedType) {
                    this.suppressedType = suppressedType;
                }

                protected static SuppressionHandler of(TypeDescription suppressedType) {
                    return suppressedType.represents(NoExceptionHandler.class) ? NoOp.INSTANCE : new Suppressing(suppressedType);
                }

                @Override // net.bytebuddy.asm.Advice.Dispatcher.SuppressionHandler
                public Bound bind(StackManipulation exceptionHandler) {
                    return new Bound(this.suppressedType, exceptionHandler);
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Dispatcher$SuppressionHandler$Suppressing$Bound.class */
                protected static class Bound implements Bound {
                    private final TypeDescription suppressedType;
                    private final StackManipulation exceptionHandler;
                    private final Label startOfMethod = new Label();
                    private final Label endOfMethod = new Label();

                    protected Bound(TypeDescription suppressedType, StackManipulation exceptionHandler) {
                        this.suppressedType = suppressedType;
                        this.exceptionHandler = exceptionHandler;
                    }

                    @Override // net.bytebuddy.asm.Advice.Dispatcher.SuppressionHandler.Bound
                    public void onPrepare(MethodVisitor methodVisitor) {
                        methodVisitor.visitTryCatchBlock(this.startOfMethod, this.endOfMethod, this.endOfMethod, this.suppressedType.getInternalName());
                    }

                    @Override // net.bytebuddy.asm.Advice.Dispatcher.SuppressionHandler.Bound
                    public void onStart(MethodVisitor methodVisitor) {
                        methodVisitor.visitLabel(this.startOfMethod);
                    }

                    @Override // net.bytebuddy.asm.Advice.Dispatcher.SuppressionHandler.Bound
                    public void onEnd(MethodVisitor methodVisitor, Implementation.Context implementationContext, MethodSizeHandler.ForAdvice methodSizeHandler, StackMapFrameHandler.ForAdvice stackMapFrameHandler, TypeDefinition returnType) {
                        methodVisitor.visitLabel(this.endOfMethod);
                        stackMapFrameHandler.injectExceptionFrame(methodVisitor);
                        methodSizeHandler.requireStackSize(1 + this.exceptionHandler.apply(methodVisitor, implementationContext).getMaximalSize());
                        if (returnType.represents(Boolean.TYPE) || returnType.represents(Byte.TYPE) || returnType.represents(Short.TYPE) || returnType.represents(Character.TYPE) || returnType.represents(Integer.TYPE)) {
                            methodVisitor.visitInsn(3);
                        } else if (returnType.represents(Long.TYPE)) {
                            methodVisitor.visitInsn(9);
                        } else if (returnType.represents(Float.TYPE)) {
                            methodVisitor.visitInsn(11);
                        } else if (returnType.represents(Double.TYPE)) {
                            methodVisitor.visitInsn(14);
                        } else if (!returnType.represents(Void.TYPE)) {
                            methodVisitor.visitInsn(1);
                        }
                    }

                    @Override // net.bytebuddy.asm.Advice.Dispatcher.SuppressionHandler.Bound
                    public void onEndWithSkip(MethodVisitor methodVisitor, Implementation.Context implementationContext, MethodSizeHandler.ForAdvice methodSizeHandler, StackMapFrameHandler.ForAdvice stackMapFrameHandler, TypeDefinition returnType) {
                        Label skipExceptionHandler = new Label();
                        methodVisitor.visitJumpInsn(167, skipExceptionHandler);
                        onEnd(methodVisitor, implementationContext, methodSizeHandler, stackMapFrameHandler, returnType);
                        methodVisitor.visitLabel(skipExceptionHandler);
                        stackMapFrameHandler.injectReturnFrame(methodVisitor);
                    }
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Dispatcher$RelocationHandler.class */
        public interface RelocationHandler {

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Dispatcher$RelocationHandler$Bound.class */
            public interface Bound {
                public static final int NO_REQUIRED_SIZE = 0;

                int apply(MethodVisitor methodVisitor, int i);
            }

            Bound bind(MethodDescription methodDescription, Relocation relocation);

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Dispatcher$RelocationHandler$Relocation.class */
            public interface Relocation {
                void apply(MethodVisitor methodVisitor);

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Dispatcher$RelocationHandler$Relocation$ForLabel.class */
                public static class ForLabel implements Relocation {
                    private final Label label;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.label.equals(((ForLabel) obj).label);
                    }

                    public int hashCode() {
                        return (17 * 31) + this.label.hashCode();
                    }

                    public ForLabel(Label label) {
                        this.label = label;
                    }

                    @Override // net.bytebuddy.asm.Advice.Dispatcher.RelocationHandler.Relocation
                    public void apply(MethodVisitor methodVisitor) {
                        methodVisitor.visitJumpInsn(167, this.label);
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Dispatcher$RelocationHandler$Disabled.class */
            public enum Disabled implements RelocationHandler, Bound {
                INSTANCE;

                @Override // net.bytebuddy.asm.Advice.Dispatcher.RelocationHandler
                public Bound bind(MethodDescription instrumentedMethod, Relocation relocation) {
                    return this;
                }

                @Override // net.bytebuddy.asm.Advice.Dispatcher.RelocationHandler.Bound
                public int apply(MethodVisitor methodVisitor, int offset) {
                    return 0;
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Dispatcher$RelocationHandler$ForValue.class */
            public enum ForValue implements RelocationHandler {
                INTEGER(21, 154, 153, 0) { // from class: net.bytebuddy.asm.Advice.Dispatcher.RelocationHandler.ForValue.1
                    @Override // net.bytebuddy.asm.Advice.Dispatcher.RelocationHandler.ForValue
                    protected void convertValue(MethodVisitor methodVisitor) {
                    }
                },
                LONG(22, 154, 153, 0) { // from class: net.bytebuddy.asm.Advice.Dispatcher.RelocationHandler.ForValue.2
                    @Override // net.bytebuddy.asm.Advice.Dispatcher.RelocationHandler.ForValue
                    protected void convertValue(MethodVisitor methodVisitor) {
                        methodVisitor.visitInsn(136);
                    }
                },
                FLOAT(23, 154, 153, 2) { // from class: net.bytebuddy.asm.Advice.Dispatcher.RelocationHandler.ForValue.3
                    @Override // net.bytebuddy.asm.Advice.Dispatcher.RelocationHandler.ForValue
                    protected void convertValue(MethodVisitor methodVisitor) {
                        methodVisitor.visitInsn(11);
                        methodVisitor.visitInsn(149);
                    }
                },
                DOUBLE(24, 154, 153, 4) { // from class: net.bytebuddy.asm.Advice.Dispatcher.RelocationHandler.ForValue.4
                    @Override // net.bytebuddy.asm.Advice.Dispatcher.RelocationHandler.ForValue
                    protected void convertValue(MethodVisitor methodVisitor) {
                        methodVisitor.visitInsn(14);
                        methodVisitor.visitInsn(151);
                    }
                },
                REFERENCE(25, 199, 198, 0) { // from class: net.bytebuddy.asm.Advice.Dispatcher.RelocationHandler.ForValue.5
                    @Override // net.bytebuddy.asm.Advice.Dispatcher.RelocationHandler.ForValue
                    protected void convertValue(MethodVisitor methodVisitor) {
                    }
                };
                
                private final int load;
                private final int defaultJump;
                private final int nonDefaultJump;
                private final int requiredSize;

                protected abstract void convertValue(MethodVisitor methodVisitor);

                ForValue(int load, int defaultJump, int nonDefaultJump, int requiredSize) {
                    this.load = load;
                    this.defaultJump = defaultJump;
                    this.nonDefaultJump = nonDefaultJump;
                    this.requiredSize = requiredSize;
                }

                protected static RelocationHandler of(TypeDefinition typeDefinition, boolean inverted) {
                    ForValue skipDispatcher;
                    if (typeDefinition.represents(Long.TYPE)) {
                        skipDispatcher = LONG;
                    } else if (typeDefinition.represents(Float.TYPE)) {
                        skipDispatcher = FLOAT;
                    } else if (typeDefinition.represents(Double.TYPE)) {
                        skipDispatcher = DOUBLE;
                    } else if (typeDefinition.represents(Void.TYPE)) {
                        throw new IllegalStateException("Cannot skip on default value for void return type");
                    } else {
                        if (typeDefinition.isPrimitive()) {
                            skipDispatcher = INTEGER;
                        } else {
                            skipDispatcher = REFERENCE;
                        }
                    }
                    if (inverted) {
                        ForValue forValue = skipDispatcher;
                        forValue.getClass();
                        return new Inverted();
                    }
                    return skipDispatcher;
                }

                @Override // net.bytebuddy.asm.Advice.Dispatcher.RelocationHandler
                public Bound bind(MethodDescription instrumentedMethod, Relocation relocation) {
                    return new Bound(instrumentedMethod, relocation, false);
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Dispatcher$RelocationHandler$ForValue$Inverted.class */
                public class Inverted implements RelocationHandler {
                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && ForValue.this.equals(ForValue.this);
                    }

                    public int hashCode() {
                        return (17 * 31) + ForValue.this.hashCode();
                    }

                    protected Inverted() {
                    }

                    @Override // net.bytebuddy.asm.Advice.Dispatcher.RelocationHandler
                    public Bound bind(MethodDescription instrumentedMethod, Relocation relocation) {
                        return new Bound(instrumentedMethod, relocation, true);
                    }
                }

                @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Dispatcher$RelocationHandler$ForValue$Bound.class */
                protected class Bound implements Bound {
                    private final MethodDescription instrumentedMethod;
                    private final Relocation relocation;
                    private final boolean inverted;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.inverted == ((Bound) obj).inverted && ForValue.this.equals(ForValue.this) && this.instrumentedMethod.equals(((Bound) obj).instrumentedMethod) && this.relocation.equals(((Bound) obj).relocation);
                    }

                    public int hashCode() {
                        return (((((((17 * 31) + this.instrumentedMethod.hashCode()) * 31) + this.relocation.hashCode()) * 31) + (this.inverted ? 1 : 0)) * 31) + ForValue.this.hashCode();
                    }

                    protected Bound(MethodDescription instrumentedMethod, Relocation relocation, boolean inverted) {
                        this.instrumentedMethod = instrumentedMethod;
                        this.relocation = relocation;
                        this.inverted = inverted;
                    }

                    @Override // net.bytebuddy.asm.Advice.Dispatcher.RelocationHandler.Bound
                    public int apply(MethodVisitor methodVisitor, int offset) {
                        if (this.instrumentedMethod.isConstructor()) {
                            throw new IllegalStateException("Cannot skip code execution from constructor: " + this.instrumentedMethod);
                        }
                        methodVisitor.visitVarInsn(ForValue.this.load, offset);
                        ForValue.this.convertValue(methodVisitor);
                        Label noSkip = new Label();
                        methodVisitor.visitJumpInsn(this.inverted ? ForValue.this.nonDefaultJump : ForValue.this.defaultJump, noSkip);
                        this.relocation.apply(methodVisitor);
                        methodVisitor.visitLabel(noSkip);
                        return ForValue.this.requiredSize;
                    }
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Dispatcher$RelocationHandler$ForType.class */
            public static class ForType implements RelocationHandler {
                private final TypeDescription typeDescription;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.typeDescription.equals(((ForType) obj).typeDescription);
                }

                public int hashCode() {
                    return (17 * 31) + this.typeDescription.hashCode();
                }

                protected ForType(TypeDescription typeDescription) {
                    this.typeDescription = typeDescription;
                }

                protected static RelocationHandler of(TypeDescription typeDescription, TypeDefinition checkedType) {
                    if (typeDescription.represents(Void.TYPE)) {
                        return Disabled.INSTANCE;
                    }
                    if (typeDescription.represents(OnDefaultValue.class)) {
                        return ForValue.of(checkedType, false);
                    }
                    if (typeDescription.represents(OnNonDefaultValue.class)) {
                        return ForValue.of(checkedType, true);
                    }
                    if (typeDescription.isPrimitive() || checkedType.isPrimitive()) {
                        throw new IllegalStateException("Cannot skip method by instance type for primitive return type " + checkedType);
                    }
                    return new ForType(typeDescription);
                }

                @Override // net.bytebuddy.asm.Advice.Dispatcher.RelocationHandler
                public Bound bind(MethodDescription instrumentedMethod, Relocation relocation) {
                    return new Bound(instrumentedMethod, relocation);
                }

                @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Dispatcher$RelocationHandler$ForType$Bound.class */
                protected class Bound implements Bound {
                    private final MethodDescription instrumentedMethod;
                    private final Relocation relocation;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.instrumentedMethod.equals(((Bound) obj).instrumentedMethod) && this.relocation.equals(((Bound) obj).relocation) && ForType.this.equals(ForType.this);
                    }

                    public int hashCode() {
                        return (((((17 * 31) + this.instrumentedMethod.hashCode()) * 31) + this.relocation.hashCode()) * 31) + ForType.this.hashCode();
                    }

                    protected Bound(MethodDescription instrumentedMethod, Relocation relocation) {
                        this.instrumentedMethod = instrumentedMethod;
                        this.relocation = relocation;
                    }

                    @Override // net.bytebuddy.asm.Advice.Dispatcher.RelocationHandler.Bound
                    public int apply(MethodVisitor methodVisitor, int offset) {
                        if (this.instrumentedMethod.isConstructor()) {
                            throw new IllegalStateException("Cannot skip code execution from constructor: " + this.instrumentedMethod);
                        }
                        methodVisitor.visitVarInsn(25, offset);
                        methodVisitor.visitTypeInsn(193, ForType.this.typeDescription.getInternalName());
                        Label noSkip = new Label();
                        methodVisitor.visitJumpInsn(153, noSkip);
                        this.relocation.apply(methodVisitor);
                        methodVisitor.visitLabel(noSkip);
                        return 0;
                    }
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Dispatcher$Resolved.class */
        public interface Resolved extends Dispatcher {

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Dispatcher$Resolved$ForMethodEnter.class */
            public interface ForMethodEnter extends Resolved {
                boolean isPrependLineNumber();

                Map<String, TypeDefinition> getNamedTypes();
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Dispatcher$Resolved$ForMethodExit.class */
            public interface ForMethodExit extends Resolved {
                TypeDescription getThrowable();

                ArgumentHandler.Factory getArgumentHandlerFactory();
            }

            Bound bind(TypeDescription typeDescription, MethodDescription methodDescription, MethodVisitor methodVisitor, Implementation.Context context, Assigner assigner, ArgumentHandler.ForInstrumentedMethod forInstrumentedMethod, MethodSizeHandler.ForInstrumentedMethod forInstrumentedMethod2, StackMapFrameHandler.ForInstrumentedMethod forInstrumentedMethod3, StackManipulation stackManipulation, RelocationHandler.Relocation relocation);

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Dispatcher$Resolved$AbstractBase.class */
            public static abstract class AbstractBase implements Resolved {
                protected final MethodDescription.InDefinedShape adviceMethod;
                protected final PostProcessor postProcessor;
                protected final Map<Integer, OffsetMapping> offsetMappings;
                protected final SuppressionHandler suppressionHandler;
                protected final RelocationHandler relocationHandler;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.adviceMethod.equals(((AbstractBase) obj).adviceMethod) && this.postProcessor.equals(((AbstractBase) obj).postProcessor) && this.offsetMappings.equals(((AbstractBase) obj).offsetMappings) && this.suppressionHandler.equals(((AbstractBase) obj).suppressionHandler) && this.relocationHandler.equals(((AbstractBase) obj).relocationHandler);
                }

                public int hashCode() {
                    return (((((((((17 * 31) + this.adviceMethod.hashCode()) * 31) + this.postProcessor.hashCode()) * 31) + this.offsetMappings.hashCode()) * 31) + this.suppressionHandler.hashCode()) * 31) + this.relocationHandler.hashCode();
                }

                protected AbstractBase(MethodDescription.InDefinedShape adviceMethod, PostProcessor postProcessor, List<? extends OffsetMapping.Factory<?>> factories, TypeDescription throwableType, TypeDescription relocatableType, OffsetMapping.Factory.AdviceType adviceType) {
                    this.adviceMethod = adviceMethod;
                    this.postProcessor = postProcessor;
                    Map<TypeDescription, OffsetMapping.Factory<?>> offsetMappings = new HashMap<>();
                    for (OffsetMapping.Factory<?> factory : factories) {
                        offsetMappings.put(TypeDescription.ForLoadedType.of(factory.getAnnotationType()), factory);
                    }
                    this.offsetMappings = new LinkedHashMap();
                    for (ParameterDescription.InDefinedShape parameterDescription : adviceMethod.getParameters()) {
                        OffsetMapping offsetMapping = null;
                        for (AnnotationDescription annotationDescription : parameterDescription.getDeclaredAnnotations()) {
                            OffsetMapping.Factory<?> factory2 = offsetMappings.get(annotationDescription.getAnnotationType());
                            if (factory2 != null) {
                                OffsetMapping current = factory2.make(parameterDescription, annotationDescription.prepare(factory2.getAnnotationType()), adviceType);
                                if (offsetMapping == null) {
                                    offsetMapping = current;
                                } else {
                                    throw new IllegalStateException(parameterDescription + " is bound to both " + current + " and " + offsetMapping);
                                }
                            }
                        }
                        this.offsetMappings.put(Integer.valueOf(parameterDescription.getOffset()), offsetMapping == null ? new OffsetMapping.ForArgument.Unresolved(parameterDescription) : offsetMapping);
                    }
                    this.suppressionHandler = SuppressionHandler.Suppressing.of(throwableType);
                    this.relocationHandler = RelocationHandler.ForType.of(relocatableType, adviceMethod.getReturnType());
                }

                @Override // net.bytebuddy.asm.Advice.Dispatcher
                public boolean isAlive() {
                    return true;
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Dispatcher$Inactive.class */
        public enum Inactive implements Unresolved, Resolved.ForMethodEnter, Resolved.ForMethodExit, Bound {
            INSTANCE;

            @Override // net.bytebuddy.asm.Advice.Dispatcher
            public boolean isAlive() {
                return false;
            }

            @Override // net.bytebuddy.asm.Advice.Dispatcher.Unresolved
            public boolean isBinary() {
                return false;
            }

            @Override // net.bytebuddy.asm.Advice.Dispatcher
            public TypeDescription getAdviceType() {
                return TypeDescription.VOID;
            }

            @Override // net.bytebuddy.asm.Advice.Dispatcher.Resolved.ForMethodEnter
            public boolean isPrependLineNumber() {
                return false;
            }

            @Override // net.bytebuddy.asm.Advice.Dispatcher.Unresolved
            public Map<String, TypeDefinition> getNamedTypes() {
                return Collections.emptyMap();
            }

            @Override // net.bytebuddy.asm.Advice.Dispatcher.Resolved.ForMethodExit
            public TypeDescription getThrowable() {
                return NoExceptionHandler.DESCRIPTION;
            }

            @Override // net.bytebuddy.asm.Advice.Dispatcher.Resolved.ForMethodExit
            public ArgumentHandler.Factory getArgumentHandlerFactory() {
                return ArgumentHandler.Factory.SIMPLE;
            }

            @Override // net.bytebuddy.asm.Advice.Dispatcher.Unresolved
            public Resolved.ForMethodEnter asMethodEnter(List<? extends OffsetMapping.Factory<?>> userFactories, ClassReader classReader, Unresolved methodExit, PostProcessor.Factory postProcessorFactory) {
                return this;
            }

            @Override // net.bytebuddy.asm.Advice.Dispatcher.Unresolved
            public Resolved.ForMethodExit asMethodExit(List<? extends OffsetMapping.Factory<?>> userFactories, ClassReader classReader, Unresolved methodEnter, PostProcessor.Factory postProcessorFactory) {
                return this;
            }

            @Override // net.bytebuddy.asm.Advice.Dispatcher.Bound
            public void prepare() {
            }

            @Override // net.bytebuddy.asm.Advice.Dispatcher.Bound
            public void initialize() {
            }

            @Override // net.bytebuddy.asm.Advice.Dispatcher.Bound
            public void apply() {
            }

            @Override // net.bytebuddy.asm.Advice.Dispatcher.Resolved
            public Bound bind(TypeDescription instrumentedType, MethodDescription instrumentedMethod, MethodVisitor methodVisitor, Implementation.Context implementationContext, Assigner assigner, ArgumentHandler.ForInstrumentedMethod argumentHandler, MethodSizeHandler.ForInstrumentedMethod methodSizeHandler, StackMapFrameHandler.ForInstrumentedMethod stackMapFrameHandler, StackManipulation exceptionHandler, RelocationHandler.Relocation relocation) {
                return this;
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Dispatcher$Inlining.class */
        public static class Inlining implements Unresolved {
            protected final MethodDescription.InDefinedShape adviceMethod;
            private final Map<String, TypeDefinition> namedTypes = new HashMap();

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.adviceMethod.equals(((Inlining) obj).adviceMethod) && this.namedTypes.equals(((Inlining) obj).namedTypes);
            }

            public int hashCode() {
                return (((17 * 31) + this.adviceMethod.hashCode()) * 31) + this.namedTypes.hashCode();
            }

            protected Inlining(MethodDescription.InDefinedShape adviceMethod) {
                this.adviceMethod = adviceMethod;
                for (ParameterDescription parameterDescription : adviceMethod.getParameters().filter(ElementMatchers.isAnnotatedWith(Local.class))) {
                    String name = ((Local) parameterDescription.getDeclaredAnnotations().ofType(Local.class).load()).value();
                    TypeDefinition previous = this.namedTypes.put(name, parameterDescription.getType());
                    if (previous != null && !previous.equals(parameterDescription.getType())) {
                        throw new IllegalStateException("Local variable for " + name + " is defined with inconsistent types");
                    }
                }
            }

            @Override // net.bytebuddy.asm.Advice.Dispatcher
            public boolean isAlive() {
                return true;
            }

            @Override // net.bytebuddy.asm.Advice.Dispatcher.Unresolved
            public boolean isBinary() {
                return true;
            }

            @Override // net.bytebuddy.asm.Advice.Dispatcher
            public TypeDescription getAdviceType() {
                return this.adviceMethod.getReturnType().asErasure();
            }

            @Override // net.bytebuddy.asm.Advice.Dispatcher.Unresolved
            public Map<String, TypeDefinition> getNamedTypes() {
                return this.namedTypes;
            }

            @Override // net.bytebuddy.asm.Advice.Dispatcher.Unresolved
            public Resolved.ForMethodEnter asMethodEnter(List<? extends OffsetMapping.Factory<?>> userFactories, ClassReader classReader, Unresolved methodExit, PostProcessor.Factory postProcessorFactory) {
                return Resolved.ForMethodEnter.of(this.adviceMethod, postProcessorFactory.make(this.adviceMethod, false), this.namedTypes, userFactories, methodExit.getAdviceType(), classReader, methodExit.isAlive());
            }

            @Override // net.bytebuddy.asm.Advice.Dispatcher.Unresolved
            public Resolved.ForMethodExit asMethodExit(List<? extends OffsetMapping.Factory<?>> userFactories, ClassReader classReader, Unresolved methodEnter, PostProcessor.Factory postProcessorFactory) {
                Map<String, TypeDefinition> namedTypes = methodEnter.getNamedTypes();
                for (Map.Entry<String, TypeDefinition> entry : this.namedTypes.entrySet()) {
                    TypeDefinition typeDefinition = this.namedTypes.get(entry.getKey());
                    if (typeDefinition == null) {
                        throw new IllegalStateException(this.adviceMethod + " attempts use of undeclared local variable " + entry.getKey());
                    }
                    if (!typeDefinition.equals(entry.getValue())) {
                        throw new IllegalStateException(this.adviceMethod + " does not read variable " + entry.getKey() + " as " + typeDefinition);
                    }
                }
                return Resolved.ForMethodExit.of(this.adviceMethod, postProcessorFactory.make(this.adviceMethod, true), namedTypes, userFactories, classReader, methodEnter.getAdviceType());
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Dispatcher$Inlining$Resolved.class */
            protected static abstract class Resolved extends Resolved.AbstractBase {
                protected final ClassReader classReader;

                protected abstract Map<Integer, TypeDefinition> resolveInitializationTypes(ArgumentHandler argumentHandler);

                protected abstract MethodVisitor apply(MethodVisitor methodVisitor, Implementation.Context context, Assigner assigner, ArgumentHandler.ForInstrumentedMethod forInstrumentedMethod, MethodSizeHandler.ForInstrumentedMethod forInstrumentedMethod2, StackMapFrameHandler.ForInstrumentedMethod forInstrumentedMethod3, TypeDescription typeDescription, MethodDescription methodDescription, SuppressionHandler.Bound bound, RelocationHandler.Bound bound2);

                protected Resolved(MethodDescription.InDefinedShape adviceMethod, PostProcessor postProcessor, List<? extends OffsetMapping.Factory<?>> factories, TypeDescription throwableType, TypeDescription relocatableType, ClassReader classReader) {
                    super(adviceMethod, postProcessor, factories, throwableType, relocatableType, OffsetMapping.Factory.AdviceType.INLINING);
                    this.classReader = classReader;
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Dispatcher$Inlining$Resolved$AdviceMethodInliner.class */
                protected class AdviceMethodInliner extends ClassVisitor implements Bound {
                    protected final TypeDescription instrumentedType;
                    protected final MethodDescription instrumentedMethod;
                    protected final MethodVisitor methodVisitor;
                    protected final Implementation.Context implementationContext;
                    protected final Assigner assigner;
                    protected final ArgumentHandler.ForInstrumentedMethod argumentHandler;
                    protected final MethodSizeHandler.ForInstrumentedMethod methodSizeHandler;
                    protected final StackMapFrameHandler.ForInstrumentedMethod stackMapFrameHandler;
                    protected final SuppressionHandler.Bound suppressionHandler;
                    protected final RelocationHandler.Bound relocationHandler;
                    protected final ClassReader classReader;
                    protected final List<Label> labels;

                    protected AdviceMethodInliner(TypeDescription instrumentedType, MethodDescription instrumentedMethod, MethodVisitor methodVisitor, Implementation.Context implementationContext, Assigner assigner, ArgumentHandler.ForInstrumentedMethod argumentHandler, MethodSizeHandler.ForInstrumentedMethod methodSizeHandler, StackMapFrameHandler.ForInstrumentedMethod stackMapFrameHandler, SuppressionHandler.Bound suppressionHandler, RelocationHandler.Bound relocationHandler, ClassReader classReader) {
                        super(OpenedClassReader.ASM_API);
                        this.instrumentedType = instrumentedType;
                        this.instrumentedMethod = instrumentedMethod;
                        this.methodVisitor = methodVisitor;
                        this.implementationContext = implementationContext;
                        this.assigner = assigner;
                        this.argumentHandler = argumentHandler;
                        this.methodSizeHandler = methodSizeHandler;
                        this.stackMapFrameHandler = stackMapFrameHandler;
                        this.suppressionHandler = suppressionHandler;
                        this.classReader = classReader;
                        this.relocationHandler = relocationHandler;
                        this.labels = new ArrayList();
                    }

                    @Override // net.bytebuddy.asm.Advice.Dispatcher.Bound
                    public void prepare() {
                        this.classReader.accept(new ExceptionTableExtractor(), 6);
                        this.suppressionHandler.onPrepare(this.methodVisitor);
                    }

                    @Override // net.bytebuddy.asm.Advice.Dispatcher.Bound
                    public void initialize() {
                        for (Map.Entry<Integer, TypeDefinition> typeDefinition : Resolved.this.resolveInitializationTypes(this.argumentHandler).entrySet()) {
                            if (typeDefinition.getValue().represents(Boolean.TYPE) || typeDefinition.getValue().represents(Byte.TYPE) || typeDefinition.getValue().represents(Short.TYPE) || typeDefinition.getValue().represents(Character.TYPE) || typeDefinition.getValue().represents(Integer.TYPE)) {
                                this.methodVisitor.visitInsn(3);
                                this.methodVisitor.visitVarInsn(54, typeDefinition.getKey().intValue());
                            } else if (typeDefinition.getValue().represents(Long.TYPE)) {
                                this.methodVisitor.visitInsn(9);
                                this.methodVisitor.visitVarInsn(55, typeDefinition.getKey().intValue());
                            } else if (typeDefinition.getValue().represents(Float.TYPE)) {
                                this.methodVisitor.visitInsn(11);
                                this.methodVisitor.visitVarInsn(56, typeDefinition.getKey().intValue());
                            } else if (typeDefinition.getValue().represents(Double.TYPE)) {
                                this.methodVisitor.visitInsn(14);
                                this.methodVisitor.visitVarInsn(57, typeDefinition.getKey().intValue());
                            } else {
                                this.methodVisitor.visitInsn(1);
                                this.methodVisitor.visitVarInsn(58, typeDefinition.getKey().intValue());
                            }
                            this.methodSizeHandler.requireStackSize(typeDefinition.getValue().getStackSize().getSize());
                        }
                    }

                    @Override // net.bytebuddy.asm.Advice.Dispatcher.Bound
                    public void apply() {
                        this.classReader.accept(this, 2 | this.stackMapFrameHandler.getReaderHint());
                    }

                    @Override // net.bytebuddy.jar.asm.ClassVisitor
                    public MethodVisitor visitMethod(int modifiers, String internalName, String descriptor, String signature, String[] exception) {
                        return (Resolved.this.adviceMethod.getInternalName().equals(internalName) && Resolved.this.adviceMethod.getDescriptor().equals(descriptor)) ? new ExceptionTableSubstitutor(Resolved.this.apply(this.methodVisitor, this.implementationContext, this.assigner, this.argumentHandler, this.methodSizeHandler, this.stackMapFrameHandler, this.instrumentedType, this.instrumentedMethod, this.suppressionHandler, this.relocationHandler)) : Dispatcher.IGNORE_METHOD;
                    }

                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Dispatcher$Inlining$Resolved$AdviceMethodInliner$ExceptionTableExtractor.class */
                    protected class ExceptionTableExtractor extends ClassVisitor {
                        protected ExceptionTableExtractor() {
                            super(OpenedClassReader.ASM_API);
                        }

                        @Override // net.bytebuddy.jar.asm.ClassVisitor
                        public MethodVisitor visitMethod(int modifiers, String internalName, String descriptor, String signature, String[] exception) {
                            return (Resolved.this.adviceMethod.getInternalName().equals(internalName) && Resolved.this.adviceMethod.getDescriptor().equals(descriptor)) ? new ExceptionTableCollector(AdviceMethodInliner.this.methodVisitor) : Dispatcher.IGNORE_METHOD;
                        }
                    }

                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Dispatcher$Inlining$Resolved$AdviceMethodInliner$ExceptionTableCollector.class */
                    protected class ExceptionTableCollector extends MethodVisitor {
                        private final MethodVisitor methodVisitor;

                        protected ExceptionTableCollector(MethodVisitor methodVisitor) {
                            super(OpenedClassReader.ASM_API);
                            this.methodVisitor = methodVisitor;
                        }

                        @Override // net.bytebuddy.jar.asm.MethodVisitor
                        public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
                            this.methodVisitor.visitTryCatchBlock(start, end, handler, type);
                            AdviceMethodInliner.this.labels.addAll(Arrays.asList(start, end, handler));
                        }

                        @Override // net.bytebuddy.jar.asm.MethodVisitor
                        public AnnotationVisitor visitTryCatchAnnotation(int typeReference, TypePath typePath, String descriptor, boolean visible) {
                            return this.methodVisitor.visitTryCatchAnnotation(typeReference, typePath, descriptor, visible);
                        }
                    }

                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Dispatcher$Inlining$Resolved$AdviceMethodInliner$ExceptionTableSubstitutor.class */
                    protected class ExceptionTableSubstitutor extends MethodVisitor {
                        private final Map<Label, Label> substitutions;
                        private int index;

                        protected ExceptionTableSubstitutor(MethodVisitor methodVisitor) {
                            super(OpenedClassReader.ASM_API, methodVisitor);
                            this.substitutions = new IdentityHashMap();
                        }

                        @Override // net.bytebuddy.jar.asm.MethodVisitor
                        public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
                            Map<Label, Label> map = this.substitutions;
                            List<Label> list = AdviceMethodInliner.this.labels;
                            int i = this.index;
                            this.index = i + 1;
                            map.put(start, list.get(i));
                            Map<Label, Label> map2 = this.substitutions;
                            List<Label> list2 = AdviceMethodInliner.this.labels;
                            int i2 = this.index;
                            this.index = i2 + 1;
                            map2.put(end, list2.get(i2));
                            List<Label> list3 = AdviceMethodInliner.this.labels;
                            int i3 = this.index;
                            this.index = i3 + 1;
                            Label actualHandler = list3.get(i3);
                            this.substitutions.put(handler, actualHandler);
                            ((CodeTranslationVisitor) this.mv).propagateHandler(actualHandler);
                        }

                        @Override // net.bytebuddy.jar.asm.MethodVisitor
                        public AnnotationVisitor visitTryCatchAnnotation(int typeReference, TypePath typePath, String descriptor, boolean visible) {
                            return Dispatcher.IGNORE_ANNOTATION;
                        }

                        @Override // net.bytebuddy.jar.asm.MethodVisitor
                        public void visitLabel(Label label) {
                            super.visitLabel(resolve(label));
                        }

                        @Override // net.bytebuddy.jar.asm.MethodVisitor
                        public void visitJumpInsn(int opcode, Label label) {
                            super.visitJumpInsn(opcode, resolve(label));
                        }

                        @Override // net.bytebuddy.jar.asm.MethodVisitor
                        public void visitTableSwitchInsn(int minimum, int maximum, Label defaultOption, Label... label) {
                            super.visitTableSwitchInsn(minimum, maximum, defaultOption, resolve(label));
                        }

                        @Override // net.bytebuddy.jar.asm.MethodVisitor
                        public void visitLookupSwitchInsn(Label defaultOption, int[] keys, Label[] label) {
                            super.visitLookupSwitchInsn(resolve(defaultOption), keys, resolve(label));
                        }

                        private Label[] resolve(Label[] label) {
                            Label[] resolved = new Label[label.length];
                            int index = 0;
                            for (Label aLabel : label) {
                                int i = index;
                                index++;
                                resolved[i] = resolve(aLabel);
                            }
                            return resolved;
                        }

                        private Label resolve(Label label) {
                            Label substitution = this.substitutions.get(label);
                            return substitution == null ? label : substitution;
                        }
                    }
                }

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Dispatcher$Inlining$Resolved$ForMethodEnter.class */
                protected static abstract class ForMethodEnter extends Resolved implements Resolved.ForMethodEnter {
                    private final Map<String, TypeDefinition> namedTypes;
                    private final boolean prependLineNumber;

                    @Override // net.bytebuddy.asm.Advice.Dispatcher.Resolved.AbstractBase
                    public boolean equals(Object obj) {
                        if (super.equals(obj)) {
                            if (this == obj) {
                                return true;
                            }
                            return obj != null && getClass() == obj.getClass() && this.prependLineNumber == ((ForMethodEnter) obj).prependLineNumber && this.namedTypes.equals(((ForMethodEnter) obj).namedTypes);
                        }
                        return false;
                    }

                    @Override // net.bytebuddy.asm.Advice.Dispatcher.Resolved.AbstractBase
                    public int hashCode() {
                        return (((super.hashCode() * 31) + this.namedTypes.hashCode()) * 31) + (this.prependLineNumber ? 1 : 0);
                    }

                    protected ForMethodEnter(MethodDescription.InDefinedShape adviceMethod, PostProcessor postProcessor, Map<String, TypeDefinition> namedTypes, List<? extends OffsetMapping.Factory<?>> userFactories, TypeDefinition exitType, ClassReader classReader) {
                        super(adviceMethod, postProcessor, CompoundList.of(Arrays.asList(OffsetMapping.ForArgument.Unresolved.Factory.INSTANCE, OffsetMapping.ForAllArguments.Factory.INSTANCE, OffsetMapping.ForThisReference.Factory.INSTANCE, OffsetMapping.ForField.Unresolved.Factory.INSTANCE, OffsetMapping.ForOrigin.Factory.INSTANCE, OffsetMapping.ForUnusedValue.Factory.INSTANCE, OffsetMapping.ForStubValue.INSTANCE, OffsetMapping.ForThrowable.Factory.INSTANCE, OffsetMapping.ForExitValue.Factory.of(exitType), new OffsetMapping.ForLocalValue.Factory(namedTypes), new OffsetMapping.Factory.Illegal(Thrown.class), new OffsetMapping.Factory.Illegal(Enter.class), new OffsetMapping.Factory.Illegal(Return.class)), (List) userFactories), (TypeDescription) adviceMethod.getDeclaredAnnotations().ofType(OnMethodEnter.class).getValue(Advice.SUPPRESS_ENTER).resolve(TypeDescription.class), (TypeDescription) adviceMethod.getDeclaredAnnotations().ofType(OnMethodEnter.class).getValue(Advice.SKIP_ON).resolve(TypeDescription.class), classReader);
                        this.namedTypes = namedTypes;
                        this.prependLineNumber = ((Boolean) adviceMethod.getDeclaredAnnotations().ofType(OnMethodEnter.class).getValue(Advice.PREPEND_LINE_NUMBER).resolve(Boolean.class)).booleanValue();
                    }

                    protected static Resolved.ForMethodEnter of(MethodDescription.InDefinedShape adviceMethod, PostProcessor postProcessor, Map<String, TypeDefinition> namedTypes, List<? extends OffsetMapping.Factory<?>> userFactories, TypeDefinition exitType, ClassReader classReader, boolean methodExit) {
                        return methodExit ? new WithRetainedEnterType(adviceMethod, postProcessor, namedTypes, userFactories, exitType, classReader) : new WithDiscardedEnterType(adviceMethod, postProcessor, namedTypes, userFactories, exitType, classReader);
                    }

                    @Override // net.bytebuddy.asm.Advice.Dispatcher.Inlining.Resolved
                    protected Map<Integer, TypeDefinition> resolveInitializationTypes(ArgumentHandler argumentHandler) {
                        SortedMap<Integer, TypeDefinition> namedTypes = new TreeMap<>();
                        for (Map.Entry<String, TypeDefinition> entry : this.namedTypes.entrySet()) {
                            namedTypes.put(Integer.valueOf(argumentHandler.named(entry.getKey())), entry.getValue());
                        }
                        return namedTypes;
                    }

                    @Override // net.bytebuddy.asm.Advice.Dispatcher.Resolved
                    public Bound bind(TypeDescription instrumentedType, MethodDescription instrumentedMethod, MethodVisitor methodVisitor, Implementation.Context implementationContext, Assigner assigner, ArgumentHandler.ForInstrumentedMethod argumentHandler, MethodSizeHandler.ForInstrumentedMethod methodSizeHandler, StackMapFrameHandler.ForInstrumentedMethod stackMapFrameHandler, StackManipulation exceptionHandler, RelocationHandler.Relocation relocation) {
                        return new AdviceMethodInliner(instrumentedType, instrumentedMethod, methodVisitor, implementationContext, assigner, argumentHandler, methodSizeHandler, stackMapFrameHandler, this.suppressionHandler.bind(exceptionHandler), this.relocationHandler.bind(instrumentedMethod, relocation), this.classReader);
                    }

                    @Override // net.bytebuddy.asm.Advice.Dispatcher.Resolved.ForMethodEnter
                    public boolean isPrependLineNumber() {
                        return this.prependLineNumber;
                    }

                    @Override // net.bytebuddy.asm.Advice.Dispatcher.Resolved.ForMethodEnter
                    public Map<String, TypeDefinition> getNamedTypes() {
                        return this.namedTypes;
                    }

                    @Override // net.bytebuddy.asm.Advice.Dispatcher.Inlining.Resolved
                    protected MethodVisitor apply(MethodVisitor methodVisitor, Implementation.Context implementationContext, Assigner assigner, ArgumentHandler.ForInstrumentedMethod argumentHandler, MethodSizeHandler.ForInstrumentedMethod methodSizeHandler, StackMapFrameHandler.ForInstrumentedMethod stackMapFrameHandler, TypeDescription instrumentedType, MethodDescription instrumentedMethod, SuppressionHandler.Bound suppressionHandler, RelocationHandler.Bound relocationHandler) {
                        return doApply(methodVisitor, implementationContext, assigner, argumentHandler.bindEnter(this.adviceMethod), methodSizeHandler.bindEnter(this.adviceMethod), stackMapFrameHandler.bindEnter(this.adviceMethod), instrumentedType, instrumentedMethod, suppressionHandler, relocationHandler);
                    }

                    protected MethodVisitor doApply(MethodVisitor methodVisitor, Implementation.Context implementationContext, Assigner assigner, ArgumentHandler.ForAdvice argumentHandler, MethodSizeHandler.ForAdvice methodSizeHandler, StackMapFrameHandler.ForAdvice stackMapFrameHandler, TypeDescription instrumentedType, MethodDescription instrumentedMethod, SuppressionHandler.Bound suppressionHandler, RelocationHandler.Bound relocationHandler) {
                        Map<Integer, OffsetMapping.Target> offsetMappings = new HashMap<>();
                        for (Map.Entry<Integer, OffsetMapping> entry : this.offsetMappings.entrySet()) {
                            offsetMappings.put(entry.getKey(), entry.getValue().resolve(instrumentedType, instrumentedMethod, assigner, argumentHandler, OffsetMapping.Sort.ENTER));
                        }
                        return new CodeTranslationVisitor(methodVisitor, implementationContext, argumentHandler, methodSizeHandler, stackMapFrameHandler, instrumentedType, instrumentedMethod, assigner, this.adviceMethod, offsetMappings, suppressionHandler, relocationHandler, this.postProcessor, false);
                    }

                    /* JADX INFO: Access modifiers changed from: protected */
                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Dispatcher$Inlining$Resolved$ForMethodEnter$WithRetainedEnterType.class */
                    public static class WithRetainedEnterType extends ForMethodEnter {
                        protected WithRetainedEnterType(MethodDescription.InDefinedShape adviceMethod, PostProcessor postProcessor, Map<String, TypeDefinition> namedTypes, List<? extends OffsetMapping.Factory<?>> userFactories, TypeDefinition exitType, ClassReader classReader) {
                            super(adviceMethod, postProcessor, namedTypes, userFactories, exitType, classReader);
                        }

                        @Override // net.bytebuddy.asm.Advice.Dispatcher
                        public TypeDefinition getAdviceType() {
                            return this.adviceMethod.getReturnType();
                        }
                    }

                    /* JADX INFO: Access modifiers changed from: protected */
                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Dispatcher$Inlining$Resolved$ForMethodEnter$WithDiscardedEnterType.class */
                    public static class WithDiscardedEnterType extends ForMethodEnter {
                        protected WithDiscardedEnterType(MethodDescription.InDefinedShape adviceMethod, PostProcessor postProcessor, Map<String, TypeDefinition> namedTypes, List<? extends OffsetMapping.Factory<?>> userFactories, TypeDefinition exitType, ClassReader classReader) {
                            super(adviceMethod, postProcessor, namedTypes, userFactories, exitType, classReader);
                        }

                        @Override // net.bytebuddy.asm.Advice.Dispatcher
                        public TypeDefinition getAdviceType() {
                            return TypeDescription.VOID;
                        }

                        @Override // net.bytebuddy.asm.Advice.Dispatcher.Inlining.Resolved.ForMethodEnter
                        protected MethodVisitor doApply(MethodVisitor methodVisitor, Implementation.Context implementationContext, Assigner assigner, ArgumentHandler.ForAdvice argumentHandler, MethodSizeHandler.ForAdvice methodSizeHandler, StackMapFrameHandler.ForAdvice stackMapFrameHandler, TypeDescription instrumentedType, MethodDescription instrumentedMethod, SuppressionHandler.Bound suppressionHandler, RelocationHandler.Bound relocationHandler) {
                            methodSizeHandler.requireLocalVariableLengthPadding(this.adviceMethod.getReturnType().getStackSize().getSize());
                            return super.doApply(methodVisitor, implementationContext, assigner, argumentHandler, methodSizeHandler, stackMapFrameHandler, instrumentedType, instrumentedMethod, suppressionHandler, relocationHandler);
                        }
                    }
                }

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Dispatcher$Inlining$Resolved$ForMethodExit.class */
                protected static abstract class ForMethodExit extends Resolved implements Resolved.ForMethodExit {
                    private final boolean backupArguments;

                    @Override // net.bytebuddy.asm.Advice.Dispatcher.Resolved.AbstractBase
                    public boolean equals(Object obj) {
                        if (super.equals(obj)) {
                            if (this == obj) {
                                return true;
                            }
                            return obj != null && getClass() == obj.getClass() && this.backupArguments == ((ForMethodExit) obj).backupArguments;
                        }
                        return false;
                    }

                    @Override // net.bytebuddy.asm.Advice.Dispatcher.Resolved.AbstractBase
                    public int hashCode() {
                        return (super.hashCode() * 31) + (this.backupArguments ? 1 : 0);
                    }

                    protected ForMethodExit(MethodDescription.InDefinedShape adviceMethod, PostProcessor postProcessor, Map<String, TypeDefinition> namedTypes, List<? extends OffsetMapping.Factory<?>> userFactories, ClassReader classReader, TypeDefinition enterType) {
                        super(adviceMethod, postProcessor, CompoundList.of(Arrays.asList(OffsetMapping.ForArgument.Unresolved.Factory.INSTANCE, OffsetMapping.ForAllArguments.Factory.INSTANCE, OffsetMapping.ForThisReference.Factory.INSTANCE, OffsetMapping.ForField.Unresolved.Factory.INSTANCE, OffsetMapping.ForOrigin.Factory.INSTANCE, OffsetMapping.ForUnusedValue.Factory.INSTANCE, OffsetMapping.ForStubValue.INSTANCE, OffsetMapping.ForEnterValue.Factory.of(enterType), OffsetMapping.ForExitValue.Factory.of(adviceMethod.getReturnType()), new OffsetMapping.ForLocalValue.Factory(namedTypes), OffsetMapping.ForReturnValue.Factory.INSTANCE, OffsetMapping.ForThrowable.Factory.of(adviceMethod)), (List) userFactories), (TypeDescription) adviceMethod.getDeclaredAnnotations().ofType(OnMethodExit.class).getValue(Advice.SUPPRESS_EXIT).resolve(TypeDescription.class), (TypeDescription) adviceMethod.getDeclaredAnnotations().ofType(OnMethodExit.class).getValue(Advice.REPEAT_ON).resolve(TypeDescription.class), classReader);
                        this.backupArguments = ((Boolean) adviceMethod.getDeclaredAnnotations().ofType(OnMethodExit.class).getValue(Advice.BACKUP_ARGUMENTS).resolve(Boolean.class)).booleanValue();
                    }

                    protected static Resolved.ForMethodExit of(MethodDescription.InDefinedShape adviceMethod, PostProcessor postProcessor, Map<String, TypeDefinition> namedTypes, List<? extends OffsetMapping.Factory<?>> userFactories, ClassReader classReader, TypeDefinition enterType) {
                        TypeDescription throwable = (TypeDescription) adviceMethod.getDeclaredAnnotations().ofType(OnMethodExit.class).getValue(Advice.ON_THROWABLE).resolve(TypeDescription.class);
                        return throwable.represents(NoExceptionHandler.class) ? new WithoutExceptionHandler(adviceMethod, postProcessor, namedTypes, userFactories, classReader, enterType) : new WithExceptionHandler(adviceMethod, postProcessor, namedTypes, userFactories, classReader, enterType, throwable);
                    }

                    @Override // net.bytebuddy.asm.Advice.Dispatcher.Inlining.Resolved
                    protected Map<Integer, TypeDefinition> resolveInitializationTypes(ArgumentHandler argumentHandler) {
                        if (this.adviceMethod.getReturnType().represents(Void.TYPE)) {
                            return Collections.emptyMap();
                        }
                        return Collections.singletonMap(Integer.valueOf(argumentHandler.exit()), this.adviceMethod.getReturnType());
                    }

                    @Override // net.bytebuddy.asm.Advice.Dispatcher.Inlining.Resolved
                    protected MethodVisitor apply(MethodVisitor methodVisitor, Implementation.Context implementationContext, Assigner assigner, ArgumentHandler.ForInstrumentedMethod argumentHandler, MethodSizeHandler.ForInstrumentedMethod methodSizeHandler, StackMapFrameHandler.ForInstrumentedMethod stackMapFrameHandler, TypeDescription instrumentedType, MethodDescription instrumentedMethod, SuppressionHandler.Bound suppressionHandler, RelocationHandler.Bound relocationHandler) {
                        return doApply(methodVisitor, implementationContext, assigner, argumentHandler.bindExit(this.adviceMethod, getThrowable().represents(NoExceptionHandler.class)), methodSizeHandler.bindExit(this.adviceMethod), stackMapFrameHandler.bindExit(this.adviceMethod), instrumentedType, instrumentedMethod, suppressionHandler, relocationHandler);
                    }

                    private MethodVisitor doApply(MethodVisitor methodVisitor, Implementation.Context implementationContext, Assigner assigner, ArgumentHandler.ForAdvice argumentHandler, MethodSizeHandler.ForAdvice methodSizeHandler, StackMapFrameHandler.ForAdvice stackMapFrameHandler, TypeDescription instrumentedType, MethodDescription instrumentedMethod, SuppressionHandler.Bound suppressionHandler, RelocationHandler.Bound relocationHandler) {
                        Map<Integer, OffsetMapping.Target> offsetMappings = new HashMap<>();
                        for (Map.Entry<Integer, OffsetMapping> entry : this.offsetMappings.entrySet()) {
                            offsetMappings.put(entry.getKey(), entry.getValue().resolve(instrumentedType, instrumentedMethod, assigner, argumentHandler, OffsetMapping.Sort.EXIT));
                        }
                        return new CodeTranslationVisitor(methodVisitor, implementationContext, argumentHandler, methodSizeHandler, stackMapFrameHandler, instrumentedType, instrumentedMethod, assigner, this.adviceMethod, offsetMappings, suppressionHandler, relocationHandler, this.postProcessor, true);
                    }

                    @Override // net.bytebuddy.asm.Advice.Dispatcher.Resolved.ForMethodExit
                    public ArgumentHandler.Factory getArgumentHandlerFactory() {
                        return this.backupArguments ? ArgumentHandler.Factory.COPYING : ArgumentHandler.Factory.SIMPLE;
                    }

                    @Override // net.bytebuddy.asm.Advice.Dispatcher
                    public TypeDefinition getAdviceType() {
                        return this.adviceMethod.getReturnType();
                    }

                    @Override // net.bytebuddy.asm.Advice.Dispatcher.Resolved
                    public Bound bind(TypeDescription instrumentedType, MethodDescription instrumentedMethod, MethodVisitor methodVisitor, Implementation.Context implementationContext, Assigner assigner, ArgumentHandler.ForInstrumentedMethod argumentHandler, MethodSizeHandler.ForInstrumentedMethod methodSizeHandler, StackMapFrameHandler.ForInstrumentedMethod stackMapFrameHandler, StackManipulation exceptionHandler, RelocationHandler.Relocation relocation) {
                        return new AdviceMethodInliner(instrumentedType, instrumentedMethod, methodVisitor, implementationContext, assigner, argumentHandler, methodSizeHandler, stackMapFrameHandler, this.suppressionHandler.bind(exceptionHandler), this.relocationHandler.bind(instrumentedMethod, relocation), this.classReader);
                    }

                    /* JADX INFO: Access modifiers changed from: protected */
                    @HashCodeAndEqualsPlugin.Enhance
                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Dispatcher$Inlining$Resolved$ForMethodExit$WithExceptionHandler.class */
                    public static class WithExceptionHandler extends ForMethodExit {
                        private final TypeDescription throwable;

                        @Override // net.bytebuddy.asm.Advice.Dispatcher.Inlining.Resolved.ForMethodExit, net.bytebuddy.asm.Advice.Dispatcher.Resolved.AbstractBase
                        public boolean equals(Object obj) {
                            if (super.equals(obj)) {
                                if (this == obj) {
                                    return true;
                                }
                                return obj != null && getClass() == obj.getClass() && this.throwable.equals(((WithExceptionHandler) obj).throwable);
                            }
                            return false;
                        }

                        @Override // net.bytebuddy.asm.Advice.Dispatcher.Inlining.Resolved.ForMethodExit, net.bytebuddy.asm.Advice.Dispatcher.Resolved.AbstractBase
                        public int hashCode() {
                            return (super.hashCode() * 31) + this.throwable.hashCode();
                        }

                        protected WithExceptionHandler(MethodDescription.InDefinedShape adviceMethod, PostProcessor postProcessor, Map<String, TypeDefinition> namedTypes, List<? extends OffsetMapping.Factory<?>> userFactories, ClassReader classReader, TypeDefinition enterType, TypeDescription throwable) {
                            super(adviceMethod, postProcessor, namedTypes, userFactories, classReader, enterType);
                            this.throwable = throwable;
                        }

                        @Override // net.bytebuddy.asm.Advice.Dispatcher.Resolved.ForMethodExit
                        public TypeDescription getThrowable() {
                            return this.throwable;
                        }
                    }

                    /* JADX INFO: Access modifiers changed from: protected */
                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Dispatcher$Inlining$Resolved$ForMethodExit$WithoutExceptionHandler.class */
                    public static class WithoutExceptionHandler extends ForMethodExit {
                        protected WithoutExceptionHandler(MethodDescription.InDefinedShape adviceMethod, PostProcessor postProcessor, Map<String, TypeDefinition> namedTypes, List<? extends OffsetMapping.Factory<?>> userFactories, ClassReader classReader, TypeDefinition enterType) {
                            super(adviceMethod, postProcessor, namedTypes, userFactories, classReader, enterType);
                        }

                        @Override // net.bytebuddy.asm.Advice.Dispatcher.Resolved.ForMethodExit
                        public TypeDescription getThrowable() {
                            return NoExceptionHandler.DESCRIPTION;
                        }
                    }
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Dispatcher$Inlining$CodeTranslationVisitor.class */
            public static class CodeTranslationVisitor extends MethodVisitor {
                protected final MethodVisitor methodVisitor;
                protected final Implementation.Context implementationContext;
                protected final ArgumentHandler.ForAdvice argumentHandler;
                protected final MethodSizeHandler.ForAdvice methodSizeHandler;
                protected final StackMapFrameHandler.ForAdvice stackMapFrameHandler;
                private final TypeDescription instrumentedType;
                private final MethodDescription instrumentedMethod;
                private final Assigner assigner;
                protected final MethodDescription.InDefinedShape adviceMethod;
                private final Map<Integer, OffsetMapping.Target> offsetMappings;
                private final SuppressionHandler.Bound suppressionHandler;
                private final RelocationHandler.Bound relocationHandler;
                private final PostProcessor postProcessor;
                private final boolean exit;
                protected final Label endOfMethod;

                protected CodeTranslationVisitor(MethodVisitor methodVisitor, Implementation.Context implementationContext, ArgumentHandler.ForAdvice argumentHandler, MethodSizeHandler.ForAdvice methodSizeHandler, StackMapFrameHandler.ForAdvice stackMapFrameHandler, TypeDescription instrumentedType, MethodDescription instrumentedMethod, Assigner assigner, MethodDescription.InDefinedShape adviceMethod, Map<Integer, OffsetMapping.Target> offsetMappings, SuppressionHandler.Bound suppressionHandler, RelocationHandler.Bound relocationHandler, PostProcessor postProcessor, boolean exit) {
                    super(OpenedClassReader.ASM_API, new StackAwareMethodVisitor(methodVisitor, instrumentedMethod));
                    this.methodVisitor = methodVisitor;
                    this.implementationContext = implementationContext;
                    this.argumentHandler = argumentHandler;
                    this.methodSizeHandler = methodSizeHandler;
                    this.stackMapFrameHandler = stackMapFrameHandler;
                    this.instrumentedType = instrumentedType;
                    this.instrumentedMethod = instrumentedMethod;
                    this.assigner = assigner;
                    this.adviceMethod = adviceMethod;
                    this.offsetMappings = offsetMappings;
                    this.suppressionHandler = suppressionHandler;
                    this.relocationHandler = relocationHandler;
                    this.postProcessor = postProcessor;
                    this.exit = exit;
                    this.endOfMethod = new Label();
                }

                protected void propagateHandler(Label label) {
                    ((StackAwareMethodVisitor) this.mv).register(label, Collections.singletonList(StackSize.SINGLE));
                }

                @Override // net.bytebuddy.jar.asm.MethodVisitor
                public void visitParameter(String name, int modifiers) {
                }

                @Override // net.bytebuddy.jar.asm.MethodVisitor
                public void visitAnnotableParameterCount(int count, boolean visible) {
                }

                @Override // net.bytebuddy.jar.asm.MethodVisitor
                public AnnotationVisitor visitAnnotationDefault() {
                    return Dispatcher.IGNORE_ANNOTATION;
                }

                @Override // net.bytebuddy.jar.asm.MethodVisitor
                public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
                    return Dispatcher.IGNORE_ANNOTATION;
                }

                @Override // net.bytebuddy.jar.asm.MethodVisitor
                public AnnotationVisitor visitTypeAnnotation(int typeReference, TypePath typePath, String descriptor, boolean visible) {
                    return Dispatcher.IGNORE_ANNOTATION;
                }

                @Override // net.bytebuddy.jar.asm.MethodVisitor
                public AnnotationVisitor visitParameterAnnotation(int index, String descriptor, boolean visible) {
                    return Dispatcher.IGNORE_ANNOTATION;
                }

                @Override // net.bytebuddy.jar.asm.MethodVisitor
                public void visitAttribute(Attribute attribute) {
                }

                @Override // net.bytebuddy.jar.asm.MethodVisitor
                public void visitCode() {
                    this.suppressionHandler.onStart(this.methodVisitor);
                }

                @Override // net.bytebuddy.jar.asm.MethodVisitor
                public void visitFrame(int type, int localVariableLength, Object[] localVariable, int stackSize, Object[] stack) {
                    this.stackMapFrameHandler.translateFrame(this.methodVisitor, type, localVariableLength, localVariable, stackSize, stack);
                }

                @Override // net.bytebuddy.jar.asm.MethodVisitor
                public void visitVarInsn(int opcode, int offset) {
                    StackManipulation stackManipulation;
                    StackSize expectedGrowth;
                    OffsetMapping.Target target = this.offsetMappings.get(Integer.valueOf(offset));
                    if (target != null) {
                        switch (opcode) {
                            case 21:
                            case 23:
                            case 25:
                                stackManipulation = target.resolveRead();
                                expectedGrowth = StackSize.SINGLE;
                                break;
                            case 22:
                            case 24:
                                stackManipulation = target.resolveRead();
                                expectedGrowth = StackSize.DOUBLE;
                                break;
                            case 26:
                            case 27:
                            case 28:
                            case 29:
                            case 30:
                            case 31:
                            case 32:
                            case 33:
                            case 34:
                            case 35:
                            case 36:
                            case 37:
                            case 38:
                            case 39:
                            case 40:
                            case 41:
                            case 42:
                            case 43:
                            case 44:
                            case 45:
                            case 46:
                            case 47:
                            case 48:
                            case 49:
                            case 50:
                            case 51:
                            case 52:
                            case 53:
                            default:
                                throw new IllegalStateException("Unexpected opcode: " + opcode);
                            case 54:
                            case 55:
                            case 56:
                            case 57:
                            case 58:
                                stackManipulation = target.resolveWrite();
                                expectedGrowth = StackSize.ZERO;
                                break;
                        }
                        this.methodSizeHandler.requireStackSizePadding(stackManipulation.apply(this.mv, this.implementationContext).getMaximalSize() - expectedGrowth.getSize());
                        return;
                    }
                    this.mv.visitVarInsn(opcode, this.argumentHandler.mapped(offset));
                }

                @Override // net.bytebuddy.jar.asm.MethodVisitor
                public void visitIincInsn(int offset, int value) {
                    OffsetMapping.Target target = this.offsetMappings.get(Integer.valueOf(offset));
                    if (target != null) {
                        this.methodSizeHandler.requireStackSizePadding(target.resolveIncrement(value).apply(this.mv, this.implementationContext).getMaximalSize());
                    } else {
                        this.mv.visitIincInsn(this.argumentHandler.mapped(offset), value);
                    }
                }

                @Override // net.bytebuddy.jar.asm.MethodVisitor
                public void visitInsn(int opcode) {
                    switch (opcode) {
                        case 172:
                            this.methodSizeHandler.requireLocalVariableLength(((StackAwareMethodVisitor) this.mv).drainStack(54, 21, StackSize.SINGLE));
                            break;
                        case 173:
                            this.methodSizeHandler.requireLocalVariableLength(((StackAwareMethodVisitor) this.mv).drainStack(55, 22, StackSize.DOUBLE));
                            break;
                        case 174:
                            this.methodSizeHandler.requireLocalVariableLength(((StackAwareMethodVisitor) this.mv).drainStack(56, 23, StackSize.SINGLE));
                            break;
                        case 175:
                            this.methodSizeHandler.requireLocalVariableLength(((StackAwareMethodVisitor) this.mv).drainStack(57, 24, StackSize.DOUBLE));
                            break;
                        case 176:
                            this.methodSizeHandler.requireLocalVariableLength(((StackAwareMethodVisitor) this.mv).drainStack(58, 25, StackSize.SINGLE));
                            break;
                        case 177:
                            ((StackAwareMethodVisitor) this.mv).drainStack();
                            break;
                        default:
                            this.mv.visitInsn(opcode);
                            return;
                    }
                    this.mv.visitJumpInsn(167, this.endOfMethod);
                }

                @Override // net.bytebuddy.jar.asm.MethodVisitor
                public void visitEnd() {
                    this.suppressionHandler.onEnd(this.methodVisitor, this.implementationContext, this.methodSizeHandler, this.stackMapFrameHandler, this.adviceMethod.getReturnType());
                    this.methodVisitor.visitLabel(this.endOfMethod);
                    if (this.adviceMethod.getReturnType().represents(Boolean.TYPE) || this.adviceMethod.getReturnType().represents(Byte.TYPE) || this.adviceMethod.getReturnType().represents(Short.TYPE) || this.adviceMethod.getReturnType().represents(Character.TYPE) || this.adviceMethod.getReturnType().represents(Integer.TYPE)) {
                        this.stackMapFrameHandler.injectReturnFrame(this.methodVisitor);
                        this.methodVisitor.visitVarInsn(54, this.exit ? this.argumentHandler.exit() : this.argumentHandler.enter());
                    } else if (this.adviceMethod.getReturnType().represents(Long.TYPE)) {
                        this.stackMapFrameHandler.injectReturnFrame(this.methodVisitor);
                        this.methodVisitor.visitVarInsn(55, this.exit ? this.argumentHandler.exit() : this.argumentHandler.enter());
                    } else if (this.adviceMethod.getReturnType().represents(Float.TYPE)) {
                        this.stackMapFrameHandler.injectReturnFrame(this.methodVisitor);
                        this.methodVisitor.visitVarInsn(56, this.exit ? this.argumentHandler.exit() : this.argumentHandler.enter());
                    } else if (this.adviceMethod.getReturnType().represents(Double.TYPE)) {
                        this.stackMapFrameHandler.injectReturnFrame(this.methodVisitor);
                        this.methodVisitor.visitVarInsn(57, this.exit ? this.argumentHandler.exit() : this.argumentHandler.enter());
                    } else if (!this.adviceMethod.getReturnType().represents(Void.TYPE)) {
                        this.stackMapFrameHandler.injectReturnFrame(this.methodVisitor);
                        this.methodVisitor.visitVarInsn(58, this.exit ? this.argumentHandler.exit() : this.argumentHandler.enter());
                    }
                    this.methodSizeHandler.requireStackSize(this.postProcessor.resolve(this.instrumentedType, this.instrumentedMethod, this.assigner, this.argumentHandler).apply(this.methodVisitor, this.implementationContext).getMaximalSize());
                    this.methodSizeHandler.requireStackSize(this.relocationHandler.apply(this.methodVisitor, this.exit ? this.argumentHandler.exit() : this.argumentHandler.enter()));
                    this.stackMapFrameHandler.injectCompletionFrame(this.methodVisitor);
                }

                @Override // net.bytebuddy.jar.asm.MethodVisitor
                public void visitMaxs(int stackSize, int localVariableLength) {
                    this.methodSizeHandler.recordMaxima(stackSize, localVariableLength);
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Dispatcher$Delegating.class */
        public static class Delegating implements Unresolved {
            protected final MethodDescription.InDefinedShape adviceMethod;
            protected final Delegator delegator;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.adviceMethod.equals(((Delegating) obj).adviceMethod) && this.delegator.equals(((Delegating) obj).delegator);
            }

            public int hashCode() {
                return (((17 * 31) + this.adviceMethod.hashCode()) * 31) + this.delegator.hashCode();
            }

            protected Delegating(MethodDescription.InDefinedShape adviceMethod, Delegator delegator) {
                this.adviceMethod = adviceMethod;
                this.delegator = delegator;
            }

            @Override // net.bytebuddy.asm.Advice.Dispatcher
            public boolean isAlive() {
                return true;
            }

            @Override // net.bytebuddy.asm.Advice.Dispatcher.Unresolved
            public boolean isBinary() {
                return false;
            }

            @Override // net.bytebuddy.asm.Advice.Dispatcher
            public TypeDescription getAdviceType() {
                return this.adviceMethod.getReturnType().asErasure();
            }

            @Override // net.bytebuddy.asm.Advice.Dispatcher.Unresolved
            public Map<String, TypeDefinition> getNamedTypes() {
                return Collections.emptyMap();
            }

            @Override // net.bytebuddy.asm.Advice.Dispatcher.Unresolved
            public Resolved.ForMethodEnter asMethodEnter(List<? extends OffsetMapping.Factory<?>> userFactories, ClassReader classReader, Unresolved methodExit, PostProcessor.Factory postProcessorFactory) {
                return Resolved.ForMethodEnter.of(this.adviceMethod, postProcessorFactory.make(this.adviceMethod, false), this.delegator, userFactories, methodExit.getAdviceType(), methodExit.isAlive());
            }

            @Override // net.bytebuddy.asm.Advice.Dispatcher.Unresolved
            public Resolved.ForMethodExit asMethodExit(List<? extends OffsetMapping.Factory<?>> userFactories, ClassReader classReader, Unresolved methodEnter, PostProcessor.Factory postProcessorFactory) {
                Map<String, TypeDefinition> namedTypes = methodEnter.getNamedTypes();
                for (ParameterDescription parameterDescription : this.adviceMethod.getParameters().filter(ElementMatchers.isAnnotatedWith(Local.class))) {
                    String name = ((Local) parameterDescription.getDeclaredAnnotations().ofType(Local.class).load()).value();
                    TypeDefinition typeDefinition = namedTypes.get(name);
                    if (typeDefinition == null) {
                        throw new IllegalStateException(this.adviceMethod + " attempts use of undeclared local variable " + name);
                    }
                    if (!typeDefinition.equals(parameterDescription.getType())) {
                        throw new IllegalStateException(this.adviceMethod + " does not read variable " + name + " as " + typeDefinition);
                    }
                }
                return Resolved.ForMethodExit.of(this.adviceMethod, postProcessorFactory.make(this.adviceMethod, true), this.delegator, namedTypes, userFactories, methodEnter.getAdviceType());
            }

            /* JADX INFO: Access modifiers changed from: protected */
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Dispatcher$Delegating$Resolved.class */
            public static abstract class Resolved extends Resolved.AbstractBase {
                protected final Delegator delegator;

                protected abstract Bound resolve(TypeDescription typeDescription, MethodDescription methodDescription, MethodVisitor methodVisitor, Implementation.Context context, Assigner assigner, ArgumentHandler.ForInstrumentedMethod forInstrumentedMethod, MethodSizeHandler.ForInstrumentedMethod forInstrumentedMethod2, StackMapFrameHandler.ForInstrumentedMethod forInstrumentedMethod3, StackManipulation stackManipulation, RelocationHandler.Relocation relocation);

                protected Resolved(MethodDescription.InDefinedShape adviceMethod, PostProcessor postProcessor, List<? extends OffsetMapping.Factory<?>> factories, TypeDescription throwableType, TypeDescription relocatableType, Delegator delegator) {
                    super(adviceMethod, postProcessor, factories, throwableType, relocatableType, OffsetMapping.Factory.AdviceType.DELEGATION);
                    this.delegator = delegator;
                }

                @Override // net.bytebuddy.asm.Advice.Dispatcher.Resolved
                public Bound bind(TypeDescription instrumentedType, MethodDescription instrumentedMethod, MethodVisitor methodVisitor, Implementation.Context implementationContext, Assigner assigner, ArgumentHandler.ForInstrumentedMethod argumentHandler, MethodSizeHandler.ForInstrumentedMethod methodSizeHandler, StackMapFrameHandler.ForInstrumentedMethod stackMapFrameHandler, StackManipulation exceptionHandler, RelocationHandler.Relocation relocation) {
                    if (!this.adviceMethod.isVisibleTo(instrumentedType)) {
                        throw new IllegalStateException(this.adviceMethod + " is not visible to " + instrumentedMethod.getDeclaringType());
                    }
                    return resolve(instrumentedType, instrumentedMethod, methodVisitor, implementationContext, assigner, argumentHandler, methodSizeHandler, stackMapFrameHandler, exceptionHandler, relocation);
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Dispatcher$Delegating$Resolved$AdviceMethodWriter.class */
                protected static abstract class AdviceMethodWriter implements Bound {
                    protected final MethodDescription.InDefinedShape adviceMethod;
                    private final TypeDescription instrumentedType;
                    private final MethodDescription instrumentedMethod;
                    private final Assigner assigner;
                    private final List<OffsetMapping.Target> offsetMappings;
                    protected final MethodVisitor methodVisitor;
                    protected final Implementation.Context implementationContext;
                    protected final ArgumentHandler.ForAdvice argumentHandler;
                    protected final MethodSizeHandler.ForAdvice methodSizeHandler;
                    protected final StackMapFrameHandler.ForAdvice stackMapFrameHandler;
                    private final SuppressionHandler.Bound suppressionHandler;
                    private final RelocationHandler.Bound relocationHandler;
                    private final PostProcessor postProcessor;
                    private final Delegator delegator;

                    protected abstract boolean isExitAdvice();

                    protected AdviceMethodWriter(MethodDescription.InDefinedShape adviceMethod, TypeDescription instrumentedType, MethodDescription instrumentedMethod, Assigner assigner, PostProcessor postProcessor, List<OffsetMapping.Target> offsetMappings, MethodVisitor methodVisitor, Implementation.Context implementationContext, ArgumentHandler.ForAdvice argumentHandler, MethodSizeHandler.ForAdvice methodSizeHandler, StackMapFrameHandler.ForAdvice stackMapFrameHandler, SuppressionHandler.Bound suppressionHandler, RelocationHandler.Bound relocationHandler, Delegator delegator) {
                        this.adviceMethod = adviceMethod;
                        this.instrumentedType = instrumentedType;
                        this.instrumentedMethod = instrumentedMethod;
                        this.assigner = assigner;
                        this.postProcessor = postProcessor;
                        this.offsetMappings = offsetMappings;
                        this.methodVisitor = methodVisitor;
                        this.implementationContext = implementationContext;
                        this.argumentHandler = argumentHandler;
                        this.methodSizeHandler = methodSizeHandler;
                        this.stackMapFrameHandler = stackMapFrameHandler;
                        this.suppressionHandler = suppressionHandler;
                        this.relocationHandler = relocationHandler;
                        this.delegator = delegator;
                    }

                    @Override // net.bytebuddy.asm.Advice.Dispatcher.Bound
                    public void prepare() {
                        this.suppressionHandler.onPrepare(this.methodVisitor);
                    }

                    @Override // net.bytebuddy.asm.Advice.Dispatcher.Bound
                    public void apply() {
                        this.suppressionHandler.onStart(this.methodVisitor);
                        int index = 0;
                        int currentStackSize = 0;
                        int maximumStackSize = 0;
                        for (OffsetMapping.Target offsetMapping : this.offsetMappings) {
                            int i = index;
                            index++;
                            currentStackSize += ((ParameterDescription.InDefinedShape) this.adviceMethod.getParameters().get(i)).getType().getStackSize().getSize();
                            maximumStackSize = Math.max(maximumStackSize, currentStackSize + offsetMapping.resolveRead().apply(this.methodVisitor, this.implementationContext).getMaximalSize());
                        }
                        this.delegator.apply(this.methodVisitor, this.adviceMethod, this.instrumentedType, this.instrumentedMethod, isExitAdvice());
                        this.suppressionHandler.onEndWithSkip(this.methodVisitor, this.implementationContext, this.methodSizeHandler, this.stackMapFrameHandler, this.adviceMethod.getReturnType());
                        if (this.adviceMethod.getReturnType().represents(Boolean.TYPE) || this.adviceMethod.getReturnType().represents(Byte.TYPE) || this.adviceMethod.getReturnType().represents(Short.TYPE) || this.adviceMethod.getReturnType().represents(Character.TYPE) || this.adviceMethod.getReturnType().represents(Integer.TYPE)) {
                            this.methodVisitor.visitVarInsn(54, isExitAdvice() ? this.argumentHandler.exit() : this.argumentHandler.enter());
                        } else if (this.adviceMethod.getReturnType().represents(Long.TYPE)) {
                            this.methodVisitor.visitVarInsn(55, isExitAdvice() ? this.argumentHandler.exit() : this.argumentHandler.enter());
                        } else if (this.adviceMethod.getReturnType().represents(Float.TYPE)) {
                            this.methodVisitor.visitVarInsn(56, isExitAdvice() ? this.argumentHandler.exit() : this.argumentHandler.enter());
                        } else if (this.adviceMethod.getReturnType().represents(Double.TYPE)) {
                            this.methodVisitor.visitVarInsn(57, isExitAdvice() ? this.argumentHandler.exit() : this.argumentHandler.enter());
                        } else if (!this.adviceMethod.getReturnType().represents(Void.TYPE)) {
                            this.methodVisitor.visitVarInsn(58, isExitAdvice() ? this.argumentHandler.exit() : this.argumentHandler.enter());
                        }
                        this.methodSizeHandler.requireStackSize(this.postProcessor.resolve(this.instrumentedType, this.instrumentedMethod, this.assigner, this.argumentHandler).apply(this.methodVisitor, this.implementationContext).getMaximalSize());
                        this.methodSizeHandler.requireStackSize(this.relocationHandler.apply(this.methodVisitor, isExitAdvice() ? this.argumentHandler.exit() : this.argumentHandler.enter()));
                        this.stackMapFrameHandler.injectCompletionFrame(this.methodVisitor);
                        this.methodSizeHandler.requireStackSize(Math.max(maximumStackSize, this.adviceMethod.getReturnType().getStackSize().getSize()));
                        this.methodSizeHandler.requireLocalVariableLength(this.instrumentedMethod.getStackSize() + this.adviceMethod.getReturnType().getStackSize().getSize());
                    }

                    /* JADX INFO: Access modifiers changed from: protected */
                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Dispatcher$Delegating$Resolved$AdviceMethodWriter$ForMethodEnter.class */
                    public static class ForMethodEnter extends AdviceMethodWriter {
                        protected ForMethodEnter(MethodDescription.InDefinedShape adviceMethod, TypeDescription instrumentedType, MethodDescription instrumentedMethod, Assigner assigner, PostProcessor postProcessor, List<OffsetMapping.Target> offsetMappings, MethodVisitor methodVisitor, Implementation.Context implementationContext, ArgumentHandler.ForAdvice argumentHandler, MethodSizeHandler.ForAdvice methodSizeHandler, StackMapFrameHandler.ForAdvice stackMapFrameHandler, SuppressionHandler.Bound suppressionHandler, RelocationHandler.Bound relocationHandler, Delegator delegator) {
                            super(adviceMethod, instrumentedType, instrumentedMethod, assigner, postProcessor, offsetMappings, methodVisitor, implementationContext, argumentHandler, methodSizeHandler, stackMapFrameHandler, suppressionHandler, relocationHandler, delegator);
                        }

                        @Override // net.bytebuddy.asm.Advice.Dispatcher.Bound
                        public void initialize() {
                        }

                        @Override // net.bytebuddy.asm.Advice.Dispatcher.Delegating.Resolved.AdviceMethodWriter
                        protected boolean isExitAdvice() {
                            return false;
                        }
                    }

                    /* JADX INFO: Access modifiers changed from: protected */
                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Dispatcher$Delegating$Resolved$AdviceMethodWriter$ForMethodExit.class */
                    public static class ForMethodExit extends AdviceMethodWriter {
                        protected ForMethodExit(MethodDescription.InDefinedShape adviceMethod, TypeDescription instrumentedType, MethodDescription instrumentedMethod, Assigner assigner, PostProcessor postProcessor, List<OffsetMapping.Target> offsetMappings, MethodVisitor methodVisitor, Implementation.Context implementationContext, ArgumentHandler.ForAdvice argumentHandler, MethodSizeHandler.ForAdvice methodSizeHandler, StackMapFrameHandler.ForAdvice stackMapFrameHandler, SuppressionHandler.Bound suppressionHandler, RelocationHandler.Bound relocationHandler, Delegator delegator) {
                            super(adviceMethod, instrumentedType, instrumentedMethod, assigner, postProcessor, offsetMappings, methodVisitor, implementationContext, argumentHandler, methodSizeHandler, stackMapFrameHandler, suppressionHandler, relocationHandler, delegator);
                        }

                        @Override // net.bytebuddy.asm.Advice.Dispatcher.Bound
                        public void initialize() {
                            if (this.adviceMethod.getReturnType().represents(Boolean.TYPE) || this.adviceMethod.getReturnType().represents(Byte.TYPE) || this.adviceMethod.getReturnType().represents(Short.TYPE) || this.adviceMethod.getReturnType().represents(Character.TYPE) || this.adviceMethod.getReturnType().represents(Integer.TYPE)) {
                                this.methodVisitor.visitInsn(3);
                                this.methodVisitor.visitVarInsn(54, this.argumentHandler.exit());
                            } else if (this.adviceMethod.getReturnType().represents(Long.TYPE)) {
                                this.methodVisitor.visitInsn(9);
                                this.methodVisitor.visitVarInsn(55, this.argumentHandler.exit());
                            } else if (this.adviceMethod.getReturnType().represents(Float.TYPE)) {
                                this.methodVisitor.visitInsn(11);
                                this.methodVisitor.visitVarInsn(56, this.argumentHandler.exit());
                            } else if (this.adviceMethod.getReturnType().represents(Double.TYPE)) {
                                this.methodVisitor.visitInsn(14);
                                this.methodVisitor.visitVarInsn(57, this.argumentHandler.exit());
                            } else if (!this.adviceMethod.getReturnType().represents(Void.TYPE)) {
                                this.methodVisitor.visitInsn(1);
                                this.methodVisitor.visitVarInsn(58, this.argumentHandler.exit());
                            }
                            this.methodSizeHandler.requireStackSize(this.adviceMethod.getReturnType().getStackSize().getSize());
                        }

                        @Override // net.bytebuddy.asm.Advice.Dispatcher.Delegating.Resolved.AdviceMethodWriter
                        protected boolean isExitAdvice() {
                            return true;
                        }
                    }
                }

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Dispatcher$Delegating$Resolved$ForMethodEnter.class */
                protected static abstract class ForMethodEnter extends Resolved implements Resolved.ForMethodEnter {
                    private final boolean prependLineNumber;

                    @Override // net.bytebuddy.asm.Advice.Dispatcher.Resolved.AbstractBase
                    public boolean equals(Object obj) {
                        if (super.equals(obj)) {
                            if (this == obj) {
                                return true;
                            }
                            return obj != null && getClass() == obj.getClass() && this.prependLineNumber == ((ForMethodEnter) obj).prependLineNumber;
                        }
                        return false;
                    }

                    @Override // net.bytebuddy.asm.Advice.Dispatcher.Resolved.AbstractBase
                    public int hashCode() {
                        return (super.hashCode() * 31) + (this.prependLineNumber ? 1 : 0);
                    }

                    protected ForMethodEnter(MethodDescription.InDefinedShape adviceMethod, PostProcessor postProcessor, List<? extends OffsetMapping.Factory<?>> userFactories, TypeDefinition exitType, Delegator delegator) {
                        super(adviceMethod, postProcessor, CompoundList.of(Arrays.asList(OffsetMapping.ForArgument.Unresolved.Factory.INSTANCE, OffsetMapping.ForAllArguments.Factory.INSTANCE, OffsetMapping.ForThisReference.Factory.INSTANCE, OffsetMapping.ForField.Unresolved.Factory.INSTANCE, OffsetMapping.ForOrigin.Factory.INSTANCE, OffsetMapping.ForUnusedValue.Factory.INSTANCE, OffsetMapping.ForStubValue.INSTANCE, OffsetMapping.ForExitValue.Factory.of(exitType), new OffsetMapping.Factory.Illegal(Thrown.class), new OffsetMapping.Factory.Illegal(Enter.class), new OffsetMapping.Factory.Illegal(Local.class), new OffsetMapping.Factory.Illegal(Return.class)), (List) userFactories), (TypeDescription) adviceMethod.getDeclaredAnnotations().ofType(OnMethodEnter.class).getValue(Advice.SUPPRESS_ENTER).resolve(TypeDescription.class), (TypeDescription) adviceMethod.getDeclaredAnnotations().ofType(OnMethodEnter.class).getValue(Advice.SKIP_ON).resolve(TypeDescription.class), delegator);
                        this.prependLineNumber = ((Boolean) adviceMethod.getDeclaredAnnotations().ofType(OnMethodEnter.class).getValue(Advice.PREPEND_LINE_NUMBER).resolve(Boolean.class)).booleanValue();
                    }

                    protected static Resolved.ForMethodEnter of(MethodDescription.InDefinedShape adviceMethod, PostProcessor postProcessor, Delegator delegator, List<? extends OffsetMapping.Factory<?>> userFactories, TypeDefinition exitType, boolean methodExit) {
                        return methodExit ? new WithRetainedEnterType(adviceMethod, postProcessor, userFactories, exitType, delegator) : new WithDiscardedEnterType(adviceMethod, postProcessor, userFactories, exitType, delegator);
                    }

                    @Override // net.bytebuddy.asm.Advice.Dispatcher.Resolved.ForMethodEnter
                    public boolean isPrependLineNumber() {
                        return this.prependLineNumber;
                    }

                    @Override // net.bytebuddy.asm.Advice.Dispatcher.Resolved.ForMethodEnter
                    public Map<String, TypeDefinition> getNamedTypes() {
                        return Collections.emptyMap();
                    }

                    @Override // net.bytebuddy.asm.Advice.Dispatcher.Delegating.Resolved
                    protected Bound resolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, MethodVisitor methodVisitor, Implementation.Context implementationContext, Assigner assigner, ArgumentHandler.ForInstrumentedMethod argumentHandler, MethodSizeHandler.ForInstrumentedMethod methodSizeHandler, StackMapFrameHandler.ForInstrumentedMethod stackMapFrameHandler, StackManipulation exceptionHandler, RelocationHandler.Relocation relocation) {
                        return doResolve(instrumentedType, instrumentedMethod, methodVisitor, implementationContext, assigner, argumentHandler.bindEnter(this.adviceMethod), methodSizeHandler.bindEnter(this.adviceMethod), stackMapFrameHandler.bindEnter(this.adviceMethod), this.suppressionHandler.bind(exceptionHandler), this.relocationHandler.bind(instrumentedMethod, relocation));
                    }

                    protected Bound doResolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, MethodVisitor methodVisitor, Implementation.Context implementationContext, Assigner assigner, ArgumentHandler.ForAdvice argumentHandler, MethodSizeHandler.ForAdvice methodSizeHandler, StackMapFrameHandler.ForAdvice stackMapFrameHandler, SuppressionHandler.Bound suppressionHandler, RelocationHandler.Bound relocationHandler) {
                        List<OffsetMapping.Target> offsetMappings = new ArrayList<>(this.offsetMappings.size());
                        for (OffsetMapping offsetMapping : this.offsetMappings.values()) {
                            offsetMappings.add(offsetMapping.resolve(instrumentedType, instrumentedMethod, assigner, argumentHandler, OffsetMapping.Sort.ENTER));
                        }
                        return new AdviceMethodWriter.ForMethodEnter(this.adviceMethod, instrumentedType, instrumentedMethod, assigner, this.postProcessor, offsetMappings, methodVisitor, implementationContext, argumentHandler, methodSizeHandler, stackMapFrameHandler, suppressionHandler, relocationHandler, this.delegator);
                    }

                    /* JADX INFO: Access modifiers changed from: protected */
                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Dispatcher$Delegating$Resolved$ForMethodEnter$WithRetainedEnterType.class */
                    public static class WithRetainedEnterType extends ForMethodEnter {
                        protected WithRetainedEnterType(MethodDescription.InDefinedShape adviceMethod, PostProcessor postProcessor, List<? extends OffsetMapping.Factory<?>> userFactories, TypeDefinition exitType, Delegator delegator) {
                            super(adviceMethod, postProcessor, userFactories, exitType, delegator);
                        }

                        @Override // net.bytebuddy.asm.Advice.Dispatcher
                        public TypeDefinition getAdviceType() {
                            return this.adviceMethod.getReturnType();
                        }
                    }

                    /* JADX INFO: Access modifiers changed from: protected */
                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Dispatcher$Delegating$Resolved$ForMethodEnter$WithDiscardedEnterType.class */
                    public static class WithDiscardedEnterType extends ForMethodEnter {
                        protected WithDiscardedEnterType(MethodDescription.InDefinedShape adviceMethod, PostProcessor postProcessor, List<? extends OffsetMapping.Factory<?>> userFactories, TypeDefinition exitType, Delegator delegator) {
                            super(adviceMethod, postProcessor, userFactories, exitType, delegator);
                        }

                        @Override // net.bytebuddy.asm.Advice.Dispatcher
                        public TypeDefinition getAdviceType() {
                            return TypeDescription.VOID;
                        }

                        @Override // net.bytebuddy.asm.Advice.Dispatcher.Delegating.Resolved.ForMethodEnter
                        protected Bound doResolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, MethodVisitor methodVisitor, Implementation.Context implementationContext, Assigner assigner, ArgumentHandler.ForAdvice argumentHandler, MethodSizeHandler.ForAdvice methodSizeHandler, StackMapFrameHandler.ForAdvice stackMapFrameHandler, SuppressionHandler.Bound suppressionHandler, RelocationHandler.Bound relocationHandler) {
                            methodSizeHandler.requireLocalVariableLengthPadding(this.adviceMethod.getReturnType().getStackSize().getSize());
                            return super.doResolve(instrumentedType, instrumentedMethod, methodVisitor, implementationContext, assigner, argumentHandler, methodSizeHandler, stackMapFrameHandler, suppressionHandler, relocationHandler);
                        }
                    }
                }

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Dispatcher$Delegating$Resolved$ForMethodExit.class */
                protected static abstract class ForMethodExit extends Resolved implements Resolved.ForMethodExit {
                    private final boolean backupArguments;

                    @Override // net.bytebuddy.asm.Advice.Dispatcher.Resolved.AbstractBase
                    public boolean equals(Object obj) {
                        if (super.equals(obj)) {
                            if (this == obj) {
                                return true;
                            }
                            return obj != null && getClass() == obj.getClass() && this.backupArguments == ((ForMethodExit) obj).backupArguments;
                        }
                        return false;
                    }

                    @Override // net.bytebuddy.asm.Advice.Dispatcher.Resolved.AbstractBase
                    public int hashCode() {
                        return (super.hashCode() * 31) + (this.backupArguments ? 1 : 0);
                    }

                    protected ForMethodExit(MethodDescription.InDefinedShape adviceMethod, PostProcessor postProcessor, Map<String, TypeDefinition> namedTypes, List<? extends OffsetMapping.Factory<?>> userFactories, TypeDefinition enterType, Delegator delegator) {
                        super(adviceMethod, postProcessor, CompoundList.of(Arrays.asList(OffsetMapping.ForArgument.Unresolved.Factory.INSTANCE, OffsetMapping.ForAllArguments.Factory.INSTANCE, OffsetMapping.ForThisReference.Factory.INSTANCE, OffsetMapping.ForField.Unresolved.Factory.INSTANCE, OffsetMapping.ForOrigin.Factory.INSTANCE, OffsetMapping.ForUnusedValue.Factory.INSTANCE, OffsetMapping.ForStubValue.INSTANCE, OffsetMapping.ForEnterValue.Factory.of(enterType), OffsetMapping.ForExitValue.Factory.of(adviceMethod.getReturnType()), new OffsetMapping.ForLocalValue.Factory(namedTypes), OffsetMapping.ForReturnValue.Factory.INSTANCE, OffsetMapping.ForThrowable.Factory.of(adviceMethod)), (List) userFactories), (TypeDescription) adviceMethod.getDeclaredAnnotations().ofType(OnMethodExit.class).getValue(Advice.SUPPRESS_EXIT).resolve(TypeDescription.class), (TypeDescription) adviceMethod.getDeclaredAnnotations().ofType(OnMethodExit.class).getValue(Advice.REPEAT_ON).resolve(TypeDescription.class), delegator);
                        this.backupArguments = ((Boolean) adviceMethod.getDeclaredAnnotations().ofType(OnMethodExit.class).getValue(Advice.BACKUP_ARGUMENTS).resolve(Boolean.class)).booleanValue();
                    }

                    protected static Resolved.ForMethodExit of(MethodDescription.InDefinedShape adviceMethod, PostProcessor postProcessor, Delegator delegator, Map<String, TypeDefinition> namedTypes, List<? extends OffsetMapping.Factory<?>> userFactories, TypeDefinition enterType) {
                        TypeDescription throwable = (TypeDescription) adviceMethod.getDeclaredAnnotations().ofType(OnMethodExit.class).getValue(Advice.ON_THROWABLE).resolve(TypeDescription.class);
                        return throwable.represents(NoExceptionHandler.class) ? new WithoutExceptionHandler(adviceMethod, postProcessor, namedTypes, userFactories, enterType, delegator) : new WithExceptionHandler(adviceMethod, postProcessor, namedTypes, userFactories, enterType, throwable, delegator);
                    }

                    @Override // net.bytebuddy.asm.Advice.Dispatcher.Delegating.Resolved
                    protected Bound resolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, MethodVisitor methodVisitor, Implementation.Context implementationContext, Assigner assigner, ArgumentHandler.ForInstrumentedMethod argumentHandler, MethodSizeHandler.ForInstrumentedMethod methodSizeHandler, StackMapFrameHandler.ForInstrumentedMethod stackMapFrameHandler, StackManipulation exceptionHandler, RelocationHandler.Relocation relocation) {
                        return doResolve(instrumentedType, instrumentedMethod, methodVisitor, implementationContext, assigner, argumentHandler.bindExit(this.adviceMethod, getThrowable().represents(NoExceptionHandler.class)), methodSizeHandler.bindExit(this.adviceMethod), stackMapFrameHandler.bindExit(this.adviceMethod), this.suppressionHandler.bind(exceptionHandler), this.relocationHandler.bind(instrumentedMethod, relocation));
                    }

                    private Bound doResolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, MethodVisitor methodVisitor, Implementation.Context implementationContext, Assigner assigner, ArgumentHandler.ForAdvice argumentHandler, MethodSizeHandler.ForAdvice methodSizeHandler, StackMapFrameHandler.ForAdvice stackMapFrameHandler, SuppressionHandler.Bound suppressionHandler, RelocationHandler.Bound relocationHandler) {
                        List<OffsetMapping.Target> offsetMappings = new ArrayList<>(this.offsetMappings.size());
                        for (OffsetMapping offsetMapping : this.offsetMappings.values()) {
                            offsetMappings.add(offsetMapping.resolve(instrumentedType, instrumentedMethod, assigner, argumentHandler, OffsetMapping.Sort.EXIT));
                        }
                        return new AdviceMethodWriter.ForMethodExit(this.adviceMethod, instrumentedType, instrumentedMethod, assigner, this.postProcessor, offsetMappings, methodVisitor, implementationContext, argumentHandler, methodSizeHandler, stackMapFrameHandler, suppressionHandler, relocationHandler, this.delegator);
                    }

                    @Override // net.bytebuddy.asm.Advice.Dispatcher.Resolved.ForMethodExit
                    public ArgumentHandler.Factory getArgumentHandlerFactory() {
                        return this.backupArguments ? ArgumentHandler.Factory.COPYING : ArgumentHandler.Factory.SIMPLE;
                    }

                    @Override // net.bytebuddy.asm.Advice.Dispatcher
                    public TypeDefinition getAdviceType() {
                        return this.adviceMethod.getReturnType();
                    }

                    /* JADX INFO: Access modifiers changed from: protected */
                    @HashCodeAndEqualsPlugin.Enhance
                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Dispatcher$Delegating$Resolved$ForMethodExit$WithExceptionHandler.class */
                    public static class WithExceptionHandler extends ForMethodExit {
                        private final TypeDescription throwable;

                        @Override // net.bytebuddy.asm.Advice.Dispatcher.Delegating.Resolved.ForMethodExit, net.bytebuddy.asm.Advice.Dispatcher.Resolved.AbstractBase
                        public boolean equals(Object obj) {
                            if (super.equals(obj)) {
                                if (this == obj) {
                                    return true;
                                }
                                return obj != null && getClass() == obj.getClass() && this.throwable.equals(((WithExceptionHandler) obj).throwable);
                            }
                            return false;
                        }

                        @Override // net.bytebuddy.asm.Advice.Dispatcher.Delegating.Resolved.ForMethodExit, net.bytebuddy.asm.Advice.Dispatcher.Resolved.AbstractBase
                        public int hashCode() {
                            return (super.hashCode() * 31) + this.throwable.hashCode();
                        }

                        protected WithExceptionHandler(MethodDescription.InDefinedShape adviceMethod, PostProcessor postProcessor, Map<String, TypeDefinition> namedTypes, List<? extends OffsetMapping.Factory<?>> userFactories, TypeDefinition enterType, TypeDescription throwable, Delegator delegator) {
                            super(adviceMethod, postProcessor, namedTypes, userFactories, enterType, delegator);
                            this.throwable = throwable;
                        }

                        @Override // net.bytebuddy.asm.Advice.Dispatcher.Resolved.ForMethodExit
                        public TypeDescription getThrowable() {
                            return this.throwable;
                        }
                    }

                    /* JADX INFO: Access modifiers changed from: protected */
                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Dispatcher$Delegating$Resolved$ForMethodExit$WithoutExceptionHandler.class */
                    public static class WithoutExceptionHandler extends ForMethodExit {
                        protected WithoutExceptionHandler(MethodDescription.InDefinedShape adviceMethod, PostProcessor postProcessor, Map<String, TypeDefinition> namedTypes, List<? extends OffsetMapping.Factory<?>> userFactories, TypeDefinition enterType, Delegator delegator) {
                            super(adviceMethod, postProcessor, namedTypes, userFactories, enterType, delegator);
                        }

                        @Override // net.bytebuddy.asm.Advice.Dispatcher.Resolved.ForMethodExit
                        public TypeDescription getThrowable() {
                            return NoExceptionHandler.DESCRIPTION;
                        }
                    }
                }
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$AdviceVisitor.class */
    protected static abstract class AdviceVisitor extends ExceptionTableSensitiveMethodVisitor implements Dispatcher.RelocationHandler.Relocation {
        protected final MethodDescription instrumentedMethod;
        private final Label preparationStart;
        private final Dispatcher.Bound methodEnter;
        protected final Dispatcher.Bound methodExit;
        protected final ArgumentHandler.ForInstrumentedMethod argumentHandler;
        protected final MethodSizeHandler.ForInstrumentedMethod methodSizeHandler;
        protected final StackMapFrameHandler.ForInstrumentedMethod stackMapFrameHandler;

        protected abstract void onUserPrepare();

        protected abstract void onUserStart();

        protected abstract void onUserEnd();

        protected AdviceVisitor(MethodVisitor methodVisitor, Implementation.Context implementationContext, Assigner assigner, StackManipulation exceptionHandler, TypeDescription instrumentedType, MethodDescription instrumentedMethod, Dispatcher.Resolved.ForMethodEnter methodEnter, Dispatcher.Resolved.ForMethodExit methodExit, List<? extends TypeDescription> postMethodTypes, int writerFlags, int readerFlags) {
            super(OpenedClassReader.ASM_API, methodVisitor);
            List singletonList;
            List<TypeDescription> singletonList2;
            this.instrumentedMethod = instrumentedMethod;
            this.preparationStart = new Label();
            this.argumentHandler = methodExit.getArgumentHandlerFactory().resolve(instrumentedMethod, methodEnter.getAdviceType(), methodExit.getAdviceType(), methodEnter.getNamedTypes());
            if (methodExit.getAdviceType().represents(Void.TYPE)) {
                singletonList = Collections.emptyList();
            } else {
                singletonList = Collections.singletonList(methodExit.getAdviceType().asErasure());
            }
            List<TypeDescription> initialTypes = CompoundList.of(singletonList, (List) this.argumentHandler.getNamedTypes());
            if (methodEnter.getAdviceType().represents(Void.TYPE)) {
                singletonList2 = Collections.emptyList();
            } else {
                singletonList2 = Collections.singletonList(methodEnter.getAdviceType().asErasure());
            }
            List<TypeDescription> preMethodTypes = singletonList2;
            this.methodSizeHandler = MethodSizeHandler.Default.of(instrumentedMethod, initialTypes, preMethodTypes, postMethodTypes, this.argumentHandler.isCopyingArguments(), writerFlags);
            this.stackMapFrameHandler = StackMapFrameHandler.Default.of(instrumentedType, instrumentedMethod, initialTypes, preMethodTypes, postMethodTypes, methodExit.isAlive(), this.argumentHandler.isCopyingArguments(), implementationContext.getClassFileVersion(), writerFlags, readerFlags);
            this.methodEnter = methodEnter.bind(instrumentedType, instrumentedMethod, methodVisitor, implementationContext, assigner, this.argumentHandler, this.methodSizeHandler, this.stackMapFrameHandler, exceptionHandler, this);
            this.methodExit = methodExit.bind(instrumentedType, instrumentedMethod, methodVisitor, implementationContext, assigner, this.argumentHandler, this.methodSizeHandler, this.stackMapFrameHandler, exceptionHandler, new Dispatcher.RelocationHandler.Relocation.ForLabel(this.preparationStart));
        }

        @Override // net.bytebuddy.utility.visitor.ExceptionTableSensitiveMethodVisitor
        protected void onAfterExceptionTable() {
            this.methodEnter.prepare();
            onUserPrepare();
            this.methodExit.prepare();
            this.methodEnter.initialize();
            this.methodExit.initialize();
            this.stackMapFrameHandler.injectInitializationFrame(this.mv);
            this.methodEnter.apply();
            this.mv.visitLabel(this.preparationStart);
            this.methodSizeHandler.requireStackSize(this.argumentHandler.prepare(this.mv));
            this.stackMapFrameHandler.injectStartFrame(this.mv);
            onUserStart();
        }

        @Override // net.bytebuddy.utility.visitor.ExceptionTableSensitiveMethodVisitor
        protected void onVisitVarInsn(int opcode, int offset) {
            this.mv.visitVarInsn(opcode, this.argumentHandler.argument(offset));
        }

        @Override // net.bytebuddy.utility.visitor.ExceptionTableSensitiveMethodVisitor
        protected void onVisitIincInsn(int offset, int increment) {
            this.mv.visitIincInsn(this.argumentHandler.argument(offset), increment);
        }

        @Override // net.bytebuddy.utility.visitor.ExceptionTableSensitiveMethodVisitor
        public void onVisitFrame(int type, int localVariableLength, Object[] localVariable, int stackSize, Object[] stack) {
            this.stackMapFrameHandler.translateFrame(this.mv, type, localVariableLength, localVariable, stackSize, stack);
        }

        @Override // net.bytebuddy.jar.asm.MethodVisitor
        public void visitMaxs(int stackSize, int localVariableLength) {
            onUserEnd();
            this.mv.visitMaxs(this.methodSizeHandler.compoundStackSize(stackSize), this.methodSizeHandler.compoundLocalVariableLength(localVariableLength));
        }

        @Override // net.bytebuddy.jar.asm.MethodVisitor
        public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
            this.mv.visitLocalVariable(name, descriptor, signature, start, end, this.argumentHandler.variable(index));
        }

        @Override // net.bytebuddy.jar.asm.MethodVisitor
        public AnnotationVisitor visitLocalVariableAnnotation(int typeReference, TypePath typePath, Label[] start, Label[] end, int[] index, String descriptor, boolean visible) {
            int[] translated = new int[index.length];
            for (int anIndex = 0; anIndex < index.length; anIndex++) {
                translated[anIndex] = this.argumentHandler.variable(index[anIndex]);
            }
            return this.mv.visitLocalVariableAnnotation(typeReference, typePath, start, end, translated, descriptor, visible);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$AdviceVisitor$WithoutExitAdvice.class */
        public static class WithoutExitAdvice extends AdviceVisitor {
            protected WithoutExitAdvice(MethodVisitor methodVisitor, Implementation.Context implementationContext, Assigner assigner, StackManipulation exceptionHandler, TypeDescription instrumentedType, MethodDescription instrumentedMethod, Dispatcher.Resolved.ForMethodEnter methodEnter, int writerFlags, int readerFlags) {
                super(methodVisitor, implementationContext, assigner, exceptionHandler, instrumentedType, instrumentedMethod, methodEnter, Dispatcher.Inactive.INSTANCE, Collections.emptyList(), writerFlags, readerFlags);
            }

            @Override // net.bytebuddy.asm.Advice.Dispatcher.RelocationHandler.Relocation
            public void apply(MethodVisitor methodVisitor) {
                if (this.instrumentedMethod.getReturnType().represents(Boolean.TYPE) || this.instrumentedMethod.getReturnType().represents(Byte.TYPE) || this.instrumentedMethod.getReturnType().represents(Short.TYPE) || this.instrumentedMethod.getReturnType().represents(Character.TYPE) || this.instrumentedMethod.getReturnType().represents(Integer.TYPE)) {
                    methodVisitor.visitInsn(3);
                    methodVisitor.visitInsn(172);
                } else if (this.instrumentedMethod.getReturnType().represents(Long.TYPE)) {
                    methodVisitor.visitInsn(9);
                    methodVisitor.visitInsn(173);
                } else if (this.instrumentedMethod.getReturnType().represents(Float.TYPE)) {
                    methodVisitor.visitInsn(11);
                    methodVisitor.visitInsn(174);
                } else if (this.instrumentedMethod.getReturnType().represents(Double.TYPE)) {
                    methodVisitor.visitInsn(14);
                    methodVisitor.visitInsn(175);
                } else if (this.instrumentedMethod.getReturnType().represents(Void.TYPE)) {
                    methodVisitor.visitInsn(177);
                } else {
                    methodVisitor.visitInsn(1);
                    methodVisitor.visitInsn(176);
                }
            }

            @Override // net.bytebuddy.asm.Advice.AdviceVisitor
            protected void onUserPrepare() {
            }

            @Override // net.bytebuddy.asm.Advice.AdviceVisitor
            protected void onUserStart() {
            }

            @Override // net.bytebuddy.asm.Advice.AdviceVisitor
            protected void onUserEnd() {
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$AdviceVisitor$WithExitAdvice.class */
        protected static abstract class WithExitAdvice extends AdviceVisitor {
            protected final Label returnHandler;

            protected abstract void onUserReturn();

            protected abstract void onExitAdviceReturn();

            protected WithExitAdvice(MethodVisitor methodVisitor, Implementation.Context implementationContext, Assigner assigner, StackManipulation exceptionHandler, TypeDescription instrumentedType, MethodDescription instrumentedMethod, Dispatcher.Resolved.ForMethodEnter methodEnter, Dispatcher.Resolved.ForMethodExit methodExit, List<? extends TypeDescription> postMethodTypes, int writerFlags, int readerFlags) {
                super(new StackAwareMethodVisitor(methodVisitor, instrumentedMethod), implementationContext, assigner, exceptionHandler, instrumentedType, instrumentedMethod, methodEnter, methodExit, postMethodTypes, writerFlags, readerFlags);
                this.returnHandler = new Label();
            }

            @Override // net.bytebuddy.asm.Advice.Dispatcher.RelocationHandler.Relocation
            public void apply(MethodVisitor methodVisitor) {
                if (this.instrumentedMethod.getReturnType().represents(Boolean.TYPE) || this.instrumentedMethod.getReturnType().represents(Byte.TYPE) || this.instrumentedMethod.getReturnType().represents(Short.TYPE) || this.instrumentedMethod.getReturnType().represents(Character.TYPE) || this.instrumentedMethod.getReturnType().represents(Integer.TYPE)) {
                    methodVisitor.visitInsn(3);
                } else if (this.instrumentedMethod.getReturnType().represents(Long.TYPE)) {
                    methodVisitor.visitInsn(9);
                } else if (this.instrumentedMethod.getReturnType().represents(Float.TYPE)) {
                    methodVisitor.visitInsn(11);
                } else if (this.instrumentedMethod.getReturnType().represents(Double.TYPE)) {
                    methodVisitor.visitInsn(14);
                } else if (!this.instrumentedMethod.getReturnType().represents(Void.TYPE)) {
                    methodVisitor.visitInsn(1);
                }
                methodVisitor.visitJumpInsn(167, this.returnHandler);
            }

            @Override // net.bytebuddy.utility.visitor.ExceptionTableSensitiveMethodVisitor
            protected void onVisitInsn(int opcode) {
                switch (opcode) {
                    case 172:
                        this.methodSizeHandler.requireLocalVariableLength(((StackAwareMethodVisitor) this.mv).drainStack(54, 21, StackSize.SINGLE));
                        break;
                    case 173:
                        this.methodSizeHandler.requireLocalVariableLength(((StackAwareMethodVisitor) this.mv).drainStack(55, 22, StackSize.DOUBLE));
                        break;
                    case 174:
                        this.methodSizeHandler.requireLocalVariableLength(((StackAwareMethodVisitor) this.mv).drainStack(56, 23, StackSize.SINGLE));
                        break;
                    case 175:
                        this.methodSizeHandler.requireLocalVariableLength(((StackAwareMethodVisitor) this.mv).drainStack(57, 24, StackSize.DOUBLE));
                        break;
                    case 176:
                        this.methodSizeHandler.requireLocalVariableLength(((StackAwareMethodVisitor) this.mv).drainStack(58, 25, StackSize.SINGLE));
                        break;
                    case 177:
                        ((StackAwareMethodVisitor) this.mv).drainStack();
                        break;
                    default:
                        this.mv.visitInsn(opcode);
                        return;
                }
                this.mv.visitJumpInsn(167, this.returnHandler);
            }

            @Override // net.bytebuddy.asm.Advice.AdviceVisitor
            protected void onUserEnd() {
                this.mv.visitLabel(this.returnHandler);
                onUserReturn();
                this.stackMapFrameHandler.injectCompletionFrame(this.mv);
                this.methodExit.apply();
                onExitAdviceReturn();
                if (this.instrumentedMethod.getReturnType().represents(Boolean.TYPE) || this.instrumentedMethod.getReturnType().represents(Byte.TYPE) || this.instrumentedMethod.getReturnType().represents(Short.TYPE) || this.instrumentedMethod.getReturnType().represents(Character.TYPE) || this.instrumentedMethod.getReturnType().represents(Integer.TYPE)) {
                    this.mv.visitVarInsn(21, this.argumentHandler.returned());
                    this.mv.visitInsn(172);
                } else if (this.instrumentedMethod.getReturnType().represents(Long.TYPE)) {
                    this.mv.visitVarInsn(22, this.argumentHandler.returned());
                    this.mv.visitInsn(173);
                } else if (this.instrumentedMethod.getReturnType().represents(Float.TYPE)) {
                    this.mv.visitVarInsn(23, this.argumentHandler.returned());
                    this.mv.visitInsn(174);
                } else if (this.instrumentedMethod.getReturnType().represents(Double.TYPE)) {
                    this.mv.visitVarInsn(24, this.argumentHandler.returned());
                    this.mv.visitInsn(175);
                } else if (!this.instrumentedMethod.getReturnType().represents(Void.TYPE)) {
                    this.mv.visitVarInsn(25, this.argumentHandler.returned());
                    this.mv.visitInsn(176);
                } else {
                    this.mv.visitInsn(177);
                }
                this.methodSizeHandler.requireStackSize(this.instrumentedMethod.getReturnType().getStackSize().getSize());
            }

            /* JADX INFO: Access modifiers changed from: protected */
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$AdviceVisitor$WithExitAdvice$WithoutExceptionHandling.class */
            public static class WithoutExceptionHandling extends WithExitAdvice {
                /* JADX WARN: Illegal instructions before constructor call */
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct add '--show-bad-code' argument
                */
                protected WithoutExceptionHandling(net.bytebuddy.jar.asm.MethodVisitor r14, net.bytebuddy.implementation.Implementation.Context r15, net.bytebuddy.implementation.bytecode.assign.Assigner r16, net.bytebuddy.implementation.bytecode.StackManipulation r17, net.bytebuddy.description.type.TypeDescription r18, net.bytebuddy.description.method.MethodDescription r19, net.bytebuddy.asm.Advice.Dispatcher.Resolved.ForMethodEnter r20, net.bytebuddy.asm.Advice.Dispatcher.Resolved.ForMethodExit r21, int r22, int r23) {
                    /*
                        r13 = this;
                        r0 = r13
                        r1 = r14
                        r2 = r15
                        r3 = r16
                        r4 = r17
                        r5 = r18
                        r6 = r19
                        r7 = r20
                        r8 = r21
                        r9 = r19
                        net.bytebuddy.description.type.TypeDescription$Generic r9 = r9.getReturnType()
                        java.lang.Class r10 = java.lang.Void.TYPE
                        boolean r9 = r9.represents(r10)
                        if (r9 == 0) goto L26
                        java.util.List r9 = java.util.Collections.emptyList()
                        goto L35
                    L26:
                        r9 = r19
                        net.bytebuddy.description.type.TypeDescription$Generic r9 = r9.getReturnType()
                        net.bytebuddy.description.type.TypeDescription r9 = r9.asErasure()
                        java.util.List r9 = java.util.Collections.singletonList(r9)
                    L35:
                        r10 = r22
                        r11 = r23
                        r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11)
                        return
                    */
                    throw new UnsupportedOperationException("Method not decompiled: net.bytebuddy.asm.Advice.AdviceVisitor.WithExitAdvice.WithoutExceptionHandling.<init>(net.bytebuddy.jar.asm.MethodVisitor, net.bytebuddy.implementation.Implementation$Context, net.bytebuddy.implementation.bytecode.assign.Assigner, net.bytebuddy.implementation.bytecode.StackManipulation, net.bytebuddy.description.type.TypeDescription, net.bytebuddy.description.method.MethodDescription, net.bytebuddy.asm.Advice$Dispatcher$Resolved$ForMethodEnter, net.bytebuddy.asm.Advice$Dispatcher$Resolved$ForMethodExit, int, int):void");
                }

                @Override // net.bytebuddy.asm.Advice.AdviceVisitor
                protected void onUserPrepare() {
                }

                @Override // net.bytebuddy.asm.Advice.AdviceVisitor
                protected void onUserStart() {
                }

                @Override // net.bytebuddy.asm.Advice.AdviceVisitor.WithExitAdvice
                protected void onUserReturn() {
                    if (this.instrumentedMethod.getReturnType().represents(Boolean.TYPE) || this.instrumentedMethod.getReturnType().represents(Byte.TYPE) || this.instrumentedMethod.getReturnType().represents(Short.TYPE) || this.instrumentedMethod.getReturnType().represents(Character.TYPE) || this.instrumentedMethod.getReturnType().represents(Integer.TYPE)) {
                        this.stackMapFrameHandler.injectReturnFrame(this.mv);
                        this.mv.visitVarInsn(54, this.argumentHandler.returned());
                    } else if (this.instrumentedMethod.getReturnType().represents(Long.TYPE)) {
                        this.stackMapFrameHandler.injectReturnFrame(this.mv);
                        this.mv.visitVarInsn(55, this.argumentHandler.returned());
                    } else if (this.instrumentedMethod.getReturnType().represents(Float.TYPE)) {
                        this.stackMapFrameHandler.injectReturnFrame(this.mv);
                        this.mv.visitVarInsn(56, this.argumentHandler.returned());
                    } else if (this.instrumentedMethod.getReturnType().represents(Double.TYPE)) {
                        this.stackMapFrameHandler.injectReturnFrame(this.mv);
                        this.mv.visitVarInsn(57, this.argumentHandler.returned());
                    } else if (!this.instrumentedMethod.getReturnType().represents(Void.TYPE)) {
                        this.stackMapFrameHandler.injectReturnFrame(this.mv);
                        this.mv.visitVarInsn(58, this.argumentHandler.returned());
                    }
                }

                @Override // net.bytebuddy.asm.Advice.AdviceVisitor.WithExitAdvice
                protected void onExitAdviceReturn() {
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$AdviceVisitor$WithExitAdvice$WithExceptionHandling.class */
            public static class WithExceptionHandling extends WithExitAdvice {
                private final TypeDescription throwable;
                private final Label exceptionHandler;
                protected final Label userStart;

                protected WithExceptionHandling(MethodVisitor methodVisitor, Implementation.Context implementationContext, Assigner assigner, StackManipulation exceptionHandler, TypeDescription instrumentedType, MethodDescription instrumentedMethod, Dispatcher.Resolved.ForMethodEnter methodEnter, Dispatcher.Resolved.ForMethodExit methodExit, int writerFlags, int readerFlags, TypeDescription throwable) {
                    super(methodVisitor, implementationContext, assigner, exceptionHandler, instrumentedType, instrumentedMethod, methodEnter, methodExit, instrumentedMethod.getReturnType().represents(Void.TYPE) ? Collections.singletonList(TypeDescription.THROWABLE) : Arrays.asList(instrumentedMethod.getReturnType().asErasure(), TypeDescription.THROWABLE), writerFlags, readerFlags);
                    this.throwable = throwable;
                    this.exceptionHandler = new Label();
                    this.userStart = new Label();
                }

                @Override // net.bytebuddy.asm.Advice.AdviceVisitor
                protected void onUserPrepare() {
                    this.mv.visitTryCatchBlock(this.userStart, this.returnHandler, this.exceptionHandler, this.throwable.getInternalName());
                }

                @Override // net.bytebuddy.asm.Advice.AdviceVisitor
                protected void onUserStart() {
                    this.mv.visitLabel(this.userStart);
                }

                @Override // net.bytebuddy.asm.Advice.AdviceVisitor.WithExitAdvice
                protected void onUserReturn() {
                    this.stackMapFrameHandler.injectReturnFrame(this.mv);
                    if (this.instrumentedMethod.getReturnType().represents(Boolean.TYPE) || this.instrumentedMethod.getReturnType().represents(Byte.TYPE) || this.instrumentedMethod.getReturnType().represents(Short.TYPE) || this.instrumentedMethod.getReturnType().represents(Character.TYPE) || this.instrumentedMethod.getReturnType().represents(Integer.TYPE)) {
                        this.mv.visitVarInsn(54, this.argumentHandler.returned());
                    } else if (this.instrumentedMethod.getReturnType().represents(Long.TYPE)) {
                        this.mv.visitVarInsn(55, this.argumentHandler.returned());
                    } else if (this.instrumentedMethod.getReturnType().represents(Float.TYPE)) {
                        this.mv.visitVarInsn(56, this.argumentHandler.returned());
                    } else if (this.instrumentedMethod.getReturnType().represents(Double.TYPE)) {
                        this.mv.visitVarInsn(57, this.argumentHandler.returned());
                    } else if (!this.instrumentedMethod.getReturnType().represents(Void.TYPE)) {
                        this.mv.visitVarInsn(58, this.argumentHandler.returned());
                    }
                    this.mv.visitInsn(1);
                    this.mv.visitVarInsn(58, this.argumentHandler.thrown());
                    Label endOfHandler = new Label();
                    this.mv.visitJumpInsn(167, endOfHandler);
                    this.mv.visitLabel(this.exceptionHandler);
                    this.stackMapFrameHandler.injectExceptionFrame(this.mv);
                    this.mv.visitVarInsn(58, this.argumentHandler.thrown());
                    if (this.instrumentedMethod.getReturnType().represents(Boolean.TYPE) || this.instrumentedMethod.getReturnType().represents(Byte.TYPE) || this.instrumentedMethod.getReturnType().represents(Short.TYPE) || this.instrumentedMethod.getReturnType().represents(Character.TYPE) || this.instrumentedMethod.getReturnType().represents(Integer.TYPE)) {
                        this.mv.visitInsn(3);
                        this.mv.visitVarInsn(54, this.argumentHandler.returned());
                    } else if (this.instrumentedMethod.getReturnType().represents(Long.TYPE)) {
                        this.mv.visitInsn(9);
                        this.mv.visitVarInsn(55, this.argumentHandler.returned());
                    } else if (this.instrumentedMethod.getReturnType().represents(Float.TYPE)) {
                        this.mv.visitInsn(11);
                        this.mv.visitVarInsn(56, this.argumentHandler.returned());
                    } else if (this.instrumentedMethod.getReturnType().represents(Double.TYPE)) {
                        this.mv.visitInsn(14);
                        this.mv.visitVarInsn(57, this.argumentHandler.returned());
                    } else if (!this.instrumentedMethod.getReturnType().represents(Void.TYPE)) {
                        this.mv.visitInsn(1);
                        this.mv.visitVarInsn(58, this.argumentHandler.returned());
                    }
                    this.mv.visitLabel(endOfHandler);
                    this.methodSizeHandler.requireStackSize(StackSize.SINGLE.getSize());
                }

                @Override // net.bytebuddy.asm.Advice.AdviceVisitor.WithExitAdvice
                protected void onExitAdviceReturn() {
                    this.mv.visitVarInsn(25, this.argumentHandler.thrown());
                    Label endOfHandler = new Label();
                    this.mv.visitJumpInsn(198, endOfHandler);
                    this.mv.visitVarInsn(25, this.argumentHandler.thrown());
                    this.mv.visitInsn(191);
                    this.mv.visitLabel(endOfHandler);
                    this.stackMapFrameHandler.injectPostCompletionFrame(this.mv);
                }
            }
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Appender.class */
    protected static class Appender implements ByteCodeAppender {
        private final Advice advice;
        private final Implementation.Target implementationTarget;
        private final ByteCodeAppender delegate;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.advice.equals(((Appender) obj).advice) && this.implementationTarget.equals(((Appender) obj).implementationTarget) && this.delegate.equals(((Appender) obj).delegate);
        }

        public int hashCode() {
            return (((((17 * 31) + this.advice.hashCode()) * 31) + this.implementationTarget.hashCode()) * 31) + this.delegate.hashCode();
        }

        protected Appender(Advice advice, Implementation.Target implementationTarget, ByteCodeAppender delegate) {
            this.advice = advice;
            this.implementationTarget = implementationTarget;
            this.delegate = delegate;
        }

        @Override // net.bytebuddy.implementation.bytecode.ByteCodeAppender
        public ByteCodeAppender.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext, MethodDescription instrumentedMethod) {
            EmulatingMethodVisitor emulatingMethodVisitor = new EmulatingMethodVisitor(methodVisitor, this.delegate);
            MethodVisitor methodVisitor2 = this.advice.doWrap(this.implementationTarget.getInstrumentedType(), instrumentedMethod, emulatingMethodVisitor, implementationContext, 0, 0);
            return emulatingMethodVisitor.resolve(methodVisitor2, implementationContext, instrumentedMethod);
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$Appender$EmulatingMethodVisitor.class */
        protected static class EmulatingMethodVisitor extends MethodVisitor {
            private final ByteCodeAppender delegate;
            private int stackSize;
            private int localVariableLength;

            protected EmulatingMethodVisitor(MethodVisitor methodVisitor, ByteCodeAppender delegate) {
                super(OpenedClassReader.ASM_API, methodVisitor);
                this.delegate = delegate;
            }

            protected ByteCodeAppender.Size resolve(MethodVisitor methodVisitor, Implementation.Context implementationContext, MethodDescription instrumentedMethod) {
                methodVisitor.visitCode();
                ByteCodeAppender.Size size = this.delegate.apply(methodVisitor, implementationContext, instrumentedMethod);
                methodVisitor.visitMaxs(size.getOperandStackSize(), size.getLocalVariableSize());
                methodVisitor.visitEnd();
                return new ByteCodeAppender.Size(this.stackSize, this.localVariableLength);
            }

            @Override // net.bytebuddy.jar.asm.MethodVisitor
            public void visitCode() {
            }

            @Override // net.bytebuddy.jar.asm.MethodVisitor
            public void visitMaxs(int stackSize, int localVariableLength) {
                this.stackSize = stackSize;
                this.localVariableLength = localVariableLength;
            }

            @Override // net.bytebuddy.jar.asm.MethodVisitor
            public void visitEnd() {
            }
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$WithCustomMapping.class */
    public static class WithCustomMapping {
        private final PostProcessor.Factory postProcessorFactory;
        private final Delegator delegator;
        private final Map<Class<? extends Annotation>, OffsetMapping.Factory<?>> offsetMappings;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.postProcessorFactory.equals(((WithCustomMapping) obj).postProcessorFactory) && this.delegator.equals(((WithCustomMapping) obj).delegator) && this.offsetMappings.equals(((WithCustomMapping) obj).offsetMappings);
        }

        public int hashCode() {
            return (((((17 * 31) + this.postProcessorFactory.hashCode()) * 31) + this.delegator.hashCode()) * 31) + this.offsetMappings.hashCode();
        }

        protected WithCustomMapping() {
            this(PostProcessor.NoOp.INSTANCE, Collections.emptyMap(), Delegator.ForStaticInvocation.INSTANCE);
        }

        protected WithCustomMapping(PostProcessor.Factory postProcessorFactory, Map<Class<? extends Annotation>, OffsetMapping.Factory<?>> offsetMappings, Delegator delegator) {
            this.postProcessorFactory = postProcessorFactory;
            this.offsetMappings = offsetMappings;
            this.delegator = delegator;
        }

        public <T extends Annotation> WithCustomMapping bind(Class<T> type, Object value) {
            return bind(OffsetMapping.ForStackManipulation.Factory.of(type, value));
        }

        public <T extends Annotation> WithCustomMapping bind(Class<T> type, Field field) {
            return bind((Class) type, (FieldDescription) new FieldDescription.ForLoadedField(field));
        }

        public <T extends Annotation> WithCustomMapping bind(Class<T> type, FieldDescription fieldDescription) {
            return bind(new OffsetMapping.ForField.Resolved.Factory(type, fieldDescription));
        }

        public <T extends Annotation> WithCustomMapping bind(Class<T> type, Method method, int index) {
            if (index < 0) {
                throw new IllegalArgumentException("A parameter cannot be negative: " + index);
            }
            if (method.getParameterTypes().length <= index) {
                throw new IllegalArgumentException(method + " does not declare a parameter with index " + index);
            }
            return bind((Class) type, (ParameterDescription) new MethodDescription.ForLoadedMethod(method).getParameters().get(index));
        }

        public <T extends Annotation> WithCustomMapping bind(Class<T> type, Constructor<?> constructor, int index) {
            if (index < 0) {
                throw new IllegalArgumentException("A parameter cannot be negative: " + index);
            }
            if (constructor.getParameterTypes().length <= index) {
                throw new IllegalArgumentException(constructor + " does not declare a parameter with index " + index);
            }
            return bind((Class) type, (ParameterDescription) new MethodDescription.ForLoadedConstructor(constructor).getParameters().get(index));
        }

        public <T extends Annotation> WithCustomMapping bind(Class<T> type, ParameterDescription parameterDescription) {
            return bind(new OffsetMapping.ForArgument.Resolved.Factory(type, parameterDescription));
        }

        public <T extends Annotation> WithCustomMapping bind(Class<T> type, Class<?> value) {
            return bind((Class) type, TypeDescription.ForLoadedType.of(value));
        }

        public <T extends Annotation> WithCustomMapping bind(Class<T> type, TypeDescription value) {
            return bind(new OffsetMapping.ForStackManipulation.Factory(type, value));
        }

        public <T extends Annotation> WithCustomMapping bind(Class<T> type, Enum<?> value) {
            return bind((Class) type, (EnumerationDescription) new EnumerationDescription.ForLoadedEnumeration(value));
        }

        public <T extends Annotation> WithCustomMapping bind(Class<T> type, EnumerationDescription value) {
            return bind(new OffsetMapping.ForStackManipulation.Factory(type, value));
        }

        public <T extends Annotation> WithCustomMapping bindSerialized(Class<T> type, Serializable value) {
            return bindSerialized(type, value, value.getClass());
        }

        public <T extends Annotation, S extends Serializable> WithCustomMapping bindSerialized(Class<T> type, S value, Class<? super S> targetType) {
            return bind(OffsetMapping.ForSerializedValue.Factory.of(type, value, targetType));
        }

        public <T extends Annotation> WithCustomMapping bindProperty(Class<T> type, String property) {
            return bind(OffsetMapping.ForStackManipulation.OfAnnotationProperty.of(type, property));
        }

        public <T extends Annotation> WithCustomMapping bind(Class<T> type, StackManipulation stackManipulation, java.lang.reflect.Type targetType) {
            return bind(type, stackManipulation, TypeDefinition.Sort.describe(targetType));
        }

        public <T extends Annotation> WithCustomMapping bind(Class<T> type, StackManipulation stackManipulation, TypeDescription.Generic targetType) {
            return bind(new OffsetMapping.ForStackManipulation.Factory(type, stackManipulation, targetType));
        }

        public <T extends Annotation> WithCustomMapping bind(Class<T> type, OffsetMapping offsetMapping) {
            return bind(new OffsetMapping.Factory.Simple(type, offsetMapping));
        }

        public WithCustomMapping bind(OffsetMapping.Factory<?> offsetMapping) {
            HashMap hashMap = new HashMap(this.offsetMappings);
            if (!offsetMapping.getAnnotationType().isAnnotation()) {
                throw new IllegalArgumentException("Not an annotation type: " + offsetMapping.getAnnotationType());
            }
            if (hashMap.put(offsetMapping.getAnnotationType(), offsetMapping) != null) {
                throw new IllegalArgumentException("Annotation type already mapped: " + offsetMapping.getAnnotationType());
            }
            return new WithCustomMapping(this.postProcessorFactory, hashMap, this.delegator);
        }

        public WithCustomMapping bootstrap(Constructor<?> constructor) {
            return bootstrap(new MethodDescription.ForLoadedConstructor(constructor));
        }

        public WithCustomMapping bootstrap(Method method) {
            return bootstrap(new MethodDescription.ForLoadedMethod(method));
        }

        public WithCustomMapping bootstrap(MethodDescription.InDefinedShape bootstrap) {
            return new WithCustomMapping(this.postProcessorFactory, this.offsetMappings, Delegator.ForDynamicInvocation.of(bootstrap));
        }

        public WithCustomMapping with(PostProcessor.Factory postProcessorFactory) {
            return new WithCustomMapping(new PostProcessor.Factory.Compound(this.postProcessorFactory, postProcessorFactory), this.offsetMappings, this.delegator);
        }

        public Advice to(Class<?> advice) {
            return to(advice, ClassFileLocator.ForClassLoader.of(advice.getClassLoader()));
        }

        public Advice to(Class<?> advice, ClassFileLocator classFileLocator) {
            return to(TypeDescription.ForLoadedType.of(advice), classFileLocator);
        }

        public Advice to(TypeDescription advice, ClassFileLocator classFileLocator) {
            return Advice.to(advice, this.postProcessorFactory, classFileLocator, new ArrayList(this.offsetMappings.values()), this.delegator);
        }

        public Advice to(Class<?> enterAdvice, Class<?> exitAdvice) {
            ClassLoader enterLoader = enterAdvice.getClassLoader();
            ClassLoader exitLoader = exitAdvice.getClassLoader();
            return to(enterAdvice, exitAdvice, enterLoader == exitLoader ? ClassFileLocator.ForClassLoader.of(enterLoader) : new ClassFileLocator.Compound(ClassFileLocator.ForClassLoader.of(enterLoader), ClassFileLocator.ForClassLoader.of(exitLoader)));
        }

        public Advice to(Class<?> enterAdvice, Class<?> exitAdvice, ClassFileLocator classFileLocator) {
            return to(TypeDescription.ForLoadedType.of(enterAdvice), TypeDescription.ForLoadedType.of(exitAdvice), classFileLocator);
        }

        public Advice to(TypeDescription enterAdvice, TypeDescription exitAdvice) {
            return to(enterAdvice, exitAdvice, ClassFileLocator.NoOp.INSTANCE);
        }

        public Advice to(TypeDescription enterAdvice, TypeDescription exitAdvice, ClassFileLocator classFileLocator) {
            return Advice.to(enterAdvice, exitAdvice, this.postProcessorFactory, classFileLocator, new ArrayList(this.offsetMappings.values()), this.delegator);
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$NoExceptionHandler.class */
    private static class NoExceptionHandler extends Throwable {
        private static final TypeDescription DESCRIPTION = TypeDescription.ForLoadedType.of(NoExceptionHandler.class);

        private NoExceptionHandler() {
            throw new UnsupportedOperationException("This class only serves as a marker type and should not be instantiated");
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OnDefaultValue.class */
    public static final class OnDefaultValue {
        private OnDefaultValue() {
            throw new UnsupportedOperationException("This class only serves as a marker type and should not be instantiated");
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/Advice$OnNonDefaultValue.class */
    public static final class OnNonDefaultValue {
        private OnNonDefaultValue() {
            throw new UnsupportedOperationException("This class only serves as a marker type and should not be instantiated");
        }
    }
}
