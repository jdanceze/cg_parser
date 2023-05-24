package soot.jimple.spark.sets;

import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.G;
import soot.PointsToSet;
import soot.RefType;
import soot.Type;
import soot.jimple.ClassConstant;
import soot.jimple.spark.internal.TypeManager;
import soot.jimple.spark.pag.ClassConstantNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.pag.StringConstantNode;
import soot.jimple.toolkits.pointer.FullObjectSet;
import soot.util.BitVector;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/sets/PointsToSetInternal.class */
public abstract class PointsToSetInternal implements PointsToSet, EqualsSupportingPointsToSet {
    private static final Logger logger = LoggerFactory.getLogger(PointsToSetInternal.class);
    protected Type type;

    public abstract boolean forall(P2SetVisitor p2SetVisitor);

    public abstract boolean add(Node node);

    public abstract boolean contains(Node node);

    public boolean addAll(PointsToSetInternal other, final PointsToSetInternal exclude) {
        if (other instanceof DoublePointsToSet) {
            return addAll(other.getNewSet(), exclude) | addAll(other.getOldSet(), exclude);
        }
        if (other instanceof EmptyPointsToSet) {
            return false;
        }
        if (exclude instanceof EmptyPointsToSet) {
            return addAll(other, null);
        }
        if (!G.v().PointsToSetInternal_warnedAlready) {
            logger.warn("using default implementation of addAll. You should implement a faster specialized implementation.");
            logger.debug("this is of type " + getClass().getName());
            logger.debug("other is of type " + other.getClass().getName());
            if (exclude == null) {
                logger.debug("exclude is null");
            } else {
                logger.debug("exclude is of type " + exclude.getClass().getName());
            }
            G.v().PointsToSetInternal_warnedAlready = true;
        }
        return other.forall(new P2SetVisitor() { // from class: soot.jimple.spark.sets.PointsToSetInternal.1
            @Override // soot.jimple.spark.sets.P2SetVisitor
            public final void visit(Node n) {
                if (exclude == null || !exclude.contains(n)) {
                    this.returnValue = PointsToSetInternal.this.add(n) | this.returnValue;
                }
            }
        });
    }

    public PointsToSetInternal getNewSet() {
        return this;
    }

    public PointsToSetInternal getOldSet() {
        return EmptyPointsToSet.v();
    }

    public void flushNew() {
    }

    public void unFlushNew() {
    }

    public void mergeWith(PointsToSetInternal other) {
        addAll(other, null);
    }

    public PointsToSetInternal(Type type) {
        this.type = type;
    }

    @Override // soot.PointsToSet
    public boolean hasNonEmptyIntersection(PointsToSet other) {
        if (other instanceof PointsToSetInternal) {
            final PointsToSetInternal o = (PointsToSetInternal) other;
            return forall(new P2SetVisitor() { // from class: soot.jimple.spark.sets.PointsToSetInternal.2
                @Override // soot.jimple.spark.sets.P2SetVisitor
                public void visit(Node n) {
                    if (o.contains(n)) {
                        this.returnValue = true;
                    }
                }
            });
        } else if (other instanceof FullObjectSet) {
            FullObjectSet fos = (FullObjectSet) other;
            return fos.possibleTypes().contains(this.type);
        } else {
            return false;
        }
    }

    @Override // soot.PointsToSet
    public Set<Type> possibleTypes() {
        final HashSet<Type> ret = new HashSet<>();
        forall(new P2SetVisitor() { // from class: soot.jimple.spark.sets.PointsToSetInternal.3
            @Override // soot.jimple.spark.sets.P2SetVisitor
            public void visit(Node n) {
                Type t = n.getType();
                if (t instanceof RefType) {
                    RefType rt = (RefType) t;
                    if (rt.getSootClass().isAbstract()) {
                        return;
                    }
                }
                ret.add(t);
            }
        });
        return ret;
    }

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int size() {
        final int[] ret = new int[1];
        forall(new P2SetVisitor() { // from class: soot.jimple.spark.sets.PointsToSetInternal.4
            @Override // soot.jimple.spark.sets.P2SetVisitor
            public void visit(Node n) {
                int[] iArr = ret;
                iArr[0] = iArr[0] + 1;
            }
        });
        return ret[0];
    }

    public String toString() {
        final StringBuffer ret = new StringBuffer();
        forall(new P2SetVisitor() { // from class: soot.jimple.spark.sets.PointsToSetInternal.5
            @Override // soot.jimple.spark.sets.P2SetVisitor
            public final void visit(Node n) {
                ret.append(n + ",");
            }
        });
        return ret.toString();
    }

    @Override // soot.PointsToSet
    public Set<String> possibleStringConstants() {
        final HashSet<String> ret = new HashSet<>();
        if (forall(new P2SetVisitor() { // from class: soot.jimple.spark.sets.PointsToSetInternal.6
            @Override // soot.jimple.spark.sets.P2SetVisitor
            public final void visit(Node n) {
                if (n instanceof StringConstantNode) {
                    ret.add(((StringConstantNode) n).getString());
                } else {
                    this.returnValue = true;
                }
            }
        })) {
            return null;
        }
        return ret;
    }

    @Override // soot.PointsToSet
    public Set<ClassConstant> possibleClassConstants() {
        final HashSet<ClassConstant> ret = new HashSet<>();
        if (forall(new P2SetVisitor() { // from class: soot.jimple.spark.sets.PointsToSetInternal.7
            @Override // soot.jimple.spark.sets.P2SetVisitor
            public final void visit(Node n) {
                if (n instanceof ClassConstantNode) {
                    ret.add(((ClassConstantNode) n).getClassConstant());
                } else {
                    this.returnValue = true;
                }
            }
        })) {
            return null;
        }
        return ret;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public BitVector getBitMask(PointsToSetInternal other, PAG pag) {
        BitVector mask = null;
        TypeManager typeManager = pag.getTypeManager();
        if (!typeManager.castNeverFails(other.getType(), getType())) {
            mask = typeManager.get(getType());
        }
        return mask;
    }

    @Override // soot.jimple.spark.sets.EqualsSupportingPointsToSet
    public int pointsToSetHashCode() {
        P2SetVisitorInt visitor = new P2SetVisitorInt(1) { // from class: soot.jimple.spark.sets.PointsToSetInternal.8
            final int PRIME = 31;

            @Override // soot.jimple.spark.sets.P2SetVisitor
            public void visit(Node n) {
                this.intValue = (31 * this.intValue) + n.hashCode();
            }
        };
        forall(visitor);
        return visitor.intValue;
    }

    @Override // soot.jimple.spark.sets.EqualsSupportingPointsToSet
    public boolean pointsToSetEquals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof PointsToSetInternal)) {
            return false;
        }
        PointsToSetInternal otherPts = (PointsToSetInternal) other;
        return superSetOf(otherPts, this) && superSetOf(this, otherPts);
    }

    private boolean superSetOf(PointsToSetInternal onePts, final PointsToSetInternal otherPts) {
        return onePts.forall(new P2SetVisitorDefaultTrue() { // from class: soot.jimple.spark.sets.PointsToSetInternal.9
            @Override // soot.jimple.spark.sets.P2SetVisitor
            public final void visit(Node n) {
                this.returnValue = this.returnValue && otherPts.contains(n);
            }
        });
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/spark/sets/PointsToSetInternal$P2SetVisitorDefaultTrue.class */
    public static abstract class P2SetVisitorDefaultTrue extends P2SetVisitor {
        public P2SetVisitorDefaultTrue() {
            this.returnValue = true;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/spark/sets/PointsToSetInternal$P2SetVisitorInt.class */
    public static abstract class P2SetVisitorInt extends P2SetVisitor {
        protected int intValue = 1;

        public P2SetVisitorInt(int i) {
        }
    }
}
