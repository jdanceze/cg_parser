package soot.jimple;

import java.util.List;
import soot.Local;
/* loaded from: gencallgraphv3.jar:soot/jimple/EqualLocals.class */
public interface EqualLocals {
    boolean isLocalEqualToAt(Local local, Local local2, Stmt stmt);

    List getCopiesAt(Stmt stmt);
}
