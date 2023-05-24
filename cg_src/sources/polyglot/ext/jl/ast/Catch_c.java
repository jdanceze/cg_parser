package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.Block;
import polyglot.ast.Catch;
import polyglot.ast.Formal;
import polyglot.ast.Node;
import polyglot.ast.Term;
import polyglot.types.Context;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.visit.CFGBuilder;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/Catch_c.class */
public class Catch_c extends Stmt_c implements Catch {
    protected Formal formal;
    protected Block body;

    public Catch_c(Position pos, Formal formal, Block body) {
        super(pos);
        this.formal = formal;
        this.body = body;
    }

    @Override // polyglot.ast.Catch
    public Type catchType() {
        return this.formal.declType();
    }

    @Override // polyglot.ast.Catch
    public Formal formal() {
        return this.formal;
    }

    @Override // polyglot.ast.Catch
    public Catch formal(Formal formal) {
        Catch_c n = (Catch_c) copy();
        n.formal = formal;
        return n;
    }

    @Override // polyglot.ast.Catch
    public Block body() {
        return this.body;
    }

    @Override // polyglot.ast.Catch
    public Catch body(Block body) {
        Catch_c n = (Catch_c) copy();
        n.body = body;
        return n;
    }

    protected Catch_c reconstruct(Formal formal, Block body) {
        if (formal != this.formal || body != this.body) {
            Catch_c n = (Catch_c) copy();
            n.formal = formal;
            n.body = body;
            return n;
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node visitChildren(NodeVisitor v) {
        Formal formal = (Formal) visitChild(this.formal, v);
        Block body = (Block) visitChild(this.body, v);
        return reconstruct(formal, body);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Context enterScope(Context c) {
        return c.pushBlock();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        TypeSystem ts = tc.typeSystem();
        if (!catchType().isThrowable()) {
            throw new SemanticException(new StringBuffer().append("Can only throw subclasses of \"").append(ts.Throwable()).append("\".").toString(), this.formal.position());
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        return new StringBuffer().append("catch (").append(this.formal).append(") ").append(this.body).toString();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        w.write("catch (");
        printBlock(this.formal, w, tr);
        w.write(")");
        printSubStmt(this.body, w, tr);
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public Term entry() {
        return this.formal.entry();
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public List acceptCFG(CFGBuilder v, List succs) {
        v.visitCFG(this.formal, this.body.entry());
        v.visitCFG(this.body, this);
        return succs;
    }
}
