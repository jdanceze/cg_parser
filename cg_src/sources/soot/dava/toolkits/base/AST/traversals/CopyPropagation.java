package soot.dava.toolkits.base.AST.traversals;

import java.util.ArrayList;
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
import soot.dava.toolkits.base.AST.structuredAnalysis.DavaFlowSet;
import soot.dava.toolkits.base.AST.structuredAnalysis.ReachingCopies;
import soot.jimple.DefinitionStmt;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/traversals/CopyPropagation.class */
public class CopyPropagation extends DepthFirstAdapter {
    public static boolean DEBUG = false;
    ASTNode AST;
    ASTUsesAndDefs useDefs;
    ReachingCopies reachingCopies;
    ASTParentNodeFinder parentOf;
    boolean someCopyStmtModified;
    boolean ASTMODIFIED;

    public CopyPropagation(ASTNode AST) {
        this.someCopyStmtModified = false;
        this.AST = AST;
        this.ASTMODIFIED = false;
        setup();
    }

    public CopyPropagation(boolean verbose, ASTNode AST) {
        super(verbose);
        this.someCopyStmtModified = false;
        this.AST = AST;
        this.ASTMODIFIED = false;
        setup();
    }

    private void setup() {
        if (DEBUG) {
            System.out.println("computing usesAnd Defs");
        }
        this.useDefs = new ASTUsesAndDefs(this.AST);
        this.AST.apply(this.useDefs);
        if (DEBUG) {
            System.out.println("computing usesAnd Defs....done");
        }
        this.reachingCopies = new ReachingCopies(this.AST);
        this.parentOf = new ASTParentNodeFinder();
        this.AST.apply(this.parentOf);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void outASTMethodNode(ASTMethodNode node) {
        if (this.ASTMODIFIED) {
            this.AST.apply(ClosestAbruptTargetFinder.v());
            CopyPropagation prop1 = new CopyPropagation(this.AST);
            this.AST.apply(prop1);
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTStatementSequenceNode(ASTStatementSequenceNode node) {
        for (AugmentedStmt as : node.getStatements()) {
            Stmt s = as.get_Stmt();
            if (isCopyStmt(s)) {
                handleCopyStmt((DefinitionStmt) s);
            }
        }
    }

    public boolean isCopyStmt(Stmt s) {
        if (!(s instanceof DefinitionStmt)) {
            return false;
        }
        Value leftOp = ((DefinitionStmt) s).getLeftOp();
        Value rightOp = ((DefinitionStmt) s).getRightOp();
        if ((leftOp instanceof Local) && (rightOp instanceof Local)) {
            return true;
        }
        return false;
    }

    public void handleCopyStmt(DefinitionStmt copyStmt) {
        Local definedLocal = (Local) copyStmt.getLeftOp();
        Object temp = this.useDefs.getDUChain(copyStmt);
        ArrayList uses = new ArrayList();
        if (temp != null) {
            uses = (ArrayList) temp;
        }
        if (uses.size() != 0) {
            if (DEBUG) {
                System.out.println(">>>>The defined local:" + definedLocal + " is used in the following");
                System.out.println("\n numof uses:" + uses.size() + uses + ">>>>>>>>>>>>>>>\n\n");
            }
            Local leftLocal = (Local) copyStmt.getLeftOp();
            Local rightLocal = (Local) copyStmt.getRightOp();
            ReachingCopies reachingCopies = this.reachingCopies;
            reachingCopies.getClass();
            ReachingCopies.LocalPair localPair = new ReachingCopies.LocalPair(leftLocal, rightLocal);
            Iterator useIt = uses.iterator();
            while (useIt.hasNext()) {
                DavaFlowSet reaching = this.reachingCopies.getReachingCopies(useIt.next());
                if (!reaching.contains(localPair)) {
                    return;
                }
            }
            Iterator useIt2 = uses.iterator();
            while (useIt2.hasNext()) {
                Object tempUse = useIt2.next();
                if (DEBUG) {
                    System.out.println("copy stmt reached this use" + tempUse);
                }
                replace(leftLocal, rightLocal, tempUse);
            }
            removeStmt(copyStmt);
            if (this.someCopyStmtModified) {
                setup();
                this.someCopyStmtModified = false;
                return;
            }
            return;
        }
        removeStmt(copyStmt);
    }

    public void removeStmt(Stmt stmt) {
        Object tempParent = this.parentOf.getParentOf(stmt);
        if (tempParent == null) {
            return;
        }
        ASTNode parent = (ASTNode) tempParent;
        if (!(parent instanceof ASTStatementSequenceNode)) {
            throw new RuntimeException("Found a stmt whose parent is not an ASTStatementSequenceNode");
        }
        ASTStatementSequenceNode parentNode = (ASTStatementSequenceNode) parent;
        ArrayList<AugmentedStmt> newSequence = new ArrayList<>();
        for (AugmentedStmt as : parentNode.getStatements()) {
            Stmt s = as.get_Stmt();
            if (s.toString().compareTo(stmt.toString()) != 0) {
                newSequence.add(as);
            }
        }
        parentNode.setStatements(newSequence);
        this.ASTMODIFIED = true;
    }

    public void modifyUses(Local from, Local to, ASTCondition cond) {
        if (cond instanceof ASTAggregatedCondition) {
            modifyUses(from, to, ((ASTAggregatedCondition) cond).getLeftOp());
            modifyUses(from, to, ((ASTAggregatedCondition) cond).getRightOp());
        } else if (cond instanceof ASTUnaryCondition) {
            Value val = ((ASTUnaryCondition) cond).getValue();
            if (val instanceof Local) {
                Local local = (Local) val;
                if (local.getName().compareTo(from.getName()) == 0) {
                    ((ASTUnaryCondition) cond).setValue(to);
                    this.ASTMODIFIED = true;
                    return;
                }
                return;
            }
            for (ValueBox valBox : val.getUseBoxes()) {
                Value tempVal = valBox.getValue();
                if (tempVal instanceof Local) {
                    Local local2 = (Local) tempVal;
                    if (local2.getName().compareTo(from.getName()) == 0) {
                        valBox.setValue(to);
                        this.ASTMODIFIED = true;
                    }
                }
            }
        } else if (cond instanceof ASTBinaryCondition) {
            for (ValueBox valBox2 : ((ASTBinaryCondition) cond).getConditionExpr().getUseBoxes()) {
                Value tempVal2 = valBox2.getValue();
                if (tempVal2 instanceof Local) {
                    Local local3 = (Local) tempVal2;
                    if (local3.getName().compareTo(from.getName()) == 0) {
                        valBox2.setValue(to);
                        this.ASTMODIFIED = true;
                    }
                }
            }
        } else {
            throw new RuntimeException("Method getUseList in CopyPropagation encountered unknown condition type");
        }
    }

    public void modifyUseBoxes(Local from, Local to, List useBoxes) {
        Iterator it = useBoxes.iterator();
        while (it.hasNext()) {
            ValueBox valBox = (ValueBox) it.next();
            Value tempVal = valBox.getValue();
            if (tempVal instanceof Local) {
                Local local = (Local) tempVal;
                if (local.getName().compareTo(from.getName()) == 0) {
                    valBox.setValue(to);
                    this.ASTMODIFIED = true;
                }
            }
        }
    }

    public void replace(Local from, Local to, Object use) {
        if (use instanceof Stmt) {
            Stmt s = (Stmt) use;
            if (isCopyStmt(s)) {
                this.someCopyStmtModified = true;
            }
            List useBoxes = s.getUseBoxes();
            if (DEBUG) {
                System.out.println("Printing uses for stmt" + useBoxes);
            }
            modifyUseBoxes(from, to, useBoxes);
        } else if (use instanceof ASTNode) {
            if (use instanceof ASTSwitchNode) {
                ASTSwitchNode temp = (ASTSwitchNode) use;
                Value val = temp.get_Key();
                if (val instanceof Local) {
                    if (((Local) val).getName().compareTo(from.getName()) == 0) {
                        this.ASTMODIFIED = true;
                        temp.set_Key(to);
                        return;
                    }
                    return;
                }
                modifyUseBoxes(from, to, val.getUseBoxes());
            } else if (use instanceof ASTSynchronizedBlockNode) {
                ASTSynchronizedBlockNode temp2 = (ASTSynchronizedBlockNode) use;
                Local local = temp2.getLocal();
                if (local.getName().compareTo(from.getName()) == 0) {
                    temp2.setLocal(to);
                    this.ASTMODIFIED = true;
                }
            } else if (use instanceof ASTIfNode) {
                if (DEBUG) {
                    System.out.println("Use is an instanceof if node");
                }
                ASTCondition cond = ((ASTIfNode) use).get_Condition();
                modifyUses(from, to, cond);
            } else if (use instanceof ASTIfElseNode) {
                ASTCondition cond2 = ((ASTIfElseNode) use).get_Condition();
                modifyUses(from, to, cond2);
            } else if (use instanceof ASTWhileNode) {
                ASTCondition cond3 = ((ASTWhileNode) use).get_Condition();
                modifyUses(from, to, cond3);
            } else if (use instanceof ASTDoWhileNode) {
                ASTCondition cond4 = ((ASTDoWhileNode) use).get_Condition();
                modifyUses(from, to, cond4);
            } else if (use instanceof ASTForLoopNode) {
                ASTForLoopNode temp3 = (ASTForLoopNode) use;
                for (AugmentedStmt as : temp3.getInit()) {
                    Stmt s2 = as.get_Stmt();
                    replace(from, to, s2);
                }
                for (AugmentedStmt as2 : temp3.getUpdate()) {
                    Stmt s3 = as2.get_Stmt();
                    replace(from, to, s3);
                }
                ASTCondition cond5 = temp3.get_Condition();
                modifyUses(from, to, cond5);
            } else {
                throw new RuntimeException("Encountered an unknown ASTNode in copyPropagation method replace");
            }
        } else {
            throw new RuntimeException("Encountered an unknown use in copyPropagation method replace");
        }
    }
}
