package soot.toolkits.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.Body;
import soot.Trap;
import soot.Unit;
import soot.toolkits.exceptions.ThrowAnalysis;
import soot.toolkits.exceptions.ThrowableSet;
import soot.toolkits.graph.ExceptionalGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.util.PhaseDumper;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/ExceptionalBlockGraph.class */
public class ExceptionalBlockGraph extends BlockGraph implements ExceptionalGraph<Block> {
    protected Map<Block, List<Block>> blockToExceptionalPreds;
    protected Map<Block, List<Block>> blockToExceptionalSuccs;
    protected Map<Block, List<Block>> blockToUnexceptionalPreds;
    protected Map<Block, List<Block>> blockToUnexceptionalSuccs;
    protected Map<Block, Collection<ExceptionDest>> blockToExceptionDests;
    protected ThrowAnalysis throwAnalysis;

    public ExceptionalBlockGraph(Body body) {
        this(ExceptionalUnitGraphFactory.createExceptionalUnitGraph(body));
    }

    public ExceptionalBlockGraph(ExceptionalUnitGraph unitGraph) {
        super(unitGraph);
        PhaseDumper.v().dumpGraph(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.graph.BlockGraph
    public Map<Unit, Block> buildBlocks(Set<Unit> leaders, UnitGraph uncastUnitGraph) {
        ExceptionalUnitGraph unitGraph = (ExceptionalUnitGraph) uncastUnitGraph;
        Map<Unit, Block> unitToBlock = super.buildBlocks(leaders, unitGraph);
        if (unitGraph.getBody().getTraps().isEmpty()) {
            this.throwAnalysis = unitGraph.getThrowAnalysis();
            if (this.throwAnalysis == null) {
                throw new IllegalStateException("ExceptionalUnitGraph lacked a cached ThrowAnalysis for a Body with no Traps.");
            }
        } else {
            int initialMapSize = (this.mBlocks.size() * 2) / 3;
            this.blockToUnexceptionalPreds = new HashMap(initialMapSize);
            this.blockToUnexceptionalSuccs = new HashMap(initialMapSize);
            this.blockToExceptionalPreds = new HashMap(initialMapSize);
            this.blockToExceptionalSuccs = new HashMap(initialMapSize);
            for (Block block : this.mBlocks) {
                Unit blockHead = block.getHead();
                List<Unit> exceptionalPredUnits = unitGraph.getExceptionalPredsOf(blockHead);
                if (!exceptionalPredUnits.isEmpty()) {
                    List<Block> exceptionalPreds = Collections.unmodifiableList(mappedValues(exceptionalPredUnits, unitToBlock));
                    this.blockToExceptionalPreds.put(block, exceptionalPreds);
                    List<Unit> unexceptionalPredUnits = unitGraph.getUnexceptionalPredsOf(blockHead);
                    List<Block> unexceptionalPreds = unexceptionalPredUnits.isEmpty() ? Collections.emptyList() : Collections.unmodifiableList(mappedValues(unexceptionalPredUnits, unitToBlock));
                    this.blockToUnexceptionalPreds.put(block, unexceptionalPreds);
                }
                Unit blockTail = block.getTail();
                List<Unit> exceptionalSuccUnits = unitGraph.getExceptionalSuccsOf(blockTail);
                if (!exceptionalSuccUnits.isEmpty()) {
                    List<Block> exceptionalSuccs = Collections.unmodifiableList(mappedValues(exceptionalSuccUnits, unitToBlock));
                    this.blockToExceptionalSuccs.put(block, exceptionalSuccs);
                    List<Unit> unexceptionalSuccUnits = unitGraph.getUnexceptionalSuccsOf(blockTail);
                    List<Block> unexceptionalSuccs = unexceptionalSuccUnits.isEmpty() ? Collections.emptyList() : Collections.unmodifiableList(mappedValues(unexceptionalSuccUnits, unitToBlock));
                    this.blockToUnexceptionalSuccs.put(block, unexceptionalSuccs);
                }
            }
            this.blockToExceptionDests = buildExceptionDests(unitGraph, unitToBlock);
        }
        return unitToBlock;
    }

    private static <K, V> List<V> mappedValues(List<K> keys, Map<K, V> keyToValue) {
        ArrayList<V> result = new ArrayList<>(keys.size());
        for (K key : keys) {
            V value = keyToValue.get(key);
            if (value == null) {
                throw new IllegalStateException("No value corresponding to key: " + key);
            }
            result.add(value);
        }
        result.trimToSize();
        return result;
    }

    private Map<Block, Collection<ExceptionDest>> buildExceptionDests(ExceptionalUnitGraph unitGraph, Map<Unit, Block> unitToBlock) {
        Map<Block, Collection<ExceptionDest>> result = new HashMap<>((this.mBlocks.size() * 2) + 1, 0.7f);
        for (Block block : this.mBlocks) {
            result.put(block, collectDests(block, unitGraph, unitToBlock));
        }
        return result;
    }

    private Collection<ExceptionDest> collectDests(Block block, ExceptionalUnitGraph unitGraph, Map<Unit, Block> unitToBlock) {
        ThrowableSet throwables;
        Unit blockHead = block.getHead();
        Unit blockTail = block.getTail();
        ThrowableSet emptyThrowables = ThrowableSet.Manager.v().EMPTY;
        ThrowableSet escapingThrowables = emptyThrowables;
        ArrayList<ExceptionDest> blocksDests = null;
        Map<Trap, ThrowableSet> trapToThrowables = null;
        int caughtCount = 0;
        Iterator<Unit> it = block.iterator();
        while (it.hasNext()) {
            Unit unit = it.next();
            Collection<ExceptionalUnitGraph.ExceptionDest> unitDests = unitGraph.getExceptionDests(unit);
            if (unitDests.size() != 1 && unit != blockHead && unit != blockTail) {
                throw new IllegalStateException("Multiple ExceptionDests associated with a unit which does not begin or end its block.");
            }
            for (ExceptionalUnitGraph.ExceptionDest unitDest : unitDests) {
                if (unitDest.getTrap() == null) {
                    try {
                        escapingThrowables = escapingThrowables.add(unitDest.getThrowables());
                    } catch (ThrowableSet.AlreadyHasExclusionsException e) {
                        if (escapingThrowables != emptyThrowables) {
                            if (blocksDests == null) {
                                blocksDests = new ArrayList<>(10);
                            }
                            blocksDests.add(new ExceptionDest(null, escapingThrowables, null));
                        }
                        escapingThrowables = unitDest.getThrowables();
                    }
                } else if (unit != blockHead && unit != blockTail) {
                    throw new IllegalStateException("Unit " + unit.toString() + " is not a block head or tail, yet it throws " + unitDest.getThrowables() + " to " + unitDest.getTrap());
                } else {
                    caughtCount++;
                    if (trapToThrowables == null) {
                        trapToThrowables = new HashMap<>(unitDests.size() * 2);
                    }
                    Trap trap = unitDest.getTrap();
                    ThrowableSet throwables2 = trapToThrowables.get(trap);
                    if (throwables2 == null) {
                        throwables = unitDest.getThrowables();
                    } else {
                        throwables = throwables2.add(unitDest.getThrowables());
                    }
                    trapToThrowables.put(trap, throwables);
                }
            }
        }
        if (blocksDests == null) {
            blocksDests = new ArrayList<>(caughtCount + 1);
        } else {
            blocksDests.ensureCapacity(blocksDests.size() + caughtCount);
        }
        if (escapingThrowables != emptyThrowables) {
            blocksDests.add(new ExceptionDest(null, escapingThrowables, null));
        }
        if (trapToThrowables != null) {
            for (Map.Entry<Trap, ThrowableSet> entry : trapToThrowables.entrySet()) {
                Trap trap2 = entry.getKey();
                Block trapBlock = unitToBlock.get(trap2.getHandlerUnit());
                if (trapBlock == null) {
                    throw new IllegalStateException("catching unit is not recorded as a block leader.");
                }
                blocksDests.add(new ExceptionDest(trap2, entry.getValue(), trapBlock));
            }
        }
        blocksDests.trimToSize();
        return blocksDests;
    }

    @Override // soot.toolkits.graph.ExceptionalGraph
    public List<Block> getUnexceptionalPredsOf(Block b) {
        if (this.blockToUnexceptionalPreds == null || !this.blockToUnexceptionalPreds.containsKey(b)) {
            return b.getPreds();
        }
        return this.blockToUnexceptionalPreds.get(b);
    }

    @Override // soot.toolkits.graph.ExceptionalGraph
    public List<Block> getUnexceptionalSuccsOf(Block b) {
        if (this.blockToUnexceptionalSuccs == null || !this.blockToUnexceptionalSuccs.containsKey(b)) {
            return b.getSuccs();
        }
        return this.blockToUnexceptionalSuccs.get(b);
    }

    @Override // soot.toolkits.graph.ExceptionalGraph
    public List<Block> getExceptionalPredsOf(Block b) {
        if (this.blockToExceptionalPreds == null || !this.blockToExceptionalPreds.containsKey(b)) {
            return Collections.emptyList();
        }
        return this.blockToExceptionalPreds.get(b);
    }

    @Override // soot.toolkits.graph.ExceptionalGraph
    public List<Block> getExceptionalSuccsOf(Block b) {
        if (this.blockToExceptionalSuccs == null || !this.blockToExceptionalSuccs.containsKey(b)) {
            return Collections.emptyList();
        }
        return this.blockToExceptionalSuccs.get(b);
    }

    @Override // soot.toolkits.graph.ExceptionalGraph
    public Collection<ExceptionDest> getExceptionDests(final Block b) {
        if (this.blockToExceptionDests == null) {
            ExceptionDest e = new ExceptionDest(null, null, null) { // from class: soot.toolkits.graph.ExceptionalBlockGraph.1
                @Override // soot.toolkits.graph.ExceptionalBlockGraph.ExceptionDest, soot.toolkits.graph.ExceptionalGraph.ExceptionDest
                public ThrowableSet getThrowables() {
                    if (this.throwables == null) {
                        this.throwables = ThrowableSet.Manager.v().EMPTY;
                        Iterator<Unit> it = b.iterator();
                        while (it.hasNext()) {
                            Unit unit = it.next();
                            this.throwables = this.throwables.add(ExceptionalBlockGraph.this.throwAnalysis.mightThrow(unit));
                        }
                    }
                    return this.throwables;
                }
            };
            return Collections.singletonList(e);
        }
        return this.blockToExceptionDests.get(b);
    }

    /* loaded from: gencallgraphv3.jar:soot/toolkits/graph/ExceptionalBlockGraph$ExceptionDest.class */
    public static class ExceptionDest implements ExceptionalGraph.ExceptionDest<Block> {
        private final Trap trap;
        private final Block handler;
        protected ThrowableSet throwables;

        protected ExceptionDest(Trap trap, ThrowableSet throwables, Block handler) {
            this.trap = trap;
            this.throwables = throwables;
            this.handler = handler;
        }

        @Override // soot.toolkits.graph.ExceptionalGraph.ExceptionDest
        public Trap getTrap() {
            return this.trap;
        }

        @Override // soot.toolkits.graph.ExceptionalGraph.ExceptionDest
        public ThrowableSet getThrowables() {
            return this.throwables;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // soot.toolkits.graph.ExceptionalGraph.ExceptionDest
        public Block getHandlerNode() {
            return this.handler;
        }

        public String toString() {
            StringBuilder buf = new StringBuilder();
            buf.append(getThrowables());
            buf.append(" -> ");
            if (this.trap == null) {
                buf.append("(escapes)");
            } else {
                buf.append(this.trap.toString());
                buf.append("handler: ");
                buf.append(getHandlerNode().toString());
            }
            return buf.toString();
        }
    }
}
