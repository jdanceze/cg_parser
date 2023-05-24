package scm;

import jas.CodeAttr;
import jas.Insn;
/* compiled from: AutoProcs.java */
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:scm/scmaddInsn.class */
class scmaddInsn extends Procedure implements Obj {
    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // scm.Procedure
    public Obj apply(Cell args, Env f) throws Exception {
        if (args == null) {
            throw new SchemeError("jas-code-addinsn expects 2 arguments");
        }
        Obj tmp = args.car != null ? args.car.eval(f) : null;
        Cell t = args.cdr;
        if (tmp == null || (tmp instanceof primnode)) {
            if (tmp == null || (((primnode) tmp).val instanceof CodeAttr)) {
                CodeAttr arg0 = tmp != null ? (CodeAttr) ((primnode) tmp).val : null;
                if (t == null) {
                    throw new SchemeError("jas-code-addinsn expects 2 arguments");
                }
                Obj tmp2 = t.car != null ? t.car.eval(f) : null;
                Cell cell = t.cdr;
                if (tmp2 == null || (tmp2 instanceof primnode)) {
                    if (tmp2 == null || (((primnode) tmp2).val instanceof Insn)) {
                        Insn arg1 = tmp2 != null ? (Insn) ((primnode) tmp2).val : null;
                        arg0.addInsn(arg1);
                        return null;
                    }
                    throw new SchemeError("jas-code-addinsn expects a Insn for arg #2");
                }
                throw new SchemeError("jas-code-addinsn expects a Insn for arg #2");
            }
            throw new SchemeError("jas-code-addinsn expects a CodeAttr for arg #1");
        }
        throw new SchemeError("jas-code-addinsn expects a CodeAttr for arg #1");
    }

    @Override // scm.Procedure
    public String toString() {
        return "<#jas-code-addinsn#>";
    }
}
