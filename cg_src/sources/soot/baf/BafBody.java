package soot.baf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.DoubleType;
import soot.Local;
import soot.LongType;
import soot.PackManager;
import soot.SootMethod;
import soot.Trap;
import soot.Type;
import soot.Unit;
import soot.UnitBox;
import soot.baf.internal.BafLocal;
import soot.jimple.ConvertToBaf;
import soot.jimple.JimpleBody;
import soot.jimple.JimpleToBafContext;
import soot.options.Options;
/* loaded from: gencallgraphv3.jar:soot/baf/BafBody.class */
public class BafBody extends Body {
    private static final Logger logger;
    private final JimpleToBafContext jimpleToBafContext;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !BafBody.class.desiredAssertionStatus();
        logger = LoggerFactory.getLogger(BafBody.class);
    }

    public BafBody(JimpleBody jimpleBody, Map<String, String> options) {
        super(jimpleBody.getMethod());
        if (Options.v().verbose()) {
            logger.debug("[" + getMethod().getName() + "] Constructing BafBody...");
        }
        JimpleToBafContext context = new JimpleToBafContext(jimpleBody.getLocalCount());
        this.jimpleToBafContext = context;
        for (Local l : jimpleBody.getLocals()) {
            Type t = l.getType();
            BafLocal newLocal = (BafLocal) Baf.v().newLocal(l.getName(), (DoubleType.v().equals(t) || LongType.v().equals(t)) ? DoubleWordType.v() : WordType.v());
            context.setBafLocalOfJimpleLocal(l, newLocal);
            newLocal.setOriginalLocal(l);
            getLocals().add(newLocal);
        }
        if (!$assertionsDisabled && getLocals().size() != jimpleBody.getLocalCount()) {
            throw new AssertionError();
        }
        Map<Unit, Unit> origToFirstConverted = new HashMap<>();
        Iterator<Unit> it = jimpleBody.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            List<Unit> conversionList = new ArrayList<>();
            context.setCurrentUnit(u);
            ((ConvertToBaf) u).convertToBaf(context, conversionList);
            origToFirstConverted.put(u, conversionList.get(0));
            getUnits().addAll(conversionList);
        }
        for (UnitBox box : getAllUnitBoxes()) {
            Unit unit = box.getUnit();
            if (unit instanceof PlaceholderInst) {
                Unit source = ((PlaceholderInst) unit).getSource();
                box.setUnit(origToFirstConverted.get(source));
            }
        }
        for (Trap trap : jimpleBody.getTraps()) {
            getTraps().add(Baf.v().newTrap(trap.getException(), origToFirstConverted.get(trap.getBeginUnit()), origToFirstConverted.get(trap.getEndUnit()), origToFirstConverted.get(trap.getHandlerUnit())));
        }
        PackManager.v().getPack("bb").apply(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BafBody(SootMethod m) {
        super(m);
        this.jimpleToBafContext = null;
    }

    public JimpleToBafContext getContext() {
        return this.jimpleToBafContext;
    }

    @Override // soot.Body
    public Object clone() {
        Body b = new BafBody(getMethodUnsafe());
        b.importBodyContentsFrom(this);
        return b;
    }

    @Override // soot.Body
    public Object clone(boolean noLocalsClone) {
        return null;
    }
}
