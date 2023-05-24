package org.jf.dexlib2.writer.pool;

import java.util.List;
import javax.annotation.Nonnull;
import org.jf.dexlib2.iface.value.ArrayEncodedValue;
import org.jf.dexlib2.iface.value.EncodedValue;
import org.jf.dexlib2.writer.EncodedArraySection;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/pool/EncodedArrayPool.class */
public class EncodedArrayPool extends BaseOffsetPool<ArrayEncodedValue> implements EncodedArraySection<ArrayEncodedValue, EncodedValue> {
    public EncodedArrayPool(@Nonnull DexPool dexPool) {
        super(dexPool);
    }

    public void intern(@Nonnull ArrayEncodedValue arrayEncodedValue) {
        Integer prev = (Integer) this.internedItems.put(arrayEncodedValue, 0);
        if (prev == null) {
            for (EncodedValue value : arrayEncodedValue.getValue()) {
                this.dexPool.internEncodedValue(value);
            }
        }
    }

    @Override // org.jf.dexlib2.writer.EncodedArraySection
    public List<? extends EncodedValue> getEncodedValueList(ArrayEncodedValue arrayEncodedValue) {
        return arrayEncodedValue.getValue();
    }
}
