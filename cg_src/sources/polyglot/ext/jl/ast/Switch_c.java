package polyglot.ext.jl.ast;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import polyglot.ast.Case;
import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.Switch;
import polyglot.ast.SwitchElement;
import polyglot.ast.Term;
import polyglot.types.Context;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.CollectionUtil;
import polyglot.util.Position;
import polyglot.util.TypedList;
import polyglot.visit.AscriptionVisitor;
import polyglot.visit.CFGBuilder;
import polyglot.visit.FlowGraph;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/Switch_c.class */
public class Switch_c extends Stmt_c implements Switch {
    protected Expr expr;
    protected List elements;
    static Class class$polyglot$ast$SwitchElement;

    public Switch_c(Position pos, Expr expr, List elements) {
        super(pos);
        Class cls;
        this.expr = expr;
        if (class$polyglot$ast$SwitchElement == null) {
            cls = class$("polyglot.ast.SwitchElement");
            class$polyglot$ast$SwitchElement = cls;
        } else {
            cls = class$polyglot$ast$SwitchElement;
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

    @Override // polyglot.ast.Switch
    public Expr expr() {
        return this.expr;
    }

    @Override // polyglot.ast.Switch
    public Switch expr(Expr expr) {
        Switch_c n = (Switch_c) copy();
        n.expr = expr;
        return n;
    }

    @Override // polyglot.ast.Switch
    public List elements() {
        return Collections.unmodifiableList(this.elements);
    }

    @Override // polyglot.ast.Switch
    public Switch elements(List elements) {
        Class cls;
        Switch_c n = (Switch_c) copy();
        if (class$polyglot$ast$SwitchElement == null) {
            cls = class$("polyglot.ast.SwitchElement");
            class$polyglot$ast$SwitchElement = cls;
        } else {
            cls = class$polyglot$ast$SwitchElement;
        }
        n.elements = TypedList.copyAndCheck(elements, cls, true);
        return n;
    }

    protected Switch_c reconstruct(Expr expr, List elements) {
        Class cls;
        if (expr != this.expr || !CollectionUtil.equals(elements, this.elements)) {
            Switch_c n = (Switch_c) copy();
            n.expr = expr;
            if (class$polyglot$ast$SwitchElement == null) {
                cls = class$("polyglot.ast.SwitchElement");
                class$polyglot$ast$SwitchElement = cls;
            } else {
                cls = class$polyglot$ast$SwitchElement;
            }
            n.elements = TypedList.copyAndCheck(elements, cls, true);
            return n;
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Context enterScope(Context c) {
        return c.pushBlock();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node visitChildren(NodeVisitor v) {
        Expr expr = (Expr) visitChild(this.expr, v);
        List elements = visitList(this.elements, v);
        return reconstruct(expr, elements);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        Object key;
        String str;
        TypeSystem ts = tc.typeSystem();
        if (!ts.isImplicitCastValid(this.expr.type(), ts.Int())) {
            throw new SemanticException("Switch index must be an integer.", position());
        }
        Collection labels = new HashSet();
        for (SwitchElement s : this.elements) {
            if (s instanceof Case) {
                Case c = (Case) s;
                if (c.isDefault()) {
                    key = "default";
                    str = "default";
                } else if (c.expr().isConstant()) {
                    key = new Long(c.value());
                    str = new StringBuffer().append(c.expr().toString()).append(" (").append(c.value()).append(")").toString();
                } else {
                    continue;
                }
                if (labels.contains(key)) {
                    throw new SemanticException(new StringBuffer().append("Duplicate case label: ").append(str).append(".").toString(), c.position());
                }
                labels.add(key);
            }
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.Node
    public Type childExpectedType(Expr child, AscriptionVisitor av) {
        TypeSystem ts = av.typeSystem();
        if (child == this.expr) {
            return ts.Int();
        }
        return child.type();
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        return new StringBuffer().append("switch (").append(this.expr).append(") { ... }").toString();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        boolean z;
        w.write("switch (");
        printBlock(this.expr, w, tr);
        w.write(") {");
        w.allowBreak(4, Instruction.argsep);
        w.begin(0);
        boolean lastWasCase = false;
        boolean first = true;
        for (SwitchElement s : this.elements) {
            if (s instanceof Case) {
                if (lastWasCase) {
                    w.newline(0);
                } else if (!first) {
                    w.allowBreak(0, Instruction.argsep);
                }
                printBlock(s, w, tr);
                z = true;
            } else {
                w.allowBreak(4, Instruction.argsep);
                print(s, w, tr);
                z = false;
            }
            lastWasCase = z;
            first = false;
        }
        w.end();
        w.allowBreak(0, Instruction.argsep);
        w.write("}");
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public Term entry() {
        return this.expr.entry();
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public List acceptCFG(CFGBuilder v, List succs) {
        List cases = new LinkedList();
        boolean hasDefault = false;
        for (SwitchElement s : this.elements) {
            if (s instanceof Case) {
                cases.add(s.entry());
                if (((Case) s).expr() == null) {
                    hasDefault = true;
                }
            }
        }
        if (!hasDefault) {
            cases.add(this);
        }
        v.visitCFG(this.expr, FlowGraph.EDGE_KEY_OTHER, cases);
        v.push(this).visitCFGList(this.elements, this);
        return succs;
    }
}
