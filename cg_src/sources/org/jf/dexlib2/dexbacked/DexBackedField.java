package org.jf.dexlib2.dexbacked;

import com.google.common.collect.ImmutableSet;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.HiddenApiRestriction;
import org.jf.dexlib2.base.reference.BaseFieldReference;
import org.jf.dexlib2.dexbacked.reference.DexBackedFieldReference;
import org.jf.dexlib2.dexbacked.util.AnnotationsDirectory;
import org.jf.dexlib2.dexbacked.util.EncodedArrayItemIterator;
import org.jf.dexlib2.dexbacked.value.DexBackedEncodedValue;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.Field;
import org.jf.dexlib2.iface.value.EncodedValue;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/DexBackedField.class */
public class DexBackedField extends BaseFieldReference implements Field {
    @Nonnull
    public final DexBackedDexFile dexFile;
    @Nonnull
    public final ClassDef classDef;
    public final int accessFlags;
    @Nullable
    public final EncodedValue initialValue;
    public final int annotationSetOffset;
    public final int fieldIndex;
    private final int startOffset;
    private final int initialValueOffset;
    private final int hiddenApiRestrictions;
    private int fieldIdItemOffset;

    public DexBackedField(@Nonnull DexBackedDexFile dexFile, @Nonnull DexReader reader, @Nonnull DexBackedClassDef classDef, int previousFieldIndex, @Nonnull EncodedArrayItemIterator staticInitialValueIterator, @Nonnull AnnotationsDirectory.AnnotationIterator annotationIterator, int hiddenApiRestrictions) {
        this.dexFile = dexFile;
        this.classDef = classDef;
        this.startOffset = reader.getOffset();
        int fieldIndexDiff = reader.readLargeUleb128();
        this.fieldIndex = fieldIndexDiff + previousFieldIndex;
        this.accessFlags = reader.readSmallUleb128();
        this.annotationSetOffset = annotationIterator.seekTo(this.fieldIndex);
        this.initialValueOffset = staticInitialValueIterator.getReaderOffset();
        this.initialValue = staticInitialValueIterator.getNextOrNull();
        this.hiddenApiRestrictions = hiddenApiRestrictions;
    }

    public DexBackedField(@Nonnull DexBackedDexFile dexFile, @Nonnull DexReader reader, @Nonnull DexBackedClassDef classDef, int previousFieldIndex, @Nonnull AnnotationsDirectory.AnnotationIterator annotationIterator, int hiddenApiRestrictions) {
        this.dexFile = dexFile;
        this.classDef = classDef;
        this.startOffset = reader.getOffset();
        int fieldIndexDiff = reader.readLargeUleb128();
        this.fieldIndex = fieldIndexDiff + previousFieldIndex;
        this.accessFlags = reader.readSmallUleb128();
        this.annotationSetOffset = annotationIterator.seekTo(this.fieldIndex);
        this.initialValueOffset = 0;
        this.initialValue = null;
        this.hiddenApiRestrictions = hiddenApiRestrictions;
    }

    @Override // org.jf.dexlib2.iface.reference.FieldReference, org.jf.dexlib2.iface.Field, org.jf.dexlib2.iface.Member
    @Nonnull
    public String getName() {
        return (String) this.dexFile.getStringSection().get(this.dexFile.getBuffer().readSmallUint(getFieldIdItemOffset() + 4));
    }

    @Override // org.jf.dexlib2.iface.reference.FieldReference, org.jf.dexlib2.iface.Field
    @Nonnull
    public String getType() {
        return (String) this.dexFile.getTypeSection().get(this.dexFile.getBuffer().readUshort(getFieldIdItemOffset() + 2));
    }

    @Override // org.jf.dexlib2.iface.reference.FieldReference, org.jf.dexlib2.iface.Field, org.jf.dexlib2.iface.Member
    @Nonnull
    public String getDefiningClass() {
        return this.classDef.getType();
    }

    @Override // org.jf.dexlib2.iface.Field, org.jf.dexlib2.iface.Member
    public int getAccessFlags() {
        return this.accessFlags;
    }

    @Override // org.jf.dexlib2.iface.Field
    @Nullable
    public EncodedValue getInitialValue() {
        return this.initialValue;
    }

    @Override // org.jf.dexlib2.iface.Field, org.jf.dexlib2.iface.Annotatable
    @Nonnull
    public Set<? extends DexBackedAnnotation> getAnnotations() {
        return AnnotationsDirectory.getAnnotations(this.dexFile, this.annotationSetOffset);
    }

    @Override // org.jf.dexlib2.iface.Field, org.jf.dexlib2.iface.Member
    @Nonnull
    public Set<HiddenApiRestriction> getHiddenApiRestrictions() {
        if (this.hiddenApiRestrictions == 7) {
            return ImmutableSet.of();
        }
        return EnumSet.copyOf((Collection) HiddenApiRestriction.getAllFlags(this.hiddenApiRestrictions));
    }

    public static void skipFields(@Nonnull DexReader reader, int count) {
        for (int i = 0; i < count; i++) {
            reader.skipUleb128();
            reader.skipUleb128();
        }
    }

    private int getFieldIdItemOffset() {
        if (this.fieldIdItemOffset == 0) {
            this.fieldIdItemOffset = this.dexFile.getFieldSection().getOffset(this.fieldIndex);
        }
        return this.fieldIdItemOffset;
    }

    public int getSize() {
        DexReader reader = this.dexFile.getBuffer().readerAt(this.startOffset);
        reader.readLargeUleb128();
        reader.readSmallUleb128();
        int size = 0 + (reader.getOffset() - this.startOffset);
        Set<? extends DexBackedAnnotation> annotations = getAnnotations();
        if (!annotations.isEmpty()) {
            size += 8;
        }
        if (this.initialValueOffset > 0) {
            reader.setOffset(this.initialValueOffset);
            if (this.initialValue != null) {
                DexBackedEncodedValue.skipFrom(reader);
                size += reader.getOffset() - this.initialValueOffset;
            }
        }
        DexBackedFieldReference fieldRef = new DexBackedFieldReference(this.dexFile, this.fieldIndex);
        return size + fieldRef.getSize();
    }
}
