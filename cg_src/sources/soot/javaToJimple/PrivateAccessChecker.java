package soot.javaToJimple;

import java.util.ArrayList;
import polyglot.ast.Call;
import polyglot.ast.Field;
import polyglot.ast.Node;
import polyglot.types.FieldInstance;
import polyglot.types.MemberInstance;
import polyglot.types.MethodInstance;
import polyglot.visit.NodeVisitor;
/* loaded from: gencallgraphv3.jar:soot/javaToJimple/PrivateAccessChecker.class */
public class PrivateAccessChecker extends NodeVisitor {
    private final ArrayList<MemberInstance> list = new ArrayList<>();

    public ArrayList<MemberInstance> getList() {
        return this.list;
    }

    @Override // polyglot.visit.NodeVisitor
    public Node leave(Node old, Node n, NodeVisitor visitor) {
        if (n instanceof Field) {
            FieldInstance fi = ((Field) n).fieldInstance();
            if (fi.flags().isPrivate()) {
                this.list.add(fi);
            }
        }
        if (n instanceof Call) {
            MethodInstance mi = ((Call) n).methodInstance();
            if (mi.flags().isPrivate()) {
                this.list.add(mi);
            }
        }
        return n;
    }
}
