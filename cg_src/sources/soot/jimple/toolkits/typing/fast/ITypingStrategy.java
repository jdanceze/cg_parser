package soot.jimple.toolkits.typing.fast;

import java.util.List;
import soot.Local;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/fast/ITypingStrategy.class */
public interface ITypingStrategy {
    Typing createTyping(Chain<Local> chain);

    Typing createTyping(Typing typing);

    void minimize(List<Typing> list, IHierarchy iHierarchy);

    void finalizeTypes(Typing typing);
}
