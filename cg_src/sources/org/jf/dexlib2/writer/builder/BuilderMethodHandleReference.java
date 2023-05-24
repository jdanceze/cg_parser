package org.jf.dexlib2.writer.builder;

import javax.annotation.Nonnull;
import org.jf.dexlib2.base.reference.BaseMethodHandleReference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/builder/BuilderMethodHandleReference.class */
public class BuilderMethodHandleReference extends BaseMethodHandleReference implements BuilderReference {
    final int methodHandleType;
    @Nonnull
    final BuilderReference memberReference;
    int index = -1;

    public BuilderMethodHandleReference(int methodHandleType, @Nonnull BuilderReference memberReference) {
        this.methodHandleType = methodHandleType;
        this.memberReference = memberReference;
    }

    @Override // org.jf.dexlib2.iface.reference.MethodHandleReference
    public int getMethodHandleType() {
        return this.methodHandleType;
    }

    @Override // org.jf.dexlib2.iface.reference.MethodHandleReference
    @Nonnull
    public BuilderReference getMemberReference() {
        return this.memberReference;
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
