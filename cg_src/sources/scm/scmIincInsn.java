package scm;

import jas.IincInsn;
/* compiled from: AutoTypes.java */
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:scm/scmIincInsn.class */
class scmIincInsn extends Procedure implements Obj {
    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // scm.Procedure
    public Obj apply(Cell args, Env f) throws Exception {
        if (args == null) {
            throw new SchemeError("iinc expects 2 arguments");
        }
        Obj tmp = args.car != null ? args.car.eval(f) : null;
        Cell t = args.cdr;
        if (tmp instanceof Selfrep) {
            int arg0 = (int) Math.round(((Selfrep) tmp).num);
            if (t == null) {
                throw new SchemeError("iinc expects 2 arguments");
            }
            Obj tmp2 = t.car != null ? t.car.eval(f) : null;
            Cell cell = t.cdr;
            if (tmp2 instanceof Selfrep) {
                int arg1 = (int) Math.round(((Selfrep) tmp2).num);
                return new primnode(new IincInsn(arg0, arg1));
            }
            throw new SchemeError("iinc expects a number for arg #2");
        }
        throw new SchemeError("iinc expects a number for arg #1");
    }

    @Override // scm.Procedure
    public String toString() {
        return "<#iinc#>";
    }
}
