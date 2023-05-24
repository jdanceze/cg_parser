package soot.jimple.spark.pag;

import soot.coffi.Instruction;
import soot.util.ArrayNumberer;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/pag/FieldRefNode.class */
public class FieldRefNode extends ValNode {
    protected VarNode base;
    protected SparkField field;

    public VarNode getBase() {
        return this.base;
    }

    @Override // soot.jimple.spark.pag.Node
    public Node getReplacement() {
        if (this.replacement == this) {
            if (this.base.replacement == this.base) {
                return this;
            }
            Node baseRep = this.base.getReplacement();
            FieldRefNode newRep = this.pag.makeFieldRefNode((VarNode) baseRep, this.field);
            newRep.mergeWith(this);
            Node replacement = newRep.getReplacement();
            this.replacement = replacement;
            return replacement;
        }
        Node replacement2 = this.replacement.getReplacement();
        this.replacement = replacement2;
        return replacement2;
    }

    public SparkField getField() {
        return this.field;
    }

    public String toString() {
        return "FieldRefNode " + getNumber() + Instruction.argsep + this.base + "." + this.field;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FieldRefNode(PAG pag, VarNode base, SparkField field) {
        super(pag, null);
        if (field == null) {
            throw new RuntimeException("null field");
        }
        this.base = base;
        this.field = field;
        base.addField(this, field);
        pag.getFieldRefNodeNumberer().add((ArrayNumberer<FieldRefNode>) this);
    }
}
