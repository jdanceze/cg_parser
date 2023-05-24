package org.jf.dexlib2.dexbacked.util;

import java.util.ListIterator;
import java.util.NoSuchElementException;
import javax.annotation.Nonnull;
import org.jf.dexlib2.dexbacked.DexBuffer;
import org.jf.dexlib2.dexbacked.DexReader;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/util/VariableSizeListIterator.class */
public abstract class VariableSizeListIterator<T> implements ListIterator<T> {
    @Nonnull
    private DexReader<? extends DexBuffer> reader;
    protected final int size;
    private final int startOffset;
    private int index;

    protected abstract T readNextItem(@Nonnull DexReader<? extends DexBuffer> dexReader, int i);

    /* JADX INFO: Access modifiers changed from: protected */
    public VariableSizeListIterator(@Nonnull DexBuffer buffer, int offset, int size) {
        this.reader = buffer.readerAt(offset);
        this.startOffset = offset;
        this.size = size;
    }

    public int getReaderOffset() {
        return this.reader.getOffset();
    }

    @Override // java.util.ListIterator, java.util.Iterator
    public boolean hasNext() {
        return this.index < this.size;
    }

    @Override // java.util.ListIterator, java.util.Iterator
    public T next() {
        if (this.index >= this.size) {
            throw new NoSuchElementException();
        }
        DexReader<? extends DexBuffer> dexReader = this.reader;
        int i = this.index;
        this.index = i + 1;
        return readNextItem(dexReader, i);
    }

    @Override // java.util.ListIterator
    public boolean hasPrevious() {
        return this.index > 0;
    }

    @Override // java.util.ListIterator
    public T previous() {
        int targetIndex = this.index - 1;
        this.reader.setOffset(this.startOffset);
        this.index = 0;
        while (this.index < targetIndex) {
            DexReader<? extends DexBuffer> dexReader = this.reader;
            int i = this.index;
            this.index = i + 1;
            readNextItem(dexReader, i);
        }
        DexReader<? extends DexBuffer> dexReader2 = this.reader;
        int i2 = this.index;
        this.index = i2 + 1;
        return readNextItem(dexReader2, i2);
    }

    @Override // java.util.ListIterator
    public int nextIndex() {
        return this.index;
    }

    @Override // java.util.ListIterator
    public int previousIndex() {
        return this.index - 1;
    }

    @Override // java.util.ListIterator, java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.ListIterator
    public void set(T t) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.ListIterator
    public void add(T t) {
        throw new UnsupportedOperationException();
    }
}
