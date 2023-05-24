package org.jf.dexlib2.analysis;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.iface.reference.FieldReference;
import org.jf.dexlib2.iface.reference.MethodReference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/analysis/TypeProto.class */
public interface TypeProto {
    @Nonnull
    ClassPath getClassPath();

    @Nonnull
    String getType();

    boolean isInterface();

    boolean implementsInterface(@Nonnull String str);

    @Nullable
    String getSuperclass();

    @Nonnull
    TypeProto getCommonSuperclass(@Nonnull TypeProto typeProto);

    @Nullable
    FieldReference getFieldByOffset(int i);

    @Nullable
    Method getMethodByVtableIndex(int i);

    int findMethodIndexInVtable(@Nonnull MethodReference methodReference);
}
