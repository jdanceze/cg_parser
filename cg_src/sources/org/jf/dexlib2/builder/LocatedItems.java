package org.jf.dexlib2.builder;

import com.google.common.collect.ImmutableList;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.builder.ItemWithLocation;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/builder/LocatedItems.class */
public abstract class LocatedItems<T extends ItemWithLocation> {
    @Nullable
    private List<T> items = null;

    protected abstract String getAddLocatedItemError();

    /* JADX INFO: Access modifiers changed from: private */
    @Nonnull
    public List<T> getItems() {
        if (this.items == null) {
            return ImmutableList.of();
        }
        return this.items;
    }

    public Set<T> getModifiableItems(final MethodLocation newItemsLocation) {
        return new AbstractSet<T>() { // from class: org.jf.dexlib2.builder.LocatedItems.1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public /* bridge */ /* synthetic */ boolean add(@Nonnull Object obj) {
                return add((AnonymousClass1) ((ItemWithLocation) obj));
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            @Nonnull
            public Iterator<T> iterator() {
                final Iterator<T> it = LocatedItems.this.getItems().iterator();
                return (Iterator<T>) new Iterator<T>() { // from class: org.jf.dexlib2.builder.LocatedItems.1.1
                    @Nullable
                    private T currentItem = null;

                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override // java.util.Iterator
                    public T next() {
                        this.currentItem = (T) it.next();
                        return this.currentItem;
                    }

                    @Override // java.util.Iterator
                    public void remove() {
                        if (this.currentItem != null) {
                            this.currentItem.setLocation(null);
                        }
                        it.remove();
                    }
                };
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                return LocatedItems.this.getItems().size();
            }

            public boolean add(@Nonnull T item) {
                if (item.isPlaced()) {
                    throw new IllegalArgumentException(LocatedItems.this.getAddLocatedItemError());
                }
                item.setLocation(newItemsLocation);
                LocatedItems.this.addItem(item);
                return true;
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addItem(@Nonnull T item) {
        if (this.items == null) {
            this.items = new ArrayList(1);
        }
        this.items.add(item);
    }

    public void mergeItemsIntoNext(@Nonnull MethodLocation nextLocation, LocatedItems<T> otherLocatedItems) {
        if (otherLocatedItems != this && this.items != null) {
            for (T item : this.items) {
                item.setLocation(nextLocation);
            }
            List<T> mergedItems = this.items;
            mergedItems.addAll(otherLocatedItems.getItems());
            otherLocatedItems.items = mergedItems;
            this.items = null;
        }
    }
}
