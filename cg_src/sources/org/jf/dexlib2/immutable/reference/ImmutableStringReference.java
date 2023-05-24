package org.jf.dexlib2.immutable.reference;

import javax.annotation.Nonnull;
import org.jf.dexlib2.base.reference.BaseStringReference;
import org.jf.dexlib2.iface.reference.StringReference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/immutable/reference/ImmutableStringReference.class */
public class ImmutableStringReference extends BaseStringReference implements ImmutableReference {
    @Nonnull
    protected final String str;

    public ImmutableStringReference(String str) {
        this.str = str;
    }

    @Nonnull
    public static ImmutableStringReference of(@Nonnull StringReference stringReference) {
        if (stringReference instanceof ImmutableStringReference) {
            return (ImmutableStringReference) stringReference;
        }
        return new ImmutableStringReference(stringReference.getString());
    }

    @Override // org.jf.dexlib2.iface.reference.StringReference
    @Nonnull
    public String getString() {
        return this.str;
    }
}
