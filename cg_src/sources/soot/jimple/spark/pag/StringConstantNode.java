package soot.jimple.spark.pag;

import soot.RefType;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/pag/StringConstantNode.class */
public class StringConstantNode extends AllocNode {
    @Override // soot.jimple.spark.pag.AllocNode
    public String toString() {
        return "StringConstantNode " + getNumber() + Instruction.argsep + this.newExpr;
    }

    public String getString() {
        return (String) this.newExpr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public StringConstantNode(PAG pag, String sc) {
        super(pag, sc, RefType.v("java.lang.String"), null);
    }
}
