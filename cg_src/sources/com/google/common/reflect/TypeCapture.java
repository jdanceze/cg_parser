package com.google.common.reflect;

import com.google.common.base.Preconditions;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/reflect/TypeCapture.class */
abstract class TypeCapture<T> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public final Type capture() {
        Type superclass = getClass().getGenericSuperclass();
        Preconditions.checkArgument(superclass instanceof ParameterizedType, "%s isn't parameterized", superclass);
        return ((ParameterizedType) superclass).getActualTypeArguments()[0];
    }
}
