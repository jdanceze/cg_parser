package jas;

import java.io.DataOutputStream;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/GenericAttr.class */
public class GenericAttr {
    CP attr_name;
    byte[] data;

    public GenericAttr(String name, byte[] data) {
        this.attr_name = new AsciiCP(name);
        this.data = data;
    }

    public GenericAttr(CP name, byte[] data) {
        this.attr_name = name;
        this.data = data;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resolve(ClassEnv e) {
        e.addCPItem(this.attr_name);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int size() {
        return 6 + this.data.length;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
        out.writeShort(e.getCPIndex(this.attr_name));
        out.writeInt(this.data.length);
        out.write(this.data);
    }
}
