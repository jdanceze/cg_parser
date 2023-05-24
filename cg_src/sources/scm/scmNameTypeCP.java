package scm;

import jas.NameTypeCP;
/* compiled from: AutoTypes.java */
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:scm/scmNameTypeCP.class */
class scmNameTypeCP extends Procedure implements Obj {
    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // scm.Procedure
    public Obj apply(Cell args, Env f) throws Exception {
        if (args == null) {
            throw new SchemeError("make-name-type-cpe expects 2 arguments");
        }
        Obj tmp = args.car != null ? args.car.eval(f) : null;
        Cell t = args.cdr;
        if (tmp == null || (tmp instanceof Selfrep)) {
            String arg0 = tmp != null ? ((Selfrep) tmp).val : null;
            if (t == null) {
                throw new SchemeError("make-name-type-cpe expects 2 arguments");
            }
            Obj tmp2 = t.car != null ? t.car.eval(f) : null;
            Cell cell = t.cdr;
            if (tmp2 == null || (tmp2 instanceof Selfrep)) {
                String arg1 = tmp2 != null ? ((Selfrep) tmp2).val : null;
                return new primnode(new NameTypeCP(arg0, arg1));
            }
            throw new SchemeError("make-name-type-cpe expects a String for arg #2");
        }
        throw new SchemeError("make-name-type-cpe expects a String for arg #1");
    }

    @Override // scm.Procedure
    public String toString() {
        return "<#make-name-type-cpe#>";
    }
}
