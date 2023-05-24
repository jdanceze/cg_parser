package org.jf.dexlib2.dexbacked.util;

import java.util.AbstractSet;
import javax.annotation.Nonnull;
import org.jf.dexlib2.dexbacked.DexBuffer;
import org.jf.dexlib2.dexbacked.DexReader;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/util/VariableSizeSet.class */
public abstract class VariableSizeSet<T> extends AbstractSet<T> {
    @Nonnull
    private final DexBuffer buffer;
    private final int offset;
    private final int size;

    protected abstract T readNextItem(@Nonnull DexReader dexReader, int i);

    public VariableSizeSet(@Nonnull DexBuffer buffer, int offset, int size) {
        this.buffer = buffer;
        this.offset = offset;
        this.size = size;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    @Nonnull
    public VariableSizeIterator<T> iterator() {
        return new VariableSizeIterator<T>(this.buffer, this.offset, this.size) { // from class: org.jf.dexlib2.dexbacked.util.VariableSizeSet.1
            @Override // org.jf.dexlib2.dexbacked.util.VariableSizeIterator
            protected T readNextItem(@Nonnull DexReader reader, int index) {
                return (T) VariableSizeSet.this.readNextItem(reader, index);
            }
        };
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.size;
    }
}
