package jas;

import java.io.DataOutputStream;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/ElemValPair.class */
public class ElemValPair {
    AsciiCP name;
    byte kind;
    boolean noName;

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resolve(ClassEnv e) {
        e.addCPItem(this.name);
    }

    public ElemValPair(String name, char kind) {
        this.name = new AsciiCP(name);
        this.kind = (byte) kind;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int size() {
        return this.noName ? 1 : 3;
    }

    public void setNoName() {
        this.noName = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
        if (!this.noName) {
            out.writeShort(e.getCPIndex(this.name));
        }
        out.writeByte(this.kind);
    }
}
