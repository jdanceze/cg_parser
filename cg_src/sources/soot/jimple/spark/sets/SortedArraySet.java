package soot.jimple.spark.sets;

import soot.Type;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.util.BitVector;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/sets/SortedArraySet.class */
public final class SortedArraySet extends PointsToSetInternal {
    private Node[] nodes;
    private int size;
    private PAG pag;

    public SortedArraySet(Type type, PAG pag) {
        super(type);
        this.nodes = null;
        this.size = 0;
        this.pag = null;
        this.pag = pag;
    }

    @Override // soot.PointsToSet
    public final boolean isEmpty() {
        return this.size == 0;
    }

    @Override // soot.jimple.spark.sets.PointsToSetInternal
    public final boolean addAll(PointsToSetInternal other, PointsToSetInternal exclude) {
        boolean ret = false;
        BitVector typeMask = this.pag.getTypeManager().get(this.type);
        if (other instanceof SortedArraySet) {
            SortedArraySet o = (SortedArraySet) other;
            Node[] mya = this.nodes;
            Node[] oa = o.nodes;
            int osize = o.size;
            Node[] newa = new Node[this.size + osize];
            int myi = 0;
            int oi = 0;
            int newi = 0;
            while (true) {
                if (myi < this.size) {
                    if (oi < osize) {
                        int myhc = mya[myi].getNumber();
                        int ohc = oa[oi].getNumber();
                        if (myhc < ohc) {
                            int i = newi;
                            newi++;
                            int i2 = myi;
                            myi++;
                            newa[i] = mya[i2];
                        } else if (myhc > ohc) {
                            if ((this.type == null || typeMask == null || typeMask.get(ohc)) && (exclude == null || !exclude.contains(oa[oi]))) {
                                int i3 = newi;
                                newi++;
                                newa[i3] = oa[oi];
                                ret = true;
                            }
                            oi++;
                        } else {
                            int i4 = newi;
                            newi++;
                            int i5 = myi;
                            myi++;
                            newa[i4] = mya[i5];
                            oi++;
                        }
                    } else {
                        int i6 = newi;
                        newi++;
                        int i7 = myi;
                        myi++;
                        newa[i6] = mya[i7];
                    }
                } else if (oi < osize) {
                    int ohc2 = oa[oi].getNumber();
                    if ((this.type == null || typeMask == null || typeMask.get(ohc2)) && (exclude == null || !exclude.contains(oa[oi]))) {
                        int i8 = newi;
                        newi++;
                        newa[i8] = oa[oi];
                        ret = true;
                    }
                    oi++;
                } else {
                    this.nodes = newa;
                    this.size = newi;
                    return ret;
                }
            }
        } else {
            return super.addAll(other, exclude);
        }
    }

    @Override // soot.jimple.spark.sets.PointsToSetInternal
    public final boolean forall(P2SetVisitor v) {
        for (int i = 0; i < this.size; i++) {
            v.visit(this.nodes[i]);
        }
        return v.getReturnValue();
    }

    @Override // soot.jimple.spark.sets.PointsToSetInternal
    public final boolean add(Node n) {
        if (!this.pag.getTypeManager().castNeverFails(n.getType(), this.type) || contains(n)) {
            return false;
        }
        int left = 0;
        int right = this.size;
        int hc = n.getNumber();
        while (left < right) {
            int mid = (left + right) / 2;
            int midhc = this.nodes[mid].getNumber();
            if (midhc >= hc) {
                if (midhc <= hc) {
                    break;
                }
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        if (this.nodes == null) {
            this.nodes = new Node[this.size + 4];
        } else if (this.size == this.nodes.length) {
            Node[] newNodes = new Node[this.size + 4];
            System.arraycopy(this.nodes, 0, newNodes, 0, this.nodes.length);
            this.nodes = newNodes;
        }
        System.arraycopy(this.nodes, left, this.nodes, left + 1, this.size - left);
        this.nodes[left] = n;
        this.size++;
        return true;
    }

    @Override // soot.jimple.spark.sets.PointsToSetInternal
    public final boolean contains(Node n) {
        int left = 0;
        int right = this.size;
        int hc = n.getNumber();
        while (left < right) {
            int mid = (left + right) / 2;
            int midhc = this.nodes[mid].getNumber();
            if (midhc < hc) {
                left = mid + 1;
            } else if (midhc > hc) {
                right = mid;
            } else {
                return true;
            }
        }
        return false;
    }

    public static final P2SetFactory getFactory() {
        return new P2SetFactory() { // from class: soot.jimple.spark.sets.SortedArraySet.1
            @Override // soot.jimple.spark.sets.P2SetFactory
            public final PointsToSetInternal newSet(Type type, PAG pag) {
                return new SortedArraySet(type, pag);
            }
        };
    }
}
