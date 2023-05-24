package jas;

import java.io.DataOutputStream;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/DeprecatedAttr.class */
public class DeprecatedAttr {
    static CP attr = new AsciiCP("Deprecated");

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resolve(ClassEnv e) {
        e.addCPItem(attr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
        out.writeShort(e.getCPIndex(attr));
        out.writeInt(0);
    }
}
