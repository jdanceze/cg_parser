package jas;

import java.io.DataOutputStream;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/FloatCP.class */
public class FloatCP extends CP implements RuntimeConstants {
    float val;

    public FloatCP(float n) {
        this.uniq = ("Float: @#$" + n).intern();
        this.val = n;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.CP
    public void resolve(ClassEnv e) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.CP
    public void write(ClassEnv e, DataOutputStream out) throws IOException {
        out.writeByte(4);
        out.writeFloat(this.val);
    }
}
