package soot;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import soot.Unit;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/PatchingChain.class */
public class PatchingChain<E extends Unit> extends AbstractCollection<E> implements Chain<E> {
    protected final Chain<E> innerChain;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.util.Chain
    public /* bridge */ /* synthetic */ Object getPredOf(Object obj) {
        return getPredOf((PatchingChain<E>) ((Unit) obj));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.util.Chain
    public /* bridge */ /* synthetic */ void addFirst(Object obj) {
        addFirst((PatchingChain<E>) ((Unit) obj));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.util.Chain
    public /* bridge */ /* synthetic */ void insertBefore(Collection collection, Object obj) {
        insertBefore((Collection<? extends Collection>) collection, (Collection) ((Unit) obj));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.util.Chain
    public /* bridge */ /* synthetic */ void insertBefore(Chain chain, Object obj) {
        insertBefore((Chain<Chain>) chain, (Chain) ((Unit) obj));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.util.Chain
    public /* bridge */ /* synthetic */ void insertBefore(List list, Object obj) {
        insertBefore((List<List>) list, (List) ((Unit) obj));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.util.Chain
    public /* bridge */ /* synthetic */ Iterator iterator(Object obj) {
        return iterator((PatchingChain<E>) ((Unit) obj));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.util.Chain
    public /* bridge */ /* synthetic */ void addLast(Object obj) {
        addLast((PatchingChain<E>) ((Unit) obj));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.util.Chain
    public /* bridge */ /* synthetic */ void insertAfter(Collection collection, Object obj) {
        insertAfter((Collection<? extends Collection>) collection, (Collection) ((Unit) obj));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.util.Chain
    public /* bridge */ /* synthetic */ void insertAfter(Chain chain, Object obj) {
        insertAfter((Chain<Chain>) chain, (Chain) ((Unit) obj));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.util.Chain
    public /* bridge */ /* synthetic */ void insertAfter(List list, Object obj) {
        insertAfter((List<List>) list, (List) ((Unit) obj));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.AbstractCollection, java.util.Collection
    public /* bridge */ /* synthetic */ boolean add(Object obj) {
        return add((PatchingChain<E>) ((Unit) obj));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.util.Chain
    public /* bridge */ /* synthetic */ Object getSuccOf(Object obj) {
        return getSuccOf((PatchingChain<E>) ((Unit) obj));
    }

    public PatchingChain(Chain<E> aChain) {
        this.innerChain = aChain;
    }

    public Chain<E> getNonPatchingChain() {
        return this.innerChain;
    }

    public boolean add(E o) {
        return this.innerChain.add(o);
    }

    @Override // soot.util.Chain
    public void swapWith(E out, E in) {
        this.innerChain.swapWith(out, in);
        out.redirectJumpsToThisTo(in);
    }

    @Override // soot.util.Chain
    public void insertAfter(E toInsert, E point) {
        this.innerChain.insertAfter(toInsert, point);
    }

    public void insertAfter(List<E> toInsert, E point) {
        this.innerChain.insertAfter((List<List<E>>) toInsert, (List<E>) point);
    }

    public void insertAfter(Chain<E> toInsert, E point) {
        this.innerChain.insertAfter((Chain<Chain<E>>) toInsert, (Chain<E>) point);
    }

    public void insertAfter(Collection<? extends E> toInsert, E point) {
        this.innerChain.insertAfter((Collection<? extends Collection<? extends E>>) toInsert, (Collection<? extends E>) point);
    }

    public void insertBefore(List<E> toInsert, E point) {
        if (!toInsert.isEmpty()) {
            E previousPoint = point;
            ListIterator<E> it = toInsert.listIterator(toInsert.size());
            while (it.hasPrevious()) {
                E o = it.previous();
                insertBeforeNoRedirect(o, previousPoint);
                previousPoint = o;
            }
            point.redirectJumpsToThisTo(toInsert.get(0));
        }
    }

    public void insertBefore(Chain<E> toInsert, E point) {
        if (!toInsert.isEmpty()) {
            E previousPoint = point;
            E last = toInsert.getLast();
            while (true) {
                E o = last;
                if (o != null) {
                    insertBeforeNoRedirect(o, previousPoint);
                    previousPoint = o;
                    last = toInsert.getPredOf(o);
                } else {
                    point.redirectJumpsToThisTo(toInsert.getFirst());
                    return;
                }
            }
        }
    }

    @Override // soot.util.Chain
    public void insertBefore(E toInsert, E point) {
        point.redirectJumpsToThisTo(toInsert);
        this.innerChain.insertBefore(toInsert, point);
    }

    public void insertBefore(Collection<? extends E> toInsert, E point) {
        if (toInsert instanceof Chain) {
            Chain<E> temp = (Chain) toInsert;
            insertBefore((Chain<Chain<E>>) temp, (Chain<E>) point);
        } else if (toInsert instanceof List) {
            List<E> temp2 = (List) toInsert;
            insertBefore((List<List<E>>) temp2, (List<E>) point);
        } else {
            insertBefore((List<ArrayList>) new ArrayList(toInsert), (ArrayList) point);
        }
    }

    public void insertBeforeNoRedirect(E toInsert, E point) {
        this.innerChain.insertBefore(toInsert, point);
    }

    public void insertBeforeNoRedirect(List<E> toInsert, E point) {
        if (!toInsert.isEmpty()) {
            E previousPoint = point;
            ListIterator<E> it = toInsert.listIterator(toInsert.size());
            while (it.hasPrevious()) {
                E o = it.previous();
                insertBeforeNoRedirect(o, previousPoint);
                previousPoint = o;
            }
        }
    }

    @Override // soot.util.Chain
    public boolean follows(E a, E b) {
        return this.innerChain.follows(a, b);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, soot.util.Chain
    public boolean remove(Object obj) {
        if (contains(obj)) {
            Unit unit = (Unit) obj;
            patchBeforeRemoval(this.innerChain, unit);
            return this.innerChain.remove(unit);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static <E extends Unit> void patchBeforeRemoval(Chain<E> chain, E removing) {
        Unit successor = chain.getSuccOf(removing);
        if (successor == null) {
            successor = chain.getPredOf(removing);
        }
        removing.redirectJumpsToThisTo(successor);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean contains(Object u) {
        return this.innerChain.contains(u);
    }

    public void addFirst(E u) {
        this.innerChain.addFirst(u);
    }

    public void addLast(E u) {
        this.innerChain.addLast(u);
    }

    @Override // soot.util.Chain
    public void removeFirst() {
        remove(this.innerChain.getFirst());
    }

    @Override // soot.util.Chain
    public void removeLast() {
        remove(this.innerChain.getLast());
    }

    @Override // soot.util.Chain
    public E getFirst() {
        return this.innerChain.getFirst();
    }

    @Override // soot.util.Chain
    public E getLast() {
        return this.innerChain.getLast();
    }

    public E getSuccOf(E point) {
        return this.innerChain.getSuccOf(point);
    }

    public E getPredOf(E point) {
        return this.innerChain.getPredOf(point);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, soot.util.Chain
    public int size() {
        return this.innerChain.size();
    }

    @Override // soot.util.Chain
    public long getModificationCount() {
        return this.innerChain.getModificationCount();
    }

    @Override // soot.util.Chain
    public Collection<E> getElementsUnsorted() {
        return this.innerChain.getElementsUnsorted();
    }

    @Override // soot.util.Chain
    public Iterator<E> snapshotIterator() {
        return this.innerChain.snapshotIterator();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, soot.util.Chain
    public Iterator<E> iterator() {
        return new PatchingIterator(this.innerChain);
    }

    public Iterator<E> iterator(E u) {
        return new PatchingIterator(this.innerChain, u);
    }

    @Override // soot.util.Chain
    public Iterator<E> iterator(E head, E tail) {
        return new PatchingIterator(this.innerChain, head, tail);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:soot/PatchingChain$PatchingIterator.class */
    public class PatchingIterator implements Iterator<E> {
        protected final Chain<E> innerChain;
        protected final Iterator<E> innerIterator;
        protected E lastObject;
        protected boolean state = false;

        /* JADX INFO: Access modifiers changed from: protected */
        public PatchingIterator(Chain<E> innerChain) {
            this.innerChain = innerChain;
            this.innerIterator = innerChain.iterator();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public PatchingIterator(Chain<E> innerChain, E u) {
            this.innerChain = innerChain;
            this.innerIterator = innerChain.iterator(u);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public PatchingIterator(Chain<E> innerChain, E head, E tail) {
            this.innerChain = innerChain;
            this.innerIterator = innerChain.iterator(head, tail);
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.innerIterator.hasNext();
        }

        @Override // java.util.Iterator
        public E next() {
            this.lastObject = this.innerIterator.next();
            this.state = true;
            return this.lastObject;
        }

        @Override // java.util.Iterator
        public void remove() {
            if (!this.state) {
                throw new IllegalStateException("remove called before first next() call");
            }
            this.state = false;
            PatchingChain.patchBeforeRemoval(this.innerChain, this.lastObject);
            this.innerIterator.remove();
        }
    }
}
