package jas;

import java.io.DataOutputStream;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/ClassCP.class */
public class ClassCP extends CP implements RuntimeConstants {
    AsciiCP name;

    public ClassCP(String name) {
        this.uniq = ("CLASS: #$%^#$" + name).intern();
        this.name = new AsciiCP(name);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.CP
    public void resolve(ClassEnv e) {
        e.addCPItem(this.name);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.CP
    public void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
        out.writeByte(7);
        out.writeShort(e.getCPIndex(this.name));
    }
}
