package org.jf.dexlib2.dexbacked.value;

import javax.annotation.Nonnull;
import org.jf.dexlib2.base.value.BaseStringEncodedValue;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.DexReader;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/value/DexBackedStringEncodedValue.class */
public class DexBackedStringEncodedValue extends BaseStringEncodedValue {
    @Nonnull
    public final DexBackedDexFile dexFile;
    private final int stringIndex;

    public DexBackedStringEncodedValue(@Nonnull DexBackedDexFile dexFile, @Nonnull DexReader reader, int valueArg) {
        this.dexFile = dexFile;
        this.stringIndex = reader.readSizedSmallUint(valueArg + 1);
    }

    @Override // org.jf.dexlib2.iface.value.StringEncodedValue
    @Nonnull
    public String getValue() {
        return (String) this.dexFile.getStringSection().get(this.stringIndex);
    }
}
