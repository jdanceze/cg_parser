package soot.jimple.infoflow.aliasing;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import heros.solver.IDESolver;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ArrayType;
import soot.Local;
import soot.PrimType;
import soot.RefLikeType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Value;
import soot.jimple.ArrayRef;
import soot.jimple.Constant;
import soot.jimple.DefinitionStmt;
import soot.jimple.FieldRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.data.AccessPath;
import soot.jimple.infoflow.data.AccessPathFactory;
import soot.jimple.infoflow.data.AccessPathFragment;
import soot.jimple.infoflow.typing.TypeUtils;
import soot.jimple.toolkits.pointer.LocalMustAliasAnalysis;
import soot.jimple.toolkits.pointer.StrongLocalMustAliasAnalysis;
import soot.toolkits.graph.UnitGraph;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/aliasing/Aliasing.class */
public class Aliasing {
    private final IAliasingStrategy aliasingStrategy;
    private final IAliasingStrategy implicitFlowAliasingStrategy;
    private final InfoflowManager manager;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Set<SootMethod> excludedFromMustAliasAnalysis = new HashSet();
    protected final LoadingCache<SootMethod, LocalMustAliasAnalysis> strongAliasAnalysis = IDESolver.DEFAULT_CACHE_BUILDER.build(new CacheLoader<SootMethod, LocalMustAliasAnalysis>() { // from class: soot.jimple.infoflow.aliasing.Aliasing.1
        @Override // com.google.common.cache.CacheLoader
        public LocalMustAliasAnalysis load(SootMethod method) throws Exception {
            return new StrongLocalMustAliasAnalysis((UnitGraph) Aliasing.this.manager.getICFG().getOrCreateUnitGraph(method));
        }
    });

    public Aliasing(IAliasingStrategy aliasingStrategy, InfoflowManager manager) {
        this.aliasingStrategy = aliasingStrategy;
        this.implicitFlowAliasingStrategy = new ImplicitFlowAliasStrategy(manager);
        this.manager = manager;
    }

    public void computeAliases(Abstraction d1, Stmt src, Value targetValue, Set<Abstraction> taintSet, SootMethod method, Abstraction newAbs) {
        if (!canHaveAliases(newAbs.getAccessPath()) && !isStringConstructorCall(src)) {
            return;
        }
        if (!d1.getAccessPath().isEmpty()) {
            this.aliasingStrategy.computeAliasTaints(d1, src, targetValue, taintSet, method, newAbs);
        } else if (targetValue instanceof InstanceFieldRef) {
            this.implicitFlowAliasingStrategy.computeAliasTaints(d1, src, targetValue, taintSet, method, newAbs);
        }
    }

    public AccessPath getReferencedAPBase(AccessPath taintedAP, SootField[] referencedFields) {
        return getReferencedAPBase(taintedAP, referencedFields, this.manager);
    }

    public static AccessPath getReferencedAPBase(AccessPath taintedAP, SootField[] referencedFields, InfoflowManager manager) {
        Collection<AccessPathFragment[]> baseForType;
        AccessPathFactory af = manager.getAccessPathFactory();
        if (taintedAP.isStaticFieldRef()) {
            baseForType = af.getBaseForType(taintedAP.getFirstFieldType());
        } else {
            baseForType = af.getBaseForType(taintedAP.getBaseType());
        }
        Collection<AccessPathFragment[]> bases = baseForType;
        if (bases != null && bases.size() > manager.getConfig().getMaxAliasingBases()) {
            return null;
        }
        for (int fieldIdx = 0; fieldIdx < referencedFields.length; fieldIdx++) {
            if (fieldIdx >= taintedAP.getFragmentCount()) {
                if (taintedAP.getTaintSubFields()) {
                    return taintedAP;
                }
                return null;
            } else if (taintedAP.getFragments()[fieldIdx].getField() != referencedFields[fieldIdx]) {
                if (bases != null) {
                    if (!taintedAP.isStaticFieldRef() || fieldIdx != 0) {
                        for (AccessPathFragment[] base : bases) {
                            if (base[0].getField() == referencedFields[fieldIdx]) {
                                AccessPathFragment[] cutFragments = new AccessPathFragment[taintedAP.getFragmentCount() + base.length];
                                System.arraycopy(taintedAP.getFragments(), 0, cutFragments, 0, fieldIdx);
                                System.arraycopy(base, 0, cutFragments, fieldIdx, base.length);
                                System.arraycopy(taintedAP.getFragments(), fieldIdx, cutFragments, fieldIdx + base.length, taintedAP.getFragmentCount() - fieldIdx);
                                return manager.getAccessPathFactory().createAccessPath(taintedAP.getPlainValue(), taintedAP.getBaseType(), cutFragments, taintedAP.getTaintSubFields(), false, false, taintedAP.getArrayTaintType());
                            }
                        }
                        return null;
                    }
                    return null;
                }
                return null;
            }
        }
        return taintedAP;
    }

    public boolean mayAlias(Value val1, Value val2) {
        if (!AccessPath.canContainValue(val1) || !AccessPath.canContainValue(val2) || (val1 instanceof Constant) || (val2 instanceof Constant)) {
            return false;
        }
        if (val1 == val2) {
            return true;
        }
        if (this.aliasingStrategy.isInteractive()) {
            return this.aliasingStrategy.mayAlias(this.manager.getAccessPathFactory().createAccessPath(val1, false), this.manager.getAccessPathFactory().createAccessPath(val2, false));
        }
        return false;
    }

    public AccessPath mayAlias(AccessPath ap, Value val) {
        if (!AccessPath.canContainValue(val) || (val instanceof Constant)) {
            return null;
        }
        if (this.aliasingStrategy.isInteractive()) {
            if (!this.aliasingStrategy.mayAlias(ap, this.manager.getAccessPathFactory().createAccessPath(val, true))) {
                return null;
            }
        } else if ((val instanceof Local) && ap.getPlainValue() != val) {
            return null;
        } else {
            if ((val instanceof ArrayRef) && ap.getPlainValue() != ((ArrayRef) val).getBase()) {
                return null;
            }
            if ((val instanceof InstanceFieldRef) && ((!ap.isLocal() && !ap.isInstanceFieldRef()) || ((InstanceFieldRef) val).getBase() != ap.getPlainValue())) {
                return null;
            }
        }
        if ((val instanceof StaticFieldRef) && !ap.isStaticFieldRef()) {
            return null;
        }
        SootField[] fields = val instanceof FieldRef ? new SootField[]{((FieldRef) val).getField()} : new SootField[0];
        return getReferencedAPBase(ap, fields, this.manager);
    }

    public boolean mustAlias(SootField field1, SootField field2) {
        return field1 == field2;
    }

    public boolean mustAlias(Local val1, Local val2, Stmt position) {
        if (val1 == val2) {
            return true;
        }
        if (!(val1.getType() instanceof RefLikeType) || !(val2.getType() instanceof RefLikeType)) {
            return false;
        }
        SootMethod method = this.manager.getICFG().getMethodOf(position);
        if (this.excludedFromMustAliasAnalysis.contains(method) || this.manager.isAnalysisAborted()) {
            return false;
        }
        try {
            LocalMustAliasAnalysis lmaa = this.strongAliasAnalysis.getUnchecked(method);
            return lmaa.mustAlias(val1, position, val2, position);
        } catch (Exception ex) {
            this.logger.error("Error in local must alias analysis", (Throwable) ex);
            return false;
        }
    }

    public boolean canHaveAliases(Stmt stmt, Value val, Abstraction source) {
        if (stmt instanceof DefinitionStmt) {
            DefinitionStmt defStmt = (DefinitionStmt) stmt;
            if ((defStmt.getLeftOp() instanceof Local) && defStmt.getLeftOp() == source.getAccessPath().getPlainValue()) {
                return false;
            }
            if ((val instanceof ArrayRef) || (val instanceof FieldRef)) {
                return true;
            }
        }
        if (val instanceof InstanceFieldRef) {
            InstanceFieldRef instanceFieldRef = (InstanceFieldRef) val;
            Value base = instanceFieldRef.getBase();
            if (base.getType() instanceof PrimType) {
                return false;
            }
        } else if ((val instanceof Local) && (val.getType() instanceof PrimType)) {
            return false;
        }
        if (val instanceof Constant) {
            return false;
        }
        if (TypeUtils.isStringType(val.getType()) && !isStringConstructorCall(stmt) && !source.getAccessPath().getCanHaveImmutableAliases()) {
            return false;
        }
        AccessPath ap = source.getAccessPath();
        if (val instanceof FieldRef) {
            return true;
        }
        if ((val instanceof Local) && (((Local) val).getType() instanceof ArrayType)) {
            return true;
        }
        return ap != null && ap.getTaintSubFields();
    }

    public boolean canHaveAliasesRightSide(Stmt stmt, Value val, Abstraction source) {
        if ((stmt instanceof DefinitionStmt) && ((val instanceof ArrayRef) || (val instanceof FieldRef))) {
            return true;
        }
        if (val instanceof InstanceFieldRef) {
            InstanceFieldRef instanceFieldRef = (InstanceFieldRef) val;
            Value base = instanceFieldRef.getBase();
            if (base.getType() instanceof PrimType) {
                return false;
            }
        } else if ((val instanceof Local) && (val.getType() instanceof PrimType)) {
            return false;
        }
        if (val instanceof Constant) {
            return false;
        }
        if (!TypeUtils.isStringType(val.getType()) || isStringConstructorCall(stmt) || source.getAccessPath().getCanHaveImmutableAliases()) {
            return (val instanceof FieldRef) || (val instanceof Local);
        }
        return false;
    }

    public boolean isStringConstructorCall(Stmt iStmt) {
        SootClass scString = Scene.v().getSootClassUnsafe("java.lang.String");
        Collection<SootMethod> callees = this.manager.getICFG().getCalleesOfCallAt(iStmt);
        if (callees != null && !callees.isEmpty()) {
            for (SootMethod callee : callees) {
                if (callee.getDeclaringClass() == scString && callee.isConstructor()) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public static boolean canHaveAliases(AccessPath ap) {
        if (TypeUtils.isStringType(ap.getBaseType()) && !ap.getCanHaveImmutableAliases()) {
            return false;
        }
        if (ap.isStaticFieldRef()) {
            if (ap.getFirstFieldType() instanceof PrimType) {
                return false;
            }
            return true;
        } else if (ap.getBaseType() instanceof PrimType) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean baseMatches(Value baseValue, Abstraction source) {
        if (baseValue instanceof Local) {
            if (baseValue.equals(source.getAccessPath().getPlainValue())) {
                return true;
            }
            return false;
        } else if (baseValue instanceof InstanceFieldRef) {
            InstanceFieldRef ifr = (InstanceFieldRef) baseValue;
            if (ifr.getBase().equals(source.getAccessPath().getPlainValue()) && source.getAccessPath().firstFieldMatches(ifr.getField())) {
                return true;
            }
            return false;
        } else if (baseValue instanceof StaticFieldRef) {
            StaticFieldRef sfr = (StaticFieldRef) baseValue;
            if (source.getAccessPath().firstFieldMatches(sfr.getField())) {
                return true;
            }
            return false;
        } else {
            return false;
        }
    }

    public static boolean baseMatchesStrict(Value baseValue, Abstraction source) {
        if (!baseMatches(baseValue, source)) {
            return false;
        }
        if (baseValue instanceof Local) {
            return source.getAccessPath().isLocal();
        }
        if ((baseValue instanceof InstanceFieldRef) || (baseValue instanceof StaticFieldRef)) {
            return source.getAccessPath().getFragmentCount() == 1;
        }
        throw new RuntimeException("Unexpected left side");
    }

    public void excludeMethodFromMustAlias(SootMethod method) {
        this.excludedFromMustAliasAnalysis.add(method);
    }

    public IAliasingStrategy getAliasingStrategy() {
        return this.aliasingStrategy;
    }
}
