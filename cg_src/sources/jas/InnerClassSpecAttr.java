package jas;

import java.io.DataOutputStream;
import java.io.IOException;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/InnerClassSpecAttr.class */
public class InnerClassSpecAttr {
    String inner_class_name;
    String outer_class_name;
    String inner_name;
    short access;

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resolve(ClassEnv e) {
        e.addCPItem(new ClassCP(this.inner_class_name));
        if (!this.outer_class_name.equals(Jimple.NULL)) {
            e.addCPItem(new ClassCP(this.outer_class_name));
        }
        if (!this.inner_name.equals(Jimple.NULL)) {
            e.addCPItem(new AsciiCP(this.inner_name));
        }
    }

    public InnerClassSpecAttr(String a, String b, String c, short d) {
        this.inner_class_name = a;
        this.outer_class_name = b;
        this.inner_name = c;
        this.access = d;
    }

    int size() {
        return 8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
        out.writeShort(e.getCPIndex(new ClassCP(this.inner_class_name)));
        if (this.outer_class_name.equals(Jimple.NULL)) {
            out.writeShort(0);
        } else {
            out.writeShort(e.getCPIndex(new ClassCP(this.outer_class_name)));
        }
        if (this.inner_name.equals(Jimple.NULL)) {
            out.writeShort(0);
        } else {
            out.writeShort(e.getCPIndex(new AsciiCP(this.inner_name)));
        }
        out.writeShort(this.access);
    }
}
