package soot.baf.toolkits.base;

import java.util.Iterator;
import soot.Body;
import soot.Unit;
import soot.baf.InstanceCastInst;
/* loaded from: gencallgraphv3.jar:soot/baf/toolkits/base/ExamplePeephole.class */
public class ExamplePeephole implements Peephole {
    @Override // soot.baf.toolkits.base.Peephole
    public boolean apply(Body b) {
        boolean changed = false;
        Iterator<Unit> it = b.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if (u instanceof InstanceCastInst) {
                it.remove();
                changed = true;
            }
        }
        return changed;
    }
}
