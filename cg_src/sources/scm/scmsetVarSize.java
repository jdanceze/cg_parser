package scm;

import jas.CodeAttr;
/* compiled from: AutoProcs.java */
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:scm/scmsetVarSize.class */
class scmsetVarSize extends Procedure implements Obj {
    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // scm.Procedure
    public Obj apply(Cell args, Env f) throws Exception {
        if (args == null) {
            throw new SchemeError("jas-code-var-size expects 2 arguments");
        }
        Obj tmp = args.car != null ? args.car.eval(f) : null;
        Cell t = args.cdr;
        if (tmp == null || (tmp instanceof primnode)) {
            if (tmp == null || (((primnode) tmp).val instanceof CodeAttr)) {
                CodeAttr arg0 = tmp != null ? (CodeAttr) ((primnode) tmp).val : null;
                if (t == null) {
                    throw new SchemeError("jas-code-var-size expects 2 arguments");
                }
                Obj tmp2 = t.car != null ? t.car.eval(f) : null;
                Cell cell = t.cdr;
                if (tmp2 instanceof Selfrep) {
                    short arg1 = (short) ((Selfrep) tmp2).num;
                    arg0.setVarSize(arg1);
                    return null;
                }
                throw new SchemeError("jas-code-var-size expects a number for arg #2");
            }
            throw new SchemeError("jas-code-var-size expects a CodeAttr for arg #1");
        }
        throw new SchemeError("jas-code-var-size expects a CodeAttr for arg #1");
    }

    @Override // scm.Procedure
    public String toString() {
        return "<#jas-code-var-size#>";
    }
}
