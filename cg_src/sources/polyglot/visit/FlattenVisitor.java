package polyglot.visit;

import java.util.LinkedList;
import java.util.List;
import polyglot.ast.Assign;
import polyglot.ast.Block;
import polyglot.ast.ConstructorCall;
import polyglot.ast.Eval;
import polyglot.ast.Expr;
import polyglot.ast.FieldDecl;
import polyglot.ast.Lit;
import polyglot.ast.Local;
import polyglot.ast.LocalDecl;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.ast.Special;
import polyglot.ast.Stmt;
import polyglot.types.Flags;
import polyglot.types.TypeSystem;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/FlattenVisitor.class */
public class FlattenVisitor extends NodeVisitor {
    protected TypeSystem ts;
    protected NodeFactory nf;
    static int count = 0;
    protected Node noFlatten = null;
    protected LinkedList stack = new LinkedList();

    public FlattenVisitor(TypeSystem ts, NodeFactory nf) {
        this.ts = ts;
        this.nf = nf;
    }

    @Override // polyglot.visit.NodeVisitor
    public Node override(Node n) {
        if ((n instanceof FieldDecl) || (n instanceof ConstructorCall)) {
            return n;
        }
        return null;
    }

    protected static String newID() {
        StringBuffer append = new StringBuffer().append("flat$$$");
        int i = count;
        count = i + 1;
        return append.append(i).toString();
    }

    @Override // polyglot.visit.NodeVisitor
    public NodeVisitor enter(Node n) {
        if (n instanceof Block) {
            this.stack.addFirst(new LinkedList());
        }
        if (n instanceof Eval) {
            Eval s = (Eval) n;
            this.noFlatten = s.expr();
        }
        return this;
    }

    @Override // polyglot.visit.NodeVisitor
    public Node leave(Node old, Node n, NodeVisitor v) {
        if (n == this.noFlatten) {
            this.noFlatten = null;
            return n;
        } else if (n instanceof Block) {
            List l = (List) this.stack.removeFirst();
            return ((Block) n).statements(l);
        } else if ((n instanceof Stmt) && !(n instanceof LocalDecl)) {
            List l2 = (List) this.stack.getFirst();
            l2.add(n);
            return n;
        } else if ((n instanceof Expr) && !(n instanceof Lit) && !(n instanceof Special) && !(n instanceof Local)) {
            Expr e = (Expr) n;
            if (e instanceof Assign) {
                return n;
            }
            String name = newID();
            LocalDecl def = this.nf.LocalDecl(e.position(), Flags.FINAL, this.nf.CanonicalTypeNode(e.position(), e.type()), name, e);
            LocalDecl def2 = def.localInstance(this.ts.localInstance(e.position(), Flags.FINAL, e.type(), name));
            List l3 = (List) this.stack.getFirst();
            l3.add(def2);
            Local use = this.nf.Local(e.position(), name);
            return ((Local) use.type(e.type())).localInstance(this.ts.localInstance(e.position(), Flags.FINAL, e.type(), name));
        } else {
            return n;
        }
    }
}
