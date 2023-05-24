package soot.jimple.toolkits.thread.mhp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.jimple.toolkits.thread.mhp.stmt.JPegStmt;
import soot.toolkits.scalar.FlowSet;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/mhp/CompactStronglyConnectedComponents.class */
public class CompactStronglyConnectedComponents {
    long compactNodes = 0;
    long add = 0;

    public CompactStronglyConnectedComponents(PegGraph pg) {
        Chain mainPegChain = pg.getMainPegChain();
        compactGraph(mainPegChain, pg);
        compactStartChain(pg);
        System.err.println("compact SCC nodes: " + this.compactNodes);
        System.err.println(" number of compacting scc nodes: " + this.add);
    }

    private void compactGraph(Chain chain, PegGraph peg) {
        Set canNotBeCompacted = peg.getCanNotBeCompacted();
        SCC scc = new SCC(chain.iterator(), peg);
        List<List<Object>> sccList = scc.getSccList();
        for (List s : sccList) {
            if (s.size() > 1 && !checkIfContainsElemsCanNotBeCompacted(s, canNotBeCompacted)) {
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

    private boolean checkIfContainsElemsCanNotBeCompacted(List list, Set canNotBeCompacted) {
        Iterator sccIt = list.iterator();
        while (sccIt.hasNext()) {
            JPegStmt node = (JPegStmt) sccIt.next();
            if (canNotBeCompacted.contains(node)) {
                return true;
            }
        }
        return false;
    }

    private void compact(List list, Chain chain, PegGraph peg) {
        Iterator it = list.iterator();
        FlowSet allNodes = peg.getAllNodes();
        HashMap unitToSuccs = peg.getUnitToSuccs();
        HashMap unitToPreds = peg.getUnitToPreds();
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        while (it.hasNext()) {
            JPegStmt s = (JPegStmt) it.next();
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
        Iterator it2 = list.iterator();
        while (it2.hasNext()) {
            JPegStmt s2 = (JPegStmt) it2.next();
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
