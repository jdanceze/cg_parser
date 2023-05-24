package scm;
/* compiled from: Procedure.java */
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:scm/EqP.class */
class EqP extends Procedure implements Obj {
    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // scm.Procedure
    public Obj apply(Cell args, Env f) throws Exception {
        if (args == null) {
            return null;
        }
        Obj target1 = args.car;
        if (target1 != null) {
            target1 = target1.eval(f);
        }
        Obj target2 = args.cdr.car;
        if (target2 != null) {
            target2 = target2.eval(f);
        }
        if (target1 == null && target2 == null) {
            return new Selfrep(1.0d);
        }
        if (target1 == null || target2 == null) {
            return null;
        }
        if (target1 == target2) {
            return target1;
        }
        if ((target1 instanceof Selfrep) && (target2 instanceof Selfrep)) {
            if (((Selfrep) target1).val == null) {
                if (((Selfrep) target1).num == ((Selfrep) target2).num) {
                    return new Selfrep(1.0d);
                }
                return null;
            } else if (((Selfrep) target1).val.equals(((Selfrep) target2).val)) {
                return new Selfrep(1.0d);
            } else {
                return null;
            }
        }
        return null;
    }

    @Override // scm.Procedure
    public String toString() {
        return "<#eq?#>";
    }
}
