package org.jf.dexlib2.dexbacked.util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import javax.annotation.Nonnull;
import org.jf.dexlib2.dexbacked.DexBuffer;
import org.jf.dexlib2.dexbacked.DexReader;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/util/VariableSizeIterator.class */
public abstract class VariableSizeIterator<T> implements Iterator<T> {
    @Nonnull
    private final DexReader reader;
    protected final int size;
    private int index;

    protected abstract T readNextItem(@Nonnull DexReader dexReader, int i);

    /* JADX INFO: Access modifiers changed from: protected */
    public VariableSizeIterator(@Nonnull DexBuffer buffer, int offset, int size) {
        this.reader = buffer.readerAt(offset);
        this.size = size;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public VariableSizeIterator(@Nonnull DexReader reader, int size) {
        this.reader = reader;
        this.size = size;
    }

    public int getReaderOffset() {
        return this.reader.getOffset();
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.index < this.size;
    }

    @Override // java.util.Iterator
    public T next() {
        if (this.index >= this.size) {
            throw new NoSuchElementException();
        }
        DexReader dexReader = this.reader;
        int i = this.index;
        this.index = i + 1;
        return readNextItem(dexReader, i);
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
