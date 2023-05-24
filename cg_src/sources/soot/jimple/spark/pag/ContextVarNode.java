package soot.jimple.spark.pag;

import soot.Context;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/pag/ContextVarNode.class */
public class ContextVarNode extends LocalVarNode {
    private Context context;

    @Override // soot.jimple.spark.pag.VarNode
    public Context context() {
        return this.context;
    }

    @Override // soot.jimple.spark.pag.LocalVarNode
    public String toString() {
        return "ContextVarNode " + getNumber() + Instruction.argsep + this.variable + Instruction.argsep + this.method + Instruction.argsep + this.context;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ContextVarNode(PAG pag, LocalVarNode base, Context context) {
        super(pag, base.getVariable(), base.getType(), base.getMethod());
        this.context = context;
        base.addContext(this, context);
    }
}
