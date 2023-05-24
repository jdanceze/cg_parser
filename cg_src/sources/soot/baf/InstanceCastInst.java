package soot.baf;

import soot.Type;
/* loaded from: gencallgraphv3.jar:soot/baf/InstanceCastInst.class */
public interface InstanceCastInst extends Inst {
    Type getCastType();

    void setCastType(Type type);
}
