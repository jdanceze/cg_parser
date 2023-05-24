package org.jf.dexlib2.dexbacked.util;

import com.google.common.collect.AbstractIterator;
import java.util.Iterator;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.dexbacked.DexBuffer;
import org.jf.dexlib2.dexbacked.DexReader;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/util/VariableSizeLookaheadIterator.class */
public abstract class VariableSizeLookaheadIterator<T> extends AbstractIterator<T> implements Iterator<T> {
    @Nonnull
    private final DexReader reader;

    @Nullable
    protected abstract T readNextItem(@Nonnull DexReader dexReader);

    /* JADX INFO: Access modifiers changed from: protected */
    public VariableSizeLookaheadIterator(@Nonnull DexBuffer buffer, int offset) {
        this.reader = buffer.readerAt(offset);
    }

    @Override // com.google.common.collect.AbstractIterator
    protected T computeNext() {
        return readNextItem(this.reader);
    }

    public final int getReaderOffset() {
        return this.reader.getOffset();
    }
}
