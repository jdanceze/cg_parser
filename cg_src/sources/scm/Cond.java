package scm;
/* compiled from: Procedure.java */
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:scm/Cond.class */
class Cond extends Procedure implements Obj {
    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // scm.Procedure
    public Obj apply(Cell args, Env f) throws Exception {
        Cell cell = args;
        while (true) {
            Cell t = cell;
            if (t != null) {
                if (t.car == null) {
                    throw new SchemeError("null clause for cond");
                }
                Obj clause = t.car;
                if (!(clause instanceof Cell)) {
                    throw new SchemeError("need a condition body for cond clause");
                }
                Obj result = ((Cell) clause).car;
                if (result != null) {
                    result = result.eval(f);
                }
                if (result == null) {
                    cell = t.cdr;
                } else {
                    Obj body = ((Cell) clause).cdr.car;
                    return body.eval(f);
                }
            } else {
                return null;
            }
        }
    }

    @Override // scm.Procedure
    public String toString() {
        return "<#cond#>";
    }
}
