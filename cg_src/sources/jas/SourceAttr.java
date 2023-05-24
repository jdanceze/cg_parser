package jas;

import java.io.DataOutputStream;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/SourceAttr.class */
public class SourceAttr {
    static CP attr = new AsciiCP("SourceFile");
    CP name;

    public SourceAttr(String name) {
        this.name = new AsciiCP(name);
    }

    public SourceAttr(CP name) {
        this.name = name;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resolve(ClassEnv e) {
        e.addCPItem(attr);
        e.addCPItem(this.name);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
        out.writeShort(e.getCPIndex(attr));
        out.writeInt(2);
        out.writeShort(e.getCPIndex(this.name));
    }
}
