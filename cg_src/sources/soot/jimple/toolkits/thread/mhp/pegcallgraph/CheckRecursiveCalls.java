package soot.jimple.toolkits.thread.mhp.pegcallgraph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import soot.jimple.toolkits.thread.mhp.SCC;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/mhp/pegcallgraph/CheckRecursiveCalls.class */
public class CheckRecursiveCalls {
    List<List> newSccList;

    public CheckRecursiveCalls(PegCallGraph pcg, Set<Object> methodNeedExtent) {
        this.newSccList = null;
        Iterator it = pcg.iterator();
        SCC scc = new SCC(it, pcg);
        List<List<Object>> sccList = scc.getSccList();
        this.newSccList = updateScc(sccList, pcg);
        check(this.newSccList, methodNeedExtent);
    }

    private List<List> updateScc(List<List<Object>> sccList, PegCallGraph pcg) {
        List<List> newList = new ArrayList<>();
        for (List s : sccList) {
            if (s.size() == 1) {
                Object o = s.get(0);
                if (pcg.getSuccsOf(o).contains(o) || pcg.getPredsOf(o).contains(o)) {
                    newList.add(s);
                }
            } else {
                newList.add(s);
            }
        }
        return newList;
    }

    private void check(List<List> sccList, Set<Object> methodNeedExtent) {
        for (List s : sccList) {
            if (s.size() > 0) {
                for (Object o : s) {
                    if (methodNeedExtent.contains(o)) {
                        System.err.println("Fail to compute MHP because interested method call relate to recursive calls!");
                        System.err.println("interested method: " + o);
                        throw new RuntimeException("Fail to compute MHP because interested method call relate to recursive calls!");
                    }
                }
                continue;
            }
        }
    }
}
