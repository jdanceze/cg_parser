package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.Local;
import polyglot.ast.Node;
import polyglot.ast.Precedence;
import polyglot.ast.Term;
import polyglot.types.Context;
import polyglot.types.Flags;
import polyglot.types.LocalInstance;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.visit.CFGBuilder;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeBuilder;
import polyglot.visit.TypeChecker;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/Local_c.class */
public class Local_c extends Expr_c implements Local {
    protected String name;
    protected LocalInstance li;

    public Local_c(Position pos, String name) {
        super(pos);
        this.name = name;
    }

    @Override // polyglot.ext.jl.ast.Expr_c, polyglot.ast.Expr
    public Precedence precedence() {
        return Precedence.LITERAL;
    }

    @Override // polyglot.ast.Local
    public String name() {
        return this.name;
    }

    @Override // polyglot.ast.Local
    public Local name(String name) {
        Local_c n = (Local_c) copy();
        n.name = name;
        return n;
    }

    @Override // polyglot.ast.Variable
    public Flags flags() {
        return this.li.flags();
    }

    @Override // polyglot.ast.Local
    public LocalInstance localInstance() {
        return this.li;
    }

    @Override // polyglot.ast.Local
    public Local localInstance(LocalInstance li) {
        Local_c n = (Local_c) copy();
        n.li = li;
        return n;
    }

    @Override // polyglot.ext.jl.ast.Expr_c, polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node buildTypes(TypeBuilder tb) throws SemanticException {
        Local_c n = (Local_c) super.buildTypes(tb);
        TypeSystem ts = tb.typeSystem();
        LocalInstance li = ts.localInstance(position(), Flags.NONE, ts.unknownType(position()), this.name);
        return n.localInstance(li);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        Context c = tc.context();
        LocalInstance li = c.findLocal(this.name);
        if (!c.isLocal(li.name()) && !li.flags().isFinal()) {
            throw new SemanticException(new StringBuffer().append("Local variable \"").append(li.name()).append("\" is accessed from an inner class, and must be declared ").append("final.").toString(), position());
        }
        return localInstance(li).type(li.type());
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public Term entry() {
        return this;
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public List acceptCFG(CFGBuilder v, List succs) {
        return succs;
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        return this.name;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        w.write(this.name);
    }

    @Override // polyglot.ext.jl.ast.Expr_c, polyglot.ext.jl.ast.Node_c, polyglot.ast.Node
    public void dump(CodeWriter w) {
        super.dump(w);
        if (this.li != null) {
            w.allowBreak(4, Instruction.argsep);
            w.begin(0);
            w.write(new StringBuffer().append("(instance ").append(this.li).append(")").toString());
            w.end();
        }
        w.allowBreak(4, Instruction.argsep);
        w.begin(0);
        w.write(new StringBuffer().append("(name ").append(this.name).append(")").toString());
        w.end();
    }

    @Override // polyglot.ext.jl.ast.Expr_c, polyglot.ast.Expr
    public boolean isConstant() {
        return this.li.isConstant();
    }

    @Override // polyglot.ext.jl.ast.Expr_c, polyglot.ast.Expr
    public Object constantValue() {
        return this.li.constantValue();
    }
}
