package soot.jimple.toolkits.pointer;

import java.util.HashMap;
import java.util.List;
import soot.G;
import soot.Local;
import soot.PointsToAnalysis;
import soot.PointsToSet;
import soot.Scene;
import soot.SideEffectTester;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.ArrayRef;
import soot.jimple.Constant;
import soot.jimple.Expr;
import soot.jimple.InstanceFieldRef;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/pointer/PASideEffectTester.class */
public class PASideEffectTester implements SideEffectTester {
    private final PointsToAnalysis pa = Scene.v().getPointsToAnalysis();
    private final SideEffectAnalysis sea = Scene.v().getSideEffectAnalysis();
    private HashMap<Unit, RWSet> unitToRead;
    private HashMap<Unit, RWSet> unitToWrite;
    private HashMap<Local, PointsToSet> localToReachingObjects;
    private SootMethod currentMethod;

    public PASideEffectTester() {
        if (G.v().Union_factory == null) {
            G.v().Union_factory = new UnionFactory() { // from class: soot.jimple.toolkits.pointer.PASideEffectTester.1
                @Override // soot.jimple.toolkits.pointer.UnionFactory
                public Union newUnion() {
                    return FullObjectSet.v();
                }
            };
        }
    }

    @Override // soot.SideEffectTester
    public void newMethod(SootMethod m) {
        this.unitToRead = new HashMap<>();
        this.unitToWrite = new HashMap<>();
        this.localToReachingObjects = new HashMap<>();
        this.currentMethod = m;
        this.sea.findNTRWSets(m);
    }

    protected RWSet readSet(Unit u) {
        RWSet ret = this.unitToRead.get(u);
        if (ret == null) {
            HashMap<Unit, RWSet> hashMap = this.unitToRead;
            RWSet readSet = this.sea.readSet(this.currentMethod, (Stmt) u);
            ret = readSet;
            hashMap.put(u, readSet);
        }
        return ret;
    }

    protected RWSet writeSet(Unit u) {
        RWSet ret = this.unitToWrite.get(u);
        if (ret == null) {
            HashMap<Unit, RWSet> hashMap = this.unitToWrite;
            RWSet writeSet = this.sea.writeSet(this.currentMethod, (Stmt) u);
            ret = writeSet;
            hashMap.put(u, writeSet);
        }
        return ret;
    }

    protected PointsToSet reachingObjects(Local l) {
        PointsToSet ret = this.localToReachingObjects.get(l);
        if (ret == null) {
            HashMap<Local, PointsToSet> hashMap = this.localToReachingObjects;
            PointsToSet reachingObjects = this.pa.reachingObjects(l);
            ret = reachingObjects;
            hashMap.put(l, reachingObjects);
        }
        return ret;
    }

    @Override // soot.SideEffectTester
    public boolean unitCanReadFrom(Unit u, Value v) {
        return valueTouchesRWSet(readSet(u), v, u.getUseBoxes());
    }

    @Override // soot.SideEffectTester
    public boolean unitCanWriteTo(Unit u, Value v) {
        return valueTouchesRWSet(writeSet(u), v, u.getDefBoxes());
    }

    protected boolean valueTouchesRWSet(RWSet s, Value v, List<ValueBox> boxes) {
        PointsToSet o1;
        PointsToSet o2;
        for (ValueBox use : v.getUseBoxes()) {
            if (valueTouchesRWSet(s, use.getValue(), boxes)) {
                return true;
            }
        }
        if (v instanceof Constant) {
            return false;
        }
        if (v instanceof Expr) {
            throw new RuntimeException("can't deal with expr");
        }
        for (ValueBox box : boxes) {
            if (box.getValue().equivTo(v)) {
                return true;
            }
        }
        if (v instanceof Local) {
            return false;
        }
        if (v instanceof InstanceFieldRef) {
            if (s == null) {
                return false;
            }
            InstanceFieldRef ifr = (InstanceFieldRef) v;
            PointsToSet o12 = s.getBaseForField(ifr.getField());
            if (o12 == null || (o2 = reachingObjects((Local) ifr.getBase())) == null) {
                return false;
            }
            return o12.hasNonEmptyIntersection(o2);
        } else if (v instanceof ArrayRef) {
            if (s == null || (o1 = s.getBaseForField(PointsToAnalysis.ARRAY_ELEMENTS_NODE)) == null) {
                return false;
            }
            ArrayRef ar = (ArrayRef) v;
            PointsToSet o22 = reachingObjects((Local) ar.getBase());
            if (o22 == null) {
                return false;
            }
            return o1.hasNonEmptyIntersection(o22);
        } else if (v instanceof StaticFieldRef) {
            if (s == null) {
                return false;
            }
            StaticFieldRef sfr = (StaticFieldRef) v;
            return s.getGlobals().contains(sfr.getField());
        } else {
            throw new RuntimeException("Forgot to handle value " + v);
        }
    }
}
