package org.jf.dexlib2.writer.builder;

import java.util.List;
import javax.annotation.Nonnull;
import org.jf.dexlib2.base.reference.BaseMethodProtoReference;
import org.jf.dexlib2.iface.reference.MethodProtoReference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/builder/BuilderMethodProtoReference.class */
public class BuilderMethodProtoReference extends BaseMethodProtoReference implements MethodProtoReference, BuilderReference {
    @Nonnull
    final BuilderStringReference shorty;
    @Nonnull
    final BuilderTypeList parameterTypes;
    @Nonnull
    final BuilderTypeReference returnType;
    int index = -1;

    public BuilderMethodProtoReference(@Nonnull BuilderStringReference shorty, @Nonnull BuilderTypeList parameterTypes, @Nonnull BuilderTypeReference returnType) {
        this.shorty = shorty;
        this.parameterTypes = parameterTypes;
        this.returnType = returnType;
    }

    @Override // org.jf.dexlib2.iface.reference.MethodProtoReference
    @Nonnull
    public List<? extends CharSequence> getParameterTypes() {
        return this.parameterTypes;
    }

    @Override // org.jf.dexlib2.iface.reference.MethodProtoReference
    @Nonnull
    public String getReturnType() {
        return this.returnType.getType();
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
