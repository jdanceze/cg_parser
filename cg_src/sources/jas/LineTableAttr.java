package jas;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/LineTableAttr.class */
public class LineTableAttr {
    static CP attr = new AsciiCP("LineNumberTable");
    Vector line = new Vector();
    Vector pc = new Vector();

    public void addEntry(Label l, int line) {
        this.pc.addElement(l);
        this.line.addElement(new Integer(line));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resolve(ClassEnv e) {
        e.addCPItem(attr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int size() {
        return 8 + (4 * this.pc.size());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void write(ClassEnv e, CodeAttr ce, DataOutputStream out) throws IOException, jasError {
        out.writeShort(e.getCPIndex(attr));
        out.writeInt(2 + (4 * this.pc.size()));
        out.writeShort(this.pc.size());
        Enumeration en = this.pc.elements();
        Enumeration ien = this.line.elements();
        while (en.hasMoreElements()) {
            Label l = (Label) en.nextElement();
            Integer i = (Integer) ien.nextElement();
            l.writeOffset(ce, null, out);
            out.writeShort(i.intValue());
        }
    }
}
