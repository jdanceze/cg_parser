package org.jf.dexlib2.iface;

import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.HiddenApiRestriction;
import org.jf.dexlib2.iface.reference.MethodReference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/iface/Method.class */
public interface Method extends MethodReference, Member {
    @Override // org.jf.dexlib2.iface.Member
    @Nonnull
    String getDefiningClass();

    @Override // org.jf.dexlib2.iface.Member
    @Nonnull
    String getName();

    @Nonnull
    List<? extends MethodParameter> getParameters();

    @Override // 
    @Nonnull
    String getReturnType();

    int getAccessFlags();

    @Nonnull
    Set<? extends Annotation> getAnnotations();

    @Nonnull
    Set<HiddenApiRestriction> getHiddenApiRestrictions();

    @Nullable
    MethodImplementation getImplementation();
}
