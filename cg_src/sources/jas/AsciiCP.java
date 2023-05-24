package jas;

import java.io.DataOutputStream;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/AsciiCP.class */
public class AsciiCP extends CP implements RuntimeConstants {
    public AsciiCP(String s) {
        this.uniq = s.intern();
    }

    @Override // jas.CP
    void resolve(ClassEnv e) {
    }

    public String toString() {
        return "AsciiCP: " + this.uniq;
    }

    @Override // jas.CP
    void write(ClassEnv e, DataOutputStream out) throws IOException {
        out.writeByte(1);
        out.writeUTF(this.uniq);
    }
}
