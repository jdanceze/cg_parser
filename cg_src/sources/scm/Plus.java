package scm;
/* compiled from: Procedure.java */
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:scm/Plus.class */
class Plus extends Procedure implements Obj {
    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // scm.Procedure
    public Obj apply(Cell args, Env f) throws Exception {
        Obj l1 = args.car.eval(f);
        Obj l2 = args.cdr.car.eval(f);
        return new Selfrep(((Selfrep) l1).num + ((Selfrep) l2).num);
    }

    @Override // scm.Procedure
    public String toString() {
        return "<#plus#>";
    }
}
