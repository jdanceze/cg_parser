package soot.dava.toolkits.base.AST.traversals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.AST.ASTSwitchNode;
import soot.dava.internal.AST.ASTSynchronizedBlockNode;
import soot.dava.internal.AST.ASTUnaryCondition;
import soot.dava.internal.AST.ASTWhileNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.jimple.FieldRef;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/traversals/AllVariableUses.class */
public class AllVariableUses extends DepthFirstAdapter {
    ASTMethodNode methodNode;
    HashMap<Local, List> localsToUses;
    HashMap<SootField, List> fieldsToUses;

    public AllVariableUses(ASTMethodNode node) {
        this.methodNode = node;
        init();
    }

    public AllVariableUses(boolean verbose, ASTMethodNode node) {
        super(verbose);
        this.methodNode = node;
        init();
    }

    public void init() {
        this.localsToUses = new HashMap<>();
        this.fieldsToUses = new HashMap<>();
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTSynchronizedBlockNode(ASTSynchronizedBlockNode node) {
        Local local = node.getLocal();
        addLocalUse(local, node);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTSwitchNode(ASTSwitchNode node) {
        Value val = node.get_Key();
        List<Value> localUses = new ArrayList<>();
        List<Value> fieldUses = new ArrayList<>();
        if (val instanceof Local) {
            localUses.add(val);
            System.out.println("Added " + val + " to local uses for switch");
        } else if (val instanceof FieldRef) {
            fieldUses.add(val);
            System.out.println("Added " + val + " to field uses for switch");
        } else {
            List useBoxes = val.getUseBoxes();
            List<Value> localsOrFieldRefs = getUsesFromBoxes(useBoxes);
            for (Value temp : localsOrFieldRefs) {
                if (temp instanceof Local) {
                    localUses.add(temp);
                    System.out.println("Added " + temp + " to local uses for switch");
                } else if (temp instanceof FieldRef) {
                    fieldUses.add(temp);
                    System.out.println("Added " + temp + " to field uses for switch");
                }
            }
        }
        Iterator<Value> it = localUses.iterator();
        while (it.hasNext()) {
            Local local = (Local) it.next();
            addLocalUse(local, node);
        }
        Iterator<Value> it2 = fieldUses.iterator();
        while (it2.hasNext()) {
            FieldRef field = (FieldRef) it2.next();
            SootField sootField = field.getField();
            addFieldUse(sootField, node);
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTStatementSequenceNode(ASTStatementSequenceNode node) {
        for (AugmentedStmt as : node.getStatements()) {
            Stmt s = as.get_Stmt();
            checkStatementUses(s, s);
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTForLoopNode(ASTForLoopNode node) {
        for (AugmentedStmt as : node.getInit()) {
            Stmt s = as.get_Stmt();
            checkStatementUses(s, node);
        }
        ASTCondition cond = node.get_Condition();
        checkConditionalUses(cond, node);
        for (AugmentedStmt as2 : node.getUpdate()) {
            Stmt s2 = as2.get_Stmt();
            checkStatementUses(s2, node);
        }
    }

    public void checkStatementUses(Stmt s, Object useNodeOrStatement) {
        List useBoxes = s.getUseBoxes();
        List<Value> uses = getUsesFromBoxes(useBoxes);
        for (Value temp : uses) {
            if (temp instanceof Local) {
                addLocalUse((Local) temp, useNodeOrStatement);
            } else if (temp instanceof FieldRef) {
                FieldRef field = (FieldRef) temp;
                SootField sootField = field.getField();
                addFieldUse(sootField, useNodeOrStatement);
            }
        }
    }

    public void checkConditionalUses(ASTCondition cond, ASTNode node) {
        List<Value> useList = getUseList(cond);
        for (Value temp : useList) {
            if (temp instanceof Local) {
                addLocalUse((Local) temp, node);
            } else if (temp instanceof FieldRef) {
                FieldRef field = (FieldRef) temp;
                SootField sootField = field.getField();
                addFieldUse(sootField, node);
            }
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTIfNode(ASTIfNode node) {
        ASTCondition cond = node.get_Condition();
        checkConditionalUses(cond, node);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTIfElseNode(ASTIfElseNode node) {
        ASTCondition cond = node.get_Condition();
        checkConditionalUses(cond, node);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTWhileNode(ASTWhileNode node) {
        ASTCondition cond = node.get_Condition();
        checkConditionalUses(cond, node);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTDoWhileNode(ASTDoWhileNode node) {
        ASTCondition cond = node.get_Condition();
        checkConditionalUses(cond, node);
    }

    public List<Value> getUseList(ASTCondition cond) {
        ArrayList<Value> useList = new ArrayList<>();
        if (cond instanceof ASTAggregatedCondition) {
            useList.addAll(getUseList(((ASTAggregatedCondition) cond).getLeftOp()));
            useList.addAll(getUseList(((ASTAggregatedCondition) cond).getRightOp()));
            return useList;
        } else if (cond instanceof ASTUnaryCondition) {
            List<Value> uses = new ArrayList<>();
            Value val = ((ASTUnaryCondition) cond).getValue();
            if ((val instanceof Local) || (val instanceof FieldRef)) {
                uses.add(val);
            } else {
                List useBoxes = val.getUseBoxes();
                uses = getUsesFromBoxes(useBoxes);
            }
            return uses;
        } else if (cond instanceof ASTBinaryCondition) {
            List useBoxes2 = ((ASTBinaryCondition) cond).getConditionExpr().getUseBoxes();
            return getUsesFromBoxes(useBoxes2);
        } else {
            throw new RuntimeException("Method getUseList in ASTUsesAndDefs encountered unknown condition type");
        }
    }

    private void addLocalUse(Local local, Object obj) {
        List<Object> uses;
        List<Object> temp = this.localsToUses.get(local);
        if (temp == null) {
            uses = new ArrayList<>();
        } else {
            uses = (ArrayList) temp;
        }
        uses.add(obj);
        this.localsToUses.put(local, uses);
    }

    private void addFieldUse(SootField field, Object obj) {
        List<Object> uses;
        List<Object> temp = this.fieldsToUses.get(field);
        if (temp == null) {
            uses = new ArrayList<>();
        } else {
            uses = (ArrayList) temp;
        }
        uses.add(obj);
        this.fieldsToUses.put(field, uses);
    }

    private List<Value> getUsesFromBoxes(List useBoxes) {
        ArrayList<Value> toReturn = new ArrayList<>();
        Iterator it = useBoxes.iterator();
        while (it.hasNext()) {
            Value val = ((ValueBox) it.next()).getValue();
            if ((val instanceof Local) || (val instanceof FieldRef)) {
                toReturn.add(val);
            }
        }
        return toReturn;
    }

    public List getUsesForField(SootField field) {
        Object temp = this.fieldsToUses.get(field);
        if (temp == null) {
            return null;
        }
        return (List) temp;
    }

    public List getUsesForLocal(Local local) {
        Object temp = this.localsToUses.get(local);
        if (temp == null) {
            return null;
        }
        return (List) temp;
    }
}
