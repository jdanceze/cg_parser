package soot.jimple.spark.pag;

import soot.RefType;
import soot.coffi.Instruction;
import soot.jimple.ClassConstant;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/pag/ClassConstantNode.class */
public class ClassConstantNode extends AllocNode {
    @Override // soot.jimple.spark.pag.AllocNode
    public String toString() {
        return "ClassConstantNode " + getNumber() + Instruction.argsep + this.newExpr;
    }

    public ClassConstant getClassConstant() {
        return (ClassConstant) this.newExpr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ClassConstantNode(PAG pag, ClassConstant cc) {
        super(pag, cc, RefType.v("java.lang.Class"), null);
    }
}
