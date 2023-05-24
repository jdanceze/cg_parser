package jas;

import java.io.DataOutputStream;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/AnnotElemValPair.class */
public class AnnotElemValPair extends ElemValPair {
    AnnotationAttr attr;

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.ElemValPair
    public void resolve(ClassEnv e) {
        super.resolve(e);
        this.attr.resolve(e);
    }

    public AnnotElemValPair(String name, char kind, AnnotationAttr attr) {
        super(name, kind);
        this.attr = attr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.ElemValPair
    public int size() {
        return super.size() + this.attr.size();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.ElemValPair
    public void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
        super.write(e, out);
        this.attr.write(e, out);
    }
}
