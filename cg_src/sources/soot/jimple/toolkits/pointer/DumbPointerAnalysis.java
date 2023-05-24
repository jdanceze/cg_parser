package soot.jimple.toolkits.pointer;

import soot.Context;
import soot.G;
import soot.Local;
import soot.PointsToAnalysis;
import soot.PointsToSet;
import soot.PrimType;
import soot.RefType;
import soot.Singletons;
import soot.SootField;
import soot.Type;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/pointer/DumbPointerAnalysis.class */
public class DumbPointerAnalysis implements PointsToAnalysis {
    public DumbPointerAnalysis(Singletons.Global g) {
    }

    public static DumbPointerAnalysis v() {
        return G.v().soot_jimple_toolkits_pointer_DumbPointerAnalysis();
    }

    @Override // soot.PointsToAnalysis
    public PointsToSet reachingObjects(Local l) {
        Type t = l.getType();
        if (t instanceof RefType) {
            return FullObjectSet.v((RefType) t);
        }
        if (t instanceof PrimType) {
            return FullObjectSet.v((PrimType) t);
        }
        return FullObjectSet.v();
    }

    @Override // soot.PointsToAnalysis
    public PointsToSet reachingObjects(Context c, Local l) {
        return reachingObjects(l);
    }

    @Override // soot.PointsToAnalysis
    public PointsToSet reachingObjects(SootField f) {
        Type t = f.getType();
        return t instanceof RefType ? FullObjectSet.v((RefType) t) : FullObjectSet.v();
    }

    @Override // soot.PointsToAnalysis
    public PointsToSet reachingObjects(PointsToSet s, SootField f) {
        return reachingObjects(f);
    }

    @Override // soot.PointsToAnalysis
    public PointsToSet reachingObjects(Local l, SootField f) {
        return reachingObjects(f);
    }

    @Override // soot.PointsToAnalysis
    public PointsToSet reachingObjects(Context c, Local l, SootField f) {
        return reachingObjects(f);
    }

    @Override // soot.PointsToAnalysis
    public PointsToSet reachingObjectsOfArrayElement(PointsToSet s) {
        return FullObjectSet.v();
    }
}
