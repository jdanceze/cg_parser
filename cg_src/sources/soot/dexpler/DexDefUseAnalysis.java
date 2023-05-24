package soot.dexpler;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.Body;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.DefinitionStmt;
import soot.toolkits.scalar.LocalDefs;
/* loaded from: gencallgraphv3.jar:soot/dexpler/DexDefUseAnalysis.class */
public class DexDefUseAnalysis implements LocalDefs {
    private final Body body;
    private final Map<Local, Set<Unit>> localToUses = new HashMap();
    private final Map<Local, Set<Unit>> localToDefs = new HashMap();
    private final Map<Local, Set<Unit>> localToDefsWithAliases = new HashMap();
    protected Map<Local, Integer> localToNumber = new HashMap();
    protected BitSet[] localToDefsBits;
    protected BitSet[] localToUsesBits;
    protected List<Unit> unitList;

    public DexDefUseAnalysis(Body body) {
        this.body = body;
        initialize();
    }

    protected void initialize() {
        int lastLocalNumber = 0;
        for (Local l : this.body.getLocals()) {
            int i = lastLocalNumber;
            lastLocalNumber++;
            this.localToNumber.put(l, Integer.valueOf(i));
        }
        this.localToDefsBits = new BitSet[this.body.getLocalCount()];
        this.localToUsesBits = new BitSet[this.body.getLocalCount()];
        this.unitList = new ArrayList(this.body.getUnits());
        for (int i2 = 0; i2 < this.unitList.size(); i2++) {
            Unit u = this.unitList.get(i2);
            if (u instanceof DefinitionStmt) {
                Value val = ((DefinitionStmt) u).getLeftOp();
                if (val instanceof Local) {
                    int localIdx = this.localToNumber.get((Local) val).intValue();
                    BitSet bs = this.localToDefsBits[localIdx];
                    if (bs == null) {
                        BitSet[] bitSetArr = this.localToDefsBits;
                        BitSet bitSet = new BitSet();
                        bs = bitSet;
                        bitSetArr[localIdx] = bitSet;
                    }
                    bs.set(i2);
                }
            }
            for (ValueBox vb : u.getUseBoxes()) {
                Value val2 = vb.getValue();
                if (val2 instanceof Local) {
                    int localIdx2 = this.localToNumber.get((Local) val2).intValue();
                    BitSet bs2 = this.localToUsesBits[localIdx2];
                    if (bs2 == null) {
                        BitSet[] bitSetArr2 = this.localToUsesBits;
                        BitSet bitSet2 = new BitSet();
                        bs2 = bitSet2;
                        bitSetArr2[localIdx2] = bitSet2;
                    }
                    bs2.set(i2);
                }
            }
        }
    }

    public Set<Unit> getUsesOf(Local l) {
        Set<Unit> uses = this.localToUses.get(l);
        if (uses == null) {
            uses = new HashSet<>();
            BitSet bs = this.localToUsesBits[this.localToNumber.get(l).intValue()];
            if (bs != null) {
                int nextSetBit = bs.nextSetBit(0);
                while (true) {
                    int i = nextSetBit;
                    if (i < 0) {
                        break;
                    }
                    uses.add(this.unitList.get(i));
                    nextSetBit = bs.nextSetBit(i + 1);
                }
            }
            this.localToUses.put(l, uses);
        }
        return uses;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Set<Unit> collectDefinitionsWithAliases(Local l) {
        Set<Unit> defs = this.localToDefsWithAliases.get(l);
        if (defs == null) {
            defs = new HashSet<>();
            Set<Local> seenLocals = new HashSet<>();
            List<Local> newLocals = new ArrayList<>();
            newLocals.add(l);
            while (!newLocals.isEmpty()) {
                Local curLocal = newLocals.remove(0);
                BitSet bsDefs = this.localToDefsBits[this.localToNumber.get(curLocal).intValue()];
                if (bsDefs != null) {
                    int nextSetBit = bsDefs.nextSetBit(0);
                    while (true) {
                        int i = nextSetBit;
                        if (i < 0) {
                            break;
                        }
                        Unit u = this.unitList.get(i);
                        defs.add(u);
                        DefinitionStmt defStmt = (DefinitionStmt) u;
                        if ((defStmt.getRightOp() instanceof Local) && seenLocals.add((Local) defStmt.getRightOp())) {
                            newLocals.add((Local) defStmt.getRightOp());
                        }
                        nextSetBit = bsDefs.nextSetBit(i + 1);
                    }
                }
                BitSet bsUses = this.localToUsesBits[this.localToNumber.get(curLocal).intValue()];
                if (bsUses != null) {
                    int nextSetBit2 = bsUses.nextSetBit(0);
                    while (true) {
                        int i2 = nextSetBit2;
                        if (i2 < 0) {
                            break;
                        }
                        Unit use = this.unitList.get(i2);
                        if (use instanceof AssignStmt) {
                            AssignStmt assignUse = (AssignStmt) use;
                            if (assignUse.getRightOp() == curLocal && (assignUse.getLeftOp() instanceof Local) && seenLocals.add((Local) assignUse.getLeftOp())) {
                                newLocals.add((Local) assignUse.getLeftOp());
                            }
                        }
                        nextSetBit2 = bsUses.nextSetBit(i2 + 1);
                    }
                }
            }
            this.localToDefsWithAliases.put(l, defs);
        }
        return defs;
    }

    @Override // soot.toolkits.scalar.LocalDefs
    public List<Unit> getDefsOfAt(Local l, Unit s) {
        return getDefsOf(l);
    }

    @Override // soot.toolkits.scalar.LocalDefs
    public List<Unit> getDefsOf(Local l) {
        Set<Unit> defs = this.localToDefs.get(l);
        if (defs == null) {
            defs = new HashSet<>();
            BitSet bs = this.localToDefsBits[this.localToNumber.get(l).intValue()];
            if (bs != null) {
                int nextSetBit = bs.nextSetBit(0);
                while (true) {
                    int i = nextSetBit;
                    if (i < 0) {
                        break;
                    }
                    Unit u = this.unitList.get(i);
                    if ((u instanceof DefinitionStmt) && ((DefinitionStmt) u).getLeftOp() == l) {
                        defs.add(u);
                    }
                    nextSetBit = bs.nextSetBit(i + 1);
                }
            }
            this.localToDefs.put(l, defs);
        }
        return new ArrayList(defs);
    }
}
