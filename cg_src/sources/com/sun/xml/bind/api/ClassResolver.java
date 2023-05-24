package com.sun.xml.bind.api;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/api/ClassResolver.class */
public abstract class ClassResolver {
    @Nullable
    public abstract Class<?> resolveElementName(@NotNull String str, @NotNull String str2) throws Exception;
}
