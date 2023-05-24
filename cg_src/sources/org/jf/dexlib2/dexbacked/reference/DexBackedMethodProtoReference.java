package org.jf.dexlib2.dexbacked.reference;

import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.Nonnull;
import org.jf.dexlib2.base.reference.BaseMethodProtoReference;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.util.FixedSizeList;
import org.jf.dexlib2.iface.reference.Reference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/reference/DexBackedMethodProtoReference.class */
public class DexBackedMethodProtoReference extends BaseMethodProtoReference {
    @Nonnull
    public final DexBackedDexFile dexFile;
    private final int protoIndex;

    public DexBackedMethodProtoReference(@Nonnull DexBackedDexFile dexFile, int protoIndex) {
        this.dexFile = dexFile;
        this.protoIndex = protoIndex;
    }

    @Override // org.jf.dexlib2.iface.reference.MethodProtoReference
    @Nonnull
    public List<String> getParameterTypes() {
        int parametersOffset = this.dexFile.getBuffer().readSmallUint(this.dexFile.getProtoSection().getOffset(this.protoIndex) + 8);
        if (parametersOffset > 0) {
            final int parameterCount = this.dexFile.getDataBuffer().readSmallUint(parametersOffset + 0);
            final int paramListStart = parametersOffset + 4;
            return new FixedSizeList<String>() { // from class: org.jf.dexlib2.dexbacked.reference.DexBackedMethodProtoReference.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // org.jf.dexlib2.dexbacked.util.FixedSizeList
                @Nonnull
                public String readItem(int index) {
                    return (String) DexBackedMethodProtoReference.this.dexFile.getTypeSection().get(DexBackedMethodProtoReference.this.dexFile.getDataBuffer().readUshort(paramListStart + (2 * index)));
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
                public int size() {
                    return parameterCount;
                }
            };
        }
        return ImmutableList.of();
    }

    @Override // org.jf.dexlib2.iface.reference.MethodProtoReference
    @Nonnull
    public String getReturnType() {
        return (String) this.dexFile.getTypeSection().get(this.dexFile.getBuffer().readSmallUint(this.dexFile.getProtoSection().getOffset(this.protoIndex) + 4));
    }

    public int getSize() {
        int size = 12;
        List<String> parameters = getParameterTypes();
        if (!parameters.isEmpty()) {
            size = 12 + 4 + (parameters.size() * 2);
        }
        return size;
    }

    @Override // org.jf.dexlib2.base.reference.BaseReference, org.jf.dexlib2.iface.reference.Reference
    public void validateReference() throws Reference.InvalidReferenceException {
        if (this.protoIndex < 0 || this.protoIndex >= this.dexFile.getProtoSection().size()) {
            throw new Reference.InvalidReferenceException("proto@" + this.protoIndex);
        }
    }
}
