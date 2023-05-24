package soot.coffi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
/* loaded from: gencallgraphv3.jar:soot/coffi/BBQ.class */
final class BBQ {
    private final ArrayList<BasicBlock> q = new ArrayList<>();

    public void push(BasicBlock b) {
        if (!b.inq) {
            b.inq = true;
            this.q.add(b);
        }
    }

    public BasicBlock pull() throws NoSuchElementException {
        if (this.q.size() == 0) {
            throw new NoSuchElementException("Pull from empty BBQ");
        }
        BasicBlock b = this.q.get(0);
        this.q.remove(0);
        b.inq = false;
        return b;
    }

    public boolean contains(BasicBlock b) {
        return b.inq;
    }

    public int size() {
        return this.q.size();
    }

    public boolean isEmpty() {
        return this.q.isEmpty();
    }

    public void clear() {
        Iterator<BasicBlock> it = this.q.iterator();
        while (it.hasNext()) {
            BasicBlock basicBlock = it.next();
            basicBlock.inq = false;
        }
        this.q.clear();
    }
}
