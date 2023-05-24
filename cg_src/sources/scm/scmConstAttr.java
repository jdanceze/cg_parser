package scm;

import jas.CP;
import jas.ConstAttr;
/* compiled from: AutoTypes.java */
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:scm/scmConstAttr.class */
class scmConstAttr extends Procedure implements Obj {
    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // scm.Procedure
    public Obj apply(Cell args, Env f) throws Exception {
        if (args == null) {
            throw new SchemeError("make-const expects 1 arguments");
        }
        Obj tmp = args.car != null ? args.car.eval(f) : null;
        Cell cell = args.cdr;
        if (tmp == null || (tmp instanceof primnode)) {
            if (tmp == null || (((primnode) tmp).val instanceof CP)) {
                CP arg0 = tmp != null ? (CP) ((primnode) tmp).val : null;
                return new primnode(new ConstAttr(arg0));
            }
            throw new SchemeError("make-const expects a CP for arg #1");
        }
        throw new SchemeError("make-const expects a CP for arg #1");
    }

    @Override // scm.Procedure
    public String toString() {
        return "<#make-const#>";
    }
}
