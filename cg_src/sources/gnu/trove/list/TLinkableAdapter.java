package gnu.trove.list;

import gnu.trove.list.TLinkable;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/list/TLinkableAdapter.class */
public abstract class TLinkableAdapter<T extends TLinkable> implements TLinkable<T> {
    private volatile T next;
    private volatile T prev;

    @Override // gnu.trove.list.TLinkable
    public T getNext() {
        return this.next;
    }

    @Override // gnu.trove.list.TLinkable
    public void setNext(T next) {
        this.next = next;
    }

    @Override // gnu.trove.list.TLinkable
    public T getPrevious() {
        return this.prev;
    }

    @Override // gnu.trove.list.TLinkable
    public void setPrevious(T prev) {
        this.prev = prev;
    }
}
