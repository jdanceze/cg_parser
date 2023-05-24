package soot.jbco.jimpleTransformations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import soot.Body;
import soot.BodyTransformer;
import soot.BooleanType;
import soot.IntType;
import soot.Local;
import soot.PatchingChain;
import soot.PrimType;
import soot.RefType;
import soot.SootField;
import soot.SootMethod;
import soot.Unit;
import soot.jbco.IJbcoTransform;
import soot.jbco.Main;
import soot.jbco.util.Rand;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.toolkits.graph.BriefUnitGraph;
/* loaded from: gencallgraphv3.jar:soot/jbco/jimpleTransformations/AddSwitches.class */
public class AddSwitches extends BodyTransformer implements IJbcoTransform {
    int switchesadded = 0;
    public static String[] dependancies = {FieldRenamer.name, "jtp.jbco_adss", "bb.jbco_ful"};
    public static String name = "jtp.jbco_adss";

    @Override // soot.jbco.IJbcoTransform
    public void outputSummary() {
        out.println("Switches added: " + this.switchesadded);
    }

    @Override // soot.jbco.IJbcoTransform
    public String[] getDependencies() {
        return dependancies;
    }

    @Override // soot.jbco.IJbcoTransform
    public String getName() {
        return name;
    }

    /* JADX WARN: Removed duplicated region for block: B:3:0x000d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean checkTraps(soot.Unit r4, soot.Body r5) {
        /*
            r3 = this;
            r0 = r5
            soot.util.Chain r0 = r0.getTraps()
            java.util.Iterator r0 = r0.iterator()
            r6 = r0
            goto L3b
        Ld:
            r0 = r6
            java.lang.Object r0 = r0.next()
            soot.Trap r0 = (soot.Trap) r0
            r7 = r0
            r0 = r7
            soot.Unit r0 = r0.getBeginUnit()
            r1 = r4
            if (r0 == r1) goto L39
            r0 = r7
            soot.Unit r0 = r0.getEndUnit()
            r1 = r4
            if (r0 == r1) goto L39
            r0 = r7
            soot.Unit r0 = r0.getHandlerUnit()
            r1 = r4
            if (r0 != r1) goto L3b
        L39:
            r0 = 1
            return r0
        L3b:
            r0 = r6
            boolean r0 = r0.hasNext()
            if (r0 != 0) goto Ld
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.jbco.jimpleTransformations.AddSwitches.checkTraps(soot.Unit, soot.Body):boolean");
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        if (b.getMethod().getSignature().indexOf("<clinit>") >= 0) {
            return;
        }
        int weight = Main.getWeight(phaseName, b.getMethod().getSignature());
        if (weight == 0) {
            return;
        }
        New2InitFlowAnalysis fa = new New2InitFlowAnalysis(new BriefUnitGraph(b));
        Vector<Unit> zeroheight = new Vector<>();
        PatchingChain<Unit> units = b.getUnits();
        Unit first = null;
        Iterator<Unit> it = units.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Unit unit = it.next();
            if (!(unit instanceof IdentityStmt)) {
                first = unit;
                break;
            }
        }
        Iterator<Unit> it2 = units.snapshotIterator();
        while (it2.hasNext()) {
            Unit unit2 = it2.next();
            if (!(unit2 instanceof IdentityStmt) && !checkTraps(unit2, b) && fa.getFlowAfter(unit2).isEmpty() && fa.getFlowBefore(unit2).isEmpty()) {
                zeroheight.add(unit2);
            }
        }
        if (zeroheight.size() < 3) {
            return;
        }
        int idx = 0;
        Unit u = null;
        for (int i = 0; i < zeroheight.size(); i++) {
            idx = Rand.getInt(zeroheight.size() - 1) + 1;
            u = zeroheight.get(idx);
            if (u.fallsThrough()) {
                break;
            }
            u = null;
        }
        if (u == null || Rand.getInt(10) > weight) {
            return;
        }
        zeroheight.remove(idx);
        while (true) {
            if (zeroheight.size() <= (weight > 3 ? weight : 3)) {
                break;
            }
            zeroheight.remove(Rand.getInt(zeroheight.size()));
        }
        Collection<Local> locals = b.getLocals();
        List<Unit> targs = new ArrayList<>();
        targs.addAll(zeroheight);
        SootField[] ops = FieldRenamer.v().getRandomOpaques();
        Local b1 = Jimple.v().newLocal("addswitchesbool1", BooleanType.v());
        locals.add(b1);
        Local b2 = Jimple.v().newLocal("addswitchesbool2", BooleanType.v());
        locals.add(b2);
        if (ops[0].getType() instanceof PrimType) {
            units.insertBefore(Jimple.v().newAssignStmt(b1, Jimple.v().newStaticFieldRef(ops[0].makeRef())), u);
        } else {
            RefType rt = (RefType) ops[0].getType();
            SootMethod m = rt.getSootClass().getMethodByName("booleanValue");
            Local B = Jimple.v().newLocal("addswitchesBOOL1", rt);
            locals.add(B);
            units.insertBefore(Jimple.v().newAssignStmt(B, Jimple.v().newStaticFieldRef(ops[0].makeRef())), u);
            units.insertBefore(Jimple.v().newAssignStmt(b1, Jimple.v().newVirtualInvokeExpr(B, m.makeRef(), Collections.emptyList())), u);
        }
        if (ops[1].getType() instanceof PrimType) {
            units.insertBefore(Jimple.v().newAssignStmt(b2, Jimple.v().newStaticFieldRef(ops[1].makeRef())), u);
        } else {
            RefType rt2 = (RefType) ops[1].getType();
            SootMethod m2 = rt2.getSootClass().getMethodByName("booleanValue");
            Local B2 = Jimple.v().newLocal("addswitchesBOOL2", rt2);
            locals.add(B2);
            units.insertBefore(Jimple.v().newAssignStmt(B2, Jimple.v().newStaticFieldRef(ops[1].makeRef())), u);
            units.insertBefore(Jimple.v().newAssignStmt(b2, Jimple.v().newVirtualInvokeExpr(B2, m2.makeRef(), Collections.emptyList())), u);
        }
        IfStmt ifstmt = Jimple.v().newIfStmt(Jimple.v().newNeExpr(b1, b2), u);
        units.insertBefore(ifstmt, (IfStmt) u);
        Local l = Jimple.v().newLocal("addswitchlocal", IntType.v());
        locals.add(l);
        units.insertBeforeNoRedirect(Jimple.v().newAssignStmt(l, IntConstant.v(0)), first);
        units.insertAfter(Jimple.v().newTableSwitchStmt(l, 1, zeroheight.size(), targs, u), (Unit) ifstmt);
        this.switchesadded += zeroheight.size() + 1;
        for (Unit nxt : targs) {
            if (Rand.getInt(5) < 4) {
                units.insertBefore(Jimple.v().newAssignStmt(l, Jimple.v().newAddExpr(l, IntConstant.v(Rand.getInt(3) + 1))), nxt);
            }
        }
        ifstmt.setTarget(u);
    }
}
