package scm;

import jas.CatchEntry;
import jas.Catchtable;
/* compiled from: AutoProcs.java */
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:scm/scmaddEntry.class */
class scmaddEntry extends Procedure implements Obj {
    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // scm.Procedure
    public Obj apply(Cell args, Env f) throws Exception {
        if (args == null) {
            throw new SchemeError("jas-add-catch-entry expects 2 arguments");
        }
        Obj tmp = args.car != null ? args.car.eval(f) : null;
        Cell t = args.cdr;
        if (tmp == null || (tmp instanceof primnode)) {
            if (tmp == null || (((primnode) tmp).val instanceof Catchtable)) {
                Catchtable arg0 = tmp != null ? (Catchtable) ((primnode) tmp).val : null;
                if (t == null) {
                    throw new SchemeError("jas-add-catch-entry expects 2 arguments");
                }
                Obj tmp2 = t.car != null ? t.car.eval(f) : null;
                Cell cell = t.cdr;
                if (tmp2 == null || (tmp2 instanceof primnode)) {
                    if (tmp2 == null || (((primnode) tmp2).val instanceof CatchEntry)) {
                        CatchEntry arg1 = tmp2 != null ? (CatchEntry) ((primnode) tmp2).val : null;
                        arg0.addEntry(arg1);
                        return null;
                    }
                    throw new SchemeError("jas-add-catch-entry expects a CatchEntry for arg #2");
                }
                throw new SchemeError("jas-add-catch-entry expects a CatchEntry for arg #2");
            }
            throw new SchemeError("jas-add-catch-entry expects a Catchtable for arg #1");
        }
        throw new SchemeError("jas-add-catch-entry expects a Catchtable for arg #1");
    }

    @Override // scm.Procedure
    public String toString() {
        return "<#jas-add-catch-entry#>";
    }
}
