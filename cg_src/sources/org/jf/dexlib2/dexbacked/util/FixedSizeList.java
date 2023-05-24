package org.jf.dexlib2.dexbacked.util;

import java.util.AbstractList;
import javax.annotation.Nonnull;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/util/FixedSizeList.class */
public abstract class FixedSizeList<T> extends AbstractList<T> {
    @Nonnull
    public abstract T readItem(int i);

    @Override // java.util.AbstractList, java.util.List
    public T get(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }
        return readItem(index);
    }
}
