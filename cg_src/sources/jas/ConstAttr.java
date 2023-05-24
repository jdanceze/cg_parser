package jas;

import java.io.DataOutputStream;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/ConstAttr.class */
public class ConstAttr {
    static CP attr = new AsciiCP("ConstantValue");
    CP val;

    public ConstAttr(CP val) {
        this.val = val;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resolve(ClassEnv e) {
        e.addCPItem(this.val);
        e.addCPItem(attr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
        out.writeShort(e.getCPIndex(attr));
        out.writeInt(2);
        out.writeShort(e.getCPIndex(this.val));
    }
}
