package soot.baf;

import soot.SootField;
import soot.SootFieldRef;
/* loaded from: gencallgraphv3.jar:soot/baf/FieldArgInst.class */
public interface FieldArgInst extends Inst {
    SootFieldRef getFieldRef();

    SootField getField();
}
