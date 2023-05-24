package scm;

import jas.ExceptAttr;
/* compiled from: AutoTypes.java */
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:scm/scmExceptAttr.class */
class scmExceptAttr extends Procedure implements Obj {
    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // scm.Procedure
    public Obj apply(Cell args, Env f) throws Exception {
        return new primnode(new ExceptAttr());
    }

    @Override // scm.Procedure
    public String toString() {
        return "<#make-exception#>";
    }
}
