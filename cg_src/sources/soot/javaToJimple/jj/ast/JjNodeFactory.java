package soot.javaToJimple.jj.ast;

import polyglot.ast.Expr;
import polyglot.ast.NodeFactory;
import polyglot.util.Position;
/* loaded from: gencallgraphv3.jar:soot/javaToJimple/jj/ast/JjNodeFactory.class */
public interface JjNodeFactory extends NodeFactory {
    JjComma_c JjComma(Position position, Expr expr, Expr expr2);
}
