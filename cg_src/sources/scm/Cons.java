package scm;
/* compiled from: Procedure.java */
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:scm/Cons.class */
class Cons extends Procedure implements Obj {
    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // scm.Procedure
    public Obj apply(Cell args, Env f) throws Exception {
        Obj ncar = args.car.eval(f);
        Obj ncdr = args.cdr.car.eval(f);
        return new Cell(ncar, (Cell) ncdr);
    }

    @Override // scm.Procedure
    public String toString() {
        return "<#cons#>";
    }
}
