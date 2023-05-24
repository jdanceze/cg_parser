package soot.dexpler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.toolkits.scalar.LocalDefs;
import soot.toolkits.scalar.LocalUses;
import soot.toolkits.scalar.UnitValueBoxPair;
/* loaded from: gencallgraphv3.jar:soot/dexpler/DexTransformer.class */
public abstract class DexTransformer extends BodyTransformer {
    protected List<Unit> collectDefinitionsWithAliases(Local l, LocalDefs localDefs, LocalUses localUses, Body body) {
        Set<Local> seenLocals = new HashSet<>();
        List<Local> newLocals = new ArrayList<>();
        List<Unit> defs = new ArrayList<>();
        newLocals.add(l);
        seenLocals.add(l);
        while (!newLocals.isEmpty()) {
            Local local = newLocals.remove(0);
            for (Unit u : collectDefinitions(local, localDefs)) {
                if (u instanceof AssignStmt) {
                    Value r = ((AssignStmt) u).getRightOp();
                    if ((r instanceof Local) && seenLocals.add((Local) r)) {
                        newLocals.add((Local) r);
                    }
                }
                defs.add(u);
                List<UnitValueBoxPair> usesOf = localUses.getUsesOf(u);
                for (UnitValueBoxPair pair : usesOf) {
                    Unit unit = pair.getUnit();
                    if (unit instanceof AssignStmt) {
                        AssignStmt assignStmt = (AssignStmt) unit;
                        Value right = assignStmt.getRightOp();
                        Value left = assignStmt.getLeftOp();
                        if (right == local && (left instanceof Local) && seenLocals.add((Local) left)) {
                            newLocals.add((Local) left);
                        }
                    }
                }
            }
        }
        return defs;
    }

    private List<Unit> collectDefinitions(Local l, LocalDefs localDefs) {
        return localDefs.getDefsOf(l);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Code restructure failed: missing block: B:117:0x033b, code lost:
        if (r11 != 0) goto L52;
     */
    /* JADX WARN: Code restructure failed: missing block: B:119:0x0340, code lost:
        if (r16 != null) goto L52;
     */
    /* JADX WARN: Code restructure failed: missing block: B:121:0x034c, code lost:
        if (r17 != r0.size()) goto L50;
     */
    /* JADX WARN: Code restructure failed: missing block: B:123:0x0352, code lost:
        return soot.NullType.v();
     */
    /* JADX WARN: Code restructure failed: missing block: B:125:0x0371, code lost:
        throw new java.lang.RuntimeException("ERROR: could not find type of array from statement '" + r10 + "'");
     */
    /* JADX WARN: Code restructure failed: missing block: B:127:0x0374, code lost:
        return r16;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public soot.Type findArrayType(soot.toolkits.scalar.LocalDefs r9, soot.jimple.Stmt r10, int r11, java.util.Set<soot.Unit> r12) {
        /*
            Method dump skipped, instructions count: 885
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.dexpler.DexTransformer.findArrayType(soot.toolkits.scalar.LocalDefs, soot.jimple.Stmt, int, java.util.Set):soot.Type");
    }
}
