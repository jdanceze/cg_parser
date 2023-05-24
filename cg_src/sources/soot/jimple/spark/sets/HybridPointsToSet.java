package soot.jimple.spark.sets;

import soot.Scene;
import soot.Type;
import soot.jimple.spark.internal.TypeManager;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.util.BitSetIterator;
import soot.util.BitVector;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/sets/HybridPointsToSet.class */
public class HybridPointsToSet extends PointsToSetInternal {
    private static P2SetFactory<HybridPointsToSet> HYBRID_PTS_FACTORY = new P2SetFactory<HybridPointsToSet>() { // from class: soot.jimple.spark.sets.HybridPointsToSet.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // soot.jimple.spark.sets.P2SetFactory
        public final HybridPointsToSet newSet(Type type, PAG pag) {
            return new HybridPointsToSet(type, pag);
        }
    };
    protected Node[] nodes;
    protected BitVector bits;
    protected PAG pag;
    protected boolean empty;

    public HybridPointsToSet(Type type, PAG pag) {
        super(type);
        this.nodes = new Node[16];
        this.bits = null;
        this.empty = true;
        this.pag = pag;
    }

    public static void setPointsToSetFactory(P2SetFactory<HybridPointsToSet> factory) {
        HYBRID_PTS_FACTORY = factory;
    }

    @Override // soot.PointsToSet
    public final boolean isEmpty() {
        return this.empty;
    }

    private boolean superAddAll(PointsToSetInternal other, PointsToSetInternal exclude) {
        boolean ret = super.addAll(other, exclude);
        if (ret) {
            this.empty = false;
        }
        return ret;
    }

    private boolean nativeAddAll(HybridPointsToSet other, HybridPointsToSet exclude) {
        boolean ret = false;
        TypeManager typeManager = this.pag.getTypeManager();
        if (other.bits != null) {
            convertToBits();
            if (exclude != null) {
                exclude.convertToBits();
            }
            BitVector mask = null;
            if (!typeManager.castNeverFails(other.getType(), getType())) {
                mask = typeManager.get(getType());
            }
            BitVector ebits = exclude == null ? null : exclude.bits;
            ret = this.bits.orAndAndNot(other.bits, mask, ebits);
        } else {
            for (int i = 0; i < this.nodes.length && other.nodes[i] != null; i++) {
                if (exclude == null || !exclude.contains(other.nodes[i])) {
                    ret = add(other.nodes[i]) | ret;
                }
            }
        }
        if (ret) {
            this.empty = false;
        }
        return ret;
    }

    @Override // soot.jimple.spark.sets.PointsToSetInternal
    public boolean addAll(PointsToSetInternal other, PointsToSetInternal exclude) {
        if ((other != null && !(other instanceof HybridPointsToSet)) || (exclude != null && !(exclude instanceof HybridPointsToSet))) {
            return superAddAll(other, exclude);
        }
        return nativeAddAll((HybridPointsToSet) other, (HybridPointsToSet) exclude);
    }

    @Override // soot.jimple.spark.sets.PointsToSetInternal
    public boolean forall(P2SetVisitor v) {
        Node[] nodeArr;
        if (this.bits == null) {
            for (Node node : this.nodes) {
                if (node == null) {
                    return v.getReturnValue();
                }
                v.visit(node);
            }
        } else {
            BitSetIterator it = this.bits.iterator();
            while (it.hasNext()) {
                v.visit(this.pag.getAllocNodeNumberer().get(it.next()));
            }
        }
        return v.getReturnValue();
    }

    @Override // soot.jimple.spark.sets.PointsToSetInternal
    public boolean add(Node n) {
        if (this.pag.getTypeManager().castNeverFails(n.getType(), this.type)) {
            return fastAdd(n);
        }
        return false;
    }

    @Override // soot.jimple.spark.sets.PointsToSetInternal
    public boolean contains(Node n) {
        Node[] nodeArr;
        if (this.bits == null) {
            for (Node node : this.nodes) {
                if (node == n) {
                    return true;
                }
                if (node == null) {
                    return false;
                }
            }
            return false;
        }
        return this.bits.get(n.getNumber());
    }

    public static P2SetFactory<HybridPointsToSet> getFactory() {
        return HYBRID_PTS_FACTORY;
    }

    protected boolean fastAdd(Node n) {
        if (this.bits == null) {
            for (int i = 0; i < this.nodes.length; i++) {
                if (this.nodes[i] == null) {
                    this.empty = false;
                    this.nodes[i] = n;
                    return true;
                } else if (this.nodes[i] == n) {
                    return false;
                }
            }
            convertToBits();
        }
        boolean ret = this.bits.set(n.getNumber());
        if (ret) {
            this.empty = false;
        }
        return ret;
    }

    protected void convertToBits() {
        Node[] nodeArr;
        if (this.bits != null) {
            return;
        }
        this.bits = new BitVector(this.pag.getAllocNodeNumberer().size());
        for (Node node : this.nodes) {
            if (node != null) {
                fastAdd(node);
            }
        }
    }

    public static HybridPointsToSet intersection(HybridPointsToSet set1, HybridPointsToSet set2, PAG pag) {
        final HybridPointsToSet ret = getFactory().newSet(Scene.v().getObjectType(), pag);
        BitVector s1Bits = set1.bits;
        BitVector s2Bits = set2.bits;
        if (s1Bits == null || s2Bits == null) {
            if (s1Bits != null) {
                set2.forall(new P2SetVisitor() { // from class: soot.jimple.spark.sets.HybridPointsToSet.2
                    @Override // soot.jimple.spark.sets.P2SetVisitor
                    public void visit(Node n) {
                        if (HybridPointsToSet.this.contains(n)) {
                            ret.add(n);
                        }
                    }
                });
            } else {
                set1.forall(new P2SetVisitor() { // from class: soot.jimple.spark.sets.HybridPointsToSet.3
                    @Override // soot.jimple.spark.sets.P2SetVisitor
                    public void visit(Node n) {
                        if (HybridPointsToSet.this.contains(n)) {
                            ret.add(n);
                        }
                    }
                });
            }
        } else {
            ret.bits = BitVector.and(s1Bits, s2Bits);
            ret.empty = false;
        }
        return ret;
    }
}
