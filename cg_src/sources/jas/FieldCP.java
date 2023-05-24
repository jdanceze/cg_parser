package jas;

import java.io.DataOutputStream;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/FieldCP.class */
public class FieldCP extends CP implements RuntimeConstants {
    ClassCP clazz;
    NameTypeCP nt;

    public FieldCP(String clazz, String name, String sig) {
        this.uniq = (clazz + "&%$#&" + name + "*()#$" + sig).intern();
        this.clazz = new ClassCP(clazz);
        this.nt = new NameTypeCP(name, sig);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.CP
    public void resolve(ClassEnv e) {
        e.addCPItem(this.clazz);
        e.addCPItem(this.nt);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.CP
    public void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
        out.writeByte(9);
        out.writeShort(e.getCPIndex(this.clazz));
        out.writeShort(e.getCPIndex(this.nt));
    }
}
