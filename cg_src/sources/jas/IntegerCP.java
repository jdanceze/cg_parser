package jas;

import java.io.DataOutputStream;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/IntegerCP.class */
public class IntegerCP extends CP implements RuntimeConstants {
    int val;

    public IntegerCP(int n) {
        this.uniq = ("Integer: @#$" + n).intern();
        this.val = n;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.CP
    public void resolve(ClassEnv e) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.CP
    public void write(ClassEnv e, DataOutputStream out) throws IOException {
        out.writeByte(3);
        out.writeInt(this.val);
    }
}
