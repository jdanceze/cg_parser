package soot.jimple.toolkits.scalar;

import java.util.Collection;
import soot.Local;
import soot.Type;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/scalar/LocalCreation.class */
public abstract class LocalCreation {
    protected final Collection<Local> localChain;
    protected final String prefix;

    public abstract Local newLocal(Type type);

    public abstract Local newLocal(String str, Type type);

    public LocalCreation(Collection<Local> locals, String prefix) {
        this.localChain = locals;
        this.prefix = prefix;
    }
}
