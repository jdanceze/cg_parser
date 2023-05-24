package soot.dava.toolkits.base.AST.traversals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import soot.Local;
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
import soot.dava.toolkits.base.AST.structuredAnalysis.ReachingDefs;
import soot.jimple.DefinitionStmt;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/traversals/ASTUsesAndDefs.class */
public class ASTUsesAndDefs extends DepthFirstAdapter {
    public static boolean DEBUG = false;
    HashMap<Object, List<DefinitionStmt>> uD;
    HashMap<Object, List> dU;
    ReachingDefs reaching;

    public ASTUsesAndDefs(ASTNode AST) {
        this.uD = new HashMap<>();
        this.dU = new HashMap<>();
        this.reaching = new ReachingDefs(AST);
    }

    public ASTUsesAndDefs(boolean verbose, ASTNode AST) {
        super(verbose);
        this.uD = new HashMap<>();
        this.dU = new HashMap<>();
        this.reaching = new ReachingDefs(AST);
    }

    private List<Value> getUsesFromBoxes(List useBoxes) {
        ArrayList<Value> toReturn = new ArrayList<>();
        Iterator it = useBoxes.iterator();
        while (it.hasNext()) {
            Value val = ((ValueBox) it.next()).getValue();
            if (val instanceof Local) {
                toReturn.add(val);
            }
        }
        return toReturn;
    }

    public void checkStatementUses(Stmt s, Object useNodeOrStatement) {
        List useBoxes = s.getUseBoxes();
        List<Value> uses = getUsesFromBoxes(useBoxes);
        Iterator<Value> it = uses.iterator();
        while (it.hasNext()) {
            Local local = (Local) it.next();
            createUDDUChain(local, useNodeOrStatement);
        }
        if ((s instanceof DefinitionStmt) && this.dU.get(s) == null) {
            this.dU.put(s, new ArrayList());
        }
    }

    public void createUDDUChain(Local local, Object useNodeOrStatement) {
        List<Object> list;
        List<DefinitionStmt> reachingDefs = this.reaching.getReachingDefs(local, useNodeOrStatement);
        if (DEBUG) {
            System.out.println("Reaching def for:" + local + " are:" + reachingDefs);
        }
        Object tempObj = this.uD.get(useNodeOrStatement);
        if (tempObj != null) {
            List<DefinitionStmt> tempList = (List) tempObj;
            tempList.addAll(reachingDefs);
            this.uD.put(useNodeOrStatement, tempList);
        } else {
            this.uD.put(useNodeOrStatement, reachingDefs);
        }
        for (Object defStmt : reachingDefs) {
            List<Object> useObj = this.dU.get(defStmt);
            if (useObj == null) {
                list = new ArrayList<>();
            } else {
                list = useObj;
            }
            List<Object> uses = list;
            uses.add(useNodeOrStatement);
            this.dU.put(defStmt, uses);
        }
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
            if (val instanceof Local) {
                if (DEBUG) {
                    System.out.println("adding local from unary condition as a use" + val);
                }
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

    public void checkConditionalUses(ASTCondition cond, ASTNode node) {
        List<Value> useList = getUseList(cond);
        Iterator<Value> it = useList.iterator();
        while (it.hasNext()) {
            Local local = (Local) it.next();
            createUDDUChain(local, node);
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTSwitchNode(ASTSwitchNode node) {
        Value val = node.get_Key();
        List<Value> uses = new ArrayList<>();
        if (val instanceof Local) {
            uses.add(val);
        } else {
            List useBoxes = val.getUseBoxes();
            uses = getUsesFromBoxes(useBoxes);
        }
        Iterator<Value> it = uses.iterator();
        while (it.hasNext()) {
            Local local = (Local) it.next();
            createUDDUChain(local, node);
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTSynchronizedBlockNode(ASTSynchronizedBlockNode node) {
        Local local = node.getLocal();
        createUDDUChain(local, node);
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

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTStatementSequenceNode(ASTStatementSequenceNode node) {
        for (AugmentedStmt as : node.getStatements()) {
            Stmt s = as.get_Stmt();
            checkStatementUses(s, s);
        }
    }

    public List getUDChain(Object node) {
        return this.uD.get(node);
    }

    public List getDUChain(Object node) {
        return this.dU.get(node);
    }

    public HashMap<Object, List> getDUHashMap() {
        return this.dU;
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void outASTMethodNode(ASTMethodNode node) {
    }

    public void print() {
        System.out.println("\n\n\nPRINTING uD dU CHAINS ______________________________");
        Iterator<Object> it = this.dU.keySet().iterator();
        while (it.hasNext()) {
            DefinitionStmt s = (DefinitionStmt) it.next();
            System.out.println("*****The def  " + s + " has following uses:");
            Object obj = this.dU.get(s);
            if (obj != null) {
                ArrayList list = (ArrayList) obj;
                Iterator tempIt = list.iterator();
                while (tempIt.hasNext()) {
                    Object tempUse = tempIt.next();
                    System.out.println("-----------Use  " + tempUse);
                    System.out.println("----------------Defs of this use:   " + this.uD.get(tempUse));
                }
            }
        }
        System.out.println("END --------PRINTING uD dU CHAINS ______________________________");
    }
}
