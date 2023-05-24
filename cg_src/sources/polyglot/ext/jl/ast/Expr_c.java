package polyglot.ext.jl.ast;

import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.Precedence;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeBuilder;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/Expr_c.class */
public abstract class Expr_c extends Term_c implements Expr {
    protected Type type;

    public Expr_c(Position pos) {
        super(pos);
    }

    @Override // polyglot.ast.Typed
    public Type type() {
        return this.type;
    }

    @Override // polyglot.ast.Expr
    public Expr type(Type type) {
        if (type == this.type) {
            return this;
        }
        Expr_c n = (Expr_c) copy();
        n.type = type;
        return n;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.Node
    public void dump(CodeWriter w) {
        super.dump(w);
        if (this.type != null) {
            w.allowBreak(4, Instruction.argsep);
            w.begin(0);
            w.write(new StringBuffer().append("(type ").append(this.type).append(")").toString());
            w.end();
        }
    }

    public Precedence precedence() {
        return Precedence.UNKNOWN;
    }

    public boolean isConstant() {
        return false;
    }

    public Object constantValue() {
        return null;
    }

    public String stringValue() {
        return (String) constantValue();
    }

    public boolean booleanValue() {
        return ((Boolean) constantValue()).booleanValue();
    }

    public byte byteValue() {
        return ((Byte) constantValue()).byteValue();
    }

    public short shortValue() {
        return ((Short) constantValue()).shortValue();
    }

    public char charValue() {
        return ((Character) constantValue()).charValue();
    }

    public int intValue() {
        return ((Integer) constantValue()).intValue();
    }

    public long longValue() {
        return ((Long) constantValue()).longValue();
    }

    public float floatValue() {
        return ((Float) constantValue()).floatValue();
    }

    public double doubleValue() {
        return ((Double) constantValue()).doubleValue();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node buildTypes(TypeBuilder tb) throws SemanticException {
        return type(tb.typeSystem().unknownType(position()));
    }

    @Override // polyglot.ast.Expr
    public void printSubExpr(Expr expr, CodeWriter w, PrettyPrinter pp) {
        printSubExpr(expr, true, w, pp);
    }

    @Override // polyglot.ast.Expr
    public void printSubExpr(Expr expr, boolean associative, CodeWriter w, PrettyPrinter pp) {
        if ((!associative && precedence().equals(expr.precedence())) || precedence().isTighter(expr.precedence())) {
            w.write("(");
            printBlock(expr, w, pp);
            w.write(")");
            return;
        }
        printBlock(expr, w, pp);
    }
}
