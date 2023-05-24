package soot.jimple.toolkits.thread.mhp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.Body;
import soot.Hierarchy;
import soot.SootMethod;
import soot.Unit;
import soot.coffi.Instruction;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.PAG;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.thread.mhp.stmt.JPegStmt;
import soot.jimple.toolkits.thread.mhp.stmt.StartStmt;
import soot.tagkit.StringTag;
import soot.tagkit.Tag;
import soot.toolkits.graph.CompleteUnitGraph;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/mhp/PegGraph.class */
public class PegGraph implements DirectedGraph {
    private List heads;
    private List tails;
    protected HashMap<Object, List> unitToSuccs;
    protected HashMap<Object, List> unitToPreds;
    private HashMap unitToPegMap;
    public HashMap<JPegStmt, List> startToThread;
    public HashMap startToAllocNodes;
    private HashMap<String, FlowSet> waitingNodes;
    private Map startToBeginNodes;
    private HashMap<String, Set<JPegStmt>> notifyAll;
    private Set methodsNeedingInlining;
    private boolean needInlining;
    private Set<List> synch;
    private Set<JPegStmt> specialJoin;
    private Body body;
    private Chain unitChain;
    private Chain mainPegChain;
    private FlowSet allNodes;
    private Map<String, FlowSet> monitor;
    private Set canNotBeCompacted;
    private Set threadAllocSites;
    private File logFile;
    private FileWriter fileWriter;
    private Set<Object> monitorObjs;
    private Set<Unit> exceHandlers;
    protected Map threadNo;
    protected Map threadNameToStart;
    protected Map<AllocNode, String> allocNodeToObj;
    protected Map<AllocNode, PegChain> allocNodeToThread;
    protected Map<JPegStmt, Chain> joinStmtToThread;
    Set allocNodes;
    List<List> inlineSites;
    Map<SootMethod, String> synchObj;
    Set multiRunAllocNodes;

    public PegGraph(CallGraph callGraph, Hierarchy hierarchy, PAG pag, Set<Object> methodsNeedingInlining, Set<AllocNode> allocNodes, List inlineSites, Map synchObj, Set<AllocNode> multiRunAllocNodes, Map allocNodeToObj, Body unitBody, SootMethod sm, boolean addExceptionEdges, boolean dontAddEdgeFromStmtBeforeAreaOfProtectionToCatchBlock) {
        this(callGraph, hierarchy, pag, methodsNeedingInlining, allocNodes, inlineSites, synchObj, multiRunAllocNodes, allocNodeToObj, unitBody, "main", sm, addExceptionEdges, dontAddEdgeFromStmtBeforeAreaOfProtectionToCatchBlock);
    }

    public PegGraph(CallGraph callGraph, Hierarchy hierarchy, PAG pag, Set methodsNeedingInlining, Set allocNodes, List<List> inlineSites, Map<SootMethod, String> synchObj, Set multiRunAllocNodes, Map<AllocNode, String> allocNodeToObj, Body unitBody, String threadName, SootMethod sm, boolean addExceEdge, boolean dontAddEdgeFromStmtBeforeAreaOfProtectionToCatchBlock) {
        this.allocNodeToObj = allocNodeToObj;
        this.multiRunAllocNodes = multiRunAllocNodes;
        this.synchObj = synchObj;
        this.inlineSites = inlineSites;
        this.allocNodes = allocNodes;
        this.methodsNeedingInlining = methodsNeedingInlining;
        this.logFile = new File("log.txt");
        try {
            this.fileWriter = new FileWriter(this.logFile);
        } catch (IOException e) {
            System.err.println("Errors occur during create FileWriter !");
        }
        this.body = unitBody;
        this.synch = new HashSet();
        this.exceHandlers = new HashSet();
        this.needInlining = true;
        this.monitorObjs = new HashSet();
        this.startToBeginNodes = new HashMap();
        this.unitChain = this.body.getUnits();
        int size = this.unitChain.size();
        this.unitToSuccs = new HashMap<>((size * 2) + 1, 0.7f);
        this.unitToPreds = new HashMap<>((size * 2) + 1, 0.7f);
        this.unitToPegMap = new HashMap((size * 2) + 1, 0.7f);
        this.startToThread = new HashMap<>((size * 2) + 1, 0.7f);
        this.startToAllocNodes = new HashMap((size * 2) + 1, 0.7f);
        this.waitingNodes = new HashMap<>((size * 2) + 1, 0.7f);
        this.joinStmtToThread = new HashMap();
        this.threadNo = new HashMap();
        this.threadNameToStart = new HashMap();
        this.allocNodeToObj = new HashMap((size * 2) + 1, 0.7f);
        this.allocNodeToThread = new HashMap((size * 2) + 1, 0.7f);
        this.notifyAll = new HashMap<>((size * 2) + 1, 0.7f);
        Set methodsNeedingInlining2 = new HashSet();
        this.allNodes = new ArraySparseSet();
        this.canNotBeCompacted = new HashSet();
        this.threadAllocSites = new HashSet();
        this.specialJoin = new HashSet();
        new CompleteUnitGraph(this.body);
        this.mainPegChain = new PegChain(callGraph, hierarchy, pag, this.threadAllocSites, methodsNeedingInlining2, allocNodes, inlineSites, synchObj, multiRunAllocNodes, allocNodeToObj, this.body, sm, threadName, true, this);
        buildSuccessor(this.mainPegChain);
        buildPredecessor(this.mainPegChain);
        addMonitorStmt();
        addTag();
        buildHeadsAndTails();
        try {
            this.fileWriter.flush();
            this.fileWriter.close();
        } catch (IOException e2) {
            System.err.println("Errors occur during close file  " + this.logFile.getName());
        }
    }

    protected Map getStartToBeginNodes() {
        return this.startToBeginNodes;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Map<JPegStmt, Chain> getJoinStmtToThread() {
        return this.joinStmtToThread;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Map getUnitToPegMap() {
        return this.unitToPegMap;
    }

    protected void addMonitorStmt() {
        if (this.synch.size() > 0) {
            for (List list : this.synch) {
                JPegStmt node = (JPegStmt) list.get(0);
                JPegStmt enter = (JPegStmt) list.get(1);
                JPegStmt exit = (JPegStmt) list.get(2);
                if (!this.mainPegChain.contains(node)) {
                    boolean find = false;
                    Set maps = this.startToThread.entrySet();
                    for (Map.Entry<JPegStmt, List> entry : maps) {
                        entry.getKey();
                        Iterator runIt = entry.getValue().iterator();
                        while (true) {
                            if (!runIt.hasNext()) {
                                break;
                            }
                            Chain chain = (Chain) runIt.next();
                            if (chain.contains(node)) {
                                find = true;
                                chain.add(enter);
                                chain.add(exit);
                                break;
                            }
                        }
                    }
                    if (!find) {
                        throw new RuntimeException("fail to find stmt: " + node + " in chains!");
                    }
                } else {
                    this.mainPegChain.add(enter);
                    this.mainPegChain.add(exit);
                }
                this.allNodes.add(enter);
                this.allNodes.add(exit);
                insertBefore(node, enter);
                insertAfter(node, exit);
            }
        }
    }

    private void insertBefore(JPegStmt node, JPegStmt enter) {
        List predOfBefore = new ArrayList();
        predOfBefore.addAll(getPredsOf(node));
        this.unitToPreds.put(enter, predOfBefore);
        for (Object pred : getPredsOf(node)) {
            List succ = getSuccsOf(pred);
            succ.remove(node);
            succ.add(enter);
        }
        List succOfBefore = new ArrayList();
        succOfBefore.add(node);
        this.unitToSuccs.put(enter, succOfBefore);
        List predOfNode = new ArrayList();
        predOfNode.add(enter);
        this.unitToPreds.put(node, predOfNode);
    }

    private void insertAfter(JPegStmt node, JPegStmt after) {
        List succOfAfter = new ArrayList();
        succOfAfter.addAll(getSuccsOf(node));
        this.unitToSuccs.put(after, succOfAfter);
        for (Object succ : getSuccsOf(node)) {
            List pred = getPredsOf(succ);
            pred.remove(node);
            pred.add(after);
        }
        List succOfNode = new ArrayList();
        succOfNode.add(after);
        this.unitToSuccs.put(node, succOfNode);
        List predOfAfter = new ArrayList();
        predOfAfter.add(node);
        this.unitToPreds.put(after, predOfAfter);
    }

    private void buildSuccessor(Chain pegChain) {
        JPegStmt pp;
        HashMap unitToPeg = (HashMap) this.unitToPegMap.get(pegChain);
        Iterator pegIt = pegChain.iterator();
        JPegStmt currentNode = pegIt.hasNext() ? (JPegStmt) pegIt.next() : null;
        if (currentNode != null) {
            JPegStmt nextNode = pegIt.hasNext() ? (JPegStmt) pegIt.next() : null;
            if (currentNode.getName().equals("begin")) {
                List<JPegStmt> successors = new ArrayList<>();
                successors.add(nextNode);
                this.unitToSuccs.put(currentNode, successors);
                currentNode = nextNode;
            }
            while (currentNode != null) {
                if (this.unitToSuccs.containsKey(currentNode) && !currentNode.getName().equals("wait")) {
                    currentNode = pegIt.hasNext() ? (JPegStmt) pegIt.next() : null;
                } else {
                    List<JPegStmt> successors2 = new ArrayList<>();
                    Unit unit = currentNode.getUnit();
                    UnitGraph unitGraph = currentNode.getUnitGraph();
                    List unitSucc = unitGraph.getSuccsOf(unit);
                    for (Unit un : unitSucc) {
                        if (!(unit instanceof ExitMonitorStmt) || !this.exceHandlers.contains(un)) {
                            if (unitToPeg.containsKey(un) && (pp = (JPegStmt) unitToPeg.get(un)) != null && !successors2.contains(pp)) {
                                successors2.add(pp);
                            }
                        }
                    }
                    if (currentNode.getName().equals("wait")) {
                        while (!currentNode.getName().equals("notified-entry")) {
                            currentNode = pegIt.hasNext() ? (JPegStmt) pegIt.next() : null;
                        }
                        this.unitToSuccs.put(currentNode, successors2);
                    } else {
                        this.unitToSuccs.put(currentNode, successors2);
                    }
                    if (currentNode.getName().equals("start") && this.startToThread.containsKey(currentNode)) {
                        List<Chain> runMethodChainList = this.startToThread.get(currentNode);
                        for (Chain subChain : runMethodChainList) {
                            if (subChain != null) {
                                buildSuccessor(subChain);
                            } else {
                                System.out.println("*********subgraph is null!!!");
                            }
                        }
                    }
                    currentNode = pegIt.hasNext() ? (JPegStmt) pegIt.next() : null;
                }
            }
        }
    }

    private void buildPredecessor(Chain pegChain) {
        Iterator unitIt = pegChain.iterator();
        while (unitIt.hasNext()) {
            this.unitToPreds.put((JPegStmt) unitIt.next(), new ArrayList());
        }
        for (Object s : pegChain) {
            if (this.unitToSuccs.containsKey(s)) {
                List<JPegStmt> succList = this.unitToSuccs.get(s);
                for (JPegStmt successor : succList) {
                    List<Object> predList = this.unitToPreds.get(successor);
                    if (predList != null && !predList.contains(s)) {
                        try {
                            predList.add(s);
                            if (successor instanceof StartStmt) {
                                List<Chain> runMethodChainList = this.startToThread.get(successor);
                                if (runMethodChainList == null) {
                                    throw new RuntimeException("null runmehtodchain: \n" + successor.getUnit());
                                }
                                for (Chain subChain : runMethodChainList) {
                                    buildPredecessor(subChain);
                                }
                            } else {
                                continue;
                            }
                        } catch (NullPointerException e) {
                            System.out.println(s + "successor: " + successor);
                            throw e;
                        }
                    } else {
                        System.err.println("predlist of " + s + " is null");
                    }
                }
            } else {
                throw new RuntimeException("unitToSuccs does not contains key" + s);
            }
        }
    }

    private void buildHeadsAndTails() {
        List tailList = new ArrayList();
        List headList = new ArrayList();
        for (JPegStmt s : this.mainPegChain) {
            List succs = this.unitToSuccs.get(s);
            if (succs.size() == 0) {
                tailList.add(s);
            }
            if (!this.unitToPreds.containsKey(s)) {
                throw new RuntimeException("unitToPreds does not contain key: " + s);
            }
            List preds = this.unitToPreds.get(s);
            if (preds.size() == 0) {
                headList.add(s);
            }
        }
        this.tails = tailList;
        this.heads = headList;
        for (Object obj : this.heads) {
        }
        buildPredecessor(this.mainPegChain);
    }

    public boolean addPeg(PegGraph pg, Chain chain) {
        if (!pg.removeBeginNode()) {
            return false;
        }
        Iterator mainIt = pg.mainIterator();
        while (mainIt.hasNext()) {
            this.mainPegChain.addLast((JPegStmt) mainIt.next());
        }
        Iterator it = pg.iterator();
        while (it.hasNext()) {
            JPegStmt s = (JPegStmt) it.next();
            if (this.allNodes.contains(s)) {
                throw new RuntimeException("error! allNodes contains: " + s);
            }
            this.allNodes.add(s);
        }
        this.unitToSuccs.putAll(pg.getUnitToSuccs());
        this.unitToPreds.putAll(pg.getUnitToPreds());
        return true;
    }

    private boolean removeBeginNode() {
        List heads = getHeads();
        if (heads.size() != 1) {
            return false;
        }
        JPegStmt head = (JPegStmt) heads.get(0);
        if (!head.getName().equals("begin")) {
            throw new RuntimeException("Error: the head is not begin node!");
        }
        heads.remove(0);
        for (JPegStmt succOfHead : getSuccsOf(head)) {
            this.unitToPreds.put(succOfHead, new ArrayList());
            heads.add(succOfHead);
        }
        if (!this.mainPegChain.remove(head)) {
            throw new RuntimeException("fail to remove begin node in from mainPegChain!");
        }
        if (!this.allNodes.contains(head)) {
            throw new RuntimeException("fail to find begin node in FlowSet allNodes!");
        }
        this.allNodes.remove(head);
        if (this.unitToSuccs.containsKey(head)) {
            this.unitToSuccs.remove(head);
            return true;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void buildSuccsForInlining(JPegStmt stmt, Chain chain, PegGraph inlinee) {
        List succList;
        int pos;
        stmt.getTags().get(0);
        Iterator headsIt = inlinee.getHeads().iterator();
        for (JPegStmt pred : getPredsOf(stmt)) {
            List succList2 = getSuccsOf(pred);
            succList2.remove(succList2.indexOf(stmt));
            while (headsIt.hasNext()) {
                succList2.add(headsIt.next());
            }
            this.unitToSuccs.put(pred, succList2);
        }
        while (headsIt.hasNext()) {
            Object head = headsIt.next();
            List predsOfHeads = new ArrayList();
            predsOfHeads.addAll(getPredsOf(head));
            this.unitToPreds.put(head, predsOfHeads);
        }
        for (JPegStmt tail : inlinee.getTails()) {
            if (this.unitToSuccs.containsKey(tail)) {
                succList = getSuccsOf(tail);
            } else {
                succList = new ArrayList();
            }
            for (JPegStmt succ : getSuccsOf(stmt)) {
                succList.add(succ);
                List predListOfSucc = getPredsOf(succ);
                if (predListOfSucc == null) {
                    throw new RuntimeException("Error: predListOfSucc is null!");
                }
                if (predListOfSucc.size() != 0 && ((pos = predListOfSucc.indexOf(stmt)) > 0 || pos == 0)) {
                    predListOfSucc.remove(pos);
                }
                this.unitToPreds.put(succ, predListOfSucc);
            }
            this.unitToSuccs.put(tail, succList);
        }
        for (JPegStmt s : inlinee.getTails()) {
            if (this.unitToSuccs.containsKey(s)) {
                for (JPegStmt successor : this.unitToSuccs.get(s)) {
                    List<JPegStmt> predList = this.unitToPreds.get(successor);
                    if (predList != null && !predList.contains(s)) {
                        try {
                            predList.add(s);
                        } catch (NullPointerException e) {
                            System.out.println(s + "successor: " + successor);
                            throw e;
                        }
                    }
                }
                continue;
            }
        }
        if (!this.allNodes.contains(stmt)) {
            throw new RuntimeException("fail to find begin node in  allNodes!");
        }
        this.allNodes.remove(stmt);
        if (!chain.contains(stmt)) {
            throw new RuntimeException("Error! Chain does not contains stmt (extending point)!");
        }
        if (!chain.remove(stmt)) {
            throw new RuntimeException("fail to remove invoke stmt in from Chain!");
        }
        if (this.unitToSuccs.containsKey(stmt)) {
            this.unitToSuccs.remove(stmt);
        }
        if (this.unitToPreds.containsKey(stmt)) {
            this.unitToPreds.remove(stmt);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void buildMaps(PegGraph pg) {
        this.exceHandlers.addAll(pg.getExceHandlers());
        this.startToThread.putAll(pg.getStartToThread());
        this.startToAllocNodes.putAll(pg.getStartToAllocNodes());
        this.startToBeginNodes.putAll(pg.getStartToBeginNodes());
        this.waitingNodes.putAll(pg.getWaitingNodes());
        this.notifyAll.putAll(pg.getNotifyAll());
        this.canNotBeCompacted.addAll(pg.getCanNotBeCompacted());
        this.synch.addAll(pg.getSynch());
        this.threadNameToStart.putAll(pg.getThreadNameToStart());
        this.specialJoin.addAll(pg.getSpecialJoin());
        this.joinStmtToThread.putAll(pg.getJoinStmtToThread());
        this.threadAllocSites.addAll(pg.getThreadAllocSites());
        this.allocNodeToThread.putAll(pg.getAllocNodeToThread());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void buildPreds() {
        buildPredecessor(this.mainPegChain);
        Set maps = getStartToThread().entrySet();
        for (Map.Entry<JPegStmt, List> entry : maps) {
            List<Chain> runMethodChainList = entry.getValue();
            for (Chain chain : runMethodChainList) {
                buildPredecessor(chain);
            }
        }
    }

    public void computeMonitorObjs() {
        Set maps = this.monitor.entrySet();
        for (Map.Entry<String, FlowSet> entry : maps) {
            FlowSet fs = entry.getValue();
            for (Object obj : fs) {
                if (!this.monitorObjs.contains(obj)) {
                    this.monitorObjs.add(obj);
                }
            }
        }
    }

    protected boolean getNeedInlining() {
        return this.needInlining;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public FlowSet getAllNodes() {
        return this.allNodes;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public HashMap getUnitToSuccs() {
        return this.unitToSuccs;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public HashMap getUnitToPreds() {
        return this.unitToPreds;
    }

    public Body getBody() {
        return this.body;
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public List getHeads() {
        return this.heads;
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public List getTails() {
        return this.tails;
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public List getPredsOf(Object s) {
        if (!this.unitToPreds.containsKey(s)) {
            throw new RuntimeException("Invalid stmt" + s);
        }
        return this.unitToPreds.get(s);
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public List getSuccsOf(Object s) {
        if (!this.unitToSuccs.containsKey(s)) {
            return new ArrayList();
        }
        return this.unitToSuccs.get(s);
    }

    public Set getCanNotBeCompacted() {
        return this.canNotBeCompacted;
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public int size() {
        return this.allNodes.size();
    }

    public Iterator mainIterator() {
        return this.mainPegChain.iterator();
    }

    @Override // soot.toolkits.graph.DirectedGraph, java.lang.Iterable
    public Iterator iterator() {
        return this.allNodes.iterator();
    }

    public String toString() {
        Iterator it = iterator();
        StringBuffer buf = new StringBuffer();
        while (it.hasNext()) {
            JPegStmt u = (JPegStmt) it.next();
            buf.append("u is: " + u + "\n");
            List l = new ArrayList();
            l.addAll(getPredsOf(u));
            buf.append("preds: " + l + "\n");
            List l2 = new ArrayList();
            l2.addAll(getSuccsOf(u));
            buf.append("succs: " + l2 + "\n");
        }
        return buf.toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Set<Unit> getExceHandlers() {
        return this.exceHandlers;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setMonitor(Map<String, FlowSet> m) {
        this.monitor = m;
    }

    public Map<String, FlowSet> getMonitor() {
        return this.monitor;
    }

    public Set<Object> getMonitorObjs() {
        return this.monitorObjs;
    }

    protected Set getThreadAllocSites() {
        return this.threadAllocSites;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Set<JPegStmt> getSpecialJoin() {
        return this.specialJoin;
    }

    public HashSet<List> getSynch() {
        return (HashSet) this.synch;
    }

    public Map<JPegStmt, List> getStartToThread() {
        return this.startToThread;
    }

    public Map getStartToAllocNodes() {
        return this.startToAllocNodes;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Map<String, FlowSet> getWaitingNodes() {
        return this.waitingNodes;
    }

    public Map<String, Set<JPegStmt>> getNotifyAll() {
        return this.notifyAll;
    }

    protected Map<AllocNode, String> getAllocNodeToObj() {
        return this.allocNodeToObj;
    }

    public Map<AllocNode, PegChain> getAllocNodeToThread() {
        return this.allocNodeToThread;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Map getThreadNameToStart() {
        return this.threadNameToStart;
    }

    public PegChain getMainPegChain() {
        return (PegChain) this.mainPegChain;
    }

    public Set getMethodsNeedingInlining() {
        return this.methodsNeedingInlining;
    }

    protected void testIterator() {
        System.out.println("********begin test iterator*******");
        Iterator testIt = iterator();
        while (testIt.hasNext()) {
            System.out.println(testIt.next());
        }
        System.out.println("********end test iterator*******");
        System.out.println("=======size is: " + size());
    }

    public void testWaitingNodes() {
        System.out.println("------waiting---begin");
        Set maps = this.waitingNodes.entrySet();
        for (Map.Entry<String, FlowSet> entry : maps) {
            System.out.println("---key=  " + ((Object) entry.getKey()));
            FlowSet<JPegStmt> fs = entry.getValue();
            if (fs.size() > 0) {
                System.out.println("**waiting nodes set:");
                for (JPegStmt unit : fs) {
                    System.out.println(unit.toString());
                }
            }
        }
        System.out.println("------------waitingnodes---ends--------");
    }

    protected void testStartToThread() {
        System.out.println("=====test startToThread ");
        Set maps = this.startToThread.entrySet();
        for (Map.Entry<JPegStmt, List> entry : maps) {
            JPegStmt key = entry.getKey();
            Tag tag = key.getTags().get(0);
            System.out.println("---key=  " + tag + Instruction.argsep + key);
        }
        System.out.println("=========startToThread--ends--------");
    }

    protected void testUnitToPeg(HashMap unitToPeg) {
        System.out.println("=====test unitToPeg ");
        Set<Map.Entry> maps = unitToPeg.entrySet();
        for (Map.Entry entry : maps) {
            System.out.println("---key=  " + entry.getKey());
            JPegStmt s = (JPegStmt) entry.getValue();
            System.out.println("--value= " + s);
        }
        System.out.println("=========unitToPeg--ends--------");
    }

    protected void testUnitToSucc() {
        System.out.println("=====test unitToSucc ");
        Set maps = this.unitToSuccs.entrySet();
        for (Map.Entry<Object, List> entry : maps) {
            JPegStmt key = (JPegStmt) entry.getKey();
            Tag tag = key.getTags().get(0);
            System.out.println("---key=  " + tag + Instruction.argsep + key);
            List<JPegStmt> list = entry.getValue();
            if (list.size() > 0) {
                System.out.println("**succ set: size: " + list.size());
                for (JPegStmt stmt : list) {
                    Tag tag1 = stmt.getTags().get(0);
                    System.out.println(tag1 + Instruction.argsep + stmt);
                }
            }
        }
        System.out.println("=========unitToSucc--ends--------");
    }

    protected void testUnitToPred() {
        System.out.println("=====test unitToPred ");
        Set maps = this.unitToPreds.entrySet();
        for (Map.Entry<Object, List> entry : maps) {
            JPegStmt key = (JPegStmt) entry.getKey();
            Tag tag = key.getTags().get(0);
            System.out.println("---key=  " + tag + Instruction.argsep + key);
            List<JPegStmt> list = entry.getValue();
            System.out.println("**pred set: size: " + list.size());
            for (JPegStmt stmt : list) {
                Tag tag1 = stmt.getTags().get(0);
                System.out.println(tag1 + Instruction.argsep + stmt);
            }
        }
        System.out.println("=========unitToPred--ends--------");
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

    protected void testSynch() {
        System.out.println("========test synch======");
        for (List list : this.synch) {
            System.out.println(list);
        }
        System.out.println("========end test synch======");
    }

    protected void testThreadNameToStart() {
        System.out.println("=====test ThreadNameToStart");
        Set<Map.Entry> maps = this.threadNameToStart.entrySet();
        for (Map.Entry entry : maps) {
            Object key = entry.getKey();
            System.out.println("---key=  " + key);
            JPegStmt stmt = (JPegStmt) entry.getValue();
            Tag tag1 = stmt.getTags().get(0);
            System.out.println("value: " + tag1 + Instruction.argsep + stmt);
        }
        System.out.println("=========ThreadNameToStart--ends--------");
    }

    protected void testJoinStmtToThread() {
        System.out.println("=====test JoinStmtToThread");
        Set<Map.Entry> maps = this.threadNameToStart.entrySet();
        for (Map.Entry entry : maps) {
            Object key = entry.getKey();
            System.out.println("---key=  " + key);
            System.out.println("value: " + entry.getValue());
        }
        System.out.println("=========JoinStmtToThread--ends--------");
    }

    protected void testPegChain(Chain chain) {
        System.out.println("******** chain********");
        Iterator it = chain.iterator();
        while (it.hasNext()) {
            JPegStmt stmt = (JPegStmt) it.next();
            System.out.println(stmt.toString());
        }
    }

    protected void computeEdgeAndThreadNo() {
        Iterator it = iterator();
        int i = 0;
        while (true) {
            int numberOfEdge = i;
            if (it.hasNext()) {
                List succList = getSuccsOf(it.next());
                i = numberOfEdge + succList.size();
            } else {
                System.err.println("**number of edges: " + (numberOfEdge + this.startToThread.size()));
                System.err.println("**number of threads: " + (this.startToThread.size() + 1));
                return;
            }
        }
    }

    protected void testList(List list) {
        for (Object obj : list) {
            System.out.println(obj);
        }
    }

    protected void testSet(Set set, String name) {
        System.out.println("$test set " + name);
        for (Object s : set) {
            System.out.println(s);
        }
    }

    public void testMonitor() {
        System.out.println("=====test monitor size: " + this.monitor.size());
        Set maps = this.monitor.entrySet();
        for (Map.Entry<String, FlowSet> entry : maps) {
            String key = entry.getKey();
            System.out.println("---key=  " + key);
            FlowSet list = entry.getValue();
            if (list.size() > 0) {
                System.out.println("**set:  " + list.size());
                for (Object obj : list) {
                    if (obj instanceof JPegStmt) {
                        JPegStmt stmt = (JPegStmt) obj;
                        Tag tag1 = stmt.getTags().get(0);
                        System.out.println(tag1 + Instruction.argsep + stmt);
                    } else {
                        System.out.println("---list---");
                        for (Object oo : (List) obj) {
                            if (oo instanceof JPegStmt) {
                                JPegStmt unit = (JPegStmt) oo;
                                Tag tag = unit.getTags().get(0);
                                System.out.println(tag + Instruction.argsep + unit);
                            } else {
                                System.out.println(oo);
                            }
                        }
                        System.out.println("---list--end-");
                    }
                }
            }
        }
        System.out.println("=========monitor--ends--------");
    }
}
