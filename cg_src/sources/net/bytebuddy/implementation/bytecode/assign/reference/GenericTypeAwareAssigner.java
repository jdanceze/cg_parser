package net.bytebuddy.implementation.bytecode.assign.reference;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.description.type.TypeList;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
import net.bytebuddy.implementation.bytecode.assign.TypeCasting;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/assign/reference/GenericTypeAwareAssigner.class */
public enum GenericTypeAwareAssigner implements Assigner {
    INSTANCE;

    @Override // net.bytebuddy.implementation.bytecode.assign.Assigner
    public StackManipulation assign(TypeDescription.Generic source, TypeDescription.Generic target, Assigner.Typing typing) {
        if (source.isPrimitive() || target.isPrimitive()) {
            return source.equals(target) ? StackManipulation.Trivial.INSTANCE : StackManipulation.Illegal.INSTANCE;
        } else if (((Boolean) source.accept(new IsAssignableToVisitor(target))).booleanValue()) {
            return StackManipulation.Trivial.INSTANCE;
        } else {
            if (typing.isDynamic()) {
                return source.asErasure().isAssignableTo(target.asErasure()) ? StackManipulation.Trivial.INSTANCE : TypeCasting.to(target);
            }
            return StackManipulation.Illegal.INSTANCE;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/assign/reference/GenericTypeAwareAssigner$IsAssignableToVisitor.class */
    public static class IsAssignableToVisitor implements TypeDescription.Generic.Visitor<Boolean> {
        private final TypeDescription.Generic typeDescription;
        private final boolean polymorphic;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.polymorphic == ((IsAssignableToVisitor) obj).polymorphic && this.typeDescription.equals(((IsAssignableToVisitor) obj).typeDescription);
        }

        public int hashCode() {
            return (((17 * 31) + this.typeDescription.hashCode()) * 31) + (this.polymorphic ? 1 : 0);
        }

        public IsAssignableToVisitor(TypeDescription.Generic typeDescription) {
            this(typeDescription, true);
        }

        protected IsAssignableToVisitor(TypeDescription.Generic typeDescription, boolean polymorphic) {
            this.typeDescription = typeDescription;
            this.polymorphic = polymorphic;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
        public Boolean onGenericArray(TypeDescription.Generic genericArray) {
            return (Boolean) this.typeDescription.accept(new OfGenericArray(genericArray, this.polymorphic));
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
        public Boolean onWildcard(TypeDescription.Generic wildcard) {
            return (Boolean) this.typeDescription.accept(new OfWildcard(wildcard));
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
        public Boolean onParameterizedType(TypeDescription.Generic parameterizedType) {
            return (Boolean) this.typeDescription.accept(new OfParameterizedType(parameterizedType, this.polymorphic));
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
        public Boolean onTypeVariable(TypeDescription.Generic typeVariable) {
            if (typeVariable.getTypeVariableSource().isInferrable()) {
                throw new UnsupportedOperationException("Assignability checks for type variables declared by methods are not currently supported");
            }
            if (typeVariable.equals(this.typeDescription)) {
                return true;
            }
            if (this.polymorphic) {
                Queue<TypeDescription.Generic> candidates = new LinkedList<>(typeVariable.getUpperBounds());
                while (!candidates.isEmpty()) {
                    TypeDescription.Generic candidate = candidates.remove();
                    if (((Boolean) candidate.accept(new IsAssignableToVisitor(this.typeDescription))).booleanValue()) {
                        return true;
                    }
                    if (candidate.getSort().isTypeVariable()) {
                        candidates.addAll(candidate.getUpperBounds());
                    }
                }
                return false;
            }
            return false;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
        public Boolean onNonGenericType(TypeDescription.Generic typeDescription) {
            return (Boolean) this.typeDescription.accept(new OfNonGenericType(typeDescription, this.polymorphic));
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/assign/reference/GenericTypeAwareAssigner$IsAssignableToVisitor$OfManifestType.class */
        public static abstract class OfManifestType implements TypeDescription.Generic.Visitor<Boolean> {
            protected final TypeDescription.Generic typeDescription;
            protected final boolean polymorphic;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.polymorphic == ((OfManifestType) obj).polymorphic && this.typeDescription.equals(((OfManifestType) obj).typeDescription);
            }

            public int hashCode() {
                return (((17 * 31) + this.typeDescription.hashCode()) * 31) + (this.polymorphic ? 1 : 0);
            }

            protected OfManifestType(TypeDescription.Generic typeDescription, boolean polymorphic) {
                this.typeDescription = typeDescription;
                this.polymorphic = polymorphic;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
            public Boolean onWildcard(TypeDescription.Generic wildcard) {
                for (TypeDescription.Generic upperBound : wildcard.getUpperBounds()) {
                    if (!((Boolean) this.typeDescription.accept(new IsAssignableToVisitor(upperBound))).booleanValue()) {
                        return false;
                    }
                }
                for (TypeDescription.Generic lowerBound : wildcard.getLowerBounds()) {
                    if (!((Boolean) lowerBound.accept(new IsAssignableToVisitor(this.typeDescription))).booleanValue()) {
                        return false;
                    }
                }
                return true;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
            public Boolean onTypeVariable(TypeDescription.Generic typeVariable) {
                if (typeVariable.getTypeVariableSource().isInferrable()) {
                    throw new UnsupportedOperationException("Assignability checks for type variables declared by methods arel not currently supported");
                }
                return false;
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/assign/reference/GenericTypeAwareAssigner$IsAssignableToVisitor$OfSimpleType.class */
        protected static abstract class OfSimpleType extends OfManifestType {
            protected OfSimpleType(TypeDescription.Generic typeDescription, boolean polymorphic) {
                super(typeDescription, polymorphic);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
            public Boolean onParameterizedType(TypeDescription.Generic parameterizedType) {
                Queue<TypeDescription.Generic> candidates = new LinkedList<>(Collections.singleton(this.typeDescription));
                Set<TypeDescription> previous = new HashSet<>(Collections.singleton(this.typeDescription.asErasure()));
                do {
                    TypeDescription.Generic candidate = candidates.remove();
                    if (candidate.asErasure().equals(parameterizedType.asErasure())) {
                        if (candidate.getSort().isNonGeneric()) {
                            return true;
                        }
                        TypeList.Generic source = candidate.getTypeArguments();
                        TypeList.Generic target = parameterizedType.getTypeArguments();
                        int size = target.size();
                        if (source.size() != size) {
                            return false;
                        }
                        for (int index = 0; index < size; index++) {
                            if (!((Boolean) ((TypeDescription.Generic) source.get(index)).accept(new IsAssignableToVisitor((TypeDescription.Generic) target.get(index), false))).booleanValue()) {
                                return false;
                            }
                        }
                        TypeDescription.Generic ownerType = parameterizedType.getOwnerType();
                        return Boolean.valueOf(ownerType == null || ((Boolean) ownerType.accept(new IsAssignableToVisitor(parameterizedType.getOwnerType()))).booleanValue());
                    } else if (this.polymorphic) {
                        TypeDescription.Generic superClass = candidate.getSuperClass();
                        if (superClass != null && previous.add(superClass.asErasure())) {
                            candidates.add(superClass);
                        }
                        for (TypeDescription.Generic anInterface : candidate.getInterfaces()) {
                            if (previous.add(anInterface.asErasure())) {
                                candidates.add(anInterface);
                            }
                        }
                    }
                } while (!candidates.isEmpty());
                return false;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
            public Boolean onNonGenericType(TypeDescription.Generic typeDescription) {
                boolean equals;
                if (this.polymorphic) {
                    equals = this.typeDescription.asErasure().isAssignableTo(typeDescription.asErasure());
                } else {
                    equals = this.typeDescription.asErasure().equals(typeDescription.asErasure());
                }
                return Boolean.valueOf(equals);
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/assign/reference/GenericTypeAwareAssigner$IsAssignableToVisitor$OfGenericArray.class */
        public static class OfGenericArray extends OfManifestType {
            protected OfGenericArray(TypeDescription.Generic typeDescription, boolean polymorphic) {
                super(typeDescription, polymorphic);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
            public Boolean onGenericArray(TypeDescription.Generic genericArray) {
                TypeDescription.Generic target;
                TypeDescription.Generic source = this.typeDescription.getComponentType();
                TypeDescription.Generic componentType = genericArray.getComponentType();
                while (true) {
                    target = componentType;
                    if (!source.getSort().isGenericArray() || !target.getSort().isGenericArray()) {
                        break;
                    }
                    source = source.getComponentType();
                    componentType = target.getComponentType();
                }
                return Boolean.valueOf((source.getSort().isGenericArray() || target.getSort().isGenericArray() || !((Boolean) source.accept(new IsAssignableToVisitor(target))).booleanValue()) ? false : true);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
            public Boolean onParameterizedType(TypeDescription.Generic parameterizedType) {
                return false;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
            public Boolean onNonGenericType(TypeDescription.Generic typeDescription) {
                boolean equals;
                if (this.polymorphic) {
                    equals = this.typeDescription.asErasure().isAssignableTo(typeDescription.asErasure());
                } else {
                    equals = this.typeDescription.asErasure().equals(typeDescription.asErasure());
                }
                return Boolean.valueOf(equals);
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/assign/reference/GenericTypeAwareAssigner$IsAssignableToVisitor$OfWildcard.class */
        public static class OfWildcard implements TypeDescription.Generic.Visitor<Boolean> {
            private final TypeDescription.Generic wildcard;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.wildcard.equals(((OfWildcard) obj).wildcard);
            }

            public int hashCode() {
                return (17 * 31) + this.wildcard.hashCode();
            }

            protected OfWildcard(TypeDescription.Generic wildcard) {
                this.wildcard = wildcard;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
            public Boolean onGenericArray(TypeDescription.Generic genericArray) {
                return false;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
            public Boolean onWildcard(TypeDescription.Generic wildcard) {
                boolean hasUpperBounds = false;
                boolean hasLowerBounds = false;
                for (TypeDescription.Generic target : wildcard.getUpperBounds()) {
                    for (TypeDescription.Generic source : this.wildcard.getUpperBounds()) {
                        if (!((Boolean) source.accept(new IsAssignableToVisitor(target))).booleanValue()) {
                            return false;
                        }
                    }
                    hasUpperBounds = hasUpperBounds || !target.represents(Object.class);
                }
                for (TypeDescription.Generic target2 : wildcard.getLowerBounds()) {
                    for (TypeDescription.Generic source2 : this.wildcard.getLowerBounds()) {
                        if (!((Boolean) target2.accept(new IsAssignableToVisitor(source2))).booleanValue()) {
                            return false;
                        }
                    }
                    hasLowerBounds = true;
                }
                if (hasUpperBounds) {
                    return Boolean.valueOf(this.wildcard.getLowerBounds().isEmpty());
                }
                if (hasLowerBounds) {
                    TypeList.Generic upperBounds = this.wildcard.getUpperBounds();
                    return Boolean.valueOf(upperBounds.size() == 0 || (upperBounds.size() == 1 && upperBounds.getOnly().represents(Object.class)));
                }
                return true;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
            public Boolean onParameterizedType(TypeDescription.Generic parameterizedType) {
                return false;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
            public Boolean onTypeVariable(TypeDescription.Generic typeVariable) {
                return false;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
            public Boolean onNonGenericType(TypeDescription.Generic typeDescription) {
                return false;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/assign/reference/GenericTypeAwareAssigner$IsAssignableToVisitor$OfParameterizedType.class */
        public static class OfParameterizedType extends OfSimpleType {
            protected OfParameterizedType(TypeDescription.Generic typeDescription, boolean polymorphic) {
                super(typeDescription, polymorphic);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
            public Boolean onGenericArray(TypeDescription.Generic genericArray) {
                return false;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/assign/reference/GenericTypeAwareAssigner$IsAssignableToVisitor$OfNonGenericType.class */
        public static class OfNonGenericType extends OfSimpleType {
            protected OfNonGenericType(TypeDescription.Generic typeDescription, boolean polymorphic) {
                super(typeDescription, polymorphic);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
            public Boolean onGenericArray(TypeDescription.Generic genericArray) {
                boolean equals;
                if (this.polymorphic) {
                    equals = this.typeDescription.asErasure().isAssignableTo(genericArray.asErasure());
                } else {
                    equals = this.typeDescription.asErasure().equals(genericArray.asErasure());
                }
                return Boolean.valueOf(equals);
            }
        }
    }
}
