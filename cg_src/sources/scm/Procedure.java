package scm;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:scm/Procedure.class */
class Procedure implements Obj {
    Cell body;
    Cell formals;
    Env procenv;

    Env extendargs(Cell args, Env f) throws Exception {
        Cell cell;
        Cell params = null;
        Cell tail = null;
        while (args != null) {
            Obj now = args.car;
            if (now != null) {
                now = now.eval(f);
            }
            if (tail != null) {
                tail.cdr = new Cell(now, null);
                cell = tail.cdr;
            } else {
                params = new Cell(now, params);
                cell = params;
            }
            tail = cell;
            args = args.cdr;
        }
        return this.procenv.extendenv(this.formals, params);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Obj apply(Cell args, Env f) throws Exception {
        Env newEnv = extendargs(args, f);
        Obj ret = null;
        for (Cell expr = this.body; expr != null; expr = expr.cdr) {
            ret = expr.car;
            if (ret != null) {
                ret = ret.eval(newEnv);
            }
        }
        return ret;
    }

    @Override // scm.Obj
    public Obj eval(Env e) {
        throw new SchemeError("Cant eval procedures directly");
    }

    public String toString() {
        return "<lambda generated> " + this.body;
    }
}
