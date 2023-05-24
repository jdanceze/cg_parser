package soot.dexpler;

import soot.Type;
import soot.ValueBox;
/* loaded from: gencallgraphv3.jar:soot/dexpler/DvkTyperBase.class */
public abstract class DvkTyperBase {
    public static boolean ENABLE_DVKTYPER = false;

    public abstract void setType(ValueBox valueBox, Type type);

    public abstract void setObjectType(ValueBox valueBox);

    public abstract void setConstraint(ValueBox valueBox, ValueBox valueBox2);

    abstract void assignType();

    public static DvkTyperBase getDvkTyper() {
        return null;
    }
}
