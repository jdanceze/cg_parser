package soot.dava.toolkits.base.AST.transformations;

import java.util.Iterator;
import java.util.List;
import soot.G;
import soot.Singletons;
import soot.dava.internal.AST.ASTLabeledNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.AST.ASTTryNode;
import soot.dava.internal.SET.SETNodeLabel;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.javaRep.DAbruptStmt;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/transformations/UselessLabelFinder.class */
public class UselessLabelFinder {
    public static boolean DEBUG = false;

    public UselessLabelFinder(Singletons.Global g) {
    }

    public static UselessLabelFinder v() {
        return G.v().soot_dava_toolkits_base_AST_transformations_UselessLabelFinder();
    }

    public boolean findAndKill(ASTNode node) {
        List subBodyTemp;
        if (!(node instanceof ASTLabeledNode)) {
            if (DEBUG) {
                System.out.println("Returning from findAndKill for node of type " + node.getClass());
                return false;
            }
            return false;
        }
        if (DEBUG) {
            System.out.println("FindAndKill continuing for node fo type" + node.getClass());
        }
        String label = ((ASTLabeledNode) node).get_Label().toString();
        if (label == null) {
            return false;
        }
        if (DEBUG) {
            System.out.println("dealing with labeled node" + label);
        }
        List<Object> subBodies = node.get_SubBodies();
        Iterator<Object> it = subBodies.iterator();
        while (it.hasNext()) {
            if (node instanceof ASTTryNode) {
                ASTTryNode.container subBody = (ASTTryNode.container) it.next();
                subBodyTemp = (List) subBody.o;
            } else {
                subBodyTemp = (List) it.next();
            }
            if (checkForBreak(subBodyTemp, label)) {
                return false;
            }
        }
        ((ASTLabeledNode) node).set_Label(new SETNodeLabel());
        if (DEBUG) {
            System.out.println("USELESS LABEL DETECTED");
            return true;
        }
        return true;
    }

    private boolean checkForBreak(List ASTNodeBody, String outerLabel) {
        List subBodyTemp;
        Iterator it = ASTNodeBody.iterator();
        while (it.hasNext()) {
            ASTNode temp = (ASTNode) it.next();
            if (temp instanceof ASTStatementSequenceNode) {
                ASTStatementSequenceNode stmtSeq = (ASTStatementSequenceNode) temp;
                for (AugmentedStmt as : stmtSeq.getStatements()) {
                    Stmt s = as.get_Stmt();
                    String labelBroken = breaksLabel(s);
                    if (labelBroken != null && outerLabel != null && labelBroken.compareTo(outerLabel) == 0) {
                        return true;
                    }
                }
                continue;
            } else {
                List<Object> subBodies = temp.get_SubBodies();
                Iterator<Object> subIt = subBodies.iterator();
                while (subIt.hasNext()) {
                    if (temp instanceof ASTTryNode) {
                        ASTTryNode.container subBody = (ASTTryNode.container) subIt.next();
                        subBodyTemp = (List) subBody.o;
                    } else {
                        subBodyTemp = (List) subIt.next();
                    }
                    if (checkForBreak(subBodyTemp, outerLabel)) {
                        return true;
                    }
                }
                continue;
            }
        }
        return false;
    }

    private String breaksLabel(Stmt stmt) {
        if (!(stmt instanceof DAbruptStmt)) {
            return null;
        }
        DAbruptStmt abStmt = (DAbruptStmt) stmt;
        if (abStmt.is_Break() || abStmt.is_Continue()) {
            SETNodeLabel label = abStmt.getLabel();
            return label.toString();
        }
        return null;
    }
}
