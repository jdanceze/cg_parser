package soot.toolkits.scalar;

import java.util.List;
import soot.Local;
import soot.Unit;
/* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/LocalDefs.class */
public interface LocalDefs {
    List<Unit> getDefsOfAt(Local local, Unit unit);

    List<Unit> getDefsOf(Local local);
}
