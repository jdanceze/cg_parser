package soot.jimple.spark.ondemand.pautil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.SootMethod;
import soot.jimple.InvokeExpr;
import soot.jimple.spark.ondemand.genericutil.ArraySet;
import soot.jimple.spark.ondemand.genericutil.ArraySetMultiMap;
import soot.jimple.spark.pag.GlobalVarNode;
import soot.jimple.spark.pag.LocalVarNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.pag.VarNode;
import soot.toolkits.scalar.Pair;
import soot.util.HashMultiMap;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/ondemand/pautil/ContextSensitiveInfo.class */
public class ContextSensitiveInfo {
    private static final Logger logger;
    private static final boolean SKIP_STRING_NODES = false;
    private static final boolean SKIP_EXCEPTION_NODES = false;
    private static final boolean SKIP_THREAD_GLOBALS = false;
    private static final boolean PRINT_CALL_SITE_INFO = false;
    private final ArraySetMultiMap<VarNode, AssignEdge> contextSensitiveAssignEdges = new ArraySetMultiMap<>();
    private final ArraySetMultiMap<VarNode, AssignEdge> contextSensitiveAssignBarEdges = new ArraySetMultiMap<>();
    private final ArraySetMultiMap<SootMethod, VarNode> methodToNodes = new ArraySetMultiMap<>();
    private final ArraySetMultiMap<SootMethod, VarNode> methodToOutPorts = new ArraySetMultiMap<>();
    private final ArraySetMultiMap<SootMethod, VarNode> methodToInPorts = new ArraySetMultiMap<>();
    private final ArraySetMultiMap<SootMethod, Integer> callSitesInMethod = new ArraySetMultiMap<>();
    private final ArraySetMultiMap<SootMethod, Integer> callSitesInvokingMethod = new ArraySetMultiMap<>();
    private final ArraySetMultiMap<Integer, SootMethod> callSiteToTargets = new ArraySetMultiMap<>();
    private final ArraySetMultiMap<Integer, AssignEdge> callSiteToEdges = new ArraySetMultiMap<>();
    private final Map<Integer, LocalVarNode> virtCallSiteToReceiver = new HashMap();
    private final Map<Integer, SootMethod> callSiteToInvokedMethod = new HashMap();
    private final Map<Integer, SootMethod> callSiteToInvokingMethod = new HashMap();
    private final ArraySetMultiMap<LocalVarNode, Integer> receiverToVirtCallSites = new ArraySetMultiMap<>();
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ContextSensitiveInfo.class.desiredAssertionStatus();
        logger = LoggerFactory.getLogger(ContextSensitiveInfo.class);
    }

    public ContextSensitiveInfo(PAG pag) {
        Iterator iter = pag.getVarNodeNumberer().iterator();
        while (iter.hasNext()) {
            VarNode varNode = iter.next();
            if (varNode instanceof LocalVarNode) {
                LocalVarNode local = (LocalVarNode) varNode;
                SootMethod method = local.getMethod();
                if (!$assertionsDisabled && method == null) {
                    throw new AssertionError(local);
                }
                this.methodToNodes.put(method, local);
                if (SootUtil.isRetNode(local)) {
                    this.methodToOutPorts.put(method, local);
                }
                if (SootUtil.isParamNode(local)) {
                    this.methodToInPorts.put(method, local);
                }
            }
        }
        int callSiteNum = 0;
        Set assignSources = pag.simpleSources();
        for (VarNode assignSource : assignSources) {
            if (!skipNode(assignSource)) {
                boolean sourceGlobal = assignSource instanceof GlobalVarNode;
                Node[] assignTargets = pag.simpleLookup(assignSource);
                for (Node node : assignTargets) {
                    VarNode assignTarget = (VarNode) node;
                    if (!skipNode(assignTarget)) {
                        boolean isFinalizerNode = false;
                        if (assignTarget instanceof LocalVarNode) {
                            LocalVarNode local2 = (LocalVarNode) assignTarget;
                            if (local2.getMethod().toString().indexOf("finalize()") != -1 && SootUtil.isThisNode(local2)) {
                                isFinalizerNode = true;
                            }
                        }
                        boolean targetGlobal = assignTarget instanceof GlobalVarNode;
                        AssignEdge assignEdge = new AssignEdge(assignSource, assignTarget);
                        if (isFinalizerNode) {
                            assignEdge.setParamEdge();
                            int i = callSiteNum;
                            callSiteNum++;
                            assignEdge.setCallSite(new Integer(i));
                        }
                        addAssignEdge(assignEdge);
                        if (sourceGlobal) {
                            if (!targetGlobal) {
                                SootMethod method2 = ((LocalVarNode) assignTarget).getMethod();
                                if (!this.methodToInPorts.get((ArraySetMultiMap<SootMethod, VarNode>) method2).contains(assignTarget)) {
                                    this.methodToInPorts.put(method2, assignSource);
                                }
                            }
                        } else if (targetGlobal) {
                            SootMethod method3 = ((LocalVarNode) assignSource).getMethod();
                            if (!this.methodToOutPorts.get((ArraySetMultiMap<SootMethod, VarNode>) method3).contains(assignSource)) {
                                this.methodToOutPorts.put(method3, assignTarget);
                            }
                        }
                    }
                }
            }
        }
        HashMultiMap callAssigns = pag.callAssigns;
        for (InvokeExpr ie : callAssigns.keySet()) {
            int i2 = callSiteNum;
            callSiteNum++;
            Integer callSite = new Integer(i2);
            this.callSiteToInvokedMethod.put(callSite, ie.getMethod());
            SootMethod invokingMethod = pag.callToMethod.get(ie);
            this.callSiteToInvokingMethod.put(callSite, invokingMethod);
            if (pag.virtualCallsToReceivers.containsKey(ie)) {
                LocalVarNode receiver = (LocalVarNode) pag.virtualCallsToReceivers.get(ie);
                if (!$assertionsDisabled && receiver == null) {
                    throw new AssertionError();
                }
                this.virtCallSiteToReceiver.put(callSite, receiver);
                this.receiverToVirtCallSites.put(receiver, callSite);
            }
            Set curEdges = callAssigns.get(ie);
            for (Pair<Node, Node> callAssign : curEdges) {
                if (callAssign.getO1() instanceof VarNode) {
                    VarNode src = (VarNode) callAssign.getO1();
                    VarNode dst = (VarNode) callAssign.getO2();
                    if (skipNode(src)) {
                        continue;
                    } else {
                        ArraySet edges = getAssignBarEdges(src);
                        AssignEdge edge = null;
                        for (int i3 = 0; i3 < edges.size() && edge == null; i3++) {
                            AssignEdge curEdge = edges.get(i3);
                            if (curEdge.getDst() == dst) {
                                edge = curEdge;
                            }
                        }
                        if (!$assertionsDisabled && edge == null) {
                            throw new AssertionError("no edge from " + src + " to " + dst);
                        }
                        boolean edgeFromOtherCallSite = edge.isCallEdge();
                        edge = edgeFromOtherCallSite ? new AssignEdge(src, dst) : edge;
                        edge.setCallSite(callSite);
                        this.callSiteToEdges.put(callSite, edge);
                        if (SootUtil.isParamNode(dst)) {
                            edge.setParamEdge();
                            SootMethod invokedMethod = ((LocalVarNode) dst).getMethod();
                            this.callSiteToTargets.put(callSite, invokedMethod);
                            this.callSitesInvokingMethod.put(invokedMethod, callSite);
                            if (src instanceof LocalVarNode) {
                                this.callSitesInMethod.put(((LocalVarNode) src).getMethod(), callSite);
                            }
                        } else if (SootUtil.isRetNode(src)) {
                            edge.setReturnEdge();
                            SootMethod invokedMethod2 = ((LocalVarNode) src).getMethod();
                            this.callSiteToTargets.put(callSite, invokedMethod2);
                            this.callSitesInvokingMethod.put(invokedMethod2, callSite);
                            if (dst instanceof LocalVarNode) {
                                this.callSitesInMethod.put(((LocalVarNode) dst).getMethod(), callSite);
                            }
                        } else if (!$assertionsDisabled) {
                            throw new AssertionError("weird call edge " + callAssign);
                        }
                        if (edgeFromOtherCallSite) {
                            addAssignEdge(edge);
                        }
                    }
                }
            }
        }
        if (!$assertionsDisabled && !callEdgesReasonable()) {
            throw new AssertionError();
        }
    }

    private boolean callEdgesReasonable() {
        Set<VarNode> vars = this.contextSensitiveAssignEdges.keySet();
        for (VarNode node : vars) {
            ArraySet<AssignEdge> assigns = this.contextSensitiveAssignEdges.get((ArraySetMultiMap<VarNode, AssignEdge>) node);
            Iterator<AssignEdge> it = assigns.iterator();
            while (it.hasNext()) {
                AssignEdge edge = it.next();
                if (edge.isCallEdge() && edge.getCallSite() == null) {
                    logger.debug(edge + " is weird!!");
                    return false;
                }
            }
        }
        return true;
    }

    private String assignEdgesWellFormed(PAG pag) {
        Iterator iter = pag.getVarNodeNumberer().iterator();
        while (iter.hasNext()) {
            VarNode v = iter.next();
            Set<AssignEdge> outgoingAssigns = getAssignBarEdges(v);
            for (AssignEdge edge : outgoingAssigns) {
                if (edge.getSrc() != v) {
                    return edge + " src should be " + v;
                }
            }
            Set<AssignEdge> incomingAssigns = getAssignEdges(v);
            for (AssignEdge edge2 : incomingAssigns) {
                if (edge2.getDst() != v) {
                    return edge2 + " dst should be " + v;
                }
            }
        }
        return null;
    }

    private boolean skipNode(VarNode node) {
        return false;
    }

    private void addAssignEdge(AssignEdge assignEdge) {
        this.contextSensitiveAssignEdges.put(assignEdge.getSrc(), assignEdge);
        this.contextSensitiveAssignBarEdges.put(assignEdge.getDst(), assignEdge);
    }

    public ArraySet<AssignEdge> getAssignBarEdges(VarNode node) {
        return this.contextSensitiveAssignEdges.get((ArraySetMultiMap<VarNode, AssignEdge>) node);
    }

    public ArraySet<AssignEdge> getAssignEdges(VarNode node) {
        return this.contextSensitiveAssignBarEdges.get((ArraySetMultiMap<VarNode, AssignEdge>) node);
    }

    public Set<SootMethod> methods() {
        return this.methodToNodes.keySet();
    }

    public ArraySet<VarNode> getNodesForMethod(SootMethod method) {
        return this.methodToNodes.get((ArraySetMultiMap<SootMethod, VarNode>) method);
    }

    public ArraySet<VarNode> getInPortsForMethod(SootMethod method) {
        return this.methodToInPorts.get((ArraySetMultiMap<SootMethod, VarNode>) method);
    }

    public ArraySet<VarNode> getOutPortsForMethod(SootMethod method) {
        return this.methodToOutPorts.get((ArraySetMultiMap<SootMethod, VarNode>) method);
    }

    public ArraySet<Integer> getCallSitesInMethod(SootMethod method) {
        return this.callSitesInMethod.get((ArraySetMultiMap<SootMethod, Integer>) method);
    }

    public Set<Integer> getCallSitesInvokingMethod(SootMethod method) {
        return this.callSitesInvokingMethod.get((ArraySetMultiMap<SootMethod, Integer>) method);
    }

    public ArraySet<AssignEdge> getCallSiteEdges(Integer callSite) {
        return this.callSiteToEdges.get((ArraySetMultiMap<Integer, AssignEdge>) callSite);
    }

    public ArraySet<SootMethod> getCallSiteTargets(Integer callSite) {
        return this.callSiteToTargets.get((ArraySetMultiMap<Integer, SootMethod>) callSite);
    }

    public LocalVarNode getReceiverForVirtCallSite(Integer callSite) {
        LocalVarNode ret = this.virtCallSiteToReceiver.get(callSite);
        if ($assertionsDisabled || ret != null) {
            return ret;
        }
        throw new AssertionError();
    }

    public Set<Integer> getVirtCallSitesForReceiver(LocalVarNode receiver) {
        return this.receiverToVirtCallSites.get((ArraySetMultiMap<LocalVarNode, Integer>) receiver);
    }

    public SootMethod getInvokedMethod(Integer callSite) {
        return this.callSiteToInvokedMethod.get(callSite);
    }

    public SootMethod getInvokingMethod(Integer callSite) {
        return this.callSiteToInvokingMethod.get(callSite);
    }

    public boolean isVirtCall(Integer callSite) {
        return this.virtCallSiteToReceiver.containsKey(callSite);
    }
}
