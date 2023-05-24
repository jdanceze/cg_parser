package soot.javaToJimple;

import java.util.ArrayList;
import polyglot.ast.Call;
import polyglot.ast.ConstructorCall;
import polyglot.ast.Field;
import polyglot.ast.New;
import polyglot.ast.Node;
import polyglot.types.FieldInstance;
import polyglot.types.ProcedureInstance;
import polyglot.util.IdentityKey;
import polyglot.visit.NodeVisitor;
/* loaded from: gencallgraphv3.jar:soot/javaToJimple/PrivateAccessUses.class */
public class PrivateAccessUses extends NodeVisitor {
    private final ArrayList<IdentityKey> list = new ArrayList<>();
    private ArrayList avail;

    public ArrayList<IdentityKey> getList() {
        return this.list;
    }

    public void avail(ArrayList list) {
        this.avail = list;
    }

    @Override // polyglot.visit.NodeVisitor
    public Node leave(Node old, Node n, NodeVisitor visitor) {
        if (n instanceof Field) {
            FieldInstance fi = ((Field) n).fieldInstance();
            if (this.avail.contains(new IdentityKey(fi))) {
                this.list.add(new IdentityKey(fi));
            }
        }
        if (n instanceof Call) {
            ProcedureInstance pi = ((Call) n).methodInstance();
            if (this.avail.contains(new IdentityKey(pi))) {
                this.list.add(new IdentityKey(pi));
            }
        }
        if (n instanceof New) {
            ProcedureInstance pi2 = ((New) n).constructorInstance();
            if (this.avail.contains(new IdentityKey(pi2))) {
                this.list.add(new IdentityKey(pi2));
            }
        }
        if (n instanceof ConstructorCall) {
            ProcedureInstance pi3 = ((ConstructorCall) n).constructorInstance();
            if (this.avail.contains(new IdentityKey(pi3))) {
                this.list.add(new IdentityKey(pi3));
            }
        }
        return n;
    }
}
