package net.bytebuddy.dynamic.scaffold.subclass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.MethodList;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.Transformer;
import net.bytebuddy.dynamic.scaffold.MethodRegistry;
import net.bytebuddy.implementation.MethodCall;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.implementation.attribute.MethodAttributeAppender;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.matcher.LatentMatcher;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/subclass/ConstructorStrategy.class */
public interface ConstructorStrategy {
    List<MethodDescription.Token> extractConstructors(TypeDescription typeDescription);

    MethodRegistry inject(TypeDescription typeDescription, MethodRegistry methodRegistry);

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/subclass/ConstructorStrategy$Default.class */
    public enum Default implements ConstructorStrategy {
        NO_CONSTRUCTORS { // from class: net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy.Default.1
            @Override // net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy.Default
            protected List<MethodDescription.Token> doExtractConstructors(TypeDescription superClass) {
                return Collections.emptyList();
            }

            @Override // net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy.Default
            protected MethodRegistry doInject(MethodRegistry methodRegistry, MethodAttributeAppender.Factory methodAttributeAppenderFactory) {
                return methodRegistry;
            }
        },
        DEFAULT_CONSTRUCTOR { // from class: net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy.Default.2
            @Override // net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy.Default
            protected List<MethodDescription.Token> doExtractConstructors(TypeDescription instrumentedType) {
                TypeDescription.Generic superClass = instrumentedType.getSuperClass();
                MethodList<?> defaultConstructors = superClass == null ? new MethodList.Empty<>() : superClass.getDeclaredMethods().filter(ElementMatchers.isConstructor().and(ElementMatchers.takesArguments(0)).and(ElementMatchers.isVisibleTo(instrumentedType)));
                if (defaultConstructors.size() == 1) {
                    return Collections.singletonList(new MethodDescription.Token(1));
                }
                throw new IllegalArgumentException(instrumentedType.getSuperClass() + " declares no constructor that is visible to " + instrumentedType);
            }

            @Override // net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy.Default
            protected MethodRegistry doInject(MethodRegistry methodRegistry, MethodAttributeAppender.Factory methodAttributeAppenderFactory) {
                return methodRegistry.append(new LatentMatcher.Resolved(ElementMatchers.isConstructor()), new MethodRegistry.Handler.ForImplementation(SuperMethodCall.INSTANCE), methodAttributeAppenderFactory, Transformer.NoOp.make());
            }
        },
        IMITATE_SUPER_CLASS { // from class: net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy.Default.3
            @Override // net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy.Default
            protected List<MethodDescription.Token> doExtractConstructors(TypeDescription instrumentedType) {
                TypeDescription.Generic superClass = instrumentedType.getSuperClass();
                return (superClass == null ? new MethodList.Empty() : superClass.getDeclaredMethods().filter(ElementMatchers.isConstructor().and(ElementMatchers.isVisibleTo(instrumentedType)))).asTokenList(ElementMatchers.is(instrumentedType));
            }

            @Override // net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy.Default
            public MethodRegistry doInject(MethodRegistry methodRegistry, MethodAttributeAppender.Factory methodAttributeAppenderFactory) {
                return methodRegistry.append(new LatentMatcher.Resolved(ElementMatchers.isConstructor()), new MethodRegistry.Handler.ForImplementation(SuperMethodCall.INSTANCE), methodAttributeAppenderFactory, Transformer.NoOp.make());
            }
        },
        IMITATE_SUPER_CLASS_PUBLIC { // from class: net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy.Default.4
            @Override // net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy.Default
            protected List<MethodDescription.Token> doExtractConstructors(TypeDescription instrumentedType) {
                TypeDescription.Generic superClass = instrumentedType.getSuperClass();
                return (superClass == null ? new MethodList.Empty() : superClass.getDeclaredMethods().filter(ElementMatchers.isPublic().and(ElementMatchers.isConstructor()))).asTokenList(ElementMatchers.is(instrumentedType));
            }

            @Override // net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy.Default
            public MethodRegistry doInject(MethodRegistry methodRegistry, MethodAttributeAppender.Factory methodAttributeAppenderFactory) {
                return methodRegistry.append(new LatentMatcher.Resolved(ElementMatchers.isConstructor()), new MethodRegistry.Handler.ForImplementation(SuperMethodCall.INSTANCE), methodAttributeAppenderFactory, Transformer.NoOp.make());
            }
        },
        IMITATE_SUPER_CLASS_OPENING { // from class: net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy.Default.5
            @Override // net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy.Default
            protected List<MethodDescription.Token> doExtractConstructors(TypeDescription instrumentedType) {
                TypeDescription.Generic superClass = instrumentedType.getSuperClass();
                return (superClass == null ? new MethodList.Empty() : superClass.getDeclaredMethods().filter(ElementMatchers.isConstructor().and(ElementMatchers.isVisibleTo(instrumentedType)))).asTokenList(ElementMatchers.is(instrumentedType));
            }

            @Override // net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy.Default
            public MethodRegistry doInject(MethodRegistry methodRegistry, MethodAttributeAppender.Factory methodAttributeAppenderFactory) {
                return methodRegistry.append(new LatentMatcher.Resolved(ElementMatchers.isConstructor()), new MethodRegistry.Handler.ForImplementation(SuperMethodCall.INSTANCE), methodAttributeAppenderFactory, Transformer.NoOp.make());
            }

            @Override // net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy.Default
            protected int resolveModifier(int modifiers) {
                return 1;
            }
        };

        protected abstract List<MethodDescription.Token> doExtractConstructors(TypeDescription typeDescription);

        protected abstract MethodRegistry doInject(MethodRegistry methodRegistry, MethodAttributeAppender.Factory factory);

        @Override // net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy
        public List<MethodDescription.Token> extractConstructors(TypeDescription instrumentedType) {
            List<MethodDescription.Token> tokens = doExtractConstructors(instrumentedType);
            List<MethodDescription.Token> stripped = new ArrayList<>(tokens.size());
            for (MethodDescription.Token token : tokens) {
                stripped.add(new MethodDescription.Token(token.getName(), resolveModifier(token.getModifiers()), token.getTypeVariableTokens(), token.getReturnType(), token.getParameterTokens(), token.getExceptionTypes(), token.getAnnotations(), token.getDefaultValue(), TypeDescription.Generic.UNDEFINED));
            }
            return stripped;
        }

        protected int resolveModifier(int modifiers) {
            return modifiers;
        }

        @Override // net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy
        public MethodRegistry inject(TypeDescription instrumentedType, MethodRegistry methodRegistry) {
            return doInject(methodRegistry, MethodAttributeAppender.NoOp.INSTANCE);
        }

        public ConstructorStrategy with(MethodAttributeAppender.Factory methodAttributeAppenderFactory) {
            return new WithMethodAttributeAppenderFactory(this, methodAttributeAppenderFactory);
        }

        public ConstructorStrategy withInheritedAnnotations() {
            return new WithMethodAttributeAppenderFactory(this, MethodAttributeAppender.ForInstrumentedMethod.EXCLUDING_RECEIVER);
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/subclass/ConstructorStrategy$Default$WithMethodAttributeAppenderFactory.class */
        protected static class WithMethodAttributeAppenderFactory implements ConstructorStrategy {
            private final Default delegate;
            private final MethodAttributeAppender.Factory methodAttributeAppenderFactory;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.delegate.equals(((WithMethodAttributeAppenderFactory) obj).delegate) && this.methodAttributeAppenderFactory.equals(((WithMethodAttributeAppenderFactory) obj).methodAttributeAppenderFactory);
            }

            public int hashCode() {
                return (((17 * 31) + this.delegate.hashCode()) * 31) + this.methodAttributeAppenderFactory.hashCode();
            }

            protected WithMethodAttributeAppenderFactory(Default delegate, MethodAttributeAppender.Factory methodAttributeAppenderFactory) {
                this.delegate = delegate;
                this.methodAttributeAppenderFactory = methodAttributeAppenderFactory;
            }

            @Override // net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy
            public List<MethodDescription.Token> extractConstructors(TypeDescription instrumentedType) {
                return this.delegate.extractConstructors(instrumentedType);
            }

            @Override // net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy
            public MethodRegistry inject(TypeDescription instrumentedType, MethodRegistry methodRegistry) {
                return this.delegate.doInject(methodRegistry, this.methodAttributeAppenderFactory);
            }
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/subclass/ConstructorStrategy$ForDefaultConstructor.class */
    public static class ForDefaultConstructor implements ConstructorStrategy {
        private final ElementMatcher<? super MethodDescription> elementMatcher;
        private final MethodAttributeAppender.Factory methodAttributeAppenderFactory;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.elementMatcher.equals(((ForDefaultConstructor) obj).elementMatcher) && this.methodAttributeAppenderFactory.equals(((ForDefaultConstructor) obj).methodAttributeAppenderFactory);
        }

        public int hashCode() {
            return (((17 * 31) + this.elementMatcher.hashCode()) * 31) + this.methodAttributeAppenderFactory.hashCode();
        }

        public ForDefaultConstructor() {
            this(ElementMatchers.any());
        }

        public ForDefaultConstructor(ElementMatcher<? super MethodDescription> elementMatcher) {
            this(elementMatcher, MethodAttributeAppender.NoOp.INSTANCE);
        }

        public ForDefaultConstructor(MethodAttributeAppender.Factory methodAttributeAppenderFactory) {
            this(ElementMatchers.any(), methodAttributeAppenderFactory);
        }

        public ForDefaultConstructor(ElementMatcher<? super MethodDescription> elementMatcher, MethodAttributeAppender.Factory methodAttributeAppenderFactory) {
            this.elementMatcher = elementMatcher;
            this.methodAttributeAppenderFactory = methodAttributeAppenderFactory;
        }

        @Override // net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy
        public List<MethodDescription.Token> extractConstructors(TypeDescription instrumentedType) {
            if (instrumentedType.getSuperClass().getDeclaredMethods().filter(ElementMatchers.isConstructor()).isEmpty()) {
                throw new IllegalStateException("Cannot define default constructor for class without super class constructor");
            }
            return Collections.singletonList(new MethodDescription.Token(1));
        }

        @Override // net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy
        public MethodRegistry inject(TypeDescription instrumentedType, MethodRegistry methodRegistry) {
            MethodList<?> candidates = instrumentedType.getSuperClass().getDeclaredMethods().filter(ElementMatchers.isConstructor().and(this.elementMatcher));
            if (candidates.isEmpty()) {
                throw new IllegalStateException("No possible candidate for super constructor invocation in " + instrumentedType.getSuperClass());
            }
            if (!candidates.filter(ElementMatchers.takesArguments(0)).isEmpty()) {
                candidates = candidates.filter(ElementMatchers.takesArguments(0));
            } else if (candidates.size() > 1) {
                throw new IllegalStateException("More than one possible super constructor for constructor delegation: " + candidates);
            }
            MethodCall methodCall = MethodCall.invoke((MethodDescription) candidates.getOnly());
            for (TypeDescription typeDescription : ((MethodDescription) candidates.getOnly()).getParameters().asTypeList().asErasures()) {
                methodCall = methodCall.with(typeDescription.getDefaultValue());
            }
            return methodRegistry.append(new LatentMatcher.Resolved(ElementMatchers.isConstructor().and(ElementMatchers.takesArguments(0))), new MethodRegistry.Handler.ForImplementation(methodCall), this.methodAttributeAppenderFactory, Transformer.NoOp.make());
        }
    }
}
