package polyglot.ext.jl.ast;

import polyglot.ast.CanonicalTypeNode;
import polyglot.ast.Node;
import polyglot.main.Options;
import polyglot.types.ClassType;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.Translator;
import polyglot.visit.TypeChecker;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/CanonicalTypeNode_c.class */
public class CanonicalTypeNode_c extends TypeNode_c implements CanonicalTypeNode {
    public CanonicalTypeNode_c(Position pos, Type type) {
        super(pos);
        this.type = type;
    }

    @Override // polyglot.ext.jl.ast.TypeNode_c, polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        if (this.type != null) {
            w.write(Translator.cScope(this.type.toString()));
        } else {
            w.write("<unknown-type>");
        }
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        TypeSystem ts = tc.typeSystem();
        if (this.type.isClass()) {
            ClassType ct = this.type.toClass();
            if ((ct.isTopLevel() || ct.isMember()) && !ts.classAccessible(ct, tc.context())) {
                throw new SemanticException(new StringBuffer().append("Cannot access class \"").append(ct).append("\" from the body of \"").append(tc.context().currentClass()).append("\".").toString(), position());
            }
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void translate(CodeWriter w, Translator tr) {
        TypeSystem ts = tr.typeSystem();
        if (tr.outerClass() != null) {
            w.write(this.type.translate(ts.classContextResolver(tr.outerClass())));
        } else if (Options.global.fully_qualified_names) {
            w.write(this.type.translate(null));
        } else {
            w.write(this.type.translate(tr.context()));
        }
    }

    @Override // polyglot.ext.jl.ast.TypeNode_c, polyglot.ext.jl.ast.Node_c
    public String toString() {
        return this.type == null ? "<unknown-type>" : this.type.toString();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.Node
    public void dump(CodeWriter w) {
        super.dump(w);
        w.allowBreak(4, Instruction.argsep);
        w.begin(0);
        w.write(new StringBuffer().append("(type ").append(this.type).append(")").toString());
        w.end();
    }
}
