package soot.grimp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.Local;
import soot.PackManager;
import soot.SootMethod;
import soot.Trap;
import soot.Unit;
import soot.ValueBox;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.AssignStmt;
import soot.jimple.BreakpointStmt;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.GotoStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.InvokeStmt;
import soot.jimple.JimpleBody;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.NopStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.Stmt;
import soot.jimple.StmtBody;
import soot.jimple.TableSwitchStmt;
import soot.jimple.ThrowStmt;
import soot.jimple.internal.StmtBox;
import soot.options.Options;
import soot.tagkit.LineNumberTag;
import soot.tagkit.SourceLnPosTag;
import soot.tagkit.Tag;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/grimp/GrimpBody.class */
public class GrimpBody extends StmtBody {
    private static final Logger logger = LoggerFactory.getLogger(GrimpBody.class);

    /* JADX INFO: Access modifiers changed from: package-private */
    public GrimpBody(SootMethod m) {
        super(m);
    }

    @Override // soot.Body
    public Object clone() {
        Body b = Grimp.v().newBody(getMethodUnsafe());
        b.importBodyContentsFrom(this);
        return b;
    }

    @Override // soot.Body
    public Object clone(boolean noLocalsClone) {
        Body b = Grimp.v().newBody(getMethod());
        b.importBodyContentsFrom(this, true);
        return b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public GrimpBody(Body body) {
        super(body.getMethod());
        if (Options.v().verbose()) {
            logger.debug("[" + getMethod().getName() + "] Constructing GrimpBody...");
        }
        if (!(body instanceof JimpleBody)) {
            throw new RuntimeException("Can only construct GrimpBody's from JimpleBody's (for now)");
        }
        JimpleBody jBody = (JimpleBody) body;
        Chain<Local> thisLocals = getLocals();
        for (Local loc : jBody.getLocals()) {
            thisLocals.add(loc);
        }
        final Grimp grimp = Grimp.v();
        Chain<Unit> thisUnits = getUnits();
        final HashMap<Stmt, Stmt> oldToNew = new HashMap<>((thisUnits.size() * 2) + 1, 0.7f);
        ArrayList<Unit> updates = new ArrayList<>();
        Iterator<Unit> it = jBody.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            Stmt oldStmt = (Stmt) u;
            final StmtBox newStmtBox = (StmtBox) grimp.newStmtBox(null);
            final StmtBox updateStmtBox = (StmtBox) grimp.newStmtBox(null);
            oldStmt.apply(new AbstractStmtSwitch() { // from class: soot.grimp.GrimpBody.1
                @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
                public void caseAssignStmt(AssignStmt s) {
                    newStmtBox.setUnit(grimp.newAssignStmt(s));
                }

                @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
                public void caseIdentityStmt(IdentityStmt s) {
                    newStmtBox.setUnit(grimp.newIdentityStmt(s));
                }

                @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
                public void caseBreakpointStmt(BreakpointStmt s) {
                    newStmtBox.setUnit(grimp.newBreakpointStmt(s));
                }

                @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
                public void caseInvokeStmt(InvokeStmt s) {
                    newStmtBox.setUnit(grimp.newInvokeStmt(s));
                }

                @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
                public void caseEnterMonitorStmt(EnterMonitorStmt s) {
                    newStmtBox.setUnit(grimp.newEnterMonitorStmt(s));
                }

                @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
                public void caseExitMonitorStmt(ExitMonitorStmt s) {
                    newStmtBox.setUnit(grimp.newExitMonitorStmt(s));
                }

                @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
                public void caseGotoStmt(GotoStmt s) {
                    newStmtBox.setUnit(grimp.newGotoStmt(s));
                    updateStmtBox.setUnit(s);
                }

                @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
                public void caseIfStmt(IfStmt s) {
                    newStmtBox.setUnit(grimp.newIfStmt(s));
                    updateStmtBox.setUnit(s);
                }

                @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
                public void caseLookupSwitchStmt(LookupSwitchStmt s) {
                    newStmtBox.setUnit(grimp.newLookupSwitchStmt(s));
                    updateStmtBox.setUnit(s);
                }

                @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
                public void caseNopStmt(NopStmt s) {
                    newStmtBox.setUnit(grimp.newNopStmt(s));
                }

                @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
                public void caseReturnStmt(ReturnStmt s) {
                    newStmtBox.setUnit(grimp.newReturnStmt(s));
                }

                @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
                public void caseReturnVoidStmt(ReturnVoidStmt s) {
                    newStmtBox.setUnit(grimp.newReturnVoidStmt(s));
                }

                @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
                public void caseTableSwitchStmt(TableSwitchStmt s) {
                    newStmtBox.setUnit(grimp.newTableSwitchStmt(s));
                    updateStmtBox.setUnit(s);
                }

                @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
                public void caseThrowStmt(ThrowStmt s) {
                    newStmtBox.setUnit(grimp.newThrowStmt(s));
                }
            });
            Stmt newStmt = (Stmt) newStmtBox.getUnit();
            for (ValueBox box : newStmt.getUseBoxes()) {
                box.setValue(grimp.newExpr(box.getValue()));
            }
            for (ValueBox box2 : newStmt.getDefBoxes()) {
                box2.setValue(grimp.newExpr(box2.getValue()));
            }
            thisUnits.add(newStmt);
            oldToNew.put(oldStmt, newStmt);
            if (updateStmtBox.getUnit() != null) {
                updates.add(updateStmtBox.getUnit());
            }
            Tag lnTag = oldStmt.getTag(LineNumberTag.NAME);
            if (lnTag != null) {
                newStmt.addTag(lnTag);
            }
            Tag slpTag = oldStmt.getTag(SourceLnPosTag.NAME);
            if (slpTag != null) {
                newStmt.addTag(slpTag);
            }
        }
        AbstractStmtSwitch tgtUpdateSwitch = new AbstractStmtSwitch() { // from class: soot.grimp.GrimpBody.2
            @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
            public void caseGotoStmt(GotoStmt s) {
                GotoStmt newStmt2 = (GotoStmt) oldToNew.get(s);
                newStmt2.setTarget((Unit) oldToNew.get((Stmt) newStmt2.getTarget()));
            }

            @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
            public void caseIfStmt(IfStmt s) {
                IfStmt newStmt2 = (IfStmt) oldToNew.get(s);
                newStmt2.setTarget((Unit) oldToNew.get(newStmt2.getTarget()));
            }

            @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
            public void caseLookupSwitchStmt(LookupSwitchStmt s) {
                LookupSwitchStmt newStmt2 = (LookupSwitchStmt) oldToNew.get(s);
                newStmt2.setDefaultTarget((Unit) oldToNew.get((Stmt) newStmt2.getDefaultTarget()));
                Unit[] newTargList = new Unit[newStmt2.getTargetCount()];
                for (int i = 0; i < newTargList.length; i++) {
                    newTargList[i] = (Unit) oldToNew.get((Stmt) newStmt2.getTarget(i));
                }
                newStmt2.setTargets(newTargList);
            }

            @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
            public void caseTableSwitchStmt(TableSwitchStmt s) {
                TableSwitchStmt newStmt2 = (TableSwitchStmt) oldToNew.get(s);
                newStmt2.setDefaultTarget((Unit) oldToNew.get((Stmt) newStmt2.getDefaultTarget()));
                int tc = (newStmt2.getHighIndex() - newStmt2.getLowIndex()) + 1;
                LinkedList<Unit> newTargList = new LinkedList<>();
                for (int i = 0; i < tc; i++) {
                    newTargList.add((Unit) oldToNew.get((Stmt) newStmt2.getTarget(i)));
                }
                newStmt2.setTargets(newTargList);
            }
        };
        Iterator<Unit> it2 = updates.iterator();
        while (it2.hasNext()) {
            Unit u2 = it2.next();
            u2.apply(tgtUpdateSwitch);
        }
        Chain<Trap> thisTraps = getTraps();
        for (Trap oldTrap : jBody.getTraps()) {
            thisTraps.add(grimp.newTrap(oldTrap.getException(), oldToNew.get((Stmt) oldTrap.getBeginUnit()), oldToNew.get((Stmt) oldTrap.getEndUnit()), oldToNew.get((Stmt) oldTrap.getHandlerUnit())));
        }
        PackManager.v().getPack("gb").apply(this);
    }
}
