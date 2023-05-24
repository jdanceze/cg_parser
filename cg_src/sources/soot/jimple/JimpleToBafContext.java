package soot.jimple;

import java.util.HashMap;
import java.util.Map;
import soot.Local;
import soot.Unit;
import soot.baf.BafBody;
/* loaded from: gencallgraphv3.jar:soot/jimple/JimpleToBafContext.class */
public class JimpleToBafContext {
    private final Map<Local, Local> jimpleLocalToBafLocal;
    private BafBody bafBody;
    private Unit mCurrentUnit;

    public JimpleToBafContext(int localCount) {
        this.jimpleLocalToBafLocal = new HashMap((localCount * 2) + 1, 0.7f);
    }

    public void setCurrentUnit(Unit u) {
        this.mCurrentUnit = u;
    }

    public Unit getCurrentUnit() {
        return this.mCurrentUnit;
    }

    public Local getBafLocalOfJimpleLocal(Local jimpleLocal) {
        return this.jimpleLocalToBafLocal.get(jimpleLocal);
    }

    public void setBafLocalOfJimpleLocal(Local jimpleLocal, Local bafLocal) {
        this.jimpleLocalToBafLocal.put(jimpleLocal, bafLocal);
    }

    public BafBody getBafBody() {
        return this.bafBody;
    }

    public void setBafBody(BafBody bafBody) {
        this.bafBody = bafBody;
    }
}
