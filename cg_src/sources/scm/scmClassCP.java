package scm;

import jas.ClassCP;
/* compiled from: AutoTypes.java */
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:scm/scmClassCP.class */
class scmClassCP extends Procedure implements Obj {
    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // scm.Procedure
    public Obj apply(Cell args, Env f) throws Exception {
        if (args == null) {
            throw new SchemeError("make-class-cpe expects 1 arguments");
        }
        Obj tmp = args.car != null ? args.car.eval(f) : null;
        Cell cell = args.cdr;
        if (tmp == null || (tmp instanceof Selfrep)) {
            String arg0 = tmp != null ? ((Selfrep) tmp).val : null;
            return new primnode(new ClassCP(arg0));
        }
        throw new SchemeError("make-class-cpe expects a String for arg #1");
    }

    @Override // scm.Procedure
    public String toString() {
        return "<#make-class-cpe#>";
    }
}
