package soot.jimple.spark.pag;

import soot.coffi.Instruction;
import soot.util.ArrayNumberer;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/pag/AllocDotField.class */
public class AllocDotField extends Node {
    protected AllocNode base;
    protected SparkField field;

    public AllocNode getBase() {
        return this.base;
    }

    public SparkField getField() {
        return this.field;
    }

    public String toString() {
        return "AllocDotField " + getNumber() + Instruction.argsep + this.base + "." + this.field;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AllocDotField(PAG pag, AllocNode base, SparkField field) {
        super(pag, null);
        if (field == null) {
            throw new RuntimeException("null field");
        }
        this.base = base;
        this.field = field;
        base.addField(this, field);
        pag.getAllocDotFieldNodeNumberer().add((ArrayNumberer<AllocDotField>) this);
    }
}
