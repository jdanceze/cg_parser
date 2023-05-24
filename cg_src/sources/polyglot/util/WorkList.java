package polyglot.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/util/WorkList.class */
public class WorkList {
    LinkedList pending = new LinkedList();
    HashMap results = new HashMap();
    int size = 0;
    public static final Object NOT_CALCULATED = new Object();

    public void addWork(Object o) {
        if (!this.results.containsKey(o)) {
            this.results.put(o, NOT_CALCULATED);
            this.pending.addLast(o);
            this.size++;
        }
    }

    public void addWork(Collection c) {
        for (Object obj : c) {
            addWork(obj);
        }
    }

    public boolean finished() {
        return this.size == 0;
    }

    public Object getWork() {
        if (this.size > 0) {
            return this.pending.getFirst();
        }
        throw new NoSuchElementException("WorkList.getWork");
    }

    public void finishWork(Object work, Object result) {
        if (this.results.get(work) == NOT_CALCULATED) {
            ListIterator i = this.pending.listIterator();
            while (i.hasNext()) {
                if (i.next().equals(work)) {
                    i.remove();
                }
            }
        }
        this.results.put(work, result);
    }

    public void finishWork(Object work) {
        finishWork(work, null);
    }

    public boolean isFinished(Object work) {
        return this.results.containsKey(work) && this.results.get(work) != NOT_CALCULATED;
    }

    public Map getMap() {
        return Collections.unmodifiableMap(this.results);
    }
}
