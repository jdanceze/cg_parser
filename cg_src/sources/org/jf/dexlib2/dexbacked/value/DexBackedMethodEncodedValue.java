package org.jf.dexlib2.dexbacked.value;

import javax.annotation.Nonnull;
import org.jf.dexlib2.base.value.BaseMethodEncodedValue;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.DexReader;
import org.jf.dexlib2.dexbacked.reference.DexBackedMethodReference;
import org.jf.dexlib2.iface.reference.MethodReference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/value/DexBackedMethodEncodedValue.class */
public class DexBackedMethodEncodedValue extends BaseMethodEncodedValue {
    @Nonnull
    public final DexBackedDexFile dexFile;
    private final int methodIndex;

    public DexBackedMethodEncodedValue(@Nonnull DexBackedDexFile dexFile, @Nonnull DexReader reader, int valueArg) {
        this.dexFile = dexFile;
        this.methodIndex = reader.readSizedSmallUint(valueArg + 1);
    }

    @Override // org.jf.dexlib2.iface.value.MethodEncodedValue
    @Nonnull
    public MethodReference getValue() {
        return new DexBackedMethodReference(this.dexFile, this.methodIndex);
    }
}
