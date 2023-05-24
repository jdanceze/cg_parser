package soot.jimple.toolkits.typing.fast;

import java.util.Collection;
import soot.Type;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/fast/IHierarchy.class */
public interface IHierarchy {
    Collection<Type> lcas(Type type, Type type2, boolean z);

    boolean ancestor(Type type, Type type2);
}
