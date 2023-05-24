package soot.jimple.toolkits.typing.fast;

import java.util.Collection;
import soot.Type;
import soot.Value;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/fast/IEvalFunction.class */
public interface IEvalFunction {
    Collection<Type> eval(Typing typing, Value value, Stmt stmt);
}
