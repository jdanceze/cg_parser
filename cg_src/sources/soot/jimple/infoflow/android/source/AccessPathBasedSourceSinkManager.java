package soot.jimple.infoflow.android.source;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.Constant;
import soot.jimple.DefinitionStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.ParameterRef;
import soot.jimple.ReturnStmt;
import soot.jimple.Stmt;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.android.InfoflowAndroidConfiguration;
import soot.jimple.infoflow.android.callbacks.AndroidCallbackDefinition;
import soot.jimple.infoflow.android.resources.controls.AndroidLayoutControl;
import soot.jimple.infoflow.data.AccessPath;
import soot.jimple.infoflow.sourcesSinks.definitions.AccessPathTuple;
import soot.jimple.infoflow.sourcesSinks.definitions.FieldSourceSinkDefinition;
import soot.jimple.infoflow.sourcesSinks.definitions.IAccessPathBasedSourceSinkDefinition;
import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition;
import soot.jimple.infoflow.sourcesSinks.definitions.MethodSourceSinkDefinition;
import soot.jimple.infoflow.sourcesSinks.definitions.StatementSourceSinkDefinition;
import soot.jimple.infoflow.sourcesSinks.manager.SinkInfo;
import soot.jimple.infoflow.sourcesSinks.manager.SourceInfo;
import soot.jimple.infoflow.util.SystemClassHandler;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/source/AccessPathBasedSourceSinkManager.class */
public class AccessPathBasedSourceSinkManager extends AndroidSourceSinkManager {
    private static volatile /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$sourcesSinks$definitions$MethodSourceSinkDefinition$CallType;

    static /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$sourcesSinks$definitions$MethodSourceSinkDefinition$CallType() {
        int[] iArr = $SWITCH_TABLE$soot$jimple$infoflow$sourcesSinks$definitions$MethodSourceSinkDefinition$CallType;
        if (iArr != null) {
            return iArr;
        }
        int[] iArr2 = new int[MethodSourceSinkDefinition.CallType.valuesCustom().length];
        try {
            iArr2[MethodSourceSinkDefinition.CallType.Callback.ordinal()] = 2;
        } catch (NoSuchFieldError unused) {
        }
        try {
            iArr2[MethodSourceSinkDefinition.CallType.MethodCall.ordinal()] = 1;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            iArr2[MethodSourceSinkDefinition.CallType.Return.ordinal()] = 3;
        } catch (NoSuchFieldError unused3) {
        }
        $SWITCH_TABLE$soot$jimple$infoflow$sourcesSinks$definitions$MethodSourceSinkDefinition$CallType = iArr2;
        return iArr2;
    }

    public AccessPathBasedSourceSinkManager(Collection<? extends ISourceSinkDefinition> sources, Collection<? extends ISourceSinkDefinition> sinks, InfoflowAndroidConfiguration config) {
        super(sources, sinks, config);
    }

    public AccessPathBasedSourceSinkManager(Collection<? extends ISourceSinkDefinition> sources, Collection<? extends ISourceSinkDefinition> sinks, Set<AndroidCallbackDefinition> callbackMethods, InfoflowAndroidConfiguration config, Map<Integer, AndroidLayoutControl> layoutControls) {
        super(sources, sinks, callbackMethods, config, layoutControls);
    }

    @Override // soot.jimple.infoflow.sourcesSinks.manager.BaseSourceSinkManager
    protected SourceInfo createSourceInfo(Stmt sCallSite, InfoflowManager manager, ISourceSinkDefinition def) {
        if (def == null) {
            return null;
        }
        if (!(def instanceof IAccessPathBasedSourceSinkDefinition)) {
            return super.createSourceInfo(sCallSite, manager, def);
        }
        IAccessPathBasedSourceSinkDefinition apDef = (IAccessPathBasedSourceSinkDefinition) def;
        if (apDef.isEmpty()) {
            return super.createSourceInfo(sCallSite, manager, def);
        }
        Set<AccessPath> aps = new HashSet<>();
        Set<AccessPathTuple> apTuples = new HashSet<>();
        if (def instanceof MethodSourceSinkDefinition) {
            MethodSourceSinkDefinition methodDef = (MethodSourceSinkDefinition) def;
            switch ($SWITCH_TABLE$soot$jimple$infoflow$sourcesSinks$definitions$MethodSourceSinkDefinition$CallType()[methodDef.getCallType().ordinal()]) {
                case 1:
                    if ((sCallSite instanceof InvokeStmt) && (sCallSite.getInvokeExpr() instanceof InstanceInvokeExpr) && methodDef.getBaseObjects() != null) {
                        Value baseVal = ((InstanceInvokeExpr) sCallSite.getInvokeExpr()).getBase();
                        for (AccessPathTuple apt : methodDef.getBaseObjects()) {
                            if (apt.getSourceSinkType().isSource()) {
                                aps.add(apt.toAccessPath(baseVal, manager, true));
                                apTuples.add(apt);
                            }
                        }
                    }
                    if ((sCallSite instanceof DefinitionStmt) && methodDef.getReturnValues() != null) {
                        Value returnVal = ((DefinitionStmt) sCallSite).getLeftOp();
                        for (AccessPathTuple apt2 : methodDef.getReturnValues()) {
                            if (apt2.getSourceSinkType().isSource()) {
                                aps.add(apt2.toAccessPath(returnVal, manager, false));
                                apTuples.add(apt2);
                            }
                        }
                    }
                    if (sCallSite.containsInvokeExpr() && methodDef.getParameters() != null && methodDef.getParameters().length > 0) {
                        for (int i = 0; i < sCallSite.getInvokeExpr().getArgCount(); i++) {
                            if (methodDef.getParameters().length > i) {
                                for (AccessPathTuple apt3 : methodDef.getParameters()[i]) {
                                    if (apt3.getSourceSinkType().isSource()) {
                                        aps.add(apt3.toAccessPath(sCallSite.getInvokeExpr().getArg(i), manager, true));
                                        apTuples.add(apt3);
                                    }
                                }
                            }
                        }
                        break;
                    }
                    break;
                case 2:
                    if (sCallSite instanceof IdentityStmt) {
                        IdentityStmt is = (IdentityStmt) sCallSite;
                        if (is.getRightOp() instanceof ParameterRef) {
                            ParameterRef paramRef = (ParameterRef) is.getRightOp();
                            if (methodDef.getParameters() != null && methodDef.getParameters().length > paramRef.getIndex()) {
                                for (AccessPathTuple apt4 : methodDef.getParameters()[paramRef.getIndex()]) {
                                    aps.add(apt4.toAccessPath(is.getLeftOp(), manager, false));
                                    apTuples.add(apt4);
                                }
                                break;
                            }
                        }
                    }
                    break;
                default:
                    return null;
            }
        } else if (def instanceof FieldSourceSinkDefinition) {
            FieldSourceSinkDefinition fieldDef = (FieldSourceSinkDefinition) def;
            if ((sCallSite instanceof AssignStmt) && fieldDef.getAccessPaths() != null) {
                AssignStmt assignStmt = (AssignStmt) sCallSite;
                for (AccessPathTuple apt5 : fieldDef.getAccessPaths()) {
                    if (apt5.getSourceSinkType().isSource()) {
                        aps.add(apt5.toAccessPath(assignStmt.getLeftOp(), manager, false));
                        apTuples.add(apt5);
                    }
                }
            }
        } else if (def instanceof StatementSourceSinkDefinition) {
            StatementSourceSinkDefinition ssdef = (StatementSourceSinkDefinition) def;
            if (ssdef.getAccessPaths() != null) {
                for (AccessPathTuple apt6 : ssdef.getAccessPaths()) {
                    if (apt6.getSourceSinkType().isSource()) {
                        aps.add(apt6.toAccessPath(ssdef.getLocal(), manager, true));
                        apTuples.add(apt6);
                    }
                }
            }
        }
        if (aps.isEmpty()) {
            return null;
        }
        return new SourceInfo(apDef.filter(apTuples), aps);
    }

    @Override // soot.jimple.infoflow.sourcesSinks.manager.BaseSourceSinkManager, soot.jimple.infoflow.sourcesSinks.manager.ISourceSinkManager
    public SinkInfo getSinkInfo(Stmt sCallSite, InfoflowManager manager, AccessPath sourceAccessPath) {
        ISourceSinkDefinition def = getSinkDefinition(sCallSite, manager, sourceAccessPath);
        if (def == null) {
            return null;
        }
        if (!(def instanceof IAccessPathBasedSourceSinkDefinition)) {
            return super.getSinkInfo(sCallSite, manager, sourceAccessPath);
        }
        IAccessPathBasedSourceSinkDefinition apDef = (IAccessPathBasedSourceSinkDefinition) def;
        if (apDef.isEmpty() && sCallSite.containsInvokeExpr()) {
            if (SystemClassHandler.v().isTaintVisible(sourceAccessPath, sCallSite.getInvokeExpr().getMethod())) {
                return new SinkInfo(def);
            }
            return null;
        } else if (sourceAccessPath == null) {
            return new SinkInfo(def);
        } else {
            if (!(def instanceof MethodSourceSinkDefinition)) {
                if (!(def instanceof FieldSourceSinkDefinition)) {
                    if (def instanceof StatementSourceSinkDefinition) {
                        StatementSourceSinkDefinition ssdef = (StatementSourceSinkDefinition) def;
                        for (AccessPathTuple apt : ssdef.getAccessPaths()) {
                            if (apt.getSourceSinkType().isSink() && accessPathMatches(sourceAccessPath, apt)) {
                                return new SinkInfo(apDef.filter(Collections.singleton(apt)));
                            }
                        }
                        return null;
                    }
                    return null;
                }
                FieldSourceSinkDefinition fieldDef = (FieldSourceSinkDefinition) def;
                if ((sCallSite instanceof AssignStmt) && fieldDef.getAccessPaths() != null) {
                    for (AccessPathTuple apt2 : fieldDef.getAccessPaths()) {
                        if (apt2.getSourceSinkType().isSink() && accessPathMatches(sourceAccessPath, apt2)) {
                            return new SinkInfo(apDef.filter(Collections.singleton(apt2)));
                        }
                    }
                    return null;
                }
                return null;
            }
            MethodSourceSinkDefinition methodDef = (MethodSourceSinkDefinition) def;
            if (methodDef.getCallType() == MethodSourceSinkDefinition.CallType.Return) {
                return new SinkInfo(def);
            }
            InvokeExpr iexpr = sCallSite.getInvokeExpr();
            if ((iexpr instanceof InstanceInvokeExpr) && methodDef.getBaseObjects() != null) {
                InstanceInvokeExpr iiexpr = (InstanceInvokeExpr) iexpr;
                if (iiexpr.getBase() == sourceAccessPath.getPlainValue()) {
                    for (AccessPathTuple apt3 : methodDef.getBaseObjects()) {
                        if (apt3.getSourceSinkType().isSink() && accessPathMatches(sourceAccessPath, apt3)) {
                            return new SinkInfo(apDef.filter(Collections.singleton(apt3)));
                        }
                    }
                }
            }
            if (methodDef.getParameters() != null && methodDef.getParameters().length > 0) {
                for (int i = 0; i < sCallSite.getInvokeExpr().getArgCount(); i++) {
                    if (sCallSite.getInvokeExpr().getArg(i) == sourceAccessPath.getPlainValue() && methodDef.getParameters().length > i) {
                        for (AccessPathTuple apt4 : methodDef.getParameters()[i]) {
                            if (apt4.getSourceSinkType().isSink() && accessPathMatches(sourceAccessPath, apt4)) {
                                return new SinkInfo(apDef.filter(Collections.singleton(apt4)));
                            }
                        }
                        continue;
                    }
                }
                return null;
            }
            return null;
        }
    }

    private boolean accessPathMatches(AccessPath sourceAccessPath, AccessPathTuple apt) {
        if (apt.getFields() == null || apt.getFields().length == 0 || sourceAccessPath == null) {
            return true;
        }
        for (int i = 0; i < apt.getFields().length; i++) {
            if (i >= sourceAccessPath.getFragmentCount()) {
                return sourceAccessPath.getTaintSubFields();
            }
            if (!sourceAccessPath.getFragments()[i].getField().getName().equals(apt.getFields()[i])) {
                return false;
            }
        }
        return true;
    }

    @Override // soot.jimple.infoflow.sourcesSinks.manager.BaseSourceSinkManager, soot.jimple.infoflow.sourcesSinks.manager.IReversibleSourceSinkManager
    public SourceInfo getInverseSinkInfo(Stmt sCallSite, InfoflowManager manager) {
        ISourceSinkDefinition def = getInverseSink(sCallSite, manager.getICFG());
        if (def == null) {
            return null;
        }
        if (!(def instanceof IAccessPathBasedSourceSinkDefinition)) {
            return super.getInverseSinkInfo(sCallSite, manager);
        }
        IAccessPathBasedSourceSinkDefinition apDef = (IAccessPathBasedSourceSinkDefinition) def;
        if (apDef.isEmpty()) {
            return super.getInverseSinkInfo(sCallSite, manager);
        }
        if (def instanceof MethodSourceSinkDefinition) {
            MethodSourceSinkDefinition methodDef = (MethodSourceSinkDefinition) def;
            if (methodDef.getCallType() == MethodSourceSinkDefinition.CallType.Return) {
                Set<AccessPath> aps = new HashSet<>();
                for (SootMethod dest : manager.getICFG().getCalleesOfCallAt(sCallSite)) {
                    if (dest.hasActiveBody()) {
                        Iterator<Unit> it = dest.getActiveBody().getUnits().iterator();
                        while (it.hasNext()) {
                            Unit unit = it.next();
                            if (unit instanceof ReturnStmt) {
                                for (AccessPathTuple apt : methodDef.getReturnValues()) {
                                    if (apt.getSourceSinkType().isSink()) {
                                        aps.add(apt.toAccessPath(((ReturnStmt) unit).getOp(), manager, false));
                                    }
                                }
                            }
                        }
                    }
                }
                return new SourceInfo(def, aps);
            } else if ((sCallSite.getInvokeExpr() instanceof InstanceInvokeExpr) && methodDef.getBaseObjects() != null) {
                for (AccessPathTuple apt2 : methodDef.getBaseObjects()) {
                    if (apt2.getSourceSinkType().isSink()) {
                        AccessPath ap = apt2.toAccessPath(((InstanceInvokeExpr) sCallSite.getInvokeExpr()).getBase(), manager, true);
                        return new SourceInfo(def, ap);
                    }
                }
                return null;
            } else if (methodDef.getParameters() != null && methodDef.getParameters().length > 0) {
                Set<AccessPath> aps2 = new HashSet<>();
                for (int i = 0; i < sCallSite.getInvokeExpr().getArgCount(); i++) {
                    if (!(sCallSite.getInvokeExpr().getArg(i) instanceof Constant) && methodDef.getParameters().length > i) {
                        for (AccessPathTuple apt3 : methodDef.getParameters()[i]) {
                            if (apt3.getSourceSinkType().isSink()) {
                                AccessPath ap2 = apt3.toAccessPath(sCallSite.getInvokeExpr().getArg(i), manager, true);
                                aps2.add(ap2);
                            }
                        }
                    }
                }
                return new SourceInfo(def, aps2);
            } else {
                return null;
            }
        } else if (def instanceof FieldSourceSinkDefinition) {
            FieldSourceSinkDefinition fieldDef = (FieldSourceSinkDefinition) def;
            Set<AccessPath> aps3 = new HashSet<>();
            if ((sCallSite instanceof AssignStmt) && fieldDef.getAccessPaths() != null) {
                for (AccessPathTuple apt4 : fieldDef.getAccessPaths()) {
                    if (apt4.getSourceSinkType().isSink()) {
                        aps3.add(apt4.toAccessPath(sCallSite.getFieldRef(), manager, false));
                    }
                }
                return new SourceInfo(fieldDef, aps3);
            }
            return null;
        } else if (def instanceof StatementSourceSinkDefinition) {
            StatementSourceSinkDefinition ssdef = (StatementSourceSinkDefinition) def;
            Set<AccessPath> aps4 = new HashSet<>();
            Set<AccessPathTuple> apsTuple = new HashSet<>();
            Iterator<AccessPathTuple> it2 = ssdef.getAccessPaths().iterator();
            if (it2.hasNext()) {
                AccessPathTuple apt5 = it2.next();
                if (apt5.getSourceSinkType().isSink()) {
                    apsTuple.add(apt5);
                    aps4.add(apt5.toAccessPath(sCallSite.getFieldRef(), manager, true));
                }
                return new SourceInfo(apDef.filter(apsTuple), aps4);
            }
            return null;
        } else {
            return null;
        }
    }

    @Override // soot.jimple.infoflow.sourcesSinks.manager.BaseSourceSinkManager, soot.jimple.infoflow.sourcesSinks.manager.IReversibleSourceSinkManager
    public SinkInfo getInverseSourceInfo(Stmt sCallSite, InfoflowManager manager, AccessPath sourceAccessPath) {
        ISourceSinkDefinition def = getInverseSource(sCallSite, manager, sourceAccessPath);
        if (def == null) {
            return null;
        }
        if (!(def instanceof IAccessPathBasedSourceSinkDefinition)) {
            return super.getInverseSourceInfo(sCallSite, manager, sourceAccessPath);
        }
        IAccessPathBasedSourceSinkDefinition apDef = (IAccessPathBasedSourceSinkDefinition) def;
        if (apDef.isEmpty()) {
            return super.getInverseSourceInfo(sCallSite, manager, sourceAccessPath);
        }
        if (apDef.isEmpty() && sCallSite.containsInvokeExpr()) {
            if (SystemClassHandler.v().isTaintVisible(sourceAccessPath, sCallSite.getInvokeExpr().getMethod())) {
                return new SinkInfo(def);
            }
            return null;
        } else if (sourceAccessPath == null) {
            return new SinkInfo(def);
        } else {
            Set<AccessPath> aps = new HashSet<>();
            Set<AccessPathTuple> apTuples = new HashSet<>();
            if (def instanceof MethodSourceSinkDefinition) {
                MethodSourceSinkDefinition methodDef = (MethodSourceSinkDefinition) def;
                switch ($SWITCH_TABLE$soot$jimple$infoflow$sourcesSinks$definitions$MethodSourceSinkDefinition$CallType()[methodDef.getCallType().ordinal()]) {
                    case 1:
                        if ((sCallSite instanceof InvokeStmt) && (sCallSite.getInvokeExpr() instanceof InstanceInvokeExpr) && methodDef.getBaseObjects() != null) {
                            Value baseVal = ((InstanceInvokeExpr) sCallSite.getInvokeExpr()).getBase();
                            for (AccessPathTuple apt : methodDef.getBaseObjects()) {
                                if (apt.getSourceSinkType().isSource()) {
                                    AccessPath ap = apt.toAccessPath(baseVal, manager, true);
                                    if (accessPathMatches(sourceAccessPath, apt)) {
                                        aps.add(ap);
                                        apTuples.add(apt);
                                    }
                                }
                            }
                        }
                        if ((sCallSite instanceof DefinitionStmt) && methodDef.getReturnValues() != null) {
                            Value returnVal = ((DefinitionStmt) sCallSite).getLeftOp();
                            for (AccessPathTuple apt2 : methodDef.getReturnValues()) {
                                if (apt2.getSourceSinkType().isSource()) {
                                    AccessPath ap2 = apt2.toAccessPath(returnVal, manager, false);
                                    if (accessPathMatches(sourceAccessPath, apt2)) {
                                        aps.add(ap2);
                                        apTuples.add(apt2);
                                    }
                                }
                            }
                        }
                        if (sCallSite.containsInvokeExpr() && methodDef.getParameters() != null && methodDef.getParameters().length > 0) {
                            for (int i = 0; i < sCallSite.getInvokeExpr().getArgCount(); i++) {
                                if (methodDef.getParameters().length > i) {
                                    for (AccessPathTuple apt3 : methodDef.getParameters()[i]) {
                                        if (apt3.getSourceSinkType().isSource()) {
                                            AccessPath ap3 = apt3.toAccessPath(sCallSite.getInvokeExpr().getArg(i), manager, true);
                                            if (accessPathMatches(sourceAccessPath, apt3)) {
                                                aps.add(ap3);
                                                apTuples.add(apt3);
                                            }
                                        }
                                    }
                                }
                            }
                            break;
                        }
                        break;
                    case 2:
                        if (sCallSite instanceof IdentityStmt) {
                            IdentityStmt is = (IdentityStmt) sCallSite;
                            if (is.getRightOp() instanceof ParameterRef) {
                                ParameterRef paramRef = (ParameterRef) is.getRightOp();
                                if (methodDef.getParameters() != null && methodDef.getParameters().length > paramRef.getIndex()) {
                                    for (AccessPathTuple apt4 : methodDef.getParameters()[paramRef.getIndex()]) {
                                        AccessPath ap4 = apt4.toAccessPath(is.getLeftOp(), manager, false);
                                        if (accessPathMatches(sourceAccessPath, apt4)) {
                                            aps.add(ap4);
                                            apTuples.add(apt4);
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                        break;
                    default:
                        return null;
                }
            } else if (def instanceof FieldSourceSinkDefinition) {
                FieldSourceSinkDefinition fieldDef = (FieldSourceSinkDefinition) def;
                if ((sCallSite instanceof AssignStmt) && fieldDef.getAccessPaths() != null) {
                    AssignStmt assignStmt = (AssignStmt) sCallSite;
                    for (AccessPathTuple apt5 : fieldDef.getAccessPaths()) {
                        if (apt5.getSourceSinkType().isSource()) {
                            AccessPath ap5 = apt5.toAccessPath(assignStmt.getLeftOp(), manager, false);
                            if (accessPathMatches(sourceAccessPath, apt5)) {
                                aps.add(ap5);
                                apTuples.add(apt5);
                            }
                        }
                    }
                }
            } else if (def instanceof StatementSourceSinkDefinition) {
                StatementSourceSinkDefinition ssdef = (StatementSourceSinkDefinition) def;
                for (AccessPathTuple apt6 : ssdef.getAccessPaths()) {
                    if (apt6.getSourceSinkType().isSource()) {
                        AccessPath ap6 = apt6.toAccessPath(ssdef.getLocal(), manager, true);
                        if (accessPathMatches(sourceAccessPath, apt6)) {
                            aps.add(ap6);
                            apTuples.add(apt6);
                        }
                    }
                }
            }
            if (aps.isEmpty()) {
                return null;
            }
            return new SinkInfo(apDef.filter(apTuples));
        }
    }
}
