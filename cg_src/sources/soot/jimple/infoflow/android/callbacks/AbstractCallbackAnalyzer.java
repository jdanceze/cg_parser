package soot.jimple.infoflow.android.callbacks;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import heros.solver.Pair;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import org.apache.tools.ant.taskdefs.optional.vss.MSVSSConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.AnySubType;
import soot.Body;
import soot.FastHierarchy;
import soot.Local;
import soot.PointsToSet;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.CastExpr;
import soot.jimple.ClassConstant;
import soot.jimple.FieldRef;
import soot.jimple.IdentityStmt;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.Stmt;
import soot.jimple.infoflow.android.InfoflowAndroidConfiguration;
import soot.jimple.infoflow.android.callbacks.AndroidCallbackDefinition;
import soot.jimple.infoflow.android.callbacks.filters.ICallbackFilter;
import soot.jimple.infoflow.android.entryPointCreators.AndroidEntryPointConstants;
import soot.jimple.infoflow.android.source.parsers.xml.ResourceUtils;
import soot.jimple.infoflow.entryPointCreators.SimulatedCodeElementTag;
import soot.jimple.infoflow.typing.TypeUtils;
import soot.jimple.infoflow.util.SootMethodRepresentationParser;
import soot.jimple.infoflow.util.SystemClassHandler;
import soot.jimple.infoflow.values.IValueProvider;
import soot.jimple.infoflow.values.SimpleConstantValueProvider;
import soot.jimple.toolkits.callgraph.Edge;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.ExceptionalUnitGraphFactory;
import soot.toolkits.scalar.SimpleLocalDefs;
import soot.util.HashMultiMap;
import soot.util.MultiMap;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/callbacks/AbstractCallbackAnalyzer.class */
public abstract class AbstractCallbackAnalyzer {
    private static final String SIG_CAR_CREATE = "<android.car.Car: android.car.Car createCar(android.content.Context,android.content.ServiceConnection)>";
    protected final Logger logger;
    protected final SootClass scContext;
    protected final SootClass scBroadcastReceiver;
    protected final SootClass scServiceConnection;
    protected final SootClass scFragmentTransaction;
    protected final SootClass scFragment;
    protected final SootClass scSupportFragmentTransaction;
    protected final SootClass scAndroidXFragmentTransaction;
    protected final SootClass scSupportFragment;
    protected final SootClass scAndroidXFragment;
    protected final SootClass scSupportViewPager;
    protected final SootClass scAndroidXViewPager;
    protected final SootClass scFragmentStatePagerAdapter;
    protected final SootClass scFragmentPagerAdapter;
    protected final SootClass scAndroidXFragmentStatePagerAdapter;
    protected final SootClass scAndroidXFragmentPagerAdapter;
    protected final InfoflowAndroidConfiguration config;
    protected final Set<SootClass> entryPointClasses;
    protected final Set<String> androidCallbacks;
    protected final MultiMap<SootClass, AndroidCallbackDefinition> callbackMethods;
    protected final MultiMap<SootClass, Integer> layoutClasses;
    protected final Set<SootClass> dynamicManifestComponents;
    protected final MultiMap<SootClass, SootClass> fragmentClasses;
    protected final MultiMap<SootClass, SootClass> fragmentClassesRev;
    protected final Map<SootClass, Integer> fragmentIDs;
    protected final List<ICallbackFilter> callbackFilters;
    protected final Set<SootClass> excludedEntryPoints;
    protected IValueProvider valueProvider;
    protected LoadingCache<SootField, List<Type>> arrayToContentTypes;

    public AbstractCallbackAnalyzer(InfoflowAndroidConfiguration config, Set<SootClass> entryPointClasses) throws IOException {
        this(config, entryPointClasses, "AndroidCallbacks.txt");
    }

    public AbstractCallbackAnalyzer(InfoflowAndroidConfiguration config, Set<SootClass> entryPointClasses, String callbackFile) throws IOException {
        this(config, entryPointClasses, loadAndroidCallbacks(callbackFile));
    }

    public AbstractCallbackAnalyzer(InfoflowAndroidConfiguration config, Set<SootClass> entryPointClasses, InputStream inputStream) throws IOException {
        this(config, entryPointClasses, loadAndroidCallbacks(new InputStreamReader(inputStream)));
    }

    public AbstractCallbackAnalyzer(InfoflowAndroidConfiguration config, Set<SootClass> entryPointClasses, Reader reader) throws IOException {
        this(config, entryPointClasses, loadAndroidCallbacks(reader));
    }

    public AbstractCallbackAnalyzer(InfoflowAndroidConfiguration config, Set<SootClass> entryPointClasses, Set<String> androidCallbacks) throws IOException {
        this.logger = LoggerFactory.getLogger(getClass());
        this.scContext = Scene.v().getSootClassUnsafe("android.content.Context");
        this.scBroadcastReceiver = Scene.v().getSootClassUnsafe(AndroidEntryPointConstants.BROADCASTRECEIVERCLASS);
        this.scServiceConnection = Scene.v().getSootClassUnsafe(AndroidEntryPointConstants.SERVICECONNECTIONINTERFACE);
        this.scFragmentTransaction = Scene.v().getSootClassUnsafe("android.app.FragmentTransaction");
        this.scFragment = Scene.v().getSootClassUnsafe(AndroidEntryPointConstants.FRAGMENTCLASS);
        this.scSupportFragmentTransaction = Scene.v().getSootClassUnsafe("android.support.v4.app.FragmentTransaction");
        this.scAndroidXFragmentTransaction = Scene.v().getSootClassUnsafe("androidx.fragment.app.FragmentTransaction");
        this.scSupportFragment = Scene.v().getSootClassUnsafe(AndroidEntryPointConstants.SUPPORTFRAGMENTCLASS);
        this.scAndroidXFragment = Scene.v().getSootClassUnsafe(AndroidEntryPointConstants.ANDROIDXFRAGMENTCLASS);
        this.scSupportViewPager = Scene.v().getSootClassUnsafe("android.support.v4.view.ViewPager");
        this.scAndroidXViewPager = Scene.v().getSootClassUnsafe("androidx.viewpager.widget.ViewPager");
        this.scFragmentStatePagerAdapter = Scene.v().getSootClassUnsafe("android.support.v4.app.FragmentStatePagerAdapter");
        this.scFragmentPagerAdapter = Scene.v().getSootClassUnsafe("android.support.v4.app.FragmentPagerAdapter");
        this.scAndroidXFragmentStatePagerAdapter = Scene.v().getSootClassUnsafe("androidx.fragment.app.FragmentStatePagerAdapter");
        this.scAndroidXFragmentPagerAdapter = Scene.v().getSootClassUnsafe("androidx.fragment.app.FragmentPagerAdapter");
        this.callbackMethods = new HashMultiMap();
        this.layoutClasses = new HashMultiMap();
        this.dynamicManifestComponents = new HashSet();
        this.fragmentClasses = new HashMultiMap();
        this.fragmentClassesRev = new HashMultiMap();
        this.fragmentIDs = new HashMap();
        this.callbackFilters = new ArrayList();
        this.excludedEntryPoints = new HashSet();
        this.valueProvider = new SimpleConstantValueProvider();
        this.arrayToContentTypes = CacheBuilder.newBuilder().build(new CacheLoader<SootField, List<Type>>() { // from class: soot.jimple.infoflow.android.callbacks.AbstractCallbackAnalyzer.1
            @Override // com.google.common.cache.CacheLoader
            public List<Type> load(SootField field) throws Exception {
                List<Type> typeList = new ArrayList<>();
                field.getDeclaringClass().getMethods().stream().filter(m -> {
                    return m.isConcrete();
                }).map(m2 -> {
                    return m2.retrieveActiveBody();
                }).forEach(b -> {
                    Set<Local> arrayLocals = new HashSet<>();
                    Iterator<Unit> it = b.getUnits().iterator();
                    while (it.hasNext()) {
                        Unit u = it.next();
                        if (u instanceof AssignStmt) {
                            AssignStmt assignStmt = (AssignStmt) u;
                            Value rop = assignStmt.getRightOp();
                            Value lop = assignStmt.getLeftOp();
                            if ((rop instanceof FieldRef) && ((FieldRef) rop).getField() == field) {
                                arrayLocals.add((Local) lop);
                            } else if ((lop instanceof FieldRef) && ((FieldRef) lop).getField() == field) {
                                arrayLocals.add((Local) rop);
                            }
                        }
                    }
                    Iterator<Unit> it2 = b.getUnits().iterator();
                    while (it2.hasNext()) {
                        Unit u2 = it2.next();
                        if (u2 instanceof AssignStmt) {
                            AssignStmt assignStmt2 = (AssignStmt) u2;
                            Value rop2 = assignStmt2.getRightOp();
                            Value lop2 = assignStmt2.getLeftOp();
                            if (rop2 instanceof CastExpr) {
                                CastExpr ce = (CastExpr) rop2;
                                if (arrayLocals.contains(ce.getOp())) {
                                    arrayLocals.add((Local) lop2);
                                } else if (arrayLocals.contains(lop2)) {
                                    arrayLocals.add((Local) ce.getOp());
                                }
                            }
                        }
                    }
                    Iterator<Unit> it3 = b.getUnits().iterator();
                    while (it3.hasNext()) {
                        Unit u3 = it3.next();
                        if (u3 instanceof AssignStmt) {
                            AssignStmt assignStmt3 = (AssignStmt) u3;
                            Value rop3 = assignStmt3.getRightOp();
                            Value lop3 = assignStmt3.getLeftOp();
                            if (lop3 instanceof ArrayRef) {
                                ArrayRef arrayRef = (ArrayRef) lop3;
                                if (arrayLocals.contains(arrayRef.getBase())) {
                                    Type t = rop3.getType();
                                    if (t instanceof RefType) {
                                        typeList.add(rop3.getType());
                                    }
                                }
                            }
                        }
                    }
                });
                return typeList;
            }
        });
        this.config = config;
        this.entryPointClasses = entryPointClasses;
        this.androidCallbacks = androidCallbacks;
    }

    private static Set<String> loadAndroidCallbacks(String androidCallbackFile) throws IOException {
        Throwable th;
        Throwable th2;
        String fileName = androidCallbackFile;
        if (!new File(fileName).exists()) {
            fileName = "../soot-infoflow-android/AndroidCallbacks.txt";
            if (!new File(fileName).exists()) {
                th = null;
                try {
                    InputStream is = ResourceUtils.getResourceStream("/AndroidCallbacks.txt");
                    Set<String> loadAndroidCallbacks = loadAndroidCallbacks(new InputStreamReader(is));
                    if (is != null) {
                        is.close();
                    }
                    return loadAndroidCallbacks;
                } finally {
                }
            }
        }
        th = null;
        try {
            FileReader fr = new FileReader(fileName);
            Set<String> loadAndroidCallbacks2 = loadAndroidCallbacks(fr);
            if (fr != null) {
                fr.close();
            }
            return loadAndroidCallbacks2;
        } finally {
        }
    }

    public static Set<String> loadAndroidCallbacks(Reader reader) throws IOException {
        Set<String> androidCallbacks = new HashSet<>();
        Throwable th = null;
        try {
            BufferedReader bufReader = new BufferedReader(reader);
            while (true) {
                String line = bufReader.readLine();
                if (line == null) {
                    break;
                } else if (!line.isEmpty()) {
                    androidCallbacks.add(line);
                }
            }
            if (bufReader != null) {
                bufReader.close();
            }
            return androidCallbacks;
        } catch (Throwable th2) {
            if (0 == 0) {
                th = th2;
            } else if (null != th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    public void collectCallbackMethods() {
        for (ICallbackFilter filter : this.callbackFilters) {
            filter.reset();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void analyzeMethodForCallbackRegistrations(SootClass lifecycleElement, SootMethod method) {
        if (SystemClassHandler.v().isClassInSystemPackage(method.getDeclaringClass().getName()) || !method.isConcrete()) {
            return;
        }
        Set<SootClass> callbackClasses = new HashSet<>();
        Iterator<Unit> it = method.retrieveActiveBody().getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            Stmt stmt = (Stmt) u;
            if (stmt.containsInvokeExpr() && (stmt.getInvokeExpr() instanceof InstanceInvokeExpr)) {
                InstanceInvokeExpr iinv = (InstanceInvokeExpr) stmt.getInvokeExpr();
                SootMethodRef mref = iinv.getMethodRef();
                for (int i = 0; i < iinv.getArgCount(); i++) {
                    Type type = mref.getParameterType(i);
                    if (type instanceof RefType) {
                        String param = type.toString();
                        if (this.androidCallbacks.contains(param)) {
                            Value arg = iinv.getArg(i);
                            if (SystemClassHandler.v().isClassInSystemPackage(iinv.getMethod().getDeclaringClass().getName()) && (arg instanceof Local)) {
                                Set<Type> possibleTypes = Scene.v().getPointsToAnalysis().reachingObjects((Local) arg).possibleTypes();
                                if (possibleTypes.isEmpty()) {
                                    Type argType = ((Local) arg).getType();
                                    checkAndAddCallback(callbackClasses, argType);
                                } else {
                                    for (Type possibleType : possibleTypes) {
                                        checkAndAddCallback(callbackClasses, possibleType);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        for (SootClass callbackClass : callbackClasses) {
            analyzeClassInterfaceCallbacks(callbackClass, callbackClass, lifecycleElement);
        }
    }

    protected void checkAndAddCallback(Set<SootClass> callbackClasses, Type argType) {
        if (argType instanceof RefType) {
            RefType baseType = (RefType) argType;
            SootClass targetClass = baseType.getSootClass();
            if (!SystemClassHandler.v().isClassInSystemPackage(targetClass)) {
                callbackClasses.add(targetClass);
            }
        } else if (argType instanceof AnySubType) {
            RefType baseType2 = ((AnySubType) argType).getBase();
            SootClass baseClass = baseType2.getSootClass();
            for (SootClass sc : TypeUtils.getAllDerivedClasses(baseClass)) {
                if (!SystemClassHandler.v().isClassInSystemPackage(sc)) {
                    callbackClasses.add(sc);
                }
            }
        } else {
            this.logger.warn("Unsupported type detected in callback analysis");
        }
    }

    private boolean filterAccepts(SootClass lifecycleElement, SootClass targetClass) {
        for (ICallbackFilter filter : this.callbackFilters) {
            if (!filter.accepts(lifecycleElement, targetClass)) {
                return false;
            }
        }
        return true;
    }

    private boolean filterAccepts(SootClass lifecycleElement, SootMethod targetMethod) {
        for (ICallbackFilter filter : this.callbackFilters) {
            if (!filter.accepts(lifecycleElement, targetMethod)) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void analyzeMethodForDynamicBroadcastReceiver(SootMethod method) {
        if (SystemClassHandler.v().isClassInSystemPackage(method.getDeclaringClass().getName()) || !method.isConcrete() || !method.hasActiveBody()) {
            return;
        }
        FastHierarchy fastHierarchy = Scene.v().getFastHierarchy();
        RefType contextType = this.scContext.getType();
        Iterator<Unit> it = method.getActiveBody().getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            Stmt stmt = (Stmt) u;
            if (stmt.containsInvokeExpr()) {
                InvokeExpr iexpr = stmt.getInvokeExpr();
                SootMethodRef methodRef = iexpr.getMethodRef();
                if (methodRef.getName().equals("registerReceiver") && iexpr.getArgCount() > 0 && fastHierarchy.canStoreType(methodRef.getDeclaringClass().getType(), contextType)) {
                    Value br = iexpr.getArg(0);
                    if (br.getType() instanceof RefType) {
                        RefType rt = (RefType) br.getType();
                        if (!SystemClassHandler.v().isClassInSystemPackage(rt.getSootClass().getName())) {
                            this.dynamicManifestComponents.add(rt.getSootClass());
                        }
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void analyzeMethodForServiceConnection(SootMethod method) {
        if (SystemClassHandler.v().isClassInSystemPackage(method.getDeclaringClass().getName()) || !method.isConcrete() || !method.hasActiveBody()) {
            return;
        }
        Iterator<Unit> it = method.getActiveBody().getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            Stmt stmt = (Stmt) u;
            if (stmt.containsInvokeExpr()) {
                InvokeExpr iexpr = stmt.getInvokeExpr();
                SootMethodRef methodRef = iexpr.getMethodRef();
                if (methodRef.getSignature().equals(SIG_CAR_CREATE)) {
                    Value br = iexpr.getArg(1);
                    if ((br instanceof Local) && Scene.v().hasPointsToAnalysis()) {
                        PointsToSet pts = Scene.v().getPointsToAnalysis().reachingObjects((Local) br);
                        for (Type tp : pts.possibleTypes()) {
                            if (tp instanceof RefType) {
                                RefType rt = (RefType) tp;
                                if (!SystemClassHandler.v().isClassInSystemPackage(rt.getSootClass().getName())) {
                                    this.dynamicManifestComponents.add(rt.getSootClass());
                                }
                            }
                        }
                    }
                    if (br.getType() instanceof RefType) {
                        RefType rt2 = (RefType) br.getType();
                        if (!SystemClassHandler.v().isClassInSystemPackage(rt2.getSootClass().getName())) {
                            this.dynamicManifestComponents.add(rt2.getSootClass());
                        }
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void analyzeMethodForFragmentTransaction(SootClass lifecycleElement, SootMethod method) {
        if (((this.scFragment == null || this.scFragmentTransaction == null) && ((this.scSupportFragment == null || this.scSupportFragmentTransaction == null) && (this.scAndroidXFragment == null || this.scAndroidXFragmentTransaction == null))) || !method.isConcrete() || !method.hasActiveBody()) {
            return;
        }
        boolean isFragmentManager = false;
        boolean isFragmentTransaction = false;
        boolean isAddTransaction = false;
        Iterator<Unit> it = method.getActiveBody().getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            Stmt stmt = (Stmt) u;
            if (stmt.containsInvokeExpr()) {
                String methodName = stmt.getInvokeExpr().getMethod().getName();
                if (methodName.equals("getFragmentManager") || methodName.equals("getSupportFragmentManager")) {
                    isFragmentManager = true;
                } else if (methodName.equals("beginTransaction")) {
                    isFragmentTransaction = true;
                } else if (methodName.equals("add") || methodName.equals(MSVSSConstants.WRITABLE_REPLACE)) {
                    isAddTransaction = true;
                } else if (methodName.equals("inflate") && stmt.getInvokeExpr().getArgCount() > 1) {
                    Value arg = stmt.getInvokeExpr().getArg(0);
                    Integer fragmentID = (Integer) this.valueProvider.getValue(method, stmt, arg, Integer.class);
                    if (fragmentID != null) {
                        this.fragmentIDs.put(lifecycleElement, fragmentID);
                    }
                }
            }
        }
        if (isFragmentManager && isFragmentTransaction && isAddTransaction) {
            Iterator<Unit> it2 = method.getActiveBody().getUnits().iterator();
            while (it2.hasNext()) {
                Unit u2 = it2.next();
                Stmt stmt2 = (Stmt) u2;
                if (stmt2.containsInvokeExpr()) {
                    InvokeExpr invExpr = stmt2.getInvokeExpr();
                    if (invExpr instanceof InstanceInvokeExpr) {
                        InstanceInvokeExpr iinvExpr = (InstanceInvokeExpr) invExpr;
                        boolean isFragmentTransaction2 = this.scFragmentTransaction != null && Scene.v().getFastHierarchy().canStoreType(iinvExpr.getBase().getType(), this.scFragmentTransaction.getType());
                        boolean isFragmentTransaction3 = isFragmentTransaction2 | (this.scSupportFragmentTransaction != null && Scene.v().getFastHierarchy().canStoreType(iinvExpr.getBase().getType(), this.scSupportFragmentTransaction.getType())) | (this.scAndroidXFragmentTransaction != null && Scene.v().getFastHierarchy().canStoreType(iinvExpr.getBase().getType(), this.scAndroidXFragmentTransaction.getType()));
                        boolean isAddTransaction2 = stmt2.getInvokeExpr().getMethod().getName().equals("add") || stmt2.getInvokeExpr().getMethod().getName().equals(MSVSSConstants.WRITABLE_REPLACE);
                        if (isFragmentTransaction3 && isAddTransaction2) {
                            for (int i = 0; i < stmt2.getInvokeExpr().getArgCount(); i++) {
                                Value br = stmt2.getInvokeExpr().getArg(i);
                                if (br.getType() instanceof RefType) {
                                    RefType rt = (RefType) br.getType();
                                    if (br instanceof ClassConstant) {
                                        rt = (RefType) ((ClassConstant) br).toSootType();
                                    }
                                    boolean addFragment = this.scFragment != null && Scene.v().getFastHierarchy().canStoreType(rt, this.scFragment.getType());
                                    if (addFragment | (this.scSupportFragment != null && Scene.v().getFastHierarchy().canStoreType(rt, this.scSupportFragment.getType())) | (this.scAndroidXFragment != null && Scene.v().getFastHierarchy().canStoreType(rt, this.scAndroidXFragment.getType()))) {
                                        checkAndAddFragment(method.getDeclaringClass(), rt.getSootClass());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void analyzeMethodForViewPagers(SootClass clazz, SootMethod method) {
        Body b;
        if (this.scSupportViewPager == null && this.scAndroidXViewPager == null) {
            return;
        }
        if ((this.scFragmentStatePagerAdapter == null && this.scAndroidXFragmentStatePagerAdapter == null && this.scFragmentPagerAdapter == null && this.scAndroidXFragmentPagerAdapter == null) || !method.isConcrete()) {
            return;
        }
        Body body = method.retrieveActiveBody();
        Iterator<Unit> it = body.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            Stmt stmt = (Stmt) u;
            if (stmt.containsInvokeExpr()) {
                InvokeExpr invExpr = stmt.getInvokeExpr();
                if (invExpr instanceof InstanceInvokeExpr) {
                    InstanceInvokeExpr iinvExpr = (InstanceInvokeExpr) invExpr;
                    if (safeIsType(iinvExpr.getBase(), this.scSupportViewPager) || safeIsType(iinvExpr.getBase(), this.scAndroidXViewPager)) {
                        if (stmt.getInvokeExpr().getMethod().getName().equals("setAdapter") && stmt.getInvokeExpr().getArgCount() == 1) {
                            Value pa = stmt.getInvokeExpr().getArg(0);
                            if (pa.getType() instanceof RefType) {
                                RefType rt = (RefType) pa.getType();
                                if (safeIsType(pa, this.scFragmentStatePagerAdapter) || safeIsType(pa, this.scAndroidXFragmentStatePagerAdapter) || safeIsType(pa, this.scFragmentPagerAdapter) || safeIsType(pa, this.scAndroidXFragmentPagerAdapter)) {
                                    SootMethod getItem = rt.getSootClass().getMethodUnsafe("android.support.v4.app.Fragment getItem(int)");
                                    if (getItem == null) {
                                        getItem = rt.getSootClass().getMethodUnsafe("androidx.fragment.app.Fragment getItem(int)");
                                    }
                                    if (getItem != null && getItem.isConcrete() && (b = getItem.retrieveActiveBody()) != null) {
                                        Iterator<Unit> it2 = b.getUnits().iterator();
                                        while (it2.hasNext()) {
                                            Unit getItemUnit = it2.next();
                                            if (getItemUnit instanceof ReturnStmt) {
                                                ReturnStmt rs = (ReturnStmt) getItemUnit;
                                                Value rv = rs.getOp();
                                                Type type = rv.getType();
                                                if (type instanceof RefType) {
                                                    SootClass rtClass = ((RefType) type).getSootClass();
                                                    if ((rv instanceof Local) && (rtClass.getName().startsWith("android.") || rtClass.getName().startsWith("androidx."))) {
                                                        analyzeFragmentCandidates(rs, getItem, (Local) rv);
                                                    } else {
                                                        checkAndAddFragment(method.getDeclaringClass(), rtClass);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void analyzeFragmentCandidates(Stmt s, SootMethod m, Local l) {
        ExceptionalUnitGraph g = ExceptionalUnitGraphFactory.createExceptionalUnitGraph(m.getActiveBody());
        SimpleLocalDefs lds = new SimpleLocalDefs(g);
        List<Pair<Local, Stmt>> toSearch = new ArrayList<>();
        Set<Pair<Local, Stmt>> doneSet = new HashSet<>();
        toSearch.add(new Pair<>(l, s));
        while (!toSearch.isEmpty()) {
            Pair<Local, Stmt> pair = toSearch.remove(0);
            if (doneSet.add(pair)) {
                List<Unit> defs = lds.getDefsOfAt(pair.getO1(), pair.getO2());
                for (Unit def : defs) {
                    if (def instanceof AssignStmt) {
                        AssignStmt assignStmt = (AssignStmt) def;
                        Value rop = assignStmt.getRightOp();
                        if (rop instanceof ArrayRef) {
                            ArrayRef arrayRef = (ArrayRef) rop;
                            toSearch.add(new Pair<>((Local) arrayRef.getBase(), assignStmt));
                        } else if (rop instanceof FieldRef) {
                            FieldRef fieldRef = (FieldRef) rop;
                            try {
                                List<Type> typeList = this.arrayToContentTypes.get(fieldRef.getField());
                                typeList.stream().map(t -> {
                                    return ((RefType) t).getSootClass();
                                }).forEach(c -> {
                                    checkAndAddFragment(r5.getDeclaringClass(), m);
                                });
                            } catch (ExecutionException e) {
                                this.logger.error(String.format("Could not load potential types for field %s", fieldRef.getField().getSignature()), (Throwable) e);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean safeIsType(Value val, SootClass clazz) {
        return clazz != null && Scene.v().getFastHierarchy().canStoreType(val.getType(), clazz.getType());
    }

    protected boolean isInheritedMethod(Stmt stmt, String... classNames) {
        if (!stmt.containsInvokeExpr()) {
            return false;
        }
        SootMethod tgt = stmt.getInvokeExpr().getMethod();
        for (String className : classNames) {
            if (className.equals(tgt.getDeclaringClass().getName())) {
                return true;
            }
        }
        if (Scene.v().hasCallGraph()) {
            Iterator<Edge> edgeIt = Scene.v().getCallGraph().edgesOutOf(stmt);
            while (edgeIt.hasNext()) {
                Edge edge = edgeIt.next();
                String targetClass = edge.getTgt().method().getDeclaringClass().getName();
                for (String className2 : classNames) {
                    if (className2.equals(targetClass)) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean invokesSetContentView(InvokeExpr inv) {
        String methodName = SootMethodRepresentationParser.v().getMethodNameFromSubSignature(inv.getMethodRef().getSubSignature().getString());
        if (!methodName.equals("setContentView")) {
            return false;
        }
        SootClass declaringClass = inv.getMethod().getDeclaringClass();
        while (true) {
            SootClass curClass = declaringClass;
            if (curClass != null) {
                String curClassName = curClass.getName();
                if (curClassName.equals(AndroidEntryPointConstants.ACTIVITYCLASS) || curClassName.equals("android.support.v7.app.ActionBarActivity") || curClassName.equals(AndroidEntryPointConstants.APPCOMPATACTIVITYCLASS_V7) || curClassName.equals(AndroidEntryPointConstants.APPCOMPATACTIVITYCLASS_X)) {
                    return true;
                }
                declaringClass = curClass.hasSuperclass() ? curClass.getSuperclass() : null;
            } else {
                return false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean invokesInflate(InvokeExpr inv) {
        String methodName = SootMethodRepresentationParser.v().getMethodNameFromSubSignature(inv.getMethodRef().getSubSignature().getString());
        if (!methodName.equals("inflate")) {
            return false;
        }
        SootClass declaringClass = inv.getMethod().getDeclaringClass();
        while (true) {
            SootClass curClass = declaringClass;
            if (curClass != null) {
                String curClassName = curClass.getName();
                if (curClassName.equals(AndroidEntryPointConstants.FRAGMENTCLASS) || curClassName.equals("android.view.LayoutInflater")) {
                    return true;
                }
                declaringClass = curClass.hasSuperclass() ? curClass.getSuperclass() : null;
            } else {
                return false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void analyzeMethodOverrideCallbacks(SootClass sootClass) {
        SootMethod parentMethod;
        if (!sootClass.isConcrete() || sootClass.isInterface()) {
            return;
        }
        if (this.config.getIgnoreFlowsInSystemPackages() && SystemClassHandler.v().isClassInSystemPackage(sootClass.getName())) {
            return;
        }
        Map<String, SootMethod> systemMethods = new HashMap<>(10000);
        for (SootClass parentClass : Scene.v().getActiveHierarchy().getSuperclassesOf(sootClass)) {
            if (SystemClassHandler.v().isClassInSystemPackage(parentClass.getName())) {
                for (SootMethod sm : parentClass.getMethods()) {
                    if (!sm.isConstructor()) {
                        systemMethods.put(sm.getSubSignature(), sm);
                    }
                }
            }
        }
        for (SootClass parentClass2 : Scene.v().getActiveHierarchy().getSuperclassesOfIncluding(sootClass)) {
            if (!SystemClassHandler.v().isClassInSystemPackage(parentClass2.getName())) {
                for (SootMethod method : parentClass2.getMethods()) {
                    if (!method.hasTag(SimulatedCodeElementTag.TAG_NAME) && (parentMethod = systemMethods.get(method.getSubSignature())) != null && checkAndAddMethod(method, parentMethod, sootClass, AndroidCallbackDefinition.CallbackType.Default)) {
                        systemMethods.remove(parentMethod.getSubSignature());
                    }
                }
            }
        }
    }

    private SootMethod getMethodFromHierarchyEx(SootClass c, String methodSignature) {
        SootMethod m = c.getMethodUnsafe(methodSignature);
        if (m != null) {
            return m;
        }
        SootClass superClass = c.getSuperclassUnsafe();
        if (superClass != null) {
            return getMethodFromHierarchyEx(superClass, methodSignature);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void analyzeClassInterfaceCallbacks(SootClass baseClass, SootClass sootClass, SootClass lifecycleElement) {
        if (!baseClass.isConcrete() || SystemClassHandler.v().isClassInSystemPackage(baseClass.getName()) || SystemClassHandler.v().isClassInSystemPackage(sootClass.getName()) || !filterAccepts(lifecycleElement, baseClass) || !filterAccepts(lifecycleElement, sootClass)) {
            return;
        }
        SootClass superClass = sootClass.getSuperclassUnsafe();
        if (superClass != null) {
            analyzeClassInterfaceCallbacks(baseClass, superClass, lifecycleElement);
        }
        for (SootClass i : collectAllInterfaces(sootClass)) {
            checkAndAddCallback(i, baseClass, lifecycleElement);
        }
        for (SootClass c : collectAllSuperClasses(sootClass)) {
            checkAndAddCallback(c, baseClass, lifecycleElement);
        }
    }

    private void checkAndAddCallback(SootClass sc, SootClass baseClass, SootClass lifecycleElement) {
        if (this.androidCallbacks.contains(sc.getName())) {
            AndroidCallbackDefinition.CallbackType callbackType = isUICallback(sc) ? AndroidCallbackDefinition.CallbackType.Widget : AndroidCallbackDefinition.CallbackType.Default;
            for (SootMethod sm : sc.getMethods()) {
                SootMethod callbackImplementation = getMethodFromHierarchyEx(baseClass, sm.getSubSignature());
                if (callbackImplementation != null) {
                    checkAndAddMethod(callbackImplementation, sm, lifecycleElement, callbackType);
                }
            }
        }
    }

    private boolean isUICallback(SootClass i) {
        return i.getName().startsWith("android.widget") || i.getName().startsWith("android.view") || i.getName().startsWith("android.content.DialogInterface$");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean checkAndAddMethod(SootMethod method, SootMethod parentMethod, SootClass lifecycleClass, AndroidCallbackDefinition.CallbackType callbackType) {
        if (SystemClassHandler.v().isClassInSystemPackage(method.getDeclaringClass().getName())) {
            return false;
        }
        if ((method.isConcrete() && isEmpty(method.retrieveActiveBody())) || method.isConstructor() || method.isStaticInitializer() || !filterAccepts(lifecycleClass, method.getDeclaringClass()) || !filterAccepts(lifecycleClass, method)) {
            return false;
        }
        return this.callbackMethods.put(lifecycleClass, new AndroidCallbackDefinition(method, parentMethod, callbackType));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void checkAndAddFragment(SootClass componentClass, SootClass fragmentClass) {
        this.fragmentClasses.put(componentClass, fragmentClass);
        this.fragmentClassesRev.put(fragmentClass, componentClass);
    }

    private boolean isEmpty(Body activeBody) {
        Iterator<Unit> it = activeBody.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if (!(u instanceof IdentityStmt) && !(u instanceof ReturnVoidStmt)) {
                return false;
            }
        }
        return true;
    }

    private Set<SootClass> collectAllInterfaces(SootClass sootClass) {
        Set<SootClass> interfaces = new HashSet<>(sootClass.getInterfaces());
        for (SootClass i : sootClass.getInterfaces()) {
            interfaces.addAll(collectAllInterfaces(i));
        }
        return interfaces;
    }

    private Set<SootClass> collectAllSuperClasses(SootClass sootClass) {
        Set<SootClass> classes = new HashSet<>();
        if (sootClass.hasSuperclass()) {
            classes.add(sootClass.getSuperclass());
            classes.addAll(collectAllSuperClasses(sootClass.getSuperclass()));
        }
        return classes;
    }

    public MultiMap<SootClass, AndroidCallbackDefinition> getCallbackMethods() {
        return this.callbackMethods;
    }

    public MultiMap<SootClass, Integer> getLayoutClasses() {
        return this.layoutClasses;
    }

    public MultiMap<SootClass, SootClass> getFragmentClasses() {
        return this.fragmentClasses;
    }

    public Set<SootClass> getDynamicManifestComponents() {
        return this.dynamicManifestComponents;
    }

    public void addCallbackFilter(ICallbackFilter filter) {
        this.callbackFilters.add(filter);
    }

    public void excludeEntryPoint(SootClass entryPoint) {
        this.excludedEntryPoints.add(entryPoint);
    }

    public boolean isExcludedEntryPoint(SootClass entryPoint) {
        return this.excludedEntryPoints.contains(entryPoint);
    }

    public void setValueProvider(IValueProvider valueProvider) {
        this.valueProvider = valueProvider;
    }
}
