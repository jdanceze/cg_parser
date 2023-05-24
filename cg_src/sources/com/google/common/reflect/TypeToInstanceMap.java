package com.google.common.reflect;

import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Map;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
@Beta
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/reflect/TypeToInstanceMap.class */
public interface TypeToInstanceMap<B> extends Map<TypeToken<? extends B>, B> {
    @NullableDecl
    <T extends B> T getInstance(Class<T> cls);

    @NullableDecl
    <T extends B> T getInstance(TypeToken<T> typeToken);

    @CanIgnoreReturnValue
    @NullableDecl
    <T extends B> T putInstance(Class<T> cls, @NullableDecl T t);

    @CanIgnoreReturnValue
    @NullableDecl
    <T extends B> T putInstance(TypeToken<T> typeToken, @NullableDecl T t);
}
