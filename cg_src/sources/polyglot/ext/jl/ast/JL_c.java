package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.JL;
import polyglot.ast.Node;
import polyglot.types.Context;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.visit.AddMemberVisitor;
import polyglot.visit.AmbiguityRemover;
import polyglot.visit.ExceptionChecker;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.Translator;
import polyglot.visit.TypeBuilder;
import polyglot.visit.TypeChecker;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/JL_c.class */
public class JL_c extends Ext_c implements JL {
    public JL jl() {
        return node();
    }

    @Override // polyglot.ast.NodeOps
    public Node visitChildren(NodeVisitor v) {
        return jl().visitChildren(v);
    }

    @Override // polyglot.ast.NodeOps
    public Context enterScope(Context c) {
        return jl().enterScope(c);
    }

    @Override // polyglot.ast.NodeOps
    public Context enterScope(Node child, Context c) {
        return jl().enterScope(child, c);
    }

    @Override // polyglot.ast.NodeOps
    public void addDecls(Context c) {
        jl().addDecls(c);
    }

    @Override // polyglot.ast.NodeOps
    public NodeVisitor buildTypesEnter(TypeBuilder tb) throws SemanticException {
        return jl().buildTypesEnter(tb);
    }

    @Override // polyglot.ast.NodeOps
    public Node buildTypes(TypeBuilder tb) throws SemanticException {
        return jl().buildTypes(tb);
    }

    @Override // polyglot.ast.NodeOps
    public NodeVisitor disambiguateEnter(AmbiguityRemover ar) throws SemanticException {
        return jl().disambiguateEnter(ar);
    }

    @Override // polyglot.ast.NodeOps
    public Node disambiguate(AmbiguityRemover ar) throws SemanticException {
        return jl().disambiguate(ar);
    }

    @Override // polyglot.ast.NodeOps
    public NodeVisitor addMembersEnter(AddMemberVisitor am) throws SemanticException {
        return jl().addMembersEnter(am);
    }

    @Override // polyglot.ast.NodeOps
    public Node addMembers(AddMemberVisitor am) throws SemanticException {
        return jl().addMembers(am);
    }

    @Override // polyglot.ast.NodeOps
    public NodeVisitor typeCheckEnter(TypeChecker tc) throws SemanticException {
        return jl().typeCheckEnter(tc);
    }

    @Override // polyglot.ast.NodeOps
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        return jl().typeCheck(tc);
    }

    @Override // polyglot.ast.NodeOps
    public NodeVisitor exceptionCheckEnter(ExceptionChecker ec) throws SemanticException {
        return jl().exceptionCheckEnter(ec);
    }

    @Override // polyglot.ast.NodeOps
    public Node exceptionCheck(ExceptionChecker ec) throws SemanticException {
        return jl().exceptionCheck(ec);
    }

    @Override // polyglot.ast.NodeOps
    public List throwTypes(TypeSystem ts) {
        return jl().throwTypes(ts);
    }

    @Override // polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter pp) {
        jl().prettyPrint(w, pp);
    }

    @Override // polyglot.ast.NodeOps
    public void translate(CodeWriter w, Translator tr) {
        jl().translate(w, tr);
    }
}
