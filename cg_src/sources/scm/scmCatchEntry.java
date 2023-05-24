package scm;

import jas.CP;
import jas.CatchEntry;
import jas.Label;
/* compiled from: AutoTypes.java */
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:scm/scmCatchEntry.class */
class scmCatchEntry extends Procedure implements Obj {
    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // scm.Procedure
    public Obj apply(Cell args, Env f) throws Exception {
        if (args == null) {
            throw new SchemeError("make-catch-entry expects 4 arguments");
        }
        Obj tmp = args.car != null ? args.car.eval(f) : null;
        Cell t = args.cdr;
        if (tmp == null || (tmp instanceof primnode)) {
            if (tmp == null || (((primnode) tmp).val instanceof Label)) {
                Label arg0 = tmp != null ? (Label) ((primnode) tmp).val : null;
                if (t == null) {
                    throw new SchemeError("make-catch-entry expects 4 arguments");
                }
                Obj tmp2 = t.car != null ? t.car.eval(f) : null;
                Cell t2 = t.cdr;
                if (tmp2 == null || (tmp2 instanceof primnode)) {
                    if (tmp2 == null || (((primnode) tmp2).val instanceof Label)) {
                        Label arg1 = tmp2 != null ? (Label) ((primnode) tmp2).val : null;
                        if (t2 == null) {
                            throw new SchemeError("make-catch-entry expects 4 arguments");
                        }
                        Obj tmp3 = t2.car != null ? t2.car.eval(f) : null;
                        Cell t3 = t2.cdr;
                        if (tmp3 == null || (tmp3 instanceof primnode)) {
                            if (tmp3 == null || (((primnode) tmp3).val instanceof Label)) {
                                Label arg2 = tmp3 != null ? (Label) ((primnode) tmp3).val : null;
                                if (t3 == null) {
                                    throw new SchemeError("make-catch-entry expects 4 arguments");
                                }
                                Obj tmp4 = t3.car != null ? t3.car.eval(f) : null;
                                Cell cell = t3.cdr;
                                if (tmp4 == null || (tmp4 instanceof primnode)) {
                                    if (tmp4 == null || (((primnode) tmp4).val instanceof CP)) {
                                        CP arg3 = tmp4 != null ? (CP) ((primnode) tmp4).val : null;
                                        return new primnode(new CatchEntry(arg0, arg1, arg2, arg3));
                                    }
                                    throw new SchemeError("make-catch-entry expects a CP for arg #4");
                                }
                                throw new SchemeError("make-catch-entry expects a CP for arg #4");
                            }
                            throw new SchemeError("make-catch-entry expects a Label for arg #3");
                        }
                        throw new SchemeError("make-catch-entry expects a Label for arg #3");
                    }
                    throw new SchemeError("make-catch-entry expects a Label for arg #2");
                }
                throw new SchemeError("make-catch-entry expects a Label for arg #2");
            }
            throw new SchemeError("make-catch-entry expects a Label for arg #1");
        }
        throw new SchemeError("make-catch-entry expects a Label for arg #1");
    }

    @Override // scm.Procedure
    public String toString() {
        return "<#make-catch-entry#>";
    }
}
