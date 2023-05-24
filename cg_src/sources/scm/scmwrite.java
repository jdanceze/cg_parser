package scm;

import jas.ClassEnv;
/* compiled from: AutoProcs.java */
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:scm/scmwrite.class */
class scmwrite extends Procedure implements Obj {
    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // scm.Procedure
    public Obj apply(Cell args, Env f) throws Exception {
        if (args == null) {
            throw new SchemeError("jas-class-write expects 2 arguments");
        }
        Obj tmp = args.car != null ? args.car.eval(f) : null;
        Cell t = args.cdr;
        if (tmp == null || (tmp instanceof primnode)) {
            if (tmp == null || (((primnode) tmp).val instanceof ClassEnv)) {
                ClassEnv arg0 = tmp != null ? (ClassEnv) ((primnode) tmp).val : null;
                if (t == null) {
                    throw new SchemeError("jas-class-write expects 2 arguments");
                }
                Obj tmp2 = t.car != null ? t.car.eval(f) : null;
                Cell cell = t.cdr;
                if (tmp2 == null || (tmp2 instanceof primnode)) {
                    if (tmp2 == null || (((primnode) tmp2).val instanceof scmOutputStream)) {
                        scmOutputStream arg1 = tmp2 != null ? (scmOutputStream) ((primnode) tmp2).val : null;
                        arg0.write(arg1);
                        return null;
                    }
                    throw new SchemeError("jas-class-write expects a scmOutputStream for arg #2");
                }
                throw new SchemeError("jas-class-write expects a scmOutputStream for arg #2");
            }
            throw new SchemeError("jas-class-write expects a ClassEnv for arg #1");
        }
        throw new SchemeError("jas-class-write expects a ClassEnv for arg #1");
    }

    @Override // scm.Procedure
    public String toString() {
        return "<#jas-class-write#>";
    }
}
