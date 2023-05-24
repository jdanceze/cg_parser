package scm;
/* compiled from: Procedure.java */
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:scm/Cdr.class */
class Cdr extends Procedure implements Obj {
    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // scm.Procedure
    public Obj apply(Cell args, Env f) throws Exception {
        Cell tmp = (Cell) args.car.eval(f);
        return tmp.cdr;
    }

    @Override // scm.Procedure
    public String toString() {
        return "<#cdr#>";
    }
}
