package polyglot.ast;

import java.util.List;
import polyglot.types.MethodInstance;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/Call.class */
public interface Call extends Expr, ProcedureCall {
    Receiver target();

    Call target(Receiver receiver);

    String name();

    Call name(String str);

    boolean isTargetImplicit();

    Call targetImplicit(boolean z);

    @Override // polyglot.ast.ProcedureCall
    List arguments();

    @Override // polyglot.ast.ProcedureCall
    ProcedureCall arguments(List list);

    MethodInstance methodInstance();

    Call methodInstance(MethodInstance methodInstance);
}
