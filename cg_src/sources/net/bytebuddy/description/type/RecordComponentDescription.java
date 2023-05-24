package net.bytebuddy.description.type;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.GenericSignatureFormatError;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.List;
import net.bytebuddy.build.CachedReturnPlugin;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.ByteCodeElement;
import net.bytebuddy.description.DeclaredByType;
import net.bytebuddy.description.NamedElement;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.annotation.AnnotationList;
import net.bytebuddy.description.annotation.AnnotationSource;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.jar.asm.signature.SignatureVisitor;
import net.bytebuddy.jar.asm.signature.SignatureWriter;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/RecordComponentDescription.class */
public interface RecordComponentDescription extends DeclaredByType, NamedElement.WithDescriptor, AnnotationSource, ByteCodeElement.TypeDependant<InDefinedShape, Token> {

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/RecordComponentDescription$InGenericShape.class */
    public interface InGenericShape extends RecordComponentDescription {
        @Override // net.bytebuddy.description.type.RecordComponentDescription, net.bytebuddy.description.type.RecordComponentDescription.InDefinedShape
        MethodDescription.InGenericShape getAccessor();
    }

    TypeDescription.Generic getType();

    MethodDescription getAccessor();

    @Override // net.bytebuddy.description.ByteCodeElement.TypeDependant
    Token asToken(ElementMatcher<? super TypeDescription> elementMatcher);

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/RecordComponentDescription$InDefinedShape.class */
    public interface InDefinedShape extends RecordComponentDescription {
        @Override // 
        MethodDescription.InDefinedShape getAccessor();

        @Override // net.bytebuddy.description.DeclaredByType, net.bytebuddy.description.field.FieldDescription.InDefinedShape
        TypeDescription getDeclaringType();

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/RecordComponentDescription$InDefinedShape$AbstractBase.class */
        public static abstract class AbstractBase extends AbstractBase implements InDefinedShape {
            @Override // net.bytebuddy.description.type.RecordComponentDescription, net.bytebuddy.description.type.RecordComponentDescription.InDefinedShape
            public MethodDescription.InDefinedShape getAccessor() {
                return (MethodDescription.InDefinedShape) getDeclaringType().getDeclaredMethods().filter(ElementMatchers.named(getActualName())).getOnly();
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // net.bytebuddy.description.ByteCodeElement.TypeDependant
            public InDefinedShape asDefined() {
                return this;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/RecordComponentDescription$AbstractBase.class */
    public static abstract class AbstractBase implements RecordComponentDescription {
        @Override // net.bytebuddy.description.ByteCodeElement.TypeDependant
        public /* bridge */ /* synthetic */ Token asToken(ElementMatcher elementMatcher) {
            return asToken((ElementMatcher<? super TypeDescription>) elementMatcher);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // net.bytebuddy.description.type.RecordComponentDescription, net.bytebuddy.description.ByteCodeElement.TypeDependant
        public Token asToken(ElementMatcher<? super TypeDescription> matcher) {
            return new Token(getActualName(), (TypeDescription.Generic) getType().accept(new TypeDescription.Generic.Visitor.Substitutor.ForDetachment(matcher)), getDeclaredAnnotations());
        }

        @Override // net.bytebuddy.description.NamedElement.WithDescriptor
        public String getDescriptor() {
            return getType().asErasure().getDescriptor();
        }

        @Override // net.bytebuddy.description.NamedElement.WithDescriptor
        public String getGenericSignature() {
            TypeDescription.Generic recordComponentType = getType();
            try {
                return recordComponentType.getSort().isNonGeneric() ? NON_GENERIC_SIGNATURE : ((SignatureVisitor) recordComponentType.accept(new TypeDescription.Generic.Visitor.ForSignatureVisitor(new SignatureWriter()))).toString();
            } catch (GenericSignatureFormatError e) {
                return NON_GENERIC_SIGNATURE;
            }
        }

        public int hashCode() {
            return getActualName().hashCode();
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof RecordComponentDescription)) {
                return false;
            }
            RecordComponentDescription recordComponentDescription = (RecordComponentDescription) other;
            return getActualName().equals(recordComponentDescription.getActualName());
        }

        public String toString() {
            return getType().getTypeName() + Instruction.argsep + getActualName();
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/RecordComponentDescription$ForLoadedRecordComponent.class */
    public static class ForLoadedRecordComponent extends InDefinedShape.AbstractBase {
        protected static final Dispatcher DISPATCHER = (Dispatcher) AccessController.doPrivileged(Dispatcher.CreationAction.INSTANCE);
        private final AnnotatedElement recordComponent;

        /* JADX INFO: Access modifiers changed from: protected */
        public ForLoadedRecordComponent(AnnotatedElement recordComponent) {
            this.recordComponent = recordComponent;
        }

        public static RecordComponentDescription of(Object recordComponent) {
            if (!DISPATCHER.isInstance(recordComponent)) {
                throw new IllegalArgumentException("Not a record component: " + recordComponent);
            }
            return new ForLoadedRecordComponent((AnnotatedElement) recordComponent);
        }

        @Override // net.bytebuddy.description.type.RecordComponentDescription
        public TypeDescription.Generic getType() {
            return new TypeDescription.Generic.LazyProjection.OfRecordComponent(this.recordComponent);
        }

        @Override // net.bytebuddy.description.type.RecordComponentDescription.InDefinedShape.AbstractBase, net.bytebuddy.description.type.RecordComponentDescription, net.bytebuddy.description.type.RecordComponentDescription.InDefinedShape
        public MethodDescription.InDefinedShape getAccessor() {
            return new MethodDescription.ForLoadedMethod(DISPATCHER.getAccessor(this.recordComponent));
        }

        @Override // net.bytebuddy.description.DeclaredByType, net.bytebuddy.description.field.FieldDescription.InDefinedShape
        public TypeDescription getDeclaringType() {
            return TypeDescription.ForLoadedType.of(DISPATCHER.getDeclaringType(this.recordComponent));
        }

        @Override // net.bytebuddy.description.NamedElement
        public String getActualName() {
            return DISPATCHER.getName(this.recordComponent);
        }

        @Override // net.bytebuddy.description.type.RecordComponentDescription.AbstractBase, net.bytebuddy.description.NamedElement.WithDescriptor
        public String getGenericSignature() {
            return DISPATCHER.getGenericSignature(this.recordComponent);
        }

        @Override // net.bytebuddy.description.annotation.AnnotationSource
        public AnnotationList getDeclaredAnnotations() {
            return new AnnotationList.ForLoadedAnnotations(this.recordComponent.getDeclaredAnnotations());
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/RecordComponentDescription$ForLoadedRecordComponent$Dispatcher.class */
        public interface Dispatcher {
            boolean isInstance(Object obj);

            Object[] getRecordComponents(Class<?> cls);

            boolean isRecord(Class<?> cls);

            String getName(Object obj);

            Class<?> getDeclaringType(Object obj);

            Method getAccessor(Object obj);

            Class<?> getType(Object obj);

            Type getGenericType(Object obj);

            String getGenericSignature(Object obj);

            AnnotatedElement getAnnotatedType(Object obj);

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/RecordComponentDescription$ForLoadedRecordComponent$Dispatcher$CreationAction.class */
            public enum CreationAction implements PrivilegedAction<Dispatcher> {
                INSTANCE;

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public Dispatcher run() {
                    try {
                        Class<?> recordComponent = Class.forName("java.lang.reflect.RecordComponent");
                        return new ForJava14CapableVm(recordComponent, Class.class.getMethod("getRecordComponents", new Class[0]), Class.class.getMethod("isRecord", new Class[0]), recordComponent.getMethod("getName", new Class[0]), recordComponent.getMethod("getDeclaringRecord", new Class[0]), recordComponent.getMethod("getAccessor", new Class[0]), recordComponent.getMethod("getType", new Class[0]), recordComponent.getMethod("getGenericType", new Class[0]), recordComponent.getMethod("getGenericSignature", new Class[0]), recordComponent.getMethod("getAnnotatedType", new Class[0]));
                    } catch (ClassNotFoundException e) {
                        return ForLegacyVm.INSTANCE;
                    } catch (NoSuchMethodException e2) {
                        return ForLegacyVm.INSTANCE;
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/RecordComponentDescription$ForLoadedRecordComponent$Dispatcher$ForLegacyVm.class */
            public enum ForLegacyVm implements Dispatcher {
                INSTANCE;

                @Override // net.bytebuddy.description.type.RecordComponentDescription.ForLoadedRecordComponent.Dispatcher
                public boolean isInstance(Object instance) {
                    return false;
                }

                @Override // net.bytebuddy.description.type.RecordComponentDescription.ForLoadedRecordComponent.Dispatcher
                @SuppressFBWarnings(value = {"PZLA_PREFER_ZERO_LENGTH_ARRAYS"}, justification = "Null value return is aligned with OpenJDK return value.")
                public Object[] getRecordComponents(Class<?> type) {
                    return null;
                }

                @Override // net.bytebuddy.description.type.RecordComponentDescription.ForLoadedRecordComponent.Dispatcher
                public boolean isRecord(Class<?> type) {
                    return false;
                }

                @Override // net.bytebuddy.description.type.RecordComponentDescription.ForLoadedRecordComponent.Dispatcher
                public String getName(Object recordComponent) {
                    throw new IllegalStateException("The current VM does not support record components");
                }

                @Override // net.bytebuddy.description.type.RecordComponentDescription.ForLoadedRecordComponent.Dispatcher
                public Class<?> getDeclaringType(Object recordComponent) {
                    throw new IllegalStateException("The current VM does not support record components");
                }

                @Override // net.bytebuddy.description.type.RecordComponentDescription.ForLoadedRecordComponent.Dispatcher
                public Method getAccessor(Object recordComponent) {
                    throw new IllegalStateException("The current VM does not support record components");
                }

                @Override // net.bytebuddy.description.type.RecordComponentDescription.ForLoadedRecordComponent.Dispatcher
                public Class<?> getType(Object recordComponent) {
                    throw new IllegalStateException("The current VM does not support record components");
                }

                @Override // net.bytebuddy.description.type.RecordComponentDescription.ForLoadedRecordComponent.Dispatcher
                public Type getGenericType(Object recordComponent) {
                    throw new IllegalStateException("The current VM does not support record components");
                }

                @Override // net.bytebuddy.description.type.RecordComponentDescription.ForLoadedRecordComponent.Dispatcher
                public String getGenericSignature(Object recordComponent) {
                    throw new IllegalStateException("The current VM does not support record components");
                }

                @Override // net.bytebuddy.description.type.RecordComponentDescription.ForLoadedRecordComponent.Dispatcher
                public AnnotatedElement getAnnotatedType(Object recordComponent) {
                    throw new IllegalStateException("The current VM does not support record components");
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/RecordComponentDescription$ForLoadedRecordComponent$Dispatcher$ForJava14CapableVm.class */
            public static class ForJava14CapableVm implements Dispatcher {
                private final Class<?> recordComponent;
                private final Method getRecordComponents;
                private final Method isRecord;
                private final Method getName;
                private final Method getDeclaringType;
                private final Method getAccessor;
                private final Method getType;
                private final Method getGenericType;
                private final Method getGenericSignature;
                private final Method getAnnotatedType;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.recordComponent.equals(((ForJava14CapableVm) obj).recordComponent) && this.getRecordComponents.equals(((ForJava14CapableVm) obj).getRecordComponents) && this.isRecord.equals(((ForJava14CapableVm) obj).isRecord) && this.getName.equals(((ForJava14CapableVm) obj).getName) && this.getDeclaringType.equals(((ForJava14CapableVm) obj).getDeclaringType) && this.getAccessor.equals(((ForJava14CapableVm) obj).getAccessor) && this.getType.equals(((ForJava14CapableVm) obj).getType) && this.getGenericType.equals(((ForJava14CapableVm) obj).getGenericType) && this.getGenericSignature.equals(((ForJava14CapableVm) obj).getGenericSignature) && this.getAnnotatedType.equals(((ForJava14CapableVm) obj).getAnnotatedType);
                }

                public int hashCode() {
                    return (((((((((((((((((((17 * 31) + this.recordComponent.hashCode()) * 31) + this.getRecordComponents.hashCode()) * 31) + this.isRecord.hashCode()) * 31) + this.getName.hashCode()) * 31) + this.getDeclaringType.hashCode()) * 31) + this.getAccessor.hashCode()) * 31) + this.getType.hashCode()) * 31) + this.getGenericType.hashCode()) * 31) + this.getGenericSignature.hashCode()) * 31) + this.getAnnotatedType.hashCode();
                }

                protected ForJava14CapableVm(Class<?> recordComponent, Method getRecordComponents, Method isRecord, Method getName, Method getDeclaringType, Method getAccessor, Method getType, Method getGenericType, Method getGenericSignature, Method getAnnotatedType) {
                    this.recordComponent = recordComponent;
                    this.getRecordComponents = getRecordComponents;
                    this.isRecord = isRecord;
                    this.getName = getName;
                    this.getDeclaringType = getDeclaringType;
                    this.getAccessor = getAccessor;
                    this.getType = getType;
                    this.getGenericType = getGenericType;
                    this.getGenericSignature = getGenericSignature;
                    this.getAnnotatedType = getAnnotatedType;
                }

                @Override // net.bytebuddy.description.type.RecordComponentDescription.ForLoadedRecordComponent.Dispatcher
                public boolean isInstance(Object instance) {
                    return this.recordComponent.isInstance(instance);
                }

                @Override // net.bytebuddy.description.type.RecordComponentDescription.ForLoadedRecordComponent.Dispatcher
                public Object[] getRecordComponents(Class<?> type) {
                    try {
                        return (Object[]) this.getRecordComponents.invoke(type, new Object[0]);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access java.lang.Class#getRecordComponents", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.Class#getRecordComponents", exception2.getCause());
                    }
                }

                @Override // net.bytebuddy.description.type.RecordComponentDescription.ForLoadedRecordComponent.Dispatcher
                public boolean isRecord(Class<?> type) {
                    try {
                        return ((Boolean) this.isRecord.invoke(type, new Object[0])).booleanValue();
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access java.lang.Class#isRecord", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.Class#isRecord", exception2.getCause());
                    }
                }

                @Override // net.bytebuddy.description.type.RecordComponentDescription.ForLoadedRecordComponent.Dispatcher
                public String getName(Object recordComponent) {
                    try {
                        return (String) this.getName.invoke(recordComponent, new Object[0]);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access java.lang.reflection.RecordComponent#getName", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.reflection.RecordComponent#getName", exception2.getCause());
                    }
                }

                @Override // net.bytebuddy.description.type.RecordComponentDescription.ForLoadedRecordComponent.Dispatcher
                public Class<?> getDeclaringType(Object recordComponent) {
                    try {
                        return (Class) this.getDeclaringType.invoke(recordComponent, new Object[0]);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access java.lang.reflection.RecordComponent#getDeclaringType", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.reflection.RecordComponent#getDeclaringType", exception2.getCause());
                    }
                }

                @Override // net.bytebuddy.description.type.RecordComponentDescription.ForLoadedRecordComponent.Dispatcher
                public Method getAccessor(Object recordComponent) {
                    try {
                        return (Method) this.getAccessor.invoke(recordComponent, new Object[0]);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access java.lang.reflection.RecordComponent#getAccessor", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.reflection.RecordComponent#getAccessor", exception2.getCause());
                    }
                }

                @Override // net.bytebuddy.description.type.RecordComponentDescription.ForLoadedRecordComponent.Dispatcher
                public Class<?> getType(Object recordComponent) {
                    try {
                        return (Class) this.getType.invoke(recordComponent, new Object[0]);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access java.lang.reflection.RecordComponent#getType", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.reflection.RecordComponent#getType", exception2.getCause());
                    }
                }

                @Override // net.bytebuddy.description.type.RecordComponentDescription.ForLoadedRecordComponent.Dispatcher
                public Type getGenericType(Object recordComponent) {
                    try {
                        return (Type) this.getGenericType.invoke(recordComponent, new Object[0]);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access java.lang.reflection.RecordComponent#getGenericType", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.reflection.RecordComponent#getGenericType", exception2.getCause());
                    }
                }

                @Override // net.bytebuddy.description.type.RecordComponentDescription.ForLoadedRecordComponent.Dispatcher
                public String getGenericSignature(Object recordComponent) {
                    try {
                        return (String) this.getGenericSignature.invoke(recordComponent, new Object[0]);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access java.lang.reflection.RecordComponent#getGenericSignature", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.reflection.RecordComponent#getGenericSignature", exception2.getCause());
                    }
                }

                @Override // net.bytebuddy.description.type.RecordComponentDescription.ForLoadedRecordComponent.Dispatcher
                public AnnotatedElement getAnnotatedType(Object recordComponent) {
                    try {
                        return (AnnotatedElement) this.getAnnotatedType.invoke(recordComponent, new Object[0]);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access java.lang.reflection.RecordComponent#getAnnotatedType", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.reflection.RecordComponent#getAnnotatedType", exception2.getCause());
                    }
                }
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/RecordComponentDescription$Latent.class */
    public static class Latent extends InDefinedShape.AbstractBase {
        private final TypeDescription declaringType;
        private final String name;
        private final TypeDescription.Generic type;
        private final List<? extends AnnotationDescription> annotations;

        public Latent(TypeDescription declaringType, Token token) {
            this(declaringType, token.getName(), token.getType(), token.getAnnotations());
        }

        public Latent(TypeDescription declaringType, String name, TypeDescription.Generic type, List<? extends AnnotationDescription> annotations) {
            this.declaringType = declaringType;
            this.name = name;
            this.type = type;
            this.annotations = annotations;
        }

        @Override // net.bytebuddy.description.type.RecordComponentDescription
        public TypeDescription.Generic getType() {
            return (TypeDescription.Generic) this.type.accept(TypeDescription.Generic.Visitor.Substitutor.ForAttachment.of(this));
        }

        @Override // net.bytebuddy.description.DeclaredByType, net.bytebuddy.description.field.FieldDescription.InDefinedShape
        public TypeDescription getDeclaringType() {
            return this.declaringType;
        }

        @Override // net.bytebuddy.description.NamedElement
        public String getActualName() {
            return this.name;
        }

        @Override // net.bytebuddy.description.annotation.AnnotationSource
        public AnnotationList getDeclaredAnnotations() {
            return new AnnotationList.Explicit(this.annotations);
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/RecordComponentDescription$TypeSubstituting.class */
    public static class TypeSubstituting extends AbstractBase implements InGenericShape {
        private final TypeDescription.Generic declaringType;
        private final RecordComponentDescription recordComponentDescription;
        private final TypeDescription.Generic.Visitor<? extends TypeDescription.Generic> visitor;

        public TypeSubstituting(TypeDescription.Generic declaringType, RecordComponentDescription recordComponentDescription, TypeDescription.Generic.Visitor<? extends TypeDescription.Generic> visitor) {
            this.declaringType = declaringType;
            this.recordComponentDescription = recordComponentDescription;
            this.visitor = visitor;
        }

        @Override // net.bytebuddy.description.type.RecordComponentDescription, net.bytebuddy.description.type.RecordComponentDescription.InDefinedShape
        public MethodDescription.InGenericShape getAccessor() {
            return (MethodDescription.InGenericShape) this.declaringType.getDeclaredMethods().filter(ElementMatchers.named(getActualName())).getOnly();
        }

        @Override // net.bytebuddy.description.type.RecordComponentDescription
        public TypeDescription.Generic getType() {
            return (TypeDescription.Generic) this.recordComponentDescription.getType().accept(this.visitor);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // net.bytebuddy.description.ByteCodeElement.TypeDependant
        public InDefinedShape asDefined() {
            return this.recordComponentDescription.asDefined();
        }

        @Override // net.bytebuddy.description.DeclaredByType, net.bytebuddy.description.field.FieldDescription.InDefinedShape
        public TypeDefinition getDeclaringType() {
            return this.declaringType;
        }

        @Override // net.bytebuddy.description.NamedElement
        public String getActualName() {
            return this.recordComponentDescription.getActualName();
        }

        @Override // net.bytebuddy.description.annotation.AnnotationSource
        public AnnotationList getDeclaredAnnotations() {
            return this.recordComponentDescription.getDeclaredAnnotations();
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/RecordComponentDescription$Token.class */
    public static class Token implements ByteCodeElement.Token<Token> {
        private final String name;
        private final TypeDescription.Generic type;
        private final List<? extends AnnotationDescription> annotations;
        private transient /* synthetic */ int hashCode_P0R7Fqsw;

        @Override // net.bytebuddy.description.ByteCodeElement.Token
        public /* bridge */ /* synthetic */ Token accept(TypeDescription.Generic.Visitor visitor) {
            return accept((TypeDescription.Generic.Visitor<? extends TypeDescription.Generic>) visitor);
        }

        public Token(String name, TypeDescription.Generic type) {
            this(name, type, Collections.emptyList());
        }

        public Token(String name, TypeDescription.Generic type, List<? extends AnnotationDescription> annotations) {
            this.name = name;
            this.type = type;
            this.annotations = annotations;
        }

        public String getName() {
            return this.name;
        }

        public TypeDescription.Generic getType() {
            return this.type;
        }

        public AnnotationList getAnnotations() {
            return new AnnotationList.Explicit(this.annotations);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // net.bytebuddy.description.ByteCodeElement.Token
        public Token accept(TypeDescription.Generic.Visitor<? extends TypeDescription.Generic> visitor) {
            return new Token(this.name, (TypeDescription.Generic) this.type.accept(visitor), this.annotations);
        }

        @CachedReturnPlugin.Enhance
        public int hashCode() {
            int hashCode;
            if (this.hashCode_P0R7Fqsw != 0) {
                hashCode = 0;
            } else {
                int result = this.name.hashCode();
                hashCode = (31 * ((31 * result) + this.type.hashCode())) + this.annotations.hashCode();
            }
            int i = hashCode;
            if (i == 0) {
                i = this.hashCode_P0R7Fqsw;
            } else {
                this.hashCode_P0R7Fqsw = i;
            }
            return i;
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (other == null || getClass() != other.getClass()) {
                return false;
            }
            Token token = (Token) other;
            return this.name.equals(token.name) && this.type.equals(token.type) && this.annotations.equals(token.annotations);
        }
    }
}
