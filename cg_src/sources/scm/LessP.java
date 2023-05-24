package scm;
/* compiled from: Procedure.java */
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:scm/LessP.class */
class LessP extends Procedure implements Obj {
    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // scm.Procedure
    public Obj apply(Cell args, Env f) throws Exception {
        if (args == null) {
            throw new SchemeError("< expects a pair of arguments");
        }
        Obj target1 = args.car;
        if (target1 != null) {
            target1 = target1.eval(f);
        }
        Obj target2 = args.cdr.car;
        if (target2 != null) {
            target2 = target2.eval(f);
        }
        if (target1 == null || target2 == null) {
            throw new SchemeError("< expects a pair of arguments");
        }
        if (!(target1 instanceof Selfrep) || !(target2 instanceof Selfrep)) {
            throw new SchemeError("< expects a pair of numbers as args");
        }
        if (((Selfrep) target1).num < ((Selfrep) target2).num) {
            return target1;
        }
        return null;
    }

    @Override // scm.Procedure
    public String toString() {
        return "<#<#>";
    }
}
