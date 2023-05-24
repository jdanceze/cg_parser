package org.jf.dexlib2.iface;

import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.debug.LocalInfo;
import org.jf.dexlib2.iface.reference.TypeReference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/iface/MethodParameter.class */
public interface MethodParameter extends TypeReference, LocalInfo {
    @Override // org.jf.dexlib2.iface.reference.TypeReference, org.jf.dexlib2.iface.ClassDef
    @Nonnull
    String getType();

    @Nonnull
    Set<? extends Annotation> getAnnotations();

    @Nullable
    String getName();

    @Nullable
    String getSignature();
}
