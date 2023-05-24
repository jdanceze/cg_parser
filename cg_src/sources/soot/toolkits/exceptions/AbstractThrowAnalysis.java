package soot.toolkits.exceptions;

import java.util.Set;
import java.util.stream.Collectors;
import soot.AnySubType;
import soot.Local;
import soot.NullType;
import soot.RefType;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.UnknownType;
import soot.Value;
import soot.baf.ThrowInst;
import soot.grimp.NewInvokeExpr;
import soot.jimple.DefinitionStmt;
import soot.jimple.NewExpr;
import soot.jimple.ThrowStmt;
import soot.toolkits.exceptions.ThrowableSet;
/* loaded from: gencallgraphv3.jar:soot/toolkits/exceptions/AbstractThrowAnalysis.class */
public abstract class AbstractThrowAnalysis implements ThrowAnalysis {
    @Override // soot.toolkits.exceptions.ThrowAnalysis
    public abstract ThrowableSet mightThrow(Unit unit);

    @Override // soot.toolkits.exceptions.ThrowAnalysis
    public abstract ThrowableSet mightThrowImplicitly(ThrowInst throwInst);

    @Override // soot.toolkits.exceptions.ThrowAnalysis
    public abstract ThrowableSet mightThrowImplicitly(ThrowStmt throwStmt);

    @Override // soot.toolkits.exceptions.ThrowAnalysis
    public ThrowableSet mightThrowExplicitly(ThrowInst t) {
        return ThrowableSet.Manager.v().ALL_THROWABLES;
    }

    @Override // soot.toolkits.exceptions.ThrowAnalysis
    public ThrowableSet mightThrowExplicitly(ThrowStmt t) {
        return mightThrowExplicitly(t, null);
    }

    public ThrowableSet mightThrowExplicitly(ThrowStmt t, SootMethod sm) {
        ThrowableSet result;
        Value thrownExpression = t.getOp();
        Type thrownType = thrownExpression.getType();
        if (thrownType == null || (thrownType instanceof UnknownType)) {
            return ThrowableSet.Manager.v().ALL_THROWABLES;
        }
        if (thrownType instanceof NullType) {
            return ThrowableSet.Manager.v().EMPTY.add(ThrowableSet.Manager.v().NULL_POINTER_EXCEPTION);
        }
        if (!(thrownType instanceof RefType)) {
            throw new IllegalStateException("UnitThrowAnalysis StmtSwitch: type of throw argument is not a RefType!");
        }
        ThrowableSet result2 = ThrowableSet.Manager.v().EMPTY;
        if (thrownExpression instanceof NewInvokeExpr) {
            result = result2.add((RefType) thrownType);
        } else {
            RefType preciseType = null;
            if ((thrownExpression instanceof Local) && sm != null) {
                Set<RefType> types = (Set) sm.getActiveBody().getUnits().stream().filter(u -> {
                    return u instanceof DefinitionStmt;
                }).map(u2 -> {
                    return (DefinitionStmt) u2;
                }).filter(d -> {
                    return d.getLeftOp() == thrownExpression;
                }).map(d2 -> {
                    return d2.getRightOp();
                }).filter(o -> {
                    return o instanceof NewExpr;
                }).map(o2 -> {
                    return (NewExpr) o2;
                }).map(n -> {
                    return n.getType();
                }).filter(r -> {
                    return r instanceof RefType;
                }).map(r2 -> {
                    return (RefType) r2;
                }).collect(Collectors.toSet());
                if (types.size() == 1) {
                    preciseType = types.iterator().next();
                }
            }
            if (preciseType == null) {
                result = result2.add(AnySubType.v((RefType) thrownType));
            } else {
                result = result2.add(preciseType);
            }
        }
        return result;
    }
}
