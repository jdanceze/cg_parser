package soot.jimple.spark.sets;

import soot.Type;
import soot.jimple.spark.internal.TypeManager;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.util.BitSetIterator;
import soot.util.BitVector;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/sets/BitPointsToSet.class */
public final class BitPointsToSet extends PointsToSetInternal {
    private BitVector bits;
    private boolean empty;
    private PAG pag;

    public BitPointsToSet(Type type, PAG pag) {
        super(type);
        this.bits = null;
        this.empty = true;
        this.pag = null;
        this.pag = pag;
        this.bits = new BitVector(pag.getAllocNodeNumberer().size());
    }

    @Override // soot.PointsToSet
    public final boolean isEmpty() {
        return this.empty;
    }

    private final boolean superAddAll(PointsToSetInternal other, PointsToSetInternal exclude) {
        boolean ret = super.addAll(other, exclude);
        if (ret) {
            this.empty = false;
        }
        return ret;
    }

    private final boolean nativeAddAll(BitPointsToSet other, BitPointsToSet exclude) {
        BitVector mask = null;
        TypeManager typeManager = this.pag.getTypeManager();
        if (!typeManager.castNeverFails(other.getType(), getType())) {
            mask = typeManager.get(getType());
        }
        BitVector obits = other == null ? null : other.bits;
        BitVector ebits = exclude == null ? null : exclude.bits;
        boolean ret = this.bits.orAndAndNot(obits, mask, ebits);
        if (ret) {
            this.empty = false;
        }
        return ret;
    }

    @Override // soot.jimple.spark.sets.PointsToSetInternal
    public final boolean addAll(PointsToSetInternal other, PointsToSetInternal exclude) {
        if ((other != null && !(other instanceof BitPointsToSet)) || (exclude != null && !(exclude instanceof BitPointsToSet))) {
            return superAddAll(other, exclude);
        }
        return nativeAddAll((BitPointsToSet) other, (BitPointsToSet) exclude);
    }

    @Override // soot.jimple.spark.sets.PointsToSetInternal
    public final boolean forall(P2SetVisitor v) {
        BitSetIterator it = this.bits.iterator();
        while (it.hasNext()) {
            v.visit(this.pag.getAllocNodeNumberer().get(it.next()));
        }
        return v.getReturnValue();
    }

    @Override // soot.jimple.spark.sets.PointsToSetInternal
    public final boolean add(Node n) {
        if (this.pag.getTypeManager().castNeverFails(n.getType(), this.type)) {
            return fastAdd(n);
        }
        return false;
    }

    @Override // soot.jimple.spark.sets.PointsToSetInternal
    public final boolean contains(Node n) {
        return this.bits.get(n.getNumber());
    }

    public static P2SetFactory getFactory() {
        return new P2SetFactory() { // from class: soot.jimple.spark.sets.BitPointsToSet.1
            @Override // soot.jimple.spark.sets.P2SetFactory
            public PointsToSetInternal newSet(Type type, PAG pag) {
                return new BitPointsToSet(type, pag);
            }
        };
    }

    private boolean fastAdd(Node n) {
        boolean ret = this.bits.set(n.getNumber());
        if (ret) {
            this.empty = false;
        }
        return ret;
    }
}
