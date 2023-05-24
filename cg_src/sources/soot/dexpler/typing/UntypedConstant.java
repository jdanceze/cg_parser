package soot.dexpler.typing;

import soot.Type;
import soot.Value;
import soot.jimple.Constant;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/dexpler/typing/UntypedConstant.class */
public abstract class UntypedConstant extends Constant {
    private static final long serialVersionUID = -742448859930407635L;

    public abstract Value defineType(Type type);

    @Override // soot.Value
    public Type getType() {
        throw new RuntimeException("no type yet!");
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
    }
}
