package soot.jimple.toolkits.callgraph;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.tools.ant.taskdefs.Definer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.AnySubType;
import soot.ArrayType;
import soot.Body;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.Context;
import soot.DoubleType;
import soot.EntryPoints;
import soot.FastHierarchy;
import soot.FloatType;
import soot.IntType;
import soot.JavaMethods;
import soot.Kind;
import soot.Local;
import soot.LocalGenerator;
import soot.LongType;
import soot.MethodContext;
import soot.MethodOrMethodContext;
import soot.MethodSubSignature;
import soot.NullType;
import soot.PackManager;
import soot.PhaseOptions;
import soot.PrimType;
import soot.RefLikeType;
import soot.RefType;
import soot.Scene;
import soot.SceneTransformer;
import soot.ShortType;
import soot.SootClass;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Transform;
import soot.Type;
import soot.Unit;
import soot.UnitPatchingChain;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.DynamicInvokeExpr;
import soot.jimple.FieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.NullConstant;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.ThrowStmt;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.spark.pag.AllocDotField;
import soot.jimple.toolkits.annotation.nullcheck.NullnessAnalysis;
import soot.jimple.toolkits.callgraph.ConstantArrayAnalysis;
import soot.jimple.toolkits.callgraph.VirtualEdgesSummaries;
import soot.jimple.toolkits.reflection.ReflectionTraceInfo;
import soot.options.CGOptions;
import soot.options.Options;
import soot.options.SparkOptions;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.ExceptionalUnitGraphFactory;
import soot.util.HashMultiMap;
import soot.util.IterableNumberer;
import soot.util.LargeNumberedMap;
import soot.util.MultiMap;
import soot.util.NumberedString;
import soot.util.SmallNumberedMap;
import soot.util.StringNumberer;
import soot.util.queue.ChunkedQueue;
import soot.util.queue.QueueReader;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/callgraph/OnFlyCallGraphBuilder.class */
public class OnFlyCallGraphBuilder {
    private static final Logger logger;
    static boolean registeredGuardsTransformation;
    private static final PrimType[] CHAR_NARROWINGS;
    private static final PrimType[] INT_NARROWINGS;
    private static final PrimType[] SHORT_NARROWINGS;
    private static final PrimType[] LONG_NARROWINGS;
    private static final ByteType[] BYTE_NARROWINGS;
    private static final PrimType[] FLOAT_NARROWINGS;
    private static final PrimType[] BOOLEAN_NARROWINGS;
    private static final PrimType[] DOUBLE_NARROWINGS;
    protected final NumberedString sigFinalize;
    protected final NumberedString sigInit;
    protected final NumberedString sigForName;
    protected final RefType clRunnable;
    protected final RefType clAsyncTask;
    protected final RefType clHandler;
    private final CallGraph cicg;
    protected final LargeNumberedMap<Local, List<VirtualCallSite>> receiverToSites;
    protected final LargeNumberedMap<SootMethod, List<Local>> methodToReceivers;
    protected final LargeNumberedMap<SootMethod, List<Local>> methodToInvokeBases;
    protected final LargeNumberedMap<SootMethod, List<Local>> methodToInvokeArgs;
    protected final LargeNumberedMap<SootMethod, List<Local>> methodToStringConstants;
    protected final SmallNumberedMap<Local, List<VirtualCallSite>> stringConstToSites;
    protected final HashSet<SootMethod> analyzedMethods;
    protected final MultiMap<Local, InvokeCallSite> baseToInvokeSite;
    protected final MultiMap<Local, InvokeCallSite> invokeArgsToInvokeSite;
    protected final Map<Local, BitSet> invokeArgsToSize;
    protected final MultiMap<AllocDotField, Local> allocDotFieldToLocal;
    protected final MultiMap<Local, Type> reachingArgTypes;
    protected final MultiMap<Local, Type> reachingBaseTypes;
    protected final ChunkedQueue<SootMethod> targetsQueue;
    protected final QueueReader<SootMethod> targets;
    protected final ReflectionModel reflectionModel;
    protected final CGOptions options;
    protected boolean appOnly;
    protected final ReachableMethods rm;
    protected final QueueReader<MethodOrMethodContext> worklist;
    protected final ContextManager cm;
    protected final VirtualEdgesSummaries virtualEdgeSummaries;
    protected NullnessAnalysis nullnessCache;
    protected ConstantArrayAnalysis arrayCache;
    protected SootMethod analysisKey;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !OnFlyCallGraphBuilder.class.desiredAssertionStatus();
        logger = LoggerFactory.getLogger(OnFlyCallGraphBuilder.class);
        registeredGuardsTransformation = false;
        CharType cT = CharType.v();
        IntType iT = IntType.v();
        ShortType sT = ShortType.v();
        ByteType bT = ByteType.v();
        LongType lT = LongType.v();
        FloatType fT = FloatType.v();
        CHAR_NARROWINGS = new PrimType[]{cT};
        INT_NARROWINGS = new PrimType[]{iT, cT, sT, bT, sT};
        SHORT_NARROWINGS = new PrimType[]{sT, bT};
        LONG_NARROWINGS = new PrimType[]{lT, iT, cT, sT, bT, sT};
        BYTE_NARROWINGS = new ByteType[]{bT};
        FLOAT_NARROWINGS = new PrimType[]{fT, lT, iT, cT, sT, bT, sT};
        BOOLEAN_NARROWINGS = new PrimType[]{BooleanType.v()};
        DOUBLE_NARROWINGS = new PrimType[]{DoubleType.v(), fT, lT, iT, cT, sT, bT, sT};
    }

    public OnFlyCallGraphBuilder(ContextManager cm, ReachableMethods rm, boolean appOnly) {
        this.clRunnable = RefType.v("java.lang.Runnable");
        this.clAsyncTask = RefType.v("android.os.AsyncTask");
        this.clHandler = RefType.v("android.os.Handler");
        this.cicg = Scene.v().internalMakeCallGraph();
        this.analyzedMethods = new HashSet<>();
        this.baseToInvokeSite = new HashMultiMap();
        this.invokeArgsToInvokeSite = new HashMultiMap();
        this.invokeArgsToSize = new IdentityHashMap();
        this.allocDotFieldToLocal = new HashMultiMap();
        this.reachingArgTypes = new HashMultiMap();
        this.reachingBaseTypes = new HashMultiMap();
        this.targetsQueue = new ChunkedQueue<>();
        this.targets = this.targetsQueue.reader();
        this.virtualEdgeSummaries = initializeEdgeSummaries();
        this.nullnessCache = null;
        this.arrayCache = null;
        this.analysisKey = null;
        Scene sc = Scene.v();
        StringNumberer nmbr = sc.getSubSigNumberer();
        if (Options.v().src_prec() == 7) {
            this.sigFinalize = nmbr.findOrAdd("void Finalize()");
        } else {
            this.sigFinalize = nmbr.findOrAdd(JavaMethods.SIG_FINALIZE);
        }
        this.sigInit = nmbr.findOrAdd(JavaMethods.SIG_INIT);
        this.sigForName = nmbr.findOrAdd(JavaMethods.SIG_INIT);
        this.receiverToSites = new LargeNumberedMap<>(sc.getLocalNumberer());
        IterableNumberer<SootMethod> methodNumberer = sc.getMethodNumberer();
        this.methodToReceivers = new LargeNumberedMap<>(methodNumberer);
        this.methodToInvokeBases = new LargeNumberedMap<>(methodNumberer);
        this.methodToInvokeArgs = new LargeNumberedMap<>(methodNumberer);
        this.methodToStringConstants = new LargeNumberedMap<>(methodNumberer);
        this.stringConstToSites = new SmallNumberedMap<>();
        this.cm = cm;
        this.rm = rm;
        this.worklist = rm.listener();
        this.options = new CGOptions(PhaseOptions.v().getPhaseOptions("cg"));
        if (!this.options.verbose()) {
            logger.debug("[Call Graph] For information on where the call graph may be incomplete, use the verbose option to the cg phase.");
        }
        if (this.options.reflection_log() == null || this.options.reflection_log().length() == 0) {
            if (this.options.types_for_invoke() && new SparkOptions(PhaseOptions.v().getPhaseOptions("cg.spark")).enabled()) {
                this.reflectionModel = new TypeBasedReflectionModel();
            } else {
                this.reflectionModel = new DefaultReflectionModel();
            }
        } else {
            this.reflectionModel = new TraceBasedReflectionModel(this, null);
        }
        this.appOnly = appOnly;
    }

    public OnFlyCallGraphBuilder(ContextManager cm, ReachableMethods rm) {
        this(cm, rm, false);
    }

    protected VirtualEdgesSummaries initializeEdgeSummaries() {
        return new VirtualEdgesSummaries();
    }

    public ContextManager getContextManager() {
        return this.cm;
    }

    public LargeNumberedMap<SootMethod, List<Local>> methodToReceivers() {
        return this.methodToReceivers;
    }

    public LargeNumberedMap<SootMethod, List<Local>> methodToInvokeArgs() {
        return this.methodToInvokeArgs;
    }

    public LargeNumberedMap<SootMethod, List<Local>> methodToInvokeBases() {
        return this.methodToInvokeBases;
    }

    public LargeNumberedMap<SootMethod, List<Local>> methodToStringConstants() {
        return this.methodToStringConstants;
    }

    public void processReachables() {
        while (true) {
            if (!this.worklist.hasNext()) {
                this.rm.update();
                if (!this.worklist.hasNext()) {
                    return;
                }
            }
            MethodOrMethodContext momc = this.worklist.next();
            SootMethod m = momc.method();
            if (!this.appOnly || m.getDeclaringClass().isApplicationClass()) {
                if (this.analyzedMethods.add(m)) {
                    processNewMethod(m);
                }
                processNewMethodContext(momc);
            }
        }
    }

    public boolean wantTypes(Local receiver) {
        return (this.receiverToSites.get(receiver) == null && this.baseToInvokeSite.get(receiver) == null) ? false : true;
    }

    public void addBaseType(Local base, Context context, Type ty) {
        if (!$assertionsDisabled && context != null) {
            throw new AssertionError();
        }
        Set<InvokeCallSite> invokeSites = this.baseToInvokeSite.get(base);
        if (invokeSites != null && this.reachingBaseTypes.put(base, ty) && !invokeSites.isEmpty()) {
            resolveInvoke(invokeSites);
        }
    }

    public void addInvokeArgType(Local argArray, Context context, Type t) {
        if (!$assertionsDisabled && context != null) {
            throw new AssertionError();
        }
        Set<InvokeCallSite> invokeSites = this.invokeArgsToInvokeSite.get(argArray);
        if (invokeSites != null && this.reachingArgTypes.put(argArray, t)) {
            resolveInvoke(invokeSites);
        }
    }

    public void setArgArrayNonDetSize(Local argArray, Context context) {
        if (!$assertionsDisabled && context != null) {
            throw new AssertionError();
        }
        Set<InvokeCallSite> invokeSites = this.invokeArgsToInvokeSite.get(argArray);
        if (invokeSites != null && !this.invokeArgsToSize.containsKey(argArray)) {
            this.invokeArgsToSize.put(argArray, null);
            resolveInvoke(invokeSites);
        }
    }

    public void addPossibleArgArraySize(Local argArray, int value, Context context) {
        if (!$assertionsDisabled && context != null) {
            throw new AssertionError();
        }
        Set<InvokeCallSite> invokeSites = this.invokeArgsToInvokeSite.get(argArray);
        if (invokeSites != null) {
            BitSet sizeSet = this.invokeArgsToSize.get(argArray);
            if (sizeSet == null || !sizeSet.isEmpty()) {
                if (sizeSet == null) {
                    Map<Local, BitSet> map = this.invokeArgsToSize;
                    BitSet bitSet = new BitSet();
                    sizeSet = bitSet;
                    map.put(argArray, bitSet);
                }
                if (!sizeSet.get(value)) {
                    sizeSet.set(value);
                    resolveInvoke(invokeSites);
                }
            }
        }
    }

    private static Set<RefLikeType> resolveToClasses(Set<Type> rawTypes) {
        Set<SootClass> singleton;
        Set<RefLikeType> toReturn = new HashSet<>();
        if (!rawTypes.isEmpty()) {
            FastHierarchy fh = Scene.v().getOrMakeFastHierarchy();
            for (Type ty : rawTypes) {
                if (ty instanceof AnySubType) {
                    AnySubType anySubType = (AnySubType) ty;
                    RefType base = anySubType.getBase();
                    if (base.getSootClass().isInterface()) {
                        singleton = fh.getAllImplementersOfInterface(base.getSootClass());
                    } else {
                        singleton = Collections.singleton(base.getSootClass());
                    }
                    Set<SootClass> classRoots = singleton;
                    toReturn.addAll(getTransitiveSubClasses(classRoots));
                } else if (ty instanceof RefType) {
                    toReturn.add((RefType) ty);
                }
            }
        }
        return toReturn;
    }

    private static Collection<RefLikeType> getTransitiveSubClasses(Set<SootClass> classRoots) {
        Set<RefLikeType> resolved = new HashSet<>();
        if (!classRoots.isEmpty()) {
            FastHierarchy fh = Scene.v().getOrMakeFastHierarchy();
            LinkedList<SootClass> worklist = new LinkedList<>(classRoots);
            while (!worklist.isEmpty()) {
                SootClass cls = worklist.removeFirst();
                if (resolved.add(cls.getType())) {
                    worklist.addAll(fh.getSubclassesOf(cls));
                }
            }
        }
        return resolved;
    }

    private void resolveInvoke(Collection<InvokeCallSite> list) {
        for (InvokeCallSite ics : list) {
            Set<Type> s = this.reachingBaseTypes.get(ics.base());
            if (s != null && !s.isEmpty()) {
                if (ics.reachingTypes() != null) {
                    if (!$assertionsDisabled && ics.nullnessCode() == 0) {
                        throw new AssertionError();
                    }
                    resolveStaticTypes(s, ics);
                } else {
                    boolean mustNotBeNull = ics.nullnessCode() == 1;
                    boolean mustBeNull = ics.nullnessCode() == 0;
                    if (mustBeNull || (ics.nullnessCode() == -1 && (!this.invokeArgsToSize.containsKey(ics.argArray()) || !this.reachingArgTypes.containsKey(ics.argArray())))) {
                        for (Type bType : resolveToClasses(s)) {
                            if (!$assertionsDisabled && !(bType instanceof RefType)) {
                                throw new AssertionError();
                            }
                            if (!(bType instanceof ArrayType)) {
                                SootClass baseClass = ((RefType) bType).getSootClass();
                                if (!$assertionsDisabled && baseClass.isInterface()) {
                                    throw new AssertionError();
                                }
                                Iterator<SootMethod> mIt = getPublicNullaryMethodIterator(baseClass);
                                while (mIt.hasNext()) {
                                    SootMethod sm = mIt.next();
                                    this.cm.addVirtualEdge(ics.getContainer(), ics.getStmt(), sm, Kind.REFL_INVOKE, null);
                                }
                            }
                        }
                        continue;
                    } else {
                        Set<Type> reachingTypes = this.reachingArgTypes.get(ics.argArray());
                        if (reachingTypes == null || !this.invokeArgsToSize.containsKey(ics.argArray())) {
                            if (!$assertionsDisabled && ics.nullnessCode() != 1) {
                                throw new AssertionError(ics);
                            }
                            return;
                        }
                        BitSet methodSizes = this.invokeArgsToSize.get(ics.argArray());
                        for (Type bType2 : resolveToClasses(s)) {
                            if (!$assertionsDisabled && !(bType2 instanceof RefLikeType)) {
                                throw new AssertionError();
                            }
                            if (!(bType2 instanceof NullType) && !(bType2 instanceof ArrayType)) {
                                Iterator<SootMethod> mIt2 = getPublicMethodIterator(((RefType) bType2).getSootClass(), reachingTypes, methodSizes, mustNotBeNull);
                                while (mIt2.hasNext()) {
                                    SootMethod sm2 = mIt2.next();
                                    this.cm.addVirtualEdge(ics.container(), ics.stmt(), sm2, Kind.REFL_INVOKE, null);
                                }
                            }
                        }
                        continue;
                    }
                }
            }
        }
    }

    private void resolveStaticTypes(Set<Type> s, InvokeCallSite ics) {
        ConstantArrayAnalysis.ArrayTypes at = ics.reachingTypes();
        for (Type bType : resolveToClasses(s)) {
            if (!(bType instanceof ArrayType)) {
                SootClass baseClass = ((RefType) bType).getSootClass();
                Iterator<SootMethod> mIt = getPublicMethodIterator(baseClass, at);
                while (mIt.hasNext()) {
                    SootMethod sm = mIt.next();
                    this.cm.addVirtualEdge(ics.getContainer(), ics.getStmt(), sm, Kind.REFL_INVOKE, null);
                }
            }
        }
    }

    private static Iterator<SootMethod> getPublicMethodIterator(SootClass baseClass, final ConstantArrayAnalysis.ArrayTypes at) {
        return new AbstractMethodIterator(baseClass) { // from class: soot.jimple.toolkits.callgraph.OnFlyCallGraphBuilder.1
            @Override // soot.jimple.toolkits.callgraph.OnFlyCallGraphBuilder.AbstractMethodIterator
            protected boolean acceptMethod(SootMethod m) {
                if (!at.possibleSizes.contains(Integer.valueOf(m.getParameterCount()))) {
                    return false;
                }
                for (int i = 0; i < m.getParameterCount(); i++) {
                    Set<Type> possibleType = at.possibleTypes[i];
                    if (!possibleType.isEmpty() && !OnFlyCallGraphBuilder.isReflectionCompatible(m.getParameterType(i), possibleType)) {
                        return false;
                    }
                }
                return true;
            }
        };
    }

    private static PrimType[] narrowings(PrimType f) {
        if (f instanceof IntType) {
            return INT_NARROWINGS;
        }
        if (f instanceof ShortType) {
            return SHORT_NARROWINGS;
        }
        if (f instanceof LongType) {
            return LONG_NARROWINGS;
        }
        if (f instanceof ByteType) {
            return BYTE_NARROWINGS;
        }
        if (f instanceof FloatType) {
            return FLOAT_NARROWINGS;
        }
        if (f instanceof BooleanType) {
            return BOOLEAN_NARROWINGS;
        }
        if (f instanceof DoubleType) {
            return DOUBLE_NARROWINGS;
        }
        if (f instanceof CharType) {
            return CHAR_NARROWINGS;
        }
        throw new RuntimeException("Unexpected primitive type: " + f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isReflectionCompatible(Type paramType, Set<Type> reachingTypes) {
        PrimType[] narrowings;
        if (reachingTypes.contains(NullType.v())) {
            return true;
        }
        if (paramType instanceof RefLikeType) {
            FastHierarchy fh = Scene.v().getOrMakeFastHierarchy();
            for (Type rType : reachingTypes) {
                if (fh.canStoreType(rType, paramType)) {
                    return true;
                }
            }
            return false;
        } else if (paramType instanceof PrimType) {
            for (PrimType narrowings2 : narrowings((PrimType) paramType)) {
                if (reachingTypes.contains(narrowings2.boxedType())) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    private static Iterator<SootMethod> getPublicMethodIterator(SootClass baseClass, final Set<Type> reachingTypes, final BitSet methodSizes, final boolean mustNotBeNull) {
        if (baseClass.isPhantom()) {
            return Collections.emptyIterator();
        }
        return new AbstractMethodIterator(baseClass) { // from class: soot.jimple.toolkits.callgraph.OnFlyCallGraphBuilder.2
            @Override // soot.jimple.toolkits.callgraph.OnFlyCallGraphBuilder.AbstractMethodIterator
            protected boolean acceptMethod(SootMethod n) {
                if (methodSizes != null) {
                    int nParams = n.getParameterCount();
                    boolean compatibleSize = methodSizes.get(nParams) || (!mustNotBeNull && nParams == 0);
                    if (!compatibleSize) {
                        return false;
                    }
                }
                for (Type pTy : n.getParameterTypes()) {
                    if (!OnFlyCallGraphBuilder.isReflectionCompatible(pTy, reachingTypes)) {
                        return false;
                    }
                }
                return true;
            }
        };
    }

    private static Iterator<SootMethod> getPublicNullaryMethodIterator(SootClass baseClass) {
        if (baseClass.isPhantom()) {
            return Collections.emptyIterator();
        }
        return new AbstractMethodIterator(baseClass) { // from class: soot.jimple.toolkits.callgraph.OnFlyCallGraphBuilder.3
            @Override // soot.jimple.toolkits.callgraph.OnFlyCallGraphBuilder.AbstractMethodIterator
            protected boolean acceptMethod(SootMethod n) {
                return n.getParameterCount() == 0;
            }
        };
    }

    public void addType(Local receiver, Context srcContext, Type type, Context typeContext) {
        SootMethodRef ref;
        List<VirtualCallSite> rcvrToCallSites = this.receiverToSites.get(receiver);
        if (rcvrToCallSites != null) {
            VirtualCalls virtualCalls = VirtualCalls.v();
            Scene sc = Scene.v();
            FastHierarchy fh = sc.getOrMakeFastHierarchy();
            for (VirtualCallSite site : rcvrToCallSites) {
                if (!skipSite(site, fh, type)) {
                    InstanceInvokeExpr iie = site.iie();
                    if ((iie instanceof SpecialInvokeExpr) && !Kind.isFake(site.kind())) {
                        SootMethod target = virtualCalls.resolveSpecial(iie.getMethodRef(), site.getContainer(), this.appOnly);
                        if (target != null) {
                            this.targetsQueue.add(target);
                        }
                    } else {
                        Type receiverType = receiver.getType();
                        if (receiverType instanceof RefType) {
                            SootClass receiverClass = ((RefType) receiverType).getSootClass();
                            MethodSubSignature subsig = site.subSig();
                            ref = sc.makeMethodRef(receiverClass, subsig.methodName, subsig.parameterTypes, subsig.getReturnType(), Kind.isStatic(site.kind()));
                        } else {
                            ref = site.getStmt().getInvokeExpr().getMethodRef();
                        }
                        if (ref != null) {
                            virtualCalls.resolve(type, receiver.getType(), ref, site.getContainer(), this.targetsQueue, this.appOnly);
                            if (!this.targets.hasNext() && this.options.resolve_all_abstract_invokes()) {
                                virtualCalls.resolveSuperType(type, receiver.getType(), iie.getMethodRef(), this.targetsQueue, this.appOnly);
                            }
                        }
                    }
                    while (this.targets.hasNext()) {
                        this.cm.addVirtualEdge(MethodContext.v(site.getContainer(), srcContext), site.getStmt(), this.targets.next(), site.kind(), typeContext);
                    }
                }
            }
        }
        if (this.baseToInvokeSite.get(receiver) != null) {
            addBaseType(receiver, srcContext, type);
        }
    }

    protected boolean skipSite(VirtualCallSite site, FastHierarchy fh, Type type) {
        Kind k = site.kind();
        return k == Kind.THREAD ? !fh.canStoreType(type, this.clRunnable) : k == Kind.EXECUTOR ? !fh.canStoreType(type, this.clRunnable) : k == Kind.ASYNCTASK ? !fh.canStoreType(type, this.clAsyncTask) : k == Kind.HANDLER && !fh.canStoreType(type, this.clHandler);
    }

    public boolean wantStringConstants(Local stringConst) {
        return this.stringConstToSites.get(stringConst) != null;
    }

    public void addStringConstant(Local l, Context srcContext, String constant) {
        if (constant == null) {
            if (this.options.verbose()) {
                Iterator<VirtualCallSite> siteIt = this.stringConstToSites.get(l).iterator();
                while (siteIt.hasNext()) {
                    logger.warn("Method " + siteIt.next().getContainer() + " is reachable, and calls Class.forName on a non-constant String; graph will be incomplete! Use safe-forname option for a conservative result.");
                }
                return;
            }
            return;
        }
        Scene sc = Scene.v();
        for (VirtualCallSite site : this.stringConstToSites.get(l)) {
            int constLen = constant.length();
            if (constLen > 0 && constant.charAt(0) == '[') {
                if (constLen > 2 && constant.charAt(1) == 'L' && constant.charAt(constLen - 1) == ';') {
                    constant = constant.substring(2, constLen - 1);
                }
            }
            if (sc.containsClass(constant)) {
                SootClass sootcls = sc.getSootClass(constant);
                if (!sootcls.isApplicationClass() && !sootcls.isPhantom()) {
                    sootcls.setLibraryClass();
                }
                for (SootMethod clinit : EntryPoints.v().clinitsOf(sootcls)) {
                    this.cm.addStaticEdge(MethodContext.v(site.getContainer(), srcContext), site.getStmt(), clinit, Kind.CLINIT);
                }
            } else if (this.options.verbose()) {
                logger.warn("Class " + constant + " is a dynamic class and was not specified as such; graph will be incomplete!");
            }
        }
    }

    public boolean wantArrayField(AllocDotField df) {
        return this.allocDotFieldToLocal.containsKey(df);
    }

    public void addInvokeArgType(AllocDotField df, Context context, Type type) {
        if (this.allocDotFieldToLocal.containsKey(df)) {
            for (Local l : this.allocDotFieldToLocal.get(df)) {
                addInvokeArgType(l, context, type);
            }
        }
    }

    public boolean wantInvokeArg(Local receiver) {
        return this.invokeArgsToInvokeSite.containsKey(receiver);
    }

    public void addInvokeArgDotField(Local receiver, AllocDotField dot) {
        this.allocDotFieldToLocal.put(dot, receiver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addInvokeCallSite(Stmt s, SootMethod container, InstanceInvokeExpr d) {
        int nullnessCode;
        InvokeCallSite ics;
        Local l = (Local) d.getArg(0);
        Value argArray = d.getArg(1);
        if (argArray instanceof NullConstant) {
            ics = new InvokeCallSite(s, container, d, l);
        } else {
            if (this.analysisKey != container) {
                ExceptionalUnitGraph graph = ExceptionalUnitGraphFactory.createExceptionalUnitGraph(container.getActiveBody());
                this.nullnessCache = new NullnessAnalysis(graph);
                this.arrayCache = new ConstantArrayAnalysis(graph, container.getActiveBody());
                this.analysisKey = container;
            }
            Local argLocal = (Local) argArray;
            if (this.nullnessCache.isAlwaysNonNullBefore(s, argLocal)) {
                nullnessCode = 1;
            } else if (this.nullnessCache.isAlwaysNullBefore(s, argLocal)) {
                nullnessCode = 0;
            } else {
                nullnessCode = -1;
            }
            if (nullnessCode != 0 && this.arrayCache.isConstantBefore(s, argLocal)) {
                ConstantArrayAnalysis.ArrayTypes reachingArgTypes = this.arrayCache.getArrayTypesBefore(s, argLocal);
                if (nullnessCode == -1) {
                    reachingArgTypes.possibleSizes.add(0);
                }
                ics = new InvokeCallSite(s, container, d, l, reachingArgTypes, nullnessCode);
            } else {
                ics = new InvokeCallSite(s, container, d, l, argLocal, nullnessCode);
                this.invokeArgsToInvokeSite.put(argLocal, ics);
            }
        }
        this.baseToInvokeSite.put(l, ics);
    }

    private void addVirtualCallSite(Stmt s, SootMethod m, Local receiver, InstanceInvokeExpr iie, MethodSubSignature subSig, Kind kind) {
        List<VirtualCallSite> sites = this.receiverToSites.get(receiver);
        if (sites == null) {
            LargeNumberedMap<Local, List<VirtualCallSite>> largeNumberedMap = this.receiverToSites;
            List<VirtualCallSite> arrayList = new ArrayList<>();
            sites = arrayList;
            largeNumberedMap.put(receiver, arrayList);
            List<Local> receivers = this.methodToReceivers.get(m);
            if (receivers == null) {
                LargeNumberedMap<SootMethod, List<Local>> largeNumberedMap2 = this.methodToReceivers;
                List<Local> arrayList2 = new ArrayList<>();
                receivers = arrayList2;
                largeNumberedMap2.put(m, arrayList2);
            }
            receivers.add(receiver);
        }
        sites.add(new VirtualCallSite(s, m, iie, subSig, kind));
    }

    protected void processNewMethod(SootMethod m) {
        if (m.isConcrete()) {
            Body b = m.retrieveActiveBody();
            getImplicitTargets(m);
            findReceivers(m, b);
        }
    }

    protected void findReceivers(SootMethod m, Body b) {
        Iterator<Unit> it = b.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            Stmt s = (Stmt) u;
            if (s.containsInvokeExpr()) {
                InvokeExpr ie = s.getInvokeExpr();
                if (ie instanceof InstanceInvokeExpr) {
                    InstanceInvokeExpr iie = (InstanceInvokeExpr) ie;
                    Local receiver = (Local) iie.getBase();
                    MethodSubSignature subSig = new MethodSubSignature(iie.getMethodRef());
                    addVirtualCallSite(s, m, receiver, iie, new MethodSubSignature(iie.getMethodRef()), Edge.ieToKind(iie));
                    VirtualEdgesSummaries.VirtualEdge virtualEdge = this.virtualEdgeSummaries.getVirtualEdgesMatchingSubSig(subSig);
                    if (virtualEdge != null) {
                        for (VirtualEdgesSummaries.VirtualEdgeTarget t : virtualEdge.targets) {
                            processVirtualEdgeSummary(m, s, receiver, t, virtualEdge.edgeType);
                        }
                    }
                } else if (ie instanceof DynamicInvokeExpr) {
                    if (this.options.verbose()) {
                        logger.warn("InvokeDynamic to " + ie + " not resolved during call-graph construction.");
                    }
                } else {
                    SootMethod tgt = ie.getMethod();
                    if (tgt != null) {
                        addEdge(m, s, tgt);
                        String signature = tgt.getSignature();
                        VirtualEdgesSummaries.VirtualEdge virtualEdge2 = this.virtualEdgeSummaries.getVirtualEdgesMatchingFunction(signature);
                        if (virtualEdge2 != null) {
                            for (VirtualEdgesSummaries.VirtualEdgeTarget t2 : virtualEdge2.targets) {
                                if (t2 instanceof VirtualEdgesSummaries.DirectTarget) {
                                    VirtualEdgesSummaries.DirectTarget directTarget = (VirtualEdgesSummaries.DirectTarget) t2;
                                    if (!t2.isBase()) {
                                        Value runnable = ie.getArg(t2.argIndex);
                                        if (runnable instanceof Local) {
                                            addVirtualCallSite(s, m, (Local) runnable, null, directTarget.targetMethod, Kind.GENERIC_FAKE);
                                        }
                                    }
                                }
                            }
                        }
                    } else if (!Options.v().ignore_resolution_errors()) {
                        throw new InternalError("Unresolved target " + ie.getMethod() + ". Resolution error should have occured earlier.");
                    }
                }
            }
        }
    }

    protected void processVirtualEdgeSummary(SootMethod m, Stmt s, Local receiver, VirtualEdgesSummaries.VirtualEdgeTarget target, Kind edgeType) {
        processVirtualEdgeSummary(m, s, s, receiver, target, edgeType);
    }

    protected void processVirtualEdgeSummary(SootMethod callSiteMethod, Stmt callSite, Stmt curStmt, Local receiver, VirtualEdgesSummaries.VirtualEdgeTarget target, Kind edgeType) {
        InvokeExpr ie = curStmt.getInvokeExpr();
        Local targetLocal = null;
        if (target.isBase()) {
            targetLocal = receiver;
        } else if (target.argIndex < ie.getArgCount()) {
            Value runnable = ie.getArg(target.argIndex);
            if (runnable instanceof Local) {
                targetLocal = (Local) runnable;
            }
        }
        if (targetLocal == null) {
            return;
        }
        if (target instanceof VirtualEdgesSummaries.DirectTarget) {
            VirtualEdgesSummaries.DirectTarget directTarget = (VirtualEdgesSummaries.DirectTarget) target;
            addVirtualCallSite(callSite, callSiteMethod, targetLocal, (InstanceInvokeExpr) ie, directTarget.targetMethod, edgeType);
        } else if (target instanceof VirtualEdgesSummaries.IndirectTarget) {
            VirtualEdgesSummaries.IndirectTarget w = (VirtualEdgesSummaries.IndirectTarget) target;
            List<VirtualCallSite> indirectSites = this.receiverToSites.get(targetLocal);
            if (indirectSites != null) {
                Iterator it = new ArrayList(indirectSites).iterator();
                while (it.hasNext()) {
                    VirtualCallSite site = (VirtualCallSite) it.next();
                    if (w.getTargetMethod().equals(site.subSig())) {
                        for (VirtualEdgesSummaries.VirtualEdgeTarget siteTarget : w.getTargets()) {
                            Stmt siteStmt = site.getStmt();
                            if (siteStmt.containsInvokeExpr()) {
                                processVirtualEdgeSummary(callSiteMethod, callSite, siteStmt, receiver, siteTarget, edgeType);
                            }
                        }
                    }
                }
            }
        }
    }

    private void getImplicitTargets(SootMethod source) {
        SootClass scl = source.getDeclaringClass();
        if (!source.isConcrete()) {
            return;
        }
        if (source.getSubSignature().contains("<init>")) {
            handleInit(source, scl);
        }
        Iterator<Unit> it = source.retrieveActiveBody().getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            Stmt s = (Stmt) u;
            if (s.containsInvokeExpr()) {
                InvokeExpr ie = s.getInvokeExpr();
                SootMethodRef methodRef = ie.getMethodRef();
                String name = methodRef.getDeclaringClass().getName();
                switch (name.hashCode()) {
                    case -600300427:
                        if (name.equals("java.lang.reflect.Constructor") && "java.lang.Object newInstance(java.lang.Object[])".equals(methodRef.getSubSignature().getString())) {
                            this.reflectionModel.contructorNewInstance(source, s);
                            break;
                        }
                        break;
                    case -530663260:
                        if (name.equals("java.lang.Class") && "java.lang.Object newInstance()".equals(methodRef.getSubSignature().getString())) {
                            this.reflectionModel.classNewInstance(source, s);
                            break;
                        }
                        break;
                    case 253453190:
                        if (name.equals("java.lang.reflect.Method") && "java.lang.Object invoke(java.lang.Object,java.lang.Object[])".equals(methodRef.getSubSignature().getString())) {
                            this.reflectionModel.methodInvoke(source, s);
                            break;
                        }
                        break;
                }
                if (methodRef.getSubSignature() == this.sigForName) {
                    this.reflectionModel.classForName(source, s);
                }
                if (ie instanceof StaticInvokeExpr) {
                    SootClass cl = ie.getMethodRef().getDeclaringClass();
                    for (SootMethod clinit : EntryPoints.v().clinitsOf(cl)) {
                        addEdge(source, s, clinit, Kind.CLINIT);
                    }
                }
            }
            if (s.containsFieldRef()) {
                FieldRef fr = s.getFieldRef();
                if (fr instanceof StaticFieldRef) {
                    SootClass cl2 = fr.getFieldRef().declaringClass();
                    for (SootMethod clinit2 : EntryPoints.v().clinitsOf(cl2)) {
                        addEdge(source, s, clinit2, Kind.CLINIT);
                    }
                }
            }
            if (s instanceof AssignStmt) {
                Value rhs = ((AssignStmt) s).getRightOp();
                if (rhs instanceof NewExpr) {
                    NewExpr r = (NewExpr) rhs;
                    SootClass cl3 = r.getBaseType().getSootClass();
                    for (SootMethod clinit3 : EntryPoints.v().clinitsOf(cl3)) {
                        addEdge(source, s, clinit3, Kind.CLINIT);
                    }
                } else if ((rhs instanceof NewArrayExpr) || (rhs instanceof NewMultiArrayExpr)) {
                    Type t = rhs.getType();
                    if (t instanceof ArrayType) {
                        t = ((ArrayType) t).baseType;
                    }
                    if (t instanceof RefType) {
                        SootClass cl4 = ((RefType) t).getSootClass();
                        for (SootMethod clinit4 : EntryPoints.v().clinitsOf(cl4)) {
                            addEdge(source, s, clinit4, Kind.CLINIT);
                        }
                    }
                }
            }
        }
    }

    protected void processNewMethodContext(MethodOrMethodContext momc) {
        SootMethod m = momc.method();
        Iterator<Edge> it = this.cicg.edgesOutOf(m);
        while (it.hasNext()) {
            Edge e = it.next();
            this.cm.addStaticEdge(momc, e.srcUnit(), e.tgt(), e.kind());
        }
    }

    private void handleInit(SootMethod source, SootClass scl) {
        addEdge(source, null, scl, this.sigFinalize, Kind.FINALIZE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void constantForName(String cls, SootMethod src, Stmt srcUnit) {
        int clsLen = cls.length();
        if (clsLen > 0 && cls.charAt(0) == '[') {
            if (clsLen > 2 && cls.charAt(1) == 'L' && cls.charAt(clsLen - 1) == ';') {
                constantForName(cls.substring(2, clsLen - 1), src, srcUnit);
                return;
            }
            return;
        }
        Scene sc = Scene.v();
        if (!sc.containsClass(cls)) {
            if (this.options.verbose()) {
                logger.warn("Class " + cls + " is a dynamic class and was not specified as such; graph will be incomplete!");
                return;
            }
            return;
        }
        SootClass sootcls = sc.getSootClass(cls);
        if (!sootcls.isPhantomClass()) {
            if (!sootcls.isApplicationClass()) {
                sootcls.setLibraryClass();
            }
            for (SootMethod clinit : EntryPoints.v().clinitsOf(sootcls)) {
                addEdge(src, srcUnit, clinit, Kind.CLINIT);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addEdge(SootMethod src, Stmt stmt, SootMethod tgt, Kind kind) {
        if (src.equals(tgt) && src.isStaticInitializer()) {
            return;
        }
        this.cicg.addEdge(new Edge(src, stmt, tgt, kind));
    }

    private void addEdge(SootMethod src, Stmt stmt, SootClass cls, NumberedString methodSubSig, Kind kind) {
        SootMethod sm = cls.getMethodUnsafe(methodSubSig);
        if (sm != null) {
            addEdge(src, stmt, sm, kind);
        }
    }

    private void addEdge(SootMethod src, Stmt stmt, SootMethod tgt) {
        InvokeExpr ie = stmt.getInvokeExpr();
        addEdge(src, stmt, tgt, Edge.ieToKind(ie));
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/callgraph/OnFlyCallGraphBuilder$DefaultReflectionModel.class */
    public class DefaultReflectionModel implements ReflectionModel {
        protected final CGOptions options = new CGOptions(PhaseOptions.v().getPhaseOptions("cg"));
        protected final HashSet<SootMethod> warnedAlready = new HashSet<>();

        public DefaultReflectionModel() {
        }

        @Override // soot.jimple.toolkits.callgraph.ReflectionModel
        public void classForName(SootMethod source, Stmt s) {
            List<Local> stringConstants = OnFlyCallGraphBuilder.this.methodToStringConstants.get(source);
            if (stringConstants == null) {
                LargeNumberedMap<SootMethod, List<Local>> largeNumberedMap = OnFlyCallGraphBuilder.this.methodToStringConstants;
                List<Local> arrayList = new ArrayList<>();
                stringConstants = arrayList;
                largeNumberedMap.put(source, arrayList);
            }
            Value className = s.getInvokeExpr().getArg(0);
            if (className instanceof StringConstant) {
                String cls = ((StringConstant) className).value;
                OnFlyCallGraphBuilder.this.constantForName(cls, source, s);
            } else if (className instanceof Local) {
                Local constant = (Local) className;
                if (this.options.safe_forname()) {
                    for (SootMethod tgt : EntryPoints.v().clinits()) {
                        OnFlyCallGraphBuilder.this.addEdge(source, s, tgt, Kind.CLINIT);
                    }
                    return;
                }
                EntryPoints ep = EntryPoints.v();
                for (SootClass cls2 : Scene.v().dynamicClasses()) {
                    for (SootMethod clinit : ep.clinitsOf(cls2)) {
                        OnFlyCallGraphBuilder.this.addEdge(source, s, clinit, Kind.CLINIT);
                    }
                }
                VirtualCallSite site = new VirtualCallSite(s, source, null, null, Kind.CLINIT);
                List<VirtualCallSite> sites = OnFlyCallGraphBuilder.this.stringConstToSites.get(constant);
                if (sites == null) {
                    SmallNumberedMap<Local, List<VirtualCallSite>> smallNumberedMap = OnFlyCallGraphBuilder.this.stringConstToSites;
                    List<VirtualCallSite> arrayList2 = new ArrayList<>();
                    sites = arrayList2;
                    smallNumberedMap.put(constant, arrayList2);
                    stringConstants.add(constant);
                }
                sites.add(site);
            }
        }

        @Override // soot.jimple.toolkits.callgraph.ReflectionModel
        public void classNewInstance(SootMethod source, Stmt s) {
            if (this.options.safe_newinstance()) {
                for (SootMethod tgt : EntryPoints.v().inits()) {
                    OnFlyCallGraphBuilder.this.addEdge(source, s, tgt, Kind.NEWINSTANCE);
                }
                return;
            }
            for (SootClass cls : Scene.v().dynamicClasses()) {
                SootMethod sm = cls.getMethodUnsafe(OnFlyCallGraphBuilder.this.sigInit);
                if (sm != null) {
                    OnFlyCallGraphBuilder.this.addEdge(source, s, sm, Kind.NEWINSTANCE);
                }
            }
            if (this.options.verbose()) {
                OnFlyCallGraphBuilder.logger.warn("Method " + source + " is reachable, and calls Class.newInstance; graph will be incomplete! Use safe-newinstance option for a conservative result.");
            }
        }

        @Override // soot.jimple.toolkits.callgraph.ReflectionModel
        public void contructorNewInstance(SootMethod source, Stmt s) {
            if (this.options.safe_newinstance()) {
                for (SootMethod tgt : EntryPoints.v().allInits()) {
                    OnFlyCallGraphBuilder.this.addEdge(source, s, tgt, Kind.NEWINSTANCE);
                }
                return;
            }
            for (SootClass cls : Scene.v().dynamicClasses()) {
                for (SootMethod m : cls.getMethods()) {
                    if ("<init>".equals(m.getName())) {
                        OnFlyCallGraphBuilder.this.addEdge(source, s, m, Kind.NEWINSTANCE);
                    }
                }
            }
            if (this.options.verbose()) {
                OnFlyCallGraphBuilder.logger.warn("Method " + source + " is reachable, and calls Constructor.newInstance; graph will be incomplete! Use safe-newinstance option for a conservative result.");
            }
        }

        @Override // soot.jimple.toolkits.callgraph.ReflectionModel
        public void methodInvoke(SootMethod container, Stmt invokeStmt) {
            if (!warnedAlready(container)) {
                if (this.options.verbose()) {
                    OnFlyCallGraphBuilder.logger.warn("Call to java.lang.reflect.Method: invoke() from " + container + "; graph will be incomplete!");
                }
                markWarned(container);
            }
        }

        private void markWarned(SootMethod m) {
            this.warnedAlready.add(m);
        }

        private boolean warnedAlready(SootMethod m) {
            return this.warnedAlready.contains(m);
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/callgraph/OnFlyCallGraphBuilder$TypeBasedReflectionModel.class */
    public class TypeBasedReflectionModel extends DefaultReflectionModel {
        public TypeBasedReflectionModel() {
            super();
        }

        @Override // soot.jimple.toolkits.callgraph.OnFlyCallGraphBuilder.DefaultReflectionModel, soot.jimple.toolkits.callgraph.ReflectionModel
        public void methodInvoke(SootMethod container, Stmt invokeStmt) {
            if (container.getDeclaringClass().isJavaLibraryClass()) {
                super.methodInvoke(container, invokeStmt);
                return;
            }
            InstanceInvokeExpr d = (InstanceInvokeExpr) invokeStmt.getInvokeExpr();
            Value base = d.getArg(0);
            if (base instanceof Local) {
                OnFlyCallGraphBuilder.this.addInvokeCallSite(invokeStmt, container, d);
            } else {
                super.methodInvoke(container, invokeStmt);
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/callgraph/OnFlyCallGraphBuilder$TraceBasedReflectionModel.class */
    public class TraceBasedReflectionModel implements ReflectionModel {
        protected final Set<Guard> guards;
        protected final ReflectionTraceInfo reflectionInfo;

        private TraceBasedReflectionModel() {
            String logFile = OnFlyCallGraphBuilder.this.options.reflection_log();
            if (logFile == null) {
                throw new InternalError("Trace based refection model enabled but no trace file given!?");
            }
            this.reflectionInfo = new ReflectionTraceInfo(logFile);
            this.guards = new HashSet();
        }

        /* synthetic */ TraceBasedReflectionModel(OnFlyCallGraphBuilder onFlyCallGraphBuilder, TraceBasedReflectionModel traceBasedReflectionModel) {
            this();
        }

        @Override // soot.jimple.toolkits.callgraph.ReflectionModel
        public void classForName(SootMethod container, Stmt forNameInvokeStmt) {
            Set<String> classNames = this.reflectionInfo.classForNameClassNames(container);
            if (classNames == null || classNames.isEmpty()) {
                registerGuard(container, forNameInvokeStmt, "Class.forName() call site; Soot did not expect this site to be reached");
                return;
            }
            for (String clsName : classNames) {
                OnFlyCallGraphBuilder.this.constantForName(clsName, container, forNameInvokeStmt);
            }
        }

        @Override // soot.jimple.toolkits.callgraph.ReflectionModel
        public void classNewInstance(SootMethod container, Stmt newInstanceInvokeStmt) {
            Set<String> classNames = this.reflectionInfo.classNewInstanceClassNames(container);
            if (classNames == null || classNames.isEmpty()) {
                registerGuard(container, newInstanceInvokeStmt, "Class.newInstance() call site; Soot did not expect this site to be reached");
                return;
            }
            Scene sc = Scene.v();
            for (String clsName : classNames) {
                SootMethod constructor = sc.getSootClass(clsName).getMethodUnsafe(OnFlyCallGraphBuilder.this.sigInit);
                if (constructor != null) {
                    OnFlyCallGraphBuilder.this.addEdge(container, newInstanceInvokeStmt, constructor, Kind.REFL_CLASS_NEWINSTANCE);
                }
            }
        }

        @Override // soot.jimple.toolkits.callgraph.ReflectionModel
        public void contructorNewInstance(SootMethod container, Stmt newInstanceInvokeStmt) {
            Set<String> constructorSignatures = this.reflectionInfo.constructorNewInstanceSignatures(container);
            if (constructorSignatures == null || constructorSignatures.isEmpty()) {
                registerGuard(container, newInstanceInvokeStmt, "Constructor.newInstance(..) call site; Soot did not expect this site to be reached");
                return;
            }
            Scene sc = Scene.v();
            for (String constructorSignature : constructorSignatures) {
                SootMethod constructor = sc.getMethod(constructorSignature);
                OnFlyCallGraphBuilder.this.addEdge(container, newInstanceInvokeStmt, constructor, Kind.REFL_CONSTR_NEWINSTANCE);
            }
        }

        @Override // soot.jimple.toolkits.callgraph.ReflectionModel
        public void methodInvoke(SootMethod container, Stmt invokeStmt) {
            Set<String> methodSignatures = this.reflectionInfo.methodInvokeSignatures(container);
            if (methodSignatures == null || methodSignatures.isEmpty()) {
                registerGuard(container, invokeStmt, "Method.invoke(..) call site; Soot did not expect this site to be reached");
                return;
            }
            Scene sc = Scene.v();
            for (String methodSignature : methodSignatures) {
                SootMethod method = sc.getMethod(methodSignature);
                OnFlyCallGraphBuilder.this.addEdge(container, invokeStmt, method, Kind.REFL_INVOKE);
            }
        }

        private void registerGuard(SootMethod container, Stmt stmt, String string) {
            this.guards.add(new Guard(container, stmt, string));
            if (OnFlyCallGraphBuilder.this.options.verbose()) {
                OnFlyCallGraphBuilder.logger.debug("Incomplete trace file: Class.forName() is called in method '" + container + "' but trace contains no information about the receiver class of this call.");
                String guards = OnFlyCallGraphBuilder.this.options.guards();
                switch (guards.hashCode()) {
                    case -1190396462:
                        if (guards.equals(Definer.OnError.POLICY_IGNORE)) {
                            OnFlyCallGraphBuilder.logger.debug("Guarding strategy is set to 'ignore'. Will ignore this problem.");
                            break;
                        }
                        throw new RuntimeException("Invalid value for phase option (guarding): " + OnFlyCallGraphBuilder.this.options.guards());
                    case 106934957:
                        if (guards.equals("print")) {
                            OnFlyCallGraphBuilder.logger.debug("Guarding strategy is set to 'print'. Program will print a stack trace if this location is reached during execution.");
                            break;
                        }
                        throw new RuntimeException("Invalid value for phase option (guarding): " + OnFlyCallGraphBuilder.this.options.guards());
                    case 110339814:
                        if (guards.equals(Jimple.THROW)) {
                            OnFlyCallGraphBuilder.logger.debug("Guarding strategy is set to 'throw'. Program will throw an error if this location is reached during execution.");
                            break;
                        }
                        throw new RuntimeException("Invalid value for phase option (guarding): " + OnFlyCallGraphBuilder.this.options.guards());
                    default:
                        throw new RuntimeException("Invalid value for phase option (guarding): " + OnFlyCallGraphBuilder.this.options.guards());
                }
            }
            if (!OnFlyCallGraphBuilder.registeredGuardsTransformation) {
                OnFlyCallGraphBuilder.registeredGuardsTransformation = true;
                PackManager.v().getPack("wjap").add(new Transform("wjap.guards", new SceneTransformer() { // from class: soot.jimple.toolkits.callgraph.OnFlyCallGraphBuilder.TraceBasedReflectionModel.1
                    @Override // soot.SceneTransformer
                    protected void internalTransform(String phaseName, Map<String, String> options) {
                        for (Guard g : TraceBasedReflectionModel.this.guards) {
                            TraceBasedReflectionModel.this.insertGuard(g);
                        }
                    }
                }));
                PhaseOptions.v().setPhaseOption("wjap.guards", "enabled");
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void insertGuard(Guard guard) {
            if (Definer.OnError.POLICY_IGNORE.equals(OnFlyCallGraphBuilder.this.options.guards())) {
                return;
            }
            SootMethod container = guard.container;
            if (!container.hasActiveBody()) {
                OnFlyCallGraphBuilder.logger.warn("Tried to insert guard into " + container + " but couldn't because method has no body.");
                return;
            }
            Jimple jimp = Jimple.v();
            Body body = container.getActiveBody();
            UnitPatchingChain units = body.getUnits();
            LocalGenerator lg = Scene.v().createLocalGenerator(body);
            RefType runtimeExceptionType = RefType.v("java.lang.Error");
            Local exceptionLocal = lg.generateLocal(runtimeExceptionType);
            AssignStmt assignStmt = jimp.newAssignStmt(exceptionLocal, jimp.newNewExpr(runtimeExceptionType));
            units.insertBefore(assignStmt, (AssignStmt) guard.stmt);
            SootMethodRef cref = runtimeExceptionType.getSootClass().getMethod("<init>", Collections.singletonList(RefType.v("java.lang.String"))).makeRef();
            InvokeStmt initStmt = jimp.newInvokeStmt(jimp.newSpecialInvokeExpr(exceptionLocal, cref, StringConstant.v(guard.message)));
            units.insertAfter(initStmt, (InvokeStmt) assignStmt);
            String guards = OnFlyCallGraphBuilder.this.options.guards();
            switch (guards.hashCode()) {
                case 106934957:
                    if (guards.equals("print")) {
                        VirtualInvokeExpr printStackTraceExpr = jimp.newVirtualInvokeExpr(exceptionLocal, Scene.v().getSootClass(Scene.v().getBaseExceptionType().toString()).getMethod("printStackTrace", Collections.emptyList()).makeRef());
                        units.insertAfter(jimp.newInvokeStmt(printStackTraceExpr), initStmt);
                        return;
                    }
                    break;
                case 110339814:
                    if (guards.equals(Jimple.THROW)) {
                        units.insertAfter(jimp.newThrowStmt(exceptionLocal), (ThrowStmt) initStmt);
                        return;
                    }
                    break;
            }
            throw new RuntimeException("Invalid value for phase option (guarding): " + OnFlyCallGraphBuilder.this.options.guards());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/callgraph/OnFlyCallGraphBuilder$Guard.class */
    public static final class Guard {
        final SootMethod container;
        final Stmt stmt;
        final String message;

        public Guard(SootMethod container, Stmt stmt, String message) {
            this.container = container;
            this.stmt = stmt;
            this.message = message;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/callgraph/OnFlyCallGraphBuilder$AbstractMethodIterator.class */
    private static abstract class AbstractMethodIterator implements Iterator<SootMethod> {
        private SootMethod next = null;
        private SootClass currClass;
        private Iterator<SootMethod> methodIterator;

        protected abstract boolean acceptMethod(SootMethod sootMethod);

        AbstractMethodIterator(SootClass baseClass) {
            this.currClass = baseClass;
            this.methodIterator = baseClass.methodIterator();
            findNextMethod();
        }

        protected void findNextMethod() {
            this.next = null;
            if (this.methodIterator != null) {
                while (true) {
                    if (this.methodIterator.hasNext()) {
                        SootMethod n = this.methodIterator.next();
                        if (n.isPublic() && !n.isStatic() && !n.isConstructor() && !n.isStaticInitializer() && n.isConcrete() && acceptMethod(n)) {
                            this.next = n;
                            return;
                        }
                    } else if (!this.currClass.hasSuperclass()) {
                        this.methodIterator = null;
                        return;
                    } else {
                        SootClass superclass = this.currClass.getSuperclass();
                        if (superclass.isPhantom() || Scene.v().getObjectType().toString().equals(superclass.getName())) {
                            break;
                        }
                        this.methodIterator = superclass.methodIterator();
                        this.currClass = superclass;
                    }
                }
                this.methodIterator = null;
            }
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.next != null;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public SootMethod next() {
            SootMethod toRet = this.next;
            findNextMethod();
            return toRet;
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
