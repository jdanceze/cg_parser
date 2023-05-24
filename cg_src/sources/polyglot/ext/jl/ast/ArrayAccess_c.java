package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.ArrayAccess;
import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.Precedence;
import polyglot.ast.Term;
import polyglot.types.Flags;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.CollectionUtil;
import polyglot.util.Position;
import polyglot.visit.AscriptionVisitor;
import polyglot.visit.CFGBuilder;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/ArrayAccess_c.class */
public class ArrayAccess_c extends Expr_c implements ArrayAccess {
    protected Expr array;
    protected Expr index;

    public ArrayAccess_c(Position pos, Expr array, Expr index) {
        super(pos);
        this.array = array;
        this.index = index;
    }

    @Override // polyglot.ext.jl.ast.Expr_c, polyglot.ast.Expr
    public Precedence precedence() {
        return Precedence.LITERAL;
    }

    @Override // polyglot.ast.ArrayAccess
    public Expr array() {
        return this.array;
    }

    @Override // polyglot.ast.ArrayAccess
    public ArrayAccess array(Expr array) {
        ArrayAccess_c n = (ArrayAccess_c) copy();
        n.array = array;
        return n;
    }

    @Override // polyglot.ast.ArrayAccess
    public Expr index() {
        return this.index;
    }

    @Override // polyglot.ast.ArrayAccess
    public ArrayAccess index(Expr index) {
        ArrayAccess_c n = (ArrayAccess_c) copy();
        n.index = index;
        return n;
    }

    @Override // polyglot.ast.Variable
    public Flags flags() {
        return Flags.NONE;
    }

    protected ArrayAccess_c reconstruct(Expr array, Expr index) {
        if (array != this.array || index != this.index) {
            ArrayAccess_c n = (ArrayAccess_c) copy();
            n.array = array;
            n.index = index;
            return n;
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node visitChildren(NodeVisitor v) {
        Expr array = (Expr) visitChild(this.array, v);
        Expr index = (Expr) visitChild(this.index, v);
        return reconstruct(array, index);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        TypeSystem ts = tc.typeSystem();
        if (!this.array.type().isArray()) {
            throw new SemanticException("Subscript can only follow an array type.", position());
        }
        if (!ts.isImplicitCastValid(this.index.type(), ts.Int())) {
            throw new SemanticException("Array subscript must be an integer.", position());
        }
        return type(this.array.type().toArray().base());
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.Node
    public Type childExpectedType(Expr child, AscriptionVisitor av) {
        TypeSystem ts = av.typeSystem();
        if (child == this.index) {
            return ts.Int();
        }
        if (child == this.array) {
            return ts.arrayOf(this.type);
        }
        return child.type();
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        return new StringBuffer().append(this.array).append("[").append(this.index).append("]").toString();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        printSubExpr(this.array, w, tr);
        w.write("[");
        printBlock(this.index, w, tr);
        w.write("]");
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public Term entry() {
        return this.array.entry();
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public List acceptCFG(CFGBuilder v, List succs) {
        v.visitCFG(this.array, this.index.entry());
        v.visitCFG(this.index, this);
        return succs;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public List throwTypes(TypeSystem ts) {
        return CollectionUtil.list(ts.OutOfBoundsException(), ts.NullPointerException());
    }
}
