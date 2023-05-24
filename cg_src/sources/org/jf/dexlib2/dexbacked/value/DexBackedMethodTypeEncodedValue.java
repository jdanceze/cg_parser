package org.jf.dexlib2.dexbacked.value;

import javax.annotation.Nonnull;
import org.jf.dexlib2.base.value.BaseMethodTypeEncodedValue;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.DexReader;
import org.jf.dexlib2.dexbacked.reference.DexBackedMethodProtoReference;
import org.jf.dexlib2.iface.reference.MethodProtoReference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/value/DexBackedMethodTypeEncodedValue.class */
public class DexBackedMethodTypeEncodedValue extends BaseMethodTypeEncodedValue {
    @Nonnull
    public final DexBackedDexFile dexFile;
    private final int methodProtoIndex;

    public DexBackedMethodTypeEncodedValue(@Nonnull DexBackedDexFile dexFile, @Nonnull DexReader reader, int valueArg) {
        this.dexFile = dexFile;
        this.methodProtoIndex = reader.readSizedSmallUint(valueArg + 1);
    }

    @Override // org.jf.dexlib2.iface.value.MethodTypeEncodedValue
    @Nonnull
    public MethodProtoReference getValue() {
        return new DexBackedMethodProtoReference(this.dexFile, this.methodProtoIndex);
    }
}
