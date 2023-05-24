package polyglot.ast;

import java.util.List;
import polyglot.types.ConstructorInstance;
import polyglot.types.ParsedClassType;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/New.class */
public interface New extends Expr, ProcedureCall {
    ParsedClassType anonType();

    New anonType(ParsedClassType parsedClassType);

    ConstructorInstance constructorInstance();

    New constructorInstance(ConstructorInstance constructorInstance);

    Expr qualifier();

    New qualifier(Expr expr);

    TypeNode objectType();

    New objectType(TypeNode typeNode);

    @Override // polyglot.ast.ProcedureCall
    List arguments();

    @Override // polyglot.ast.ProcedureCall
    ProcedureCall arguments(List list);

    ClassBody body();

    New body(ClassBody classBody);
}
