package org.jf.dexlib2.writer.builder;

import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.HiddenApiRestriction;
import org.jf.dexlib2.base.reference.BaseMethodReference;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.iface.MethodImplementation;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/builder/BuilderMethod.class */
public class BuilderMethod extends BaseMethodReference implements Method {
    @Nonnull
    final BuilderMethodReference methodReference;
    @Nonnull
    final List<? extends BuilderMethodParameter> parameters;
    final int accessFlags;
    @Nonnull
    final BuilderAnnotationSet annotations;
    @Nonnull
    final Set<HiddenApiRestriction> hiddenApiRestrictions;
    @Nullable
    final MethodImplementation methodImplementation;
    int annotationSetRefListOffset = 0;
    int codeItemOffset = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public BuilderMethod(@Nonnull BuilderMethodReference methodReference, @Nonnull List<? extends BuilderMethodParameter> parameters, int accessFlags, @Nonnull BuilderAnnotationSet annotations, @Nonnull Set<HiddenApiRestriction> hiddenApiRestrictions, @Nullable MethodImplementation methodImplementation) {
        this.methodReference = methodReference;
        this.parameters = parameters;
        this.accessFlags = accessFlags;
        this.annotations = annotations;
        this.hiddenApiRestrictions = hiddenApiRestrictions;
        this.methodImplementation = methodImplementation;
    }

    @Override // org.jf.dexlib2.iface.reference.MethodReference, org.jf.dexlib2.iface.Method, org.jf.dexlib2.iface.Member
    @Nonnull
    public String getDefiningClass() {
        return this.methodReference.definingClass.getType();
    }

    @Override // org.jf.dexlib2.iface.reference.MethodReference, org.jf.dexlib2.iface.Method, org.jf.dexlib2.iface.Member
    @Nonnull
    public String getName() {
        return this.methodReference.name.getString();
    }

    @Override // org.jf.dexlib2.iface.reference.MethodReference
    @Nonnull
    public BuilderTypeList getParameterTypes() {
        return this.methodReference.proto.parameterTypes;
    }

    @Override // org.jf.dexlib2.iface.reference.MethodReference, org.jf.dexlib2.iface.Method
    @Nonnull
    public String getReturnType() {
        return this.methodReference.proto.returnType.getType();
    }

    @Override // org.jf.dexlib2.iface.Method
    @Nonnull
    public List<? extends BuilderMethodParameter> getParameters() {
        return this.parameters;
    }

    @Override // org.jf.dexlib2.iface.Method, org.jf.dexlib2.iface.Member
    public int getAccessFlags() {
        return this.accessFlags;
    }

    @Override // org.jf.dexlib2.iface.Method, org.jf.dexlib2.iface.Annotatable
    @Nonnull
    public BuilderAnnotationSet getAnnotations() {
        return this.annotations;
    }

    @Override // org.jf.dexlib2.iface.Method, org.jf.dexlib2.iface.Member
    @Nonnull
    public Set<HiddenApiRestriction> getHiddenApiRestrictions() {
        return this.hiddenApiRestrictions;
    }

    @Override // org.jf.dexlib2.iface.Method
    @Nullable
    public MethodImplementation getImplementation() {
        return this.methodImplementation;
    }
}
