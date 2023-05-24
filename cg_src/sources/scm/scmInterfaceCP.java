package scm;

import jas.InterfaceCP;
/* compiled from: AutoTypes.java */
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:scm/scmInterfaceCP.class */
class scmInterfaceCP extends Procedure implements Obj {
    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // scm.Procedure
    public Obj apply(Cell args, Env f) throws Exception {
        if (args == null) {
            throw new SchemeError("make-interface-cpe expects 3 arguments");
        }
        Obj tmp = args.car != null ? args.car.eval(f) : null;
        Cell t = args.cdr;
        if (tmp == null || (tmp instanceof Selfrep)) {
            String arg0 = tmp != null ? ((Selfrep) tmp).val : null;
            if (t == null) {
                throw new SchemeError("make-interface-cpe expects 3 arguments");
            }
            Obj tmp2 = t.car != null ? t.car.eval(f) : null;
            Cell t2 = t.cdr;
            if (tmp2 == null || (tmp2 instanceof Selfrep)) {
                String arg1 = tmp2 != null ? ((Selfrep) tmp2).val : null;
                if (t2 == null) {
                    throw new SchemeError("make-interface-cpe expects 3 arguments");
                }
                Obj tmp3 = t2.car != null ? t2.car.eval(f) : null;
                Cell cell = t2.cdr;
                if (tmp3 == null || (tmp3 instanceof Selfrep)) {
                    String arg2 = tmp3 != null ? ((Selfrep) tmp3).val : null;
                    return new primnode(new InterfaceCP(arg0, arg1, arg2));
                }
                throw new SchemeError("make-interface-cpe expects a String for arg #3");
            }
            throw new SchemeError("make-interface-cpe expects a String for arg #2");
        }
        throw new SchemeError("make-interface-cpe expects a String for arg #1");
    }

    @Override // scm.Procedure
    public String toString() {
        return "<#make-interface-cpe#>";
    }
}
