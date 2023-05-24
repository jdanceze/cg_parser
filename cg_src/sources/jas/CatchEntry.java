package jas;

import java.io.DataOutputStream;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/CatchEntry.class */
public class CatchEntry {
    Label start_pc;
    Label end_pc;
    Label handler_pc;
    CP catch_cpe;

    public CatchEntry(Label start, Label end, Label handler, CP cat) {
        this.start_pc = start;
        this.end_pc = end;
        this.handler_pc = handler;
        this.catch_cpe = cat;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resolve(ClassEnv e) {
        if (this.catch_cpe != null) {
            e.addCPItem(this.catch_cpe);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void write(ClassEnv e, CodeAttr ce, DataOutputStream out) throws IOException, jasError {
        this.start_pc.writeOffset(ce, null, out);
        this.end_pc.writeOffset(ce, null, out);
        this.handler_pc.writeOffset(ce, null, out);
        if (this.catch_cpe != null) {
            out.writeShort(e.getCPIndex(this.catch_cpe));
        } else {
            out.writeShort(0);
        }
    }
}
