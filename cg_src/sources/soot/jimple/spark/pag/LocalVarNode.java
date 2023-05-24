package soot.jimple.spark.pag;

import java.util.HashMap;
import java.util.Map;
import soot.SootMethod;
import soot.Type;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/pag/LocalVarNode.class */
public class LocalVarNode extends VarNode {
    protected Map<Object, ContextVarNode> cvns;
    protected SootMethod method;

    public ContextVarNode context(Object context) {
        if (this.cvns == null) {
            return null;
        }
        return this.cvns.get(context);
    }

    public SootMethod getMethod() {
        return this.method;
    }

    public String toString() {
        return "LocalVarNode " + getNumber() + Instruction.argsep + this.variable + Instruction.argsep + this.method;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public LocalVarNode(PAG pag, Object variable, Type t, SootMethod m) {
        super(pag, variable, t);
        this.method = m;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addContext(ContextVarNode cvn, Object context) {
        if (this.cvns == null) {
            this.cvns = new HashMap();
        }
        this.cvns.put(context, cvn);
    }
}
