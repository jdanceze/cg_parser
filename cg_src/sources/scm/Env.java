package scm;

import java.util.Hashtable;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:scm/Env.class */
public class Env {
    Hashtable bindings = null;
    static Object MAGIC_KLUDGE = "**jas-nil-internal";
    Env parent;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Obj lookup(Symbol cvar) {
        Object ret = null;
        Hashtable hashtable = this.bindings;
        for (Env f = this; ret == null && f != null; f = f.parent) {
            Hashtable b = f.bindings;
            if (b != null) {
                ret = b.get(cvar);
            }
        }
        if (ret == null) {
            throw new SchemeError("Unbound variable " + cvar);
        }
        if (ret == MAGIC_KLUDGE) {
            return null;
        }
        return (Obj) ret;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setvar(Symbol cvar, Obj val) {
        Object ret = null;
        Hashtable b = this.bindings;
        for (Env f = this; ret == null && f != null; f = f.parent) {
            b = f.bindings;
            if (b != null) {
                ret = b.get(cvar);
            }
        }
        if (ret == null) {
            throw new SchemeError("Attempted to set unbound variable " + cvar);
        }
        if (val == null) {
            b.put(cvar, MAGIC_KLUDGE);
        } else {
            b.put(cvar, val);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void definevar(Symbol v, Obj val) {
        if (this.bindings == null) {
            this.bindings = new Hashtable();
        }
        if (val == null) {
            this.bindings.put(v, MAGIC_KLUDGE);
        } else {
            this.bindings.put(v, val);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Env extendenv(Cell formals, Cell params) {
        Env ret = new Env();
        ret.parent = this;
        if (formals == null) {
            if (params != null) {
                throw new SchemeError("mismatched arglist to entend env");
            }
        } else {
            ret.bindings = new Hashtable();
            while (formals != null) {
                Symbol thissym = (Symbol) formals.car;
                Obj thisval = params.car;
                if (thisval == null) {
                    ret.bindings.put(thissym, MAGIC_KLUDGE);
                } else {
                    ret.bindings.put(thissym, thisval);
                }
                formals = formals.cdr;
                params = params.cdr;
            }
            if (params != null) {
                throw new SchemeError("mismatched arglist to extend env");
            }
        }
        return ret;
    }

    public String toString() {
        return "Binding is " + this.bindings + "\nparent is " + this.parent;
    }
}
