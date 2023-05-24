package soot.jbco.bafTransformations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.PatchingChain;
import soot.RefType;
import soot.Trap;
import soot.Unit;
import soot.baf.Baf;
import soot.baf.GotoInst;
import soot.baf.JSRInst;
import soot.baf.LookupSwitchInst;
import soot.baf.PopInst;
import soot.baf.TableSwitchInst;
import soot.baf.TargetArgInst;
import soot.jbco.IJbcoTransform;
import soot.jbco.Main;
import soot.jbco.util.BodyBuilder;
import soot.jbco.util.Rand;
/* loaded from: gencallgraphv3.jar:soot/jbco/bafTransformations/AddJSRs.class */
public class AddJSRs extends BodyTransformer implements IJbcoTransform {
    int jsrcount = 0;
    private static final Logger logger = LoggerFactory.getLogger(AddJSRs.class);
    public static String[] dependancies = {"jtp.jbco_jl", "bb.jbco_cb2ji", "bb.jbco_ful", "bb.lp"};
    public static String name = "bb.jbco_cb2ji";

    @Override // soot.jbco.IJbcoTransform
    public String[] getDependencies() {
        return dependancies;
    }

    @Override // soot.jbco.IJbcoTransform
    public String getName() {
        return name;
    }

    @Override // soot.jbco.IJbcoTransform
    public void outputSummary() {
        logger.info("{} If/Gotos replaced with JSRs.", Integer.valueOf(this.jsrcount));
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        JSRInst ji;
        int weight = Main.getWeight(phaseName, b.getMethod().getSignature());
        if (weight == 0) {
            return;
        }
        boolean fallsthrough = false;
        HashMap<Trap, Unit> trapsToHandler = new HashMap<>();
        for (Trap t : b.getTraps()) {
            trapsToHandler.put(t, t.getHandlerUnit());
        }
        List<Unit> targets = new ArrayList<>();
        List<Unit> seenUts = new ArrayList<>();
        HashMap<Unit, List<Unit>> switches = new HashMap<>();
        HashMap<Unit, Unit> switchDefs = new HashMap<>();
        HashMap<TargetArgInst, Unit> ignoreJumps = new HashMap<>();
        PatchingChain<Unit> u = b.getUnits();
        Iterator<Unit> it = u.snapshotIterator();
        while (it.hasNext()) {
            Unit unit = it.next();
            if (unit instanceof TargetArgInst) {
                TargetArgInst ti = (TargetArgInst) unit;
                Unit tu = ti.getTarget();
                if (Rand.getInt(10) > weight) {
                    ignoreJumps.put(ti, tu);
                } else if (!targets.contains(tu)) {
                    targets.add(tu);
                }
            }
            if (unit instanceof TableSwitchInst) {
                TableSwitchInst ts = (TableSwitchInst) unit;
                switches.put(unit, new ArrayList<>(ts.getTargets()));
                switchDefs.put(unit, ts.getDefaultTarget());
            } else if (unit instanceof LookupSwitchInst) {
                LookupSwitchInst ls = (LookupSwitchInst) unit;
                switches.put(unit, new ArrayList<>(ls.getTargets()));
                switchDefs.put(unit, ls.getDefaultTarget());
            }
            seenUts.add(unit);
        }
        Iterator<Unit> it2 = u.snapshotIterator();
        ArrayList<Unit> processedLabels = new ArrayList<>();
        HashMap<Unit, JSRInst> builtJsrs = new HashMap<>();
        HashMap<Unit, Unit> popsBuilt = new HashMap<>();
        Unit unit2 = null;
        while (true) {
            Unit prev = unit2;
            if (!it2.hasNext()) {
                break;
            }
            Unit unit3 = it2.next();
            if (targets.contains(unit3)) {
                if (fallsthrough) {
                    JSRInst ji2 = Baf.v().newJSRInst(unit3);
                    builtJsrs.put(unit3, ji2);
                    u.insertAfter(ji2, (JSRInst) prev);
                    this.jsrcount++;
                }
                PopInst pop = Baf.v().newPopInst(RefType.v());
                u.insertBefore(pop, (PopInst) unit3);
                processedLabels.add(unit3);
                popsBuilt.put(pop, unit3);
            }
            fallsthrough = unit3.fallsThrough();
            unit2 = unit3;
        }
        Iterator<Unit> it3 = u.snapshotIterator();
        while (it3.hasNext()) {
            Unit unit4 = it3.next();
            if (!builtJsrs.containsValue(unit4) && (unit4 instanceof TargetArgInst) && !ignoreJumps.containsKey(unit4)) {
                TargetArgInst ti2 = (TargetArgInst) unit4;
                Unit tu2 = ti2.getTarget();
                if (!popsBuilt.containsKey(tu2)) {
                    throw new RuntimeException("It appears a target was found that was not updated with a POP.\n\"This makes no sense,\" said the bug as it flew through the code.");
                }
                JSRInst ji3 = builtJsrs.get(popsBuilt.get(tu2));
                if (BodyBuilder.isBafIf(unit4)) {
                    if (Rand.getInt(10) > weight) {
                        ti2.setTarget(popsBuilt.get(tu2));
                    } else if (ji3 != null) {
                        ti2.setTarget(ji3);
                    } else {
                        JSRInst ji4 = Baf.v().newJSRInst(tu2);
                        u.insertAfter(ji4, (JSRInst) u.getPredOf((PatchingChain<Unit>) tu2));
                        ti2.setTarget(ji4);
                        builtJsrs.put(popsBuilt.get(tu2), ji4);
                        this.jsrcount++;
                    }
                } else if (unit4 instanceof GotoInst) {
                    if (ji3 != null) {
                        if (Rand.getInt(10) < weight) {
                            ((GotoInst) unit4).setTarget(ji3);
                        } else {
                            ((GotoInst) unit4).setTarget(popsBuilt.get(tu2));
                        }
                    } else {
                        ((GotoInst) unit4).setTarget(popsBuilt.get(tu2));
                    }
                }
            }
        }
        for (Trap t2 : trapsToHandler.keySet()) {
            t2.setHandlerUnit(trapsToHandler.get(t2));
        }
        for (TargetArgInst ti3 : ignoreJumps.keySet()) {
            if (popsBuilt.containsKey(ti3.getTarget())) {
                ti3.setTarget(popsBuilt.get(ti3.getTarget()));
            }
        }
        targets.clear();
        Iterator<Unit> it4 = u.snapshotIterator();
        while (it4.hasNext()) {
            Unit unit5 = it4.next();
            if (unit5 instanceof TargetArgInst) {
                Unit targ = ((TargetArgInst) unit5).getTarget();
                if (!targets.contains(targ)) {
                    targets.add(targ);
                }
            }
        }
        for (Unit pop2 : popsBuilt.keySet()) {
            if (!targets.contains(pop2)) {
                u.remove(pop2);
            }
        }
        for (Unit sw : switches.keySet()) {
            List<Unit> targs = switches.get(sw);
            for (int i = 0; i < targs.size(); i++) {
                if (Rand.getInt(10) <= weight && (ji = builtJsrs.get(targs.get(i))) != null) {
                    targs.set(i, ji);
                }
            }
            Unit def = switchDefs.get(sw);
            if (Rand.getInt(10) < weight && builtJsrs.get(def) != null) {
                def = builtJsrs.get(def);
            }
            if (sw instanceof TableSwitchInst) {
                ((TableSwitchInst) sw).setTargets(targs);
                ((TableSwitchInst) sw).setDefaultTarget(def);
            } else if (sw instanceof LookupSwitchInst) {
                ((LookupSwitchInst) sw).setTargets(targs);
                ((LookupSwitchInst) sw).setDefaultTarget(def);
            }
        }
        if (debug) {
            StackTypeHeightCalculator.calculateStackHeights(b);
        }
    }
}
