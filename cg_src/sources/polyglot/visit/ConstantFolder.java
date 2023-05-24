package polyglot.visit;

import polyglot.ast.Binary;
import polyglot.ast.Expr;
import polyglot.ast.FloatLit;
import polyglot.ast.IntLit;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.types.TypeSystem;
import polyglot.util.Position;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/ConstantFolder.class */
public class ConstantFolder extends NodeVisitor {
    TypeSystem ts;
    NodeFactory nf;

    public ConstantFolder(TypeSystem ts, NodeFactory nf) {
        this.ts = ts;
        this.nf = nf;
    }

    public TypeSystem typeSystem() {
        return this.ts;
    }

    public NodeFactory nodeFactory() {
        return this.nf;
    }

    @Override // polyglot.visit.NodeVisitor
    public Node leave(Node old, Node n, NodeVisitor v_) {
        if (!(n instanceof Expr)) {
            return n;
        }
        Expr e = (Expr) n;
        if (!e.isConstant()) {
            return e;
        }
        if (e instanceof Binary) {
            Binary b = (Binary) e;
            if (b.operator() == Binary.ADD && (b.left().constantValue() instanceof String) && (b.right().constantValue() instanceof String)) {
                return b;
            }
        }
        Object v = e.constantValue();
        Position pos = e.position();
        if (v == null) {
            return this.nf.NullLit(pos).type(this.ts.Null());
        }
        if (v instanceof String) {
            return this.nf.StringLit(pos, (String) v).type(this.ts.String());
        }
        if (v instanceof Boolean) {
            return this.nf.BooleanLit(pos, ((Boolean) v).booleanValue()).type(this.ts.Boolean());
        }
        if (v instanceof Double) {
            return this.nf.FloatLit(pos, FloatLit.DOUBLE, ((Double) v).doubleValue()).type(this.ts.Double());
        }
        if (v instanceof Float) {
            return this.nf.FloatLit(pos, FloatLit.FLOAT, ((Float) v).floatValue()).type(this.ts.Float());
        }
        if (v instanceof Long) {
            return this.nf.IntLit(pos, IntLit.LONG, ((Long) v).longValue()).type(this.ts.Long());
        }
        if (v instanceof Integer) {
            return this.nf.IntLit(pos, IntLit.INT, ((Integer) v).intValue()).type(this.ts.Int());
        }
        if (v instanceof Character) {
            return this.nf.CharLit(pos, ((Character) v).charValue()).type(this.ts.Char());
        }
        return e;
    }
}
