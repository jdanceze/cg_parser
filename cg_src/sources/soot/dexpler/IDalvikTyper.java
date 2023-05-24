package soot.dexpler;

import soot.Body;
import soot.Type;
import soot.ValueBox;
/* loaded from: gencallgraphv3.jar:soot/dexpler/IDalvikTyper.class */
public interface IDalvikTyper {
    public static final boolean ENABLE_DVKTYPER = false;
    public static final boolean DEBUG = false;

    void setType(ValueBox valueBox, Type type, boolean z);

    void addConstraint(ValueBox valueBox, ValueBox valueBox2);

    void assignType(Body body);
}
