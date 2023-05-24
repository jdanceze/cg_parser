package jas;

import java.io.DataOutputStream;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/StringCP.class */
public class StringCP extends CP implements RuntimeConstants {
    AsciiCP val;

    public StringCP(String s) {
        this.uniq = ("String: @#$" + s).intern();
        this.val = new AsciiCP(s);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.CP
    public void resolve(ClassEnv e) {
        e.addCPItem(this.val);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.CP
    public void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
        out.writeByte(8);
        out.writeShort(e.getCPIndex(this.val));
    }
}
