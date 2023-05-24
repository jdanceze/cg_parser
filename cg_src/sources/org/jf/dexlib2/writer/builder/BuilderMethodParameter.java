package org.jf.dexlib2.writer.builder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.BaseMethodParameter;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/builder/BuilderMethodParameter.class */
public class BuilderMethodParameter extends BaseMethodParameter {
    @Nonnull
    final BuilderTypeReference type;
    @Nullable
    final BuilderStringReference name;
    @Nonnull
    final BuilderAnnotationSet annotations;

    public BuilderMethodParameter(@Nonnull BuilderTypeReference type, @Nullable BuilderStringReference name, @Nonnull BuilderAnnotationSet annotations) {
        this.type = type;
        this.name = name;
        this.annotations = annotations;
    }

    @Override // org.jf.dexlib2.iface.reference.TypeReference, org.jf.dexlib2.iface.ClassDef
    @Nonnull
    public String getType() {
        return this.type.getType();
    }

    @Override // org.jf.dexlib2.iface.MethodParameter, org.jf.dexlib2.iface.debug.LocalInfo
    @Nullable
    public String getName() {
        if (this.name == null) {
            return null;
        }
        return this.name.getString();
    }

    @Override // org.jf.dexlib2.iface.MethodParameter
    @Nonnull
    public BuilderAnnotationSet getAnnotations() {
        return this.annotations;
    }
}
