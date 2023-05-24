package scm;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:scm/Lambda.class */
class Lambda extends Procedure implements Obj {
    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // scm.Procedure
    public Obj apply(Cell args, Env f) throws Exception {
        Procedure ret = new Procedure();
        if (args == null) {
            throw new SchemeError("null args to Lambda");
        }
        ret.formals = (Cell) args.car;
        ret.body = args.cdr;
        ret.procenv = f;
        return ret;
    }

    @Override // scm.Procedure
    public String toString() {
        return "<#Lambda#>";
    }
}
