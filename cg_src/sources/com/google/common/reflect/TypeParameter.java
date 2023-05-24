package com.google.common.reflect;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
@Beta
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/reflect/TypeParameter.class */
public abstract class TypeParameter<T> extends TypeCapture<T> {
    final TypeVariable<?> typeVariable;

    protected TypeParameter() {
        Type type = capture();
        Preconditions.checkArgument(type instanceof TypeVariable, "%s should be a type variable.", type);
        this.typeVariable = (TypeVariable) type;
    }

    public final int hashCode() {
        return this.typeVariable.hashCode();
    }

    public final boolean equals(@NullableDecl Object o) {
        if (o instanceof TypeParameter) {
            TypeParameter<?> that = (TypeParameter) o;
            return this.typeVariable.equals(that.typeVariable);
        }
        return false;
    }

    public String toString() {
        return this.typeVariable.toString();
    }
}
