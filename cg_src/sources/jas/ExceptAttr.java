package jas;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/ExceptAttr.class */
public class ExceptAttr {
    static CP attr = new AsciiCP("Exceptions");
    Vector cps = new Vector();

    public void addException(CP cp) {
        this.cps.addElement(cp);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resolve(ClassEnv e) {
        e.addCPItem(attr);
        Enumeration en = this.cps.elements();
        while (en.hasMoreElements()) {
            e.addCPItem((CP) en.nextElement());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
        out.writeShort(e.getCPIndex(attr));
        out.writeInt((this.cps.size() * 2) + 2);
        out.writeShort(this.cps.size());
        Enumeration en = this.cps.elements();
        while (en.hasMoreElements()) {
            out.writeShort(e.getCPIndex((CP) en.nextElement()));
        }
    }
}
