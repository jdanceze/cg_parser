package soot.jimple.toolkits.thread.mhp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.Body;
import soot.Hierarchy;
import soot.IntType;
import soot.Local;
import soot.LongType;
import soot.RefType;
import soot.SootClass;
import soot.SootMethod;
import soot.Trap;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.MonitorStmt;
import soot.jimple.NewExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.internal.JIdentityStmt;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.sets.P2SetVisitor;
import soot.jimple.spark.sets.PointsToSetInternal;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.thread.mhp.stmt.BeginStmt;
import soot.jimple.toolkits.thread.mhp.stmt.JPegStmt;
import soot.jimple.toolkits.thread.mhp.stmt.JoinStmt;
import soot.jimple.toolkits.thread.mhp.stmt.MonitorEntryStmt;
import soot.jimple.toolkits.thread.mhp.stmt.MonitorExitStmt;
import soot.jimple.toolkits.thread.mhp.stmt.NotifiedEntryStmt;
import soot.jimple.toolkits.thread.mhp.stmt.NotifyAllStmt;
import soot.jimple.toolkits.thread.mhp.stmt.NotifyStmt;
import soot.jimple.toolkits.thread.mhp.stmt.OtherStmt;
import soot.jimple.toolkits.thread.mhp.stmt.StartStmt;
import soot.jimple.toolkits.thread.mhp.stmt.WaitStmt;
import soot.jimple.toolkits.thread.mhp.stmt.WaitingStmt;
import soot.tagkit.StringTag;
import soot.toolkits.graph.CompleteUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
import soot.util.Chain;
import soot.util.HashChain;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/mhp/PegChain.class */
public class PegChain extends HashChain {
    CallGraph callGraph;
    private final Map<String, FlowSet> waitingNodes;
    private final PegGraph pg;
    public Body body;
    Hierarchy hierarchy;
    PAG pag;
    Set threadAllocSites;
    Set methodsNeedingInlining;
    Set allocNodes;
    List<List> inlineSites;
    Map<SootMethod, String> synchObj;
    Set multiRunAllocNodes;
    Map<AllocNode, String> allocNodeToObj;
    private final List heads = new ArrayList();
    private final List tails = new ArrayList();
    private final FlowSet pegNodes = new ArraySparseSet();
    private final Map<Unit, JPegStmt> unitToPeg = new HashMap();
    private final Set<List<Object>> joinNeedReconsidered = new HashSet();

    /* JADX INFO: Access modifiers changed from: package-private */
    public PegChain(CallGraph callGraph, Hierarchy hierarchy, PAG pag, Set threadAllocSites, Set methodsNeedingInlining, Set allocNodes, List<List> inlineSites, Map<SootMethod, String> synchObj, Set multiRunAllocNodes, Map<AllocNode, String> allocNodeToObj, Body unitBody, SootMethod sm, String threadName, boolean addBeginNode, PegGraph pegGraph) {
        this.allocNodeToObj = allocNodeToObj;
        this.multiRunAllocNodes = multiRunAllocNodes;
        this.synchObj = synchObj;
        this.inlineSites = inlineSites;
        this.allocNodes = allocNodes;
        this.methodsNeedingInlining = methodsNeedingInlining;
        this.threadAllocSites = threadAllocSites;
        this.hierarchy = hierarchy;
        this.pag = pag;
        this.callGraph = callGraph;
        this.body = unitBody;
        this.pg = pegGraph;
        this.waitingNodes = pegGraph.getWaitingNodes();
        Set<Unit> exceHandlers = this.pg.getExceHandlers();
        for (Trap trap : unitBody.getTraps()) {
            Unit handlerUnit = trap.getHandlerUnit();
            exceHandlers.add(handlerUnit);
        }
        UnitGraph graph = new CompleteUnitGraph(unitBody);
        graph.iterator();
        if (addBeginNode) {
            BeginStmt beginStmt = new BeginStmt("*", threadName, graph, sm);
            this.pg.getCanNotBeCompacted().add(beginStmt);
            addNode(beginStmt);
            this.heads.add(beginStmt);
        }
        for (Object obj : graph.getHeads()) {
            Set<Unit> gray = new HashSet<>();
            LinkedList<Object> queue = new LinkedList<>();
            queue.add(obj);
            visit((Unit) queue.getFirst(), graph, sm, threadName, addBeginNode);
            while (queue.size() > 0) {
                Unit root = (Unit) queue.getFirst();
                for (Unit succ : graph.getSuccsOf(root)) {
                    if (!gray.contains(succ)) {
                        gray.add(succ);
                        queue.addLast(succ);
                        visit(succ, graph, sm, threadName, addBeginNode);
                    }
                }
                queue.remove(root);
            }
        }
        postHandleJoinStmt();
        this.pg.getUnitToPegMap().put(this, this.unitToPeg);
    }

    private void visit(Unit unit, UnitGraph graph, SootMethod sm, String threadName, boolean addBeginNode) {
        if (unit instanceof MonitorStmt) {
            Value value = ((MonitorStmt) unit).getOp();
            if (value instanceof Local) {
                Type type = ((Local) value).getType();
                if (type instanceof RefType) {
                    ((RefType) type).getSootClass();
                    if (unit instanceof EnterMonitorStmt) {
                        JPegStmt pegStmt = new MonitorEntryStmt(makeObjName(value, type, unit), threadName, unit, graph, sm);
                        addAndPutNonCompacted(unit, pegStmt);
                        return;
                    } else if (unit instanceof ExitMonitorStmt) {
                        JPegStmt pegStmt2 = new MonitorExitStmt(makeObjName(value, type, unit), threadName, unit, graph, sm);
                        addAndPutNonCompacted(unit, pegStmt2);
                        return;
                    }
                }
            }
        }
        if (((Stmt) unit).containsInvokeExpr()) {
            Value invokeExpr = ((Stmt) unit).getInvokeExpr();
            SootMethod method = ((InvokeExpr) invokeExpr).getMethod();
            String name = method.getName();
            Value value2 = null;
            Type type2 = null;
            List paras = method.getParameterTypes();
            String objName = null;
            if (invokeExpr instanceof InstanceInvokeExpr) {
                value2 = ((InstanceInvokeExpr) invokeExpr).getBase();
                if (value2 instanceof Local) {
                    type2 = ((Local) value2).getType();
                    if (type2 instanceof RefType) {
                        SootClass sc = ((RefType) type2).getSootClass();
                        objName = sc.getName();
                    }
                }
            } else if (!(invokeExpr instanceof StaticInvokeExpr)) {
                throw new RuntimeException("Error: new type of invokeExpre: " + invokeExpr);
            }
            boolean find = false;
            if (method.getName().equals("start")) {
                List<SootClass> superClasses = this.hierarchy.getSuperclassesOfIncluding(method.getDeclaringClass());
                Iterator<SootClass> it = superClasses.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    String className = it.next().getName();
                    if (className.equals("java.lang.Thread")) {
                        find = true;
                        break;
                    }
                }
            }
            if (method.getName().equals("run") && method.getDeclaringClass().getName().equals("java.lang.Runnable")) {
                find = true;
            }
            if (name.equals("wait") && (paras.size() == 0 || ((paras.size() == 1 && (paras.get(0) instanceof LongType)) || (paras.size() == 2 && (paras.get(0) instanceof LongType) && (paras.get(1) instanceof IntType))))) {
                transformWaitNode(makeObjName(value2, type2, unit), name, threadName, unit, graph, sm);
                return;
            } else if ((name.equals("start") || name.equals("run")) && find) {
                PointsToSetInternal pts = (PointsToSetInternal) this.pag.reachingObjects((Local) value2);
                List<AllocNode> mayAlias = findMayAlias(pts, unit);
                StartStmt startStmt = new StartStmt(value2.toString(), threadName, unit, graph, sm);
                if (this.pg.getStartToThread().containsKey(startStmt)) {
                    throw new RuntimeException("map startToThread contain duplicated start() method call");
                }
                this.pg.getCanNotBeCompacted().add(startStmt);
                addAndPut(unit, startStmt);
                List<PegChain> runMethodChainList = new ArrayList<>();
                ArrayList arrayList = new ArrayList();
                if (mayAlias.size() < 1) {
                    throw new RuntimeException("The may alias set of " + unit + "is empty!");
                }
                for (AllocNode allocNode : mayAlias) {
                    RefType refType = ((NewExpr) allocNode.getNewExpr()).getBaseType();
                    SootClass maySootClass = refType.getSootClass();
                    SootMethod meth = this.hierarchy.resolveConcreteDispatch(maySootClass, method.getDeclaringClass().getMethodByName("run"));
                    Body mBody = meth.getActiveBody();
                    int threadNo = Counter.getThreadNo();
                    String callerName = "thread" + threadNo;
                    this.pg.getThreadNameToStart().put(callerName, startStmt);
                    PegChain newChain = new PegChain(this.callGraph, this.hierarchy, this.pag, this.threadAllocSites, this.methodsNeedingInlining, this.allocNodes, this.inlineSites, this.synchObj, this.multiRunAllocNodes, this.allocNodeToObj, mBody, sm, callerName, true, this.pg);
                    this.pg.getAllocNodeToThread().put(allocNode, newChain);
                    runMethodChainList.add(newChain);
                    arrayList.add(allocNode);
                }
                this.pg.getStartToThread().put(startStmt, runMethodChainList);
                this.pg.getStartToAllocNodes().put(startStmt, arrayList);
                return;
            } else if (name.equals("join") && method.getDeclaringClass().getName().equals("java.lang.Thread")) {
                PointsToSetInternal pts2 = (PointsToSetInternal) this.pag.reachingObjects((Local) value2);
                List<AllocNode> mayAlias2 = findMayAlias(pts2, unit);
                if (mayAlias2.size() != 1) {
                    if (mayAlias2.size() < 1) {
                        throw new RuntimeException("==threadAllocaSits==\n" + this.threadAllocSites.toString());
                    }
                    JPegStmt pegStmt3 = new JoinStmt(value2.toString(), threadName, unit, graph, sm);
                    addAndPutNonCompacted(unit, pegStmt3);
                    this.pg.getSpecialJoin().add(pegStmt3);
                    return;
                }
                for (Object allocNode2 : mayAlias2) {
                    JoinStmt joinStmt = new JoinStmt(value2.toString(), threadName, unit, graph, sm);
                    if (!this.pg.getAllocNodeToThread().containsKey(allocNode2)) {
                        List<Object> list = new ArrayList<>();
                        list.add(joinStmt);
                        list.add(allocNode2);
                        list.add(unit);
                        this.joinNeedReconsidered.add(list);
                    } else {
                        Chain thread = this.pg.getAllocNodeToThread().get(allocNode2);
                        addAndPutNonCompacted(unit, joinStmt);
                        this.pg.getJoinStmtToThread().put(joinStmt, thread);
                    }
                }
                return;
            } else if (name.equals("notifyAll") && paras.size() == 0) {
                String objName2 = makeObjName(value2, type2, unit);
                JPegStmt notifyAllStmt = new NotifyAllStmt(objName2, threadName, unit, graph, sm);
                addAndPutNonCompacted(unit, notifyAllStmt);
                if (this.pg.getNotifyAll().containsKey(objName2)) {
                    Set<JPegStmt> notifyAllSet = this.pg.getNotifyAll().get(objName2);
                    notifyAllSet.add(notifyAllStmt);
                    this.pg.getNotifyAll().put(objName2, notifyAllSet);
                    return;
                }
                Set<JPegStmt> notifyAllSet2 = new HashSet<>();
                notifyAllSet2.add(notifyAllStmt);
                this.pg.getNotifyAll().put(objName2, notifyAllSet2);
                return;
            } else if (name.equals("notify") && paras.size() == 0 && method.getDeclaringClass().getName().equals("java.lang.Thread")) {
                JPegStmt pegStmt4 = new NotifyStmt(makeObjName(value2, type2, unit), threadName, unit, graph, sm);
                addAndPutNonCompacted(unit, pegStmt4);
                return;
            } else if (method.isConcrete() && !method.getDeclaringClass().isLibraryClass()) {
                new LinkedList();
                SootMethod targetMethod = null;
                if (invokeExpr instanceof StaticInvokeExpr) {
                    targetMethod = method;
                } else {
                    TargetMethodsFinder tmd = new TargetMethodsFinder();
                    List<SootMethod> targetList = tmd.find(unit, this.callGraph, true, false);
                    if (targetList.size() > 1) {
                        System.out.println("target: " + targetList);
                        System.out.println("unit is: " + unit);
                        System.err.println("exit because target is bigger than 1.");
                        System.exit(1);
                    } else if (targetList.size() < 1) {
                        System.err.println("targetList size <1");
                    } else {
                        targetMethod = targetList.get(0);
                    }
                }
                if (this.methodsNeedingInlining == null) {
                    System.err.println("methodsNeedingInlining is null at " + unit);
                    return;
                } else if (targetMethod == null) {
                    System.err.println("targetMethod is null at " + unit);
                    return;
                } else if (this.methodsNeedingInlining.contains(targetMethod)) {
                    inlineMethod(targetMethod, objName, name, threadName, unit, graph, sm);
                    return;
                } else {
                    JPegStmt pegStmt5 = new OtherStmt(objName, name, threadName, unit, graph, sm);
                    addAndPut(unit, pegStmt5);
                    return;
                }
            } else {
                JPegStmt pegStmt6 = new OtherStmt(objName, name, threadName, unit, graph, sm);
                addAndPut(unit, pegStmt6);
                return;
            }
        }
        newAndAddElement(unit, graph, threadName, sm);
    }

    private void transformWaitNode(String objName, String name, String threadName, Unit unit, UnitGraph graph, SootMethod sm) {
        WaitStmt waitStmt = new WaitStmt(objName, threadName, unit, graph, sm);
        addAndPutNonCompacted(unit, waitStmt);
        WaitingStmt waitingStmt = new WaitingStmt(objName, threadName, sm);
        this.pg.getCanNotBeCompacted().add(waitingStmt);
        addNode(waitingStmt);
        if (this.waitingNodes.containsKey(objName)) {
            FlowSet waitingNodesSet = this.waitingNodes.get(objName);
            if (!waitingNodesSet.contains(waitingStmt)) {
                waitingNodesSet.add(waitingStmt);
                this.waitingNodes.put(waitingStmt.getObject(), waitingNodesSet);
            }
        } else {
            FlowSet waitingNodesSet2 = new ArraySparseSet();
            waitingNodesSet2.add(waitingStmt);
            this.waitingNodes.put(waitingStmt.getObject(), waitingNodesSet2);
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(waitingStmt);
        this.pg.getUnitToSuccs().put(waitStmt, arrayList);
        NotifiedEntryStmt notifiedEntryStmt = new NotifiedEntryStmt(objName, threadName, sm);
        this.pg.getCanNotBeCompacted().add(notifiedEntryStmt);
        addNode(notifiedEntryStmt);
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(notifiedEntryStmt);
        this.pg.getUnitToSuccs().put(waitingStmt, arrayList2);
    }

    private List<AllocNode> findMayAlias(PointsToSetInternal pts, Unit unit) {
        List<AllocNode> list = new ArrayList<>();
        Iterator<AllocNode> it = makePtsIterator(pts);
        while (it.hasNext()) {
            AllocNode obj = it.next();
            list.add(obj);
        }
        return list;
    }

    private void inlineMethod(SootMethod targetMethod, String objName, String name, String threadName, Unit unit, UnitGraph graph, SootMethod sm) {
        Body unitBody = targetMethod.getActiveBody();
        OtherStmt otherStmt = new OtherStmt(objName, name, threadName, unit, graph, sm);
        if (targetMethod.isSynchronized()) {
            String synchObj = findSynchObj(targetMethod);
            MonitorEntryStmt monitorEntryStmt = new MonitorEntryStmt(synchObj, threadName, graph, sm);
            MonitorExitStmt monitorExitStmt = new MonitorExitStmt(synchObj, threadName, graph, sm);
            this.pg.getCanNotBeCompacted().add(monitorEntryStmt);
            this.pg.getCanNotBeCompacted().add(monitorExitStmt);
            List list = new ArrayList();
            list.add(otherStmt);
            list.add(monitorEntryStmt);
            list.add(monitorExitStmt);
            this.pg.getSynch().add(list);
        }
        addAndPut(unit, otherStmt);
        PegGraph pG = new PegGraph(this.callGraph, this.hierarchy, this.pag, this.methodsNeedingInlining, this.allocNodes, this.inlineSites, this.synchObj, this.multiRunAllocNodes, this.allocNodeToObj, unitBody, threadName, targetMethod, true, false);
        List list2 = new ArrayList();
        list2.add(otherStmt);
        list2.add(this);
        list2.add(this.pg);
        list2.add(pG);
        this.inlineSites.add(list2);
    }

    private String findSynchObj(SootMethod targetMethod) {
        if (this.synchObj.containsKey(targetMethod)) {
            return this.synchObj.get(targetMethod);
        }
        String objName = null;
        if (targetMethod.isStatic()) {
            objName = targetMethod.getDeclaringClass().getName();
        } else {
            Iterator it = targetMethod.getActiveBody().getUnits().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Object obj = it.next();
                if (obj instanceof JIdentityStmt) {
                    Value thisRef = ((JIdentityStmt) obj).getLeftOp();
                    if (thisRef instanceof Local) {
                        Type type = ((Local) thisRef).getType();
                        if (type instanceof RefType) {
                            objName = makeObjName(thisRef, type, (Unit) obj);
                            this.synchObj.put(targetMethod, objName);
                            break;
                        }
                    } else {
                        continue;
                    }
                }
            }
        }
        return objName;
    }

    private void addNode(JPegStmt stmt) {
        addLast(stmt);
        this.pegNodes.add(stmt);
        this.pg.getAllNodes().add(stmt);
    }

    private void addAndPut(Unit unit, JPegStmt stmt) {
        this.unitToPeg.put(unit, stmt);
        addNode(stmt);
    }

    private void addAndPutNonCompacted(Unit unit, JPegStmt stmt) {
        this.pg.getCanNotBeCompacted().add(stmt);
        addAndPut(unit, stmt);
    }

    private void newAndAddElement(Unit unit, UnitGraph graph, String threadName, SootMethod sm) {
        JPegStmt pegStmt = new OtherStmt("*", unit.toString(), threadName, unit, graph, sm);
        addAndPut(unit, pegStmt);
    }

    public List getHeads() {
        return this.heads;
    }

    public List getTails() {
        return this.tails;
    }

    protected void addTag() {
        Iterator it = iterator();
        while (it.hasNext()) {
            JPegStmt stmt = (JPegStmt) it.next();
            int count = Counter.getTagNo();
            StringTag t = new StringTag(Integer.toString(count));
            stmt.addTag(t);
        }
    }

    private Iterator<AllocNode> makePtsIterator(PointsToSetInternal pts) {
        final HashSet<AllocNode> ret = new HashSet<>();
        pts.forall(new P2SetVisitor() { // from class: soot.jimple.toolkits.thread.mhp.PegChain.1
            @Override // soot.jimple.spark.sets.P2SetVisitor
            public void visit(Node n) {
                ret.add((AllocNode) n);
            }
        });
        return ret.iterator();
    }

    private void postHandleJoinStmt() {
        for (List list : this.joinNeedReconsidered) {
            JPegStmt pegStmt = (JPegStmt) list.get(0);
            AllocNode allocNode = (AllocNode) list.get(1);
            Unit unit = (Unit) list.get(2);
            if (!this.pg.getAllocNodeToThread().containsKey(allocNode)) {
                throw new RuntimeException("allocNodeToThread does not contains key: " + allocNode);
            }
            Chain thread = this.pg.getAllocNodeToThread().get(allocNode);
            addAndPutNonCompacted(unit, pegStmt);
            this.pg.getJoinStmtToThread().put(pegStmt, thread);
        }
    }

    private String makeObjName(Value value, Type type, Unit unit) {
        String objName;
        PointsToSetInternal pts = (PointsToSetInternal) this.pag.reachingObjects((Local) value);
        List<AllocNode> mayAlias = findMayAlias(pts, unit);
        if (this.allocNodeToObj == null) {
            throw new RuntimeException("allocNodeToObj is null!");
        }
        if (mayAlias.size() == 1) {
            AllocNode an = mayAlias.get(0);
            if (this.allocNodeToObj.containsKey(an)) {
                objName = this.allocNodeToObj.get(an);
            } else {
                objName = "obj" + Counter.getObjNo();
                this.allocNodeToObj.put(an, objName);
            }
        } else {
            AllocNode an2 = mayAlias.get(0);
            if (this.allocNodeToObj.containsKey(an2)) {
                objName = "MULTI" + this.allocNodeToObj.get(an2);
            } else {
                objName = "MULTIobj" + Counter.getObjNo();
                this.allocNodeToObj.put(an2, objName);
            }
        }
        if (objName == null) {
            throw new RuntimeException("Can not find target object for " + unit);
        }
        return objName;
    }

    protected Map<String, FlowSet> getWaitingNodes() {
        return this.waitingNodes;
    }

    protected void testChain() {
        System.out.println("******** chain********");
        Iterator it = iterator();
        while (it.hasNext()) {
            JPegStmt stmt = (JPegStmt) it.next();
            System.out.println(stmt.toString());
        }
    }
}
