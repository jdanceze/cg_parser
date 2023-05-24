package scm;
/* compiled from: Procedure.java */
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:scm/Mapcar.class */
class Mapcar extends Procedure implements Obj {
    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // scm.Procedure
    public Obj apply(Cell args, Env f) throws Exception {
        Obj ftmp = args.car;
        if (ftmp != null) {
            ftmp = ftmp.eval(f);
        }
        if (ftmp == null) {
            throw new SchemeError("null function for mapcar");
        }
        if (!(ftmp instanceof Procedure)) {
            throw new SchemeError("expected a procedure for mapcar");
        }
        Procedure fn = (Procedure) ftmp;
        Cell res = null;
        Cell tail = null;
        for (Cell t = (Cell) args.cdr.car.eval(f); t != null; t = t.cdr) {
            if (tail == null) {
                res = new Cell(fn.apply(new Cell(t.car, null), f), null);
                tail = res;
            } else {
                tail.cdr = new Cell(fn.apply(new Cell(t.car, null), f), null);
            }
        }
        return res;
    }
}
