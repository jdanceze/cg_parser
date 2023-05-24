package org.jf.dexlib2.immutable;

import com.google.common.collect.ImmutableSet;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.BaseAnnotationElement;
import org.jf.dexlib2.iface.AnnotationElement;
import org.jf.dexlib2.iface.value.EncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableEncodedValueFactory;
import org.jf.util.ImmutableConverter;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/immutable/ImmutableAnnotationElement.class */
public class ImmutableAnnotationElement extends BaseAnnotationElement {
    @Nonnull
    protected final String name;
    @Nonnull
    protected final ImmutableEncodedValue value;
    private static final ImmutableConverter<ImmutableAnnotationElement, AnnotationElement> CONVERTER = new ImmutableConverter<ImmutableAnnotationElement, AnnotationElement>() { // from class: org.jf.dexlib2.immutable.ImmutableAnnotationElement.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.jf.util.ImmutableConverter
        public boolean isImmutable(@Nonnull AnnotationElement item) {
            return item instanceof ImmutableAnnotationElement;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.jf.util.ImmutableConverter
        @Nonnull
        public ImmutableAnnotationElement makeImmutable(@Nonnull AnnotationElement item) {
            return ImmutableAnnotationElement.of(item);
        }
    };

    public ImmutableAnnotationElement(@Nonnull String name, @Nonnull EncodedValue value) {
        this.name = name;
        this.value = ImmutableEncodedValueFactory.of(value);
    }

    public ImmutableAnnotationElement(@Nonnull String name, @Nonnull ImmutableEncodedValue value) {
        this.name = name;
        this.value = value;
    }

    public static ImmutableAnnotationElement of(AnnotationElement annotationElement) {
        if (annotationElement instanceof ImmutableAnnotationElement) {
            return (ImmutableAnnotationElement) annotationElement;
        }
        return new ImmutableAnnotationElement(annotationElement.getName(), annotationElement.getValue());
    }

    @Override // org.jf.dexlib2.iface.AnnotationElement
    @Nonnull
    public String getName() {
        return this.name;
    }

    @Override // org.jf.dexlib2.iface.AnnotationElement
    @Nonnull
    public EncodedValue getValue() {
        return this.value;
    }

    @Nonnull
    public static ImmutableSet<ImmutableAnnotationElement> immutableSetOf(@Nullable Iterable<? extends AnnotationElement> list) {
        return CONVERTER.toSet(list);
    }
}
