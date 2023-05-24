package org.jf.dexlib2.dexbacked.value;

import javax.annotation.Nonnull;
import org.jf.dexlib2.base.value.BaseMethodHandleEncodedValue;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.DexReader;
import org.jf.dexlib2.dexbacked.reference.DexBackedMethodHandleReference;
import org.jf.dexlib2.iface.reference.MethodHandleReference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/value/DexBackedMethodHandleEncodedValue.class */
public class DexBackedMethodHandleEncodedValue extends BaseMethodHandleEncodedValue {
    @Nonnull
    public final DexBackedDexFile dexFile;
    private final int methodHandleIndex;

    public DexBackedMethodHandleEncodedValue(@Nonnull DexBackedDexFile dexFile, @Nonnull DexReader reader, int valueArg) {
        this.dexFile = dexFile;
        this.methodHandleIndex = reader.readSizedSmallUint(valueArg + 1);
    }

    @Override // org.jf.dexlib2.iface.value.MethodHandleEncodedValue
    @Nonnull
    public MethodHandleReference getValue() {
        return new DexBackedMethodHandleReference(this.dexFile, this.methodHandleIndex);
    }
}
