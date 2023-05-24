package soot.jimple.toolkits.typing.fast;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/fast/QueuedSet.class */
public class QueuedSet<E> {
    private final Set<E> hs;
    private final LinkedList<E> ll;

    public QueuedSet() {
        this.hs = new HashSet();
        this.ll = new LinkedList<>();
    }

    public QueuedSet(List<E> os) {
        this();
        for (E o : os) {
            this.ll.addLast(o);
            this.hs.add(o);
        }
    }

    public QueuedSet(QueuedSet<E> qs) {
        this(qs.ll);
    }

    public boolean isEmpty() {
        return this.ll.isEmpty();
    }

    public boolean addLast(E o) {
        boolean r = this.hs.contains(o);
        if (!r) {
            this.ll.addLast(o);
            this.hs.add(o);
        }
        return r;
    }

    public int addLast(List<E> os) {
        int r = 0;
        for (E o : os) {
            if (addLast((QueuedSet<E>) o)) {
                r++;
            }
        }
        return r;
    }

    public E removeFirst() {
        E r = this.ll.removeFirst();
        this.hs.remove(r);
        return r;
    }
}
