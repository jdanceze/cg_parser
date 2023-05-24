package org.jf.dexlib2.writer.builder;

import javax.annotation.Nonnull;
import org.jf.dexlib2.base.reference.BaseStringReference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/builder/BuilderStringReference.class */
public class BuilderStringReference extends BaseStringReference implements BuilderReference {
    @Nonnull
    final String string;
    int index = -1;

    /* JADX INFO: Access modifiers changed from: package-private */
    public BuilderStringReference(@Nonnull String string) {
        this.string = string;
    }

    @Override // org.jf.dexlib2.iface.reference.StringReference
    @Nonnull
    public String getString() {
        return this.string;
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
