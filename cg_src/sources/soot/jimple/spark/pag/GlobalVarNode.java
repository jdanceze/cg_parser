package soot.jimple.spark.pag;

import soot.SootClass;
import soot.SootField;
import soot.Type;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/pag/GlobalVarNode.class */
public class GlobalVarNode extends VarNode {
    /* JADX INFO: Access modifiers changed from: package-private */
    public GlobalVarNode(PAG pag, Object variable, Type t) {
        super(pag, variable, t);
    }

    public String toString() {
        return "GlobalVarNode " + getNumber() + Instruction.argsep + this.variable;
    }

    public SootClass getDeclaringClass() {
        if (this.variable instanceof SootField) {
            return ((SootField) this.variable).getDeclaringClass();
        }
        return null;
    }
}
