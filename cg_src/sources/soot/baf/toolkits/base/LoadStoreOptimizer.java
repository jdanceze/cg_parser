package soot.baf.toolkits.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import polyglot.main.Report;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Local;
import soot.PhaseOptions;
import soot.Singletons;
import soot.Trap;
import soot.Unit;
import soot.baf.AddInst;
import soot.baf.AndInst;
import soot.baf.ArrayReadInst;
import soot.baf.ArrayWriteInst;
import soot.baf.Baf;
import soot.baf.Dup1Inst;
import soot.baf.DupInst;
import soot.baf.EnterMonitorInst;
import soot.baf.ExitMonitorInst;
import soot.baf.FieldArgInst;
import soot.baf.FieldGetInst;
import soot.baf.FieldPutInst;
import soot.baf.IdentityInst;
import soot.baf.IncInst;
import soot.baf.Inst;
import soot.baf.LoadInst;
import soot.baf.MethodArgInst;
import soot.baf.MulInst;
import soot.baf.NewInst;
import soot.baf.OrInst;
import soot.baf.PushInst;
import soot.baf.StaticGetInst;
import soot.baf.StaticPutInst;
import soot.baf.StoreInst;
import soot.baf.XorInst;
import soot.options.Options;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.BlockGraph;
import soot.toolkits.graph.ZonedBlockGraph;
import soot.toolkits.scalar.LocalDefs;
import soot.toolkits.scalar.LocalUses;
import soot.toolkits.scalar.UnitValueBoxPair;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/baf/toolkits/base/LoadStoreOptimizer.class */
public class LoadStoreOptimizer extends BodyTransformer {
    private static final Logger logger = LoggerFactory.getLogger(LoadStoreOptimizer.class);
    private static final boolean SKIP_SLOW_ASSERTS = true;
    private static final int FAILURE = 0;
    private static final int SUCCESS = 1;
    private static final int MAKE_DUP = 2;
    private static final int MAKE_DUP1_X1 = 3;
    private static final int SPECIAL_SUCCESS = 4;
    private static final int HAS_CHANGED = 5;
    private static final int SPECIAL_SUCCESS2 = 6;
    private static final int STORE_LOAD_ELIMINATION = 0;
    private static final int STORE_LOAD_LOAD_ELIMINATION = -1;

    public LoadStoreOptimizer(Singletons.Global g) {
    }

    public static LoadStoreOptimizer v() {
        return G.v().soot_baf_toolkits_base_LoadStoreOptimizer();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
        boolean debug = PhaseOptions.getBoolean(options, Report.debug);
        if (Options.v().verbose()) {
            logger.debug("[" + body.getMethod().getName() + "] Performing LoadStore optimizations...");
        }
        if (debug) {
            logger.debug("\n\nOptimizing Method: " + body.getMethod().getName());
        }
        new Instance(body, options, debug).go();
    }

    /* loaded from: gencallgraphv3.jar:soot/baf/toolkits/base/LoadStoreOptimizer$Instance.class */
    private static class Instance {
        private final boolean debug;
        private final Map<String, String> gOptions;
        private final Chain<Unit> mUnits;
        private final Body mBody;
        private final Map<Unit, Block> mUnitToBlockMap = new HashMap();
        private LocalDefs mLocalDefs;
        private LocalUses mLocalUses;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !LoadStoreOptimizer.class.desiredAssertionStatus();
        }

        public Instance(Body b, Map<String, String> options, boolean debug) {
            this.mBody = b;
            this.mUnits = b.getUnits();
            this.gOptions = options;
            this.debug = debug;
        }

        public void go() {
            if (!this.mUnits.isEmpty()) {
                buildUnitToBlockMap();
                if (this.debug) {
                    LoadStoreOptimizer.logger.debug("Calling optimizeLoadStore(1)\n");
                }
                optimizeLoadStores(false);
                if (PhaseOptions.getBoolean(this.gOptions, "inter")) {
                    if (this.debug) {
                        LoadStoreOptimizer.logger.debug("Calling doInterBlockOptimizations");
                    }
                    doInterBlockOptimizations();
                }
                if (PhaseOptions.getBoolean(this.gOptions, "sl2") || PhaseOptions.getBoolean(this.gOptions, "sll2")) {
                    if (this.debug) {
                        LoadStoreOptimizer.logger.debug("Calling optimizeLoadStore(2)");
                    }
                    optimizeLoadStores(true);
                }
            }
        }

        private void buildUnitToBlockMap() {
            this.mUnitToBlockMap.clear();
            BlockGraph blockGraph = new ZonedBlockGraph(this.mBody);
            if (this.debug) {
                LoadStoreOptimizer.logger.debug("Method " + this.mBody.getMethod().getName() + " Block Graph: ");
                LoadStoreOptimizer.logger.debug(blockGraph.toString());
            }
            for (Block block : blockGraph.getBlocks()) {
                Iterator<Unit> it = block.iterator();
                while (it.hasNext()) {
                    Unit unit = it.next();
                    this.mUnitToBlockMap.put(unit, block);
                }
            }
        }

        private void computeLocalDefsAndLocalUsesInfo() {
            if (this.mLocalDefs == null) {
                this.mLocalDefs = G.v().soot_toolkits_scalar_LocalDefsFactory().newLocalDefs(this.mBody);
            }
            if (this.mLocalUses == null) {
                this.mLocalUses = LocalUses.Factory.newLocalUses(this.mBody, this.mLocalDefs);
            }
        }

        private void clearLocalDefsAndLocalUsesInfo() {
            this.mLocalDefs = null;
            this.mLocalUses = null;
        }

        private List<Unit> buildStoreList() {
            List<Unit> storeList = new ArrayList<>();
            for (Unit unit : this.mUnits) {
                if (unit instanceof StoreInst) {
                    storeList.add(unit);
                }
            }
            return storeList;
        }

        private void optimizeLoadStores(boolean mPass2) {
            computeLocalDefsAndLocalUsesInfo();
            List<Unit> storeList = buildStoreList();
            boolean hasChanged = true;
            boolean hasChangedFlag = false;
            while (hasChanged) {
                hasChanged = false;
                Iterator<Unit> unitIt = storeList.iterator();
                while (unitIt.hasNext()) {
                    Unit unit = unitIt.next();
                    List<UnitValueBoxPair> uses = this.mLocalUses.getUsesOf(unit);
                    if (uses.size() < 3) {
                        Iterator<UnitValueBoxPair> it = uses.iterator();
                        while (true) {
                            if (it.hasNext()) {
                                UnitValueBoxPair pair = it.next();
                                Unit loadUnit = pair.getUnit();
                                if (!(loadUnit instanceof LoadInst)) {
                                    break;
                                }
                                List<Unit> defs = this.mLocalDefs.getDefsOfAt((Local) pair.getValueBox().getValue(), loadUnit);
                                if (defs.size() <= 1) {
                                    if (defs.get(0) != unit) {
                                        break;
                                    }
                                } else {
                                    break;
                                }
                            } else {
                                Block storeBlock = this.mUnitToBlockMap.get(unit);
                                Iterator<UnitValueBoxPair> it2 = uses.iterator();
                                while (true) {
                                    if (it2.hasNext()) {
                                        Block useBlock = this.mUnitToBlockMap.get(it2.next().getUnit());
                                        if (useBlock != storeBlock) {
                                            break;
                                        }
                                    } else {
                                        switch (uses.size()) {
                                            case 1:
                                                if (PhaseOptions.getBoolean(this.gOptions, "sl") && (!mPass2 || PhaseOptions.getBoolean(this.gOptions, "sl2"))) {
                                                    Unit loadUnit2 = uses.get(0).getUnit();
                                                    Block block = this.mUnitToBlockMap.get(unit);
                                                    int test = stackIndependent(unit, loadUnit2, block, 0);
                                                    if (test != 1 && test != 4) {
                                                        break;
                                                    } else {
                                                        block.remove(unit);
                                                        this.mUnitToBlockMap.remove(unit);
                                                        block.remove(loadUnit2);
                                                        this.mUnitToBlockMap.remove(loadUnit2);
                                                        unitIt.remove();
                                                        hasChanged = true;
                                                        hasChangedFlag = false;
                                                        if (!this.debug) {
                                                            break;
                                                        } else {
                                                            LoadStoreOptimizer.logger.debug("Store/Load elimination occurred case1.");
                                                            break;
                                                        }
                                                    }
                                                }
                                                break;
                                            case 2:
                                                if (PhaseOptions.getBoolean(this.gOptions, "sll") && (!mPass2 || PhaseOptions.getBoolean(this.gOptions, "sll2"))) {
                                                    Unit firstLoad = uses.get(0).getUnit();
                                                    Unit secondLoad = uses.get(1).getUnit();
                                                    if (this.mUnits.follows(firstLoad, secondLoad)) {
                                                        secondLoad = firstLoad;
                                                        firstLoad = secondLoad;
                                                    }
                                                    Block block2 = this.mUnitToBlockMap.get(unit);
                                                    int result = stackIndependent(unit, firstLoad, block2, 0);
                                                    if (result != 1) {
                                                        if (result != 4 && result != 5 && result != 6) {
                                                            break;
                                                        } else if (!hasChangedFlag) {
                                                            hasChangedFlag = true;
                                                            hasChanged = true;
                                                            break;
                                                        } else {
                                                            break;
                                                        }
                                                    } else {
                                                        block2.remove(firstLoad);
                                                        block2.insertAfter(firstLoad, unit);
                                                        int res = stackIndependent(unit, secondLoad, block2, -1);
                                                        if (res != 2) {
                                                            break;
                                                        } else {
                                                            Dup1Inst dup = Baf.v().newDup1Inst(((LoadInst) secondLoad).getOpType());
                                                            dup.addAllTagsOf(unit);
                                                            replaceUnit(unit, dup);
                                                            unitIt.remove();
                                                            block2.remove(firstLoad);
                                                            this.mUnitToBlockMap.remove(firstLoad);
                                                            block2.remove(secondLoad);
                                                            this.mUnitToBlockMap.remove(secondLoad);
                                                            hasChanged = true;
                                                            hasChangedFlag = false;
                                                            break;
                                                        }
                                                    }
                                                }
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        private boolean isRequiredByFollowingUnits(Unit from, Unit to) {
            Unit currentInst;
            if (from != to) {
                int stackHeight = 0;
                Iterator<Unit> it = this.mUnits.iterator(from, to);
                it.next();
                while (it.hasNext() && (currentInst = it.next()) != to) {
                    int stackHeight2 = stackHeight - ((Inst) currentInst).getInCount();
                    if (stackHeight2 < 0) {
                        return true;
                    }
                    stackHeight = stackHeight2 + ((Inst) currentInst).getOutCount();
                }
                return false;
            }
            return false;
        }

        private int pushStoreToLoad(Unit from, Unit to, Block block) {
            Unit storePred = block.getPredOf(from);
            if (storePred == null || ((Inst) storePred).getOutCount() != 1) {
                if (this.debug) {
                    if (storePred == null) {
                        LoadStoreOptimizer.logger.debug("xxx: failed due: cannot move past block tail ");
                        return 0;
                    }
                    LoadStoreOptimizer.logger.debug("xxx: failed due: pred out-count != 1 ");
                    return 0;
                }
                return 0;
            }
            Set<Unit> unitsToMove = new HashSet<>();
            unitsToMove.add(storePred);
            unitsToMove.add(from);
            int h = ((Inst) storePred).getInCount();
            Unit currentUnit = storePred;
            if (h != 0) {
                Unit predOf = block.getPredOf(storePred);
                while (true) {
                    currentUnit = predOf;
                    if (currentUnit == null) {
                        break;
                    }
                    int h2 = h - ((Inst) currentUnit).getOutCount();
                    if (h2 < 0) {
                        if (this.debug) {
                            LoadStoreOptimizer.logger.debug("xxx: negative");
                            return 0;
                        }
                        return 0;
                    }
                    h = h2 + ((Inst) currentUnit).getInCount();
                    unitsToMove.add(currentUnit);
                    if (h == 0) {
                        break;
                    }
                    predOf = block.getPredOf(currentUnit);
                }
            }
            if (currentUnit == null) {
                if (this.debug) {
                    LoadStoreOptimizer.logger.debug("xxx: null");
                    return 0;
                }
                return 0;
            }
            Unit succOf = block.getSuccOf(from);
            while (true) {
                Unit uu = succOf;
                if (uu != to) {
                    for (Unit nu : unitsToMove) {
                        if (!canMoveUnitOver(nu, uu)) {
                            if (this.debug) {
                                LoadStoreOptimizer.logger.debug("xxx: failure cannot move " + nu + " over " + uu);
                                return 0;
                            }
                            return 0;
                        } else if (this.debug) {
                            LoadStoreOptimizer.logger.debug("can move " + nu + " over " + uu);
                        }
                    }
                    succOf = block.getSuccOf(uu);
                } else {
                    Unit unit = currentUnit;
                    while (true) {
                        Unit unitToMove = unit;
                        if (unitToMove == from) {
                            break;
                        }
                        Unit succ = block.getSuccOf(unitToMove);
                        if (this.debug) {
                            LoadStoreOptimizer.logger.debug("moving " + unitToMove);
                        }
                        block.remove(unitToMove);
                        block.insertBefore(unitToMove, to);
                        unit = succ;
                    }
                    block.remove(from);
                    block.insertBefore(from, to);
                    if (this.debug) {
                        LoadStoreOptimizer.logger.debug("xxx: success pushing forward stuff.");
                        return 4;
                    }
                    return 4;
                }
            }
        }

        private int stackIndependent(Unit from, Unit to, Block block, int aContext) {
            if ($assertionsDisabled || aContext == 0 || aContext == -1) {
                if (this.debug) {
                    LoadStoreOptimizer.logger.debug("Trying: " + from + "/" + to + " in block  " + block.getIndexInMethod() + ":");
                    LoadStoreOptimizer.logger.debug("context: " + (aContext == 0 ? "STORE_LOAD_ELIMINATION" : "STORE_LOAD_LOAD_ELIMINATION"));
                }
                int minStackHeightAttained = 0;
                int stackHeight = 0;
                Iterator<Unit> it = this.mUnits.iterator(this.mUnits.getSuccOf(from));
                Unit currentInst = it.next();
                if (aContext == -1) {
                    currentInst = it.next();
                }
                while (currentInst != to && it.hasNext()) {
                    int stackHeight2 = stackHeight - ((Inst) currentInst).getInCount();
                    if (stackHeight2 < minStackHeightAttained) {
                        minStackHeightAttained = stackHeight2;
                    }
                    stackHeight = stackHeight2 + ((Inst) currentInst).getOutCount();
                    currentInst = it.next();
                }
                if (this.debug) {
                    LoadStoreOptimizer.logger.debug("nshv = " + stackHeight);
                    LoadStoreOptimizer.logger.debug("mshv = " + minStackHeightAttained);
                }
                boolean hasChanged = true;
                while (hasChanged) {
                    hasChanged = false;
                    if (aContext == -1) {
                        if (stackHeight == 0 && minStackHeightAttained == 0) {
                            if (this.debug) {
                                LoadStoreOptimizer.logger.debug("xxx: succ: -1, makedup ");
                                return 2;
                            }
                            return 2;
                        } else if (stackHeight == -1 && minStackHeightAttained == -1) {
                            if (this.debug) {
                                LoadStoreOptimizer.logger.debug("xxx: succ: -1, makedup , -1");
                                return 2;
                            }
                            return 2;
                        } else if (stackHeight == -2 && minStackHeightAttained == -2) {
                            if (this.debug) {
                                LoadStoreOptimizer.logger.debug("xxx: succ -1 , make dupx1 ");
                                return 3;
                            }
                            return 3;
                        } else if (minStackHeightAttained < -2) {
                            if (this.debug) {
                                LoadStoreOptimizer.logger.debug("xxx: failed due: minStackHeightAttained < -2 ");
                                return 0;
                            }
                            return 0;
                        }
                    } else if (aContext == 0) {
                        if (stackHeight == 0 && minStackHeightAttained == 0) {
                            if (this.debug) {
                                LoadStoreOptimizer.logger.debug("xxx: success due: 0, SUCCESS");
                                return 1;
                            }
                            return 1;
                        } else if (minStackHeightAttained < 0) {
                            return pushStoreToLoad(from, to, block);
                        }
                    }
                    Iterator<Unit> it2 = this.mUnits.iterator(this.mUnits.getSuccOf(from), to);
                    Unit u = it2.next();
                    if (aContext == -1) {
                        u = it2.next();
                    }
                    while (true) {
                        if (u == to) {
                            break;
                        }
                        Unit unitToMove = null;
                        if (((Inst) u).getNetCount() == 1) {
                            if ((u instanceof LoadInst) || (u instanceof PushInst) || (u instanceof NewInst) || (u instanceof StaticGetInst) || (u instanceof Dup1Inst)) {
                                if (!isRequiredByFollowingUnits(u, to)) {
                                    unitToMove = u;
                                }
                            } else if (this.debug) {
                                LoadStoreOptimizer.logger.debug("(LoadStoreOptimizer@stackIndependent): found unknown unit w/ getNetCount == 1: " + u);
                            }
                        }
                        if (unitToMove == null || !tryToMoveUnit(unitToMove, block, from, to, 0)) {
                            u = it2.next();
                        } else if (stackHeight > -2 && minStackHeightAttained == -2) {
                            if (this.debug) {
                                LoadStoreOptimizer.logger.debug("xxx: has changed ");
                                return 5;
                            }
                            return 5;
                        } else {
                            stackHeight--;
                            if (stackHeight < minStackHeightAttained) {
                                minStackHeightAttained = stackHeight;
                            }
                            hasChanged = true;
                        }
                    }
                }
                if (isCommutativeBinOp(block.getSuccOf(to))) {
                    if (aContext == 0 && stackHeight == 1 && minStackHeightAttained == 0) {
                        if (this.debug) {
                            LoadStoreOptimizer.logger.debug("xxx: commutative ");
                            return 4;
                        }
                        return 4;
                    }
                    Inst toAsInst = (Inst) to;
                    if (toAsInst.getOutCount() == 1 && toAsInst.getInCount() == 0) {
                        Inst toPred = (Inst) this.mUnits.getPredOf(to);
                        if (toPred.getOutCount() == 1 && toPred.getInCount() == 0) {
                            block.remove(toPred);
                            block.insertAfter(toPred, to);
                            if (this.debug) {
                                LoadStoreOptimizer.logger.debug("xxx: (commutative) has changed ");
                                return 5;
                            }
                            return 5;
                        }
                    }
                    if (this.debug) {
                        LoadStoreOptimizer.logger.debug("xxx: (commutative) failed due: ??? ");
                        return 0;
                    }
                    return 0;
                } else if (aContext == 0) {
                    return pushStoreToLoad(from, to, block);
                } else {
                    if (this.debug) {
                        LoadStoreOptimizer.logger.debug("xxx: failed due: ??? ");
                        return 0;
                    }
                    return 0;
                }
            }
            throw new AssertionError();
        }

        private boolean isNonLocalReadOrWrite(Unit aUnit) {
            return (aUnit instanceof FieldArgInst) || (aUnit instanceof ArrayReadInst) || (aUnit instanceof ArrayWriteInst);
        }

        private boolean canMoveUnitOver(Unit aUnitToMove, Unit aUnitToGoOver) {
            if (!(aUnitToGoOver instanceof MethodArgInst) || !(aUnitToMove instanceof MethodArgInst)) {
                if (!(aUnitToGoOver instanceof MethodArgInst) || !isNonLocalReadOrWrite(aUnitToMove)) {
                    if (!isNonLocalReadOrWrite(aUnitToGoOver) || !(aUnitToMove instanceof MethodArgInst)) {
                        if (!(aUnitToGoOver instanceof ArrayReadInst) || !(aUnitToMove instanceof ArrayWriteInst)) {
                            if (!(aUnitToGoOver instanceof ArrayWriteInst) || !(aUnitToMove instanceof ArrayReadInst)) {
                                if (!(aUnitToGoOver instanceof ArrayWriteInst) || !(aUnitToMove instanceof ArrayWriteInst)) {
                                    if (!(aUnitToGoOver instanceof FieldPutInst) || !(aUnitToMove instanceof FieldGetInst) || ((FieldArgInst) aUnitToGoOver).getField() != ((FieldArgInst) aUnitToMove).getField()) {
                                        if (!(aUnitToGoOver instanceof FieldGetInst) || !(aUnitToMove instanceof FieldPutInst) || ((FieldArgInst) aUnitToGoOver).getField() != ((FieldArgInst) aUnitToMove).getField()) {
                                            if (!(aUnitToGoOver instanceof FieldPutInst) || !(aUnitToMove instanceof FieldPutInst) || ((FieldArgInst) aUnitToGoOver).getField() != ((FieldArgInst) aUnitToMove).getField()) {
                                                if (!(aUnitToGoOver instanceof StaticPutInst) || !(aUnitToMove instanceof StaticGetInst) || ((FieldArgInst) aUnitToGoOver).getField() != ((FieldArgInst) aUnitToMove).getField()) {
                                                    if (!(aUnitToGoOver instanceof StaticGetInst) || !(aUnitToMove instanceof StaticPutInst) || ((FieldArgInst) aUnitToGoOver).getField() != ((FieldArgInst) aUnitToMove).getField()) {
                                                        if (((aUnitToGoOver instanceof StaticPutInst) && (aUnitToMove instanceof StaticPutInst) && ((FieldArgInst) aUnitToGoOver).getField() == ((FieldArgInst) aUnitToMove).getField()) || (aUnitToGoOver instanceof EnterMonitorInst) || (aUnitToGoOver instanceof ExitMonitorInst) || (aUnitToMove instanceof EnterMonitorInst) || (aUnitToGoOver instanceof ExitMonitorInst) || (aUnitToGoOver instanceof IdentityInst) || (aUnitToMove instanceof IdentityInst)) {
                                                            return false;
                                                        }
                                                        if (aUnitToMove instanceof LoadInst) {
                                                            if (aUnitToGoOver instanceof StoreInst) {
                                                                if (((StoreInst) aUnitToGoOver).getLocal() == ((LoadInst) aUnitToMove).getLocal()) {
                                                                    return false;
                                                                }
                                                            } else if ((aUnitToGoOver instanceof IncInst) && ((IncInst) aUnitToGoOver).getLocal() == ((LoadInst) aUnitToMove).getLocal()) {
                                                                return false;
                                                            }
                                                        }
                                                        if (aUnitToMove instanceof StoreInst) {
                                                            if (aUnitToGoOver instanceof LoadInst) {
                                                                if (((LoadInst) aUnitToGoOver).getLocal() == ((StoreInst) aUnitToMove).getLocal()) {
                                                                    return false;
                                                                }
                                                            } else if ((aUnitToGoOver instanceof IncInst) && ((IncInst) aUnitToGoOver).getLocal() == ((StoreInst) aUnitToMove).getLocal()) {
                                                                return false;
                                                            }
                                                        }
                                                        if (aUnitToMove instanceof IncInst) {
                                                            if (aUnitToGoOver instanceof StoreInst) {
                                                                if (((StoreInst) aUnitToGoOver).getLocal() == ((IncInst) aUnitToMove).getLocal()) {
                                                                    return false;
                                                                }
                                                                return true;
                                                            } else if ((aUnitToGoOver instanceof LoadInst) && ((LoadInst) aUnitToGoOver).getLocal() == ((IncInst) aUnitToMove).getLocal()) {
                                                                return false;
                                                            } else {
                                                                return true;
                                                            }
                                                        }
                                                        return true;
                                                    }
                                                    return false;
                                                }
                                                return false;
                                            }
                                            return false;
                                        }
                                        return false;
                                    }
                                    return false;
                                }
                                return false;
                            }
                            return false;
                        }
                        return false;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }

        private boolean tryToMoveUnit(Unit unitToMove, Block block, Unit from, Unit to, int flag) {
            if (this.debug) {
                LoadStoreOptimizer.logger.debug("[tryToMoveUnit]: trying to move:" + unitToMove);
            }
            if (unitToMove == null) {
                return false;
            }
            int h = 0;
            boolean reachedStore = false;
            boolean reorderingOccurred = false;
            Unit current = unitToMove;
            while (true) {
                if (current == block.getHead()) {
                    break;
                }
                current = this.mUnits.getPredOf(current);
                if (!canMoveUnitOver(current, unitToMove)) {
                    return false;
                }
                if (current == from) {
                    reachedStore = true;
                }
                int h2 = h - ((Inst) current).getOutCount();
                if (h2 < 0) {
                    if (this.debug) {
                        LoadStoreOptimizer.logger.debug("(LoadStoreOptimizer@stackIndependent): Stack went negative while trying to reorder code.");
                    }
                    if (flag == 1 && (current instanceof DupInst)) {
                        block.remove(unitToMove);
                        block.insertAfter(unitToMove, current);
                        return false;
                    }
                    return false;
                }
                h = h2 + ((Inst) current).getInCount();
                if (h == 0 && reachedStore && !isRequiredByFollowingUnits(unitToMove, to)) {
                    if (this.debug) {
                        LoadStoreOptimizer.logger.debug("(LoadStoreOptimizer@stackIndependent): reordering bytecode move: " + unitToMove + " before: " + current);
                    }
                    block.remove(unitToMove);
                    block.insertBefore(unitToMove, current);
                    reorderingOccurred = true;
                }
            }
            if (this.debug) {
                if (reorderingOccurred) {
                    LoadStoreOptimizer.logger.debug("reordering occurred");
                } else {
                    LoadStoreOptimizer.logger.debug("(LoadStoreOptimizer@stackIndependent):failed to find a new slot for unit to move");
                }
            }
            return reorderingOccurred;
        }

        private void replaceUnit(Unit aToReplace1, Unit aToReplace2, Unit aReplacement) {
            Block block = this.mUnitToBlockMap.get(aToReplace1);
            if (aToReplace2 != null) {
                block.insertAfter(aReplacement, aToReplace2);
                block.remove(aToReplace2);
                this.mUnitToBlockMap.remove(aToReplace2);
            } else {
                block.insertAfter(aReplacement, aToReplace1);
            }
            block.remove(aToReplace1);
            this.mUnitToBlockMap.remove(aToReplace1);
            this.mUnitToBlockMap.put(aReplacement, block);
        }

        private void replaceUnit(Unit aToReplace, Unit aReplacement) {
            replaceUnit(aToReplace, null, aReplacement);
        }

        private boolean isExceptionHandlerBlock(Block aBlock) {
            Unit blockHead = aBlock.getHead();
            for (Trap trap : this.mBody.getTraps()) {
                if (trap.getHandlerUnit() == blockHead) {
                    return true;
                }
            }
            return false;
        }

        private int getDeltaStackHeightFromTo(Unit aFrom, Unit aTo) {
            int h = 0;
            Iterator<Unit> it = this.mUnits.iterator(aFrom, aTo);
            while (it.hasNext()) {
                Unit next = it.next();
                h += ((Inst) next).getNetCount();
            }
            return h;
        }

        private boolean isZeroStackDeltaWithoutClobbering(Unit aFrom, Unit aTo) {
            int h = 0;
            Iterator<Unit> it = this.mUnits.iterator(aFrom, aTo);
            while (it.hasNext()) {
                Inst next = (Inst) it.next();
                if (next.getInCount() > h) {
                    return false;
                }
                h += next.getNetCount();
                if (h < 0) {
                    return false;
                }
            }
            return h == 0;
        }

        /* JADX WARN: Removed duplicated region for block: B:30:0x0147  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private void doInterBlockOptimizations() {
            /*
                Method dump skipped, instructions count: 1113
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: soot.baf.toolkits.base.LoadStoreOptimizer.Instance.doInterBlockOptimizations():void");
        }

        private boolean allSuccesorsOfAreThePredecessorsOf(Block aFirstBlock, Block aSecondBlock) {
            List<Block> preds = aSecondBlock.getPreds();
            for (Block next : aFirstBlock.getSuccs()) {
                if (!preds.contains(next)) {
                    return false;
                }
            }
            return aFirstBlock.getSuccs().size() == preds.size();
        }

        private boolean isCommutativeBinOp(Unit aUnit) {
            return (aUnit instanceof AddInst) || (aUnit instanceof MulInst) || (aUnit instanceof AndInst) || (aUnit instanceof OrInst) || (aUnit instanceof XorInst);
        }

        private boolean unitToBlockMapIsValid() {
            for (Unit u : this.mUnits) {
                if (!$assertionsDisabled && !this.mUnitToBlockMap.containsKey(u)) {
                    throw new AssertionError();
                }
            }
            HashSet<Block> blocks = new HashSet<>();
            for (Map.Entry<Unit, Block> e : this.mUnitToBlockMap.entrySet()) {
                blocks.add(e.getValue());
                if (!$assertionsDisabled && !contains(e.getValue(), e.getKey())) {
                    throw new AssertionError();
                }
            }
            Iterator<Block> it = blocks.iterator();
            while (it.hasNext()) {
                Block b = it.next();
                Unit t = b.getTail();
                if (!$assertionsDisabled && this.mUnitToBlockMap.get(t) != b) {
                    throw new AssertionError();
                }
                Unit head = b.getHead();
                while (true) {
                    Unit u2 = head;
                    if (u2 == t) {
                        break;
                    } else if (!$assertionsDisabled && this.mUnitToBlockMap.get(u2) != b) {
                        throw new AssertionError();
                    } else {
                        head = b.getSuccOf(u2);
                    }
                }
            }
            return true;
        }

        private static boolean contains(Block b, Unit u) {
            Unit t = b.getTail();
            if (u == t) {
                return true;
            }
            Unit head = b.getHead();
            while (true) {
                Unit u2 = head;
                if (u2 != t) {
                    if (u != u2) {
                        head = b.getSuccOf(u2);
                    } else {
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }
    }
}
