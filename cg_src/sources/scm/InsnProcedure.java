package scm;

import jas.CP;
import jas.Insn;
import jas.Label;
import jas.RuntimeConstants;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:scm/InsnProcedure.class */
class InsnProcedure extends Procedure implements Obj, RuntimeConstants {
    int opc;

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // scm.Procedure
    public Obj apply(Cell args, Env f) throws Exception {
        if (args == null) {
            return new primnode(new Insn(this.opc));
        }
        Obj t = args.car.eval(f);
        if (t instanceof Selfrep) {
            int val = (int) ((Selfrep) t).num;
            return new primnode(new Insn(this.opc, val));
        }
        if (t instanceof primnode) {
            Object tprime = ((primnode) t).val;
            if (tprime instanceof CP) {
                return new primnode(new Insn(this.opc, (CP) tprime));
            }
            if (tprime instanceof Label) {
                return new primnode(new Insn(this.opc, (Label) tprime));
            }
        }
        throw new SchemeError("Sorry, not yet implemented " + toString());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InsnProcedure(int opc) {
        this.opc = opc;
    }

    @Override // scm.Procedure
    public String toString() {
        return "<#insn " + opcNames[this.opc] + "#>";
    }
}
