package soot.jimple.spark.sets;

import soot.Type;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.util.BitVector;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/sets/SharedListSet.class */
public class SharedListSet extends PointsToSetInternal {
    private PAG pag;
    private ListNode data;

    public SharedListSet(Type type, PAG pag) {
        super(type);
        this.data = null;
        this.pag = pag;
    }

    public static final P2SetFactory getFactory() {
        return new P2SetFactory() { // from class: soot.jimple.spark.sets.SharedListSet.1
            @Override // soot.jimple.spark.sets.P2SetFactory
            public final PointsToSetInternal newSet(Type type, PAG pag) {
                return new SharedListSet(type, pag);
            }
        };
    }

    @Override // soot.jimple.spark.sets.PointsToSetInternal
    public boolean contains(Node n) {
        ListNode listNode = this.data;
        while (true) {
            ListNode i = listNode;
            if (i == null) {
                return false;
            }
            if (i.elem == n) {
                return true;
            }
            listNode = i.next;
        }
    }

    @Override // soot.PointsToSet
    public boolean isEmpty() {
        return this.data == null;
    }

    @Override // soot.jimple.spark.sets.PointsToSetInternal
    public boolean forall(P2SetVisitor v) {
        ListNode listNode = this.data;
        while (true) {
            ListNode i = listNode;
            if (i != null) {
                v.visit(i.elem);
                listNode = i.next;
            } else {
                return v.getReturnValue();
            }
        }
    }

    private ListNode advanceExclude(ListNode exclude, ListNode other) {
        int otherNum = other.elem.getNumber();
        while (exclude != null && exclude.elem.getNumber() < otherNum) {
            exclude = exclude.next;
        }
        return exclude;
    }

    private boolean excluded(ListNode exclude, ListNode other, BitVector mask) {
        if (exclude == null || other.elem != exclude.elem) {
            return (mask == null || mask.get(other.elem.getNumber())) ? false : true;
        }
        return true;
    }

    private ListNode union(ListNode first, ListNode other, ListNode exclude, BitVector mask, boolean detachChildren) {
        ListNode retVal;
        if (first == null) {
            if (other == null) {
                return null;
            }
            if (exclude == null && mask == null) {
                return makeNode(other.elem, other.next);
            }
            ListNode exclude2 = advanceExclude(exclude, other);
            if (excluded(exclude2, other, mask)) {
                return union(first, other.next, exclude2, mask, detachChildren);
            }
            return makeNode(other.elem, union(first, other.next, exclude2, mask, detachChildren));
        } else if (other == null) {
            return first;
        } else {
            if (first == other) {
                return first;
            }
            if (first.elem.getNumber() > other.elem.getNumber()) {
                ListNode exclude3 = advanceExclude(exclude, other);
                retVal = excluded(exclude3, other, mask) ? union(first, other.next, exclude3, mask, detachChildren) : makeNode(other.elem, union(first, other.next, exclude3, mask, detachChildren));
            } else {
                if (first.refCount > 1) {
                    detachChildren = false;
                }
                if (first.elem == other.elem) {
                    other = other.next;
                }
                retVal = makeNode(first.elem, union(first.next, other, exclude, mask, detachChildren));
                if (detachChildren && first != retVal && first.next != null) {
                    first.next.decRefCount();
                }
            }
            return retVal;
        }
    }

    private boolean addOrAddAll(ListNode first, ListNode other, ListNode exclude, BitVector mask) {
        ListNode result = union(first, other, exclude, mask, true);
        if (result == this.data) {
            return false;
        }
        result.incRefCount();
        if (this.data != null) {
            this.data.decRefCount();
        }
        this.data = result;
        return true;
    }

    @Override // soot.jimple.spark.sets.PointsToSetInternal
    public boolean add(Node n) {
        ListNode other = makeNode(n, null);
        other.incRefCount();
        boolean added = addOrAddAll(this.data, other, null, null);
        other.decRefCount();
        return added;
    }

    @Override // soot.jimple.spark.sets.PointsToSetInternal
    public boolean addAll(PointsToSetInternal other, PointsToSetInternal exclude) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof SharedListSet) || (exclude != null && !(exclude instanceof SharedListSet))) {
            return super.addAll(other, exclude);
        }
        SharedListSet realOther = (SharedListSet) other;
        SharedListSet realExclude = (SharedListSet) exclude;
        BitVector mask = getBitMask(realOther, this.pag);
        ListNode excludeData = realExclude == null ? null : realExclude.data;
        return addOrAddAll(this.data, realOther.data, excludeData, mask);
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/spark/sets/SharedListSet$Pair.class */
    public class Pair {
        public Node first;
        public ListNode second;

        public Pair(Node first, ListNode second) {
            this.first = first;
            this.second = second;
        }

        public int hashCode() {
            if (this.second == null) {
                return this.first.hashCode();
            }
            return this.first.hashCode() + this.second.hashCode();
        }

        public boolean equals(Object other) {
            if (!(other instanceof Pair)) {
                return false;
            }
            Pair o = (Pair) other;
            if ((this.first == null && o.first == null) || this.first == o.first) {
                return (this.second == null && o.second == null) || this.second == o.second;
            }
            return false;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/spark/sets/SharedListSet$ListNode.class */
    public class ListNode {
        private Node elem;
        private ListNode next;
        public long refCount = 0;

        public ListNode(Node elem, ListNode next) {
            this.next = null;
            this.elem = elem;
            this.next = next;
        }

        public void incRefCount() {
            this.refCount++;
        }

        public void decRefCount() {
            long j = this.refCount - 1;
            this.refCount = j;
            if (j == 0) {
                AllSharedListNodes.v().allNodes.remove(new Pair(this.elem, this.next));
            }
        }
    }

    private ListNode makeNode(Node elem, ListNode next) {
        Pair p = new Pair(elem, next);
        ListNode retVal = AllSharedListNodes.v().allNodes.get(p);
        if (retVal == null) {
            retVal = new ListNode(elem, next);
            if (next != null) {
                next.incRefCount();
            }
            AllSharedListNodes.v().allNodes.put(p, retVal);
        }
        return retVal;
    }
}
