package soot.javaToJimple;

import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.frontend.Job;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.Position;
import polyglot.visit.AscriptionVisitor;
import polyglot.visit.NodeVisitor;
/* loaded from: gencallgraphv3.jar:soot/javaToJimple/CastInsertionVisitor.class */
public class CastInsertionVisitor extends AscriptionVisitor {
    public CastInsertionVisitor(Job job, TypeSystem ts, NodeFactory nf) {
        super(job, ts, nf);
    }

    @Override // polyglot.visit.AscriptionVisitor
    public Expr ascribe(Expr e, Type toType) {
        Expr newExpr;
        Type fromType = e.type();
        if (toType == null || toType.isVoid()) {
            return e;
        }
        Position p = e.position();
        if (toType.equals(fromType)) {
            return e;
        }
        if (toType.isPrimitive() && fromType.isPrimitive()) {
            if (fromType.isFloat() || fromType.isLong() || fromType.isDouble()) {
                if (toType.isFloat() || toType.isLong() || toType.isDouble() || toType.isInt()) {
                    newExpr = this.nf.Cast(p, this.nf.CanonicalTypeNode(p, toType), e).type(toType);
                } else {
                    newExpr = this.nf.Cast(p, this.nf.CanonicalTypeNode(p, toType), this.nf.Cast(p, this.nf.CanonicalTypeNode(p, this.ts.Int()), e).type(this.ts.Int())).type(toType);
                }
            } else {
                newExpr = this.nf.Cast(p, this.nf.CanonicalTypeNode(p, toType), e).type(toType);
            }
            return newExpr;
        }
        return e;
    }

    @Override // polyglot.visit.AscriptionVisitor, polyglot.visit.ErrorHandlingVisitor
    public Node leaveCall(Node old, Node n, NodeVisitor v) throws SemanticException {
        return super.leaveCall(old, n, v);
    }
}
