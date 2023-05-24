package soot.jimple.toolkits.thread.mhp.pegcallgraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.toolkits.graph.DirectedGraph;
import soot.util.Chain;
import soot.util.HashChain;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/mhp/pegcallgraph/PegCallGraph.class */
public class PegCallGraph implements DirectedGraph {
    private final Set clinitMethods = new HashSet();
    Chain chain = new HashChain();
    List heads = new ArrayList();
    List tails = new ArrayList();
    private final Map<Object, List> methodToSuccs = new HashMap();
    private final Map<Object, List> methodToPreds = new HashMap();
    private final Map<Object, List> methodToSuccsTrim = new HashMap();

    public PegCallGraph(CallGraph cg) {
        buildChainAndSuccs(cg);
        buildPreds();
    }

    protected void testChain() {
        System.out.println("******** chain of pegcallgraph********");
        for (SootMethod sm : this.chain) {
            System.out.println(sm);
        }
    }

    public Set getClinitMethods() {
        return this.clinitMethods;
    }

    private void buildChainAndSuccs(CallGraph cg) {
        Iterator it = cg.sourceMethods();
        while (it.hasNext()) {
            SootMethod sm = (SootMethod) it.next();
            if (sm.getName().equals("main")) {
                this.heads.add(sm);
            }
            if (sm.isConcrete() && sm.getDeclaringClass().isApplicationClass()) {
                if (!this.chain.contains(sm)) {
                    this.chain.add(sm);
                }
                List succsList = new ArrayList();
                Iterator edgeIt = cg.edgesOutOf(sm);
                while (edgeIt.hasNext()) {
                    Edge edge = edgeIt.next();
                    SootMethod target = edge.tgt();
                    if (target.isConcrete() && target.getDeclaringClass().isApplicationClass()) {
                        succsList.add(target);
                        if (!this.chain.contains(target)) {
                            this.chain.add(target);
                        }
                        if (edge.isClinit()) {
                            this.clinitMethods.add(target);
                        }
                    }
                }
                if (succsList.size() > 0) {
                    this.methodToSuccs.put(sm, succsList);
                }
            }
        }
        for (SootMethod sm2 : this.chain) {
            if (!this.methodToSuccs.containsKey(sm2)) {
                this.methodToSuccs.put(sm2, new ArrayList());
            }
        }
        Iterator chainIt = this.chain.iterator();
        while (it.hasNext()) {
            SootMethod s = (SootMethod) chainIt.next();
            if (this.methodToSuccs.containsKey(s)) {
                List succList = this.methodToSuccs.get(s);
                succList.size();
            }
        }
        for (SootMethod s2 : this.chain) {
            if (this.methodToSuccs.containsKey(s2)) {
                this.methodToSuccs.put(s2, Collections.unmodifiableList(this.methodToSuccs.get(s2)));
            }
        }
    }

    private void buildPreds() {
        for (Object obj : this.chain) {
            this.methodToPreds.put(obj, new ArrayList());
        }
        for (Object s : this.chain) {
            List succList = this.methodToSuccs.get(s);
            if (succList.size() > 0) {
                for (Object successor : succList) {
                    List<Object> predList = this.methodToPreds.get(successor);
                    try {
                        predList.add(s);
                    } catch (NullPointerException e) {
                        System.out.println(s + "successor: " + successor);
                        throw e;
                    }
                }
                continue;
            }
        }
        for (SootMethod s2 : this.chain) {
            if (this.methodToPreds.containsKey(s2)) {
                List predList2 = this.methodToPreds.get(s2);
                this.methodToPreds.put(s2, Collections.unmodifiableList(predList2));
            }
        }
    }

    public void trim() {
        Set maps = this.methodToSuccs.entrySet();
        for (Map.Entry<Object, List> entry : maps) {
            List list = entry.getValue();
            List<Object> newList = new ArrayList<>();
            for (Object obj : list) {
                if (!list.contains(obj)) {
                    newList.add(obj);
                }
            }
            this.methodToSuccsTrim.put(entry.getKey(), newList);
        }
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public List getHeads() {
        return this.heads;
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public List getTails() {
        return this.tails;
    }

    public List getTrimSuccsOf(Object s) {
        if (!this.methodToSuccsTrim.containsKey(s)) {
            return Collections.EMPTY_LIST;
        }
        return this.methodToSuccsTrim.get(s);
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public List getSuccsOf(Object s) {
        if (!this.methodToSuccs.containsKey(s)) {
            return Collections.EMPTY_LIST;
        }
        return this.methodToSuccs.get(s);
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public List getPredsOf(Object s) {
        if (!this.methodToPreds.containsKey(s)) {
            return Collections.EMPTY_LIST;
        }
        return this.methodToPreds.get(s);
    }

    @Override // soot.toolkits.graph.DirectedGraph, java.lang.Iterable
    public Iterator iterator() {
        return this.chain.iterator();
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public int size() {
        return this.chain.size();
    }

    protected void testMethodToSucc() {
        System.out.println("=====test methodToSucc ");
        Set maps = this.methodToSuccs.entrySet();
        for (Map.Entry<Object, List> entry : maps) {
            System.out.println("---key=  " + entry.getKey());
            List<Object> list = entry.getValue();
            if (list.size() > 0) {
                System.out.println("**succ set:");
                for (Object obj : list) {
                    System.out.println(obj);
                }
            }
        }
        System.out.println("=========methodToSucc--ends--------");
    }

    protected void testMethodToPred() {
        System.out.println("=====test methodToPred ");
        Set maps = this.methodToPreds.entrySet();
        for (Map.Entry<Object, List> entry : maps) {
            System.out.println("---key=  " + entry.getKey());
            List<Object> list = entry.getValue();
            if (list.size() > 0) {
                System.out.println("**pred set:");
                for (Object obj : list) {
                    System.out.println(obj);
                }
            }
        }
        System.out.println("=========methodToPred--ends--------");
    }
}
