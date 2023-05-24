package scm;
/* compiled from: Procedure.java */
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:scm/StringP.class */
class StringP extends Procedure implements Obj {
    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // scm.Procedure
    public Obj apply(Cell args, Env f) throws Exception {
        if (args == null) {
            return null;
        }
        Obj target = args.car;
        if (target != null) {
            target = target.eval(f);
        }
        if (target != null && (target instanceof Selfrep) && ((Selfrep) target).val != null) {
            return target;
        }
        return null;
    }

    @Override // scm.Procedure
    public String toString() {
        return "<#string?#>";
    }
}
