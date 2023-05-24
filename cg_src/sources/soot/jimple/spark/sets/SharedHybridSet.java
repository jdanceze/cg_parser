package soot.jimple.spark.sets;

import java.util.List;
import soot.Type;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.util.BitSetIterator;
import soot.util.BitVector;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/sets/SharedHybridSet.class */
public class SharedHybridSet extends PointsToSetInternal {
    public static final int OVERFLOW_SIZE = 16;
    public static final int OVERFLOW_THRESHOLD = 5;
    private PointsToBitVector bitVector;
    private OverflowList overflow;
    private PAG pag;
    private int numElements;

    public SharedHybridSet(Type type, PAG pag) {
        super(type);
        this.bitVector = null;
        this.overflow = new OverflowList();
        this.numElements = 0;
        this.pag = pag;
    }

    @Override // soot.jimple.spark.sets.PointsToSetInternal
    public boolean contains(Node n) {
        if ((this.bitVector != null && this.bitVector.contains(n)) || this.overflow.contains(n)) {
            return true;
        }
        return false;
    }

    @Override // soot.PointsToSet
    public boolean isEmpty() {
        return this.numElements == 0;
    }

    private OverflowList remainder(PointsToBitVector a, PointsToBitVector b) {
        PointsToBitVector xorResult = new PointsToBitVector(a);
        xorResult.xor(b);
        return new OverflowList(xorResult);
    }

    private void findAppropriateBitVector(PointsToBitVector newBitVector, PointsToBitVector otherBitVector, int otherSize, int szBitvector) {
        int bitVectorCardinality;
        if (otherBitVector != null && otherSize <= this.numElements && otherSize + 5 >= this.numElements && otherBitVector.isSubsetOf(newBitVector)) {
            setNewBitVector(szBitvector, otherBitVector);
            this.overflow = remainder(newBitVector, otherBitVector);
        } else if (this.bitVector != null && szBitvector <= this.numElements && szBitvector + 5 >= this.numElements && this.bitVector.isSubsetOf(newBitVector)) {
            this.overflow = remainder(newBitVector, this.bitVector);
        } else {
            for (int overFlowSize = 0; overFlowSize < 5 && (bitVectorCardinality = this.numElements - overFlowSize) >= 0; overFlowSize++) {
                if (bitVectorCardinality < AllSharedHybridNodes.v().lookupMap.map.length && AllSharedHybridNodes.v().lookupMap.map[bitVectorCardinality] != null) {
                    List<PointsToBitVector> lst = AllSharedHybridNodes.v().lookupMap.map[bitVectorCardinality];
                    for (PointsToBitVector candidate : lst) {
                        if (candidate.isSubsetOf(newBitVector)) {
                            setNewBitVector(szBitvector, candidate);
                            this.overflow = remainder(newBitVector, candidate);
                            return;
                        }
                    }
                    continue;
                }
            }
            setNewBitVector(szBitvector, newBitVector);
            this.overflow.removeAll();
            AllSharedHybridNodes.v().lookupMap.add(this.numElements, newBitVector);
        }
    }

    private void setNewBitVector(int size, PointsToBitVector newBitVector) {
        newBitVector.incRefCount();
        if (this.bitVector != null) {
            this.bitVector.decRefCount();
            if (this.bitVector.unused()) {
                AllSharedHybridNodes.v().lookupMap.remove(size, this.bitVector);
            }
        }
        this.bitVector = newBitVector;
    }

    @Override // soot.jimple.spark.sets.PointsToSetInternal
    public boolean add(Node n) {
        PointsToBitVector newBitVector;
        if (contains(n)) {
            return false;
        }
        this.numElements++;
        if (!this.overflow.full()) {
            this.overflow.add(n);
            return true;
        }
        if (this.bitVector == null) {
            newBitVector = new PointsToBitVector(this.pag.getAllocNodeNumberer().size());
        } else {
            newBitVector = new PointsToBitVector(this.bitVector);
        }
        newBitVector.add(n);
        add(newBitVector, this.overflow);
        findAppropriateBitVector(newBitVector, null, 0, (this.numElements - this.overflow.size()) - 1);
        return true;
    }

    private boolean nativeAddAll(SharedHybridSet other, SharedHybridSet exclude) {
        PointsToBitVector newBitVector;
        BitVector mask = getBitMask(other, this.pag);
        if (exclude != null) {
            if (exclude.overflow.size() > 0) {
                if (exclude.bitVector == null) {
                    newBitVector = new PointsToBitVector(this.pag.getAllocNodeNumberer().size());
                } else {
                    newBitVector = new PointsToBitVector(exclude.bitVector);
                }
                add(newBitVector, exclude.overflow);
                exclude = new SharedHybridSet(this.type, this.pag);
                exclude.bitVector = newBitVector;
            } else if (exclude.bitVector == null) {
                exclude = null;
            }
        }
        int originalSize = size();
        int originalOnes = originalSize - this.overflow.size();
        int otherBitVectorSize = other.size() - other.overflow.size();
        if (this.bitVector == null) {
            this.bitVector = other.bitVector;
            if (this.bitVector != null) {
                this.bitVector.incRefCount();
                OverflowList toReAdd = this.overflow;
                this.overflow = new OverflowList();
                boolean newBitVectorCreated = false;
                this.numElements = otherBitVectorSize;
                if (exclude != null || mask != null) {
                    PointsToBitVector result = new PointsToBitVector(this.bitVector);
                    if (exclude != null) {
                        result.andNot(exclude.bitVector);
                    }
                    if (mask != null) {
                        result.and(mask);
                    }
                    if (!result.equals(this.bitVector)) {
                        add(result, toReAdd);
                        int newBitVectorSize = result.cardinality();
                        this.numElements = newBitVectorSize;
                        findAppropriateBitVector(result, other.bitVector, otherBitVectorSize, otherBitVectorSize);
                        newBitVectorCreated = true;
                    }
                }
                if (!newBitVectorCreated) {
                    OverflowList.ListNode listNode = toReAdd.overflow;
                    while (true) {
                        OverflowList.ListNode i = listNode;
                        if (i == null) {
                            break;
                        }
                        add(i.elem);
                        listNode = i.next;
                    }
                }
            }
        } else if (other.bitVector != null) {
            PointsToBitVector newBitVector2 = new PointsToBitVector(other.bitVector);
            if (exclude != null) {
                newBitVector2.andNot(exclude.bitVector);
            }
            if (mask != null) {
                newBitVector2.and(mask);
            }
            newBitVector2.or(this.bitVector);
            if (!newBitVector2.equals(this.bitVector)) {
                if (other.overflow.size() != 0) {
                    PointsToBitVector toAdd = new PointsToBitVector(newBitVector2.size());
                    add(toAdd, other.overflow);
                    if (mask != null) {
                        toAdd.and(mask);
                    }
                    if (exclude != null) {
                        toAdd.andNot(exclude.bitVector);
                    }
                    newBitVector2.or(toAdd);
                }
                int numOnes = newBitVector2.cardinality();
                int numAdded = add(newBitVector2, this.overflow);
                this.numElements += ((numOnes - originalOnes) + numAdded) - this.overflow.size();
                if (size() > originalSize) {
                    findAppropriateBitVector(newBitVector2, other.bitVector, otherBitVectorSize, originalOnes);
                    return true;
                }
                return false;
            }
        }
        OverflowList overflow = other.overflow;
        OverflowList.ListNode listNode2 = overflow.overflow;
        while (true) {
            OverflowList.ListNode i2 = listNode2;
            if (i2 == null) {
                break;
            }
            Node nodeToMaybeAdd = i2.elem;
            if ((exclude == null || !exclude.contains(nodeToMaybeAdd)) && (mask == null || mask.get(nodeToMaybeAdd.getNumber()))) {
                add(nodeToMaybeAdd);
            }
            listNode2 = i2.next;
        }
        return size() > originalSize;
    }

    private int add(PointsToBitVector p, OverflowList arr) {
        int retVal = 0;
        OverflowList.ListNode listNode = arr.overflow;
        while (true) {
            OverflowList.ListNode i = listNode;
            if (i != null) {
                if (p.add(i.elem)) {
                    retVal++;
                }
                listNode = i.next;
            } else {
                return retVal;
            }
        }
    }

    @Override // soot.jimple.spark.sets.PointsToSetInternal
    public boolean addAll(PointsToSetInternal other, PointsToSetInternal exclude) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof SharedHybridSet) || (exclude != null && !(exclude instanceof SharedHybridSet))) {
            return super.addAll(other, exclude);
        }
        return nativeAddAll((SharedHybridSet) other, (SharedHybridSet) exclude);
    }

    @Override // soot.jimple.spark.sets.PointsToSetInternal
    public boolean forall(P2SetVisitor v) {
        if (this.bitVector != null) {
            BitSetIterator it = this.bitVector.iterator();
            while (it.hasNext()) {
                v.visit(this.pag.getAllocNodeNumberer().get(it.next()));
            }
        }
        OverflowList.ListNode listNode = this.overflow.overflow;
        while (true) {
            OverflowList.ListNode i = listNode;
            if (i != null) {
                v.visit(i.elem);
                listNode = i.next;
            } else {
                return v.getReturnValue();
            }
        }
    }

    public static final P2SetFactory getFactory() {
        return new P2SetFactory() { // from class: soot.jimple.spark.sets.SharedHybridSet.1
            @Override // soot.jimple.spark.sets.P2SetFactory
            public final PointsToSetInternal newSet(Type type, PAG pag) {
                return new SharedHybridSet(type, pag);
            }
        };
    }

    @Override // soot.jimple.spark.sets.PointsToSetInternal
    public int size() {
        return this.numElements;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/jimple/spark/sets/SharedHybridSet$OverflowList.class */
    public class OverflowList {
        public ListNode overflow = null;
        private int overflowElements = 0;

        /* loaded from: gencallgraphv3.jar:soot/jimple/spark/sets/SharedHybridSet$OverflowList$ListNode.class */
        public class ListNode {
            public Node elem;
            public ListNode next;

            public ListNode(Node elem, ListNode next) {
                this.elem = elem;
                this.next = next;
            }
        }

        public OverflowList() {
        }

        public OverflowList(PointsToBitVector bv) {
            BitSetIterator it = bv.iterator();
            while (it.hasNext()) {
                Node n = SharedHybridSet.this.pag.getAllocNodeNumberer().get(it.next());
                add(n);
            }
        }

        public void add(Node n) {
            if (full()) {
                throw new RuntimeException("Can't add an element to a full overflow list.");
            }
            this.overflow = new ListNode(n, this.overflow);
            this.overflowElements++;
        }

        public int size() {
            return this.overflowElements;
        }

        public boolean full() {
            return this.overflowElements == 16;
        }

        public boolean contains(Node n) {
            ListNode listNode = this.overflow;
            while (true) {
                ListNode l = listNode;
                if (l != null) {
                    if (n != l.elem) {
                        listNode = l.next;
                    } else {
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }

        public void removeAll() {
            this.overflow = null;
            this.overflowElements = 0;
        }
    }
}
