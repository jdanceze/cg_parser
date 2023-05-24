package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.Formal;
import polyglot.ast.Node;
import polyglot.ast.Term;
import polyglot.ast.TypeNode;
import polyglot.types.Context;
import polyglot.types.Flags;
import polyglot.types.LocalInstance;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.visit.AmbiguityRemover;
import polyglot.visit.CFGBuilder;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeBuilder;
import polyglot.visit.TypeChecker;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/Formal_c.class */
public class Formal_c extends Term_c implements Formal {
    protected LocalInstance li;
    protected Flags flags;
    protected TypeNode type;
    protected String name;
    boolean reachable;

    public Formal_c(Position pos, Flags flags, TypeNode type, String name) {
        super(pos);
        this.flags = flags;
        this.type = type;
        this.name = name;
    }

    @Override // polyglot.ast.VarDecl
    public Type declType() {
        return this.type.type();
    }

    @Override // polyglot.ast.VarDecl
    public Flags flags() {
        return this.flags;
    }

    @Override // polyglot.ast.Formal
    public Formal flags(Flags flags) {
        Formal_c n = (Formal_c) copy();
        n.flags = flags;
        return n;
    }

    @Override // polyglot.ast.VarDecl
    public TypeNode type() {
        return this.type;
    }

    @Override // polyglot.ast.Formal
    public Formal type(TypeNode type) {
        Formal_c n = (Formal_c) copy();
        n.type = type;
        return n;
    }

    @Override // polyglot.ast.VarDecl
    public String name() {
        return this.name;
    }

    @Override // polyglot.ast.Formal
    public Formal name(String name) {
        Formal_c n = (Formal_c) copy();
        n.name = name;
        return n;
    }

    @Override // polyglot.ast.VarDecl
    public LocalInstance localInstance() {
        return this.li;
    }

    @Override // polyglot.ast.Formal
    public Formal localInstance(LocalInstance li) {
        Formal_c n = (Formal_c) copy();
        n.li = li;
        return n;
    }

    protected Formal_c reconstruct(TypeNode type) {
        if (this.type != type) {
            Formal_c n = (Formal_c) copy();
            n.type = type;
            return n;
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node visitChildren(NodeVisitor v) {
        TypeNode type = (TypeNode) visitChild(this.type, v);
        return reconstruct(type);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void addDecls(Context c) {
        c.addVariable(this.li);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        w.write(this.flags.translate());
        print(this.type, w, tr);
        w.write(Instruction.argsep);
        w.write(this.name);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node buildTypes(TypeBuilder tb) throws SemanticException {
        Formal_c n = (Formal_c) super.buildTypes(tb);
        TypeSystem ts = tb.typeSystem();
        LocalInstance li = ts.localInstance(position(), Flags.NONE, ts.unknownType(position()), name());
        return n.localInstance(li);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node disambiguate(AmbiguityRemover ar) throws SemanticException {
        if (declType().isCanonical() && !this.li.type().isCanonical()) {
            TypeSystem ts = ar.typeSystem();
            LocalInstance li = ts.localInstance(position(), flags(), declType(), name());
            return localInstance(li);
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        TypeSystem ts = tc.typeSystem();
        try {
            ts.checkLocalFlags(flags());
            return this;
        } catch (SemanticException e) {
            throw new SemanticException(e.getMessage(), position());
        }
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public Term entry() {
        return this;
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public List acceptCFG(CFGBuilder v, List succs) {
        return succs;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.Node
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

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        return new StringBuffer().append(this.flags.translate()).append(this.type).append(Instruction.argsep).append(this.name).toString();
    }
}
