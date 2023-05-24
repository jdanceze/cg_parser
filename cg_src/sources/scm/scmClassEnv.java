package scm;

import jas.ClassEnv;
/* compiled from: AutoTypes.java */
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:scm/scmClassEnv.class */
class scmClassEnv extends Procedure implements Obj {
    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // scm.Procedure
    public Obj apply(Cell args, Env f) throws Exception {
        return new primnode(new ClassEnv());
    }

    @Override // scm.Procedure
    public String toString() {
        return "<#make-class-env#>";
    }
}
