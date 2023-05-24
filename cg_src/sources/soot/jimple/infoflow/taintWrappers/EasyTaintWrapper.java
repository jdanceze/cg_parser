package soot.jimple.infoflow.taintWrappers;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import heros.TwoElementSet;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.cli.HelpFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Value;
import soot.jimple.Constant;
import soot.jimple.DefinitionStmt;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.data.AccessPath;
import soot.jimple.infoflow.problems.BackwardsInfoflowProblem;
import soot.jimple.infoflow.util.ResourceUtils;
import soot.jimple.infoflow.util.SootMethodRepresentationParser;
import soot.jimple.infoflow.util.SystemClassHandler;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/taintWrappers/EasyTaintWrapper.class */
public class EasyTaintWrapper extends AbstractTaintWrapper implements IReversibleTaintWrapper, Cloneable {
    private final Logger logger;
    private final Map<String, Set<String>> classList;
    private final Map<String, Set<String>> excludeList;
    private final Map<String, Set<String>> killList;
    private final Set<String> includeList;
    private LoadingCache<SootMethod, MethodWrapType> methodWrapCache;
    private boolean aggressiveMode;
    private boolean alwaysModelEqualsHashCode;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/taintWrappers/EasyTaintWrapper$MethodWrapType.class */
    public enum MethodWrapType {
        CreateTaint,
        KillTaint,
        Exclude,
        NotRegistered;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static MethodWrapType[] valuesCustom() {
            MethodWrapType[] valuesCustom = values();
            int length = valuesCustom.length;
            MethodWrapType[] methodWrapTypeArr = new MethodWrapType[length];
            System.arraycopy(valuesCustom, 0, methodWrapTypeArr, 0, length);
            return methodWrapTypeArr;
        }
    }

    static {
        $assertionsDisabled = !EasyTaintWrapper.class.desiredAssertionStatus();
    }

    public EasyTaintWrapper(Map<String, Set<String>> classList) {
        this(classList, new HashMap(), new HashMap(), new HashSet());
    }

    public EasyTaintWrapper(Map<String, Set<String>> classList, Map<String, Set<String>> excludeList) {
        this(classList, excludeList, new HashMap(), new HashSet());
    }

    public EasyTaintWrapper(Map<String, Set<String>> classList, Map<String, Set<String>> excludeList, Map<String, Set<String>> killList) {
        this(classList, excludeList, killList, new HashSet());
    }

    public EasyTaintWrapper(Map<String, Set<String>> classList, Map<String, Set<String>> excludeList, Map<String, Set<String>> killList, Set<String> includeList) {
        this.logger = LoggerFactory.getLogger(getClass());
        this.methodWrapCache = CacheBuilder.newBuilder().build(new CacheLoader<SootMethod, MethodWrapType>() { // from class: soot.jimple.infoflow.taintWrappers.EasyTaintWrapper.1
            @Override // com.google.common.cache.CacheLoader
            public MethodWrapType load(SootMethod arg0) throws Exception {
                return EasyTaintWrapper.this.getMethodWrapType(arg0.getSubSignature(), arg0.getDeclaringClass());
            }
        });
        this.aggressiveMode = false;
        this.alwaysModelEqualsHashCode = true;
        this.classList = classList;
        this.excludeList = excludeList;
        this.killList = killList;
        this.includeList = includeList;
    }

    public static EasyTaintWrapper getDefault() throws IOException {
        Throwable th = null;
        try {
            InputStream is = ResourceUtils.getResourceStream("/EasyTaintWrapperSource.txt");
            EasyTaintWrapper easyTaintWrapper = new EasyTaintWrapper(is);
            if (is != null) {
                is.close();
            }
            return easyTaintWrapper;
        } catch (Throwable th2) {
            if (0 == 0) {
                th = th2;
            } else if (null != th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    public EasyTaintWrapper(String f) throws IOException {
        this(new FileReader(new File(f).getAbsoluteFile()));
    }

    public EasyTaintWrapper(InputStream stream) throws IOException {
        this(new InputStreamReader(stream));
    }

    public EasyTaintWrapper(Reader reader) throws IOException {
        this.logger = LoggerFactory.getLogger(getClass());
        this.methodWrapCache = CacheBuilder.newBuilder().build(new CacheLoader<SootMethod, MethodWrapType>() { // from class: soot.jimple.infoflow.taintWrappers.EasyTaintWrapper.1
            @Override // com.google.common.cache.CacheLoader
            public MethodWrapType load(SootMethod arg0) throws Exception {
                return EasyTaintWrapper.this.getMethodWrapType(arg0.getSubSignature(), arg0.getDeclaringClass());
            }
        });
        this.aggressiveMode = false;
        this.alwaysModelEqualsHashCode = true;
        BufferedReader bufReader = new BufferedReader(reader);
        try {
            List<String> methodList = new LinkedList<>();
            List<String> excludeList = new LinkedList<>();
            List<String> killList = new LinkedList<>();
            this.includeList = new HashSet();
            for (String line = bufReader.readLine(); line != null; line = bufReader.readLine()) {
                if (!line.isEmpty() && !line.startsWith("#")) {
                    if (line.startsWith("~")) {
                        excludeList.add(line.substring(1));
                    } else if (line.startsWith(HelpFormatter.DEFAULT_OPT_PREFIX)) {
                        killList.add(line.substring(1));
                    } else if (line.startsWith("^")) {
                        this.includeList.add(line.substring(1));
                    } else {
                        methodList.add(line);
                    }
                }
            }
            this.classList = SootMethodRepresentationParser.v().parseClassNames(methodList, true);
            this.excludeList = SootMethodRepresentationParser.v().parseClassNames(excludeList, true);
            this.killList = SootMethodRepresentationParser.v().parseClassNames(killList, true);
            this.logger.info("Loaded wrapper entries for {} classes and {} exclusions.", Integer.valueOf(this.classList.size()), Integer.valueOf(excludeList.size()));
        } finally {
            bufReader.close();
        }
    }

    public EasyTaintWrapper(File f) throws IOException {
        this(new FileReader(f));
    }

    public EasyTaintWrapper(EasyTaintWrapper taintWrapper) {
        this(taintWrapper.classList, taintWrapper.excludeList, taintWrapper.killList, taintWrapper.includeList);
    }

    @Override // soot.jimple.infoflow.taintWrappers.AbstractTaintWrapper
    public Set<AccessPath> getTaintsForMethodInternal(Stmt stmt, AccessPath taintedPath) {
        if (!stmt.containsInvokeExpr()) {
            return Collections.singleton(taintedPath);
        }
        SootMethod method = stmt.getInvokeExpr().getMethod();
        String subSig = stmt.getInvokeExpr().getMethodRef().getSubSignature().getString();
        boolean taintEqualsHashCode = this.alwaysModelEqualsHashCode && (subSig.equals("boolean equals(java.lang.Object)") || subSig.equals("int hashCode()"));
        if (!taintedPath.isEmpty() && method.getDeclaringClass().getName().equals("java.lang.String") && subSig.equals("void getChars(int,int,char[],int)")) {
            return handleStringGetChars(stmt.getInvokeExpr(), taintedPath);
        }
        boolean isSupported = this.includeList == null || this.includeList.isEmpty();
        if (!isSupported) {
            Iterator<String> it = this.includeList.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                String supportedClass = it.next();
                if (method.getDeclaringClass().getName().startsWith(supportedClass)) {
                    isSupported = true;
                    break;
                }
            }
        }
        if (!isSupported && !this.aggressiveMode && !taintEqualsHashCode) {
            return null;
        }
        if (taintedPath.isStaticFieldRef()) {
            return Collections.singleton(taintedPath);
        }
        Set<AccessPath> taints = new HashSet<>();
        MethodWrapType wrapType = this.methodWrapCache.getUnchecked(method);
        if (stmt.getInvokeExpr() instanceof InstanceInvokeExpr) {
            InstanceInvokeExpr iiExpr = (InstanceInvokeExpr) stmt.getInvokeExpr();
            if (taintedPath.isEmpty() || iiExpr.getBase().equals(taintedPath.getPlainValue())) {
                if (wrapType == MethodWrapType.KillTaint) {
                    return null;
                }
                if (stmt instanceof DefinitionStmt) {
                    DefinitionStmt def = (DefinitionStmt) stmt;
                    if (wrapType != MethodWrapType.Exclude && SystemClassHandler.v().isTaintVisible(taintedPath, method)) {
                        taints.add(this.manager.getAccessPathFactory().createAccessPath(def.getLeftOp(), true));
                    }
                }
            }
        }
        taints.add(taintedPath);
        if (isSupported && wrapType == MethodWrapType.CreateTaint) {
            boolean doTaint = taintedPath.isEmpty();
            if (!doTaint) {
                Iterator<Value> it2 = stmt.getInvokeExpr().getArgs().iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    Value param = it2.next();
                    if (param.equals(taintedPath.getPlainValue()) && !taintEqualsHashCode) {
                        doTaint = true;
                        break;
                    }
                }
            }
            if (doTaint) {
                if (stmt instanceof DefinitionStmt) {
                    taints.add(this.manager.getAccessPathFactory().createAccessPath(((DefinitionStmt) stmt).getLeftOp(), true));
                }
                if (stmt.getInvokeExprBox().getValue() instanceof InstanceInvokeExpr) {
                    taints.add(this.manager.getAccessPathFactory().createAccessPath(((InstanceInvokeExpr) stmt.getInvokeExprBox().getValue()).getBase(), true));
                }
            }
        }
        return taints;
    }

    @Override // soot.jimple.infoflow.taintWrappers.IReversibleTaintWrapper
    public Set<Abstraction> getInverseTaintsForMethod(Stmt stmt, Abstraction d1, Abstraction taintedPath) {
        Set<AccessPath> aps = getInverseTaintsForMethodInternal(stmt, taintedPath);
        if (aps == null || aps.isEmpty()) {
            return null;
        }
        Set<Abstraction> res = new HashSet<>(aps.size());
        for (AccessPath ap : aps) {
            if (ap == taintedPath.getAccessPath()) {
                res.add(taintedPath);
            } else if (ap != null) {
                res.add(taintedPath.deriveNewAbstraction(ap, stmt));
            }
        }
        return res;
    }

    public Set<AccessPath> getInverseTaintsForMethodInternal(Stmt stmt, Abstraction taintedAbs) {
        AccessPath taintedPath = taintedAbs.getAccessPath();
        if (!stmt.containsInvokeExpr()) {
            return null;
        }
        Set<AccessPath> taints = new HashSet<>();
        SootMethod method = stmt.getInvokeExpr().getMethod();
        if ((method.isPhantom() || !method.hasActiveBody()) && (taintedPath.isStaticFieldRef() || !(stmt instanceof DefinitionStmt) || ((DefinitionStmt) stmt).getLeftOp() != taintedPath.getPlainValue())) {
            taints.add(taintedPath);
        }
        if (taintedPath.isStaticFieldRef()) {
            return Collections.singleton(taintedPath);
        }
        String subSig = stmt.getInvokeExpr().getMethodRef().getSubSignature().getString();
        boolean taintEqualsHashCode = this.alwaysModelEqualsHashCode && (subSig.equals("boolean equals(java.lang.Object)") || subSig.equals("int hashCode()"));
        if (!taintedPath.isEmpty() && method.getDeclaringClass().getName().equals("java.lang.String") && subSig.equals("void getChars(int,int,char[],int)")) {
            return handleInverseStringGetChars(stmt.getInvokeExpr(), taintedPath);
        }
        boolean isSupported = this.includeList == null || this.includeList.isEmpty() || this.includeList.stream().anyMatch(clazz -> {
            return method.getDeclaringClass().getName().startsWith(clazz);
        });
        if (!isSupported && !this.aggressiveMode && !taintEqualsHashCode) {
            return taints;
        }
        MethodWrapType wrapType = this.methodWrapCache.getUnchecked(method);
        if (wrapType == MethodWrapType.Exclude) {
            return Collections.singleton(taintedPath);
        }
        boolean taintedObj = false;
        if (stmt.containsInvokeExpr() && (stmt.getInvokeExpr() instanceof InstanceInvokeExpr)) {
            InstanceInvokeExpr iiExpr = (InstanceInvokeExpr) stmt.getInvokeExpr();
            if (iiExpr.getBase().equals(taintedPath.getPlainValue())) {
                if (wrapType == MethodWrapType.KillTaint) {
                    return Collections.emptySet();
                }
                if (wrapType == MethodWrapType.CreateTaint && taintedAbs.getDominator() != null && taintedAbs.getDominator() != null) {
                    taints.add(AccessPath.getEmptyAccessPath());
                }
            }
            if (stmt instanceof DefinitionStmt) {
                DefinitionStmt def = (DefinitionStmt) stmt;
                if (def.getLeftOp().equals(taintedPath.getPlainValue())) {
                    taints.add(this.manager.getAccessPathFactory().createAccessPath(((InstanceInvokeExpr) stmt.getInvokeExprBox().getValue()).getBase(), true));
                    taintedObj = true;
                }
            }
        }
        if (!taintedObj) {
            taints.add(taintedPath);
        }
        if (isSupported && wrapType == MethodWrapType.CreateTaint) {
            boolean doTaint = taintedObj;
            if (!doTaint) {
                if (stmt.getInvokeExpr() instanceof InstanceInvokeExpr) {
                    InstanceInvokeExpr iiExpr2 = (InstanceInvokeExpr) stmt.getInvokeExpr();
                    doTaint = !taintEqualsHashCode && iiExpr2.getBase().equals(taintedPath.getPlainValue());
                } else if ((stmt.getInvokeExpr() instanceof StaticInvokeExpr) && (stmt instanceof DefinitionStmt)) {
                    doTaint = !taintEqualsHashCode && ((DefinitionStmt) stmt).getLeftOp().equals(taintedPath.getPlainValue());
                }
            }
            if (doTaint) {
                for (Value arg : stmt.getInvokeExpr().getArgs()) {
                    if (!(arg instanceof Constant)) {
                        taints.add(this.manager.getAccessPathFactory().createAccessPath(arg, true));
                    }
                }
            }
        }
        return taints;
    }

    private Set<AccessPath> handleStringGetChars(InvokeExpr invokeExpr, AccessPath taintedPath) {
        if (((InstanceInvokeExpr) invokeExpr).getBase() == taintedPath.getPlainValue()) {
            return new TwoElementSet(taintedPath, this.manager.getAccessPathFactory().createAccessPath(invokeExpr.getArg(2), true));
        }
        return Collections.singleton(taintedPath);
    }

    private Set<AccessPath> handleInverseStringGetChars(InvokeExpr invokeExpr, AccessPath taintedPath) {
        if (invokeExpr.getArg(2) == taintedPath.getPlainValue()) {
            return new TwoElementSet(taintedPath, this.manager.getAccessPathFactory().createAccessPath(((InstanceInvokeExpr) invokeExpr).getBase(), true));
        }
        return Collections.singleton(taintedPath);
    }

    private boolean hasWrappedMethodsForClass(SootClass parentClass, boolean newTaints, boolean killTaints, boolean excludeTaints) {
        if (newTaints && this.classList.containsKey(parentClass.getName())) {
            return true;
        }
        if (excludeTaints && this.excludeList.containsKey(parentClass.getName())) {
            return true;
        }
        if (killTaints && this.killList.containsKey(parentClass.getName())) {
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public MethodWrapType getMethodWrapType(String subSig, SootClass parentClass) {
        boolean isSupported = false;
        Iterator<String> it = this.includeList.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            String supportedClass = it.next();
            if (parentClass.getName().startsWith(supportedClass)) {
                isSupported = true;
                break;
            }
        }
        if (this.alwaysModelEqualsHashCode && (subSig.equals("boolean equals(java.lang.Object)") || subSig.equals("int hashCode()"))) {
            return MethodWrapType.CreateTaint;
        }
        if (!isSupported) {
            return MethodWrapType.NotRegistered;
        }
        if (parentClass.isInterface()) {
            return getInterfaceWrapType(subSig, parentClass);
        }
        List<SootClass> superclasses = Scene.v().getActiveHierarchy().getSuperclassesOfIncluding(parentClass);
        for (SootClass sclass : superclasses) {
            MethodWrapType wtClass = getMethodWrapTypeDirect(sclass.getName(), subSig);
            if (wtClass != MethodWrapType.NotRegistered) {
                return wtClass;
            }
            for (SootClass ifc : sclass.getInterfaces()) {
                MethodWrapType wtIface = getInterfaceWrapType(subSig, ifc);
                if (wtIface != MethodWrapType.NotRegistered) {
                    return wtIface;
                }
            }
        }
        return MethodWrapType.NotRegistered;
    }

    private MethodWrapType getMethodWrapTypeDirect(String className, String subSignature) {
        if (this.alwaysModelEqualsHashCode && (subSignature.equals("boolean equals(java.lang.Object)") || subSignature.equals("int hashCode()"))) {
            return MethodWrapType.CreateTaint;
        }
        Set<String> cEntries = this.classList.get(className);
        Set<String> eEntries = this.excludeList.get(className);
        Set<String> kEntries = this.killList.get(className);
        if (cEntries != null && cEntries.contains(subSignature)) {
            return MethodWrapType.CreateTaint;
        }
        if (eEntries != null && eEntries.contains(subSignature)) {
            return MethodWrapType.Exclude;
        }
        if (kEntries != null && kEntries.contains(subSignature)) {
            return MethodWrapType.KillTaint;
        }
        return MethodWrapType.NotRegistered;
    }

    private MethodWrapType getInterfaceWrapType(String subSig, SootClass ifc) {
        if (ifc.isPhantom()) {
            return getMethodWrapTypeDirect(ifc.getName(), subSig);
        }
        if (!$assertionsDisabled && !ifc.isInterface()) {
            throw new AssertionError("Class " + ifc.getName() + " is not an interface, though returned by getInterfaces().");
        }
        for (SootClass pifc : Scene.v().getActiveHierarchy().getSuperinterfacesOfIncluding(ifc)) {
            MethodWrapType wt = getMethodWrapTypeDirect(pifc.getName(), subSig);
            if (wt != MethodWrapType.NotRegistered) {
                return wt;
            }
        }
        return MethodWrapType.NotRegistered;
    }

    @Override // soot.jimple.infoflow.taintWrappers.AbstractTaintWrapper
    public boolean isExclusiveInternal(Stmt stmt, AccessPath taintedPath) {
        SootMethod method = stmt.getInvokeExpr().getMethod();
        if (hasWrappedMethodsForClass(method.getDeclaringClass(), true, true, false)) {
            return true;
        }
        if (this.aggressiveMode && (stmt.getInvokeExpr() instanceof InstanceInvokeExpr)) {
            if (this.manager.getForwardSolver().getTabulationProblem() instanceof BackwardsInfoflowProblem) {
                if ((stmt instanceof DefinitionStmt) && ((DefinitionStmt) stmt).getLeftOp().equals(taintedPath.getPlainValue())) {
                    return true;
                }
            } else {
                InstanceInvokeExpr iiExpr = (InstanceInvokeExpr) stmt.getInvokeExpr();
                if (iiExpr.getBase().equals(taintedPath.getPlainValue())) {
                    return true;
                }
            }
        }
        MethodWrapType wrapType = this.methodWrapCache.getUnchecked(method);
        return wrapType != MethodWrapType.NotRegistered;
    }

    public void setAggressiveMode(boolean aggressiveMode) {
        this.aggressiveMode = aggressiveMode;
    }

    public boolean getAggressiveMode() {
        return this.aggressiveMode;
    }

    public void setAlwaysModelEqualsHashCode(boolean alwaysModelEqualsHashCode) {
        this.alwaysModelEqualsHashCode = alwaysModelEqualsHashCode;
    }

    public boolean getAlwaysModelEqualsHashCode() {
        return this.alwaysModelEqualsHashCode;
    }

    public void addIncludePrefix(String prefix) {
        this.includeList.add(prefix);
    }

    public void addMethodForWrapping(String className, String subSignature) {
        Set<String> methods = this.classList.get(className);
        if (methods == null) {
            methods = new HashSet<>();
            this.classList.put(className, methods);
        }
        methods.add(subSignature);
    }

    /* renamed from: clone */
    public EasyTaintWrapper m2789clone() {
        return new EasyTaintWrapper(this);
    }

    @Override // soot.jimple.infoflow.taintWrappers.ITaintPropagationWrapper
    public boolean supportsCallee(SootMethod method) {
        if (this.aggressiveMode) {
            return true;
        }
        String subSig = method.getSubSignature();
        if (this.alwaysModelEqualsHashCode && (subSig.equals("boolean equals(java.lang.Object)") || subSig.equals("int hashCode()"))) {
            return true;
        }
        for (String supportedClass : this.includeList) {
            if (method.getDeclaringClass().getName().startsWith(supportedClass)) {
                return true;
            }
        }
        return false;
    }

    @Override // soot.jimple.infoflow.taintWrappers.ITaintPropagationWrapper
    public boolean supportsCallee(Stmt callSite) {
        if (!callSite.containsInvokeExpr()) {
            return false;
        }
        SootMethod method = callSite.getInvokeExpr().getMethod();
        if (!supportsCallee(method)) {
            return false;
        }
        if (!this.aggressiveMode) {
            MethodWrapType wrapType = this.methodWrapCache.getUnchecked(method);
            if (wrapType != MethodWrapType.CreateTaint) {
                return false;
            }
        }
        if (callSite.getInvokeExpr() instanceof InstanceInvokeExpr) {
            return true;
        }
        for (Value val : callSite.getInvokeExpr().getArgs()) {
            if (!(val instanceof Constant)) {
                return true;
            }
        }
        return false;
    }

    @Override // soot.jimple.infoflow.taintWrappers.ITaintPropagationWrapper
    public Set<Abstraction> getAliasesForMethod(Stmt stmt, Abstraction d1, Abstraction taintedPath) {
        return null;
    }

    public static File locateDefaultDefinitionFile() {
        File twSourceFile = new File("../soot-infoflow/EasyTaintWrapperSource.txt");
        if (twSourceFile.exists()) {
            return twSourceFile;
        }
        File twSourceFile2 = new File("EasyTaintWrapperSource.txt");
        if (twSourceFile2.exists()) {
            return twSourceFile2;
        }
        return null;
    }
}
