package polyglot.ext.jl.ast;

import java.util.Iterator;
import java.util.List;
import polyglot.ast.ArrayInit;
import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.Term;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.CollectionUtil;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.util.TypedList;
import polyglot.visit.AscriptionVisitor;
import polyglot.visit.CFGBuilder;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/ArrayInit_c.class */
public class ArrayInit_c extends Expr_c implements ArrayInit {
    protected List elements;
    static Class class$polyglot$ast$Expr;

    public ArrayInit_c(Position pos, List elements) {
        super(pos);
        Class cls;
        if (class$polyglot$ast$Expr == null) {
            cls = class$("polyglot.ast.Expr");
            class$polyglot$ast$Expr = cls;
        } else {
            cls = class$polyglot$ast$Expr;
        }
        this.elements = TypedList.copyAndCheck(elements, cls, true);
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    @Override // polyglot.ast.ArrayInit
    public List elements() {
        return this.elements;
    }

    @Override // polyglot.ast.ArrayInit
    public ArrayInit elements(List elements) {
        Class cls;
        ArrayInit_c n = (ArrayInit_c) copy();
        if (class$polyglot$ast$Expr == null) {
            cls = class$("polyglot.ast.Expr");
            class$polyglot$ast$Expr = cls;
        } else {
            cls = class$polyglot$ast$Expr;
        }
        n.elements = TypedList.copyAndCheck(elements, cls, true);
        return n;
    }

    protected ArrayInit_c reconstruct(List elements) {
        Class cls;
        if (!CollectionUtil.equals(elements, this.elements)) {
            ArrayInit_c n = (ArrayInit_c) copy();
            if (class$polyglot$ast$Expr == null) {
                cls = class$("polyglot.ast.Expr");
                class$polyglot$ast$Expr = cls;
            } else {
                cls = class$polyglot$ast$Expr;
            }
            n.elements = TypedList.copyAndCheck(elements, cls, true);
            return n;
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node visitChildren(NodeVisitor v) {
        List elements = visitList(this.elements, v);
        return reconstruct(elements);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        TypeSystem ts = tc.typeSystem();
        Type type = null;
        for (Expr e : this.elements) {
            if (type == null) {
                type = e.type();
            } else {
                type = ts.leastCommonAncestor(type, e.type());
            }
        }
        if (type == null) {
            return type(ts.Null());
        }
        return type(ts.arrayOf(type));
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.Node
    public Type childExpectedType(Expr child, AscriptionVisitor av) {
        if (this.elements.isEmpty()) {
            return child.type();
        }
        Type t = av.toType();
        if (!t.isArray()) {
            throw new InternalCompilerError("Type of array initializer must be an array.", position());
        }
        Type t2 = t.toArray().base();
        TypeSystem ts = av.typeSystem();
        for (Expr e : this.elements) {
            if (e == child) {
                if (ts.numericConversionValid(t2, e.constantValue())) {
                    return child.type();
                }
                return t2;
            }
        }
        return child.type();
    }

    @Override // polyglot.ast.ArrayInit
    public void typeCheckElements(Type lhsType) throws SemanticException {
        TypeSystem ts = lhsType.typeSystem();
        if (!lhsType.isArray()) {
            throw new SemanticException(new StringBuffer().append("Cannot initialize ").append(lhsType).append(" with ").append(this.type).append(".").toString(), position());
        }
        Type t = lhsType.toArray().base();
        for (Expr e : this.elements) {
            Type s = e.type();
            if (e instanceof ArrayInit) {
                ((ArrayInit) e).typeCheckElements(t);
            } else if (!ts.isImplicitCastValid(s, t) && !ts.equals(s, t) && !ts.numericConversionValid(t, e.constantValue())) {
                throw new SemanticException(new StringBuffer().append("Cannot assign ").append(s).append(" to ").append(t).append(".").toString(), e.position());
            }
        }
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        return "{ ... }";
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        w.write("{ ");
        Iterator i = this.elements.iterator();
        while (i.hasNext()) {
            Expr e = (Expr) i.next();
            print(e, w, tr);
            if (i.hasNext()) {
                w.write(", ");
            }
        }
        w.write(" }");
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public Term entry() {
        return listEntry(this.elements, this);
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public List acceptCFG(CFGBuilder v, List succs) {
        v.visitCFGList(this.elements, this);
        return succs;
    }
}
