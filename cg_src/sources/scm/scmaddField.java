package scm;

import jas.ClassEnv;
import jas.Var;
/* compiled from: AutoProcs.java */
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:scm/scmaddField.class */
class scmaddField extends Procedure implements Obj {
    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // scm.Procedure
    public Obj apply(Cell args, Env f) throws Exception {
        if (args == null) {
            throw new SchemeError("jas-class-addfield expects 2 arguments");
        }
        Obj tmp = args.car != null ? args.car.eval(f) : null;
        Cell t = args.cdr;
        if (tmp == null || (tmp instanceof primnode)) {
            if (tmp == null || (((primnode) tmp).val instanceof ClassEnv)) {
                ClassEnv arg0 = tmp != null ? (ClassEnv) ((primnode) tmp).val : null;
                if (t == null) {
                    throw new SchemeError("jas-class-addfield expects 2 arguments");
                }
                Obj tmp2 = t.car != null ? t.car.eval(f) : null;
                Cell cell = t.cdr;
                if (tmp2 == null || (tmp2 instanceof primnode)) {
                    if (tmp2 == null || (((primnode) tmp2).val instanceof Var)) {
                        Var arg1 = tmp2 != null ? (Var) ((primnode) tmp2).val : null;
                        arg0.addField(arg1);
                        return null;
                    }
                    throw new SchemeError("jas-class-addfield expects a Var for arg #2");
                }
                throw new SchemeError("jas-class-addfield expects a Var for arg #2");
            }
            throw new SchemeError("jas-class-addfield expects a ClassEnv for arg #1");
        }
        throw new SchemeError("jas-class-addfield expects a ClassEnv for arg #1");
    }

    @Override // scm.Procedure
    public String toString() {
        return "<#jas-class-addfield#>";
    }
}
