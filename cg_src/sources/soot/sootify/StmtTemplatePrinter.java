package soot.sootify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import net.bytebuddy.implementation.auxiliary.TypeProxy;
import soot.PatchingChain;
import soot.Unit;
import soot.UnitBox;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.BreakpointStmt;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.GotoStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.IntConstant;
import soot.jimple.InvokeStmt;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.NopStmt;
import soot.jimple.RetStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.StmtSwitch;
import soot.jimple.TableSwitchStmt;
import soot.jimple.ThrowStmt;
/* loaded from: gencallgraphv3.jar:soot/sootify/StmtTemplatePrinter.class */
class StmtTemplatePrinter implements StmtSwitch {
    private final TemplatePrinter p;
    private final ValueTemplatePrinter vtp;
    private List<Unit> jumpTargets = new ArrayList();

    public StmtTemplatePrinter(TemplatePrinter templatePrinter, PatchingChain<Unit> units) {
        this.p = templatePrinter;
        this.vtp = new ValueTemplatePrinter(this.p);
        Iterator<Unit> it = units.iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            for (UnitBox ub : u.getUnitBoxes()) {
                this.jumpTargets.add(ub.getUnit());
            }
        }
        Collections.sort(this.jumpTargets, new Comparator<Unit>(units) { // from class: soot.sootify.StmtTemplatePrinter.1
            final List<Unit> unitsList;

            {
                this.unitsList = new ArrayList(units);
            }

            @Override // java.util.Comparator
            public int compare(Unit o1, Unit o2) {
                return this.unitsList.indexOf(o1) - this.unitsList.indexOf(o2);
            }
        });
        for (int i = 0; i < this.jumpTargets.size(); i++) {
            this.p.println("NopStmt jumpTarget" + i + "= Jimple.v().newNopStmt();");
        }
    }

    private String nameOfJumpTarget(Unit u) {
        if (!isJumpTarget(u)) {
            throw new InternalError("not a jumpt target! " + u);
        }
        return "jumpTarget" + this.jumpTargets.indexOf(u);
    }

    private boolean isJumpTarget(Unit u) {
        return this.jumpTargets.contains(u);
    }

    private String printValueAssignment(Value value, String varName) {
        return this.vtp.printValueAssignment(value, varName);
    }

    private void printStmt(Unit u, String... ops) {
        String stmtClassName = u.getClass().getSimpleName();
        if (stmtClassName.charAt(0) == 'J') {
            stmtClassName = stmtClassName.substring(1);
        }
        if (isJumpTarget(u)) {
            String nameOfJumpTarget = nameOfJumpTarget(u);
            this.p.println("units.add(" + nameOfJumpTarget + ");");
        }
        this.p.print("units.add(");
        printFactoryMethodCall(stmtClassName, ops);
        this.p.printlnNoIndent(");");
    }

    private void printFactoryMethodCall(String stmtClassName, String... ops) {
        this.p.printNoIndent("Jimple.v().new");
        this.p.printNoIndent(stmtClassName);
        this.p.printNoIndent("(");
        int i = 1;
        for (String op : ops) {
            this.p.printNoIndent(op);
            if (i < ops.length) {
                this.p.printNoIndent(",");
            }
            i++;
        }
        this.p.printNoIndent(")");
    }

    @Override // soot.jimple.StmtSwitch
    public void caseThrowStmt(ThrowStmt stmt) {
        String varName = printValueAssignment(stmt.getOp(), "op");
        printStmt(stmt, varName);
    }

    @Override // soot.jimple.StmtSwitch
    public void caseTableSwitchStmt(TableSwitchStmt stmt) {
        this.p.openBlock();
        String varName = printValueAssignment(stmt.getKey(), "key");
        int lowIndex = stmt.getLowIndex();
        this.p.println("int lowIndex=" + lowIndex + ";");
        int highIndex = stmt.getHighIndex();
        this.p.println("int highIndex=" + highIndex + ";");
        this.p.println("List<Unit> targets = new LinkedList<Unit>();");
        for (Unit s : stmt.getTargets()) {
            String nameOfJumpTarget = nameOfJumpTarget(s);
            this.p.println("targets.add(" + nameOfJumpTarget + ")");
        }
        Unit defaultTarget = stmt.getDefaultTarget();
        this.p.println("Unit defaultTarget = " + nameOfJumpTarget(defaultTarget) + ";");
        printStmt(stmt, varName, "lowIndex", "highIndex", "targets", "defaultTarget");
        this.p.closeBlock();
    }

    @Override // soot.jimple.StmtSwitch
    public void caseReturnVoidStmt(ReturnVoidStmt stmt) {
        printStmt(stmt, new String[0]);
    }

    @Override // soot.jimple.StmtSwitch
    public void caseReturnStmt(ReturnStmt stmt) {
        String varName = printValueAssignment(stmt.getOp(), "retVal");
        printStmt(stmt, varName);
    }

    @Override // soot.jimple.StmtSwitch
    public void caseRetStmt(RetStmt stmt) {
        String varName = printValueAssignment(stmt.getStmtAddress(), "stmtAddress");
        printStmt(stmt, varName);
    }

    @Override // soot.jimple.StmtSwitch
    public void caseNopStmt(NopStmt stmt) {
        printStmt(stmt, new String[0]);
    }

    @Override // soot.jimple.StmtSwitch
    public void caseLookupSwitchStmt(LookupSwitchStmt stmt) {
        this.p.openBlock();
        String keyVarName = printValueAssignment(stmt.getKey(), "key");
        this.p.println("List<IntConstant> lookupValues = new LinkedList<IntConstant>();");
        int i = 0;
        for (IntConstant c : stmt.getLookupValues()) {
            this.vtp.suggestVariableName("lookupValue" + i);
            c.apply(this.vtp);
            i++;
            this.p.println("lookupValues.add(lookupValue" + i + ");");
        }
        this.p.println("List<Unit> targets = new LinkedList<Unit>();");
        for (Unit u : stmt.getTargets()) {
            String nameOfJumpTarget = nameOfJumpTarget(u);
            this.p.println("targets.add(" + nameOfJumpTarget + ")");
        }
        Unit defaultTarget = stmt.getDefaultTarget();
        this.p.println("Unit defaultTarget=" + defaultTarget.toString() + ";");
        printStmt(stmt, keyVarName, "lookupValues", "targets", "defaultTarget");
        this.p.closeBlock();
    }

    @Override // soot.jimple.StmtSwitch
    public void caseInvokeStmt(InvokeStmt stmt) {
        String varName = printValueAssignment(stmt.getInvokeExpr(), "ie");
        printStmt(stmt, varName);
    }

    @Override // soot.jimple.StmtSwitch
    public void caseIfStmt(IfStmt stmt) {
        String varName = printValueAssignment(stmt.getCondition(), "condition");
        Unit target = stmt.getTarget();
        this.vtp.suggestVariableName(TypeProxy.INSTANCE_FIELD);
        String targetName = this.vtp.getLastAssignedVarName();
        this.p.println("Unit " + targetName + "=" + nameOfJumpTarget(target) + ";");
        printStmt(stmt, varName, targetName);
    }

    @Override // soot.jimple.StmtSwitch
    public void caseIdentityStmt(IdentityStmt stmt) {
        String varName = printValueAssignment(stmt.getLeftOp(), "lhs");
        String varName2 = printValueAssignment(stmt.getRightOp(), "idRef");
        printStmt(stmt, varName, varName2);
    }

    @Override // soot.jimple.StmtSwitch
    public void caseGotoStmt(GotoStmt stmt) {
        Unit target = stmt.getTarget();
        this.vtp.suggestVariableName(TypeProxy.INSTANCE_FIELD);
        String targetName = this.vtp.getLastAssignedVarName();
        this.p.println("Unit " + targetName + "=" + nameOfJumpTarget(target) + ";");
        printStmt(stmt, targetName);
    }

    @Override // soot.jimple.StmtSwitch
    public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
        String varName = printValueAssignment(stmt.getOp(), "monitor");
        printStmt(stmt, varName);
    }

    @Override // soot.jimple.StmtSwitch
    public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
        String varName = printValueAssignment(stmt.getOp(), "monitor");
        printStmt(stmt, varName);
    }

    @Override // soot.jimple.StmtSwitch
    public void caseBreakpointStmt(BreakpointStmt stmt) {
        printStmt(stmt, new String[0]);
    }

    @Override // soot.jimple.StmtSwitch
    public void caseAssignStmt(AssignStmt stmt) {
        String varName = printValueAssignment(stmt.getLeftOp(), "lhs");
        String varName2 = printValueAssignment(stmt.getRightOp(), "rhs");
        printStmt(stmt, varName, varName2);
    }

    @Override // soot.jimple.StmtSwitch
    public void defaultCase(Object obj) {
        throw new InternalError("should never be called");
    }
}
