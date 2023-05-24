package scm;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:scm/Selfrep.class */
class Selfrep implements Obj {
    String val;
    double num;

    @Override // scm.Obj
    public Obj eval(Env e) {
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Selfrep(double num) {
        this.num = num;
        this.val = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Selfrep(String s) {
        this.val = s;
    }

    public String toString() {
        if (this.val == null) {
            return "Number: " + this.num;
        }
        return "String: " + this.val;
    }
}
