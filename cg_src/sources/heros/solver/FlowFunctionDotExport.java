package heros.solver;

import com.google.common.collect.Table;
import heros.InterproceduralCFG;
import heros.ItemPrinter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/solver/FlowFunctionDotExport.class */
public class FlowFunctionDotExport<N, D, M, I extends InterproceduralCFG<N, M>> {
    private final IDESolver<N, D, M, ?, I> solver;
    private final ItemPrinter<? super N, ? super D, ? super M> printer;
    private final Set<M> methodWhitelist;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/solver/FlowFunctionDotExport$Numberer.class */
    public static class Numberer<D> {
        long counter;
        Map<D, Long> map;

        private Numberer() {
            this.counter = 1L;
            this.map = new HashMap();
        }

        public void add(D o) {
            if (this.map.containsKey(o)) {
                return;
            }
            Map<D, Long> map = this.map;
            long j = this.counter;
            this.counter = j + 1;
            map.put(o, Long.valueOf(j));
        }

        public long get(D o) {
            if (o == null) {
                throw new IllegalArgumentException("Null key");
            }
            if (!this.map.containsKey(o)) {
                throw new IllegalArgumentException("Failed to find number for: " + o);
            }
            return this.map.get(o).longValue();
        }
    }

    public FlowFunctionDotExport(IDESolver<N, D, M, ?, I> solver, ItemPrinter<? super N, ? super D, ? super M> printer) {
        this(solver, printer, null);
    }

    public FlowFunctionDotExport(IDESolver<N, D, M, ?, I> solver, ItemPrinter<? super N, ? super D, ? super M> printer, Set<M> methodWhitelist) {
        this.solver = solver;
        this.printer = printer;
        this.methodWhitelist = methodWhitelist;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <K, U> Set<U> getOrMakeSet(Map<K, Set<U>> map, K key) {
        if (map.containsKey(key)) {
            return map.get(key);
        }
        HashSet<U> toRet = new HashSet<>();
        map.put(key, toRet);
        return toRet;
    }

    private String escapeLabelString(String toEscape) {
        return toEscape.replace("\\", "\\\\").replace("\"", "\\\"").replace("<", "\\<").replace(">", "\\>");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/solver/FlowFunctionDotExport$UnitFactTracker.class */
    public class UnitFactTracker {
        private Numberer<Pair<N, D>> factNumbers;
        private Numberer<N> unitNumbers;
        private Map<N, Set<D>> factsForUnit;
        private Map<M, Set<N>> methodToUnit;
        private Map<M, Set<N>> stubMethods;
        static final /* synthetic */ boolean $assertionsDisabled;

        private UnitFactTracker() {
            this.factNumbers = new Numberer<>();
            this.unitNumbers = new Numberer<>();
            this.factsForUnit = new HashMap();
            this.methodToUnit = new HashMap();
            this.stubMethods = new HashMap();
        }

        static {
            $assertionsDisabled = !FlowFunctionDotExport.class.desiredAssertionStatus();
        }

        public void registerFactAtUnit(N unit, D fact) {
            FlowFunctionDotExport.getOrMakeSet(this.factsForUnit, unit).add(fact);
            this.factNumbers.add(new Pair<>(unit, fact));
        }

        public void registerUnit(M method, N unit) {
            this.unitNumbers.add(unit);
            FlowFunctionDotExport.getOrMakeSet(this.methodToUnit, method).add(unit);
        }

        public void registerStubUnit(M method, N unit) {
            if (!$assertionsDisabled && this.methodToUnit.containsKey(method)) {
                throw new AssertionError();
            }
            this.unitNumbers.add(unit);
            FlowFunctionDotExport.getOrMakeSet(this.stubMethods, method).add(unit);
        }

        public String getUnitLabel(N unit) {
            return "u" + this.unitNumbers.get(unit);
        }

        public String getFactLabel(N unit, D fact) {
            return "f" + this.factNumbers.get(new Pair<>(unit, fact));
        }

        public String getEdgePoint(N unit, D fact) {
            return getUnitLabel(unit) + ":" + getFactLabel(unit, fact);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void numberEdges(Table<N, N, Map<D, Set<D>>> edgeSet, FlowFunctionDotExport<N, D, M, I>.UnitFactTracker utf) {
        for (Table.Cell<N, N, Map<D, Set<D>>> c : edgeSet.cellSet()) {
            N sourceUnit = c.getRowKey();
            N destUnit = c.getColumnKey();
            Object methodOf = this.solver.icfg.getMethodOf(destUnit);
            Object methodOf2 = this.solver.icfg.getMethodOf(sourceUnit);
            if (!isMethodFiltered(methodOf2) || !isMethodFiltered(methodOf)) {
                if (isMethodFiltered(methodOf)) {
                    utf.registerStubUnit(methodOf, destUnit);
                } else {
                    utf.registerUnit(methodOf, destUnit);
                }
                if (isMethodFiltered(methodOf2)) {
                    utf.registerStubUnit(methodOf2, sourceUnit);
                } else {
                    utf.registerUnit(methodOf2, sourceUnit);
                }
                for (Map.Entry<D, Set<D>> entry : c.getValue().entrySet()) {
                    utf.registerFactAtUnit(sourceUnit, entry.getKey());
                    for (D destFact : entry.getValue()) {
                        utf.registerFactAtUnit(destUnit, destFact);
                    }
                }
            }
        }
    }

    private boolean isMethodFiltered(M method) {
        return (this.methodWhitelist == null || this.methodWhitelist.contains(method)) ? false : true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private boolean isNodeFiltered(N node) {
        return isMethodFiltered(this.solver.icfg.getMethodOf(node));
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void printMethodUnits(Set<N> units, M method, PrintStream pf, FlowFunctionDotExport<N, D, M, I>.UnitFactTracker utf) {
        for (N methodUnit : units) {
            Set<D> loc = (Set) ((UnitFactTracker) utf).factsForUnit.get(methodUnit);
            String unitText = escapeLabelString(this.printer.printNode(methodUnit, method));
            pf.print(utf.getUnitLabel(methodUnit) + " [shape=record,label=\"" + unitText + Instruction.argsep);
            if (loc != null) {
                for (Object obj : loc) {
                    pf.print("| <" + utf.getFactLabel(methodUnit, obj) + "> " + escapeLabelString(this.printer.printFact(obj)));
                }
            }
            pf.println("\"];");
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void dumpDotFile(String fileName) {
        File f = new File(fileName);
        PrintStream pf = null;
        try {
            try {
                pf = new PrintStream(f);
                UnitFactTracker unitFactTracker = new UnitFactTracker();
                numberEdges(this.solver.computedIntraPEdges, unitFactTracker);
                numberEdges(this.solver.computedInterPEdges, unitFactTracker);
                pf.println("digraph ifds {node[shape=record];");
                int methodCounter = 0;
                for (Map.Entry<M, Set<N>> kv : unitFactTracker.methodToUnit.entrySet()) {
                    Set<N> intraProc = kv.getValue();
                    pf.println("subgraph cluster" + methodCounter + " {");
                    methodCounter++;
                    printMethodUnits(intraProc, kv.getKey(), pf, unitFactTracker);
                    for (N methodUnit : intraProc) {
                        Map<N, Map<D, Set<D>>> flows = this.solver.computedIntraPEdges.row(methodUnit);
                        for (Map.Entry<N, Map<D, Set<D>>> kv2 : flows.entrySet()) {
                            N destUnit = kv2.getKey();
                            for (Map.Entry<D, Set<D>> pointFlow : kv2.getValue().entrySet()) {
                                for (D destFact : pointFlow.getValue()) {
                                    String edge = unitFactTracker.getEdgePoint(methodUnit, pointFlow.getKey()) + " -> " + unitFactTracker.getEdgePoint(destUnit, destFact);
                                    pf.print(edge);
                                    pf.println(";");
                                }
                            }
                        }
                    }
                    pf.println("label=\"" + escapeLabelString(this.printer.printMethod((Object) kv.getKey())) + "\";");
                    pf.println("}");
                }
                for (Map.Entry<M, Set<N>> kv3 : unitFactTracker.stubMethods.entrySet()) {
                    int i = methodCounter;
                    methodCounter++;
                    pf.println("subgraph cluster" + i + " {");
                    printMethodUnits(kv3.getValue(), kv3.getKey(), pf, unitFactTracker);
                    pf.println("label=\"" + escapeLabelString("[STUB] " + this.printer.printMethod((Object) kv3.getKey())) + "\";");
                    pf.println("graph[style=dotted];");
                    pf.println("}");
                }
                for (Table.Cell<N, N, Map<D, Set<D>>> c : this.solver.computedInterPEdges.cellSet()) {
                    if (!isNodeFiltered(c.getRowKey()) || !isNodeFiltered(c.getColumnKey())) {
                        for (Map.Entry<D, Set<D>> kv4 : c.getValue().entrySet()) {
                            for (D dFact : kv4.getValue()) {
                                pf.print(unitFactTracker.getEdgePoint(c.getRowKey(), kv4.getKey()));
                                pf.print(" -> ");
                                pf.print(unitFactTracker.getEdgePoint(c.getColumnKey(), dFact));
                                pf.println(" [style=dotted];");
                            }
                        }
                    }
                }
                pf.println("}");
                if (pf != null) {
                    pf.close();
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException("Writing dot output failed", e);
            }
        } catch (Throwable th) {
            if (pf != null) {
                pf.close();
            }
            throw th;
        }
    }
}
