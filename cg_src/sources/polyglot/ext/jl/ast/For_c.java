package polyglot.ext.jl.ast;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import polyglot.ast.Expr;
import polyglot.ast.For;
import polyglot.ast.ForInit;
import polyglot.ast.ForUpdate;
import polyglot.ast.LocalDecl;
import polyglot.ast.Node;
import polyglot.ast.Stmt;
import polyglot.ast.Term;
import polyglot.types.Context;
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
import polyglot.visit.FlowGraph;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/For_c.class */
public class For_c extends Loop_c implements For {
    protected List inits;
    protected Expr cond;
    protected List iters;
    protected Stmt body;
    static Class class$polyglot$ast$ForInit;
    static Class class$polyglot$ast$ForUpdate;

    public For_c(Position pos, List inits, Expr cond, List iters, Stmt body) {
        super(pos);
        Class cls;
        Class cls2;
        if (class$polyglot$ast$ForInit == null) {
            cls = class$("polyglot.ast.ForInit");
            class$polyglot$ast$ForInit = cls;
        } else {
            cls = class$polyglot$ast$ForInit;
        }
        this.inits = TypedList.copyAndCheck(inits, cls, true);
        this.cond = cond;
        if (class$polyglot$ast$ForUpdate == null) {
            cls2 = class$("polyglot.ast.ForUpdate");
            class$polyglot$ast$ForUpdate = cls2;
        } else {
            cls2 = class$polyglot$ast$ForUpdate;
        }
        this.iters = TypedList.copyAndCheck(iters, cls2, true);
        this.body = body;
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    @Override // polyglot.ast.For
    public List inits() {
        return Collections.unmodifiableList(this.inits);
    }

    @Override // polyglot.ast.For
    public For inits(List inits) {
        Class cls;
        For_c n = (For_c) copy();
        if (class$polyglot$ast$ForInit == null) {
            cls = class$("polyglot.ast.ForInit");
            class$polyglot$ast$ForInit = cls;
        } else {
            cls = class$polyglot$ast$ForInit;
        }
        n.inits = TypedList.copyAndCheck(inits, cls, true);
        return n;
    }

    @Override // polyglot.ast.Loop
    public Expr cond() {
        return this.cond;
    }

    @Override // polyglot.ast.For
    public For cond(Expr cond) {
        For_c n = (For_c) copy();
        n.cond = cond;
        return n;
    }

    @Override // polyglot.ast.For
    public List iters() {
        return Collections.unmodifiableList(this.iters);
    }

    @Override // polyglot.ast.For
    public For iters(List iters) {
        Class cls;
        For_c n = (For_c) copy();
        if (class$polyglot$ast$ForUpdate == null) {
            cls = class$("polyglot.ast.ForUpdate");
            class$polyglot$ast$ForUpdate = cls;
        } else {
            cls = class$polyglot$ast$ForUpdate;
        }
        n.iters = TypedList.copyAndCheck(iters, cls, true);
        return n;
    }

    @Override // polyglot.ast.Loop
    public Stmt body() {
        return this.body;
    }

    @Override // polyglot.ast.For
    public For body(Stmt body) {
        For_c n = (For_c) copy();
        n.body = body;
        return n;
    }

    protected For_c reconstruct(List inits, Expr cond, List iters, Stmt body) {
        Class cls;
        Class cls2;
        if (!CollectionUtil.equals(inits, this.inits) || cond != this.cond || !CollectionUtil.equals(iters, this.iters) || body != this.body) {
            For_c n = (For_c) copy();
            if (class$polyglot$ast$ForInit == null) {
                cls = class$("polyglot.ast.ForInit");
                class$polyglot$ast$ForInit = cls;
            } else {
                cls = class$polyglot$ast$ForInit;
            }
            n.inits = TypedList.copyAndCheck(inits, cls, true);
            n.cond = cond;
            if (class$polyglot$ast$ForUpdate == null) {
                cls2 = class$("polyglot.ast.ForUpdate");
                class$polyglot$ast$ForUpdate = cls2;
            } else {
                cls2 = class$polyglot$ast$ForUpdate;
            }
            n.iters = TypedList.copyAndCheck(iters, cls2, true);
            n.body = body;
            return n;
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node visitChildren(NodeVisitor v) {
        List inits = visitList(this.inits, v);
        Expr cond = (Expr) visitChild(this.cond, v);
        List iters = visitList(this.iters, v);
        Stmt body = (Stmt) visitChild(this.body, v);
        return reconstruct(inits, cond, iters, body);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Context enterScope(Context c) {
        return c.pushBlock();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        TypeSystem ts = tc.typeSystem();
        Type t = null;
        for (ForInit s : this.inits) {
            if (s instanceof LocalDecl) {
                LocalDecl d = (LocalDecl) s;
                Type dt = d.type().type();
                if (t == null) {
                    t = dt;
                } else if (!t.equals(dt)) {
                    throw new InternalCompilerError(new StringBuffer().append("Local variable declarations in a for loop initializer must all be the same type, in this case ").append(t).append(", not ").append(dt).append(".").toString(), d.position());
                }
            }
        }
        if (this.cond != null && !ts.isImplicitCastValid(this.cond.type(), ts.Boolean())) {
            throw new SemanticException("The condition of a for statement must have boolean type.", this.cond.position());
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.Node
    public Type childExpectedType(Expr child, AscriptionVisitor av) {
        TypeSystem ts = av.typeSystem();
        if (child == this.cond) {
            return ts.Boolean();
        }
        return child.type();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        w.write("for (");
        w.begin(0);
        if (this.inits != null) {
            boolean first = true;
            Iterator i = this.inits.iterator();
            while (i.hasNext()) {
                ForInit s = (ForInit) i.next();
                printForInit(s, w, tr, first);
                first = false;
                if (i.hasNext()) {
                    w.write(",");
                    w.allowBreak(2, Instruction.argsep);
                }
            }
        }
        w.write(";");
        w.allowBreak(0);
        if (this.cond != null) {
            printBlock(this.cond, w, tr);
        }
        w.write(";");
        w.allowBreak(0);
        if (this.iters != null) {
            Iterator i2 = this.iters.iterator();
            while (i2.hasNext()) {
                ForUpdate s2 = (ForUpdate) i2.next();
                printForUpdate(s2, w, tr);
                if (i2.hasNext()) {
                    w.write(",");
                    w.allowBreak(2, Instruction.argsep);
                }
            }
        }
        w.end();
        w.write(")");
        printSubStmt(this.body, w, tr);
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        return "for (...) ...";
    }

    private void printForInit(ForInit s, CodeWriter w, PrettyPrinter tr, boolean printType) {
        boolean oldSemiColon = tr.appendSemicolon(false);
        boolean oldPrintType = tr.printType(printType);
        printBlock(s, w, tr);
        tr.printType(oldPrintType);
        tr.appendSemicolon(oldSemiColon);
    }

    private void printForUpdate(ForUpdate s, CodeWriter w, PrettyPrinter tr) {
        boolean oldSemiColon = tr.appendSemicolon(false);
        printBlock(s, w, tr);
        tr.appendSemicolon(oldSemiColon);
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public Term entry() {
        return listEntry(this.inits, this.cond != null ? this.cond.entry() : this.body.entry());
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public List acceptCFG(CFGBuilder v, List succs) {
        v.visitCFGList(this.inits, this.cond != null ? this.cond.entry() : this.body.entry());
        if (this.cond != null) {
            if (condIsConstantTrue()) {
                v.visitCFG(this.cond, this.body.entry());
            } else {
                v.visitCFG(this.cond, FlowGraph.EDGE_KEY_TRUE, this.body.entry(), FlowGraph.EDGE_KEY_FALSE, this);
            }
        }
        v.push(this).visitCFG(this.body, continueTarget());
        v.visitCFGList(this.iters, this.cond != null ? this.cond.entry() : this.body.entry());
        return succs;
    }

    @Override // polyglot.ast.Loop
    public Term continueTarget() {
        return listEntry(this.iters, this.cond != null ? this.cond.entry() : this.body.entry());
    }
}
