package soot.dexpler.instructions;

import soot.Body;
import soot.Type;
import soot.dexpler.DexBody;
/* loaded from: gencallgraphv3.jar:soot/dexpler/instructions/RetypeableInstruction.class */
public interface RetypeableInstruction {
    void setRealType(DexBody dexBody, Type type);

    void retype(Body body);
}
