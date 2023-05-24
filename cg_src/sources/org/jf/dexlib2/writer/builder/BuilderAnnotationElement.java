package org.jf.dexlib2.writer.builder;

import javax.annotation.Nonnull;
import org.jf.dexlib2.base.BaseAnnotationElement;
import org.jf.dexlib2.iface.value.EncodedValue;
import org.jf.dexlib2.writer.builder.BuilderEncodedValues;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/builder/BuilderAnnotationElement.class */
public class BuilderAnnotationElement extends BaseAnnotationElement {
    @Nonnull
    final BuilderStringReference name;
    @Nonnull
    final BuilderEncodedValues.BuilderEncodedValue value;

    public BuilderAnnotationElement(@Nonnull BuilderStringReference name, @Nonnull BuilderEncodedValues.BuilderEncodedValue value) {
        this.name = name;
        this.value = value;
    }

    @Override // org.jf.dexlib2.iface.AnnotationElement
    @Nonnull
    public String getName() {
        return this.name.getString();
    }

    @Override // org.jf.dexlib2.iface.AnnotationElement
    @Nonnull
    public EncodedValue getValue() {
        return this.value;
    }
}
