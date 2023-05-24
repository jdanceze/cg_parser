package scm;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:scm/primnode.class */
class primnode implements Obj {
    Object val;

    @Override // scm.Obj
    public Obj eval(Env e) {
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public primnode(Object thing) {
        this.val = thing;
    }

    public String toString() {
        return this.val.toString();
    }
}
