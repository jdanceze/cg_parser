package scm;
/* compiled from: Procedure.java */
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:scm/Quote.class */
class Quote extends Procedure implements Obj {
    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // scm.Procedure
    public Obj apply(Cell args, Env f) throws Exception {
        if (args == null) {
            throw new SchemeError("null args to Quote");
        }
        return args.car;
    }

    @Override // scm.Procedure
    public String toString() {
        return "<#Quote#>";
    }
}
