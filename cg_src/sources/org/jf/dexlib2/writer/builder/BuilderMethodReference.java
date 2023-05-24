package org.jf.dexlib2.writer.builder;

import javax.annotation.Nonnull;
import org.jf.dexlib2.base.reference.BaseMethodReference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/builder/BuilderMethodReference.class */
public class BuilderMethodReference extends BaseMethodReference implements BuilderReference {
    @Nonnull
    final BuilderTypeReference definingClass;
    @Nonnull
    final BuilderStringReference name;
    @Nonnull
    final BuilderMethodProtoReference proto;
    int index = -1;

    /* JADX INFO: Access modifiers changed from: package-private */
    public BuilderMethodReference(@Nonnull BuilderTypeReference definingClass, @Nonnull BuilderStringReference name, @Nonnull BuilderMethodProtoReference proto) {
        this.definingClass = definingClass;
        this.name = name;
        this.proto = proto;
    }

    @Override // org.jf.dexlib2.iface.reference.MethodReference, org.jf.dexlib2.iface.Method, org.jf.dexlib2.iface.Member
    @Nonnull
    public String getDefiningClass() {
        return this.definingClass.getType();
    }

    @Override // org.jf.dexlib2.iface.reference.MethodReference, org.jf.dexlib2.iface.Method, org.jf.dexlib2.iface.Member
    @Nonnull
    public String getName() {
        return this.name.getString();
    }

    @Override // org.jf.dexlib2.iface.reference.MethodReference
    @Nonnull
    public BuilderTypeList getParameterTypes() {
        return this.proto.parameterTypes;
    }

    @Override // org.jf.dexlib2.iface.reference.MethodReference, org.jf.dexlib2.iface.Method
    @Nonnull
    public String getReturnType() {
        return this.proto.returnType.getType();
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
