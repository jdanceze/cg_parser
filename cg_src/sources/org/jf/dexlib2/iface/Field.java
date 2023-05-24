package org.jf.dexlib2.iface;

import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.HiddenApiRestriction;
import org.jf.dexlib2.iface.reference.FieldReference;
import org.jf.dexlib2.iface.value.EncodedValue;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/iface/Field.class */
public interface Field extends FieldReference, Member {
    @Override // org.jf.dexlib2.iface.Member
    @Nonnull
    String getDefiningClass();

    @Override // org.jf.dexlib2.iface.Member
    @Nonnull
    String getName();

    @Override // 
    @Nonnull
    String getType();

    int getAccessFlags();

    @Nullable
    EncodedValue getInitialValue();

    @Nonnull
    Set<? extends Annotation> getAnnotations();

    @Nonnull
    Set<HiddenApiRestriction> getHiddenApiRestrictions();
}
