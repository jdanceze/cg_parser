package heros.solver;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import heros.DontSynchronize;
import heros.EdgeFunction;
import heros.SynchronizedBy;
import heros.ThreadSafe;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
@ThreadSafe
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/solver/JumpFunctions.class */
public class JumpFunctions<N, D, L> {
    @SynchronizedBy("consistent lock on this")
    protected Table<N, D, Map<D, EdgeFunction<L>>> nonEmptyReverseLookup = HashBasedTable.create();
    @SynchronizedBy("consistent lock on this")
    protected Table<D, N, Map<D, EdgeFunction<L>>> nonEmptyForwardLookup = HashBasedTable.create();
    @SynchronizedBy("consistent lock on this")
    protected Map<N, Table<D, D, EdgeFunction<L>>> nonEmptyLookupByTargetNode = new LinkedHashMap();
    @DontSynchronize("immutable")
    private final EdgeFunction<L> allTop;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !JumpFunctions.class.desiredAssertionStatus();
    }

    public JumpFunctions(EdgeFunction<L> allTop) {
        this.allTop = allTop;
    }

    public synchronized void addFunction(D sourceVal, N target, D targetVal, EdgeFunction<L> function) {
        if (!$assertionsDisabled && sourceVal == null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && target == null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && targetVal == null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && function == null) {
            throw new AssertionError();
        }
        if (function.equalTo(this.allTop)) {
            return;
        }
        Map<D, EdgeFunction<L>> sourceValToFunc = this.nonEmptyReverseLookup.get(target, targetVal);
        if (sourceValToFunc == null) {
            sourceValToFunc = new LinkedHashMap<>();
            this.nonEmptyReverseLookup.put(target, targetVal, sourceValToFunc);
        }
        sourceValToFunc.put(sourceVal, function);
        Map<D, EdgeFunction<L>> targetValToFunc = this.nonEmptyForwardLookup.get(sourceVal, target);
        if (targetValToFunc == null) {
            targetValToFunc = new LinkedHashMap<>();
            this.nonEmptyForwardLookup.put(sourceVal, target, targetValToFunc);
        }
        targetValToFunc.put(targetVal, function);
        Table<D, D, EdgeFunction<L>> table = this.nonEmptyLookupByTargetNode.get(target);
        if (table == null) {
            table = HashBasedTable.create();
            this.nonEmptyLookupByTargetNode.put(target, table);
        }
        table.put(sourceVal, targetVal, function);
    }

    public synchronized Map<D, EdgeFunction<L>> reverseLookup(N target, D targetVal) {
        if ($assertionsDisabled || target != null) {
            if ($assertionsDisabled || targetVal != null) {
                Map<D, EdgeFunction<L>> res = this.nonEmptyReverseLookup.get(target, targetVal);
                return res == null ? Collections.emptyMap() : res;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public synchronized Map<D, EdgeFunction<L>> forwardLookup(D sourceVal, N target) {
        if ($assertionsDisabled || sourceVal != null) {
            if ($assertionsDisabled || target != null) {
                Map<D, EdgeFunction<L>> res = this.nonEmptyForwardLookup.get(sourceVal, target);
                return res == null ? Collections.emptyMap() : res;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public synchronized Set<Table.Cell<D, D, EdgeFunction<L>>> lookupByTarget(N target) {
        Set<Table.Cell<D, D, EdgeFunction<L>>> res;
        if ($assertionsDisabled || target != null) {
            Table<D, D, EdgeFunction<L>> table = this.nonEmptyLookupByTargetNode.get(target);
            if (table != null && (res = table.cellSet()) != null) {
                return res;
            }
            return Collections.emptySet();
        }
        throw new AssertionError();
    }

    public synchronized boolean removeFunction(D sourceVal, N target, D targetVal) {
        if ($assertionsDisabled || sourceVal != null) {
            if ($assertionsDisabled || target != null) {
                if ($assertionsDisabled || targetVal != null) {
                    Map<D, EdgeFunction<L>> sourceValToFunc = this.nonEmptyReverseLookup.get(target, targetVal);
                    if (sourceValToFunc == null || sourceValToFunc.remove(sourceVal) == null) {
                        return false;
                    }
                    if (sourceValToFunc.isEmpty()) {
                        this.nonEmptyReverseLookup.remove(targetVal, targetVal);
                    }
                    Map<D, EdgeFunction<L>> targetValToFunc = this.nonEmptyForwardLookup.get(sourceVal, target);
                    if (targetValToFunc == null || targetValToFunc.remove(targetVal) == null) {
                        return false;
                    }
                    if (targetValToFunc.isEmpty()) {
                        this.nonEmptyForwardLookup.remove(sourceVal, target);
                    }
                    Table<D, D, EdgeFunction<L>> table = this.nonEmptyLookupByTargetNode.get(target);
                    if (table == null || table.remove(sourceVal, targetVal) == null) {
                        return false;
                    }
                    if (table.isEmpty()) {
                        this.nonEmptyLookupByTargetNode.remove(target);
                        return true;
                    }
                    return true;
                }
                throw new AssertionError();
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public synchronized void clear() {
        this.nonEmptyForwardLookup.clear();
        this.nonEmptyLookupByTargetNode.clear();
        this.nonEmptyReverseLookup.clear();
    }
}
