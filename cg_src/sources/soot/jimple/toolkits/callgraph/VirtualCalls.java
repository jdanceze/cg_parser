package soot.jimple.toolkits.callgraph;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.AnySubType;
import soot.ArrayType;
import soot.FastHierarchy;
import soot.G;
import soot.NullType;
import soot.PhaseOptions;
import soot.PrimType;
import soot.RefType;
import soot.Scene;
import soot.Singletons;
import soot.SootClass;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Type;
import soot.options.CGOptions;
import soot.toolkits.scalar.Pair;
import soot.util.HashMultiMap;
import soot.util.MultiMap;
import soot.util.queue.ChunkedQueue;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/callgraph/VirtualCalls.class */
public class VirtualCalls {
    private static final Logger LOGGER;
    private final CGOptions options = new CGOptions(PhaseOptions.v().getPhaseOptions("cg"));
    protected MultiMap<Pair<Type, SootMethodRef>, Pair<Type, SootMethodRef>> baseToPossibleSubTypes = new HashMultiMap();
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !VirtualCalls.class.desiredAssertionStatus();
        LOGGER = LoggerFactory.getLogger(VirtualCalls.class);
    }

    public VirtualCalls(Singletons.Global g) {
    }

    public static VirtualCalls v() {
        return G.v().soot_jimple_toolkits_callgraph_VirtualCalls();
    }

    public SootMethod resolveSpecial(SootMethodRef calleeRef, SootMethod container) {
        return resolveSpecial(calleeRef, container, false);
    }

    public SootMethod resolveSpecial(SootMethodRef calleeRef, SootMethod container, boolean appOnly) {
        SootMethod callee = calleeRef.resolve();
        SootClass containerCls = container.getDeclaringClass();
        SootClass calleeCls = callee.getDeclaringClass();
        if (containerCls.getType() != calleeCls.getType() && Scene.v().getOrMakeFastHierarchy().canStoreType(containerCls.getType(), calleeCls.getType()) && !"<init>".equals(callee.getName()) && !"<clinit>".equals(callee.getName()) && !calleeCls.isInterface()) {
            return resolveNonSpecial(containerCls.getSuperclass().getType(), calleeRef, appOnly);
        }
        return callee;
    }

    public SootMethod resolveNonSpecial(RefType t, SootMethodRef callee) {
        return resolveNonSpecial(t, callee, false);
    }

    public SootMethod resolveNonSpecial(RefType t, SootMethodRef callee, boolean appOnly) {
        SootClass cls = t.getSootClass();
        if ((!appOnly || !cls.isLibraryClass()) && !cls.isInterface()) {
            return Scene.v().getOrMakeFastHierarchy().resolveConcreteDispatch(cls, callee);
        }
        return null;
    }

    public void resolve(Type t, Type declaredType, SootMethodRef callee, SootMethod container, ChunkedQueue<SootMethod> targets) {
        resolve(t, declaredType, (Type) null, callee, container, targets);
    }

    public void resolve(Type t, Type declaredType, SootMethodRef callee, SootMethod container, ChunkedQueue<SootMethod> targets, boolean appOnly) {
        resolve(t, declaredType, null, callee, container, targets, appOnly);
    }

    public void resolve(Type t, Type declaredType, Type sigType, SootMethodRef callee, SootMethod container, ChunkedQueue<SootMethod> targets) {
        resolve(t, declaredType, sigType, callee, container, targets, false);
    }

    public void resolve(Type t, Type declaredType, Type sigType, SootMethodRef callee, SootMethod container, ChunkedQueue<SootMethod> targets, boolean appOnly) {
        if (declaredType instanceof ArrayType) {
            declaredType = Scene.v().getObjectType();
        }
        if (sigType instanceof ArrayType) {
            sigType = Scene.v().getObjectType();
        }
        if (t instanceof ArrayType) {
            t = Scene.v().getObjectType();
        }
        FastHierarchy fastHierachy = Scene.v().getOrMakeFastHierarchy();
        if (declaredType != null && !fastHierachy.canStoreType(t, declaredType)) {
            return;
        }
        if (sigType != null && !fastHierachy.canStoreType(t, sigType)) {
            return;
        }
        if (t instanceof RefType) {
            SootMethod target = resolveNonSpecial((RefType) t, callee, appOnly);
            if (target != null) {
                targets.add(target);
            }
        } else if (t instanceof AnySubType) {
            RefType base = ((AnySubType) t).getBase();
            if (this.options.library() == 3 && base.getSootClass().isInterface()) {
                LOGGER.warn("Deprecated library dispatch is conducted. The results might be unsound...");
                resolveLibrarySignature(declaredType, sigType, callee, container, targets, appOnly, base);
                return;
            }
            for (SootMethod dispatch : Scene.v().getOrMakeFastHierarchy().resolveAbstractDispatch(base.getSootClass(), callee)) {
                targets.add(dispatch);
            }
        } else if (!(t instanceof NullType) && !(t instanceof PrimType)) {
            throw new RuntimeException("oops " + t);
        }
    }

    public void resolveSuperType(Type t, Type declaredType, SootMethodRef callee, ChunkedQueue<SootMethod> targets, boolean appOnly) {
        RefType child;
        SootMethod target;
        if (declaredType == null || t == null) {
            return;
        }
        if (declaredType instanceof ArrayType) {
            declaredType = Scene.v().getObjectType();
        }
        if (t instanceof ArrayType) {
            t = Scene.v().getObjectType();
        }
        if (declaredType instanceof RefType) {
            if (t instanceof AnySubType) {
                child = ((AnySubType) t).getBase();
            } else if (t instanceof RefType) {
                child = (RefType) t;
            } else {
                return;
            }
            FastHierarchy fh = Scene.v().getOrMakeFastHierarchy();
            if (fh.canStoreClass(child.getSootClass(), ((RefType) declaredType).getSootClass()) && (target = resolveNonSpecial(child, callee, appOnly)) != null) {
                targets.add(target);
            }
        }
    }

    @Deprecated
    protected void resolveLibrarySignature(Type declaredType, Type sigType, SootMethodRef callee, SootMethod container, ChunkedQueue<SootMethod> targets, boolean appOnly, RefType base) {
        FastHierarchy fh = Scene.v().getOrMakeFastHierarchy();
        if (!$assertionsDisabled && !(declaredType instanceof RefType)) {
            throw new AssertionError();
        }
        Pair<Type, SootMethodRef> pair = new Pair<>(base, callee);
        Set<Pair<Type, SootMethodRef>> types = this.baseToPossibleSubTypes.get(pair);
        if (types != null) {
            for (Pair<Type, SootMethodRef> tuple : types) {
                Type st = tuple.getO1();
                if (!fh.canStoreType(st, declaredType)) {
                    resolve(st, st, sigType, callee, container, targets, appOnly);
                } else {
                    resolve(st, declaredType, sigType, callee, container, targets, appOnly);
                }
            }
            return;
        }
        Set<Pair<Type, SootMethodRef>> types2 = new HashSet<>();
        Type declaredReturnType = callee.getReturnType();
        List<Type> declaredParamTypes = callee.getParameterTypes();
        String declaredName = callee.getName();
        for (SootClass sc : Scene.v().getClasses()) {
            for (SootMethod sm : sc.getMethods()) {
                if (!sm.isAbstract() && sm.getName().equals(declaredName) && fh.canStoreType(sm.getReturnType(), declaredReturnType)) {
                    List<Type> paramTypes = sm.getParameterTypes();
                    if (declaredParamTypes.size() == paramTypes.size()) {
                        boolean check = true;
                        int i = 0;
                        while (true) {
                            if (i < paramTypes.size()) {
                                if (fh.canStoreType(declaredParamTypes.get(i), paramTypes.get(i))) {
                                    i++;
                                } else {
                                    check = false;
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                        if (check) {
                            Type st2 = sc.getType();
                            if (!fh.canStoreType(st2, declaredType)) {
                                if (!sc.isFinal()) {
                                    resolve(st2, st2, sigType, sm.makeRef(), container, targets, appOnly);
                                    types2.add(new Pair<>(st2, sm.makeRef()));
                                }
                            } else {
                                resolve(st2, declaredType, sigType, callee, container, targets, appOnly);
                                types2.add(new Pair<>(st2, callee));
                            }
                        }
                    }
                }
            }
        }
        this.baseToPossibleSubTypes.putAll(pair, types2);
    }
}
