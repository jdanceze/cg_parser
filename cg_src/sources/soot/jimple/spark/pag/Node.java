package soot.jimple.spark.pag;

import soot.Type;
import soot.jimple.spark.internal.TypeManager;
import soot.jimple.spark.sets.EmptyPointsToSet;
import soot.jimple.spark.sets.PointsToSetInternal;
import soot.jimple.toolkits.pointer.representations.ReferenceVariable;
import soot.options.Options;
import soot.util.Numberable;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/pag/Node.class */
public class Node implements ReferenceVariable, Numberable {
    private int number = 0;
    protected Type type;
    protected Node replacement;
    protected PAG pag;
    protected PointsToSetInternal p2set;

    public final int hashCode() {
        return this.number;
    }

    public final boolean equals(Object other) {
        return this == other;
    }

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        if (TypeManager.isUnresolved(type) && !Options.v().ignore_resolution_errors()) {
            throw new RuntimeException("Unresolved type " + type);
        }
        this.type = type;
    }

    public Node getReplacement() {
        if (this.replacement != this.replacement.replacement) {
            this.replacement = this.replacement.getReplacement();
        }
        return this.replacement;
    }

    public void mergeWith(Node other) {
        if (other.replacement != other) {
            throw new RuntimeException("Shouldn't happen");
        }
        Node myRep = getReplacement();
        if (other == myRep) {
            return;
        }
        other.replacement = myRep;
        if (other.p2set != this.p2set && other.p2set != null && !other.p2set.isEmpty()) {
            if (myRep.p2set == null || myRep.p2set.isEmpty()) {
                myRep.p2set = other.p2set;
            } else {
                myRep.p2set.mergeWith(other.p2set);
            }
        }
        other.p2set = null;
        this.pag.mergedWith(myRep, other);
        if ((other instanceof VarNode) && (myRep instanceof VarNode) && ((VarNode) other).isInterProcTarget()) {
            ((VarNode) myRep).setInterProcTarget();
        }
    }

    public PointsToSetInternal getP2Set() {
        if (this.p2set != null) {
            if (this.replacement != this) {
                throw new RuntimeException("Node " + this + " has replacement " + this.replacement + " but has p2set");
            }
            return this.p2set;
        }
        Node rep = getReplacement();
        if (rep == this) {
            return EmptyPointsToSet.v();
        }
        return rep.getP2Set();
    }

    public PointsToSetInternal makeP2Set() {
        if (this.p2set != null) {
            if (this.replacement != this) {
                throw new RuntimeException("Node " + this + " has replacement " + this.replacement + " but has p2set");
            }
            return this.p2set;
        }
        Node rep = getReplacement();
        if (rep == this) {
            this.p2set = this.pag.getSetFactory().newSet(this.type, this.pag);
        }
        return rep.makeP2Set();
    }

    public PAG getPag() {
        return this.pag;
    }

    public void discardP2Set() {
        this.p2set = null;
    }

    public void setP2Set(PointsToSetInternal ptsInternal) {
        this.p2set = ptsInternal;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Node(PAG pag, Type type) {
        if (TypeManager.isUnresolved(type) && !Options.v().ignore_resolution_errors()) {
            throw new RuntimeException("Unresolved type " + type);
        }
        this.type = type;
        this.pag = pag;
        this.replacement = this;
    }

    @Override // soot.util.Numberable
    public final int getNumber() {
        return this.number;
    }

    @Override // soot.util.Numberable
    public final void setNumber(int number) {
        this.number = number;
    }
}
