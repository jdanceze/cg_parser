package soot.jimple.infoflow.android.source;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Local;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.FieldRef;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.NullConstant;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.android.InfoflowAndroidConfiguration;
import soot.jimple.infoflow.android.callbacks.AndroidCallbackDefinition;
import soot.jimple.infoflow.android.entryPointCreators.AndroidEntryPointConstants;
import soot.jimple.infoflow.android.entryPointCreators.AndroidEntryPointUtils;
import soot.jimple.infoflow.android.resources.ARSCFileParser;
import soot.jimple.infoflow.android.resources.controls.AndroidLayoutControl;
import soot.jimple.infoflow.callbacks.CallbackDefinition;
import soot.jimple.infoflow.data.AccessPath;
import soot.jimple.infoflow.solver.cfg.IInfoflowCFG;
import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition;
import soot.jimple.infoflow.sourcesSinks.manager.BaseSourceSinkManager;
import soot.jimple.infoflow.sourcesSinks.manager.IOneSourceAtATimeManager;
import soot.jimple.infoflow.sourcesSinks.manager.ISourceSinkManager;
import soot.jimple.toolkits.ide.icfg.BiDiInterproceduralCFG;
import soot.jimple.toolkits.scalar.ConstantPropagatorAndFolder;
import soot.tagkit.IntegerConstantValueTag;
import soot.tagkit.Tag;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/source/AndroidSourceSinkManager.class */
public class AndroidSourceSinkManager extends BaseSourceSinkManager implements ISourceSinkManager, IOneSourceAtATimeManager {
    private final Logger logger;
    protected static final String Activity_FindViewById = "<android.app.Activity: android.view.View findViewById(int)>";
    protected static final String View_FindViewById = "<android.view.View: android.view.View findViewById(int)>";
    protected SootMethod smActivityFindViewById;
    protected SootMethod smViewFindViewById;
    protected final Map<Integer, AndroidLayoutControl> layoutControls;
    protected List<ARSCFileParser.ResPackage> resourcePackages;
    protected String appPackageName;
    protected final Set<SootMethod> analyzedLayoutMethods;
    protected SootClass[] iccBaseClasses;
    protected AndroidEntryPointUtils entryPointUtils;

    public AndroidSourceSinkManager(Collection<? extends ISourceSinkDefinition> sources, Collection<? extends ISourceSinkDefinition> sinks, InfoflowAndroidConfiguration config) {
        this(sources, sinks, Collections.emptySet(), config, null);
    }

    public AndroidSourceSinkManager(Collection<? extends ISourceSinkDefinition> sources, Collection<? extends ISourceSinkDefinition> sinks, Set<AndroidCallbackDefinition> callbackMethods, InfoflowAndroidConfiguration config, Map<Integer, AndroidLayoutControl> layoutControls) {
        super(sources, sinks, callbackMethods, config);
        this.logger = LoggerFactory.getLogger(getClass());
        this.appPackageName = "";
        this.analyzedLayoutMethods = new HashSet();
        this.iccBaseClasses = null;
        this.entryPointUtils = new AndroidEntryPointUtils();
        this.layoutControls = layoutControls;
    }

    @Override // soot.jimple.infoflow.sourcesSinks.manager.BaseSourceSinkManager, soot.jimple.infoflow.sourcesSinks.manager.ISourceSinkManager
    public void initialize() {
        super.initialize();
        this.smActivityFindViewById = Scene.v().grabMethod(Activity_FindViewById);
        this.smViewFindViewById = Scene.v().grabMethod(View_FindViewById);
        if (this.iccBaseClasses == null) {
            this.iccBaseClasses = new SootClass[]{Scene.v().getSootClass("android.content.Context"), Scene.v().getSootClass("android.content.ContentResolver"), Scene.v().getSootClass(AndroidEntryPointConstants.ACTIVITYCLASS)};
        }
    }

    private ARSCFileParser.AbstractResource findResource(String resName, String resID, String packageName) {
        for (ARSCFileParser.ResPackage pkg : this.resourcePackages) {
            boolean matches = (packageName == null || packageName.isEmpty()) && pkg.getPackageName().equals(this.appPackageName);
            if (matches | pkg.getPackageName().equals(packageName)) {
                for (ARSCFileParser.ResType type : pkg.getDeclaredTypes()) {
                    if (type.getTypeName().equals(resID)) {
                        ARSCFileParser.AbstractResource res = type.getFirstResource(resName);
                        return res;
                    }
                }
                continue;
            }
        }
        return null;
    }

    private Integer findLastResIDAssignment(Stmt stmt, Local local, BiDiInterproceduralCFG<Unit, SootMethod> cfg, Set<Stmt> doneSet) {
        Integer lastAssignment;
        String packageName;
        if (!doneSet.add(stmt)) {
            return null;
        }
        if (stmt instanceof AssignStmt) {
            AssignStmt assign = (AssignStmt) stmt;
            if (assign.getLeftOp() == local) {
                if (assign.getRightOp() instanceof IntConstant) {
                    return Integer.valueOf(((IntConstant) assign.getRightOp()).value);
                }
                if (assign.getRightOp() instanceof FieldRef) {
                    SootField field = ((FieldRef) assign.getRightOp()).getField();
                    for (Tag tag : field.getTags()) {
                        if (tag instanceof IntegerConstantValueTag) {
                            return Integer.valueOf(((IntegerConstantValueTag) tag).getIntValue());
                        }
                        this.logger.error(String.format("Constant %s was of unexpected type", field.toString()));
                    }
                } else if (assign.getRightOp() instanceof InvokeExpr) {
                    InvokeExpr inv = (InvokeExpr) assign.getRightOp();
                    if (inv.getMethod().getName().equals("getIdentifier") && inv.getMethod().getDeclaringClass().getName().equals("android.content.res.Resources") && this.resourcePackages != null) {
                        if (inv.getArgCount() != 3) {
                            this.logger.error(String.format("Invalid parameter count (%d) for call to getIdentifier", Integer.valueOf(inv.getArgCount())));
                            return null;
                        }
                        String resName = "";
                        String resID = "";
                        if (inv.getArg(0) instanceof StringConstant) {
                            resName = ((StringConstant) inv.getArg(0)).value;
                        }
                        if (inv.getArg(1) instanceof StringConstant) {
                            resID = ((StringConstant) inv.getArg(1)).value;
                        }
                        Value thirdArg = inv.getArg(2);
                        if (thirdArg instanceof StringConstant) {
                            packageName = ((StringConstant) thirdArg).value;
                        } else if (thirdArg instanceof Local) {
                            packageName = findLastStringAssignment(stmt, (Local) thirdArg, cfg);
                        } else if (thirdArg instanceof NullConstant) {
                            return null;
                        } else {
                            this.logger.error(String.format("Unknown parameter type %s in call to getIdentifier", inv.getArg(2).getClass().getName()));
                            return null;
                        }
                        ARSCFileParser.AbstractResource res = findResource(resName, resID, packageName);
                        if (res != null) {
                            return Integer.valueOf(res.getResourceID());
                        }
                    }
                }
            }
        }
        for (Unit pred : cfg.getPredsOf(stmt)) {
            if ((pred instanceof Stmt) && (lastAssignment = findLastResIDAssignment((Stmt) pred, local, cfg, doneSet)) != null) {
                return lastAssignment;
            }
        }
        return null;
    }

    public void setResourcePackages(List<ARSCFileParser.ResPackage> resourcePackages) {
        this.resourcePackages = resourcePackages;
    }

    public void setAppPackageName(String appPackageName) {
        this.appPackageName = appPackageName;
    }

    private String findLastStringAssignment(Stmt stmt, Local local, BiDiInterproceduralCFG<Unit, SootMethod> cfg) {
        LinkedList<Stmt> workList = new LinkedList<>();
        Set<Stmt> seen = new HashSet<>();
        workList.add(stmt);
        while (!workList.isEmpty()) {
            Stmt stmt2 = workList.removeFirst();
            if (stmt2 instanceof AssignStmt) {
                AssignStmt assign = (AssignStmt) stmt2;
                if (assign.getLeftOp() == local && (assign.getRightOp() instanceof StringConstant)) {
                    return ((StringConstant) assign.getRightOp()).value;
                }
            }
            for (Unit pred : cfg.getPredsOf(stmt2)) {
                if (pred instanceof Stmt) {
                    Stmt s = (Stmt) pred;
                    if (seen.add(s)) {
                        workList.add(s);
                    }
                }
            }
        }
        return null;
    }

    protected AndroidLayoutControl getLayoutControl(Stmt sCallSite, IInfoflowCFG cfg) {
        if (this.layoutControls == null) {
            return null;
        }
        SootMethod uiMethod = cfg.getMethodOf(sCallSite);
        if (this.analyzedLayoutMethods.add(uiMethod)) {
            ConstantPropagatorAndFolder.v().transform(uiMethod.getActiveBody());
        }
        InvokeExpr iexpr = sCallSite.getInvokeExpr();
        if (iexpr.getArgCount() != 1) {
            this.logger.error("Framework method call with unexpected number of arguments");
            return null;
        }
        Integer id = (Integer) this.valueProvider.getValue(uiMethod, sCallSite, iexpr.getArg(0), Integer.class);
        if (id == null && (iexpr.getArg(0) instanceof Local)) {
            id = findLastResIDAssignment(sCallSite, (Local) iexpr.getArg(0), cfg, new HashSet(cfg.getMethodOf(sCallSite).getActiveBody().getUnits().size()));
        }
        if (id == null) {
            this.logger.debug("Could not find assignment to local " + ((Local) iexpr.getArg(0)).getName() + " in method " + cfg.getMethodOf(sCallSite).getSignature());
            return null;
        }
        AndroidLayoutControl control = this.layoutControls.get(id);
        if (control == null) {
            return null;
        }
        return control;
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x005f  */
    @Override // soot.jimple.infoflow.sourcesSinks.manager.BaseSourceSinkManager
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition getUISourceDefinition(soot.jimple.Stmt r5, soot.jimple.infoflow.solver.cfg.IInfoflowCFG r6) {
        /*
            Method dump skipped, instructions count: 268
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.jimple.infoflow.android.source.AndroidSourceSinkManager.getUISourceDefinition(soot.jimple.Stmt, soot.jimple.infoflow.solver.cfg.IInfoflowCFG):soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition");
    }

    @Override // soot.jimple.infoflow.sourcesSinks.manager.BaseSourceSinkManager
    protected boolean isEntryPointMethod(SootMethod method) {
        return this.entryPointUtils.isEntryPointMethod(method);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.jimple.infoflow.sourcesSinks.manager.BaseSourceSinkManager
    public ISourceSinkDefinition getSinkDefinition(Stmt sCallSite, InfoflowManager manager, AccessPath ap) {
        SootClass[] sootClassArr;
        SootMethod sm;
        ISourceSinkDefinition definition = super.getSinkDefinition(sCallSite, manager, ap);
        if (definition != null) {
            return definition;
        }
        if (sCallSite.containsInvokeExpr()) {
            SootMethod callee = sCallSite.getInvokeExpr().getMethod();
            String subSig = callee.getSubSignature();
            SootClass sc = callee.getDeclaringClass();
            boolean isParamTainted = false;
            if (ap != null && !sc.isInterface() && !ap.isStaticFieldRef()) {
                int i = 0;
                while (true) {
                    if (i < sCallSite.getInvokeExpr().getArgCount()) {
                        if (sCallSite.getInvokeExpr().getArg(i) != ap.getPlainValue()) {
                            i++;
                        } else {
                            isParamTainted = true;
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
            if (isParamTainted || ap == null) {
                for (SootClass clazz : this.iccBaseClasses) {
                    if (Scene.v().getOrMakeFastHierarchy().isSubclass(sc, clazz) && (sm = clazz.getMethodUnsafe(subSig)) != null) {
                        ISourceSinkDefinition def = this.sinkMethods.get(sm);
                        if (def != null) {
                            return def;
                        }
                        return null;
                    }
                }
                return null;
            }
            return null;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.jimple.infoflow.sourcesSinks.manager.BaseSourceSinkManager
    public ISourceSinkDefinition getInverseSink(Stmt sCallSite, IInfoflowCFG cfg) {
        SootClass[] sootClassArr;
        SootMethod sm;
        ISourceSinkDefinition definition = super.getInverseSink(sCallSite, cfg);
        if (definition != null) {
            return definition;
        }
        if (sCallSite.containsInvokeExpr()) {
            SootMethod callee = sCallSite.getInvokeExpr().getMethod();
            String subSig = callee.getSubSignature();
            SootClass sc = callee.getDeclaringClass();
            for (SootClass clazz : this.iccBaseClasses) {
                if (Scene.v().getOrMakeFastHierarchy().isSubclass(sc, clazz) && (sm = clazz.getMethodUnsafe(subSig)) != null) {
                    ISourceSinkDefinition def = this.sinkMethods.get(sm);
                    if (def != null) {
                        return def;
                    }
                    return null;
                }
            }
            return null;
        }
        return null;
    }

    @Override // soot.jimple.infoflow.sourcesSinks.manager.BaseSourceSinkManager
    protected CallbackDefinition getCallbackDefinition(SootMethod method) {
        CallbackDefinition def = super.getCallbackDefinition(method);
        if (def instanceof AndroidCallbackDefinition) {
            AndroidCallbackDefinition d = (AndroidCallbackDefinition) def;
            if (d.getCallbackType() == AndroidCallbackDefinition.CallbackType.Widget && this.sourceSinkConfig.getLayoutMatchingMode() != InfoflowConfiguration.LayoutMatchingMode.MatchAll) {
                return null;
            }
        }
        return def;
    }
}
