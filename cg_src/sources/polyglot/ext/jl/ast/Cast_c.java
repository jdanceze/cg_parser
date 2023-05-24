package polyglot.ext.jl.ast;

import java.util.Collections;
import java.util.List;
import polyglot.ast.Cast;
import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.Precedence;
import polyglot.ast.Term;
import polyglot.ast.TypeNode;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.visit.AscriptionVisitor;
import polyglot.visit.CFGBuilder;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/Cast_c.class */
public class Cast_c extends Expr_c implements Cast {
    protected TypeNode castType;
    protected Expr expr;

    public Cast_c(Position pos, TypeNode castType, Expr expr) {
        super(pos);
        this.castType = castType;
        this.expr = expr;
    }

    @Override // polyglot.ext.jl.ast.Expr_c, polyglot.ast.Expr
    public Precedence precedence() {
        return Precedence.CAST;
    }

    @Override // polyglot.ast.Cast
    public TypeNode castType() {
        return this.castType;
    }

    @Override // polyglot.ast.Cast
    public Cast castType(TypeNode castType) {
        Cast_c n = (Cast_c) copy();
        n.castType = castType;
        return n;
    }

    @Override // polyglot.ast.Cast
    public Expr expr() {
        return this.expr;
    }

    @Override // polyglot.ast.Cast
    public Cast expr(Expr expr) {
        Cast_c n = (Cast_c) copy();
        n.expr = expr;
        return n;
    }

    protected Cast_c reconstruct(TypeNode castType, Expr expr) {
        if (castType != this.castType || expr != this.expr) {
            Cast_c n = (Cast_c) copy();
            n.castType = castType;
            n.expr = expr;
            return n;
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node visitChildren(NodeVisitor v) {
        TypeNode castType = (TypeNode) visitChild(this.castType, v);
        Expr expr = (Expr) visitChild(this.expr, v);
        return reconstruct(castType, expr);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        TypeSystem ts = tc.typeSystem();
        if (!ts.isCastValid(this.expr.type(), this.castType.type())) {
            throw new SemanticException(new StringBuffer().append("Cannot cast the expression of type \"").append(this.expr.type()).append("\" to type \"").append(this.castType.type()).append("\".").toString(), position());
        }
        return type(this.castType.type());
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.Node
    public Type childExpectedType(Expr child, AscriptionVisitor av) {
        TypeSystem ts = av.typeSystem();
        if (child == this.expr) {
            if (this.castType.type().isReference()) {
                return ts.Object();
            }
            if (this.castType.type().isNumeric()) {
                return ts.Double();
            }
            if (this.castType.type().isBoolean()) {
                return ts.Boolean();
            }
        }
        return child.type();
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        return new StringBuffer().append("(").append(this.castType).append(") ").append(this.expr).toString();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        w.begin(0);
        w.write("(");
        print(this.castType, w, tr);
        w.write(")");
        w.allowBreak(2, Instruction.argsep);
        printSubExpr(this.expr, w, tr);
        w.end();
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public Term entry() {
        return this.expr.entry();
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public List acceptCFG(CFGBuilder v, List succs) {
        v.visitCFG(this.expr, this);
        return succs;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public List throwTypes(TypeSystem ts) {
        if (this.expr.type().isReference()) {
            return Collections.singletonList(ts.ClassCastException());
        }
        return Collections.EMPTY_LIST;
    }

    @Override // polyglot.ext.jl.ast.Expr_c, polyglot.ast.Expr
    public boolean isConstant() {
        return this.expr.isConstant() && this.castType.type().isPrimitive();
    }

    @Override // polyglot.ext.jl.ast.Expr_c, polyglot.ast.Expr
    public Object constantValue() {
        Object v = this.expr.constantValue();
        if (v == null) {
            return null;
        }
        if ((v instanceof Boolean) && this.castType.type().isBoolean()) {
            return v;
        }
        if (v instanceof String) {
            TypeSystem ts = this.castType.type().typeSystem();
            if (this.castType.type().equals(ts.String())) {
                return v;
            }
        }
        if (v instanceof Double) {
            double vv = ((Double) v).doubleValue();
            if (this.castType.type().isDouble()) {
                return new Double(vv);
            }
            if (this.castType.type().isFloat()) {
                return new Float((float) vv);
            }
            if (this.castType.type().isLong()) {
                return new Long((long) vv);
            }
            if (this.castType.type().isInt()) {
                return new Integer((int) vv);
            }
            if (this.castType.type().isChar()) {
                return new Character((char) vv);
            }
            if (this.castType.type().isShort()) {
                return new Short((short) vv);
            }
            if (this.castType.type().isByte()) {
                return new Byte((byte) vv);
            }
        }
        if (v instanceof Float) {
            float vv2 = ((Float) v).floatValue();
            if (this.castType.type().isDouble()) {
                return new Double(vv2);
            }
            if (this.castType.type().isFloat()) {
                return new Float(vv2);
            }
            if (this.castType.type().isLong()) {
                return new Long(vv2);
            }
            if (this.castType.type().isInt()) {
                return new Integer((int) vv2);
            }
            if (this.castType.type().isChar()) {
                return new Character((char) vv2);
            }
            if (this.castType.type().isShort()) {
                return new Short((short) vv2);
            }
            if (this.castType.type().isByte()) {
                return new Byte((byte) vv2);
            }
        }
        if (v instanceof Number) {
            long vv3 = ((Number) v).longValue();
            if (this.castType.type().isDouble()) {
                return new Double(vv3);
            }
            if (this.castType.type().isFloat()) {
                return new Float((float) vv3);
            }
            if (this.castType.type().isLong()) {
                return new Long(vv3);
            }
            if (this.castType.type().isInt()) {
                return new Integer((int) vv3);
            }
            if (this.castType.type().isChar()) {
                return new Character((char) vv3);
            }
            if (this.castType.type().isShort()) {
                return new Short((short) vv3);
            }
            if (this.castType.type().isByte()) {
                return new Byte((byte) vv3);
            }
        }
        if (v instanceof Character) {
            char vv4 = ((Character) v).charValue();
            if (this.castType.type().isDouble()) {
                return new Double(vv4);
            }
            if (this.castType.type().isFloat()) {
                return new Float(vv4);
            }
            if (this.castType.type().isLong()) {
                return new Long(vv4);
            }
            if (this.castType.type().isInt()) {
                return new Integer(vv4);
            }
            if (this.castType.type().isChar()) {
                return new Character(vv4);
            }
            if (this.castType.type().isShort()) {
                return new Short((short) vv4);
            }
            if (this.castType.type().isByte()) {
                return new Byte((byte) vv4);
            }
            return null;
        }
        return null;
    }
}
