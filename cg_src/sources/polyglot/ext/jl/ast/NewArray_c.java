package polyglot.ext.jl.ast;

import java.util.Collections;
import java.util.List;
import polyglot.ast.ArrayInit;
import polyglot.ast.Expr;
import polyglot.ast.NewArray;
import polyglot.ast.Node;
import polyglot.ast.Term;
import polyglot.ast.TypeNode;
import polyglot.types.ArrayType;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.CollectionUtil;
import polyglot.util.Position;
import polyglot.util.TypedList;
import polyglot.visit.AscriptionVisitor;
import polyglot.visit.CFGBuilder;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/NewArray_c.class */
public class NewArray_c extends Expr_c implements NewArray {
    protected TypeNode baseType;
    protected List dims;
    protected int addDims;
    protected ArrayInit init;
    static Class class$polyglot$ast$Expr;

    public NewArray_c(Position pos, TypeNode baseType, List dims, int addDims, ArrayInit init) {
        super(pos);
        Class cls;
        this.baseType = baseType;
        if (class$polyglot$ast$Expr == null) {
            cls = class$("polyglot.ast.Expr");
            class$polyglot$ast$Expr = cls;
        } else {
            cls = class$polyglot$ast$Expr;
        }
        this.dims = TypedList.copyAndCheck(dims, cls, true);
        this.addDims = addDims;
        this.init = init;
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    @Override // polyglot.ast.NewArray
    public TypeNode baseType() {
        return this.baseType;
    }

    @Override // polyglot.ast.NewArray
    public NewArray baseType(TypeNode baseType) {
        NewArray_c n = (NewArray_c) copy();
        n.baseType = baseType;
        return n;
    }

    @Override // polyglot.ast.NewArray
    public List dims() {
        return Collections.unmodifiableList(this.dims);
    }

    @Override // polyglot.ast.NewArray
    public NewArray dims(List dims) {
        Class cls;
        NewArray_c n = (NewArray_c) copy();
        if (class$polyglot$ast$Expr == null) {
            cls = class$("polyglot.ast.Expr");
            class$polyglot$ast$Expr = cls;
        } else {
            cls = class$polyglot$ast$Expr;
        }
        n.dims = TypedList.copyAndCheck(dims, cls, true);
        return n;
    }

    @Override // polyglot.ast.NewArray
    public int numDims() {
        return this.dims.size() + this.addDims;
    }

    @Override // polyglot.ast.NewArray
    public int additionalDims() {
        return this.addDims;
    }

    @Override // polyglot.ast.NewArray
    public NewArray additionalDims(int addDims) {
        NewArray_c n = (NewArray_c) copy();
        n.addDims = addDims;
        return n;
    }

    @Override // polyglot.ast.NewArray
    public ArrayInit init() {
        return this.init;
    }

    @Override // polyglot.ast.NewArray
    public NewArray init(ArrayInit init) {
        NewArray_c n = (NewArray_c) copy();
        n.init = init;
        return n;
    }

    protected NewArray_c reconstruct(TypeNode baseType, List dims, ArrayInit init) {
        Class cls;
        if (baseType != this.baseType || !CollectionUtil.equals(dims, this.dims) || init != this.init) {
            NewArray_c n = (NewArray_c) copy();
            n.baseType = baseType;
            if (class$polyglot$ast$Expr == null) {
                cls = class$("polyglot.ast.Expr");
                class$polyglot$ast$Expr = cls;
            } else {
                cls = class$polyglot$ast$Expr;
            }
            n.dims = TypedList.copyAndCheck(dims, cls, true);
            n.init = init;
            return n;
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node visitChildren(NodeVisitor v) {
        TypeNode baseType = (TypeNode) visitChild(this.baseType, v);
        List dims = visitList(this.dims, v);
        ArrayInit init = (ArrayInit) visitChild(this.init, v);
        return reconstruct(baseType, dims, init);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        TypeSystem ts = tc.typeSystem();
        for (Expr expr : this.dims) {
            if (!ts.isImplicitCastValid(expr.type(), ts.Int())) {
                throw new SemanticException("Array dimension must be an integer.", expr.position());
            }
        }
        ArrayType type = ts.arrayOf(this.baseType.type(), this.dims.size() + this.addDims);
        if (this.init != null) {
            this.init.typeCheckElements(type);
        }
        return type(type);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.Node
    public Type childExpectedType(Expr child, AscriptionVisitor av) {
        if (child == this.init) {
            return this.type;
        }
        return child.type();
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        return new StringBuffer().append("new ").append(this.baseType).append("[...]").toString();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        w.write("new ");
        print(this.baseType, w, tr);
        for (Expr e : this.dims) {
            w.write("[");
            printBlock(e, w, tr);
            w.write("]");
        }
        for (int i = 0; i < this.addDims; i++) {
            w.write("[]");
        }
        if (this.init != null) {
            w.write(Instruction.argsep);
            print(this.init, w, tr);
        }
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public Term entry() {
        return listEntry(this.dims, this.init != null ? this.init.entry() : this);
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public List acceptCFG(CFGBuilder v, List succs) {
        v.visitCFGList(this.dims, this.init != null ? this.init.entry() : this);
        if (this.init != null) {
            v.visitCFG(this.init, this);
        }
        return succs;
    }
}
