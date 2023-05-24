package polyglot.ext.jl.ast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import polyglot.ast.Block;
import polyglot.ast.Node;
import polyglot.ast.Stmt;
import polyglot.ast.Term;
import polyglot.types.Context;
import polyglot.util.CodeWriter;
import polyglot.util.CollectionUtil;
import polyglot.util.Position;
import polyglot.util.TypedList;
import polyglot.visit.CFGBuilder;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/AbstractBlock_c.class */
public abstract class AbstractBlock_c extends Stmt_c implements Block {
    protected List statements;
    static Class class$polyglot$ast$Stmt;

    public AbstractBlock_c(Position pos, List statements) {
        super(pos);
        Class cls;
        if (class$polyglot$ast$Stmt == null) {
            cls = class$("polyglot.ast.Stmt");
            class$polyglot$ast$Stmt = cls;
        } else {
            cls = class$polyglot$ast$Stmt;
        }
        this.statements = TypedList.copyAndCheck(statements, cls, true);
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    @Override // polyglot.ast.Block
    public List statements() {
        return this.statements;
    }

    @Override // polyglot.ast.Block
    public Block statements(List statements) {
        Class cls;
        AbstractBlock_c n = (AbstractBlock_c) copy();
        if (class$polyglot$ast$Stmt == null) {
            cls = class$("polyglot.ast.Stmt");
            class$polyglot$ast$Stmt = cls;
        } else {
            cls = class$polyglot$ast$Stmt;
        }
        n.statements = TypedList.copyAndCheck(statements, cls, true);
        return n;
    }

    @Override // polyglot.ast.Block
    public Block append(Stmt stmt) {
        List l = new ArrayList(this.statements.size() + 1);
        l.addAll(this.statements);
        l.add(stmt);
        return statements(l);
    }

    @Override // polyglot.ast.Block
    public Block prepend(Stmt stmt) {
        List l = new ArrayList(this.statements.size() + 1);
        l.add(stmt);
        l.addAll(this.statements);
        return statements(l);
    }

    protected AbstractBlock_c reconstruct(List statements) {
        Class cls;
        if (!CollectionUtil.equals(statements, this.statements)) {
            AbstractBlock_c n = (AbstractBlock_c) copy();
            if (class$polyglot$ast$Stmt == null) {
                cls = class$("polyglot.ast.Stmt");
                class$polyglot$ast$Stmt = cls;
            } else {
                cls = class$polyglot$ast$Stmt;
            }
            n.statements = TypedList.copyAndCheck(statements, cls, true);
            return n;
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node visitChildren(NodeVisitor v) {
        List statements = visitList(this.statements, v);
        return reconstruct(statements);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Context enterScope(Context c) {
        return c.pushBlock();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        w.begin(0);
        Iterator i = this.statements.iterator();
        while (i.hasNext()) {
            Stmt n = (Stmt) i.next();
            printBlock(n, w, tr);
            if (i.hasNext()) {
                w.newline(0);
            }
        }
        w.end();
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public Term entry() {
        return listEntry(this.statements, this);
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public List acceptCFG(CFGBuilder v, List succs) {
        v.visitCFGList(this.statements, this);
        return succs;
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        int count = 0;
        Iterator i = this.statements.iterator();
        while (true) {
            if (!i.hasNext()) {
                break;
            }
            int i2 = count;
            count++;
            if (i2 > 2) {
                sb.append(" ...");
                break;
            }
            Stmt n = (Stmt) i.next();
            sb.append(Instruction.argsep);
            sb.append(n.toString());
        }
        sb.append(" }");
        return sb.toString();
    }
}
