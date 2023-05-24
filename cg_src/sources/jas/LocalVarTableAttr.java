package jas;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/LocalVarTableAttr.class */
public class LocalVarTableAttr {
    static CP attr = new AsciiCP("LocalVariableTable");
    Vector vars = new Vector();

    public void addEntry(LocalVarEntry e) {
        this.vars.addElement(e);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resolve(ClassEnv e) {
        e.addCPItem(attr);
        Enumeration en = this.vars.elements();
        while (en.hasMoreElements()) {
            LocalVarEntry lv = (LocalVarEntry) en.nextElement();
            lv.resolve(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int size() {
        return 8 + (10 * this.vars.size());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void write(ClassEnv e, CodeAttr ce, DataOutputStream out) throws IOException, jasError {
        out.writeShort(e.getCPIndex(attr));
        out.writeInt(2 + (10 * this.vars.size()));
        out.writeShort(this.vars.size());
        Enumeration en = this.vars.elements();
        while (en.hasMoreElements()) {
            LocalVarEntry lv = (LocalVarEntry) en.nextElement();
            lv.write(e, ce, out);
        }
    }
}
