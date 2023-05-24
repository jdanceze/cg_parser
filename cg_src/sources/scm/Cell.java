package scm;

import soot.coffi.Instruction;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:scm/Cell.class */
public class Cell implements Obj {
    Obj car;
    Cell cdr;

    @Override // scm.Obj
    public Obj eval(Env e) throws Exception {
        Procedure p;
        if (this.car == null) {
            throw new SchemeError("null car cell trying to eval " + this);
        }
        if (this.car instanceof Procedure) {
            p = (Procedure) this.car;
        } else {
            p = (Procedure) this.car.eval(e);
        }
        return p.apply(this.cdr, e);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Cell(Obj a, Cell b) {
        this.car = a;
        this.cdr = b;
    }

    public String toString() {
        return toString("");
    }

    public String toString(String s) {
        String s2 = this.car == null ? s + "()" : s + this.car.toString();
        if (this.cdr == null) {
            return "(" + s2 + ")";
        }
        return this.cdr.toString(s2 + Instruction.argsep);
    }
}
