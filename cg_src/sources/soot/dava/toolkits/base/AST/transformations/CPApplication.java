package soot.dava.toolkits.base.AST.transformations;

import java.util.HashMap;
import java.util.List;
import soot.Local;
import soot.SootField;
import soot.Value;
import soot.ValueBox;
import soot.dava.internal.AST.ASTAggregatedCondition;
import soot.dava.internal.AST.ASTBinaryCondition;
import soot.dava.internal.AST.ASTCondition;
import soot.dava.internal.AST.ASTDoWhileNode;
import soot.dava.internal.AST.ASTForLoopNode;
import soot.dava.internal.AST.ASTIfElseNode;
import soot.dava.internal.AST.ASTIfNode;
import soot.dava.internal.AST.ASTMethodNode;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.AST.ASTSwitchNode;
import soot.dava.internal.AST.ASTUnaryCondition;
import soot.dava.internal.AST.ASTWhileNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.dava.toolkits.base.AST.structuredAnalysis.CP;
import soot.dava.toolkits.base.AST.structuredAnalysis.CPFlowSet;
import soot.dava.toolkits.base.AST.structuredAnalysis.CPHelper;
import soot.jimple.FieldRef;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/transformations/CPApplication.class */
public class CPApplication extends DepthFirstAdapter {
    CP cp;
    String className;

    public CPApplication(ASTMethodNode AST, HashMap<String, Object> constantValueFields, HashMap<String, SootField> classNameFieldNameToSootFieldMapping) {
        this.cp = null;
        this.className = null;
        this.className = AST.getDavaBody().getMethod().getDeclaringClass().getName();
        this.cp = new CP(AST, constantValueFields, classNameFieldNameToSootFieldMapping);
    }

    public CPApplication(boolean verbose, ASTMethodNode AST, HashMap<String, Object> constantValueFields, HashMap<String, SootField> classNameFieldNameToSootFieldMapping) {
        super(verbose);
        this.cp = null;
        this.className = null;
        this.className = AST.getDavaBody().getMethod().getDeclaringClass().getName();
        this.cp = new CP(AST, constantValueFields, classNameFieldNameToSootFieldMapping);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTSwitchNode(ASTSwitchNode node) {
        Value newValue;
        Value newValue2;
        Object obj = this.cp.getBeforeSet(node);
        if (obj == null || !(obj instanceof CPFlowSet)) {
            return;
        }
        CPFlowSet beforeSet = (CPFlowSet) obj;
        Value key = node.get_Key();
        if (key instanceof Local) {
            Local useLocal = (Local) key;
            Object value = beforeSet.contains(this.className, useLocal.toString());
            if (value != null && (newValue2 = CPHelper.createConstant(value)) != null) {
                node.set_Key(newValue2);
            }
        } else if (key instanceof FieldRef) {
            FieldRef useField = (FieldRef) key;
            SootField usedSootField = useField.getField();
            Object value2 = beforeSet.contains(usedSootField.getDeclaringClass().getName(), usedSootField.getName().toString());
            if (value2 != null && (newValue = CPHelper.createConstant(value2)) != null) {
                node.set_Key(newValue);
            }
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTForLoopNode(ASTForLoopNode node) {
        for (AugmentedStmt as : node.getInit()) {
            Stmt s = as.get_Stmt();
            List useBoxes = s.getUseBoxes();
            Object obj = this.cp.getBeforeSet(s);
            if (obj != null && (obj instanceof CPFlowSet)) {
                CPFlowSet beforeSet = (CPFlowSet) obj;
                substituteUses(useBoxes, beforeSet);
            }
        }
        Object obj2 = this.cp.getAfterSet(node);
        if (obj2 == null || !(obj2 instanceof CPFlowSet)) {
            return;
        }
        CPFlowSet afterSet = (CPFlowSet) obj2;
        ASTCondition cond = node.get_Condition();
        changedCondition(cond, afterSet);
        for (AugmentedStmt as2 : node.getUpdate()) {
            List useBoxes2 = as2.get_Stmt().getUseBoxes();
            substituteUses(useBoxes2, afterSet);
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTWhileNode(ASTWhileNode node) {
        Object obj = this.cp.getAfterSet(node);
        if (obj == null || !(obj instanceof CPFlowSet)) {
            return;
        }
        CPFlowSet afterSet = (CPFlowSet) obj;
        ASTCondition cond = node.get_Condition();
        changedCondition(cond, afterSet);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTDoWhileNode(ASTDoWhileNode node) {
        Object obj = this.cp.getAfterSet(node);
        if (obj == null || !(obj instanceof CPFlowSet)) {
            return;
        }
        CPFlowSet afterSet = (CPFlowSet) obj;
        ASTCondition cond = node.get_Condition();
        changedCondition(cond, afterSet);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTIfNode(ASTIfNode node) {
        Object obj = this.cp.getBeforeSet(node);
        if (obj == null || !(obj instanceof CPFlowSet)) {
            return;
        }
        CPFlowSet beforeSet = (CPFlowSet) obj;
        ASTCondition cond = node.get_Condition();
        changedCondition(cond, beforeSet);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTIfElseNode(ASTIfElseNode node) {
        Object obj = this.cp.getBeforeSet(node);
        if (obj == null || !(obj instanceof CPFlowSet)) {
            return;
        }
        CPFlowSet beforeSet = (CPFlowSet) obj;
        ASTCondition cond = node.get_Condition();
        changedCondition(cond, beforeSet);
    }

    public ASTCondition changedCondition(ASTCondition cond, CPFlowSet set) {
        Value newValue;
        Value newValue2;
        if (cond instanceof ASTAggregatedCondition) {
            ASTCondition left = changedCondition(((ASTAggregatedCondition) cond).getLeftOp(), set);
            ASTCondition right = changedCondition(((ASTAggregatedCondition) cond).getRightOp(), set);
            ((ASTAggregatedCondition) cond).setLeftOp(left);
            ((ASTAggregatedCondition) cond).setRightOp(right);
            return cond;
        } else if (cond instanceof ASTUnaryCondition) {
            Value val = ((ASTUnaryCondition) cond).getValue();
            if (val instanceof Local) {
                Object value = set.contains(this.className, ((Local) val).toString());
                if (value != null && (newValue2 = CPHelper.createConstant(value)) != null) {
                    ((ASTUnaryCondition) cond).setValue(newValue2);
                }
            } else if (val instanceof FieldRef) {
                FieldRef useField = (FieldRef) val;
                SootField usedSootField = useField.getField();
                Object value2 = set.contains(usedSootField.getDeclaringClass().getName(), usedSootField.getName().toString());
                if (value2 != null && (newValue = CPHelper.createConstant(value2)) != null) {
                    ((ASTUnaryCondition) cond).setValue(newValue);
                }
            } else {
                substituteUses(val.getUseBoxes(), set);
            }
            return cond;
        } else if (cond instanceof ASTBinaryCondition) {
            substituteUses(((ASTBinaryCondition) cond).getConditionExpr().getUseBoxes(), set);
            return cond;
        } else {
            throw new RuntimeException("Method getUseList in ASTUsesAndDefs encountered unknown condition type");
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTStatementSequenceNode(ASTStatementSequenceNode node) {
        for (AugmentedStmt as : node.getStatements()) {
            Stmt s = as.get_Stmt();
            List useBoxes = s.getUseBoxes();
            Object obj = this.cp.getBeforeSet(s);
            if (obj != null && (obj instanceof CPFlowSet)) {
                CPFlowSet beforeSet = (CPFlowSet) obj;
                substituteUses(useBoxes, beforeSet);
            }
        }
    }

    public void substituteUses(List useBoxes, CPFlowSet beforeSet) {
        Value newValue;
        Value newValue2;
        for (Object useObj : useBoxes) {
            Value use = ((ValueBox) useObj).getValue();
            if (use instanceof Local) {
                Local useLocal = (Local) use;
                Object value = beforeSet.contains(this.className, useLocal.toString());
                if (value != null && (newValue = CPHelper.createConstant(value)) != null) {
                    ((ValueBox) useObj).setValue(newValue);
                }
            } else if (use instanceof FieldRef) {
                FieldRef useField = (FieldRef) use;
                SootField usedSootField = useField.getField();
                Object value2 = beforeSet.contains(usedSootField.getDeclaringClass().getName(), usedSootField.getName().toString());
                if (value2 != null && (newValue2 = CPHelper.createConstant(value2)) != null) {
                    ((ValueBox) useObj).setValue(newValue2);
                }
            }
        }
    }
}
