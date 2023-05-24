package net.bytebuddy.dynamic.scaffold.inline;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.annotation.AnnotationList;
import net.bytebuddy.description.annotation.AnnotationValue;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.ParameterDescription;
import net.bytebuddy.description.method.ParameterList;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.description.type.TypeList;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodAccessorFactory;
import net.bytebuddy.implementation.auxiliary.AuxiliaryType;
import net.bytebuddy.implementation.auxiliary.TrivialType;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.CompoundList;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/inline/MethodRebaseResolver.class */
public interface MethodRebaseResolver {
    Resolution resolve(MethodDescription.InDefinedShape inDefinedShape);

    List<DynamicType> getAuxiliaryTypes();

    Map<MethodDescription.SignatureToken, Resolution> asTokenMap();

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/inline/MethodRebaseResolver$Disabled.class */
    public enum Disabled implements MethodRebaseResolver {
        INSTANCE;

        @Override // net.bytebuddy.dynamic.scaffold.inline.MethodRebaseResolver
        public Resolution resolve(MethodDescription.InDefinedShape methodDescription) {
            return new Resolution.Preserved(methodDescription);
        }

        @Override // net.bytebuddy.dynamic.scaffold.inline.MethodRebaseResolver
        public List<DynamicType> getAuxiliaryTypes() {
            return Collections.emptyList();
        }

        @Override // net.bytebuddy.dynamic.scaffold.inline.MethodRebaseResolver
        public Map<MethodDescription.SignatureToken, Resolution> asTokenMap() {
            return Collections.emptyMap();
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/inline/MethodRebaseResolver$Resolution.class */
    public interface Resolution {
        boolean isRebased();

        MethodDescription.InDefinedShape getResolvedMethod();

        TypeList getPrependedParameters();

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/inline/MethodRebaseResolver$Resolution$Preserved.class */
        public static class Preserved implements Resolution {
            private final MethodDescription.InDefinedShape methodDescription;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.methodDescription.equals(((Preserved) obj).methodDescription);
            }

            public int hashCode() {
                return (17 * 31) + this.methodDescription.hashCode();
            }

            public Preserved(MethodDescription.InDefinedShape methodDescription) {
                this.methodDescription = methodDescription;
            }

            @Override // net.bytebuddy.dynamic.scaffold.inline.MethodRebaseResolver.Resolution
            public boolean isRebased() {
                return false;
            }

            @Override // net.bytebuddy.dynamic.scaffold.inline.MethodRebaseResolver.Resolution
            public MethodDescription.InDefinedShape getResolvedMethod() {
                return this.methodDescription;
            }

            @Override // net.bytebuddy.dynamic.scaffold.inline.MethodRebaseResolver.Resolution
            public TypeList getPrependedParameters() {
                throw new IllegalStateException("Cannot process additional parameters for non-rebased method: " + this.methodDescription);
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/inline/MethodRebaseResolver$Resolution$ForRebasedMethod.class */
        public static class ForRebasedMethod implements Resolution {
            private final MethodDescription.InDefinedShape methodDescription;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.methodDescription.equals(((ForRebasedMethod) obj).methodDescription);
            }

            public int hashCode() {
                return (17 * 31) + this.methodDescription.hashCode();
            }

            protected ForRebasedMethod(MethodDescription.InDefinedShape methodDescription) {
                this.methodDescription = methodDescription;
            }

            public static Resolution of(TypeDescription instrumentedType, MethodDescription.InDefinedShape methodDescription, MethodNameTransformer methodNameTransformer) {
                return new ForRebasedMethod(new RebasedMethod(instrumentedType, methodDescription, methodNameTransformer));
            }

            @Override // net.bytebuddy.dynamic.scaffold.inline.MethodRebaseResolver.Resolution
            public boolean isRebased() {
                return true;
            }

            @Override // net.bytebuddy.dynamic.scaffold.inline.MethodRebaseResolver.Resolution
            public MethodDescription.InDefinedShape getResolvedMethod() {
                return this.methodDescription;
            }

            @Override // net.bytebuddy.dynamic.scaffold.inline.MethodRebaseResolver.Resolution
            public TypeList getPrependedParameters() {
                return new TypeList.Empty();
            }

            /* JADX INFO: Access modifiers changed from: protected */
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/inline/MethodRebaseResolver$Resolution$ForRebasedMethod$RebasedMethod.class */
            public static class RebasedMethod extends MethodDescription.InDefinedShape.AbstractBase {
                private final TypeDescription instrumentedType;
                private final MethodDescription.InDefinedShape methodDescription;
                private final MethodNameTransformer methodNameTransformer;

                protected RebasedMethod(TypeDescription instrumentedType, MethodDescription.InDefinedShape methodDescription, MethodNameTransformer methodNameTransformer) {
                    this.instrumentedType = instrumentedType;
                    this.methodDescription = methodDescription;
                    this.methodNameTransformer = methodNameTransformer;
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
                    return this.methodDescription.getDeclaringType();
                }

                @Override // net.bytebuddy.description.ModifierReviewable
                public int getModifiers() {
                    return 4096 | (this.methodDescription.isStatic() ? 8 : 0) | (this.methodDescription.isNative() ? 272 : 0) | ((!this.instrumentedType.isInterface() || this.methodDescription.isNative()) ? 2 : 1);
                }

                @Override // net.bytebuddy.description.NamedElement.WithRuntimeName
                public String getInternalName() {
                    return this.methodNameTransformer.transform(this.methodDescription);
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/inline/MethodRebaseResolver$Resolution$ForRebasedConstructor.class */
        public static class ForRebasedConstructor implements Resolution {
            private final MethodDescription.InDefinedShape methodDescription;
            private final TypeDescription placeholderType;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.methodDescription.equals(((ForRebasedConstructor) obj).methodDescription) && this.placeholderType.equals(((ForRebasedConstructor) obj).placeholderType);
            }

            public int hashCode() {
                return (((17 * 31) + this.methodDescription.hashCode()) * 31) + this.placeholderType.hashCode();
            }

            protected ForRebasedConstructor(MethodDescription.InDefinedShape methodDescription, TypeDescription placeholderType) {
                this.methodDescription = methodDescription;
                this.placeholderType = placeholderType;
            }

            public static Resolution of(MethodDescription.InDefinedShape methodDescription, TypeDescription placeholderType) {
                return new ForRebasedConstructor(new RebasedConstructor(methodDescription, placeholderType), placeholderType);
            }

            @Override // net.bytebuddy.dynamic.scaffold.inline.MethodRebaseResolver.Resolution
            public boolean isRebased() {
                return true;
            }

            @Override // net.bytebuddy.dynamic.scaffold.inline.MethodRebaseResolver.Resolution
            public MethodDescription.InDefinedShape getResolvedMethod() {
                return this.methodDescription;
            }

            @Override // net.bytebuddy.dynamic.scaffold.inline.MethodRebaseResolver.Resolution
            public TypeList getPrependedParameters() {
                return new TypeList.Explicit(this.placeholderType);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/inline/MethodRebaseResolver$Resolution$ForRebasedConstructor$RebasedConstructor.class */
            public static class RebasedConstructor extends MethodDescription.InDefinedShape.AbstractBase {
                private final MethodDescription.InDefinedShape methodDescription;
                private final TypeDescription placeholderType;

                protected RebasedConstructor(MethodDescription.InDefinedShape methodDescription, TypeDescription placeholderType) {
                    this.methodDescription = methodDescription;
                    this.placeholderType = placeholderType;
                }

                @Override // net.bytebuddy.description.method.MethodDescription
                public TypeDescription.Generic getReturnType() {
                    return TypeDescription.Generic.VOID;
                }

                @Override // net.bytebuddy.description.method.MethodDescription, net.bytebuddy.description.method.MethodDescription.InDefinedShape
                public ParameterList<ParameterDescription.InDefinedShape> getParameters() {
                    return new ParameterList.Explicit.ForTypes(this, CompoundList.of(this.methodDescription.getParameters().asTypeList().asErasures(), this.placeholderType));
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
                    return this.methodDescription.getDeclaringType();
                }

                @Override // net.bytebuddy.description.ModifierReviewable
                public int getModifiers() {
                    return 4098;
                }

                @Override // net.bytebuddy.description.NamedElement.WithRuntimeName
                public String getInternalName() {
                    return "<init>";
                }
            }
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/inline/MethodRebaseResolver$Default.class */
    public static class Default implements MethodRebaseResolver {
        private final Map<MethodDescription.InDefinedShape, Resolution> resolutions;
        private final List<DynamicType> dynamicTypes;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.resolutions.equals(((Default) obj).resolutions) && this.dynamicTypes.equals(((Default) obj).dynamicTypes);
        }

        public int hashCode() {
            return (((17 * 31) + this.resolutions.hashCode()) * 31) + this.dynamicTypes.hashCode();
        }

        protected Default(Map<MethodDescription.InDefinedShape, Resolution> resolutions, List<DynamicType> dynamicTypes) {
            this.resolutions = resolutions;
            this.dynamicTypes = dynamicTypes;
        }

        public static MethodRebaseResolver make(TypeDescription instrumentedType, Set<? extends MethodDescription.Token> rebaseableMethodTokens, ClassFileVersion classFileVersion, AuxiliaryType.NamingStrategy auxiliaryTypeNamingStrategy, MethodNameTransformer methodNameTransformer) {
            Resolution of;
            DynamicType placeholderType = null;
            Map<MethodDescription.InDefinedShape, Resolution> resolutions = new HashMap<>();
            for (MethodDescription.InDefinedShape instrumentedMethod : instrumentedType.getDeclaredMethods()) {
                if (rebaseableMethodTokens.contains(instrumentedMethod.asToken(ElementMatchers.is(instrumentedType)))) {
                    if (instrumentedMethod.isConstructor()) {
                        if (placeholderType == null) {
                            placeholderType = TrivialType.SIGNATURE_RELEVANT.make(auxiliaryTypeNamingStrategy.name(instrumentedType), classFileVersion, MethodAccessorFactory.Illegal.INSTANCE);
                        }
                        of = Resolution.ForRebasedConstructor.of(instrumentedMethod, placeholderType.getTypeDescription());
                    } else {
                        of = Resolution.ForRebasedMethod.of(instrumentedType, instrumentedMethod, methodNameTransformer);
                    }
                    Resolution resolution = of;
                    resolutions.put(instrumentedMethod, resolution);
                }
            }
            if (placeholderType == null) {
                return new Default(resolutions, Collections.emptyList());
            }
            return new Default(resolutions, Collections.singletonList(placeholderType));
        }

        @Override // net.bytebuddy.dynamic.scaffold.inline.MethodRebaseResolver
        public Resolution resolve(MethodDescription.InDefinedShape methodDescription) {
            Resolution resolution = this.resolutions.get(methodDescription);
            return resolution == null ? new Resolution.Preserved(methodDescription) : resolution;
        }

        @Override // net.bytebuddy.dynamic.scaffold.inline.MethodRebaseResolver
        public List<DynamicType> getAuxiliaryTypes() {
            return this.dynamicTypes;
        }

        @Override // net.bytebuddy.dynamic.scaffold.inline.MethodRebaseResolver
        public Map<MethodDescription.SignatureToken, Resolution> asTokenMap() {
            Map<MethodDescription.SignatureToken, Resolution> tokenMap = new HashMap<>();
            for (Map.Entry<MethodDescription.InDefinedShape, Resolution> entry : this.resolutions.entrySet()) {
                tokenMap.put(entry.getKey().asSignatureToken(), entry.getValue());
            }
            return tokenMap;
        }
    }
}
