package polyglot.ast;

import polyglot.types.Type;
import polyglot.util.CodeWriter;
import polyglot.visit.PrettyPrinter;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/Expr.class */
public interface Expr extends Receiver, Term {
    Expr type(Type type);

    Precedence precedence();

    boolean isConstant();

    Object constantValue();

    void printSubExpr(Expr expr, boolean z, CodeWriter codeWriter, PrettyPrinter prettyPrinter);

    void printSubExpr(Expr expr, CodeWriter codeWriter, PrettyPrinter prettyPrinter);
}
