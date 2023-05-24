package net.bytebuddy.implementation.bind;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.ParameterDescription;
import net.bytebuddy.description.method.ParameterList;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.bind.MethodDelegationBinder;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/ArgumentTypeResolver.class */
public enum ArgumentTypeResolver implements MethodDelegationBinder.AmbiguityResolver {
    INSTANCE;

    private static MethodDelegationBinder.AmbiguityResolver.Resolution resolveRivalBinding(TypeDescription sourceParameterType, int leftParameterIndex, MethodDelegationBinder.MethodBinding left, int rightParameterIndex, MethodDelegationBinder.MethodBinding right) {
        TypeDescription leftParameterType = ((ParameterDescription) left.getTarget().getParameters().get(leftParameterIndex)).getType().asErasure();
        TypeDescription rightParameterType = ((ParameterDescription) right.getTarget().getParameters().get(rightParameterIndex)).getType().asErasure();
        if (!leftParameterType.equals(rightParameterType)) {
            if (leftParameterType.isPrimitive() && rightParameterType.isPrimitive()) {
                return PrimitiveTypePrecedence.forPrimitive(leftParameterType).resolve(PrimitiveTypePrecedence.forPrimitive(rightParameterType));
            }
            if (leftParameterType.isPrimitive()) {
                return sourceParameterType.isPrimitive() ? MethodDelegationBinder.AmbiguityResolver.Resolution.LEFT : MethodDelegationBinder.AmbiguityResolver.Resolution.RIGHT;
            } else if (rightParameterType.isPrimitive()) {
                return sourceParameterType.isPrimitive() ? MethodDelegationBinder.AmbiguityResolver.Resolution.RIGHT : MethodDelegationBinder.AmbiguityResolver.Resolution.LEFT;
            } else if (leftParameterType.isAssignableFrom(rightParameterType)) {
                return MethodDelegationBinder.AmbiguityResolver.Resolution.RIGHT;
            } else {
                if (rightParameterType.isAssignableFrom(leftParameterType)) {
                    return MethodDelegationBinder.AmbiguityResolver.Resolution.LEFT;
                }
                return MethodDelegationBinder.AmbiguityResolver.Resolution.AMBIGUOUS;
            }
        }
        return MethodDelegationBinder.AmbiguityResolver.Resolution.UNKNOWN;
    }

    private static MethodDelegationBinder.AmbiguityResolver.Resolution resolveByScore(int boundParameterScore) {
        if (boundParameterScore == 0) {
            return MethodDelegationBinder.AmbiguityResolver.Resolution.AMBIGUOUS;
        }
        if (boundParameterScore > 0) {
            return MethodDelegationBinder.AmbiguityResolver.Resolution.LEFT;
        }
        return MethodDelegationBinder.AmbiguityResolver.Resolution.RIGHT;
    }

    @Override // net.bytebuddy.implementation.bind.MethodDelegationBinder.AmbiguityResolver
    public MethodDelegationBinder.AmbiguityResolver.Resolution resolve(MethodDescription source, MethodDelegationBinder.MethodBinding left, MethodDelegationBinder.MethodBinding right) {
        MethodDelegationBinder.AmbiguityResolver.Resolution resolution = MethodDelegationBinder.AmbiguityResolver.Resolution.UNKNOWN;
        ParameterList<?> sourceParameters = source.getParameters();
        int leftExtra = 0;
        int rightExtra = 0;
        for (int sourceParameterIndex = 0; sourceParameterIndex < sourceParameters.size(); sourceParameterIndex++) {
            ParameterIndexToken parameterIndexToken = new ParameterIndexToken(sourceParameterIndex);
            Integer leftParameterIndex = left.getTargetParameterIndex(parameterIndexToken);
            Integer rightParameterIndex = right.getTargetParameterIndex(parameterIndexToken);
            if (leftParameterIndex != null && rightParameterIndex != null) {
                resolution = resolution.merge(resolveRivalBinding(((ParameterDescription) sourceParameters.get(sourceParameterIndex)).getType().asErasure(), leftParameterIndex.intValue(), left, rightParameterIndex.intValue(), right));
            } else if (leftParameterIndex != null) {
                leftExtra++;
            } else if (rightParameterIndex != null) {
                rightExtra++;
            }
        }
        return resolution == MethodDelegationBinder.AmbiguityResolver.Resolution.UNKNOWN ? resolveByScore(leftExtra - rightExtra) : resolution;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/ArgumentTypeResolver$PrimitiveTypePrecedence.class */
    public enum PrimitiveTypePrecedence {
        BOOLEAN(0),
        BYTE(1),
        SHORT(2),
        INTEGER(3),
        CHARACTER(4),
        LONG(5),
        FLOAT(6),
        DOUBLE(7);
        
        private final int score;

        PrimitiveTypePrecedence(int score) {
            this.score = score;
        }

        public static PrimitiveTypePrecedence forPrimitive(TypeDescription typeDescription) {
            if (typeDescription.represents(Boolean.TYPE)) {
                return BOOLEAN;
            }
            if (typeDescription.represents(Byte.TYPE)) {
                return BYTE;
            }
            if (typeDescription.represents(Short.TYPE)) {
                return SHORT;
            }
            if (typeDescription.represents(Integer.TYPE)) {
                return INTEGER;
            }
            if (typeDescription.represents(Character.TYPE)) {
                return CHARACTER;
            }
            if (typeDescription.represents(Long.TYPE)) {
                return LONG;
            }
            if (typeDescription.represents(Float.TYPE)) {
                return FLOAT;
            }
            if (typeDescription.represents(Double.TYPE)) {
                return DOUBLE;
            }
            throw new IllegalArgumentException("Not a non-void, primitive type " + typeDescription);
        }

        public MethodDelegationBinder.AmbiguityResolver.Resolution resolve(PrimitiveTypePrecedence right) {
            if (this.score - right.score == 0) {
                return MethodDelegationBinder.AmbiguityResolver.Resolution.UNKNOWN;
            }
            if (this.score - right.score > 0) {
                return MethodDelegationBinder.AmbiguityResolver.Resolution.RIGHT;
            }
            return MethodDelegationBinder.AmbiguityResolver.Resolution.LEFT;
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/ArgumentTypeResolver$ParameterIndexToken.class */
    public static class ParameterIndexToken {
        private final int parameterIndex;

        public ParameterIndexToken(int parameterIndex) {
            this.parameterIndex = parameterIndex;
        }

        public int hashCode() {
            return this.parameterIndex;
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (other == null || getClass() != other.getClass()) {
                return false;
            }
            ParameterIndexToken parameterIndexToken = (ParameterIndexToken) other;
            return this.parameterIndex == parameterIndexToken.parameterIndex;
        }
    }
}
