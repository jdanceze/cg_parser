package jas;

import java.io.DataOutputStream;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/DoubleCP.class */
public class DoubleCP extends CP implements RuntimeConstants {
    double val;

    public DoubleCP(double n) {
        this.uniq = ("Double: @#$" + n).intern();
        this.val = n;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.CP
    public void resolve(ClassEnv e) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.CP
    public void write(ClassEnv e, DataOutputStream out) throws IOException {
        out.writeByte(6);
        out.writeDouble(this.val);
    }
}
