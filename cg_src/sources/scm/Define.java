package scm;
/* compiled from: Procedure.java */
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:scm/Define.class */
class Define extends Procedure implements Obj {
    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // scm.Procedure
    public Obj apply(Cell args, Env f) throws Exception {
        if (args == null) {
            throw new SchemeError("null args to define");
        }
        if (args.car instanceof Symbol) {
            Symbol v = (Symbol) args.car;
            if (v == null) {
                throw new SchemeError("null symbol value");
            }
            Cell val = args.cdr;
            if (val == null) {
                throw new SchemeError("not enough args to define");
            }
            Obj ret = val.car;
            if (ret != null) {
                ret = ret.eval(f);
            }
            f.definevar(v, ret);
            return ret;
        }
        throw new SchemeError("bad argtype to define" + args.car);
    }

    @Override // scm.Procedure
    public String toString() {
        return "<#define#>";
    }
}
