package soot.dava.toolkits.base.AST.transformations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.G;
import soot.Local;
import soot.Type;
import soot.Value;
import soot.ValueBox;
import soot.dava.DecompilationException;
import soot.dava.internal.AST.ASTMethodNode;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.javaRep.DArrayInitExpr;
import soot.dava.internal.javaRep.DArrayInitValueBox;
import soot.dava.internal.javaRep.DAssignStmt;
import soot.dava.internal.javaRep.DShortcutAssignStmt;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.dava.toolkits.base.AST.traversals.InitializationDeclarationShortcut;
import soot.jimple.ArrayRef;
import soot.jimple.DefinitionStmt;
import soot.jimple.IntConstant;
import soot.jimple.NewArrayExpr;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/transformations/ShortcutArrayInit.class */
public class ShortcutArrayInit extends DepthFirstAdapter {
    public static boolean DEBUG = false;
    ASTMethodNode methodNode;

    public ShortcutArrayInit() {
    }

    public ShortcutArrayInit(boolean verbose) {
        super(verbose);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTMethodNode(ASTMethodNode node) {
        this.methodNode = node;
    }

    public void debug(String msg) {
        if (DEBUG) {
            System.out.println("[SHortcutArrayInit]  DEBUG" + msg);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:44:0x01bb, code lost:
        if (r7 == false) goto L74;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x01be, code lost:
        r0 = new soot.dava.internal.javaRep.DArrayInitExpr(r0, r0);
        r0 = new soot.dava.internal.javaRep.DArrayInitValueBox(r0);
        r0 = new soot.dava.internal.javaRep.DAssignStmt(r0.getLeftOpBox(), r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x01eb, code lost:
        if (soot.dava.toolkits.base.AST.transformations.ShortcutArrayInit.DEBUG == false) goto L49;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x01ee, code lost:
        java.lang.System.out.println("Created new DAssignStmt and replacing it");
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x01f6, code lost:
        r0 = new soot.dava.toolkits.base.AST.traversals.InitializationDeclarationShortcut(r0);
        r5.methodNode.apply(r0);
        r0 = r0.isShortcutPossible();
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x0213, code lost:
        if (r0 == false) goto L55;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x0219, code lost:
        if (soot.dava.toolkits.base.AST.transformations.ShortcutArrayInit.DEBUG == false) goto L54;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x021c, code lost:
        java.lang.System.out.println("Shortcut is possible");
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x0224, code lost:
        r0 = new soot.dava.internal.javaRep.DShortcutAssignStmt(r0, r0);
        r0.set_Stmt(r0);
        markLocal(r0.getLeftOp());
     */
    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void inASTStatementSequenceNode(soot.dava.internal.AST.ASTStatementSequenceNode r6) {
        /*
            Method dump skipped, instructions count: 694
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.dava.toolkits.base.AST.transformations.ShortcutArrayInit.inASTStatementSequenceNode(soot.dava.internal.AST.ASTStatementSequenceNode):void");
    }

    public boolean isInSequenceAssignment(Stmt s, Value leftOp, int index) {
        if (!(s instanceof DefinitionStmt)) {
            return false;
        }
        DefinitionStmt ds = (DefinitionStmt) s;
        Value leftValue = ds.getLeftOp();
        if (!(leftValue instanceof ArrayRef)) {
            return false;
        }
        if (DEBUG) {
            System.out.println("Stmt number " + index + " is an array ref assignment" + leftValue);
            System.out.println("Array is" + leftOp);
        }
        ArrayRef leftRef = (ArrayRef) leftValue;
        if (!leftOp.equals(leftRef.getBase())) {
            if (DEBUG) {
                System.out.println("Not assigning to same array");
                return false;
            }
            return false;
        } else if (!(leftRef.getIndex() instanceof IntConstant)) {
            if (DEBUG) {
                System.out.println("Cant determine index of assignment");
                return false;
            }
            return false;
        } else {
            IntConstant leftIndex = (IntConstant) leftRef.getIndex();
            if (leftIndex.value != index) {
                if (DEBUG) {
                    System.out.println("Out of order assignment");
                    return false;
                }
                return false;
            }
            return true;
        }
    }

    public void secondPattern(ASTStatementSequenceNode node) {
        boolean success = false;
        ArrayList<AugmentedStmt> toRemove = new ArrayList<>();
        Iterator<AugmentedStmt> it = node.getStatements().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            AugmentedStmt as = it.next();
            success = false;
            Stmt s = as.get_Stmt();
            if (s instanceof DefinitionStmt) {
                DefinitionStmt ds = (DefinitionStmt) s;
                ValueBox right = ds.getRightOpBox();
                Value rightValue = right.getValue();
                if (rightValue instanceof NewArrayExpr) {
                    if (DEBUG) {
                        System.out.println("Found a new ArrayExpr" + rightValue);
                        System.out.println("Type of array is:" + rightValue.getType());
                    }
                    Type arrayType = rightValue.getType();
                    Value size = ((NewArrayExpr) rightValue).getSize();
                    if (!(size instanceof IntConstant)) {
                        continue;
                    } else if (((IntConstant) size).value == 0) {
                        debug("Found value to be 0 doing nothing");
                    } else {
                        if (DEBUG) {
                            System.out.println("Size of array is: " + ((IntConstant) size).value);
                        }
                        Iterator<AugmentedStmt> tempIt = node.getStatements().iterator();
                        while (tempIt.hasNext()) {
                            AugmentedStmt tempAs = tempIt.next();
                            Stmt tempS = tempAs.get_Stmt();
                            if (tempS.equals(s)) {
                                break;
                            }
                        }
                        ValueBox[] array = new ValueBox[((IntConstant) size).value];
                        success = true;
                        int i = 0;
                        while (true) {
                            if (i >= ((IntConstant) size).value) {
                                break;
                            } else if (!tempIt.hasNext()) {
                                if (DEBUG) {
                                    System.out.println("returning");
                                    return;
                                }
                                return;
                            } else {
                                AugmentedStmt augOne = tempIt.next();
                                Stmt augSOne = augOne.get_Stmt();
                                if (!tempIt.hasNext()) {
                                    if (DEBUG) {
                                        System.out.println("returning");
                                        return;
                                    }
                                    return;
                                }
                                AugmentedStmt augTwo = tempIt.next();
                                Stmt augSTwo = augTwo.get_Stmt();
                                if (!isInSequenceAssignmentPatternTwo(augSOne, augSTwo, ds.getLeftOp(), i)) {
                                    if (DEBUG) {
                                        System.out.println("Out of order assignment aborting attempt");
                                    }
                                    success = false;
                                } else {
                                    if (DEBUG) {
                                        System.out.println("Assignment stmt in order adding to array");
                                    }
                                    array[i] = ((DShortcutAssignStmt) augSOne).getRightOpBox();
                                    toRemove.add(augOne);
                                    toRemove.add(augTwo);
                                    i++;
                                }
                            }
                        }
                        if (success) {
                            DArrayInitExpr tempExpr = new DArrayInitExpr(array, arrayType);
                            DArrayInitValueBox tempValueBox = new DArrayInitValueBox(tempExpr);
                            DAssignStmt newStmt = new DAssignStmt(ds.getLeftOpBox(), tempValueBox);
                            if (DEBUG) {
                                System.out.println("Created new DAssignStmt and replacing it");
                            }
                            InitializationDeclarationShortcut shortcutChecker = new InitializationDeclarationShortcut(as);
                            this.methodNode.apply(shortcutChecker);
                            boolean possible = shortcutChecker.isShortcutPossible();
                            if (possible) {
                                if (DEBUG) {
                                    System.out.println("Shortcut is possible");
                                }
                                DShortcutAssignStmt newShortcutStmt = new DShortcutAssignStmt(newStmt, arrayType);
                                as.set_Stmt(newShortcutStmt);
                                markLocal(ds.getLeftOp());
                            }
                        }
                    }
                } else {
                    continue;
                }
            }
        }
        if (success) {
            List<AugmentedStmt> newStmtList = new ArrayList<>();
            for (AugmentedStmt as2 : node.getStatements()) {
                if (toRemove.contains(as2)) {
                    toRemove.remove(as2);
                } else {
                    newStmtList.add(as2);
                }
            }
            node.setStatements(newStmtList);
            inASTStatementSequenceNode(node);
            G.v().ASTTransformations_modified = true;
        }
    }

    public boolean isInSequenceAssignmentPatternTwo(Stmt one, Stmt two, Value leftOp, int index) {
        if (!(two instanceof DefinitionStmt)) {
            return false;
        }
        DefinitionStmt ds = (DefinitionStmt) two;
        Value leftValue = ds.getLeftOp();
        if (!(leftValue instanceof ArrayRef)) {
            return false;
        }
        ArrayRef leftRef = (ArrayRef) leftValue;
        if (!leftOp.equals(leftRef.getBase())) {
            if (DEBUG) {
                System.out.println("Not assigning to same array");
                return false;
            }
            return false;
        } else if (!(leftRef.getIndex() instanceof IntConstant)) {
            if (DEBUG) {
                System.out.println("Cant determine index of assignment");
                return false;
            }
            return false;
        } else {
            IntConstant leftIndex = (IntConstant) leftRef.getIndex();
            if (leftIndex.value != index) {
                if (DEBUG) {
                    System.out.println("Out of order assignment");
                    return false;
                }
                return false;
            }
            Value rightOp = ds.getRightOp();
            if (!(one instanceof DShortcutAssignStmt)) {
                return false;
            }
            DShortcutAssignStmt shortcut = (DShortcutAssignStmt) one;
            Value shortcutVar = shortcut.getLeftOp();
            if (!shortcutVar.equals(rightOp)) {
                return false;
            }
            return true;
        }
    }

    public void markLocal(Value shortcutLocal) {
        if (!(shortcutLocal instanceof Local)) {
            throw new DecompilationException("Found non local. report to developer.");
        }
        this.methodNode.addToDontPrintLocalsList((Local) shortcutLocal);
    }
}
