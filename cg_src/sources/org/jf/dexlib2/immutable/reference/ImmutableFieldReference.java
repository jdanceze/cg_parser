package org.jf.dexlib2.immutable.reference;

import javax.annotation.Nonnull;
import org.jf.dexlib2.base.reference.BaseFieldReference;
import org.jf.dexlib2.iface.reference.FieldReference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/immutable/reference/ImmutableFieldReference.class */
public class ImmutableFieldReference extends BaseFieldReference implements ImmutableReference {
    @Nonnull
    protected final String definingClass;
    @Nonnull
    protected final String name;
    @Nonnull
    protected final String type;

    public ImmutableFieldReference(@Nonnull String definingClass, @Nonnull String name, @Nonnull String type) {
        this.definingClass = definingClass;
        this.name = name;
        this.type = type;
    }

    @Nonnull
    public static ImmutableFieldReference of(@Nonnull FieldReference fieldReference) {
        if (fieldReference instanceof ImmutableFieldReference) {
            return (ImmutableFieldReference) fieldReference;
        }
        return new ImmutableFieldReference(fieldReference.getDefiningClass(), fieldReference.getName(), fieldReference.getType());
    }

    @Override // org.jf.dexlib2.iface.reference.FieldReference, org.jf.dexlib2.iface.Field, org.jf.dexlib2.iface.Member
    @Nonnull
    public String getDefiningClass() {
        return this.definingClass;
    }

    @Override // org.jf.dexlib2.iface.reference.FieldReference, org.jf.dexlib2.iface.Field, org.jf.dexlib2.iface.Member
    @Nonnull
    public String getName() {
        return this.name;
    }

    @Override // org.jf.dexlib2.iface.reference.FieldReference, org.jf.dexlib2.iface.Field
    @Nonnull
    public String getType() {
        return this.type;
    }
}
