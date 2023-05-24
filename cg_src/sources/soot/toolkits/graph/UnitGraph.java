package soot.toolkits.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.SootMethod;
import soot.Unit;
import soot.UnitBox;
import soot.options.Options;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/UnitGraph.class */
public abstract class UnitGraph implements DirectedBodyGraph<Unit> {
    private static final Logger logger = LoggerFactory.getLogger(UnitGraph.class);
    protected final Body body;
    protected final Chain<Unit> unitChain;
    protected final SootMethod method;
    protected List<Unit> heads;
    protected List<Unit> tails;
    protected Map<Unit, List<Unit>> unitToSuccs;
    protected Map<Unit, List<Unit>> unitToPreds;

    /* JADX INFO: Access modifiers changed from: protected */
    public UnitGraph(Body body) {
        this.body = body;
        this.unitChain = body.getUnits();
        this.method = body.getMethod();
        if (Options.v().verbose()) {
            logger.debug("[" + this.method.getName() + "]     Constructing " + getClass().getName() + "...");
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void buildUnexceptionalEdges(Map<Unit, List<Unit>> unitToSuccs, Map<Unit, List<Unit>> unitToPreds) {
        Iterator<Unit> unitIt = this.unitChain.iterator();
        Unit nextUnit = unitIt.hasNext() ? unitIt.next() : null;
        while (nextUnit != null) {
            Unit currentUnit = nextUnit;
            nextUnit = unitIt.hasNext() ? unitIt.next() : null;
            ArrayList<Unit> successors = new ArrayList<>();
            if (currentUnit.fallsThrough() && nextUnit != null) {
                successors.add(nextUnit);
                List<Unit> preds = unitToPreds.get(nextUnit);
                List<Unit> preds2 = preds;
                if (preds == null) {
                    List<Unit> preds3 = new ArrayList<>();
                    unitToPreds.put(nextUnit, preds3);
                    preds2 = preds3;
                }
                preds2.add(currentUnit);
            }
            if (currentUnit.branches()) {
                for (UnitBox targetBox : currentUnit.getUnitBoxes()) {
                    Unit target = targetBox.getUnit();
                    if (!successors.contains(target)) {
                        successors.add(target);
                        List<Unit> preds4 = unitToPreds.get(target);
                        List<Unit> preds5 = preds4;
                        if (preds4 == null) {
                            List<Unit> preds6 = new ArrayList<>();
                            unitToPreds.put(target, preds6);
                            preds5 = preds6;
                        }
                        preds5.add(currentUnit);
                    }
                }
            }
            if (!successors.isEmpty()) {
                successors.trimToSize();
                unitToSuccs.put(currentUnit, successors);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void buildHeadsAndTails() {
        this.tails = new ArrayList();
        this.heads = new ArrayList();
        Unit entryPoint = null;
        if (!this.unitChain.isEmpty()) {
            entryPoint = this.unitChain.getFirst();
        }
        boolean hasEntryPoint = false;
        for (Unit s : this.unitChain) {
            List<Unit> succs = this.unitToSuccs.get(s);
            if (succs == null || succs.isEmpty()) {
                this.tails.add(s);
            }
            List<Unit> preds = this.unitToPreds.get(s);
            if (preds == null || preds.isEmpty()) {
                if (s == entryPoint) {
                    hasEntryPoint = true;
                }
                this.heads.add(s);
            }
        }
        if (entryPoint != null && !hasEntryPoint) {
            this.heads.add(entryPoint);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Map<Unit, List<Unit>> combineMapValues(Map<Unit, List<Unit>> mapA, Map<Unit, List<Unit>> mapB) {
        List<Unit> list;
        Map<Unit, List<Unit>> result = new LinkedHashMap<>((mapA.size() * 2) + 1, 0.7f);
        for (Unit unit : this.unitChain) {
            List<Unit> listA = mapA.get(unit);
            if (listA == null) {
                listA = Collections.emptyList();
            }
            List<Unit> listB = mapB.get(unit);
            if (listB == null) {
                listB = Collections.emptyList();
            }
            int resultSize = listA.size() + listB.size();
            if (resultSize == 0) {
                result.put(unit, Collections.emptyList());
            } else {
                List<Unit> resultList = new ArrayList<>(resultSize);
                if (listA.size() >= listB.size()) {
                    resultList.addAll(listA);
                    list = listB;
                } else {
                    resultList.addAll(listB);
                    list = listA;
                }
                for (Unit element : list) {
                    if (!resultList.contains(element)) {
                        resultList.add(element);
                    }
                }
                result.put(unit, resultList);
            }
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void addEdge(Map<Unit, List<Unit>> unitToSuccs, Map<Unit, List<Unit>> unitToPreds, Unit head, Unit tail) {
        List<Unit> headsSuccs = unitToSuccs.get(head);
        if (headsSuccs == null) {
            headsSuccs = new ArrayList<>(3);
            unitToSuccs.put(head, headsSuccs);
        }
        if (!headsSuccs.contains(tail)) {
            headsSuccs.add(tail);
            List<Unit> tailsPreds = unitToPreds.get(tail);
            if (tailsPreds == null) {
                tailsPreds = new ArrayList<>();
                unitToPreds.put(tail, tailsPreds);
            }
            tailsPreds.add(head);
        }
    }

    @Override // soot.toolkits.graph.DirectedBodyGraph
    public Body getBody() {
        return this.body;
    }

    public List<Unit> getExtendedBasicBlockPathBetween(Unit from, Unit to) {
        if (getPredsOf(to).size() > 1) {
            return null;
        }
        LinkedList<Unit> pathStack = new LinkedList<>();
        LinkedList<Integer> pathStackIndex = new LinkedList<>();
        pathStack.add(from);
        pathStackIndex.add(0);
        int psiMax = getSuccsOf(from).size();
        int level = 0;
        while (pathStackIndex.get(0).intValue() != psiMax) {
            int p = pathStackIndex.get(level).intValue();
            List<Unit> succs = getSuccsOf(pathStack.get(level));
            if (p >= succs.size()) {
                pathStack.remove(level);
                pathStackIndex.remove(level);
                level--;
                int q = pathStackIndex.get(level).intValue();
                pathStackIndex.set(level, Integer.valueOf(q + 1));
            } else {
                Unit betweenUnit = succs.get(p);
                if (betweenUnit == to) {
                    pathStack.add(to);
                    return pathStack;
                } else if (getPredsOf(betweenUnit).size() > 1) {
                    pathStackIndex.set(level, Integer.valueOf(p + 1));
                } else {
                    level++;
                    pathStackIndex.add(0);
                    pathStack.add(betweenUnit);
                }
            }
        }
        return null;
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public List<Unit> getHeads() {
        return this.heads;
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public List<Unit> getTails() {
        return this.tails;
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public List<Unit> getPredsOf(Unit u) {
        List<Unit> l = this.unitToPreds.get(u);
        return l == null ? Collections.emptyList() : l;
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public List<Unit> getSuccsOf(Unit u) {
        List<Unit> l = this.unitToSuccs.get(u);
        return l == null ? Collections.emptyList() : l;
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public int size() {
        return this.unitChain.size();
    }

    @Override // soot.toolkits.graph.DirectedGraph, java.lang.Iterable
    public Iterator<Unit> iterator() {
        return this.unitChain.iterator();
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        for (Unit u : this.unitChain) {
            buf.append("// preds: ").append(getPredsOf(u)).append('\n');
            buf.append(u).append('\n');
            buf.append("// succs ").append(getSuccsOf(u)).append('\n');
        }
        return buf.toString();
    }
}
