package soot.jimple.toolkits.typing.fast;

import soot.Type;
import soot.Value;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/fast/IUseVisitor.class */
public interface IUseVisitor {
    Value visit(Value value, Type type, Stmt stmt, boolean z);

    boolean finish();

    default Value visit(Value op, Type useType, Stmt stmt) {
        return visit(op, useType, stmt, false);
    }
}
