package org.jf.dexlib2.writer.builder;

import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.HiddenApiRestriction;
import org.jf.dexlib2.base.reference.BaseFieldReference;
import org.jf.dexlib2.iface.Field;
import org.jf.dexlib2.writer.builder.BuilderEncodedValues;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/builder/BuilderField.class */
public class BuilderField extends BaseFieldReference implements Field {
    @Nonnull
    final BuilderFieldReference fieldReference;
    final int accessFlags;
    @Nullable
    final BuilderEncodedValues.BuilderEncodedValue initialValue;
    @Nonnull
    final BuilderAnnotationSet annotations;
    @Nonnull
    Set<HiddenApiRestriction> hiddenApiRestrictions;

    /* JADX INFO: Access modifiers changed from: package-private */
    public BuilderField(@Nonnull BuilderFieldReference fieldReference, int accessFlags, @Nullable BuilderEncodedValues.BuilderEncodedValue initialValue, @Nonnull BuilderAnnotationSet annotations, @Nonnull Set<HiddenApiRestriction> hiddenApiRestrictions) {
        this.fieldReference = fieldReference;
        this.accessFlags = accessFlags;
        this.initialValue = initialValue;
        this.annotations = annotations;
        this.hiddenApiRestrictions = hiddenApiRestrictions;
    }

    @Override // org.jf.dexlib2.iface.Field, org.jf.dexlib2.iface.Member
    public int getAccessFlags() {
        return this.accessFlags;
    }

    @Override // org.jf.dexlib2.iface.Field
    @Nullable
    public BuilderEncodedValues.BuilderEncodedValue getInitialValue() {
        return this.initialValue;
    }

    @Override // org.jf.dexlib2.iface.Field, org.jf.dexlib2.iface.Annotatable
    @Nonnull
    public BuilderAnnotationSet getAnnotations() {
        return this.annotations;
    }

    @Override // org.jf.dexlib2.iface.reference.FieldReference, org.jf.dexlib2.iface.Field, org.jf.dexlib2.iface.Member
    @Nonnull
    public String getDefiningClass() {
        return this.fieldReference.definingClass.getType();
    }

    @Override // org.jf.dexlib2.iface.reference.FieldReference, org.jf.dexlib2.iface.Field, org.jf.dexlib2.iface.Member
    @Nonnull
    public String getName() {
        return this.fieldReference.name.getString();
    }

    @Override // org.jf.dexlib2.iface.reference.FieldReference, org.jf.dexlib2.iface.Field
    @Nonnull
    public String getType() {
        return this.fieldReference.fieldType.getType();
    }

    @Override // org.jf.dexlib2.iface.Field, org.jf.dexlib2.iface.Member
    @Nonnull
    public Set<HiddenApiRestriction> getHiddenApiRestrictions() {
        return this.hiddenApiRestrictions;
    }
}
