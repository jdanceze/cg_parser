package net.bytebuddy.dynamic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.annotation.AnnotationList;
import net.bytebuddy.description.annotation.AnnotationValue;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.ParameterDescription;
import net.bytebuddy.description.method.ParameterList;
import net.bytebuddy.description.modifier.ModifierContributor;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.description.type.TypeList;
import net.bytebuddy.matcher.ElementMatchers;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/Transformer.class */
public interface Transformer<T> {
    T transform(TypeDescription typeDescription, T t);

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/Transformer$NoOp.class */
    public enum NoOp implements Transformer<Object> {
        INSTANCE;

        public static <T> Transformer<T> make() {
            return INSTANCE;
        }

        @Override // net.bytebuddy.dynamic.Transformer
        public Object transform(TypeDescription instrumentedType, Object target) {
            return target;
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/Transformer$ForField.class */
    public static class ForField implements Transformer<FieldDescription> {
        private final Transformer<FieldDescription.Token> transformer;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.transformer.equals(((ForField) obj).transformer);
        }

        public int hashCode() {
            return (17 * 31) + this.transformer.hashCode();
        }

        public ForField(Transformer<FieldDescription.Token> transformer) {
            this.transformer = transformer;
        }

        public static Transformer<FieldDescription> withModifiers(ModifierContributor.ForField... modifierContributor) {
            return withModifiers(Arrays.asList(modifierContributor));
        }

        public static Transformer<FieldDescription> withModifiers(List<? extends ModifierContributor.ForField> modifierContributors) {
            return new ForField(new FieldModifierTransformer(ModifierContributor.Resolver.of(modifierContributors)));
        }

        @Override // net.bytebuddy.dynamic.Transformer
        public FieldDescription transform(TypeDescription instrumentedType, FieldDescription fieldDescription) {
            return new TransformedField(instrumentedType, fieldDescription.getDeclaringType(), this.transformer.transform(instrumentedType, fieldDescription.asToken(ElementMatchers.none())), fieldDescription.asDefined());
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/Transformer$ForField$FieldModifierTransformer.class */
        public static class FieldModifierTransformer implements Transformer<FieldDescription.Token> {
            private final ModifierContributor.Resolver<ModifierContributor.ForField> resolver;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.resolver.equals(((FieldModifierTransformer) obj).resolver);
            }

            public int hashCode() {
                return (17 * 31) + this.resolver.hashCode();
            }

            protected FieldModifierTransformer(ModifierContributor.Resolver<ModifierContributor.ForField> resolver) {
                this.resolver = resolver;
            }

            @Override // net.bytebuddy.dynamic.Transformer
            public FieldDescription.Token transform(TypeDescription instrumentedType, FieldDescription.Token target) {
                return new FieldDescription.Token(target.getName(), this.resolver.resolve(target.getModifiers()), target.getType(), target.getAnnotations());
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/Transformer$ForField$TransformedField.class */
        public static class TransformedField extends FieldDescription.AbstractBase {
            private final TypeDescription instrumentedType;
            private final TypeDefinition declaringType;
            private final FieldDescription.Token token;
            private final FieldDescription.InDefinedShape fieldDescription;

            protected TransformedField(TypeDescription instrumentedType, TypeDefinition declaringType, FieldDescription.Token token, FieldDescription.InDefinedShape fieldDescription) {
                this.instrumentedType = instrumentedType;
                this.declaringType = declaringType;
                this.token = token;
                this.fieldDescription = fieldDescription;
            }

            @Override // net.bytebuddy.description.field.FieldDescription
            public TypeDescription.Generic getType() {
                return (TypeDescription.Generic) this.token.getType().accept(TypeDescription.Generic.Visitor.Substitutor.ForAttachment.of(this.instrumentedType));
            }

            @Override // net.bytebuddy.description.annotation.AnnotationSource
            public AnnotationList getDeclaredAnnotations() {
                return this.token.getAnnotations();
            }

            @Override // net.bytebuddy.description.DeclaredByType, net.bytebuddy.description.field.FieldDescription.InDefinedShape
            public TypeDefinition getDeclaringType() {
                return this.declaringType;
            }

            @Override // net.bytebuddy.description.ModifierReviewable
            public int getModifiers() {
                return this.token.getModifiers();
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // net.bytebuddy.description.ByteCodeElement.TypeDependant
            public FieldDescription.InDefinedShape asDefined() {
                return this.fieldDescription;
            }

            @Override // net.bytebuddy.description.NamedElement.WithRuntimeName
            public String getName() {
                return this.token.getName();
            }
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/Transformer$ForMethod.class */
    public static class ForMethod implements Transformer<MethodDescription> {
        private final Transformer<MethodDescription.Token> transformer;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.transformer.equals(((ForMethod) obj).transformer);
        }

        public int hashCode() {
            return (17 * 31) + this.transformer.hashCode();
        }

        public ForMethod(Transformer<MethodDescription.Token> transformer) {
            this.transformer = transformer;
        }

        public static Transformer<MethodDescription> withModifiers(ModifierContributor.ForMethod... modifierContributor) {
            return withModifiers(Arrays.asList(modifierContributor));
        }

        public static Transformer<MethodDescription> withModifiers(List<? extends ModifierContributor.ForMethod> modifierContributors) {
            return new ForMethod(new MethodModifierTransformer(ModifierContributor.Resolver.of(modifierContributors)));
        }

        @Override // net.bytebuddy.dynamic.Transformer
        public MethodDescription transform(TypeDescription instrumentedType, MethodDescription methodDescription) {
            return new TransformedMethod(instrumentedType, methodDescription.getDeclaringType(), this.transformer.transform(instrumentedType, methodDescription.asToken(ElementMatchers.none())), methodDescription.asDefined());
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/Transformer$ForMethod$MethodModifierTransformer.class */
        public static class MethodModifierTransformer implements Transformer<MethodDescription.Token> {
            private final ModifierContributor.Resolver<ModifierContributor.ForMethod> resolver;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.resolver.equals(((MethodModifierTransformer) obj).resolver);
            }

            public int hashCode() {
                return (17 * 31) + this.resolver.hashCode();
            }

            protected MethodModifierTransformer(ModifierContributor.Resolver<ModifierContributor.ForMethod> resolver) {
                this.resolver = resolver;
            }

            @Override // net.bytebuddy.dynamic.Transformer
            public MethodDescription.Token transform(TypeDescription instrumentedType, MethodDescription.Token target) {
                return new MethodDescription.Token(target.getName(), this.resolver.resolve(target.getModifiers()), target.getTypeVariableTokens(), target.getReturnType(), target.getParameterTokens(), target.getExceptionTypes(), target.getAnnotations(), target.getDefaultValue(), target.getReceiverType());
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/Transformer$ForMethod$TransformedMethod.class */
        public static class TransformedMethod extends MethodDescription.AbstractBase {
            private final TypeDescription instrumentedType;
            private final TypeDefinition declaringType;
            private final MethodDescription.Token token;
            private final MethodDescription.InDefinedShape methodDescription;

            protected TransformedMethod(TypeDescription instrumentedType, TypeDefinition declaringType, MethodDescription.Token token, MethodDescription.InDefinedShape methodDescription) {
                this.instrumentedType = instrumentedType;
                this.declaringType = declaringType;
                this.token = token;
                this.methodDescription = methodDescription;
            }

            @Override // net.bytebuddy.description.TypeVariableSource
            public TypeList.Generic getTypeVariables() {
                return new TypeList.Generic.ForDetachedTypes.OfTypeVariables(this, this.token.getTypeVariableTokens(), new AttachmentVisitor());
            }

            @Override // net.bytebuddy.description.method.MethodDescription
            public TypeDescription.Generic getReturnType() {
                return (TypeDescription.Generic) this.token.getReturnType().accept(new AttachmentVisitor());
            }

            @Override // net.bytebuddy.description.method.MethodDescription, net.bytebuddy.description.method.MethodDescription.InDefinedShape
            public ParameterList<?> getParameters() {
                return new TransformedParameterList();
            }

            @Override // net.bytebuddy.description.method.MethodDescription
            public TypeList.Generic getExceptionTypes() {
                return new TypeList.Generic.ForDetachedTypes(this.token.getExceptionTypes(), new AttachmentVisitor());
            }

            @Override // net.bytebuddy.description.annotation.AnnotationSource
            public AnnotationList getDeclaredAnnotations() {
                return this.token.getAnnotations();
            }

            @Override // net.bytebuddy.description.NamedElement.WithRuntimeName
            public String getInternalName() {
                return this.token.getName();
            }

            @Override // net.bytebuddy.description.DeclaredByType, net.bytebuddy.description.field.FieldDescription.InDefinedShape
            public TypeDefinition getDeclaringType() {
                return this.declaringType;
            }

            @Override // net.bytebuddy.description.ModifierReviewable
            public int getModifiers() {
                return this.token.getModifiers();
            }

            @Override // net.bytebuddy.description.method.MethodDescription
            public AnnotationValue<?, ?> getDefaultValue() {
                return this.token.getDefaultValue();
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // net.bytebuddy.description.ByteCodeElement.TypeDependant
            public MethodDescription.InDefinedShape asDefined() {
                return this.methodDescription;
            }

            @Override // net.bytebuddy.description.method.MethodDescription
            public TypeDescription.Generic getReceiverType() {
                TypeDescription.Generic receiverType = this.token.getReceiverType();
                return receiverType == null ? TypeDescription.Generic.UNDEFINED : (TypeDescription.Generic) receiverType.accept(new AttachmentVisitor());
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/Transformer$ForMethod$TransformedMethod$TransformedParameterList.class */
            protected class TransformedParameterList extends ParameterList.AbstractBase<ParameterDescription> {
                protected TransformedParameterList() {
                }

                @Override // java.util.AbstractList, java.util.List
                public ParameterDescription get(int index) {
                    return new TransformedParameter(index, TransformedMethod.this.token.getParameterTokens().get(index));
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
                public int size() {
                    return TransformedMethod.this.token.getParameterTokens().size();
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/Transformer$ForMethod$TransformedMethod$TransformedParameter.class */
            public class TransformedParameter extends ParameterDescription.AbstractBase {
                private final int index;
                private final ParameterDescription.Token parameterToken;

                protected TransformedParameter(int index, ParameterDescription.Token parameterToken) {
                    this.index = index;
                    this.parameterToken = parameterToken;
                }

                @Override // net.bytebuddy.description.method.ParameterDescription
                public TypeDescription.Generic getType() {
                    return (TypeDescription.Generic) this.parameterToken.getType().accept(new AttachmentVisitor());
                }

                @Override // net.bytebuddy.description.method.ParameterDescription, net.bytebuddy.description.method.ParameterDescription.InDefinedShape
                public MethodDescription getDeclaringMethod() {
                    return TransformedMethod.this;
                }

                @Override // net.bytebuddy.description.method.ParameterDescription
                public int getIndex() {
                    return this.index;
                }

                @Override // net.bytebuddy.description.NamedElement.WithOptionalName
                public boolean isNamed() {
                    return this.parameterToken.getName() != null;
                }

                @Override // net.bytebuddy.description.method.ParameterDescription
                public boolean hasModifiers() {
                    return this.parameterToken.getModifiers() != null;
                }

                @Override // net.bytebuddy.description.method.ParameterDescription.AbstractBase, net.bytebuddy.description.NamedElement.WithRuntimeName
                public String getName() {
                    if (isNamed()) {
                        return this.parameterToken.getName();
                    }
                    return super.getName();
                }

                @Override // net.bytebuddy.description.method.ParameterDescription.AbstractBase, net.bytebuddy.description.ModifierReviewable
                public int getModifiers() {
                    if (hasModifiers()) {
                        return this.parameterToken.getModifiers().intValue();
                    }
                    return super.getModifiers();
                }

                @Override // net.bytebuddy.description.annotation.AnnotationSource
                public AnnotationList getDeclaredAnnotations() {
                    return this.parameterToken.getAnnotations();
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.ByteCodeElement.TypeDependant
                public ParameterDescription.InDefinedShape asDefined() {
                    return (ParameterDescription.InDefinedShape) TransformedMethod.this.methodDescription.getParameters().get(this.index);
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/Transformer$ForMethod$TransformedMethod$AttachmentVisitor.class */
            public class AttachmentVisitor extends TypeDescription.Generic.Visitor.Substitutor.WithoutTypeSubstitution {
                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && TransformedMethod.this.equals(TransformedMethod.this);
                }

                public int hashCode() {
                    return (17 * 31) + TransformedMethod.this.hashCode();
                }

                protected AttachmentVisitor() {
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public TypeDescription.Generic onTypeVariable(TypeDescription.Generic typeVariable) {
                    TypeDescription.Generic only;
                    TypeList.Generic candidates = TransformedMethod.this.getTypeVariables().filter(ElementMatchers.named(typeVariable.getSymbol()));
                    if (candidates.isEmpty()) {
                        only = TransformedMethod.this.instrumentedType.findVariable(typeVariable.getSymbol());
                    } else {
                        only = candidates.getOnly();
                    }
                    TypeDescription.Generic attached = only;
                    if (attached == null) {
                        throw new IllegalArgumentException("Cannot attach undefined variable: " + typeVariable);
                    }
                    return new TypeDescription.Generic.OfTypeVariable.WithAnnotationOverlay(attached, typeVariable);
                }
            }
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/Transformer$Compound.class */
    public static class Compound<S> implements Transformer<S> {
        private final List<Transformer<S>> transformers;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.transformers.equals(((Compound) obj).transformers);
        }

        public int hashCode() {
            return (17 * 31) + this.transformers.hashCode();
        }

        public Compound(Transformer<S>... transformer) {
            this(Arrays.asList(transformer));
        }

        public Compound(List<? extends Transformer<S>> transformers) {
            this.transformers = new ArrayList();
            for (Transformer<S> transformer : transformers) {
                if (transformer instanceof Compound) {
                    this.transformers.addAll(((Compound) transformer).transformers);
                } else if (!(transformer instanceof NoOp)) {
                    this.transformers.add(transformer);
                }
            }
        }

        @Override // net.bytebuddy.dynamic.Transformer
        public S transform(TypeDescription instrumentedType, S target) {
            for (Transformer<S> transformer : this.transformers) {
                target = transformer.transform(instrumentedType, target);
            }
            return target;
        }
    }
}
