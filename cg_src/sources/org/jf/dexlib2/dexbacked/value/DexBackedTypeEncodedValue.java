package org.jf.dexlib2.dexbacked.value;

import javax.annotation.Nonnull;
import org.jf.dexlib2.base.value.BaseTypeEncodedValue;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.DexReader;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/value/DexBackedTypeEncodedValue.class */
public class DexBackedTypeEncodedValue extends BaseTypeEncodedValue {
    @Nonnull
    public final DexBackedDexFile dexFile;
    private final int typeIndex;

    public DexBackedTypeEncodedValue(@Nonnull DexBackedDexFile dexFile, @Nonnull DexReader reader, int valueArg) {
        this.dexFile = dexFile;
        this.typeIndex = reader.readSizedSmallUint(valueArg + 1);
    }

    @Override // org.jf.dexlib2.iface.value.TypeEncodedValue
    @Nonnull
    public String getValue() {
        return (String) this.dexFile.getTypeSection().get(this.typeIndex);
    }
}
