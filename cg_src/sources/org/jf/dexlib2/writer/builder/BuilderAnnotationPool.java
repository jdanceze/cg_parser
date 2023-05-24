package org.jf.dexlib2.writer.builder;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.Nonnull;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.writer.AnnotationSection;
import org.jf.dexlib2.writer.builder.BuilderEncodedValues;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/builder/BuilderAnnotationPool.class */
class BuilderAnnotationPool extends BaseBuilderPool implements AnnotationSection<BuilderStringReference, BuilderTypeReference, BuilderAnnotation, BuilderAnnotationElement, BuilderEncodedValues.BuilderEncodedValue> {
    @Nonnull
    private final ConcurrentMap<Annotation, BuilderAnnotation> internedItems;

    public BuilderAnnotationPool(@Nonnull DexBuilder dexBuilder) {
        super(dexBuilder);
        this.internedItems = Maps.newConcurrentMap();
    }

    @Nonnull
    public BuilderAnnotation internAnnotation(@Nonnull Annotation annotation) {
        BuilderAnnotation ret = this.internedItems.get(annotation);
        if (ret != null) {
            return ret;
        }
        BuilderAnnotation dexBuilderAnnotation = new BuilderAnnotation(annotation.getVisibility(), ((BuilderTypePool) this.dexBuilder.typeSection).internType(annotation.getType()), this.dexBuilder.internAnnotationElements(annotation.getElements()));
        BuilderAnnotation ret2 = this.internedItems.putIfAbsent(dexBuilderAnnotation, dexBuilderAnnotation);
        return ret2 == null ? dexBuilderAnnotation : ret2;
    }

    @Override // org.jf.dexlib2.writer.AnnotationSection
    public int getVisibility(@Nonnull BuilderAnnotation key) {
        return key.visibility;
    }

    @Override // org.jf.dexlib2.writer.AnnotationSection
    @Nonnull
    public BuilderTypeReference getType(@Nonnull BuilderAnnotation key) {
        return key.type;
    }

    @Override // org.jf.dexlib2.writer.AnnotationSection
    @Nonnull
    public Collection<? extends BuilderAnnotationElement> getElements(@Nonnull BuilderAnnotation key) {
        return key.elements;
    }

    @Override // org.jf.dexlib2.writer.AnnotationSection
    @Nonnull
    public BuilderStringReference getElementName(@Nonnull BuilderAnnotationElement element) {
        return element.name;
    }

    @Override // org.jf.dexlib2.writer.AnnotationSection
    @Nonnull
    public BuilderEncodedValues.BuilderEncodedValue getElementValue(@Nonnull BuilderAnnotationElement element) {
        return element.value;
    }

    @Override // org.jf.dexlib2.writer.OffsetSection
    public int getItemOffset(@Nonnull BuilderAnnotation key) {
        return key.offset;
    }

    @Override // org.jf.dexlib2.writer.OffsetSection
    @Nonnull
    public Collection<? extends Map.Entry<? extends BuilderAnnotation, Integer>> getItems() {
        return new BuilderMapEntryCollection<BuilderAnnotation>(this.internedItems.values()) { // from class: org.jf.dexlib2.writer.builder.BuilderAnnotationPool.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.jf.dexlib2.writer.builder.BuilderMapEntryCollection
            public int getValue(@Nonnull BuilderAnnotation key) {
                return key.offset;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.jf.dexlib2.writer.builder.BuilderMapEntryCollection
            public int setValue(@Nonnull BuilderAnnotation key, int value) {
                int prev = key.offset;
                key.offset = value;
                return prev;
            }
        };
    }
}
