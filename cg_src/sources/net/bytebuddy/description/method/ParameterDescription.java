package net.bytebuddy.description.method;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.AbstractList;
import java.util.Collections;
import java.util.List;
import net.bytebuddy.build.CachedReturnPlugin;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.ByteCodeElement;
import net.bytebuddy.description.ModifierReviewable;
import net.bytebuddy.description.NamedElement;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.annotation.AnnotationList;
import net.bytebuddy.description.annotation.AnnotationSource;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.description.type.TypeList;
import net.bytebuddy.implementation.bytecode.StackSize;
import net.bytebuddy.matcher.ElementMatcher;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/method/ParameterDescription.class */
public interface ParameterDescription extends AnnotationSource, NamedElement.WithRuntimeName, NamedElement.WithOptionalName, ModifierReviewable.ForParameterDescription, ByteCodeElement.TypeDependant<InDefinedShape, Token> {
    public static final String NAME_PREFIX = "arg";

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/method/ParameterDescription$InGenericShape.class */
    public interface InGenericShape extends ParameterDescription {
        @Override // net.bytebuddy.description.method.ParameterDescription, net.bytebuddy.description.method.ParameterDescription.InDefinedShape
        MethodDescription.InGenericShape getDeclaringMethod();
    }

    TypeDescription.Generic getType();

    MethodDescription getDeclaringMethod();

    int getIndex();

    boolean hasModifiers();

    int getOffset();

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/method/ParameterDescription$InDefinedShape.class */
    public interface InDefinedShape extends ParameterDescription {
        @Override // 
        MethodDescription.InDefinedShape getDeclaringMethod();

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/method/ParameterDescription$InDefinedShape$AbstractBase.class */
        public static abstract class AbstractBase extends AbstractBase implements InDefinedShape {
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // net.bytebuddy.description.ByteCodeElement.TypeDependant
            public InDefinedShape asDefined() {
                return this;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/method/ParameterDescription$AbstractBase.class */
    public static abstract class AbstractBase extends ModifierReviewable.AbstractBase implements ParameterDescription {
        private transient /* synthetic */ int hashCode_RZwaV7cJ;

        @Override // net.bytebuddy.description.ByteCodeElement.TypeDependant
        public /* bridge */ /* synthetic */ Token asToken(ElementMatcher elementMatcher) {
            return asToken((ElementMatcher<? super TypeDescription>) elementMatcher);
        }

        @Override // net.bytebuddy.description.NamedElement.WithRuntimeName
        public String getName() {
            return "arg".concat(String.valueOf(getIndex()));
        }

        @Override // net.bytebuddy.description.NamedElement.WithRuntimeName
        public String getInternalName() {
            return getName();
        }

        @Override // net.bytebuddy.description.NamedElement
        public String getActualName() {
            return isNamed() ? getName() : "";
        }

        @Override // net.bytebuddy.description.ModifierReviewable
        public int getModifiers() {
            return 0;
        }

        @Override // net.bytebuddy.description.method.ParameterDescription
        public int getOffset() {
            int size;
            TypeList parameterType = getDeclaringMethod().getParameters().asTypeList().asErasures();
            if (getDeclaringMethod().isStatic()) {
                size = StackSize.ZERO.getSize();
            } else {
                size = StackSize.SINGLE.getSize();
            }
            int offset = size;
            for (int i = 0; i < getIndex(); i++) {
                offset += ((TypeDescription) parameterType.get(i)).getStackSize().getSize();
            }
            return offset;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // net.bytebuddy.description.ByteCodeElement.TypeDependant
        public Token asToken(ElementMatcher<? super TypeDescription> matcher) {
            return new Token((TypeDescription.Generic) getType().accept(new TypeDescription.Generic.Visitor.Substitutor.ForDetachment(matcher)), getDeclaredAnnotations(), isNamed() ? getName() : Token.NO_NAME, hasModifiers() ? Integer.valueOf(getModifiers()) : Token.NO_MODIFIERS);
        }

        @CachedReturnPlugin.Enhance
        public int hashCode() {
            int hashCode = this.hashCode_RZwaV7cJ != 0 ? 0 : getDeclaringMethod().hashCode() ^ getIndex();
            if (hashCode == 0) {
                hashCode = this.hashCode_RZwaV7cJ;
            } else {
                this.hashCode_RZwaV7cJ = hashCode;
            }
            return hashCode;
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof ParameterDescription)) {
                return false;
            }
            ParameterDescription parameterDescription = (ParameterDescription) other;
            return getDeclaringMethod().equals(parameterDescription.getDeclaringMethod()) && getIndex() == parameterDescription.getIndex();
        }

        public String toString() {
            String name;
            StringBuilder stringBuilder = new StringBuilder(Modifier.toString(getModifiers()));
            if (getModifiers() != 0) {
                stringBuilder.append(' ');
            }
            if (isVarArgs()) {
                name = getType().asErasure().getName().replaceFirst("\\[\\]$", "...");
            } else {
                name = getType().asErasure().getName();
            }
            stringBuilder.append(name);
            return stringBuilder.append(' ').append(getName()).toString();
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/method/ParameterDescription$ForLoadedParameter.class */
    public static abstract class ForLoadedParameter<T extends AccessibleObject> extends InDefinedShape.AbstractBase {
        private static final Dispatcher DISPATCHER = (Dispatcher) AccessController.doPrivileged(Dispatcher.CreationAction.INSTANCE);
        protected final T executable;
        protected final int index;
        protected final ParameterAnnotationSource parameterAnnotationSource;

        protected ForLoadedParameter(T executable, int index, ParameterAnnotationSource parameterAnnotationSource) {
            this.executable = executable;
            this.index = index;
            this.parameterAnnotationSource = parameterAnnotationSource;
        }

        @Override // net.bytebuddy.description.method.ParameterDescription.AbstractBase, net.bytebuddy.description.NamedElement.WithRuntimeName
        public String getName() {
            return DISPATCHER.getName(this.executable, this.index);
        }

        @Override // net.bytebuddy.description.method.ParameterDescription
        public int getIndex() {
            return this.index;
        }

        @Override // net.bytebuddy.description.NamedElement.WithOptionalName
        public boolean isNamed() {
            return DISPATCHER.isNamePresent(this.executable, this.index);
        }

        @Override // net.bytebuddy.description.method.ParameterDescription.AbstractBase, net.bytebuddy.description.ModifierReviewable
        public int getModifiers() {
            return DISPATCHER.getModifiers(this.executable, this.index);
        }

        @Override // net.bytebuddy.description.method.ParameterDescription
        public boolean hasModifiers() {
            return isNamed() || getModifiers() != 0;
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/method/ParameterDescription$ForLoadedParameter$ParameterAnnotationSource.class */
        public interface ParameterAnnotationSource {
            Annotation[][] getParameterAnnotations();

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/method/ParameterDescription$ForLoadedParameter$ParameterAnnotationSource$ForLoadedConstructor.class */
            public static class ForLoadedConstructor implements ParameterAnnotationSource {
                private final Constructor<?> constructor;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.constructor.equals(((ForLoadedConstructor) obj).constructor);
                }

                public int hashCode() {
                    return (17 * 31) + this.constructor.hashCode();
                }

                public ForLoadedConstructor(Constructor<?> constructor) {
                    this.constructor = constructor;
                }

                @Override // net.bytebuddy.description.method.ParameterDescription.ForLoadedParameter.ParameterAnnotationSource
                public Annotation[][] getParameterAnnotations() {
                    return this.constructor.getParameterAnnotations();
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/method/ParameterDescription$ForLoadedParameter$ParameterAnnotationSource$ForLoadedMethod.class */
            public static class ForLoadedMethod implements ParameterAnnotationSource {
                private final Method method;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.method.equals(((ForLoadedMethod) obj).method);
                }

                public int hashCode() {
                    return (17 * 31) + this.method.hashCode();
                }

                public ForLoadedMethod(Method method) {
                    this.method = method;
                }

                @Override // net.bytebuddy.description.method.ParameterDescription.ForLoadedParameter.ParameterAnnotationSource
                public Annotation[][] getParameterAnnotations() {
                    return this.method.getParameterAnnotations();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/method/ParameterDescription$ForLoadedParameter$Dispatcher.class */
        public interface Dispatcher {
            int getModifiers(AccessibleObject accessibleObject, int i);

            boolean isNamePresent(AccessibleObject accessibleObject, int i);

            String getName(AccessibleObject accessibleObject, int i);

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/method/ParameterDescription$ForLoadedParameter$Dispatcher$CreationAction.class */
            public enum CreationAction implements PrivilegedAction<Dispatcher> {
                INSTANCE;

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                @SuppressFBWarnings(value = {"REC_CATCH_EXCEPTION"}, justification = "Exception should not be rethrown but trigger a fallback")
                public Dispatcher run() {
                    try {
                        Class<?> executableType = Class.forName("java.lang.reflect.Executable");
                        Class<?> parameterType = Class.forName("java.lang.reflect.Parameter");
                        return new ForJava8CapableVm(executableType.getMethod("getParameters", new Class[0]), parameterType.getMethod("getName", new Class[0]), parameterType.getMethod("isNamePresent", new Class[0]), parameterType.getMethod("getModifiers", new Class[0]));
                    } catch (Exception e) {
                        return ForLegacyVm.INSTANCE;
                    }
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/method/ParameterDescription$ForLoadedParameter$Dispatcher$ForJava8CapableVm.class */
            public static class ForJava8CapableVm implements Dispatcher {
                private static final Object[] NO_ARGUMENTS = new Object[0];
                private final Method getParameters;
                private final Method getName;
                private final Method isNamePresent;
                private final Method getModifiers;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.getParameters.equals(((ForJava8CapableVm) obj).getParameters) && this.getName.equals(((ForJava8CapableVm) obj).getName) && this.isNamePresent.equals(((ForJava8CapableVm) obj).isNamePresent) && this.getModifiers.equals(((ForJava8CapableVm) obj).getModifiers);
                }

                public int hashCode() {
                    return (((((((17 * 31) + this.getParameters.hashCode()) * 31) + this.getName.hashCode()) * 31) + this.isNamePresent.hashCode()) * 31) + this.getModifiers.hashCode();
                }

                protected ForJava8CapableVm(Method getParameters, Method getName, Method isNamePresent, Method getModifiers) {
                    this.getParameters = getParameters;
                    this.getName = getName;
                    this.isNamePresent = isNamePresent;
                    this.getModifiers = getModifiers;
                }

                @Override // net.bytebuddy.description.method.ParameterDescription.ForLoadedParameter.Dispatcher
                public int getModifiers(AccessibleObject executable, int index) {
                    try {
                        return ((Integer) this.getModifiers.invoke(getParameter(executable, index), NO_ARGUMENTS)).intValue();
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access java.lang.reflect.Parameter#getModifiers", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.reflect.Parameter#getModifiers", exception2.getCause());
                    }
                }

                @Override // net.bytebuddy.description.method.ParameterDescription.ForLoadedParameter.Dispatcher
                public boolean isNamePresent(AccessibleObject executable, int index) {
                    try {
                        return ((Boolean) this.isNamePresent.invoke(getParameter(executable, index), NO_ARGUMENTS)).booleanValue();
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access java.lang.reflect.Parameter#isNamePresent", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.reflect.Parameter#isNamePresent", exception2.getCause());
                    }
                }

                @Override // net.bytebuddy.description.method.ParameterDescription.ForLoadedParameter.Dispatcher
                public String getName(AccessibleObject executable, int index) {
                    try {
                        return (String) this.getName.invoke(getParameter(executable, index), NO_ARGUMENTS);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access java.lang.reflect.Parameter#getName", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.reflect.Parameter#getName", exception2.getCause());
                    }
                }

                private Object getParameter(AccessibleObject executable, int index) {
                    try {
                        return Array.get(this.getParameters.invoke(executable, NO_ARGUMENTS), index);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access java.lang.reflect.Executable#getParameters", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.reflect.Executable#getParameters", exception2.getCause());
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/method/ParameterDescription$ForLoadedParameter$Dispatcher$ForLegacyVm.class */
            public enum ForLegacyVm implements Dispatcher {
                INSTANCE;

                @Override // net.bytebuddy.description.method.ParameterDescription.ForLoadedParameter.Dispatcher
                public int getModifiers(AccessibleObject executable, int index) {
                    throw new UnsupportedOperationException("Cannot dispatch method for java.lang.reflect.Parameter");
                }

                @Override // net.bytebuddy.description.method.ParameterDescription.ForLoadedParameter.Dispatcher
                public boolean isNamePresent(AccessibleObject executable, int index) {
                    throw new UnsupportedOperationException("Cannot dispatch method for java.lang.reflect.Parameter");
                }

                @Override // net.bytebuddy.description.method.ParameterDescription.ForLoadedParameter.Dispatcher
                public String getName(AccessibleObject executable, int index) {
                    throw new UnsupportedOperationException("Cannot dispatch method for java.lang.reflect.Parameter");
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/method/ParameterDescription$ForLoadedParameter$OfMethod.class */
        protected static class OfMethod extends ForLoadedParameter<Method> {
            /* JADX INFO: Access modifiers changed from: protected */
            public OfMethod(Method method, int index, ParameterAnnotationSource parameterAnnotationSource) {
                super(method, index, parameterAnnotationSource);
            }

            @Override // net.bytebuddy.description.method.ParameterDescription, net.bytebuddy.description.method.ParameterDescription.InDefinedShape
            @SuppressFBWarnings(value = {"BC_UNCONFIRMED_CAST"}, justification = "The implicit field type casting is not understood by Findbugs")
            public MethodDescription.InDefinedShape getDeclaringMethod() {
                return new MethodDescription.ForLoadedMethod((Method) this.executable);
            }

            @Override // net.bytebuddy.description.method.ParameterDescription
            @SuppressFBWarnings(value = {"BC_UNCONFIRMED_CAST"}, justification = "The implicit field type casting is not understood by Findbugs")
            public TypeDescription.Generic getType() {
                if (TypeDescription.AbstractBase.RAW_TYPES) {
                    return TypeDescription.Generic.OfNonGenericType.ForLoadedType.of(((Method) this.executable).getParameterTypes()[this.index]);
                }
                return new TypeDescription.Generic.LazyProjection.OfMethodParameter((Method) this.executable, this.index, ((Method) this.executable).getParameterTypes());
            }

            @Override // net.bytebuddy.description.annotation.AnnotationSource
            @SuppressFBWarnings(value = {"BC_UNCONFIRMED_CAST"}, justification = "The implicit field type casting is not understood by Findbugs")
            public AnnotationList getDeclaredAnnotations() {
                return new AnnotationList.ForLoadedAnnotations(this.parameterAnnotationSource.getParameterAnnotations()[this.index]);
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/method/ParameterDescription$ForLoadedParameter$OfConstructor.class */
        protected static class OfConstructor extends ForLoadedParameter<Constructor<?>> {
            /* JADX INFO: Access modifiers changed from: protected */
            public OfConstructor(Constructor<?> constructor, int index, ParameterAnnotationSource parameterAnnotationSource) {
                super(constructor, index, parameterAnnotationSource);
            }

            @Override // net.bytebuddy.description.method.ParameterDescription, net.bytebuddy.description.method.ParameterDescription.InDefinedShape
            @SuppressFBWarnings(value = {"BC_UNCONFIRMED_CAST"}, justification = "The implicit field type casting is not understood by Findbugs")
            public MethodDescription.InDefinedShape getDeclaringMethod() {
                return new MethodDescription.ForLoadedConstructor((Constructor) this.executable);
            }

            @Override // net.bytebuddy.description.method.ParameterDescription
            @SuppressFBWarnings(value = {"BC_UNCONFIRMED_CAST"}, justification = "The implicit field type casting is not understood by Findbugs")
            public TypeDescription.Generic getType() {
                if (TypeDescription.AbstractBase.RAW_TYPES) {
                    return TypeDescription.Generic.OfNonGenericType.ForLoadedType.of(((Constructor) this.executable).getParameterTypes()[this.index]);
                }
                return new TypeDescription.Generic.LazyProjection.OfConstructorParameter((Constructor) this.executable, this.index, ((Constructor) this.executable).getParameterTypes());
            }

            @Override // net.bytebuddy.description.annotation.AnnotationSource
            @SuppressFBWarnings(value = {"BC_UNCONFIRMED_CAST"}, justification = "The implicit field type casting is not understood by Findbugs")
            public AnnotationList getDeclaredAnnotations() {
                Annotation[][] annotation = this.parameterAnnotationSource.getParameterAnnotations();
                MethodDescription.InDefinedShape declaringMethod = getDeclaringMethod();
                if (annotation.length != declaringMethod.getParameters().size() && declaringMethod.getDeclaringType().isInnerClass()) {
                    return this.index == 0 ? new AnnotationList.Empty() : new AnnotationList.ForLoadedAnnotations(annotation[this.index - 1]);
                }
                return new AnnotationList.ForLoadedAnnotations(annotation[this.index]);
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/method/ParameterDescription$ForLoadedParameter$OfLegacyVmMethod.class */
        protected static class OfLegacyVmMethod extends InDefinedShape.AbstractBase {
            private final Method method;
            private final int index;
            private final Class<?>[] parameterType;
            private final ParameterAnnotationSource parameterAnnotationSource;

            /* JADX INFO: Access modifiers changed from: protected */
            public OfLegacyVmMethod(Method method, int index, Class<?>[] parameterType, ParameterAnnotationSource parameterAnnotationSource) {
                this.method = method;
                this.index = index;
                this.parameterType = parameterType;
                this.parameterAnnotationSource = parameterAnnotationSource;
            }

            @Override // net.bytebuddy.description.method.ParameterDescription
            public TypeDescription.Generic getType() {
                if (TypeDescription.AbstractBase.RAW_TYPES) {
                    return TypeDescription.Generic.OfNonGenericType.ForLoadedType.of(this.parameterType[this.index]);
                }
                return new TypeDescription.Generic.LazyProjection.OfMethodParameter(this.method, this.index, this.parameterType);
            }

            @Override // net.bytebuddy.description.method.ParameterDescription, net.bytebuddy.description.method.ParameterDescription.InDefinedShape
            public MethodDescription.InDefinedShape getDeclaringMethod() {
                return new MethodDescription.ForLoadedMethod(this.method);
            }

            @Override // net.bytebuddy.description.method.ParameterDescription
            public int getIndex() {
                return this.index;
            }

            @Override // net.bytebuddy.description.NamedElement.WithOptionalName
            public boolean isNamed() {
                return false;
            }

            @Override // net.bytebuddy.description.method.ParameterDescription
            public boolean hasModifiers() {
                return false;
            }

            @Override // net.bytebuddy.description.annotation.AnnotationSource
            public AnnotationList getDeclaredAnnotations() {
                return new AnnotationList.ForLoadedAnnotations(this.parameterAnnotationSource.getParameterAnnotations()[this.index]);
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/method/ParameterDescription$ForLoadedParameter$OfLegacyVmConstructor.class */
        protected static class OfLegacyVmConstructor extends InDefinedShape.AbstractBase {
            private final Constructor<?> constructor;
            private final int index;
            private final Class<?>[] parameterType;
            private final ParameterAnnotationSource parameterAnnotationSource;

            /* JADX INFO: Access modifiers changed from: protected */
            public OfLegacyVmConstructor(Constructor<?> constructor, int index, Class<?>[] parameterType, ParameterAnnotationSource parameterAnnotationSource) {
                this.constructor = constructor;
                this.index = index;
                this.parameterType = parameterType;
                this.parameterAnnotationSource = parameterAnnotationSource;
            }

            @Override // net.bytebuddy.description.method.ParameterDescription
            public TypeDescription.Generic getType() {
                if (TypeDescription.AbstractBase.RAW_TYPES) {
                    return TypeDescription.Generic.OfNonGenericType.ForLoadedType.of(this.parameterType[this.index]);
                }
                return new TypeDescription.Generic.LazyProjection.OfConstructorParameter(this.constructor, this.index, this.parameterType);
            }

            @Override // net.bytebuddy.description.method.ParameterDescription, net.bytebuddy.description.method.ParameterDescription.InDefinedShape
            public MethodDescription.InDefinedShape getDeclaringMethod() {
                return new MethodDescription.ForLoadedConstructor(this.constructor);
            }

            @Override // net.bytebuddy.description.method.ParameterDescription
            public int getIndex() {
                return this.index;
            }

            @Override // net.bytebuddy.description.NamedElement.WithOptionalName
            public boolean isNamed() {
                return false;
            }

            @Override // net.bytebuddy.description.method.ParameterDescription
            public boolean hasModifiers() {
                return false;
            }

            @Override // net.bytebuddy.description.annotation.AnnotationSource
            public AnnotationList getDeclaredAnnotations() {
                MethodDescription.InDefinedShape declaringMethod = getDeclaringMethod();
                Annotation[][] parameterAnnotation = this.parameterAnnotationSource.getParameterAnnotations();
                if (parameterAnnotation.length != declaringMethod.getParameters().size() && declaringMethod.getDeclaringType().isInnerClass()) {
                    return this.index == 0 ? new AnnotationList.Empty() : new AnnotationList.ForLoadedAnnotations(parameterAnnotation[this.index - 1]);
                }
                return new AnnotationList.ForLoadedAnnotations(parameterAnnotation[this.index]);
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/method/ParameterDescription$Latent.class */
    public static class Latent extends InDefinedShape.AbstractBase {
        private final MethodDescription.InDefinedShape declaringMethod;
        private final TypeDescription.Generic parameterType;
        private final List<? extends AnnotationDescription> declaredAnnotations;
        private final String name;
        private final Integer modifiers;
        private final int index;
        private final int offset;

        public Latent(MethodDescription.InDefinedShape declaringMethod, Token token, int index, int offset) {
            this(declaringMethod, token.getType(), token.getAnnotations(), token.getName(), token.getModifiers(), index, offset);
        }

        public Latent(MethodDescription.InDefinedShape declaringMethod, TypeDescription.Generic parameterType, int index, int offset) {
            this(declaringMethod, parameterType, Collections.emptyList(), Token.NO_NAME, Token.NO_MODIFIERS, index, offset);
        }

        public Latent(MethodDescription.InDefinedShape declaringMethod, TypeDescription.Generic parameterType, List<? extends AnnotationDescription> declaredAnnotations, String name, Integer modifiers, int index, int offset) {
            this.declaringMethod = declaringMethod;
            this.parameterType = parameterType;
            this.declaredAnnotations = declaredAnnotations;
            this.name = name;
            this.modifiers = modifiers;
            this.index = index;
            this.offset = offset;
        }

        @Override // net.bytebuddy.description.method.ParameterDescription
        public TypeDescription.Generic getType() {
            return (TypeDescription.Generic) this.parameterType.accept(TypeDescription.Generic.Visitor.Substitutor.ForAttachment.of(this));
        }

        @Override // net.bytebuddy.description.method.ParameterDescription, net.bytebuddy.description.method.ParameterDescription.InDefinedShape
        public MethodDescription.InDefinedShape getDeclaringMethod() {
            return this.declaringMethod;
        }

        @Override // net.bytebuddy.description.method.ParameterDescription
        public int getIndex() {
            return this.index;
        }

        @Override // net.bytebuddy.description.method.ParameterDescription.AbstractBase, net.bytebuddy.description.method.ParameterDescription
        public int getOffset() {
            return this.offset;
        }

        @Override // net.bytebuddy.description.NamedElement.WithOptionalName
        public boolean isNamed() {
            return this.name != null;
        }

        @Override // net.bytebuddy.description.method.ParameterDescription
        public boolean hasModifiers() {
            return this.modifiers != null;
        }

        @Override // net.bytebuddy.description.method.ParameterDescription.AbstractBase, net.bytebuddy.description.NamedElement.WithRuntimeName
        public String getName() {
            return isNamed() ? this.name : super.getName();
        }

        @Override // net.bytebuddy.description.method.ParameterDescription.AbstractBase, net.bytebuddy.description.ModifierReviewable
        public int getModifiers() {
            if (hasModifiers()) {
                return this.modifiers.intValue();
            }
            return super.getModifiers();
        }

        @Override // net.bytebuddy.description.annotation.AnnotationSource
        public AnnotationList getDeclaredAnnotations() {
            return new AnnotationList.Explicit(this.declaredAnnotations);
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/method/ParameterDescription$TypeSubstituting.class */
    public static class TypeSubstituting extends AbstractBase implements InGenericShape {
        private final MethodDescription.InGenericShape declaringMethod;
        private final ParameterDescription parameterDescription;
        private final TypeDescription.Generic.Visitor<? extends TypeDescription.Generic> visitor;

        public TypeSubstituting(MethodDescription.InGenericShape declaringMethod, ParameterDescription parameterDescription, TypeDescription.Generic.Visitor<? extends TypeDescription.Generic> visitor) {
            this.declaringMethod = declaringMethod;
            this.parameterDescription = parameterDescription;
            this.visitor = visitor;
        }

        @Override // net.bytebuddy.description.method.ParameterDescription
        public TypeDescription.Generic getType() {
            return (TypeDescription.Generic) this.parameterDescription.getType().accept(this.visitor);
        }

        @Override // net.bytebuddy.description.method.ParameterDescription, net.bytebuddy.description.method.ParameterDescription.InDefinedShape
        public MethodDescription.InGenericShape getDeclaringMethod() {
            return this.declaringMethod;
        }

        @Override // net.bytebuddy.description.method.ParameterDescription
        public int getIndex() {
            return this.parameterDescription.getIndex();
        }

        @Override // net.bytebuddy.description.NamedElement.WithOptionalName
        public boolean isNamed() {
            return this.parameterDescription.isNamed();
        }

        @Override // net.bytebuddy.description.method.ParameterDescription
        public boolean hasModifiers() {
            return this.parameterDescription.hasModifiers();
        }

        @Override // net.bytebuddy.description.method.ParameterDescription.AbstractBase, net.bytebuddy.description.method.ParameterDescription
        public int getOffset() {
            return this.parameterDescription.getOffset();
        }

        @Override // net.bytebuddy.description.method.ParameterDescription.AbstractBase, net.bytebuddy.description.NamedElement.WithRuntimeName
        public String getName() {
            return this.parameterDescription.getName();
        }

        @Override // net.bytebuddy.description.method.ParameterDescription.AbstractBase, net.bytebuddy.description.ModifierReviewable
        public int getModifiers() {
            return this.parameterDescription.getModifiers();
        }

        @Override // net.bytebuddy.description.annotation.AnnotationSource
        public AnnotationList getDeclaredAnnotations() {
            return this.parameterDescription.getDeclaredAnnotations();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // net.bytebuddy.description.ByteCodeElement.TypeDependant
        public InDefinedShape asDefined() {
            return this.parameterDescription.asDefined();
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/method/ParameterDescription$Token.class */
    public static class Token implements ByteCodeElement.Token<Token> {
        public static final String NO_NAME = null;
        public static final Integer NO_MODIFIERS = null;
        private final TypeDescription.Generic type;
        private final List<? extends AnnotationDescription> annotations;
        private final String name;
        private final Integer modifiers;
        private transient /* synthetic */ int hashCode_2Ip9SAGg;

        @Override // net.bytebuddy.description.ByteCodeElement.Token
        public /* bridge */ /* synthetic */ Token accept(TypeDescription.Generic.Visitor visitor) {
            return accept((TypeDescription.Generic.Visitor<? extends TypeDescription.Generic>) visitor);
        }

        public Token(TypeDescription.Generic type) {
            this(type, Collections.emptyList());
        }

        public Token(TypeDescription.Generic type, List<? extends AnnotationDescription> annotations) {
            this(type, annotations, NO_NAME, NO_MODIFIERS);
        }

        public Token(TypeDescription.Generic type, String name, Integer modifiers) {
            this(type, Collections.emptyList(), name, modifiers);
        }

        public Token(TypeDescription.Generic type, List<? extends AnnotationDescription> annotations, String name, Integer modifiers) {
            this.type = type;
            this.annotations = annotations;
            this.name = name;
            this.modifiers = modifiers;
        }

        public TypeDescription.Generic getType() {
            return this.type;
        }

        public AnnotationList getAnnotations() {
            return new AnnotationList.Explicit(this.annotations);
        }

        public String getName() {
            return this.name;
        }

        public Integer getModifiers() {
            return this.modifiers;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // net.bytebuddy.description.ByteCodeElement.Token
        public Token accept(TypeDescription.Generic.Visitor<? extends TypeDescription.Generic> visitor) {
            return new Token((TypeDescription.Generic) this.type.accept(visitor), this.annotations, this.name, this.modifiers);
        }

        @CachedReturnPlugin.Enhance
        public int hashCode() {
            int hashCode;
            if (this.hashCode_2Ip9SAGg != 0) {
                hashCode = 0;
            } else {
                int result = this.type.hashCode();
                hashCode = (31 * ((31 * ((31 * result) + this.annotations.hashCode())) + (this.name != null ? this.name.hashCode() : 0))) + (this.modifiers != null ? this.modifiers.hashCode() : 0);
            }
            int i = hashCode;
            if (i == 0) {
                i = this.hashCode_2Ip9SAGg;
            } else {
                this.hashCode_2Ip9SAGg = i;
            }
            return i;
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Token)) {
                return false;
            }
            Token token = (Token) other;
            return this.type.equals(token.type) && this.annotations.equals(token.annotations) && (this.name == null ? token.name == null : this.name.equals(token.name)) && (this.modifiers == null ? token.modifiers == null : this.modifiers.equals(token.modifiers));
        }

        public String toString() {
            return "ParameterDescription.Token{type=" + this.type + ", annotations=" + this.annotations + ", name='" + this.name + "', modifiers=" + this.modifiers + '}';
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/method/ParameterDescription$Token$TypeList.class */
        public static class TypeList extends AbstractList<Token> {
            private final List<? extends TypeDefinition> typeDescriptions;

            public TypeList(List<? extends TypeDefinition> typeDescriptions) {
                this.typeDescriptions = typeDescriptions;
            }

            @Override // java.util.AbstractList, java.util.List
            public Token get(int index) {
                return new Token(this.typeDescriptions.get(index).asGenericType());
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public int size() {
                return this.typeDescriptions.size();
            }
        }
    }
}
