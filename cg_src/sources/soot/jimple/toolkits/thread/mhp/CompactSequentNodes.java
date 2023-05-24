package soot.jimple.toolkits.thread.mhp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import soot.jimple.toolkits.thread.mhp.stmt.JPegStmt;
import soot.toolkits.scalar.FlowSet;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/mhp/CompactSequentNodes.class */
public class CompactSequentNodes {
    long compactNodes = 0;
    long add = 0;

    public CompactSequentNodes(PegGraph pg) {
        Chain mainPegChain = pg.getMainPegChain();
        compactGraph(mainPegChain, pg);
        compactStartChain(pg);
        System.err.println("compact seq. node: " + this.compactNodes);
        System.err.println("number of compacting seq. nodes: " + this.add);
    }

    private void compactGraph(Chain chain, PegGraph peg) {
        Set canNotBeCompacted = peg.getCanNotBeCompacted();
        List<List<Object>> list = computeSequentNodes(chain, peg);
        for (List s : list) {
            if (!checkIfContainsElemsCanNotBeCompacted(s, canNotBeCompacted)) {
                this.add++;
                compact(s, chain, peg);
            }
        }
    }

    private void compactStartChain(PegGraph graph) {
        Set maps = graph.getStartToThread().entrySet();
        for (Map.Entry<JPegStmt, List> entry : maps) {
            List<Chain> runMethodChainList = entry.getValue();
            for (Chain chain : runMethodChainList) {
                compactGraph(chain, graph);
            }
        }
    }

    private List<List<Object>> computeSequentNodes(Chain chain, PegGraph pg) {
        Set<Object> gray = new HashSet<>();
        List<List<Object>> sequentNodes = new ArrayList<>();
        Set canNotBeCompacted = pg.getCanNotBeCompacted();
        TopologicalSorter ts = new TopologicalSorter(chain, pg);
        ListIterator<Object> it = ts.sorter().listIterator();
        while (it.hasNext()) {
            Object node = it.next();
            List<Object> list = new ArrayList<>();
            if (!gray.contains(node)) {
                visitNode(pg, node, list, canNotBeCompacted, gray);
                if (list.size() > 1) {
                    gray.addAll(list);
                    sequentNodes.add(list);
                }
            }
        }
        return sequentNodes;
    }

    private void visitNode(PegGraph pg, Object node, List<Object> list, Set canNotBeCompacted, Set<Object> gray) {
        if (pg.getPredsOf(node).size() == 1 && pg.getSuccsOf(node).size() == 1 && !canNotBeCompacted.contains(node) && !gray.contains(node)) {
            list.add(node);
            for (Object o : pg.getSuccsOf(node)) {
                visitNode(pg, o, list, canNotBeCompacted, gray);
            }
        }
    }

    private boolean checkIfContainsElemsCanNotBeCompacted(List list, Set canNotBeCompacted) {
        for (Object node : list) {
            if (canNotBeCompacted.contains(node)) {
                return true;
            }
        }
        return false;
    }

    private void compact(List list, Chain chain, PegGraph peg) {
        FlowSet allNodes = peg.getAllNodes();
        HashMap unitToSuccs = peg.getUnitToSuccs();
        HashMap unitToPreds = peg.getUnitToPreds();
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (Object s : list) {
            for (Object pred : peg.getPredsOf(s)) {
                List succsOfPred = peg.getSuccsOf(pred);
                succsOfPred.remove(s);
                if (!list.contains(pred)) {
                    arrayList.add(pred);
                    succsOfPred.add(list);
                }
            }
            for (Object succ : peg.getSuccsOf(s)) {
                List predsOfSucc = peg.getPredsOf(succ);
                predsOfSucc.remove(s);
                if (!list.contains(succ)) {
                    arrayList2.add(succ);
                    predsOfSucc.add(list);
                }
            }
        }
        unitToSuccs.put(list, arrayList2);
        unitToPreds.put(list, arrayList);
        allNodes.add(list);
        chain.add(list);
        updateMonitor(peg, list);
        for (Object s2 : list) {
            chain.remove(s2);
            allNodes.remove(s2);
            unitToSuccs.remove(s2);
            unitToPreds.remove(s2);
        }
        this.compactNodes += list.size();
    }

    private void updateMonitor(PegGraph pg, List list) {
        Set maps = pg.getMonitor().entrySet();
        for (Map.Entry<String, FlowSet> entry : maps) {
            FlowSet fs = entry.getValue();
            Iterator it = list.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Object obj = it.next();
                if (fs.contains(obj)) {
                    fs.add(list);
                    break;
                }
            }
        }
    }
}
