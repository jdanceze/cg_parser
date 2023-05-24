package soot.javaToJimple.jj.ast;

import polyglot.ast.Expr;
import polyglot.ast.TypeNode;
import polyglot.ext.jl.ast.FieldDecl_c;
import polyglot.types.Flags;
import polyglot.types.Type;
import polyglot.util.Position;
import polyglot.visit.AscriptionVisitor;
/* loaded from: gencallgraphv3.jar:soot/javaToJimple/jj/ast/JjFieldDecl_c.class */
public class JjFieldDecl_c extends FieldDecl_c {
    public JjFieldDecl_c(Position pos, Flags flags, TypeNode type, String name, Expr init) {
        super(pos, flags, type, name, init);
    }

    @Override // polyglot.ext.jl.ast.FieldDecl_c, polyglot.ext.jl.ast.Node_c, polyglot.ast.Node
    public Type childExpectedType(Expr child, AscriptionVisitor av) {
        return type().type();
    }
}
