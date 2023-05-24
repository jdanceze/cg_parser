package soot.jimple.toolkits.pointer;

import java.util.Collections;
import java.util.Set;
import soot.AnySubType;
import soot.G;
import soot.PointsToSet;
import soot.PrimType;
import soot.RefType;
import soot.Scene;
import soot.Singletons;
import soot.Type;
import soot.jimple.ClassConstant;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/pointer/FullObjectSet.class */
public class FullObjectSet extends Union {
    private final Set<Type> types;

    public static FullObjectSet v() {
        return G.v().soot_jimple_toolkits_pointer_FullObjectSet();
    }

    public static FullObjectSet v(RefType t) {
        return Scene.v().getObjectType().toString().equals(t.getClassName()) ? v() : new FullObjectSet(t);
    }

    public static FullObjectSet v(PrimType t) {
        return new FullObjectSet(t);
    }

    public FullObjectSet(Singletons.Global g) {
        this(Scene.v().getObjectType());
    }

    private FullObjectSet(PrimType declaredType) {
        this.types = Collections.singleton(declaredType);
    }

    private FullObjectSet(RefType declaredType) {
        this.types = Collections.singleton(AnySubType.v(declaredType));
    }

    public Type type() {
        return this.types.iterator().next();
    }

    @Override // soot.PointsToSet
    public boolean isEmpty() {
        return false;
    }

    @Override // soot.PointsToSet
    public boolean hasNonEmptyIntersection(PointsToSet other) {
        return other != null;
    }

    @Override // soot.PointsToSet
    public Set<Type> possibleTypes() {
        return this.types;
    }

    @Override // soot.jimple.toolkits.pointer.Union
    public boolean addAll(PointsToSet s) {
        return false;
    }

    @Override // soot.jimple.toolkits.pointer.Union, soot.PointsToSet
    public Set<String> possibleStringConstants() {
        return null;
    }

    @Override // soot.jimple.toolkits.pointer.Union, soot.PointsToSet
    public Set<ClassConstant> possibleClassConstants() {
        return null;
    }

    public int depth() {
        return 0;
    }
}
