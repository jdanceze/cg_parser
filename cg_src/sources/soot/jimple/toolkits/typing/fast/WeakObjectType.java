package soot.jimple.toolkits.typing.fast;

import soot.RefType;
import soot.Singletons;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/fast/WeakObjectType.class */
public class WeakObjectType extends RefType {
    public WeakObjectType(Singletons.Global g) {
        super(g);
    }

    public WeakObjectType(String className) {
        super(className);
    }
}
