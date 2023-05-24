package org.jf.dexlib2.dexbacked.value;

import java.util.List;
import javax.annotation.Nonnull;
import org.jf.dexlib2.base.value.BaseArrayEncodedValue;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.DexReader;
import org.jf.dexlib2.dexbacked.util.VariableSizeList;
import org.jf.dexlib2.iface.value.ArrayEncodedValue;
import org.jf.dexlib2.iface.value.EncodedValue;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/value/DexBackedArrayEncodedValue.class */
public class DexBackedArrayEncodedValue extends BaseArrayEncodedValue implements ArrayEncodedValue {
    @Nonnull
    public final DexBackedDexFile dexFile;
    private final int elementCount;
    private final int encodedArrayOffset;

    public DexBackedArrayEncodedValue(@Nonnull DexBackedDexFile dexFile, @Nonnull DexReader reader) {
        this.dexFile = dexFile;
        this.elementCount = reader.readSmallUleb128();
        this.encodedArrayOffset = reader.getOffset();
        skipElementsFrom(reader, this.elementCount);
    }

    public static void skipFrom(@Nonnull DexReader reader) {
        int elementCount = reader.readSmallUleb128();
        skipElementsFrom(reader, elementCount);
    }

    private static void skipElementsFrom(@Nonnull DexReader reader, int elementCount) {
        for (int i = 0; i < elementCount; i++) {
            DexBackedEncodedValue.skipFrom(reader);
        }
    }

    @Override // org.jf.dexlib2.iface.value.ArrayEncodedValue
    @Nonnull
    public List<? extends EncodedValue> getValue() {
        return new VariableSizeList<EncodedValue>(this.dexFile.getDataBuffer(), this.encodedArrayOffset, this.elementCount) { // from class: org.jf.dexlib2.dexbacked.value.DexBackedArrayEncodedValue.1
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.jf.dexlib2.dexbacked.util.VariableSizeList
            @Nonnull
            public EncodedValue readNextItem(@Nonnull DexReader dexReader, int index) {
                return DexBackedEncodedValue.readFrom(DexBackedArrayEncodedValue.this.dexFile, dexReader);
            }
        };
    }
}
