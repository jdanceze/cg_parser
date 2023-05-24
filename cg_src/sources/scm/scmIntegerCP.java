package scm;

import jas.IntegerCP;
/* compiled from: AutoTypes.java */
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:scm/scmIntegerCP.class */
class scmIntegerCP extends Procedure implements Obj {
    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // scm.Procedure
    public Obj apply(Cell args, Env f) throws Exception {
        if (args == null) {
            throw new SchemeError("make-integer-cpe expects 1 arguments");
        }
        Obj tmp = args.car != null ? args.car.eval(f) : null;
        Cell cell = args.cdr;
        if (tmp instanceof Selfrep) {
            int arg0 = (int) Math.round(((Selfrep) tmp).num);
            return new primnode(new IntegerCP(arg0));
        }
        throw new SchemeError("make-integer-cpe expects a number for arg #1");
    }

    @Override // scm.Procedure
    public String toString() {
        return "<#make-integer-cpe#>";
    }
}
