package org.jf.dexlib2.dexbacked.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.DexReader;
import org.jf.dexlib2.dexbacked.value.DexBackedEncodedValue;
import org.jf.dexlib2.iface.value.EncodedValue;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/util/EncodedArrayItemIterator.class */
public abstract class EncodedArrayItemIterator {
    public static final EncodedArrayItemIterator EMPTY = new EncodedArrayItemIterator() { // from class: org.jf.dexlib2.dexbacked.util.EncodedArrayItemIterator.1
        @Override // org.jf.dexlib2.dexbacked.util.EncodedArrayItemIterator
        @Nullable
        public EncodedValue getNextOrNull() {
            return null;
        }

        @Override // org.jf.dexlib2.dexbacked.util.EncodedArrayItemIterator
        public void skipNext() {
        }

        @Override // org.jf.dexlib2.dexbacked.util.EncodedArrayItemIterator
        public int getReaderOffset() {
            return 0;
        }

        @Override // org.jf.dexlib2.dexbacked.util.EncodedArrayItemIterator
        public int getItemCount() {
            return 0;
        }
    };

    @Nullable
    public abstract EncodedValue getNextOrNull();

    public abstract void skipNext();

    public abstract int getReaderOffset();

    public abstract int getItemCount();

    @Nonnull
    public static EncodedArrayItemIterator newOrEmpty(@Nonnull DexBackedDexFile dexFile, int offset) {
        if (offset == 0) {
            return EMPTY;
        }
        return new EncodedArrayItemIteratorImpl(dexFile, offset);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/util/EncodedArrayItemIterator$EncodedArrayItemIteratorImpl.class */
    public static class EncodedArrayItemIteratorImpl extends EncodedArrayItemIterator {
        @Nonnull
        private final DexReader reader;
        @Nonnull
        private final DexBackedDexFile dexFile;
        private final int size;
        private int index = 0;

        public EncodedArrayItemIteratorImpl(@Nonnull DexBackedDexFile dexFile, int offset) {
            this.dexFile = dexFile;
            this.reader = dexFile.getDataBuffer().readerAt(offset);
            this.size = this.reader.readSmallUleb128();
        }

        @Override // org.jf.dexlib2.dexbacked.util.EncodedArrayItemIterator
        @Nullable
        public EncodedValue getNextOrNull() {
            if (this.index < this.size) {
                this.index++;
                return DexBackedEncodedValue.readFrom(this.dexFile, this.reader);
            }
            return null;
        }

        @Override // org.jf.dexlib2.dexbacked.util.EncodedArrayItemIterator
        public void skipNext() {
            if (this.index < this.size) {
                this.index++;
                DexBackedEncodedValue.skipFrom(this.reader);
            }
        }

        @Override // org.jf.dexlib2.dexbacked.util.EncodedArrayItemIterator
        public int getReaderOffset() {
            return this.reader.getOffset();
        }

        @Override // org.jf.dexlib2.dexbacked.util.EncodedArrayItemIterator
        public int getItemCount() {
            return this.size;
        }
    }
}
