package scm;
/* compiled from: Procedure.java */
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:scm/Progn.class */
class Progn extends Procedure implements Obj {
    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // scm.Procedure
    public Obj apply(Cell args, Env f) throws Exception {
        Obj eval;
        Obj result = null;
        for (Cell t = args; t != null; t = t.cdr) {
            if (t.car == null) {
                eval = null;
            } else {
                eval = t.car.eval(f);
            }
            result = eval;
        }
        return result;
    }

    @Override // scm.Procedure
    public String toString() {
        return "<#progn#>";
    }
}
