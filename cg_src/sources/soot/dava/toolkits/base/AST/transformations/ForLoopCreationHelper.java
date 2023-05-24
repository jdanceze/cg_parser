package soot.dava.toolkits.base.AST.transformations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Value;
import soot.dava.internal.AST.ASTAggregatedCondition;
import soot.dava.internal.AST.ASTBinaryCondition;
import soot.dava.internal.AST.ASTCondition;
import soot.dava.internal.AST.ASTForLoopNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.AST.ASTUnaryCondition;
import soot.dava.internal.AST.ASTWhileNode;
import soot.dava.internal.SET.SETNodeLabel;
import soot.dava.internal.asg.AugmentedStmt;
import soot.jimple.ConditionExpr;
import soot.jimple.DefinitionStmt;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/transformations/ForLoopCreationHelper.class */
public class ForLoopCreationHelper {
    ASTStatementSequenceNode stmtSeqNode;
    ASTWhileNode whileNode;
    ASTStatementSequenceNode newStmtSeqNode;
    ASTForLoopNode forNode;
    List<AugmentedStmt> myStmts;
    boolean removeLast = false;
    Map<String, Integer> varToStmtMap = new HashMap();

    public ForLoopCreationHelper(ASTStatementSequenceNode stmtSeqNode, ASTWhileNode whileNode) {
        this.stmtSeqNode = stmtSeqNode;
        this.whileNode = whileNode;
    }

    public List<Object> createNewBody(List<Object> oldSubBody, int nodeNumber) {
        List<Object> newSubBody = new ArrayList<>();
        if (oldSubBody.size() <= nodeNumber) {
            return null;
        }
        Iterator<Object> oldIt = oldSubBody.iterator();
        for (int index = 0; index != nodeNumber; index++) {
            newSubBody.add(oldIt.next());
        }
        ASTNode temp = (ASTNode) oldIt.next();
        if (!(temp instanceof ASTStatementSequenceNode)) {
            return null;
        }
        ASTNode temp2 = (ASTNode) oldIt.next();
        if (!(temp2 instanceof ASTWhileNode)) {
            return null;
        }
        if (this.newStmtSeqNode != null) {
            newSubBody.add(this.newStmtSeqNode);
        }
        newSubBody.add(this.forNode);
        while (oldIt.hasNext()) {
            newSubBody.add(oldIt.next());
        }
        return newSubBody;
    }

    private List<String> getDefs() {
        if (this.stmtSeqNode == null) {
            return null;
        }
        List<String> toReturn = new ArrayList<>();
        int stmtNum = 0;
        for (AugmentedStmt as : this.stmtSeqNode.getStatements()) {
            Stmt s = as.get_Stmt();
            if (s instanceof DefinitionStmt) {
                Value left = ((DefinitionStmt) s).getLeftOp();
                toReturn.add(left.toString());
                this.varToStmtMap.put(left.toString(), new Integer(stmtNum));
            } else {
                toReturn = new ArrayList<>();
                this.varToStmtMap = new HashMap();
            }
            stmtNum++;
        }
        return toReturn;
    }

    private List<String> getCondUses() {
        if (this.whileNode == null) {
            return null;
        }
        ASTCondition cond = this.whileNode.get_Condition();
        return getCond(cond);
    }

    private List<String> getCond(ASTCondition cond) {
        List<String> toReturn = new ArrayList<>();
        if (cond instanceof ASTUnaryCondition) {
            toReturn.add(((ASTUnaryCondition) cond).toString());
        } else if (cond instanceof ASTBinaryCondition) {
            ConditionExpr condExpr = ((ASTBinaryCondition) cond).getConditionExpr();
            toReturn.add(condExpr.getOp1().toString());
            toReturn.add(condExpr.getOp2().toString());
        } else if (cond instanceof ASTAggregatedCondition) {
            toReturn.addAll(getCond(((ASTAggregatedCondition) cond).getLeftOp()));
            toReturn.addAll(getCond(((ASTAggregatedCondition) cond).getRightOp()));
        }
        return toReturn;
    }

    private List<String> getCommonVars(List<String> defs, List<String> condUses) {
        List<String> toReturn = new ArrayList<>();
        for (String defString : defs) {
            Iterator<String> condIt = condUses.iterator();
            while (true) {
                if (!condIt.hasNext()) {
                    break;
                }
                String condString = condIt.next();
                if (condString.compareTo(defString) == 0) {
                    toReturn.add(defString);
                    break;
                }
            }
        }
        return toReturn;
    }

    public boolean checkPattern() {
        List<String> condUses;
        List<String> commonVars;
        List<AugmentedStmt> update;
        List<String> defs = getDefs();
        if (defs == null || defs.size() == 0 || (condUses = getCondUses()) == null || condUses.size() == 0 || (update = getUpdate(defs, condUses, (commonVars = getCommonVars(defs, condUses)))) == null || update.size() == 0 || commonVars == null || commonVars.size() == 0) {
            return false;
        }
        List<AugmentedStmt> init = createNewStmtSeqNodeAndGetInit(commonVars);
        if (init.size() == 0) {
            return false;
        }
        ASTCondition condition = this.whileNode.get_Condition();
        List<Object> body = (List) this.whileNode.get_SubBodies().get(0);
        SETNodeLabel label = this.whileNode.get_Label();
        if (this.removeLast) {
            this.myStmts.remove(this.myStmts.size() - 1);
            this.removeLast = false;
        }
        this.forNode = new ASTForLoopNode(label, init, condition, update, body);
        return true;
    }

    private List<AugmentedStmt> getUpdate(List<String> defs, List<String> condUses, List<String> commonUses) {
        List<AugmentedStmt> toReturn = new ArrayList<>();
        List<Object> subBodies = this.whileNode.get_SubBodies();
        if (subBodies.size() != 1) {
            return toReturn;
        }
        List subBody = (List) subBodies.get(0);
        Iterator it = subBody.iterator();
        while (it.hasNext()) {
            ASTNode temp = (ASTNode) it.next();
            if (!it.hasNext()) {
                if (!(temp instanceof ASTStatementSequenceNode)) {
                    return null;
                }
                List<AugmentedStmt> stmts = ((ASTStatementSequenceNode) temp).getStatements();
                AugmentedStmt last = stmts.get(stmts.size() - 1);
                Stmt lastStmt = last.get_Stmt();
                if (!(lastStmt instanceof DefinitionStmt)) {
                    return null;
                }
                Value left = ((DefinitionStmt) lastStmt).getLeftOp();
                for (String defString : defs) {
                    if (left.toString().compareTo(defString) == 0) {
                        toReturn.add(last);
                        this.myStmts = stmts;
                        this.removeLast = true;
                        boolean matched = false;
                        for (String str : commonUses) {
                            if (defString.compareTo(str) == 0) {
                                matched = true;
                            }
                        }
                        if (!matched) {
                            commonUses.add(defString);
                        }
                        return toReturn;
                    }
                }
                for (String condString : condUses) {
                    if (left.toString().compareTo(condString) == 0) {
                        toReturn.add(last);
                        this.myStmts = stmts;
                        this.removeLast = true;
                        boolean matched2 = false;
                        for (String str2 : commonUses) {
                            if (condString.compareTo(str2) == 0) {
                                matched2 = true;
                            }
                        }
                        if (!matched2) {
                            commonUses.add(condString);
                        }
                        return toReturn;
                    }
                }
                continue;
            }
        }
        return toReturn;
    }

    private List<AugmentedStmt> createNewStmtSeqNodeAndGetInit(List<String> commonVars) {
        int currentLowestPosition = 999;
        for (String temp : commonVars) {
            Integer tempInt = this.varToStmtMap.get(temp);
            if (tempInt != null && tempInt.intValue() < currentLowestPosition) {
                currentLowestPosition = tempInt.intValue();
            }
        }
        List<AugmentedStmt> stmts = new ArrayList<>();
        List<AugmentedStmt> statements = this.stmtSeqNode.getStatements();
        Iterator<AugmentedStmt> stmtIt = statements.iterator();
        for (int stmtNum = 0; stmtNum < currentLowestPosition && stmtIt.hasNext(); stmtNum++) {
            stmts.add(stmtIt.next());
        }
        if (stmts.size() > 0) {
            this.newStmtSeqNode = new ASTStatementSequenceNode(stmts);
        } else {
            this.newStmtSeqNode = null;
        }
        List<AugmentedStmt> init = new ArrayList<>();
        while (stmtIt.hasNext()) {
            init.add(stmtIt.next());
        }
        return init;
    }
}
