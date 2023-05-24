package jas;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/Catchtable.class */
public class Catchtable {
    Vector entries = new Vector();

    public void addEntry(CatchEntry entry) {
        this.entries.addElement(entry);
    }

    public void addEntry(Label start, Label end, Label handler, CP cat) {
        addEntry(new CatchEntry(start, end, handler, cat));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resolve(ClassEnv e) {
        Enumeration en = this.entries.elements();
        while (en.hasMoreElements()) {
            CatchEntry ce = (CatchEntry) en.nextElement();
            ce.resolve(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int size() {
        return 8 * this.entries.size();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void write(ClassEnv e, CodeAttr ce, DataOutputStream out) throws IOException, jasError {
        out.writeShort(this.entries.size());
        Enumeration en = this.entries.elements();
        while (en.hasMoreElements()) {
            CatchEntry entry = (CatchEntry) en.nextElement();
            entry.write(e, ce, out);
        }
    }
}
