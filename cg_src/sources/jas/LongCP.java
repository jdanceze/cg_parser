package jas;

import java.io.DataOutputStream;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/LongCP.class */
public class LongCP extends CP implements RuntimeConstants {
    long val;

    public LongCP(long n) {
        this.uniq = ("Long: @#$" + n).intern();
        this.val = n;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.CP
    public void resolve(ClassEnv e) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.CP
    public void write(ClassEnv e, DataOutputStream out) throws IOException {
        out.writeByte(5);
        out.writeLong(this.val);
    }
}
