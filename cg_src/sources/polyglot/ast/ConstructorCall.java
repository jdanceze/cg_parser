package polyglot.ast;

import java.util.List;
import polyglot.types.ConstructorInstance;
import polyglot.util.Enum;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/ConstructorCall.class */
public interface ConstructorCall extends Stmt, ProcedureCall {
    public static final Kind SUPER = new Kind("super");
    public static final Kind THIS = new Kind("this");

    Expr qualifier();

    ConstructorCall qualifier(Expr expr);

    Kind kind();

    ConstructorCall kind(Kind kind);

    @Override // polyglot.ast.ProcedureCall
    List arguments();

    @Override // polyglot.ast.ProcedureCall
    ProcedureCall arguments(List list);

    ConstructorInstance constructorInstance();

    ConstructorCall constructorInstance(ConstructorInstance constructorInstance);

    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/ConstructorCall$Kind.class */
    public static class Kind extends Enum {
        public Kind(String name) {
            super(name);
        }
    }
}
