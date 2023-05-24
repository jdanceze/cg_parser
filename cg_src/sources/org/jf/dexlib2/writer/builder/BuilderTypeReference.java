package org.jf.dexlib2.writer.builder;

import javax.annotation.Nonnull;
import org.jf.dexlib2.base.reference.BaseTypeReference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/builder/BuilderTypeReference.class */
public class BuilderTypeReference extends BaseTypeReference implements BuilderReference {
    @Nonnull
    final BuilderStringReference stringReference;
    int index = -1;

    /* JADX INFO: Access modifiers changed from: package-private */
    public BuilderTypeReference(@Nonnull BuilderStringReference stringReference) {
        this.stringReference = stringReference;
    }

    @Override // org.jf.dexlib2.iface.reference.TypeReference, org.jf.dexlib2.iface.ClassDef
    @Nonnull
    public String getType() {
        return this.stringReference.getString();
    }

    @Override // org.jf.dexlib2.writer.builder.BuilderReference
    public int getIndex() {
        return this.index;
    }

    @Override // org.jf.dexlib2.writer.builder.BuilderReference
    public void setIndex(int index) {
        this.index = index;
    }
}
