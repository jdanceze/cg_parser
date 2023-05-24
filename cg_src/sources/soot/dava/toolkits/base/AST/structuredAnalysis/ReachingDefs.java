package soot.dava.toolkits.base.AST.structuredAnalysis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.Local;
import soot.Value;
import soot.dava.internal.AST.ASTDoWhileNode;
import soot.dava.internal.AST.ASTForLoopNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTUnaryBinaryCondition;
import soot.dava.internal.AST.ASTUnconditionalLoopNode;
import soot.dava.internal.AST.ASTWhileNode;
import soot.dava.toolkits.base.AST.traversals.AllDefinitionsFinder;
import soot.jimple.DefinitionStmt;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/structuredAnalysis/ReachingDefs.class */
public class ReachingDefs extends StructuredAnalysis<Stmt> {
    Object toAnalyze;

    public ReachingDefs(Object analyze) {
        this.toAnalyze = analyze;
        process(analyze, new DavaFlowSet());
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public DavaFlowSet<Stmt> emptyFlowSet() {
        return new DavaFlowSet<>();
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public DavaFlowSet<Stmt> newInitialFlow() {
        DavaFlowSet<Stmt> initial = new DavaFlowSet<>();
        AllDefinitionsFinder defFinder = new AllDefinitionsFinder();
        ((ASTNode) this.toAnalyze).apply(defFinder);
        List<DefinitionStmt> allDefs = defFinder.getAllDefs();
        for (DefinitionStmt def : allDefs) {
            initial.add(def);
        }
        return initial;
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public void setMergeType() {
        this.MERGETYPE = 1;
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public DavaFlowSet<Stmt> cloneFlowSet(DavaFlowSet<Stmt> flowSet) {
        return flowSet.mo2534clone();
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public DavaFlowSet<Stmt> processUnaryBinaryCondition(ASTUnaryBinaryCondition cond, DavaFlowSet<Stmt> inSet) {
        return inSet;
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public DavaFlowSet<Stmt> processSynchronizedLocal(Local local, DavaFlowSet<Stmt> inSet) {
        return inSet;
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public DavaFlowSet<Stmt> processSwitchKey(Value key, DavaFlowSet<Stmt> inSet) {
        return inSet;
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.StructuredAnalysis
    public DavaFlowSet<Stmt> processStatement(Stmt s, DavaFlowSet<Stmt> inSet) {
        if (inSet == this.NOPATH) {
            return inSet;
        }
        if (s instanceof DefinitionStmt) {
            DavaFlowSet<Stmt> toReturn = cloneFlowSet(inSet);
            Value leftOp = ((DefinitionStmt) s).getLeftOp();
            if (leftOp instanceof Local) {
                kill(toReturn, (Local) leftOp);
                gen(toReturn, (DefinitionStmt) s);
                return toReturn;
            }
        }
        return inSet;
    }

    public void gen(DavaFlowSet<Stmt> in, DefinitionStmt s) {
        in.add(s);
    }

    public void kill(DavaFlowSet<Stmt> in, Local redefined) {
        String redefinedLocalName = redefined.getName();
        Iterator<Stmt> listIt = in.iterator();
        while (listIt.hasNext()) {
            DefinitionStmt tempStmt = (DefinitionStmt) listIt.next();
            Value leftOp = tempStmt.getLeftOp();
            if (leftOp instanceof Local) {
                String storedLocalName = ((Local) leftOp).getName();
                if (redefinedLocalName.compareTo(storedLocalName) == 0) {
                    listIt.remove();
                }
            }
        }
    }

    public List<DefinitionStmt> getReachingDefs(Local local, Object node) {
        DavaFlowSet<Stmt> beforeSet;
        ArrayList<DefinitionStmt> toReturn = new ArrayList<>();
        if ((node instanceof ASTWhileNode) || (node instanceof ASTDoWhileNode) || (node instanceof ASTUnconditionalLoopNode) || (node instanceof ASTForLoopNode)) {
            beforeSet = getAfterSet(node);
        } else {
            beforeSet = getBeforeSet(node);
        }
        if (beforeSet == null) {
            throw new RuntimeException("Could not get reaching defs of node");
        }
        Iterator<Stmt> it = beforeSet.iterator();
        while (it.hasNext()) {
            Object temp = it.next();
            if (!(temp instanceof DefinitionStmt)) {
                throw new RuntimeException("Not an instanceof DefinitionStmt" + temp);
            }
            DefinitionStmt stmt = (DefinitionStmt) temp;
            Value leftOp = stmt.getLeftOp();
            if (leftOp.toString().compareTo(local.toString()) == 0) {
                toReturn.add(stmt);
            }
        }
        return toReturn;
    }

    public void reachingDefsToString(Object node) {
        DavaFlowSet<Stmt> beforeSet;
        if ((node instanceof ASTWhileNode) || (node instanceof ASTDoWhileNode) || (node instanceof ASTUnconditionalLoopNode) || (node instanceof ASTForLoopNode)) {
            beforeSet = getAfterSet(node);
        } else {
            beforeSet = getBeforeSet(node);
        }
        if (beforeSet == null) {
            throw new RuntimeException("Could not get reaching defs of node");
        }
        Iterator<Stmt> it = beforeSet.iterator();
        while (it.hasNext()) {
            Object o = it.next();
            System.out.println("Reaching def:" + o);
        }
    }
}
