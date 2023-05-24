package soot.javaToJimple.jj.ast;

import java.util.List;
import polyglot.ast.Expr;
import polyglot.ext.jl.ast.ArrayInit_c;
import polyglot.types.Type;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.visit.AscriptionVisitor;
/* loaded from: gencallgraphv3.jar:soot/javaToJimple/jj/ast/JjArrayInit_c.class */
public class JjArrayInit_c extends ArrayInit_c {
    public JjArrayInit_c(Position pos, List elements) {
        super(pos, elements);
    }

    @Override // polyglot.ext.jl.ast.ArrayInit_c, polyglot.ext.jl.ast.Node_c, polyglot.ast.Node
    public Type childExpectedType(Expr child, AscriptionVisitor av) {
        if (this.elements.isEmpty()) {
            return child.type();
        }
        Type t = av.toType();
        if (t == null) {
            return child.type();
        }
        if (!t.isArray()) {
            throw new InternalCompilerError("Type of array initializer must be an array.", position());
        }
        Type t2 = t.toArray().base();
        for (Expr e : this.elements) {
            if (e == child) {
                return t2;
            }
        }
        return child.type();
    }
}
