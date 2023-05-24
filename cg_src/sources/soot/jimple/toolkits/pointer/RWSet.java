package soot.jimple.toolkits.pointer;

import java.util.Set;
import soot.PointsToSet;
import soot.SootField;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/pointer/RWSet.class */
public abstract class RWSet {
    public abstract boolean getCallsNative();

    public abstract boolean setCallsNative();

    public abstract int size();

    public abstract Set<?> getGlobals();

    public abstract Set<?> getFields();

    public abstract PointsToSet getBaseForField(Object obj);

    public abstract boolean hasNonEmptyIntersection(RWSet rWSet);

    public abstract boolean union(RWSet rWSet);

    public abstract boolean addGlobal(SootField sootField);

    public abstract boolean addFieldRef(PointsToSet pointsToSet, Object obj);

    public abstract boolean isEquivTo(RWSet rWSet);
}
