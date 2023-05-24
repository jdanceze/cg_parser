package jas;

import java.io.DataOutputStream;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/MethodCP.class */
public class MethodCP extends CP implements RuntimeConstants {
    ClassCP clazz;
    NameTypeCP nt;

    public MethodCP(String cname, String varname, String sig) {
        this.uniq = cname + "&%$91&" + varname + "*(012$" + sig;
        this.clazz = new ClassCP(cname);
        this.nt = new NameTypeCP(varname, sig);
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
        out.writeByte(10);
        out.writeShort(e.getCPIndex(this.clazz));
        out.writeShort(e.getCPIndex(this.nt));
    }
}
